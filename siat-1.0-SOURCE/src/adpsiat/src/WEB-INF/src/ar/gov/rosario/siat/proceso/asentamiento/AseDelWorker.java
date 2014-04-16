//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.proceso.asentamiento;

import java.io.File;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.bal.buss.bean.AseDel;
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
 * Procesa el Asentamiento Delegado (Delegador de Asentamiento).
 * @author Tecso Coop. Ltda.
 */
public class AseDelWorker implements AdpWorker {

	private static Logger log = Logger.getLogger(AseDelWorker.class);
		
	public void execute(AdpRun adpRun) throws Exception {
		// verfica numero paso y estado en adprun,
		// llama a cada metodo segun el numero de paso
		Long pasoActual = adpRun.getPasoActual();
		if (pasoActual.equals(1L)){ // Paso 1 del AseDel: Determinar Transacciones a Procesar y enviar a Sistema Externo
			AdpParameter paramIdAseDel = adpRun.getAdpParameter("idAseDel");
			long pasoTime = System.currentTimeMillis();
			this.ejecutarPaso1(adpRun, NumberUtil.getLong(paramIdAseDel.getValue()));
			pasoTime = System.currentTimeMillis() - pasoTime;
			adpRun.logDebug("<----------- Paso 1 -----------> Tiempo total : "+(pasoTime/60000)+" m </----------- Paso 1 ----------->");
		}
		if (pasoActual.equals(2L)){ // Paso 2 del AseDel: Lee los archivos de sincronismo de Sistema Externo y carga datos en SIAT
			AdpParameter paramIdAseDel = adpRun.getAdpParameter("idAseDel");
			long pasoTime = System.currentTimeMillis();
			this.ejecutarPaso2(adpRun, NumberUtil.getLong(paramIdAseDel.getValue()));
			pasoTime = System.currentTimeMillis() - pasoTime;
			adpRun.logDebug("<----------- Paso 2 -----------> Tiempo total : "+(pasoTime/60000)+" m </----------- Paso 2 ----------->");
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
	 * Paso 1 del Proceso de AseDel. Determina las Transacciones a Procesar y envia a Sistema Externo. Genera formularios para control.
	 * 
	 * @param adpRun
	 * @param idAseDel
	 * @throws Exception
	 */
	public void ejecutarPaso1(AdpRun adpRun, Long idAseDel) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			AseDel aseDel = AseDel.getByIdNull(idAseDel);
			if (aseDel == null) {
            	adpRun.changeState(AdpRunState.FIN_ERROR, "No se encontro el Asentamiento Delegado pasado como parametro", false);
            	adpRun.logError("No se encontro el Asentamiento Delegado pasado como parametro");
            	return;
			}

			// 1.1) Validar condiciones necesarias para comenzar el proceso.
			aseDel.validarConfiguracion();
			if (aseDel.hasError()) {
				String descripcion = aseDel.getListError().get(0).key().substring(1);;
				adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false); 
				adpRun.logError(descripcion);
            	return;
			}
			
			// 1.2) Leer transacciones de entrada y generar archivo y/o grabar en db externa.
			AdpRun.changeRunMessage("Obtener Transacciones para Sincronismo...", 0);
			aseDel.obtenerTransacciones(adpRun.getProcessDir(AdpRunDirEnum.SALIDA)+File.separator);
		
			tx = session.getTransaction();
			if(tx == null)
				tx = session.beginTransaction();
		
			if (aseDel.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				long errores = 0;
				for(DemodaStringMsg error:aseDel.getListError()){
					adpRun.logError(error.key().substring(1));
					errores++;
				}
				String descripcion = errores+" Errores al procesar las transacciones. Para mas detalle ver Logs.";
				adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false); 
	        	return;
			}
			AdpRun.changeRunMessage("Generando Reportes...", 0);
			
			// 1.3)Generar Formularios para control. 
			aseDel.generarFormulariosPaso1(adpRun.getProcessDir(AdpRunDirEnum.SALIDA)+File.separator);
			
            if (aseDel.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
            	String descripcion = aseDel.getListError().get(0).key().substring(1);
            	adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false);
            	adpRun.logError(descripcion);
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				// Ejecutamos un update statistics high sobre la tabla bal_transaccion para acelerar los proximos pasos.
				BalDAOFactory.getTransaccionDAO().updateStatisticsHigh();
				// cambiar estado, incremetar paso, y actualiza el pasoCorrida (que fue creado cuando se activo(esqueduleo)) 
				adpRun.changeState(AdpRunState.ESPERA_CONTINUAR, "", true); 
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
	 * Paso 2 del Proceso de AseDel. Lee los archivos de sincronismo de Sistema Externo y carga datos en SIAT. Genera formularios para control.
	 * 
	 * @param adpRun
	 * @param idAseDel
	 * @throws Exception
	 */
	public void ejecutarPaso2(AdpRun adpRun, Long idAseDel) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx  = null; 
		AseDel aseDel = null;
			
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();		
			
