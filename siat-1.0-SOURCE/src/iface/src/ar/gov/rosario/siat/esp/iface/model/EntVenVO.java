//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.gde.iface.model.DeudaVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

public class EntVenVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;
	
	private HabilitacionVO habilitacion = new HabilitacionVO();
	private Integer totalVendidas=0;
	private EntHabVO entHab = new EntHabVO();
	private Date fechaEmision;
	private DeudaVO deuda = new DeudaVO();
	private Double importe=0.0;
	
	private String totalVendidasView = "0";
	private String fechaEmisionView="";
	private String importeView="0.0";
	
	private Integer esAnulada=0; // 0 - Venta
								 // 1 - Anulada
	

	
	private String noAnulable="";
	
	//Constructores 
	public EntVenVO(){
		super();
	}

	// Getters & Setters
	public HabilitacionVO getHabilitacion() {
		return habilitacion;
	}
	public void setHabilitacion(HabilitacionVO habilitacion) {
		this.habilitacion = habilitacion;
	}
	public Integer getTotalVendidas() {
		return totalVendidas;
	}
	public void setTotalVendidas(Integer totalVendidas) {
		this.totalVendidas = totalVendidas;
		this.totalVendidasView = StringUtil.formatInteger(totalVendidas);
	}
	public String getTotalVendidasView() {
		return totalVendidasView;
	}
	public void setTotalVendidasView(String totalVendidasView) {
		this.totalVendidasView = totalVendidasView;
	}

	public EntHabVO getEntHab() {
		return entHab;
	}

	public void setEntHab(EntHabVO entHab) {
		this.entHab = entHab;
	}

	public Date getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
		this.fechaEmisionView = DateUtil.formatDate(fechaEmision, DateUtil.ddSMMSYYYY_MASK);
	}

	public DeudaVO getDeuda() {
		return deuda;
	}

	public void setDeuda(DeudaVO deuda) {
		this.deuda = deuda;
	}

	public String getFechaEmisionView() {
		return fechaEmisionView;
	}

	public void setFechaEmisionView(String fechaEmisionView) {
		this.fechaEmisionView = fechaEmisionView;
	}

	public Double getImporte() {
		return importe;
	}

	public void setImporte(Double importe) {
		this.importe = importe;
		this.importeView = StringUtil.formatDouble(importe);
	}

	public String getImporteView() {
		return importeView;
	}

	public void setImporteView(String importeView) {
		this.importeView = importeView;
	}
	
	public String getNoAnulable() {
		return noAnulable;
	}

	public void setNoAnulable(String noAnulable) {
		this.noAnulable = noAnulable;
	}

	public Integer getEsAnulada() {
		return esAnulada;
	}

	public void setEsAnulada(Integer esAnulada) {
		this.esAnulada = esAnulada;
	}

	
}
