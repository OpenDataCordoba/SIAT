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
 * SearchPage del Sellado
 * 
 * @author Tecso
 *
 */
public class SelladoSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "selladoSearchPageVO";
	
	private SelladoVO sellado= new SelladoVO();
	
	// Constructores
	public SelladoSearchPage() {       
       super(BalSecurityConstants.ABM_SELLADO);        
    }
	
	// Getters y Setters
	public SelladoVO getSellado() {
		return sellado;
	}
	public void setSellado(SelladoVO sellado) {
		this.sellado = sellado;
	}

	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Consulta de Sellados");
		 report.setReportBeanName("ejercicio");
		 report.setReportFileName(this.getClass().getName());
		 
		 // Código
		
		 report.addReportFiltro("Código", this.getSellado().getCodSellado());
		 
		 // carga de filtros
		 
		 report.addReportFiltro("Descripción",  this.getSellado().getDesSellado());
		 
		 // Order by
		 //report.setReportOrderBy("desTipoObra ASC");
	     
	     ReportTableVO rtSe = new ReportTableVO("rtSe");
	     rtSe.setTitulo("Listado de Sellados");
	   
	     // carga de columnas
	     rtSe.addReportColumn("Código", "codSellado");
	     rtSe.addReportColumn("Descripción", "desSellado");
	     rtSe.addReportColumn("Estado", "estadoView");
	   	 report.getReportListTable().add(rtSe);

	    }
}
