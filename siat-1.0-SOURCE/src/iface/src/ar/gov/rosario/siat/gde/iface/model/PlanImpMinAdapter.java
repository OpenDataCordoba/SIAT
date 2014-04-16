//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;

/**
 * Adapter del PlanImpMin
 * 
 * @author tecso
 */
public class PlanImpMinAdapter extends SiatAdapterModel{
	
	public static final String NAME = "planImpMinAdapterVO";
	
    private PlanImpMinVO planImpMin = new PlanImpMinVO();
    
    // Constructores
    public PlanImpMinAdapter(){
    	super(GdeSecurityConstants.ABM_PLANIMPMIN);
    }
    
    //  Getters y Setters
	public PlanImpMinVO getPlanImpMin() {
		return planImpMin;
	}

	public void setPlanImpMin(PlanImpMinVO planImpMinVO) {
		this.planImpMin = planImpMinVO;
	}

	
	// View getters
}
