//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;

/**
 * Adapter del OpeInv
 * 
 * @author tecso
 */
public class OpeInvAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "opeInvAdapterVO";
	
    private OpeInvVO opeInv = new OpeInvVO();
    
	private List<PlanFiscalVO> listPlanFiscal = new ArrayList<PlanFiscalVO>();
    
    // Constructores
    public OpeInvAdapter(){
    	super(EfSecurityConstants.ABM_OPEINV);
    }
    
    //  Getters y Setters
	public OpeInvVO getOpeInv() {
		return opeInv;
	}

	public void setOpeInv(OpeInvVO opeInvVO) {
		this.opeInv = opeInvVO;
	}

	public List<PlanFiscalVO> getListPlanFiscal() {
		return listPlanFiscal;
	}

	public void setListPlanFiscal(List<PlanFiscalVO> listPlan) {
		this.listPlanFiscal = listPlan;
	}


	
	// View getters
}
