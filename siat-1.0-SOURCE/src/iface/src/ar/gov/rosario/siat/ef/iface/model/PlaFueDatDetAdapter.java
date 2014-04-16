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
public class PlaFueDatDetAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "plaFueDatDetAdapterVO";
	
    private PlaFueDatDetVO plaFueDatDet = new PlaFueDatDetVO();

    private Integer periodoDesde;
    private Integer anioDesde;
    
    private Integer periodoHasta;
    private Integer anioHasta;

    // Constructores
    public PlaFueDatDetAdapter(){
    	super(EfSecurityConstants.ABM_PLAFUEDATDET);
    }
    
    //  Getters y Setters
    
	public String getName(){
		return NAME;
	}
			
	public PlaFueDatDetVO getPlaFueDatDet() {
		return plaFueDatDet;
	}

	public void setPlaFueDatDet(PlaFueDatDetVO plaFueDatDet) {
		this.plaFueDatDet = plaFueDatDet;
	}

	public Integer getPeriodoDesde() {
		return periodoDesde;
	}

	public void setPeriodoDesde(Integer periodoDesde) {
		this.periodoDesde = periodoDesde;
	}

	public Integer getAnioDesde() {
		return anioDesde;
	}

	public void setAnioDesde(Integer anioDesde) {
		this.anioDesde = anioDesde;
	}

	public Integer getPeriodoHasta() {
		return periodoHasta;
	}

	public void setPeriodoHasta(Integer periodoHasta) {
		this.periodoHasta = periodoHasta;
	}

	public Integer getAnioHasta() {
		return anioHasta;
	}

	public void setAnioHasta(Integer anioHasta) {
		this.anioHasta = anioHasta;
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
