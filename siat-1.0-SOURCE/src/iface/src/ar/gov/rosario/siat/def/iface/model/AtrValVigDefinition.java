//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.Date;

import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.BussImageModel;
import coop.tecso.demoda.iface.model.Vigencia;

/**
 * Vigencia de un Valor de atributo.
 * <p>Posee el valor y las fechas de vigencia
 * 
 * @author Tecso Coop. Ltda.
 *
 */
public class AtrValVigDefinition extends BussImageModel {

	private static final long serialVersionUID = 1L;
	
	private Date fechaDesde;
	private Date fechaHasta;
	private Date fechaNovedad;
	private String valor;
	
	private String fechaDesdeView;
	private String fechaHastaView;
	private String fechaNovedadView;
	
	/**
	 * @return the fechaDesde de Vigencia
	 */
	public Date getFechaDesde() {
		return fechaDesde;
	}
	/**
	 * @param fechaDesde the fechaDesde to set
	 */
	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
		this.fechaDesdeView = DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK);
	}
	
	/**
	 * @return the fechaHasta de Vigencia
	 */
	public Date getFechaHasta() {
		return fechaHasta;
	}
	/**
	 * @param fechaHasta the fechaHasta to set
	 */
	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
		this.fechaHastaView = DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK);
	}
	
	/**
	 * @return the fechaNovedad de Vigencia
	 */
	public Date getFechaNovedad() {
		return fechaNovedad;
	}
	/**
	 * @param fechaNovedad the fechaNovedad to set
	 */
	public void setFechaNovedad(Date fechaNovedad) {
		this.fechaNovedad = fechaNovedad;
		this.fechaNovedadView = DateUtil.formatDate(fechaNovedad, DateUtil.ddSMMSYYYY_MASK);
	}
	
	/**
	 * @return the valor de Vigencia
	 */
	public String getValor() {
		return valor;
	}
	/**
	 * @param valor the valor to set
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}
	
	
	public String getFechaDesdeView() {
		return fechaDesdeView;
	}
	public void setFechaDesdeView(String fechaDesdeView) {
		this.fechaDesdeView = fechaDesdeView;
	}
	/**
	 * 
	 * Si la fecha Hasta el nula devuelve la cadena Fecha actual.
	 * 
	 * @return  fechaHastaView
	 */
	public String getFechaHastaView() {
		return fechaHastaView != "" ? fechaHastaView : "Fecha actual";
	}
	public void setFechaHastaView(String fechaHastaView) {
		this.fechaHastaView = fechaHastaView;
	}
	public String getFechaNovedadView() {
		return fechaNovedadView;
	}
	public void setFechaNovedadView(String fechaNovedadView) {
		this.fechaNovedadView = fechaNovedadView;
	}
	
	/** Si la fecha desde es menor o igual a la actual y la fecha hasta es nula
	 *  el atrVal esta vigente.
	 *  Si la fecha actual esta entre la fecha Desde y Hasta el atrVal
	 *  esta vigente.
	 *  Sino esta no vigente.
	 *  
	 */

    public Vigencia getVigencia() {

    	return Vigencia.getById(this.getVigenciaForDate(new Date()));

    }

	/** Si la fecha desde es menor o igual a la fecha a validar 
	 *  y la fecha hasta es nula el atrVal esta vigente.
	 *  Si la fecha a validar esta entre la fecha Desde y Hasta el atrVal
	 *  esta vigente.
	 *  Sino esta no vigente.
	 *  
	 */
    public Integer getVigenciaForDate(Date dateToValidate) {

    	Date fechaHastaValue = this.getFechaHasta();
    	Date fechaDesdeValue = this.getFechaDesde();

    	Integer vigencia = Vigencia.NOVIGENTE.getId();
    	
    	if(DateUtil.isDateInRange(dateToValidate, fechaDesdeValue, fechaHastaValue)){
    		vigencia = Vigencia.VIGENTE.getId();
    	}
    	
    	return vigencia;
    }

}
