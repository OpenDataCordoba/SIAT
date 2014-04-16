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
 * Adapter del Procurador a Excluir del Envio Judicial 
 * 
 * @author tecso
 */
public class ProMasProExcAdapter extends SiatAdapterModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String NAME = "proMasProExcAdapterVO";
	
    private ProMasProExcVO proMasProExc = new ProMasProExcVO();
    
    private List<ProcuradorVO> listProcurador = new ArrayList<ProcuradorVO>();
    
    // Constructores
    public ProMasProExcAdapter(){    	
    	super(GdeSecurityConstants.ABM_PROMASPROEXC);
    }
    
    //  Getters y Setters

	public ProMasProExcVO getProMasProExc() {
		return proMasProExc;
	}
	public void setProMasProExc(ProMasProExcVO proMasProExc) {
		this.proMasProExc = proMasProExc;
	}
	public List<ProcuradorVO> getListProcurador() {
		return listProcurador;
	}
	public void setListProcurador(List<ProcuradorVO> listProcurador) {
		this.listProcurador = listProcurador;
	}
	
	
	// View getters
}
