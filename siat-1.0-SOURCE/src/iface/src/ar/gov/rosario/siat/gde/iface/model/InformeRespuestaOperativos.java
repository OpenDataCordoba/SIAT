//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

public class InformeRespuestaOperativos extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	private Long cantDeudas = 0L;
	private Long cantDeudasPagas = 0L;
	private Long cantConvenios = 0L;
	private Long cantRecibos = 0L;
	private Long cantDeudaEnConvenio = 0L;
	private Long cantDeudaEnRecibo = 0L;
	private String fechaProceso = null;
	private String fechaReporte = null;
	
	String desTipoProceso = null;
	String desCorrida = null;

	// Getters y Setters
	public Long getCantConvenios() {
		return cantConvenios;
	}
	public void setCantConvenios(Long cantConvenios) {
		this.cantConvenios = cantConvenios;
	}
	public Long getCantDeudas() {
		return cantDeudas;
	}
	public void setCantDeudas(Long cantDeudas) {
		this.cantDeudas = cantDeudas;
	}
	public Long getCantDeudasPagas() {
		return cantDeudasPagas;
	}
	public void setCantDeudasPagas(Long cantDeudasPagas) {
		this.cantDeudasPagas = cantDeudasPagas;
	}
	public Long getCantRecibos() {
		return cantRecibos;
	}
	public void setCantRecibos(Long cantRecibos) {
		this.cantRecibos = cantRecibos;
	}
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
	public String getFechaProceso() {
		return fechaProceso;
	}
	public void setFechaProceso(String fechaProceso) {
		this.fechaProceso = fechaProceso;
	}
	public String getFechaReporte() {
		return fechaReporte;
	}
	public void setFechaReporte(String fechaReporte) {
		this.fechaReporte = fechaReporte;
	}
	public Long getCantDeudaEnConvenio() {
		return cantDeudaEnConvenio;
	}
	public void setCantDeudaEnConvenio(Long cantDeudaEnConvenio) {
		this.cantDeudaEnConvenio = cantDeudaEnConvenio;
	}
	public Long getCantDeudaEnRecibo() {
		return cantDeudaEnRecibo;
	}
	public void setCantDeudaEnRecibo(Long cantDeudaEnRecibo) {
		this.cantDeudaEnRecibo = cantDeudaEnRecibo;
	}
	
	
}
