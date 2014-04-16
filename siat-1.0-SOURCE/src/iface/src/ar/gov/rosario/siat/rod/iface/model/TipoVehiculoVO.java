//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rod.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del TipoVehiculo
 * @author tecso
 *
 */
public class TipoVehiculoVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipoVehiculoVO";
	
	private String codTipoGen="";
	private String desTipoGen="";
	private String codTipoEsp="";
	private String desTipoEsp="";
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public TipoVehiculoVO() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public TipoVehiculoVO(String codGen, String desGen, String codEsp, String desEsp) {
		super();
		
	}
	// Getters y Setters

	public String getCodTipoGen() {
		return codTipoGen;
	}

	public void setCodTipoGen(String codTipoGen) {
		this.codTipoGen = codTipoGen;
	}

	public String getDesTipoGen() {
		return desTipoGen;
	}

	public void setDesTipoGen(String desTipoGen) {
		this.desTipoGen = desTipoGen;
	}

	public String getCodTipoEsp() {
		return codTipoEsp;
	}

	public void setCodTipoEsp(String codTipoEsp) {
		this.codTipoEsp = codTipoEsp;
	}

	public String getDesTipoEsp() {
		return desTipoEsp;
	}

	public void setDesTipoEsp(String desTipoEsp) {
		this.desTipoEsp = desTipoEsp;
	}	
	
	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
