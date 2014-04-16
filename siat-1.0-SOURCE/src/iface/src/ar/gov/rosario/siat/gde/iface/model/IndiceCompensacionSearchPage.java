//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del IndiceCompensacion
 * 
 * @author Tecso
 *
 */
public class IndiceCompensacionSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "indiceCompensacionSearchPageVO";
	
	private List<IndiceCompensacionVO> listIndice = new ArrayList<IndiceCompensacionVO>();
	
	private Date fechaDesde;
	private Date fechaHasta;
	
	private String fechaDesdeView="";
	private String fechaHastaView="";

	// Constructores
	public IndiceCompensacionSearchPage() {       
       super(GdeSecurityConstants.ABM_INDICECOMPENSACION);        
    }
	
	// Getters y Setters
    public String getName(){    
		return NAME;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	public List<IndiceCompensacionVO> getListIndice() {
		return listIndice;
	}

	public void setListIndice(List<IndiceCompensacionVO> listIndice) {
		this.listIndice = listIndice;
	}
	
	public void prepareReport(Long format) {

		ReportVO report = this.getReport(); // no instanciar una nueva
		report.setReportFormat(format);	
		report.setReportTitle("Listados de Indice de Compesnación");
		report.setReportBeanName("IndiceCompensacion");
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
		//report.addReportFiltro("Descripción", this.getIndiceCompensacion().getDesIndiceCompensacion());
		

		ReportTableVO rtIndiceCompensacion = new ReportTableVO("rtIndiceCompensacion");
		rtIndiceCompensacion.setTitulo("B\u00FAsqueda de Indice Compensación");

		// carga de columnas
		rtIndiceCompensacion.addReportColumn("Descripción", "desIndiceCompensacion");
		
		 
	    report.getReportListTable().add(rtIndiceCompensacion);

	}

	// View getters
	public String getFechaDesdeView() {
		return fechaDesdeView;
	}

	public void setFechaDesdeView(String fechaDesdeView) {
		this.fechaDesdeView = fechaDesdeView;
	}

	public String getFechaHastaView() {
		return fechaHastaView;
	}

	public void setFechaHastaView(String fechaHastaView) {
		this.fechaHastaView = fechaHastaView;
	}
}
