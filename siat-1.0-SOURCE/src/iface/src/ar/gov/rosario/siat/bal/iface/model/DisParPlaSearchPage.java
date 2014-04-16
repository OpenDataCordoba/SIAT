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
 * Search Page de DisParPla
 * @author tecso
 *
 */
public class DisParPlaSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "disParPlaSearchPageVO";

	private DisParPlaVO disParPla = new DisParPlaVO();
	
	private List<DisParPlaVO> listDisParPla = new ArrayList<DisParPlaVO>();
	
	public DisParPlaSearchPage(){
		super(BalSecurityConstants.ABM_DISPARPLA);
	}

	// Getters y Setters
	public DisParPlaVO getDisParPla() {
		return disParPla;
	}
	public void setDisParPla(DisParPlaVO disParPla) {
		this.disParPla = disParPla;
	}
	public List<DisParPlaVO> getListDisParPla() {
		return listDisParPla;
	}
	public void setListDisParPla(List<DisParPlaVO> listDisParPla) {
		this.listDisParPla = listDisParPla;
	}
	
}
