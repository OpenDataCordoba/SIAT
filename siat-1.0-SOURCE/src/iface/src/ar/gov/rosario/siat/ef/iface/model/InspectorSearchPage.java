//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del Inspector
 * 
 * @author Tecso
 *
 */
public class InspectorSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "inspectorSearchPageVO";
	
	private InspectorVO inspector= new InspectorVO();
	
	
	// Constructores
	public InspectorSearchPage() {       
       super(EfSecurityConstants.ABM_INSPECTOR);        
    }
	
	// Getters y Setters
	public InspectorVO getInspector() {
		return inspector;
	}
	public void setInspector(InspectorVO inspector) {
		this.inspector = inspector;
	}
	public String getName(){
		return NAME;
	}
		

	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Consulta de Inspector");
		 report.setReportBeanName("Inspector");
		 report.setReportFileName(this.getClass().getName());
		 
	     ReportTableVO rtInspector = new ReportTableVO("rtInspector");
	     rtInspector.setTitulo("Listado de Inspectores");
	    	
		 report.addReportFiltro("Fecha Desde", this.getInspector().getFechaDesdeView());
		 report.addReportFiltro("Fecha Hasta", this.getInspector().getFechaHastaView());
		 report.addReportFiltro("Descripción", this.getInspector().getDesInspector());
		 		 
	     // carga de columnas
		 rtInspector.addReportColumn("Fecha Desde", "fechaDesde");
		 rtInspector.addReportColumn("Fecha Hasta", "fechaHasta");
		 rtInspector.addReportColumn("Descripción", "desInspector");
	    
	     report.getReportListTable().add(rtInspector);

	    }
	// View getters
}
