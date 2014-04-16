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
import ar.gov.rosario.siat.def.buss.bean.DomAtrVal;
import ar.gov.rosario.siat.exe.buss.bean.Exencion;
import ar.gov.rosario.siat.exe.buss.bean.TipoSujeto;
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
 * de totales de la Emision Masiva de Deuda
 * 
 * @author Tecso
 *
 */
public class EmiMasTotReport extends Common {
	
	private static final String COD_EMI_MAS_TOT = "EMI_MAS_TOT";

	private static final long serialVersionUID = 1L;

	private Emision emision;
	
	private PrintModel printModel;
	
	private Map<String, Object[]> tab1Totales;
	
	private static final int TAB1_COL_CUENTAS = 0;
	
	private static final int TAB1_COL_CUENTAS_EXENTAS = 1;
	
	private static final int TAB1_COL_IMPORTE = 2;
	
	private static final int TAB1_COL_IMPORTE_EXENTAS = 3;

	private Map<String, Object[]> tab2Exenciones;

	private static final int TAB2_COL_CUENTAS = 0;
	
	private static final int TAB2_COL_IMPORTE = 1;

	// Mapa con las descripciones de las 
	// Exenciones
	private Map<String, String> descExencion;

	// Mapa con las descripciones de los 
	// Tipo Sujeto
	private Map<String, String> descTipoSujeto;
	

	// Mapa con las descripciones de los 
	// Tipo Sujeto
	private Map<String, String> descAtributosValores;

	
	private boolean abrirByAtrAsentamiento = false;

	//	 Constructor
	public EmiMasTotReport(Emision emision) throws Exception {
		this.emision = emision;
		this.printModel = Formulario.getPrintModelForPDF(COD_EMI_MAS_TOT);
	
		// Tablas del reporte
		this.tab1Totales = new HashMap<String, Object[]>();
		this.tab2Exenciones = new HashMap<String, Object[]>();
	
		Long idRecurso = this.emision.getRecurso().getId();
		this.descExencion = new HashMap<String, String>();
		for (Exencion exencion: Exencion.getListActivosByIdRecurso(idRecurso)) {
			descExencion.put(exencion.getCodExencion(), 
							 exencion.getDesExencion());
		}

		this.descTipoSujeto = new HashMap<String, String>();
		for (TipoSujeto tipoSujeto: TipoSujeto.getListActivos()) {
			descTipoSujeto.put(tipoSujeto.getCodTipoSujeto(), 
							   tipoSujeto.getDesTipoSujeto());
		}

		// Seteamos si debemos abrir o no por atributo de asentamiento
		Atributo atributo = this.emision.getRecurso().getAtributoAse();
		this.abrirByAtrAsentamiento = (atributo != null && atributo.getDomAtr() != null);
		
		if (abrirByAtrAsentamiento) {
			this.descAtributosValores = new HashMap<String, String>();
				for (DomAtrVal v: atributo.getDomAtr().getListDomAtrVal()) {
					this.descAtributosValores.put(v.getStrValor(), v.getDesValor());
			}
		}
		
		this.addHeaderData();
	}
	
	public synchronized void addReportData(AuxDeuda deuda) {
		this.addTablaTotalesEntry(deuda);

		if (deuda.getCodExencion() != null)
			this.addTablaExencionesEntry(deuda);
	}	

	private void addTablaTotalesEntry(AuxDeuda deuda) {
		// Construimos la Clave
		String key ="";

		if (abrirByAtrAsentamiento) {
			String atrAse = StringUtil.getXMLContentByTag(deuda.getAtrAseVal(), "AtrAse");
			atrAse += "-";
			key += atrAse;
		} 
				
		key += deuda.getAnio() + "-" + formatPeriodo(deuda.getPeriodo()) + "-";

		Object[] tabTotalesRow = tab1Totales.get(key);
		if (tabTotalesRow == null) {
			Object[] rowData = new Object[4];
			rowData[TAB1_COL_CUENTAS] = 0;
			rowData[TAB1_COL_CUENTAS_EXENTAS] = 0;
			rowData[TAB1_COL_IMPORTE] = 0D;
			rowData[TAB1_COL_IMPORTE_EXENTAS] = 0D;

			if (deuda.getCodExencion() == null) {
				rowData[TAB1_COL_CUENTAS] = 1;
				rowData[TAB1_COL_IMPORTE] = deuda.getImporte();
			} else {
				rowData[TAB1_COL_CUENTAS_EXENTAS] = 1;
				rowData[TAB1_COL_IMPORTE_EXENTAS] = deuda.getImporte();
			}
			tabTotalesRow = rowData;
		} else {
			if (deuda.getCodExencion() == null) {
				tabTotalesRow[TAB1_COL_CUENTAS] = ((Integer) tabTotalesRow[0]) + 1;
				tabTotalesRow[TAB1_COL_IMPORTE] = ((Double)  tabTotalesRow[2]) + deuda.getImporte();

			} else {
				tabTotalesRow[TAB1_COL_CUENTAS_EXENTAS] = ((Integer) tabTotalesRow[1]) + 1;
				tabTotalesRow[TAB1_COL_IMPORTE_EXENTAS] = ((Double)  tabTotalesRow[3]) + deuda.getImporte();
			}
		}
		
		tab1Totales.put(key, tabTotalesRow);
	}
	
