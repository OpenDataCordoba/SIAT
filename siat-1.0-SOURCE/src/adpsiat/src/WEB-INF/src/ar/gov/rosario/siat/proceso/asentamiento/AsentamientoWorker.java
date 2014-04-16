//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.proceso.asentamiento;

import java.io.BufferedReader;
import java.io.File;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.bal.buss.bean.Asentamiento;
import ar.gov.rosario.siat.bal.buss.bean.IndeterminadoFacade;
import ar.gov.rosario.siat.bal.buss.cache.AsentamientoCache;
import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpRunDirEnum;
import coop.tecso.adpcore.AdpRunState;
import coop.tecso.adpcore.AdpWorker;
import coop.tecso.adpcore.engine.AdpParameter;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.model.DemodaStringMsg;

/**
 * Procesa el Asentamiento.
 * @author Tecso Coop. Ltda.
 */
public class AsentamientoWorker implements AdpWorker {

	private static Logger log = Logger.getLogger(AsentamientoWorker.class);
		
	public void execute(AdpRun adpRun) throws Exception {
		// verfica numero paso y estado en adprun,
		// llama a cada metodo segun el numero de paso
		Long pasoActual = adpRun.getPasoActual();
		if (pasoActual.equals(1L)){ // Paso 1 del Asentamiento: Determinar Transacciones a Procesar
			AdpParameter paramIdAsentamiento = adpRun.getAdpParameter("idAsentamiento");
			long pasoTime = System.currentTimeMillis();
			this.ejecutarPaso1(adpRun, NumberUtil.getLong(paramIdAsentamiento.getValue()));
			pasoTime = System.currentTimeMillis() - pasoTime;
			adpRun.logDebug("<----------- Paso 1 -----------> Tiempo total : "+(pasoTime/60000)+" m </----------- Paso 1 ----------->");
		}
		if (pasoActual.equals(2L)){ // Paso 2 del Asentamiento.
			AdpParameter paramIdAsentamiento = adpRun.getAdpParameter("idAsentamiento");
			long pasoTime = System.currentTimeMillis();
			this.ejecutarPaso2(adpRun, NumberUtil.getLong(paramIdAsentamiento.getValue()));
			pasoTime = System.currentTimeMillis() - pasoTime;
			adpRun.logDebug("<----------- Paso 2 -----------> Tiempo total : "+(pasoTime/60000)+" m </----------- Paso 2 ----------->");
		}
		if (pasoActual.equals(3L)){ // Paso 3 del Asentamiento.
			AdpParameter paramIdAsentamiento = adpRun.getAdpParameter("idAsentamiento");
			long pasoTime = System.currentTimeMillis();
			this.ejecutarPaso3(adpRun, NumberUtil.getLong(paramIdAsentamiento.getValue()));
			pasoTime = System.currentTimeMillis() - pasoTime;
			adpRun.logDebug("<----------- Paso 3 -----------> Tiempo total : "+(pasoTime/60000)+" m </----------- Paso 3 ----------->");
		}
	}

	public void cancel(AdpRun adpRun) throws Exception {
		}

	public void reset(AdpRun adpRun) throws Exception {
		adpRun.changeState(AdpRunState.PREPARACION, "Reiniciado", false, null);
	}

	public boolean validate(AdpRun adpRun) throws Exception {
		return false;
	}

	/**
	 * Paso 1 del Proceso de Asentamiento. Determina las Transacciones a Procesar y genera formularios para control.
	 * 
	 * @param adpRun
	 * @param idAsentamiento
	 * @throws Exception
	 */
	public void ejecutarPaso1(AdpRun adpRun, Long idAsentamiento) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx  = null; 
		Asentamiento asentamiento = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			asentamiento = Asentamiento.getByIdNull(idAsentamiento);
			if (asentamiento == null) {
            	adpRun.changeState(AdpRunState.FIN_ERROR, "No se encontro el Asentamiento pasado como parametro", false);
            	adpRun.logError("No se encontro el Asentamiento pasado como parametro");
            	return;
			}

