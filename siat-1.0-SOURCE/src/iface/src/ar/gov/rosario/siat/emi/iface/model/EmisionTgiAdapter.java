//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.model.GenericAtrDefinition;
import ar.gov.rosario.siat.def.iface.model.VencimientoVO;
import ar.gov.rosario.siat.def.iface.model.ZonaVO;
import ar.gov.rosario.siat.emi.iface.util.EmiSecurityConstants;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Adapter del Emision de Tgi
 * 
 * @author tecso
 */
public class EmisionTgiAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "emisionAdapterVO";

	private ZonaVO zona = new ZonaVO();
	
	private Integer anio;
	
	private Integer periodoDesde;
	
	private Integer periodoHasta;
	
	private VencimientoVO vencimiento = new VencimientoVO();

	private String anioView;
	
	private String periodoDesdeView;
	
	private String periodoHastaView;

	// Contiene las zonas como atributo Zona con su DomAtr y la lista de DomAtrVal
	private GenericAtrDefinition genericAtrDefinition = new GenericAtrDefinition();

	// Lista de Vencimientos
	private List<VencimientoVO> listVencimiento = new ArrayList<VencimientoVO>();

	// Constructores
    public EmisionTgiAdapter(){
    	super(EmiSecurityConstants.ABM_EMISIONMAS);
    }
    
    //  Getters y Setters
	public GenericAtrDefinition getGenericAtrDefinition() {
		return genericAtrDefinition;
	}
	public void setGenericAtrDefinition(GenericAtrDefinition genericAtrDefinition) {
		this.genericAtrDefinition = genericAtrDefinition;
	}
	public ZonaVO getZona() {
		return zona;
	}
	public void setZona(ZonaVO zona) {
		this.zona = zona;
	}

	public VencimientoVO getVencimiento() {
		return vencimiento;
	}

	public void setVencimiento(VencimientoVO vencimiento) {
		this.vencimiento = vencimiento;
	}

	public List<VencimientoVO> getListVencimiento() {
		return listVencimiento;
	}

	public void setListVencimiento(List<VencimientoVO> listVencimiento) {
		this.listVencimiento = listVencimiento;
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
	
}