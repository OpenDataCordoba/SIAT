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
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

public class ArchivoVO extends SiatBussImageModel {


	private static final long serialVersionUID = 1L;
	
	private String nombre = "";
	private EstadoArcVO estadoArc = new EstadoArcVO();
	private TipoArcVO tipoArc = new TipoArcVO();
	private BalanceVO balance = new BalanceVO();
	private Long idOrigen;
	private Double total;
	private Long cantTrans;
	private Integer nroBanco;
	private Date fechaBanco;
	private String prefix = "";
	private String observacion = "";
	
	private String idOrigenView = "";
	private String totalView = "";
	private String cantTransView = "";
	private String nroBancoView = "";
	private String fechaBancoView = "";
	
	// Listas de Entidades Relacionadas con Archivo
	private List<TranArcVO> listTranArc = new ArrayList<TranArcVO>();

	private Boolean anularBussEnabled   = true;
	private Boolean aceptarBussEnabled   = true;

	// Constructores 
	public ArchivoVO(){
		super();
	}

	// Getters Y Setters 
	public EstadoArcVO getEstadoArc() {
		return estadoArc;
	}
	public void setEstadoArc(EstadoArcVO estadoArc) {
		this.estadoArc = estadoArc;
	}
	public BalanceVO getBalance() {
		return balance;
	}
	public void setBalance(BalanceVO balance) {
		this.balance = balance;
	}
	public List<TranArcVO> getListTranArc() {
		return listTranArc;
	}
	public void setListTranArc(List<TranArcVO> listTranArc) {
		this.listTranArc = listTranArc;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public TipoArcVO getTipoArc() {
		return tipoArc;
	}
	public void setTipoArc(TipoArcVO tipoArc) {
		this.tipoArc = tipoArc;
	}
	public Long getCantTrans() {
		return cantTrans;
	}
	public void setCantTrans(Long cantTrans) {
		this.cantTrans = cantTrans;
		this.cantTransView = StringUtil.formatLong(cantTrans);
	}
	public String getCantTransView() {
		return cantTransView;
	}
	public void setCantTransView(String cantTransView) {
		this.cantTransView = cantTransView;
	}
	public Date getFechaBanco() {
		return fechaBanco;
	}
	public void setFechaBanco(Date fechaBanco) {
		this.fechaBanco = fechaBanco;
		this.fechaBancoView = DateUtil.formatDate(fechaBanco, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaBancoView() {
		return fechaBancoView;
	}
	public void setFechaBancoView(String fechaBancoView) {
		this.fechaBancoView = fechaBancoView;
	}
	public Long getIdOrigen() {
		return idOrigen;
	}
	public void setIdOrigen(Long idOrigen) {
		this.idOrigen = idOrigen;
		this.idOrigenView = StringUtil.formatLong(idOrigen);
	}
	public String getIdOrigenView() {
		return idOrigenView;
	}
	public void setIdOrigenView(String idOrigenView) {
		this.idOrigenView = idOrigenView;
	}
	public Integer getNroBanco() {
		return nroBanco;
	}
	public void setNroBanco(Integer nroBanco) {
		this.nroBanco = nroBanco;
		this.nroBancoView = StringUtil.formatInteger(nroBanco);
	}
	public String getNroBancoView() {
		return nroBancoView;
	}
	public void setNroBancoView(String nroBancoView) {
		this.nroBancoView = nroBancoView;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
		this.totalView = StringUtil.formatDouble(total);
	}
	public String getTotalView() {
		return totalView;
	}
	public void setTotalView(String totalView) {
		this.totalView = totalView;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	// Flags Seguridad
	public Boolean getAnularBussEnabled() {
		return anularBussEnabled;
	}

	public void setAnularBussEnabled(Boolean anularBussEnabled) {
		this.anularBussEnabled = anularBussEnabled;
	}
	
	public String getAnularEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getAnularBussEnabled(), 
				BalSecurityConstants.ABM_ARCHIVO, BaseSecurityConstants.ANULAR);
	}
	
	public Boolean getAceptarBussEnabled() {
		return aceptarBussEnabled;
	}

	public void setAceptarBussEnabled(Boolean aceptarBussEnabled) {
		this.aceptarBussEnabled = aceptarBussEnabled;
	}
	
	public String getAceptarEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getAceptarBussEnabled(), 
				BalSecurityConstants.ABM_ARCHIVO, BaseSecurityConstants.ACEPTAR);
	}	
}
