//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Value Object del DetallePago
 * @author tecso
 *
 */
public class DetallePagoVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "detallePagoVO";	

	private TranAfipVO tranAfip = new TranAfipVO();

	private Integer caja;

	private Integer periodo;

	private Integer anio;
	
	private Date fechaPago;
	
	private Double importePago;

	private EstDetPagoVO estDetPago = new EstDetPagoVO();
	
	private NovedadEnvioVO novedadEnvio = new NovedadEnvioVO();

	private Integer impuesto;	
	
	private String fechaPagoView = "";
	
	private String numeroCuenta = "";
	
	
	// Constructores
	public DetallePagoVO() {
		super();
	}

	
	// Getters y Setters
	public TranAfipVO getTranAfip() {
		return tranAfip;
	}

	public void setTranAfip(TranAfipVO tranAfip) {
		this.tranAfip = tranAfip;
	}

	public void setNovedadEnvio(NovedadEnvioVO novedadEnvio) {
		this.novedadEnvio = novedadEnvio;
	}


	public NovedadEnvioVO getNovedadEnvio() {
		return novedadEnvio;
	}

	public Integer getCaja() {
		return caja;
	}

	public void setCaja(Integer caja) {
		this.caja = caja;
	}

	public Integer getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
	}

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	public Date getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
		this.fechaPagoView = DateUtil.formatDate(fechaPago, DateUtil.ddSMMSYYYY_MASK);
	}

	public Double getImportePago() {
		return importePago;
	}

	public void setImportePago(Double importePago) {
		this.importePago = importePago;
	}

	public EstDetPagoVO getEstDetPago() {
		return estDetPago;
	}

	public void setEstDetPago(EstDetPagoVO estDetPago) {
		this.estDetPago = estDetPago;
	}

	public Integer getImpuesto() {
		return impuesto;
	}

	public void setImpuesto(Integer impuesto) {
		this.impuesto = impuesto;
	}	

	// View getters	
	public String getFechaPagoView() {
		return fechaPagoView;
	}
	
	public String getCajaView(){
		return (this.caja!=null)?caja.toString():"";
	}
	
	public String getPeriodoView(){
		return (this.periodo!=null)?periodo.toString():"";
	}
	
	public String getAnioView(){
		return (this.anio!=null)?anio.toString():"";
	}
	
	public String getImportePagoView(){
		return (this.importePago!=null)?importePago.toString():"";
	}
	
	public String getImpuestoView(){
		return (this.impuesto!=null)?impuesto.toString():"";
	}


	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	public String getNumeroCuenta() {
		return numeroCuenta;
	}
	
}
