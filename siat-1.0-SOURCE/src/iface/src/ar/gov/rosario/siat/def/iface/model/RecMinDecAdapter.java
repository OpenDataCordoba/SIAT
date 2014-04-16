//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;


import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;

/**
 * Adapter de RecGenCueAtrVa
 * 
 * @author tecso
 */
public class RecMinDecAdapter extends SiatAdapterModel {
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "recMinDecAdapterVO";
	
	private RecMinDecVO recMinDec=new RecMinDecVO();


	public RecMinDecAdapter(){
		super(DefSecurityConstants.ABM_RECMINDEC);
	}


	//	 Getters y Setters

	

	public RecMinDecVO getRecMinDec() {
		return recMinDec;
	}


	public void setRecMinDec(RecMinDecVO recMinDec) {
		this.recMinDec = recMinDec;
	}
	

}
