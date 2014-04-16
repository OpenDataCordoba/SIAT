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
 * Value Object del ProRecCom
 * @author tecso
 *
 */
public class ProRecComVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "proRecComVO";
	
	private ProRecVO proRec = new ProRecVO();
	
	private Date fecVtoDeuDes;  
	private Date fecVtoDeuHas;  
	
	private Double porcentajeComision;      
	
	private Date fechaDesde;     
	private Date fechaHasta;     

	// View Constants
	
	private String fecVtoDeuDesView="";  
	private String fecVtoDeuHasView="";

	private String porcentajeComisionView="";
	
	private String fechaDesdeView="";     
	private String fechaHastaView=""; 
	
	// Constructores
	public ProRecComVO() {
		super();
	}
	
	//	 Getters y Setters
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

	public Date getFecVtoDeuDes() {
		return fecVtoDeuDes;
	}

	public void setFecVtoDeuDes(Date fecVtoDeuDes) {
		this.fecVtoDeuDes = fecVtoDeuDes;
		this.fecVtoDeuDesView = DateUtil.formatDate(fecVtoDeuDes, DateUtil.ddSMMSYYYY_MASK);
	}

	public Date getFecVtoDeuHas() {
		return fecVtoDeuHas;
	}

	public void setFecVtoDeuHas(Date fecVtoDeuHas) {
		this.fecVtoDeuHas = fecVtoDeuHas;
		this.fecVtoDeuHasView = DateUtil.formatDate(fecVtoDeuHas, DateUtil.ddSMMSYYYY_MASK);
	}

	public Double getPorcentajeComision() {
		return porcentajeComision;
	}

	public void setPorcentajeComision(Double porcentajeComision) {
		this.porcentajeComision = porcentajeComision;
		this.porcentajeComisionView = StringUtil.formatDouble(porcentajeComision);
	}

	public ProRecVO getProRec() {
		return proRec;
	}

	public void setProRec(ProRecVO proRec) {
		this.proRec = proRec;
	}

	//	 View getters
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

	public String getFecVtoDeuDesView() {
		return fecVtoDeuDesView;
	}

	public void setFecVtoDeuDesView(String fecVtoDeuDesView) {
		this.fecVtoDeuDesView = fecVtoDeuDesView;
	}

	public String getFecVtoDeuHasView() {
		return fecVtoDeuHasView;
	}

	public void setFecVtoDeuHasView(String fecVtoDeuHasView) {
		this.fecVtoDeuHasView = fecVtoDeuHasView;
	}

	public String getPorcentajeComisionView() {
		return porcentajeComisionView;
	}

	public void setPorcentajeComisionView(String porcentajeComisionView) {
		this.porcentajeComisionView = porcentajeComisionView;
	}
	
}
