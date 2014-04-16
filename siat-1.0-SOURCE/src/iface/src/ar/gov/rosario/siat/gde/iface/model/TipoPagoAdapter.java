//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Adapter del TipoPago
 * 
 * @author tecso
 */
public class TipoPagoAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "tipoPagoAdapterVO";
	
    private TipoPagoVO tipoPago = new TipoPagoVO();
    
    
    // Constructores
    public TipoPagoAdapter(){
    	super(GdeSecurityConstants.ABM_TIPOPAGO);
    }
    
    //  Getters y Setters
	public TipoPagoVO getTipoPago() {
		return tipoPago;
	}

	public void setTipoPago(TipoPagoVO tipoPagoVO) {
		this.tipoPago = tipoPagoVO;
	}

	public String getName(){
		return NAME;
	}
			
	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de Tipo de Pago");     
		 report.setReportBeanName("TipoPago");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		// Order by: no 
		 
		 ReportVO reportTipoPago = new ReportVO();
		 reportTipoPago.setReportTitle("Datos del Tipo de Pago");
		 // carga de datos
	     
		 //Descripción
		 reportTipoPago.addReportDato("Descripción", "desTipoPago");
	     
		 report.getListReport().add(reportTipoPago);
	
	}
	
	// View getters
}