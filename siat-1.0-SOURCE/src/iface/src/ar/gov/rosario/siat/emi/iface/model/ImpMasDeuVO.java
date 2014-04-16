//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.AtributoVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.frm.iface.model.FormatoSalida;
import ar.gov.rosario.siat.pro.iface.model.CorridaVO;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Value Object del ImpMasDeu
 * @author tecso
 *
 */
public class ImpMasDeuVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "impMasDeuVO";
	
	private RecursoVO recurso = new RecursoVO();
	
	private FormatoSalida formatoSalida = FormatoSalida.OpcionSelecionar;
	
	private CorridaVO corrida = new CorridaVO();
	
	private AtributoVO atributo = new AtributoVO();
	
	private String atrValor;
	
	private Integer anio;
	
	private Integer periodoDesde;
	
	private Integer periodoHasta;
	
	private String anioView = "";
	
	private String periodoDesdeView = "";
	
	private String periodoHastaView = "";
	
	private SiNo abrirPorBroche = SiNo.SI;
	
	// View Flags
	private boolean administrarProcesoEnabled = true;
	
	// View Constants
	
	// Constructores
	public ImpMasDeuVO() {
		super();
	}

	// Getters y Setters
	public RecursoVO getRecurso() {
		return recurso;
	}

	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}
	
	public FormatoSalida getFormatoSalida() {
		return formatoSalida;
	}

	public void setFormatoSalida(FormatoSalida formatoSalida) {
		this.formatoSalida = formatoSalida;
	}

	public CorridaVO getCorrida() {
		return corrida;
	}

	public void setCorrida(CorridaVO corrida) {
		this.corrida = corrida;
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

	public AtributoVO getAtributo() {
		return atributo;
	}

	public void setAtributo(AtributoVO atributo) {
		this.atributo = atributo;
	}

	public String getAtrValor() {
		return atrValor;
	}

	public void setAtrValor(String atrValor) {
		this.atrValor = atrValor;
	}

	public SiNo getAbrirPorBroche() {
		return abrirPorBroche;
	}

	public void setAbrirPorBroche(SiNo abrirPorBroche) {
		this.abrirPorBroche = abrirPorBroche;
	}
}
