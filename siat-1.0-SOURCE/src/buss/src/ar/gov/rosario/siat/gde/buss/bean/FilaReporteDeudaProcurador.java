//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;


/**
 * Corresponde a una fila del reporte de deudas por procurador
 * @author alejandro
 *
 */
public class FilaReporteDeudaProcurador {
	private String idProcurador;
	private String desProcurador;
	private String zona;
	private String seccion;
	private String nroCuenta;
	private String strPeriodoAnio="";
	
	private Double importehist;
	private Double importeAct;
	private Double saldoHist;
	private Double saldoAct; 
	

	
	public String getIdProcurador() {
		return idProcurador;
	}
	public void setIdProcurador(String idProcurador) {
		this.idProcurador = idProcurador;
	}
	public String getDesProcurador() {
		return desProcurador;
	}
	public void setDesProcurador(String desProcurador) {
		this.desProcurador = desProcurador;
	}
	public String getZona() {
		return zona;
	}
	public void setZona(String zona) {
		this.zona = zona;
	}
	public String getSeccion() {
		return seccion;
	}
	public void setSeccion(String seccion) {
		this.seccion = seccion;
	}
	public String getStrPeriodoAnio() {
		return strPeriodoAnio;
	}
	public void setStrPeriodoAnio(String strPeriodoAnio) {
		this.strPeriodoAnio = strPeriodoAnio;
	}
	public Double getImportehist() {
		return importehist;
	}
	public void setImportehist(Double importehist) {
		this.importehist = importehist;
	}
	public Double getImporteAct() {
		return importeAct;
	}
	public void setImporteAct(Double importeAct) {
		this.importeAct = importeAct;
	}
	public Double getSaldoHist() {
		return saldoHist;
	}
	public void setSaldoHist(Double saldoHist) {
		this.saldoHist = saldoHist;
	}
	public Double getSaldoAct() {
		return saldoAct;
	}
	public void setSaldoAct(Double saldoAct) {
		this.saldoAct = saldoAct;
	}
	public String getNroCuenta() {
		return nroCuenta;
	}
	public void setNroCuenta(String nroCuenta) {
		this.nroCuenta = nroCuenta;
	}
	
}
