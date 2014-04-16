//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;

public class RelClaVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "relClaVO";
	
	private NodoVO nodo1 = new NodoVO();
	private NodoVO nodo2 = new NodoVO();
	private Date fechaDesde; 
	private Date fechaHasta; 

	private String fechaDesdeView = "";
	private String fechaHastaView = "";
	
	//Constructores 
	public RelClaVO(){
		super();
	}

	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public RelClaVO(int id, String descripcion) {
		super();
		setId(new Long(id));
		//setDescripcion(descripcion);
	}

	// Getters y Setters
	public NodoVO getNodo1() {
		return nodo1;
	}
	public void setNodo1(NodoVO nodo1) {
		this.nodo1 = nodo1;
	}
	public NodoVO getNodo2() {
		return nodo2;
	}
	public void setNodo2(NodoVO nodo2) {
		this.nodo2 = nodo2;
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
