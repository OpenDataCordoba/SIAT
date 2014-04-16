//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Adapter del TipCueBan
 * 
 * @author tecso
 */
public class TipCueBanAdapter extends SiatAdapterModel{
	
	public static final String NAME = "TipCueBanAdapterVO";
	
    private TipCueBanVO TipCueBan = new TipCueBanVO();
    

    
    // Constructores
    public TipCueBanAdapter(){
    	super(BalSecurityConstants.ABM_TIPCUEBAN);
    }
    
    //  Getters y Setters
	public TipCueBanVO getTipCueBan() {
		return TipCueBan;
	}

	public void setTipCueBan(TipCueBanVO TipCueBanVO) {
		this.TipCueBan = TipCueBanVO;
	}


	public String getName(){
		return NAME;
	}
	
		
	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de Tipo Cuenta Bancaria");     
		 report.setReportBeanName("TipCueBan");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		 // Order by: no 
		 
		 ReportVO reportTipCueBan = new ReportVO();
		 reportTipCueBan.setReportTitle("Datos de Tipo Cuenta Bancaria");
		 // carga de datos
	     
	     //Descripción
		 reportTipCueBan.addReportDato("Descripción", "descripcion");
		//Estado
		 reportTipCueBan.addReportDato("Estado", "estadoView");
	     
		 report.getListReport().add(reportTipCueBan);
	
	}
	
	// View getters
}
