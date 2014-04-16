//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;


/**
 * Value Object del InsSup
 * @author tecso
 *
 */
public class InsSupVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "inssupVO";
	
	
	private InspectorVO inspector = new InspectorVO();
	
	private SupervisorVO supervisor  = new SupervisorVO();
	
	private Date fechaDesde;
	
	private Date fechaHasta;
	
	// Buss Flags

	
	// View Properties
    private String fechaDesdeView = "";
	
	private String fechaHastaView = "";
	// View Constants
	
	// Constructores
	public InsSupVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public InsSupVO(int id, String desc) {
		super();
		setId(new Long(id));
	
	}

	public InspectorVO getInspector() {
		return inspector;
	}

	public void setInspector(InspectorVO inspector) {
		this.inspector = inspector;
	}

	public SupervisorVO getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(SupervisorVO supervisor) {
		this.supervisor = supervisor;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
		this.fechaDesdeView = DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK);
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
		this.fechaHastaView = DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK);
	}

	public String getFechaDesdeView() {
		return fechaDesdeView;
	}

	public void setFechaDesdeView(String fechaDesdeView) {
		this.fechaDesdeView = fechaDesdeView;
	}

	public String getFechaHastaView() {
		return fechaHastaView;
	}

	public void setFechaHastaView(String fechaHastaView) {
		this.fechaHastaView = fechaHastaView;
	}
	
	// Getters y Setters


	
	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
