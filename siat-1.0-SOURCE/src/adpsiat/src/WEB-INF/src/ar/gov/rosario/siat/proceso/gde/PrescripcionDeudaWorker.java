//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.proceso.gde;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.ServicioBanco;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.gde.buss.bean.EstadoDeuda;
import ar.gov.rosario.siat.gde.buss.bean.ProPreDeu;
import ar.gov.rosario.siat.gde.buss.bean.ProPreDeuDet;
import ar.gov.rosario.siat.gde.buss.dao.DeudaDAO;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.pad.buss.bean.ExentaAreaCache;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpRunDirEnum;
import coop.tecso.adpcore.AdpRunState;
import coop.tecso.adpcore.MultiThreadedAdpWorker;
import coop.tecso.adpcore.ProcessMessage;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.SplitedFileWriter;
import coop.tecso.demoda.iface.helper.SpooledWriter;

/**
 * Worker del Proceso de Prescripcion Masiva de Deuda
 */
public class PrescripcionDeudaWorker extends MultiThreadedAdpWorker {

	private static Logger log = Logger.getLogger(PrescripcionDeudaWorker.class);

	public static final String PRESCRIBE_FILENAME    = "prescribe.csv";
	public static final String NO_PRESCRIBE_FILENAME = "no_prescribe.csv";

	public void execute(AdpRun run) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		Long pasoActual = run.getPasoActual();
		log.debug("Ejecutando paso " + pasoActual);
		try {
			String infoMsg = "";
			if (pasoActual.equals(1L)) {
				ejecutarPaso1(run);
				infoMsg = "La deuda a prescribir ha sido seleccionada";
				run.changeState(AdpRunState.ESPERA_CONTINUAR, infoMsg);
			}
			
			if (pasoActual.equals(2L)) {
				ejecutarPaso2(run);
				infoMsg = "La deuda seleccionada ha sido prescripta";
				run.changeState(AdpRunState.FIN_OK, infoMsg);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		
		} catch (Exception e) {			
			// Logueamos los errores acumulados y la excepcion
			String infoError = "Ocurrieron errores durante la ejecucion del " + 
							   "paso " + pasoActual + ". Consulte los logs.";
			run.addError(infoError + ": " + e.getMessage());
			// Cambiamos el estado a: FINALIZADO CON ERROR e informamos al usuario
			run.changeState(AdpRunState.FIN_ERROR, infoError);
		}
	}
	
