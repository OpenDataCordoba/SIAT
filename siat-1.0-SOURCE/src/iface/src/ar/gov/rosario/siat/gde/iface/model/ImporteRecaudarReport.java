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
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.PlanillaVO;

/**
 * SearchPage del ImporteRecaudar
 * 
 * @author Tecso
 *
 */
public class ImporteRecaudarReport extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "importeRecaudarReportVO";
	
	private PlanVO plan = new PlanVO();
	
	private Date fechaVencimientoDesde;
	
	private Date fechaVencimientoHasta;
	
	private String fechaVencimientoDesdeView = "";
	private String fechaVencimientoHastaView = "";

	private Long idProcuradorSesion = null; // id del procurador de la sesion
	
	private List<ProcuradorVO> listProcurador = new ArrayList<ProcuradorVO>();
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	private List<PlanVO> listPlan = new ArrayList<PlanVO>();

	
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
	public ImporteRecaudarReport() {       
       super(GdeSecurityConstants.CONSULTAR_IMPORTE_RECAUDAR);        
    }

	// Getters y Setters
	public PlanVO getPlan() {
		return plan;
	}
	public void setPlan(PlanVO plan) {
		this.plan = plan;
	}

	public Date getFechaVencimientoDesde() {
		return fechaVencimientoDesde;
	}
	public void setFechaVencimientoDesde(Date fechaVencimientoDesde) {
		this.fechaVencimientoDesde = fechaVencimientoDesde;
		this.fechaVencimientoDesdeView = DateUtil.formatDate(fechaVencimientoDesde, DateUtil.ddSMMSYYYY_MASK);
	}

	public Date getFechaVencimientoHasta() {
		return fechaVencimientoHasta;
	}
	public void setFechaVencimientoHasta(Date fechaVencimientoHasta) {
		this.fechaVencimientoHasta = fechaVencimientoHasta;
		this.fechaVencimientoHastaView = DateUtil.formatDate(fechaVencimientoHasta, DateUtil.ddSMMSYYYY_MASK);
	}
	
	public void setIdProcuradorSesion(Long idProcuradorSesion) {
		this.idProcuradorSesion = idProcuradorSesion;
	}

	public Long getIdProcuradorSesion() {
		return idProcuradorSesion;
	}

	public void setListProcurador(List<ProcuradorVO> listProcurador) {
		this.listProcurador = listProcurador;
	}

	public List<ProcuradorVO> getListProcurador() {
		return listProcurador;
	}

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}
	public void setListRecurso(List<RecursoVO> listRecursoVO) {
		this.listRecurso = listRecursoVO;
	}

	public List<PlanVO> getListPlan() {
		return listPlan;
	}
	public void setListPlan(List<PlanVO> listPlan) {
		this.listPlan = listPlan;
	}
	
	public PlanillaVO getReporteGenerado() {
		return reporteGenerado;
	}
	public void setReporteGenerado(PlanillaVO reporteGenerado) {
		this.reporteGenerado = reporteGenerado;
	}

	public CantCuoVO getCantCuo(long idProcurador, Integer mes, Integer anio,
			Integer nroCuota) {
		CantCuoVO cantCuo = new CantCuoVO();
		cantCuo.getProcurador().setId(idProcurador);
		cantCuo.setMes(mes);
		cantCuo.setAnio(anio);
		cantCuo.setNroCuota(nroCuota);
		int index = ((ArrayList<ProcuradorPlanVO>) getListResult()).indexOf(cantCuo);
		if(index>0)
			return ((ArrayList<CantCuoVO>) getListResult()).get(index);
		return null;
	}

	public void prepararParaPDF(){
		Long idRecurso = plan.getRecurso().getId();
		RecursoVO recurso = new RecursoVO();
		recurso.setId(idRecurso);
		recurso.setCategoria(null);
		recurso.setTipObjImp(null);
		recurso.setRecPri(null);
		recurso.setCriAsiPro(null);
		recurso.setGenCue(null);
		recurso.setGenCodGes(null);
		recurso.setAtributoAse(null);
		recurso.setListRecAtrCue(null);
		recurso.setListRecAtrCueEmi(null);
		recurso.setListRecAtrVal(null);
		recurso.setListRecClaDeu(null);
		recurso.setListRecCon(null);
		recurso.setListRecEmi(null);
		recurso.setListRecGenCueAtrVa(null);
		
		plan.setRecurso(recurso);
		plan.setListPlanAtrVal(null);
		plan.setListPlanClaDeu(null);
		plan.setListPlanDescuento(null);
		plan.setListPlanExe(null);
		plan.setListPlanForActDeu(null);
		plan.setListPlanImpMin(null);
		plan.setListPlanIntFin(null);
		plan.setListPlanMotCad(null);
		plan.setListPlanProrroga(null);
		plan.setListPlanVen(null);
		
		setListRecurso(null);
		setListPlan(null);
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
	// View getters
	public void setFechaVencimientoDesdeView(String fechaVencimientoDesdeView) {
		this.fechaVencimientoDesdeView = fechaVencimientoDesdeView;
	}
	public String getFechaVencimientoDesdeView() {
		return fechaVencimientoDesdeView;
	}

	public void setFechaVencimientoHastaView(String fechaVencimientoHastaView) {
		this.fechaVencimientoHastaView = fechaVencimientoHastaView;
	}
	public String getFechaVencimientoHastaView() {
		return fechaVencimientoHastaView;
	}




}
