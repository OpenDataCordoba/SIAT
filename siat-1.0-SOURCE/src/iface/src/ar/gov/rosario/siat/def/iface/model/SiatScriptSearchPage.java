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
 * SearchPage del ${Bean}
 * 
 * @author Tecso
 *
 */
public class SiatScriptSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "siatScriptSearchPageVO";
	
	private SiatScriptVO siatScript= new SiatScriptVO();
	
	// Constructores
	public SiatScriptSearchPage() {       
       super(DefSecurityConstants.ABM_SIATSCRIPT);        
    }
	
	// Getters y Setters
	public SiatScriptVO getSiatScript() {
		return siatScript;
	}
	public void setSiatScript(SiatScriptVO siatScript) {
		this.siatScript = siatScript;
	}           

    public String getName(){    
		return NAME;
	}
	
	public void prepareReport(Long format) {

		ReportVO report = this.getReport(); // no instanciar una nueva
		report.setReportFormat(format);	
		report.setReportTitle("Listados de SiatScript");
		report.setReportBeanName("SiatScript");
		report.setReportFileName(this.getClass().getName());

        /* Codigo de ejemplo para mostrar filtros de Combos en los imprimir
		String desRecurso = "";

		RecursoVO recursoVO = (RecursoVO) ModelUtil.getBussImageModelByIdForList(
				this.getReclamo().getRecurso().getId(),
				this.getListRecurso());
		if (recursoVO != null){
			desRecurso = recursoVO.getDesRecurso();
		}
		report.addReportFiltro("Recurso", desRecurso);*/

		//C�digo
		report.addReportFiltro("C�digo", this.getSiatScript().getCodigo());
       //Descripci�n
		report.addReportFiltro("Descripci�n", this.getSiatScript().getDescripcion());
		

		ReportTableVO rtSiatScript = new ReportTableVO("rtSiatScript");
		rtSiatScript.setTitulo("B\u00FAsqueda de SiatScript");

		// carga de columnas
		rtSiatScript.addReportColumn("C�digo","codigo");
		rtSiatScript.addReportColumn("Descripci�n", "descripcion");
		
		 
	    report.getReportListTable().add(rtSiatScript);

	}
	// View getters
}
