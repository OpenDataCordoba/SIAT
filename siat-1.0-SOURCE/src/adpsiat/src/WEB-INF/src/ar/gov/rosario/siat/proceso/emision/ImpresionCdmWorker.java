//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.proceso.emision;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.emi.buss.bean.Emision;
import ar.gov.rosario.siat.emi.iface.model.ImpresionCdMAdapter;
import ar.gov.rosario.siat.frm.buss.bean.Formulario;
import ar.gov.rosario.siat.frm.iface.model.FormatoSalida;
import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAdmin;
import ar.gov.rosario.siat.gde.buss.bean.LiqDeudaBeanHelper;
import ar.gov.rosario.siat.gde.buss.bean.Recibo;
import ar.gov.rosario.siat.gde.buss.bean.ReciboBeanHelper;
import ar.gov.rosario.siat.gde.buss.bean.SelAlmDeuda;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.model.LiqCuentaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqReciboVO;
import ar.gov.rosario.siat.gde.iface.model.LiqRecibos;
import ar.gov.rosario.siat.rec.buss.bean.EstadoObra;
import ar.gov.rosario.siat.rec.buss.bean.Obra;
import ar.gov.rosario.siat.rec.buss.bean.PlanillaCuadra;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpRunDirEnum;
import coop.tecso.adpcore.AdpRunState;
import coop.tecso.adpcore.AdpWorker;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaTimer;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CeldaVO;
import coop.tecso.demoda.iface.model.ContenedorVO;
import coop.tecso.demoda.iface.model.FilaVO;
import coop.tecso.demoda.iface.model.PrintModel;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.TablaVO;

/**
 * Procesa la Impresion Mensual de la deuda de Contribucion de Mejoras
 * @author Tecso Coop. Ltda.
 */
public class ImpresionCdmWorker implements AdpWorker {

	private static Logger log = Logger.getLogger(ImpresionCdmWorker.class);
	
	private static String ID_SELALMDEU = "idSelAlm"; // Parametro de Adp para la Seleccion Almacenada
	
	private static Long TODAS = -1L;
	
	// Manejo de Errores del Worker
 	private StringBuilder errorBuffer = new StringBuilder();
	boolean hasError= false;
	int totalErrors = 0;
	String errorMarker = "\t(!)";

	// Manejo de Warnings del Worker 
	private StringBuilder warningBuffer = new StringBuilder();
	boolean hasWarning=false;
	int totalWarnings = 0;
	String warningMarker = "\t(*)";

	public void reset(AdpRun adpRun) throws Exception {
		adpRun.changeState(AdpRunState.PREPARACION, "Reiniciado", false, null);
	}
	
	public void cancel(AdpRun adpRun) throws Exception {
	}

	public void execute(AdpRun adpRun) throws Exception {
	
		// Verfica numero paso y estado en adprun,
		// Llama a cada metodo segun el numero de paso
		
		Long pasoActual = adpRun.getPasoActual();
		
		if (pasoActual.equals(1L)) { 
			
			// Obtenemos los parametros del proceso
			String idImpresion = adpRun.getParameter(Emision.ADP_PARAM_ID); 
			String idObra = adpRun.getParameter(Obra.ID_OBRA);
			String anio = adpRun.getParameter(ImpresionCdMAdapter.ANIO);
			String mes = adpRun.getParameter(ImpresionCdMAdapter.MES); 
			String formatoSalida = adpRun.getParameter(ImpresionCdMAdapter.FORMATOSALIDA);
			String impresionTotal = adpRun.getParameter(ImpresionCdMAdapter.IMPRESIONTOTAL);
		
			// Ejecutamos el Paso 1
			this.ejecutarPaso1(adpRun, NumberUtil.getLong(idImpresion),
				NumberUtil.getLong(idObra),
				NumberUtil.getInt(anio),
				NumberUtil.getInt(mes),
				FormatoSalida.getById(new Integer(formatoSalida)),
				SiNo.getById(new Integer(impresionTotal)));
		}
		
		if (pasoActual.equals(2L)) { 

			// Obtenemos los parametros del proceso			
			String idImpresion = adpRun.getParameter(Emision.ADP_PARAM_ID); 
			String idObra = adpRun.getParameter(Obra.ID_OBRA);
			
			// Ejecutamos el Paso 2
			this.ejecutarPaso2(adpRun, NumberUtil.getLong(idImpresion),
				NumberUtil.getLong(idObra));
			
		}
	}

