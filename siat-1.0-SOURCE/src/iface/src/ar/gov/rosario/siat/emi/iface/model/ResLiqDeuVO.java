//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.pro.iface.model.CorridaVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Value Object del ResLiqDeu
 * @author tecso
 *
 */
public class ResLiqDeuVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "resLiqDeuVO";
	
	public static final String ADP_PARAM_ID = "idResLiqDeu";
	
	private RecursoVO recurso = new RecursoVO();

	private CorridaVO corrida = new CorridaVO();

	private Date fechaAnalisis;

	private Integer anio;

	private Integer periodoDesde;

	private Integer periodoHasta;
	
	private Integer esAlfax = SiNo.NO.getBussId();

	private String fechaAnalisisView = "";

	private String anioView = "";

	private String periodoDesdeView = "";

	private String periodoHastaView = "";
	
	// View Flags
	private boolean administrarProcesoEnabled = true;

	// Constructores
	public ResLiqDeuVO() {
		super();
	}

	// Getters y Setters
	public RecursoVO getRecurso() {
		return recurso;
	}

	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}

	public CorridaVO getCorrida() {
		return corrida;
	}

	public void setCorrida(CorridaVO corrida) {
		this.corrida = corrida;
	}

	public Date getFechaAnalisis() {
		return fechaAnalisis;
	}
	
	public Integer getEsAlfax() {
		return esAlfax;
	}

	public void setEsAlfax(Integer esAlfax) {
		this.esAlfax = esAlfax;
	}

	public void setFechaAnalisis(Date fechaAnalisis) {
		this.fechaAnalisis = fechaAnalisis;
		this.fechaAnalisisView = DateUtil.formatDate(fechaAnalisis, DateUtil.ddSMMSYYYY_MASK);
	}

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
		this.anioView = StringUtil.formatInteger(anio);
	}

	public Integer getPeriodoDesde() {
		return periodoDesde;
	}

	public void setPeriodoDesde(Integer periodoDesde) {
		this.periodoDesde = periodoDesde;
		this.periodoDesdeView = StringUtil.formatInteger(periodoDesde);
	}

	public Integer getPeriodoHasta() {
		return periodoHasta;
	}

	public void setPeriodoHasta(Integer periodoHasta) {
		this.periodoHasta = periodoHasta;
		this.periodoHastaView = StringUtil.formatInteger(periodoHasta);
	}

	public String getFechaAnalisisView() {
		return fechaAnalisisView;
	}

	public void setFechaAnalisisView(String fechaAnalisisView) {
		this.fechaAnalisisView = fechaAnalisisView;
	}

	public String getAnioView() {
		return anioView;
	}

	public void setAnioView(String anioView) {
		this.anioView = anioView;
	}

	public String getPeriodoDesdeView() {
		return periodoDesdeView;
	}

	public void setPeriodoDesdeView(String periodoDesdeView) {
		this.periodoDesdeView = periodoDesdeView;
	}

	public String getPeriodoHastaView() {
		return periodoHastaView;
	}

	public void setPeriodoHastaView(String periodoHastaView) {
		this.periodoHastaView = periodoHastaView;
	}

	public boolean isAdministrarProcesoEnabled() {
		return administrarProcesoEnabled;
	}

	public void setAdministrarProcesoEnabled(boolean administrarProcesoEnabled) {
		this.administrarProcesoEnabled = administrarProcesoEnabled;
	}

	public boolean getEsAlfaxView() {
		return esAlfax.equals( SiNo.SI.getBussId());
	}
}
