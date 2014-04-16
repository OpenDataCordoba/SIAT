//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del FuenteInfo
 * 
 * @author Tecso
 *
 */
public class FuenteInfoSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "fuenteInfoSearchPageVO";
	
	private FuenteInfoVO fuenteInfo= new FuenteInfoVO();
	
	// Constructores
	public FuenteInfoSearchPage() {       
       super(EfSecurityConstants.ABM_FUENTEINFO);        
    }
	
	// Getters y Setters
	public FuenteInfoVO getFuenteInfo() {
		return fuenteInfo;
	}
	public void setFuenteInfo(FuenteInfoVO fuenteInfo) {
		this.fuenteInfo = fuenteInfo;
	}           

    public String getName(){    
		return NAME;
	}
	
	public void prepareReport(Long format) {

		ReportVO report = this.getReport(); // no instanciar una nueva
		report.setReportFormat(format);	
		report.setReportTitle("Listados de la Fuente de Información");
		report.setReportBeanName("FuenteInfo");
		report.setReportFileName(this.getClass().getName());

     	
	
       //Descripción
		report.addReportFiltro("Nombre Fuente", this.getFuenteInfo().getNombreFuente());
		

		ReportTableVO rtFuenteInfo = new ReportTableVO("rtFuenteInfo");
		rtFuenteInfo.setTitulo("B\u00FAsqueda de la Fuente de Información");

		// carga de columnas
		rtFuenteInfo.addReportColumn("Nombre Fuente","nombreFuente");
		rtFuenteInfo.addReportColumn("Tipo Periodicidad", "tipoPeriodicidad");
		rtFuenteInfo.addReportColumn("Apertura", "apertura");
		rtFuenteInfo.addReportColumn("Descripción Columna", "desCol1");
		
		 
	    report.getReportListTable().add(rtFuenteInfo);

	}
	// View getters
}
