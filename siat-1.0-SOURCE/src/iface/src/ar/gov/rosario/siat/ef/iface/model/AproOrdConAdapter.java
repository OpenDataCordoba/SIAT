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
 * Adapter del AproOrdCon 
 * 
 * @author tecso
 */
public class AproOrdConAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "aproOrdConAdapterVO";
	
    private AproOrdConVO aproOrdCon = new AproOrdConVO();
    
    private List<EstadoOrdenVO>  listEstadoOrden = new ArrayList<EstadoOrdenVO>();
    
    private Boolean aprobarOrdenBussEnabled = false;
    private Boolean aplicarAjusteBussEnabled = false;
    
    private String aplicarAjuste = "";
    
    // Constructores
    public AproOrdConAdapter(){
    	super(EfSecurityConstants.ABM_APROORDCON);
    }
    
    //  Getters y Setters
	public AproOrdConVO getAproOrdCon() {
		return aproOrdCon;
	}

	public void setAproOrdCon(AproOrdConVO aproOrdConVO) {
		this.aproOrdCon = aproOrdConVO;
	}
	
	public List<EstadoOrdenVO> getListEstadoOrden() {
		return listEstadoOrden;
	}

	public void setListEstadoOrden(List<EstadoOrdenVO> listEstadoOrden) {
		this.listEstadoOrden = listEstadoOrden;
	}

	public void setAprobarOrdenBussEnabled(Boolean aprobarOrdenBussEnabled) {
		this.aprobarOrdenBussEnabled = aprobarOrdenBussEnabled;
	}

	public Boolean getAprobarOrdenBussEnabled() {
		return aprobarOrdenBussEnabled;
	}
	
	public Boolean getAplicarAjusteBussEnabled() {
		return aplicarAjusteBussEnabled;
	}

	public void setAplicarAjusteBussEnabled(Boolean aplicarAjusteBussEnabled) {
		this.aplicarAjusteBussEnabled = aplicarAjusteBussEnabled;
	}

	public String getAprobarOrdenEnabled() {
		return Boolean.toString(aprobarOrdenBussEnabled);
	}
	
	public String getAplicarAjusteEnabled() {
		return Boolean.toString(aplicarAjusteBussEnabled);
	}
	
	public String getName(){
		return NAME;
	}
			
	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de AproOrdCon");     
		 report.setReportBeanName("AproOrdCon");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		// Order by: no 
		 
		 ReportVO reportAproOrdCon = new ReportVO();
		 reportAproOrdCon.setReportTitle("Datos del AproOrdCon");
		 // carga de datos
	     
	     //C�digo
		 reportAproOrdCon.addReportDato("C�digo", "codAproOrdCon");
		 //Descripci�n
		 reportAproOrdCon.addReportDato("Descripci�n", "desAproOrdCon");
	     
		 report.getListReport().add(reportAproOrdCon);
	
	}
	// View getters

	public void setAplicarAjuste(String aplicarAjuste) {
		this.aplicarAjuste = aplicarAjuste;
	}

	public String getAplicarAjuste() {
		return aplicarAjuste;
	}
}
