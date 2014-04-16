//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del Reclamo
 * 
 * @author Tecso
 *
 */
public class ReclamoSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "reclamoSearchPageVO";


	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	private List<EstadoReclamoVO> listEstadoReclamo = new ArrayList<EstadoReclamoVO>();

	private ReclamoVO reclamo = new ReclamoVO();

	private Date 	fechaDesde; 
	private Date 	fechaHasta; 
	private String 	fechaDesdeView = "";
	private String 	fechaHastaView = "";

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
		this.fechaHastaView = DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK);
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
		this.fechaDesdeView = DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK);
	}

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

	// Constructores
	public ReclamoSearchPage() {       
		super(BalSecurityConstants.ABM_RECLAMO);        
	}

	// Getters y Setters


	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	public List<EstadoReclamoVO> getListEstadoReclamo() {
		return listEstadoReclamo;
	}

	public void setListEstadoReclamo(List<EstadoReclamoVO> listEstadoReclamo) {
		this.listEstadoReclamo = listEstadoReclamo;
	}

	public ReclamoVO getReclamo() {
		return reclamo;
	}

	public void setReclamo(ReclamoVO reclamo) {
		this.reclamo = reclamo;
	}

	public String getName(){
		return NAME;
	}

	public void prepareReport(Long format) {

		ReportVO report = this.getReport(); // no instanciar una nueva
		report.setReportFormat(format);	
		report.setReportTitle("Listados de Reclamos");
		report.setReportBeanName("reclamo");
		report.setReportFileName(this.getClass().getName());


		String desRecurso = "";

		RecursoVO recursoVO = (RecursoVO) ModelUtil.getBussImageModelByIdForList(
				this.getReclamo().getRecurso().getId(),
				this.getListRecurso());
		if (recursoVO != null){
			desRecurso = recursoVO.getDesRecurso();
		}
		report.addReportFiltro("Recurso", desRecurso);

		// Estado Reclamo
		String desEstadoReclamo = "";

		EstadoReclamoVO estadoReclamoVO = (EstadoReclamoVO) ModelUtil.getBussImageModelByIdForList(
				this.getReclamo().getEstadoReclamo().getId(),
				this.getListEstadoReclamo());
		if (estadoReclamoVO != null){
			desEstadoReclamo = estadoReclamoVO.getDesEstadoReclamo();
		}
		report.addReportFiltro("Estado Reclamo", desEstadoReclamo);

		//Fecha Desde
		report.addReportFiltro("Fecha Desde", this.getFechaDesdeView());

		//Fecha Hasta
		report.addReportFiltro("Fecha Hasta", this.getFechaHastaView());

		//Nro Cuenta
		report.addReportFiltro("Nro Cuenta", this.getReclamo().getNroCuentaView());


		ReportTableVO rtReclamo = new ReportTableVO("rtReclamo");
		rtReclamo.setTitulo("B\u00FAsqueda de Reclamos");

		// carga de columnas
		rtReclamo.addReportColumn("Nro.","id");
		rtReclamo.addReportColumn("Fecha","fechaAlta");
		rtReclamo.addReportColumn("Cuenta", "nroCuenta");
		rtReclamo.addReportColumn("TipoBoleta", "desTipoBoleta");
		rtReclamo.addReportColumn("Estado", "estadoReclamo.desEstadoReclamo");
		//rtReclamo.addReportColumn("Observación", "observacion");
		 
	    report.getReportListTable().add(rtReclamo);

	}
}
