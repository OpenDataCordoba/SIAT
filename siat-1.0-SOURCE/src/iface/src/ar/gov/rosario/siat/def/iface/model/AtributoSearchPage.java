//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

public class AtributoSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "atributoSearchPageVO";
	
	private AtributoVO atributo = new AtributoVO();  
	
	public AtributoSearchPage() {
       super(DefSecurityConstants.ABM_ATRIBUTO);
    }
	
	// Getters y Setters
	public AtributoVO getAtributo() {
		return atributo;
	}

	public void setAtributo(AtributoVO atributo) {
		this.atributo = atributo;
	}
	
	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva	
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de Atributos del Siat");
		 report.setReportBeanName("Atributos");
		 report.setReportFileName(this.getClass().getName());
		 
		 // carga de filtros
		 
		 report.addReportFiltro("Código", this.getAtributo().getCodAtributo());
		 report.addReportFiltro("Descripción", this.getAtributo().getDesAtributo());
		 
		 // Order by
		 report.setReportOrderBy("desAtributo ASC");
		 
		 ReportTableVO rtAtributos = new ReportTableVO("Atributos");
		 rtAtributos.setTitulo("Listado de Atributos");
		 // carga de columnas
		 rtAtributos.addReportColumn("Código", "codAtributo");
		 rtAtributos.addReportColumn("Descripción", "desAtributo");
		 rtAtributos.addReportColumn("Tipo Atr.", "tipoAtributo.desTipoAtributo");
		 rtAtributos.addReportColumn("Dominio", "domAtr.desDomAtr");
		 rtAtributos.addReportColumn("Máscara Visual", "mascaraVisual");
		 rtAtributos.addReportColumn("Estado", "estadoView");
		 report.getReportListTable().add(rtAtributos);
	     
	}
	
}
