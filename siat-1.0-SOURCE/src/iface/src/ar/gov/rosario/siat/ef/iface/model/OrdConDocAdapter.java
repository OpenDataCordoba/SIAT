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

/**
 * Adapter del OrdConDoc
 * 
 * @author tecso
 */
public class OrdConDocAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "ordConDocAdapterVO";
	
	private OrdConDocVO ordConDoc = new OrdConDocVO();
    
	private List<DocumentacionVO> listDocumentacion = new ArrayList<DocumentacionVO>();
    
    private String[] idsSelected;
    
    // Constructores
    public OrdConDocAdapter(){
    	super(EfSecurityConstants.ABM_ORDCONDOC);
    }
    
    //  Getters y Setters
	public String[] getIdsSelected() {
		return idsSelected;
	}

	public void setIdsSelected(String[] idsSelected) {
		this.idsSelected = idsSelected;
	}
	
	public List<DocumentacionVO> getListDocumentacion() {
		return listDocumentacion;
	}

	public void setListDocumentacion(List<DocumentacionVO> listDocumentacion) {
		this.listDocumentacion = listDocumentacion;
	}

	public OrdConDocVO getOrdConDoc() {
		return ordConDoc;
	}

	public void setOrdConDoc(OrdConDocVO ordConDocVO) {
		this.ordConDoc = ordConDocVO;
	}
		
	public String getName(){
		return NAME;
	}
			
	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de OrdConDoc");     
		 report.setReportBeanName("OrdConDoc");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		// Order by: no 
		 
		 ReportVO reportOrdConDoc = new ReportVO();
		 reportOrdConDoc.setReportTitle("Datos del OrdConDoc");
		 // carga de datos
	     
	     //C�digo
		 reportOrdConDoc.addReportDato("C�digo", "codOrdConDoc");
		 //Descripci�n
		 reportOrdConDoc.addReportDato("Descripci�n", "desOrdConDoc");
	     
		 report.getListReport().add(reportOrdConDoc);
	
	}
	
	
	// View getters
}
