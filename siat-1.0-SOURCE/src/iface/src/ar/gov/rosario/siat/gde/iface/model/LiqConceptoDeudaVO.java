//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import coop.tecso.demoda.iface.helper.StringUtil;


/**
 * Value Object del LiqConceptoDeuda
 * @author tecso
 *
 */
public class LiqConceptoDeudaVO {

	private String desConcepto="";
	private Double importe=0D;
	private Double saldo =0D;
	private String abrConcepto="";
	
	private String importeView="";
	
	private String idRecConView = "";
	
	// Getters y Setters	
	public String getDesConcepto() {
		return desConcepto;
	}
	public void setDesConcepto(String desConcepto) {
		this.desConcepto = desConcepto;
	}
	public Double getImporte() {
		return importe;
	}
	public void setImporte(Double importe) {
		this.importe = importe;
		this.importeView = StringUtil.formatDouble(importe);
	}
	public Double getSaldo() {
		return saldo;
	}
	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}
	public String getSaldoView(){
		return saldo.toString();
	}
	public String getImporteView() {
		return importeView;
	}
	public void setImporteView(String importeView) {
		this.importeView = importeView;
	}
	public String getAbrConcepto() {
		return abrConcepto;
	}
	public void setAbrConcepto(String abrConcepto) {
		this.abrConcepto = abrConcepto;
	}
	public String getIdRecConView() {
		return idRecConView;
	}
	public void setIdRecConView(String idRecConView) {
		this.idRecConView = idRecConView;
	}
	
	
}
