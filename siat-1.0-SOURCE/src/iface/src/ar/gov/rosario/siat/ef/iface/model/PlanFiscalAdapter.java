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
 * Adapter del PlanFiscal
 * 
 * @author tecso
 */
public class PlanFiscalAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "planFiscalAdapterVO";
	
    private PlanFiscalVO planFiscal = new PlanFiscalVO();
    
    private List<EstadoPlanFisVO> listEstadoPlanFis = new ArrayList<EstadoPlanFisVO>();
    
    // Constructores
    public PlanFiscalAdapter(){
    	super(EfSecurityConstants.ADM_PLANFISCAL);
    }
    
    //  Getters y Setters
	public PlanFiscalVO getPlanFiscal() {
		return planFiscal;
	}

	public void setPlanFiscal(PlanFiscalVO planFiscalVO) {
		this.planFiscal = planFiscalVO;
	}

	public List<EstadoPlanFisVO> getListEstadoPlanFis() {
		return listEstadoPlanFis;
	}

	public void setListEstadoPlanFis(List<EstadoPlanFisVO> listEstadoPlanFis) {
		this.listEstadoPlanFis = listEstadoPlanFis;
	}
	
	// View getters
}
