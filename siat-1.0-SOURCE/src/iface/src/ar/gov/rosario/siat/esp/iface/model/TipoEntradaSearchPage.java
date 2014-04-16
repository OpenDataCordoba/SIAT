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
 * SearchPage del TipoEntrada
 * 
 * @author Tecso
 *
 */
public class TipoEntradaSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipoEntradaSearchPageVO";
	
	private TipoEntradaVO tipoEntrada= new TipoEntradaVO();
	
	// Constructores
	public TipoEntradaSearchPage() {       
       super(EspSecurityConstants.ABM_TIPOENTRADA);        
    }
	
	// Getters y Setters
	public TipoEntradaVO getTipoEntrada() {
		return tipoEntrada;
	}
	public void setTipoEntrada(TipoEntradaVO tipoEntrada) {
		this.tipoEntrada = tipoEntrada;
	}           

    public String getName(){    
		return NAME;
	}
	
	public void prepareReport(Long format) {

		ReportVO report = this.getReport(); // no instanciar una nueva
		report.setReportFormat(format);	
		report.setReportTitle("Listados de TipoEntrada");
		report.setReportBeanName("TipoEntrada");
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
		report.addReportFiltro("Código", this.getTipoEntrada().getCodigo());
       //Descripción
		report.addReportFiltro("Descripción", this.getTipoEntrada().getDescripcion());
		

		ReportTableVO rtTipoEntrada = new ReportTableVO("rtTipoEntrada");
		rtTipoEntrada.setTitulo("B\u00FAsqueda de TipoEntrada");

		// carga de columnas
		rtTipoEntrada.addReportColumn("Código","codigo");
		rtTipoEntrada.addReportColumn("Descripción", "descripcion");
		
		 
	    report.getReportListTable().add(rtTipoEntrada);

	}
	// View getters
}
