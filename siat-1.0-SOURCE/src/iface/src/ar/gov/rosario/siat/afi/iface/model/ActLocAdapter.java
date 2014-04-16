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
 * Adapter del ActLoc
 * 
 * @author tecso
 */
public class ActLocAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "actLocAdapterVO";
	
    private ActLocVO actLoc = new ActLocVO();
    
    private List<SiNo>           listSiNo = new ArrayList<SiNo>();
    
    // Constructores
    public ActLocAdapter(){
    	super(AfiSecurityConstants.ABM_ACTLOC);
    }
    
    //  Getters y Setters
	public ActLocVO getActLoc() {
		return actLoc;
	}

	public void setActLoc(ActLocVO actLocVO) {
		this.actLoc = actLocVO;
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
		 report.setReportTitle("Reporte de ActLoc");     
		 report.setReportBeanName("ActLoc");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		// Order by: no 
		 
		 ReportVO reportActLoc = new ReportVO();
		 reportActLoc.setReportTitle("Datos del ActLoc");
		 // carga de datos
	     
	     //C�digo
		 reportActLoc.addReportDato("C�digo", "codActLoc");
		 //Descripci�n
		 reportActLoc.addReportDato("Descripci�n", "desActLoc");
	     
		 report.getListReport().add(reportActLoc);
	
	}
	
	// View getters
}