//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Value Object del AgeRet
 * @author tecso
 *
 */
public class AgeRetVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "ageRetVO";
	
	private RecursoVO recurso = new RecursoVO();
	
	private String desAgeRet;
	
	private String cuit;
	
	private Date fechaDesde;

	private Date fechaHasta;
	
	private String fechaDesdeView = "";
	
	private String fechaHastaView = "";
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public AgeRetVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public AgeRetVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesAgeRet(desc);
	}

	
	// Getters y Setters
	
	public RecursoVO getRecurso() {
		return recurso;
	}

	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}

	public String getDesAgeRet() {
		return desAgeRet;
	}

	public void setDesAgeRet(String desAgeRet) {
		this.desAgeRet = desAgeRet;
	}

	public String getCuit() {
		return cuit;
	}

	public void setCuit(String cuit) {
		this.cuit = cuit;
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

	// Buss flags getters y setters
	
	
	// View flags getters
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
	
	
	// View getters
	public String getDesCuitView(){
		return(this.desAgeRet + " " + this.cuit);
	} 
}
