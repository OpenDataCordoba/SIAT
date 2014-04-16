//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.AreaVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del PerCob
 * 
 * @author Tecso
 *
 */
public class PerCobSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "perCobSearchPageVO";
	
	private PerCobVO perCob= new PerCobVO();
	private List<AreaVO> listArea = new ArrayList<AreaVO>();
	
	// Constructores
	public PerCobSearchPage() {       
       super(GdeSecurityConstants.ABM_PERCOB);        
    }
	
	// Getters y Setters
	public PerCobVO getPerCob() {
		return perCob;
	}
	public void setPerCob(PerCobVO perCob) {
		this.perCob = perCob;
	}           

    public String getName(){    
		return NAME;
	}
    
    
	public List<AreaVO> getListArea() {
		return listArea;
	}

	public void setListArea(List<AreaVO> listArea) {
		this.listArea = listArea;
	}

	public void prepareReport(Long format) {

		ReportVO report = this.getReport(); // no instanciar una nueva
		report.setReportFormat(format);	
		report.setReportTitle("Listados de PerCob");
		report.setReportBeanName("PerCob");
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

		
       //Descripción
		report.addReportFiltro("Descripción", this.getPerCob().getNombreApellido());
		

		ReportTableVO rtPerCob = new ReportTableVO("rtPerCob");
		rtPerCob.setTitulo("B\u00FAsqueda de PerCob");

		// carga de columnas
		
		rtPerCob.addReportColumn("Descripción", "nombreApellido");
		
		 
	    report.getReportListTable().add(rtPerCob);

	}
	// View getters
}
