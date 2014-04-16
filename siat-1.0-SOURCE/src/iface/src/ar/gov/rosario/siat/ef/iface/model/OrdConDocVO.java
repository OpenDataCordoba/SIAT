//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del OrdConDoc
 * @author tecso
 *
 */
public class OrdConDocVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "ordConDocVO";
	
	private DocumentacionVO	documentacion = new DocumentacionVO();
	private ActaVO acta= new ActaVO();
	private ActaVO actaProc = new ActaVO();
	private OrdenControlVO ordenControl;
	private String observaciones;
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public OrdConDocVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public OrdConDocVO(int id, String desc) {
		super();
		setId(new Long(id));		
	}
	
	// Getters y Setters

	public DocumentacionVO getDocumentacion() {
		return documentacion;
	}

	public void setDocumentacion(DocumentacionVO documentacion) {
		this.documentacion = documentacion;
	}

	public ActaVO getActa() {
		return acta;
	}

	public void setActa(ActaVO acta) {
		this.acta = acta;
	}

	public OrdenControlVO getOrdenControl() {
		return ordenControl;
	}

	public void setOrdenControl(OrdenControlVO ordenControl) {
		this.ordenControl = ordenControl;
	}

	
	public ActaVO getActaProc() {
		return actaProc;
	}

	public void setActaProc(ActaVO actaProc) {
		this.actaProc = actaProc;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	
	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
