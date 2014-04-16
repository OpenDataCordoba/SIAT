//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

public class FilaConvenioCuotaVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;
	
	private String nroConvenio = "";
	
	private String fechaFor="";
	
	private String nroCuota = "";
	
	private String nroCuotaImputada = "";
	
	private String totalImporteAplicado = "0";
	
	private String totalActAplicado = "0";
	
	private String total = "0";

	private List<AuxLiqComProDeuVO> listAuxLiqComProDeu = new ArrayList<AuxLiqComProDeuVO>();
	
	public String getNroConvenio() {
		return nroConvenio;
	}

	public void setNroConvenio(String nroConvenio) {
		this.nroConvenio = nroConvenio;
	}

	public String getNroCuota() {
		return nroCuota;
	}

	public void setNroCuota(String nroCuota) {
		this.nroCuota = nroCuota;
	}

	public String getNroCuotaImputada() {
		return nroCuotaImputada;
	}

	public void setNroCuotaImputada(String nroCuotaImputada) {
		this.nroCuotaImputada = nroCuotaImputada;
	}

	public String getTotalImporteAplicado() {
		return totalImporteAplicado;
	}

	public void setTotalImporteAplicado(String totalImporteAplicado) {
		this.totalImporteAplicado = totalImporteAplicado;
	}

	public String getTotalActAplicado() {
		return totalActAplicado;
	}

	public void setTotalActAplicado(String totalActAplicado) {
		this.totalActAplicado = totalActAplicado;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public List<AuxLiqComProDeuVO> getListAuxLiqComProDeu() {
		return listAuxLiqComProDeu;
	}

	public void setListAuxLiqComProDeu(List<AuxLiqComProDeuVO> listAuxLiqComProDeu) {
		this.listAuxLiqComProDeu = listAuxLiqComProDeu;
	}

	public String getFechaFor() {
		return fechaFor;
	}

	public void setFechaFor(String fechaFor) {
		this.fechaFor = fechaFor;
	}
	

	
	
	
}
