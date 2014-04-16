//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;

/**
 * SearchPage del ProPreDeuDet
 * 
 * @author Tecso
 *
 */
public class ProPreDeuDetSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "proPreDeuDetSearchPageVO";
	
	private ProPreDeuDetVO proPreDeuDet= new ProPreDeuDetVO();
	
	// id seleccionados, usados para seleccion multiple
	private String[] listId;

	// Constructores
	public ProPreDeuDetSearchPage() {       
       super(GdeSecurityConstants.ABM_PROPREDEU);        
    }
	
	// Getters y Setters
	public ProPreDeuDetVO getProPreDeuDet() {
		return proPreDeuDet;
	}
	public void setProPreDeuDet(ProPreDeuDetVO proPreDeuDet) {
		this.proPreDeuDet = proPreDeuDet;
	}

	public String[] getListId() {
		return listId;
	}

	public void setListId(String[] listId) {
		this.listId = listId;
	}
	
}
