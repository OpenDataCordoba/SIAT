//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.RecClaDeuVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.PlanillaVO;

/**
 * Clase Auxiliar para la Generacion del Reporte de Emision
 * 
 * @author Tecso
 *
 */
public class RecaudadoReport extends SiatPageModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "recaudadoReportVO";
	
	private RecursoVO recurso = new RecursoVO();
	private ProcuradorVO procurador = new ProcuradorVO();
	
	private Date   fechaDesde;
	private Date   fechaHasta;
	private Date   fechaPagoHasta;
	
	private String fechaDesdeView = "";
	private String fechaHastaView = "";
	private String fechaHastaPagoView = "";
	private String userName;

	private Long totalCantCuentas;
	private Double totalImporteDeudas;
	
	private List<ProcuradorVO> listProcurador = new ArrayList<ProcuradorVO>();
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	private List<RecClaDeuVO> listRecClaDeu = new ArrayList<RecClaDeuVO>();
	
	private PlanillaVO reporteGenerado = new PlanillaVO();
	private Long idProcuradorSesion = null; // id del procurador de la sesion

	
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

	
	//	 Constructores
	public RecaudadoReport() {
		super(GdeSecurityConstants.RECAUDADO_REPORT);
	}

	// Getters y Setters
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
	public Date getFechaPagoHasta() {
		return fechaPagoHasta;
	}
	public void setFechaPagoHasta(Date fechaPagoHasta) {
		this.fechaPagoHasta = fechaPagoHasta;
		this.setFechaHastaPagoView(DateUtil.formatDate(fechaPagoHasta, DateUtil.ddSMMSYYYY_MASK));
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
	public void setFechaHastaPagoView(String fechaPagoHastaView) {
		this.fechaHastaPagoView = fechaPagoHastaView;
	}
	public String getFechaHastaPagoView() {
		return fechaHastaPagoView;
	}
	public PlanillaVO getReporteGenerado() {
		return reporteGenerado;
	}
	public void setReporteGenerado(PlanillaVO reporteGenerado) {
		this.reporteGenerado = reporteGenerado;
	}

	public void setIdProcuradorSesion(Long idProcuradorSesion) {
		this.idProcuradorSesion = idProcuradorSesion;
	}

	public Long getIdProcuradorSesion() {
		return idProcuradorSesion;
	}

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getTotalCantCuentas() {
		return totalCantCuentas;
	}

	public void setTotalCantCuentas(Long totalCantCuentas) {
		this.totalCantCuentas = totalCantCuentas;
	}

	public Double getTotalImporteDeudas() {
		return totalImporteDeudas;
	}

	public void setTotalImporteDeudas(Double totalImporteDeudas) {
		this.totalImporteDeudas = totalImporteDeudas;
	}

	public void setListProcurador(List<ProcuradorVO> listProcurador) {
		this.listProcurador = listProcurador;
	}

	public List<ProcuradorVO> getListProcurador() {
		return listProcurador;
	}

	public RecursoVO getRecurso() {
		return recurso;
	}

	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}

	public void setProcurador(ProcuradorVO procurador) {
		this.procurador = procurador;
	}

	public ProcuradorVO getProcurador() {
		return procurador;
	}

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecursoVO) {
		this.listRecurso = listRecursoVO;
	}

	public List<RecursoVO> getListRecursoVO() {
		return listRecurso;
	}

	public void setListRecursoVO(List<RecursoVO> listRecursoVO) {
		this.listRecurso = listRecursoVO;
	}
	
	public List<RecClaDeuVO> getListRecClaDeu() {
		return listRecClaDeu;
	}

	public void setListRecClaDeu(List<RecClaDeuVO> listRecClaDeuVO) {
		this.listRecClaDeu = listRecClaDeuVO;
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

	public String getDesErrorRun() {
		return desErrorRun;
	}

	public void setDesErrorRun(String desErrorRun) {
		this.desErrorRun = desErrorRun;
	}

	public String getEstErrorRun() {
		return estErrorRun;
	}

	public void setEstErrorRun(String estErrorRun) {
		this.estErrorRun = estErrorRun;
	}

	public boolean isProcesando() {
		return procesando;
	}

	public void setProcesando(boolean procesando) {
		this.procesando = procesando;
	}

	public boolean isExisteReporteGenerado() {
		return existeReporteGenerado;
	}

	public void setExisteReporteGenerado(boolean existeReporteGenerado) {
		this.existeReporteGenerado = existeReporteGenerado;
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

	
}
