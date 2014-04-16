//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del Calendario
 * 
 * @author Tecso
 *
 */
public class CalendarioSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "calendarioSearchPageVO";
	
	private CalendarioVO calendario= new CalendarioVO();
	
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();  
	private Date fechaDesde;
	private Date fechaHasta;
	
	private String fechaDesdeView="";
	private String fechaHastaView="";
	
	// Constructores
	public CalendarioSearchPage() {       
       super(DefSecurityConstants.ABM_CALENDARIO);        
    }
	
	// Getters y Setters
	public CalendarioVO getCalendario() {
		return calendario;
	}
	public void setCalendario(CalendarioVO calendario) {
		this.calendario = calendario;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
		this.fechaDesdeView = DateUtil.formatDate(fechaDesde, "dd/MM/yyyy");
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
		this.fechaHastaView = DateUtil.formatDate(fechaHasta, "dd/MM/yyyy");
	}

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	//	 View getters
	public String getFechaDesdeView() {
		return fechaDesdeView;
	}

	public void setFechaDesdeView(String fechaDesdeView) {
		this.fechaDesdeView = fechaDesdeView;
	}

	public String getFechaHastaView() {
		return fechaHastaView;
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
		report.setReportTitle("Reporte de Calendarios de Vencimientos");
		report.setReportBeanName("CalendariosVencimientos");
		report.setReportFileName(this.getClass().getName());
		 
		 // carga de filtros
		 String desRecurso = "";
		 RecursoVO recursoVO = (RecursoVO) ModelUtil.getBussImageModelByIdForList(
				 this.getCalendario().getRecurso().getId(), 
				 this.getListRecurso());
		 if (recursoVO != null){
			 desRecurso = recursoVO.getDesRecurso();
		 }
		 boolean recursoSeleccionado = !ModelUtil.isNullOrEmpty(this.getCalendario().getRecurso());
		 
		 report.addReportFiltro("Recurso", desRecurso);
		 report.addReportFiltro("Fecha Desde", this.getFechaDesdeView());
		 report.addReportFiltro("Fecha Hasta", this.getFechaHastaView());
		 
		 // Order by
		 if (recursoSeleccionado){
			 report.setReportOrderBy("fechaVencimiento ASC");
		 }else{
			 report.setReportOrderBy("recurso.desRecurso, fechaVencimiento ASC");
		 }
		 
	     ReportTableVO rtCalendario = new ReportTableVO("Calendario");
	     rtCalendario.setTitulo("Listado de Calendarios de Vencimientos");
	     
		 // carga de columnas
	     if (!recursoSeleccionado){
	    	 rtCalendario.addReportColumn("Recurso", "recurso.desRecurso");
	     }
	     rtCalendario.addReportColumn("Fecha Vto.", "fechaVencimiento");
	     rtCalendario.addReportColumn("Periodo", "periodo");
	     rtCalendario.addReportColumn("Zona", "zona.descripcion");
	     rtCalendario.addReportColumn("Estado", "estadoView");
	     
	     report.getReportListTable().add(rtCalendario);
	}

}
