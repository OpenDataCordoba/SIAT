//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Adapter del InicioInv
 * 
 * @author tecso
 */
public class InicioInvAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "inicioInvAdapterVO";
	
    private InicioInvVO inicioInv = new InicioInvVO();
    
    // Constructores
    public InicioInvAdapter(){
    	super(EfSecurityConstants.ABM_INICIOINV);
    }
    
    //  Getters y Setters
	public InicioInvVO getInicioInv() {
		return inicioInv;
	}

	public void setInicioInv(InicioInvVO inicioInvVO) {
		this.inicioInv = inicioInvVO;
	}

	public String getName(){
		return NAME;
	}
			
	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de InicioInv");     
		 report.setReportBeanName("InicioInv");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		// Order by: no 
		 
		 ReportVO reportInicioInv = new ReportVO();
		 reportInicioInv.setReportTitle("Datos del InicioInv");
		 // carga de datos
	     
	     //C�digo
		 reportInicioInv.addReportDato("C�digo", "codInicioInv");
		 //Descripci�n
		 reportInicioInv.addReportDato("Descripci�n", "desInicioInv");
	     
		 report.getListReport().add(reportInicioInv);
	
	}
	
	// View getters
}
