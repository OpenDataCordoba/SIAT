//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.proceso.gde;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.gde.buss.bean.Anulacion;
import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.gde.buss.bean.EstProPreDeuDet;
import ar.gov.rosario.siat.gde.buss.bean.GdeGDeudaManager;
import ar.gov.rosario.siat.gde.buss.bean.MotAnuDeu;
import ar.gov.rosario.siat.gde.buss.bean.ProPreDeu;
import ar.gov.rosario.siat.gde.buss.bean.ProPreDeuDet;
import ar.gov.rosario.siat.gde.buss.dao.DeudaDAO;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.pad.buss.bean.ExentaAreaCache;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import coop.tecso.adpcore.AdpProcessor;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.ProcessMessage;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;

public class PrescripcionDeudaProcessor extends AdpProcessor {

	private static Logger log = Logger.getLogger(PrescripcionDeudaProcessor.class);
	// Tamaño de la pagina para flushear la sesion   
	private static final int FLUSH_PAGE_SIZE = 100;
	// Context del Thread
	private PreDeuProContext context;
	// Current Message Id
	private Long currentMsgId;

	// Constructor
	public PrescripcionDeudaProcessor(PreDeuProContext context, 
				BlockingQueue<ProcessMessage<Long>> msgQueue) throws Exception {
		super(context, msgQueue);
		this.context = context;
	} 
	
