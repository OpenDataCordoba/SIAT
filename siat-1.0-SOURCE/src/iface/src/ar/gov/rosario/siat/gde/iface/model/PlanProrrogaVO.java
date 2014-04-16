//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Value Object del PlanProrroga
 * @author tecso
 *
 */
public class PlanProrrogaVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "planProrrogaVO";
	
	private String desPlanProrroga;
	private PlanVO plan = new PlanVO();
	private Date fecVto;
	private Date fecVtoNue;
	private Date fechaDesde;
	private Date fechaHasta;
	private CasoVO caso = new CasoVO();

	private String fecVtoView = "";
	private String fecVtoNueView = "";
	private String fechaDesdeView = "";
	private String fechaHastaView = "";
	// Buss Flags
	
	
	// View Constants

	
	// Constructores
	public PlanProrrogaVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public PlanProrrogaVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesPlanProrroga(desc);
	}


	// Getters y Setters
	public CasoVO getCaso() {
		return caso;
	}

	public void setCaso(CasoVO caso) {
		this.caso = caso;
	}

	public String getDesPlanProrroga() {
		return desPlanProrroga;
	}

	public void setDesPlanProrroga(String desPlanProrroga) {
		this.desPlanProrroga = desPlanProrroga;
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

	public Date getFecVto() {
		return fecVto;
	}

	public void setFecVto(Date fecVto) {
		this.fecVto = fecVto;
		this.fecVtoView = DateUtil.formatDate(fecVto, DateUtil.ddSMMSYYYY_MASK);
	}

	public Date getFecVtoNue() {
		return fecVtoNue;
	}

	public void setFecVtoNue(Date fecVtoNue) {
		this.fecVtoNue = fecVtoNue;
		this.fecVtoNueView = DateUtil.formatDate(fecVtoNue, DateUtil.ddSMMSYYYY_MASK);
	}

	public PlanVO getPlan() {
		return plan;
	}

	public void setPlan(PlanVO plan) {
		this.plan = plan;
	}
	
	

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
	public void setFecVtoView(String fecVtoView) {
		this.fecVtoView = fecVtoView;
	}
	public String getFecVtoView() {
		return fecVtoView;
	}

	public void setFecVtoNueView(String fecVtoNueView) {
		this.fecVtoNueView = fecVtoNueView;
	}
	public String getFecVtoNueView() {
		return fecVtoNueView;
	}

	public void setFechaDesdeView(String fechaDesdeView) {
		this.fechaDesdeView = fechaDesdeView;
	}
	public String getFechaDesdeView() {
		return fechaDesdeView;
	}

	public void setFechaHastaView(String fechaHastaView) {
		this.fechaHastaView = fechaHastaView;
	}
	public String getFechaHastaView() {
		return fechaHastaView;
	}

}
