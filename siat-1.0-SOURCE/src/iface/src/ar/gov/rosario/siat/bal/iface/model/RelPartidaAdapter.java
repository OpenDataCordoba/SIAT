//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;

/**
 * Adapter de RelPartida (Relacion de Nodo con Partida)
 * 
 * @author tecso
 */
public class RelPartidaAdapter extends SiatAdapterModel {
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "relPartidaAdapterVO";
	
	private RelPartidaVO relPartida = new RelPartidaVO();
	
	private List<PartidaVO> listPartida = new ArrayList<PartidaVO>();

	public RelPartidaAdapter(){
		super(BalSecurityConstants.ABM_RELPARTIDA);
	}

	// Getters y Setters
	
	public List<PartidaVO> getListPartida() {
		return listPartida;
	}
	public void setListPartida(List<PartidaVO> listPartida) {
		this.listPartida = listPartida;
	}
	public RelPartidaVO getRelPartida() {
		return relPartida;
	}
	public void setRelPartida(RelPartidaVO relPartida) {
		this.relPartida = relPartida;
	}

	
}
