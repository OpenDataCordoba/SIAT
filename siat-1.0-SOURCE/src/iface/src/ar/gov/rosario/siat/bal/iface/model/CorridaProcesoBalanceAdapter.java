//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;

/**
 * Adapter de Corridas del Balance sin uso de ADP
 * 
 * @author tecso
 */
public class CorridaProcesoBalanceAdapter extends SiatAdapterModel {

	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "corridaProcesoBalanceAdapterVO";
	
	private BalanceVO balance = new BalanceVO(); 
 
	// Constructores
	public CorridaProcesoBalanceAdapter() {       
    }

	// Getters y Setters
	public BalanceVO getBalance() {
		return balance;
	}
	public void setBalance(BalanceVO balance) {
		this.balance = balance;
	}

}
