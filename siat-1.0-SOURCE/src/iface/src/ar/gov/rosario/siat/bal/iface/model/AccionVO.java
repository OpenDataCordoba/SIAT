//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del Accion
 * @author tecso
 *
 */
public class AccionVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "accionVO";
	
	private String codAccion;
	private String desAccion;
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public AccionVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public AccionVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesAccion(desc);
	}

	public String getCodAccion() {
		return codAccion;
	}

	public void setCodAccion(String codAccion) {
		this.codAccion = codAccion;
	}

	public String getDesAccion() {
		return desAccion;
	}

	public void setDesAccion(String desAccion) {
		this.desAccion = desAccion;
	}
	
	// Getters y Setters
	
	
	
	// View getters
}
