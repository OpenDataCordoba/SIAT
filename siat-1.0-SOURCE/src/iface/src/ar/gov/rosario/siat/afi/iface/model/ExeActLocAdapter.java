//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.afi.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.afi.iface.util.AfiSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import coop.tecso.demoda.iface.model.ReportVO;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter del ExeActLoc
 * 
 * @author tecso
 */
public class ExeActLocAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "exeActLocAdapterVO";
	
    private ExeActLocVO exeActLoc = new ExeActLocVO();
    
    private List<SiNo>           listSiNo = new ArrayList<SiNo>();
    
    // Constructores
    public ExeActLocAdapter(){
    	super(AfiSecurityConstants.ABM_EXEACTLOC);
    }
    
    //  Getters y Setters
	public ExeActLocVO getExeActLoc() {
		return exeActLoc;
	}

	public void setExeActLoc(ExeActLocVO exeActLocVO) {
		this.exeActLoc = exeActLocVO;
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
		 report.setReportTitle("Reporte de ExeActLoc");     
		 report.setReportBeanName("ExeActLoc");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		// Order by: no 
		 
		 ReportVO reportExeActLoc = new ReportVO();
		 reportExeActLoc.setReportTitle("Datos del ExeActLoc");
		 // carga de datos
	     
	     //Código
		 reportExeActLoc.addReportDato("Código", "codExeActLoc");
		 //Descripción
		 reportExeActLoc.addReportDato("Descripción", "desExeActLoc");
	     
		 report.getListReport().add(reportExeActLoc);
	
	}
	
	// View getters
}