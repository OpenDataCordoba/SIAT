//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.proceso.emision;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.util.SiatUtil;
import ar.gov.rosario.siat.def.buss.bean.Atributo;
import ar.gov.rosario.siat.def.buss.bean.DefGravamenManager;
import ar.gov.rosario.siat.def.buss.bean.RecCon;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.TipObjImp;
import ar.gov.rosario.siat.emi.buss.bean.AuxDeuda;
import ar.gov.rosario.siat.emi.buss.bean.EmiGeneralManager;
import ar.gov.rosario.siat.emi.buss.bean.EmiInfCue;
import ar.gov.rosario.siat.emi.buss.bean.EmiMasTotReport;
import ar.gov.rosario.siat.emi.buss.bean.Emision;
import ar.gov.rosario.siat.emi.buss.dao.EmiDAOFactory;
import ar.gov.rosario.siat.exe.buss.bean.CueExeCache;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAdmin;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import ar.gov.rosario.siat.pro.buss.bean.ProManager;
import ar.gov.rosario.siat.pro.buss.bean.Proceso;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpRunDirEnum;
import coop.tecso.adpcore.AdpRunState;
import coop.tecso.adpcore.MultiThreadedAdpWorker;
import coop.tecso.adpcore.ProcessMessage;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.SplitedFileWriter;
import coop.tecso.demoda.iface.helper.SpooledWriter;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Worker del Proceso de Emision Masiva de Deuda
 * 
 * @author Tecso Coop. Ltda.
 */
public class EmisionMasWorker extends MultiThreadedAdpWorker {

	private static Logger log = Logger.getLogger(EmisionMasWorker.class);

	public static final String COD_FRM_PASO2 = "EMI_MAS_TOT";
	
	public void execute(AdpRun run) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		Long pasoActual = run.getPasoActual();
		log.debug("Ejecutando paso " + pasoActual);
		try {
			String infoMsg = "";

			// Paso 1: Generacion de deuda para control.
			if (pasoActual.equals(1L)) {
				ejecutarPaso1(run);
				infoMsg = "La deuda para control ha sido generada " +
						  "exitosamente";
				run.changeState(AdpRunState.ESPERA_CONTINUAR, infoMsg);
			}
			
			// Paso 2: Incorporacion de deuda.
			if (pasoActual.equals(2L)) {
				ejecutarPaso2(run);
				infoMsg = "La deuda administrativa ha sido generada " +
				  		  "exitosamente";
				run.changeState(AdpRunState.FIN_OK, infoMsg);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		
		} catch (Exception e) {			
			String infoError = "Ocurrieron errores durante la ejecucion del " + 
							   "paso " + pasoActual + ". Consulte los logs.";
			run.addError(infoError + ": ", e);

			// Cambiamos el estado a: FINALIZADO CON ERROR e informamos al usuario.
			run.changeState(AdpRunState.FIN_ERROR, infoError);
		}
	}

