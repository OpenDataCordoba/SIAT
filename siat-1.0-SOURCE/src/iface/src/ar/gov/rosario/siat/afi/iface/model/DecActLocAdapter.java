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
 * Adapter del DecActLoc
 * 
 * @author tecso
 */
public class DecActLocAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "decActLocAdapterVO";
	
    private DecActLocVO decActLoc = new DecActLocVO();
    
    private List<SiNo>   listSiNo = new ArrayList<SiNo>();
    
    private Boolean paramEtur = false;
    
    // Constructores
    public DecActLocAdapter(){
    	super(AfiSecurityConstants.ABM_DECACTLOC);
    }
    
    //  Getters y Setters
	public DecActLocVO getDecActLoc() {
		return decActLoc;
	}

	public void setDecActLoc(DecActLocVO decActLocVO) {
		this.decActLoc = decActLocVO;
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
		 report.setReportTitle("Reporte de DecActLoc");     
		 report.setReportBeanName("DecActLoc");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		// Order by: no 
		 
		 ReportVO reportDecActLoc = new ReportVO();
		 reportDecActLoc.setReportTitle("Datos del DecActLoc");
		 // carga de datos
	     
	     //Código
		 reportDecActLoc.addReportDato("Código", "codDecActLoc");
		 //Descripción
		 reportDecActLoc.addReportDato("Descripción", "desDecActLoc");
	     
		 report.getListReport().add(reportDecActLoc);
	
	}

	public Boolean getParamEtur() {
		return paramEtur;
	}
	public void setParamEtur(Boolean paramEtur) {
		this.paramEtur = paramEtur;
	}
	
	
	
}