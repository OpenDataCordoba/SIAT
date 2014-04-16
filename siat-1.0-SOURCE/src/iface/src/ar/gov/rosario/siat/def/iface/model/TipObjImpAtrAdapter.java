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
 * Adapter de TipObjImpAtr
 * 
 * @author tecso
 */
public class TipObjImpAtrAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
		
	public static final String NAME = "tipObjImpAtrAdapterVO";
	
    private TipObjImpAtrVO tipObjImpAtr = new TipObjImpAtrVO();
    
    private List<SiNo> listSiNo     = new ArrayList<SiNo>();
    
    
    // Constructores
    public TipObjImpAtrAdapter(){
    	super(DefSecurityConstants.ABM_TIPO_OBJETO_IMPONIBLE_ATRIBUTO);
    }
    
    public TipObjImpAtrAdapter(TipObjImpVO tipObjImp ) {
		this();
		this.tipObjImpAtr = new TipObjImpAtrVO(tipObjImp);
	}
	
	// Getters y Setters
	public TipObjImpAtrVO getTipObjImpAtr() {
		return tipObjImpAtr;
	}
	public void setTipObjImpAtr(TipObjImpAtrVO tipObjImpAtrVO) {
		this.tipObjImpAtr = tipObjImpAtrVO;
	}
	public List<SiNo> getListSiNo() {
		return listSiNo;
	}
	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}
	
	public SiNo getSi(){ 
		return SiNo.SI;
	}
}