	/**
	 * Paso 1 del Proceso de Emision Masiva: 
	 * 
	 * Calcula el monto a pagar para cada cuenta en base a el algoritmo 
	 * definido para el recurso, los atributos del objeto imponible, de 
	 * cuenta, etc. y genera la deuda correspondiente en las tablas auxiliares.
	 * 
	 * Ademas genera una planilla Excel para control.
	 * 
	 * @param run
	 * @throws Exception
	 */
	private void ejecutarPaso1(AdpRun run) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		Session session = null;
		Transaction tx = null;
		try {
			session = SiatHibernateUtil.currentSession();

			// Obtenemos los parametros del proceso
			Long idEmision = Long.parseLong(run.getParameter(Emision.ADP_PARAM_ID));
			Emision emision = Emision.getById(idEmision);
			Corrida corrida = emision.getCorrida();
			Recurso recurso = emision.getRecurso();
			Integer anio = emision.getAnio();
			Integer periodoDesde = emision.getPeriodoDesde();
			Integer periodoHasta = emision.getPeriodoHasta();		
			TipObjImp tipObjImp  = recurso.getTipObjImp();
			
			//Verifico que el recurso tenga asociado un OI
			if (null != tipObjImp) {
				
				//Obtengo el proceso asociado al OI
				Proceso proceso = tipObjImp.getProceso();				
				if (null != proceso && proceso.getLocked() == 0) {
					tx = session.beginTransaction();
					//Cambio el estado del proceso a locked=1 (bloqueado)					
					proceso.setLocked(1);
					ProManager.getInstance().updateProceso(proceso);
					tx.commit();
				}				
			}		

			// Inicializamos el cache de Exenciones
			// con todas las exenciones "Ha lugar" para 
			// el recurso a emitir.
			run.changeStatusMsg("Inicializando caches");
            CueExeCache cueExeCache = new CueExeCache();
			cueExeCache.initialize(recurso);
			
			// Instanciamos un spooler para poder generar
			// el reporte ordenado, ya que el worker es 
			// multi-threaded.
			SpooledWriter spool = new SpooledWriter();

			// Creamos el directorio de salida
			String outputdir = run.getProcessDir(AdpRunDirEnum.SALIDA) + 
				File.separator + "Emision_" + emision.getId();
	 		(new File(outputdir)).mkdir();
	 		
			// Creamos el contexto para los threads
			EmisionMasContext context = new EmisionMasContext();
			context.setAdpRun(run);
			context.setCueExeCache(cueExeCache);
			context.setSpool(spool);

			// Inicializamos la clase que nos permitira generar
			// planillas Excel.
			// Se utiliza un SplitedFileWriter, que nos permitira
			// generar una o varias planillas de no mas de 35000
			// (para mayor compatibilidad con las versiones  de 
			// Excel) lineas cada una.
			SplitedFileWriter csvReport = createCSVReport(context,run, spool, emision, outputdir);
			
			run.changeStatusMsg("Inicializando Procesadores");
			initializeProcessors(EmisionMasProcessor.class, 8, context);

			run.changeStatusMsg("Obteniendo cuentas");
			obtenerCuentas(run,emision,anio,periodoDesde,periodoHasta);
			
			// Esperamos que finalicen los procesadores
			log.debug("Finalizando processo");
			finishProcessors();
			
			log.debug("Cerrando spool");
			spool.close();
			
			// Agregamos los archivos a la corrida
			for (File cssFile: csvReport.getListSplitedFiles()) {
				String desCssFile = "Hoja de calculo de deuda a generar";
				corrida.addOutputFileByPaso(1,cssFile.getName(), desCssFile, cssFile.getAbsolutePath());
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		} catch (Exception e) {
			log.error("Ocurrio una excepcion durante la ejecucion del paso",  e);
			abortProcessors();
			throw new DemodaServiceException(e);
		} finally {
			session.close();
		}
	}

	/**
	 * Inicializa el reporte csv con los registros de deuda a emitirse.
	 * 
	 * Formato del nombre del reporte: 
	 * 	deuda-codRecurso-fechaHoy(ddmmyyyy)[-atributo de segmenacion][_volumen].csv
	 * 
	 * Ejemplo:
	 * 	deuda-tgi-10102009-2_1.csv
	 */
	private SplitedFileWriter createCSVReport(EmisionMasContext context, AdpRun run, 
			SpooledWriter spool, Emision emision, String outputdir) {

		try {
			Recurso recurso = emision.getRecurso();
			// Nombre de la planilla:
			String reportName = "deuda-" + recurso.getCodRecurso() + "-" 
				+ DateUtil.formatDate(new Date(), DateUtil.ddMMYYYY_MASK);
		
			// Si tiene atributo de segmentacion, lo contemplamos en el nombre.
			if (emision.getAtributo() != null && emision.getValor() != null) {
				reportName += "-" + emision.getAtributo().getCodAtributo() + emision.getValor();
			}
			
			// Agregamos la extencion
			String fullName = outputdir + File.separator + reportName;
			fullName += ".csv";
			
			SplitedFileWriter csvReport = new SplitedFileWriter(fullName, 35000L);
		
			String header = "CUENTA, DEUDA, VENCIMIENTO, IMPORTE, IMPORTE BRUTO, COD. EXENCION, COD. TIPO SUJETO,";
			for (RecCon concepto: recurso.getListRecConPorOrdenVisualizacion()) { 
				if (concepto.getId().equals(5L)) continue;
				header += concepto.getAbrRecCon().toUpperCase() + ",";
			}
			
			String codigo = recurso.getCodigoEmisionBy(new Date());
			
			List<String> listResult = new ArrayList<String>();
			List<String> listAtributos = new ArrayList<String>();
		
			Pattern pattern = Pattern.compile("atributos.getValor\\(\"(.*?)\"\\)");
			Matcher matcher = pattern.matcher(codigo);
		
		    int pos = 0;
		    while (matcher.find(pos)) {
		      listResult.add(matcher.group(1));
		      pos = matcher.end();
		    }
		
			for (String atributo: listResult) {
				header += atributo.toUpperCase() + ",";
				listAtributos.add(atributo);
			}

			context.setListAtributos(listAtributos);
			
			csvReport.writeln(header);
			
			spool.addWriter("csvReport", csvReport);
			
			return csvReport;
			
		} catch (Exception e) {
			run.addError("No se pudo crear la planilla de deuda: ", e);
			return null;
		}				
	}
	
