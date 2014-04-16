//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Indica la forma en que se pueden generar los codigos de gestion personal.
 * @author tecso
 *
 */
public class GenCodGesVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "genCodGesVO";

	private String codGenCodGes;
	private String desGenCodGes;
	
	// Constructores
	public GenCodGesVO(){
		super();
	}
	public GenCodGesVO(int id, String desGenCodGes) {
		super(id);
		setDesGenCodGes(desGenCodGes);
	}

	
	// Getters y Setters
	public String getCodGenCodGes(){
		return codGenCodGes;
	}
	public void setCodGenCodGes(String codGenCodGes){
		this.codGenCodGes = codGenCodGes;
	}
	public String getDesGenCodGes(){
		return desGenCodGes;
	}
	public void setDesGenCodGes(String desGenCodGes){
		this.desGenCodGes = desGenCodGes;
	}

}
