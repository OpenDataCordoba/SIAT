//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter del Investigador
 * 
 * @author tecso
 */
public class InvestigadorAdapter extends SiatAdapterModel{
	
	public static final String NAME = "investigadorAdapterVO";
	
    private InvestigadorVO investigador = new InvestigadorVO();
    
    private List<SiNo>           listSiNo = new ArrayList<SiNo>();
    
    // Constructores
    public InvestigadorAdapter(){
    	super(EfSecurityConstants.ABM_INVESTIGADOR);
    }
    
    //  Getters y Setters
	public InvestigadorVO getInvestigador() {
		return investigador;
	}

	public void setInvestigador(InvestigadorVO investigadorVO) {
		this.investigador = investigadorVO;
	}

	public List<SiNo> getListSiNo() {
		return listSiNo;
	}

	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}
	
	// View getters
}
