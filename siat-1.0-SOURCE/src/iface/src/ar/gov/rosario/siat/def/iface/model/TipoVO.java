//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Tipo
 * @author tecso
 *
 */
public class TipoVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipoVO";
	
	private String 	desTipo;
	
	public TipoVO() {
		super();
	}
	
	public TipoVO(int id, String desTipo) {
		super(id);
		setDesTipo(desTipo);
	}

	public String getDesTipo() {
		return desTipo;
	}

	public void setDesTipo(String desTipo) {
		this.desTipo = desTipo;
	}
		
}
