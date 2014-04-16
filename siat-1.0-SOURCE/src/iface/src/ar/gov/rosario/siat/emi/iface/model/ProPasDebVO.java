//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.AtributoVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.pro.iface.model.CorridaVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del ProPasDeb
 * @author tecso
 *
 */
public class ProPasDebVO extends SiatBussImageModel {

	public static final String NAME = "proPasDebVO";

	private static final long serialVersionUID = 1L;

	private RecursoVO recurso = new RecursoVO();
	
	private AtributoVO atributo = new AtributoVO();

	private CorridaVO corrida = new CorridaVO();
	
	private String atrValor; 
	
	private Integer anio;

	private Integer periodo;
	
	private Date fechaEnvio;
	
	private String anioView = "";

	private String periodoView = "";
	
	private String fechaEnvioView = "";
	
	// View Flags
	private boolean administrarProcesoEnabled = true;

	// Constructores
	public ProPasDebVO() {
		super();
	}

	// Getters y Setters
	public RecursoVO getRecurso() {
		return recurso;
	}

	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}

	public AtributoVO getAtributo() {
		return atributo;
	}

	public void setAtributo(AtributoVO atributo) {
		this.atributo = atributo;
	}

	public CorridaVO getCorrida() {
		return corrida;
	}

	public void setCorrida(CorridaVO corrida) {
		this.corrida = corrida;
	}

	public String getAtrValor() {
		return atrValor;
	}

	public void setAtrValor(String atrValor) {
		this.atrValor = atrValor;
	}

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
		this.anioView = StringUtil.formatInteger(anio);
	}

	public Integer getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
		this.periodoView = StringUtil.formatInteger(periodo);
	}

	public Date getFechaEnvio() {
		return fechaEnvio;
	}

	public void setFechaEnvio(Date fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
		this.fechaEnvioView = DateUtil.formatDate(fechaEnvio, DateUtil.ddSMMSYYYY_MASK);
	}

	public String getAnioView() {
		return anioView;
	}

	public void setAnioView(String anioView) {
		this.anioView = anioView;
	}

	public String getPeriodoView() {
		return periodoView;
	}

	public void setPeriodoView(String periodoView) {
		this.periodoView = periodoView;
	}
	
	public String getFechaEnvioView() {
		return fechaEnvioView;
	}

	public void setFechaEnvioView(String fechaEnvioView) {
		this.fechaEnvioView = fechaEnvioView;
	}

	public boolean isAdministrarProcesoEnabled() {
		return administrarProcesoEnabled;
	}

	public void setAdministrarProcesoEnabled(boolean administrarProcesoEnabled) {
		this.administrarProcesoEnabled = administrarProcesoEnabled;
	}
}
