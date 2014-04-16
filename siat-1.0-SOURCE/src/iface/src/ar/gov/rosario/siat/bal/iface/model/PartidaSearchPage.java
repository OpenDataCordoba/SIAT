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
 * SearchPage del Partida
 * 
 * @author Tecso
 *
 */
public class PartidaSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "partidaSearchPageVO";
	
	private PartidaVO partida= new PartidaVO();
	
	// Constructores
	public PartidaSearchPage() {       
       super(BalSecurityConstants.ABM_PARTIDA);        
    }
	
	// Getters y Setters
	public PartidaVO getPartida() {
		return partida;
	}
	public void setPartida(PartidaVO partida) {
		this.partida = partida;
	}

	public String getName(){
		return NAME;
	}
		
	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Consulta de Partidas");
		 report.setReportBeanName("Partida");
		 report.setReportFileName(this.getClass().getName());
		 
	     ReportTableVO rtPartidas = new ReportTableVO("rtPartidas");
	     rtPartidas.setTitulo("Listado de Partidas");
	    	
		 report.addReportFiltro("Código", this.getPartida().getCodPartida());
		 report.addReportFiltro("Descripción", this.getPartida().getDesPartida());
		 		 
	     // carga de columnas
		
		 rtPartidas.addReportColumn("Código", "codPartida");
		 rtPartidas.addReportColumn("Descripción", "desPartida");
		 rtPartidas.addReportColumn("Estado", "estadoView");
	    
	     report.getReportListTable().add(rtPartidas);

	    }
}
