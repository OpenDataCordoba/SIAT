//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;

/**
 * Search Page de DisParRec
 * @author tecso
 *
 */
public class DisParRecSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "disParRecSearchPageVO";

	private DisParRecVO disParRec = new DisParRecVO();
	
	private List<DisParRecVO> listDisParRec = new ArrayList<DisParRecVO>();

	//Flags
	private boolean tieneAtributo = false;
	
	public DisParRecSearchPage(){
		super(BalSecurityConstants.ABM_DISPARREC);
	}

	// Getters y Setters
	public DisParRecVO getDisParRec() {
		return disParRec;
	}
	public void setDisParRec(DisParRecVO disParRec) {
		this.disParRec = disParRec;
	}
	public List<DisParRecVO> getListDisParRec() {
		return listDisParRec;
	}
	public void setListDisParRec(List<DisParRecVO> listDisParRec) {
		this.listDisParRec = listDisParRec;
	}
	public boolean isTieneAtributo() {
		return tieneAtributo;
	}
	public void setTieneAtributo(boolean tieneAtributo) {
		this.tieneAtributo = tieneAtributo;
	}
	
}
