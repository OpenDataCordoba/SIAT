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
 * Adapter del HabLoc
 * 
 * @author tecso
 */
public class HabLocAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "habLocAdapterVO";
	
    private HabLocVO habLoc = new HabLocVO();
    
    private List<SiNo>           listSiNo = new ArrayList<SiNo>();
    
    // Constructores
    public HabLocAdapter(){
    	super(AfiSecurityConstants.ABM_HABLOC);
    }
    
    //  Getters y Setters
	public HabLocVO getHabLoc() {
		return habLoc;
	}

	public void setHabLoc(HabLocVO habLocVO) {
		this.habLoc = habLocVO;
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
		 report.setReportTitle("Reporte de HabLoc");     
		 report.setReportBeanName("HabLoc");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		// Order by: no 
		 
		 ReportVO reportHabLoc = new ReportVO();
		 reportHabLoc.setReportTitle("Datos del HabLoc");
		 // carga de datos
	     
	     //Código
		 reportHabLoc.addReportDato("Código", "codHabLoc");
		 //Descripción
		 reportHabLoc.addReportDato("Descripción", "desHabLoc");
	     
		 report.getListReport().add(reportHabLoc);
	
	}
	
	// View getters
}