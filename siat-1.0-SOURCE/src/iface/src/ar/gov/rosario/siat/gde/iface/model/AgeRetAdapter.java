//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.model.ReportVO;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter del Agente de Retención
 * 
 * @author tecso
 */
public class AgeRetAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "ageRetAdapterVO";
	
    private AgeRetVO ageRet = new AgeRetVO();
    
    private List<SiNo>           listSiNo = new ArrayList<SiNo>();
    
    private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
    
    // Constructores
    public AgeRetAdapter(){
    	super(GdeSecurityConstants.ABM_AGERET);
    }
    
    //  Getters y Setters
	public AgeRetVO getAgeRet() {
		return ageRet;
	}

	public void setAgeRet(AgeRetVO ageRetVO) {
		this.ageRet = ageRetVO;
	}

	public List<SiNo> getListSiNo() {
		return listSiNo;
	}

	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
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
		 report.setReportTitle("Reporte de AgeRet");     
		 report.setReportBeanName("AgeRet");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		// Order by: no 
		 
		 ReportVO reportAgeRet = new ReportVO();
		 reportAgeRet.setReportTitle("Datos del AgeRet");
		 // carga de datos
	     
	     //Código
		 reportAgeRet.addReportDato("Código", "codAgeRet");
		 //Descripción
		 reportAgeRet.addReportDato("Descripción", "desAgeRet");
	     
		 report.getListReport().add(reportAgeRet);
	
	}
	
	// View getters
}