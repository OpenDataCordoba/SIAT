//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import coop.tecso.demoda.iface.model.ReportVO;


/**
 * Adapter del Clasificador
 * 
 * @author tecso
 */
public class ClasificadorAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "clasificadorAdapterVO";
	
    private ClasificadorVO clasificador = new ClasificadorVO();
    
    
    // Constructores
    public ClasificadorAdapter(){
    	super(BalSecurityConstants.ABM_CLASIFICADOR);
    }
    
    //  Getters y Setters
	public ClasificadorVO getClasificador() {
		return clasificador;
	}

	public void setClasificador(ClasificadorVO clasificadorVO) {
		this.clasificador = clasificadorVO;
	}

	public String getName(){
		return NAME;
	}
			
	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de Clasificador");     
		 report.setReportBeanName("Clasificador");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		// Order by: no 
		 
		 ReportVO reportClasificador = new ReportVO();
		 reportClasificador.setReportTitle("Datos del Clasificador");
		 // carga de datos
	     
	     //Descripción
		 reportClasificador.addReportDato("Cantidad de Niveles", "cantNivel");
		 reportClasificador.addReportDato("Descripción", "descripcion");
	     
		 report.getListReport().add(reportClasificador);
	
	}
	
	// View getters
}