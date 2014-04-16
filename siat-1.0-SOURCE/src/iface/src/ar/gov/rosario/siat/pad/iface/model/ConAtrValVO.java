//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import ar.gov.rosario.siat.def.iface.model.ConAtrVO;
import coop.tecso.demoda.iface.helper.DateUtil;

public class ConAtrValVO extends SiatBussImageModel {
	
	// Propiedades
	private static final long serialVersionUID = 1L;

	private ContribuyenteVO contribuyente = new ContribuyenteVO();
	private ConAtrVO conAtr = new ConAtrVO();
	private String valor;
	private Date fechaDesde;
	private Date fechaHasta;
	private CasoVO caso = new CasoVO();
	
	private String fechaDesdeView; 
	private String fechaHastaView;	
	
	// Constructores
	public ConAtrValVO(){
		super();
	}

	// Getters y setters	
	public CasoVO getCaso() {
		return caso;
	}
	public void setCaso(CasoVO caso) {
		this.caso = caso;
	}
	public ConAtrVO getConAtr() {
		return conAtr;
	}
	public void setConAtr(ConAtrVO conAtr) {
		this.conAtr = conAtr;
	}
	public ContribuyenteVO getContribuyente() {
		return contribuyente;
	}
	public void setContribuyente(ContribuyenteVO contribuyente) {
		this.contribuyente = contribuyente;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
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
