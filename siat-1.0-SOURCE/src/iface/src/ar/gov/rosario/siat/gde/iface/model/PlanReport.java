//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

public class PlanReport {

	
	private Long idPlan;
	private String desPlan = "";
	private List<AreaReport> listArea = new ArrayList<AreaReport>();
	
	
	public PlanReport(){
		
	}

	public Long getIdPlan() {
		return idPlan;
	}
	public void setIdPlan(Long idPlan) {
		this.idPlan = idPlan;
	}

	public String getDesPlan() {
		return desPlan;
	}
	public void setDesPlan(String desPlan) {
		this.desPlan = desPlan;
	}

	public List<AreaReport> getListArea() {
		return listArea;
	}
	public void setListArea(List<AreaReport> listArea) {
		this.listArea = listArea;
	}
	
	
}
