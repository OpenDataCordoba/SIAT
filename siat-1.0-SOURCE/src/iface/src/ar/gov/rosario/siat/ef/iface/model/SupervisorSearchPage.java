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
 * SearchPage del Supervisor
 * 
 * @author Tecso
 *
 */
public class SupervisorSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "supervisorSearchPageVO";
	
	private SupervisorVO supervisor= new SupervisorVO();
	
	// Constructores
	public SupervisorSearchPage() {       
       super(EfSecurityConstants.ABM_SUPERVISOR);        
    }
	
	// Getters y Setters
	public SupervisorVO getSupervisor() {
		return supervisor;
	}
	public void setSupervisor(SupervisorVO supervisor) {
		this.supervisor = supervisor;
	}

	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Consulta  de Supervisores");
		 report.setReportBeanName("supervisor");
		 report.setReportFileName(this.getClass().getName());
		
		 // Descripción
		
		 report.addReportFiltro("Descripción", this.getSupervisor().getDesSupervisor());
		 
	
		 
		 // Fecha Desde
		 report.addReportFiltro("Fecha Desde", this.getSupervisor().getFechaDesdeView());
	     
		// Fecha Hasta
		 report.addReportFiltro("Fecha Hasta",  this.getSupervisor().getFechaHastaView());
	     
		 
	
	     // Order by
		 //report.setReportOrderBy("desTipoObra ASC");
	     
	     ReportTableVO rtInv = new ReportTableVO("rtInv");
	     rtInv.setTitulo("Listado de Supervisores");
	   
	     // carga de columnas
	     rtInv.addReportColumn("Descripción", "desSupervisor");
	     rtInv.addReportColumn("Fecha Desde", "fechaDesde");
	     rtInv.addReportColumn("Fecha Hasta", "fechaHasta");
	 
	     report.getReportListTable().add(rtInv);

	    }
}
