//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del IndiceCompensacion
 * 
 * @author tecso
 * 
 */
public class IndiceCompensacionVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "indiceCompensacionVO";


	private Double indice;
	private String indiceView;
	private Date fechaDesde;
	private Date fechaHasta;
	private String fechaDesdeView="";
	private String fechaHastaView="";
	private String periodoMesDesdeView;
	private String periodoAnioDesdeView;
	private String periodoMesHastaView;
	private String periodoAnioHastaView;

	// Constructores
	public IndiceCompensacionVO() {
		super();  
	}

	// Getters y Setters
	public Double getIndice() {
		return indice;
	}

	public void setIndice(Double indice) {
		this.indiceView=StringUtil.formatDouble(NumberUtil.truncate(indice,SiatParam.DEC_PORCENTAJE_DB));
		this.indice = indice;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesdeView = DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK);
		this.fechaDesde = fechaDesde;
		this.periodoMesDesdeView=Integer.toString(DateUtil.getMes(fechaDesde));
		this.periodoAnioDesdeView=Integer.toString(DateUtil.getAnio(fechaDesde));
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHastaView = DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK);
		this.fechaHasta = fechaHasta;
		this.periodoMesHastaView=Integer.toString(DateUtil.getMes(fechaHasta));
		this.periodoAnioHastaView=Integer.toString(DateUtil.getAnio(fechaHasta));
	}

	
	// View getters
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
	
	public String getIndiceView() {
		return indiceView;
	}

	public void setIndiceView(String indiceView) {
		this.indiceView = indiceView;
	}
	
	public String getPeriodoMesDesdeView() {
		return periodoMesDesdeView;
	}

	public void setPeriodoMesDesdeView(String periodoMesDesdeView) {
		this.periodoMesDesdeView = periodoMesDesdeView;
	}

	public String getPeriodoAnioDesdeView() {
		return periodoAnioDesdeView;
	}

	public void setPeriodoAnioDesdeView(String periodoAnioDesdeView) {
		this.periodoAnioDesdeView = periodoAnioDesdeView;
	}

	public String getPeriodoMesHastaView() {
		return periodoMesHastaView;
	}

	public void setPeriodoMesHastaView(String periodoMesHastaView) {
		this.periodoMesHastaView = periodoMesHastaView;
	}

	public String getPeriodoAnioHastaView() {
		return periodoAnioHastaView;
	}

	public void setPeriodoAnioHastaView(String periodoAnioHastaView) {
		this.periodoAnioHastaView = periodoAnioHastaView;
	}




}