	private void obtenerCuentas(AdpRun run, Emision emision, Integer anio, 
			Integer periodoDesde, Integer periodoHasta) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		Recurso recurso = emision.getRecurso();
		Atributo atributo = emision.getAtributo();
		String valor = emision.getValor();
		
		Date fechaAnalisis = DateUtil.getFirstDatOfMonth(periodoDesde, anio);
		// Obtenemos los ids de las cuentas a procesar
		List<Long> listIdCuenta = EmiDAOFactory.getEmisionDAO()
			.getListCuentaActivasBy(recurso, atributo, valor, fechaAnalisis);
		
		// Seteamos la cantidad de registros a procesar
		run.setTotalRegs(new Long(listIdCuenta.size()));
		run.changeStatusMsg("Se procesaran " + run.getTotalRegs() + " registros.");
		for (Long idCuenta: listIdCuenta) {
			// Armamos el mensaje
			ProcessMessage<Object> msg = new ProcessMessage<Object>();
			msg.setData(idCuenta);
			sendMessage(msg);
		}
			
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
	}

	/**
	 * Paso 2 del Proceso de Emision Masiva de Deuda:
	 *  
	 * Incorpora la deuda generada en las tablas auxiliares
	 * como deuda administrativa.
	 * 
	 * @param run
	 * @throws Exception
	 */
	private void ejecutarPaso2(AdpRun run) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		Session session = null;
		Transaction tx =  null;
		try {
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			// Obtenemos la Emision
			Long idEmision  = Long.parseLong(run.getParameter(Emision.ADP_PARAM_ID));
			Emision emision = Emision.getById(idEmision);
			
			// Se realiza un update statistics sobre la tabla emi_auxdeuda. (Mantis 5258)
			EmiDAOFactory.getAuxDeudaDAO().updateStatisticsHigh();
			
			// Generamos la deuda administrativa
			this.generarDeudaAdmin(run, emision);

			if (emision.hasError()) {
 				// borrado de los registros DeuAdmRecCon	
 				int deuAdmRecConBorrados = GdeDAOFactory.getDeuAdmRecConDAO().deleteListDeuAdmRecConByEmision(emision);
 				log.debug("registros borrados de DeuAdmRecCon: " + deuAdmRecConBorrados);
 				// borrado DeudaAdmin creados
 				int regBorrados = GdeDAOFactory.getDeudaAdminDAO().deleteListDeudaAdminByEmision(emision);
 				log.debug("registros borrados de la DeudaAdmin: " + regBorrados);

 				String descripcion = "Error al generar los registros de Deuda Administrativa. Se limpiaron todos los registros de Deuda y Conceptos de esta Emision ";
				run.changeState(AdpRunState.FIN_ERROR, descripcion, false);
				run.logError(descripcion);
	 		} else {
	 			// borrado de los registros auxdeu de la emision
	 			int regBorrados = EmiDAOFactory.getAuxDeudaDAO().deleteAuxDeudaByIdEmision(emision);
	 			log.debug("registros borrados de la AuxDeuda: " + regBorrados);
				// cambiar estado, incremetar paso, y actualiza el pasoCorrida (que fue creado cuando se activo (esqueduleo)) 
				run.changeState(AdpRunState.FIN_OK, "Registros de Deuda Administrativas generados exitosamente", true);
				
	 		}			
			
	 		// Si o si realiza el commit.
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			if (!emision.hasError()) {
				
				emision = Emision.getById(idEmision);
				TipObjImp tipObjImp = emision.getRecurso().getTipObjImp();
				
				//Verifico que el recurso tenga asociado un OI
				if (null != tipObjImp) {
					
					//Obtengo el proceso asociado al OI
					Proceso proceso = tipObjImp.getProceso();				
					if (null != proceso && proceso.getLocked() == 1
							&& !EmiDAOFactory.getEmisionDAO().existeEmisionEnEjecucion(emision.getRecurso())) {
						//Cambio el estado del proceso a locked=0 (desbloqueado)					
						proceso.setLocked(0);
						ProManager.getInstance().updateProceso(proceso);
					}				
				}			
				
				// Actualizamos el ultimo periodo emitido.
				updateUltPeriodoEmitido(run, emision);
			}

			tx.commit();

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		} catch (Exception e) {
			log.error("Ocurrio una excepcion durante la ejecucion del paso",  e);
			tx = SiatHibernateUtil.currentSession().getTransaction();
		    try { if(tx != null) tx.rollback(); } catch (Exception ex) {}
			throw new DemodaServiceException(e);
		} finally {
			session.close();
		}
	}

	private void generarDeudaAdmin(AdpRun adpRun, Emision emision) throws Exception {
		//mapa de las cuentas a las que se le genero deuda
 		Map<String, Long> cuentas = new HashMap<String, Long>(); 

 		// Inicializamos el mapa de conceptos de la emision
 		emision.initializeMapCodRecCon();

 		EmiMasTotReport reporte = new EmiMasTotReport(emision);
 		
 		// cada 2500 registros hace commit y beginTransaction. (No flush y clear)
		int first = 0;
		int pageSize = 2500;
 		boolean contieneAuxDeuda = true;
 		while (contieneAuxDeuda) {
 			// obtencion de la lista de pares AuxDeuda-NumeroCuenta paginada
  			List<Object[]> listAuxDeuda = AuxDeuda
  				.getListAuxDeudaByIdEmision(emision.getId(), first, pageSize);
  			
  			contieneAuxDeuda = (listAuxDeuda.size() > 0);
 			
 			for (Object[] datos : listAuxDeuda) {
 				AuxDeuda auxDeuda = (AuxDeuda) datos[0];
 				
 				// Agregamos los datos relevantes
 				// para el reporte
 				reporte.addReportData(auxDeuda);
 				
 				// Creamos la informacion de los atributos
 				EmiInfCue infAtributos;
 				infAtributos = new EmiInfCue();
 				infAtributos.setRecurso(emision.getRecurso());
 				infAtributos.setCuenta(auxDeuda.getCuenta());
 				infAtributos.setCorrida(emision.getCorrida());
 				infAtributos.setAnio(emision.getAnio());
 				infAtributos.setPeriodoDesde(auxDeuda.getPeriodo().intValue());
 				infAtributos.setPeriodoHasta(auxDeuda.getPeriodo().intValue());
 				infAtributos.setTag("Atributos");
 				infAtributos.setContenido(auxDeuda.getStrAtrVal());
				
 				EmiGeneralManager.getInstance().createEmiInfCue(infAtributos);
				
 				String nroCuenta  = (String) datos[1];

 				AdpRun.changeRunMessage("Deudas administrativas generadas: " + first, 30);

 				DeudaAdmin deudaAdmin = emision.createDeudaAdminFromAuxDeuda(auxDeuda);
 				if (deudaAdmin.hasError()) {
	     			String descripcion = "Error al crear el registro de deudaAdmin a partir de la auxDeuda: " + auxDeuda.getId();
	     			log.error(descripcion);
	     			AdpRun.logRun(descripcion);
	     			emision.addRecoverableValueError(descripcion);
	     			auxDeuda.addErrorMessages(emision);
	     		} else {
	     			Long periodoGrabado = cuentas.get(nroCuenta);
	     			if (periodoGrabado == null) {
	     				int bimestre = SiatUtil.calcularBimestre(deudaAdmin.getPeriodo().intValue());
	     				cuentas.put(nroCuenta, deudaAdmin.getAnio()*100 + bimestre);
	     			}
	     		}
 			}
 			
 			first += pageSize; // paginacion
 			
 			SiatHibernateUtil.currentSession().getTransaction().commit();
			SiatHibernateUtil.closeSession();

			SiatHibernateUtil.currentSession();
 			SiatHibernateUtil.currentSession().beginTransaction();
 		}
 		
 		if (!emision.hasError()) { 		
 			// Si es TGI, actualizamos el Sistema de Catastro
 			if (emision.getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_TGI)) {
	 			// Recorremos el Mapa
				AdpRun.changeRunMessage("Actualizando sistema de catastro: " + first, 1);
	
				int cuentasProcesadas = 0;
				for(String nroCuenta: cuentas.keySet()) {
					Long anioPeriodo = cuentas.get(nroCuenta);
					if (cuentasProcesadas % 1000 == 0) {
						AdpRun.changeRunMessage("Actualizando sistema de catastro: cuentas procesadas " + cuentasProcesadas,0);
					}
					PadDAOFactory.getCatastroJDBCDAO().updatePerEmiCue(nroCuenta,anioPeriodo, "siat");
					cuentasProcesadas ++;
				}
 			}
	
			AdpRun.changeRunMessage("Generando Reporte de Totales", 1);
			try {
				String outputdir = adpRun.getProcessDir(AdpRunDirEnum.SALIDA) 
	 				+ File.separator + "Emision_" + emision.getId();
	
				String fullName = outputdir + File.separator + "totales.pdf";
				File fileReport = reporte.createReport(fullName);
				String desPdfFile = "Totales de Emision";
				emision.getCorrida().addOutputFileByPaso(2,fileReport.getName(), desPdfFile, fileReport.getAbsolutePath());
			} catch (Exception e) {
     			log.error("No se pudo crear el reporte");
			}
		}
 	}

	/**
	 * Actualiza el ultimo periodo emitido en el recurso.
	 */
	private void updateUltPeriodoEmitido(AdpRun run, Emision emision) {
		try {
			Recurso recurso = emision.getRecurso();

			Integer ultPer  = emision.getPeriodoHasta();  // Ultimo periodo emitido
			Integer ultAnio = emision.getAnio();		  // Ultimo anio emitido
			
			// Comparamos con el valor anterior
			String ultPerEmi = recurso.getUltPerEmi();
			if (!StringUtil.isNullOrEmpty(ultPerEmi)) {
				Integer prevPer  = Integer.parseInt(ultPerEmi.substring(4,6)); // Periodo Previo emitido. 
				Integer prevAnio = Integer.parseInt(ultPerEmi.substring(0,4)); // Anio Previo emitido.
				if (prevAnio != null && prevPer != null) {
					if ((prevAnio > ultAnio) || (prevAnio.equals(ultAnio) && prevPer > ultPer)) {
						ultPer  = prevPer;
						ultAnio = prevAnio;
					}
				}
			}
			
			// Formato del campo : anio(dddd)periodo(dd).
			ultPerEmi = "";
			ultPerEmi += ultAnio;
			ultPerEmi += (ultPer  < 10 ? "0" : "") + ultPer;

			// Actualizamos el recurso
			recurso.setUltPerEmi(ultPerEmi);
			DefGravamenManager.getInstance().updateRecurso(recurso);

		} catch (Exception e) {
			String msg = "Fallo la actualizacion del ultimo periodo emitido.";
			run.addError(msg);
			log.error(msg);
		}
	}
	
}