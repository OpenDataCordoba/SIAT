//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del PlanImpMin
 * @author tecso
 *
 */
public class PlanImpMinVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "planImpMinVO";
	
	private PlanVO plan = new PlanVO();
	private Integer cantidadCuotas;
	private Double impMinCuo;
	private Double impMinDeu;
	private Date fechaDesde;
	private Date fechaHasta;


	private String cantidadCuotasView = "";
	private String fechaDesdeView = "";
	private String fechaHastaView = "";
	private String impMinCuoView = "";
	private String impMinDeuView = "";


	// Constructores
	public PlanImpMinVO() {
		super();
	}


	// Getters y Setters
	public Integer getCantidadCuotas() {
		return cantidadCuotas;
	}
	public void setCantidadCuotas(Integer cantidadCuotas) {
		this.cantidadCuotas = cantidadCuotas;
		this.cantidadCuotasView = StringUtil.formatInteger(cantidadCuotas);
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

	public Double getImpMinCuo() {
		return impMinCuo;
	}
	public void setImpMinCuo(Double impMinCuo) {
		this.impMinCuo = impMinCuo;
		this.impMinCuoView = StringUtil.formatDouble(impMinCuo);
	}

	public Double getImpMinDeu() {
		return impMinDeu;
	}
	public void setImpMinDeu(Double impMinDeu) {
		this.impMinDeu = impMinDeu;
		this.impMinDeuView = StringUtil.formatDouble(impMinDeu);
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
	public void setCantidadCuotasView(String cantidadCuotasView) {
		this.cantidadCuotasView = cantidadCuotasView;
	}
	public String getCantidadCuotasView() {
		return cantidadCuotasView;
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

	public void setImpMinCuoView(String impMinCuoView) {
		this.impMinCuoView = impMinCuoView;
	}
	public String getImpMinCuoView() {
		return impMinCuoView;
	}

	public void setImpMinDeuView(String impMinDeuView) {
		this.impMinDeuView = impMinDeuView;
	}
	public String getImpMinDeuView() {
		return impMinDeuView;
	}

	

}
