//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;

public class ConstanciaDeuPrint extends SiatAdapterModel{

	private static final long serialVersionUID = 1L;

	private Long nroConstancia;
	private String desTributo;
	private String cuit;
	private String desdomicilio;

	private String nroCuenta;
	private String desTitularPrincipal;
	private String desDomicilioConstancia;
	private String catastral;
	private Long idProcurador;
	private String desProcurador;
	private List<ConDeuDetPrint> listConDeuDetPrint = new ArrayList<ConDeuDetPrint>(); 

	public ConstanciaDeuPrint(){
		super("");
	}

	public String getCatastral() {
		return catastral;
	}
	public void setCatastral(String catastral) {
		this.catastral = catastral;
	}
	public String getCuit() {
		return cuit;
	}
	public void setCuit(String cuit) {
		this.cuit = cuit;
	}
	public String getDesdomicilio() {
		return desdomicilio;
	}
	public void setDesdomicilio(String desdomicilio) {
		this.desdomicilio = desdomicilio;
	}
	public String getDesDomicilioConstancia() {
		return desDomicilioConstancia;
	}
	public void setDesDomicilioConstancia(String desDomicilioConstancia) {
		this.desDomicilioConstancia = desDomicilioConstancia;
	}
	public String getDesProcurador() {
		return desProcurador;
	}
	public void setDesProcurador(String desProcurador) {
		this.desProcurador = desProcurador;
	}
	public String getDesTitularPrincipal() {
		return desTitularPrincipal;
	}
	public void setDesTitularPrincipal(String desTitularPrincipal) {
		this.desTitularPrincipal = desTitularPrincipal;
	}
	public String getDesTributo() {
		return desTributo;
	}
	public void setDesTributo(String desTributo) {
		this.desTributo = desTributo;
	}
	public Long getIdProcurador() {
		return idProcurador;
	}
	public void setIdProcurador(Long idProcurador) {
		this.idProcurador = idProcurador;
	}
	public List<ConDeuDetPrint> getListConDeuDetPrint() {
		return listConDeuDetPrint;
	}
	public void setListConDeuDetPrint(List<ConDeuDetPrint> listConDeuDetPrint) {
		this.listConDeuDetPrint = listConDeuDetPrint;
	}
	public Long getNroConstancia() {
		return nroConstancia;
	}
	public void setNroConstancia(Long nroConstancia) {
		this.nroConstancia = nroConstancia;
	}
	public String getNroCuenta() {
		return nroCuenta;
	}
	public void setNroCuenta(String nroCuenta) {
		this.nroCuenta = nroCuenta;
	}

	public void addConDeuDetPrint(ConDeuDetPrint conDeuDetPrint){
		this.getListConDeuDetPrint().add(conDeuDetPrint);
	}
}


