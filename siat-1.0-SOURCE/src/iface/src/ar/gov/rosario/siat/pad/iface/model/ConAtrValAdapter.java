//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;


/**
 * Adapter de Contribuyente
 * 
 * @author tecso
 */
public class ConAtrValAdapter extends SiatAdapterModel{
	
	public static final String NAME = "conAtrValAdapterVO";
	
    private ContribuyenteVO contribuyente = new ContribuyenteVO();
    private ConAtrDefinition conAtrDefinition = new ConAtrDefinition();
    
    // Constructores
    public ConAtrValAdapter(){
    	super(PadSecurityConstants.ABM_CONTRIBUYENTE_ATRIBUTO_VALOR);
    }

    //  Getters y Setters
	public ContribuyenteVO getContribuyente() {
		return contribuyente;
	}

	public void setContribuyente(ContribuyenteVO contribuyente) {
		this.contribuyente = contribuyente;
	}

	public ConAtrDefinition getConAtrDefinition() {
		return conAtrDefinition;
	}

	public void setConAtrDefinition(ConAtrDefinition conAtrDefinition) {
		this.conAtrDefinition = conAtrDefinition;
	}
    
}