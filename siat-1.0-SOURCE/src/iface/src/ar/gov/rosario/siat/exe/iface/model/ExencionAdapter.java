//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.exe.iface.util.ExeSecurityConstants;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter del Exencion
 * 
 * @author tecso
 */
public class ExencionAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "exencionAdapterVO";
	public static final String ENC_NAME = "encExencionAdapterVO";
	
    private ExencionVO exencion = new ExencionVO();
    
    private List<SiNo>           listSiNo = new ArrayList<SiNo>();
    private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
    
    // Constructores
    public ExencionAdapter(){
    	super(ExeSecurityConstants.ABM_EXENCION);
    	ACCION_MODIFICAR_ENCABEZADO = ExeSecurityConstants.ABM_EXENCION_ENC;
    }
    
    //  Getters y Setters
	public ExencionVO getExencion() {
		return exencion;
	}

	public void setExencion(ExencionVO exencionVO) {
		this.exencion = exencionVO;
	}

	public List<SiNo> getListSiNo() {
		return listSiNo;
	}

	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}

	
	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	// Permisos para ExeRecCon
	public String getVerExeRecConEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(ExeSecurityConstants.ABM_EXERECCON, BaseSecurityConstants.VER);
	}
	
	public String getModificarExeRecConEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(ExeSecurityConstants.ABM_EXERECCON, BaseSecurityConstants.MODIFICAR);
	}

	public String getEliminarExeRecConEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(ExeSecurityConstants.ABM_EXERECCON, BaseSecurityConstants.ELIMINAR);
	}

	public String getAgregarExeRecConEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(ExeSecurityConstants.ABM_EXERECCON, BaseSecurityConstants.AGREGAR);
	}
	
	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de Exenci�n/Caso Social/Otro");
		 report.setReportBeanName("Exencion");
		 report.setReportFileName(this.getClass().getName());
		
		 // Order by
		 //report.setReportOrderBy("desTipoObra ASC");
	     
		 ReportVO reportDatosExe = new ReportVO();
		 reportDatosExe.setReportTitle("Datos de la Exenci�n/Caso Social/Otro");
	   
	 // carga de columnas
		 reportDatosExe.addReportDato("Recurso", "recurso.desRecurso");	 
		 reportDatosExe.addReportDato("C�digo", "codExencion");
		 reportDatosExe.addReportDato("Descripci�n", "desExencion");
		 reportDatosExe.addReportDato("Actualiza Deuda", "actualizaDeuda", SiNo.class);
		 reportDatosExe.addReportDato("Participa en Par�metros de Procesos Masivos", "enviaJudicial", SiNo.class);
		 reportDatosExe.addReportDato("Env�a Deuda a Conc. y Quiebra", "enviaCyQ", SiNo.class);
		 reportDatosExe.addReportDato("Afecta Emisi�n", "afectaEmision", SiNo.class);
		 reportDatosExe.addReportDato("Es Parcial", "esParcial", SiNo.class);
		 reportDatosExe.addReportDato("Monto M�nimo", "montoMinimo");
		 reportDatosExe.addReportDato("Aplica M�nimo", "aplicaMinimo", SiNo.class);
		 reportDatosExe.addReportDato("Caso", "idCaso");
		 reportDatosExe.addReportDato("Estado", "estadoView");
	     report.getListReport().add(reportDatosExe);
	       	  	  
		 //Conceptos de la Exenci�n/Caso Social/Otro
		ReportTableVO rtExeRecCon = new ReportTableVO("ExeRecCon");
		rtExeRecCon.setTitulo("Conceptos de la Exenci�n/Caso Social/Otro");
		rtExeRecCon.setReportMetodo("listExeRecCon");
				     	 
	       	// carga de columnas
	       	//Fecha Desde
		rtExeRecCon.addReportColumn("Fecha Desde", "fechaDesde");
			//Fecha Hasta
		rtExeRecCon.addReportColumn("Fecha Hasta", "fechaHasta");
	       	//Concepto
		rtExeRecCon.addReportColumn("Concepto", "recCon.desRecCon");
	       	//porcentaje
		rtExeRecCon.addReportColumn("Porcentaje", "porcentaje");
	        //Monto Fijo
		rtExeRecCon.addReportColumn("Monto Fijo", "montoFijo");
		
	       	report.getReportListTable().add(rtExeRecCon);
	       	
	     //Acciones de la Exenci�n/Caso Social/Otro
	       	
	 /*      	ReportTableVO rtExeRes = new ReportTableVO("ExeRes");
	       	rtExeRes.setTitulo("Acciones de la Exenci�n/Caso Social/Otro");
	       	rtExeRes.setReportMetodo("listExeRecCon");
					     	 
		       	// carga de columnas
		       	//C�digo
	       	rtExeRes.addReportColumn("C�digo", "resolucion.codResolucion");
				//Descripci�n
	       	rtExeRes.addReportColumn("Descripci�n", "resolucion.desResolucion");
		       	//Es Resoluci�n
	       	rtExeRes.addReportColumn("Es Resoluci�n", "resolucion.esResolucion",  SiNo.class);
		       	//Otorga Exenci�n/Caso Social/Otro
	       	rtExeRes.addReportColumn("Otorga Exenci�n/Caso Social/Otro", "resolucion.otorga",  SiNo.class);
		        //Deniega Exenci�n/Caso Social/Otro
	       	rtExeRes.addReportColumn("Deniega Exenci�n/Caso Social/Otro", "resolucion.denega",  SiNo.class);
	        //Revoca Exenci�n/Caso Social/Otro
	       	rtExeRes.addReportColumn("Revoca Exenci�n/Caso Social/Otro", "resolucion.revoca",  SiNo.class);
	       	
		       	report.getReportListTable().add(rtExeRes);*/
		       	
	       	
	    }
	
	// View getters
}
