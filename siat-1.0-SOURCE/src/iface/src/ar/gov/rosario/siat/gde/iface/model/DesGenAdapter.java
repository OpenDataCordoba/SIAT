//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Adapter del DesGen
 * 
 * @author tecso
 */
public class DesGenAdapter extends SiatAdapterModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String NAME = "desGenAdapterVO";
	
    private DesGenVO desGen = new DesGenVO();
    
    // Constructores
    public DesGenAdapter(){    	
    	super(GdeSecurityConstants.ABM_DESGEN);
    }
    
    //  Getters y Setters
	public DesGenVO getDesGen() {
		return desGen;
	}

	public void setDesGen(DesGenVO desGenVO) {
		this.desGen = desGenVO;
	}
	
	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de Descuento General");
		 report.setReportBeanName("desGen");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		 // Order by: no 
		 
		 ReportVO reportDatosEsp = new ReportVO();
		 reportDatosEsp.setReportTitle("Datos de Descuento General");
		 // carga de datos
	     //Descripción                                
		 reportDatosEsp.addReportDato("Descripción", "desDesGen");
	     //Leyenda
		 reportDatosEsp.addReportDato("Leyenda", "leyendaDesGen");
		 //Porcentaje
		 reportDatosEsp.addReportDato("Porcentaje", "porDes");
	     //Estado
		 reportDatosEsp.addReportDato("Estado", "estadoView");
		
		 report.getListReport().add(reportDatosEsp);
		
	}
}
