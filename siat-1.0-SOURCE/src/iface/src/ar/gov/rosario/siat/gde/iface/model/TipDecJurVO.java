//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del TipoDeudaPlan
 * @author tecso
 *
 */
public class TipDecJurVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipDecJurVO";
	
	private String desTipo="";
	
	private String abreviatura="";
	

	
	// Constructores
	public TipDecJurVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public TipDecJurVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesTipo(desc);
	}
	
	

	
	// Getters y Setters
	public String getDesTipo() {
		return desTipo;
	}

	public void setDesTipo(String desTipo) {
		this.desTipo = desTipo;
	}

	public String getAbreviatura() {
		return abreviatura;
	}

	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}



	

	
	
	
	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
