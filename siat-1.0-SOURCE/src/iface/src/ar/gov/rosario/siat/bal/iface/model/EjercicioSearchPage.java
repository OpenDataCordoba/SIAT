//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del Ejercicio
 * 
 * @author Tecso
 *
 */
public class EjercicioSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "ejercicioSearchPageVO";
	
	private EjercicioVO ejercicio= new EjercicioVO();
	
	// Constructores
	public EjercicioSearchPage() {       
       super(BalSecurityConstants.ABM_EJERCICIO);        
    }
	
	// Getters y Setters
	public EjercicioVO getEjercicio() {
		return ejercicio;
	}
	public void setEjercicio(EjercicioVO ejercicio) {
		this.ejercicio = ejercicio;
	}

	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Consulta de ejercicios");
		 report.setReportBeanName("ejercicio");
		 report.setReportFileName(this.getClass().getName());
		 
		 // carga de filtros
		 
		 report.addReportFiltro("Descripción", this.getEjercicio().getDesEjercicio());
		 
		 // Fecha de Inicio
		
		 report.addReportFiltro("Fecha de Inicio", this.getEjercicio().getFecIniEjeView());
		 
		 // Fecha de Fin
		 report.addReportFiltro("Fecha de Fin", this.getEjercicio().getFecFinEjeView());
	     
		 // Order by
		 //report.setReportOrderBy("desTipoObra ASC");
	     
	     ReportTableVO rtEj = new ReportTableVO("rtEj");
	     rtEj.setTitulo("Listado de ejercicios");
	   
	     // carga de columnas
	     rtEj.addReportColumn("Fecha de Inicio", "fecIniEje");
	     rtEj.addReportColumn("Fecha de Fin", "fecFinEje");
	     rtEj.addReportColumn("Fecha de Cierre", "fechaCierre");
	     rtEj.addReportColumn("Estado Ejercicio", "estEjercicio.desEjeBal");
	     report.getReportListTable().add(rtEj);

	    }
}
