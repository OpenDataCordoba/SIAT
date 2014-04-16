//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Adapter del TranBal
 * 
 * @author tecso
 */
public class TranBalAdapter extends SiatAdapterModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tranBalAdapterVO";
	
    private TranBalVO tranBal = new TranBalVO();
   
    // Constructores
    public TranBalAdapter(){
    	super(BalSecurityConstants.ABM_TRANBAL);
    }
    
    //  Getters y Setters
	public TranBalVO getTranBal() {
		return tranBal;
	}
	public void setTranBal(TranBalVO tranBalVO) {
		this.tranBal = tranBalVO;
	}    
	
	public String getName(){
		return NAME;
	}
	
		
	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de  TranBal");     
		 report.setReportBeanName("TranBal");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		 // Order by: no 
		 
		 ReportVO reportTranBal = new ReportVO();
		 reportTranBal.setReportTitle("Datos del  TranBal");
		 // carga de datos
	     
		 reportTranBal.addReportDato("Fecha","fecha");
		 reportTranBal.addReportDato("Partida", "partida.desPartida");
		 reportTranBal.addReportDato("Importe", "importe");
		 reportTranBal.addReportDato("Descripcion", "descripcion");
	     
		 report.getListReport().add(reportTranBal);
	
	}


}
