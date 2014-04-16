//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.model.VencimientoVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter del PlanVen
 * 
 * @author tecso
 */
public class PlanVenAdapter extends SiatAdapterModel{
	
	public static final String NAME = "planVenAdapterVO";
	
    private PlanVenVO planVen = new PlanVenVO();
    
    private List<SiNo>           listSiNo = new ArrayList<SiNo>();
    
    private List<VencimientoVO>	 listVencimiento = new ArrayList<VencimientoVO>();
    
    // Constructores
    public PlanVenAdapter(){
    	super(GdeSecurityConstants.ABM_PLANVEN);
    }
    
    //  Getters y Setters
	public PlanVenVO getPlanVen() {
		return planVen;
	}

	public void setPlanVen(PlanVenVO planVenVO) {
		this.planVen = planVenVO;
	}

	public List<SiNo> getListSiNo() {
		return listSiNo;
	}

	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}

	
	public List<VencimientoVO> getListVencimiento() {
		return listVencimiento;
	}

	public void setListVencimiento(List<VencimientoVO> listVencimiento) {
		this.listVencimiento = listVencimiento;
	}
	
	
	// View getters
}
