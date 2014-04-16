//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

public class DomAtrSearchPage extends SiatPageModel {

	public static final String NAME = "domAtrSearchPageVO";
	
	private DomAtrVO domAtr = new DomAtrVO(); 
	
	public DomAtrSearchPage() {
		super(DefSecurityConstants.ABM_DOMINIO_ATRIBUTO);
		ACCION_AGREGAR = DefSecurityConstants.ABM_DOMINIO_ATRIBUTO_ENC;
    }
	// ------------------------ Getters y Setters --------------------------------//

	public DomAtrVO getDomAtr() {
		return domAtr;
	}

	public void setDomAtr(DomAtrVO domAtr) {
		this.domAtr = domAtr;
	}
	
	// Getter para struts ------------------------------------------------------------------------------------------------
	
	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de Dominios de Atributos");
		 report.setReportBeanName("DominiosAtributos");
		 report.setReportFileName(this.getClass().getName());
		 
		 // carga de filtros
		 report.addReportFiltro("Código", this.getDomAtr().getCodDomAtr());
		 report.addReportFiltro("Descripción", this.getDomAtr().getDesDomAtr());
		 
		 // Order by
		 report.setReportOrderBy("codDomAtr ASC");
		 
	     // PageModel de DomAtr
	     ReportVO pmDA = new ReportVO();
	     // carga de datos por cada domAtr
	     
	     pmDA.addReportDato("Código del Dominio Atributo", "codDomAtr");
	     pmDA.addReportDato("Descripción del Dominio Atributo", "desDomAtr");
	     pmDA.addReportDato("Tipo Atributo", "tipoAtributo.desTipoAtributo");
	     pmDA.addReportDato("Class For Name", "classForName");
	     pmDA.addReportDato("Estado", "estadoView");

	     ReportTableVO rtDomAtrVal = new ReportTableVO("DomAtrVal");
	     rtDomAtrVal.setTitulo("Listado de Valores del Dominio Atributo");
	     rtDomAtrVal.setReportMetodo("listDomAtrVal");  // metodo a ejecutar para llenar la tabla de DomAtrVal
		 // carga de columnas
	     rtDomAtrVal.addReportColumn("Fecha Desde", "fechaDesde");
	     rtDomAtrVal.addReportColumn("Fecha Hasta", "fechaHasta");
	     rtDomAtrVal.addReportColumn("Valor", "desValor");
	     rtDomAtrVal.addReportColumn("Descripción", "domAtr.desDomAtr");
	     
	     pmDA.getReportListTable().add(rtDomAtrVal);

	     report.getListReport().add(pmDA);
	}

}
