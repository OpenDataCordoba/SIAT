//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;

/**
 * Adapter del 
 * 
 * @author tecso
 */
public class ConRecNoLiqAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "AdapterVO";
	
	String[] idsSelected;
	boolean resultTipoRecibo;
	
    // Constructores
    public ConRecNoLiqAdapter(){
    	super(GdeSecurityConstants.CONSULTAR_CONRECNOLIQ);
    }

    //  Getters y Setters
	public String[] getIdsSelected() {
		return idsSelected;
	}

	public void setIdsSelected(String[] idsSelected) {
		this.idsSelected = idsSelected;
	}

	public boolean getResultTipoRecibo() {
		return resultTipoRecibo;
	}

	public void setResultTipoRecibo(boolean resultTipoRecibo) {
		this.resultTipoRecibo = resultTipoRecibo;
	}
    
	
	// View getters
}
