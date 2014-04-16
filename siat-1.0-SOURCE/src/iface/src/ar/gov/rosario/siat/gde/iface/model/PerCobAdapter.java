//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.model.AreaVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Adapter del PerCob
 * 
 * @author tecso
 */
public class PerCobAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "perCobAdapterVO";
	
    private PerCobVO perCob = new PerCobVO();
    private List<AreaVO> listArea = new ArrayList<AreaVO>();
    
    // Constructores
    public PerCobAdapter(){
    	super(GdeSecurityConstants.ABM_PERCOB);
    }
    
    //  Getters y Setters
	public PerCobVO getPerCob() {
		return perCob;
	}

	public void setPerCob(PerCobVO perCobVO) {
		this.perCob = perCobVO;
	}
	
	public String getName(){
		return NAME;
	}
			
	public List<AreaVO> getListArea() {
		return listArea;
	}

	public void setListArea(List<AreaVO> listArea) {
		this.listArea = listArea;
	}

	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de Agentes de Cobranza");     
		 report.setReportBeanName("PerCob");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		// Order by: no 
		 
		 ReportVO reportPerCob = new ReportVO();
		 reportPerCob.setReportTitle("Datos del Agente de Cobranza");
		 // carga de datos
	     
	     //Descripción
		 reportPerCob.addReportDato("Descripción", "nombreApellido");
	     
		 report.getListReport().add(reportPerCob);
	
	}
	
	// View getters
}