//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.StringUtil;


/**
 * Representa lo recaudado para un plan en un periodo
 * @author alejandro
 *
 */
public class PlanRecPeriodoVO extends SiatBussImageModel {
	
	private static final long serialVersionUID = 1L;
	
	private Long idPlan;
	private Integer cantCuotas;
	private Integer anio;
	private Integer mes;
	private String desPlan;
	private String desViaDeuda;
	private Long idProcurador;
	private String desProcurador;
	private Double totCapital;
	private Double totInteres;
	private Double totActualiz;
	private Double totImporte;
	
	// Getters y Setters
	public Long getIdPlan() {
		return idPlan;
	}
	public void setIdPlan(Long idPlan) {
		this.idPlan = idPlan;
	}
	public Integer getAnio() {
		return anio;
	}
	public void setAnio(Integer anio) {
		this.anio = anio;
	}
	public Integer getMes() {
		return mes;
	}
	public void setMes(Integer mes) {
		this.mes = mes;
	}
	public String getDesPlan() {
		return desPlan;
	}
	public void setDesPlan(String desPlan) {
		this.desPlan = desPlan;
	}
	public Double getTotCapital() {
		return totCapital;
	}
	public void setTotCapital(Double totCapital) {
		this.totCapital = totCapital;
	}
	public Double getTotInteres() {
		return totInteres;
	}
	public void setTotInteres(Double totInteres) {
		this.totInteres = totInteres;
	}
	public Double getTotActualiz() {
		return totActualiz;
	}
	public void setTotActualiz(Double totActualiz) {
		this.totActualiz = totActualiz;
	}
	public Double getTotImporte() {
		return totImporte;
	}
	public void setTotImporte(Double totImporte) {
		this.totImporte = totImporte;
	}
	
	public String getDesViaDeuda() {
		return desViaDeuda;
	}
	public void setDesViaDeuda(String desViaDeuda) {
		this.desViaDeuda = desViaDeuda;
	}
	
	
	public void setIdProcurador(Long idProcurador) {
		this.idProcurador = idProcurador;
	}
	public Long getIdProcurador() {
		return idProcurador;
	}
	public void setDesProcurador(String desProcurador) {
		this.desProcurador = desProcurador;
	}
	public String getDesProcurador() {
		return desProcurador;
	}
	public Integer getCantCuotas() {
		return cantCuotas;
	}
	public void setCantCuotas(Integer cantCuotas) {
		this.cantCuotas = cantCuotas;
	}
	// View Getters
	public String getPeriodoView(){
		return mes+"/"+anio;
	}
	
	public String getTotCapitalView(){
		return StringUtil.parsePointToComa(StringUtil.redondearDecimales(totCapital, 1, 2));
	}
	
	public String getTotInteresView(){
		return StringUtil.parsePointToComa(StringUtil.redondearDecimales(totInteres, 1, 2));
	}
	
	public String getTotActualizView(){
		return StringUtil.parsePointToComa(StringUtil.redondearDecimales(totActualiz, 1, 2));
	}
	
	public String getTotImporteView(){
		return StringUtil.parsePointToComa(StringUtil.redondearDecimales(totImporte, 1, 2));
	}
}
