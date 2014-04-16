//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

public class AreaReport {

	
	private Long idArea;
	private String desArea = "";
	private List<ProcuradorReport> listProcuradores = new ArrayList<ProcuradorReport>();
	List<TotalesConvenioReport> listTotales = new ArrayList<TotalesConvenioReport>();
	
	public AreaReport(){
		
	}

	public Long getIdArea() {
		return idArea;
	}
	public void setIdArea(Long idArea) {
		this.idArea = idArea;
	}

	public String getDesArea() {
		return desArea;
	}
	public void setDesArea(String desArea) {
		this.desArea = desArea;
	}
	
	public List<TotalesConvenioReport> getListTotales() {
		
		return listTotales;
	}
	public void setListTotales(List<TotalesConvenioReport> listTotales) {
		this.listTotales = listTotales;
	}
	
}
