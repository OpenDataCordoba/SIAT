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
 * Search Page de RecAtrVal
 * @author tecso
 *
 */
public class RecAtrValSearchPage extends SiatPageModel {
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "recAtrValSearchPageVO";
	
	private RecAtrValVO recAtrVal = new RecAtrValVO();
	
	private List<RecAtrValVO> listRecAtrVal = new ArrayList<RecAtrValVO>();
	
	public RecAtrValSearchPage(){
		super(DefSecurityConstants.ABM_RECATRVAL);
	}
	
	// Getters y Setters
	public RecAtrValVO getRecAtrVal(){
		return recAtrVal;
	}
	public void setRecAtrVal(RecAtrValVO recAtrVal){
		this.recAtrVal = recAtrVal;
	}
	public List<RecAtrValVO> getListRecAtrVal(){
		return listRecAtrVal;
	}
	public void setListRecAtrVal(List<RecAtrValVO> listRecAtrVal){
		this.listRecAtrVal = listRecAtrVal;
	}
	
}
