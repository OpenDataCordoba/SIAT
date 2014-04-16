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
 * Adapter de ValUnRecConADeAdapter
 * 
 * @author tecso
 */
public class ValUnRecConADeAdapter extends SiatAdapterModel {
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "valUnRecConADeAdapterVO";
	
	private ValUnRecConADeVO valUnRecConADe=new ValUnRecConADeVO();
	
	private List<RecAliVO>listRecAli=new ArrayList<RecAliVO>();
	

	
	public ValUnRecConADeAdapter(){
		super(DefSecurityConstants.ABM_VALUNRECCONADE);
	}


	//	 Getters y Setters


	public ValUnRecConADeVO getValUnRecConADe() {
		return valUnRecConADe;
	}

	public void setValUnRecConADe(ValUnRecConADeVO valUnRecConADe) {
		this.valUnRecConADe = valUnRecConADe;
	}


	public List<RecAliVO> getListRecAli() {
		return listRecAli;
	}


	public void setListRecAli(List<RecAliVO> listRecAli) {
		this.listRecAli = listRecAli;
	}

	
}
