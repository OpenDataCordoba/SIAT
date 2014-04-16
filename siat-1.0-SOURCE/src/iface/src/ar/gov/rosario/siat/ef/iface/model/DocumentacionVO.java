//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del Documentacion
 * @author tecso
 *
 */
public class DocumentacionVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "documentacionVO";
	
	private String desDoc;
	
	private TipoDocVO tipoDoc = new TipoDocVO();
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public DocumentacionVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public DocumentacionVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesDoc(desc);
	}
	
	// Getters y Setters

	public String getDesDoc() {
		return desDoc;
	}

	public void setDesDoc(String desDocumentacion) {
		this.desDoc = desDocumentacion;
	}

	public TipoDocVO getTipoDoc() {
		return tipoDoc;
	}

	public void setTipoDoc(TipoDocVO tipoDoc) {
		this.tipoDoc = tipoDoc;
	}

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
