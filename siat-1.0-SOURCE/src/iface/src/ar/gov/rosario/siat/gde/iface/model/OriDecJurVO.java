//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del OriDecJud
 * @author tecso
 *
 */
public class OriDecJurVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipDecJurVO";
	
	private String desOrigen;
	
	private Date fechaDesde;
	private Date fechahasta;
	
	// Constructores
	public OriDecJurVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public OriDecJurVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesOrigen(desc);
	}

	
	// Getters y Setters
	public String getDesOrigen() {
		return desOrigen;
	}

	public void setDesOrigen(String desOrigen) {
		this.desOrigen = desOrigen;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Date getFechahasta() {
		return fechahasta;
	}

	public void setFechahasta(Date fechahasta) {
		this.fechahasta = fechahasta;
	}

	



	

	
	
	
	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
