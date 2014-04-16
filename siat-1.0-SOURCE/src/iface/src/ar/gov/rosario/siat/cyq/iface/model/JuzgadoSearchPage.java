//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.cyq.iface.util.CyqSecurityConstants;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del Juzgado
 * 
 * @author Tecso
 *
 */
public class JuzgadoSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "juzgadoSearchPageVO";
	
	private JuzgadoVO juzgado= new JuzgadoVO();
	
	private List<AbogadoVO> listAbogado = new ArrayList<AbogadoVO>(); 
	 
	public List<AbogadoVO> getListAbogado() {
		return listAbogado;
	}

	public void setListAbogado(List<AbogadoVO> listAbogado) {
		this.listAbogado = listAbogado;
	}

	// Constructores
	public JuzgadoSearchPage() {       
       super(CyqSecurityConstants.ABM_JUZGADO);        
    }
	
	// Getters y Setters
	public JuzgadoVO getJuzgado() {
		return juzgado;
	}
	public void setJuzgado(JuzgadoVO juzgado) {
		this.juzgado = juzgado;
	}
	// View getters
	
	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Consulta de Juzgados");
		 report.setReportBeanName("Juzgado");
		 report.setReportFileName(this.getClass().getName());
		 
		
		 // carga de filtros
		 report.addReportFiltro("Descripcion", this.getJuzgado().getDesJuzgado());
	
		
		 // Order by
		 //report.setReportOrderBy("desTipoObra ASC");
	     
	     ReportTableVO rtJuz = new ReportTableVO("rtJuz");
	     rtJuz.setTitulo("Listado de Juzgados");
	   
	     // carga de columnas
	    
	     rtJuz.addReportColumn("Descripción", "desJuzgado");
	     rtJuz.addReportColumn("Estado", "estadoView");
	     report.getReportListTable().add(rtJuz);

	    }
	
}
