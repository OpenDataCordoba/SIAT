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
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del Saldo Por Caducidad Masivo de Convenios
 * @author tecso
 *
 */
public class SaldoPorCaducidadVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "saldoPorCaducidadVO";
	
	
	private RecursoVO recurso = new RecursoVO();
	
	private PlanVO plan = new PlanVO();
	
	private Date fechaFormDesdeSaldo;

	private Date fechaFormHastaSaldo;
	
	private Date fechaSaldo = new Date();
	
	private String observacion;
		
	private CasoVO caso = new CasoVO();
	
	private CorridaVO corrida = new CorridaVO();
	
	private Double cuotaSuperiorA;
	
	private Long idSelAlm =0L;
	
	private String fechaFormDesdeSaldoView = "";
	private String fechaFormHastaSaldoView = "";
	private String fechaSaldoView = "";
	private String cuotaSuperiorAView="";
	
	private Boolean admSeleccionBussEnabled = true;
	private Boolean admProcesoBussEnabled = true;
	
	private Boolean modificaActivo=false;
	private Boolean eliminaActivo=false;
	private Boolean seleccionaActivo=false;
	private Boolean administraActivo=false;
	private List<ConvenioVO>listConveniosSelAlm;

	


	// Constructores
	public SaldoPorCaducidadVO() {
		super();
	}

	// Getters y Setters
	public RecursoVO getRecurso() {
		return recurso;
	}
	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}

	public PlanVO getPlan() {
		return plan;
	}
	public void setPlan(PlanVO plan) {
		this.plan = plan;
	}

	public Date getFechaFormDesdeSaldo() {
		return fechaFormDesdeSaldo;
	}

	public void setFechaFormDesdeSaldo(Date fechaFormDesdeSaldo) {
		this.fechaFormDesdeSaldo = fechaFormDesdeSaldo;
		this.fechaFormDesdeSaldoView = DateUtil.formatDate(fechaFormDesdeSaldo, DateUtil.ddSMMSYYYY_MASK);
	}

	public Date getFechaFormHastaSaldo() {
		return fechaFormHastaSaldo;
	}

	public void setFechaFormHastaSaldo(Date fechaFormHastaSaldo) {
		this.fechaFormHastaSaldo = fechaFormHastaSaldo;
		this.fechaFormHastaSaldoView = DateUtil.formatDate(fechaFormHastaSaldo, DateUtil.ddSMMSYYYY_MASK);
	}

	public Date getFechaSaldo() {
		return fechaSaldo;
	}

	public void setFechaSaldo(Date fechaSaldo) {
		this.fechaSaldo = fechaSaldo;
		this.fechaSaldoView = DateUtil.formatDate(fechaSaldo, DateUtil.ddSMMSYYYY_MASK);
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

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

	public Double getCuotaSuperiorA() {
		return cuotaSuperiorA;
	}

	public void setCuotaSuperiorA(Double cuotaSuperiorA) {
		this.cuotaSuperiorA = cuotaSuperiorA;
		this.cuotaSuperiorAView = StringUtil.formatDouble(cuotaSuperiorA);
	}

	// View getters y setters
	public String getFechaFormDesdeSaldoView() {
		return fechaFormDesdeSaldoView;
	}
	public void setFechaFormDesdeSaldoView(String fechaFormDesdeSaldoView) {
		this.fechaFormDesdeSaldoView = fechaFormDesdeSaldoView;
	}

	public String getFechaFormHastaSaldoView() {
		return fechaFormHastaSaldoView;
	}
	public void setFechaFormHastaSaldoView(String fechaFormHastaSaldoView) {
		this.fechaFormHastaSaldoView = fechaFormHastaSaldoView;
	}

	public String getFechaSaldoView() {
		return fechaSaldoView;
	}
	public void setFechaSaldoView(String fechaSaldoView) {
		this.fechaSaldoView = fechaSaldoView;
	}

	public String getCuotaSuperiorAView() {
		return cuotaSuperiorAView;
	}
	public void setCuotaSuperiorAView(String cuotaSuperiorAView) {
		this.cuotaSuperiorAView = cuotaSuperiorAView;
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

	public Boolean getEliminaConvenioActivo (){
		if (corrida.getEstadoCorrida().getDesEstadoCorrida().equals("En espera continuar")){
			return true;
		}else{
			return false;
		}
	}

}
