//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.esp.iface.util.EspSecurityConstants;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del LugarEvento
 * 
 * @author Tecso
 *
 */
public class LugarEventoSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "lugarEventoSearchPageVO";
	
	private LugarEventoVO lugarEvento = new LugarEventoVO();
	
	// Constructores
	public LugarEventoSearchPage() {       
       super(EspSecurityConstants.ABM_LUGAREVENTO);        
    }
	
	// Getters y Setters
	public LugarEventoVO getLugarEvento() {
		return lugarEvento;
	}
	public void setLugarEvento(LugarEventoVO lugarEvento) {
		this.lugarEvento = lugarEvento;
	}           

    public String getName(){    
		return NAME;
	}
	
	public void prepareReport(Long format) {
		ReportVO report = this.getReport(); // no instanciar una nueva
		report.setReportFormat(format);	
		report.setReportTitle("Listados de Lugares de Eventos");
		report.setReportBeanName("LugarEvento");
		report.setReportFileName(this.getClass().getName());

		// Descripción
		report.addReportFiltro("Descripción", this.getLugarEvento().getDescripcion());
		// Domicilio
		report.addReportFiltro("Domicilio", this.getLugarEvento().getDomicilio());
		

		ReportTableVO rtLugarEvento = new ReportTableVO("rtLugarEvento");
		rtLugarEvento.setTitulo("B\u00FAsqueda de Lugares de Eventos");

		// carga de columnas
		rtLugarEvento.addReportColumn("Descripción", "descripcion");
		rtLugarEvento.addReportColumn("Domicilio","domicilio");
		rtLugarEvento.addReportColumn("Factor Ocup.","factorOcupacional");
		 
	    report.getReportListTable().add(rtLugarEvento);
	}
	
}
