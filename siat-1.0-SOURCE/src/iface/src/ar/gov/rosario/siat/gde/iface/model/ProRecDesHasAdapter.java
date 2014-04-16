//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;

/**
 * Adapter del ProRecDesHas
 * 
 * @author tecso
 */
public class ProRecDesHasAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "proRecDesHasAdapterVO";
	
    private ProRecDesHasVO proRecDesHas = new ProRecDesHasVO();
    
    // Constructores
    public ProRecDesHasAdapter(){
    	super(GdeSecurityConstants.ABM_PRORECDESHAS);
    }
    
    //  Getters y Setters
	public ProRecDesHasVO getProRecDesHas() {
		return proRecDesHas;
	}

	public void setProRecDesHas(ProRecDesHasVO proRecDesHasVO) {
		this.proRecDesHas = proRecDesHasVO;
	}
}
