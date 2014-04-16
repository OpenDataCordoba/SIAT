//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.cas.iface.util.CasSecurityConstants;
import ar.gov.rosario.siat.def.iface.model.AreaVO;
import coop.tecso.demoda.iface.model.ReportVO;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter del TipoSolicitud
 * 
 * @author tecso
 */
public class TipoSolicitudAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "tipoSolicitudAdapterVO";

	public static final String ENC_NAME = "encTipoSolicitudAdapterVO";
	
    private TipoSolicitudVO tipoSolicitud = new TipoSolicitudVO();
    
    private List<AreaVO> listArea =  new ArrayList<AreaVO>();
    
	private List<SiNo>           listSiNo = new ArrayList<SiNo>();
    
    // Constructores
    public TipoSolicitudAdapter(){
    	super(CasSecurityConstants.ABM_TIPOSOLICITUD);
    	ACCION_MODIFICAR_ENCABEZADO = CasSecurityConstants.ABM_TIPOSOLICITUD_ENC;
    }
    
    
    //  Getters y Setters
	public TipoSolicitudVO getTipoSolicitud() {
		return tipoSolicitud;
	}

	public void setTipoSolicitud(TipoSolicitudVO tipoSolicitudVO) {
		this.tipoSolicitud = tipoSolicitudVO;
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
			
    public List<AreaVO> getListArea() {
		return listArea;
	}

	public void setListArea(List<AreaVO> listArea) {
		this.listArea = listArea;
	}
	
	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte Tipo de Solicitud");     
		 report.setReportBeanName("TipoSolicitud");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		// Order by: no 
		 
		 ReportVO reportTipoSolicitud = new ReportVO();
		 reportTipoSolicitud.setReportTitle("Datos Tipo de Solicitud");
		 // carga de datos
	     
	     //Código
		 reportTipoSolicitud.addReportDato("Código", "codigo");
		 //Descripción
		 reportTipoSolicitud.addReportDato("Descripción", "descripcion");
		 //Estado		 
		 reportTipoSolicitud.addReportDato("Estado", "estado");
		 //Fecha última modificación
		 reportTipoSolicitud.addReportDato("Fecha Última Modificación", "fechaUltMdf");
		 
		 report.getListReport().add(reportTipoSolicitud);
	
	}
	
	// View getters
	// Permisos para AreaSolicitud
	public String getVerAreaSolicitudEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(CasSecurityConstants.ABM_AREASOLICITUD, BaseSecurityConstants.VER);
	}
	
	public String getModificarAreaSolicitudEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(CasSecurityConstants.ABM_AREASOLICITUD, BaseSecurityConstants.MODIFICAR);
	}

	public String getEliminarAreaSolicitudEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(CasSecurityConstants.ABM_AREASOLICITUD, BaseSecurityConstants.ELIMINAR);
	}
	
	public String getAgregarAreaSolicitudEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(CasSecurityConstants.ABM_AREASOLICITUD, BaseSecurityConstants.AGREGAR);
	}
		
	public String getModificarEncabezadoEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(CasSecurityConstants.ABM_TIPOSOLICITUD_ENC, BaseSecurityConstants.MODIFICAR);
	}
	
	
}