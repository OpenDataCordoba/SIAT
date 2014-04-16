//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.SeccionVO;
import coop.tecso.demoda.iface.helper.DateUtil;

public class CriRepCatVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	private RepartidorVO repartidor = new RepartidorVO();
	private SeccionVO seccion = new SeccionVO();
	private String catastralDesde="";
	private String catastralHasta="";
	private Date fechaDesde;
	private Date fechaHasta;
	
	private String fechaDesdeView = "";
	private String fechaHastaView = "";

	// Constructores
	
	public CriRepCatVO(){
		super();
	}

	// Getters y Setters
	
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
	public String getCatastralDesde() {
		return catastralDesde;
	}
	public void setCatastralDesde(String catastralDesde) {
		this.catastralDesde = catastralDesde;		
	}
	public String getCatastralHasta() {
		return catastralHasta;
	}
	public void setCatastralHasta(String catastralHasta) {
		this.catastralHasta = catastralHasta;
		
	}
	public RepartidorVO getRepartidor() {
		return repartidor;
	}
	public void setRepartidor(RepartidorVO repartidor) {
		this.repartidor = repartidor;
	}
	public SeccionVO getSeccion() {
		return seccion;
	}
	public void setSeccion(SeccionVO seccion) {
		this.seccion = seccion;
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
