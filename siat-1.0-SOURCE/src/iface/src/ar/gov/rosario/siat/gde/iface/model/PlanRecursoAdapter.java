//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;

/**
 * Adapter del PlanDescuento
 * 
 * @author tecso
 */
public class PlanRecursoAdapter extends SiatAdapterModel{
	
	public static final String NAME = "planRecursoAdapterVO";
	
    private PlanRecursoVO planRecurso = new PlanRecursoVO();
    
    private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
    
    private Boolean esEditable=true;
    
    // Constructores
    public PlanRecursoAdapter(){
    	super(GdeSecurityConstants.ABM_PLANRECURSO);
    }
    
    //  Getters y Setters
	public PlanRecursoVO getPlanRecurso() {
		return planRecurso;
	}

	public void setPlanRecurso(PlanRecursoVO planRecursoVO) {
		this.planRecurso = planRecursoVO;
	}


	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	public Boolean getEsEditable() {
		return esEditable;
	}

	public void setEsEditable(Boolean esEditable) {
		this.esEditable = esEditable;
	}
	
	// View getters
}
