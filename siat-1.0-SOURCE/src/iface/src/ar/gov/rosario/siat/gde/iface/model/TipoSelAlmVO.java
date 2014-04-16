//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * VO correspondiente al Tipo de Seleccion Almacenada
 * Tambien representa los Tipos de Detalle de Seleccion Almacenada
 * 
 * 
 * @author tecso
 *
 */
public class TipoSelAlmVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipoSelAlmVO";
	
	private String codTipoSelAlm;   // CHAR(10) NOT NULL

	private String desTipoSelAlm;   // VARCHAR(255) NOT NULL 


	// Contructores
	public TipoSelAlmVO(){
		super();
	}

	// Getters y Setters
	public String getCodTipoSelAlm() {
		return codTipoSelAlm;
	}
	public void setCodTipoSelAlm(String codTipoSelAlm) {
		this.codTipoSelAlm = codTipoSelAlm;
	}
	public String getDesTipoSelAlm() {
		return desTipoSelAlm;
	}
	public void setDesTipoSelAlm(String desTipoSelAlm) {
		this.desTipoSelAlm = desTipoSelAlm;
	}
	
}
