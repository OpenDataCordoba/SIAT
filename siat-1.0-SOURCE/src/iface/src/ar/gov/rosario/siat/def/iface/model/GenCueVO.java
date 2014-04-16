//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Indica la forma en que se pueden generar los numeros de cuenta.
 * @author tecso
 *
 */
public class GenCueVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "genCueVO";

	private String codGenCue;
	private String desGenCue;
	
	// Constructores
	public GenCueVO(){
		super();
	}

	public GenCueVO(int id, String desGenCue) {
		super(id);
		setDesGenCue(desGenCue);
	}

	
	// Getters y Setters
	public String getCodGenCue(){
		return codGenCue;
	}
	public void setCodGenCue(String codGenCue){
		this.codGenCue = codGenCue;
	}
	public String getDesGenCue(){
		return desGenCue;
	}
	public void setDesGenCue(String desGenCue){
		this.desGenCue = desGenCue;
	}
}
