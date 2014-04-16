//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.model.SeccionVO;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;

/**
 * Adapter de CriRepCat
 * 
 * @author tecso
 */
public class CriRepCatAdapter extends SiatAdapterModel {

	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "criRepCatAdapterVO";
	
	private CriRepCatVO criRepCat = new CriRepCatVO();
	private List<SeccionVO> listSeccion = new ArrayList<SeccionVO>();
	
	public CriRepCatAdapter(){
		super(PadSecurityConstants.ABM_CRIREPCAT);
	}

	// Getters y Setter
	
	public CriRepCatVO getCriRepCat() {
		return criRepCat;
	}
	public void setCriRepCat(CriRepCatVO criRepCat) {
		this.criRepCat = criRepCat;
	}
	public List<SeccionVO> getListSeccion() {
		return listSeccion;
	}
	public void setListSeccion(List<SeccionVO> listSeccion) {
		this.listSeccion = listSeccion;
	}
	
	
}
