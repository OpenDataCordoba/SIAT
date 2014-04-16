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
 * Adapter del OtrosPagos
 * 
 * @author tecso
 */
public class OtrosPagosAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "otrosPagosAdapterVO";
	
    private OtrosPagosVO otrosPagos = new OtrosPagosVO();
    
    private List<SiNo>           listSiNo = new ArrayList<SiNo>();
    
    // Constructores
    public OtrosPagosAdapter(){
    	super(AfiSecurityConstants.ABM_OTROSPAGOS);
    }
    
    //  Getters y Setters
	public OtrosPagosVO getOtrosPagos() {
		return otrosPagos;
	}

	public void setOtrosPagos(OtrosPagosVO otrosPagosVO) {
		this.otrosPagos = otrosPagosVO;
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
		 report.setReportTitle("Reporte de OtrosPagos");     
		 report.setReportBeanName("OtrosPagos");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		// Order by: no 
		 
		 ReportVO reportOtrosPagos = new ReportVO();
		 reportOtrosPagos.setReportTitle("Datos del OtrosPagos");
		 // carga de datos
	     
	     //Código
		 reportOtrosPagos.addReportDato("Código", "codOtrosPagos");
		 //Descripción
		 reportOtrosPagos.addReportDato("Descripción", "desOtrosPagos");
	     
		 report.getListReport().add(reportOtrosPagos);
	
	}
	
	// View getters
}