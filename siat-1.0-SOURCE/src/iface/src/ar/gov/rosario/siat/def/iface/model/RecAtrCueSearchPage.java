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
 * Search Page de RecAtrCue
 * @author tecso
 *
 */
public class RecAtrCueSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "recGenCueAtrVaSearchPageVO";
	
	private RecAtrCueVO recGenCueAtrVa = new RecAtrCueVO();
	
	private List<RecAtrCueVO> listRecAtrCue = new ArrayList<RecAtrCueVO>();
	
	public RecAtrCueSearchPage(){
		super(DefSecurityConstants.ABM_RECATRCUE);
	}
	
	// Getters y Setters
	public RecAtrCueVO getRecAtrCue(){
		return recGenCueAtrVa;
	}
	public void setRecAtrCue(RecAtrCueVO recGenCueAtrVa){
		this.recGenCueAtrVa = recGenCueAtrVa;
	}
	public List<RecAtrCueVO> getListRecAtrCue(){
		return listRecAtrCue;
	}
	public void setListRecAtrCue(List<RecAtrCueVO> listRecAtrCue){
		this.listRecAtrCue = listRecAtrCue;
	}

}
