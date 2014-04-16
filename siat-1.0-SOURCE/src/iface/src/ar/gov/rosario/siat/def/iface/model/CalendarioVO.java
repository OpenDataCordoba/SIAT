//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Value Object del Calendario
 * @author tecso
 *
 */
public class CalendarioVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "CalendarioVO";
	
	private RecursoVO recurso = new RecursoVO();
	private ZonaVO zona = new ZonaVO();
	private String periodo;
	private Date fechaVencimiento;

	// Buss Flags
	
	// View Constants
	private String fechaVencimientoView="";
	

	// Constructores
	public CalendarioVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public CalendarioVO(int id, String perioro) {
		super();
		setId(new Long(id));
		setPeriodo(periodo);
	}
	
	// Getters y Setters
	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
		this.fechaVencimientoView = DateUtil.formatDate(fechaVencimiento, "dd/MM/yyyy");
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	public RecursoVO getRecurso() {
		return recurso;
	}

	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}

	public ZonaVO getZona() {
		return zona;
	}

	public void setZona(ZonaVO zona) {
		this.zona = zona;
	}

	//	 View getters
	public String getFechaVencimientoView() {
		return fechaVencimientoView;
	}

	public void setFechaVencimientoView(String fechaVencimientoView) {
		this.fechaVencimientoView = fechaVencimientoView;
	}
	
}
