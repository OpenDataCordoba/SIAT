//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.model.RecAliVO;
import ar.gov.rosario.siat.def.iface.model.RecConADecVO;
import ar.gov.rosario.siat.def.iface.model.RecTipUniVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;

/**
 * Adapter del DecJurDet
 * 
 * @author tecso
 */
public class DecJurDetAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "decJurDetAdapterVO";
	
	private List<RecConADecVO> listRecConADec = new ArrayList<RecConADecVO>();
	
	private List<RecAliVO> listRecAli = new ArrayList<RecAliVO>();
	
	private DecJurDetVO decJurDet = new DecJurDetVO();
    
	private List<RecTipUniVO> listRecTipUni = new ArrayList<RecTipUniVO>();
	
	private List<RecConADecVO> listTipoUnidad = new ArrayList<RecConADecVO>();
	
	private String idFoco="";
	

    // Propiedades para la asignacion de permisos
   
    // Constructores
    public DecJurDetAdapter(){
    	super(GdeSecurityConstants.ABM_DECJUR);
    }



	public List<RecConADecVO> getListRecConADec() {
		return listRecConADec;
	}



	public void setListRecConADec(List<RecConADecVO> listRecConADec) {
		this.listRecConADec = listRecConADec;
	}


	public List<RecAliVO> getListRecAli() {
		return listRecAli;
	}



	public void setListRecAli(List<RecAliVO> listRecAli) {
		this.listRecAli = listRecAli;
	}


	public DecJurDetVO getDecJurDet() {
		return decJurDet;
	}



	public void setDecJurDet(DecJurDetVO decJurDet) {
		this.decJurDet = decJurDet;
	}



	public List<RecTipUniVO> getListRecTipUni() {
		return listRecTipUni;
	}



	public void setListRecTipUni(List<RecTipUniVO> listRecTipUni) {
		this.listRecTipUni = listRecTipUni;
	}



	public List<RecConADecVO> getListTipoUnidad() {
		return listTipoUnidad;
	}



	public void setListTipoUnidad(List<RecConADecVO> listTipoUnidad) {
		this.listTipoUnidad = listTipoUnidad;
	}



	public String getIdFoco() {
		return idFoco;
	}



	public void setIdFoco(String idFoco) {
		this.idFoco = idFoco;
	}




	
	
    
    //  Getters y Setters
		
}