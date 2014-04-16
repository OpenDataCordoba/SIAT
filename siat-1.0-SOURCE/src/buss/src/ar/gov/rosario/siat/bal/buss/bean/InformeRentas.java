//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.model.FilaVO;

public class InformeRentas extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	private String fechaDesde = null;
	private String fechaHasta = null;
	private String fechaReporte = null;
	private String nroBalance = "";	
	
	private String reporteProcesoBalance = "false";
	
	private String reporteCuenta = "false";
	
	private List<FilaVO> listPartidasOCuentas = new ArrayList<FilaVO>();
	
	String desTipoProceso = null;
	String desCorrida = null;
	
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
	public String getReporteProcesoBalance() {
		return reporteProcesoBalance;
	}
	public void setReporteProcesoBalance(String reporteProcesoBalance) {
		this.reporteProcesoBalance = reporteProcesoBalance;
	}
	public List<FilaVO> getListPartidasOCuentas() {
		return listPartidasOCuentas;
	}
	public void setListPartidasOCuentas(List<FilaVO> listPartidasOCuentas) {
		this.listPartidasOCuentas = listPartidasOCuentas;
	}
	public String getReporteCuenta() {
		return reporteCuenta;
	}
	public void setReporteCuenta(String reporteCuenta) {
		this.reporteCuenta = reporteCuenta;
	}
	public String getNroBalance() {
		return nroBalance;
	}
	public void setNroBalance(String nroBalance) {
		this.nroBalance = nroBalance;
	}
	
	
	
}