	public boolean validatePaso1(List<Obra> listObra, Emision emision) throws Exception {
		// Validaciones necesarias para el Paso 1
		return true;
	}

	/** Paso 1 del Proceso de Impresion: 
	 * -- genera los recibos de deuda para el mes y an~o suministrado.
	 * -- genera el padron de contribuyentes
	 *
	 * @param adpRun
	 * @param idImpresion
	 * @param idObra
	 * @param anio
	 * @param mes
	 * @param formatoSalida
	 * @param impresionTotal
	 *
	 * @return void
	 * @throws Exception
	 **/
	public void ejecutarPaso1(AdpRun adpRun, Long idImpresion, Long idObra, Integer anio, 
						Integer mes, FormatoSalida formatoSalida ,SiNo impresionTotal) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		Session session = null;
		Transaction tx  = null; 

		try {
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

			// Recuperamos la Impresion
			Emision impresion = Emision.getById(idImpresion);
			if (impresion == null) {
            	String descripcion = "No se pudo recuperar la impresion";
				addError(descripcion);
			}
			
			// Recuperamos el Recurso
			Long idRecurso = impresion.getRecurso().getId();
			if (idRecurso == null) {
            	String descripcion = "No se pudo recuperar el Recurso";
				addError(descripcion);
			}			
			
			// Recuperamos la(s) obra(s) para la cual se hara la impresion
			
			List<Obra> listObra = new ArrayList<Obra>();
			// Si se selecciono TODAS las obras
			if (idObra.equals(TODAS)) { 
				// imprimimos todas la obras para el recurso seleccionado, ordenadas por numero de Obra
				listObra = Obra.getListByRecursoYEstado(idRecurso, EstadoObra.ID_ACTIVA);
			}
			else {
				// Si no, imprimimos una obra en particular
				listObra.add(Obra.getByIdNull(idObra));
			}

			adpRun.logDebug("Se imprimiran las siguientes obras:");
			for (Obra obra: listObra) {
				adpRun.logDebug("\tObra: Nro. " + obra.getNumeroObra()  + " "
					+ (obra.getDesObra() == null ? "(sin descripcion)" : obra.getDesObra()));
			}
			
			// Determinamos el modo de impresion
			Integer estaImpresa = null;
			// Si no es una impresion Total, imprimimos la deuda no impresa unicamente
			if (impresionTotal.getId().equals(SiNo.NO.getId())) 
				estaImpresa = 0;
		
			adpRun.logDebug("Modo de Impresion: " + (estaImpresa !=  null ? "Parcial" : "Total"));
			
			// Calculamos el Total de Deuda a Imprimir
			Integer totalRecibos = this.totalAImprimir(listObra, anio, mes, estaImpresa);
			printStatusMessage(adpRun, "Se Generaran " + totalRecibos + " recibos ");
				
			// Validamos el paso 1
			this.validatePaso1(listObra, impresion);
			
			// Lista de recibos a imprimir
			List<LiqReciboVO> listRecibosVO = new ArrayList<LiqReciboVO>();
			
			// Lista de deuda a imprimir
			List<Deuda> listDeudaAdmin = new ArrayList<Deuda>();

			// Relacion entre Obras y sus correspondientes Recibos
			HashMap<Obra, List<LiqReciboVO>> mapRecibosVO = new HashMap<Obra, List<LiqReciboVO>>();
			
			// Si no hubo errores
			if (!hasError) {

				// Creamos una seleccion almacenada de deuda
				SelAlmDeuda selAlmDeuda = new SelAlmDeuda();
				GdeDAOFactory.getSelAlmDAO().update(selAlmDeuda);

				// La guardamos en los parametros de Adp
				adpRun.putParameter(ID_SELALMDEU, selAlmDeuda.getId().toString());

				Integer reciboActual = 1;
				// Para cada una de las obras
				for (Obra obra: listObra) {
					
					adpRun.logDebug("Procesando obra: Nro. " + obra.getNumeroObra() + " "  
							+ (obra.getDesObra() == null ? "(sin descripcion)" : obra.getDesObra()));					
					
					// Limpiamos la lista de Deuda para esta obra
					listDeudaAdmin = new ArrayList<Deuda>();
					
					// Limpiamos la lista de Recibos para esta obra
					listRecibosVO = new ArrayList<LiqReciboVO>();
					
					// Obtenemos las planillas, ordenadas por numero de cuadra
					for (PlanillaCuadra planilla: obra.getListPlanillaCuadra()) {
	
						adpRun.logDebug("--Procesando planilla: Nro. " + planilla.getId() + " "   
								+ (planilla.getDescripcion() == null ? "(sin descripcion)" : planilla.getDescripcion()));					
						adpRun.logDebug("---Obteniendo deuda administrativa de la planilla");
						
						DemodaTimer dt = new DemodaTimer();
						// Obtenemos la deuda asociada a la planilla
						List<DeudaAdmin> listDeudaPlanilla = GdeDAOFactory.getDeudaAdminDAO().
							getListDeudaAdminByIdPlanillaCuadraAndAnioAndPeriodo(planilla.getId(), anio, mes, estaImpresa);
						adpRun.logDebug(dt.stop("---Se obtuvo la deuda administrativa de la planilla"));

						for (DeudaAdmin deuda: listDeudaPlanilla) {
							 
								printStatusMessage(adpRun, "Generando Recibo " + (reciboActual++) + " de "+ totalRecibos);
								
								// Construimos un LiqReciboVO  
								LiqDeudaBeanHelper liqDeudaBeanHelper = new LiqDeudaBeanHelper(deuda.getCuenta());
								LiqCuentaVO liqCuentaVO = liqDeudaBeanHelper.getCuenta(LiqDeudaBeanHelper.OBJIMP);
								
								Recibo recibo = deuda.getCuenta().generarReciboCuotaCdM(deuda); 
								LiqReciboVO liqReciboVO = ReciboBeanHelper.getReciboVO(recibo, liqCuentaVO);
		
								// Lo agregamos a la lista para impresion
								listRecibosVO.add(liqReciboVO);
							
								// La agregamos en la lista para la seleccion
								listDeudaAdmin.add(deuda);
						}
					}
					
					if (ListUtil.isNullOrEmpty(listDeudaAdmin)) 
						adpRun.logDebug("La obra numero " + obra.getNumeroObra() 
									 + " no tiene deuda administrativa para el mes " + mes + " del " + anio);
					
					if (!ListUtil.isNullOrEmpty(listDeudaAdmin)) {
						adpRun.logDebug("Guardando deuda en la seleccion almacenada");
						// Guardamos las deudas en la seleccion almacenada
						selAlmDeuda.cargarSelAlmDeudaFromList(listDeudaAdmin);
						tx.commit();
						tx = session.beginTransaction();

						// Guardamos los recibos con la obra correspondiente
						mapRecibosVO.put(obra, listRecibosVO);
					}
				}
			}
			
			if (hasError) {
            	if (tx != null) tx.rollback(); 
            	if(log.isDebugEnabled()) log.debug(funcName + ": tx.rollback");
            	// Cambiamos el estado a Procesado con Error
            	adpRun.changeState(AdpRunState.FIN_ERROR, "Paso 1 con Errores: Consulte los Logs", false);
            
			} else {
				if (tx != null) tx.commit();
				if(log.isDebugEnabled()) log.debug(funcName + ": tx.commit");
				printStatusMessage(adpRun, "Generando los Archivos de Salida correspondientes al Paso 1"); 
			}
			
			// Generamos los Reportes
			generarOutputFilesPaso1(adpRun,impresion, mapRecibosVO, formatoSalida);
			
			if (hasError) {
				// Cambiamos el estado a Procesado con Error
            	adpRun.changeState(AdpRunState.FIN_ERROR, "Paso 1 con Errores: Consulte los Logs", false);
            
			} else {
				// Cambiamos el estado a En Espera a Continuar 
				adpRun.changeState(AdpRunState.ESPERA_CONTINUAR, "Los reportes han sido generados con exito", true);
			}

			// Volcamos el buffer de warnings
        	dumpWarningBuffer(adpRun);
        	
        	// Volcamos el buffer de errores
        	dumpErrorBuffer(adpRun);
			
			log.debug(funcName + ": exit");
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
		
			addError(e.toString());
			dumpErrorBuffer(adpRun);

			// Cambiamos el estado a Abortado por Excepcion 
			adpRun.changeState(AdpRunState.ABORT_EXCEP, "Se produjo un excepcion: Consulte los Logs", true);
			
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	private void generarOutputFilesPaso1(AdpRun adpRun, Emision impresion, 
			HashMap<Obra, List<LiqReciboVO>> map, FormatoSalida formatoSalida) throws Exception {
		
		try {
		
			// Seteamos el directorio de Salida
			String outPutDir = adpRun.getProcessDir(AdpRunDirEnum.SALIDA)+File.separator+"/";

			// Lista de los archivos de salida generados
			List<String> listOutputFilename = new ArrayList<String>(); 
			
			// Creamos el directorio de salida para esta corrida
			String dirName = adpRun.getProcessDir(AdpRunDirEnum.SALIDA) + File.separator+"/"+ "ImpresionCdM_" + impresion.getId();
	
			(new File(dirName)).mkdir();
				
			outPutDir = dirName + File.separator+"/";
	
			Iterator it = map.entrySet().iterator();
			
			List<LiqReciboVO> totales = new ArrayList<LiqReciboVO>();
			
			Long counter = 0L;			
			
			// Para cada una de las obras, generamos los Recibos y el Padron de Firmas
			while (it.hasNext()) {
				
				// Obtenemos una entrada del 
				Map.Entry e = (Map.Entry)it.next();
				
				// Obtenemos la obra
				Obra obra = (Obra) e.getKey();
				
				// Obtenemos la lista de Recibos asociados a la obra
				List<LiqReciboVO> listRecibos = (ArrayList<LiqReciboVO>) e.getValue();
				
				totales.addAll(listRecibos);
				
				// Seteamos el nombre para el archivo de Recibos de la obra segun la especificacion de ImpriPost
				//String recibosFileName = outPutDir+"ImpresionCdM"+ impresion.getId().toString() + "-Obra " + obra.getNumeroObra();
				String tipObr = "";
				if (obra.getRecurso().getEsObraPavimento())
					tipObr = "pa";
				if (obra.getRecurso().getEsObraGas())
					tipObr = "ga";
				String numObr = StringUtil.completarCerosIzq(obra.getNumeroObra().toString(), 6);
				String recibosFileName = outPutDir+"tr_cdm_"+ tipObr +"_"+numObr+"."
					+ DateUtil.formatDate(impresion.getFechaEmision(), DateUtil.yyMMdd_MASK);
				recibosFileName = generarRecibosDeuda(recibosFileName, formatoSalida, listRecibos);
				
				listOutputFilename.add(recibosFileName);
		
				if (recibosFileName == null) {
					adpRun.logError("No se pudo generar el archivo de Recibos para la Obra. Abortamos " + obra.getNumeroObra());
					continue;
				}
				
				String nomRecibo = "Obra Nro. " + obra.getNumeroObra() + " " + (obra.getDesObra() == null ? "" : obra.getDesObra());
				String desRecibo = listRecibos.size() + " Recibos de dueda administrativa correspondientes a la obra "
													+ obra.getNumeroObra();
						
				// Agregamos el archivo de Recibos a la salida de la Corrida
				impresion.getCorrida().addOutputFileOrdenada(nomRecibo, desRecibo, recibosFileName, counter++);
			
				// Seteamos el nombre para el archivo del Padron de Firmas
				String padronFileName = outPutDir+"ImpresionCdM"+ impresion.getId().toString() + "-PadronObra" + obra.getNumeroObra();
				padronFileName = generarPadronFirmas(padronFileName, listRecibos);

				listOutputFilename.add(padronFileName);
				
				if (padronFileName == null) {
					adpRun.logError("No se pudo generar el archivo del Padron de Firmas para la Obra. " +
											"Abortamos " + obra.getNumeroObra());
					continue;
				}
	
				String nomPadron = "Padron de Firmas Obra " + obra.getNumeroObra();
				String desPadron = "Padron de Firmas de los contribuyentes de Contribuci\u00F3n de Mejoras de la Obra " 
						+ obra.getNumeroObra();
				
				// Agregamos el archivo Padron de Firmas a la salida de la Corrida
				impresion.getCorrida().addOutputFileOrdenada(nomPadron, desPadron, padronFileName,counter++);
			}
			
			// Generamos el archivo con los totales de la impresion
			String totalesFileName = outPutDir+"ImpresionCdM"+ impresion.getId().toString() + "-Totales";
			totalesFileName = generarReporteTotales(totalesFileName, impresion, map);
			
			listOutputFilename.add(totalesFileName);
			
			String nomTotales = "Totales";
			String desTotales = "Reporte con los totales de la impresion ";
			
			// Agregamos el archivo de Totales a la Corrida
			impresion.getCorrida().addOutputFileOrdenada(nomTotales, desTotales, totalesFileName, counter++);
			
			// Copiamos los archivos generados al directorio "ultimo"
			String dirSalida = AdpRun.currentRun().getProcessDir(AdpRunDirEnum.SALIDA);
			File dirUltimo = new File(dirSalida, "ultimo");
			dirUltimo.mkdir();
			AdpRun.deleteDirFiles(dirUltimo);
			for(String filename: listOutputFilename) {
				File src = new File(filename);
				AdpRun.copyFile(src, dirUltimo);
			}
			
		}
		catch (Exception e) {
			log.error("Service Error: ",  e);
			addError("No se pudieron generar los reportes del Paso 1.\n" +
					"\t Verifique la estructura de directorios\n" + 
					"\t Verifique los permisos de los directorios");
		}

	}
	
	private String generarRecibosDeuda(String fileName, FormatoSalida formatoSalida, 
			List<LiqReciboVO> listRecibos) throws Exception {
		
		try {
			// Obtenemos el Print Model para los recibos de CdM
			PrintModel printModel= Formulario.getPrintModel(Recibo.COD_FRM_RECIBO_CDM, formatoSalida);
			// Seteamos los datos
			printModel.setData(new LiqRecibos(listRecibos));
			printModel.setTopeProfundidad(3);
			// Borramos el XML de datos
			printModel.setDeleteXMLFile(false);
			
			// Obtenemos el stream de bytes del PrintModel
			byte[] byteStream = printModel.getByteArray();

			// Seteamos la extension del archivo segun el tipo de salida
			if (formatoSalida.getEsPDF())
				fileName +=  ".pdf";

			if (formatoSalida.getEsTXT())
				fileName +=  ".txt";

			// Creamos el archivo
			FileOutputStream recibosFile = new FileOutputStream(fileName);
			recibosFile.write(byteStream);
			recibosFile.close();
	
			return fileName;
		}
		
		catch (Exception e) {
			return null;
		}
	}

	private String generarPadronFirmas(String fileName, List<LiqReciboVO> listRecibosVO) throws Exception {

		try {
			// Obtenemos el Print Model del Padron de Firmas 
			PrintModel printModel= Formulario.getPrintModelForPDF(Recibo.COD_FRM_PADRON_CDM);
			// Seteamos los datos
			printModel.setData(new LiqRecibos(listRecibosVO));
			printModel.setTopeProfundidad(3);
			// Borramos el XML de datos
			//printModel.setDeleteXMLFile(true);
			
			printModel.setExcludeFileName("");
	
			// Obtenemos el stream de bytes del PrintModel
			byte[] byteStream = printModel.getByteArray();
			
			// Seteamos la extension a Pdf
			fileName += ".pdf";

			//Creamos el archivo
			FileOutputStream padronFile = new FileOutputStream(new File(fileName));
			padronFile.write(byteStream);
			padronFile.close();
			
			return fileName;
		}
		catch (Exception e) {
			return null;
		}
	}
	
	/*
	private String generarReporteTotales(String fileName, List<LiqReciboVO> listRecibosVO) throws Exception {
		
		try {
			// Obtenemos el Print Model del Reporte de Totales
			PrintModel printModel= Formulario.getPrintModelForPDF(Recibo.COD_FRM_REPOBRA_IMP_CDM);
			// Seteamos los datos
			printModel.setData(new LiqRecibos(listRecibosVO));
			printModel.setTopeProfundidad(3);
			// Borramos el XML de datos
			//printModel.setDeleteXMLFile(true);
			
			printModel.setExcludeFileName("");
			
			// Obtenemos el stream de bytes del PrintModel
			byte[] byteStream = printModel.getByteArray();
	
			// Seteamos la extension a pdf
			fileName += ".pdf";

			//Creamos el archivo
			FileOutputStream recibosFile = new FileOutputStream(fileName);
			recibosFile.write(byteStream);
			recibosFile.close();
	
			return fileName;
		}
		
		catch (Exception e) {
			return null;
		}
	}*/	
		
	@SuppressWarnings("unchecked")
	private String generarReporteTotales(String fileName, Emision impresion,
				HashMap<Obra, List<LiqReciboVO>> map) throws Exception {
		
		try {
			
			Integer cantCuentas = 0;
			Double montoTotal = 0D;
			
			// Contenedor de Datos del Reporte
			ContenedorVO contenedorVO = new ContenedorVO("Contenedor");			
			
			// Tabla de Datos
			TablaVO tablaContenido = new TablaVO("Contenido");
			
			FilaVO filaCabecera = new FilaVO();
			filaCabecera.add(new CeldaVO("Obra"));
			filaCabecera.add(new CeldaVO("Cant. de Cuentas"));
			filaCabecera.add(new CeldaVO("Monto Total"));
			tablaContenido.setFilaCabecera(filaCabecera);

			Iterator it = map.entrySet().iterator();
			// Para cada una de las obras
			while (it.hasNext()) {
				// Obtenemos una entrada del 
				Map.Entry e = (Map.Entry)it.next();
				
				// Obtenemos la obra
				Obra obra = (Obra) e.getKey();
				
				// Obtenemos la lista de Recibos asociados a la obra
				List<LiqReciboVO> listRecibos = (ArrayList<LiqReciboVO>) e.getValue();

				// Genera la fila del reporte
				FilaVO filaContenido = new FilaVO();
				// Obra
				filaContenido.add(new CeldaVO(obra.getNumeroObra() + " - " + obra.getDesObra()));

				Integer cantCuentasObra = this.totalCuentas(listRecibos);
				
				cantCuentas += cantCuentasObra;
				// Cant. Cuentas			
				filaContenido.add(new CeldaVO(cantCuentasObra.toString()));

				Double montoTotalObra = this.sumTotalesImportes(listRecibos);
				
				montoTotal += montoTotalObra;
				
				// Monto Total de la Obra			
				String strMontoTotalObra = StringUtil.redondearDecimales(montoTotalObra, 1, 2);
				filaContenido.add(new CeldaVO("$ " + strMontoTotalObra));

				tablaContenido.add(filaContenido);
			}

			contenedorVO.add(tablaContenido);

			// Se agrega la fila con los totales generales
			FilaVO filaPie = new FilaVO();
			filaPie.add(new CeldaVO("TOTAL"));
			filaPie.add(new CeldaVO(cantCuentas.toString()));
			String strMontoTotal = StringUtil.redondearDecimales(montoTotal, 1, 2);
			filaPie.add(new CeldaVO("$ " + strMontoTotal));

			tablaContenido.addFilaPie(filaPie);

			
			// Generacion del PrintModel		
			PrintModel printModel = new PrintModel();
			
			printModel.setRenderer(FormatoSalida.PDF.getId());
			printModel.putCabecera("FileSharePath", SiatParam.getString("FileSharePath"));
			printModel.setExcludeFileName("/publico/general/reportes/default.exclude");
			printModel.cargarXsl("/mnt/publico/general/reportes/pageModel.xsl", PrintModel.RENDER_PDF);
			printModel.setData(contenedorVO);
			printModel.setTopeProfundidad(5);

			printModel.putCabecera("TituloReporte", "Padron de Totales");
			printModel.putCabecera("Fecha", DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_MASK));
			printModel.putCabecera("Hora", DateUtil.formatDate(new Date(), DateUtil.HOUR_MINUTE_MASK));
			printModel.putCabecera("Usuario", impresion.getUsuario());
			
			//printModel.setDeleteXMLFile(true);
			
			// Obtenemos el stream de bytes del PrintModel
			byte[] byteStream = printModel.getByteArray();
	
			// Seteamos la extension a pdf
			fileName += ".pdf";

			// Creamos el archivo
			FileOutputStream recibosFile = new FileOutputStream(fileName);
			recibosFile.write(byteStream);
			recibosFile.close();
	
			return fileName;									
		}
		
		catch (Exception e) {
			return null;
		}
	}	

