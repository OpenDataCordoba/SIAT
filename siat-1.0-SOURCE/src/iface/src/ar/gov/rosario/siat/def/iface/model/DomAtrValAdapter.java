//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Adapter de DomAtrVal
 * 
 * @author tecso
 */
public class DomAtrValAdapter extends SiatAdapterModel{
	
	public static final String NAME = "domAtrValAdapterVO";
	
    private DomAtrValVO domAtrVal = new DomAtrValVO();
    
    private List<Estado> listEstado = new ArrayList<Estado>();
    
    public DomAtrValAdapter(){
    	super(DefSecurityConstants.ABM_DOMINIO_ATRIBUTO_VALOR);
    }

	public DomAtrValVO getDomAtrVal() {
		return domAtrVal;
	}

	public void setDomAtrVal(DomAtrValVO domAtrValVO) {
		this.domAtrVal = domAtrValVO;
	}

	public List<Estado> getListEstado() {
		return listEstado;
	}

	public void setListEstado(List<Estado> listEstado) {
		this.listEstado = listEstado;
	}
	
	
	
	
}
