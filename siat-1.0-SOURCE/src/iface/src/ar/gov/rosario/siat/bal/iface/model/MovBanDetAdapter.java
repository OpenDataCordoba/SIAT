//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Adapter del MovBanDet
 * 
 * @author tecso
 */
public class MovBanDetAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "movBanDetAdapterVO";
	
    private MovBanDetVO movBanDet = new MovBanDetVO();
    
    // Constructores
    public MovBanDetAdapter(){
    	super(BalSecurityConstants.ABM_MOVBANDET);
    }
    
    //  Getters y Setters
	public MovBanDetVO getMovBanDet() {
		return movBanDet;
	}

	public void setMovBanDet(MovBanDetVO movBanDetVO) {
		this.movBanDet = movBanDetVO;
	}
	
	public String getName(){
		return NAME;
	}
	
		
	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de Detalle de Movimientos Bancarios AFIP");     
		 report.setReportBeanName("MovBanDet");
		 report.setReportFileName(this.getClass().getName());
		 		 
		 ReportVO reportMovBanDet = new ReportVO();
		 reportMovBanDet.setReportTitle("Datos del Detalle");
		 // carga de datos
	     
		 reportMovBanDet.addReportDato("", "");
		 reportMovBanDet.addReportDato("", "");
		 reportMovBanDet.addReportDato("", "");
	     
	     report.getListReport().add(reportMovBanDet);
	
	}

}
