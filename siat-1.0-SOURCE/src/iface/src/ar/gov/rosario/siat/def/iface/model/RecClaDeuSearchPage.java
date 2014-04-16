//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;

/**
 * Search Page de RecClaDeu
 * @author tecso
 *
 */
public class RecClaDeuSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "recClaDeuSearchPageVO";
	
	private RecClaDeuVO recClaDeu = new RecClaDeuVO();
	
	private List<RecClaDeuVO> listRecClaDeu = new ArrayList<RecClaDeuVO>();
	
	public RecClaDeuSearchPage(){
		super(DefSecurityConstants.ABM_RECCLADEU);
	}
	
	// Getters y Setters
	public RecClaDeuVO getRecClaDeu(){
		return recClaDeu;
	}
	public void setRecClaDeu(RecClaDeuVO recClaDeu){
		this.recClaDeu = recClaDeu;
	}
	public List<RecClaDeuVO> getListRecClaDeu(){
		return listRecClaDeu;
	}
	public void setListRecClaDeu(List<RecClaDeuVO> listRecClaDeu){
		this.listRecClaDeu = listRecClaDeu;
	}
	
}