	//Calculo de los recibos totales de los recibos por obra
	private Double sumTotalesImportes (List<LiqReciboVO> listRecibos) {
		
		Double total = 0D;
		
		for (LiqReciboVO recibo: listRecibos) {
			 total += recibo.getTotalPagar();
		}
		
		return total;
	}

	//Calculo de los recibos impresos.
	private Integer totalCuentas (List<LiqReciboVO> listRecibos) {
		
		Integer cuentas = 0;
		
		for (LiqReciboVO recibo: listRecibos) {
			 if (recibo.getDatosReciboCdMVO().getCantCuotas() != 1)
				 cuentas++;
		}
		
		return cuentas;
	}
	
	
	/**
	 * Paso 2 del Proceso de Impresion: 
	 * 	--Marca la deuda como impresa.
	 * 
	 * @param adpRun
	 * @param impresion
	 * @param idObra
	 * 
	 * @return void
	 * @throws Exception
	 */
	public void ejecutarPaso2(AdpRun adpRun, Long idImpresion, Long idObra) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");
			try {

				SiatHibernateUtil.currentSession().beginTransaction();

				Emision impresion = Emision.getById(idImpresion);
				
				// Obtenemos las seleccion almacenada desde Adp
				adpRun.logDebug("Obteniendo seleccion almacenada");
				Long idSelAlm = new Long(adpRun.getParameter(ID_SELALMDEU));
				SelAlmDeuda selAlm = SelAlmDeuda.getByIdNull(idSelAlm);
				if (selAlm == null) {
					addError("No se pudo obtener la seleccion almacenada");
				}

				// si no hubo errores
				if (!hasError) {
					// Para cada una de las deudas en la seleccion
					for(DeudaAdmin deuda: selAlm.getListDeudaAdmin()) {
						// La seteamos como impresa 
						adpRun.logDebug("Marcando la deuda con id: " + deuda.getId() + "como impresa");
						deuda.setEstaImpresa(1); 
						GdeDAOFactory.getDeudaAdminDAO().update(deuda);
					}
					
					// Eliminamos el detalle
					adpRun.logDebug("Eliminando detalle");
					selAlm.deleteListSelAlmDet();
				
				}
				// Eliminamos la seleccion almacenada
				adpRun.logDebug("Eliminando seleccion almacenada");
				GdeDAOFactory.getSelAlmDAO().delete(selAlm);
				
				if (hasError) {
				
					SiatHibernateUtil.currentSession().getTransaction().rollback(); 
	            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}

	            	// Cambiamos el estado a Procesado con Error
	            	adpRun.changeState(AdpRunState.FIN_ERROR, "Paso 1 con Errores: Consulte los Logs", false);


				} else {
	            
	            	SiatHibernateUtil.currentSession().getTransaction().commit();
					if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}

