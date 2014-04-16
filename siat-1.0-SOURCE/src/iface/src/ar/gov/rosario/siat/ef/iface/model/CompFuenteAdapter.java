//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Adapter del CompFuente
 * 
 * @author tecso
 */
public class CompFuenteAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "compFuenteAdapterVO";
	
    private CompFuenteVO compFuente = new CompFuenteVO();
    
    List<PlaFueDatVO> listPlaFueDat = new ArrayList<PlaFueDatVO>();
    
    // Constructores
    public CompFuenteAdapter(){
    	super(EfSecurityConstants.ABM_COMPFUENTE);
    }
    
    //  Getters y Setters
	public CompFuenteVO getCompFuente() {
		return compFuente;
	}

	public void setCompFuente(CompFuenteVO compFuenteVO) {
		this.compFuente = compFuenteVO;
	}

	public List<PlaFueDatVO> getListPlaFueDat() {
		return listPlaFueDat;
	}

	public void setListPlaFueDat(List<PlaFueDatVO> listPlaFueDat) {
		this.listPlaFueDat = listPlaFueDat;
	}

	public String getName(){
		return NAME;
	}
			
	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de CompFuente");     
		 report.setReportBeanName("CompFuente");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		// Order by: no 
		 
		 ReportVO reportCompFuente = new ReportVO();
		 reportCompFuente.setReportTitle("Datos del CompFuente");
		 // carga de datos
	     
	     //C�digo
		 reportCompFuente.addReportDato("C�digo", "codCompFuente");
		 //Descripci�n
		 reportCompFuente.addReportDato("Descripci�n", "desCompFuente");
	     
		 report.getListReport().add(reportCompFuente);
	
	}
	
	// View getters
}
