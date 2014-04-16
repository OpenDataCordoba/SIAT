//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.cas.iface.util.CasSecurityConstants;
import ar.gov.rosario.siat.def.iface.model.AreaVO;
import coop.tecso.demoda.iface.model.ReportVO;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter del AreaSolicitud
 * 
 * @author tecso
 */
public class AreaSolicitudAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "areaSolicitudAdapterVO";
	
    private AreaSolicitudVO areaSolicitud = new AreaSolicitudVO();
    
    private TipoSolicitudVO tipoSolicitud = new TipoSolicitudVO();
    
    private List<AreaVO> listArea =  new ArrayList<AreaVO>();
    
	private List<SiNo>           listSiNo = new ArrayList<SiNo>();
    
    // Constructores
    public AreaSolicitudAdapter(){
    	super(CasSecurityConstants.ABM_AREASOLICITUD);
    }
    
    //  Getters y Setters
	public AreaSolicitudVO getAreaSolicitud() {
		return areaSolicitud;
	}

	public void setAreaSolicitud(AreaSolicitudVO areaSolicitudVO) {
		this.areaSolicitud = areaSolicitudVO;
	}
	
    public TipoSolicitudVO getTipoSolicitud() {
		return tipoSolicitud;
	}

	public void setTipoSolicitud(TipoSolicitudVO tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
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
		 report.setReportTitle("Reporte de AreaSolicitud");     
		 report.setReportBeanName("AreaSolicitud");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		// Order by: no 
		 
		 ReportVO reportAreaSolicitud = new ReportVO();
		 reportAreaSolicitud.setReportTitle("Datos del AreaSolicitud");
		 // carga de datos
	     
	     //Código
		 reportAreaSolicitud.addReportDato("Código", "codAreaSolicitud");
		 //Descripción
		 reportAreaSolicitud.addReportDato("Descripción", "desAreaSolicitud");
	     
		 report.getListReport().add(reportAreaSolicitud);
	
	}

    public List<AreaVO> getListArea() {
		return listArea;
	}

	public void setListArea(List<AreaVO> listArea) {
		this.listArea = listArea;
	}
	
	// View getters
}