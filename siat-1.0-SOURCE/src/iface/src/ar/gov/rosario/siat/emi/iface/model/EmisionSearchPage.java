//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.emi.iface.util.EmiSecurityConstants;
import ar.gov.rosario.siat.pro.iface.model.EstadoCorridaVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del Emision
 * 
 * @author Tecso
 *
 */
public class EmisionSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "emisionSearchPageVO";
	
	private EmisionVO emision= new EmisionVO();
	
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	private List<EstadoCorridaVO> listEstadoCorrida = new ArrayList<EstadoCorridaVO>();	
	
	private Date 	fechaDesde; 
	private Date 	fechaHasta; 
	private String 	fechaDesdeView = "";
	private String 	fechaHastaView = "";
	
	private boolean mostrarEstadoCorrida = true;
	
	// Constructores
	public EmisionSearchPage(String strSecurityConstant) {       
       super(strSecurityConstant);        
    }
	
	// Getters y Setters
	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	public List<EstadoCorridaVO> getListEstadoCorrida() {
		return listEstadoCorrida;
	}

	public void setListEstadoCorrida(List<EstadoCorridaVO> listEstadoCorrida) {
		this.listEstadoCorrida = listEstadoCorrida;
	}

	public EmisionVO getEmision() {
		return emision;
	}
	public void setEmision(EmisionVO emision) {
		this.emision = emision;
	}
	public Date getFechaDesde() {
		return fechaDesde;
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

	public boolean getMostrarEstadoCorrida() {
		return mostrarEstadoCorrida;
	}

	public void setMostrarEstadoCorrida(boolean mostrarEstadoCorrida) {
		this.mostrarEstadoCorrida = mostrarEstadoCorrida;
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
	
	public String getAdministrarProcesoEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(EmiSecurityConstants.ABM_EMISION_CDM, EmiSecurityConstants.MTD_ADMINISTRAR_PROCESO);
	}
	
	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Consulta de Emisión");
		 report.setReportBeanName("Emision");
		 report.setReportFileName(this.getClass().getName());
		 
		 // carga de filtros
		 // recurso
		 String desRecurso = "";
			
		 RecursoVO recursoVO = (RecursoVO) ModelUtil.getBussImageModelByIdForList(
				 this.getEmision().getRecurso().getId(),
				 this.getListRecurso());
		 if (recursoVO != null){
			 desRecurso = recursoVO.getDesRecurso();
		 }
		 report.addReportFiltro("Recurso", desRecurso);
		 
		 // Fecha Desde
		 report.addReportFiltro("Fecha Desde", this.getFechaDesdeView());
	     
		// Fecha Hasta
		 report.addReportFiltro("Fecha Hasta", this.getFechaHastaView());
	     
        
		 // Order by
		 report.setReportOrderBy("fechaEmision DESC, t.fechaUltMdf DESC");
	     
	     ReportTableVO rtEmi = new ReportTableVO("rtEmi");
	     rtEmi.setTitulo("Listado de Emisión");
	   
	     // carga de columnas
	     rtEmi.addReportColumn("Fecha Emisión", "fechaEmision");
	     rtEmi.addReportColumn("Recurso", "recurso.desRecurso");
	     rtEmi.addReportColumn("Estado Corrida", "corrida.estadoCorrida.desEstadoCorrida");
	     rtEmi.addReportColumn("Estado", "estadoView");
	     report.getReportListTable().add(rtEmi);

	    }

}
