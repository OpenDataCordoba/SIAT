//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del TipoPago
 * 
 * @author Tecso
 *
 */
public class TipoPagoSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipoPagoSearchPageVO";
	
	private TipoPagoVO tipoPago= new TipoPagoVO();
	
	// Constructores
	public TipoPagoSearchPage() {       
       super(GdeSecurityConstants.ABM_TIPOPAGO);        
    }
	
	// Getters y Setters
	public TipoPagoVO getTipoPago() {
		return tipoPago;
	}
	public void setTipoPago(TipoPagoVO tipoPago) {
		this.tipoPago = tipoPago;
	}           

    public String getName(){    
		return NAME;
	}
	
	public void prepareReport(Long format) {

		ReportVO report = this.getReport(); // no instanciar una nueva
		report.setReportFormat(format);	
		report.setReportTitle("Listados de Tipo de Pago");
		report.setReportBeanName("TipoPago");
		report.setReportFileName(this.getClass().getName());

	    //Descripción
		report.addReportFiltro("Descripción", this.getTipoPago().getDesTipoPago());

		ReportTableVO rtTipoPago = new ReportTableVO("rtTipoPago");
		rtTipoPago.setTitulo("Lista de Tipo de Pago");

		// carga de columnas
		rtTipoPago.addReportColumn("Descripción", "desTipoPago");
		 
	    report.getReportListTable().add(rtTipoPago);

	}

}
