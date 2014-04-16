//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;

/**
 * Adapter de RecAtrCue
 * 
 * @author tecso
 */
public class RecAtrCueVAdapter extends SiatAdapterModel {

	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "recAtrCueVAdapterVO";
	
	private CuentaVO cuenta = new CuentaVO();

	private RecAtrCueDefinition recAtrCueDefinition = new RecAtrCueDefinition();
		
	
	public RecAtrCueVAdapter(){
		super(PadSecurityConstants.ABM_RECATRCUEV);
	}


	// Getters y Setter
	public CuentaVO getCuenta() {
		return cuenta;
	}
	public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}

	public RecAtrCueDefinition getRecAtrCueDefinition() {
		return recAtrCueDefinition;
	}
	public void setRecAtrCueDefinition(RecAtrCueDefinition recAtrCueDefinition) {
		this.recAtrCueDefinition = recAtrCueDefinition;
	}
	

	
}
