//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

public class EstEjercicioVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	private String desEjeBal;
	
	//Constructores 	
	public EstEjercicioVO(){
		super();
	}

	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public EstEjercicioVO(int id, String desEjeBal) {
		super();
		setId(new Long(id));
		setDesEjeBal(desEjeBal);
	}
	// Getters y Setters
	public String getDesEjeBal() {
		return desEjeBal;
	}
	public void setDesEjeBal(String desEjeBal) {
		this.desEjeBal = desEjeBal;
	}

}
