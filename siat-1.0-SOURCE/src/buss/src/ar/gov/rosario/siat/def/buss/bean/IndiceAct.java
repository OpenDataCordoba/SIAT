//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

import java.util.Calendar;
import java.util.Date;

/**
 * Representa un Indice de Acualizacion Diario o Mensual 
 * 
 * @author Tecso Coop. Ltda.
 */
public class IndiceAct {

	public static final Date MAX_DATE = maxDate();
	
	private int mensualAnioApli = -1;
	private int mensualMesApli = -1;
	private int mensualAnioVen = -1;
	private int mensualMesVen = -1;
	private double mensualIndiceMin = -1;
	private double mensualIndiceMax = -1;

	private Date diarioFechaDesde = MAX_DATE;
	private Date diarioFechaHasta = MAX_DATE;
	private double diarioCoeficiente = -1;

	/** Fecha arbitraria lo sufientemente alta para 
	 * interpretar que es una fecha de aqui a la eternidad 
	*/
	protected static Date maxDate() {
		Calendar cal = Calendar.getInstance();
		cal.set(9999, 01, 01);
		return cal.getTime();
	}

	/**
	 * @return the diarioCoeficiente
	 */
	public double getDiarioCoeficiente() {
		return diarioCoeficiente;
	}

	/**
	 * @param diarioCoeficiente the diarioCoeficiente to set
	 */
	public void setDiarioCoeficiente(double diarioCoeficiente) {
		this.diarioCoeficiente = diarioCoeficiente;
	}

	/**
	 * @return the diarioFechaDesde
	 */
	public Date getDiarioFechaDesde() {
		return diarioFechaDesde;
	}

	/**
	 * @param diarioFechaDesde the diarioFechaDesde to set
	 */
	public void setDiarioFechaDesde(Date diarioFechaDesde) {
		this.diarioFechaDesde = diarioFechaDesde;
	}

	/**
	 * @return the diarioFechaHasta
	 */
	public Date getDiarioFechaHasta() {
		return diarioFechaHasta;
	}

	/**
	 * @param diarioFechaHasta the diarioFechaHasta to set
	 */
	public void setDiarioFechaHasta(Date diarioFechaHasta) {
		this.diarioFechaHasta = diarioFechaHasta;
	}

	/**
	 * @return the mensualAnioAapli
	 */
	public int getMensualAnioApli() {
		return mensualAnioApli;
	}

	/**
	 * @param mensualAnioAapli the mensualAnioAapli to set
	 */
	public void setMensualAnioApli(int mensualAnioApli) {
		this.mensualAnioApli = mensualAnioApli;
	}

	/**
	 * @return the mensualAnioVen
	 */
	public int getMensualAnioVen() {
		return mensualAnioVen;
	}

	/**
	 * @param mensualAnioVen the mensualAnioVen to set
	 */
	public void setMensualAnioVen(int mensualAnioVen) {
		this.mensualAnioVen = mensualAnioVen;
	}

	/**
	 * @return the mensualIndiceMax
	 */
	public double getMensualIndiceMax() {
		return mensualIndiceMax;
	}

	/**
	 * @param mensualIndiceMax the mensualIndiceMax to set
	 */
	public void setMensualIndiceMax(double mensualIndiceMax) {
		this.mensualIndiceMax = mensualIndiceMax;
	}

	/**
	 * @return the mensualIndiceMin
	 */
	public double getMensualIndiceMin() {
		return mensualIndiceMin;
	}

	/**
	 * @param mensualIndiceMin the mensualIndiceMin to set
	 */
	public void setMensualIndiceMin(double mensualIndiceMin) {
		this.mensualIndiceMin = mensualIndiceMin;
	}

	/**
	 * @return the mensualMesApli
	 */
	public int getMensualMesApli() {
		return mensualMesApli;
	}

	/**
	 * @param mensualMesApli the mensualMesApli to set
	 */
	public void setMensualMesApli(int mensualMesApli) {
		this.mensualMesApli = mensualMesApli;
	}

	/**
	 * @return the mensualMesVen
	 */
	public int getMensualMesVen() {
		return mensualMesVen;
	}

	/**
	 * @param mensualMesVen the mensualMesVen to set
	 */
	public void setMensualMesVen(int mensualMesVen) {
		this.mensualMesVen = mensualMesVen;
	}
}
