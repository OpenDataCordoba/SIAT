//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.model.IntegerVO;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.rec.iface.model.NovedadRSVO;
import ar.gov.rosario.siat.rec.iface.model.TipoTramiteRSVO;
import ar.gov.rosario.siat.rec.iface.util.RecSecurityConstants;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * SearchPage del Convenio
 * 
 * @author Tecso
 *
 */
public class NovedadRSSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "novedadRSSearchPageVO";
	
	private NovedadRSVO novedadRS = new NovedadRSVO();
	
	private RecursoVO recurso = new RecursoVO();
	
	private List<TipoTramiteRSVO> listTipoTramiteRS = new ArrayList<TipoTramiteRSVO>();
	
    private List<IntegerVO> listEstadoRS = new ArrayList<IntegerVO>();
	
	private Date fechaNovedadDesde;
	
	private Date fechaNovedadHasta;
	
	private Boolean aplicarBussEnabled    		  = true;
	private Boolean aplicarMasivoBussEnabled      = true;
	
	private Boolean origenLiquidacion = false;
	
	private Boolean filtroConObservacion   = false;
	
	// Constructores
	public NovedadRSSearchPage() {       
       super(RecSecurityConstants.ABM_NOVEDADRS);        
    }

	// Getters y Setters

	public NovedadRSVO getNovedadRS() {
		return novedadRS;
	}

	public void setNovedadRS(NovedadRSVO novedadRS) {
		this.novedadRS = novedadRS;
	}

	public RecursoVO getRecurso() {
		return recurso;
	}

	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}

	public List<TipoTramiteRSVO> getListTipoTramiteRS() {
		return listTipoTramiteRS;
	}

	public void setListTipoTramiteRS(List<TipoTramiteRSVO> listTipoTramiteRS) {
		this.listTipoTramiteRS = listTipoTramiteRS;
	}

	public Date getFechaNovedadDesde() {
		return fechaNovedadDesde;
	}

	public void setFechaNovedadDesde(Date fechaNovedadDesde) {
		this.fechaNovedadDesde = fechaNovedadDesde;
	}

	public Date getFechaNovedadHasta() {
		return fechaNovedadHasta;
	}

	public void setFechaNovedadHasta(Date fechaNovedadHasta) {
		this.fechaNovedadHasta = fechaNovedadHasta;
	}
	
	public List<IntegerVO> getListEstadoRS() {
		return listEstadoRS;
	}
	
	public void setListEstadoRS(List<IntegerVO> listEstadoRS) {
		this.listEstadoRS = listEstadoRS;
	}
	
	public Boolean getOrigenLiquidacion() {
		return origenLiquidacion;
	}
	
	public void setOrigenLiquidacion(Boolean origenLiquidacion) {
		this.origenLiquidacion = origenLiquidacion;
	}
	
	// View getters	
	public String getFechaNovedadDesdeView(){
		return (this.fechaNovedadDesde!=null)? DateUtil.formatDate(this.fechaNovedadDesde, DateUtil.ddSMMSYYYY_MASK):"";
	}
	
	public String getFechaNovedadHastaView(){
		return (this.fechaNovedadHasta!=null)?DateUtil.formatDate(this.fechaNovedadHasta, DateUtil.ddSMMSYYYY_MASK):"";
	}
	
	public Boolean getFiltroConObservacion() {
		return filtroConObservacion;
	}

	public void setFiltroConObservacion(Boolean filtroConObservacion) {
		this.filtroConObservacion = filtroConObservacion;
	}

	public String getName(){
		return NAME;
	}
	
	//	Flags Seguridad
	public Boolean getAplicarBussEnabled() {
		return aplicarBussEnabled;
	}

	public void setAplicarBussEnabled(Boolean aplicarBussEnabled) {
		this.aplicarBussEnabled = aplicarBussEnabled;
	}
	
	public String getAplicarEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getAplicarBussEnabled(), 
				RecSecurityConstants.ABM_NOVEDADRS, RecSecurityConstants.MTD_APLICAR);
	}
	
	public Boolean getAplicarMasivoBussEnabled() {
		return aplicarMasivoBussEnabled;
	}

	public void setAplicarMasivoBussEnabled(Boolean aplicarMasivoBussEnabled) {
		this.aplicarMasivoBussEnabled = aplicarMasivoBussEnabled;
	}
	
	public String getAplicarMasivoEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getAplicarMasivoBussEnabled(), 
				RecSecurityConstants.ABM_NOVEDADRS, RecSecurityConstants.MTD_APLICARMASIVO);
	}
	
}
