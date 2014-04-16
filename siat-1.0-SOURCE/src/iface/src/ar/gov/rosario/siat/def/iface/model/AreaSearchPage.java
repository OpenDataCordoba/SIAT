//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del Area
 * 
 * @author Tecso
 *
 */
public class AreaSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "areaSearchPageVO";

	private AreaVO area= new AreaVO();
	
	private Boolean admOficinaBussEnabled = true;

	// Constructores
	public AreaSearchPage() {       
       super(DefSecurityConstants.ABM_AREA);        
    }

	// Getters y Setters
	public AreaVO getArea() {
		return area;
	}
	public void setArea(AreaVO area) {
		this.area = area;
	}

	public String getAdmOficinaEnabled() {
		//por ahora no hacen falta definir permisos
		return getAdmOficinaBussEnabled() ? "enabled" : "disabled";
	}

	public Boolean getAdmOficinaBussEnabled() {
		return admOficinaBussEnabled;
	}

	public void setAdmOficinaBussEnabled(Boolean admOficinaBussEnabled) {
		this.admOficinaBussEnabled = admOficinaBussEnabled;
	}
		
	public String getAdmRecursoAreaEnabled(){
		return SiatBussImageModel.hasEnabledFlag(DefSecurityConstants.ABM_RECURSOAREA, BaseSecurityConstants.AGREGAR);
	}
	
	// View getters
	
	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva	
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de Areas");
		 report.setReportBeanName("Areas");
		 report.setReportFileName(this.getClass().getName());
		 
		 // carga de filtros
		 report.addReportFiltro("Código", this.getArea().getCodArea());
		 report.addReportFiltro("Descripción", this.getArea().getDesArea());
		 
		 // Order by
		 report.setReportOrderBy("desArea ASC");
		 
	     ReportTableVO rtCategoria = new ReportTableVO("Area");
	     rtCategoria.setTitulo("Listado de Areas");
	     
		 // carga de columnas
	     rtCategoria.addReportColumn("Código", "codArea");
	     rtCategoria.addReportColumn("Descripción", "desArea");
	     // columnas con subseldas
	     rtCategoria.addReportColumn("Oficinas", "listOficina.desOficinaReport"); // que groso
	     
	     rtCategoria.addReportColumn("Estado", "estadoView");
	     
	     report.getReportListTable().add(rtCategoria);
	}

}
