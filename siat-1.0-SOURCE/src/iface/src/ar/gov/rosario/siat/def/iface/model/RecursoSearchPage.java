//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Search Page de Recurso
 * @author tecso
 *
 */
public class RecursoSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "recursoSearchPageVO";
	
	private RecursoVO recurso = new RecursoVO();
	
	private boolean esNoTrib=false;
	
	private List<CategoriaVO> listCategoria = new ArrayList<CategoriaVO>();
	private List<TipoVO> listTipoCategoria = new ArrayList<TipoVO>();
	
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	
	public RecursoSearchPage(){
		super(DefSecurityConstants.ABM_RECURSO);
	}
	
	// Getters y Setters
	public RecursoVO getRecurso() {
		return recurso;
	}
	public void setRecurso(RecursoVO recurso){
		this.recurso = recurso;
	}
	public List<CategoriaVO> getListCategoria() {
		return listCategoria;
	}
	public void setListCategoria(List<CategoriaVO> listCategoria) {
		this.listCategoria = listCategoria;
	}
	public List<TipoVO> getListTipoCategoria() {
		return listTipoCategoria;
	}
	public void setListTipoCategoria(List<TipoVO> listTipoCategoria) {
		this.listTipoCategoria = listTipoCategoria;
	}
	public List<RecursoVO> getListRecurso(){
		return listRecurso; 
	}
	public void setListRecurso(List<RecursoVO> listRecurso){
		this.listRecurso = listRecurso;
	}
	
	public boolean isEsNoTrib() {
		return esNoTrib;
	}

	public void setEsNoTrib(boolean esNoTrib) {
		this.esNoTrib = esNoTrib;
	}

	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva 
		report.setReportTitle("Reporte de Recursos del Siat");
		report.setReportBeanName("Recursos");
		report.setReportFormat(format);
	     // nombre del archivo 
		report.setReportFileName(this.getClass().getName());
		 
		 // carga de filtros:
		 // tipo de categoria
		 String desTipo = "";
		 TipoVO tipoVO = (TipoVO) ModelUtil.getBussImageModelByIdForList(
				 this.getRecurso().getCategoria().getTipo().getId(), 
				 this.getListTipoCategoria());
		 if (tipoVO != null){
			 desTipo = tipoVO.getDesTipo();
		 }
		 report.addReportFiltro("Tipo de Categoría", desTipo);
		 // categoria
		 String desCategoria = "No seleccionada";
		 CategoriaVO categoriaVO = (CategoriaVO) ModelUtil.getBussImageModelByIdForList(
				 this.getRecurso().getCategoria().getId(), 
				 this.getListCategoria());
		 if (categoriaVO != null){
			 desCategoria = categoriaVO.getDesCategoria();
		 }
		 //addReportFiltro("Categoria", "desCategoria", this.getRecurso().getCategoria().getId(), this.getListCategoria() );
		 
		 report.addReportFiltro("Categoria", desCategoria);
		 // codigo
		 report.addReportFiltro("Código Recurso", this.getRecurso().getCodRecurso());
		 // descripcion
		 report.addReportFiltro("Descripción Recurso", this.getRecurso().getDesRecurso());
		 
		 
		 // Order by
		 report.setReportOrderBy("codRecurso ASC");
		 
		 ReportTableVO rtRecursos = new ReportTableVO("Recursos");
		 rtRecursos.setTitulo("Listado de Recursos");

		 rtRecursos.addReportColumn("Descripcion", "recInfo");
		 rtRecursos.addReportColumn("Conceptos", "concInfo");
		 rtRecursos.addReportColumn("Partidas Capital", "disParInfo");

		 report.getReportListTable().add(rtRecursos);
	}

}
