//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.ReportVO;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Search Page de Dias de Cobranza incluidos en Folios de Tesoreria
 * 
 * @author tecso
 *
 */
public class FolDiaCobSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	private FolDiaCobVO folDiaCob = new FolDiaCobVO();
	
	public static final String NAME = "folDiaCobSearchPageVO";
	
	private Date fechaCobDesde;
	private Date fechaCobHasta;	
	private String fechaCobDesdeView = "";
	private String fechaCobHastaView = "";

	private String descripcion = "";
	
	private List<EstadoFolVO> listEstadoFol = new ArrayList<EstadoFolVO>();
	
	private String totalDiaCobView = "";
	
	private List<TipoCobVO> listTipoCob = new ArrayList<TipoCobVO>();
	private List<FolDiaCobColVO> listTotales = new ArrayList<FolDiaCobColVO>();

	private Date fechaFolioDesde;
	private Date fechaFolioHasta;	
	private String fechaFolioDesdeView = "";
	private String fechaFolioHastaView = "";
	
	private Double importeTotalFilaFiltro = 0D;
	private String importeTotalFilaFiltroView = "";

	private Long nroFolioDesde = 0L;
	private Long nroFolioHasta = 0L;
	private String nroFolioDesdeView = "";
	private String nroFolioHastaView = "";
	
	private List<SiNo> listConciliado= new ArrayList<SiNo>();
	
    private String[] listIdFolDiaCobConciliado;
    Map<String, String> mapIdFolDiaCobConciliado = new HashMap<String, String>();
    
    private String planillaFileName = "";
    
	public FolDiaCobSearchPage(){
		super(BalSecurityConstants.ABM_FOLDIACOB);
	}

	// Getters Y Setters
	public FolDiaCobVO getFolDiaCob() {
		return folDiaCob;
	}
	public void setFolDiaCob(FolDiaCobVO folDiaCob) {
		this.folDiaCob = folDiaCob;
	}
	public List<EstadoFolVO> getListEstadoFol() {
		return listEstadoFol;
	}
	public void setListEstadoFol(List<EstadoFolVO> listEstadoFol) {
		this.listEstadoFol = listEstadoFol;
	}
	public Date getFechaCobDesde() {
		return fechaCobDesde;
	}
	public void setFechaCobDesde(Date fechaCobDesde) {
		this.fechaCobDesde = fechaCobDesde;
		this.fechaCobDesdeView = DateUtil.formatDate(fechaCobDesde, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaCobDesdeView() {
		return fechaCobDesdeView;
	}
	public void setFechaCobDesdeView(String fechaCobDesdeView) {
		this.fechaCobDesdeView = fechaCobDesdeView;
	}
	public Date getFechaCobHasta() {
		return fechaCobHasta;
	}
	public void setFechaCobHasta(Date fechaCobHasta) {
		this.fechaCobHasta = fechaCobHasta;
		this.fechaCobHastaView = DateUtil.formatDate(fechaCobHasta, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaCobHastaView() {
		return fechaCobHastaView;
	}
	public void setFechaCobHastaView(String fechaCobHastaView) {
		this.fechaCobHastaView = fechaCobHastaView;
	}
	public List<TipoCobVO> getListTipoCob() {
		return listTipoCob;
	}
	public void setListTipoCob(List<TipoCobVO> listTipoCob) {
		this.listTipoCob = listTipoCob;
	}
	public List<FolDiaCobColVO> getListTotales() {
		return listTotales;
	}
	public void setListTotales(List<FolDiaCobColVO> listTotales) {
		this.listTotales = listTotales;
	}
	public String getTotalDiaCobView() {
		return totalDiaCobView;
	}
	public void setTotalDiaCobView(String totalDiaCobView) {
		this.totalDiaCobView = totalDiaCobView;
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
	public Double getImporteTotalFilaFiltro() {
		return importeTotalFilaFiltro;
	}
	public void setImporteTotalFilaFiltro(Double importeTotalFilaFiltro) {
		this.importeTotalFilaFiltro = importeTotalFilaFiltro;
		this.importeTotalFilaFiltroView = StringUtil.formatDouble(importeTotalFilaFiltro);
	}
	public String getImporteTotalFilaFiltroView() {
		return importeTotalFilaFiltroView;
	}
	public void setImporteTotalFilaFiltroView(String importeTotalFilaFiltroView) {
		this.importeTotalFilaFiltroView = importeTotalFilaFiltroView;
	}
	public Long getNroFolioDesde() {
		return nroFolioDesde;
	}
	public void setNroFolioDesde(Long nroFolioDesde) {
		this.nroFolioDesde = nroFolioDesde;
		this.nroFolioDesdeView = StringUtil.formatLong(nroFolioDesde);
	}
	public String getNroFolioDesdeView() {
		return nroFolioDesdeView;
	}
	public void setNroFolioDesdeView(String nroFolioDesdeView) {
		this.nroFolioDesdeView = nroFolioDesdeView;
	}
	public Long getNroFolioHasta() {
		return nroFolioHasta;
	}
	public void setNroFolioHasta(Long nroFolioHasta) {
		this.nroFolioHasta = nroFolioHasta;
		this.nroFolioHastaView = StringUtil.formatLong(nroFolioHasta);
	}
	public String getNroFolioHastaView() {
		return nroFolioHastaView;
	}
	public void setNroFolioHastaView(String nroFolioHastaView) {
		this.nroFolioHastaView = nroFolioHastaView;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public List<SiNo> getListConciliado() {
		return listConciliado;
	}
	public void setListConciliado(List<SiNo> listConciliado) {
		this.listConciliado = listConciliado;
	}
	public String[] getListIdFolDiaCobConciliado() {
		return listIdFolDiaCobConciliado;
	}
	public void setListIdFolDiaCobConciliado(String[] listIdFolDiaCobConciliado) {
		this.listIdFolDiaCobConciliado = listIdFolDiaCobConciliado;
	}
	public Map<String, String> getMapIdFolDiaCobConciliado() {
		return mapIdFolDiaCobConciliado;
	}
	public void setMapIdFolDiaCobConciliado(
			Map<String, String> mapIdFolDiaCobConciliado) {
		this.mapIdFolDiaCobConciliado = mapIdFolDiaCobConciliado;
	}
	public String getPlanillaFileName() {
		return planillaFileName;
	}
	public void setPlanillaFileName(String planillaFileName) {
		this.planillaFileName = planillaFileName;
	}

	public String getName(){
		return NAME;
	}
	
	public String getGuardarEnabled() {
		return SiatBussImageModel.hasEnabledFlag(BalSecurityConstants.ABM_FOLDIACOB, BalSecurityConstants.ABM_FOLDIACOBE_GUARDAR);
	}
	
	public void prepareReport(Long format){
		
		ReportVO report = this.getReport(); 
		report.setReportFormat(format);	
		report.setReportBeanName("FolDiaCob");
		report.setReportFileName(this.getClass().getName());
		
	}
}
