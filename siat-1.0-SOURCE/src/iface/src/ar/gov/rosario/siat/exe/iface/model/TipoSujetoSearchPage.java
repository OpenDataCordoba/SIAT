//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.exe.iface.util.ExeSecurityConstants;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del TipoSujeto
 * 
 * @author Tecso
 *
 */
public class TipoSujetoSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipoSujetoSearchPageVO";
	
	private TipoSujetoVO tipoSujeto= new TipoSujetoVO();
	
	// Constructores
	public TipoSujetoSearchPage() {       
       super(ExeSecurityConstants.ABM_TIPOSUJETO);        
    }
	
	// Getters y Setters
	public TipoSujetoVO getTipoSujeto() {
		return tipoSujeto;
	}
	public void setTipoSujeto(TipoSujetoVO tipoSujeto) {
		this.tipoSujeto = tipoSujeto;
	}

	// View getters
	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Consulta de Tipo de Sujeto");
		 report.setReportBeanName("TipoSujeto");
		 report.setReportFileName(this.getClass().getName());
		 
	
		 // carga de filtros
		 report.addReportFiltro("Código", this.getTipoSujeto().getCodTipoSujeto());
	
		 report.addReportFiltro("Descripción", this.getTipoSujeto().getDesTipoSujeto());
	
		 // Order by
		 //report.setReportOrderBy("desTipoObra ASC");
	     
	     ReportTableVO rtTpoSjto = new ReportTableVO("rtTpoSjto");
	     rtTpoSjto.setTitulo("Listado de Tipo de Sujeto");
	   
	     // carga de columnas
	     rtTpoSjto.addReportColumn("Código", "codTipoSujeto");
	     rtTpoSjto.addReportColumn("Descripción", "desTipoSujeto");
	     rtTpoSjto.addReportColumn("Estado", "estadoView");
	     report.getReportListTable().add(rtTpoSjto);
	     

	    }
		
}
