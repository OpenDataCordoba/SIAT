//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;

/**
 * Adapter del ProRecCom
 * 
 * @author tecso
 */
public class ProRecComAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "proRecComAdapterVO";
	
    private ProRecComVO proRecCom = new ProRecComVO();
    
    // Constructores
    public ProRecComAdapter(){
    	super(GdeSecurityConstants.ABM_PRORECCOM);
    }
    
    //  Getters y Setters
	public ProRecComVO getProRecCom() {
		return proRecCom;
	}

	public void setProRecCom(ProRecComVO proRecComVO) {
		this.proRecCom = proRecComVO;
	}

	// View getters
}
