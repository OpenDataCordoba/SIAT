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
 * Categorias
 * @author tecso
 *
 */
public class CategoriaSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "categoriaSearchPageVO";
	
	private CategoriaVO	categoria = new CategoriaVO();
	
	private List<TipoVO> listTipo = new ArrayList<TipoVO>();
	
	private List<CategoriaVO> listCategoria = new ArrayList<CategoriaVO>();
	
	public CategoriaSearchPage() {
		super(DefSecurityConstants.ABM_CATEGORIA);
    }
	
	// Getters y Setters
	public CategoriaVO getCategoria() {
		return categoria;
	}
	public void setCategoria(CategoriaVO categoria) {
		this.categoria = categoria;
	}		
	public TipoVO getTipo() {
		return categoria.getTipo();
	}
	public void setTipo(TipoVO tipo) {
		this.categoria.setTipo(tipo);
	}
	public List<CategoriaVO> getListCategoria(){
		return listCategoria;
	}
	public void setListCategoria(List<CategoriaVO> listCategoria){
		this.listCategoria = listCategoria;
	}
	public List<TipoVO> getListTipo() {
		return listTipo;
	}
	public void setListTipo(List<TipoVO> listTipo) {
		this.listTipo = listTipo;
	}
	
	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva
		report.setReportFormat(format);	
		report.setReportTitle("Reporte de Categorias");
		report.setReportBeanName("Categorias");
		report.setReportFileName(this.getClass().getName());
		 
		 // carga de filtros
		 String desTipoCategoria = "";
		 TipoVO tipoVO = (TipoVO) ModelUtil.getBussImageModelByIdForList(
				 this.getCategoria().getTipo().getId(), 
				 this.getListTipo());
		 if (tipoVO != null){
			 desTipoCategoria = tipoVO.getDesTipo();
		 }
		 boolean tipoCategoriaSeleccionado = !ModelUtil.isNullOrEmpty(this.getCategoria().getTipo());
		 
		 report.addReportFiltro("Tipo de Categoría", desTipoCategoria);
		 report.addReportFiltro("Descripción", this.getCategoria().getDesCategoria());
		 
		 // Order by
		 report.setReportOrderBy("desCategoria ASC");
		 
	     ReportTableVO rtCategoria = new ReportTableVO("Categoria");
	     rtCategoria.setTitulo("Listado de Categorías");
	     
		 // carga de columnas
	     rtCategoria.addReportColumn("Descripción", "desCategoria");
	     if (!tipoCategoriaSeleccionado){
	    	 rtCategoria.addReportColumn("Tipo de Categoría", "tipo.desTipo");
	     }
	     rtCategoria.addReportColumn("Estado", "estadoView");
	     
	     report.getReportListTable().add(rtCategoria);
	}

}
