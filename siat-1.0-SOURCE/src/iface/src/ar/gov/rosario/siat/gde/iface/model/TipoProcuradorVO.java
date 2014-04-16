//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * VO correspondiente al Tipo de Seleccion Almacenada
 * 
 * @author tecso
 *
 */
public class TipoProcuradorVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipoProcuradorVO";
	
	private String desTipoProcurador;   // VARCHAR(100) NOT NULL 

	// Contructores
	public TipoProcuradorVO(){
		super();
	}

	public TipoProcuradorVO(int id, String desTipoProcurador) {
		super();
		setId(new Long(id));		
		this.desTipoProcurador = desTipoProcurador;
	}
	
	// Getters y Setters
	public String getDesTipoProcurador() {
		return desTipoProcurador;
	}
	public void setDesTipoProcurador(String desTipoProcurador) {
		this.desTipoProcurador = desTipoProcurador;
	}
	
}
