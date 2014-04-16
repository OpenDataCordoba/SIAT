//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.cyq.iface.util.CyqSecurityConstants;
import coop.tecso.demoda.iface.model.ReportVO;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter del MotivoResInf
 * 
 * @author tecso
 */
public class MotivoResInfAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "motivoResInfAdapterVO";
	
    private MotivoResInfVO motivoResInf = new MotivoResInfVO();
    
    private List<SiNo>           listSiNo = new ArrayList<SiNo>();
    
    // Constructores
    public MotivoResInfAdapter(){
    	super(CyqSecurityConstants.ABM_MOTIVORESINF);
    }
    
    //  Getters y Setters
	public MotivoResInfVO getMotivoResInf() {
		return motivoResInf;
	}

	public void setMotivoResInf(MotivoResInfVO motivoResInfVO) {
		this.motivoResInf = motivoResInfVO;
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
		 report.setReportTitle("Reporte de MotivoResInf");     
		 report.setReportBeanName("MotivoResInf");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		// Order by: no 
		 
		 ReportVO reportMotivoResInf = new ReportVO();
		 reportMotivoResInf.setReportTitle("Datos del MotivoResInf");
		 // carga de datos
	     
	     //C�digo
		 reportMotivoResInf.addReportDato("C�digo", "codMotivoResInf");
		 //Descripci�n
		 reportMotivoResInf.addReportDato("Descripci�n", "desMotivoResInf");
	     
		 report.getListReport().add(reportMotivoResInf);
	
	}
	
	// View getters
}
