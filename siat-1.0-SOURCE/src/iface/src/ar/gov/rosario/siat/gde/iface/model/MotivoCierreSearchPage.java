//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del MotivoCierre
 * 
 * @author Tecso
 *
 */
public class MotivoCierreSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "motivoCierreSearchPageVO";
	
	private MotivoCierreVO motivoCierre= new MotivoCierreVO();
	
	// Constructores
	public MotivoCierreSearchPage() {       
       super(GdeSecurityConstants.ABM_MOTIVOCIERRE);        
    }
	
	// Getters y Setters
	public MotivoCierreVO getMotivoCierre() {
		return motivoCierre;
	}
	public void setMotivoCierre(MotivoCierreVO motivoCierre) {
		this.motivoCierre = motivoCierre;
	}           

    public String getName(){    
		return NAME;
	}
	
	public void prepareReport(Long format) {

		ReportVO report = this.getReport(); // no instanciar una nueva
		report.setReportFormat(format);	
		report.setReportTitle("Listados de MotivoCierre");
		report.setReportBeanName("MotivoCierre");
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

       //Descripci�n
		report.addReportFiltro("Descripcion", this.getMotivoCierre().getDesMotivo());
		

		ReportTableVO rtMotivoCierre = new ReportTableVO("rtMotivoCierre");
		rtMotivoCierre.setTitulo("B\u00FAsqueda de MotivoCierre");

		// carga de columnas
		rtMotivoCierre.addReportColumn("Descripcion", "desMotivo");
		
		 
	    report.getReportListTable().add(rtMotivoCierre);

	}
	// View getters
}
