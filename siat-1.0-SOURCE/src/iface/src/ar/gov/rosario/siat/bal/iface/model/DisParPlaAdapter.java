//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.model.DomAtrValVO;
import ar.gov.rosario.siat.def.iface.model.GenericAtrDefinition;
import ar.gov.rosario.siat.gde.iface.model.PlanVO;

/**
 * Adapter de DisParPla
 * 
 * @author tecso
 */
public class DisParPlaAdapter extends SiatAdapterModel {

	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "disParPlaAdapterVO";

	private DisParPlaVO disParPla = new DisParPlaVO();
    private GenericAtrDefinition genericAtrDefinition = new GenericAtrDefinition();
	
	private List<PlanVO> listPlan = new ArrayList<PlanVO>();
	private List<DomAtrValVO> listDomAtrVal = new ArrayList<DomAtrValVO>();

	// Flags
	private boolean paramPlan = false;
	
	public DisParPlaAdapter(){
		super(BalSecurityConstants.ABM_DISPARPLA);
	}

	// Getters y Setters
	public DisParPlaVO getDisParPla() {
		return disParPla;
	}
	public void setDisParPla(DisParPlaVO disParPla) {
		this.disParPla = disParPla;
	}
	public List<DomAtrValVO> getListDomAtrVal() {
		return listDomAtrVal;
	}
	public void setListDomAtrVal(List<DomAtrValVO> listDomAtrVal) {
		this.listDomAtrVal = listDomAtrVal;
	}
	public List<PlanVO> getListPlan() {
		return listPlan;
	}
	public void setListPlan(List<PlanVO> listPlan) {
		this.listPlan = listPlan;
	}
	public GenericAtrDefinition getGenericAtrDefinition() {
		return genericAtrDefinition;
	}
	public void setGenericAtrDefinition(GenericAtrDefinition genericAtrDefinition) {
		this.genericAtrDefinition = genericAtrDefinition;
	}
	public boolean isParamPlan() {
		return paramPlan;
	}
	public void setParamPlan(boolean paramPlan) {
		this.paramPlan = paramPlan;
	}
	
}
