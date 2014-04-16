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
 * Adapter de Atributo
 * 
 * @author tecso
 */
public class AtributoAdapter extends SiatAdapterModel{
	
	public static final String NAME = "atributoAdapterVO";
	
    private AtributoVO atributo = new AtributoVO();
    
    private List<TipoAtributoVO> listTipoAtributo = new ArrayList<TipoAtributoVO>(); 
    private List<DomAtrVO>       listDomAtr = new ArrayList<DomAtrVO>();
    
    
    // Constructores
    public AtributoAdapter(){
    	super(DefSecurityConstants.ABM_ATRIBUTO);
    }
    
    //  Getters y Setters
	public AtributoVO getAtributo() {
		return atributo;
	}

	public void setAtributo(AtributoVO atributoVO) {
		this.atributo = atributoVO;
	}

	public List<DomAtrVO> getListDomAtr() {
		return listDomAtr;
	}

	public void setListDomAtr(List<DomAtrVO> listDomAtr) {
		this.listDomAtr = listDomAtr;
	}

	public List<TipoAtributoVO> getListTipoAtributo() {
		return listTipoAtributo;
	}

	public void setListTipoAtributo(List<TipoAtributoVO> listTipoAtributo) {
		this.listTipoAtributo = listTipoAtributo;
	}

}