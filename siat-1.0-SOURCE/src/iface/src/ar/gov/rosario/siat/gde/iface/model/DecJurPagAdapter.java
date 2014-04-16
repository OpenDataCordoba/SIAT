//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;

/**
 * Adapter del DecJurDet
 * 
 * @author tecso
 */
public class DecJurPagAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "decJurPagAdapterVO";
	
	private List<TipPagDecJurVO> listTipPagDecJur = new ArrayList<TipPagDecJurVO>();
	
	private DecJurPagVO decJurPag = new DecJurPagVO();
    
	private List<AgeRetVO> listAgeRet = new ArrayList<AgeRetVO>();

    // Propiedades para la asignacion de permisos
   

	// Constructores
    public DecJurPagAdapter(){
    	super(GdeSecurityConstants.ABM_DECJUR);
    }


    //  Getters y Setters
	public List<TipPagDecJurVO> getListTipPagDecJur() {
		return listTipPagDecJur;
	}



	public void setListTipPagDecJur(List<TipPagDecJurVO> listTipPagDecJur) {
		this.listTipPagDecJur = listTipPagDecJur;
	}



	public DecJurPagVO getDecJurPag() {
		return decJurPag;
	}



	public void setDecJurPag(DecJurPagVO decJurPag) {
		this.decJurPag = decJurPag;
	}

    

    public List<AgeRetVO> getListAgeRet() {
		return listAgeRet;
	}



	public void setListAgeRet(List<AgeRetVO> listAgeRet) {
		this.listAgeRet = listAgeRet;
	}
		
}