//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.esp.iface.util.EspSecurityConstants;

/**
 * Adapter de PrecioEvento
 * 
 * @author tecso
 */
public class PrecioEventoAdapter extends SiatAdapterModel {

	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "precioEventoAdapterVO";

	PrecioEventoVO precioEvento = new PrecioEventoVO();
	
	private List<TipoEntradaVO> listTipoEntrada = new ArrayList<TipoEntradaVO>();
		
	// Flags
	private boolean paramTipoInterna = false;
	
	public PrecioEventoAdapter(){
		super(EspSecurityConstants.ABM_PRECIOEVENTO);
	}

	// Getters & Setters
	public List<TipoEntradaVO> getListTipoEntrada() {
		return listTipoEntrada;
	}
	public void setListTipoEntrada(List<TipoEntradaVO> listTipoEntrada) {
		this.listTipoEntrada = listTipoEntrada;
	}
	public PrecioEventoVO getPrecioEvento() {
		return precioEvento;
	}
	public void setPrecioEvento(PrecioEventoVO precioEvento) {
		this.precioEvento = precioEvento;
	}
	public boolean isParamTipoInterna() {
		return paramTipoInterna;
	}
	public void setParamTipoInterna(boolean paramTipoInterna) {
		this.paramTipoInterna = paramTipoInterna;
	}
	
}
