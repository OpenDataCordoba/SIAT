//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del EstadoOrden
 * @author tecso
 *
 */
public class EstadoOrdenVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "estadoOrdenVO";
	
	private String desEstadoOrden;
	
	private Integer ordenOcurrencia;
	
	private boolean esEstadoInv=false;
	
	public static final String SINASIGNAR = "Sin Asignar";
	
	public static final String ASIGNADA = "Asignada a Inpector";
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public EstadoOrdenVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public EstadoOrdenVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesEstadoOrden(desc);
	}
	
	// Getters y Setters

	public String getDesEstadoOrden() {
		return desEstadoOrden;
	}

	public void setDesEstadoOrden(String desEstadoOrden) {
		this.desEstadoOrden = desEstadoOrden;
	}


	public Integer getOrdenOcurrencia() {
		return ordenOcurrencia;
	}

	public void setOrdenOcurrencia(Integer ordenOcurrencia) {
		this.ordenOcurrencia = ordenOcurrencia;
	}

	public boolean getEsEstadoInv() {
		return esEstadoInv;
	}

	public void setEsEstadoInv(boolean esEstadoInv) {
		this.esEstadoInv = esEstadoInv;
	}

	
	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
