//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.model.RecClaDeuVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;

/**
 * Adapter del PlanClaDeu
 * 
 * @author tecso
 */
public class PlanClaDeuAdapter extends SiatAdapterModel{
	
	public static final String NAME = "planClaDeuAdapterVO";
	
    private PlanClaDeuVO planClaDeu = new PlanClaDeuVO();
    
    private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
    
    private List<RecClaDeuVO>	listRecClaDeu = new ArrayList<RecClaDeuVO>();
    
    // Constructores
    public PlanClaDeuAdapter(){
    	super(GdeSecurityConstants.ABM_PLANCLADEU);
    }
    
    //  Getters y Setters
	public PlanClaDeuVO getPlanClaDeu() {
		return planClaDeu;
	}

	public void setPlanClaDeu(PlanClaDeuVO planClaDeuVO) {
		this.planClaDeu = planClaDeuVO;
	}


	public List<RecClaDeuVO> getListRecClaDeu() {
		return listRecClaDeu;
	}

	public void setListRecClaDeu(List<RecClaDeuVO> listRecClaDeu) {
		this.listRecClaDeu = listRecClaDeu;
	}

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}


	// View getters
}
