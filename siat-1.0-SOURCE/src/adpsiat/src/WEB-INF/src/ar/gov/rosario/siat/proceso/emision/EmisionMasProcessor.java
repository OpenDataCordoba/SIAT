//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.proceso.emision;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.Vencimiento;
import ar.gov.rosario.siat.emi.buss.bean.AuxDeuda;
import ar.gov.rosario.siat.emi.buss.bean.Emision;
import ar.gov.rosario.siat.emi.buss.dao.EmiDAOFactory;
import ar.gov.rosario.siat.exe.buss.bean.CueExe;
import ar.gov.rosario.siat.exe.buss.bean.CueExeCache;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import coop.tecso.adpcore.AdpProcessor;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.ProcessMessage;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class EmisionMasProcessor extends AdpProcessor {

	private static Logger log = Logger.getLogger(EmisionMasProcessor.class);

	// Tamaño de la pagina para flushear la sesion   
	private static final int FLUSH_PAGE_SIZE = 20;
	// Context
	private EmisionMasContext context;
	// Id del mensaje que se esta procesando
	private long currentMsgId;

	public EmisionMasProcessor(EmisionMasContext context,
			BlockingQueue<ProcessMessage<Long>> msgQueue) throws Exception {

		super(context, msgQueue);
		this.context = context;
	}

	public void execute(AdpRun run) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		Long pasoActual = run.getPasoActual();
		log.debug("Ejecutando paso " + pasoActual);
		
		try {
		
			if (pasoActual.equals(1L)) {
				ejecutarPaso1(run);
			}

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		
		} catch (Exception e) {
			String msg = "Error durante la ejecucion del paso " + pasoActual;
			log.error(msg, e);
		}
	}

	private void ejecutarPaso1(AdpRun run) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		Session session = null;
		Transaction tx = null;
		try {
			session = SiatHibernateUtil.currentSession();
			tx 		= session.beginTransaction();
			
			// Obtenemos los parametros de Adp
			Long idEmision =  Long.parseLong(run.getParameter(Emision.ADP_PARAM_ID));
			Emision emision =  Emision.getById(idEmision);
			Recurso recurso = emision.getRecurso();
			Integer anio = emision.getAnio();
			Integer periodoDesde = emision.getPeriodoDesde();
			Integer periodoHasta = emision.getPeriodoHasta();
	        Vencimiento vencimiento = recurso.getVencimiento();

			String codigo = recurso.getCodigoEmisionBy(new Date());
			emision.ininitializaEngine(codigo);

			Long flushCounter = 0L;
	   		while (true) {
				// Obtenemos un mensaje de la cola
				ProcessMessage<Long> msg = getMessage();
				// Verificamos si se debe terminar con el thread
				if (msg.isPoisonMessage()) { 
					break; 
				}
				Long idCuenta = msg.getData();
				log.debug("Analizando cuenta con id: " + idCuenta);
				
				this.currentMsgId = msg.getId();
	
				procesarCuenta(idCuenta, emision, anio, periodoDesde, periodoHasta, vencimiento);
				
				flushCounter++;
				run.incRegCounter();
				
				if (flushCounter % FLUSH_PAGE_SIZE == 0) {
					Long status = run.getRunStatus();
					run.changeStatusMsg("Generando deuda para analisis: " + status + "%");

					tx.commit();
					SiatHibernateUtil.closeSession();

					session = SiatHibernateUtil.currentSession();
					tx 		= session.beginTransaction();
					log.debug("Flushing Session: "+ flushCounter);
				}
			}
	   		// Actualizamos la BD
			tx.commit();
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			
		} catch (Exception e) {
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			session.close();
		}
	}
	
	private void procesarCuenta(Long idCuenta,Emision emision, Integer anio, Integer periodoDesde, 
			Integer periodoHasta, Vencimiento vencimiento) throws Exception {

		AdpRun run = context.getAdpRun();
		try {
			// Obtenemos la cuenta junto con el objeto imponible
			Cuenta cuenta = PadDAOFactory.getCuentaDAO().getByIdWithObjImp(idCuenta);
			
			// Obtenemos el cache de exenciones
			CueExeCache cueExeCache = context.getCueExeCache();
                        
            Integer periodoActual = periodoDesde;
            List<AuxDeuda> listAuxDeuda = new ArrayList<AuxDeuda>();
			while (periodoActual <= periodoHasta) {
				
				Date fechaAnalisis = DateUtil.getFirstDatOfMonth(periodoActual,anio);
				
				if (cuenta.getObjImp() != null) {
					Date fechaLimite = cuenta.getObjImp().getFechaBaja();
					if (fechaLimite != null) {
						if (fechaLimite.before(fechaAnalisis))
							break;
					}
				}
				
				List<CueExe> listCueExe = (ArrayList<CueExe>) cueExeCache.getListCueExe(idCuenta, fechaAnalisis);
				
				AuxDeuda auxDeuda = emision.eval(cuenta, listCueExe, anio, periodoActual);
			
				if (auxDeuda != null) {
					EmiDAOFactory.getAuxDeudaDAO().update(auxDeuda);
					listAuxDeuda.add(auxDeuda);
				} else {
					log.info("Emision de deuda cancelada.");
				}
	    		
	    		periodoActual++;
			}
			
			writeReportEntry(cuenta,listAuxDeuda);
			
			run.logDebug("Procesando cuenta id " + idCuenta + ": OK");
		} catch (Exception e) {
			context.getSpool().error(currentMsgId);
			log.error(": Processor Error ",  e);
			run.logDebug("Procesando deuda id " + idCuenta + ": ERROR");
			run.addWarning("No se pudo procesar la cuenta con id: " + idCuenta, e);
		}
	}

	private void writeReportEntry(Cuenta cuenta, List<AuxDeuda> listAuxDeuda) throws Exception {
		
		StringBuilder entry = new StringBuilder();
		for (AuxDeuda deuda: listAuxDeuda) {
			entry.append(cuenta.getNumeroCuenta() + ",");
			entry.append(deuda.getPeriodo() + "/" + deuda.getAnio() + "," );
			entry.append(DateUtil.formatDate(deuda.getFechaVencimiento(),DateUtil.ddSMMSYYYY_MASK) + ",");
			entry.append(deuda.getImporte() + "," );
			entry.append(deuda.getImporteBruto() + ",");
			entry.append(deuda.getStrExencion() + ","); // Codigo Exencion, Tipo Sujeto(Si tiene)
			entry.append(deuda.getConc1() + ",");
			entry.append(deuda.getConc2() + ",");
			entry.append(deuda.getConc3() + ",");
			entry.append(deuda.getConc4() + ",");
			Map<String,String> mapAtributos = deuda.getMapStrAtrVal();
			for (String cod: context.getListAtributos()) {
				entry.append(mapAtributos.get(cod) + ",");
			}
			
			entry.append("\n");
		}
		
		context.getSpool().write("csvReport", this.currentMsgId, entry.toString());
	}
}