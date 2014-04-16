//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import ar.gov.rosario.siat.def.iface.model.AreaVO;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import coop.tecso.demoda.iface.helper.DateUtil;

public class CompensacionVO extends SiatBussImageModel {
	
	private static final long serialVersionUID = 1L;

	private Date fechaAlta;
	private String descripcion = "";
	private EstadoComVO estadoCom = new EstadoComVO();
	private String idCaso = "";
	private TipoComVO tipoCom = new TipoComVO();
	private AreaVO area = new AreaVO();
	private CuentaVO cuenta = new CuentaVO();
	private CuentaBancoVO cuentaBanco = new CuentaBancoVO();
	
	private CasoVO caso = new CasoVO();
	
	private BalanceVO balance = new BalanceVO();
	
	private List<ComDeuVO> listComDeu = new ArrayList<ComDeuVO>();
	private List<SaldoAFavorVO> listSaldoAFavor = new ArrayList<SaldoAFavorVO>();
	
	private String fechaAltaView = "";
		
	private boolean paramEstado = false;
	
	private Boolean enviarBussEnabled   = true;
	private Boolean devolverBussEnabled   = true;
	
	private Boolean paramEnviado   = false;

	// Constructores 
	public CompensacionVO(){
		super();
	}

	// Getters Y Setters
	public AreaVO getArea() {
		return area;
	}
	public void setArea(AreaVO area) {
		this.area = area;
	}
	public CuentaVO getCuenta() {
		return cuenta;
	}
	public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}
	public CuentaBancoVO getCuentaBanco() {
		return cuentaBanco;
	}
	public void setCuentaBanco(CuentaBancoVO cuentaBanco) {
		this.cuentaBanco = cuentaBanco;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public EstadoComVO getEstadoCom() {
		return estadoCom;
	}
	public void setEstadoCom(EstadoComVO estadoCom) {
		this.estadoCom = estadoCom;
	}
	public Date getFechaAlta() {
		return fechaAlta;
	}
	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
		this.fechaAltaView = DateUtil.formatDate(fechaAlta, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getIdCaso() {
		return idCaso;
	}
	public void setIdCaso(String idCaso) {
		this.idCaso = idCaso;
	}
	public List<ComDeuVO> getListComDeu() {
		return listComDeu;
	}
	public void setListComDeu(List<ComDeuVO> listComDeu) {
		this.listComDeu = listComDeu;
	}
	public TipoComVO getTipoCom() {
		return tipoCom;
	}
	public void setTipoCom(TipoComVO tipoCom) {
		this.tipoCom = tipoCom;
	}
	public String getFechaAltaView() {
		return fechaAltaView;
	}
	public void setFechaAltaView(String fechaAltaView) {
		this.fechaAltaView = fechaAltaView;
	}
	public CasoVO getCaso() {
		return caso;
	}
	public void setCaso(CasoVO caso) {
		this.caso = caso;
	}
	public BalanceVO getBalance() {
		return balance;
	}
	public void setBalance(BalanceVO balance) {
		this.balance = balance;
	}
	public List<SaldoAFavorVO> getListSaldoAFavor() {
		return listSaldoAFavor;
	}
	public void setListSaldoAFavor(List<SaldoAFavorVO> listSaldoAFavor) {
		this.listSaldoAFavor = listSaldoAFavor;
	}
	public boolean isParamEstado() {
		return paramEstado;
	}
	public void setParamEstado(boolean paramEstado) {
		this.paramEstado = paramEstado;
	}
	
	public Boolean getParamEnviado() {
		return paramEnviado;
	}

	public void setParamEnviado(Boolean paramEnviado) {
		this.paramEnviado = paramEnviado;
	}

	// Flags Seguridad
	public Boolean getEnviarBussEnabled() {
		return enviarBussEnabled;
	}

	public void setEnviarBussEnabled(Boolean enviarBussEnabled) {
		this.enviarBussEnabled = enviarBussEnabled;
	}
	
	public String getEnviarEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getEnviarBussEnabled(), 
				BalSecurityConstants.ABM_COMPENSACION, BaseSecurityConstants.ENVIAR);
	}
	public Boolean getDevolverBussEnabled() {
		return devolverBussEnabled;
	}

	public void setDevolverBussEnabled(Boolean devolverBussEnabled) {
		this.devolverBussEnabled = devolverBussEnabled;
	}
	
	public String getDevolverEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getDevolverBussEnabled(), 
				BalSecurityConstants.ABM_COMPENSACION, BaseSecurityConstants.DEVOLVER);
	}

}
