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
 * SearchPage del Parametro
 * 
 * @author Tecso
 *
 */
public class ParametroSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "parametroSearchPageVO";
	
	private ParametroVO parametro= new ParametroVO();
	
	// Constructores
	public ParametroSearchPage() {       
       super(DefSecurityConstants.ABM_PARAMETRO);        
    }
	
	// Getters y Setters
	public ParametroVO getParametro() {
		return parametro;
	}
	public void setParametro(ParametroVO parametro) {
		this.parametro = parametro;
	}

	// View getters
	
	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de Parámetros");
		 report.setReportBeanName("Parametros");
		 report.setReportFileName(this.getClass().getName());
		 
		 // carga de filtros
		 report.addReportFiltro("Código", this.getParametro().getCodParam());
		 report.addReportFiltro("Descripción", this.getParametro().getDesParam());
		 
		 // Order by
		 report.setReportOrderBy("codParam ASC");
		 
	     ReportTableVO rtParametro = new ReportTableVO("Parametro");
	     rtParametro.setTitulo("Listado de Parámetros");
	     
		 // carga de columnas
	     rtParametro.addReportColumn("Código", "codParam");
	     rtParametro.addReportColumn("Descripción", "desParam");
	     rtParametro.addReportColumn("Valor", "valor");
	     
	     report.getReportListTable().add(rtParametro);
	}

}
