//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import coop.tecso.demoda.iface.helper.NumberUtil;

/**
 * Value Object auxiliar para el Reporte de Recaudacion
 * @author tecso
 *
 */
public class RecaudacionForReportVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "recaudacionForReportVO";

	public static final Integer EMITIDO = 1;
	public static final Integer RECAUDADO_VTO  = 2;
	public static final Integer RECAUDADO_GRAL = 3;
	public static final Integer IMPAGO = 4;
	
	private RecursoVO recurso = new RecursoVO();
	private Long anio;
	private Long periodo;
	private Double totalEmitido;         // No inicializar en cero
	private Double totalRecaudadoVto;
	private Double totalRecaudadoGral;
	private Double totalImpago;
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public RecaudacionForReportVO() {
		super();
	}

	// Getters y Setters
	
	public Long getAnio() {
		return anio;
	}
	public void setAnio(Long anio) {
		this.anio = anio;
	}
	public Long getPeriodo() {
		return periodo;
	}
	public void setPeriodo(Long mes) {
		this.periodo = mes;
	}
	public Double getTotalEmitido() {
		return totalEmitido;
	}
	public void setTotalEmitido(Double totalEmitido) {
		this.totalEmitido = totalEmitido;
	}
	public void addTotalEmitido(Double totalEmitido) {
		this.totalEmitido = NumberUtil.addDoubles(this.totalEmitido, totalEmitido);
	}
	public Double getTotalImpago() {
		return totalImpago;
	}
	public void setTotalImpago(Double totalImpago) {
		this.totalImpago = totalImpago;
	}
	public void addTotalImpago(Double totalImpago) {
		this.totalImpago = NumberUtil.addDoubles(this.totalImpago, totalImpago);
	}
	public Double getTotalRecaudadoGral() {
		return totalRecaudadoGral;
	}
	public void setTotalRecaudadoGral(Double totalRecaudadoGral) {
		this.totalRecaudadoGral = totalRecaudadoGral;
	}
	public void addTotalRecaudadoGral(Double totalRecaudadoGral) {
		this.totalRecaudadoGral = NumberUtil.addDoubles(this.totalRecaudadoGral, totalRecaudadoGral);
	}
	public Double getTotalRecaudadoVto() {
		return totalRecaudadoVto;
	}
	public void setTotalRecaudadoVto(Double totalRecaudadoVto) {
		this.totalRecaudadoVto = totalRecaudadoVto;
	}
	public void addTotalRecaudadoVto(Double totalRecaudadoVto) {
		this.totalRecaudadoVto = NumberUtil.addDoubles(this.totalRecaudadoVto, totalRecaudadoVto);		
	}
	public RecursoVO getRecurso() {
		return recurso;
	}
	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}
	public String getClave(){
		return this.getRecurso().getId() + "-" + getAnioPeriodo();
	}
	
	public String getAnioPeriodo(){
		return this.getAnio() + "-" +this.getPeriodo();
	}

	
	public Double getPorcRecVto(){
		if(this.getTotalRecaudadoVto() != null && this.getTotalEmitido() != null){
			return this.getTotalRecaudadoVto()/ this.getTotalEmitido();
		}else 
			return null;
	}
	
	public Double getPorcRecGral(){
		if(this.getTotalRecaudadoGral() != null && this.getTotalEmitido() != null){
			return this.getTotalRecaudadoGral()/ this.getTotalEmitido();
		}else 
			return null;
	}
	
	public Double getPorcImpago(){
		if(this.getTotalImpago() != null && this.getTotalEmitido() != null){
			return this.getTotalImpago()/ this.getTotalEmitido();
		}else 
			return null;
	}
	
	public void addTotal(Double total, Integer alternativa) {
		if (EMITIDO.equals(alternativa)){
			this.addTotalEmitido(total);
		}else if (RECAUDADO_VTO.equals(alternativa)){
			this.addTotalRecaudadoVto(total);
		}else if (RECAUDADO_GRAL.equals(alternativa)){
			this.addTotalRecaudadoGral(total);
		}else if (IMPAGO.equals(alternativa)){
			this.addTotalImpago(total);
		} 
	}

}
