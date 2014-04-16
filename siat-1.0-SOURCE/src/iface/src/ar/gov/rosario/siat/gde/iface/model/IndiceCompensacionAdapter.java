//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Adapter del Indice de Compensacion
 * 
 * @author tecso
 */
public class IndiceCompensacionAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "indiceCompensacionAdapterVO";
	
    private IndiceCompensacionVO indiceCompensacion = new IndiceCompensacionVO();
    
    
    // Constructores
    public IndiceCompensacionAdapter(){
    	super(GdeSecurityConstants.ABM_INDICECOMPENSACION);
    }
    
    //  Getters y Setters
	public IndiceCompensacionVO getIndiceCompensacion() {
		return indiceCompensacion;
	}

	public void setIndiceCompensacion(IndiceCompensacionVO indiceCompensacionVO) {
		this.indiceCompensacion = indiceCompensacionVO;
	}

	public String getName(){
		return NAME;
	}
			
	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de IndiceCompensacion");     
		 report.setReportBeanName("IndiceCompensacion");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		// Order by: no 
		 
		 ReportVO reportIndiceCompensacion = new ReportVO();
		 reportIndiceCompensacion.setReportTitle("Datos del IndiceCompensacion");
		 // carga de datos
	     
	     //Código
		 reportIndiceCompensacion.addReportDato("Código", "codIndiceCompensacion");
		 //Descripción
		 reportIndiceCompensacion.addReportDato("Descripción", "desIndiceCompensacion");
	     
		 report.getListReport().add(reportIndiceCompensacion);
	
	}
	
	// View getters
}