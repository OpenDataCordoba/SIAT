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
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;


/**
 * Value Object del MovBan
 * @author tecso
 *
 */
public class MovBanVO extends SiatBussImageModel {
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "movBanVO";

	private Long idOrgRecAfip;
	private Long bancoAdm;
	private Date fechaProceso;
	private Date fechaAcredit;
	private Double totalDebito;
	private Double totalCredito;
	private Long cantDetalle;
	private SiNo conciliado = SiNo.OpcionTodo;
	
	private String idOrgRecAfipView = "";
	private String bancoAdmView = "";
	private String fechaProcesoView = "";
	private String fechaAcreditView = "";
	private String totalDebitoView = "";
	private String totalCreditoView = "";
	private String cantDetalleView = "";

	private List<MovBanDetVO> listMovBanDet = new ArrayList<MovBanDetVO>();

	// Buss Flags
	private Boolean conciliarBussEnabled   = true;
	private Boolean anularBussEnabled   = true;
	
	// Constructores
	public MovBanVO() {
		super();
	}

	// Getters y Setters
	public Long getBancoAdm() {
		return bancoAdm;
	}
	public void setBancoAdm(Long bancoAdm) {
		this.bancoAdm = bancoAdm;
		this.bancoAdmView = StringUtil.formatLong(bancoAdm);
	}
	public String getBancoAdmView() {
		return bancoAdmView;
	}
	public void setBancoAdmView(String bancoAdmView) {
		this.bancoAdmView = bancoAdmView;
	}
	public Long getCantDetalle() {
		return cantDetalle;
	}
	public void setCantDetalle(Long cantDetalle) {
		this.cantDetalle = cantDetalle;
		this.cantDetalleView = StringUtil.formatLong(cantDetalle);
	}
	public String getCantDetalleView() {
		return cantDetalleView;
	}
	public void setCantDetalleView(String cantDetalleView) {
		this.cantDetalleView = cantDetalleView;
	}
	public SiNo getConciliado() {
		return conciliado;
	}
	public void setConciliado(SiNo conciliado) {
		this.conciliado = conciliado;
	}
	public Date getFechaAcredit() {
		return fechaAcredit;
	}
	public void setFechaAcredit(Date fechaAcredit) {
		this.fechaAcredit = fechaAcredit;
		this.fechaAcreditView = DateUtil.formatDate(fechaAcredit, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaAcreditView() {
		return fechaAcreditView;
	}
	public void setFechaAcreditView(String fechaAcreditView) {
		this.fechaAcreditView = fechaAcreditView;
	}
	public Date getFechaProceso() {
		return fechaProceso;
	}
	public void setFechaProceso(Date fechaProceso) {
		this.fechaProceso = fechaProceso;
		this.fechaProcesoView = DateUtil.formatDate(fechaProceso, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaProcesoView() {
		return fechaProcesoView;
	}
	public void setFechaProcesoView(String fechaProcesoView) {
		this.fechaProcesoView = fechaProcesoView;
	}
	public Long getIdOrgRecAfip() {
		return idOrgRecAfip;
	}
	public void setIdOrgRecAfip(Long idOrgRecAfip) {
		this.idOrgRecAfip = idOrgRecAfip;
		this.idOrgRecAfipView = StringUtil.formatLong(idOrgRecAfip);
	}
	public String getIdOrgRecAfipView() {
		return idOrgRecAfipView;
	}
	public void setIdOrgRecAfipView(String idOrgRecAfipView) {
		this.idOrgRecAfipView = idOrgRecAfipView;
	}
	public Double getTotalCredito() {
		return totalCredito;
	}
	public void setTotalCredito(Double totalCredito) {
		this.totalCredito = totalCredito;
		this.totalCreditoView = StringUtil.formatDouble(totalCredito);
	}
	public String getTotalCreditoView() {
		return totalCreditoView;
	}
	public void setTotalCreditoView(String totalCreditoView) {
		this.totalCreditoView = totalCreditoView;
	}
	public Double getTotalDebito() {
		return totalDebito;
	}
	public void setTotalDebito(Double totalDebito) {
		this.totalDebito = totalDebito;
		this.totalDebitoView = StringUtil.formatDouble(totalDebito);
	}
	public String getTotalDebitoView() {
		return totalDebitoView;
	}
	public void setTotalDebitoView(String totalDebitoView) {
		this.totalDebitoView = totalDebitoView;
	}
	public List<MovBanDetVO> getListMovBanDet() {
		return listMovBanDet;
	}
	public void setListMovBanDet(List<MovBanDetVO> listMovBanDet) {
		this.listMovBanDet = listMovBanDet;
	}
	
	// Flags Seguridad
	public Boolean getConciliarBussEnabled() {
		return conciliarBussEnabled;
	}

	public void setConciliarBussEnabled(Boolean conciliarBussEnabled) {
		this.conciliarBussEnabled = conciliarBussEnabled;
	}
	
	public String getConciliarEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getConciliarBussEnabled(), 
				BalSecurityConstants.ABM_MOVBAN, BaseSecurityConstants.CONCILIAR);
	}	
	
	public Boolean getAnularBussEnabled() {
		return anularBussEnabled;
	}

	public void setAnularBussEnabled(Boolean anularBussEnabled) {
		this.anularBussEnabled = anularBussEnabled;
	}
	
	public String getAnularEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getAnularBussEnabled(), 
				BalSecurityConstants.ABM_MOVBAN, BaseSecurityConstants.ANULAR);
	}
}
