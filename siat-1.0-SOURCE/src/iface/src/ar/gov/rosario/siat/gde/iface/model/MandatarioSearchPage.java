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
 * SearchPage del Mandatario
 * 
 * @author Tecso
 *
 */
public class MandatarioSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "mandatarioSearchPageVO";
	
	private MandatarioVO mandatario= new MandatarioVO();
	
	// Constructores
	public MandatarioSearchPage() {       
       super(GdeSecurityConstants.ABM_MANDATARIO);        
    }
	
	// Getters y Setters
	public MandatarioVO getMandatario() {
		return mandatario;
	}
	public void setMandatario(MandatarioVO mandatario) {
		this.mandatario = mandatario;
	}           

    public String getName(){    
		return NAME;
	}
	
	public void prepareReport(Long format) {

		ReportVO report = this.getReport(); // no instanciar una nueva
		report.setReportFormat(format);	
		report.setReportTitle("Listados de Mandatario");
		report.setReportBeanName("Mandatario");
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

		//Código
		//report.addReportFiltro("Código", this.getMandatario().getCodMandatario());
       //Descripción
		report.addReportFiltro("Descripción", this.getMandatario().getDescripcion());
		

		ReportTableVO rtMandatario = new ReportTableVO("rtMandatario");
		rtMandatario.setTitulo("B\u00FAsqueda de Mandatario");

		// carga de columnas
		rtMandatario.addReportColumn("Descripción", "descripcion");
		
		 
	    report.getReportListTable().add(rtMandatario);

	}
	// View getters
}
