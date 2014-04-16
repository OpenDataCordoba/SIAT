//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del DesGen
 * 
 * @author Tecso
 *
 */
public class DesGenSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "desGenSearchPageVO";
	
	private DesGenVO desGen= new DesGenVO();
	
	private List<DesGenVO> listDesGen;
	
	
	// Constructores
	public DesGenSearchPage() {       
       super(GdeSecurityConstants.ABM_DESGEN);        
    }
	
	// Getters y Setters
	
	public DesGenVO getDesGen() {
		return desGen;
	}
	public void setDesGen(DesGenVO desGen) {
		this.desGen = desGen;
	}

	public List<DesGenVO> getListDesGen() {
		return listDesGen;
	}

	public void setListDesGen(List<DesGenVO> listDesGen) {
		this.listDesGen = listDesGen;
	}

	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Consulta de Descuento General");
		 report.setReportBeanName("DesGen");
		 report.setReportFileName(this.getClass().getName());
				 
	     ReportTableVO rtDes = new ReportTableVO("rtDes");
	     rtDes.setTitulo("Listado de Descuento General");
	   
	     // carga de columnas
	     rtDes.addReportColumn("Descripción", "desDesGen");
	     rtDes.addReportColumn("Porcentaje", "porDesString");
	     rtDes.addReportColumn("Estado", "estadoView");
	     report.getReportListTable().add(rtDes);

	    }
		
	
}
