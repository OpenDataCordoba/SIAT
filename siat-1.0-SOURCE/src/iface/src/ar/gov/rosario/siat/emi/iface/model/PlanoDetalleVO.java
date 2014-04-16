//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.CategoriaInmuebleVO;
import ar.gov.rosario.siat.def.iface.model.SolicitudInmuebleVO;
import ar.gov.rosario.siat.def.iface.model.SuperficieInmuebleVO;

public class PlanoDetalleVO extends SiatBussImageModel {
		 
	private static final long serialVersionUID = 1L;

	public static final String NAME	= "planoDetalleVO";

	private SolicitudInmuebleVO tipoSolicitud = new SolicitudInmuebleVO();
	
	private CategoriaInmuebleVO catInm = new CategoriaInmuebleVO();
	
	private Double supEdif;
	
	private String supEdifView = "";
	
	private SuperficieInmuebleVO supFinal = new SuperficieInmuebleVO();
	
	// Constructor
	public PlanoDetalleVO() {
	}

	// Getters y Setters
	public SolicitudInmuebleVO getTipoSolicitud() {
		return tipoSolicitud;
	}

	public void setTipoSolicitud(SolicitudInmuebleVO tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}

	public CategoriaInmuebleVO getCatInm() {
		return catInm;
	}

	public void setCatInm(CategoriaInmuebleVO catInm) {
		this.catInm = catInm;
	}

	public Double getSupEdif() {
		return supEdif;
	}

	public void setSupEdif(Double supEdif) {
		this.supEdif = supEdif;
	}

	public String getSupEdifView() {
		return supEdifView;
	}

	public void setSupEdifView(String supEdifView) {
		this.supEdifView = supEdifView;
	}

	public SuperficieInmuebleVO getSupFinal() {
		return supFinal;
	}

	public void setSupFinal(SuperficieInmuebleVO supFinal) {
		this.supFinal = supFinal;
	}
}

