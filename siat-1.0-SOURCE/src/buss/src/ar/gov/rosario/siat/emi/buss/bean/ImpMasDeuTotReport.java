//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.buss.bean;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
 * de totales durante Impresion Masiva de Deuda
 * 
 * @author Tecso
 *
 */
public class ImpMasDeuTotReport extends Common {
	
	private static final String COD_IMP_MAS_DEU_TOT = "IMP_MAS_DEU_TOT";

	private static final long serialVersionUID = 1L;

	private ImpMasDeu impMasDeu;
	
	private PrintModel printModel;
	
	private Map<String,Integer> fileRegs;
	
	// Constructor
	public ImpMasDeuTotReport(ImpMasDeu impMasDeu) throws Exception {
		this.impMasDeu = impMasDeu;
		this.printModel = Formulario.getPrintModelForPDF(COD_IMP_MAS_DEU_TOT);
		this.fileRegs = new HashMap<String, Integer>();
		
		this.addHeaderData();
	}
	
	public synchronized void addReportData(String fileName, String broche) {
		String key = fileName +"-"+ broche;
		Integer r = fileRegs.get(key);
		if (r == null) {
			fileRegs.put(key, 1);
		} else {
			fileRegs.put(key, r + 1);
		}
	}	

	private void addHeaderData() {
		// Datos del Encabezado Generico
		Date currentDate = new Date();
		String currentUser = this.impMasDeu.getUsuario();
		this.printModel.putCabecera("TituloReporte", "Impresi\u00F3n Masiva de Deuda: Totales");
		this.printModel.putCabecera("Fecha", formatDate(currentDate));
		this.printModel.putCabecera("Hora" ,formatTime(currentDate));
		this.printModel.putCabecera("Usuario", currentUser);
		
		// Datos del Encabezado del PDF
		this.printModel.putCabecera("Recurso", this.impMasDeu.getRecurso().getDesRecurso());

		// Filtro por atributo de Segmentacion
		Atributo atrSegment =  this.impMasDeu.getAtributo();
		String atrSegmentValor =  this.impMasDeu.getAtrValor();
		if (atrSegment != null && !StringUtil.isNullOrEmpty(atrSegmentValor)) {
			this.printModel.putCabecera("AtrSegment"	 , atrSegment.getDesAtributo());
			this.printModel.putCabecera("AtrSegmentValor", atrSegmentValor);
		}
		this.printModel.putCabecera("Anio", this.impMasDeu.getAnio().toString());
		this.printModel.putCabecera("PeriodoDesde", formatInteger(this.impMasDeu.getPeriodoDesde()));
		this.printModel.putCabecera("PeriodoHasta", formatInteger(this.impMasDeu.getPeriodoHasta()));
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
		
		TablaVO tabla = new TablaVO("RegistrosTotales");
		tabla.setTitulo("Registros por archivo");
		
		FilaVO filaCabecera = new FilaVO();
		filaCabecera.add(new CeldaVO("Archivo","archivo"));
		filaCabecera.add(new CeldaVO("Nro. Broche","broche"));
		filaCabecera.add(new CeldaVO("Recibos","recibos"));
		tabla.setFilaCabecera(filaCabecera);

		Integer totalRegistros = 0;
		boolean addLeyendaBroche = true;
		String brocheActual = "";
		for(String fileName: sortKeys(fileRegs.keySet())) {
			String [] s = fileName.split("-");
			addLeyendaBroche = !(s[0].equals(brocheActual)); 
			brocheActual = s[0];
			FilaVO fila = new FilaVO();
			String fname = impMasDeu.getReciboFileName(s[0]);
			fila.add(new CeldaVO(addLeyendaBroche ? fname :"" ,"archivo"));
			fila.add(new CeldaVO(StringUtil.fillWithCharToLeft(s[1],'0',4) ,"broche"));
			Integer registros = fileRegs.get(fileName);
			fila.add(new CeldaVO(formatInteger(registros) ,"recibos"));
			totalRegistros += registros;
			tabla.add(fila);
		}

		FilaVO filaPie = new FilaVO();
		filaPie.add(new CeldaVO("Total","total"));
		filaPie.add(new CeldaVO("",""));
		filaPie.add(new CeldaVO(formatInteger(totalRegistros) ,"totalRecibos"));
		tabla.addFilaPie(filaPie);

		contenedor.add(tabla);

		return contenedor;
	}	

	// Metodos Auxiliares
	private String formatInteger(Integer n) {
		return StringUtil.formatInteger(n);
	}
	
	private String formatDate(Date date) {
		return DateUtil.formatDate(date,DateUtil.ddSMMSYYYY_MASK);
	}

	private String formatTime(Date date) {
		return DateUtil.formatDate(date, "HH:mm:ss");
	}
	
	private List<String> sortKeys(Set<String> keySet) {
		List<String> listKeys = new ArrayList<String>();
		for (String k: keySet) 
			listKeys.add(k);
		
		Collections.sort(listKeys);
		
		return listKeys;
	}

}
