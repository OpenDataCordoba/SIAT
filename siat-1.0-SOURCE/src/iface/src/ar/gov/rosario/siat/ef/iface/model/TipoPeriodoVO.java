//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del TipoPeriodo
 * @author tecso
 *
 */
public class TipoPeriodoVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipoPeriodoVO";
	
	private String desTipoPeriodo;
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public TipoPeriodoVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public TipoPeriodoVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesTipoPeriodo(desc);
	}
	
	// Getters y Setters

	public String getDesTipoPeriodo() {
		return desTipoPeriodo;
	}

	public void setDesTipoPeriodo(String desTipoPeriodo) {
		this.desTipoPeriodo = desTipoPeriodo;
	}

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
