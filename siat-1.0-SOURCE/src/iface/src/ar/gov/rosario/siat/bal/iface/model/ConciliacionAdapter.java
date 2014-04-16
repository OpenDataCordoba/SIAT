//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Adapter del Conciliacion
 * 
 * @author tecso
 */
public class ConciliacionAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "conciliacionAdapterVO";
	
    private ConciliacionVO conciliacion = new ConciliacionVO();
      
   
    // Constructores
    public ConciliacionAdapter(){
    	super(BalSecurityConstants.ABM_CONCILIACION);
    }
    
    //  Getters y Setters
	public ConciliacionVO getConciliacion() {
		return conciliacion;
	}

	public void setConciliacion(ConciliacionVO conciliacionVO) {
		this.conciliacion = conciliacionVO;
	}
	
	public String getName(){
		return NAME;
	}			
	
	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de Conciliacion");     
		 report.setReportBeanName("Conciliacion");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		// Order by: no 
		 
		 ReportVO reportConciliacion = new ReportVO();
		 reportConciliacion.setReportTitle("Datos del Conciliacion");
		 // carga de datos
	     
	     //Código
		 reportConciliacion.addReportDato("Código", "codConciliacion");
		 //Descripción
		 reportConciliacion.addReportDato("Descripción", "desConciliacion");
	     
		 report.getListReport().add(reportConciliacion);
	
	}
	
	// Flags Seguridad

}