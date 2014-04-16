//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;

/**
 * Adapter del Reingreso de Indeterminado
 * 
 * @author tecso
 */
public class IndetReingAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "indetReingAdapterVO";
	
    private IndetVO indetReing = new IndetVO();
    
    private IndetVO original = new IndetVO();
     	
    private Boolean paramOriginal = false;
    
    // Constructores
    public IndetReingAdapter(){
    	super(BalSecurityConstants.ABM_INDETREING);
    }
    
    //  Getters y Setters
	public IndetVO getIndetReing() {
		return indetReing;
	}
	public void setIndetReing(IndetVO indetReing) {
		this.indetReing = indetReing;
	}
	public IndetVO getOriginal() {
		return original;
	}
	public void setOriginal(IndetVO original) {
		this.original = original;
	}
	public Boolean getParamOriginal() {
		return paramOriginal;
	}
	public void setParamOriginal(Boolean paramOriginal) {
		this.paramOriginal = paramOriginal;
	}

	public String getName(){
		return NAME;
	}
			
	
}