//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 *  Representa un reporte.
 *   
 * @author Tecso
 *
 */
public class ReportVO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public static double PAGE_HEIGHT = 29.7D; // altura estandar A4
	public static double PAGE_WIDTH  = 21.0D; // ancho estandar A4
	
    private Boolean imprimir = false; // bandera que se utiliza para imprimir o no 
    private String reportTitle = "";
    private String reportBeanName = "";
    private Long   reportFormat = 1L;
    private String reportOrderBy = "";
    private String reportFileName = ""; 
    private String reportFileSharePath = "";  
    private String reportFileDir = "/tmp";
    private String reportFileNamePdf = "";
    private String imprimirDetalle = "";
    private String reportFiltrosTitle = "";
    private Map<String,String> reportFiltros = new LinkedHashMap<String,String>();   // "nombre filtro" , "metodos a invocar"
    
    //private Map<String,String> reportDatos = new LinkedHashMap<String,String>();   // "nombre columna", "metodos a invocar"
    
    private List<ReportDatoVO> reportListDato = new ArrayList<ReportDatoVO>();
    
    private List<ReportTableVO> reportListTable = new ArrayList<ReportTableVO>();
    
    private Long reportCtdMaxRes     = 5000L;

    private String reportMetodo = "";  // metodo a invocar para obtener el contenido de la lista del page model.
    private List<ReportVO> listReport = new ArrayList<ReportVO>();
    
    private double pageHeight= PAGE_HEIGHT; 
    private double pageWidth = PAGE_WIDTH;
    
    private String AlterXSL="";

	public ReportVO(){
	}

	// Getters Y Setters
	//public Map<String, String> getReportDatos() {
	//	return reportDatos;
	//}
	//public void setReportDatos(Map<String, String> reportDato) {
	//	this.reportDatos = reportDato;
	//}
	
	public void addReportDato(String nombre, String value) {
		//this.reportDatos.put(nombre, value);
		ReportDatoVO reportDato = new ReportDatoVO(nombre,value);
		this.getReportListDato().add(reportDato);
	}
	public void addReportDato(String nombre, String value,Class claseEnumeracion) {
		//this.reportDatos.put(nombre, value);
		ReportDatoVO reportDato = new ReportDatoVO(nombre,value);
		reportDato.setClaseEnumeracion(claseEnumeracion);
		this.getReportListDato().add(reportDato);
	}
	
	
	public List<ReportDatoVO> getReportListDato() {
		return reportListDato;
	}
	public void setReportListDato(List<ReportDatoVO> reportListDato) {
		this.reportListDato = reportListDato;
	}

	public Long getReportFormat() {
		return reportFormat;
	}
	public void setReportFormat(Long reportFormat) {
		this.reportFormat = reportFormat;
	}
	
	public String getReportOrderBy() {
		return reportOrderBy;
	}
	public void setReportOrderBy(String reportOrderBy) {
		this.reportOrderBy = reportOrderBy;
	}
	public String getReportTitle() {
		return reportTitle;
	}
	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}
	public String getReportFileName() {
		return reportFileName;
	}
	public void setReportFileName(String reportFileName) {
		this.reportFileName = reportFileName;
	}
	public Boolean getImprimir() {
		return imprimir;
	}
	public void setImprimir(Boolean imprimir) {
		this.imprimir = imprimir;
	}
	
	public Map<String, String> getReportFiltros() {
		return reportFiltros;
	}
	public void setReportFiltros(Map<String, String> reportFiltros) {
		this.reportFiltros = reportFiltros;
	}
	public void addReportFiltro(String nombre, String value) {
		this.reportFiltros.put(nombre, value);
	}
	public List<ReportVO> getListReport() {
		return listReport;
	}
	public void setListReport(List<ReportVO> listReport) {
		this.listReport = listReport;
	}
	public String getReportMetodo() {
		return reportMetodo;
	}
	public void setReportMetodo(String reportMetodo) {
		this.reportMetodo = reportMetodo;
	}

	public String getReportBeanName() {
		return reportBeanName;
	}
	public void setReportBeanName(String reportBeanName) {
		this.reportBeanName = reportBeanName;
	}
	public Long getReportCtdMaxRes() {
		return reportCtdMaxRes;
	}
	public void setReportCtdMaxRes(Long reportCtdMaxRes) {
		this.reportCtdMaxRes = reportCtdMaxRes;
	}
	public String getReportFileSharePath() {
		return reportFileSharePath;
	}
	public void setReportFileSharePath(String reportFileSharePath) {
		this.reportFileSharePath = reportFileSharePath;
	}
	public List<ReportTableVO> getReportListTable() {
		return reportListTable;
	}
	public void setReportListTable(List<ReportTableVO> reportListTable) {
		this.reportListTable = reportListTable;
	}
	public String getReportFiltrosTitle() {
		return reportFiltrosTitle;
	}
	public void setReportFiltrosTitle(String reportFiltrosTitle) {
		this.reportFiltrosTitle = reportFiltrosTitle;
	}
	public double getPageHeight() {
		return pageHeight;
	}
	public void setPageHeight(double pageHeight) {
		this.pageHeight = pageHeight;
	}

	public double getPageWidth() {
		return pageWidth;
	}
	public void setPageWidth(double pageWidth) {
		this.pageWidth = pageWidth;
	}
	public String getReportFileDir() {
		return reportFileDir;
	}
	public void setReportFileDir(String reportFileDir) {
		this.reportFileDir = reportFileDir;
	}
	public String getReportFileNamePdf() {
		return reportFileNamePdf;
	}
	public void setReportFileNamePdf(String reportFileNamePdf) {
		this.reportFileNamePdf = reportFileNamePdf;
	}
	
	public String getPathCompletoArchivoXsl(){
		
		if(this.AlterXSL.equals("")) return this.getReportFileSharePath() + "/publico/general/reportes/pageModel.xsl";
		else return this.getReportFileSharePath() + this.AlterXSL;
	} 

	public String getAlterXSL() {
		return AlterXSL;
	}

	public void setAlterXSL(String alterXSL) {
		AlterXSL = alterXSL;
	}
	
	
	/**
	 * Limpia la lista de Reports y la lista de Tables que contiene
	 *
	 */
	public void clear(){
		this.getListReport().clear();
		this.getReportListTable().clear();
		this.getReportListDato().clear();
	}

	public String getImprimirDetalle() {
		return imprimirDetalle;
	}

	public void setImprimirDetalle(String imprimirDetalle) {
		this.imprimirDetalle = imprimirDetalle;
	}
	
}