	public void execute(AdpRun run) {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		Long pasoActual = run.getPasoActual(); 
		log.debug("Ejecutando paso " + pasoActual);
		try {
			if (pasoActual.equals(1L)) { 
				ejecutarPaso1(run);
			}

			if (pasoActual.equals(2L)) { 
				ejecutarPaso2(run);
			}

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		} catch (Exception e) {
			String msg = "Error durante la ejecucion del thread.";
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
	
			Long idProPreDeu = context.getIdProPreDeu();
			ProPreDeu proPreDeu = ProPreDeu.getById(idProPreDeu);
			ViaDeuda via = proPreDeu.getViaDeuda();
			DeudaDAO deudaDAO = null;
			if (via.getId().equals(ViaDeuda.ID_VIA_ADMIN)) {
				deudaDAO  = GdeDAOFactory.getDeudaAdminDAO();
			}
			if (via.getId().equals(ViaDeuda.ID_VIA_JUDICIAL)) {
				deudaDAO  = GdeDAOFactory.getDeudaJudicialDAO();
			}
			
			Long flushCounter = 0L;
			while (true) {
				// Obtenemos un mensaje de la cola
				ProcessMessage<Long> msg = getMessage();
				// Verificamos si se debe terminar con el thread
				if (msg.isPoisonMessage()) { 
					break; 
				}
				this.currentMsgId = msg.getId();
				Long idDeuda = msg.getData();
				log.debug("Analizando deuda id: " + idDeuda);
				// Analizamos la deuda y generamos 
				// proPreDeuDet correspondiente
				analizarDeuda(run,deudaDAO, proPreDeu, idDeuda);
				flushCounter++;
				run.incRegCounter();
				
				if (flushCounter % FLUSH_PAGE_SIZE == 0) {
					Long status = run.getRunStatus();
					run.changeStatusMsg("Seleccionando deuda: " + status + "%");
					session.flush();
					session.clear();
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
			SiatHibernateUtil.closeSession();
		}
	}
	
	private void analizarDeuda(AdpRun run, DeudaDAO deudaDAO, ProPreDeu proPreDeu, Long idDeuda) {
		try {
			// Obtenemos la deuda con el convenio
			// seteado, para optimizar los accesos
			// a la base de datos
			Deuda deuda = deudaDAO.getByIdWithConvenio(idDeuda);

			// Por defecto, prescribimos
			int accion = ProPreDeuDet.PRESCRIBIR;
			String observacion = "";

			// Cache con cuentas excluidas del proceso
			ExentaAreaCache exentaAreaCache = context.getExentaAreaCache(); 

			// Si no esta excluida
			if (ListUtil.isNullOrEmpty(exentaAreaCache.esDeudaExcluida(deuda))) {
				Date fechaTope = proPreDeu.getFechaTope();
				// Analizamos si prescribe o no
				observacion = deuda.prescribe(fechaTope);
				
				if (!observacion.equals("Prescribe"))
					accion = ProPreDeuDet.NO_PRESCRIBIR;
			
				// Creamos el detalle
				ProPreDeuDet proPreDeuDet = new ProPreDeuDet();
				EstProPreDeuDet estProPreDeuDet = EstProPreDeuDet.getById(EstProPreDeuDet.ID_EN_PROCESO);
				proPreDeuDet.setProPreDeu(proPreDeu);
				proPreDeuDet.setIdDeuda(deuda.getId());
				proPreDeuDet.setCuenta(deuda.getCuenta());
				proPreDeuDet.setViaDeuda(deuda.getViaDeuda());
				proPreDeuDet.setEstadoDeuda(deuda.getEstadoDeuda());
				proPreDeuDet.setAccion(accion);
				proPreDeuDet.setEstProPreDeuDet(estProPreDeuDet);
				proPreDeuDet.setObservacion(observacion);
				
				proPreDeu.createProPreDetDet(proPreDeuDet);
			}
			// Si esta excluida
			else {
				accion = ProPreDeuDet.NO_PRESCRIBIR;
				observacion = "Excluida de la seleccion";
			}

			String planilla = (accion == ProPreDeuDet.PRESCRIBIR) ?  
					context.getPathPresFile() : context.getPathNoPresFile();
			
			writePlanilla(planilla, currentMsgId, deuda, observacion);
			 
			run.logDebug("Procesando deuda id " + idDeuda + ": OK");
		} catch (Exception e) {
			log.error(": Processor Error ",  e);
			run.logDebug("Procesando deuda id " + idDeuda + ": ERROR");
			run.addError("No se pudo procesar la deuda con id: " + idDeuda + ". Motivo " + e);
		}
	}

	private void writePlanilla(String planilla, Long idMsg, Deuda deuda, String observacion) {
		try {
			context.getSpool().write(planilla, idMsg, deuda.getCuenta().getNumeroCuenta() + "," +
				deuda.getPeriodo() + "/" + deuda.getAnio() + "," +
				deuda.getRecClaDeu().getDesClaDeu() + "," +
				DateUtil.formatDate(deuda.getFechaVencimiento(), DateUtil.ddSMMSYYYY_MASK) + "," +
				deuda.getImporte() + "," +
				deuda.getSaldo() + "," +
				observacion + "\n");
		} catch (IOException e){
			log.error(": IO Error ",  e);
		}
	}

	private void ejecutarPaso2(AdpRun run) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = null;
		Transaction tx = null;
		try {
			session = SiatHibernateUtil.currentSession();
			tx 		= session.beginTransaction();
	
			Long idProPreDeu = context.getIdProPreDeu();
			ProPreDeu proPreDeu = ProPreDeu.getById(idProPreDeu);
			ViaDeuda via = proPreDeu.getViaDeuda();
			DeudaDAO deudaDAO = null;
			if (via.getId().equals(ViaDeuda.ID_VIA_ADMIN)) {
				deudaDAO  = GdeDAOFactory.getDeudaAdminDAO();
			}
			if (via.getId().equals(ViaDeuda.ID_VIA_JUDICIAL)) {
				deudaDAO  = GdeDAOFactory.getDeudaJudicialDAO();
			}
			Long flushCounter = 0L;
			while (true) {
				// Obtenemos un mensaje de la cola
				ProcessMessage<Long> msg = getMessage();
				// Verificamos si se debe terminar con el thread
				if (msg.isPoisonMessage()) { 
					break; 
				}
				Long idProPreDeuDet = msg.getData();
				log.debug("Prescribiendo detalle id: " + idProPreDeuDet);
				
				prescribirDeuda(run, deudaDAO, proPreDeu, idProPreDeuDet);
				flushCounter++;
				run.incRegCounter();
				
				if (flushCounter % FLUSH_PAGE_SIZE == 0) {
					Long status = run.getRunStatus();
					run.changeStatusMsg("Prescribiendo  deuda: " + status + "%");
					//session.flush();
					//session.clear();
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
			SiatHibernateUtil.closeSession();
		}
	}
	
	private void prescribirDeuda(AdpRun run,DeudaDAO deudaDAO, ProPreDeu proPreDeu, Long idProPreDeuDet) {
		try {
			ProPreDeuDet proPreDeuDet  = ProPreDeuDet.getById(idProPreDeuDet);
			// Obtenemos la deuda con el convenio
			// seteado, para optimizar los accesos
			// a la base de datos
			Deuda deuda = deudaDAO.getByIdWithConvenio(proPreDeuDet.getIdDeuda());
		
			if (proPreDeuDet.prescribe()) {
				MotAnuDeu motAnuDeu = MotAnuDeu.getById(MotAnuDeu.ID_PRESCRIPCION);
				Anulacion anulacion = new Anulacion(); 
				anulacion.setFechaAnulacion(new Date());
				anulacion.setMotAnuDeu(motAnuDeu);
				anulacion.setIdDeuda(deuda.getId());
				anulacion.setIdCaso(null);
				anulacion.setObservacion("Prescripta por Proceso de Prescripcion Masiva de Deuda");
				anulacion.setRecurso(deuda.getRecurso());
				anulacion.setViaDeuda(proPreDeuDet.getViaDeuda());
				
				Corrida corrida = proPreDeu.getCorrida();
				GdeGDeudaManager.getInstance().anularDeuda(anulacion, deuda, corrida);
			} else {
				deuda.setObsMotNoPre(proPreDeuDet.getObservacion());
				deudaDAO.update(deuda);
			}
			
			run.logDebug("Prescribiendo detalle id " + idProPreDeuDet + ": OK");
		} catch (Exception e) {
			log.error(": Processor Error ",  e);
			run.logDebug("Prescribiendo detalle id " + idProPreDeuDet + ": ERROR");
			run.addError("No se pudo prescribir el detalle con id: " + idProPreDeuDet + ". Motivo " + e);
		}
	}
	
}