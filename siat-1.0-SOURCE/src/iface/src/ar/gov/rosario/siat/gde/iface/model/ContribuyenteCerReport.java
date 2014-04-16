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
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.PlanillaVO;

/**
 * Clase Auxiliar para la Generacion del Reporte de Contribuyente Cer
 * 
 * @author Tecso
 *
 */
public class ContribuyenteCerReport extends SiatPageModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "contribuyenteCerReportVO";
	
	
	private RecursoVO recurso = new RecursoVO();
	private Date   fechaDesde;
	private Date   fechaHasta;
	private String fechaDesdeView = "";
	private String fechaHastaView = "";
	private Date   fechaReporte;
	private String fechaReporteView = "";
	private String periodoDesdeView = "";
	private String periodoHastaView = "";
	private String anioDesdeView = "";
	private String anioHastaView = "";


	private Integer periodoDesde;
	private Integer anioDesde;
	private Integer periodoHasta;
	private Integer anioHasta;
	private String userName;

	private Long totalCantCuentas;
	private Double totalImporteDeudas;
	
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	private List<RecClaDeuVO> listRecClaDeu = new ArrayList<RecClaDeuVO>();
	
	private PlanillaVO reporteGenerado = new PlanillaVO();
	
	
	private String desRunningRun = "";
	private String estRunningRun = "";
	private String desErrorRun = "";
	private String estErrorRun = "";

	//Flags
	private boolean procesando = false;
	private boolean existeReporteGenerado = false;
	private boolean error = false;

	//	 Constructores
	public ContribuyenteCerReport() {
		super(GdeSecurityConstants.CONTRIBUYENTECER_REPORT);
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
	public PlanillaVO getReporteGenerado() {
		return reporteGenerado;
	}
	public void setReporteGenerado(PlanillaVO reporteGenerado) {
		this.reporteGenerado = reporteGenerado;
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

	public RecursoVO getRecurso() {
		return recurso;
	}

	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
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

	public Date getFechaReporte() {
		return fechaReporte;
	}

	public void setFechaReporte(Date fechaReporte) {
		this.fechaReporte = fechaReporte;
		this.fechaReporteView = DateUtil.formatDate(fechaReporte, DateUtil.ddSMMSYYYY_MASK);
	}

	public String getFechaReporteView() {
		return fechaReporteView;
	}

	public void setFechaReporteView(String fechaReporteView) {
		this.fechaReporteView = fechaReporteView;
	}

	public Integer getPeriodoDesde() {
		return periodoDesde;
	}

	public void setPeriodoDesde(Integer periodoDesde) {
		this.periodoDesde = periodoDesde;
		this.periodoDesdeView = StringUtil.formatInteger(periodoDesde);
	}

	public Integer getAnioDesde() {
		return anioDesde;
	}

	public void setAnioDesde(Integer anioDesde) {
		this.anioDesde = anioDesde;
		this.anioDesdeView = StringUtil.formatInteger(anioDesde);
	}

	public Integer getPeriodoHasta() {
		return periodoHasta;
	}

	public void setPeriodoHasta(Integer periodoHasta) {
		this.periodoHasta = periodoHasta;
		this.periodoHastaView = StringUtil.formatInteger(periodoHasta);

	}

	public Integer getAnioHasta() {
		return anioHasta;
	}

	public void setAnioHasta(Integer anioHasta) {
		this.anioHasta = anioHasta;
		this.anioHastaView = StringUtil.formatInteger(anioHasta);
	}

	public String getPeriodoHastaView() {
		return periodoHastaView;
	}

	public void setPeriodoHastaView(String periodoHastaView) {
		this.periodoHastaView = periodoHastaView;
	}

	public String getPeriodoDesdeView() {
		return periodoDesdeView;
	}

	public void setPeriodoDesdeView(String periodoDesdeView) {
		this.periodoDesdeView = periodoDesdeView;
	}

	public String getAnioHastaView() {
		return anioHastaView;
	}

	public void setAnioHastaView(String anioHastaView) {
		this.anioHastaView = anioHastaView;
	}

	public String getAnioDesdeView() {
		return anioDesdeView;
	}

	public void setAnioDesdeView(String anioDesdeView) {
		this.anioDesdeView = anioDesdeView;
	}

	
}
