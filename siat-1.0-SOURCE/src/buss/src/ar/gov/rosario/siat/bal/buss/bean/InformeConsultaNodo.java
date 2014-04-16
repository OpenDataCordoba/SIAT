//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.model.NodoVO;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.model.TablaVO;

public class InformeConsultaNodo extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	private String fechaDesde = null;
	private String fechaHasta = null;
	private String clasificador = "";
	private String nivel = "";
	
	private NodoVO nodo = new NodoVO();
	private List<NodoVO> listNodo = new ArrayList<NodoVO>();
		
	private String rangosFechaExtra = "false";
	private String totalRangos = "";
	private String especial = "false";
	
	private TablaVO tablaReporteEspecial = new TablaVO("tablaReporteEspecial");
	
	// Getters y Setters
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
	public String getClasificador() {
		return clasificador;
	}
	public void setClasificador(String clasificador) {
		this.clasificador = clasificador;
	}
	public String getNivel() {
		return nivel;
	}
	public void setNivel(String nivel) {
		this.nivel = nivel;
	}
	public NodoVO getNodo() {
		return nodo;
	}
	public void setNodo(NodoVO nodo) {
		this.nodo = nodo;
	}
	public List<NodoVO> getListNodo() {
		return listNodo;
	}
	public void setListNodo(List<NodoVO> listNodo) {
		this.listNodo = listNodo;
	}
	public String getRangosFechaExtra() {
		return rangosFechaExtra;
	}
	public void setRangosFechaExtra(String rangosFechaExtra) {
		this.rangosFechaExtra = rangosFechaExtra;
	}
	public String getTotalRangos() {
		return totalRangos;
	}
	public void setTotalRangos(String totalRangos) {
		this.totalRangos = totalRangos;
	}
	public String getEspecial() {
		return especial;
	}
	public void setEspecial(String especial) {
		this.especial = especial;
	}
	public TablaVO getTablaReporteEspecial() {
		return tablaReporteEspecial;
	}
	public void setTablaReporteEspecial(TablaVO tablaReporteEspecial) {
		this.tablaReporteEspecial = tablaReporteEspecial;
	}

	
}
