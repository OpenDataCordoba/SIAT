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
 * Adapter del DocSop
 * 
 * @author tecso
 */
public class DocSopAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "docSopAdapterVO";
	
    private DocSopVO docSop = new DocSopVO();
    
    private List<SiNo>     listSiNo = new ArrayList<SiNo>();
    
    // Constructores
    public DocSopAdapter(){
    	super(EfSecurityConstants.ABM_DOCSOP);
    }
    
    //  Getters y Setters
	public DocSopVO getDocSop() {
		return docSop;
	}

	public void setDocSop(DocSopVO docSopVO) {
		this.docSop = docSopVO;
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
		 report.setReportTitle("Reporte de DocSop");     
		 report.setReportBeanName("DocSop");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		// Order by: no 
		 
		 ReportVO reportDocSop = new ReportVO();
		 reportDocSop.setReportTitle("Datos del DocSop");
		 // carga de datos
	     
	     //C�digo
		 reportDocSop.addReportDato("C�digo", "codDocSop");
		 //Descripci�n
		 reportDocSop.addReportDato("Descripci�n", "desDocSop");
	     
		 report.getListReport().add(reportDocSop);
	
	}
	
	// View getters
}