					// cambiar estado, incremetar paso, y actualiza el pasoCorrida. 
					adpRun.changeState(AdpRunState.FIN_OK, "La deuda ha sido marcada como impresa", true); 
				}

				// Volcamos el buffer de warnings
	        	dumpWarningBuffer(adpRun);
	        	
	        	// Volcamos el buffer de errores
	        	dumpErrorBuffer(adpRun);

				log.debug(funcName + ": exit");
				
			} catch (Exception e) {
				Transaction tx = SiatHibernateUtil.currentSession().getTransaction();
				log.error("Service Error: ",  e);
				if(tx != null) tx.rollback();

				addError(e.toString());
				dumpErrorBuffer(adpRun);

				// Cambiamos el estado a Abortado por Excepcion 
				adpRun.changeState(AdpRunState.ABORT_EXCEP, "Se produjo un excepcion: Consulte los Logs", true);
			
			} finally {
				SiatHibernateUtil.closeSession();
			}
		}
	
	public boolean validate(AdpRun adpRun) throws Exception {
		// TODO Ver Validaciones necesarias
		return false;
	}
	
	
	/**
	 * Calcula el total de deuda a imprimir 
	 **/
	public Integer totalAImprimir(List<Obra> listObra, Integer anio, Integer mes, Integer estaImpresa) {
		
		Integer cantRecibos = 0;
		
		if (!ListUtil.isNullOrEmpty(listObra)) {
			List<PlanillaCuadra> listPlanillas = new ArrayList<PlanillaCuadra>();
			
			for (Obra obra: listObra) {
				listPlanillas.addAll(obra.getListPlanillaCuadra());
			}
		
			if (!ListUtil.isNullOrEmpty(listPlanillas)) {
				Long[] listIdPlanilla = ListUtilBean.getArrLongIdFromListBaseBO(listPlanillas);
				
				List<DeudaAdmin> listDeuda = GdeDAOFactory.getDeudaAdminDAO().
					getListDeudaAdminByListIdPlanillaCuadraAndAnioAndPeriodo(listIdPlanilla, anio, mes, estaImpresa);
				
				if (!ListUtil.isNullOrEmpty(listPlanillas)) {
					cantRecibos = listDeuda.size();
				}
			}
		}
	
		return cantRecibos;
	}
	
	// Manejo de Mensajes de Status
	private void printStatusMessage(AdpRun adpRun, String msg) {
		try {
			// Mostramos msg en la pantalla
			adpRun.changeMessage(msg);
			// Logueamos msg
			adpRun.logDebug(msg);
		}
		catch(Exception e) {
			
		}
	}
	
	// Manejo de Errores del Worker.
	private void addError(String msg) {
		this.hasError = true;
		this.totalErrors++;
		this.errorBuffer.append(errorMarker + "Error " + this.totalErrors + ": " + msg + '\n');
	}

	private void dumpErrorBuffer(AdpRun adpRun) {
		this.errorBuffer.append(errorMarker + "Total Errors: " + this.totalErrors);
		adpRun.logDebug(this.errorBuffer.toString());
	}

	// Manejo de Warnings del Worker 
	private void addWarning(String msg) {
		this.hasWarning = true;
		this.totalWarnings++;
		this.warningBuffer.append(warningMarker + "Warning " + this.totalWarnings + ": " + msg + '\n');
	}

	private void dumpWarningBuffer(AdpRun adpRun) {
		this.warningBuffer.append(warningMarker + "Total Warnings: " + this.totalWarnings);
		adpRun.logDebug(this.warningBuffer.toString());
	}
	
}
