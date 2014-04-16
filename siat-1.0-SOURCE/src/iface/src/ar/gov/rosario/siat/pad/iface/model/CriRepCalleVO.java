//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.ZonaVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

public class CriRepCalleVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	private RepartidorVO repartidor = new RepartidorVO();
	private ZonaVO zona = new ZonaVO();
	private CalleVO calle = new CalleVO();
	private Long nroDesde;
	private Long nroHasta;
	private Date fechaDesde;
	private Date fechaHasta;
	
	private String nroDesdeView = "";
	private String nroHastaView = "";
	private String fechaDesdeView = "";
	private String fechaHastaView = "";
	
	// Constructores
	
	public CriRepCalleVO(){
		super();
	}
	
	// Getters y Setters
	
	public CalleVO getCalle() {
		return calle;
	}
	public void setCalle(CalleVO calle) {
		this.calle = calle;
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
	public Long getNroDesde() {
		return nroDesde;
	}
	public void setNroDesde(Long nroDesde) {
		this.nroDesde = nroDesde;
		this.nroDesdeView = StringUtil.formatLong(nroDesde);
	}
	public Long getNroHasta() {
		return nroHasta;
	}
	public void setNroHasta(Long nroHasta) {
		this.nroHasta = nroHasta;
		this.nroHastaView = StringUtil.formatLong(nroHasta);
	}
	public RepartidorVO getRepartidor() {
		return repartidor;
	}
	public void setRepartidor(RepartidorVO repartidor) {
		this.repartidor = repartidor;
	}
	public ZonaVO getZona() {
		return zona;
	}
	public void setZona(ZonaVO zona) {
		this.zona = zona;
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
	public String getNroDesdeView() {
		return nroDesdeView;
	}
	public void setNroDesdeView(String nroDesdeView) {
		this.nroDesdeView = nroDesdeView;
	}
	public String getNroHastaView() {
		return nroHastaView;
	}
	public void setNroHastaView(String nroHastaView) {
		this.nroHastaView = nroHastaView;
	}

}
