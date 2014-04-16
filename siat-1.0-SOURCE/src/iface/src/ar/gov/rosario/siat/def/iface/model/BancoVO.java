//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del Banco
 * @author tecso
 *
 */
public class BancoVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "bancoVO";
	
	private String codBanco;
	private String desBanco;
		
	// Constructores
	public BancoVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public BancoVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesBanco(desc);
	}

	
	// Getters y Setters
	public String getCodBanco() {
		return codBanco;
	}
	public void setCodBanco(String codBanco) {
		this.codBanco = codBanco;
	}
	
	public String getDesBanco() {
		return desBanco;
	}
	public void setDesBanco(String desBanco) {
		this.desBanco = desBanco;
	}


	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
