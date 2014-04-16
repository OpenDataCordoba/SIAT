//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Adapter del PlaFueDatCol
 * 
 * @author tecso
 */
public class PlaFueDatColAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "plaFueDatColAdapterVO";
	
    private PlaFueDatColVO plaFueDatCol = new PlaFueDatColVO();
    
    private boolean sumaEnTotalChecked;
    
    private boolean ocultaChecked;
    
    // Constructores
    public PlaFueDatColAdapter(){
    	super(EfSecurityConstants.ABM_PLAFUEDATCOL);
    }
    
    //  Getters y Setters
	public PlaFueDatColVO getPlaFueDatCol() {
		return plaFueDatCol;
	}

	public void setPlaFueDatCol(PlaFueDatColVO plaFueDatColVO) {
		this.plaFueDatCol = plaFueDatColVO;
	}

	public boolean getSumaEnTotalChecked() {
		return sumaEnTotalChecked;
	}

	public void setSumaEnTotalChecked(boolean sumaEnTotalChecked) {
		this.sumaEnTotalChecked = sumaEnTotalChecked;
	}

	public boolean getOcultaChecked() {
		return ocultaChecked;
	}

	public void setOcultaChecked(boolean ocultaChecked) {
		this.ocultaChecked = ocultaChecked;
	}

	public String getName(){
		return NAME;
	}
			
	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de PlaFueDatCol");     
		 report.setReportBeanName("PlaFueDatCol");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		// Order by: no 
		 
		 ReportVO reportPlaFueDatCol = new ReportVO();
		 reportPlaFueDatCol.setReportTitle("Datos del PlaFueDatCol");
		 // carga de datos
	     
	     //C�digo
		 reportPlaFueDatCol.addReportDato("C�digo", "codPlaFueDatCol");
		 //Descripci�n
		 reportPlaFueDatCol.addReportDato("Descripci�n", "desPlaFueDatCol");
	     
		 report.getListReport().add(reportPlaFueDatCol);
	
	}
	
	// View getters
}
