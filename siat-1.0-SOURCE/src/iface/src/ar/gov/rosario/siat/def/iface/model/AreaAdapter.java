//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Adapter del Area
 * 
 * @author tecso
 */
public class AreaAdapter extends SiatAdapterModel {
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "areaAdapterVO";
	
    private AreaVO area = new AreaVO();
    
    // Constructores
    public AreaAdapter(){
    	super(DefSecurityConstants.ABM_AREA);
    }
    
    //  Getters y Setters
	public AreaVO getArea() {
		return area;
	}

	public void setArea(AreaVO areaVO) {
		this.area = areaVO;
	}
	
	public String getName(){
		return NAME;
	}
	
		
	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de Area");
		 report.setReportBeanName("vencimiento");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		 // Order by: no 
		 
		 ReportVO reportDatosArea = new ReportVO();
		 reportDatosArea.setReportTitle("Datos de Area");
		 
		// carga de datos
	
	     //Código                                
		 reportDatosArea.addReportDato("Código", "codArea");
		//Descripción                                
		 reportDatosArea.addReportDato("Descripción", "desArea");
		 //Estado
		 reportDatosArea.addReportDato("Estado", "estadoView");
	     	   
		 report.getListReport().add(reportDatosArea);
	
		 // Formas de Pago de Obra
	/*	 ReportTableVO rtObraFormaPago = new ReportTableVO("ObraFormaPago");
	     rtObraFormaPago.setTitulo("Listado de Formas de Pago de la Obra");
	     rtObraFormaPago.setReportMetodo("listObraFormaPago");
	     report.getReportListTable().add(rtObraFormaPago);*/
	}
	
	// Permisos para ABM RecursoArea
	public String getVerRecursoAreaEnabled(){
		return SiatBussImageModel.hasEnabledFlag(DefSecurityConstants.ABM_RECURSOAREA, BaseSecurityConstants.VER);
	}
	public String getModificarRecursoAreaEnabled(){
		return SiatBussImageModel.hasEnabledFlag(DefSecurityConstants.ABM_RECURSOAREA, BaseSecurityConstants.MODIFICAR);
	}
	public String getEliminarRecursoAreaEnabled(){
		return SiatBussImageModel.hasEnabledFlag(DefSecurityConstants.ABM_RECURSOAREA, BaseSecurityConstants.ELIMINAR);
	}
	public String getAgregarRecursoAreaEnabled(){
		return SiatBussImageModel.hasEnabledFlag(DefSecurityConstants.ABM_RECURSOAREA, BaseSecurityConstants.AGREGAR);
	}
	
}