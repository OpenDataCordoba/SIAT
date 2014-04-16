//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del TipoPago
 * @author tecso
 *
 */
public class TipoPagoVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipoPagoVO";
	
	private String desTipoPago;
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public TipoPagoVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public TipoPagoVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesTipoPago(desc);
	}
	
	// Getters y Setters
	public String getDesTipoPago() {
		return desTipoPago;
	}
	public void setDesTipoPago(String desTipoPago) {
		this.desTipoPago = desTipoPago;
	}

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
