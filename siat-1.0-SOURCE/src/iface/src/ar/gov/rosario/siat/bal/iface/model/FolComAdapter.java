//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;

/**
 * Adapter de Compensaciones de Tesoreria
 * 
 * @author tecso
 */
public class FolComAdapter extends SiatAdapterModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "folComAdapterVO";
	
	private FolComVO folCom = new FolComVO();

	public FolComAdapter(){
		super(BalSecurityConstants.ABM_FOLCOM);
	}

	// Getters Y Setters
	public FolComVO getFolCom() {
		return folCom;
	}
	public void setFolCom(FolComVO folCom) {
		this.folCom = folCom;
	}
	
}
