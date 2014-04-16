//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import coop.tecso.demoda.iface.model.ReportVO;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.TipoPeriodicidad;

/**
 * Adapter del FuenteInfo
 * 
 * @author tecso
 */
public class FuenteInfoAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "fuenteInfoAdapterVO";
	
    private FuenteInfoVO fuenteInfo = new FuenteInfoVO();
    
    private List<SiNo>           listSiNo = new ArrayList<SiNo>();
    private List<TipoPeriodicidad>   listTipoPeriodicidad = new ArrayList<TipoPeriodicidad>();
    
    // Constructores
    public FuenteInfoAdapter(){
    	super(EfSecurityConstants.ABM_FUENTEINFO);
    }
    
    //  Getters y Setters
	public FuenteInfoVO getFuenteInfo() {
		return fuenteInfo;
	}

	public void setFuenteInfo(FuenteInfoVO fuenteInfoVO) {
		this.fuenteInfo = fuenteInfoVO;
	}

	public List<SiNo> getListSiNo() {
		return listSiNo;
	}

	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}
	
	public String getName(){
		return NAME;
	}
			
	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de la Fuente de Información");     
		 report.setReportBeanName("FuenteInfo");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		// Order by: no 
		 
		 ReportVO reportFuenteInfo = new ReportVO();
		 reportFuenteInfo.setReportTitle("Datos de la Fuente de Información");
		 // carga de datos
	     
	     //Nombre Fuente
		 reportFuenteInfo.addReportDato("Nombre Fuente","nombreFuente");
		 reportFuenteInfo.addReportDato("Tipo Periodicidad", "tipoPeriodicidad");
		 reportFuenteInfo.addReportDato("Apertura", "apertura");
		 reportFuenteInfo.addReportDato("Descripción Columna", "desCol1");
			
	     
		 report.getListReport().add(reportFuenteInfo);
	
	}

	public List<TipoPeriodicidad> getListTipoPeriodicidad() {
		return listTipoPeriodicidad;
	}

	public void setListTipoPeriodicidad(List<TipoPeriodicidad> listTipoPeriodicidad) {
		this.listTipoPeriodicidad = listTipoPeriodicidad;
	}
	
	// View getters
}
