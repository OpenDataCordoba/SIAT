//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Adapter del Inspector
 * 
 * @author tecso
 */
public class InspectorAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "inspectorAdapterVO";
	public static final String ENC_NAME = "encInspectorAdapterVO";
	
	private InspectorVO inspector = new InspectorVO();
    
    // Constructores
    public InspectorAdapter(){
    	super(EfSecurityConstants.ABM_INSPECTOR);
    	ACCION_MODIFICAR_ENCABEZADO = EfSecurityConstants.ABM_INSPECTOR_ENC;
    }
    

    //  Permisos para ABM InsSup
    public String getAgregarInsSupEnabled(){
    	return SiatBussImageModel.hasEnabledFlag
    	(EfSecurityConstants.ABM_INSSUP, BaseSecurityConstants.AGREGAR);
    }
    public String getVerInsSupEnabled(){
    	return SiatBussImageModel.hasEnabledFlag
    	(EfSecurityConstants.ABM_INSSUP, BaseSecurityConstants.VER);
    }
    public String getModificarInsSupEnabled(){
    	return SiatBussImageModel.hasEnabledFlag
    	(EfSecurityConstants.ABM_INSSUP, BaseSecurityConstants.MODIFICAR);
    }
    public String getEliminarInsSupEnabled(){
    	return SiatBussImageModel.hasEnabledFlag
    	(EfSecurityConstants.ABM_INSSUP, BaseSecurityConstants.ELIMINAR);
    }	

    //  Getters y Setters
	public InspectorVO getInspector() {
		return inspector;
	}

	public void setInspector(InspectorVO inspectorVO) {
		this.inspector = inspectorVO;
	}

	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de Inspector");
		 report.setReportBeanName("Inspector");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		 // Order by: no 
		 
		 ReportVO reportInspector = new ReportVO();
		 reportInspector.setReportTitle("Datos de Inspector");
		 
		// carga de datos
		

		 //Fecha Desde
		 reportInspector.addReportDato("Fecha Desde", "fechaDesde");
		//Fecha Hasta
		 reportInspector.addReportDato("Fecha Hasta", "fechaHasta");
		//Descripción                                
		 reportInspector.addReportDato("Descripción", "desInspector");
		
	   
		 report.getListReport().add(reportInspector);
		 
		 // Cuentas Partidas
		 ReportTableVO rtInsSup = new ReportTableVO("insSup");
		 rtInsSup.setTitulo("Listado de Supervisores");
		 rtInsSup.setReportMetodo("listInsSup");
    		// carga de columnas
			//Fecha Desde
		 rtInsSup.addReportColumn("Fecha Desde", "fechaDesde");
			//Fecha Hasta
		 rtInsSup.addReportColumn("Fecha Hasta", "fechaHasta");
			//
		 rtInsSup.addReportColumn("Supervisor", "supervisor.desSupervisor");
		 
	     report.getReportListTable().add(rtInsSup);
	
		
	}


	
	// View getters
}
