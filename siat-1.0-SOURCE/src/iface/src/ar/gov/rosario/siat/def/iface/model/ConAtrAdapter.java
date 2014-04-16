//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter de Atributos de Contribuyente
 * 
 * @author tecso
 */
public class ConAtrAdapter extends SiatAdapterModel{

	private static final long serialVersionUID = 1L;

	public static final String NAME = "conAtrAdapterVO";

    private ConAtrVO conAtr = new ConAtrVO(); 
    private GenericAtrDefinition genericAtrDefinition = new GenericAtrDefinition();
    
    private List<SiNo> listSiNo = new ArrayList<SiNo>();

  
    // Constructores
    public ConAtrAdapter(){
    	super(DefSecurityConstants.ABM_CONTRIBUYENTE_ATRIBUTO);
    }

    //  Getters y Setters
	public ConAtrVO getConAtr() {
		return conAtr;
	}
	public void setConAtr(ConAtrVO conAtr) {
		this.conAtr = conAtr;
	}
	public List<SiNo> getListSiNo() {
		return listSiNo;
	}
	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}
	public GenericAtrDefinition getGenericAtrDefinition() {
		return genericAtrDefinition;
	}
	public void setGenericAtrDefinition(GenericAtrDefinition genericAtrDefinition) {
		this.genericAtrDefinition = genericAtrDefinition;
	}
	    
}