//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.proceso.rec;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.bal.buss.bean.EnvioOsiris;
import ar.gov.rosario.siat.bal.buss.bean.EstadoEnvio;
import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import ar.gov.rosario.siat.pro.buss.bean.Proceso;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpRunState;
import coop.tecso.adpcore.AdpWorker;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;

/**
 *  Procesamiento de los Envíos Osiris conciliados. Genera Declaraciones Juradas y Archivos de Transacciones de Pago.
 * 
 * @author Tecso
 *
 */
public class ProcesarEnviosOsirisWorker implements AdpWorker {

	private static Logger log = Logger.getLogger(ProcesarEnviosOsirisWorker.class);
		
	public void execute(AdpRun adpRun) throws Exception {
		// verfica numero paso y estado en adprun,
		// llama a cada metodo segun el numero de paso
		Long pasoActual = adpRun.getPasoActual();
		if (pasoActual.equals(1L)){ // Paso 1: Generar Declaraciones Juradas y Formularios DJ AFIP 
			this.ejecutarPaso1(adpRun);
		}
		if (pasoActual.equals(2L)){ // Paso 2: Generar Archivos de Transacciones 
			this.ejecutarPaso2(adpRun);
		}

	}

	public void cancel(AdpRun adpRun) throws Exception {
		}

	public void reset(AdpRun adpRun) throws Exception {
		adpRun.changeState(AdpRunState.PREPARACION, "Reiniciado", false, null);
	}

	public boolean validate(AdpRun adpRun) throws Exception {
		return EnvioOsiris.existenEnvioOsirisConciliados();
	}

