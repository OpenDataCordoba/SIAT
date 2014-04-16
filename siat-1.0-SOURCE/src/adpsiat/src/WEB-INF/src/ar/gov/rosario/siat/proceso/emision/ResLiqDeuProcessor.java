//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.proceso.emision;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.emi.buss.bean.EmiGeneralManager;
import ar.gov.rosario.siat.emi.buss.bean.EmiInfCue;
import ar.gov.rosario.siat.emi.buss.bean.ResLiqDeu;
import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.gde.buss.bean.EstadoDeuda;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import coop.tecso.adpcore.AdpProcessor;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.ProcessMessage;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

public class ResLiqDeuProcessor extends AdpProcessor {

	private static Logger log = Logger.getLogger(ResLiqDeuProcessor.class);

 	private static final String TAG_NAME = "ResLiqDeu"; 

 	// Longitud de los segmentos en que 
 	// se dividen las leyendas
 	private static final int CHUNK_SIZE = 145;
	
	// Tamaño de la pagina para flushear la sesion   
	private static final int FLUSH_PAGE_SIZE = 100;
	
	// Context del Thread
	private ResLiqDeuProContext context;

	public ResLiqDeuProcessor(ResLiqDeuProContext context,
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

			// Obtenemos los parametros de ADP
			ResLiqDeu resLiqDeu = context.getResLiqDeu();
			Recurso recurso = resLiqDeu.getRecurso();
			Date fechaAnalisis = resLiqDeu.getFechaAnalisis();
			Integer anio = resLiqDeu.getAnio();
			Integer periodoDesde = resLiqDeu.getPeriodoDesde();
			Integer periodoHasta = resLiqDeu.getPeriodoHasta();
			
			Long flushCounter = 0L;
	   		while (true) {
	   			// Obtenemos un mensaje de la cola
				ProcessMessage<Long> msg = getMessage();
				
				// Verificamos si se debe terminar con el thread
				if (msg.isPoisonMessage()) { 
					break; 
				}

				Long idCuenta = msg.getData();
				
				log.debug("Analizando cuenta id: " + idCuenta);

				analizarCuenta(run,idCuenta,recurso,fechaAnalisis, 
						anio, periodoDesde, periodoHasta);
				
				flushCounter++;
				// Registros procesados
				run.incRegCounter();
				// Verificamos si hay que flushear
				if (flushCounter % FLUSH_PAGE_SIZE == 0) {
					Long status = run.getRunStatus();
					run.changeStatusMsg("Analizando cuentas: " + status + "%");

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

	private void analizarCuenta(AdpRun run, Long idCuenta, Recurso recurso, Date fechaAnalisis,
			Integer anio, Integer periodoDesde, Integer periodoHasta) {
		try {
			Cuenta cuenta = Cuenta.getById(idCuenta);
			Long idRecurso  = recurso.getId();
			
			// Cache con los convenios de la cuenta
			HashMap<Long,String> convenios = new HashMap<Long,String>();  
 			
 			// Analizamos la deuda administrativa de la cuenta
			List<Deuda> listDeudaAdmin = GdeDAOFactory.getDeudaAdminDAO() 
				.getListDeudaBy(idCuenta,idRecurso,EstadoDeuda.ID_ADMINISTRATIVA,ViaDeuda.ID_VIA_ADMIN,fechaAnalisis);
			
			String adminMsg = procesarDeudaAdmin(listDeudaAdmin, convenios); 
			
			// Analizamos la deuda judicial de la cuenta
			List<Deuda> listDeudaJudicial = GdeDAOFactory.getDeudaJudicialDAO()
				.getListDeudaBy(idCuenta,idRecurso,EstadoDeuda.ID_JUDICIAL,ViaDeuda.ID_VIA_JUDICIAL,fechaAnalisis);
			
			String judicMsg = procesarDeudaJudicial(listDeudaJudicial, convenios); 

			// Analizamos los convenios
			String convMsg  = procesarConvenios(convenios);

			// Creamos los registros en la BD
			crearInfCue(cuenta, context.getResLiqDeu(),adminMsg, judicMsg, convMsg);
			
			run.logDebug("Procesando cuenta id " + idCuenta + ": OK");
		} catch (Exception e) {
			log.error(": Processor Error ",  e);
			run.logDebug("Procesando cuenta id " + idCuenta + ": ERROR");
			run.addWarning("No se pudo generar leyendas para la cuenta con id: " + idCuenta, e);
		}
	}

	private void crearInfCue(Cuenta cuenta,ResLiqDeu resLiqDeu, String adminMsg, String judicMsg, 
			String convMsg) throws Exception {
		
		Recurso recurso = resLiqDeu.getRecurso();
		Date fechaAnalisis = resLiqDeu.getFechaAnalisis();
		Corrida corrida = resLiqDeu.getCorrida();
		Integer anio = resLiqDeu.getAnio();
		Integer periodoDesde = resLiqDeu.getPeriodoDesde();
		Integer periodoHasta = resLiqDeu.getPeriodoHasta();

		String tagName = TAG_NAME;
		StringBuilder info = new StringBuilder();
		
		if (!StringUtil.isNullOrEmpty(judicMsg) || !StringUtil.isNullOrEmpty(adminMsg) || !StringUtil.isNullOrEmpty(convMsg)) {

			info.append("Al " + DateUtil.formatDate(fechaAnalisis, DateUtil.dd_MM_YYYY_MASK)); 
			info.append(" su cuenta mantiene las siguientes deudas: \n");

			// Si hay leyendas de deuda judicial
			if (!StringUtil.isNullOrEmpty(judicMsg)) {
				info.append(judicMsg + "\n");
			}
			
			// Si hay leyendas de deuda administrativa
			if (!StringUtil.isNullOrEmpty(adminMsg)) {
				info.append(adminMsg + "\n");
			}
			
			// Si hay leyendas de convenios
			if (!StringUtil.isNullOrEmpty(convMsg)) {
				info.append(convMsg + "\n");
			}

		} else {
			info.append("Al " + DateUtil.formatDate(fechaAnalisis, DateUtil.dd_MM_YYYY_MASK));
			info.append(" no registra per\u00EDodos/convenios impagos.\n");
		}

		EmiInfCue emiInfCue = new EmiInfCue();
		emiInfCue.setRecurso(recurso);
		emiInfCue.setCuenta(cuenta);
		emiInfCue.setCorrida(corrida);
		emiInfCue.setAnio(anio);
		emiInfCue.setPeriodoDesde(periodoDesde);
		emiInfCue.setPeriodoHasta(periodoHasta);
		String content = "";
		for (String line: info.toString().split("\n")) {
			for (String lineChunk: StringUtil.splitIntoChunks(line, CHUNK_SIZE)) {
				content += writeXMLNode("Linea", lineChunk); 
			}
		}
		emiInfCue.setTag(tagName);
		emiInfCue.setContenido(content);
		EmiGeneralManager.getInstance().createEmiInfCue(emiInfCue);
	}

	private String procesarDeudaAdmin(List<Deuda> listDeudaAdmin,  Map<Long,String> convenios) 
		throws Exception {
		
		AdpRun run = context.getAdpRun();
		HashMap<Long, String> cacheConvenios = context.getCacheConvenios();
		String info = "";
		
		if (!listDeudaAdmin.isEmpty()) {

			String listDeuda = "";
			
			for (Deuda deuda: listDeudaAdmin) {
				if (deuda.getEsExcentaPago()) {
					run.logDebug("Deuda: " + deuda.getId() + " -> excenta de pago");
					
				} else if (deuda.getEsConvenio()) {
					run.logDebug("Deuda: " + deuda.getId() + " -> en convenio");
					
					// Agregamos el convenio al mapa de convenios 
					// de la cuenta.
					Long idConvenio = deuda.getIdConvenio();
					if (!convenios.containsKey(idConvenio)) {
						String value = cacheConvenios.get(idConvenio);
						convenios.put(idConvenio, value);
					}
					
				} else if (deuda.getEsIndeterminada()) {
					run.logDebug("Deuda: " + deuda.getId() + " -> indeterminada");
					
				} else if (deuda.getEsReclamada()) {
					run.logDebug("Deuda: " + deuda.getId() + " -> reclamada");
					
				} else if (deuda.getEstaEnAsentamiento() ) {
					run.logDebug("Deuda: " + deuda.getId() + " -> en asentamiento");

				} else {
					Long periodo = deuda.getPeriodo();
					listDeuda += formatLong(Long.valueOf(periodo), 2);
					listDeuda += "/";
					Long anio    = deuda.getAnio();
					// Mostramos unicamente los dos ultimos numeros del
					// año, para aprovechar mas el espacio.
					if (anio != null) anio = anio % 100; 
					
					listDeuda += formatLong(Long.valueOf(anio), 2);
					listDeuda += " ";
				}		

				if (listDeuda != "") {
					info = "En v\u00EDa administrativa, per\u00EDodos: ";
					info += listDeuda;
				}
				
			}
		}
		
		return info;
	}
	
	private String procesarDeudaJudicial(List<Deuda> listDeudaJudicial, Map<Long,String> convenios) 
			throws NumberFormatException, Exception {

		Map<Long, String> cacheProcuradores = context.getCacheProcuradores();
		Map<Long, String> cacheConvenios 	= context.getCacheConvenios();
		Map<Long,String> procuradores 		= new HashMap<Long, String>();
		AdpRun run = context.getAdpRun();
		
		String info = "";
		if (!listDeudaJudicial.isEmpty()) {
		
			info = "En v\u00EDa judicial, consulte al procurador: ";
		
			for (Deuda deudaJudicial: listDeudaJudicial) {
				// Agregamos al mapa de procudadores
				if (!procuradores.containsKey(deudaJudicial.getIdProcurador())){
					String value = cacheProcuradores.get(deudaJudicial.getIdProcurador());
					info += value + " ";
					// Lo agregamos para registrar que se proceso el procurador
					procuradores.put(deudaJudicial.getIdProcurador(), value);
				}
				
				if (deudaJudicial.getEsConvenio()) {
					// Agregamos el convenio al mapa de convenios 
					// de la cuenta.
					Long key = deudaJudicial.getIdConvenio();
					if (!convenios.containsKey(key)) {
						String value = cacheConvenios.get(key);
						convenios.put(key, value);
					}
				
					run.logDebug("Deuda: " + deudaJudicial.getId() + " -> en convenio");
				}
			}
		}
	
		return info;
	}
	
	private String procesarConvenios(Map<Long,String> convenios) {
		String info = "";
		
		if (convenios.size() > 0){
			info = "Formalizada mediante convenios: ";
			
			Set<Long> listKeys = convenios.keySet();
			for (Long idConvenio:listKeys){
				info += convenios.get(idConvenio) + " ";
			}
		}

		return info;
	}
	
	private static String formatLong(Long l, int size) {
		String s = "";
		if (l != null) {
			s = StringUtil.formatLong(l);
		}
		
		s = StringUtil.fillWithCharToLeft(s, '0', size);
		
		return s;
	}
	
	private static String writeXMLNode(String tagName, String content) {
		return StringUtil.writeXMLNode(tagName, content) + "\n";
	}
	
}