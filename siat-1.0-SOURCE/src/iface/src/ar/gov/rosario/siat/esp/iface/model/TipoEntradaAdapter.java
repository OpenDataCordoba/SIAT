//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.esp.iface.util.EspSecurityConstants;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Adapter del TipoEntrada
 * 
 * @author tecso
 */
public class TipoEntradaAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "tipoEntradaAdapterVO";
	
    private TipoEntradaVO tipoEntrada = new TipoEntradaVO();
    
    
    // Constructores
    public TipoEntradaAdapter(){
    	super(EspSecurityConstants.ABM_TIPOENTRADA);
    }
    
    //  Getters y Setters
	public TipoEntradaVO getTipoEntrada() {
		return tipoEntrada;
	}

	public void setTipoEntrada(TipoEntradaVO tipoEntradaVO) {
		this.tipoEntrada = tipoEntradaVO;
	}

	public String getName(){
		return NAME;
	}
			
	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de TipoEntrada");     
		 report.setReportBeanName("TipoEntrada");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		// Order by: no 
		 
		 ReportVO reportTipoEntrada = new ReportVO();
		 reportTipoEntrada.setReportTitle("Datos del TipoEntrada");
		 // carga de datos
	     
	     //Código
		 reportTipoEntrada.addReportDato("Código", "codigo");
		 //Descripción
		 reportTipoEntrada.addReportDato("Descripción", "descripcion");
	     
		 report.getListReport().add(reportTipoEntrada);
	
	}
	
	// View getters
}