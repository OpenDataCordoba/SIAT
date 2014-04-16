//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Search Page de CierreBanco
 * @author tecso
 *
 */
public class CierreBancoSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "cierreBancoSearchPageVO";

	private CierreBancoVO cierreBanco = new CierreBancoVO();
	
	private Date fechaDesde;
	private Date fechaHasta;
		
	private Boolean conciliarBussEnabled    = true;
	
	public CierreBancoSearchPage(){
		super(BalSecurityConstants.ABM_CIERREBANCO);
	}

	// Getters y Setters
	public CierreBancoVO getCierreBanco() {
		return cierreBanco;
	}
	public void setCierreBanco(CierreBancoVO cierreBanco) {
		this.cierreBanco = cierreBanco;
	}
	public Date getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}
	public Date getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}	
	public String getFechaDesdeView(){
		return (this.fechaDesde!=null)?DateUtil.formatDate(this.fechaDesde, DateUtil.ddSMMSYYYY_MASK):"";
	}
	public String getFechaHastaView(){
		return (this.fechaHasta!=null)?DateUtil.formatDate(this.fechaHasta, DateUtil.ddSMMSYYYY_MASK):"";
	}

	public String getName(){
		return NAME;
	}
	
	//	Flags Seguridad
		
	public Boolean getConciliarBussEnabled() {
		return conciliarBussEnabled;
	}

	public void setConciliarBussEnabled(Boolean conciliarBussEnabled) {
		this.conciliarBussEnabled = conciliarBussEnabled;
	}

	public String getConciliarEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getConciliarBussEnabled(), 
				BalSecurityConstants.ABM_CIERREBANCO, BaseSecurityConstants.CONCILIAR);
	}
	
}
