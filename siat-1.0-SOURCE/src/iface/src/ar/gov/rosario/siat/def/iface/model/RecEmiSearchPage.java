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
 * Search Page de RecEmi
 * @author tecso
 *
 */
public class RecEmiSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "recEmiSearchPageVO";
	
	private RecEmiVO recEmi = new RecEmiVO();
	
	private List<RecEmiVO> listRecEmi = new ArrayList<RecEmiVO>();
	
	public RecEmiSearchPage(){
		super(DefSecurityConstants.ABM_RECEMI);
	}
	
	// Getters y Setters
	public RecEmiVO getRecEmi(){
		return recEmi;
	}
	public void setRecEmi(RecEmiVO recEmi){
		this.recEmi = recEmi;
	}
	public List<RecEmiVO> getListRecEmi(){
		return listRecEmi;
	}
	public void setListRecEmi(List<RecEmiVO> listRecEmi){
		this.listRecEmi = listRecEmi;
	}
	
}