	private void addTablaExencionesEntry(AuxDeuda deuda) {
		// Construimos la Clave
		String codExencion = deuda.getCodExencion();
		String codTipoSujeto = deuda.getCodTipoSujeto();
		String key = "";
		key += (codExencion   != null) ? codExencion   + "-" : "";
		key += (codTipoSujeto != null) ? codTipoSujeto + "-" : "vacio-";
		key += deuda.getAnio() + "-" + formatPeriodo(deuda.getPeriodo()) + "-";
		
		Double importe = deuda.getImporte();

		Object[] tabExencRow = tab2Exenciones.get(key); 
		if (tabExencRow == null) {
			Object[] rowData = new Object[3];
			rowData[TAB2_COL_CUENTAS] = 1;
			rowData[TAB2_COL_IMPORTE] = importe;
			tabExencRow = rowData;
		} else {
			tabExencRow[TAB2_COL_CUENTAS] = ((Integer) tabExencRow[TAB2_COL_CUENTAS]) + 1;
			tabExencRow[TAB2_COL_IMPORTE] = ((Double)  tabExencRow[TAB2_COL_IMPORTE]) + importe;
		}

		tab2Exenciones.put(key, tabExencRow);
	}

	
	private void addHeaderData() {
		// Datos del Encabezado Generico
		Date currentDate = new Date();
		String currentUser = this.emision.getUsuario();
		this.printModel.putCabecera("TituloReporte", "Reporte de Emision Masiva");
		this.printModel.putCabecera("Fecha", formatDate(currentDate));
		this.printModel.putCabecera("Hora" ,formatTime(currentDate));
		this.printModel.putCabecera("Usuario", currentUser);
		
		// Datos del Encabezado del PDF
		this.printModel.putCabecera("Recurso", this.emision.getRecurso().getDesRecurso());

		// Filtro por atributo de Segmentacion
		Atributo atrSegment =  this.emision.getAtributo();
		String atrSegmentValor =  this.emision.getValor();
		if (atrSegment != null && !StringUtil.isNullOrEmpty(atrSegmentValor)) {
			this.printModel.putCabecera("AtrSegment"	 , atrSegment.getDesAtributo());
			this.printModel.putCabecera("AtrSegmentValor", atrSegmentValor);
		}
		this.printModel.putCabecera("Anio", this.emision.getAnio().toString());
		this.printModel.putCabecera("PeriodoDesde", formatInteger(this.emision.getPeriodoDesde()));
		this.printModel.putCabecera("PeriodoHasta", formatInteger(this.emision.getPeriodoHasta()));
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

		TablaVO tabla = new TablaVO("ImportesTotales");
		tabla.setTitulo("Totales de Cuentas");
		
		FilaVO filaCabecera = new FilaVO();
		CeldaVO celdaVO;
		if (abrirByAtrAsentamiento) {
			celdaVO = new CeldaVO("Tipo","tipo");
			celdaVO.setWidth(20);
			filaCabecera.add(celdaVO);
		}
		celdaVO = new CeldaVO("Periodo","periodo");
		celdaVO.setWidth(16);
		filaCabecera.add(celdaVO);
		celdaVO = new CeldaVO("Cuentas S/Exec.","cuentas");
		celdaVO.setWidth(35);
		filaCabecera.add(celdaVO);
		celdaVO = new CeldaVO("Cuentas C/Exec.","cuentasExentas");
		celdaVO.setWidth(35);
		filaCabecera.add(celdaVO);
		celdaVO = new CeldaVO("Importe Cuentas S/Exec.","importe");
		celdaVO.setWidth(36);
		filaCabecera.add(celdaVO);
		celdaVO = new CeldaVO("Importe Cuentas C/Exec.","importeExento");
		celdaVO.setWidth(36);
		filaCabecera.add(celdaVO);
		tabla.setFilaCabecera(filaCabecera);
		
		Double totalImpSExc = 0D;
		Double totalImpCExc = 0D;
		for(String key: sortKeys(tab1Totales.keySet())){
			FilaVO fila = new FilaVO();
			String[] keyColumns = key.split("-");
			if (abrirByAtrAsentamiento) {
				fila.add(new CeldaVO(descAtributosValores.get(keyColumns[0]),"tipo"));
				String perAnio = keyColumns[2] + "/" + keyColumns[1];
				fila.add(new CeldaVO(perAnio,"periodo"));			
			} else {
				String perAnio = keyColumns[1] + "/" + keyColumns[0];
				fila.add(new CeldaVO(perAnio,"periodo"));			
			}
			Object[] rowData = tab1Totales.get(key);
			fila.add(new CeldaVO(formatInteger((Integer) rowData[0]) ,"cuentas"));
			fila.add(new CeldaVO(formatInteger((Integer) rowData[1]) ,"cuentasExentas"));
			totalImpSExc += (Double) rowData[2];
			totalImpCExc += (Double) rowData[3];
			fila.add(new CeldaVO("$ " + formatDouble((Double) 	 rowData[2]) ,"importe"));
			fila.add(new CeldaVO("$ " + formatDouble((Double) 	 rowData[3]) ,"importeExento"));
			tabla.add(fila);
		}
		
		FilaVO filaPie = new FilaVO();
		filaPie.add(new CeldaVO("",""));
		filaPie.add(new CeldaVO("",""));
		filaPie.add(new CeldaVO("",""));
		filaPie.add(new CeldaVO("",""));
		filaPie.add(new CeldaVO(formatDouble(totalImpSExc) ,"totalImpSExc"));
		filaPie.add(new CeldaVO(formatDouble(totalImpCExc)	,"totalImpCExc"));
		tabla.addFilaPie(filaPie);

		contenedor.add(tabla);
		
		TablaVO tabla2 = new TablaVO("Exenciones");
		tabla2.setTitulo("Totales por Exencion");
		
		filaCabecera = new FilaVO();
		celdaVO = new CeldaVO("Exencion","exencion");
		celdaVO.setWidth(75);
		filaCabecera.add(celdaVO);
		celdaVO = new CeldaVO("TipoSujeto","tipoSujeto");
		celdaVO.setWidth(35);
		filaCabecera.add(celdaVO);
		celdaVO = new CeldaVO("Periodo","periodo");
		celdaVO.setWidth(16);
		filaCabecera.add(celdaVO);
		celdaVO = new CeldaVO("Cuentas","cuentas");
		celdaVO.setWidth(27);
		filaCabecera.add(celdaVO);
		celdaVO = new CeldaVO("Importe","importe");
		filaCabecera.add(celdaVO);
		celdaVO.setWidth(27);
		tabla2.setFilaCabecera(filaCabecera);
		
		Double total = 0D;
		for(String key: sortKeys(tab2Exenciones.keySet())){
			FilaVO fila = new FilaVO();
			String[] keyColumns = key.split("-");
			fila.add(new CeldaVO(descExencion.get(keyColumns[0]),"exencion"));
			fila.add(new CeldaVO(descTipoSujeto.get(keyColumns[1]),"tipoSujeto"));
			String perAnio = keyColumns[3] + "/" + keyColumns[2];
			fila.add(new CeldaVO(perAnio,"periodo"));
			Object[] rowData = tab2Exenciones.get(key);
			Integer cuentas = (Integer) rowData[0];
			fila.add(new CeldaVO(formatInteger(cuentas) ,"cuentas"));
			Double importe = (Double) rowData[1];
			total += importe;
			fila.add(new CeldaVO("$ " + formatDouble(importe) ,"importe"));
			tabla2.add(fila);
		}
		
		filaPie = new FilaVO();
		filaPie.add(new CeldaVO("",""));
		filaPie.add(new CeldaVO("",""));
		filaPie.add(new CeldaVO("",""));
		filaPie.add(new CeldaVO("",""));
		filaPie.add(new CeldaVO(formatDouble(total)	,"total"));
		tabla2.addFilaPie(filaPie);
		
		contenedor.add(tabla2);

		return contenedor;
	}	

	// Metodos Auxiliares

	private String formatPeriodo(Long p) {
		return (p < 10L) ? "0" + p: "" + p;
	}

	private String formatInteger(Integer n) {
		return StringUtil.formatInteger(n);
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

	private List<String> sortKeys (Set<String> keySet) {
		List<String> listKeys = new ArrayList<String>();
		for (String k: keySet) {
			listKeys.add(k);
		}
		Collections.sort(listKeys);
		
		return listKeys;
	}

}
