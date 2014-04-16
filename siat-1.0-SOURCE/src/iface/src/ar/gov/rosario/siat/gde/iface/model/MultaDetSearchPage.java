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
 * SearchPage del MultaDet
 * 
 * @author Tecso
 *
 */
public class MultaDetSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "multaDetSearchPageVO";
	
	private MultaDetVO multaDet= new MultaDetVO();
	
	// Constructores
	public MultaDetSearchPage() {       
       super(GdeSecurityConstants.ABM_MULTADET);        
    }
	
	// Getters y Setters
	public MultaDetVO getMultaDet() {
		return multaDet;
	}
	public void setMultaDet(MultaDetVO multaDet) {
		this.multaDet = multaDet;
	}           

    public String getName(){    
		return NAME;
	}
	
	public void prepareReport(Long format) {

		ReportVO report = this.getReport(); // no instanciar una nueva
		report.setReportFormat(format);	
		report.setReportTitle("Listados de MultaDet");
		report.setReportBeanName("MultaDet");
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

		
		ReportTableVO rtMultaDet = new ReportTableVO("rtMultaDet");
		rtMultaDet.setTitulo("B\u00FAsqueda de MultaDet");

		// carga de columnas
		
		 
	    report.getReportListTable().add(rtMultaDet);

	}
	// View getters
}
