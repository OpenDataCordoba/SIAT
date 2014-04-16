//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object de la Calle
 * @author tecso
 *
 */
public class CalleVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "calleVO";
			
	private String 	nombreCalle;
	
    private Long codCalle;	
    
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public CalleVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public CalleVO(Long id, String desc) {
		super();
		setId(id);
		setNombreCalle(desc);
	}
	
	/**
	 * Obtiene una instancia duplicada de la calleVO
	 * @return CalleVO
	 */
	public CalleVO getDuplicate(){
		
		CalleVO calleNuevaReferencia = new CalleVO();
		calleNuevaReferencia.setId(this.getId());
		calleNuevaReferencia.setNombreCalle(nombreCalle);
		return calleNuevaReferencia;
	}
	

	public String getNombreCalle() {
		return nombreCalle;
	}

	public void setNombreCalle(String nombreCalle) {
		this.nombreCalle = nombreCalle;
		if (this.nombreCalle != null) {
			this.nombreCalle = this.nombreCalle.trim();
		}
	}

	public Long getCodCalle() {
		return codCalle;
	}

	public void setCodCalle(Long codCalle) {
		this.codCalle = codCalle;
	}

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
