//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.esp.iface.util.EspSecurityConstants;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import ar.gov.rosario.siat.pad.iface.model.PersonaVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Search Page de Habilitacion
 * @author tecso
 *
 */
public class HabilitacionSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "habilitacionSearchPageVO";

	private HabilitacionVO habilitacion = new HabilitacionVO();

	private Date fechaDesde;
	private Date fechaHasta;
	private String fechaDesdeView = "";
	private String fechaHastaView = "";
	private PersonaVO titular = new PersonaVO();
	private List<RecursoVO> listRecurso 	 = new ArrayList<RecursoVO>();
	private List<TipoCobroVO> listTipoCobro  = new ArrayList<TipoCobroVO>();
	private List<TipoHabVO> listTipoHab	   	 = new ArrayList<TipoHabVO>();
	private List<EstHabVO> listEstHab 		 = new ArrayList<EstHabVO>();
	private List<CuentaVO> listCuentaPersona = new ArrayList<CuentaVO>();
	
	private Date fechaEventoDesde;
	private Date fechaEventoHasta;
	private String fechaEventoDesdeView = "";
	private String fechaEventoHastaView = "";
	private boolean reporteSinEntradasVendidas = false;
	private String  reporteSinEntradasVendidasView = "";
	
	private String tipoExterna = "false";
	
	private List<OrganizadorForReport> listOrganizador = new ArrayList<OrganizadorForReport>(); 
	
	private boolean poseeDatosPersona = false; 
	
	private List<HabilitacionVO> listHabilitacion = new ArrayList<HabilitacionVO>();
	private Date fechaEmision ;
	private String fechaEmisionView = ""; 
	
	private Boolean entVenInternaBussEnabled  = true;
	private Boolean entVenExternaBussEnabled  = true;
	
	private Boolean cambiarEstadoBussEnabled  = true;

	public HabilitacionSearchPage(){
		super(EspSecurityConstants.ABM_HABILITACION);
	}

	// Getters & Setters
	public HabilitacionVO getHabilitacion() {
		return habilitacion;
	}
	public void setHabilitacion(HabilitacionVO habilitacion) {
		this.habilitacion = habilitacion;
	}
	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}
	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}
	public List<TipoCobroVO> getListTipoCobro() {
		return listTipoCobro;
	}
	public void setListTipoCobro(List<TipoCobroVO> listTipoCobro) {
		this.listTipoCobro = listTipoCobro;
	}
	public List<TipoHabVO> getListTipoHab() {
		return listTipoHab;
	}
	public void setListTipoHab(List<TipoHabVO> listTipoHab) {
		this.listTipoHab = listTipoHab;
	}
	public PersonaVO getTitular() {
		return titular;
	}
	public void setTitular(PersonaVO titular) {
		this.titular = titular;
	}
	public List<EstHabVO> getListEstHab() {
		return listEstHab;
	}
	public void setListEstHab(List<EstHabVO> listEstHab) {
		this.listEstHab = listEstHab;
	}

	public String getName(){
		return NAME;
	}
	public Date getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
		this.fechaDesdeView = DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaDesdeView() {
		return fechaDesdeView;
	}
	public void setFechaDesdeView(String fechaDesdeView) {
		this.fechaDesdeView = fechaDesdeView;
	}
	public Date getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
		this.fechaHastaView = DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaHastaView() {
		return fechaHastaView;
	}
	public void setFechaHastaView(String fechaHastaView) {
		this.fechaHastaView = fechaHastaView;
	}
	public boolean isPoseeDatosPersona() {
		return poseeDatosPersona;
	}
	public void setPoseeDatosPersona(boolean poseeDatosPersona) {
		this.poseeDatosPersona = poseeDatosPersona;
	}
	public List<CuentaVO> getListCuentaPersona() {
		return listCuentaPersona;
	}
	public void setListCuentaPersona(List<CuentaVO> listCuentaPersona) {
		this.listCuentaPersona = listCuentaPersona;
	}

	public List<HabilitacionVO> getListHabilitacion() {
		return listHabilitacion;
	}

	public void setListHabilitacion(List<HabilitacionVO> listHabilitacion) {
		this.listHabilitacion = listHabilitacion;
	}

	public Date getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
		this.fechaEmisionView = DateUtil.formatDate(fechaEmision, DateUtil.ddSMMSYYYY_MASK);
	}

	public String getFechaEmisionView() {
		return fechaEmisionView;
	}

	public void setFechaEmisionView(String fechaEmisionView) {
		this.fechaEmisionView = fechaEmisionView;
	}

	public List<OrganizadorForReport> getListOrganizador() {
		return listOrganizador;
	}

	public void setListOrganizador(List<OrganizadorForReport> listOrganizador) {
		this.listOrganizador = listOrganizador;
	}

	public String getTipoExterna() {
		return tipoExterna;
	}

	public void setTipoExterna(String tipoExterna) {
		this.tipoExterna = tipoExterna;
	}

	public String getEntVenInternaEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getEntVenInternaBussEnabled(), 
				EspSecurityConstants.ABM_HABILITACION, EspSecurityConstants.ENTVEN_INTERNA);
	}

	public String getEntVenExternaEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getEntVenExternaBussEnabled(), 
				EspSecurityConstants.ABM_HABILITACION, EspSecurityConstants.ENTVEN_EXTERNA);
	}
	
	public Boolean getEntVenInternaBussEnabled() {
		return entVenInternaBussEnabled;
	}

	public void setEntVenInternaBussEnabled(Boolean entVenInternaBussEnabled) {
		this.entVenInternaBussEnabled = entVenInternaBussEnabled;
	}

	public Boolean getEntVenExternaBussEnabled() {
		return entVenExternaBussEnabled;
	}

	public void setEntVenExternaBussEnabled(Boolean entVenExternaBussEnabled) {
		this.entVenExternaBussEnabled = entVenExternaBussEnabled;
	}
	
	public Boolean getCambiarEstadoBussEnabled() {
		return cambiarEstadoBussEnabled;
	}

	public void setCambiarEstadoBussEnabled(Boolean cambiarEstadoBussEnabled) {
		this.cambiarEstadoBussEnabled = cambiarEstadoBussEnabled;
	}
	
	public String getCambiarEstadoEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getCambiarEstadoBussEnabled(), 
				EspSecurityConstants.ABM_HABILITACION, EspSecurityConstants.CAMBIAR_ESTADO);
	}
	
	public void prepareReport(Long format) {

		ReportVO report = this.getReport();

		report.setReportFormat(format);
		report.setReportTitle("Reporte de Habilitaciones");
		report.setReportBeanName("Habilitaciones");
		report.setReportFileName(this.getClass().getName());

		// carga de filtros
		report.setReportFiltrosTitle("Filtros de B\u00FAsqueda: ");
	    // Recurso
	    String desRecurso = "";
		RecursoVO recursoVO = (RecursoVO) ModelUtil.getBussImageModelByIdForList(
				this.getHabilitacion().getRecurso().getId(), 
				this.getListRecurso());
	    if (recursoVO != null) desRecurso = recursoVO.getDesRecurso();
		report.addReportFiltro("Recurso", desRecurso);
		report.addReportFiltro("Cuenta", this.getHabilitacion().getCuenta().getNumeroCuenta());
		report.addReportFiltro("Descripci\u00F3n", this.getHabilitacion().getDescripcion());
		report.addReportFiltro("Sexo", this.getTitular().getSexo().getValue());
		report.addReportFiltro("Documento", this.getTitular().getDocumento().getNumeroView());
		report.addReportFiltro("N\u00FAmero", this.getHabilitacion().getNumeroView());
		report.addReportFiltro("A\u00F1o", this.getHabilitacion().getAnioView());
	    // Tipo de Habilitacion
	    String desTipoHab = "";
		TipoHabVO tipoHabVO = (TipoHabVO) ModelUtil.getBussImageModelByIdForList(
				this.getHabilitacion().getTipoHab().getId(), 
				this.getListTipoHab());
	    if (tipoHabVO != null) desTipoHab = tipoHabVO.getDescripcion();
		report.addReportFiltro("Tipo de Habilitaci\u00F3n", desTipoHab);
		report.addReportFiltro("Fecha Desde", this.getFechaDesdeView());
		report.addReportFiltro("Fecha Hasta", this.getFechaHastaView());

		// Order by
		report.setReportOrderBy("fechaHab desc, numero desc, anio desc ");

		ReportTableVO rtHabilitacion = new ReportTableVO("Habilitacion");
		rtHabilitacion.setTitulo("Listado de Habilitaciones");

		// carga de columnas
		// Numero
		rtHabilitacion.addReportColumn("N\u00FAmero", "numero");
		// Anio
		rtHabilitacion.addReportColumn("A\u00F1o", "anio");
		// Recurso
		rtHabilitacion.addReportColumn("Recurso", "recurso.desRecurso");
		// Cuenta
		rtHabilitacion.addReportColumn("Cuenta", "cuenta.numeroCuenta");
		// Descripcion
		rtHabilitacion.addReportColumn("Descripci\u00F3n", "descripcion");
		// Fecha de Habilitacion
		rtHabilitacion.addReportColumn("Fecha de Habilitaci\u00F3n", "fechaHab");

		report.getReportListTable().add(rtHabilitacion);
	}
	
	public Date getFechaEventoDesde() {
		return fechaEventoDesde;
	}
	public void setFechaEventoDesde(Date fechaEventoDesde) {
		this.fechaEventoDesde = fechaEventoDesde;
		this.fechaEventoDesdeView = DateUtil.formatDate(fechaEventoDesde, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaEventoDesdeView() {
		return fechaEventoDesdeView;
	}
	public void setFechaEventoDesdeView(String fechaEventoDesdeView) {
		this.fechaEventoDesdeView = fechaEventoDesdeView;
	}
	public Date getFechaEventoHasta() {
		return fechaEventoHasta;
	}
	public void setFechaEventoHasta(Date fechaEventoHasta) {
		this.fechaEventoHasta = fechaEventoHasta;
		this.fechaEventoHastaView = DateUtil.formatDate(fechaEventoHasta, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaEventoHastaView() {
		return fechaEventoHastaView;
	}
	public void setFechaEventoHastaView(String fechaEventoHastaView) {
		this.fechaEventoHastaView = fechaEventoHastaView;
	}
	public boolean getReporteSinEntradasVendidas() {
		return reporteSinEntradasVendidas;
	}
	public void setReporteSinEntradasVendidas(boolean reporteSinEntradasVendidas) {
		this.reporteSinEntradasVendidas = reporteSinEntradasVendidas;
		if(reporteSinEntradasVendidas) this.reporteSinEntradasVendidasView = "true";
		else this.reporteSinEntradasVendidasView = "false";
	}

	public String getReporteSinEntradasVendidasView() {
		return reporteSinEntradasVendidasView;
	}
	public void setReporteSinEntradasVendidasView(
			String reporteSinEntradasVendidasView) {
		this.reporteSinEntradasVendidasView = reporteSinEntradasVendidasView;
	}
	
}
