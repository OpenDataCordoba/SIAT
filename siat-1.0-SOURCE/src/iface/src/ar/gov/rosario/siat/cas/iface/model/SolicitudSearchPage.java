//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.cas.iface.util.CasSecurityConstants;
import ar.gov.rosario.siat.def.iface.model.AreaVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del Solicitud
 * 
 * @author Tecso
 *
 */
public class SolicitudSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "solicitudSearchPageVO";
	
	private SolicitudVO solicitud = new SolicitudVO();
	
	// listaas para los filtros
    private List<TipoSolicitudVO> listTipoSolicitud = new ArrayList<TipoSolicitudVO>();
    private List<AreaVO> listArea = new ArrayList<AreaVO>();
    private List<EstSolicitudVO> listEstSolicitud = new ArrayList<EstSolicitudVO>();
        
	private Date 	fechaDesde; 
	private Date 	fechaHasta; 
	
	private String 	fechaDesdeView = "";
	private String 	fechaHastaView = "";
	private String  desAreaLog = "";              
	private boolean estaEnSolPendiente=false;
	private boolean estaEnSolEmitidas=false;
	private boolean mostrarFiltro=true;
	
	private Boolean cambiarEstadoBussEnabled=true;	
	
	// Constructores
	public SolicitudSearchPage() {       
       super(CasSecurityConstants.ABM_SOLICITUD);        
    }
	
	// Getters y Setters
	public SolicitudVO getSolicitud() {
		return solicitud;
	}
	public void setSolicitud(SolicitudVO solicitud) {
		this.solicitud = solicitud;
	}

	public List<TipoSolicitudVO> getListTipoSolicitud() {
		return listTipoSolicitud;
	}
	public void setListTipoSolicitud(List<TipoSolicitudVO> listTipoSolicitud) {
		this.listTipoSolicitud = listTipoSolicitud;
	}
	public List<AreaVO> getListArea() {
		return listArea;
	}
	public void setListArea(List<AreaVO> listArea) {
		this.listArea = listArea;
	}

	public List<EstSolicitudVO> getListEstSolicitud() {
		return listEstSolicitud;
	}

	public void setListEstSolicitud(List<EstSolicitudVO> listEstSolicitud) {
		this.listEstSolicitud = listEstSolicitud;
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



	public boolean getEstaEnSolPendiente() {
		return estaEnSolPendiente;
	}
	
	public void setDesAreaLog(String desAreaLog){
		this.desAreaLog = desAreaLog;
	}
	
	public String getDesAreaLog() {
		return desAreaLog;
	}
	
	public void setEstaEnSolPendiente(boolean estaEnSolPendiente) {
		this.estaEnSolPendiente = estaEnSolPendiente;
	}

	public boolean getEstaEnSolEmitidas() {
		return estaEnSolEmitidas;
	}

	public void setEstaEnSolEmitidas(boolean estaEnSolEmitidas) {
		this.estaEnSolEmitidas = estaEnSolEmitidas;
	}

	
	public boolean getMostrarFiltro() {
		return mostrarFiltro;
	}

	public void setMostrarFiltro(boolean mostrarFiltro) {
		this.mostrarFiltro = mostrarFiltro;
	}
	
	//	 View getters
	public int getCantResult(){
		if(getListResult()!=null && !getListResult().isEmpty())
			return getListResult().size();
		return 0;
	}
	
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

	public Boolean getCambiarEstadoBussEnabled() {
		return cambiarEstadoBussEnabled;
	}

	public void setCambiarEstadoBussEnabled(Boolean cambiarEstadoBussEnabled) {
		this.cambiarEstadoBussEnabled = cambiarEstadoBussEnabled;
	}

	public String getCambiarEstadoEnabled() {
		return SiatBussImageModel.hasEnabledFlag(cambiarEstadoBussEnabled, CasSecurityConstants.ABM_SOLICITUD, CasSecurityConstants.MTD_CAMBIARESTADO);
	}
	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Solicitudes Pendientes del Area");
		 report.setReportBeanName("solicitud");
		 report.setReportFileName(this.getClass().getName());
		 	         
		 if (!StringUtil.isNullOrEmpty(this.getDesAreaLog())){
			 report.addReportFiltro("Area Destino", this.getDesAreaLog());
			 report.addReportFiltro("Estado Solicitud", "Pendientes");
		 }else{
		 // carga de filtros
		 report.addReportFiltro("Fecha Desde", this.getFechaDesdeView());
	
		 report.addReportFiltro("Fecha Hasta", this.getFechaHastaView());
		 
		 report.addReportFiltro("Número", this.getSolicitud().getIdView());
		 
		 // desAreaOrigen
		 String desAreaOrigen = "";
			
		AreaVO areaVO = (AreaVO) ModelUtil.getBussImageModelByIdForList(
				 this.getSolicitud().getAreaOrigen().getId(),
				 this.getListArea());
		 if (areaVO != null){
			 desAreaOrigen = areaVO.getDesArea();
		 }
		 report.addReportFiltro("Area Origen", desAreaOrigen);
		 
		 // desAreaDestino
		 String desAreaDestino = "";
			
		AreaVO areaDestinoVO = (AreaVO) ModelUtil.getBussImageModelByIdForList(
				 this.getSolicitud().getAreaDestino().getId(),
				 this.getListArea());
		 if (areaDestinoVO != null){
			 desAreaDestino = areaDestinoVO.getDesArea();
		 }
		 report.addReportFiltro("Area Destino", desAreaDestino);
         
		 //Estado Solicitud
		 
		 String estadoSol = "";
			
		 EstSolicitudVO estSolicitudVO = (EstSolicitudVO) ModelUtil.getBussImageModelByIdForList(
					 this.getSolicitud().getEstSolicitud().getId(),
					 this.getListEstSolicitud());
			 if (estSolicitudVO != null){
				 estadoSol = estSolicitudVO.getDescripcion();
			 }
			 report.addReportFiltro("Estado Solicitud", estadoSol);
		 }
		 		
		 
		 // Order by
		 //report.setReportOrderBy("desTipoObra ASC");
	     
	     ReportTableVO rtSol = new ReportTableVO("rtSol");
	     rtSol.setTitulo("Listado de Solicitudes Pendientes del Area");
	   
	     // carga de columnas
	     rtSol.addReportColumn("Numero", "id");
	     rtSol.addReportColumn("Tipo Solicitud", "tipoSolicitud.descripcion");
	     rtSol.addReportColumn("Area Origen", "areaOrigen.desArea");
	     rtSol.addReportColumn("Area Destino", "areaDestino.desArea");
	     rtSol.addReportColumn("Fecha Alta", "fechaAlta");
	     rtSol.addReportColumn("Asunto", "asuntoSolicitud");
	     rtSol.addReportColumn("Estado Solicitud", "estSolicitud.descripcion");
	     report.getReportListTable().add(rtSol);

	    }
		
}
