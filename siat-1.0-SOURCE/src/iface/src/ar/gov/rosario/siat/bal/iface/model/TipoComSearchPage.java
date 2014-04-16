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
 * SearchPage del TipoCom
 * 
 * @author Tecso
 *
 */
public class TipoComSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipoComSearchPageVO";
	
	private TipoComVO tipoCom= new TipoComVO();
	
	// Constructores
	public TipoComSearchPage() {       
       super(BalSecurityConstants.ABM_TIPOCOM);        
    }
	
	// Getters y Setters
	public TipoComVO getTipoCom() {
		return tipoCom;
	}
	public void setTipoCom(TipoComVO tipoCom) {
		this.tipoCom = tipoCom;
	}           

    public String getName(){    
		return NAME;
	}
	
	public void prepareReport(Long format) {

		ReportVO report = this.getReport(); // no instanciar una nueva
		report.setReportFormat(format);	
		report.setReportTitle("Listados de TipoCom");
		report.setReportBeanName("TipoCom");
		report.setReportFileName(this.getClass().getName());

	    //Descripción
		report.addReportFiltro("Descripción", this.getTipoCom().getDescripcion());

		ReportTableVO rtTipoCom = new ReportTableVO("rtTipoCom");
		rtTipoCom.setTitulo("Lista de Tipo de Compensación");

		// carga de columnas
		rtTipoCom.addReportColumn("Descripción", "descripcion");
		 
	    report.getReportListTable().add(rtTipoCom);

	}

}
