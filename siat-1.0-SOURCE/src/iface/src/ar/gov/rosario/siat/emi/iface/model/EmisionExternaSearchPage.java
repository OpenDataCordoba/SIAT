//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.emi.iface.util.EmiSecurityConstants;
import ar.gov.rosario.siat.pro.iface.model.EstadoCorridaVO;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * SearchPage de Emision Masiva
 * 
 * @author Tecso
 *
 */
public class EmisionExternaSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "emisionExternaSearchPageVO";
	
	private EmisionVO emision =  new EmisionVO();
	private Date fechaDesde; 
	private Date fechaHasta; 
	private String fechaDesdeView = "";
	private String fechaHastaView = "";
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	private List<EstadoCorridaVO> listEstadoCorrida = new ArrayList<EstadoCorridaVO>();
	
	private List<String> listArchivo = new ArrayList<String>();
	
	// Constructores
	public EmisionExternaSearchPage() {       
       super(EmiSecurityConstants.ABM_EMISION_EXTERNA);        
    }
	
	// Getters y Setters
	public EmisionVO getEmision() {
		return emision;
	}
	public void setEmision(EmisionVO emision) {
		this.emision = emision;
	}

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}
	
	public List<EstadoCorridaVO> getListEstadoCorrida() {
		return listEstadoCorrida;
	}

	public void setListEstadoCorrida(List<EstadoCorridaVO> listEstadoCorrida) {
		this.listEstadoCorrida = listEstadoCorrida;
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
	
	public List<String> getListArchivo() {
		return listArchivo;
	}
	public void setListArchivo(List<String> listArchivo) {
		this.listArchivo = listArchivo;
	}

	public String getAdministrarProcesoEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(EmiSecurityConstants.ABM_EMISION_EXTERNA, EmiSecurityConstants.ACT_ADMINISTRAR_PROCESO);
	}

}
