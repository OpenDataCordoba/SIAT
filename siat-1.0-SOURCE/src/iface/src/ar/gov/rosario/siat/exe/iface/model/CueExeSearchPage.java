//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.exe.iface.util.ExeSecurityConstants;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del CueExe
 * 
 * @author Tecso
 *
 */
public class CueExeSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "cueExeSearchPageVO";

	public CueExeVO  cueExe = new CueExeVO();
	
	private List<RecursoVO> 	 listRecurso = new ArrayList<RecursoVO>();	
	private List<ExencionVO>	 listExencion  = new ArrayList<ExencionVO>();
	private List<EstadoCueExeVO> listEstadoCueExe = new ArrayList<EstadoCueExeVO>();

	// Bandera para consulta estado en historico o no en historico
	private Boolean estadoEnHistorico = true;
	private boolean modoVer = false;
	
	@Deprecated
	private boolean disableCombo =false;
	
	@Deprecated
	private boolean conExencionPreseteada = false;
	private boolean permiteManPad = false;

	private boolean administrarCueExeEnabled = false;	
	
	// Constructor
	public CueExeSearchPage() {       
		super(ExeSecurityConstants.ABM_CUEEXE);        
	}

	// Getters y Setters
	public CueExeVO getCueExe() {
		return cueExe;
	}
	public void setCueExe(CueExeVO cueExe) {
		this.cueExe = cueExe;
	}

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}
	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	public List<ExencionVO> getListExencion() {
		return listExencion;
	}
	public void setListExencion(List<ExencionVO> listExencion) {
		this.listExencion = listExencion;
	}

	public List<EstadoCueExeVO> getListEstadoCueExe() {
		return listEstadoCueExe;
	}
	public void setListEstadoCueExe(List<EstadoCueExeVO> listEstadoCueExe) {
		this.listEstadoCueExe = listEstadoCueExe;
	}

	public Boolean getEstadoEnHistorico() {
		return estadoEnHistorico;
	}
	public void setEstadoEnHistorico(Boolean estadoEnHistorico) {
		this.estadoEnHistorico = estadoEnHistorico;
	}

	public boolean isModoVer() {
		return modoVer;
	}
	public void setModoVer(boolean modoVer) {
		this.modoVer = modoVer;
	}

	public String getName(){
		return NAME;
	}

	public void prepareReport(Long format) {

		ReportVO report = this.getReport(); // no instanciar una nueva
		report.setReportFormat(format);	
		report.setReportBeanName("CueExe");
		report.setReportFileName(this.getClass().getName());

		if(report.getImprimirDetalle().equals("2")){
			
			ReportVO reportCueDet = this.getReport(); // no instanciar una nueva
			reportCueDet.setReportFormat(format);	
			reportCueDet.setReportBeanName("CueExe");
			reportCueDet.setReportFileName(this.getClass().getName());
			reportCueDet.setPageWidth(29.7D);
			reportCueDet.setPageHeight(21.0D);

			reportCueDet.setReportTitle("B\u00FAsqueda de Exenciones Con Detalles");
             
			if(this.getEstadoEnHistorico()) {
				// Estado en Historico
				String desEstadoHis = "";

				EstadoCueExeVO hisEstCueExeVO = (EstadoCueExeVO) ModelUtil.getBussImageModelByIdForList(
						this.getCueExe().getHisEstCueExe().getEstadoCueExe().getId(),
						this.getListEstadoCueExe());
				if (hisEstCueExeVO != null){
					desEstadoHis = hisEstCueExeVO.getDesEstadoCueExe();
				}
				reportCueDet.addReportFiltro("Estado en Historico", desEstadoHis);
			}

			// carga de filtros

			// Recurso
			String desRecurso = "";

			RecursoVO recursoVO = (RecursoVO) ModelUtil.getBussImageModelByIdForList(
					this.getCueExe().getRecurso().getId(),
					this.getListRecurso());
			if (recursoVO != null){
				desRecurso = recursoVO.getDesRecurso();
			}
			reportCueDet.addReportFiltro("Recurso", desRecurso);

			// Exención/Caso Social/Otro
			String desExencion = "";

			ExencionVO exencionVO = (ExencionVO) ModelUtil.getBussImageModelByIdForList(
					this.getCueExe().getExencion().getId(),
					this.getListExencion());
			if (exencionVO != null){
				desExencion = exencionVO.getDesExencion();
			}

			reportCueDet.addReportFiltro("Exención/Caso Social/Otro", desExencion);
			
	
			//Solicitante
			reportCueDet.addReportFiltro("Solicitante", this.getCueExe().getSolicitante().getRepresent());
			//Descripción Solicitante
			reportCueDet.addReportFiltro("Descripción Solicitante", this.getCueExe().getSolicDescripcion());
			//Cuenta
			report.addReportFiltro("N\u00FAmero Cuenta", this.getCueExe().getCuenta().getNumeroCuenta());
			//Fecha Desde
			reportCueDet.addReportFiltro("Fecha Desde", this.getCueExe().getFechaDesdeView());
			//Fecha Hasta
			reportCueDet.addReportFiltro("Fecha Hasta", this.getCueExe().getFechaHastaView());
			// Estado
			String desEstado = "";

			EstadoCueExeVO estadoVO = (EstadoCueExeVO) ModelUtil.getBussImageModelByIdForList(
					this.getCueExe().getEstadoCueExe().getId(),
					this.getListEstadoCueExe());
			if (estadoVO != null){
				desEstado = estadoVO.getDesEstadoCueExe();
			}

			reportCueDet.addReportFiltro("Estado", desEstado);

			ReportTableVO rtExeDet = new ReportTableVO("rtExeDet");
			rtExeDet.setTitulo("B\u00FAsqueda de Exenciones Con Detalles");

			// carga de columnas
			rtExeDet.addReportColumn("Fecha Solicitud","fechaSolicitud", 18);
			rtExeDet.addReportColumn("Recurso", "cuenta.recurso.codRecurso", 16);
			rtExeDet.addReportColumn("N\u00FAmero cuenta", "cuenta.numeroCuenta", 18);
			rtExeDet.addReportColumn("Tipo de Sujeto", "tipoSujeto.desTipoSujeto", 18);
			rtExeDet.addReportColumn("Fecha Desde", "fechaDesde", 18);
			rtExeDet.addReportColumn("Fecha Hasta", "fechaHasta", 18);
			rtExeDet.addReportColumn("Exención/Caso Social/Otro", "exencion.desExencion", 22);
			rtExeDet.addReportColumn("Estado", "estadoCueExe.desEstadoCueExe", 18);
			rtExeDet.addReportColumn("Ord/Art/Inc", "ordArtInc", 20);
			rtExeDet.addReportColumn("Log. Historial", "logHistoricos");
			rtExeDet.addReportColumn("Expediente", "casoView",36);
			
			reportCueDet.getReportListTable().add(rtExeDet); 	

			report.setImprimirDetalle("1");

		} else {

			ReportVO reportCueDet = this.getReport(); // no instanciar una nueva
			reportCueDet.setReportFormat(format);	
			reportCueDet.setReportBeanName("CueExe");
			reportCueDet.setReportFileName(this.getClass().getName());
			reportCueDet.setPageWidth(21.0D);
			reportCueDet.setPageHeight(29.7D);

			reportCueDet.setReportTitle("B\u00FAsqueda de Exenciones");

			if(this.getEstadoEnHistorico()) {
				// Estado en Historico
				String desEstadoHis = "";

				EstadoCueExeVO hisEstCueExeVO = (EstadoCueExeVO) ModelUtil.getBussImageModelByIdForList(
						this.getCueExe().getHisEstCueExe().getEstadoCueExe().getId(),
						this.getListEstadoCueExe());
				if (hisEstCueExeVO != null){
					desEstadoHis = hisEstCueExeVO.getDesEstadoCueExe();
				}
				report.addReportFiltro("Estado en Historico", desEstadoHis);
			}

			// carga de filtros

			// Recurso
			String desRecurso = "";

			RecursoVO recursoVO = (RecursoVO) ModelUtil.getBussImageModelByIdForList(
					this.getCueExe().getRecurso().getId(),
					this.getListRecurso());
			if (recursoVO != null){
				desRecurso = recursoVO.getDesRecurso();
			}
			report.addReportFiltro("Recurso", desRecurso);

			// Exención/Caso Social/Otro
			String desExencion = "";

			ExencionVO exencionVO = (ExencionVO) ModelUtil.getBussImageModelByIdForList(
					this.getCueExe().getExencion().getId(),
					this.getListExencion());
			if (exencionVO != null){
				desExencion = exencionVO.getDesExencion();
			}

			report.addReportFiltro("Exención/Caso Social/Otro", desExencion);
			//Solicitante
			reportCueDet.addReportFiltro("Solicitante", this.getCueExe().getSolicitante().getRepresent());
			//Descripción Solicitante
			reportCueDet.addReportFiltro("Descripción Solicitante", this.getCueExe().getSolicDescripcion());
			//Cuenta
			report.addReportFiltro("N\u00FAmero Cuenta", this.getCueExe().getCuenta().getNumeroCuenta());
			//Fecha Desde
			report.addReportFiltro("Fecha Desde", this.getCueExe().getFechaDesdeView());
			//Fecha Hasta
			report.addReportFiltro("Fecha Hasta", this.getCueExe().getFechaHastaView());
			// Estado
			String desEstado = "";

			EstadoCueExeVO estadoVO = (EstadoCueExeVO) ModelUtil.getBussImageModelByIdForList(
					this.getCueExe().getEstadoCueExe().getId(),
					this.getListEstadoCueExe());
			if (estadoVO != null){
				desEstado = estadoVO.getDesEstadoCueExe();
			}

			report.addReportFiltro("Estado", desEstado);

			// carga de columnas 
			ReportTableVO rtExe = new ReportTableVO("rtExe");
			rtExe.setTitulo("B\u00FAsqueda de Exenciones");

			rtExe.addReportColumn("Fecha Solicitud","fechaSolicitud");
			rtExe.addReportColumn("Recurso", "cuenta.recurso.desRecurso");
			rtExe.addReportColumn("N\u00FAmero cuenta", "cuenta.numeroCuenta");
			rtExe.addReportColumn("Tipo de Sujeto", "tipoSujeto.desTipoSujeto");
			rtExe.addReportColumn("Fecha Desde", "fechaDesde");
			rtExe.addReportColumn("Fecha Hasta", "fechaHasta");
			rtExe.addReportColumn("Exención/Caso Social/Otro", "exencion.desExencion");
			rtExe.addReportColumn("Estado", "estadoCueExe.desEstadoCueExe");

			report.getReportListTable().add(rtExe); 

			report.setImprimirDetalle("0");

		}
	}
	public boolean getDisableCombo() {
		return disableCombo;
	}

	public void setDisableCombo(boolean disableCombo) {
		this.disableCombo = disableCombo;
	}

	public boolean getConExencionPreseteada() {
		return conExencionPreseteada;
	}

	public void setConExencionPreseteada(boolean conExencionPreseteada) {
		this.conExencionPreseteada = conExencionPreseteada;
	}

	public boolean isAdministrarCueExeEnabled() {
		return administrarCueExeEnabled;
	}
	public void setAdministrarCueExeEnabled(boolean administrarCueExeEnabled) {
		this.administrarCueExeEnabled = administrarCueExeEnabled;
	}

	public boolean getPermiteManPad() {
		return permiteManPad;
	}
	public void setPermiteManPad(boolean permiteManPad) {
		this.permiteManPad = permiteManPad;
	}

}
