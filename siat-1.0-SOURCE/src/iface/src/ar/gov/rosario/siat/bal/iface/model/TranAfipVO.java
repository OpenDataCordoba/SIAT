//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import coop.tecso.demoda.iface.helper.DateUtil;

public class TranAfipVO extends SiatBussImageModel {
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "tranAfipVO";	

	private EnvioOsirisVO envioOsiris = new EnvioOsirisVO();	

	private Long idTransaccionAfip;	

	private TipoOperacionVO tipoOperacion = new TipoOperacionVO();	

	private CuentaVO cuenta = new CuentaVO();	
	
	private Integer formulario;	
	
	private Date fechaProceso;
	
	private Date fechaAnulacion;
		
	private Double totMontoIngresado;
	
	private String cuit = "";
	
	private CierreBancoVO cierreBanco = new CierreBancoVO();
	
	private String numeroCuenta = "";
	
	private String observacion = "";
	
	private Integer canPago;
	
	private Integer canDecJur;
	
	private EstTranAfipVO estTranAfip = new EstTranAfipVO();
	
	private String vep;

	private String cumur = "";

	private Long nroTran;

	private Long nroTranPres;
	
	private Integer nroCheque;
	
	private Integer ctaCteCheque;
	
	private Integer bancoCheque;
	
	private Integer sucursalCheque;
	
	private String codPostalCheque;
	
	private Date fechaAcredCheque;

	private List<DetallePagoVO> listDetallePago;
	
	private List<DetalleDJVO> listDetalleDJ;
	
	private String fechaProcesoView = "";
	
	private String fechaAnulacionView = "";
	
	private String fechaAcredChequeView="";
	
	// Buss Flags
	private Boolean generarDecJurBussEnabled  = true;
	private Boolean eliminarTranAfipBussEnabled  = true;
	
	public TranAfipVO() {
		super();
	}

	public EnvioOsirisVO getEnvioOsiris() {
		return envioOsiris;
	}

	public void setEnvioOsiris(EnvioOsirisVO envioOsiris) {
		this.envioOsiris = envioOsiris;
	}

	public Long getIdTransaccionAfip() {
		return idTransaccionAfip;
	}

	public void setIdTransaccionAfip(Long idTransaccionAfip) {
		this.idTransaccionAfip = idTransaccionAfip;
	}

	public TipoOperacionVO getTipoOperacion() {
		return tipoOperacion;
	}

	public void setTipoOperacion(TipoOperacionVO tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}

	public CuentaVO getCuenta() {
		return cuenta;
	}

