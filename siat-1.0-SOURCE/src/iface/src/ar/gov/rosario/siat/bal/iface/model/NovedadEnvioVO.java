//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;


/**
 * Value Object del NovedadEnvio
 * @author tecso
 *
 */
public class NovedadEnvioVO extends SiatBussImageModel {
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "novedadEnvioVO";

	private CierreBancoVO cierreBanco = new CierreBancoVO();
	private Long idTransaccionAfip;
	private Integer formaPago;
	private Long sucursal;
	private Integer tipoSucursal;
	private Integer tipoOperacion;
	private SiNo aceptada = SiNo.OpcionNula;
	private SiNo deOficio = SiNo.OpcionNula;
	private Date fechaNovedad;
	private Date fechaRegistro;
	
	private String idTransaccionAfipView = "";
	private String formaPagoView = "";
	private String sucursalView = "";
	private String tipoSucursalView = "";
	private String tipoOperacionView = "";
	private String fechaNovedadView = "";
	private String fechaRegistroView = "";

	// Buss Flags


	// Constructores
	public NovedadEnvioVO() {
		super();
	}

	// Getters y Setters
	public SiNo getAceptada() {
		return aceptada;
	}
	public void setAceptada(SiNo aceptada) {
		this.aceptada = aceptada;
	}
	public CierreBancoVO getCierreBanco() {
		return cierreBanco;
	}
	public void setCierreBanco(CierreBancoVO cierreBanco) {
		this.cierreBanco = cierreBanco;
	}
	public SiNo getDeOficio() {
		return deOficio;
	}
	public void setDeOficio(SiNo deOficio) {
		this.deOficio = deOficio;
	}
	public Date getFechaNovedad() {
		return fechaNovedad;
	}
	public void setFechaNovedad(Date fechaNovedad) {
		this.fechaNovedad = fechaNovedad;
		this.fechaNovedadView = DateUtil.formatDate(fechaNovedad, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaNovedadView() {
		return fechaNovedadView;
	}
	public void setFechaNovedadView(String fechaNovedadView) {
		this.fechaNovedadView = fechaNovedadView;
	}
	public Date getFechaRegistro() {
		return fechaRegistro;
	}
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
		this.fechaRegistroView = DateUtil.formatDate(fechaRegistro, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaRegistroView() {
		return fechaRegistroView;
	}
	public void setFechaRegistroView(String fechaRegistroView) {
		this.fechaRegistroView = fechaRegistroView;
	}
	public Integer getFormaPago() {
		return formaPago;
	}
	public void setFormaPago(Integer formaPago) {
		this.formaPago = formaPago;
		this.formaPagoView = StringUtil.formatInteger(formaPago);
	}
	public String getFormaPagoView() {
		return formaPagoView;
	}
	public void setFormaPagoView(String formaPagoView) {
		this.formaPagoView = formaPagoView;
	}
	public Long getSucursal() {
		return sucursal;
	}
	public void setSucursal(Long sucursal) {
		this.sucursal = sucursal;
		this.sucursalView = StringUtil.formatLong(sucursal);
	}
	public String getSucursalView() {
		return sucursalView;
	}
	public void setSucursalView(String sucursalView) {
		this.sucursalView = sucursalView;
	}
	public Integer getTipoOperacion() {
		return tipoOperacion;
	}
	public void setTipoOperacion(Integer tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
		this.tipoOperacionView = StringUtil.formatInteger(tipoOperacion);
	}
	public String getTipoOperacionView() {
		return tipoOperacionView;
	}
	public void setTipoOperacionView(String tipoOperacionView) {
		this.tipoOperacionView = tipoOperacionView;
	}
	public Integer getTipoSucursal() {
		return tipoSucursal;
	}
	public void setTipoSucursal(Integer tipoSucursal) {
		this.tipoSucursal = tipoSucursal;
		this.tipoSucursalView = StringUtil.formatInteger(tipoSucursal);
	}
	public String getTipoSucursalView() {
		return tipoSucursalView;
	}
	public void setTipoSucursalView(String tipoSucursalView) {
		this.tipoSucursalView = tipoSucursalView;
	}
	public Long getIdTransaccionAfip() {
		return idTransaccionAfip;
	}
	public void setIdTransaccionAfip(Long idTransaccionAfip) {
		this.idTransaccionAfip = idTransaccionAfip;
		this.idTransaccionAfipView = StringUtil.formatLong(idTransaccionAfip);
	}
	public String getIdTransaccionAfipView() {
		return idTransaccionAfipView;
	}
	public void setIdTransaccionAfipView(String idTransaccionAfipView) {
		this.idTransaccionAfipView = idTransaccionAfipView;
	}
	
	
}
