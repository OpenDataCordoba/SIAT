//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Value Object del PlanMotCad
 * @author tecso
 *
 */
public class PlanMotCadVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "planMotCadVO";
	
	private PlanVO plan = new PlanVO();
	private String desPlanMotCad;
	private SiNo esEspecial = SiNo.OpcionSelecionar;
	private Integer cantCuoCon;
	private Integer cantCuoAlt;
	private Integer cantDias;
	private String className = "";
	private Date fechaDesde;
	private Date fechaHasta;

	private String cantCuoAltView = "";
	private String cantCuoConView = "";
	private String cantDiasView = "";
	private String fechaDesdeView = "";
	private String fechaHastaView = "";
	
	// Buss Flags
	
	
	// View Constants

	// Constructores
	public PlanMotCadVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public PlanMotCadVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesPlanMotCad(desc);
	}


	// Getters y Setters
	public Integer getCantCuoAlt() {
		return cantCuoAlt;
	}
	public void setCantCuoAlt(Integer cantCuoAlt) {
		this.cantCuoAlt = cantCuoAlt;
		this.cantCuoAltView = StringUtil.formatInteger(cantCuoAlt);
	}

	public Integer getCantCuoCon() {
		return cantCuoCon;
	}
	public void setCantCuoCon(Integer cantCuoCon) {
		this.cantCuoCon = cantCuoCon;
		this.cantCuoConView = StringUtil.formatInteger(cantCuoCon);
	}

	public Integer getCantDias() {
		return cantDias;
	}
	public void setCantDias(Integer cantDias) {
		this.cantDias = cantDias;
		this.cantDiasView = StringUtil.formatInteger(cantDias);
	}

	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}

	public String getDesPlanMotCad() {
		return desPlanMotCad;
	}
	public void setDesPlanMotCad(String desPlanMotCad) {
		this.desPlanMotCad = desPlanMotCad;
	}

	public SiNo getEsEspecial() {
		return esEspecial;
	}
	public void setEsEspecial(SiNo esEspecial) {
		this.esEspecial = esEspecial;
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
	

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
	public void setCantCuoAltView(String cantCuoAltView) {
		this.cantCuoAltView = cantCuoAltView;
	}
	public String getCantCuoAltView() {
		return cantCuoAltView;
	}

	public void setCantCuoConView(String cantCuoConView) {
		this.cantCuoConView = cantCuoConView;
	}
	public String getCantCuoConView() {
		return cantCuoConView;
	}

	public void setCantDiasView(String cantDiasView) {
		this.cantDiasView = cantDiasView;
	}
	public String getCantDiasView() {
		return cantDiasView;
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
