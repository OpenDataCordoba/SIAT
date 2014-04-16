//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rod.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del TipoPago
 * @author tecso
 *
 */
public class TipoPagoVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipoPagoVO";
	
	private Integer codPago;
	private String desPago="";
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public TipoPagoVO() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public TipoPagoVO(Integer cod, String desc) {
		super();
		setCodPago(cod);
		setDesPago(desc);
	}

	
	
	public Integer getCodPago() {
		return codPago;
	}

	public void setCodPago(Integer codPago) {
		this.codPago = codPago;
	}

	public String getDesPago() {
		return desPago;
	}

	public void setDesPago(String desPago) {
		this.desPago = desPago;
	}


	
	// Getters y Setters

	

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
