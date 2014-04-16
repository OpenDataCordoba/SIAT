//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.rec.iface.util.RecSecurityConstants;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage de Tipo de Obra
 * 
 * @author Tecso
 *
 */
public class TipoObraSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipoObraSearchPageVO";
		
	private TipoObraVO tipoObra = new TipoObraVO();
	
	List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	
	// Constructores
	public TipoObraSearchPage() {       
       super(RecSecurityConstants.ABM_TIPOOBRA);        
    }

	// Getters y Setters
	public TipoObraVO getTipoObra() {
		return tipoObra;
	}
	
	public void setTipoObra(TipoObraVO tipoObra) {
		this.tipoObra = tipoObra;
	}

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de Tipo de Obra");
		 report.setReportBeanName("TiposObras");
		 report.setReportFileName(this.getClass().getName());
		 
		 // recurso
		 String desRecurso = "";
			
		 RecursoVO recursoVO = (RecursoVO) ModelUtil.getBussImageModelByIdForList(
				 this.getTipoObra().getRecurso().getId(), 
				 this.getListRecurso());
		 if (recursoVO != null){
			 desRecurso = recursoVO.getDesRecurso();
		 }
		 report.addReportFiltro("Recurso", desRecurso);

		 // carga de filtros
		 report.addReportFiltro("Descripci\u00F3n", this.getTipoObra().getDesTipoObra());
		 
		 // Order by
		 report.setReportOrderBy("recurso.codRecurso, desTipoObra");
	     
	     ReportTableVO rtTipoObra = new ReportTableVO("rtTipoObra");
	     rtTipoObra.setTitulo("Listado de Tipo de Obra");

		 // carga de columnas
	     rtTipoObra.addReportColumn("Recurso", "recurso.desRecurso");
	     rtTipoObra.addReportColumn("Descripci\u00F3n", "desTipoObra");
	     rtTipoObra.addReportColumn("Costo Cuadra", "costoCuadra");
	     rtTipoObra.addReportColumn("Costo Metro Frente", "costoMetroFrente");
	     rtTipoObra.addReportColumn("Costo UT", "costoUT");
	     rtTipoObra.addReportColumn("Costo por M\u00F3dulo", "costoModulo");
	     rtTipoObra.addReportColumn("Estado", "estadoView");
	    
	     report.getReportListTable().add(rtTipoObra);

	    }

}
