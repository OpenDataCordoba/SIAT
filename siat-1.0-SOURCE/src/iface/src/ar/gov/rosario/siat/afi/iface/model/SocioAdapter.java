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
 * Adapter del Socio
 * 
 * @author tecso
 */
public class SocioAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "socioAdapterVO";
	
    private SocioVO socio = new SocioVO();
    
    private List<SiNo>   listSiNo = new ArrayList<SiNo>();
    
    private Boolean paramDatosDomicilio = false;
    
    // Constructores
    public SocioAdapter(){
    	super(AfiSecurityConstants.ABM_SOCIO);
    }
    
    //  Getters y Setters
	public SocioVO getSocio() {
		return socio;
	}

	public void setSocio(SocioVO socioVO) {
		this.socio = socioVO;
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
		 report.setReportTitle("Reporte de Socio");     
		 report.setReportBeanName("Socio");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		// Order by: no 
		 
		 ReportVO reportSocio = new ReportVO();
		 reportSocio.setReportTitle("Datos del Socio");
		 // carga de datos
	     
	     //Código
		 reportSocio.addReportDato("Código", "codSocio");
		 //Descripción
		 reportSocio.addReportDato("Descripción", "desSocio");
	     
		 report.getListReport().add(reportSocio);
	}

	
	// View getters
	public Boolean getParamDatosDomicilio() {
		return paramDatosDomicilio;
	}
	
	public void setParamDatosDomicilio(Boolean paramDatosDomicilio) {
		this.paramDatosDomicilio = paramDatosDomicilio;
	}
	
	
}