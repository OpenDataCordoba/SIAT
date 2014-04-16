//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.pro.iface.model.CorridaVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

public class BalanceVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;
	
	private Date fechaBalance;
	private EjercicioVO ejercicio = new EjercicioVO();
	private Date fechaDesde;
	private Date fechaHasta;
	private String observacion = "";
	private CorridaVO corrida = new CorridaVO();
	private Date fechaAlta;
	private Integer enviado;
	
	private List<FolioVO> listFolio = new ArrayList<FolioVO>();
	private List<ArchivoVO> listArchivo = new ArrayList<ArchivoVO>();
	private List<Caja7VO> listCaja7 = new ArrayList<Caja7VO>();
	private List<Caja69VO> listCaja69 = new ArrayList<Caja69VO>();
	private List<TranBalVO> listTranBal = new ArrayList<TranBalVO>();
	private List<ReingresoVO> listReingreso = new ArrayList<ReingresoVO>();
	private List<AsentamientoVO> listAsentamiento = new ArrayList<AsentamientoVO>();
	private List<AseDelVO> listAseDel = new ArrayList<AseDelVO>();
	private List<CompensacionVO> listCompensacion = new ArrayList<CompensacionVO>();
	
	private String fechaBalanceView = "";
	private String fechaDesdeView = "";
	private String fechaHastaView = "";
	private String fechaAltaView = "";
	private String enviadoView;
	
	private String idCorridaView = "";

	private Boolean admProcesoBussEnabled      = true;

	// Constructores 
	public BalanceVO(){
		super();
	}

	// Getters y Setters
	public CorridaVO getCorrida() {
		return corrida;
	}
	public void setCorrida(CorridaVO corrida) {
		this.corrida = corrida;
	}
	public EjercicioVO getEjercicio() {
		return ejercicio;
	}
	public void setEjercicio(EjercicioVO ejercicio) {
		this.ejercicio = ejercicio;
	}
	public Integer getEnviado() {
		return enviado;
	}
	public void setEnviado(Integer enviado) {
		this.enviado = enviado;
		this.enviadoView = StringUtil.formatInteger(enviado);
	}
	public String getEnviadoView() {
		return enviadoView;
	}
	public void setEnviadoView(String enviadoView) {
		this.enviadoView = enviadoView;
	}
	public Date getFechaAlta() {
		return fechaAlta;
	}
	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
		this.fechaAltaView = DateUtil.formatDate(fechaAlta, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaAltaView() {
		return fechaAltaView;
	}
	public void setFechaAltaView(String fechaAltaView) {
		this.fechaAltaView = fechaAltaView;
	}
	public Date getFechaBalance() {
		return fechaBalance;
	}
	public void setFechaBalance(Date fechaBalance) {
		this.fechaBalance = fechaBalance;
		this.fechaBalanceView = DateUtil.formatDate(fechaBalance, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaBalanceView() {
		return fechaBalanceView;
	}
	public void setFechaBalanceView(String fechaBalanceView) {
		this.fechaBalanceView = fechaBalanceView;
	}
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
	public String getIdCorridaView() {
		return idCorridaView;
	}
	public void setIdCorridaView(String idCorridaView) {
		this.idCorridaView = idCorridaView;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public List<FolioVO> getListFolio() {
		return listFolio;
	}
	public void setListFolio(List<FolioVO> listFolio) {
		this.listFolio = listFolio;
	}
	public List<ArchivoVO> getListArchivo() {
		return listArchivo;
	}
	public void setListArchivo(List<ArchivoVO> listArchivo) {
		this.listArchivo = listArchivo;
	}
	public List<Caja7VO> getListCaja7() {
		return listCaja7;
	}
	public void setListCaja7(List<Caja7VO> listCaja7) {
		this.listCaja7 = listCaja7;
	}
	public List<ReingresoVO> getListReingreso() {
		return listReingreso;
	}
	public void setListReingreso(List<ReingresoVO> listReingreso) {
		this.listReingreso = listReingreso;
	}
	public List<Caja69VO> getListCaja69() {
		return listCaja69;
	}
	public void setListCaja69(List<Caja69VO> listCaja69) {
		this.listCaja69 = listCaja69;
	}
	public List<TranBalVO> getListTranBal() {
		return listTranBal;
	}
	public void setListTranBal(List<TranBalVO> listTranBal) {
		this.listTranBal = listTranBal;
	}
	public List<AseDelVO> getListAseDel() {
		return listAseDel;
	}
	public void setListAseDel(List<AseDelVO> listAseDel) {
		this.listAseDel = listAseDel;
	}
	public List<AsentamientoVO> getListAsentamiento() {
		return listAsentamiento;
	}
	public void setListAsentamiento(List<AsentamientoVO> listAsentamiento) {
		this.listAsentamiento = listAsentamiento;
	}
	public List<CompensacionVO> getListCompensacion() {
		return listCompensacion;
	}
	public void setListCompensacion(List<CompensacionVO> listCompensacion) {
		this.listCompensacion = listCompensacion;
	}

	//	 Buss flags getters y setters
	public Boolean getAdmProcesoBussEnabled() {
		return admProcesoBussEnabled;
	}
	public void setAdmProcesoBussEnabled(Boolean admProcesoBussEnabled) {
		this.admProcesoBussEnabled = admProcesoBussEnabled;
	}
	public String getAdmProcesoEnabled() {
		return this.getAdmProcesoBussEnabled() ? ENABLED : DISABLED;
	}
	
}
