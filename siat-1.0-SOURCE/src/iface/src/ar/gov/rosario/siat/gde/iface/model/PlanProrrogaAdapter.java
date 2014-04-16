//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter del PlanProrroga
 * 
 * @author tecso
 */
public class PlanProrrogaAdapter extends SiatAdapterModel{
	
	public static final String NAME = "planProrrogaAdapterVO";
	
    private PlanProrrogaVO planProrroga = new PlanProrrogaVO();
    
    private List<SiNo>           listSiNo = new ArrayList<SiNo>();
    
    // Constructores
    public PlanProrrogaAdapter(){
    	super(GdeSecurityConstants.ADM_PLANPRORROGA);
    }
    
    //  Getters y Setters
	public PlanProrrogaVO getPlanProrroga() {
		return planProrroga;
	}

	public void setPlanProrroga(PlanProrrogaVO planProrrogaVO) {
		this.planProrroga = planProrrogaVO;
	}

	public List<SiNo> getListSiNo() {
		return listSiNo;
	}

	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}
	
	// View getters
}
