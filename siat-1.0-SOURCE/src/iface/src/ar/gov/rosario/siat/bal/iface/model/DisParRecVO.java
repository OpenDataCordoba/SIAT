//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.ViaDeudaVO;
import coop.tecso.demoda.iface.helper.DateUtil;

public class DisParRecVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	private DisParVO disPar = new DisParVO();
	private RecursoVO recurso = new RecursoVO();
	private ViaDeudaVO viaDeuda = new ViaDeudaVO();
	private String valor;
	private Date fechaDesde;
	private Date fechaHasta;
	private CasoVO caso = new CasoVO();
	
	private String fechaDesdeView = "";
	private String fechaHastaView = "";
	private String valorView = "";

	//Constructores 
	public DisParRecVO(){
		super();
	}
	// Getters y Setters

	public DisParVO getDisPar() {
		return disPar;
	}
	public void setDisPar(DisParVO disPar) {
		this.disPar = disPar;
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
	public CasoVO getCaso() {
		return caso;
	}
	public void setCaso(CasoVO caso) {
		this.caso = caso;
	}
	public RecursoVO getRecurso() {
		return recurso;
	}
	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public ViaDeudaVO getViaDeuda() {
		return viaDeuda;
	}
	public void setViaDeuda(ViaDeudaVO viaDeuda) {
		this.viaDeuda = viaDeuda;
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
	public String getValorView() {
		return valorView;
	}
	public void setValorView(String valorView) {
		this.valorView = valorView;
	}
	
}
