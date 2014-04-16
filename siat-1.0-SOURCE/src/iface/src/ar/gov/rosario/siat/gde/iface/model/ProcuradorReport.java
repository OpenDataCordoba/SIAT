//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

public class ProcuradorReport {

	
	private Long idProcurador;
	private String desProcurador = "";
	private List<PlanReport> listPlanReport = new ArrayList<PlanReport>();
	
	
	public ProcuradorReport(){
		
	}

	public Long getIdProcurador() {
		return idProcurador;
	}
	public void setIdProcurador(Long idProcurador) {
		this.idProcurador = idProcurador;
	}

	public String getDesProcurador() {
		return desProcurador;
	}
	public void setDesProcurador(String desProcurador) {
		this.desProcurador = desProcurador;
	}

	public List<PlanReport> getListPlanReport() {
		return listPlanReport;
	}
	public void setListPlanReport(List<PlanReport> listTotales) {
		this.listPlanReport = listTotales;
	}
	
}
