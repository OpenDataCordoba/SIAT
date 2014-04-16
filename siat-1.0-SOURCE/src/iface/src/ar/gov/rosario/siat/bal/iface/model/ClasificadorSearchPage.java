//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del Clasificador
 * 
 * @author Tecso
 *
 */
public class ClasificadorSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "clasificadorSearchPageVO";
	
	private ClasificadorVO clasificador= new ClasificadorVO();
	
	// Constructores
	public ClasificadorSearchPage() {       
       super(BalSecurityConstants.ABM_CLASIFICADOR);        
    }
	
	// Getters y Setters
	public ClasificadorVO getClasificador() {
		return clasificador;
	}
	public void setClasificador(ClasificadorVO clasificador) {
		this.clasificador = clasificador;
	}           

    public String getName(){    
		return NAME;
	}
	
	public void prepareReport(Long format) {

		ReportVO report = this.getReport(); // no instanciar una nueva
		report.setReportFormat(format);	
		report.setReportTitle("Listados de Clasificador");
		report.setReportBeanName("Clasificador");
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

		//Cant. nivel
		report.addReportFiltro("Cant. Nivel", this.getClasificador().getCantNivelView());
       //Descripción
		report.addReportFiltro("Descripción", this.getClasificador().getDescripcion());
		

		ReportTableVO rtClasificador = new ReportTableVO("rtClasificador");
		rtClasificador.setTitulo("B\u00FAsqueda de Clasificador");

		// carga de columnas
		
		rtClasificador.addReportColumn("Descripción", "descripcion");
		
		 
	    report.getReportListTable().add(rtClasificador);

	}
	// View getters
}
