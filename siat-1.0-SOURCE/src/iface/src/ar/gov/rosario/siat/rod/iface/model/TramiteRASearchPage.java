//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rod.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.rod.iface.util.RodSecurityConstants;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del TramiteRA
 * 
 * @author Tecso
 *
 */
public class TramiteRASearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tramiteRASearchPageVO";
	
	private TramiteRAVO tramiteRA= new TramiteRAVO();
    private Date fechaEmisionDesde;
    private Date fechaEmisionHasta;
    
    private String fechaEmisionDesdeView="";  
    private String fechaEmisionHastaView="";
	// Constructores
	public TramiteRASearchPage() {       
       super(RodSecurityConstants.ABM_TRAMITERA);        
    }
	
	// Getters y Setters
	public TramiteRAVO getTramiteRA() {
		return tramiteRA;
	}
	public void setTramiteRA(TramiteRAVO tramiteRA) {
		this.tramiteRA = tramiteRA;
	}           

    public String getName(){    
		return NAME;
	}

    // Getters y Setters View
 
	
	public Date getFechaEmisionDesde() {
		return fechaEmisionDesde;
	}

	public void setFechaEmisionDesde(Date fechaEmisionDesde) {
		this.fechaEmisionDesde = fechaEmisionDesde;
	}

	public Date getFechaEmisionHasta() {
		return fechaEmisionHasta;
	}

	public void setFechaEmisionHasta(Date fechaEmisionHasta) {
		this.fechaEmisionHasta = fechaEmisionHasta;
	}

	public String getFechaEmisionDesdeView() {
		return fechaEmisionDesdeView;
	}

	public void setFechaEmisionDesdeView(String fechaEmisionDesdeView) {
		this.fechaEmisionDesdeView = fechaEmisionDesdeView;
	}

	public String getFechaEmisionHastaView() {
		return fechaEmisionHastaView;
	}

	public void setFechaEmisionHastaView(String fechaEmisionHastaView) {
		this.fechaEmisionHastaView = fechaEmisionHastaView;
	}

	public String getCambiarEstadoEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(RodSecurityConstants.ABM_TRAMITERA, RodSecurityConstants.MTD_TRAMITERA_CAMBIAR_ESTADO);

	}
	
	public void prepareReport(Long format) {

		ReportVO report = this.getReport(); // no instanciar una nueva
		report.setReportFormat(format);	
		report.setReportTitle("Listados de TramiteRA");
		report.setReportBeanName("TramiteRA");
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

		

		ReportTableVO rtTramiteRA = new ReportTableVO("rtTramiteRA");
		rtTramiteRA.setTitulo("B\u00FAsqueda de TramiteRA");

		// carga de columnas
		rtTramiteRA.addReportColumn("Código","codTramiteRA");
		rtTramiteRA.addReportColumn("Descripción", "desTramiteRA");
		
		 
	    report.getReportListTable().add(rtTramiteRA);

	}
	// View getters
}
