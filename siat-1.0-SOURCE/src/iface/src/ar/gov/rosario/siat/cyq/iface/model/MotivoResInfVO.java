//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del MotivoResInf
 * @author tecso
 *
 */
public class MotivoResInfVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "motivoResInfVO";
	
	private String desMotivoResInf;
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public MotivoResInfVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public MotivoResInfVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesMotivoResInf(desc);
	}

	
	// Getters y Setters
	public String getDesMotivoResInf() {
		return desMotivoResInf;
	}
	public void setDesMotivoResInf(String desMotivoResInf) {
		this.desMotivoResInf = desMotivoResInf;
	}

	
	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
