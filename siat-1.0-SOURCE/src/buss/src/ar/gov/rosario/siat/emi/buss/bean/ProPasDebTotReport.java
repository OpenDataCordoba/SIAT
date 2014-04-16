//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.buss.bean;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import ar.gov.rosario.siat.def.buss.bean.Atributo;
import ar.gov.rosario.siat.frm.buss.bean.Formulario;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CeldaVO;
import coop.tecso.demoda.iface.model.Common;
import coop.tecso.demoda.iface.model.ContenedorVO;
import coop.tecso.demoda.iface.model.FilaVO;
import coop.tecso.demoda.iface.model.PrintModel;
import coop.tecso.demoda.iface.model.TablaVO;

/**
 * Clase auxiliar para la generacion del reporte   
 * de totales del Proceso de Pago Automatico de
 * Servicios y Debito
 * 
 * @author Tecso
 *
 */
public class ProPasDebTotReport extends Common {
	
	private static final String COD_PRO_PAS_DEB_TOT = "PRO_PAS_DEB_TOT";

	private static final long serialVersionUID = 1L;

	private ProPasDeb proPasDeb;
	
	private PrintModel printModel;
	
	private LinkFile linkFile;
	
	private BanelcoFile banelcoFile;
	
	private DebitoFile debitoFile;
	
	//	 Constructor
	public ProPasDebTotReport(ProPasDeb proPasDeb) throws Exception {
		this.proPasDeb = proPasDeb;
		this.printModel = Formulario.getPrintModelForPDF(COD_PRO_PAS_DEB_TOT);
		
		this.addHeaderData();
	}
	
	public synchronized void addReportData(LinkFile linkFile, 
			BanelcoFile banelcoFile, DebitoFile debitoFile) {

		this.linkFile = linkFile;
		this.banelcoFile = banelcoFile;
		this.debitoFile = debitoFile;
	}	

	private void addHeaderData() {
		// Datos del Encabezado Generico
		Date currentDate = new Date();
		String currentUser = this.proPasDeb.getUsuario();
		this.printModel.putCabecera("TituloReporte", "Pago Automatico de Servicios y Debito");
		this.printModel.putCabecera("Fecha", formatDate(currentDate));
		this.printModel.putCabecera("Hora" ,formatTime(currentDate));
		this.printModel.putCabecera("Usuario", currentUser);
		
		// Datos del Encabezado del PDF
		this.printModel.putCabecera("Recurso", this.proPasDeb.getRecurso().getDesRecurso());

		// Filtro por atributo de Segmentacion
		Atributo atrSegment =  this.proPasDeb.getAtributo();
		String atrSegmentValor =  this.proPasDeb.getAtrValor();
		if (atrSegment != null && !StringUtil.isNullOrEmpty(atrSegmentValor)) {
			this.printModel.putCabecera("AtrSegment"	 , atrSegment.getDesAtributo());
			this.printModel.putCabecera("AtrSegmentValor", atrSegmentValor);
		}
		this.printModel.putCabecera("Anio", this.proPasDeb.getAnio().toString());
		this.printModel.putCabecera("Periodo", formatInteger(this.proPasDeb.getPeriodo()));

	}
	
	/**
	 * Crea el reporte PDF con el nombre pasado como parametro
	 * 
	 * @throws Exception 
	 * */
	public File createReport(String fileName) throws Exception {
		try {
			this.printModel.setData(this.getContenedor());
			this.printModel.setTopeProfundidad(5);
			
			byte[] pdfByteStream = this.printModel.getByteArray();
			File report = new File(fileName);
			FileOutputStream pdfFile = new FileOutputStream(report);
			pdfFile.write(pdfByteStream);
			pdfFile.close();
			
			return report;
		} catch (Exception e) {
			String desException = "No se pudo crear el reporte " + fileName;
			e.printStackTrace();
			throw new Exception(desException);
		}
	}

