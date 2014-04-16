//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.pro.iface.model.EstadoCorridaVO;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * SearchPage del Rescate
 * 
 * @author Tecso
 *
 */
public class SaldoPorCaducidadSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "saldoPorCaducidadSearchPageVO";
	
	public Long idRecursoSelected;
	
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	
	private List<PlanVO> listPlan = new ArrayList<PlanVO>();

	private SaldoPorCaducidadVO    saldoPorCaducidad  = new SaldoPorCaducidadVO();
	
	private List<EstadoCorridaVO> listEstadoCorrida = new ArrayList<EstadoCorridaVO>();
	
	private Date fechaDesde;
	private Date fechaHasta;
	
	private String 	fechaDesdeView = "";
	private String 	fechaHastaView = "";
	
	private Boolean admSeleccionBussEnabled = true;
	private Boolean admProcesoBussEnabled   = true;
	
	// Constructores
	public SaldoPorCaducidadSearchPage() {       
       super(GdeSecurityConstants.ABM_SALDOPORCADUCIDAD);        
    }
	
	// Getters y Setters
	public List<PlanVO> getListPlan() {
		return listPlan;
	}
	public void setListPlan(List<PlanVO> listPlan) {
		this.listPlan = listPlan;
	}

	public SaldoPorCaducidadVO getSaldoPorCaducidad() {
		return saldoPorCaducidad;
	}

	public void setSaldoPorCaducidad(SaldoPorCaducidadVO saldoPorCaducidad) {
		this.saldoPorCaducidad = saldoPorCaducidad;
	}

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}
	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}
	public Date getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
		this.fechaDesdeView = DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaDesdeView() {
		return fechaDesdeView;
	}
	public void setFechaDesdeView(String fechaDesdeView) {
		this.fechaDesdeView = fechaDesdeView;
	}
	public Date getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
		this.fechaHastaView = DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaHastaView() {
		return fechaHastaView;
	}
	public void setFechaHastaView(String fechaHastaView) {
		this.fechaHastaView = fechaHastaView;
	}
	public List<EstadoCorridaVO> getListEstadoCorrida() {
		return listEstadoCorrida;
	}
	public void setListEstadoCorrida(List<EstadoCorridaVO> listEstadoCorrida) {
		this.listEstadoCorrida = listEstadoCorrida;
	}

	public Boolean getAdmSeleccionBussEnabled() {
		return admSeleccionBussEnabled;
	}
	public void setAdmSeleccionBussEnabled(Boolean admSeleccionBussEnabled) {
		this.admSeleccionBussEnabled = admSeleccionBussEnabled;
	}
	public Boolean getAdmProcesoBussEnabled() {
		return admProcesoBussEnabled;
	}
	public void setAdmProcesoBussEnabled(Boolean admProcesoBussEnabled) {
		this.admProcesoBussEnabled = admProcesoBussEnabled;
	}
	

	public Long getIdRecursoSelected() {
		return idRecursoSelected;
	}

	public void setIdRecursoSelected(Long idRecursoSelected) {
		this.idRecursoSelected = idRecursoSelected;
	}

	// View getters
	public String getAdmSeleccionEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getAdmSeleccionBussEnabled(),
				GdeSecurityConstants.ABM_SALDOPORCADUCIDAD,	GdeSecurityConstants.MTD_ADM_SELECCION);
	}
	public String getAdmProcesoEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getAdmProcesoBussEnabled(),
				GdeSecurityConstants.ABM_SALDOPORCADUCIDAD,	GdeSecurityConstants.MTD_ADM_PROCESO);
	}
	

}
