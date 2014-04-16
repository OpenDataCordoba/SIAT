//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearcPage de atributos del contribuyente
 * 
 * @author Tecso
 *
 */
public class ConAtrSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "conAtrSearchPageVO";
	
	private ConAtrVO conAtr = new ConAtrVO();  
	
	public ConAtrSearchPage() {
    	super(DefSecurityConstants.ABM_CONTRIBUYENTE_ATRIBUTO);

    }

	// Getters y Setters	
	public ConAtrVO getConAtr() {
		return conAtr;
	}

	public void setConAtr(ConAtrVO conAtr) {
		this.conAtr = conAtr;
	}
	
	

	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de Atributos del Contribuyente");
		 report.setReportBeanName("AtributosContribuyentes");
		 report.setReportFileName(this.getClass().getName());
		 
		 // carga de filtros
		 report.addReportFiltro("Código", this.getConAtr().getAtributo().getCodAtributo());
		 report.addReportFiltro("Descripción", this.getConAtr().getAtributo().getDesAtributo());
		 
		 // Order by
		 report.setReportOrderBy("atributo.codAtributo ASC");
		 
	     ReportTableVO rtConAtr = new ReportTableVO("ConAtr");
	     rtConAtr.setTitulo("Listado de Atributos del Contribuyente");
	     
		 // carga de columnas
	     rtConAtr.addReportColumn("Código", "atributo.codAtributo");
	     rtConAtr.addReportColumn("Descripción", "atributo.desAtributo");
	     rtConAtr.addReportColumn("Es Atr. Seg.", "esAtrSegmentacionView");
	     rtConAtr.addReportColumn("Valor Defecto", "valorDefecto");
	     rtConAtr.addReportColumn("Es Vis. Con. Deu.", "esVisConDeuView");
	     rtConAtr.addReportColumn("Es Atr. Bus.", "esAtributoBusView");
	     rtConAtr.addReportColumn("Adm. Bus. por Ran.", "admBusPorRanView");
	     rtConAtr.addReportColumn("Estado", "estadoView");
	     
	     report.getReportListTable().add(rtConAtr);
	}

}
