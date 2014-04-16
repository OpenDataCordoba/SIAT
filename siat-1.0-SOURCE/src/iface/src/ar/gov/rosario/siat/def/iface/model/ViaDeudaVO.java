//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del ViaDeuda
 * @author tecso
 *
 */
public class ViaDeudaVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "viaDeudaVO";
	public static final long ID_VIA_ADMIN = 1L;
	public static final long ID_VIA_JUDICIAL = 2L;
	public static final long ID_VIA_CYQ = 3L;

	
	private String desViaDeuda;
	
	// Constructores
	public ViaDeudaVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public ViaDeudaVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesViaDeuda(desc);
	}

	// Getters y Setters
	public String getDesViaDeuda() {
		return desViaDeuda;
	}

	public void setDesViaDeuda(String desViaDeuda) {
		this.desViaDeuda = desViaDeuda;
	}

}
