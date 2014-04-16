//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import ar.gov.rosario.siat.def.iface.model.ServicioBancoVO;
import ar.gov.rosario.siat.pro.iface.model.CorridaVO;
import coop.tecso.demoda.iface.helper.DateUtil;

public class AsentamientoVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	private Date fechaBalance;
	private EjercicioVO ejercicio = new EjercicioVO();
	private ServicioBancoVO servicioBanco = new ServicioBancoVO();
	private CasoVO caso = new CasoVO();
	private String observacion;
	private CorridaVO corrida = new CorridaVO();
	private String usuarioAlta;
	private BalanceVO balance = new BalanceVO();
	
	private String fechaBalanceView = "";
	private String idCorridaView = "";

	private Boolean admProcesoBussEnabled      = true;
	
	private boolean logDetalladoEnabled = false;
	
	//Constructores 
	public AsentamientoVO(){
		super();
	}

	// Getters y Setters
	public CasoVO getCaso() {
		return caso;
	}
	public void setCaso(CasoVO caso) {
		this.caso = caso;
	}
	public EjercicioVO getEjercicio() {
		return ejercicio;
	}
	public void setEjercicio(EjercicioVO ejercicio) {
		this.ejercicio = ejercicio;
	}
	public Date getFechaBalance() {
		return fechaBalance;
	}
	public void setFechaBalance(Date fechaBalance) {
		this.fechaBalance = fechaBalance;
		this.fechaBalanceView = DateUtil.formatDate(fechaBalance, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaBalanceView() {
		return fechaBalanceView;
	}
	public void setFechaBalanceView(String fechaBalanceView) {
		this.fechaBalanceView = fechaBalanceView;
	}
	public CorridaVO getCorrida() {
		return corrida;
	}
	public void setCorrida(CorridaVO corrida) {
		this.corrida = corrida;
	}
	public String getIdCorridaView() {
		return idCorridaView;
	}
	public void setIdCorridaView(String idCorridaView) {
		this.idCorridaView = idCorridaView;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public ServicioBancoVO getServicioBanco() {
		return servicioBanco;
	}
	public void setServicioBanco(ServicioBancoVO servicioBanco) {
		this.servicioBanco = servicioBanco;
	}
	public String getUsuarioAlta() {
		return usuarioAlta;
	}
	public void setUsuarioAlta(String usuarioAlta) {
		this.usuarioAlta = usuarioAlta;
	}
	public BalanceVO getBalance() {
		return balance;
	}
	public void setBalance(BalanceVO balance) {
		this.balance = balance;
	}

	//	 Buss flags getters y setters
	public Boolean getAdmProcesoBussEnabled() {
		return admProcesoBussEnabled;
	}
	public void setAdmProcesoBussEnabled(Boolean admProcesoBussEnabled) {
		this.admProcesoBussEnabled = admProcesoBussEnabled;
	}
	public String getAdmProcesoEnabled() {
		return this.getAdmProcesoBussEnabled() ? ENABLED : DISABLED;
	}
	
	public boolean isLogDetalladoEnabled() {
		return logDetalladoEnabled;
	}
	public void setLogDetalladoEnabled(boolean logDetalladoEnabled) {
		this.logDetalladoEnabled = logDetalladoEnabled;
	}

}
