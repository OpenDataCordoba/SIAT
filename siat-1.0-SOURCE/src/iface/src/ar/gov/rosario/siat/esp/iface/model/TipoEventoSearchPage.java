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
 * SearchPage del TipoEvento
 * 
 * @author Tecso
 *
 */
public class TipoEventoSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipoEventoSearchPageVO";
	
	private TipoEventoVO tipoEvento= new TipoEventoVO();
	
	// Constructores
	public TipoEventoSearchPage() {       
       super(EspSecurityConstants.ABM_TIPOEVENTO);        
    }
	
	// Getters y Setters
	public TipoEventoVO getTipoEvento() {
		return tipoEvento;
	}
	public void setTipoEvento(TipoEventoVO tipoEvento) {
		this.tipoEvento = tipoEvento;
	}           

    public String getName(){    
		return NAME;
	}
	
	public void prepareReport(Long format) {

		ReportVO report = this.getReport(); // no instanciar una nueva
		report.setReportFormat(format);	
		report.setReportTitle("Listados de TipoEvento");
		report.setReportBeanName("TipoEvento");
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
		report.addReportFiltro("Descripción", this.getTipoEvento().getDescripcion());
		

		ReportTableVO rtTipoEvento = new ReportTableVO("rtTipoEvento");
		rtTipoEvento.setTitulo("B\u00FAsqueda de TipoEvento");

		// carga de columnas
		rtTipoEvento.addReportColumn("Código","codigo");
		rtTipoEvento.addReportColumn("Descripción", "descripcion");
		
		 
	    report.getReportListTable().add(rtTipoEvento);

	}
	// View getters
}
