//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;


/**
 * Value Object del CierreSucursal
 * @author tecso
 *
 */
public class CierreSucursalVO extends SiatBussImageModel {
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "cierreSucursalVO";

	private CierreBancoVO cierreBanco = new CierreBancoVO();
	private Long nroCieSuc;
	private Long sucursal;
	private Integer tipoSucursal;
	private Date fechaRegistro;
	private Date fechaCierre;

	private String nroCieSucView = "";
	private String sucursalView = "";
	private String tipoSucursalView = "";
	private String fechaRegistroView = "";
	private String fechaCierreView = "";

	// Buss Flags


	// Constructores
	public CierreSucursalVO() {
		super();
	}

	// Getters y Setters
	public CierreBancoVO getCierreBanco() {
		return cierreBanco;
	}
	public void setCierreBanco(CierreBancoVO cierreBanco) {
		this.cierreBanco = cierreBanco;
	}
	public Date getFechaCierre() {
		return fechaCierre;
	}
	public void setFechaCierre(Date fechaCierre) {
		this.fechaCierre = fechaCierre;
		this.fechaCierreView = DateUtil.formatDate(fechaCierre, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaCierreView() {
		return fechaCierreView;
	}
	public void setFechaCierreView(String fechaCierreView) {
		this.fechaCierreView = fechaCierreView;
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
	public Long getNroCieSuc() {
		return nroCieSuc;
	}
	public void setNroCieSuc(Long nroCieSuc) {
		this.nroCieSuc = nroCieSuc;
		this.nroCieSucView = StringUtil.formatLong(nroCieSuc);
	}
	public String getNroCieSucView() {
		return nroCieSucView;
	}
	public void setNroCieSucView(String nroCieSucView) {
		this.nroCieSucView = nroCieSucView;
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

	
}
