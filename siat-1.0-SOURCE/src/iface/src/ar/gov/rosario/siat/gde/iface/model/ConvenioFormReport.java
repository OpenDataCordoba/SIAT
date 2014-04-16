//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.ViaDeudaVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.PlanillaVO;

/**
 * Report del Convenio
 * 
 * @author Tecso
 *
 */
public class ConvenioFormReport extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "convenioFormReportVO";
	public static final long CTD_MAX_REG = 8000;
	
	// Convenio utilizado para cargarle los datos de los filtros de busqueda:
	// 		convenio.viaDeuda
	private ConvenioVO convenio = new ConvenioVO();
	
	private List<ViaDeudaVO> listViaDeuda = new ArrayList<ViaDeudaVO>();
	
	private Date   fechaConvenioDesde;
	private Date   fechaConvenioHasta;
	private String fechaConvenioDesdeView;
	private String fechaConvenioHastaView;
	
	private Long idProcuradorSesion = null; // id del procurador de la sesion
	
	private PlanillaVO reporteGenerado = new PlanillaVO();

	// Arbol contenedor del resultado del reporte
	private List<ProcuradorVO> listProcurador = new ArrayList<ProcuradorVO>();
	private List<ProcuradorReport> listProcuradorReport = new ArrayList<ProcuradorReport>();
	private List<PlanReport> listPlanReport = new ArrayList<PlanReport>();
	
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
	public ConvenioFormReport() {       
       super(GdeSecurityConstants.ABM_CONVENIO_FORM_REPORT);        
    }

	// Getters y Setters	
	public ConvenioVO getConvenio() {
		return convenio;
	}
	public void setConvenio(ConvenioVO convenio) {
		this.convenio = convenio;
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
	
	public void setIdProcuradorSesion(Long idProcuradorSesion) {
		this.idProcuradorSesion = idProcuradorSesion;
	}

	public Long getIdProcuradorSesion() {
		return idProcuradorSesion;
	}

	public List<ViaDeudaVO> getListViaDeuda() {
		return listViaDeuda;
	}
	public void setListViaDeuda(List<ViaDeudaVO> listViaDeuda) {
		this.listViaDeuda = listViaDeuda;
	}

	public PlanillaVO getReporteGenerado() {
		return reporteGenerado;
	}
	public void setReporteGenerado(PlanillaVO reporteGenerado) {
		this.reporteGenerado = reporteGenerado;
	}
	
	public void setListProcurador(List<ProcuradorVO> listProcurador) {
		this.listProcurador = listProcurador;
	}

	public List<ProcuradorVO> getListProcurador() {
		return listProcurador;
	}

	public List<ProcuradorReport> getListProcuradores() {
		return listProcuradorReport;
	}
	public void setListProcuradores(List<ProcuradorReport> listProcuradores) {
		this.listProcuradorReport = listProcuradores;
	}
	
	public List<PlanReport> getListPlanReport() {
		return listPlanReport;
	}
	public void setListPlanReport(List<PlanReport> listPlanReport) {
		this.listPlanReport = listPlanReport;
	}
	
	
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
	public void setVisualizarComboProcurador(boolean visualizarComboProcurador) {
		this.visualizarComboProcurador = visualizarComboProcurador;
	}

	public boolean isVisualizarComboProcurador() {
		return visualizarComboProcurador;
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

	// View getters
	// Metodos auxiliares utilizados para determinar si se selecciono:
	public boolean getViaDeudaSeleccionada(){
		return !ModelUtil.isNullOrEmpty(this.getConvenio().getViaDeuda());
	}
	
	public boolean getProcuradorSeleccionado(){
		return !ModelUtil.isNullOrEmpty(this.getConvenio().getProcurador());
	}

}
