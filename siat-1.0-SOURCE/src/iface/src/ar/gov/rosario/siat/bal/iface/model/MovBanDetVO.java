//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;


/**
 * Value Object del MovBanDet
 * @author tecso
 *
 */
public class MovBanDetVO extends SiatBussImageModel {
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "movBanDetVO";

	private MovBanVO movBan = new MovBanVO();
	private Long impuesto;
	private Long bancoRec;
	private Integer anexoOperativo;
	private Long nroCierreBanco;
	private Double debito;
	private Double credito;
	private String nroCuenta;
	private Integer moneda;
	private SiNo conciliado = SiNo.OpcionNula;
	
	private String impuestoView = "";
	private String bancoRecView = "";
	private String anexoOperativoView = "";
	private String nroCierreBancoView = "";
	private String debitoView = "";
	private String creditoView = "";
	private String monedaView = "";

	// Buss Flags
	private Boolean conciliarBussEnabled   = true;
	
	// Constructores
	public MovBanDetVO() {
		super();
	}


	// Getters y Setters
	public Integer getAnexoOperativo() {
		return anexoOperativo;
	}
	public void setAnexoOperativo(Integer anexoOperativo) {
		this.anexoOperativo = anexoOperativo;
		this.anexoOperativoView = StringUtil.formatInteger(anexoOperativo);
	}
	public String getAnexoOperativoView() {
		return anexoOperativoView;
	}
	public void setAnexoOperativoView(String anexoOperativoView) {
		this.anexoOperativoView = anexoOperativoView;
	}
	public Long getBancoRec() {
		return bancoRec;
	}
	public void setBancoRec(Long bancoRec) {
		this.bancoRec = bancoRec;
		this.bancoRecView = StringUtil.formatLong(bancoRec);
	}
	public String getBancoRecView() {
		return bancoRecView;
	}
	public void setBancoRecView(String bancoRecView) {
		this.bancoRecView = bancoRecView;
	}
	public SiNo getConciliado() {
		return conciliado;
	}
	public void setConciliado(SiNo conciliado) {
		this.conciliado = conciliado;
	}
	public Double getCredito() {
		return credito;
	}
	public void setCredito(Double credito) {
		this.credito = credito;
		this.creditoView = StringUtil.formatDouble(credito);
	}
	public String getCreditoView() {
		return creditoView;
	}
	public void setCreditoView(String creditoView) {
		this.creditoView = creditoView;
	}
	public Double getDebito() {
		return debito;
	}
	public void setDebito(Double debito) {
		this.debito = debito;
		this.debitoView = StringUtil.formatDouble(debito);
	}
	public String getDebitoView() {
		return debitoView;
	}
	public void setDebitoView(String debitoView) {
		this.debitoView = debitoView;
	}
	public Long getImpuesto() {
		return impuesto;
	}
	public void setImpuesto(Long impuesto) {
		this.impuesto = impuesto;
		this.impuestoView = StringUtil.formatLong(impuesto);
	}
	public String getImpuestoView() {
		return impuestoView;
	}
	public void setImpuestoView(String impuestoView) {
		this.impuestoView = impuestoView;
	}
	public Integer getMoneda() {
		return moneda;
	}
	public void setMoneda(Integer moneda) {
		this.moneda = moneda;
		this.monedaView = StringUtil.formatInteger(moneda);
	}
	public String getMonedaView() {
		return monedaView;
	}
	public void setMonedaView(String monedaView) {
		this.monedaView = monedaView;
	}
	public MovBanVO getMovBan() {
		return movBan;
	}
	public void setMovBan(MovBanVO movBan) {
		this.movBan = movBan;
	}
	public Long getNroCierreBanco() {
		return nroCierreBanco;
	}
	public void setNroCierreBanco(Long nroCierreBanco) {
		this.nroCierreBanco = nroCierreBanco;
		this.nroCierreBancoView = StringUtil.formatLong(nroCierreBanco);
	}
	public String getNroCierreBancoView() {
		return nroCierreBancoView;
	}
	public void setNroCierreBancoView(String nroCierreBancoView) {
		this.nroCierreBancoView = nroCierreBancoView;
	}
	public String getNroCuenta() {
		return nroCuenta;
	}
	public void setNroCuenta(String nroCuenta) {
		this.nroCuenta = nroCuenta;
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
				BalSecurityConstants.ABM_MOVBANDET, BaseSecurityConstants.CONCILIAR);
	}	
}
