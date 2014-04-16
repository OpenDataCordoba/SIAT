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

public class FolioVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	private Date fechaFolio;
	private Long numero;
	private String descripcion = "";
	private String desDiaCob = "";
	private EstadoFolVO estadoFol = new EstadoFolVO();
	private BalanceVO balance = new BalanceVO();

	private List<FolComVO> listFolCom = new ArrayList<FolComVO>();
	private List<OtrIngTesVO> listOtrIngTes = new ArrayList<OtrIngTesVO>();
	private List<FolDiaCobVO> listFolDiaCob = new ArrayList<FolDiaCobVO>();

	private String fechaFolioView = "";
	private String numeroView = "";
	
	private Boolean enviarBussEnabled   = true;
	private Boolean devolverBussEnabled   = true;
	
	private Boolean paramEnviado   = false;
	
	//Constructores 
	public FolioVO(){
		super();
	}

	// Getters Y Setters 
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public EstadoFolVO getEstadoFol() {
		return estadoFol;
	}
	public void setEstadoFol(EstadoFolVO estadoFol) {
		this.estadoFol = estadoFol;
	}
	public Date getFechaFolio() {
		return fechaFolio;
	}
	public void setFechaFolio(Date fechaFolio) {
		this.fechaFolio = fechaFolio;
		this.fechaFolioView = DateUtil.formatDate(fechaFolio, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaFolioView() {
		return fechaFolioView;
	}
	public void setFechaFolioView(String fechaFolioView) {
		this.fechaFolioView = fechaFolioView;
	}
	public List<FolComVO> getListFolCom() {
		return listFolCom;
	}
	public void setListFolCom(List<FolComVO> listFolCom) {
		this.listFolCom = listFolCom;
	}
	public List<FolDiaCobVO> getListFolDiaCob() {
		return listFolDiaCob;
	}
	public void setListFolDiaCob(List<FolDiaCobVO> listFolDiaCob) {
		this.listFolDiaCob = listFolDiaCob;
	}
	public List<OtrIngTesVO> getListOtrIngTes() {
		return listOtrIngTes;
	}
	public void setListOtrIngTes(List<OtrIngTesVO> listOtrIngTes) {
		this.listOtrIngTes = listOtrIngTes;
	}
	public Long getNumero() {
		return numero;
	}
	public void setNumero(Long numero) {
		this.numero = numero;
		this.numeroView = StringUtil.formatLong(numero);
	}
	public String getNumeroView() {
		return numeroView;
	}
	public void setNumeroView(String numeroView) {
		this.numeroView = numeroView;
	}	
	public Boolean getParamEnviado() {
		return paramEnviado;
	}
	public void setParamEnviado(Boolean paramEnviado) {
		this.paramEnviado = paramEnviado;
	}
	public BalanceVO getBalance() {
		return balance;
	}
	public void setBalance(BalanceVO balance) {
		this.balance = balance;
	}
	public String getDesDiaCob() {
		return desDiaCob;
	}
	public void setDesDiaCob(String desDiaCob) {
		this.desDiaCob = desDiaCob;
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
				BalSecurityConstants.ABM_FOLIO, BaseSecurityConstants.ENVIAR);
	}
	public Boolean getDevolverBussEnabled() {
		return devolverBussEnabled;
	}

	public void setDevolverBussEnabled(Boolean devolverBussEnabled) {
		this.devolverBussEnabled = devolverBussEnabled;
	}
	
	public String getDevolverEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getDevolverBussEnabled(), 
				BalSecurityConstants.ABM_FOLIO, BaseSecurityConstants.DEVOLVER);
	}
}
