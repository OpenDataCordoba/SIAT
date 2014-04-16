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
 * Adapter del PlanForActDeu
 * 
 * @author tecso
 */
public class PlanForActDeuAdapter extends SiatAdapterModel{
	
	public static final String NAME = "planForActDeuAdapterVO";
	
    private PlanForActDeuVO planForActDeu = new PlanForActDeuVO();
    
    private List<SiNo>           listSiNo = new ArrayList<SiNo>();
    
    private Boolean flagEsComun = null;
    
    // Constructores
    public PlanForActDeuAdapter(){
    	super(GdeSecurityConstants.ABM_PLANFORACTDEU);
    }
    
    //  Getters y Setters
	public PlanForActDeuVO getPlanForActDeu() {
		return planForActDeu;
	}

	public void setPlanForActDeu(PlanForActDeuVO planForActDeuVO) {
		this.planForActDeu = planForActDeuVO;
	}

	public List<SiNo> getListSiNo() {
		return listSiNo;
	}

	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}

	
	public Boolean getFlagEsComun() {
		return flagEsComun;
	}

	public void setFlagEsComun(Boolean flagEsComun) {
		this.flagEsComun = flagEsComun;
	}
	
	
	// View getters
}