	public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}

	public Integer getFormulario() {
		return formulario;
	}

	public void setFormulario(Integer formulario) {
		this.formulario = formulario;
	}

	public void setFechaProceso(Date fechaProceso) {
		this.fechaProceso = fechaProceso;		
		this.fechaProcesoView = DateUtil.formatDate(fechaProceso, DateUtil.ddSMMSYYYY_MASK);
	}
	
	public void setFechaAnulacion(Date fechaAnulacion) {
		this.fechaAnulacion = fechaAnulacion;		
		this.fechaAnulacionView = DateUtil.formatDate(fechaAnulacion, DateUtil.ddSMMSYYYY_MASK);
	}

	public Double getTotMontoIngresado() {
		return totMontoIngresado;
	}

	public void setTotMontoIngresado(Double totMontoIngresado) {
		this.totMontoIngresado = totMontoIngresado;
	}

	public String getCuit() {
		return cuit;
	}

	public void setCuit(String cuit) {
		this.cuit = cuit;
	}

	public CierreBancoVO getCierreBanco() {
		return cierreBanco;
	}

	public void setCierreBanco(CierreBancoVO cierreBanco) {
		this.cierreBanco = cierreBanco;
	}

	public String getNumeroCuenta() {
		return numeroCuenta;
	}

	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public Integer getCanPago() {
		return canPago;
	}

	public void setCanPago(Integer canPago) {
		this.canPago = canPago;
	}

	public Integer getCanDecJur() {
		return canDecJur;
	}

	public void setCanDecJur(Integer canDecJur) {
		this.canDecJur = canDecJur;
	}

	public EstTranAfipVO getEstTranAfip() {
		return estTranAfip;
	}

	public void setEstTranAfip(EstTranAfipVO estTranAfip) {
		this.estTranAfip = estTranAfip;
	}

	public String getVep() {
		return vep;
	}

	public void setVep(String vep) {
		this.vep = vep;
	}

	public String getCumur() {
		return cumur;
	}

	public void setCumur(String cumur) {
		this.cumur = cumur;
	}

	public Long getNroTran() {
		return nroTran;
	}

	public void setNroTran(Long nroTran) {
		this.nroTran = nroTran;
	}

	public Long getNroTranPres() {
		return nroTranPres;
	}

	public Date getFechaProceso() {
		return fechaProceso;
	}

	public Date getFechaAnulacion() {
		return fechaAnulacion;
	}

	public void setNroTranPres(Long nroTranPres) {
		this.nroTranPres = nroTranPres;
	}

	public List<DetallePagoVO> getListDetallePago() {
		return listDetallePago;
	}

	public void setListDetallePago(List<DetallePagoVO> listDetallePago) {
		this.listDetallePago = listDetallePago;
	}
	
	public String getIdTransaccionAfipView(){
		return (this.idTransaccionAfip!=null)?idTransaccionAfip.toString():"";
	}
	
	public String getFormularioView(){
		return (this.formulario!=null)?formulario.toString():"";
	}

	public String getTotMontoIngresadoView(){
		return (this.totMontoIngresado!=null)?totMontoIngresado.toString():"";
	}
	
	public String getCanPagoView(){
		return (this.canPago!=null)?canPago.toString():"";
	}
	
	public String getCanDecJurView(){
		return (this.canDecJur!=null)?canDecJur.toString():"";
	}
	public String getVepView(){
		return (this.vep!=null)?vep.toString():"";
	}
	
	public String getNroTranView(){
		return (this.nroTran!=null)?nroTran.toString():"";
	}
	
	public String getNroTranPresView(){
		return (this.nroTranPres!=null)?nroTranPres.toString():"";
	}
	
	public String getNroChequeView(){
		return (this.nroCheque!=null)?nroCheque.toString():"";
	}
	
	public String getBancoChequeView(){
		return (this.bancoCheque!=null)?bancoCheque.toString():"";
	}
	
	public String getSucursalChequeView(){
		return (this.sucursalCheque!=null)?sucursalCheque.toString():"";
	}
	
	public String getCtaCteChequeView(){
		return (this.ctaCteCheque!=null)?ctaCteCheque.toString():"";
	}

	public String getFechaProcesoView() {
		return fechaProcesoView;
	}

	public String getFechaAnulacionView() {
		return fechaAnulacionView;
	}
	
	public String getFechaAcredChequeView() {
		return fechaAcredChequeView;
	}

	public void setListDetalleDJ(List<DetalleDJVO> listDetalleDJ) {
		this.listDetalleDJ = listDetalleDJ;
	}

	public List<DetalleDJVO> getListDetalleDJ() {
		return listDetalleDJ;
	}
	
	public Integer getNroCheque() {
		return nroCheque;
	}

	public void setNroCheque(Integer nroCheque) {
		this.nroCheque = nroCheque;
	}

	public Integer getCtaCteCheque() {
		return ctaCteCheque;
	}

	public void setCtaCteCheque(Integer ctaCteCheque) {
		this.ctaCteCheque = ctaCteCheque;
	}

	public Integer getSucursalCheque() {
		return sucursalCheque;
	}

	public void setSucursalCheque(Integer sucursalCheque) {
		this.sucursalCheque = sucursalCheque;
	}

	public Integer getBancoCheque() {
		return bancoCheque;
	}

	public void setBancoCheque(Integer bancoCheque) {
		this.bancoCheque = bancoCheque;
	}

	public String getCodPostalCheque() {
		return codPostalCheque;
	}

	public void setCodPostalCheque(String codPostalCheque) {
		this.codPostalCheque = codPostalCheque;
	}

	public Date getFechaAcredCheque() {
		return fechaAcredCheque;
	}

	public void setFechaAcredCheque(Date fechaAcredCheque) {
		this.fechaAcredCheque = fechaAcredCheque;
		this.fechaAcredChequeView = DateUtil.formatDate(fechaAcredCheque, DateUtil.ddSMMSYYYY_MASK);
	}

	//	 Buss flags getters y setters
	public Boolean getGenerarDecJurBussEnabled() {
		return generarDecJurBussEnabled;
	}
	public void setGenerarDecJurBussEnabled(Boolean generarDecJurBussEnabled) {
		this.generarDecJurBussEnabled = generarDecJurBussEnabled;
	}
	
	public Boolean getEliminarTranAfipBussEnabled() {
		return eliminarTranAfipBussEnabled;
	}
	public void setEliminarTranAfipBussEnabled(Boolean eliminarTranAfipBussEnabled) {
		this.eliminarTranAfipBussEnabled = eliminarTranAfipBussEnabled;
	}
	
	public String getEliminarTranAfipEnabled() {
		return this.getEliminarTranAfipBussEnabled() ? ENABLED : DISABLED;
	}
	
	public String getGenerarDecJurEnabled() {
		return this.getGenerarDecJurBussEnabled() ? ENABLED : DISABLED;
	}
}
