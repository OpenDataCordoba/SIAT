//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.pro.iface.model.CorridaVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

public class EnvioOsirisVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;
	
	private Long idEnvioAfip;
	private Long idOrgRecAfip;
	private EstadoEnvioVO estadoEnvio = new EstadoEnvioVO();
	private Date fechaRecepcion;
	private Date fechaProceso;
	private String observacion="";
	private Integer cantidadPagos;
	private Double importePagos;
	private Integer canDecJur;
	private CorridaVO corrida = new CorridaVO();
	private Date fechaRegistroMulat;
	
	private Integer canTranPago;
	private Integer canTranDecJur;
	
	private String idEnvioAfipView = "";
	
	private List<CierreBancoVO> listCierreBanco = new ArrayList<CierreBancoVO>(); 
	private List<ConciliacionVO> listConciliacion = new ArrayList<ConciliacionVO>();
	private List<EnvNotOblVO> 	 listEnvNotObl = new ArrayList<EnvNotOblVO>();

	// Buss Flags
	private Boolean generarTransaccionBussEnabled  = false;
	private Boolean generarForDecJurBussEnabled  = false;
	private Boolean cambiarEstadoBussEnabled  = false;

	// Constructores 
	public EnvioOsirisVO(){
		super();
	}


	// Getters y Setters
	public Long getIdEnvioAfip() {
		return idEnvioAfip;
	}

	public void setIdEnvioAfip(Long idEnvioAfip) {
		this.idEnvioAfip = idEnvioAfip;
		this.idEnvioAfipView = StringUtil.formatLong(idEnvioAfip);
	}

	public Long getIdOrgRecAfip() {
		return idOrgRecAfip;
	}

	public void setIdOrgRecAfip(Long idOrgRecAfip) {
		this.idOrgRecAfip = idOrgRecAfip;
	}

	public EstadoEnvioVO getEstadoEnvio() {
		return estadoEnvio;
	}

	public void setEstadoEnvio(EstadoEnvioVO estadoEnvio) {
		this.estadoEnvio = estadoEnvio;
	}

	public Date getFechaRecepcion() {
		return fechaRecepcion;
	}

	public void setFechaRecepcion(Date fechaRecepcion) {
		this.fechaRecepcion = fechaRecepcion;
	}

	public Date getFechaProceso() {
		return fechaProceso;
	}

	public void setFechaProceso(Date fechaProceso) {
		this.fechaProceso = fechaProceso;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public Integer getCantidadPagos() {
		return cantidadPagos;
	}

	public void setCantidadPagos(Integer cantidadPagos) {
		this.cantidadPagos = cantidadPagos;
	}

	public Double getImportePagos() {
		return importePagos;
	}

	public void setImportePagos(Double importePagos) {
		this.importePagos = importePagos;
	}

	public Integer getCanDecJur() {
		return canDecJur;
	}

	public void setCanDecJur(Integer canDecJur) {
		this.canDecJur = canDecJur;
	}

	public CorridaVO getCorrida() {
		return corrida;
	}

	public void setCorrida(CorridaVO corrida) {
		this.corrida = corrida;
	}
	
	public Date getFechaRegistroMulat() {
		return fechaRegistroMulat;
	}

	public void setFechaRegistroMulat(Date fechaRegistroMulat) {
		this.fechaRegistroMulat = fechaRegistroMulat;
	}
	
	public List<ConciliacionVO> getListConciliacion() {
		return listConciliacion;
	}
	
	public void setListConciliacion(List<ConciliacionVO> listConciliacion) {
		this.listConciliacion = listConciliacion;
	}
	
	public void setListEnvNotObl(List<EnvNotOblVO> listEnvNotObl) {
		this.listEnvNotObl = listEnvNotObl;
	}


	public List<EnvNotOblVO> getListEnvNotObl() {
		return listEnvNotObl;
	}


	public Integer getCanTranPago() {
		return canTranPago;
	}
		
	public void setCanTranPago(Integer canTranPago) {
		this.canTranPago = canTranPago;
	}
	
	
	public Integer getCanTranDecJur() {
		return canTranDecJur;
	}
	
	public void setCanTranDecJur(Integer canTranDecJur) {
		this.canTranDecJur = canTranDecJur;
	}
	//	 Buss flags getters y setters
	
	//  View getter
	public String getFechaProcesoView(){
		return (this.fechaProceso!=null)?DateUtil.formatDate(this.fechaProceso, DateUtil.ddSMMSYYYY_MASK):"";
	}
	
	public String getFechaRecepcionView(){
		return (this.fechaRecepcion!=null)?DateUtil.formatDate(this.fechaRecepcion, DateUtil.ddSMMSYYYY_MASK):"";
	}
	
	public String getFechaRegistroMulatView(){
		return (this.fechaRegistroMulat!=null)?DateUtil.formatDate(this.fechaRegistroMulat, DateUtil.ddSMMSYYYY_MASK):"";
	}
	
	public String getCantidadPagosView(){
		return (this.cantidadPagos!=null)?cantidadPagos.toString():"";
	}
	
	public String getCanDecJurView(){
		return (this.canDecJur!=null)?canDecJur.toString():"";
	}
	
	public String getImportePagosView(){
		return (this.importePagos!=null)?NumberUtil.round(this.importePagos, SiatParam.DEC_IMPORTE_VIEW).toString():"";
	}
	
	public void setIdEnvioAfipView(String idEnvioAfipView) {
		this.idEnvioAfipView = idEnvioAfipView;
	}

	public String getIdEnvioAfipView(){
		return this.idEnvioAfipView;
	}
	
	public String getIdOrgRecAfipView(){
		return (this.idOrgRecAfip!=null)?idOrgRecAfip.toString():"";
	}
	

	public List<CierreBancoVO> getListCierreBanco() {
		return listCierreBanco;
	}

	public void setListCierreBanco(List<CierreBancoVO> listCierreBanco) {
		this.listCierreBanco = listCierreBanco;
	}
	
	public String getCanTranDecJurView(){
		return (this.canTranDecJur!=null)?canTranDecJur.toString():"";
	}
	
	public String getCanTranPagoView(){
		return (this.canTranPago!=null)?canTranPago.toString():"";
	}

	//	 Buss flags getters y setters
	public Boolean getGenerarTransaccionBussEnabled() {
		return generarTransaccionBussEnabled;
	}
	public void setGenerarTransaccionBussEnabled(Boolean generarTransaccionBussEnabled) {
		this.generarTransaccionBussEnabled = generarTransaccionBussEnabled;
	}
	public String getGenerarTransaccionEnabled() {
		return this.getGenerarTransaccionBussEnabled() ? ENABLED : DISABLED;
	}
	
	public Boolean getGenerarForDecJurBussEnabled() {
		return generarForDecJurBussEnabled;
	}
	public void setGenerarForDecJurBussEnabled(Boolean generarForDecJurBussEnabled) {
		this.generarForDecJurBussEnabled = generarForDecJurBussEnabled;
	}
	public String getGenerarForDecJurEnabled() {
		return this.getGenerarForDecJurBussEnabled() ? ENABLED : DISABLED;
	}

	public void setCambiarEstadoBussEnabled(Boolean cambiarEstadoBussEnabled) {
		this.cambiarEstadoBussEnabled = cambiarEstadoBussEnabled;
	}
	public Boolean getCambiarEstadoBussEnabled() {
		return cambiarEstadoBussEnabled;
	}
	public String getCambiarEstadoEnabled() {
		return this.getCambiarEstadoBussEnabled() ? ENABLED : DISABLED;
	}
	
}