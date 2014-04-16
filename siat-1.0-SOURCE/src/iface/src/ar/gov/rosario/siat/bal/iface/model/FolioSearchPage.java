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
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Search Page de Folio de Tesoreria
 * @author tecso
 *
 */
public class FolioSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	private FolioVO folio = new FolioVO();
	
	public static final String NAME = "folioSearchPageVO";
	
	private Date fechaFolioDesde;
	private Date fechaFolioHasta;	
	private String fechaFolioDesdeView = "";
	private String fechaFolioHastaView = "";
	
	private Date fechaCobranzaDesde;
	private Date fechaCobranzaHasta;	
	private String fechaCobranzaDesdeView = "";
	private String fechaCobranzaHastaView = "";

	private List<EstadoFolVO> listEstadoFol = new ArrayList<EstadoFolVO>();
	
	// Flag para excluir de la busqueda los folios que ya esten incluidos en algun balance
	private Boolean paramExBalance = false;
	
	private Boolean enviarBussEnabled   = true;
	private Boolean devolverBussEnabled   = true;

	public FolioSearchPage(){
		super(BalSecurityConstants.ABM_FOLIO);
	}

	// Getters Y Setters
	public Date getFechaCobranzaDesde() {
		return fechaCobranzaDesde;
	}
	public void setFechaCobranzaDesde(Date fechaCobranzaDesde) {
		this.fechaCobranzaDesde = fechaCobranzaDesde;
		this.fechaCobranzaDesdeView = DateUtil.formatDate(fechaCobranzaDesde, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaCobranzaDesdeView() {
		return fechaCobranzaDesdeView;
	}
	public void setFechaCobranzaDesdeView(String fechaCobranzaDesdeView) {
		this.fechaCobranzaDesdeView = fechaCobranzaDesdeView;
	}
	public Date getFechaCobranzaHasta() {
		return fechaCobranzaHasta;
	}
	public void setFechaCobranzaHasta(Date fechaCobranzaHasta) {
		this.fechaCobranzaHasta = fechaCobranzaHasta;
		this.fechaCobranzaHastaView = DateUtil.formatDate(fechaCobranzaHasta, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaCobranzaHastaView() {
		return fechaCobranzaHastaView;
	}
	public void setFechaCobranzaHastaView(String fechaCobranzaHastaView) {
		this.fechaCobranzaHastaView = fechaCobranzaHastaView;
	}
	public Date getFechaFolioDesde() {
		return fechaFolioDesde;
	}
	public void setFechaFolioDesde(Date fechaFolioDesde) {
		this.fechaFolioDesde = fechaFolioDesde;
		this.fechaFolioDesdeView = DateUtil.formatDate(fechaFolioDesde, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaFolioDesdeView() {
		return fechaFolioDesdeView;
	}
	public void setFechaFolioDesdeView(String fechaFolioDesdeView) {
		this.fechaFolioDesdeView = fechaFolioDesdeView;
	}
	public Date getFechaFolioHasta() {
		return fechaFolioHasta;
	}
	public void setFechaFolioHasta(Date fechaFolioHasta) {
		this.fechaFolioHasta = fechaFolioHasta;
		this.fechaFolioHastaView = DateUtil.formatDate(fechaFolioHasta, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaFolioHastaView() {
		return fechaFolioHastaView;
	}
	public void setFechaFolioHastaView(String fechaFolioHastaView) {
		this.fechaFolioHastaView = fechaFolioHastaView;
	}
	public FolioVO getFolio() {
		return folio;
	}
	public void setFolio(FolioVO folio) {
		this.folio = folio;
	}
	public List<EstadoFolVO> getListEstadoFol() {
		return listEstadoFol;
	}
	public void setListEstadoFol(List<EstadoFolVO> listEstadoFol) {
		this.listEstadoFol = listEstadoFol;
	}
	public Boolean getParamExBalance() {
		return paramExBalance;
	}
	public void setParamExBalance(Boolean paramExBalance) {
		this.paramExBalance = paramExBalance;
	}

	public String getName(){
		return NAME;
	}
	
	//	 Flags Seguridad
	public Boolean getEnviarBussEnabled() {
		return enviarBussEnabled;
	}

	public void setEnviarBussEnabled(Boolean enviarBussEnabled) {
		this.enviarBussEnabled = enviarBussEnabled;
	}
	
	public String getEnviarEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getEnviarBussEnabled(), 
				BalSecurityConstants.ABM_FOLIO, BaseSecurityConstants.ENVIAR);
	}
	public Boolean getDevolverBussEnabled() {
		return devolverBussEnabled;
	}

	public void setDevolverBussEnabled(Boolean devolverBussEnabled) {
		this.devolverBussEnabled = devolverBussEnabled;
	}
	
	public String getDevolverEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getDevolverBussEnabled(), 
				BalSecurityConstants.ABM_FOLIO, BaseSecurityConstants.DEVOLVER);
	}

	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); 
		report.setReportTitle("Reporte de Folios de Tesoreria");
		report.setReportBeanName("Folio");
		report.setReportFormat(format);
	     // nombre del archivo 
		report.setReportFileName(this.getClass().getName());
		 
		 // carga de filtros:
		 // Numero
		 report.addReportFiltro("Número", this.getFolio().getNumeroView());
		 // Descripcion
		 report.addReportFiltro("Descripción", this.getFolio().getDescripcion());
		 // Fecha de Registro Desde y Hasta
		 report.addReportFiltro("Fecha Folio Desde", this.getFechaFolioDesdeView());
		 report.addReportFiltro("Fecha Folio Hasta", this.getFechaFolioHastaView());
		 // Estado
		 String desEstado = "No seleccionado";
		 EstadoFolVO estadoFolVO = (EstadoFolVO) ModelUtil.getBussImageModelByIdForList(
				 this.getFolio().getEstadoFol().getId(), 
				 this.getListEstadoFol());
		 if (estadoFolVO != null){
			 desEstado = estadoFolVO.getDescripcion();
		 }	
		 report.addReportFiltro("Estado", desEstado);
		 
		 // Order by
		 report.setReportOrderBy("fechaFolio DESC, numero DESC");
		 
		 ReportTableVO rtOtrIngTes = new ReportTableVO("Folio");
		 rtOtrIngTes.setTitulo("Listado de Folio de Tesorería");

		 rtOtrIngTes.addReportColumn("Fecha de Folio", "fechaFolio");
		 rtOtrIngTes.addReportColumn("Número", "numero");
		 rtOtrIngTes.addReportColumn("Descripción", "descripcion");
		 rtOtrIngTes.addReportColumn("Estado", "estadoFol.descripcion");

		 report.getReportListTable().add(rtOtrIngTes);
	}

}
