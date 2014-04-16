//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.PORCENTAJE;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Value Object del PlanDescuento
 * @author tecso
 *
 */
public class PlanDescuentoVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "planDescuentoVO";
	
	private PlanVO plan = new PlanVO();
	private Integer cantidadCuotasPlan;
	private Integer aplTotImp;
	private Double porDesCap;
	private Double porDesAct;
	private Double porDesInt;
	private Date fechaDesde;
	private Date fechaHasta;
	
	
	private String cantidadCuotasPlanView = "";
	private String fechaDesdeView = "";
	private String fechaHastaView = "";
	private String porDesActView = "";
	private String porDesCapView = "";
	private String porDesIntView = "";
	private String aplTotImpView="";

	// Constructores
	public PlanDescuentoVO() {
		super();
	}


	// Getters y Setters
	public Integer getCantidadCuotasPlan() {
		return cantidadCuotasPlan;
	}
	public void setCantidadCuotasPlan(Integer cantidadCuotasPlan) {
		this.cantidadCuotasPlan = cantidadCuotasPlan;
		this.cantidadCuotasPlanView = StringUtil.formatInteger(cantidadCuotasPlan);
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

	public PlanVO getPlan() {
		return plan;
	}
	public void setPlan(PlanVO plan) {
		this.plan = plan;
	}

	public Double getPorDesAct() {
		return porDesAct;
	}
	public void setPorDesAct(Double porDesAct) {
		this.porDesAct = porDesAct;
		this.porDesActView = StringUtil.formatDouble(porDesAct);
	}
	public Double getPorDesCap() {
		return porDesCap;
	}
	@PORCENTAJE
	public void setPorDesCap(Double porDesCap) {
		this.porDesCap = porDesCap;
		this.porDesCapView = StringUtil.formatDouble(porDesCap);
	}

	public Double getPorDesInt() {
		return porDesInt;
	}
	public void setPorDesInt(Double porDesInt) {
		this.porDesInt = porDesInt;
		this.porDesIntView = StringUtil.formatDouble(porDesInt);
	}
	
	public Integer getAplTotImp() {
		return aplTotImp;
	}


	public void setAplTotImp(Integer aplTotImp) {
		this.aplTotImp = aplTotImp;
		this.aplTotImpView = StringUtil.formatInteger(aplTotImp);
	}

	// Buss flags getters y setters
	
	
	// View flags getters
	
	



	// View getters
	public void setCantidadCuotasPlanView(String cantidadCuotasPlanView) {
		this.cantidadCuotasPlanView = cantidadCuotasPlanView;
	}
	public String getCantidadCuotasPlanView() {
		return cantidadCuotasPlanView;
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

	public void setPorDesActView(String porDesActView) {
		this.porDesActView = porDesActView;
	}
	public String getPorDesActView() {
		return porDesActView;
	}

	public void setPorDesCapView(String porDesCapView) {
		this.porDesCapView = porDesCapView;
	}
	public String getPorDesCapView() {
		return porDesCapView;
	}

	public void setPorDesIntView(String porDesIntView) {
		this.porDesIntView = porDesIntView;
	}
	public String getPorDesIntView() {
		if (porDesIntView!=""){
			return porDesIntView;
		}else{
			return "0.0";
		}
	}


	public String getAplTotImpView() {
		
		return SiNo.getById(this.aplTotImp).getValue();
	}


	public void setAplTotImpView(String aplTotImpView) {
		this.aplTotImpView = aplTotImpView;
	}
	
	

}
