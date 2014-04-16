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
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Search Page de Archivos de Transacciones para Balance
 * @author tecso
 *
 */
public class ArchivoSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	private ArchivoVO archivo = new ArchivoVO();
	
	public static final String NAME = "archivoSearchPageVO";
	
	private Date fechaBancoDesde;
	private Date fechaBancoHasta;	
	private String fechaBancoDesdeView = "";
	private String fechaBancoHastaView = "";

	private List<TipoArcVO> listTipoArc = new ArrayList<TipoArcVO>();
	private List<EstadoArcVO> listEstadoArc = new ArrayList<EstadoArcVO>();
	
	private BalanceVO balance = new BalanceVO();    
    private String[] listIdArchivoSelected;
    
	// Flag para excluir de la busqueda los archivos que ya esten incluidos en algun balance
	private Boolean paramExBalance = false;

	private Boolean anularBussEnabled   = true;
	private Boolean aceptarBussEnabled   = true;
	
	public ArchivoSearchPage(){
		super(BalSecurityConstants.ABM_ARCHIVO);
	}
	
	// Getters Y Setters
	public ArchivoVO getArchivo() {
		return archivo;
	}
	public void setArchivo(ArchivoVO archivo) {
		this.archivo = archivo;
	}
	public Date getFechaBancoDesde() {
		return fechaBancoDesde;
	}
	public void setFechaBancoDesde(Date fechaBancoDesde) {
		this.fechaBancoDesde = fechaBancoDesde;
		this.fechaBancoDesdeView = DateUtil.formatDate(fechaBancoDesde, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaBancoDesdeView() {
		return fechaBancoDesdeView;
	}
	public void setFechaBancoDesdeView(String fechaBancoDesdeView) {
		this.fechaBancoDesdeView = fechaBancoDesdeView;
	}
	public Date getFechaBancoHasta() {
		return fechaBancoHasta;
	}
	public void setFechaBancoHasta(Date fechaBancoHasta) {
		this.fechaBancoHasta = fechaBancoHasta;
		this.fechaBancoHastaView = DateUtil.formatDate(fechaBancoHasta, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaBancoHastaView() {
		return fechaBancoHastaView;
	}
	public void setFechaBancoHastaView(String fechaBancoHastaView) {
		this.fechaBancoHastaView = fechaBancoHastaView;
	}
	public List<EstadoArcVO> getListEstadoArc() {
		return listEstadoArc;
	}
	public void setListEstadoArc(List<EstadoArcVO> listEstadoArc) {
		this.listEstadoArc = listEstadoArc;
	}
	public List<TipoArcVO> getListTipoArc() {
		return listTipoArc;
	}
	public void setListTipoArc(List<TipoArcVO> listTipoArc) {
		this.listTipoArc = listTipoArc;
	}
	public Boolean getParamExBalance() {
		return paramExBalance;
	}
	public void setParamExBalance(Boolean paramExBalance) {
		this.paramExBalance = paramExBalance;
	}
	public BalanceVO getBalance() {
		return balance;
	}
	public void setBalance(BalanceVO balance) {
		this.balance = balance;
	}
	public String[] getListIdArchivoSelected() {
		return listIdArchivoSelected;
	}
	public void setListIdArchivoSelected(String[] listIdArchivoSelected) {
		this.listIdArchivoSelected = listIdArchivoSelected;
	}
	public String getName(){
		return NAME;
	}
	
	//	 Flags Seguridad
	public Boolean getAnularBussEnabled() {
		return anularBussEnabled;
	}

	public void setAnularBussEnabled(Boolean anularBussEnabled) {
		this.anularBussEnabled = anularBussEnabled;
	}
	
	public String getAnularEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getAnularBussEnabled(), 
				BalSecurityConstants.ABM_ARCHIVO, BaseSecurityConstants.ANULAR);
	}
	
	public Boolean getAceptarBussEnabled() {
		return aceptarBussEnabled;
	}

	public void setAceptarBussEnabled(Boolean aceptarBussEnabled) {
		this.aceptarBussEnabled = aceptarBussEnabled;
	}
	
	public String getAceptarEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getAceptarBussEnabled(), 
				BalSecurityConstants.ABM_ARCHIVO, BaseSecurityConstants.ACEPTAR);
	}
	
	public void prepareReport(Long format){
		
		ReportVO report = this.getReport(); 
		report.setReportFormat(format);	
		report.setReportBeanName("Archivo");
		report.setReportFileName(this.getClass().getName());	
	}
	
}
