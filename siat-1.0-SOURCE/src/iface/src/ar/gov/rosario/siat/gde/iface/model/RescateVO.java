//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.pro.iface.model.CorridaVO;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Value Object del Rescate Masivo de Convenios
 * @author tecso
 *
 */
public class RescateVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "rescateVO";
	
	
	private RecursoVO recurso = new RecursoVO();
	
	private PlanVO plan = new PlanVO();
	
	private Date fechaRescate;

	private Date fechaVigRescate;
	
	private String observacion;
		
	private CasoVO caso = new CasoVO();
	
	private CorridaVO corrida = new CorridaVO();
	private Long idSelAlm =0L;
	private String fechaRescateView    = "";
	private String fechaVigRescateView = "";
	
	private Boolean admSeleccionBussEnabled = true;
	private Boolean admProcesoBussEnabled = true;
	
	private Boolean modificaActivo=false;
	private Boolean eliminaActivo=false;
	private Boolean seleccionaActivo=false;
	private Boolean administraActivo=false;
	private Boolean eliminaConvenioActivo=false;
	private List<ConvenioVO>listConveniosSelAlm;
	// Constructores
	public RescateVO() {
		super();
	}

	// Getters y Setters
	public CasoVO getCaso() {
		return caso;
	}
	public void setCaso(CasoVO caso) {
		this.caso = caso;
	}
	public CorridaVO getCorrida() {
		return corrida;
	}
	public void setCorrida(CorridaVO corrida) {
		this.corrida = corrida;
	}
	public Date getFechaRescate() {
		return fechaRescate;
	}
	public void setFechaRescate(Date fechaRescate) {
		this.fechaRescate = fechaRescate;
		this.fechaRescateView = DateUtil.formatDate(fechaRescate, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaRescateView() {
		return fechaRescateView;
	}
	public void setFechaRescateView(String fechaRescateView) {
		this.fechaRescateView = fechaRescateView;
	}
	public Date getFechaVigRescate() {
		return fechaVigRescate;
	}
	public void setFechaVigRescate(Date fechaVigRescate) {
		this.fechaVigRescate = fechaVigRescate;
		this.fechaVigRescateView = DateUtil.formatDate(fechaVigRescate, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaVigRescateView() {
		return fechaVigRescateView;
	}
	public void setFechaVigRescateView(String fechaVigRescateView) {
		this.fechaVigRescateView = fechaVigRescateView;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public PlanVO getPlan() {
		return plan;
	}
	public void setPlan(PlanVO plan) {
		this.plan = plan;
	}
	public RecursoVO getRecurso() {
		return recurso;
	}
	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}
	
	

	public Boolean getModificaActivo() {
		return modificaActivo;
	}

	public void setModificaActivo(Boolean modificaActivo) {
		this.modificaActivo = modificaActivo;
	}

	public Boolean getEliminaActivo() {
		return eliminaActivo;
	}

	public void setEliminaActivo(Boolean eliminaActivo) {
		this.eliminaActivo = eliminaActivo;
	}

	public Boolean getSeleccionaActivo() {
		return seleccionaActivo;
	}

	public void setSeleccionaActivo(Boolean seleccionaActivo) {
		this.seleccionaActivo = seleccionaActivo;
	}

	public Boolean getAdministraActivo() {
		return administraActivo;
	}

	public void setAdministraActivo(Boolean administraActivo) {
		this.administraActivo = administraActivo;
	}

	public List<ConvenioVO> getListConveniosSelAlm() {
		return listConveniosSelAlm;
	}

	public void setListConveniosSelAlm(List<ConvenioVO> listConveniosSelAlm) {
		this.listConveniosSelAlm = listConveniosSelAlm;
	}
	
	public Long getIdSelAlm() {
		return idSelAlm;
	}

	public void setIdSelAlm(Long idSelAlm) {
		this.idSelAlm = idSelAlm;
	}

	// Buss flags getters y setters
	public Boolean getAdmProcesoBussEnabled() {
		return admProcesoBussEnabled;
	}
	public void setAdmProcesoBussEnabled(Boolean admProcesoBussEnabled) {
		this.admProcesoBussEnabled = admProcesoBussEnabled;
	}
	public Boolean getAdmSeleccionBussEnabled() {
		return admSeleccionBussEnabled;
	}
	public void setAdmSeleccionBussEnabled(Boolean admSeleccionBussEnabled) {
		this.admSeleccionBussEnabled = admSeleccionBussEnabled;
	}

	public Boolean getEliminaConvenioActivo() {
		return eliminaConvenioActivo;
	}

	public void setEliminaConvenioActivo(Boolean eliminaConvenioActivo) {
		this.eliminaConvenioActivo = eliminaConvenioActivo;
	}
	
	
	// View flags getters
	
	
	// View getters

}
