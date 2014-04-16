//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.fra.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.fra.iface.util.FraSecurityConstants;
import coop.tecso.demoda.iface.model.Modulo;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del Frase
 * 
 * @author Tecso
 *
 */
public class FraseSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "fraseSearchPageVO";
	
	private FraseVO frase= new FraseVO();
	private Modulo modulo = Modulo.SELECCIONAR;
	private List<Modulo> listModulo = new ArrayList<Modulo>();
	
	// Constructores
	public FraseSearchPage() {       
       super(FraSecurityConstants.ABM_FRASE);        
    }
	
	// Getters y Setters
	public FraseVO getFrase() {
		return frase;
	}
	public void setFrase(FraseVO frase) {
		this.frase = frase;
	}

	public Modulo getModulo() {
		return modulo;
	}

	public void setModulo(Modulo modulo) {
		this.modulo = modulo;
	}

	public List<Modulo> getListModulo() {
		return listModulo;
	}

	public void setListModulo(List<Modulo> listModulo) {
		this.listModulo = listModulo;
	}

	// View getters
	public String getPublicarEnabled() {
		return SiatBussImageModel.hasEnabledFlag(FraSecurityConstants.ABM_FRASE, FraSecurityConstants.MTD_PUBLICAR);
	}
	
	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Consulta de Frases");
		 report.setReportBeanName("Frase");
		 report.setReportFileName(this.getClass().getName());
		 
		 
		// recurso
		
	
		 report.addReportFiltro("Modulo", this.getModulo().getValue());
		 		 
		 
		 // carga de filtros
	     report.addReportFiltro("Pagina", this.getFrase().getPagina());
		 
	     report.addReportFiltro("Etiqueta", this.getFrase().getEtiqueta());
	
		 report.addReportFiltro("Valor Publicado", this.getFrase().getValorPublico());
		 
		 report.addReportFiltro("Descripci\u00F3n", this.getFrase().getDesFrase());
		 
		 // Order by
		 report.setReportOrderBy("modulo ASC");
	     
	     ReportTableVO rtFra = new ReportTableVO("rtFra");
	     rtFra.setTitulo("B\u00FAsqueda de Frases");
	   
	     // carga de columnas
	    
	     rtFra.addReportColumn("M\u00F3dulo-Pagina-Etiqueta", "descReport");
	   	 rtFra.addReportColumn("Valor Publicado", "valorPublico");
	    // rtFra.addReportColumn("Descripci\u00F3n", "desFrase");
		 
	     report.getReportListTable().add(rtFra);

	    }


   }
