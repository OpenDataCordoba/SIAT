//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;


import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;

/**
 * Adapter de RecGenCueAtrVa
 * 
 * @author tecso
 */
public class RecConADecAdapter extends SiatAdapterModel {
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "recConADecAdapterVO";
	
	private RecConADecVO recConADec=new RecConADecVO();
	
	private List<TipRecConADecVO> listTipRecConADec=new ArrayList<TipRecConADecVO>();
	
	private List<RecTipUniVO>listRecTipUni = new ArrayList<RecTipUniVO>();

	private Boolean paramCodigoAfip = false;
	
	public RecConADecAdapter(){
		super(DefSecurityConstants.ABM_RECCONADEC);
	}
	
	//	 Getters y Setters

	public RecConADecVO getRecConADec() {
		return recConADec;
	}

	public void setRecConADec(RecConADecVO recConADec) {
		this.recConADec = recConADec;
	}

	public List<TipRecConADecVO> getListTipRecConADec() {
		return listTipRecConADec;
	}

	public void setListTipRecConADec(List<TipRecConADecVO> listTipRecConADec) {
		this.listTipRecConADec = listTipRecConADec;
	}

	public List<RecTipUniVO> getListRecTipUni() {
		return listRecTipUni;
	}

	public void setListRecTipUni(List<RecTipUniVO> listRecTipUni) {
		this.listRecTipUni = listRecTipUni;
	}

	public Boolean getParamCodigoAfip() {
		return paramCodigoAfip;
	}

	public void setParamCodigoAfip(Boolean paramCodigoAfip) {
		this.paramCodigoAfip = paramCodigoAfip;
	}
	
	

}
