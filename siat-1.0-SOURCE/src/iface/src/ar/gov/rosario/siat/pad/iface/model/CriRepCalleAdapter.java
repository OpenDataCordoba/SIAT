//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.model.ZonaVO;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;

/**
 * Adapter de CriRepCalle
 * 
 * @author tecso
 */
public class CriRepCalleAdapter extends SiatAdapterModel {

	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "criRepCalleAdapterVO";
	
	private CriRepCalleVO criRepCalle = new CriRepCalleVO();
	private List<ZonaVO> listZona = new ArrayList<ZonaVO>();
	
	public CriRepCalleAdapter(){
		super(PadSecurityConstants.ABM_CRIREPCALLE);
	}

	// Getters y Setters
	
	public CriRepCalleVO getCriRepCalle() {
		return criRepCalle;
	}
	public void setCriRepCalle(CriRepCalleVO criRepCalle) {
		this.criRepCalle = criRepCalle;
	}
	public List<ZonaVO> getListZona() {
		return listZona;
	}
	public void setListZona(List<ZonaVO> listZona) {
		this.listZona = listZona;
	}
	
}
