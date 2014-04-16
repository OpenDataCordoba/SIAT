//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Adapter del RecAli
 * 
 * @author tecso
 */
public class RecAliAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "recAliAdapterVO";
	
    private RecAliVO recAli = new RecAliVO();
    
    private List<RecTipAliVO> listRecTipAli = new ArrayList<RecTipAliVO>();
    
    // Constructores
    public RecAliAdapter(){
    	super(DefSecurityConstants.ABM_RECALI);
    }
    
    //  Getters y Setters
	public RecAliVO getRecAli() {
		return recAli;
	}

	public void setRecAli(RecAliVO recAliVO) {
		this.recAli = recAliVO;
	}

	public List<RecTipAliVO> getListRecTipAli() {
		return listRecTipAli;
	}

	public void setListRecTipAli(List<RecTipAliVO> listRecTipAli) {
		this.listRecTipAli = listRecTipAli;
	}
	
	public String getName(){
		return NAME;
	}
			
	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de RecAli");     
		 report.setReportBeanName("RecAli");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		// Order by: no 
		 
		 ReportVO reportRecAli = new ReportVO();
		 reportRecAli.setReportTitle("Datos del RecAli");
		 // carga de datos
	     
	     //Código
		 reportRecAli.addReportDato("Código", "codRecAli");
		 //Descripción
		 reportRecAli.addReportDato("Descripción", "desRecAli");
	     
		 report.getListReport().add(reportRecAli);
	
	}
	
	// View getters
}