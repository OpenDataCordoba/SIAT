//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del AgeRet
 * 
 * @author Tecso
 *
 */
public class AgeRetSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "ageRetSearchPageVO";
	
	private AgeRetVO ageRet= new AgeRetVO();
	
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	
	// Constructores
	public AgeRetSearchPage() {       
       super(GdeSecurityConstants.ABM_AGERET);        
    }

	// Getters y Setters
	public AgeRetVO getAgeRet() {
		return ageRet;
	}
	public void setAgeRet(AgeRetVO ageRet) {
		this.ageRet = ageRet;
	}           

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}
	
    public String getName(){    
		return NAME;
	}
	
	public void prepareReport(Long format) {

		ReportVO report = this.getReport(); // no instanciar una nueva
		report.setReportFormat(format);	
		report.setReportTitle("Listados de AgeRet");
		report.setReportBeanName("AgeRet");
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
		report.addReportFiltro("Descripción", this.getAgeRet().getDesAgeRet());
		

		ReportTableVO rtAgeRet = new ReportTableVO("rtAgeRet");
		rtAgeRet.setTitulo("B\u00FAsqueda de AgeRet");

		// carga de columnas
		rtAgeRet.addReportColumn("Código","codAgeRet");
		rtAgeRet.addReportColumn("Descripción", "desAgeRet");
		
		 
	    report.getReportListTable().add(rtAgeRet);

	}
	// View getters
}
