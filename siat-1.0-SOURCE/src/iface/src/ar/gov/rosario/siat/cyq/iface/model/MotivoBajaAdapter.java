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
 * Adapter del MotivoBaja
 * 
 * @author tecso
 */
public class MotivoBajaAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "motivoBajaAdapterVO";
	
    private MotivoBajaVO motivoBaja = new MotivoBajaVO();
    
    private List<SiNo>           listSiNo = new ArrayList<SiNo>();
    
    // Constructores
    public MotivoBajaAdapter(){
    	super(CyqSecurityConstants.ABM_MOTIVOBAJA);
    }
    
    //  Getters y Setters
	public MotivoBajaVO getMotivoBaja() {
		return motivoBaja;
	}

	public void setMotivoBaja(MotivoBajaVO motivoBajaVO) {
		this.motivoBaja = motivoBajaVO;
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
		 report.setReportTitle("Reporte de  MotivoBaja");     
		 report.setReportBeanName("MotivoBaja");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		 // Order by: no 
		 
		 ReportVO reportMotivoBaja = new ReportVO();
		 reportMotivoBaja.setReportTitle("Datos del  MotivoBaja");
		 // carga de datos
	     
	     //C�digo
		 reportMotivoBaja.addReportDato("C�digo", "codMotivoBaja");
		 //Descripci�n
		 reportMotivoBaja.addReportDato("Descripci�n", "desMotivoBaja");
	     
		 report.getListReport().add(reportMotivoBaja);
	
	}
	// View getters
}
