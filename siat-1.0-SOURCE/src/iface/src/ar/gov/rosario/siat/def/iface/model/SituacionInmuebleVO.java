//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;


import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * SituacionInmuebleVO
 * @author tecso
 *
 */
public class SituacionInmuebleVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "situacionInmuebleVO";
	
	private String desSituacionInmueble = "";
	
	// Constructores
	public SituacionInmuebleVO() {
		super();
        // Acciones y Metodos para seguridad
	}

	public SituacionInmuebleVO(int id, String desSituacionInmueble) {
		super();
		setId(new Long(id));
		setDesSituacionInmueble(desSituacionInmueble);
	}

	// Getters y Setters
	public String getDesSituacionInmueble() {
		return desSituacionInmueble;
	}

	public void setDesSituacionInmueble(String desSituacionInmueble) {
		this.desSituacionInmueble = desSituacionInmueble;
	}

}
