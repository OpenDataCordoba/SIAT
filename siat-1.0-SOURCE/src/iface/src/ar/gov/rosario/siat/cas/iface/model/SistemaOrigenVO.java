//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.iface.model;


import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value object de Caso
 * 
 * @author tecso
 */
public class SistemaOrigenVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;
	public static final String NAME = "sistemaOrigenVO";

	private String  desSistemaOrigen = "";
	private Integer esValidable;
	
	// Constructores
	public SistemaOrigenVO() {
		super();
	}

	public SistemaOrigenVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesSistemaOrigen(desc);
	}

	public SistemaOrigenVO(Long id, String desc, Integer esValidable) {
		super();
		setId(id);
		setDesSistemaOrigen(desc);
		setEsValidable(esValidable);
	}
	
	// Getters y Setters
	public String getDesSistemaOrigen() {
		return desSistemaOrigen;
	}

	public void setDesSistemaOrigen(String desSistemaOrigen) {
		this.desSistemaOrigen = desSistemaOrigen;
	}

	public boolean isEsValidable() {
		
		if (getEsValidable() != null && 
				getEsValidable().intValue() == 1)
			return true;
		else
			return false;
	}

	public Integer getEsValidable() {
		return esValidable;
	}

	public void setEsValidable(Integer esValidable) {
		this.esValidable = esValidable;
	}

	
}