	// Getters y Setters
	private ContenedorVO getContenedor() throws Exception {

		// Se arma un contenedor de tablas para guardas los datos.
		ContenedorVO contenedor = new ContenedorVO("Contenedor");

		Date currentDate = new Date();
		File file;
		Integer cantRegs;
		Double importeTotal;
		Double importeTotalAct;
		Date fecUltVenc;
		
		TablaVO tabla1 = new TablaVO("resumenLink");
		tabla1.setTitulo("Archivo Link");
		
		LinkFile linkFile = this.linkFile;
		FilaVO fila = new FilaVO();
		fila.add(new CeldaVO(linkFile.getFileName(),"nombre", "Nombre"));
		fila.add(new CeldaVO(formatDate(currentDate),"fecha",  "Fecha"));
		file = new File(linkFile.getAbsolutePath());
		fila.add(new CeldaVO(formatLong(file.length()),"longitud","Longitud (bytes)"));
		cantRegs = linkFile.getCantidadRegistros();
		fila.add(new CeldaVO(formatInteger(cantRegs),"cantRegs","Cant. Registros"));
		importeTotal = linkFile.getImporteTotal();
		fila.add(new CeldaVO(formatDouble(importeTotal),"importeTotal","Importe Pri. Venc."));
		importeTotalAct = linkFile.getImporteTotalAct();
		fila.add(new CeldaVO(formatDouble(importeTotalAct),"importeTotalAct","Importe Seg. Venc."));
		fecUltVenc = linkFile.getFecUltVenc();
		fila.add(new CeldaVO(formatDate(fecUltVenc),"fecUltVenc","Fecha Ult. Vencimiento"));
		tabla1.add(fila);
		
		contenedor.add(tabla1);
		
		TablaVO tabla2 = new TablaVO("resumenBanelco");
		tabla2.setTitulo("Archivo Banelco");
		
		BanelcoFile banelcoFile = this.banelcoFile;
		fila = new FilaVO();
		fila.add(new CeldaVO(banelcoFile.getFileName(),"nombre", "Nombre"));
		fila.add(new CeldaVO(formatDate(currentDate),"fecha",  "Fecha"));
		file = new File(banelcoFile.getAbsolutePath());
		fila.add(new CeldaVO(formatLong(file.length()),"longitud","Longitud (bytes)"));
		cantRegs = banelcoFile.getCantidadRegistros();
		fila.add(new CeldaVO(formatInteger(cantRegs),"cantRegs","Cant. Registros"));
		importeTotal = banelcoFile.getImporteTotal();
		fila.add(new CeldaVO(formatDouble(importeTotal),"importeTotal","Importe Pri. Venc."));
		importeTotalAct = banelcoFile.getImporteTotalAct();
		fila.add(new CeldaVO(formatDouble(importeTotalAct),"importeTotalAct","Importe Seg. Venc."));
		fecUltVenc = banelcoFile.getFecUltVenc();
		fila.add(new CeldaVO(formatDate(fecUltVenc),"fecUltVenc","Fecha Ult. Vencimiento"));
		tabla2.add(fila);
		
		contenedor.add(tabla2);

		TablaVO tabla3 = new TablaVO("resumenDebito");
		tabla3.setTitulo("Archivo de Debitos");
		
		DebitoFile debitoFile = this.debitoFile;
		fila = new FilaVO();
		fila.add(new CeldaVO(debitoFile.getFileName(),"nombre", "Nombre"));
		cantRegs = debitoFile.getTotoalRegs();
		fila.add(new CeldaVO(formatInteger(cantRegs),"cantRegs","Cant. Registros"));
		tabla3.add(fila);
		
		contenedor.add(tabla3);

		
		return contenedor;
	}	

	// Metodos Auxiliares
	private String formatInteger(Integer n) {
		return StringUtil.formatInteger(n);
	}
	
	private String formatLong(Long l) {
		return StringUtil.formatLong(l);
	}

	private String formatDouble(Double d) {
		return StringUtil.formatDouble(d, "###############.##");
	}
	
	private String formatDate(Date date) {
		return DateUtil.formatDate(date,DateUtil.ddSMMSYYYY_MASK);
	}

	private String formatTime(Date date) {
		return DateUtil.formatDate(date, "HH:mm:ss");
	}

}
