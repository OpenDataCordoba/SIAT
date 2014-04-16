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
 * SearchPage del TipoSolicitud
 * 
 * @author Tecso
 *
 */
public class TipoSolicitudSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipoSolicitudSearchPageVO";
	
	private TipoSolicitudVO tipoSolicitud= new TipoSolicitudVO();
	
	// Constructores
	public TipoSolicitudSearchPage() {       
       super(CasSecurityConstants.ABM_TIPOSOLICITUD);        
    }
	
	// Getters y Setters
	public TipoSolicitudVO getTipoSolicitud() {
		return tipoSolicitud;
	}
	public void setTipoSolicitud(TipoSolicitudVO tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}           

    public String getName(){    
		return NAME;
	}
	
	public void prepareReport(Long format) {

		ReportVO report = this.getReport(); // no instanciar una nueva
		report.setReportFormat(format);	
		report.setReportTitle("Listado Tipo de Solicitud");
		report.setReportBeanName("TipoSolicitud");
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
//		report.addReportFiltro("Código", this.getTipoSolicitud().getCodTipoSolicitud());
       //Descripción
//		report.addReportFiltro("Descripción", this.getTipoSolicitud().getDesTipoSolicitud());
		

		ReportTableVO rtTipoSolicitud = new ReportTableVO("rtTipoSolicitud");
		rtTipoSolicitud.setTitulo("B\u00FAsqueda de TipoSolicitud");

		// carga de columnas
		rtTipoSolicitud.addReportColumn("Código","codigo");
		rtTipoSolicitud.addReportColumn("Descripción", "descripcion");
		
		 
	    report.getReportListTable().add(rtTipoSolicitud);

	}
	// View getters
}
