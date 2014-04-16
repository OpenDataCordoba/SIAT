//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.proceso.emision;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.emi.buss.bean.ResLiqDeu;
import ar.gov.rosario.siat.gde.buss.bean.Convenio;
import ar.gov.rosario.siat.gde.buss.bean.Procurador;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpRunState;
import coop.tecso.adpcore.MultiThreadedAdpWorker;
import coop.tecso.adpcore.ProcessMessage;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Worker del Proceso de Resumen de Liquidacion de Deuda
 */
public class ResLiqDeuWorker extends MultiThreadedAdpWorker {

	private static Logger log = Logger.getLogger(ResLiqDeuWorker.class);

	// Utilizamos un espacio en blanco para
	// separar las strings generadas
	private static final String SEPARATOR = " ";
	
	public void execute(AdpRun run) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		Long pasoActual = run.getPasoActual();
		log.debug("Ejecutando paso " + pasoActual);
		try {
			
			String infoMsg = "";
			if (pasoActual.equals(1L)) {
				ejecutarPaso1(run);
				infoMsg += "Las leyendas se han generado ";
				infoMsg += "exitosamente";
				run.changeState(AdpRunState.FIN_OK, infoMsg);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		
		} catch (Exception e) {			
			String infoError = "Ocurrieron errores durante la ejecucion del " + 
							   "paso " + pasoActual;
			run.addError(infoError,  e);
			// Cambiamos el estado a: FINALIZADO CON ERROR e informamos al usuario
			run.changeState(AdpRunState.FIN_ERROR, infoError + ". Consulte los logs.");
		}
	}
	
	private void ejecutarPaso1(AdpRun run) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		Session session = null;
		try {
			session = SiatHibernateUtil.currentSession();
			
			run.changeStatusMsg("Inicializando el Proceso");

			run.logDebug("Inicializando cache de procuradores");
			HashMap<Long,String> cacheProcuradores = new HashMap<Long,String>();
			List<Procurador> listProcuradores = Procurador.getList();
			for (Procurador procurador: listProcuradores) {
				Long key = procurador.getId();
				String value = formatString(procurador.getDescripcion(), 30) + SEPARATOR + 
							   formatString(procurador.getDomicilio()  , 30) + SEPARATOR +
							   formatString(procurador.getTelefono()   , 30); 
				cacheProcuradores.put(key, value);
			}			

			run.logDebug("Inicializando cache de convenios");
			HashMap<Long,String> cacheConvenios = new HashMap<Long,String>();
			Long firstResult = 0L;
			Long maxResults  = 2500L; 
			boolean nextPage = true;
			while (nextPage) {
				List<Convenio> listConvenios = GdeDAOFactory.getConvenioDAO().getListVigentes(firstResult, maxResults); 
				nextPage = (listConvenios.size() > 0);
				for (Convenio convenio:listConvenios) {
					Long key = convenio.getId();
					String value = formatLong(convenio.getSistema().getNroSistema(), 2) + "-" + SEPARATOR +
								   formatInteger(convenio.getNroConvenio(), 10);  
					cacheConvenios.put(key, value);
				}			
				firstResult += maxResults;
				session.clear();
			}

			// Obtenemos los parametros de Adp
			Long idResLiqDeu = Long.parseLong(run.getParameter(ResLiqDeu.ADP_PARAM_ID));
			ResLiqDeu resLiqDeu = ResLiqDeu.getById(idResLiqDeu);

			Recurso recurso = resLiqDeu.getRecurso();
			
			// Seteamos el context a los procesadores
			ResLiqDeuProContext context = new ResLiqDeuProContext();
			context.setAdpRun(run);
			context.setResLiqDeu(resLiqDeu);
			context.setCacheProcuradores(cacheProcuradores);
			context.setCacheConvenios(cacheConvenios);
			
			run.logDebug("Inicializando Procesadores");
			initializeProcessors(ResLiqDeuProcessor.class, 10, context);

			run.logDebug("Procesando Cuentas");
			obtenerCuentas(run, recurso);
			
			run.logDebug("Finalizando Procesadores");
			finishProcessors();
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		} catch (Exception e) {
			log.error("Ocurrio una excepcion durante la ejecucion del paso",  e);
			abortProcessors();
			throw new DemodaServiceException(e);
		} finally {
			session.close();
		}
	}
	
	private void obtenerCuentas(AdpRun run, Recurso recurso) throws Exception {
		// Obtenemos la lista de cuentas a procesar
		List<Long> listIdCuenta = PadDAOFactory.getCuentaDAO().getListIdBy(recurso.getId());

		// Seteamos la cantidad de registros a procesar
		run.setTotalRegs(new Long(listIdCuenta.size()));
		run.changeStatusMsg("Se procesaran " + run.getTotalRegs() + " cuentas.");
		for (Long idCuenta: listIdCuenta) {
			// Armamos el mensaje
			ProcessMessage<Object> msg = new ProcessMessage<Object>();
			msg.setData(idCuenta);
			// Enviamos los mensajes a los threads procesadores
			sendMessage(msg);
		}
	}

	// Metodos para formatear la salida
	private static String formatLong(Long l, int size) {
		String s = "";
		if (l != null) { 
			s = StringUtil.formatLong(l);
		}
		
		s = StringUtil.fillWithCharToLeft(s, '0', size);
		
		return s;
	}
	
	private static String formatInteger(Integer n, int size) {
		String s = "";
		if (n != null) { 
			s = StringUtil.formatInteger(n);
		}

		s = StringUtil.fillWithCharToLeft(s, '0', size);
		
		return s;
	}
	
	private static String formatString(String s, int size) {
		return StringUtil.fillWithBlanksToRight(s, size);
	}

}