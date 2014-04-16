//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Adapter del TipoCom
 * 
 * @author tecso
 */
public class TipoComAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "tipoComAdapterVO";
	
    private TipoComVO tipoCom = new TipoComVO();
    
    
    // Constructores
    public TipoComAdapter(){
    	super(BalSecurityConstants.ABM_TIPOCOM);
    }
    
    //  Getters y Setters
	public TipoComVO getTipoCom() {
		return tipoCom;
	}

	public void setTipoCom(TipoComVO tipoComVO) {
		this.tipoCom = tipoComVO;
	}

	public String getName(){
		return NAME;
	}
			
	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de TipoCom");     
		 report.setReportBeanName("TipoCom");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		// Order by: no 
		 
		 ReportVO reportTipoCom = new ReportVO();
		 reportTipoCom.setReportTitle("Datos del TipoCom");
		 // carga de datos
	     
		 //Descripción
		 reportTipoCom.addReportDato("Descripción", "desTipoCom");
	     
		 report.getListReport().add(reportTipoCom);
	
	}
	
	// View getters
}