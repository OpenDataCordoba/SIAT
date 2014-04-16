//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;


/**
 * Value Object del Conciliacion
 * @author tecso
 *
 */
public class ConciliacionVO extends SiatBussImageModel {
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "conciliacionVO";

	private EnvioOsirisVO envioOsiris = new EnvioOsirisVO();
	private Long idEnvioAfip;
	private Long banco;
	private Long nroCierreBanco;
	private Date fechaConciliacion;
	private Long cantNota;
	private Double totalConciliado;
	
	private List<NotaConcVO> listNotaConc = new ArrayList<NotaConcVO>();
	
	private String idEnvioAfipView = "";
	private String bancoView = "";
	private String nroCierreBancoView = "";
	private String fechaConciliacionView = "";
	private String cantNotaView = "";
	private String totalConciliadoView = "";

	// Buss Flags


	// Constructores
	public ConciliacionVO() {
		super();
	}

	// Getters y Setters
	public Long getBanco() {
		return banco;
	}
	public void setBanco(Long banco) {
		this.banco = banco;
		this.bancoView = StringUtil.formatLong(banco);
	}
	public String getBancoView() {
		return bancoView;
	}
	public void setBancoView(String bancoView) {
		this.bancoView = bancoView;
	}
	public Long getCantNota() {
		return cantNota;
	}
	public void setCantNota(Long cantNota) {
		this.cantNota = cantNota;
		this.cantNotaView = StringUtil.formatLong(cantNota);
	}
	public String getCantNotaView() {
		return cantNotaView;
	}
	public void setCantNotaView(String cantNotaView) {
		this.cantNotaView = cantNotaView;
	}
	public EnvioOsirisVO getEnvioOsiris() {
		return envioOsiris;
	}
	public void setEnvioOsiris(EnvioOsirisVO envioOsiris) {
		this.envioOsiris = envioOsiris;
	}
	public Date getFechaConciliacion() {
		return fechaConciliacion;
	}
	public void setFechaConciliacion(Date fechaConciliacion) {
		this.fechaConciliacion = fechaConciliacion;
		this.fechaConciliacionView = DateUtil.formatDate(fechaConciliacion, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaConciliacionView() {
		return fechaConciliacionView;
	}
	public void setFechaConciliacionView(String fechaConciliacionView) {
		this.fechaConciliacionView = fechaConciliacionView;
	}
	public Long getIdEnvioAfip() {
		return idEnvioAfip;
	}
	public void setIdEnvioAfip(Long idEnvioAfip) {
		this.idEnvioAfip = idEnvioAfip;
		this.idEnvioAfipView = StringUtil.formatLong(idEnvioAfip);
	}
	public String getIdEnvioAfipView() {
		return idEnvioAfipView;
	}
	public void setIdEnvioAfipView(String idEnvioAfipView) {
		this.idEnvioAfipView = idEnvioAfipView;
	}
	public Long getNroCierreBanco() {
		return nroCierreBanco;
	}
	public void setNroCierreBanco(Long nroCierreBanco) {
		this.nroCierreBanco = nroCierreBanco;
		this.nroCierreBancoView = StringUtil.formatLong(nroCierreBanco);
	}
	public String getNroCierreBancoView() {
		return nroCierreBancoView;
	}
	public void setNroCierreBancoView(String nroCierreBancoView) {
		this.nroCierreBancoView = nroCierreBancoView;
	}
	public Double getTotalConciliado() {
		return totalConciliado;
	}
	public void setTotalConciliado(Double totalConciliado) {
		this.totalConciliado = totalConciliado;
		this.totalConciliadoView = StringUtil.formatDouble(totalConciliado);
	}
	public String getTotalConciliadoView() {
		return totalConciliadoView;
	}
	public void setTotalConciliadoView(String totalConciliadoView) {
		this.totalConciliadoView = totalConciliadoView;
	}
	public List<NotaConcVO> getListNotaConc() {
		return listNotaConc;
	}
	public void setListNotaConc(List<NotaConcVO> listNotaConc) {
		this.listNotaConc = listNotaConc;
	}

	
}
