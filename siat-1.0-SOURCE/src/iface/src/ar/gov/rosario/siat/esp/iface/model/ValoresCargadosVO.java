//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.iface.model;

import ar.gov.rosario.siat.bal.iface.model.PartidaVO;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

public class ValoresCargadosVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;
	
	private String descripcion = "";
	private PartidaVO partida = new PartidaVO();

	//Constructores 
	public ValoresCargadosVO(){
		super();
	}

	// Getters & Setters
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public PartidaVO getPartida() {
		return partida;
	}
	public void setPartida(PartidaVO partida) {
		this.partida = partida;
	}
	
}
