//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.cas.iface.util.CasSecurityConstants;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del AreaSolicitud
 * 
 * @author Tecso
 *
 */
public class AreaSolicitudSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "areaSolicitudSearchPageVO";
	
	private AreaSolicitudVO areaSolicitud= new AreaSolicitudVO();
	
	// Constructores
	public AreaSolicitudSearchPage() {       
       super(CasSecurityConstants.ABM_AREASOLICITUD);        
    }
	
	// Getters y Setters
	public AreaSolicitudVO getAreaSolicitud() {
		return areaSolicitud;
	}
	public void setAreaSolicitud(AreaSolicitudVO areaSolicitud) {
		this.areaSolicitud = areaSolicitud;
	}           

    public String getName(){    
		return NAME;
	}
	
	public void prepareReport(Long format) {

		ReportVO report = this.getReport(); // no instanciar una nueva
		report.setReportFormat(format);	
		report.setReportTitle("Listados de AreaSolicitud");
		report.setReportBeanName("AreaSolicitud");
		report.setReportFileName(this.getClass().getName());

        /* Codigo de ejemplo para mostrar filtros de Combos en los imprimir
		String desRecurso = "";

		RecursoVO recursoVO = (RecursoVO) ModelUtil.getBussImageModelByIdForList(
				this.getReclamo().getRecurso().getId(),
				this.getListRecurso());
		if (recursoVO != null){
			desRecurso = recursoVO.getDesRecurso();
		}
		report.addReportFiltro("Recurso", desRecurso);*/

		//Código
//		report.addReportFiltro("Código", this.getAreaSolicitud().getCodAreaSolicitud());
       //Descripción
//		report.addReportFiltro("Descripción", this.getAreaSolicitud().getDesAreaSolicitud());
		

		ReportTableVO rtAreaSolicitud = new ReportTableVO("rtAreaSolicitud");
		rtAreaSolicitud.setTitulo("B\u00FAsqueda de AreaSolicitud");

		// carga de columnas
		rtAreaSolicitud.addReportColumn("Código","codAreaSolicitud");
		rtAreaSolicitud.addReportColumn("Descripción", "desAreaSolicitud");
		
		 
	    report.getReportListTable().add(rtAreaSolicitud);

	}
	// View getters
}
