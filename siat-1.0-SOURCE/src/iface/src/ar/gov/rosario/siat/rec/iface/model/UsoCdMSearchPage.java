//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.rec.iface.util.RecSecurityConstants;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del UsoCdM
 * 
 * @author Tecso
 *
 */
public class UsoCdMSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "usoCdMSearchPageVO";
	
	private UsoCdMVO usoCdM= new UsoCdMVO();
	
	// Constructores
	public UsoCdMSearchPage() {       
       super(RecSecurityConstants.ABM_USOCDM);        
    }
	
	// Getters y Setters
	public UsoCdMVO getUsoCdM() {
		return usoCdM;
	}
	public void setUsoCdM(UsoCdMVO usoCdM) {
		this.usoCdM = usoCdM;
	}

	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Consulta de Usos");
		 report.setReportBeanName("UsoCmd");
		 report.setReportFileName(this.getClass().getName());
		 
		
		 report.addReportFiltro("Descripci\u00F3n", this.getUsoCdM().getDesUsoCdM());
		 
	     // Order by
		 report.setReportOrderBy("desUsoCdM");
	     
	     ReportTableVO rtUso = new ReportTableVO("rtUso");
	     rtUso.setTitulo("Listado de Usos");
	   
	     // carga de columnas
	     rtUso.addReportColumn("Descripci\u00F3n", "desUsoCdM");
	     rtUso.addReportColumn("Factor", "factor");
	     rtUso.addReportColumn("Usos de Catastro ", "usosCatastro");
	     rtUso.addReportColumn("Estado", "estadoView");
	     report.getReportListTable().add(rtUso);

	    }
	
	
}
