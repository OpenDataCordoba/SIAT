//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.model;

import java.util.ArrayList;
import java.util.List;


/**
 *  Representa una tabla de un reporte.
 *   
 * @author Tecso
 *
 */
public class ReportTableVO {
	
    // nombre que lo identifica
	private String nombre = "";  
	
	private String titulo = "";

	// columnas de la tabla
	//private Map<String,String[]>   reportColumns = new LinkedHashMap<String,String[]>();   // "nombre columna", "metodos a invocar"
	private List<ReportColumnVO> listReportColumns = new ArrayList<ReportColumnVO>();
	
    // pies de cada columna de la tabla
	//private Map<String,ReportPieVO> reportPies = new LinkedHashMap<String,ReportPieVO>(); // "nombre columna", ReportPie{"metodos a invocar","SUM o COUNT"}

	// metodo a ejecutar para obtener la lista del contenido de la tabla
	private String reportMetodo;
	
	public ReportTableVO(String nombre){
		this.nombre    = nombre;
	}

	// Getters Y Setters
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public List<ReportColumnVO> getListReportColumns() {
		return listReportColumns;
	}
	public void setListReportColumns(List<ReportColumnVO> listReportColumns) {
		this.listReportColumns = listReportColumns;
	}
	public void addReportColumn(ReportColumnVO reportColumnVO) {
		this.listReportColumns.add(reportColumnVO);
	}
	public ReportColumnVO addReportColumn(String nombreColumna, String[] metodosEjecutar) {
		ReportColumnVO rp = new ReportColumnVO(nombreColumna, metodosEjecutar);
		this.addReportColumn(rp);
		return rp;
	}
	public ReportColumnVO addReportColumn(String nombreColumna, String metodoEjecutar) {
		ReportColumnVO rp = new ReportColumnVO(nombreColumna, metodoEjecutar);
		this.addReportColumn(rp);
		return rp;
	}
	public ReportColumnVO addReportColumn(String nombreColumna, String metodoEjecutar, Integer width) {
		ReportColumnVO rp = new ReportColumnVO(nombreColumna, metodoEjecutar, width);
		this.addReportColumn(rp);
		return rp;
	}
	
	public ReportColumnVO addReportColumn(String nombreColumna, String metodoEjecutar, Class claseEnumeracion) {
		ReportColumnVO rp = new ReportColumnVO(nombreColumna, metodoEjecutar);
		rp.setClaseEnumeracion(claseEnumeracion);
		this.addReportColumn(rp);
		return rp;
	}
	

	/*
	public void addReportPie(ReportPieVO reportPie) {
		this.reportPies.put(reportPie.getNombre(), reportPie);
	}
	public void addReportPie(String nombre, Integer accion) {
		this.addReportPie(new ReportPieVO(nombre, accion));
	}
	public void addReportPie(String nombre, String propiedad, Integer accion) {
		this.addReportPie(new ReportPieVO(nombre, propiedad, accion));
	}
	*/
	
	public String getReportMetodo() {
		return reportMetodo;
	}
	public void setReportMetodo(String reportMetodo) {
		this.reportMetodo = reportMetodo;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	
}
