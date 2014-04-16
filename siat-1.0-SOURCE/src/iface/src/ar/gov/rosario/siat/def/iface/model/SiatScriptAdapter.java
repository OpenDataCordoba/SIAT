//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import coop.tecso.demoda.iface.model.ReportVO;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter del SiatScript
 * 
 * @author tecso
 */
public class SiatScriptAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "siatScriptAdapterVO";

	public static final String ENC_NAME = "encSiatScriptAdapterVO";
	
    private SiatScriptVO siatScript = new SiatScriptVO();
    
    private List<SiatScriptUsrVO> listSiatScriptUsr = new ArrayList<SiatScriptUsrVO>();
    
    private List<SiNo>           listSiNo = new ArrayList<SiNo>();
    
	    
    // Constructores
    public SiatScriptAdapter(){
    	super(DefSecurityConstants.ABM_SIATSCRIPT);
    }
    
    //  Getters y Setters
	public SiatScriptVO getSiatScript() {
		return siatScript;
	}

	public void setSiatScript(SiatScriptVO siatScriptVO) {
		this.siatScript = siatScriptVO;
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
			
	public List<SiatScriptUsrVO> getListSiatScriptUsr() {
		return listSiatScriptUsr;
	}

	public void setListSiatScriptUsr(List<SiatScriptUsrVO> arrayList) {
		this.listSiatScriptUsr = arrayList;
	}

	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de SiatScript");     
		 report.setReportBeanName("SiatScript");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		// Order by: no 
		 
		 ReportVO reportSiatScript = new ReportVO();
		 reportSiatScript.setReportTitle("Datos del SiatScript");
		 // carga de datos
	     
	     //Código
		 reportSiatScript.addReportDato("Código", "codigo");
		 //Descripción
		 reportSiatScript.addReportDato("Descripción", "descripcion");
		 //Path
		 reportSiatScript.addReportDato("Ruta/Path", "path");
		 //Fecha Midificado
		 reportSiatScript.addReportDato("Fecha Modificado", "fechaUltMdf");		 
		 
		 report.getListReport().add(reportSiatScript);
	
	}
	
	// Permisos para AreaSolicitud
	public String getVerSiatScriptUsrEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(DefSecurityConstants.ABM_SIATSCRIPTUSR, BaseSecurityConstants.VER);
	}
	
	public String getModificarSiatScriptUsrEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(DefSecurityConstants.ABM_SIATSCRIPTUSR, BaseSecurityConstants.MODIFICAR);
	}

	public String getEliminarSiatScriptUsrEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(DefSecurityConstants.ABM_SIATSCRIPTUSR, BaseSecurityConstants.ELIMINAR);
	}
	
	public String getAgregarSiatScriptUsrEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(DefSecurityConstants.ABM_SIATSCRIPTUSR, BaseSecurityConstants.AGREGAR);
	}
		
	public String getModificarEncabezadoEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(DefSecurityConstants.ABM_SIATSCRIPT_ENC, BaseSecurityConstants.MODIFICAR);
	}

	
}