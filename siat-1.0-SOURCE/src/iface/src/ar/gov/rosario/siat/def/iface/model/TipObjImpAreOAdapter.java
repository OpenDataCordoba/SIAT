//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;

/**
 * Adapter de TipObjImpAtr
 * 
 * @author tecso
 */
public class TipObjImpAreOAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
		
	public static final String NAME = "tipObjImpAreOAdapterVO";
	
    private TipObjImpAreOVO tipObjImpAreO = new TipObjImpAreOVO();
    
    private List<AreaVO> listAreaOrigen  = new ArrayList<AreaVO>();
    
    // Constructores
    public TipObjImpAreOAdapter(){
    	super("");
    }

	// Getters y Setters    
	public List<AreaVO> getListAreaOrigen() {
		return listAreaOrigen;
	}
	public void setListAreaOrigen(List<AreaVO> listAreaOrigen) {
		this.listAreaOrigen = listAreaOrigen;
	}
	public TipObjImpAreOVO getTipObjImpAreO() {
		return tipObjImpAreO;
	}
	public void setTipObjImpAreO(TipObjImpAreOVO tipObjImpAreO) {
		this.tipObjImpAreO = tipObjImpAreO;
	}
	
}
