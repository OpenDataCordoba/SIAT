//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.cas.iface.model.SistemaOrigenVO;
import ar.gov.rosario.siat.gde.iface.model.CierreComercioVO;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del CierreComercio
 * 
 * @author Tecso
 *
 */
public class CierreComercioSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "cierreComercioSearchPageVO";

	private CierreComercioVO cierreComercio = new CierreComercioVO();
	
	private Date fechaCierreDefDesde;

	private Date fechaCierreDefHasta;
	
	private String fechaCierreDefDesdeView = "";
	
	private String fechaCierreDefHastaView = "";

	private Date fechaCeseActividadDesde;

	private Date fechaCeseActividadHasta;
	
	private String fechaCeseActividadDesdeView = "";
	
	private String fechaCeseActividadHastaView = "";

	private List<SistemaOrigenVO> listSistemaOrigen = new ArrayList<SistemaOrigenVO>();
	
	// Constructores
	public CierreComercioSearchPage() {       
       super(PadSecurityConstants.ABM_CIERRECOMERCIO);        
    }

	// Getters y Setters
	public CierreComercioVO getCierreComercio() {
		return cierreComercio;
	}
	public void setCierreComercio(CierreComercioVO cierreComercio) {
		this.cierreComercio = cierreComercio;
	}           
		
    public String getName(){    
		return NAME;
	}

	public Date getFechaCierreDefDesde() {
		return fechaCierreDefDesde;
	}

	public void setFechaCierreDefDesde(Date fechaCierreDefDesde) {
		this.fechaCierreDefDesde = fechaCierreDefDesde;
		this.fechaCierreDefDesdeView = DateUtil.formatDate(fechaCierreDefDesde, DateUtil.ddSMMSYYYY_MASK);
	}

	public Date getFechaCierreDefHasta() {
		return fechaCierreDefHasta;
	}

	public void setFechaCierreDefHasta(Date fechaCierreDefHasta) {
		this.fechaCierreDefHasta = fechaCierreDefHasta;
		this.fechaCierreDefHastaView = DateUtil.formatDate(fechaCierreDefHasta, DateUtil.ddSMMSYYYY_MASK);
	}

	public Date getFechaCeseActividadDesde() {
		return fechaCeseActividadDesde;
	}

	public void setFechaCeseActividadDesde(Date fechaCeseActividadDesde) {
		this.fechaCeseActividadDesde = fechaCeseActividadDesde;
		this.fechaCeseActividadDesdeView = DateUtil.formatDate(fechaCeseActividadDesde, DateUtil.ddSMMSYYYY_MASK);
	}

	public Date getFechaCeseActividadHasta() {
		return fechaCeseActividadHasta;
	}

	public void setFechaCeseActividadHasta(Date fechaCeseActividadHasta) {
		this.fechaCeseActividadHasta = fechaCeseActividadHasta;
		this.fechaCeseActividadHastaView = DateUtil.formatDate(fechaCeseActividadHasta, DateUtil.ddSMMSYYYY_MASK);
	}

	public List<SistemaOrigenVO> getListSistemaOrigen() {
		return listSistemaOrigen;
	}

	public void setListSistemaOrigen(List<SistemaOrigenVO> listSistemaOrigen) {
		this.listSistemaOrigen = listSistemaOrigen;
	}

	
	// View flags getters
	public String getFechaCierreDefDesdeView() {
		return fechaCierreDefDesdeView;
	}

	public void setFechaCierreDefDesdeView(
			String fechaCierreDefDesdeView) {
		this.fechaCierreDefDesdeView = fechaCierreDefDesdeView;
	}

	public String getFechaCierreDefHastaView() {
		return fechaCierreDefHastaView;
	}

	public void setFechaCierreDefHastaView(
			String fechaCierreDefHastaView) {
		this.fechaCierreDefHastaView = fechaCierreDefHastaView;
	}
	
	public String getFechaCeseActividadDesdeView() {
		return fechaCeseActividadDesdeView;
	}

	public void setFechaCeseActividadDesdeView(String fechaCeseActividadDesdeView) {
		this.fechaCeseActividadDesdeView = fechaCeseActividadDesdeView;
	}

	public String getFechaCeseActividadHastaView() {
		return fechaCeseActividadHastaView;
	}

	public void setFechaCeseActividadHastaView(String fechaCeseActividadHastaView) {
		this.fechaCeseActividadHastaView = fechaCeseActividadHastaView;
	}
    
    
	public void prepareReport(Long format) {

		ReportVO report = this.getReport(); // no instanciar una nueva
		report.setReportFormat(format);	
		report.setReportTitle("Listados de CierreComercio");
		report.setReportBeanName("CierreComercio");
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
		//report.addReportFiltro("Descripción", this.getCierreComercio().getDesCierreComercio());
		

		ReportTableVO rtCierreComercio = new ReportTableVO("rtCierreComercio");
		rtCierreComercio.setTitulo("B\u00FAsqueda de CierreComercio");

		// carga de columnas
		rtCierreComercio.addReportColumn("Código","codCierreComercio");
		rtCierreComercio.addReportColumn("Descripción", "desCierreComercio");
		
		 
	    report.getReportListTable().add(rtCierreComercio);

	}
	// View getters
}
