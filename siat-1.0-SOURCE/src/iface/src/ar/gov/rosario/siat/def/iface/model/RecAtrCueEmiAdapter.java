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
 * Adapter de RecAtrCueEmi
 * 
 * @author tecso
 */
public class RecAtrCueEmiAdapter extends SiatAdapterModel {
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "recAtrCueEmiAdapterVO";
	
	private RecAtrCueEmiVO recAtrCueEmi = new RecAtrCueEmiVO();

	private List<SiNo> listSiNo = new ArrayList<SiNo>();
	
	public RecAtrCueEmiAdapter(){
		super(DefSecurityConstants.ABM_RECATRCUEEMI);
	}
	
	// Getters y Setter
	public RecAtrCueEmiVO getRecAtrCueEmi(){
		return recAtrCueEmi;
	}

	public void setRecAtrCueEmi(RecAtrCueEmiVO recAtrCueEmi) {
		this.recAtrCueEmi = recAtrCueEmi;
	}

	public List<SiNo> getListSiNo() {
		return listSiNo;
	}

	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}
}
