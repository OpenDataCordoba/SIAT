//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.cyq.iface.util.CyqSecurityConstants;

/**
 * Adapter del ProCueNoDeu
 * 
 * @author tecso
 */
public class ProCueNoDeuAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "proCueNoDeuAdapterVO";
	
    private ProCueNoDeuVO proCueNoDeu = new ProCueNoDeuVO();
    
    // Constructores
    public ProCueNoDeuAdapter(){
    	super(CyqSecurityConstants.ABM_PROCUENODEU);
    }
    
    //  Getters y Setters
	public ProCueNoDeuVO getProCueNoDeu() {
		return proCueNoDeu;
	}

	public void setProCueNoDeu(ProCueNoDeuVO proCueNoDeuVO) {
		this.proCueNoDeu = proCueNoDeuVO;
	}

	// View getters
}