	/**
	 * Paso 1 del Proceso de Prescipcion de Deuda: 
	 * Selecciona deuda a prescribir y registrarla
	 * en una estructura auxiliar. 
	 * Ademas genera dos planillas de calculo en el 
	 * directorio de salida del proceso:
	 *   
	 *     prescribe.xls : lista de deudas a prescribir
	 * 	no_prescribe.xls : lista de deudas que no prescriben
	 *
	 * @param run
	 * @throws Exception
	 */
	private void ejecutarPaso1(AdpRun run) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		SplitedFileWriter sfwPrescribe = null;
		SplitedFileWriter sfwNoPrescribe = null;
		Session session = null;
		try {
			session = SiatHibernateUtil.currentSession();
			
			// Obtenemos los parametros de Adp
			Long idProPreDeu = Long.parseLong(run.getParameter(ProPreDeu.ADP_PARAM_ID));
			
			// Obtenemos los datos para la prescripcion
			ProPreDeu proPreDeu		    = ProPreDeu.getById(idProPreDeu);
			ViaDeuda via 		 		= proPreDeu.getViaDeuda();
			ServicioBanco servicioBanco = proPreDeu.getServicioBanco();
			Date fechaTope 		 		= proPreDeu.getFechaTope();
			
			// Creamos los archivos de salida
			String outputdir = run.getProcessDir(AdpRunDirEnum.SALIDA) 
					+ File.separator + "ProPreDeu_" + proPreDeu.getId();
			
			// Creamos el directorio de salida
			(new File(outputdir)).mkdir();
			
			String	 pathPresFile = outputdir + File.separator + PRESCRIBE_FILENAME;
			String pathNoPresFile = outputdir + File.separator + NO_PRESCRIBE_FILENAME;
			
			SpooledWriter spool = new SpooledWriter();
			SplitedFileWriter prescribeFile = new SplitedFileWriter(pathPresFile, 65536L);
			SplitedFileWriter noPrescribeFile = new SplitedFileWriter(pathNoPresFile, 65536L);

			spool.addWriter(pathPresFile, prescribeFile);
			spool.addWriter(pathNoPresFile, noPrescribeFile);
		
			String csvHeader = "CUENTA, DEUDA, CLASIFICACION, FECHA VENCIMIENTO, IMPORTE, SALDO, OBSERVACION,\n";
			spool.write(pathPresFile,  csvHeader);
			spool.write(pathNoPresFile,csvHeader);
			
			run.changeStatusMsg("Buscando cuentas excluidas");
			ExentaAreaCache exentaAreaCache = new ExentaAreaCache();
			exentaAreaCache.initializeCache();
			
			// Seteamos el contexto para los threads 
			// procesadores
			PreDeuProContext context = new PreDeuProContext();
			context.setAdpRun(run);
			context.setIdProPreDeu(idProPreDeu);
			context.setSpool(spool);
			context.setPathPresFile(pathPresFile);
			context.setPathNoPresFile(pathNoPresFile);
			context.setExentaAreaCache(exentaAreaCache);
			
			run.changeStatusMsg("Inicializando el paso");
			initializeProcessors(PrescripcionDeudaProcessor.class, 8, context);
	
			run.changeStatusMsg("Obteniendo la deuda");
			procesarDeudaAPrescribir(run, via, servicioBanco, fechaTope);
			
			finishProcessors();
			
			// Cerramos los archivos de salida
			spool.close();
			
			// Obtenemos la corrida y limpiamos los archivos de salida
			Corrida corrida = proPreDeu.getCorrida();
			corrida.deleteListFileCorridaByPaso(1);

			// Agregamos las planillas a la lista de archivos de 
			// la corrida
			List<String> listOutputFilename  = new ArrayList<String>();
			for (File file: prescribeFile.getListSplitedFiles()) {
				String presDesc	= "Hoja de calculo con las deudas que prescriben";
				listOutputFilename.add(file.getAbsolutePath());
				corrida.addOutputFileByPaso(1,file.getName(), presDesc, file.getAbsolutePath());
			}

			for (File file: noPrescribeFile.getListSplitedFiles()) {
				String noPresDesc = "Hoja de calculo con las deudas que no prescriben";
				listOutputFilename.add(file.getAbsolutePath());
				corrida.addOutputFileByPaso(1,file.getName(), noPresDesc, file.getAbsolutePath());
			}
			
			// Copiamos los archivos generados al directorio "ultimo"
			String dirSalida = AdpRun.currentRun().getProcessDir(AdpRunDirEnum.SALIDA);
			File dirUltimo = new File(dirSalida, "ultimo");
			dirUltimo.mkdir();
			AdpRun.deleteDirFiles(dirUltimo);
			for(String filename: listOutputFilename) {
				File src = new File(filename);
				AdpRun.copyFile(src, dirUltimo);
			}

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		} catch (Exception e) {
			log.error("Worker Error: ",  e);
			abortProcessors();
			// Cerramos los archivos de salida
			if (sfwPrescribe != null)   sfwPrescribe.close();
			if (sfwNoPrescribe != null) sfwNoPrescribe.close();
			throw new DemodaServiceException(e);
		} finally {
			session.close();
		}
	}

	/** 
	 * Obtiene todos los id de deuda con fechaVencimiento < fechaTope 
	 * filtradas por Via y Servicio Banco. 
	 * Para cada uno, se crea un mensaje y se envia a la cola para ser 
	 * consumido por los threads procesadores
   	 */
	private void procesarDeudaAPrescribir(AdpRun run, ViaDeuda via, ServicioBanco servicioBanco, 
			Date fechaTope) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
		
			DeudaDAO deudaDAO = null;
			Long idEstadoDeuda = null;
			// Obtenemos todas las deuda con fechaVencimiento <
			// fechaTope filtradas por Servicio Banco
			if (via.getId().equals(ViaDeuda.ID_VIA_ADMIN)) {
				deudaDAO = GdeDAOFactory.getDeudaAdminDAO();
				idEstadoDeuda =  EstadoDeuda.ID_ADMINISTRATIVA;
			}
			if (via.getId().equals(ViaDeuda.ID_VIA_JUDICIAL)) {
				deudaDAO = GdeDAOFactory.getDeudaJudicialDAO();
				idEstadoDeuda = EstadoDeuda.ID_JUDICIAL;
			}
		
			List<Long> listIdDeuda = deudaDAO.getListDeudaAPrescribir(servicioBanco.getId(), fechaTope, idEstadoDeuda);

			// Seteamos la cantidad de registros a procesar
			run.setTotalRegs(new Long(listIdDeuda.size()));
			for(Long idDeuda: listIdDeuda) {
				// Creamos un mensaje y los enviamos a la cola
				ProcessMessage<Object> msgDeuda = new ProcessMessage<Object>();
				msgDeuda.setData(idDeuda);
				sendMessage(msgDeuda);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		} catch (Exception e) {
			log.error("Service Error: " + e.getMessage());
			throw new DemodaServiceException(e);
		}
	}

	/**
	 * Paso 2 del Proceso de Prescipcion de Deuda: 
	 * Se prescriben las deudas seleccionadas en el 
	 * paso anterior
	 * 
	 * @param run
	 * @throws Exception
	 */
	private void ejecutarPaso2(AdpRun run) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = null;
		try {
			session = SiatHibernateUtil.currentSession();
			
			// Obtenemos los parametros de Adp
			Long idProPreDeu = Long.parseLong(run.getParameter(ProPreDeu.ADP_PARAM_ID));
			
			PreDeuProContext context = new PreDeuProContext();
			context.setAdpRun(run);
			context.setIdProPreDeu(idProPreDeu);

			run.changeStatusMsg("Inicializando el paso");
			initializeProcessors(PrescripcionDeudaProcessor.class, 8, context);

			ProPreDeu proPreDeu = ProPreDeu.getById(idProPreDeu);
			prescribirDeuda(run, proPreDeu);
			
			finishProcessors();
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		} catch (Exception e) {
			log.error("Worker Error: ",  e);
			abortProcessors();
			throw new DemodaServiceException(e);
		} finally {
			session.close();
		}
	}

	/** 
	 * Obtiene todos los id de deuda que se prescribiran  
 	 * Para cada uno, se crea un mensaje y se envia a la cola para ser 
	 * consumido por los threads procesadores
   	 */
	private void prescribirDeuda(AdpRun run, ProPreDeu proPreDeu) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {

			List<ProPreDeuDet> list = proPreDeu.getListProPreDeuDet(); 
			
			// Seteamos la cantidad de registros a procesar
			run.setTotalRegs(new Long(list.size()));
			for(ProPreDeuDet proPreDeuDet: list) {
				// Creamos un mensaje y los enviamos a la cola
				ProcessMessage<Object> msgDeuda = new ProcessMessage<Object>();
				msgDeuda.setData(proPreDeuDet.getId());
				sendMessage(msgDeuda);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		} catch (Exception e) {
			log.error("Service Error: " + e.getMessage());
			throw new DemodaServiceException(e);
		}
	}
	
}
