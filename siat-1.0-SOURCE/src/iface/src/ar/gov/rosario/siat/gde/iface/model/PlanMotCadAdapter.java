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
 * Adapter del PlanMotCad
 * 
 * @author tecso
 */
public class PlanMotCadAdapter extends SiatAdapterModel{
	
	public static final String NAME = "planMotCadAdapterVO";
	
    private PlanMotCadVO planMotCad = new PlanMotCadVO();
    
    private List<SiNo>           listSiNo = new ArrayList<SiNo>();
    
    private Boolean flagEsEspecial = null;
    
    // Constructores
    public PlanMotCadAdapter(){
    	super(GdeSecurityConstants.ABM_PLANMOTCAD);
    }
    
    //  Getters y Setters
	public PlanMotCadVO getPlanMotCad() {
		return planMotCad;
	}

	public void setPlanMotCad(PlanMotCadVO planMotCadVO) {
		this.planMotCad = planMotCadVO;
	}

	public List<SiNo> getListSiNo() {
		return listSiNo;
	}

	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}

	
	// View getters
	public Boolean getFlagEsEspecial() {
		return flagEsEspecial;
	}
	
	public void setFlagEsEspecial(Boolean flagEsEspecial) {
		this.flagEsEspecial = flagEsEspecial;
	}
	
}
