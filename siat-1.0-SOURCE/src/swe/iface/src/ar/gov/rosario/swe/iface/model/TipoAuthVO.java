//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.iface.model;



/**
 * Aplicaciones
 * @author tecso
 *
 */
public class TipoAuthVO extends SweBussImageModel {


	private static final long serialVersionUID = 1L;

	public static final String NAME = "TipoAuthVO";
	
	private String descripcion;
		
	public TipoAuthVO() {
		super();
	}

	public TipoAuthVO(String desc) {
		super();
		setDescripcion(desc);
	}
	
	public TipoAuthVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDescripcion(desc);
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}