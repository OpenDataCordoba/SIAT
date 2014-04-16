//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.StringUtil;

public class LiqRecMinVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;
	
	private Integer periodo;
	private Integer anio;
	
	private Integer periodoDesde;
	private Integer anioDesde;
	private Integer periodoHasta;
	private Integer anioHasta;
    private String valor="";
	
    private String periodoView = "";
	private String anioView = "";
    private String periodoDesdeView = "";
	private String anioDesdeView = "";
	private String periodoHastaView = "";
	private String anioHastaView = "";
	
	
	private boolean quitarEnabled = false;
	
    // Constructor
    public LiqRecMinVO(){
    	
    }
    
    public LiqRecMinVO(Integer periodo, Integer anio, Long id){
    	this.setPeriodo(periodo);
    	this.setAnio(anio);
    	this.setId(id);
    }	
    	
    public LiqRecMinVO(Integer periodoDesde, Integer anioDesde, Integer periodoHasta, Integer anioHasta, String valor){
    	this.setPeriodoDesde(periodoDesde);
    	this.setAnioDesde(anioDesde);
    	this.setPeriodoHasta(periodoHasta);
    	this.setAnioHasta(anioHasta);
    	this.setValor(valor);
    }

    //  Getters y Setters
	public Integer getPeriodoDesde() {
		return periodoDesde;
	}
	public void setPeriodoDesde(Integer periodoDesde) {
		this.periodoDesde = periodoDesde;
		this.periodoDesdeView = StringUtil.formatInteger(periodoDesde);
	}

	public Integer getAnioDesde() {
		return anioDesde;
	}
	public void setAnioDesde(Integer anioDesde) {
		this.anioDesde = anioDesde;
		this.anioDesdeView = StringUtil.formatInteger(anioDesde);
	}

	public Integer getPeriodoHasta() {
		return periodoHasta;
	}
	public void setPeriodoHasta(Integer periodoHasta) {
		this.periodoHasta = periodoHasta;
		this.periodoHastaView = StringUtil.formatInteger(periodoHasta);
	}

	public Integer getAnioHasta() {
		return anioHasta;
	}
	public void setAnioHasta(Integer anioHasta) {
		this.anioHasta = anioHasta;
		this.anioHastaView = StringUtil.formatInteger(anioHasta);
	}

	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getPeriodoDesdeView() {
		return periodoDesdeView;
	}
	public void setPeriodoDesdeView(String periodoDesdeView) {
		this.periodoDesdeView = periodoDesdeView;
	}

	public String getAnioDesdeView() {
		return anioDesdeView;
	}
	public void setAnioDesdeView(String anioDesdeView) {
		this.anioDesdeView = anioDesdeView;
	}

	public String getPeriodoHastaView() {
		return periodoHastaView;
	}
	public void setPeriodoHastaView(String periodoHastaView) {
		this.periodoHastaView = periodoHastaView;
	}

	public String getAnioHastaView() {
		return anioHastaView;
	}
	public void setAnioHastaView(String anioHastaView) {
		this.anioHastaView = anioHastaView;
	}

	public Integer getPeriodo() {
		return periodo;
	}
	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
		this.periodoView = StringUtil.formatInteger(periodo);
	}

	public Integer getAnio() {
		return anio;
	}
	public void setAnio(Integer anio) {
		this.anio = anio;
		this.anioView = StringUtil.formatInteger(anio);
	}

	public String getPeriodoView() {
		return periodoView;
	}
	public void setPeriodoView(String periodoView) {
		this.periodoView = periodoView;
	}

	public String getAnioView() {
		return anioView;
	}
	public void setAnioView(String anioView) {
		this.anioView = anioView;
	}
	
	public String getPeriodoAnioView() {
		return periodoView + "/"+ anioView;
	}

	public boolean isQuitarEnabled() {
		return quitarEnabled;
	}
	public void setQuitarEnabled(boolean quitarEnabled) {
		this.quitarEnabled = quitarEnabled;
	}
	
	
}
