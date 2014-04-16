//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del TipoTramite
 * @author tecso
 *
 */
public class TipoTramiteVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipoTramiteVO";
	
	public static final String COD_SELLADO_CONSULTA = "1";
	public static final String COD_SELLADO_LIBREDEUDA = "2";
	
	private String codTipoTramite;
	private String desTipoTramite;
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public TipoTramiteVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public TipoTramiteVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesTipoTramite(desc);
	}


	
	// Getters y Setters
	public String getCodTipoTramite() {
		return codTipoTramite;
	}

	public void setCodTipoTramite(String codTipoTramite) {
		this.codTipoTramite = codTipoTramite;
	}

	public String getDesTipoTramite() {
		return desTipoTramite;
	}

	public void setDesTipoTramite(String desTipoTramite) {
		this.desTipoTramite = desTipoTramite;
	}
	
	

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
