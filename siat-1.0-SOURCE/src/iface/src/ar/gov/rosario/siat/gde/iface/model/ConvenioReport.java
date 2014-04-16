//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.ViaDeudaVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.PlanillaVO;
import coop.tecso.demoda.iface.model.TipoReporte;

/**
 * Report del Convenio
 * 
 * @author Tecso
 *
 */
public class ConvenioReport extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "convenioReportVO";
	public static final long CTD_MAX_REG = 12000;
	
	// Convenio utilizado para cargarle los datos de los filtros de busqueda:
	// 		convenio.plan
	// 		convenio.viaDeuda
	// 		convenio.procurador
	// 		convenio.estadoConvenio
	private ConvenioVO convenio = new ConvenioVO();
	
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	private RecursoVO recurso = new RecursoVO();
	private List<PlanVO> listPlan = new ArrayList<PlanVO>();
	private Date   fechaConvenioDesde;
	private String fechaConvenioDesdeView;
	private Date   fechaConvenioHasta;
	private String fechaConvenioHastaView;
	private List<ViaDeudaVO> listViaDeuda = new ArrayList<ViaDeudaVO>();
	// propiedad utilizada para mantener en la vista el id de la via deuda judicial
	//private Long idViaDeudaJudicial = null; 
	
	private List<ProcuradorVO> listProcurador = new ArrayList<ProcuradorVO>();
	private Integer cuotaDesde; 
	private String  cuotaDesdeView;
	private Integer cuotaHasta;
	private String  cuotaHastaView;

	private List<EstadoConvenioVO> listEstadoConvenio = new ArrayList<EstadoConvenioVO>();
	private List<TipoReporte> listTipoReporte = new ArrayList<TipoReporte>();
	private TipoReporte tipoReporte = TipoReporte.OpcionNula;
	private String idTipoReporte = null;
	
	private Long idProcuradorSesion = null; // id del procurador de la sesion
	
	private PlanillaVO reporteGenerado = new PlanillaVO();

	private String desRunningRun = "";
	private String estRunningRun = "";
	private String desErrorRun = "";
	private String estErrorRun = "";
	
	//Flags
	private boolean procesando = false;
	private boolean existeReporteGenerado = false;
	private boolean error = false;

	// bandera utilizada por el jsp para visualizar o no el combo procurador
	private boolean visualizarComboProcurador = false;
	
	// Constructores
	public ConvenioReport() {       
       super(GdeSecurityConstants.ABM_CONVENIO_REPORT);        
    }

	// Getters y Setters	
	public ConvenioVO getConvenio() {
		return convenio;
	}
	public void setConvenio(ConvenioVO convenio) {
		this.convenio = convenio;
	}
	public Integer getCuotaDesde() {
		return cuotaDesde;
	}
	public void setCuotaDesde(Integer cuotaDesde) {
		this.cuotaDesde = cuotaDesde;
		this.cuotaDesdeView = StringUtil.formatInteger(this.cuotaDesde);
	}
	public String getCuotaDesdeView() {
		return cuotaDesdeView;
	}
	public void setCuotaDesdeView(String cuotaDesdeView) {
		this.cuotaDesdeView = cuotaDesdeView;
	}
	public Integer getCuotaHasta() {
		return cuotaHasta;
	}
	public void setCuotaHasta(Integer cuotaHasta) {
		this.cuotaHasta = cuotaHasta;
		this.cuotaHastaView = StringUtil.formatInteger(this.cuotaHasta);
	}
	public String getCuotaHastaView() {
		return cuotaHastaView;
	}
	public void setCuotaHastaView(String cuotaHastaView) {
		this.cuotaHastaView = cuotaHastaView;
	}
	public Date getFechaConvenioDesde() {
		return fechaConvenioDesde;
	}
	public void setFechaConvenioDesde(Date fechaConvenioDesde) {
		this.fechaConvenioDesde = fechaConvenioDesde;
		this.fechaConvenioDesdeView = DateUtil.formatDate(fechaConvenioDesde, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaConvenioDesdeView() {
		return fechaConvenioDesdeView;
	}
	public void setFechaConvenioDesdeView(String fechaConvenioDesdeView) {
		this.fechaConvenioDesdeView = fechaConvenioDesdeView;
	}
	public Date getFechaConvenioHasta() {
		return fechaConvenioHasta;
	}
	public void setFechaConvenioHasta(Date fechaConvenioHasta) {
		this.fechaConvenioHasta = fechaConvenioHasta;
		this.fechaConvenioHastaView = DateUtil.formatDate(fechaConvenioHasta, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaConvenioHastaView() {
		return fechaConvenioHastaView;
	}
	public void setFechaConvenioHastaView(String fechaConvenioHastaView) {
		this.fechaConvenioHastaView = fechaConvenioHastaView;
	}
	public List<EstadoConvenioVO> getListEstadoConvenio() {
		return listEstadoConvenio;
	}
	public void setListEstadoConvenio(List<EstadoConvenioVO> listEstadoConvenio) {
		this.listEstadoConvenio = listEstadoConvenio;
	}
	public List<PlanVO> getListPlan() {
		return listPlan;
	}
	public void setListPlan(List<PlanVO> listPlan) {
		this.listPlan = listPlan;
	}
	public List<ProcuradorVO> getListProcurador() {
		return listProcurador;
	}
	public void setListProcurador(List<ProcuradorVO> listProcurador) {
		this.listProcurador = listProcurador;
	}
	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}
	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}
	public List<TipoReporte> getListTipoReporte() {
		return listTipoReporte;
	}
	public void setListTipoReporte(List<TipoReporte> listTipoReporte) {
		this.listTipoReporte = listTipoReporte;
	}
	public List<ViaDeudaVO> getListViaDeuda() {
		return listViaDeuda;
	}
	public void setListViaDeuda(List<ViaDeudaVO> listViaDeuda) {
		this.listViaDeuda = listViaDeuda;
	}
	public RecursoVO getRecurso() {
		return recurso;
	}
	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}
	public TipoReporte getTipoReporte() {
		return tipoReporte;
	}
	public void setTipoReporte(TipoReporte tipoReporte) {
		this.tipoReporte = tipoReporte;
	}
	/*
	public Long getIdViaDeudaJudicial() {
		return idViaDeudaJudicial;
	}
	public void setIdViaDeudaJudicial(Long idViaDeudaJudicial) {
		this.idViaDeudaJudicial = idViaDeudaJudicial;
	}
	*/
	public PlanillaVO getReporteGenerado() {
		return reporteGenerado;
	}
	public void setReporteGenerado(PlanillaVO reporteGenerado) {
		this.reporteGenerado = reporteGenerado;
	}
	public String getIdTipoReporte() {
		return idTipoReporte;
	}
	public void setIdTipoReporte(String idTipoReporte) {
		this.idTipoReporte = idTipoReporte;
	}
	public Long getIdProcuradorSesion() {
		return idProcuradorSesion;
	}
	public void setIdProcuradorSesion(Long idProcuradorSesion) {
		this.idProcuradorSesion = idProcuradorSesion;
	}
	public boolean getVisualizarComboProcurador() {
		return visualizarComboProcurador;
	}
	public void setVisualizarComboProcurador(boolean visualizarComboProcurador) {
		this.visualizarComboProcurador = visualizarComboProcurador;
	}
	
	public String getDesRunningRun() {
		return desRunningRun;
	}

	public void setDesRunningRun(String desRunningRun) {
		this.desRunningRun = desRunningRun;
	}

	public String getEstRunningRun() {
		return estRunningRun;
	}

	public void setEstRunningRun(String estRunningRun) {
		this.estRunningRun = estRunningRun;
	}

	public boolean isExisteReporteGenerado() {
		return existeReporteGenerado;
	}

	public void setExisteReporteGenerado(boolean existeReporteGenerado) {
		this.existeReporteGenerado = existeReporteGenerado;
	}

	public boolean isProcesando() {
		return procesando;
	}

	public void setProcesando(boolean procesando) {
		this.procesando = procesando;
	}
	
	public String getDesErrorRun() {
		return desErrorRun;
	}

	public void setDesErrorRun(String desErrorRun) {
		this.desErrorRun = desErrorRun;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public String getEstErrorRun() {
		return estErrorRun;
	}

	public void setEstErrorRun(String estErrorRun) {
		this.estErrorRun = estErrorRun;
	}

	
	// View getters
	
	
	/*
	public boolean getViaDeudaEsJudicialView(){
		Long idViaDeudaJudicial = this.getIdViaDeudaJudicial();
		return (idViaDeudaJudicial != null && idViaDeudaJudicial.equals(this.getConvenio().getViaDeuda().getId()));
	}
	 */
	
	// Metodos auxiliares utilizados para determinar si se selecciono:
	// plan, viaDeuda, procurador, cuota desde, cuota hasta y estado de convenio
	public boolean getPlanSeleccionado(){
		return !ModelUtil.isNullOrEmpty(this.getConvenio().getPlan());
	}
	public boolean getViaDeudaSeleccionada(){
		return !ModelUtil.isNullOrEmpty(this.getConvenio().getViaDeuda());
	}
	public boolean getProcuradorSeleccionado(){
		return !ModelUtil.isNullOrEmpty(this.getConvenio().getProcurador());
	}
	public boolean getCuotaDesdeSeleccionada(){
		return this.getCuotaDesde() != null;
	}
	public boolean getCuotaHastaSeleccionada(){
		return this.getCuotaHasta() != null;
	}
	public boolean getEstadoConvenioSeleccionado(){
		return !ModelUtil.isNullOrEmpty(this.getConvenio().getEstadoConvenio());
	}
	
	
}
