//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.pro.iface.model.FileCorridaVO;


/**
 * Adapter de Balance
 * 
 * @author tecso
 */
public class BalanceAdapter extends SiatAdapterModel {
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "balanceAdapterVO";
	
	private BalanceVO balance = new BalanceVO();
	
	private List<EjercicioVO> listEjercicio = new ArrayList<EjercicioVO>();
	
	private List<FileCorridaVO> listFileCorrida = new ArrayList<FileCorridaVO>();
	
	//Flags
	private String paramEstadoEjercicio = "";
	private boolean paramProcesadoOk = false;
	
	public BalanceAdapter(){
		super(BalSecurityConstants.ABM_BALANCE);
	}

	// Getters y Setters
	public BalanceVO getBalance() {
		return balance;
	}
	public void setBalance(BalanceVO balance) {
		this.balance = balance;
	}
	public List<EjercicioVO> getListEjercicio() {
		return listEjercicio;
	}
	public void setListEjercicio(List<EjercicioVO> listEjercicio) {
		this.listEjercicio = listEjercicio;
	}
	public List<FileCorridaVO> getListFileCorrida() {
		return listFileCorrida;
	}
	public void setListFileCorrida(List<FileCorridaVO> listFileCorrida) {
		this.listFileCorrida = listFileCorrida;
	}
	
	// Flags
	public boolean isParamProcesadoOk() {
		return paramProcesadoOk;
	}
	public void setParamProcesadoOk(boolean paramProcesadoOk) {
		this.paramProcesadoOk = paramProcesadoOk;
	}
	public String getParamEstadoEjercicio() {
		return paramEstadoEjercicio;
	}
	public void setParamEstadoEjercicio(String paramEstadoEjercicio) {
		this.paramEstadoEjercicio = paramEstadoEjercicio;
	}
}
