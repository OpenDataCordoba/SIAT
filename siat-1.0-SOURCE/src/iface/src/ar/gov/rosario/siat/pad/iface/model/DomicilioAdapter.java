//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter del Domicilio
 * 
 * @author tecso
 */
public class DomicilioAdapter extends SiatAdapterModel{
	
	public static final String NAME = "domicilioAdapterVO";
	
    private DomicilioVO domicilio = new DomicilioVO();
    
    private List<SiNo>   listSiNo = new ArrayList<SiNo>();
    
    // Constructores
    public DomicilioAdapter(){
    	super(PadSecurityConstants.ABM_DOMICILIO);
    }
    
    public DomicilioAdapter(DomicilioVO domicilioVO){
    	this();
    	this.domicilio = domicilioVO;
    }
    
    //  Getters y Setters
	public DomicilioVO getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(DomicilioVO domicilioVO) {
		this.domicilio = domicilioVO;
	}

	public List<SiNo> getListSiNo() {
		return listSiNo;
	}

	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}


}
