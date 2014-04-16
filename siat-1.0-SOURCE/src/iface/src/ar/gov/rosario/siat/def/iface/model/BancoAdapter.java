//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Adapter del Banco
 * 
 * @author tecso
 */
public class BancoAdapter extends SiatAdapterModel{

	private static final long serialVersionUID = 1L;

	public static final String NAME = "BancoAdapterVO";
	
    private BancoVO Banco = new BancoVO();
    
    // Constructores
    public BancoAdapter(){
    	super(DefSecurityConstants.ABM_BANCO);
    }
    
    //  Getters y Setters
	public BancoVO getBanco() {
		return Banco;
	}
	public void setBanco(BancoVO BancoVO) {
		this.Banco = BancoVO;
	}

	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport();
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de Banco");     
		 report.setReportBeanName("Banco");
		 report.setReportFileName(this.getClass().getName());
		 
		 ReportVO reportBanco = new ReportVO();
		 reportBanco.setReportTitle("Datos de Banco");
	
		 //Codigo
		 reportBanco.addReportDato("Código", "codBanco");
	     //Descripcion
		 reportBanco.addReportDato("Descripción", "desBanco");
	     
		 report.getListReport().add(reportBanco);	
	}
	
}
