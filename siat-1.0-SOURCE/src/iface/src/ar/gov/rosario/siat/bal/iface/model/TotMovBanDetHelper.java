//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.Date;

import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 *  Clase Helper para Mostrar Valores Totalizados de MovBanDet para determinado CierreBanco agrupados por impuesto y cuenta. 
 *  (Tambien se utiliza para subtotales por Cuenta)
 * 
 * @author pgp
 *
 */
public class TotMovBanDetHelper {

	private Long impuesto;	
	private String nroCuenta;
	private Double debito;
	private Double credito;
	private Date fechaAcredit;
	
	private String impuestoView = "";
	private String debitoView = "";
	private String creditoView = "";
	private String fechaAcreditView = ""; 
	
	// Contructores
	public TotMovBanDetHelper() {
		super();
	}
	
	public TotMovBanDetHelper(Date fechaAcredit,Long impuesto, String nroCuenta, Double debito, Double credito) {
		super();
		this.setFechaAcredit(fechaAcredit);
		this.setImpuesto(impuesto);
		this.setNroCuenta(nroCuenta);
		this.setDebito(debito);
		this.setCredito(credito);
	}
	
	/**
	 *  Contructor para Subtotales por Cuenta: 
	 *  
	 *  Numero Cuenta, Subtotal Debito, Subtotal Credito
	 * 
	 * @param nroCuenta
	 * @param debito
	 * @param credito
	 */
	public TotMovBanDetHelper(String nroCuenta, Double debito, Double credito) {
		super();
		this.setNroCuenta(nroCuenta);
		this.setDebito(debito);
		this.setCredito(credito);
	}

	// Getters Y Setters
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
	public String getNroCuenta() {
		return nroCuenta;
	}
	public void setNroCuenta(String nroCuenta) {
		this.nroCuenta = nroCuenta;
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
	
}
