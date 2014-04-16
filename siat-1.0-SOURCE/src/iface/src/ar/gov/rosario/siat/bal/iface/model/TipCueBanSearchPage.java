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
 * SearchPage del TipCueBan
 * 
 * @author Tecso
 *
 */
public class TipCueBanSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipCueBanSearchPageVO";
	
	private TipCueBanVO tipCueBan= new TipCueBanVO();
	
	// Constructores
	public TipCueBanSearchPage() {       
       super(BalSecurityConstants.ABM_TIPCUEBAN);        
    }
	
	// Getters y Setters
	public TipCueBanVO getTipCueBan() {
		return tipCueBan;
	}
	public void setTipCueBan(TipCueBanVO tipCueBan) {
		this.tipCueBan = tipCueBan;
	}
	 public String getName(){    
			return NAME;
		}
		
		public void prepareReport(Long format) {

			ReportVO report = this.getReport(); // no instanciar una nueva
			report.setReportFormat(format);	
			report.setReportTitle("Listados de Tipo Cuenta Bancaria");
			report.setReportBeanName("TipCueBan");
			report.setReportFileName(this.getClass().getName());

	
	       //Descripción
			report.addReportFiltro("Descripción", this.getTipCueBan().getDescripcion());
			
        	ReportTableVO rtTipCueBan = new ReportTableVO("rtTipCueBan");
			rtTipCueBan.setTitulo("B\u00FAsqueda de Tipo Cuenta Bancaria");

			// carga de columnas
			
			rtTipCueBan.addReportColumn("Descripción", "descripcion");
			rtTipCueBan.addReportColumn("Estado", "estadoView");
			 
		    report.getReportListTable().add(rtTipCueBan);

		}
	// View getters
}
