//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import coop.tecso.demoda.iface.model.ReportVO;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter del InsSup
 * 
 * @author tecso
 */
public class InsSupAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "insSupAdapterVO";
	
	private List<SupervisorVO> listSupervisor =  new ArrayList<SupervisorVO>();
	
    private InsSupVO insSup = new InsSupVO();
    
    private List<SiNo>           listSiNo = new ArrayList<SiNo>();
    
    // Constructores
    public InsSupAdapter(){
    	super(EfSecurityConstants.ABM_INSSUP);
    }
    
    //  Getters y Setters
	public InsSupVO getInsSup() {
		return insSup;
	}

	public void setInsSup(InsSupVO insSupVO) {
		this.insSup = insSupVO;
	}

	public List<SiNo> getListSiNo() {
		return listSiNo;
	}

	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}
	
	public String getName(){
		return NAME;
	}
			
	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de Inspector/Supervisor");     
		 report.setReportBeanName("InsSup");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		// Order by: no 
		 
		 ReportVO reportInsSup = new ReportVO();
		 reportInsSup.setReportTitle("Datos del Inspector/Supervisor");
		 // carga de datos
	     
	     //Fecha Desde
		 reportInsSup.addReportDato("Fecha Desde", "fechaDesde");
		 //Fecha Hasta
		 reportInsSup.addReportDato("Fecha Hasta", "fechaHasta"); 
		 //Supervisor
		 reportInsSup.addReportDato("Supervisor", "supervisor.desSupervisor");
	     
		 report.getListReport().add(reportInsSup);
	
	}

	public List<SupervisorVO> getListSupervisor() {
		return listSupervisor;
	}

	public void setListSupervisor(List<SupervisorVO> listSupervisor) {
		this.listSupervisor = listSupervisor;
	}
	
	// View getters
}
