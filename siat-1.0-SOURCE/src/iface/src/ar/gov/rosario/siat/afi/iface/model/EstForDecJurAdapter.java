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
 * Adapter del EstForDecJur
 * 
 * @author tecso
 */
public class EstForDecJurAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "estForDecJurAdapterVO";
	
    private EstForDecJurVO estForDecJur = new EstForDecJurVO();
    
    private List<SiNo>           listSiNo = new ArrayList<SiNo>();
    
    // Constructores
    public EstForDecJurAdapter(){
    	super(AfiSecurityConstants.ABM_ESTFORDECJUR);
    }
    
    //  Getters y Setters
	public EstForDecJurVO getEstForDecJur() {
		return estForDecJur;
	}

	public void setEstForDecJur(EstForDecJurVO estForDecJurVO) {
		this.estForDecJur = estForDecJurVO;
	}

	public List<SiNo> getListSiNo() {
		return listSiNo;
	}

	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}
	
	public String getName(){
		return NAME;
	}
			
	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de EstForDecJur");     
		 report.setReportBeanName("EstForDecJur");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		// Order by: no 
		 
		 ReportVO reportEstForDecJur = new ReportVO();
		 reportEstForDecJur.setReportTitle("Datos del EstForDecJur");
		 // carga de datos
	     
	     //Código
		 reportEstForDecJur.addReportDato("Código", "codEstForDecJur");
		 //Descripción
		 reportEstForDecJur.addReportDato("Descripción", "desEstForDecJur");
	     
		 report.getListReport().add(reportEstForDecJur);
	
	}
	
	// View getters
}