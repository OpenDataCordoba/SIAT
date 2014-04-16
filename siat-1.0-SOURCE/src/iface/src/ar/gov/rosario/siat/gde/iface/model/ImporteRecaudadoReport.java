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
import coop.tecso.demoda.iface.model.PlanillaVO;

/**
 * SearchPage del ImporteRecaudado
 * 
 * @author Tecso
 *
 */
public class ImporteRecaudadoReport extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "importeRecaudadoReportVO";
	
	private PlanVO plan = new PlanVO();
	
	private Integer tipoReporte=0;
	
	private String viaDeudaEnabled="true";
	
	private Date fechaPagoDesde;
	
	private Date fechaPagoHasta;
	
	private String fechaPagoDesdeView = "";
	private String fechaPagoHastaView = "";

	private Long idProcuradorSesion = null; // id del procurador de la sesion
	
	private List<ProcuradorVO> listProcurador = new ArrayList<ProcuradorVO>();
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	private List<PlanVO> listPlan = new ArrayList<PlanVO>();
	private List<ViaDeudaVO> listViaDeuda = new ArrayList<ViaDeudaVO>();

	private PlanillaVO reporteGenerado = new PlanillaVO();
	
	private String desRunningRun = "";
	private String estRunningRun = "";
	private String desErrorRun = "";
	private String estErrorRun = "";
	private String msgReporteExcesivo="";
	
	//Flags
	private boolean procesando = false;
	private boolean existeReporteGenerado = false;
	private boolean error = false;
	// bandera utilizada por el jsp para visualizar o no el combo procurador
	private boolean visualizarComboProcurador = false;


	
	// Constructores
	public ImporteRecaudadoReport() {       
       super(GdeSecurityConstants.CONSULTAR_IMPORTE_RECAUDADO_PLANES);        
    }
	
	// Getters y Setters

	public PlanVO getPlan() {
		return plan;
	}

	public void setPlan(PlanVO plan) {
		this.plan = plan;
	}

	public Date getFechaPagoDesde() {
		return fechaPagoDesde;
	}

	public void setFechaPagoDesde(Date fechaPagoDesde) {
		this.fechaPagoDesde = fechaPagoDesde;
	}

	public Date getFechaPagoHasta() {
		return fechaPagoHasta;
	}

	public void setFechaPagoHasta(Date fechaPagoHasta) {
		this.fechaPagoHasta = fechaPagoHasta;
	}

	public String getFechaPagoDesdeView() {
		return fechaPagoDesdeView;
	}

	public void setFechaPagoDesdeView(String fechaPagoDesdeView) {
		this.fechaPagoDesdeView = fechaPagoDesdeView;
	}

	public String getFechaPagoHastaView() {
		return fechaPagoHastaView;
	}

	public void setFechaPagoHastaView(String fechaPagoHastaView) {
		this.fechaPagoHastaView = fechaPagoHastaView;
	}

	public Long getIdProcuradorSesion() {
		return idProcuradorSesion;
	}

	public void setIdProcuradorSesion(Long idProcuradorSesion) {
		this.idProcuradorSesion = idProcuradorSesion;
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

	public List<PlanVO> getListPlan() {
		return listPlan;
	}

	public void setListPlan(List<PlanVO> listPlan) {
		this.listPlan = listPlan;
	}

	public Integer getTipoReporte() {
		return tipoReporte;
	}

	public void setTipoReporte(Integer tipoReporte) {
		this.tipoReporte = tipoReporte;
	}

	public List<ViaDeudaVO> getListViaDeuda() {
		return listViaDeuda;
	}

	public void setListViaDeuda(List<ViaDeudaVO> listViaDeuda) {
		this.listViaDeuda = listViaDeuda;
	}

	public String getViaDeudaEnabled() {
		return viaDeudaEnabled;
	}

	public void setViaDeudaEnabled(String viaDeudaEnabled) {
		this.viaDeudaEnabled = viaDeudaEnabled;
	}

	public PlanillaVO getReporteGenerado() {
		return reporteGenerado;
	}

	public void setReporteGenerado(PlanillaVO reporteGenerado) {
		this.reporteGenerado = reporteGenerado;
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

	public void setVisualizarComboProcurador(boolean visualizarComboProcurador) {
		this.visualizarComboProcurador = visualizarComboProcurador;
	}

	public boolean isVisualizarComboProcurador() {
		return visualizarComboProcurador;
	}

	public void setMsgReporteExcesivo(String s) {
		this.msgReporteExcesivo = s;
	}
	
	public String getMsgReporteExcesivo(String s) {
		return this.msgReporteExcesivo;
	}
	
	// View getters
}
