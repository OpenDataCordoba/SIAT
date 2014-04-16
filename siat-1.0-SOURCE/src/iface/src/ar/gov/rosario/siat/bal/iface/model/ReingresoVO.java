//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.StringUtil;

public class ReingresoVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;
	
	private BalanceVO balance = new BalanceVO();
	private Long nroReingreso;
	private Double importePago;
	
	private String nroReingresoView;
	private String importePagoView;
	
	private IndetVO indet = new IndetVO();
	
	//Constructores 
	public ReingresoVO(){
		super();
	}

	// Getters Y Setters
	public BalanceVO getBalance() {
		return balance;
	}
	public void setBalance(BalanceVO balance) {
		this.balance = balance;
	}
	public Double getImportePago() {
		return importePago;
	}
	public void setImportePago(Double importePago) {
		this.importePago = importePago;
		this.importePagoView = StringUtil.formatDouble(importePago);
	}
	public String getImportePagoView() {
		return importePagoView;
	}
	public void setImportePagoView(String importePagoView) {
		this.importePagoView = importePagoView;
	}
	public Long getNroReingreso() {
		return nroReingreso;
	}
	public void setNroReingreso(Long nroReingreso) {
		this.nroReingreso = nroReingreso;
		this.nroReingresoView = StringUtil.formatLong(nroReingreso);
	}
	public String getNroReingresoView() {
		return nroReingresoView;
	}
	public void setNroReingresoView(String nroReingresoView) {
		this.nroReingresoView = nroReingresoView;
	}
	public IndetVO getIndet() {
		return indet;
	}
	public void setIndet(IndetVO indet) {
		this.indet = indet;
	}
	
	
}
