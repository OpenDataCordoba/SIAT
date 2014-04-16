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
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.PlanillaVO;

/**
 * Report de Recaudacion por Recurso
 * 
 * @author Tecso
 *
 */
public class GesJudReport extends SiatPageModel {

	public static final String SEPARADOR_CAMPOS_EVENTOS = ",";

	public static final String SEPARADOR_EVENTOS = "|";

	private static final long serialVersionUID = 1L;

	public static final String NAME = "gesJudReportVO";
	
	private GesJudVO gesJud = new GesJudVO();
	private CuentaVO cuenta = new CuentaVO();
	
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();	
	private List<ProcuradorVO> listProcurador = new ArrayList<ProcuradorVO>();
	private List<EventoVO> listEvento = new ArrayList<EventoVO>();
	private List<TipoJuzgadoVO> listTipoJuzgado = new ArrayList<TipoJuzgadoVO>();
	
	private String strEventosOpe = "";
	
	
	private Date   fechaDesde;
	private String fechaDesdeView;
	private Date   fechaHasta;
	private String fechaHastaView;
	
	private PlanillaVO reporteGenerado = new PlanillaVO();
	
	private String desRunningRun = "";
	private String estRunningRun = "";
	private String desErrorRun = "";
	private String estErrorRun = "";

	//Flags
	private boolean procesando = false;
	private boolean existeReporteGenerado = false;
	private boolean error = false;
	
	// Constructores
	public GesJudReport() {       
		super(GdeSecurityConstants.ABM_GEJUD_REPORT);        
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
	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}
	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}
	public PlanillaVO getReporteGenerado() {
		return reporteGenerado;
	}
	public void setReporteGenerado(PlanillaVO reporteGenerado) {
		this.reporteGenerado = reporteGenerado;
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

	public GesJudVO getGesJud() {
		return gesJud;
	}

	public void setGesJud(GesJudVO gesJud) {
		this.gesJud = gesJud;
	}

	public CuentaVO getCuenta() {
		return cuenta;
	}

	public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}

	public List<ProcuradorVO> getListProcurador() {
		return listProcurador;
	}

	public void setListProcurador(List<ProcuradorVO> listProcurador) {
		this.listProcurador = listProcurador;
	}

	public List<EventoVO> getListEvento() {
		return listEvento;
	}

	public void setListEvento(List<EventoVO> listEvento) {
		this.listEvento = listEvento;
	}

	public String getStrEventosOpe() {
		return strEventosOpe;
	}

	public void setStrEventosOpe(String strEventosOpe) {
		this.strEventosOpe = strEventosOpe;
	}

	public List<TipoJuzgadoVO> getListTipoJuzgado() {
		return listTipoJuzgado;
	}

	public void setListTipoJuzgado(List<TipoJuzgadoVO> listTipoJuzgadoVO) {
		this.listTipoJuzgado = listTipoJuzgadoVO;
	}

	/**
	 * Agrega un evento al string strEvento con el formato "idEvento,valorOperacion,valorTiempo,valorUnidad"<br>
	 * Los distintos eventos estan separador por "|"
	 * @param idEvento
	 * @param valorOperacion
	 * @param valorTiempo
	 * @param valorUnidad
	 */
	public void addEventoOpe(String idEvento, String valorOperacion, String operacionTiempo, String valorTiempo, String valorUnidad){
		if(strEventosOpe.length()>0)
			strEventosOpe +=SEPARADOR_EVENTOS;
		strEventosOpe +=idEvento+SEPARADOR_CAMPOS_EVENTOS+valorOperacion+SEPARADOR_CAMPOS_EVENTOS+
					operacionTiempo+SEPARADOR_CAMPOS_EVENTOS+valorTiempo+SEPARADOR_CAMPOS_EVENTOS+valorUnidad;			
	}
	//	 View getters
	
}
