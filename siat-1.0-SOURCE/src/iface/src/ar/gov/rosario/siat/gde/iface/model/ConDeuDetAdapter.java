//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;

/**
 * Adapter del ConDeuDet
 * 
 * @author tecso
 */
public class ConDeuDetAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "conDeuDetAdapterVO";
	
    private ConDeuDetVO conDeuDet = new ConDeuDetVO();
    
    private List<DeudaJudicialVO> listDeuda = new ArrayList<DeudaJudicialVO>();
    
    private String[] idsDeudaSelected;
    
    private String strDomEnv="";
    
    // Constructores
    public ConDeuDetAdapter(){
    	super(GdeSecurityConstants.ABM_CONDEUDET);
    }
    
    //  Getters y Setters
	public ConDeuDetVO getConDeuDet() {
		return conDeuDet;
	}

	public void setConDeuDet(ConDeuDetVO conDeuDetVO) {
		this.conDeuDet = conDeuDetVO;
	}

	public String[] getIdsDeudaSelected() {
		return idsDeudaSelected;
	}

	public void setIdsDeudaSelected(String[] idsDeudaSelected) {
		this.idsDeudaSelected = idsDeudaSelected;
	}

	public List<DeudaJudicialVO> getListDeuda() {
		return listDeuda;
	}

	public void setListDeuda(List<DeudaJudicialVO> listDeuda) {
		this.listDeuda = listDeuda;
	}

	public String getStrDomEnv() {
		return strDomEnv;
	}

	public void setStrDomEnv(String strDesDomEnv) {
		this.strDomEnv = strDesDomEnv;
	}
	
	// View getters
}
