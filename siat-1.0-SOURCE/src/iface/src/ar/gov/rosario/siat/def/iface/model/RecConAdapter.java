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
 * Adapter de RecCon
 * 
 * @author tecso
 */
public class RecConAdapter extends SiatAdapterModel {
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "recConAdapterVO";
	
	private RecConVO recCon = new RecConVO();

	private List<SiNo> listSiNo= new ArrayList<SiNo>();
	
	public RecConAdapter(){
		super(DefSecurityConstants.ABM_RECCON);
	}
	
	// Getters y Setter
	public RecConVO getRecCon(){
		return recCon;
	}
	public void setRecCon(RecConVO recCon) {
		this.recCon = recCon;
	}

	public List<SiNo> getListSiNo() {
		return listSiNo;
	}
	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}
	
}