	/**
	 * Generar Declaraciones Juradas y Formularios DJ AFIP 
	 * 
	 * @throws Exception
	 */
	public void ejecutarPaso1(AdpRun adpRun) throws Exception{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			SiatHibernateUtil.currentSession();
			
			/*-issue #7805: AFIP - Acomodar deuda. Verificación ejecución de proceso de asentamiento en curso
			 *  Antes de empezar a procesar verfica si existe alguna pro_corrida del proceso 
			 * "Acomodar Deuda" (codproceso = "NovedadRS", idproceso = 57) con fechainicio >= fechaactual - 6 horas
			 * 		Si existe, corre el proceso normalmente.
			 *		Sino, deja la corrida con "Sin procesar" y mensajeestado = "No existe corrida previa de Acomodar Deuda".
			 */
			if(!hasPreviousRun(Proceso.PROCESO_NOVEDADRS)){
				adpRun.changeState(AdpRunState.SIN_PROCESAR, "No existe corrida previa de Acomodar Deuda", false);
	            log.debug(funcName + ": exit");
				return;
			}

			List<EnvioOsiris> listEnvioOsiris = EnvioOsiris.getListConciliado(); 
			
			//si esta definido este parametro solo procesamos los envios validos que estan 
			//en el rango del parametro PROCESARENVIO_ENVIOID con formato min:max
			String findeos = SiatParam.getString(SiatParam.PROCESARENVIO_ENVIOID, "");
			if (!findeos.equals("")) {
				List<EnvioOsiris> ltmp = new ArrayList<EnvioOsiris>();
				String[] ids = findeos.split(":");
				long idmin = Long.valueOf(ids[0]);
				long idmax = Long.valueOf(ids[1]);
				log.debug("Procesar EnvioOsiris PASO-1: " + findeos + " de lista.size():" + listEnvioOsiris.size());
				for(EnvioOsiris eo : listEnvioOsiris) {
					long id = eo.getIdEnvioAfip();
					if (idmin <= id && id <= idmax) {
						ltmp.add(eo);
					} else {
						log.debug("Procesar EnvioOsiris PASO-1: id " + id + " se descarta. por fuera de rango: " + findeos);
					}
				}
				listEnvioOsiris = ltmp;
				for(EnvioOsiris eo : listEnvioOsiris) {	log.debug("Procesar Envio: id:" + eo.getIdEnvioAfip()); }
			}
			
			//Por cada envio
			for(EnvioOsiris envioOsiris: listEnvioOsiris){
				SiatHibernateUtil.currentSession().refresh(envioOsiris);
				//Procesar envio (con reprocesar = false)
				envioOsiris.procesarEnvio(false);

				SiatHibernateUtil.closeSession();
			}
			
			adpRun.changeState(AdpRunState.ESPERA_CONTINUAR, "", true); 
			adpRun.execute(new Date());
            log.debug(funcName + ": exit");            
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			SiatHibernateUtil.currentSession().getTransaction().rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	/**
	 * Generar Transacciones
	 * 
	 * @throws Exception
	 */
	public void ejecutarPaso2(AdpRun adpRun) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			session = SiatHibernateUtil.currentSession();

			List<EnvioOsiris> listEnvioOsiris = EnvioOsiris.getListConciliado(); 
			
			//si esta definido este parametro solo procesamos los envios validos que estan 
			//en el rango del parametro PROCESARENVIO_ENVIOID con formato min:max
			String findeos = SiatParam.getString(SiatParam.PROCESARENVIO_ENVIOID, "");
			if (!findeos.equals("")) {
				List<EnvioOsiris> ltmp = new ArrayList<EnvioOsiris>();
				String[] ids = findeos.split(":");
				long idmin = Long.valueOf(ids[0]);
				long idmax = Long.valueOf(ids[1]);
				log.debug("Procesar EnvioOsiris PASO-2: " + findeos + " de lista.size():" + listEnvioOsiris.size());
				for(EnvioOsiris eo : listEnvioOsiris) {
					long id = eo.getIdEnvioAfip();
					if (idmin <= id && id <= idmax) {
						ltmp.add(eo);
					} else {
						log.debug("Procesar EnvioOsiris PASO-2: id " + id + " se descarta. por fuera de rango: " + findeos);
					}
					
				}
				listEnvioOsiris = ltmp;
				for(EnvioOsiris eo : listEnvioOsiris) {	log.debug("Procesar Envio: id:" + eo.getIdEnvioAfip()); }
			}
			
			for(EnvioOsiris envioOsiris: listEnvioOsiris){
				//Tran Pago Error al Generar				
				int tranErr = 0;
				
				//Contador de Transacciones
				envioOsiris.getCountMap().put(3, 0);
				
				tx = session.beginTransaction();
				try{
					envioOsiris.generarTransacciones();
				}catch (Exception e) {
					tranErr++;
					envioOsiris.addRecoverableValueError("Envio Osiris de id: "+envioOsiris.getId()+" IdEnvioAfip: "+envioOsiris.getIdEnvioAfip()+". Error inesperado al intentar generar de transacciones de pago.");
					adpRun.logError("Envio Osiris de id: "+envioOsiris.getId()+" IdEnvioAfip: "+envioOsiris.getIdEnvioAfip()+". Error inesperado al intentar generar de transacciones de pago.");
					adpRun.logError("Envio Osiris de id: "+envioOsiris.getId()+" IdEnvioAfip: "+envioOsiris.getIdEnvioAfip()+". ", e);
				}
				
				if (envioOsiris.hasError()) {
					tx.rollback();
					envioOsiris.setEstadoEnvio(EstadoEnvio.getById(EstadoEnvio.ID_ESTADO_PROCESADO_ERROR));
				} else {
					tx.commit();
					envioOsiris.setEstadoEnvio(EstadoEnvio.getById(EstadoEnvio.ID_ESTADO_PROCESADO_EXITO));
				}
				
				String observacion = envioOsiris.getObservacion();
				observacion +="\n"+DateUtil.formatDate(new Date(), DateUtil.yyyy_MM_dd_HH_MM_SS_MASK);
				observacion+="\n- Se generaron " + envioOsiris.getCountMap().get(3) + " Transacciones de Pago. ";
				if (tranErr > 0) {
					observacion+= tranErr + " quedaron con error.\n";
				}
				envioOsiris.setObservacion(observacion);
				
				tx = session.beginTransaction();
				BalDAOFactory.getEnvioOsirisDAO().update(envioOsiris);
				tx.commit();
			}	
			
			
			if (!listEnvioOsiris.isEmpty()) {
				//Obtengo el utlimo envio
				EnvioOsiris ultEnvioOsiris = listEnvioOsiris.get(listEnvioOsiris.size() - 1); 
				
				//Actualizo la fecha de ultimo envio procesado para los recursos 
				if (DateUtil.isDateBefore(Recurso.getDReI().getFecUltEnvPro(), ultEnvioOsiris.getFechaRegistroMulat())) {
					tx = session.beginTransaction();
					//Actualizo la fecha de ultimo envio procesado para los recursos 
					DefDAOFactory.getRecursoDAO().updateFecUltEnvProForDReIorETuR(ultEnvioOsiris.getFechaRegistroMulat());
					tx.commit();
				}
			}
			
            adpRun.changeState(AdpRunState.FIN_OK, "", false);
            log.debug(funcName + ": exit");            
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	/**
	 * Verifica si existe alguna Corrida del proceso "Acomodar Deuda" 
	 * (codproceso = "NovedadRS", idproceso = 57) con fechainicio mayor
	 * o igual a fecha actual - 6 horas.
	 * @return
	 */
	private Boolean hasPreviousRun(String codProceso){
    	Calendar cal = new GregorianCalendar();
    	//Resto 6 hrs a la fecha actual
    	cal.add(Calendar.HOUR_OF_DAY, -6);
		List<Corrida> listCorrida = Corrida.getListByCodProAndFecIni(codProceso, cal.getTime());
		return !ListUtil.isNullOrEmpty(listCorrida);
	}
}
