//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;

/**
* SearchPage del TranBal
* 
* @author Tecso
*
*/
public class TranBalSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tranBalSearchPageVO";
	
	private TranBalVO tranBal= new TranBalVO();
	
	// Constructores
	public TranBalSearchPage() {       
       super(BalSecurityConstants.ABM_TRANBAL);        
    }
	
	// Getters y Setters
	public TranBalVO getTranBal() {
		return tranBal;
	}
	public void setTranBal(TranBalVO tranBal) {
		this.tranBal = tranBal;
	}

	public String getName(){
		return NAME;
	}
	

}
