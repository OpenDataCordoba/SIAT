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
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Adapter del EmiMat
 * 
 * @author tecso
 */
public class EmiMatAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME     = "emiMatAdapterVO";
	public static final String ENC_NAME = "encEmiMatAdapterVO";
	
    private EmiMatVO emiMat = new EmiMatVO();
    
    private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
    
    // Constructores
    public EmiMatAdapter(){
    	super(DefSecurityConstants.ABM_EMIMAT);
    }
    
    //  Getters y Setters
	public EmiMatVO getEmiMat() {
		return emiMat;
	}

	public void setEmiMat(EmiMatVO emiMatVO) {
		this.emiMat = emiMatVO;
	}

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}
	
	// View getters
	public String getModificarEncabezadoEnabled(){
		return SiatBussImageModel.hasEnabledFlag(DefSecurityConstants.ABM_EMIMAT_ENC, BaseSecurityConstants.MODIFICAR);
	}
	
	public String getVerColEmiMatEnabled(){
		return SiatBussImageModel.hasEnabledFlag(DefSecurityConstants.ABM_COLEMIMAT, BaseSecurityConstants.VER);
	}

	public String getModificarColEmiMatEnabled(){
		return SiatBussImageModel.hasEnabledFlag(DefSecurityConstants.ABM_COLEMIMAT, BaseSecurityConstants.MODIFICAR);
	}
	
	public String getEliminarColEmiMatEnabled(){
		return SiatBussImageModel.hasEnabledFlag(DefSecurityConstants.ABM_COLEMIMAT, BaseSecurityConstants.ELIMINAR);
	}
	
	public String getAgregarColEmiMatEnabled(){
		return SiatBussImageModel.hasEnabledFlag(DefSecurityConstants.ABM_COLEMIMAT, BaseSecurityConstants.AGREGAR);
	}

	public String getName(){
		return NAME;
	}
			
	public void prepareReport(Long format) {
		 
	 ReportVO report = this.getReport();
	 report.setReportFormat(format);	
	 report.setReportTitle("Reporte de Tabla de Par\u00E1metros de Emisi\u00F3n");     
	 report.setReportBeanName("EmiMat");
	 report.setReportFileName(this.getClass().getName());

	 ReportVO reportEmiMat = new ReportVO();
	 reportEmiMat.setReportTitle("Datos de la Tabla de Par\u00E1metros");

	 // Código
	 reportEmiMat.addReportDato("Código", "codEmiMat");
	 // Recurso
	 reportEmiMat.addReportDato("Recurso", "recurso.desRecurso");
     
	 report.getListReport().add(reportEmiMat);
	 
	 // Columnas de la Tabla
	 ReportTableVO rtColEmiMat = new ReportTableVO("ColEmiMat");
	 rtColEmiMat.setTitulo("Columnas de la Tabla");
	 rtColEmiMat.setReportMetodo("listColEmiMat");

	 // Codigo
	 rtColEmiMat.addReportColumn("Nombre", "codColumna");
	 // Tipo de Dato
	 rtColEmiMat.addReportColumn("Tipo de Dato", "tipoDatoForReport");
	 // Tipo de Columna
	 rtColEmiMat.addReportColumn("Clave", "tipoColumnaForReport");

	 report.getReportListTable().add(rtColEmiMat);
   }

}
