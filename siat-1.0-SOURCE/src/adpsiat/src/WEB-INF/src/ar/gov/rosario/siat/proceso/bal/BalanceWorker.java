//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.proceso.bal;

import java.io.File;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.bal.buss.bean.Balance;
import ar.gov.rosario.siat.bal.buss.bean.BalanceSession;
import ar.gov.rosario.siat.bal.buss.cache.BalanceCache;
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

public class BalanceWorker implements AdpWorker {

	private static Logger log = Logger.getLogger(BalanceWorker.class);
		
	public void execute(AdpRun adpRun) throws Exception {
		// verfica numero paso y estado en adprun,
		// llama a cada metodo segun el numero de paso
		Long pasoActual = adpRun.getPasoActual();
		if (pasoActual.equals(1L)){ // Paso 1 del Balance: Preparacion del Balance
			AdpParameter paramIdBalance = adpRun.getAdpParameter("idBalance");
			long pasoTime = System.currentTimeMillis();
			this.ejecutarPaso1(adpRun, NumberUtil.getLong(paramIdBalance.getValue()));
			pasoTime = System.currentTimeMillis() - pasoTime;
			adpRun.logDebug("<----------- Paso 1 -----------> Tiempo total : "+(pasoTime/60000)+" m </----------- Paso 1 ----------->");
		}
		if (pasoActual.equals(2L)){ // Paso 2 del Balance: Formacion de Cajas
			AdpParameter paramIdBalance = adpRun.getAdpParameter("idBalance");
			long pasoTime = System.currentTimeMillis();
			this.ejecutarPaso2(adpRun, NumberUtil.getLong(paramIdBalance.getValue()));
			pasoTime = System.currentTimeMillis() - pasoTime;
			adpRun.logDebug("<----------- Paso 2 -----------> Tiempo total : "+(pasoTime/60000)+" m </----------- Paso 1 ----------->");
		}
		if (pasoActual.equals(3L)){ // Paso 3 del Balance: Desgloce por Servicio Banco
			AdpParameter paramIdBalance = adpRun.getAdpParameter("idBalance");
			long pasoTime = System.currentTimeMillis();
			this.ejecutarPaso3(adpRun, NumberUtil.getLong(paramIdBalance.getValue()));
			pasoTime = System.currentTimeMillis() - pasoTime;
			adpRun.logDebug("<----------- Paso 3 -----------> Tiempo total : "+(pasoTime/60000)+" m </----------- Paso 1 ----------->");
		}
		if (pasoActual.equals(4L)){ // Paso 4 del Balance: Administracion de Procesos de Asentamientos
			AdpParameter paramIdBalance = adpRun.getAdpParameter("idBalance");
			long pasoTime = System.currentTimeMillis();
			this.ejecutarPaso4(adpRun, NumberUtil.getLong(paramIdBalance.getValue()));
			pasoTime = System.currentTimeMillis() - pasoTime;
			adpRun.logDebug("<----------- Paso 4 -----------> Tiempo total : "+(pasoTime/60000)+" m </----------- Paso 1 ----------->");
		}
		if (pasoActual.equals(5L)){ // Paso 5 del Balance: Ajustes Finales
			AdpParameter paramIdBalance = adpRun.getAdpParameter("idBalance");
			long pasoTime = System.currentTimeMillis();
			this.ejecutarPaso5(adpRun, NumberUtil.getLong(paramIdBalance.getValue()));
			pasoTime = System.currentTimeMillis() - pasoTime;
			adpRun.logDebug("<----------- Paso 5 -----------> Tiempo total : "+(pasoTime/60000)+" m </----------- Paso 1 ----------->");
		}
		if (pasoActual.equals(6L)){ // Paso 6 del Balance: Afectar maestro de Rentas.
			AdpParameter paramIdBalance = adpRun.getAdpParameter("idBalance");
			long pasoTime = System.currentTimeMillis();
			this.ejecutarPaso6(adpRun, NumberUtil.getLong(paramIdBalance.getValue()));
			pasoTime = System.currentTimeMillis() - pasoTime;
			adpRun.logDebug("<----------- Paso 6 -----------> Tiempo total : "+(pasoTime/60000)+" m </----------- Paso 1 ----------->");
		}
		if (pasoActual.equals(7L)){ // Paso 7 del Balance: Actualizar Indeterminados y Guardar Saldos a Favor.
			AdpParameter paramIdBalance = adpRun.getAdpParameter("idBalance");
			long pasoTime = System.currentTimeMillis();
			this.ejecutarPaso7(adpRun, NumberUtil.getLong(paramIdBalance.getValue()));
			pasoTime = System.currentTimeMillis() - pasoTime;
			adpRun.logDebug("<----------- Paso 7 -----------> Tiempo total : "+(pasoTime/60000)+" m </----------- Paso 1 ----------->");
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
	 * Paso 1 del Proceso de Balance: Preparacion del Balance
	 * 
	 * @param idBalance
	 * @throws Exception
	 */
	public void ejecutarPaso1(AdpRun adpRun, Long idBalance) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			Balance balance = Balance.getByIdNull(idBalance);
			if (balance == null) {
            	adpRun.changeState(AdpRunState.FIN_ERROR, "No se encontro el Balance pasado como parametro", false);
            	adpRun.logError("No se encontro el Balance pasado como parametro");
            	return;
			}
			
			// Inicializa el Cache para el Balance.
			AdpRun.changeRunMessage("Inicializando Cache...", 0);
			if(!BalanceCache.getInstance().initialize() ) {
            	adpRun.changeState(AdpRunState.FIN_ERROR, "Fallo la inicializacion del Cache de Balance.", false);
            	adpRun.logError("Fallo la inicializacion del Cache de Balance.");
            	return;				
			}
	
			// Creamos un BalanceSession para la ejecucion del paso
			balance.setBalSession(new BalanceSession());
			
			//1.1)Validar condiciones necesarias para comenzar el proceso.
			balance.validarConfiguracion();
			if (balance.hasError()) {
				String descripcion = balance.getListError().get(0).key().substring(1);;
				adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false); 
				adpRun.logError(descripcion);
            	return;
			}
			
			// 1.2) Preparar Caja 7
			AdpRun.changeRunMessage("Preparando Caja 7...", 0);
			balance.prepararCaja7();
		
			tx = session.getTransaction();
			if(tx == null)
				tx = session.beginTransaction();
		
			if (balance.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				long errores = 0;
				for(DemodaStringMsg error:balance.getListError()){
					adpRun.logError(error.key().substring(1));
					errores++;
				}
				long warnings = balance.getBalSession().getWarnings();
				String descripcion = errores+" Errores y "+warnings+" Advertencias al preparar Caja 7. Para mas detalle ver Logs.";
				adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false); 
	        	return;
			}
			session.flush();

