//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.PlanillaVO;

public class ClasificadorReport extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "clasificadorReportVO";
	
	private List<PlanillaVO> listReporteGenerado = new ArrayList<PlanillaVO>();
	private String tituloReporte = "";
	
	private List<EjercicioVO> listEjercicio = new ArrayList<EjercicioVO>();
	private EjercicioVO ejercicio = new EjercicioVO();

	private List<ClasificadorVO> listClasificador = new ArrayList<ClasificadorVO>();
	private List<NodoVO> listNodo = new ArrayList<NodoVO>();
	
	private List<IntegerVO> listNivel = new ArrayList<IntegerVO>();
	
	private Date fechaDesde;
	private Date fechaHasta;
	private ClasificadorVO clasificador = new ClasificadorVO();
	private Integer nivel = -1;
	private NodoVO nodo = new NodoVO();
	private Long nroBalance;

	private String fechaDesdeView = "";
	private String fechaHastaView = "";
	private String nivelView = "";
	private String nroBalanceView = "";

	private String desRunningRun = "";
	private String estRunningRun = "";
	private String desErrorRun = "";
	private String estErrorRun = "";

	//Flags
	private boolean paramEjercicio = false;
	
	private boolean procesando = false;
	private boolean existeReporteGenerado = false;
	private boolean error = false;
	private boolean reporteExtra = false;
	
	// Constructores
	public ClasificadorReport() {       
       super(BalSecurityConstants.CONSULTAR_CLASIFICADOR);        
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
	public EjercicioVO getEjercicio() {
		return ejercicio;
	}
	public void setEjercicio(EjercicioVO ejercicio) {
		this.ejercicio = ejercicio;
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
	public List<EjercicioVO> getListEjercicio() {
		return listEjercicio;
	}
	public void setListEjercicio(List<EjercicioVO> listEjercicio) {
		this.listEjercicio = listEjercicio;
	}
	public boolean isProcesando() {
		return procesando;
	}
	public void setProcesando(boolean procesando) {
		this.procesando = procesando;
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
	public boolean isParamEjercicio() {
		return paramEjercicio;
	}
	public void setParamEjercicio(boolean paramEjercicio) {
		this.paramEjercicio = paramEjercicio;
	}
	public List<PlanillaVO> getListReporteGenerado() {
		return listReporteGenerado;
	}
	public void setListReporteGenerado(List<PlanillaVO> listReporteGenerado) {
		this.listReporteGenerado = listReporteGenerado;
	}
	public String getTituloReporte() {
		return tituloReporte;
	}
	public void setTituloReporte(String tituloReporte) {
		this.tituloReporte = tituloReporte;
	}
	public ClasificadorVO getClasificador() {
		return clasificador;
	}
	public void setClasificador(ClasificadorVO clasificador) {
		this.clasificador = clasificador;
	}
	public Integer getNivel() {
		return nivel;
	}
	public void setNivel(Integer nivel) {
		this.nivel = nivel;
		this.nivelView = StringUtil.formatInteger(nivel);
	}
	public String getNivelView() {
		return nivelView;
	}
	public void setNivelView(String nivelView) {
		this.nivelView = nivelView;
	}
	public NodoVO getNodo() {
		return nodo;
	}
	public void setNodo(NodoVO nodo) {
		this.nodo = nodo;
	}
	public List<ClasificadorVO> getListClasificador() {
		return listClasificador;
	}
	public void setListClasificador(List<ClasificadorVO> listClasificador) {
		this.listClasificador = listClasificador;
	}
	public List<NodoVO> getListNodo() {
		return listNodo;
	}
	public void setListNodo(List<NodoVO> listNodo) {
		this.listNodo = listNodo;
	}
	public List<IntegerVO> getListNivel() {
		return listNivel;
	}
	public void setListNivel(List<IntegerVO> listNivel) {
		this.listNivel = listNivel;
	}
	public boolean isReporteExtra() {
		return reporteExtra;
	}
	public void setReporteExtra(boolean reporteExtra) {
		this.reporteExtra = reporteExtra;
	}
	public Long getNroBalance() {
		return nroBalance;
	}
	public void setNroBalance(Long nroBalance) {
		this.nroBalance = nroBalance;
		this.nroBalanceView = StringUtil.formatLong(nroBalance);
	}
	public String getNroBalanceView() {
		return nroBalanceView;
	}
	public void setNroBalanceView(String nroBalanceView) {
		this.nroBalanceView = nroBalanceView;
	}

}
