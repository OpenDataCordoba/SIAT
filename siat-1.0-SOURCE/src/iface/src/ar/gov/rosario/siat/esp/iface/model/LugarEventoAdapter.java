//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.esp.iface.util.EspSecurityConstants;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Adapter del LugarEvento
 * 
 * @author tecso
 */
public class LugarEventoAdapter extends SiatAdapterModel {
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "lugarEventoAdapterVO";
	
    private LugarEventoVO lugarEvento = new LugarEventoVO();
    
    // Constructores
    public LugarEventoAdapter(){
    	super(EspSecurityConstants.ABM_LUGAREVENTO);
    }
    
    //  Getters y Setters
	public LugarEventoVO getLugarEvento() {
		return lugarEvento;
	}

	public void setLugarEvento(LugarEventoVO lugarEventoVO) {
		this.lugarEvento = lugarEventoVO;
	}

	public String getName(){
		return NAME;
	}
			
	public void prepareReport(Long format) {
		 ReportVO report = this.getReport(); 
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de Lugar del Evento");     
		 report.setReportBeanName("LugarEvento");
		 report.setReportFileName(this.getClass().getName());
		 		 
		 ReportVO reportLugarEvento = new ReportVO();
		 reportLugarEvento.setReportTitle("Datos del Lugar del Evento");
	     
		 // Descripción
		 reportLugarEvento.addReportDato("Descripción", "descripcion");
		 // Domicilio
		 reportLugarEvento.addReportDato("Domicilio", "domicilio");
		 // Factor Ocupacional
		 reportLugarEvento.addReportDato("factorOcupacional", "factorOcupacional");
	     
		 report.getListReport().add(reportLugarEvento);
	}
	
}
