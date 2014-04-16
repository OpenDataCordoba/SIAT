//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Adapter del Caja 69
 * 
 * @author tecso
 */
public class Caja69Adapter extends SiatAdapterModel {
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "caja69AdapterVO";
	
    private Caja69VO caja69 = new Caja69VO();
   
    // Constructores
    public Caja69Adapter(){
    	super(BalSecurityConstants.ABM_CAJA69);
    }
    
    //  Getters y Setters
	public Caja69VO getCaja69() {
		return caja69;
	}
	public void setCaja69(Caja69VO caja69VO) {
		this.caja69 = caja69VO;
	}    
	
	public String getName(){
		return NAME;
	}
	
		
	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de  Caja69");     
		 report.setReportBeanName("Caja69");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		 // Order by: no 
		 
		 ReportVO reportCaja69 = new ReportVO();
		 reportCaja69.setReportTitle("Datos del  Caja69");
		 // carga de datos
	     
		 reportCaja69.addReportDato("Fecha","fecha");
		 reportCaja69.addReportDato("Partida", "partida.desPartida");
		 reportCaja69.addReportDato("Importe", "importe");
		 reportCaja69.addReportDato("Descripcion", "descripcion");
	     
		 report.getListReport().add(reportCaja69);
	
	}


}
