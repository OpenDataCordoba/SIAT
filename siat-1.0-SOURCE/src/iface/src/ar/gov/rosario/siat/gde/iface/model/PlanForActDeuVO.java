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
 * Value Object del PlanForActDeu
 * @author tecso
 *
 */
public class PlanForActDeuVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "planForActDeuVO";
	
	private PlanVO plan  = new PlanVO();
	private Date fecVenDeuDes;
	private SiNo esComun = SiNo.OpcionSelecionar;
	private Double porcentaje;
	private Date fechaDesde;
	private Date fechaHasta;
	private String className = "";
	
	private String fecVenDeuDesView = "";
	private String fechaDesdeView = "";
	private String fechaHastaView = "";
	private String porcentajeView = "";

	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public PlanForActDeuVO() {
		super();
	}

	// Getters y Setters	
	public SiNo getEsComun() {
		return esComun;
	}

	public void setEsComun(SiNo esComun) {
		this.esComun = esComun;
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

	public Date getFecVenDeuDes() {
		return fecVenDeuDes;
	}

	public void setFecVenDeuDes(Date fecVenDeuDes) {
		this.fecVenDeuDes = fecVenDeuDes;
		this.fecVenDeuDesView = DateUtil.formatDate(fecVenDeuDes, DateUtil.ddSMMSYYYY_MASK);
	}

	public PlanVO getPlan() {
		return plan;
	}

	public void setPlan(PlanVO plan) {
		this.plan = plan;
	}

	public Double getPorcentaje() {
		return porcentaje;
	}

	@PORCENTAJE
	public void setPorcentaje(Double porcentaje) {
		this.porcentaje = porcentaje;
		this.porcentajeView = StringUtil.formatDouble(porcentaje);
	}
	
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	
	// Buss flags getters y setters
	
	
	// View flags getters
	
	

	// View getters
	public void setFecVenDeuDesView(String fecVenDeuDesView) {
		this.fecVenDeuDesView = fecVenDeuDesView;
	}
	public String getFecVenDeuDesView() {
		return fecVenDeuDesView;
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

	public void setPorcentajeView(String porcentajeView) {
		this.porcentajeView = porcentajeView;
	}
	public String getPorcentajeView() {
		return porcentajeView;
	}

}
