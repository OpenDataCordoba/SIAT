//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del TipoDoc
 * @author tecso
 *
 */
public class TipoDocVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipoDocVO";
	
	private String desTipoDoc;

	private Integer orden;

	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public TipoDocVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public TipoDocVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesTipoDoc(desc);
	}
	
	// Getters y Setters

	public String getDesTipoDoc() {
		return desTipoDoc;
	}

	public void setDesTipoDoc(String desTipoDoc) {
		this.desTipoDoc = desTipoDoc;
	}

	public Integer getOrden() {
		return orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
