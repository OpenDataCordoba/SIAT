//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.AreaVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

public class OtrIngTesSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "otrIngTesSearchPageVO";

	private OtrIngTesVO otrIngTes = new OtrIngTesVO();

	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	private List<AreaVO> listAreaOrigen = new ArrayList<AreaVO>();
	private List<EstOtrIngTesVO> listEstOtrIngTes = new ArrayList<EstOtrIngTesVO>();
	
	private Date fechaRegistroDesde;
	private Date fechaRegistroHasta;	
	private String fechaRegistroDesdeView = "";
	private String fechaRegistroHastaView = "";
	private Boolean filtroDistribucionErronea = false;

	private FolioVO folio = new FolioVO();    
    private String[] listIdOtrIngTesSelected;
	
	private Boolean imputarOtrIngTesBussEnabled     = true;
	private Boolean distribuirOtrIngTesBussEnabled  = true;
	private Boolean generarReciboBussEnabled  = true;
	
	// Contructores
	public OtrIngTesSearchPage(){
		super(BalSecurityConstants.ABM_OTRINGTES);
	}


	// Getters Y Setters
	public OtrIngTesVO getOtrIngTes() {
		return otrIngTes;
	}
	public void setOtrIngTes(OtrIngTesVO otrIngTes) {
		this.otrIngTes = otrIngTes;
	}
	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}
	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}
	public List<AreaVO> getListAreaOrigen() {
		return listAreaOrigen;
	}
	public void setListAreaOrigen(List<AreaVO> listAreaOrigen) {
		this.listAreaOrigen = listAreaOrigen;
	}
	public List<EstOtrIngTesVO> getListEstOtrIngTes() {
		return listEstOtrIngTes;
	}
	public void setListEstOtrIngTes(List<EstOtrIngTesVO> listEstOtrIngTes) {
		this.listEstOtrIngTes = listEstOtrIngTes;
	}
	public Date getFechaRegistroDesde() {
		return fechaRegistroDesde;
	}
	public void setFechaRegistroDesde(Date fechaRegistroDesde) {
		this.fechaRegistroDesde = fechaRegistroDesde;
		this.fechaRegistroDesdeView = DateUtil.formatDate(fechaRegistroDesde, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaRegistroDesdeView() {
		return fechaRegistroDesdeView;
	}
	public void setFechaRegistroDesdeView(String fechaRegistroDesdeView) {
		this.fechaRegistroDesdeView = fechaRegistroDesdeView;
	}
	public Date getFechaRegistroHasta() {
		return fechaRegistroHasta;
	}
	public void setFechaRegistroHasta(Date fechaRegistroHasta) {
		this.fechaRegistroHasta = fechaRegistroHasta;
		this.fechaRegistroHastaView = DateUtil.formatDate(fechaRegistroHasta, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaRegistroHastaView() {
		return fechaRegistroHastaView;
	}
	public void setFechaRegistroHastaView(String fechaRegistroHastaView) {
		this.fechaRegistroHastaView = fechaRegistroHastaView;
	}
	public FolioVO getFolio() {
		return folio;
	}
	public void setFolio(FolioVO folio) {
		this.folio = folio;
	}
	public String[] getListIdOtrIngTesSelected() {
		return listIdOtrIngTesSelected;
	}
	public void setListIdOtrIngTesSelected(String[] listIdOtrIngTesSelected) {
		this.listIdOtrIngTesSelected = listIdOtrIngTesSelected;
	}
	public Boolean getFiltroDistribucionErronea() {
		return filtroDistribucionErronea;
	}
	public void setFiltroDistribucionErronea(Boolean filtroDistribucionErronea) {
		this.filtroDistribucionErronea = filtroDistribucionErronea;
	}

	// Flags Seguridad
	public Boolean getDistribuirOtrIngTesBussEnabled() {
		return distribuirOtrIngTesBussEnabled;
	}

	public void setDistribuirOtrIngTesBussEnabled(Boolean distribuirOtrIngTesBussEnabled) {
		this.distribuirOtrIngTesBussEnabled = distribuirOtrIngTesBussEnabled;
	}
	
	public String getDistribuirOtrIngTesEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getDistribuirOtrIngTesBussEnabled(), 
				BalSecurityConstants.ABM_OTRINGTES, BalSecurityConstants.DISTRIBUIR);
	}
	
	public Boolean getImputarOtrIngTesBussEnabled() {
		return imputarOtrIngTesBussEnabled;
	}
	public void setImputarOtrIngTesBussEnabled(Boolean imputarOtrIngTesBussEnabled) {
		this.imputarOtrIngTesBussEnabled = imputarOtrIngTesBussEnabled;
	}
	
	public String getImputarOtrIngTesEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getImputarOtrIngTesBussEnabled(), 
				BalSecurityConstants.ABM_OTRINGTES, BalSecurityConstants.IMPUTAR);
	}
	
	public Boolean getGenerarReciboBussEnabled() {
		return generarReciboBussEnabled;
	}

	public void setGenerarReciboBussEnabled(Boolean generarReciboBussEnabled) {
		this.generarReciboBussEnabled = generarReciboBussEnabled;
	}
	
	public String getGenerarReciboEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getGenerarReciboBussEnabled(), 
				BalSecurityConstants.ABM_OTRINGTES, BalSecurityConstants.GENERAR_RECIBO);
	}
	
	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); 
		report.setReportTitle("Reporte de Otros Ingresos de Tesoreria");
		report.setReportBeanName("OtrIngTes");
		report.setReportFormat(format);
	     // nombre del archivo 
		report.setReportFileName(this.getClass().getName());
		 
		 // carga de filtros:
		 // Recurso
		 String desRecurso = "No seleccionado";
		 RecursoVO recursoVO = (RecursoVO) ModelUtil.getBussImageModelByIdForList(this.getOtrIngTes().getRecurso().getId(),this.getListRecurso());
		 if (recursoVO != null){
			 desRecurso = recursoVO.getDesRecurso();
		 }
		 report.addReportFiltro("Recurso", desRecurso);
		 // Area
		 String desArea = "No seleccionada";
		 AreaVO areaVO = (AreaVO) ModelUtil.getBussImageModelByIdForList(
				 this.getOtrIngTes().getAreaOrigen().getId(), 
				 this.getListAreaOrigen());
		 if (areaVO != null){
			 desArea = areaVO.getDesArea();
		 }		 
		 report.addReportFiltro("Area", desArea);
		 // Descripcion
		 report.addReportFiltro("Descripción", this.getOtrIngTes().getDescripcion());			 
		 
		 // Fecha de Registro Desde y Hasta
		 report.addReportFiltro("Fecha Registro Desde", this.getFechaRegistroDesdeView());
		 report.addReportFiltro("Fecha Registro Hasta", this.getFechaRegistroHastaView());
		 // Area
		 String desEstado = "No seleccionado";
		 EstOtrIngTesVO estOtrIngTesVO = (EstOtrIngTesVO) ModelUtil.getBussImageModelByIdForList(
				 this.getOtrIngTes().getEstOtrIngTes().getId(), 
				 this.getListEstOtrIngTes());
		 if (estOtrIngTesVO != null){
			 desEstado = estOtrIngTesVO.getDesEstOtrIngTes();
		 }	
		 report.addReportFiltro("Estado", desEstado);
		 
		 // Order by
		 report.setReportOrderBy("fechaOtrIngTes DESC, id DESC");
		 
		 ReportTableVO rtOtrIngTes = new ReportTableVO("OtrIngTes");
		 rtOtrIngTes.setTitulo("Listado de Otros Ingresos de Tesorería");

		 rtOtrIngTes.addReportColumn("Fecha de Registro", "fechaOtrIngTes");
		 rtOtrIngTes.addReportColumn("Importe", "importeForReport");
		 rtOtrIngTes.addReportColumn("Descripción", "descripcion");
		 rtOtrIngTes.addReportColumn("Recurso", "recurso.desRecurso");
		 rtOtrIngTes.addReportColumn("Area", "areaOrigen.desArea");

		 report.getReportListTable().add(rtOtrIngTes);
	}

}
