//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Value Object del OpeInv
 * @author tecso
 *
 */
public class OpeInvVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "opeInvVO";
	
	private String desOpeInv;

	private PlanFiscalVO planFiscal = new PlanFiscalVO();

	private Date fechaInicio;

	private String observacion;

	private EstOpeInvVO estOpeInv = new EstOpeInvVO();

	private String fechaInicioView="";
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public OpeInvVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public OpeInvVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesOpeInv(desc);
	}
	
	// Getters y Setters

	public String getDesOpeInv() {
		return desOpeInv;
	}

	public void setDesOpeInv(String desOpeInv) {
		this.desOpeInv = desOpeInv;
	}

	public PlanFiscalVO getPlanFiscal() {
		return planFiscal;
	}

	public void setPlanFiscal(PlanFiscalVO planFiscal) {
		this.planFiscal = planFiscal;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
		fechaInicioView = DateUtil.formatDate(fechaInicio, DateUtil.ddSMMSYYYY_MASK);
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public EstOpeInvVO getEstOpeInv() {
		return estOpeInv;
	}

	public void setEstOpeInv(EstOpeInvVO estOpeInv) {
		this.estOpeInv = estOpeInv;
	}


	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
	public String getFechaInicioView() {
		return fechaInicioView;
	}
	
	public void setFechaInicioView(String fechaInicioView) {
		this.fechaInicioView = fechaInicioView;
	}
}
