//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;

/**
 * Value Object del DescuentoMulta
 * @author tecso
 *
 */
public class DescuentoMultaVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "descuentoMultaVO";
	
	private String descripcion = "";
	private RecursoVO recurso=new RecursoVO();
	private Date fechaDesde = new Date();
	private Date fechaHasta = new Date();
	private Double porcentaje;
	
	// Buss Flags
	
	
	// View Constants
	
	// Constructores
	public DescuentoMultaVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public DescuentoMultaVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDescripcion(desc);
	}
	
	
	// Getters y Setters

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public RecursoVO getRecurso() {
		return recurso;
	}

	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	public Double getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(Double porcentaje) {
		this.porcentaje = porcentaje;
	}
	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
