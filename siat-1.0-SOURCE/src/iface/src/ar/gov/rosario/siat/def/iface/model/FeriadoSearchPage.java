//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

public class FeriadoSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "feriadoSearchPageVO";
	
	private Date 	fechaDesde; 
	private Date 	fechaHasta; 
	private String  desFeriado;
	
	private String 	fechaDesdeView = "";
	private String 	fechaHastaView = "";
	
	public FeriadoSearchPage() {       
       super(DefSecurityConstants.ABM_FERIADO);
        
    }
	
	// Getters y Setters
	public Date getFechaDesde() {
		return fechaDesde;
	}
	
	public String getDesFeriado() {
		return desFeriado;
	}

	public void setDesFeriado(String desFeriado) {
		this.desFeriado = desFeriado;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
		this.fechaDesdeView = DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK);
	}
	public Date getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
		this.fechaHastaView = DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK);
	}

	//	 View getters
	public String getFechaDesdeView() {
		return fechaDesdeView;
	}
	public String getFechaHastaView() {
		return fechaHastaView;
	}

	public void setFechaDesdeView(String fechaDesdeView) {
		this.fechaDesdeView = fechaDesdeView;
	}

	public void setFechaHastaView(String fechaHastaView) {
		this.fechaHastaView = fechaHastaView;
	}

	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de Feriados");
		 report.setReportBeanName("Feriados");
		 report.setReportFileName(this.getClass().getName());
		 
		 // carga de filtros
		 
		 report.addReportFiltro("Fecha Desde", this.getFechaDesdeView());
		 report.addReportFiltro("Fecha Hasta", this.getFechaHastaView());
		 report.addReportFiltro("Descripción", this.getDesFeriado());
		 
		 // Order by
		 report.setReportOrderBy("fechaFeriado ASC");
		 
	     ReportTableVO rtFeriado = new ReportTableVO("Feriado");
	     rtFeriado.setTitulo("Listado de Feriados");
	     
		 // carga de columnas
	     rtFeriado.addReportColumn("Fecha", "fechaFeriado");
	     rtFeriado.addReportColumn("Descripción", "desFeriado");
	     rtFeriado.addReportColumn("Estado", "estadoView");
	     
	     report.getReportListTable().add(rtFeriado);
	}


}