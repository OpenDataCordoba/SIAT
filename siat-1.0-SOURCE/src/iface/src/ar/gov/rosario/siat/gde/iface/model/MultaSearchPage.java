//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del Multa
 * 
 * @author Tecso
 *
 */
public class MultaSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "multaSearchPageVO";
	
	private MultaVO multa= new MultaVO();
	
    private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
    
    private RecursoVO recurso = new RecursoVO();
    
    private Date fechaEmisionDesde;
    private Date fechaEmisionHasta;
    
    private String fechaEmisionDesdeView="";  
    private String fechaEmisionHastaView="";
	

	// Constructores
	public MultaSearchPage() {       
       super(GdeSecurityConstants.ABM_MULTA);        
    }
	
	// Getters y Setters
	public MultaVO getMulta() {
		return multa;
	}
	public void setMulta(MultaVO multa) {
		this.multa = multa;
	}           

    public Date getFechaEmisionDesde() {
		return fechaEmisionDesde;
	}

	public void setFechaEmisionDesde(Date fechaEmisionDesde) {
		this.fechaEmisionDesde = fechaEmisionDesde;
		this.fechaEmisionDesdeView = DateUtil.formatDate(fechaEmisionDesde, DateUtil.ddSMMSYYYY_MASK);
	}

	public Date getFechaEmisionHasta() {
		return fechaEmisionHasta;
	}

	public void setFechaEmisionHasta(Date fechaEmisionHasta) {
		this.fechaEmisionHasta = fechaEmisionHasta;
		this.fechaEmisionHastaView = DateUtil.formatDate(fechaEmisionHasta, DateUtil.ddSMMSYYYY_MASK);
	}

	public String getName(){    
		return NAME;
	}
	
	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	public RecursoVO getRecurso() {
		return recurso;
	}

	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}

	public void prepareReport(Long format) {

		ReportVO report = this.getReport(); // no instanciar una nueva
		report.setReportFormat(format);	
		report.setReportTitle("Listados de Multa");
		report.setReportBeanName("Multa");
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
/*
		//Código
		report.addReportFiltro("Código", this.getMulta().getCodMulta());
       //Descripción
		report.addReportFiltro("Descripción", this.getMulta().getDesMulta());
		

		ReportTableVO rtMulta = new ReportTableVO("rtMulta");
		rtMulta.setTitulo("B\u00FAsqueda de Multa");

		// carga de columnas
		rtMulta.addReportColumn("Código","codMulta");
		rtMulta.addReportColumn("Descripción", "desMulta");
		
		 
	    report.getReportListTable().add(rtMulta);
*/
	}
	// View getters
	
	public String getFechaEmisionDesdeView() {
		return fechaEmisionDesdeView;
	}

	public void setFechaEmisionDesdeView(String fechaEmisionDesdeView) {
		this.fechaEmisionDesdeView = fechaEmisionDesdeView;
	}

	public String getFechaEmisionHastaView() {
		return fechaEmisionHastaView;
	}

	public void setFechaEmisionHastaView(String fechaEmisionHastaView) {
		this.fechaEmisionHastaView = fechaEmisionHastaView;
	}
}
