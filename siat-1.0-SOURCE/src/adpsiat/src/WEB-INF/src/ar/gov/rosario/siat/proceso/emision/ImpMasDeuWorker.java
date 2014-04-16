//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.proceso.emision;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.Driver;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.Options;
import org.apache.fop.apps.TraxInputHandler;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.def.buss.bean.Atributo;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.emi.buss.bean.ImpMasDeu;
import ar.gov.rosario.siat.emi.buss.bean.ImpMasDeuTotReport;
import ar.gov.rosario.siat.emi.buss.dao.EmiDAOFactory;
import ar.gov.rosario.siat.frm.buss.bean.Formulario;
import ar.gov.rosario.siat.frm.iface.model.FormatoSalida;
import ar.gov.rosario.siat.pad.buss.bean.GeneralFacade;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;

import com.lowagie.text.pdf.PdfCopyFields;
import com.lowagie.text.pdf.PdfReader;

import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpRunDirEnum;
import coop.tecso.adpcore.AdpRunState;
import coop.tecso.adpcore.MultiThreadedAdpWorker;
import coop.tecso.adpcore.ProcessMessage;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.SplitedWriter;
import coop.tecso.demoda.iface.helper.SpooledWriter;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Worker del Proceso de Impresion Masiva de Deuda
 * 
 * @author Tecso Coop. Ltda.
 */
public class ImpMasDeuWorker extends MultiThreadedAdpWorker {

	private static Logger log = Logger.getLogger(ImpMasDeuWorker.class);

	private static final String OUTPUTDIR_PREFIX	  = "ImpMasDeu_";
	private static final String TMP_FILE_PREFIX 	  = "/tmp/impresionMasiva-";
	private static final String TMP_NOTIF_FILE_PREFIX = "/tmp/impresionMasiva-notif";

	public void execute(AdpRun run) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		Long pasoActual = run.getPasoActual();
		log.debug("Ejecutando paso " + pasoActual);
		try {
			String infoMsg = "";
			if (pasoActual.equals(1L)) {
				ejecutarPaso1(run);
				infoMsg += "Los archivos de impresion se ";
				infoMsg += "han generado exitosamente";
				run.changeState(AdpRunState.FIN_OK, infoMsg);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		
		} catch (Exception e) {			
			String infoError = "Ocurrieron errores durante la ejecucion del " + 
							   "paso " + pasoActual + ". Consulte los logs.";
			run.addError(infoError + ": ", e);
			// Cambiamos el estado a: FINALIZADO CON ERROR e informamos al usuario
			run.changeState(AdpRunState.FIN_ERROR, infoError);
		}
	}

