//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del Banco
 * 
 * @author Tecso
 *
 */
public class BancoSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "bancoSearchPageVO";
	
	private BancoVO banco= new BancoVO();
	
	// Constructores
	public BancoSearchPage() {       
       super(DefSecurityConstants.ABM_BANCO);        
    }
	
	// Getters y Setters
	public BancoVO getBanco() {
		return banco;
	}
	public void setBanco(BancoVO banco) {
		this.banco = banco;
	}
	
	public String getName(){    
			return NAME;
	}
		
	public void prepareReport(Long format) {

		ReportVO report = this.getReport(); 
		report.setReportFormat(format);	
		report.setReportTitle("Listados de Banco");
		report.setReportBeanName("Banco");
		report.setReportFileName(this.getClass().getName());
		report.setReportOrderBy("codBanco");

       //Descripción
		report.addReportFiltro("Código", this.getBanco().getCodBanco());
		report.addReportFiltro("Descripción", this.getBanco().getDesBanco());
		
    	ReportTableVO rtBanco = new ReportTableVO("rtBanco");
		rtBanco.setTitulo("B\u00FAsqueda de Banco");

		// carga de columnas
		
		rtBanco.addReportColumn("Código", "codBanco");
		rtBanco.addReportColumn("Descripción", "desBanco");
		 
	    report.getReportListTable().add(rtBanco);

	}

}