			// Se setea el nombre del archivo de entrada al proceso tomando su valor del parametro Siat correspondiente
			adpRun.setInputFilename("tran_"+asentamiento.getServicioBanco().getCodServicioBanco()+".txt");

			// Inicializa el Cache para el asentamiento.
			AdpRun.changeRunMessage("Inicializando Cache...", 0);
			if(!AsentamientoCache.getInstance().initialize()) {
            	adpRun.changeState(AdpRunState.FIN_ERROR, "Fallo la inicializacion del Cache de Asentamiento.", false);
            	adpRun.logError("Fallo la inicializacion del Cache de Asentamiento.");
            	return;				
			}
			if(!AsentamientoCache.getInstance().createSession(asentamiento.getId())) {
            	adpRun.changeState(AdpRunState.FIN_ERROR, "Fallo la inicializacion de la Sesion de Asentamiento.", false);
            	adpRun.logError("Fallo la inicializacion de la Sesion de Asentamiento.");
            	return;				
			}
			
			// 1.1)Validar condiciones necesarias para comenzar el proceso.
			asentamiento.validarConfiguracion();
			if (asentamiento.hasError()) {
				String descripcion = asentamiento.getListError().get(0).key().substring(1);;
				adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false); 
				adpRun.logError(descripcion);
            	return;
			}

			// Si el asentamiento no se encuentra asociado a un Proceso de Balance, se lee el archivo de transacciones.
			if(asentamiento.getBalance() == null){
				//Leer el archivo de entrada asociado, mediante adp.
				BufferedReader input = adpRun.getInputFileReader(AdpRunDirEnum.ENTRADA);
				if (input == null) {
					adpRun.changeState(AdpRunState.FIN_ERROR, "No se encontro un archivo de transacciones asociado", false);
					adpRun.logError("No se encontro un archivo de transacciones asociado");
					return;
				}
				
				// 1.2)Obtenemos las Transacciones a procesar.
				AdpRun.changeRunMessage("Obtener Transacciones...", 0);
				asentamiento.obtenerTransacciones(input);
				
				tx = session.getTransaction();
				if(tx == null)
					tx = session.beginTransaction();
				
				if (asentamiento.hasError()) {
					tx.rollback();
					if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
					long errores = 0;
					for(DemodaStringMsg error:asentamiento.getListError()){
						adpRun.logError(error.key().substring(1));
						errores++;
					}
					long warnings = AsentamientoCache.getInstance().getSession(asentamiento.getId()).getWarnings();
					String descripcion = errores+" Errores y "+warnings+" Advertencias al procesar las transacciones. Para mas detalle ver Logs.";
					adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false); 
					return;
				}
			}
			
			AdpRun.changeRunMessage("Generando Reportes...", 0);
			// 1.3)Generar Formularios para control. 
			asentamiento.generarFormulariosPaso1(adpRun.getProcessDir(AdpRunDirEnum.SALIDA)+File.separator);
			
            if (asentamiento.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
            	String descripcion = asentamiento.getListError().get(0).key().substring(1);
            	adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false);
            	adpRun.logError(descripcion);
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				if(asentamiento.getBalance() == null){
					try{
						// Ejecutamos un update statistics high sobre la tabla bal_transaccion para acelerar los proximos pasos.
						BalDAOFactory.getTransaccionDAO().updateStatisticsHigh();											
					}catch(Exception ex){
						adpRun.logError("Fall� el update statistics sobre bal_transaccion. Sin embargo el paso finaliz� correctamente.");
					}
				}
				// cambiar estado, incremetar paso, y actualiza el pasoCorrida (que fue creado cuando se activo(esqueduleo)) 
				adpRun.changeState(AdpRunState.ESPERA_CONTINUAR, "", true); 
			}
            log.debug(funcName + ": exit");
            
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			if(asentamiento != null)
				AsentamientoCache.getInstance().closeSession(asentamiento.getId());
			SiatHibernateUtil.closeSession();
		}
	}

	/**
	 * Paso 2 del Proceso de Asentamiento. Distribuye las Partida y genera formularios para control.
	 * 
	 * @param adpRun
	 * @param idAsentamiento
	 * @throws Exception
	 */
	public void ejecutarPaso2(AdpRun adpRun, Long idAsentamiento) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx  = null; 
		Asentamiento asentamiento = null;
			
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			// Inicializa el Cache para el asentamiento.
			AdpRun.changeRunMessage("Inicializando Cache...", 0);
			if(!AsentamientoCache.getInstance().initialize()) {
            	adpRun.changeState(AdpRunState.FIN_ERROR, "Fallo la inicializacion del Cache de Asentamiento.", false);
            	adpRun.logError("Fallo la inicializacion del Cache de Asentamiento.");
            	return;				
			}
			
			asentamiento = Asentamiento.getByIdNull(idAsentamiento);
			if (asentamiento == null) {
            	adpRun.changeState(AdpRunState.FIN_ERROR, "No se encontro el Asentamiento pasado como parametro", false);
            	adpRun.logError("No se encontro el Asentamiento pasado como parametro");
            	return;
			}
			
			// Se invalida el Cache de Indeterminados, para usar un cache actualizado al Validar los criterios de Caducidad de Convenios.
			if(!IndeterminadoFacade.getInstance().invalidateCache()){
            	adpRun.changeState(AdpRunState.FIN_ERROR, "Fallo la actualizacion del cache de Indeterminado.", false);
            	adpRun.logError("Fallo la actualizacion del cache de Indeterminado.");
            	return;				
			}

			if(!AsentamientoCache.getInstance().createSession(asentamiento.getId())) {
            	adpRun.changeState(AdpRunState.FIN_ERROR, "Fallo la inicializacion de la Sesion de Asentamiento.", false);
            	adpRun.logError("Fallo la inicializacion de la Sesion de Asentamiento.");
            	return;				
			}

			// Leer parametro de Adp sobre el tipo de Log requerido. Y cargarlo en la session.
			if("true".equals(adpRun.getAdpParameter("LOG_DETALLADO").getValue())){
				AsentamientoCache.getInstance().getSession(asentamiento.getId()).setLogDetalladoEnabled(true);
			}else{
				AsentamientoCache.getInstance().getSession(asentamiento.getId()).setLogDetalladoEnabled(false);
			}
			
			// Leer parametro de Adp sobre tipo de corrida Forzado. Y cargarlo en la session.
			if(adpRun.getAdpParameter("FORZADO") != null 
					&& "true".equals(adpRun.getAdpParameter("FORZADO").getValue())){
				AsentamientoCache.getInstance().getSession(asentamiento.getId()).setForzado(true);
			}else{
				AsentamientoCache.getInstance().getSession(asentamiento.getId()).setForzado(false);
			}
			
			// Se deshabilita el modo con estadisticas.
			AsentamientoCache.getInstance().getSession(asentamiento.getId()).setLogStatsEnabled(false);
			
			// Se inicializa el cache de CueExe en la session
			AsentamientoCache.getInstance().getSession(asentamiento.getId()).getCueExeCache().initializeEspecial();
			
			// 2.1) Obtener y Procesar Transacciones SIAT.
			AdpRun.changeRunMessage("Procesando Transacciones...", 0);
			asentamiento.procesarTransaccionSiatPaginado();
			
			// Logear estadisticas
			if(AsentamientoCache.getInstance().getSession(asentamiento.getId()).isLogStatsEnabled())
				this.logearEstadisticas(asentamiento);
			
			if (asentamiento.hasError()) {
				long errores = 0;
				for(DemodaStringMsg error: asentamiento.getListError()){
					adpRun.logError(error.key().substring(1));
					errores++;
				}
				long warnings = AsentamientoCache.getInstance().getSession(asentamiento.getId()).getWarnings().longValue();
				String descripcion = errores+" Errores y "+warnings+" Advertencias al procesar las transacciones. Para mas detalle ver Logs.";
				adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false); 
	        	return;
			}
			// Abrimos la session e iniciliamos una transaccion
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			asentamiento = Asentamiento.getByIdNull(idAsentamiento);

			// 2.2) Consolidar Recaudado y Sellado. Y Validar Tolerancia de la Distribucion.
			asentamiento.consolidarValidar();
			if (asentamiento.hasError()) {
				tx.rollback();
				String descripcion = asentamiento.getListError().get(0).key().substring(1);
				adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false); 
				adpRun.logError(descripcion);
            	return;
			}

			
			// 2.3) Generar Formularios para control. 
			AdpRun.changeRunMessage("Generando Reportes...", 0);
			asentamiento.generarFormulariosPaso2(adpRun.getProcessDir(AdpRunDirEnum.SALIDA)+File.separator);
			
			if (asentamiento.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				String descripcion = asentamiento.getListError().get(0).key().substring(1);
				adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false); 
				adpRun.logError(descripcion); 
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				
				// Se controla si se encontraron advertencias y se muestra en la descripcion del paso.
				long warnings = AsentamientoCache.getInstance().getSession(asentamiento.getId()).getWarnings().longValue();
				String descripcion = "";
				if(warnings > 0)
					descripcion = warnings+" Advertencias al procesar las transacciones. Para mas detalle ver Logs.";

				// cambiar estado, incremetar paso, y actualiza el pasoCorrida (que fue creado cuando se activo(esqueduleo)) 
				adpRun.changeState(AdpRunState.ESPERA_CONTINUAR, descripcion, true);
			}
            log.debug(funcName + ": exit");
            
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			if(asentamiento != null)
				AsentamientoCache.getInstance().closeSession(asentamiento.getId());
			SiatHibernateUtil.closeSession();
		}
	}

	
	/**
	 * Paso 3 del Proceso de Asentamiento. Realiza el Asentamiento y genera formularios para control.
	 * 
	 * @param adpRun
	 * @param idAsentamiento
	 * @throws Exception
	 */
	public void ejecutarPaso3(AdpRun adpRun, Long idAsentamiento) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx  = null; 
		Asentamiento asentamiento = null;

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			asentamiento = Asentamiento.getByIdNull(idAsentamiento);
			if (asentamiento == null) {
            	adpRun.changeState(AdpRunState.FIN_ERROR, "No se encontro el Asentamiento pasado como parametro", false);
            	adpRun.logError("No se encontro el Asentamiento pasado como parametro");
            	return;
			}
			
			// Inicializa el Cache para el asentamiento.
			AdpRun.changeRunMessage("Inicializando Cache...", 0);
			if(!AsentamientoCache.getInstance().initialize() ) {
            	adpRun.changeState(AdpRunState.FIN_ERROR, "Fallo la inicializacion del Cache de Asentamiento.", false);
            	adpRun.logError("Fallo la inicializacion del Cache de Asentamiento.");
            	return;				
			}
			if(!AsentamientoCache.getInstance().createSession(asentamiento.getId())) {
            	adpRun.changeState(AdpRunState.FIN_ERROR, "Fallo la inicializacion de la Sesion de Asentamiento.", false);
            	adpRun.logError("Fallo la inicializacion de la Sesion de Asentamiento.");
            	return;				
			}
			
			// Se deshabilita el modo con estadisticas.
			AsentamientoCache.getInstance().getSession(asentamiento.getId()).setLogStatsEnabled(false);
		
			// 3.1) Asentar Transacciones.
			AdpRun.changeRunMessage("Asentando Transacciones...", 0);
			asentamiento.asentarTransaccionesPaginado();
			
			// Logear estadisticas
			if(AsentamientoCache.getInstance().getSession(asentamiento.getId()).isLogStatsEnabled())
				this.logearEstadisticas(asentamiento);
		
			if (asentamiento.hasError()) {
				String descripcion = asentamiento.getListError().get(0).key().substring(1);
				adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false); 
				adpRun.logError(descripcion);
            	return;
			}

			// Abrimos la session e iniciliamos una transaccion
			session = SiatHibernateUtil.currentSession();//MAX_TRANSACTION_TO_FLUSH);
			tx = session.beginTransaction();
					
			// Recargo el Asentamiento para volverlo a la session (se podria probrar con refresh)
			asentamiento = Asentamiento.getByIdNull(idAsentamiento);

			// 3.5) Enviar Solicitudes de Accion para el Convenio.
			asentamiento.enviarSolicitudAccionConvenio();
			if (asentamiento.hasError()) {
				tx.rollback();
				String descripcion = asentamiento.getListError().get(0).key().substring(1);
				adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false);
				adpRun.logError(descripcion);
            	return;
			}

			// 3.6) Generar Formularios para control. 
			AdpRun.changeRunMessage("Generando Reportes...", 0);
			asentamiento.generarFormulariosPaso3(adpRun.getProcessDir(AdpRunDirEnum.SALIDA)+File.separator);
			if (asentamiento.hasError()) {
				tx.rollback();
				String descripcion = asentamiento.getListError().get(0).key().substring(1);
				adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false);
				adpRun.logError(descripcion);
            	return;
			}

			// 3.7) Grabar Saldos, Indeterminados y Movimientos a Partida (en DiarioPartida) en SIAT. 
			if(asentamiento.getBalance() != null){
				AdpRun.changeRunMessage("Pasando datos al proceso de balance...", 0);
				asentamiento.consolidarAsentamiento();				
			}

			// 3.8) Generar Archivos de Sincronismo. 
			if(asentamiento.getBalance() == null){
				AdpRun.changeRunMessage("Generando archivos de sincronismo...", 0);
				asentamiento.generarArchivosSincronismo(adpRun.getProcessDir(AdpRunDirEnum.SALIDA)+File.separator);				
			}
			
            if (asentamiento.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
            	String descripcion = asentamiento.getListError().get(0).key().substring(1);
				adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false);
				adpRun.logError(descripcion);
			} else {
				// 3.8) Eliminar Tablas Temporales del Asentamiento cuando se termina de procesar bien el asentamiento. 
				AdpRun.changeRunMessage("Eliminando tablas temporales antes de finalizar...", 0);
				asentamiento.eliminarTablasTemporales();
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				// cambiar estado, incremetar paso, y actualiza el pasoCorrida (que fue creado cuando se activo(esqueduleo)) 
				adpRun.changeState(AdpRunState.FIN_OK, "", false);
				adpRun.moveInputFileSufix(AdpRunDirEnum.ENTRADA, AdpRunDirEnum.PROCESADO_OK,"."+asentamiento.getId().toString());
				
			}
            log.debug(funcName + ": exit");
            
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			if(asentamiento != null)
				AsentamientoCache.getInstance().closeSession(asentamiento.getId());
			SiatHibernateUtil.closeSession();
		}
	}

	/**
	 * Obtiene la lista de valores promedios del mapa de estadisticas de la session y los logea.
	 * @param idAsentamiento
	 */
	public void logearEstadisticas(Asentamiento asentamiento) throws Exception{
		try{
			asentamiento.logStats("Valores Promedios:");
			asentamiento.logStats("------------------");
			for(String ls: AsentamientoCache.getInstance().getSession(asentamiento.getId()).getStats()){
				asentamiento.logStats(ls);
			}			
		}catch(Exception e){
			asentamiento.logStats("Excepcion al intentar obtener estadisticas de la sesi�n.");
		}
	}
}
