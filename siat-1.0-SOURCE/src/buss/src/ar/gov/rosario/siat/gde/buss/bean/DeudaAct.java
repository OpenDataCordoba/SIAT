//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

public class DeudaAct {
	private double importeOrig = 0.0;
	private double importeAct = 0.0;
	private double recargo = 0.0;
	private double coeficiente = 0.0;

	public DeudaAct() {
	}
	
	public DeudaAct(double importeOrig, double importeAct, double recargo, double coeficiente) {
		this.importeOrig = importeOrig;
		this.importeAct = importeAct;
		this.recargo = recargo;
		this.coeficiente = coeficiente;
	}
	
	/**
	 * @return coeficiente utilizado para calcular el recargo.
	 */
	public double getCoeficiente() {
		return coeficiente;
	}
	/**
	 * @param coeficiente the coeficiente to set
	 */
	public void setCoeficiente(double coeficiente) {
		this.coeficiente = coeficiente;
	}
	/**
	 * @return Importe orignal pasado como parametro a la funcion de actualizacion
	 */
	public double getImporteOrig() {
		return importeOrig;
	}
	/**
	 * @param importeOrig the importe to set
	 */
	public void setImporte(double importeOrig) {
		this.importeOrig = importeOrig;
	}
	/**
	 * @return Importe actualizado. Es el ImporteOrignal mas el Recargo
	 */
	public double getImporteAct() {
		return importeAct;
	}
	/**
	 * @param importeAct the importeAct to set
	 */
	public void setImporteAct(double importeAct) {
		this.importeAct = importeAct;
	}
	/**
	 * @return Recargo calculado apartir del ImporteOriginal y de la Fecha de Actualizacion.
	 */
	public double getRecargo() {
		return recargo;
	}
	/**
	 * @param recargo the recargo to set
	 */
	public void setRecargo(double recargo) {
		this.recargo = recargo;
	}
	
	/**
	 * Aplica el porcentaje de descuento indicado al valor de recargo, y actualiza el importe actualizado. 
	 * @param porcentaje
	 */
	public void aplicarDescuento(Double porcentaje){
		this.recargo = this.recargo * (1 - porcentaje);
		this.importeAct = this.importeOrig + this.recargo;	
	}
}
