//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.afi.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.afi.iface.util.AfiSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import coop.tecso.demoda.iface.model.ReportVO;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter del ForDecJur
 * 
 * @author tecso
 */
public class ForDecJurAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "forDecJurAdapterVO";
	
    private ForDecJurVO forDecJur = new ForDecJurVO();
    
    private List<SiNo>   listSiNo = new ArrayList<SiNo>();
    
    private Boolean paramRegimenEspecial = false;
    
    // Constructores
    public ForDecJurAdapter(){
    	super(AfiSecurityConstants.ABM_FORDECJUR);
    }
    
    //  Getters y Setters
	public ForDecJurVO getForDecJur() {
		return forDecJur;
	}

	public void setForDecJur(ForDecJurVO forDecJurVO) {
		this.forDecJur = forDecJurVO;
	}

	public List<SiNo> getListSiNo() {
		return listSiNo;
	}

	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}
	
	public Boolean getParamRegimenEspecial() {
		return paramRegimenEspecial;
	}

	public void setParamRegimenEspecial(Boolean paramRegimenEspecial) {
		this.paramRegimenEspecial = paramRegimenEspecial;
	}

	public String getName(){
		return NAME;
	}
			
	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de ForDecJur");     
		 report.setReportBeanName("ForDecJur");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		// Order by: no 
		 
		 ReportVO reportForDecJur = new ReportVO();
		 reportForDecJur.setReportTitle("Datos del ForDecJur");
		 // carga de datos
	     
	     //Código
		 reportForDecJur.addReportDato("Código", "codForDecJur");
		 //Descripción
		 reportForDecJur.addReportDato("Descripción", "desForDecJur");
	     
		 report.getListReport().add(reportForDecJur);
	
	}
	
	// View getters
}