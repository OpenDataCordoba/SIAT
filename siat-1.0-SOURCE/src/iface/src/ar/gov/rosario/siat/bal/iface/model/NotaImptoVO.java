//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.StringUtil;


/**
 * Value Object del NotaImpto
 * @author tecso
 *
 */
public class NotaImptoVO extends SiatBussImageModel {
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "notaImptoVO";

	private CierreBancoVO cierreBanco = new CierreBancoVO();
	private Integer tipoNota;
	private Long impuesto;
	private Integer moneda;
	private String nroCuenta = "";
	private Double importe;
	private Double importeIVA;
	
	private String tipoNotaView = "";
	private String impuestoView = "";
	private String monedaView = "";
	private String importeView = "";
	private String importeIVAView = "";

	// Buss Flags


	// Constructores
	public NotaImptoVO() {
		super();
	}

	// Getters y Setters
	public CierreBancoVO getCierreBanco() {
		return cierreBanco;
	}
	public void setCierreBanco(CierreBancoVO cierreBanco) {
		this.cierreBanco = cierreBanco;
	}
	public Double getImporte() {
		return importe;
	}
	public void setImporte(Double importe) {
		this.importe = importe;
		this.importeView = StringUtil.formatDouble(importe,"#########0.00");
	}
	public Double getImporteIVA() {
		return importeIVA;
	}
	public void setImporteIVA(Double importeIVA) {
		this.importeIVA = importeIVA;
		this.importeIVAView = StringUtil.formatDouble(importeIVA);
	}
	public String getImporteIVAView() {
		return importeIVAView;
	}
	public void setImporteIVAView(String importeIVAView) {
		this.importeIVAView = importeIVAView;
	}
	public String getImporteView() {
		return importeView;
	}
	public void setImporteView(String importeView) {
		this.importeView = importeView;
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
	public String getNroCuenta() {
		return nroCuenta;
	}
	public void setNroCuenta(String nroCuenta) {
		this.nroCuenta = nroCuenta;
	}
	public Integer getTipoNota() {
		return tipoNota;
	}
	public void setTipoNota(Integer tipoNota) {
		this.tipoNota = tipoNota;
		this.tipoNotaView = StringUtil.formatInteger(tipoNota);
	}
	public String getTipoNotaView() {
		return tipoNotaView;
	}
	public void setTipoNotaView(String tipoNotaView) {
		this.tipoNotaView = tipoNotaView;
	}
	
}
