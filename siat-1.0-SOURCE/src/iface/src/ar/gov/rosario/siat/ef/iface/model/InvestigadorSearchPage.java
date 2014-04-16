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
 * SearchPage del Investigador
 * 
 * @author Tecso
 *
 */
public class InvestigadorSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "investigadorSearchPageVO";
	
	private InvestigadorVO investigador= new InvestigadorVO();
	
	// Constructores
	public InvestigadorSearchPage() {       
       super(EfSecurityConstants.ABM_INVESTIGADOR);        
    }
	
	// Getters y Setters
	public InvestigadorVO getInvestigador() {
		return investigador;
	}
	public void setInvestigador(InvestigadorVO investigador) {
		this.investigador = investigador;
	}

	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Consulta  de Investigadores");
		 report.setReportBeanName("ConribExe");
		 report.setReportFileName(this.getClass().getName());
		
		 // Descripción
		
		 report.addReportFiltro("Descripción", this.getInvestigador().getDesInvestigador());
		 
	
		 
		 // Fecha Desde
		 report.addReportFiltro("Fecha Desde", this.getInvestigador().getFechaDesdeView());
	     
		// Fecha Hasta
		 report.addReportFiltro("Fecha Hasta",  this.getInvestigador().getFechaHastaView());
	     
		 
	
	     // Order by
		 //report.setReportOrderBy("desTipoObra ASC");
	     
	     ReportTableVO rtInv = new ReportTableVO("rtInv");
	     rtInv.setTitulo("Listado de Investigadores");
	   
	     // carga de columnas
	     rtInv.addReportColumn("Descripción", "desInvestigador");
	     rtInv.addReportColumn("Fecha Desde", "fechaDesde");
	     rtInv.addReportColumn("Fecha Hasta", "fechaHasta");
	 
	     report.getReportListTable().add(rtInv);

	    }
}
