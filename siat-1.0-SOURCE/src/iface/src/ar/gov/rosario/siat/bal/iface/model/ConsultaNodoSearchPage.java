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
import coop.tecso.demoda.iface.model.CeldaVO;
import coop.tecso.demoda.iface.model.PlanillaVO;
import coop.tecso.demoda.iface.model.RangoFechaVO;

/**
 * SearchPage de Consulta de Total para Nodos
 * 
 * @author Tecso
 *
 */
public class ConsultaNodoSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "consultaNodoSearchPageVO";

	private List<PlanillaVO> listReporteGenerado = new ArrayList<PlanillaVO>();
	private String tituloReporte = "";

	private Date fechaDesde;
	private Date fechaHasta;
	private ClasificadorVO clasificador = new ClasificadorVO();
	private Integer nivel = -1;
	private Integer cantRangos = 0;
	private Integer nivelHasta = -1;
	
	private NodoVO nodo = new NodoVO();

	private String fechaDesdeView = "";
	private String fechaHastaView = "";
	private String nivelView = "";
	private String cantRangosView = "";
	private String msgValidacion = "";
	private String nivelHastaView = "";

	private List<ClasificadorVO> listClasificador = new ArrayList<ClasificadorVO>();
	private List<IntegerVO> listNivel = new ArrayList<IntegerVO>();
	private List<CeldaVO> listCodNivel = new ArrayList<CeldaVO>();
	private List<RangoFechaVO> listRangosFecha = new ArrayList<RangoFechaVO>();
	private List<IntegerVO> listRangos = new ArrayList<IntegerVO>();
	private List<IntegerVO> listNivelHasta = new ArrayList<IntegerVO>();
	
	private String desRunningRun = "";
	private String estRunningRun = "";
	private String desErrorRun = "";
	private String estErrorRun = "";

	//Flags
	private boolean procesando = false;
	private boolean existeReporteGenerado = false;
	private boolean error = false;
	
	private boolean esNodoValido = false; 
	
	private boolean rangosFechaExtras = false;
	private boolean reporteEspecial = false;
	
	// Constructores
	public ConsultaNodoSearchPage() {       
       super(BalSecurityConstants.CONSULTAR_TOTAL_NODO);
    }

	// Getters y Setters
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
	public List<IntegerVO> getListNivel() {
		return listNivel;
	}
	public void setListNivel(List<IntegerVO> listNivel) {
		this.listNivel = listNivel;
	}
	public boolean isEsNodoValido() {
		return esNodoValido;
	}
	public void setEsNodoValido(boolean esNodoValido) {
		this.esNodoValido = esNodoValido;
	}
	public String getMsgValidacion() {
		return msgValidacion;
	}
	public void setMsgValidacion(String msgValidacion) {
		this.msgValidacion = msgValidacion;
	}
	public List<CeldaVO> getListCodNivel() {
		return listCodNivel;
	}
	public void setListCodNivel(List<CeldaVO> listCodNivel) {
		this.listCodNivel = listCodNivel;
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
	public List<PlanillaVO> getListReporteGenerado() {
		return listReporteGenerado;
	}
	public void setListReporteGenerado(List<PlanillaVO> listReporteGenerado) {
		this.listReporteGenerado = listReporteGenerado;
	}
	public boolean isProcesando() {
		return procesando;
	}
	public void setProcesando(boolean procesando) {
		this.procesando = procesando;
	}
	public String getTituloReporte() {
		return tituloReporte;
	}
	public void setTituloReporte(String tituloReporte) {
		this.tituloReporte = tituloReporte;
	}
	public List<RangoFechaVO> getListRangosFecha() {
		return listRangosFecha;
	}
	public void setListRangosFecha(List<RangoFechaVO> listRangosFecha) {
		this.listRangosFecha = listRangosFecha;
	}
	public boolean isRangosFechaExtras() {
		return rangosFechaExtras;
	}
	public void setRangosFechaExtras(boolean rangosFechaExtras) {
		this.rangosFechaExtras = rangosFechaExtras;
	}
	public Integer getCantRangos() {
		return cantRangos;
	}
	public void setCantRangos(Integer cantRangos) {
		this.cantRangos = cantRangos;
	}
	public String getCantRangosView() {
		return cantRangosView;
	}
	public void setCantRangosView(String cantRangosView) {
		this.cantRangosView = cantRangosView;
	}
	public List<IntegerVO> getListRangos() {
		return listRangos;
	}
	public void setListRangos(List<IntegerVO> listRangos) {
		this.listRangos = listRangos;
	}
	public boolean isReporteEspecial() {
		return reporteEspecial;
	}
	public void setReporteEspecial(boolean reporteEspecial) {
		this.reporteEspecial = reporteEspecial;
	}
	public List<IntegerVO> getListNivelHasta() {
		return listNivelHasta;
	}
	public void setListNivelHasta(List<IntegerVO> listNivelHasta) {
		this.listNivelHasta = listNivelHasta;
	}
	public Integer getNivelHasta() {
		return nivelHasta;
	}
	public void setNivelHasta(Integer nivelHasta) {
		this.nivelHasta = nivelHasta;
		this.nivelHastaView = StringUtil.formatInteger(nivelHasta);
	}
	public String getNivelHastaView() {
		return nivelHastaView;
	}
	public void setNivelHastaView(String nivelHastaView) {
		this.nivelHastaView = nivelHastaView;
	}

	public String getName(){
		return NAME;
	}
	
}
