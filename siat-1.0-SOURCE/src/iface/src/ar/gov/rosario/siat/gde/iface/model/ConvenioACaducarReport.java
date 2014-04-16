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
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.PlanillaVO;

/**
 * Reporte de Convenios A Caducar
 * 
 * @author Tecso
 *
 */
public class ConvenioACaducarReport extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "convenioACaducarReportVO";

	private PlanillaVO reporteGenerado = new PlanillaVO();
	
	// Convenio utilizado para cargarle los datos de los filtros de busqueda:
	// 		convenio.plan
	// 		convenio.viaDeuda
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
	
	private Integer cuotaDesde; 
	private String  cuotaDesdeView;
	private Integer cuotaHasta;
	private String  cuotaHastaView;
	
	private Double importeCuotaDesde; 
	private String  importeCuotaDesdeView;
	private Double importeCuotaHasta;
	private String  importeCuotaHastaView;

	private List<EstadoConvenioVO> listEstadoConvenio = new ArrayList<EstadoConvenioVO>();

	private Date   fechaCaducidad;
	private String fechaCaducidadView;
	
	private String desRunningRun = "";
	private String estRunningRun = "";
	private String desErrorRun = "";
	private String estErrorRun = "";

	//Flags
	private boolean procesando = false;
	private boolean existeReporteGenerado = false;
	private boolean error = false;
	
	// Constructores
	public ConvenioACaducarReport() {       
       super(GdeSecurityConstants.CONSULTAR_CONVENIO_A_CADUCAR);        
    }

	// Getters y Setters
	public String getDesErrorRun() {
		return desErrorRun;
	}
	public void setDesErrorRun(String desErrorRun) {
		this.desErrorRun = desErrorRun;
	}
	public String getDesRunningRun() {
		return desRunningRun;
	}
	public void setDesRunningRun(String desRunningRun) {
		this.desRunningRun = desRunningRun;
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
	public PlanillaVO getReporteGenerado() {
		return reporteGenerado;
	}
	public void setReporteGenerado(PlanillaVO reporteGenerado) {
		this.reporteGenerado = reporteGenerado;
	}
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
	}
	public String getCuotaHastaView() {
		return cuotaHastaView;
	}
	public void setCuotaHastaView(String cuotaHastaView) {
		this.cuotaHastaView = cuotaHastaView;
	}
	public Date getFechaCaducidad() {
		return fechaCaducidad;
	}
	public void setFechaCaducidad(Date fechaCaducidad) {
		this.fechaCaducidad = fechaCaducidad;
	}
	public String getFechaCaducidadView() {
		return fechaCaducidadView;
	}
	public void setFechaCaducidadView(String fechaCaducidadView) {
		this.fechaCaducidadView = fechaCaducidadView;
	}
	public Date getFechaConvenioDesde() {
		return fechaConvenioDesde;
	}
	public void setFechaConvenioDesde(Date fechaConvenioDesde) {
		this.fechaConvenioDesde = fechaConvenioDesde;
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
	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}
	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
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
	public Double getImporteCuotaDesde() {
		return importeCuotaDesde;
	}
	public void setImporteCuotaDesde(Double importeCuotaDesde) {
		this.importeCuotaDesde = importeCuotaDesde;
	}
	public String getImporteCuotaDesdeView() {
		return importeCuotaDesdeView;
	}
	public void setImporteCuotaDesdeView(String importeCuotaDesdeView) {
		this.importeCuotaDesdeView = importeCuotaDesdeView;
	}
	public Double getImporteCuotaHasta() {
		return importeCuotaHasta;
	}
	public void setImporteCuotaHasta(Double importeCuotaHasta) {
		this.importeCuotaHasta = importeCuotaHasta;
	}
	public String getImporteCuotaHastaView() {
		return importeCuotaHastaView;
	}
	public void setImporteCuotaHastaView(String importeCuotaHastaView) {
		this.importeCuotaHastaView = importeCuotaHastaView;
	}

	// Metodos auxiliares utilizados para determinar si se selecciono:
	// plan, viaDeuda, cuota desde, cuota hasta y estado de convenio
	public boolean getPlanSeleccionado(){
		return !ModelUtil.isNullOrEmpty(this.getConvenio().getPlan());
	}
	public boolean getViaDeudaSeleccionada(){
		return !ModelUtil.isNullOrEmpty(this.getConvenio().getViaDeuda());
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
