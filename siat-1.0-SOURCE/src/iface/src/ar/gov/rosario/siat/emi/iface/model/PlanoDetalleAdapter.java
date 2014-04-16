//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.model.CategoriaInmuebleVO;
import ar.gov.rosario.siat.def.iface.model.SolicitudInmuebleVO;
import ar.gov.rosario.siat.def.iface.model.SuperficieInmuebleVO;

public class PlanoDetalleAdapter extends SiatAdapterModel {
		 
	private static final long serialVersionUID = 1L;

	public static final String NAME	= "planoDetalleAdapterVO";

	private PlanoDetalleVO planoDetalle = new PlanoDetalleVO();
	
	private List<SolicitudInmuebleVO> listSolicitudInmueble = new ArrayList<SolicitudInmuebleVO>();
	
	private List<CategoriaInmuebleVO> listCategoriaInmueble = new ArrayList<CategoriaInmuebleVO>();
	
	private List<SuperficieInmuebleVO> listSuperficieInmueble = new ArrayList<SuperficieInmuebleVO>();
	
	// Constructor
	public PlanoDetalleAdapter() {
	}


	// Getters y Setters
	public PlanoDetalleVO getPlanoDetalle() {
		return planoDetalle;
	}

	public void setPlanoDetalle(PlanoDetalleVO planoDetalle) {
		this.planoDetalle = planoDetalle;
	}

	public List<SolicitudInmuebleVO> getListSolicitudInmueble() {
		return listSolicitudInmueble;
	}

		public void setListSolicitudInmueble(
			List<SolicitudInmuebleVO> listSolicitudInmueble) {
		this.listSolicitudInmueble = listSolicitudInmueble;
	}

	public List<CategoriaInmuebleVO> getListCategoriaInmueble() {
		return listCategoriaInmueble;
	}

	public void setListCategoriaInmueble(
			List<CategoriaInmuebleVO> listCategoriaInmueble) {
		this.listCategoriaInmueble = listCategoriaInmueble;
	}

	public List<SuperficieInmuebleVO> getListSuperficieInmueble() {
		return listSuperficieInmueble;
	}

	public void setListSuperficieInmueble(
			List<SuperficieInmuebleVO> listSuperficieInmueble) {
		this.listSuperficieInmueble = listSuperficieInmueble;
	}
}

