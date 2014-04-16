//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Indica los valores de un minimo de un recurso autodeclarativo.
 *
 * @author tecso
 *
 */
public class RecMinDecVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "recMinDecVO";

	private RecursoVO recurso = new RecursoVO();
	
	private Double minimo;
	
	private Double valRefDes;
	
	private Double valRefHas;
	
	private Date fechaDesde;
	
	private Date fechaHasta;
	
	// View
	
	private String minimoView="";
	private String valRefDesView;
	private String valRefHasView;
	private String fechaDesdeView;
	private String fechaHastaView;
	
	
	// Constructores
	public RecMinDecVO(){
		super();
	}
	public RecMinDecVO(int id, String desc) {
		super();
		setId(new Long(id));
		setMinimoView(desc);
	}
	
	// Getters y Setters
	public RecursoVO getRecurso(){
		return recurso;
	}
	public void setRecurso(RecursoVO recurso){
		this.recurso = recurso;
	}
	public Double getMinimo() {
		return minimo;
	}
	public void setMinimo(Double minimo) {
		this.minimo = minimo;
		this.minimoView = StringUtil.formatDouble(minimo);
	}
	public Double getValRefDes() {
		return valRefDes;
	}
	public void setValRefDes(Double valRefDes) {
		this.valRefDes = valRefDes;
		this.valRefDesView = StringUtil.formatDouble(valRefDes);
	}
	public Double getValRefHas() {
		return valRefHas;
	}
	public void setValRefHas(Double valRefHas) {
		this.valRefHas = valRefHas;
		this.valRefHasView = StringUtil.formatDouble(valRefHas);
	}
	public Date getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
		this.fechaDesdeView = DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK);
	}
	public Date getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
		this.fechaHastaView = DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK);
	}
	

	// View
	
	public String getMinimoView() {
		return minimoView;
	}
	public void setMinimoView(String minimoView) {
		this.minimoView = minimoView;
	}
	public String getValRefDesView() {
		return valRefDesView;
	}
	public void setValRefDesView(String valRefDesView) {
		this.valRefDesView = valRefDesView;
	}
	public String getValRefHasView() {
		return valRefHasView;
	}
	public void setValRefHasView(String valRefHasView) {
		this.valRefHasView = valRefHasView;
	}
	public String getFechaDesdeView() {
		return fechaDesdeView;
	}
	public void setFechaDesdeView(String fechaDesdeView) {
		this.fechaDesdeView = fechaDesdeView;
	}
	public String getFechaHastaView() {
		return fechaHastaView;
	}
	public void setFechaHastaView(String fechaHastaView) {
		this.fechaHastaView = fechaHastaView;
	}
	
	

}
