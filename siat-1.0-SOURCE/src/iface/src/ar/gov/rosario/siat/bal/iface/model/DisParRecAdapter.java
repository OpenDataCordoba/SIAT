//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.model.DomAtrValVO;
import ar.gov.rosario.siat.def.iface.model.GenericAtrDefinition;
import ar.gov.rosario.siat.def.iface.model.ViaDeudaVO;

/**
 * Adapter de DisParRec
 * 
 * @author tecso
 */
public class DisParRecAdapter extends SiatAdapterModel {

	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "disParRecAdapterVO";

	private DisParRecVO disParRec = new DisParRecVO();
    private GenericAtrDefinition genericAtrDefinition = new GenericAtrDefinition();
    
	private List<ViaDeudaVO> listViaDeuda = new ArrayList<ViaDeudaVO>();
	private List<DomAtrValVO> listDomAtrVal = new ArrayList<DomAtrValVO>();

	//Flags
	private boolean tieneAtributo = false;
	private boolean recNoTrib = false;
	
	public DisParRecAdapter(){
		super(BalSecurityConstants.ABM_DISPARREC);
	}

	// Getters y Setters
	public DisParRecVO getDisParRec() {
		return disParRec;
	}
	public void setDisParRec(DisParRecVO disParRec) {
		this.disParRec = disParRec;
	}
	public List<DomAtrValVO> getListDomAtrVal() {
		return listDomAtrVal;
	}
	public void setListDomAtrVal(List<DomAtrValVO> listDomAtrVal) {
		this.listDomAtrVal = listDomAtrVal;
	}
	public List<ViaDeudaVO> getListViaDeuda() {
		return listViaDeuda;
	}
	public void setListViaDeuda(List<ViaDeudaVO> listViaDeuda) {
		this.listViaDeuda = listViaDeuda;
	}
	public GenericAtrDefinition getGenericAtrDefinition() {
		return genericAtrDefinition;
	}
	public void setGenericAtrDefinition(GenericAtrDefinition genericAtrDefinition) {
		this.genericAtrDefinition = genericAtrDefinition;
	}
	public boolean isTieneAtributo() {
		return tieneAtributo;
	}
	public void setTieneAtributo(boolean tieneAtributo) {
		this.tieneAtributo = tieneAtributo;
	}
	public boolean isRecNoTrib() {
		return recNoTrib;
	}
	public void setRecNoTrib(boolean recNoTrib) {
		this.recNoTrib = recNoTrib;
	}
	
}
