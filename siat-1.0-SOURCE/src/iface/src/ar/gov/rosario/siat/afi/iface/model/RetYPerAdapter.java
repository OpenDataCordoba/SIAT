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
 * Adapter del RetYPer
 * 
 * @author tecso
 */
public class RetYPerAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "retYPerAdapterVO";
	
    private RetYPerVO retYPer = new RetYPerVO();
    
    private List<SiNo>           listSiNo = new ArrayList<SiNo>();
    
    // Constructores
    public RetYPerAdapter(){
    	super(AfiSecurityConstants.ABM_RETYPER);
    }
    
    //  Getters y Setters
	public RetYPerVO getRetYPer() {
		return retYPer;
	}

	public void setRetYPer(RetYPerVO retYPerVO) {
		this.retYPer = retYPerVO;
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
		 report.setReportTitle("Reporte de RetYPer");     
		 report.setReportBeanName("RetYPer");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		// Order by: no 
		 
		 ReportVO reportRetYPer = new ReportVO();
		 reportRetYPer.setReportTitle("Datos del RetYPer");
		 // carga de datos
	     
	     //Código
		 reportRetYPer.addReportDato("Código", "codRetYPer");
		 //Descripción
		 reportRetYPer.addReportDato("Descripción", "desRetYPer");
	     
		 report.getListReport().add(reportRetYPer);
	
	}
	
	// View getters
}