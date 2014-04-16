//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del TipoCob
 * 
 * @author Tecso
 *
 */
public class TipoCobSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipoCobSearchPageVO";
	
	private TipoCobVO tipoCob= new TipoCobVO();
	
	// Constructores
	public TipoCobSearchPage() {       
       super(BalSecurityConstants.ABM_TIPOCOB);        
    }
	
	// Getters y Setters
	public TipoCobVO getTipoCob() {
		return tipoCob;
	}
	public void setTipoCob(TipoCobVO tipoCob) {
		this.tipoCob = tipoCob;
	}           

    public String getName(){    
		return NAME;
	}
	
	public void prepareReport(Long format) {

		ReportVO report = this.getReport(); // no instanciar una nueva
		report.setReportFormat(format);	
		report.setReportTitle("Listados de Tipo de Cobranza");
		report.setReportBeanName("TipoCob");
		report.setReportFileName(this.getClass().getName());

	   //Descripción
		report.addReportFiltro("Descripción", this.getTipoCob().getDescripcion());
		
		ReportTableVO rtTipoCob = new ReportTableVO("rtTipoCob");
		rtTipoCob.setTitulo("Lista de Tipo de Cobranza");

		// carga de columnas
		rtTipoCob.addReportColumn("Cod. Columna", "codColumna");
		rtTipoCob.addReportColumn("Descripción", "descripcion");
		rtTipoCob.addReportColumn("Partida Cta Pte.", "partida.desPartida");
		 
	    report.getReportListTable().add(rtTipoCob);
	}
	// View getters

}
