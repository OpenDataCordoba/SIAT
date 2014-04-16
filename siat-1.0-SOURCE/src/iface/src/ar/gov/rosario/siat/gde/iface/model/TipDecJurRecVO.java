//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;

/**
 * Value Object del TipoDeudaPlan
 * @author tecso
 *
 */
public class TipDecJurRecVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipDecJurRecVO";
	
	private RecursoVO recurso = new RecursoVO();
	
	private TipDecJurVO tipDecJur = new TipDecJurVO();
	
	private Date fechaDesde;
	
	private Date fechaHasta;
	
	// Constructores
	public TipDecJurRecVO() {
		super();
	}

	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public TipDecJurRecVO(int id, String desc) {
		super();
		setId(new Long(id));
		this.tipDecJur.setDesTipo(desc);
	}
	
	// Getters y Setters
	public RecursoVO getRecurso() {
		return recurso;
	}

	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}

	public TipDecJurVO getTipDecJur() {
		return tipDecJur;
	}

	public void setTipDecJur(TipDecJurVO tipDecJur) {
		this.tipDecJur = tipDecJur;
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
	
	

	

	

	

	
	
	
	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
