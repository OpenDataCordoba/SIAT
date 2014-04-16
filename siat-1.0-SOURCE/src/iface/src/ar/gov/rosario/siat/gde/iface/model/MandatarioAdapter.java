//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Adapter del Mandatario
 * 
 * @author tecso
 */
public class MandatarioAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "mandatarioAdapterVO";
	
    private MandatarioVO mandatario = new MandatarioVO();
    
    
    // Constructores
    public MandatarioAdapter(){
    	super(GdeSecurityConstants.ABM_MANDATARIO);
    }
    
    //  Getters y Setters
	public MandatarioVO getMandatario() {
		return mandatario;
	}

	public void setMandatario(MandatarioVO mandatarioVO) {
		this.mandatario = mandatarioVO;
	}
	
	public String getName(){
		return NAME;
	}
			
	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de Mandatario");     
		 report.setReportBeanName("Mandatario");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		// Order by: no 
		 
		 ReportVO reportMandatario = new ReportVO();
		 reportMandatario.setReportTitle("Datos del Mandatario");
		 // carga de datos
	     
	     //Código
		 reportMandatario.addReportDato("Código", "codMandatario");
		 //Descripción
		 reportMandatario.addReportDato("Descripción", "desMandatario");
	     
		 report.getListReport().add(reportMandatario);
	
	}
	
	// View getters
}