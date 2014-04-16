//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import coop.tecso.demoda.iface.model.TablaVO;

public class InformeConvenioACaducar { //extends SiatBussImageModel {

	//private static final long serialVersionUID = 1L;

	private String desRecurso = null;
	private String desPlan = null;
	private String desViaDeuda = null;
	private String desEstadoConvenio = null;

	private String fechaCaducidad = null;
	private String fechaConvenioDesde = null;
	private String fechaConvenioHasta = null;
	
	private String fechaReporte = null;
	
	private String cuotaDesde = null;
	private String cuotaHasta = null;
	private String importeCuotaDesde = null;
	private String importeCuotaHasta = null;
	
	private TablaVO tablaConvenios = null;
	
	//private Long cantConvenioACaducar = 0L;

	// Getters y Setters
	public String getDesPlan() {
		return desPlan;
	}
	public void setDesPlan(String desPlan) {
		this.desPlan = desPlan;
	}
	public String getDesRecurso() {
		return desRecurso;
	}
	public void setDesRecurso(String desRecurso) {
		this.desRecurso = desRecurso;
	}
	public String getDesViaDeuda() {
		return desViaDeuda;
	}
	public void setDesViaDeuda(String desViaDeuda) {
		this.desViaDeuda = desViaDeuda;
	}
	public String getFechaCaducidad() {
		return fechaCaducidad;
	}
	public void setFechaCaducidad(String fechaCaducidad) {
		this.fechaCaducidad = fechaCaducidad;
	}
	public String getFechaConvenioDesde() {
		return fechaConvenioDesde;
	}
	public void setFechaConvenioDesde(String fechaConvenioDesde) {
		this.fechaConvenioDesde = fechaConvenioDesde;
	}
	public String getFechaConvenioHasta() {
		return fechaConvenioHasta;
	}
	public void setFechaConvenioHasta(String fechaConvenioHasta) {
		this.fechaConvenioHasta = fechaConvenioHasta;
	}
	public String getFechaReporte() {
		return fechaReporte;
	}
	public void setFechaReporte(String fechaReporte) {
		this.fechaReporte = fechaReporte;
	}
	public String getCuotaDesde() {
		return cuotaDesde;
	}
	public void setCuotaDesde(String cuotaDesde) {
		this.cuotaDesde = cuotaDesde;
	}
	public String getCuotaHasta() {
		return cuotaHasta;
	}
	public void setCuotaHasta(String cuotaHasta) {
		this.cuotaHasta = cuotaHasta;
	}
	public String getDesEstadoConvenio() {
		return desEstadoConvenio;
	}
	public void setDesEstadoConvenio(String desEstadoConvenio) {
		this.desEstadoConvenio = desEstadoConvenio;
	}
	public String getImporteCuotaDesde() {
		return importeCuotaDesde;
	}
	public void setImporteCuotaDesde(String importeCuotaDesde) {
		this.importeCuotaDesde = importeCuotaDesde;
	}
	public String getImporteCuotaHasta() {
		return importeCuotaHasta;
	}
	public void setImporteCuotaHasta(String importeCuotaHasta) {
		this.importeCuotaHasta = importeCuotaHasta;
	}
	/*public Long getCantConvenioACaducar() {
		return cantConvenioACaducar;
	}
	public void setCantConvenioACaducar(Long cantConvenioACaducar) {
		this.cantConvenioACaducar = cantConvenioACaducar;
	}*/
	public TablaVO getTablaConvenios() {
		return tablaConvenios;
	}
	public void setTablaConvenios(TablaVO tablaConvenios) {
		this.tablaConvenios = tablaConvenios;
	}

}
