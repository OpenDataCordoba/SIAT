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
 * Adapter del DatosDomicilio
 * 
 * @author tecso
 */
public class DatosDomicilioAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "datosDomicilioAdapterVO";
	
    private DatosDomicilioVO datosDomicilio = new DatosDomicilioVO();
    
    private List<SiNo>           listSiNo = new ArrayList<SiNo>();
    
    // Constructores
    public DatosDomicilioAdapter(){
    	super(AfiSecurityConstants.ABM_DATOSDOMICILIO);
    }
    
    //  Getters y Setters
	public DatosDomicilioVO getDatosDomicilio() {
		return datosDomicilio;
	}

	public void setDatosDomicilio(DatosDomicilioVO datosDomicilioVO) {
		this.datosDomicilio = datosDomicilioVO;
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
		 report.setReportTitle("Reporte de DatosDomicilio");     
		 report.setReportBeanName("DatosDomicilio");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		// Order by: no 
		 
		 ReportVO reportDatosDomicilio = new ReportVO();
		 reportDatosDomicilio.setReportTitle("Datos del DatosDomicilio");
		 // carga de datos
	     
	     //Código
		 reportDatosDomicilio.addReportDato("Código", "codDatosDomicilio");
		 //Descripción
		 reportDatosDomicilio.addReportDato("Descripción", "desDatosDomicilio");
	     
		 report.getListReport().add(reportDatosDomicilio);
	
	}
	
	// View getters
}