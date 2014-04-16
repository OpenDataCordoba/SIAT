//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.TipoDeudaVO;
import ar.gov.rosario.siat.def.iface.model.ViaDeudaVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Adapter del DesEsp
 * 
 * @author tecso
 */
public class DesEspAdapter extends SiatAdapterModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String NAME = "desEspAdapterVO";

	public static final String ENC_NAME = "encDesEspAdapterVO";
	
    private DesEspVO desEsp = new DesEspVO();
    
    private List<RecursoVO> listRecurso;
    private List<TipoDeudaVO> listTipoDeuda;
    private List<ViaDeudaVO> listViaDeuda;
    private Boolean esPlanPagos;
    
    
    // Constructores
    public DesEspAdapter(){
    	super(GdeSecurityConstants.ABM_DESESP);
    	ACCION_MODIFICAR_ENCABEZADO = GdeSecurityConstants.ABM_DESESP_ENC;
    }
    
    // Permisos para ABM DesRecClaDeu
	public String getAgregarClasificDeudaEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(GdeSecurityConstants.ABM_DESRECCLADEU, BaseSecurityConstants.AGREGAR);
	}
	public String getVerClasificDeudaEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(GdeSecurityConstants.ABM_DESRECCLADEU, BaseSecurityConstants.VER);
	}
	public String getModificarClasificDeudaEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(GdeSecurityConstants.ABM_DESRECCLADEU, BaseSecurityConstants.MODIFICAR);
	}
	public String getEliminarClasificDeudaEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(GdeSecurityConstants.ABM_DESRECCLADEU, BaseSecurityConstants.ELIMINAR);
	}	

    // Permisos para ABM DesAtrVal
	public String getAgregarDesAtrValEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(GdeSecurityConstants.ABM_DESATRVAL, BaseSecurityConstants.AGREGAR);
	}
	public String getVerDesAtrValEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(GdeSecurityConstants.ABM_DESATRVAL, BaseSecurityConstants.VER);
	}
	public String getModificarDesAtrValEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(GdeSecurityConstants.ABM_DESATRVAL, BaseSecurityConstants.MODIFICAR);
	}
	public String getEliminarDesAtrValEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(GdeSecurityConstants.ABM_DESATRVAL, BaseSecurityConstants.ELIMINAR);
	}	
	
    // Permisos para ABM DesEspExe
	public String getAgregarDesEspExeEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(GdeSecurityConstants.ABM_DESESPEXE, BaseSecurityConstants.AGREGAR);
	}
	public String getVerDesEspExeEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(GdeSecurityConstants.ABM_DESESPEXE, BaseSecurityConstants.VER);
	}
	public String getModificarDesEspExeEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(GdeSecurityConstants.ABM_DESESPEXE, BaseSecurityConstants.MODIFICAR);
	}
	public String getEliminarDesEspExeEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(GdeSecurityConstants.ABM_DESESPEXE, BaseSecurityConstants.ELIMINAR);
	}	

	
	//  Getters y Setters
	public DesEspVO getDesEsp() {
		return desEsp;
	}

	public void setDesEsp(DesEspVO desEspVO) {
		this.desEsp = desEspVO;
	}

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	public List<TipoDeudaVO> getListTipoDeuda() {
		return listTipoDeuda;
	}

	public void setListTipoDeuda(List<TipoDeudaVO> listTipoDeuda) {
		this.listTipoDeuda = listTipoDeuda;
	}

	public List<ViaDeudaVO> getListViaDeuda() {
		return listViaDeuda;
	}

	public void setListViaDeuda(List<ViaDeudaVO> listViaDeuda) {
		this.listViaDeuda = listViaDeuda;
	}

	public Boolean getEsPlanPagos() {
		return esPlanPagos;
	}

	public void setEsPlanPagos(Boolean esPlanPagos) {
		this.esPlanPagos = esPlanPagos;
	}
	
	public String getName(){
		return NAME;
	}
	
		
	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de Descuento Especial");
		 report.setReportBeanName("desEsp");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		 // Order by: no 
		 
		 ReportVO reportDatosEsp = new ReportVO();
		 reportDatosEsp.setReportTitle("Datos de Descuento Especial");
		 // carga de datos
	     //Descripción                                
		 reportDatosEsp.addReportDato("Descripción", "desDesEsp");
	     //Recurso
		 reportDatosEsp.addReportDato("Recurso", "recurso.desRecurso");
		 //Tipo de Deuda
		 reportDatosEsp.addReportDato("Tipo de Deuda", "tipoDeuda.desTipoDeuda");
	     //Vía Deuda
		 reportDatosEsp.addReportDato("Vía Deuda", "viaDeuda.desViaDeuda");
	     //Fecha Vto. Desde
		 reportDatosEsp.addReportDato("Fecha Vto. Desde", "fechaVtoDeudaDesde");
	     //Fecha Vto. Hasta
		 reportDatosEsp.addReportDato("Fecha Vto. Hasta", "fechaVtoDeudaHasta");
	    // % Desc. Capital
		 reportDatosEsp.addReportDato("% Desc. Capital", "porDesCap");
		//% Desc. actualiz
		 reportDatosEsp.addReportDato("% Desc. actualiz", "porDesAct");
		//% Desc. Interés
		 reportDatosEsp.addReportDato("% Desc. Interés", "porDesInt");
		 //Leyenda Descuento
		 reportDatosEsp.addReportDato("Leyenda Descuento", "leyendaDesEsp");
		//Caso
		  reportDatosEsp.addReportDato("Caso", "idCaso");
		// Estado
		 reportDatosEsp.addReportDato("Estado", "estadoView");
		
		 report.getListReport().add(reportDatosEsp);
	
		 // Clasificación Deuda
		 ReportTableVO rtDesRecClaDeu = new ReportTableVO("desRecClaDeu");
    	 rtDesRecClaDeu.setTitulo("Listado de Clasificación Deuda");
    	 rtDesRecClaDeu.setReportMetodo("listDesRecClaDeu");
    		// carga de columnas
			//Fecha Desde
    	 rtDesRecClaDeu.addReportColumn("Fecha Desde", "fechaDesde");
			//Fecha Hasta
    	 rtDesRecClaDeu.addReportColumn("Fecha Hasta", "fechaHasta");
			//Clasificación
    	 rtDesRecClaDeu.addReportColumn("Clasificación", "recClaDeu.desClaDeu");
		 
	     report.getReportListTable().add(rtDesRecClaDeu);
	     
		 // Atributos
		 ReportTableVO rtDesAtrVal = new ReportTableVO("DesAtrVal");
		 rtDesAtrVal.setTitulo("Listado de Atributos");
		 rtDesAtrVal.setReportMetodo("listDesAtrVal");
    		// carga de columnas
			//Fecha Desde
		 rtDesAtrVal.addReportColumn("Fecha Desde", "fechaDesde");
			//Fecha Hasta
		 rtDesAtrVal.addReportColumn("Fecha Hasta", "fechaHasta");
			//Atributo
		 rtDesAtrVal.addReportColumn("Atributo", "atributo.desAtributo");
			//Valor
		 rtDesAtrVal.addReportColumn("Valor", "valor");
		 
	     report.getReportListTable().add(rtDesAtrVal);
	     
		 //Exenciones
			ReportTableVO rtPlanExe = new ReportTableVO("desEspExe");
			rtPlanExe.setTitulo("Exenciones");
			rtPlanExe.setReportMetodo("listDesEspExe");
					     	 
			// carga de columnas
			//Fecha Desde
			rtPlanExe.addReportColumn("Fecha Desde", "fechaDesde");
			//Fecha Hasta
			rtPlanExe.addReportColumn("Fecha Hasta", "fechaHasta");
			//Cod. Caso Social/Otro
			rtPlanExe.addReportColumn("Cod. Caso Social/Otro", "exencion.codExencion");
			//Exención Caso Social/Otro
			rtPlanExe.addReportColumn("Exención Caso Social/Otro", "exencion.desExencion");
		
			report.getReportListTable().add(rtPlanExe);
	}

	
}
