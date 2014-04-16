//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;


import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * CategoriaInmuebleVO
 * @author tecso
 *
 */
public class CategoriaInmuebleVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "categoriaInmuebleVO";
	
	private String desCategoriaInmueble = "";
	
	// Constructores
	public CategoriaInmuebleVO() {
		super();
        // Acciones y Metodos para seguridad
	}

	public CategoriaInmuebleVO(int id, String desCategoriaInmueble) {
		super();
		setId(new Long(id));
		setDesCategoriaInmueble(desCategoriaInmueble);
	}

	// Getters y Setters
	public String getDesCategoriaInmueble() {
		return desCategoriaInmueble;
	}

	public void setDesCategoriaInmueble(String desCategoriaInmueble) {
		this.desCategoriaInmueble = desCategoriaInmueble;
	}
}
