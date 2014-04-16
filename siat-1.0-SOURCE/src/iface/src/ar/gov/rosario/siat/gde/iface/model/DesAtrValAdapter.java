//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.model.RecAtrValVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;

/**
 * Adapter del DesAtrVal
 * 
 * @author tecso
 */
public class DesAtrValAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "desAtrValAdapterVO";
	
    private DesAtrValVO desAtrVal = new DesAtrValVO();
    
    private List<RecAtrValVO> listRecAtrVal;//TODO ver si son de la clase RecAtrValVO
    
    // Constructores
    public DesAtrValAdapter(){
    	super(GdeSecurityConstants.ABM_DESATRVAL);
    }
    
    //  Getters y Setters
	public DesAtrValVO getDesAtrVal() {
		return desAtrVal;
	}

	public void setDesAtrVal(DesAtrValVO desAtrValVO) {
		this.desAtrVal = desAtrValVO;
	}

	public List<RecAtrValVO> getListRecAtrVal() {
		return listRecAtrVal;
	}

	public void setListRecAtrVal(List<RecAtrValVO> listRecAtrValVO) {
		this.listRecAtrVal = listRecAtrValVO;
	}
	
	// View getters
}
