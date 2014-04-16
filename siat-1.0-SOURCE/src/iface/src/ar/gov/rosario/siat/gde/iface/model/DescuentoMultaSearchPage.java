//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del DescuentoMulta
 * 
 * @author Tecso
 *
 */
public class DescuentoMultaSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "descuentoMultaSearchPageVO";
	
	private DescuentoMultaVO descuentoMulta= new DescuentoMultaVO();
	
	// Constructores
	public DescuentoMultaSearchPage() {       
       super(GdeSecurityConstants.ABM_DESCUENTOMULTA);        
    }
	
	// Getters y Setters
	public DescuentoMultaVO getDescuentoMulta() {
		return descuentoMulta;
	}
	public void setDescuentoMulta(DescuentoMultaVO descuentoMulta) {
		this.descuentoMulta = descuentoMulta;
	}           

    public String getName(){    
		return NAME;
	}
	
	public void prepareReport(Long format) {

		ReportVO report = this.getReport(); // no instanciar una nueva
		report.setReportFormat(format);	
		report.setReportTitle("Listados de DescuentoMulta");
		report.setReportBeanName("DescuentoMulta");
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
		report.addReportFiltro("Descripción", this.getDescuentoMulta().getDescripcion());
		

		ReportTableVO rtDescuentoMulta = new ReportTableVO("rtDescuentoMulta");
		rtDescuentoMulta.setTitulo("B\u00FAsqueda de DescuentoMulta");

		// carga de columnas
		rtDescuentoMulta.addReportColumn("Descripción", "descripcion");
		
		 
	    report.getReportListTable().add(rtDescuentoMulta);

	}
	// View getters
}
