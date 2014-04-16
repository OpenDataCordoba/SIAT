//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.model.NodoVO;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

public class InformeClaCom extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	private String priFechaDesde = null;
	private String priFechaHasta = null;
	private String segFechaDesde = null;
	private String segFechaHasta = null;
	private String fechaReporte = null;
	
	private String desClasificador = ""; 
	
	private String nivelFiltro = "";
 	
	private String clasificadorRubro = "true";
	
	private List<NodoVO> listNodo = new ArrayList<NodoVO>();
	
	String desTipoProceso = null;
	String desCorrida = null;
	
	private String totalGeneralPri = "";
	private String subTotalPri = "";
	private String totalGeneralSeg = "";
	private String subTotalSeg = "";
	private String variacionTotal = "";
	private String variacionSubTotal = "";
	
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
	public String getPriFechaDesde() {
		return priFechaDesde;
	}
	public void setPriFechaDesde(String priFechaDesde) {
		this.priFechaDesde = priFechaDesde;
	}
	public String getPriFechaHasta() {
		return priFechaHasta;
	}
	public void setPriFechaHasta(String priFechaHasta) {
		this.priFechaHasta = priFechaHasta;
	}
	public String getSegFechaDesde() {
		return segFechaDesde;
	}
	public void setSegFechaDesde(String segFechaDesde) {
		this.segFechaDesde = segFechaDesde;
	}
	public String getSegFechaHasta() {
		return segFechaHasta;
	}
	public void setSegFechaHasta(String segFechaHasta) {
		this.segFechaHasta = segFechaHasta;
	}
	public String getSubTotalPri() {
		return subTotalPri;
	}
	public void setSubTotalPri(String subTotalPri) {
		this.subTotalPri = subTotalPri;
	}
	public String getSubTotalSeg() {
		return subTotalSeg;
	}
	public void setSubTotalSeg(String subTotalSeg) {
		this.subTotalSeg = subTotalSeg;
	}
	public String getTotalGeneralPri() {
		return totalGeneralPri;
	}
	public void setTotalGeneralPri(String totalGeneralPri) {
		this.totalGeneralPri = totalGeneralPri;
	}
	public String getTotalGeneralSeg() {
		return totalGeneralSeg;
	}
	public void setTotalGeneralSeg(String totalGeneralSeg) {
		this.totalGeneralSeg = totalGeneralSeg;
	}
	public String getFechaReporte() {
		return fechaReporte;
	}
	public void setFechaReporte(String fechaReporte) {
		this.fechaReporte = fechaReporte;
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
	public String getNivelFiltro() {
		return nivelFiltro;
	}
	public void setNivelFiltro(String nivelFiltro) {
		this.nivelFiltro = nivelFiltro;
	}
	public String getClasificadorRubro() {
		return clasificadorRubro;
	}
	public void setClasificadorRubro(String clasificadorRubro) {
		this.clasificadorRubro = clasificadorRubro;
	}
	public String getMostrarSubTotal() {
		return mostrarSubTotal;
	}
	public void setMostrarSubTotal(String mostrarSubTotal) {
		this.mostrarSubTotal = mostrarSubTotal;
	}
	public String getVariacionSubTotal() {
		return variacionSubTotal;
	}
	public void setVariacionSubTotal(String variacionSubTotal) {
		this.variacionSubTotal = variacionSubTotal;
	}
	public String getVariacionTotal() {
		return variacionTotal;
	}
	public void setVariacionTotal(String variacionTotal) {
		this.variacionTotal = variacionTotal;
	}
	
	
	
}
