//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.model.AtributoVO;
import ar.gov.rosario.siat.def.iface.model.GenericAtrDefinition;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter del PlanAtrVal
 * 
 * @author tecso
 */
public class PlanAtrValAdapter extends SiatAdapterModel{
	
	public static final String NAME = "planAtrValAdapterVO";
	
    private PlanAtrValVO planAtrVal = new PlanAtrValVO();
    
    private List<SiNo>           listSiNo = new ArrayList<SiNo>();
    private GenericAtrDefinition genericAtrDefinition = new GenericAtrDefinition();
    private List<AtributoVO> listAtributo = new ArrayList<AtributoVO>();     
    
    private Boolean poseeAtributo = false; 
    
    // Constructores
    public PlanAtrValAdapter(){
    	super(GdeSecurityConstants.ABM_PLANATRVAL);
    }
    
    //  Getters y Setters
	public PlanAtrValVO getPlanAtrVal() {
		return planAtrVal;
	}

	public void setPlanAtrVal(PlanAtrValVO planAtrValVO) {
		this.planAtrVal = planAtrValVO;
	}

	public List<SiNo> getListSiNo() {
		return listSiNo;
	}

	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}

	
	public GenericAtrDefinition getGenericAtrDefinition() {
		return genericAtrDefinition;
	}

	public void setGenericAtrDefinition(GenericAtrDefinition genericAtrDefinition) {
		this.genericAtrDefinition = genericAtrDefinition;
	}

	public List<AtributoVO> getListAtributo() {
		return listAtributo;
	}

	public void setListAtributo(List<AtributoVO> listAtributo) {
		this.listAtributo = listAtributo;
	}

	
	
	public Boolean getPoseeAtributo() {
		return poseeAtributo;
	}

	public void setPoseeAtributo(Boolean poseeAtributo) {
		this.poseeAtributo = poseeAtributo;
	}
	
	
	// View getters
}