			// 1.3) Preparar Caja 69
			AdpRun.changeRunMessage("Preparando Caja 69...", 0);
			balance.prepararCaja69();
		
			tx = session.getTransaction();
			if(tx == null)
				tx = session.beginTransaction();
		
			if (balance.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				long errores = 0;
				for(DemodaStringMsg error:balance.getListError()){
					adpRun.logError(error.key().substring(1));
					errores++;
				}
				long warnings = balance.getBalSession().getWarnings();
				String descripcion = errores+" Errores y "+warnings+" Advertencias al preparar Caja 69. Para mas detalle ver Logs.";
				adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false); 
	        	return;
			}
			session.flush();

			// 1.4) Preparar Transacciones de Archivos de Banco
			AdpRun.changeRunMessage("Generando Transacciones obtenidas de Archivos...", 0);
			balance.prepararTranBal();
		
			tx = session.getTransaction();
			if(tx == null)
				tx = session.beginTransaction();
		
			if (balance.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				long errores = 0;
				for(DemodaStringMsg error:balance.getListError()){
					adpRun.logError(error.key().substring(1));
					errores++;
				}
				long warnings = balance.getBalSession().getWarnings();
				String descripcion = errores+" Errores y "+warnings+" Advertencias al leer Transacciones de Archivos. Para mas detalle ver Logs.";
				adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false); 
	        	return;
			}
			session.flush();

			AdpRun.changeRunMessage("Generando Reportes...", 0);
			// 1.5)Generar Formularios para control. 
			balance.generarFormulariosPaso1(adpRun.getProcessDir(AdpRunDirEnum.SALIDA)+File.separator);
			
            if (balance.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
            	String descripcion = balance.getListError().get(0).key().substring(1);
            	adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false);
            	adpRun.logError(descripcion);
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				
				// Se controla si se encontraron advertencias y se muestra en la descripcion del paso.
				long warnings = balance.getBalSession().getWarnings().longValue();
				String descripcion = "";
				if(warnings > 0)
					descripcion = warnings+" Advertencias al generar reportes. Para mas detalle ver listado.";

				adpRun.changeState(AdpRunState.ESPERA_CONTINUAR, descripcion, true);
			}
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
	 * Paso 2 del Proceso de Balance:  Formacion de Cajas
	 * 
	 * @param adpRun
	 * @param idBalance
	 * @throws Exception
	 */
	public void ejecutarPaso2(AdpRun adpRun, Long idBalance) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			Balance balance = Balance.getByIdNull(idBalance);
			if (balance == null) {
            	adpRun.changeState(AdpRunState.FIN_ERROR, "No se encontro el Balance pasado como parametro", false);
            	adpRun.logError("No se encontro el Balance pasado como parametro");
            	return;
			}
			
			// Inicializa el Cache para el Balance.
			AdpRun.changeRunMessage("Inicializando Cache...", 0);
			if(!BalanceCache.getInstance().initialize() ) {
            	adpRun.changeState(AdpRunState.FIN_ERROR, "Fallo la inicializacion del Cache de Balance.", false);
            	adpRun.logError("Fallo la inicializacion del Cache de Balance.");
            	return;				
			}
	
			// Creamos un BalanceSession para la ejecucion del paso
			balance.setBalSession(new BalanceSession());
			
			//2.1)Validaciones sobre Cajas
			balance.validarCajas();
			if (balance.hasError()) {
				String descripcion = balance.getListError().get(0).key().substring(1);;
				adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false); 
				adpRun.logError(descripcion);
            	return;
			}					
			
			AdpRun.changeRunMessage("Generando Reportes...", 0);
			// 2.2)Generar Formularios. 
			balance.generarFormulariosPaso2(adpRun.getProcessDir(AdpRunDirEnum.SALIDA)+File.separator);
			
            if (balance.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
            	String descripcion = balance.getListError().get(0).key().substring(1);
            	adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false);
            	adpRun.logError(descripcion);
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				
				// Se controla si se encontraron advertencias y se muestra en la descripcion del paso.
				long warnings = balance.getBalSession().getWarnings().longValue();
				String descripcion = "";
				if(warnings > 0)
					descripcion = warnings+" Advertencias al generar reportes. Para mas detalle ver listado.";

				adpRun.changeState(AdpRunState.ESPERA_CONTINUAR, descripcion, true);
			}
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
	 * Paso 3 del Proceso de Balance: Desgloce por Servicio Banco
	 * 
	 * @param adpRun
	 * @param idBalance
	 * @throws Exception
	 */
	public void ejecutarPaso3(AdpRun adpRun, Long idBalance) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			Balance balance = Balance.getByIdNull(idBalance);
			if (balance == null) {
            	adpRun.changeState(AdpRunState.FIN_ERROR, "No se encontro el Balance pasado como parametro", false);
            	adpRun.logError("No se encontro el Balance pasado como parametro");
            	return;
			}
			
			// Inicializa el Cache para el Balance.
			AdpRun.changeRunMessage("Inicializando Cache...", 0);
			if(!BalanceCache.getInstance().initialize() ) {
            	adpRun.changeState(AdpRunState.FIN_ERROR, "Fallo la inicializacion del Cache de Balance.", false);
            	adpRun.logError("Fallo la inicializacion del Cache de Balance.");
            	return;				
			}
			
			// Creamos un BalanceSession para la ejecucion del paso
			balance.setBalSession(new BalanceSession());
			
			// 3.1)  Alta de Procesos de Asentamientos de Pago por Servicio Banco, y Procesos de Delegacion de Asentamiento
			AdpRun.changeRunMessage("Alta de Proceso Asociados...", 0);
			balance.altaProcesosAsociados();
			if (balance.hasError()) {
				String descripcion = balance.getListError().get(0).key().substring(1);;
				adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false); 
				adpRun.logError(descripcion);
            	return;
			}

			// 3.2) Procesar TranBal
			AdpRun.changeRunMessage("Procesando Transacciones de Archivo...", 0);
			balance.procesarTranBal();
		
			tx = session.getTransaction();
			if(tx == null)
				tx = session.beginTransaction();
		
			if (balance.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				long errores = 0;
				for(DemodaStringMsg error:balance.getListError()){
					adpRun.logError(error.key().substring(1));
					errores++;
				}
				long warnings = balance.getBalSession().getWarnings();
				String descripcion = errores+" Errores y "+warnings+" Advertencias al procesar Transacciones de Archivos. Para mas detalle ver Logs.";
				adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false); 
	        	return;
			}
			session.flush();

			// 3.3) Procesar Caja 69
			AdpRun.changeRunMessage("Procesar Caja 69...", 0);
			balance.procesarCaja69();
		
			tx = session.getTransaction();
			if(tx == null)
				tx = session.beginTransaction();
		
			if (balance.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				long errores = 0;
				for(DemodaStringMsg error:balance.getListError()){
					adpRun.logError(error.key().substring(1));
					errores++;
				}
				long warnings = balance.getBalSession().getWarnings();
				String descripcion = errores+" Errores y "+warnings+" Advertencias al procesar Caja 69. Para mas detalle ver Logs.";
				adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false); 
	        	return;
			}
			session.flush();

			AdpRun.changeRunMessage("Generando Reportes...", 0);
			// 3.4)Generar Formularios para control. 
			balance.generarFormulariosPaso3(adpRun.getProcessDir(AdpRunDirEnum.SALIDA)+File.separator);
			
            if (balance.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
            	String descripcion = balance.getListError().get(0).key().substring(1);
            	adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false);
            	adpRun.logError(descripcion);
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				
				// Se controla si se encontraron advertencias y se muestra en la descripcion del paso.
				long warnings = balance.getBalSession().getWarnings().longValue();
				String descripcion = "";
				if(warnings > 0)
					descripcion = warnings+" Advertencias al generar reportes. Para mas detalle ver listado.";

				adpRun.changeState(AdpRunState.ESPERA_CONTINUAR, descripcion, true);
			}
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
	 * Paso 4 del Proceso de Balance: Administracion de Procesos de Asentamientos
	 * 
	 * @param adpRun
	 * @param idBalance
	 * @throws Exception
	 */
	public void ejecutarPaso4(AdpRun adpRun, Long idBalance) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx  = null; 
		Balance balance = null;
			
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
						
			balance = Balance.getByIdNull(idBalance);
			if (balance == null) {
            	adpRun.changeState(AdpRunState.FIN_ERROR, "No se encontro el Balance pasado como parametro", false);
            	adpRun.logError("No se encontro el Balance pasado como parametro");
            	return;
			}
			// Creamos un BalanceSession para la ejecucion del paso
			balance.setBalSession(new BalanceSession());
	
			// 4.1) Verificar si todos los Procesos de Asentamiento Siat y de Asentamientos Delegados se encuentran 
			// en estado 'Procesado con Exito'
			AdpRun.changeRunMessage("Verificando estado de procesos asociados...", 0);
			balance.verificarProcesosAsociados();
		
			if (balance.hasError()) {
				long errores = 0;
				for(DemodaStringMsg error:balance.getListError()){
					adpRun.logError(error.key().substring(1));
					errores++;
				}
				long warnings = balance.getBalSession().getWarnings();
				String descripcion = errores+" Errores y "+warnings+" Advertencias al verificar el estado de los procesos asociados. Para mas detalle ver Logs.";
				adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false); 
	        	return;
			}

			AdpRun.changeRunMessage("Generando Reportes...", 0);
			// 4.2)Generar Formularios para control. 
			balance.generarFormulariosPaso4(adpRun.getProcessDir(AdpRunDirEnum.SALIDA)+File.separator);
			
            if (balance.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
            	String descripcion = balance.getListError().get(0).key().substring(1);
            	adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false);
            	adpRun.logError(descripcion);
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			
				// Se controla si se encontraron advertencias y se muestra en la descripcion del paso.
				long warnings = balance.getBalSession().getWarnings().longValue();
				String descripcion = "";
				if(warnings > 0)
					descripcion = warnings+" Advertencias al generar reportes. Para mas detalle ver listado.";

				adpRun.changeState(AdpRunState.ESPERA_CONTINUAR, descripcion, true);
			}
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
	 * Paso 5 del Proceso de Balance: Ajustes Finales
	 * 
	 * @param adpRun
	 * @param idBalance
	 * @throws Exception
	 */
	public void ejecutarPaso5(AdpRun adpRun, Long idBalance) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx  = null; 
		Balance balance = null;
			
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
						
			balance = Balance.getByIdNull(idBalance);
			if (balance == null) {
            	adpRun.changeState(AdpRunState.FIN_ERROR, "No se encontro el Balance pasado como parametro", false);
            	adpRun.logError("No se encontro el Balance pasado como parametro");
            	return;
			}
			
			// Inicializa el Cache para el Balance.
			AdpRun.changeRunMessage("Inicializando Cache...", 0);
			if(!BalanceCache.getInstance().initialize() ) {
            	adpRun.changeState(AdpRunState.FIN_ERROR, "Fallo la inicializacion del Cache de Balance.", false);
            	adpRun.logError("Fallo la inicializacion del Cache de Balance.");
            	return;				
			}
			
			// Creamos un BalanceSession para la ejecucion del paso
			balance.setBalSession(new BalanceSession());
	
			AdpRun.changeRunMessage("Generando Reportes...", 0);
			// 5.1)Generar Formularios para control. 
			balance.generarFormulariosPaso5(adpRun.getProcessDir(AdpRunDirEnum.SALIDA)+File.separator);
			
            if (balance.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
            	String descripcion = balance.getListError().get(0).key().substring(1);
            	adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false);
            	adpRun.logError(descripcion);
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			
				// Se controla si se encontraron advertencias y se muestra en la descripcion del paso.
				long warnings = balance.getBalSession().getWarnings().longValue();
				String descripcion = "";
				if(warnings > 0)
					descripcion = warnings+" Advertencias al generar reportes. Para mas detalle ver listado.";

				adpRun.changeState(AdpRunState.ESPERA_CONTINUAR, descripcion, true);
			}
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
	 * Paso 6 del Proceso de Balance: Afectar maestro de Rentas.
	 * 
	 * @param adpRun
	 * @param idBalance
	 * @throws Exception
	 */
	public void ejecutarPaso6(AdpRun adpRun, Long idBalance) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx  = null; 
		Balance balance = null;
			
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
						
			balance = Balance.getByIdNull(idBalance);
			if (balance == null) {
            	adpRun.changeState(AdpRunState.FIN_ERROR, "No se encontro el Balance pasado como parametro", false);
            	adpRun.logError("No se encontro el Balance pasado como parametro");
            	return;
			}
			
			// Inicializa el Cache para el Balance.
			AdpRun.changeRunMessage("Inicializando Cache...", 0);
			if(!BalanceCache.getInstance().initialize() ) {
            	adpRun.changeState(AdpRunState.FIN_ERROR, "Fallo la inicializacion del Cache de Balance.", false);
            	adpRun.logError("Fallo la inicializacion del Cache de Balance.");
            	return;				
			}
			
			// Creamos un BalanceSession para la ejecucion del paso
			balance.setBalSession(new BalanceSession());
	
			// 6.1) Afectar Maestros de Rentas con DiarioPartida
			AdpRun.changeRunMessage("Actualizando Maestros de Rentas...", 0);
			balance.aplicarDiarioPartida();
		
			tx = session.getTransaction();
			if(tx == null)
				tx = session.beginTransaction();
		
			if (balance.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				long errores = 0;
				for(DemodaStringMsg error:balance.getListError()){
					adpRun.logError(error.key().substring(1));
					errores++;
				}
				long warnings = balance.getBalSession().getWarnings();
				String descripcion = errores+" Errores y "+warnings+" Advertencias al actualizar Maestros de Rentas. Para mas detalle ver Logs.";
				adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false); 
	        	return;
			}
			session.flush();

			// 6.2) Afectar Maestros de Rentas con Ajustes de Caja7
			AdpRun.changeRunMessage("Aplicando Ajustes a Maestros de Rentas...", 0);
			balance.aplicarCaja7();
		
			tx = session.getTransaction();
			if(tx == null)
				tx = session.beginTransaction();
		
			if (balance.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				long errores = 0;
				for(DemodaStringMsg error:balance.getListError()){
					adpRun.logError(error.key().substring(1));
					errores++;
				}
				long warnings = balance.getBalSession().getWarnings();
				String descripcion = errores+" Errores y "+warnings+" Advertencias al Aplicar Ajuste de Caja 7 a Maestros de Rentas. Para mas detalle ver Logs.";
				adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false); 
	        	return;
			}
			session.flush();
			
			AdpRun.changeRunMessage("Generando Reportes...", 0);
			// 6.3)Generar Formularios para control. 
			balance.generarFormulariosPaso6(adpRun.getProcessDir(AdpRunDirEnum.SALIDA)+File.separator);
			
            if (balance.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
            	String descripcion = balance.getListError().get(0).key().substring(1);
            	adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false);
            	adpRun.logError(descripcion);
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			
				// Se controla si se encontraron advertencias y se muestra en la descripcion del paso.
				long warnings = balance.getBalSession().getWarnings().longValue();
				String descripcion = "";
				if(warnings > 0)
					descripcion = warnings+" Advertencias al generar reportes. Para mas detalle ver listado.";

				adpRun.changeState(AdpRunState.ESPERA_CONTINUAR, descripcion, true);
			}
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
	 * Paso 7 del Proceso de Balance: Actualizar Indeterminados y Guardar Saldos A Favor
	 * 
	 * @param adpRun
	 * @param idBalance
	 * @throws Exception
	 */
	public void ejecutarPaso7(AdpRun adpRun, Long idBalance) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx  = null; 
		Balance balance = null;
			
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
						
			balance = Balance.getByIdNull(idBalance);
			if (balance == null) {
            	adpRun.changeState(AdpRunState.FIN_ERROR, "No se encontro el Balance pasado como parametro", false);
            	adpRun.logError("No se encontro el Balance pasado como parametro");
            	return;
			}
			
			// Inicializa el Cache para el Balance.
			AdpRun.changeRunMessage("Inicializando Cache...", 0);
			if(!BalanceCache.getInstance().initialize() ) {
            	adpRun.changeState(AdpRunState.FIN_ERROR, "Fallo la inicializacion del Cache de Balance.", false);
            	adpRun.logError("Fallo la inicializacion del Cache de Balance.");
            	return;				
			}
			
			// Creamos un BalanceSession para la ejecucion del paso
			balance.setBalSession(new BalanceSession());
	
			// 7.1) Grabar D�plices e Indeterminados
			AdpRun.changeRunMessage("Grabando D�plices e Indeterminados...", 0);
			balance.aplicarIndeterminados();
		
			tx = session.getTransaction();
			if(tx == null)
				tx = session.beginTransaction();
		
			if (balance.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				long errores = 0;
				for(DemodaStringMsg error:balance.getListError()){
					adpRun.logError(error.key().substring(1));
					errores++;
				}
				long warnings = balance.getBalSession().getWarnings();
				String descripcion = errores+" Errores y "+warnings+" Advertencias al grabar D�plices e Indeterminados. Para mas detalle ver Logs.";
				adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false); 
	        	return;
			}
			session.flush();

			// 7.2) Generar Saldos a Favor
			AdpRun.changeRunMessage("Generando Saldos a Favor...", 0);
			balance.generarSaldosAFavor();
		
			tx = session.getTransaction();
			if(tx == null)
				tx = session.beginTransaction();
		
			if (balance.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				long errores = 0;
				for(DemodaStringMsg error:balance.getListError()){
					adpRun.logError(error.key().substring(1));
					errores++;
				}
				long warnings = balance.getBalSession().getWarnings();
				String descripcion = errores+" Errores y "+warnings+" Advertencias al generar Saldos a Favor. Para mas detalle ver Logs.";
				adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false); 
	        	return;
			}
			session.flush();
			
			// 7.3) Consolidar registros procesados
			AdpRun.changeRunMessage("Finalizando proceso de balance...", 0);
			balance.tareasFinales();
		
			tx = session.getTransaction();
			if(tx == null)
				tx = session.beginTransaction();
		
			if (balance.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				long errores = 0;
				for(DemodaStringMsg error:balance.getListError()){
					adpRun.logError(error.key().substring(1));
					errores++;
				}
				long warnings = balance.getBalSession().getWarnings();
				String descripcion = errores+" Errores y "+warnings+" Advertencias al realizar tareas de finalizaci�n. Para mas detalle ver Logs.";
				adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false); 
	        	return;
			}
			session.flush();
			
            if (balance.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
            	String descripcion = balance.getListError().get(0).key().substring(1);
            	adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false);
            	adpRun.logError(descripcion);
			} else {
				// 7.3) Eliminar Tablas Temporales del Balance cuando se termina de procesar bien el proceso.
				AdpRun.changeRunMessage("Eliminando tablas temporales antes de finalizar...", 0);
				balance.eliminarTablasTemporales();
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			
				adpRun.changeState(AdpRunState.FIN_OK, "", false);
			}
            log.debug(funcName + ": exit");
					
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	
}