			aseDel = AseDel.getByIdNull(idAseDel);
			if (aseDel == null) {
            	adpRun.changeState(AdpRunState.FIN_ERROR, "No se encontro el Asentamiento Delegado pasado como parametro", false);
            	adpRun.logError("No se encontro el Asentamiento Delegado pasado como parametro");
            	return;
			}
			
			// 2.1)Validaciones Iniciales: verifica que existan los archivos de sincronismo y los mueve a 'procesando'
			this.validacionesIniciales(adpRun, aseDel);
			if (aseDel.hasError()) {
				String descripcion = aseDel.getListError().get(0).key().substring(1);;
				adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false); 
				adpRun.logError(descripcion);
				return;
			}
			
			// 2.2) Procesar archivos recibidos.
			AdpRun.changeRunMessage("Procesando Datos Recibidos...", 0);
			
			this.procesarArchivosSinc(adpRun, aseDel);
			
			if (aseDel.hasError()) {
				long errores = 0;
				for(DemodaStringMsg error: aseDel.getListError()){
					adpRun.logError(error.key().substring(1));
					errores++;
				}
				String descripcion = errores+" Errores al procesar los archivos de Sincronismo. Para mas detalle ver Logs.";
				adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false); 
				if(tx != null ){
					tx.rollback();
					if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				}
				return;
			}
			
			// 2.3) Generar Formularios para control. 
			AdpRun.changeRunMessage("Generando Reportes...", 0);
			aseDel.generarFormulariosPaso2(adpRun.getProcessDir(AdpRunDirEnum.SALIDA)+File.separator);
			
						
			if (aseDel.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				String descripcion = aseDel.getListError().get(0).key().substring(1);
				adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false); 
				adpRun.logError(descripcion);  
			} else {
				// 3.8) Eliminar Tablas Temporales del Asentamiento cuando se termina de procesar bien el asentamiento. 
				AdpRun.changeRunMessage("Eliminando tablas temporales antes de finalizar...", 0);
				aseDel.eliminarTablasTemporales();
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				// cambiar estado, incremetar paso, y actualiza el pasoCorrida (que fue creado cuando se activo(esqueduleo)) 
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

	/**
	 *  Validaciones Iniciales para el 2do paso. Verifica que esten los tres archivos de Sincronismo y otras.
	 * 
	 * <i>(paso 2.1)</i>
	 */
	public void validacionesIniciales(AdpRun adpRun, AseDel aseDel) throws Exception{
		String fileNameSinPartida   = "parti_"+ aseDel.getServicioBanco().getCodServicioBanco();
		String fileNameSinIndet 	= "indet_"+ aseDel.getServicioBanco().getCodServicioBanco();
		String fileNameSinSaldo 	= "saldo_"+ aseDel.getServicioBanco().getCodServicioBanco();
		
		String inputDir = adpRun.getProcessDir(AdpRunDirEnum.ENTRADA); 
	 
		
		// leerlos desde ENTRADA
		File fileSinPartida = new File(inputDir+ File.separator +fileNameSinPartida);
		if(!fileSinPartida.exists()){
			String descripcion = "No se encontro el archivo: " + fileNameSinPartida;
            aseDel.addRecoverableValueError(descripcion);
		}
		File fileSinIndet = new File(inputDir+ File.separator +fileNameSinIndet);
		if(!fileSinIndet.exists()){
			String descripcion = "No se encontro el archivo: " + fileNameSinIndet;
			aseDel.addRecoverableValueError(descripcion);
		}
		
		File fileSinSaldo = new File(inputDir+ File.separator +fileNameSinSaldo);
		if(!fileSinSaldo.exists()){
			String descripcion = "No se encontro el archivo: " + fileNameSinSaldo;
			aseDel.addRecoverableValueError(descripcion);
		}
		
		if(aseDel.hasError()){
			return;
		}
		
		// Genera copias de los archivos de sincronismo para consultar desde el proceso.
		String outputDir = adpRun.getProcessDir(AdpRunDirEnum.SALIDA); 
		String fileNameSinPartidaCopy = aseDel.getId()+fileNameSinPartida;
		File fileSinPartidaCopy = new File(outputDir+"/"+fileNameSinPartidaCopy);
		try { 
			AdpRun.copyFile(fileSinPartida, fileSinPartidaCopy); 
		} catch (Exception e){
			aseDel.addRecoverableValueError("Error al realizar copia del archivo de sincronismo.");
			return;
		}
		String nombre = "Archivo de Sincronismo de Partida";
		String descripcion = "Importes asignados a Partidas, resultado de la distribuci�n. Copia del archivo de sincronismo de entrada.";
		aseDel.getCorrida().addOutputFile(nombre, descripcion, outputDir+"/"+fileNameSinPartidaCopy);		
	
		String fileNameSinIndetCopy = aseDel.getId()+fileNameSinIndet;
		File fileSinIndetCopy = new File(outputDir+"/"+fileNameSinIndetCopy);
		try { 
			AdpRun.copyFile(fileSinIndet, fileSinIndetCopy); 
		} catch (Exception e){
			aseDel.addRecoverableValueError("Error al realizar copia del archivo de sincronismo.");
			return;
		}
		nombre = "Archivo de Sincronismo de Indeterminados";
		descripcion = "Transacciones Indeterminadas en el proceso de Asentamiento. Copia del archivo de sincronismo de entrada.";
		aseDel.getCorrida().addOutputFile(nombre, descripcion, outputDir+"/"+fileNameSinIndetCopy);	
		
		String fileNameSinSaldoCopy = aseDel.getId()+fileNameSinSaldo;
		File fileSinSaldoCopy = new File(outputDir+"/"+fileNameSinSaldoCopy);
		try { 
			AdpRun.copyFile(fileSinSaldo, fileSinSaldoCopy); 
		} catch (Exception e){
			aseDel.addRecoverableValueError("Error al realizar copia del archivo de sincronismo.");
			return;
		}
		nombre = "Archivo de Sincronismo de Saldos a Favor";
		descripcion = "Saldos a Favor generados en el proceso de Asentamiento. Copia del archivo de sincronismo de entrada.";
		aseDel.getCorrida().addOutputFile(nombre, descripcion, outputDir+"/"+fileNameSinSaldoCopy);	
		
		// Mueve los archivos de sincronismo desde ENTRADA a PROCESANDO
		adpRun.moveFile(fileNameSinPartida, AdpRunDirEnum.ENTRADA, AdpRunDirEnum.PROCESANDO);
		adpRun.moveFile(fileNameSinIndet, AdpRunDirEnum.ENTRADA, AdpRunDirEnum.PROCESANDO);
		adpRun.moveFile(fileNameSinSaldo, AdpRunDirEnum.ENTRADA, AdpRunDirEnum.PROCESANDO);		
	}
	
	/**
	 *  
	 * 
	 * <i>(paso 2.2)</i>
	 */
	public void procesarArchivosSinc(AdpRun adpRun, AseDel aseDel) throws Exception{
		String fileNameSinPartida   = "parti_"+ aseDel.getServicioBanco().getCodServicioBanco();
		String fileNameSinIndet 	= "indet_"+ aseDel.getServicioBanco().getCodServicioBanco();
		String fileNameSinSaldo 	= "saldo_"+ aseDel.getServicioBanco().getCodServicioBanco();
		
		String procesandoDir = adpRun.getProcessDir(AdpRunDirEnum.PROCESANDO);	 
		
		// leerlos desde ENTRADA
		File fileSinPartida = new File(procesandoDir+ File.separator +fileNameSinPartida);
		if(!fileSinPartida.exists()){
			String descripcion = "No se encontro el archivo: " + fileNameSinPartida;
            aseDel.addRecoverableValueError(descripcion);
		}
		File fileSinIndet = new File(procesandoDir+ File.separator +fileNameSinIndet);
		if(!fileSinIndet.exists()){
			String descripcion = "No se encontro el archivo: " + fileNameSinIndet;
			aseDel.addRecoverableValueError(descripcion);
		}
		
		File fileSinSaldo = new File(procesandoDir+ File.separator +fileNameSinSaldo);
		if(!fileSinSaldo.exists()){
			String descripcion = "No se encontro el archivo: " + fileNameSinSaldo;
			aseDel.addRecoverableValueError(descripcion);
		}
		
		if(aseDel.hasError()){
			return;
		}
		
		aseDel.procesarArchivosSinc(fileSinPartida,fileSinIndet,fileSinSaldo);
		
		if (!aseDel.hasError()){
	    	// mueve los archivos de PROCESANDO A PROCESADO OK
			adpRun.moveFile(fileNameSinPartida, AdpRunDirEnum.PROCESANDO, AdpRunDirEnum.PROCESADO_OK);
			adpRun.moveFile(fileNameSinIndet, AdpRunDirEnum.PROCESANDO, AdpRunDirEnum.PROCESADO_OK);
			adpRun.moveFile(fileNameSinSaldo, AdpRunDirEnum.PROCESANDO, AdpRunDirEnum.PROCESADO_OK);
	    }else{
	    	// mueve los archivos de PROCESANDO A PROCESADO CON ERROR
			adpRun.moveFile(fileNameSinPartida, AdpRunDirEnum.PROCESANDO, AdpRunDirEnum.PROCESADO_ERROR);
			adpRun.moveFile(fileNameSinIndet, AdpRunDirEnum.PROCESANDO, AdpRunDirEnum.PROCESADO_ERROR);
			adpRun.moveFile(fileNameSinSaldo, AdpRunDirEnum.PROCESANDO, AdpRunDirEnum.PROCESADO_ERROR);
	    }
	}

}
