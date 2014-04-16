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
 * Adapter del Local
 * 
 * @author tecso
 */
public class LocalAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "localAdapterVO";
	
    private LocalVO local = new LocalVO();
    
    private List<SiNo>           listSiNo = new ArrayList<SiNo>();
    
    // Constructores
    public LocalAdapter(){
    	super(AfiSecurityConstants.ABM_LOCAL);
    }
    
    //  Getters y Setters
	public LocalVO getLocal() {
		return local;
	}

	public void setLocal(LocalVO localVO) {
		this.local = localVO;
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
		 report.setReportTitle("Reporte de Local");     
		 report.setReportBeanName("Local");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		// Order by: no 
		 
		 ReportVO reportLocal = new ReportVO();
		 reportLocal.setReportTitle("Datos del Local");
		 // carga de datos
	     
	     //Código
		 reportLocal.addReportDato("Código", "codLocal");
		 //Descripción
		 reportLocal.addReportDato("Descripción", "desLocal");
	     
		 report.getListReport().add(reportLocal);
	
	}
	
	// View getters
}