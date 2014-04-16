//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

public class LiqComContainer extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;
	
	private List<LiqProContainer> listLiqProContainer = new ArrayList<LiqProContainer>();

	public List<LiqProContainer> getListLiqProContainer() {
		return listLiqProContainer;
	}

	public void setListLiqProContainer(List<LiqProContainer> listLiqProContainer) {
		this.listLiqProContainer = listLiqProContainer;
	}

	
	
	
}
