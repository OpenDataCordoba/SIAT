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
 * Search Page de RecCon
 * @author tecso
 *
 */
public class RecConSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "recConSearchPageVO";
	
	private RecConVO recCon = new RecConVO();
	
	private List<RecConVO> listRecCon = new ArrayList<RecConVO>();
	
	public RecConSearchPage(){
		super(DefSecurityConstants.ABM_RECCON);
	}
	
	// Getters y Setters
	public RecConVO getRecCon(){
		return recCon;
	}
	public void setRecCon(RecConVO recCon){
		this.recCon = recCon;
	}
	public List<RecConVO> getListRecCon(){
		return listRecCon;
	}
	public void setListRecCon(List<RecConVO> listRecCon){
		this.listRecCon = listRecCon;
	}

}
