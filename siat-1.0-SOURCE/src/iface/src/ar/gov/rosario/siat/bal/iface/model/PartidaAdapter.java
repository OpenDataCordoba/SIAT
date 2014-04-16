//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter del Partida
 * 
 * @author tecso
 */
public class PartidaAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "partidaAdapterVO";
	public static final String ENC_NAME = "encPartidaAdapterVO";
    private PartidaVO partida = new PartidaVO();
    
    // Esta lista esta mapeada como detalle del Nodo. Se agrega en el Mantenedor de Partida solo para visualizacion.
    private List<RelPartidaVO> listRelPartida = new ArrayList<RelPartidaVO>();
    
    private List<SiNo>    listSiNo = new ArrayList<SiNo>();
    
    
    // Constructores
    public PartidaAdapter(){
    	super(BalSecurityConstants.ABM_PARTIDA);
    	ACCION_MODIFICAR_ENCABEZADO = BalSecurityConstants.ABM_PARTIDA_ENC;
    }
    
    // Permisos para ABM ParCueBan
	public String getAgregarParCueBanEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(BalSecurityConstants.ABM_PARCUEBAN, BaseSecurityConstants.AGREGAR);
	}
	public String getVerParCueBanEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(BalSecurityConstants.ABM_PARCUEBAN, BaseSecurityConstants.VER);
	}
	public String getModificarParCueBanEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(BalSecurityConstants.ABM_PARCUEBAN, BaseSecurityConstants.MODIFICAR);
	}
	public String getEliminarParCueBanEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(BalSecurityConstants.ABM_PARCUEBAN, BaseSecurityConstants.ELIMINAR);
	}	
    
    //  Getters y Setters
	public PartidaVO getPartida() {
		return partida;
	}

	public void setPartida(PartidaVO partidaVO) {
		this.partida = partidaVO;
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
	
	public List<RelPartidaVO> getListRelPartida() {
		return listRelPartida;
	}

	public void setListRelPartida(List<RelPartidaVO> listRelPartida) {
		this.listRelPartida = listRelPartida;
	}

	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de Partida");
		 report.setReportBeanName("Partida");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		 // Order by: no 
		 
		 ReportVO reportDatosPar = new ReportVO();
		 reportDatosPar.setReportTitle("Datos de Partida");
		 
		// carga de datos
		
	     //Código                                
		 reportDatosPar.addReportDato("Código", "codPartida");
		//Descripción                                
		 reportDatosPar.addReportDato("Descripción", "desPartida");
		 //Prefijo Ejercicio Actual
		 reportDatosPar.addReportDato("Prefijo Ejercicio Actual", "preEjeAct");
		//Prefijo Ejercicio Vencido
		 reportDatosPar.addReportDato("Prefijo Ejercicio Vencido", "preEjeVen");
		 //Estado
		 reportDatosPar.addReportDato("Estado", "estadoView");
	   
		 report.getListReport().add(reportDatosPar);
		 
		 // Cuentas Partidas
		 ReportTableVO rtParCueBan = new ReportTableVO("desRecClaDeu");
    	 rtParCueBan.setTitulo("Listado de Cuentas Bancarias");
    	 rtParCueBan.setReportMetodo("listParCueBan");
    		// carga de columnas
			//Fecha Desde
    	 rtParCueBan.addReportColumn("Fecha Desde", "fechaDesde");
			//Fecha Hasta
    	 rtParCueBan.addReportColumn("Fecha Hasta", "fechaHasta");
			//Nro. Cuenta Bancaria
    	 rtParCueBan.addReportColumn("Nro. Cuenta Bancaria", "cuentaBanco.nroCuenta");
		 
	     report.getReportListTable().add(rtParCueBan);
	
		
	}

	

}
