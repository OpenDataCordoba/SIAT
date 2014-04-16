//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.util.QryTableDataType;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter del ColEmiMat
 * 
 * @author tecso
 */
public class ColEmiMatAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "colEmiMatAdapterVO";
	
    private ColEmiMatVO colEmiMat = new ColEmiMatVO();
    
    private List<QryTableDataType> listEmiMatTipoDato = new ArrayList<QryTableDataType>();
    
    private List<SiNo> listSiNo = new ArrayList<SiNo>();
    
    // Constructores
    public ColEmiMatAdapter(){
    	super(DefSecurityConstants.ABM_EMIMAT);
    }

    //  Getters y Setters
	public ColEmiMatVO getColEmiMat() {
		return colEmiMat;
	}

	public void setColEmiMat(ColEmiMatVO colEmiMat) {
		this.colEmiMat = colEmiMat;
	}
	
	public List<QryTableDataType> getListEmiMatTipoDato() {
		return listEmiMatTipoDato;
	}

	public void setListEmiMatTipoDato(List<QryTableDataType> listEmiMatTipoDato) {
		this.listEmiMatTipoDato = listEmiMatTipoDato;
	}

	public List<SiNo> getListSiNo() {
		return listSiNo;
	}

	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}

}
