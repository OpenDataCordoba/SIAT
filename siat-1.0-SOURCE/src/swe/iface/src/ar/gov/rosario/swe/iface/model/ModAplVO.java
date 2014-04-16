//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.iface.model;


/**
 * ModApl
 * @author tecso 
 */
public class ModAplVO extends SweBussImageModel {
	
	private String nombreModulo = "";
	private AplicacionVO aplicacion = new AplicacionVO(); 

	public ModAplVO() {
		super();
	}
	
	public ModAplVO(long id, String nombreModulo) {
		super();
		this.setId(id);
		this.nombreModulo = nombreModulo;
	}

	public ModAplVO(String nombreModulo) {
		super();
		this.nombreModulo = nombreModulo;
	}

	public String getNombreModulo() {
		return nombreModulo;
	}

	public void setNombreModulo(String nombreModulo) {
		this.nombreModulo = nombreModulo;
	}

	public AplicacionVO getAplicacion() {
		return aplicacion;
	}

	public void setAplicacion(AplicacionVO aplicacion) {
		this.aplicacion = aplicacion;
	}
	
}
