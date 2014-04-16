//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.rec.iface.util.RecSecurityConstants;

/**
 * Adapter del UsoCdM
 * 
 * @author tecso
 */
public class UsoCdMAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;	
	
	public static final String NAME = "usoCdMAdapterVO";
	
    private UsoCdMVO usoCdM = new UsoCdMVO();
    
    // Constructores
    public UsoCdMAdapter(){
    	super(RecSecurityConstants.ABM_USOCDM);
    }
    
    //  Getters y Setters
	public UsoCdMVO getUsoCdM() {
		return usoCdM;
	}

	public void setUsoCdM(UsoCdMVO usoCdMVO) {
		this.usoCdM = usoCdMVO;
	}

}
