//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;

public class RelPartidaVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "relPartidaVO";
	
	private NodoVO nodo = new NodoVO();
	private PartidaVO partida = new PartidaVO();
	private Date fechaDesde; 
	private Date fechaHasta; 

	private String fechaDesdeView = "";
	private String fechaHastaView = "";

	//Constructores 
	public RelPartidaVO(){
		super();
	}

	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public RelPartidaVO(int id, String descripcion) {
		super();
		setId(new Long(id));
		//setDescripcion(descripcion);
	}

	// Getters y Setters
	public NodoVO getNodo() {
		return nodo;
	}
	public void setNodo(NodoVO nodo) {
		this.nodo = nodo;
	}
	public PartidaVO getPartida() {
		return partida;
	}
	public void setPartida(PartidaVO partida) {
		this.partida = partida;
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
