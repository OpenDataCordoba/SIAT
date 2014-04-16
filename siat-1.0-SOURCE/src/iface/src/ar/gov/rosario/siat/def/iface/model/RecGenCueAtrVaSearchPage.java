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
 * Search Page de RecGenCueAtrVa
 * @author tecso
 *
 */
public class RecGenCueAtrVaSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "recGenCueAtrVaSearchPageVO";
	
	private RecGenCueAtrVaVO recGenCueAtrVa = new RecGenCueAtrVaVO();
	
	private List<RecGenCueAtrVaVO> listRecGenCueAtrVa = new ArrayList<RecGenCueAtrVaVO>();
	
	public RecGenCueAtrVaSearchPage(){
		super(DefSecurityConstants.ABM_RECGENCUEATRVA);
	}
	
	// Getters y Setters
	public RecGenCueAtrVaVO getRecGenCueAtrVa(){
		return recGenCueAtrVa;
	}
	public void setRecGenCueAtrVa(RecGenCueAtrVaVO recGenCueAtrVa){
		this.recGenCueAtrVa = recGenCueAtrVa;
	}
	public List<RecGenCueAtrVaVO> getListRecGenCueAtrVa(){
		return listRecGenCueAtrVa;
	}
	public void setListRecGenCueAtrVa(List<RecGenCueAtrVaVO> listRecGenCueAtrVa){
		this.listRecGenCueAtrVa = listRecGenCueAtrVa;
	}

}
