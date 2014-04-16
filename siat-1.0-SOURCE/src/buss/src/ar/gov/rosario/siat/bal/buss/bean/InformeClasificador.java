//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.model.NodoVO;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

public class InformeClasificador extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	private String fechaDesde = null;
	private String fechaHasta = null;
	private String fechaReporte = null;
	
	private String desEjercicio = "";
	private String desClasificador = "";
	private String nroBalance = ""; 
	
	private String reporteExtra = "false";
	private String nivelFiltro = "";
	private String desNodoFiltro = ""; 
	
	private String reporteProcesoBalance = "false";
	
	private String clasificadorRubro = "true";
	private String mostrarColumnas = "false";
	
	private List<NodoVO> listNodo = new ArrayList<NodoVO>();
	
	String desTipoProceso = null;
	String desCorrida = null;
	
	private String totalGeneral = "";
	private String subTotal = "";
	
	private String mostrarSubTotal = "false";
	
	// Getters y Setters
	public String getDesCorrida() {
		return desCorrida;
	}
	public void setDesCorrida(String desCorrida) {
		this.desCorrida = desCorrida;
	}
	public String getDesTipoProceso() {
		return desTipoProceso;
	}
	public void setDesTipoProceso(String desTipoProceso) {
		this.desTipoProceso = desTipoProceso;
	}
	public String getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(String fechaDesde) {
		this.fechaDesde = fechaDesde;
	}
	public String getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(String fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
	public String getFechaReporte() {
		return fechaReporte;
	}
	public void setFechaReporte(String fechaReporte) {
		this.fechaReporte = fechaReporte;
	}
	public String getDesEjercicio() {
		return desEjercicio;
	}
	public void setDesEjercicio(String desEjercicio) {
		this.desEjercicio = desEjercicio;
	}
	public String getDesClasificador() {
		return desClasificador;
	}
	public void setDesClasificador(String desClasificador) {
		this.desClasificador = desClasificador;
	}
	public List<NodoVO> getListNodo() {
		return listNodo;
	}
	public void setListNodo(List<NodoVO> listNodo) {
		this.listNodo = listNodo;
	}
	public String getDesNodoFiltro() {
		return desNodoFiltro;
	}
	public void setDesNodoFiltro(String desNodoFiltro) {
		this.desNodoFiltro = desNodoFiltro;
	}
	public String getNivelFiltro() {
		return nivelFiltro;
	}
	public void setNivelFiltro(String nivelFiltro) {
		this.nivelFiltro = nivelFiltro;
	}
	public String getReporteExtra() {
		return reporteExtra;
	}
	public void setReporteExtra(String reporteExtra) {
		this.reporteExtra = reporteExtra;
	}
	public String getReporteProcesoBalance() {
		return reporteProcesoBalance;
	}
	public void setReporteProcesoBalance(String reporteProcesoBalance) {
		this.reporteProcesoBalance = reporteProcesoBalance;
	}
	public String getClasificadorRubro() {
		return clasificadorRubro;
	}
	public void setClasificadorRubro(String clasificadorRubro) {
		this.clasificadorRubro = clasificadorRubro;
	}
	public String getMostrarColumnas() {
		return mostrarColumnas;
	}
	public void setMostrarColumnas(String mostrarColumnas) {
		this.mostrarColumnas = mostrarColumnas;
	}
	public String getTotalGeneral() {
		return totalGeneral;
	}
	public void setTotalGeneral(String totalGeneral) {
		this.totalGeneral = totalGeneral;
	}
	public String getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(String subTotal) {
		this.subTotal = subTotal;
	}
	public String getMostrarSubTotal() {
		return mostrarSubTotal;
	}
	public void setMostrarSubTotal(String mostrarSubTotal) {
		this.mostrarSubTotal = mostrarSubTotal;
	}
	public String getNroBalance() {
		return nroBalance;
	}
	public void setNroBalance(String nroBalance) {
		this.nroBalance = nroBalance;
	}
	
	
}
