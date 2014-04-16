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
 * Adapter de RecClaDeu
 * 
 * @author tecso
 */
public class RecClaDeuAdapter extends SiatAdapterModel {

	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "recClaDeuAdapterVO";
	
	private RecClaDeuVO recClaDeu = new RecClaDeuVO();
	
	private List<SiNo> listSiNo= new ArrayList<SiNo>();
	
	public RecClaDeuAdapter(){
		super(DefSecurityConstants.ABM_RECCLADEU);
	}
	
	// Getters y Setter
	public RecClaDeuVO getRecClaDeu(){
		return recClaDeu;
	}
	public void setRecClaDeu(RecClaDeuVO recClaDeu) {
		this.recClaDeu = recClaDeu;
	}

	public List<SiNo> getListSiNo() {
		return listSiNo;
	}
	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}
	
}