	private void ejecutarPaso1(AdpRun run) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		Session session = null;
		try {
			session = SiatHibernateUtil.currentSession();
			
			// Obtenemos los parametros de Adp
			Long idImpMasDeu = Long.parseLong(run.getParameter(ImpMasDeu.ID_IMPMASDEU));
			ImpMasDeu impMasDeu = ImpMasDeu.getById(idImpMasDeu);
			Recurso recurso = impMasDeu.getRecurso();
			Atributo atributo = impMasDeu.getAtributo();
			String atrValor = impMasDeu.getAtrValor();
			Integer anio = impMasDeu.getAnio();
			Integer periodoDesde = impMasDeu.getPeriodoDesde();
			Integer periodoHasta = impMasDeu.getPeriodoHasta();

			// Creamos un cache con las localidades
			Map<String, String> mapLocalidades = GeneralFacade.getInstance().getMapaLocalidades();

			// Creamos un spooledWriter para que los procesadores 
			// puedan escribir ordenadamente los recibos
			SpooledWriter spoolRecibos = new SpooledWriter();

			// Clase para generar un Reporte de Totales 
			ImpMasDeuTotReport reporte = new ImpMasDeuTotReport(impMasDeu);
			
			// Creamos el context para los procesadores
			ImpMasDeuContext context = new ImpMasDeuContext();
			context.setAdpRun(run);
			context.setSpoolRecibos(spoolRecibos);
			context.setMapLocalidades(mapLocalidades);
			context.setReporte(reporte);

			run.changeStatusMsg("Inicializando Procesadores");
			initializeProcessors(ImpMasDeuProcessor.class, 10, context);

			run.changeStatusMsg("Obteniendo deuda a imprimir");
			obtenerCuentas(run,recurso, anio, periodoDesde, periodoHasta, atributo, atrValor);

			// Finalizamos el proceso
			finishProcessors();
			
			// Cerramos el spooler
			spoolRecibos.close();

			run.changeStatusMsg("Generando recibos de deuda");
			List<File> listOutputFile = generarArchivos(run,impMasDeu, spoolRecibos);

			// Copiamos los archivos generados al directorio de "ultimo".
			String dirSalida = AdpRun.currentRun().getProcessDir(AdpRunDirEnum.SALIDA);
			File dirUltimo = new File(dirSalida, "ultimo");
			dirUltimo.mkdir();
			
			// Borramos los archivos anteriores
			AdpRun.deleteDirFiles(dirUltimo);
			
			for (File f: listOutputFile) {
				AdpRun.copyFile(f, dirUltimo);
			}

			String outputdir = run.getProcessDir(AdpRunDirEnum.SALIDA) 
				+ File.separator + OUTPUTDIR_PREFIX + impMasDeu.getId();

			File f = reporte.createReport(outputdir + File.separator + "totales.pdf");
	 		impMasDeu.getCorrida().addOutputFileByPaso(1,f.getName(), "Totales por broche", f.getAbsolutePath());

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			
		} catch (Exception e) {
			log.error("Ocurrio una excepcion durante la ejecucion del paso", e);
			abortProcessors();
			throw new DemodaServiceException(e);
		} finally {
			session.close();
		}
	}
	
	private void obtenerCuentas(AdpRun run, Recurso recurso, Integer anio, Integer periodoDesde, 
			Integer periodoHasta, Atributo atributo, String atrValor) throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {

			List<Long> listIdCuenta = new ArrayList<Long>();
			
			// Si el recurso que no es autoliquidable.
			if (recurso.getEsAutoliquidable().equals(SiNo.NO.getBussId())) {
				if (log.isDebugEnabled()) log.debug(funcName + ": Obteniendo ids de cuentas con broches");
	
				List<Object[]> listIdCuentaConBroche = EmiDAOFactory.getImpMasDeuDAO()
					.getListIdCuentaBy(recurso,anio,periodoDesde,periodoHasta,atributo,atrValor, true);
	
				for (Object[] object: listIdCuentaConBroche) {
					listIdCuenta.add((Long) object[0]); 
				}
				
				if (log.isDebugEnabled()) log.debug(funcName + ": Obteniendo ids de cuentas sin broches");
	
				List<Object[]> listIdCuentaSinBroche = EmiDAOFactory.getImpMasDeuDAO()
					.getListIdCuentaBy(recurso,anio,periodoDesde,periodoHasta,atributo,atrValor, false);

				for (Object[] object: listIdCuentaSinBroche) {
					listIdCuenta.add((Long) object[0]);
				}
			} else {
				// Si el recurso que es autoliquidable.
				List<Object[]> listIds = EmiDAOFactory.getImpMasDeuDAO()
					.getListIdCuentaForAutoliquidables(recurso, anio, periodoDesde, periodoHasta);

				for (Object[] object: listIds) {
					listIdCuenta.add((Long) object[0]);
				}
			}

			if (log.isDebugEnabled())  log.debug(funcName + ": Filtrando cuentas excluidas");
			listIdCuenta.removeAll(ImpMasDeu.getListidCuentasExcluidas());
			
			// Seteamos la cantidad de registros a procesar
			run.setTotalRegs(new Long(listIdCuenta.size()));
			for (Long idCuenta: listIdCuenta) {
  				// Creamos un mensaje con el id de la cuenta
				// y los enviamos a la cola
				ProcessMessage<Object> msgCuenta = new ProcessMessage<Object>();
				msgCuenta.setData(idCuenta);
				sendMessage(msgCuenta);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		} catch (Exception e) {
			log.error("Service Error: " + e.getMessage());
			throw new DemodaServiceException(e);
		}
	}

	private List<File> generarArchivos(AdpRun run, ImpMasDeu impMasDeu, SpooledWriter spooledWriter) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		List<File> listOutputFile = new ArrayList<File>();
		
		// Creamos el directorio de salida
		String outputdir = run.getProcessDir(AdpRunDirEnum.SALIDA) 
				+ File.separator + OUTPUTDIR_PREFIX + impMasDeu.getId();
 		
		File dir = new File(outputdir);
		dir.mkdir();

		// Borramos los archivos anteriores
		AdpRun.deleteDirFiles(dir);
		
		Map<String, Writer> mapBrocheXMLFile = spooledWriter.getMap();
		for (String broche: sortKeys(mapBrocheXMLFile.keySet())) {
			// Para cada XML, generamos un archivo
			SplitedWriter splitedXmlFile = (SplitedWriter) mapBrocheXMLFile.get(broche);
			String reciboFileName = outputdir + File.separator + impMasDeu.getReciboFileName(broche);
			listOutputFile.add(this.createRecibo(impMasDeu,splitedXmlFile,reciboFileName));
			
			// Analizamos si debemos crear un archivo de notificacion
			Recurso recurso = impMasDeu.getRecurso();
			if (recurso.getGenNotif().equals(SiNo.SI.getBussId())) {
				String notifFileName = outputdir + File.separator + impMasDeu.getNotifFileName(broche);
				listOutputFile.add(this.createNotificacion(impMasDeu,splitedXmlFile,notifFileName));
			}
					
			// Analizamos si debemos generar un padron de firmas para imprimir
			if (recurso.getGenPadFir() != null && recurso.getGenPadFir().equals(SiNo.SI.getBussId())) {
				String padronFileName = outputdir + File.separator + impMasDeu.getPadronFileName(broche);;
				listOutputFile.add(this.createPadronFirmas(impMasDeu,splitedXmlFile,padronFileName));
			}

			// Borramos los archivos de datos (XML)
			for (File f: splitedXmlFile.getListSplitedFiles())
				f.delete();
		} 
	
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listOutputFile;
	}

	private File createRecibo(ImpMasDeu impMasDeu, SplitedWriter splitedXMLFile,  String fileName) {
		try {
			
			Recurso recurso = impMasDeu.getRecurso();
			Formulario formulario = recurso.getFormImpMasDeu();
			
			// Creamos el archivo XSL temporal
			String unidad = File.separator.equals("\\") ? "c:\\" : "";
			String tmpXslFileName = unidad + TMP_FILE_PREFIX + UUID.randomUUID() + ".xsl";
			File xslFile  = new File(tmpXslFileName);
			
			FileWriter xslFileWriter = new FileWriter(xslFile);
			String fileSharePath = SiatParam.getString("FileSharePath");

			FormatoSalida formato = FormatoSalida.getById(impMasDeu.getFormatoSalida());
			
			File reciboFile = null;
			// Determinamos el formato de salida
			if (formato.equals(FormatoSalida.PDF)) {
				xslFileWriter.write(formulario.getXsl().replace("@@FileSharePath", fileSharePath));
				xslFileWriter.close();
				reciboFile = createPdfFile(fileName, xslFile, splitedXMLFile.getListSplitedFiles()); 
			} 

			if (formato.equals(FormatoSalida.TXT)) { 
				xslFileWriter.write(formulario.getXslTxt().replace("@@FileSharePath", fileSharePath));
				xslFileWriter.close();
				reciboFile = createTxtFile(recurso, fileName, xslFile, splitedXMLFile.getListSplitedFiles());
			}
			
			String desOutputFile = "Recibos de deuda";
			Corrida corrida = impMasDeu.getCorrida();
			corrida.addOutputFileByPaso(1, reciboFile.getName(), desOutputFile, reciboFile.getAbsolutePath());

			xslFile.delete();
			
			return reciboFile;
			
		} catch (Exception e) {
			log.error("Error al generar el archivo " + fileName, e);
			return null;
		}
	}
	
	private File createNotificacion(ImpMasDeu impMasDeu, SplitedWriter splitedXMLFile,  String fileName) {
		try {
			
			Recurso recurso = impMasDeu.getRecurso();
			Formulario formulario = recurso.getFormNotif();
			
			// Creamos el archivo XSL temporal
			String unidad = File.separator.equals("\\") ? "c:\\" : "";
			String tmpXslFileName = unidad + TMP_NOTIF_FILE_PREFIX + UUID.randomUUID() + ".xsl";
			File xslFile  = new File(tmpXslFileName);
			
			FileWriter xslFileWriter = new FileWriter(xslFile);
			String fileSharePath = SiatParam.getString("FileSharePath");

			FormatoSalida formato = FormatoSalida.getById(impMasDeu.getFormatoSalida());
			
			File notifFile = null;
			// Determinamos el formato de salida
			if (formato.equals(FormatoSalida.PDF)) {
				xslFileWriter.write(formulario.getXsl().replace("@@FileSharePath", fileSharePath));
				xslFileWriter.close();
				notifFile = createPdfFile(fileName, xslFile, splitedXMLFile.getListSplitedFiles()); 
			} 

			if (formato.equals(FormatoSalida.TXT)) { 
				xslFileWriter.write(formulario.getXslTxt().replace("@@FileSharePath", fileSharePath));
				xslFileWriter.close();
				notifFile = createTxtFile(recurso, fileName, xslFile, splitedXMLFile.getListSplitedFiles());
			}
			
			String desOutputFile = "Archivo de Notificacion";
			Corrida corrida = impMasDeu.getCorrida();
			corrida.addOutputFileByPaso(1, notifFile.getName(), desOutputFile, notifFile.getAbsolutePath());

			xslFile.delete();
			
			return notifFile;
			
		} catch (Exception e) {
			log.error("Error al generar el archivo de notificacion" + fileName, e);
			return null;
		}
	}

	private File createPadronFirmas(ImpMasDeu impMasDeu, SplitedWriter splitedXMLFile,  String fileName) {
		try {
			
			Recurso recurso = impMasDeu.getRecurso();
			Formulario formulario = recurso.getFormPadFir();
			
			// Creamos el archivo XSL temporal
			String unidad = File.separator.equals("\\") ? "c:\\" : "";
			String tmpXslFileName = unidad + TMP_NOTIF_FILE_PREFIX + UUID.randomUUID() + ".xsl";
			File xslFile  = new File(tmpXslFileName);
			
			FileWriter xslFileWriter = new FileWriter(xslFile);
			String fileSharePath = SiatParam.getString("FileSharePath");

			FormatoSalida formato = FormatoSalida.getById(impMasDeu.getFormatoSalida());
			
			File padronFile = null;
			
			// Determinamos el formato de salida
			//TODO: sea TXT o PDF el formato de salida, solo tenemos implementado PDF
			
//			if (formato.equals(FormatoSalida.PDF)) {
				xslFileWriter.write(formulario.getXsl().replace("@@FileSharePath", fileSharePath));
				xslFileWriter.close();
				padronFile = createPdfFile(fileName, xslFile, splitedXMLFile.getListSplitedFiles()); 
//			} 

//			if (formato.equals(FormatoSalida.TXT)) { 
//				xslFileWriter.write(formulario.getXslTxt().replace("@@FileSharePath", fileSharePath));
//				xslFileWriter.close();
//				padronFile = createTxtFile(fileName, xslFile, splitedXMLFile.getListSplitedFiles());
//			}
			
			String desOutputFile = "Padron de Firmas";
			Corrida corrida = impMasDeu.getCorrida();
			corrida.addOutputFileByPaso(1, padronFile.getName(), desOutputFile, padronFile.getAbsolutePath());

			xslFile.delete();
			
			return padronFile;
			
		} catch (Exception e) {
			log.error("Error al generar el padron de firmas" + fileName, e);
			return null;
		}
	}
	
	private File createTxtFile(Recurso recurso, String fileName, File xslFile, List<File> listXmlFile) 
		throws TransformerException, IOException { 
		
		File file = new File(fileName);
		OutputStream outputStream = new FileOutputStream(file);

		// Añadimos el header Postcript
		outputStream.write(getPostcriptHeader(recurso).getBytes());
		
		for (File xmlFile: listXmlFile) {
			String unidad = File.separator.equals("\\") ? "c:\\" : "";
			String txtChunkFileName = unidad + TMP_FILE_PREFIX + UUID.randomUUID()  + ".txt";
			
			File txtChunk = new File(txtChunkFileName);
			
			TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer(new StreamSource(xslFile));
            transformer.transform(new StreamSource(xmlFile), new StreamResult(txtChunk));
            InputStream inputStream = new FileInputStream(txtChunk);
          
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) > 0)
            	outputStream.write(buffer, 0, len);
            inputStream.close();
            
		}
		
		outputStream.close();
		
		return file;
	}
	
	private static String getPostcriptHeader(Recurso recurso) {
		String psHeader = "";
		
		if (recurso.getCodRecurso().equals(Recurso.COD_RECURSO_TGI)) {
			psHeader += "%!\n";
			psHeader +="(tgi4.jdt)STARTLM\n";
		}
		
		if (recurso.getCodRecurso().equals(Recurso.COD_RECURSO_DReI)) {
			psHeader += StringUtil.fillWithBlanksToRight("%!",240);
			psHeader += '\n';
			psHeader += StringUtil.fillWithBlanksToRight("(drei01.jdt) STARTLM",240);
			psHeader += '\n';
		}
		
		return psHeader;
	}
		
	private File createPdfFile(String fileName, File xslFile, List<File> listXmlFile) 
		throws TransformerException, IOException, FOPException, Exception { 
		
		// Lista de fragmentos pdf del archivo principal
        List<File> listPdfChunks = new ArrayList<File>();
        
        //Agregado Leandro 14-12-10
        //Leo el archivo de metricas y fuentes -- soporte fuentes ttf
		File userConfigFile = new File("/mnt/publico/general/reportes/userconfig.xml");
		new Options(userConfigFile);
		//Fin Agregado Leandro
		
        for (File xmlFile: listXmlFile) {
			String unidad = File.separator.equals("\\") ? "c:\\" : "";
			String pdfChunkFileName = unidad + TMP_FILE_PREFIX + UUID.randomUUID()  + ".pdf";
			File pdfChunk = new File(pdfChunkFileName);
			
			BufferedOutputStream pdfChunkBuffer = new BufferedOutputStream(new FileOutputStream(pdfChunk));
			TraxInputHandler input = new TraxInputHandler(xmlFile, xslFile);
            Driver driver = new Driver();
            // Render PDF
            driver.setRenderer(1);
            driver.setOutputStream(pdfChunkBuffer);
            input.run(driver);
            pdfChunkBuffer.close();
                     
            // Agregamos el fragmento
            listPdfChunks.add(pdfChunk);
        }
		
        // Concatenamos los archivos
        File file = concatPdfFiles(listPdfChunks, fileName);
        
        // Borramos los fragmentos
        for (File pdfchunk: listPdfChunks) {
        	pdfchunk.delete();
        }
        
        return file;
	}
	
	/**
	 * Concatena la lista de pdfs pasada como parametro generando
	 * un archivo con nombre "filename".
	 * 
	 * @param  List<File> listPdfChunks
	 * @param  filename
	 * @return file 
	 */
	private static File concatPdfFiles(List<File> listPdfChunks, String filename) throws Exception {
    	log.debug("Concatenando archivos Pdf");
    	
    	File file = new File(filename);
    	PdfCopyFields mergedPdf = new PdfCopyFields(new FileOutputStream(file));
    	
    	for (File pdfchunk: listPdfChunks) {
    		String chunkname = pdfchunk.getAbsolutePath();
    		log.debug("Agregando archivo: " + chunkname);
			PdfReader reader = new PdfReader(chunkname);
    		mergedPdf.addDocument(reader);
    	}

    	mergedPdf.close();
    	
        log.debug("El archivo " + filename + " se ha generado con exito");
        
        return file;
    }

	/**
	 * Ordena las claves del mapa de un mapa. 
	 */
	private List<String> sortKeys(Set<String> keySet) {
		List<String> listKeys = new ArrayList<String>();
		for (String k: keySet) 
			listKeys.add(k);
		
		Collections.sort(listKeys);
		
		return listKeys;
	}
}
