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
 * Adapter de Entradas Habilitadas
 * 
 * @author tecso
 */
public class EntHabAdapter extends SiatAdapterModel {

	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "entHabAdapterVO";

	EntHabVO entHab = new EntHabVO();
	
	private List<PrecioEventoVO> listPrecioEvento = new ArrayList<PrecioEventoVO>();
	
	public EntHabAdapter(){
		super(EspSecurityConstants.ABM_ENTHAB);
	}

	// Getters & Setters
	public EntHabVO getEntHab() {
		return entHab;
	}
	public void setEntHab(EntHabVO entHab) {
		this.entHab = entHab;
	}
	public List<PrecioEventoVO> getListPrecioEvento() {
		return listPrecioEvento;
	}
	public void setListPrecioEvento(List<PrecioEventoVO> listPrecioEvento) {
		this.listPrecioEvento = listPrecioEvento;
	}
	

}
