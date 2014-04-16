//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.model.RecClaDeuVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;

/**
 * Adapter del DesRecClaDeu
 * 
 * @author tecso
 */
public class DesRecClaDeuAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "desRecClaDeuAdapterVO";
	
    private DesRecClaDeuVO desRecClaDeu = new DesRecClaDeuVO();
    
    private List<RecClaDeuVO> listRecClaDeu = new ArrayList<RecClaDeuVO>();
    
    // Constructores
    public DesRecClaDeuAdapter(){
    	super(GdeSecurityConstants.ABM_DESRECCLADEU);
    }
    
    //  Getters y Setters
	public DesRecClaDeuVO getDesRecClaDeu() {
		return desRecClaDeu;
	}

	public void setDesRecClaDeu(DesRecClaDeuVO desRecClaDeuVO) {
		this.desRecClaDeu = desRecClaDeuVO;
	}

	public List<RecClaDeuVO> getListRecClaDeu() {
		return listRecClaDeu;
	}

	public void setListRecClaDeu(List<RecClaDeuVO> listRecClaDeu) {
		this.listRecClaDeu = listRecClaDeu;
	}
	
	// View getters
}
