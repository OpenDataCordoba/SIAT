//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del EstadoCuenta
 * 
 * @author Tecso
 *
 */
public class EstCueSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "estCueSearchPageVO";
	
	private EstCueVO estCue= new EstCueVO();
	
	// Constructores
	public EstCueSearchPage() {       
       super(PadSecurityConstants.ABM_ESTADOCUENTA);        
    }
	
	// Getters y Setters

    public String getName(){    
		return NAME;
	}

	public EstCueVO getEstCue() {
		return estCue;
	}

	public void setEstCue(EstCueVO estCue) {
		this.estCue = estCue;
	}
	
	public void prepareReport(Long format) {

		ReportVO report = this.getReport(); // no instanciar una nueva
		report.setReportFormat(format);	
		report.setReportTitle("Listados de Estado de Cuentas");
		report.setReportBeanName("EstCue");
		report.setReportFileName(this.getClass().getName());


       //Descripción
		report.addReportFiltro("Descripción", this.getEstCue().getDescripcion());
		

		ReportTableVO rtEstCue = new ReportTableVO("rtEstCue");
		rtEstCue.setTitulo("B\u00FAsqueda de Estados de Cuenta");

		// carga de columnas
		rtEstCue.addReportColumn("Id", "id");
		rtEstCue.addReportColumn("Descripción", "descripcion");
		
		 
	    report.getReportListTable().add(rtEstCue);
	}
	// View getters
}
