//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.CategoriaVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.ViaDeudaVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.PlanillaVO;

/**
 * Report de Recaudacion por Recurso
 * 
 * @author Tecso
 *
 */
public class RecaudacionReport extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "recaudacionReportVO";
	public static final long CTD_MAX_REG = 8000;
	
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	private RecursoVO recurso = new RecursoVO();
	private ViaDeudaVO viaDeuda = new ViaDeudaVO();
	
	private List<CategoriaVO> listCategoria = new ArrayList<CategoriaVO>();
	private List<ViaDeudaVO> listViaDeuda = new ArrayList<ViaDeudaVO>();
	private Date   fechaDesde;
	private String fechaDesdeView;
	private Date   fechaHasta;
	private String fechaHastaView;

	private Integer tipoFecha=0;
	
	private PlanillaVO reporteGenerado = new PlanillaVO();
	
	//private boolean existeCorridaEnProceso = false;
	
	private String desRunningRun = "";
	private String estRunningRun = "";
	private String desErrorRun = "";
	private String estErrorRun = "";

	//Flags
	private boolean procesando = false;
	private boolean existeReporteGenerado = false;
	private boolean error = false;
	
	// Constructores
	public RecaudacionReport() {       
		super(GdeSecurityConstants.ABM_RECAUDACION_REPORT);        
	}
	
	// Getters y Setters	
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
	public List<CategoriaVO> getListCategoria() {
		return listCategoria;
	}
	public void setListCategoria(List<CategoriaVO> listCategoria) {
		this.listCategoria = listCategoria;
	}
	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}
	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}
	public RecursoVO getRecurso() {
		return recurso;
	}
	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}
	public PlanillaVO getReporteGenerado() {
		return reporteGenerado;
	}
	public void setReporteGenerado(PlanillaVO reporteGenerado) {
		this.reporteGenerado = reporteGenerado;
	}
	
	/*public boolean isExisteCorridaEnProceso() {
		return existeCorridaEnProceso;
	}
	public void setExisteCorridaEnProceso(boolean existeCorridaEnProceso) {
		this.existeCorridaEnProceso = existeCorridaEnProceso;
	}*/
		
	public Integer getTipoFecha() {
		return tipoFecha;
	}

	public void setTipoFecha(Integer tipoFecha) {
		this.tipoFecha = tipoFecha;
	}

	public List<ViaDeudaVO> getListViaDeuda() {
		return listViaDeuda;
	}
	
	public void setListViaDeuda(List<ViaDeudaVO> listViaDeuda) {
		this.listViaDeuda = listViaDeuda;
	}
	
	public ViaDeudaVO getViaDeuda() {
		return viaDeuda;
	}
	
	public void setViaDeuda(ViaDeudaVO viaDeuda) {
		this.viaDeuda = viaDeuda;
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
	
	//	 View getters

	// Metodos auxiliares utilizados para determinar si se selecciono:
	// recurso, categoria
	public boolean getRecursoSeleccionado(){
		return !ModelUtil.isNullOrEmpty(this.getRecurso());
	}
	public boolean getCategoriaSeleccionada(){
		return !ModelUtil.isNullOrEmpty(this.getRecurso().getCategoria());
	}
	public boolean getViaDeudaSeleccionada(){
		return !ModelUtil.isNullOrEmpty(this.getViaDeuda());
	}
	
	
}
