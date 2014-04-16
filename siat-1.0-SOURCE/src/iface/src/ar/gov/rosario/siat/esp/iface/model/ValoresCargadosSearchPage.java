//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.esp.iface.util.EspSecurityConstants;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del ValoresCargados
 * 
 * @author Tecso
 *
 */
public class ValoresCargadosSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "valoresCargadosSearchPageVO";
	
	private ValoresCargadosVO valoresCargados= new ValoresCargadosVO();
	
	// Constructores
	public ValoresCargadosSearchPage() {       
       super(EspSecurityConstants.ABM_VALORESCARGADOS);        
    }
	
	// Getters y Setters
	public ValoresCargadosVO getValoresCargados() {
		return valoresCargados;
	}
	public void setValoresCargados(ValoresCargadosVO valoresCargados) {
		this.valoresCargados = valoresCargados;
	}           

    public String getName(){    
		return NAME;
	}
	
	public void prepareReport(Long format) {

		ReportVO report = this.getReport(); // no instanciar una nueva
		report.setReportFormat(format);	
		report.setReportTitle("Listados de ValoresCargados");
		report.setReportBeanName("ValoresCargados");
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

		//Descripción
		report.addReportFiltro("Descripción", this.getValoresCargados().getDescripcion());
		

		ReportTableVO rtValoresCargados = new ReportTableVO("rtValoresCargados");
		rtValoresCargados.setTitulo("B\u00FAsqueda de ValoresCargados");

		// carga de columnas
		rtValoresCargados.addReportColumn("Descripción", "descripcion");
		
		 
	    report.getReportListTable().add(rtValoresCargados);

	}
	// View getters
}
