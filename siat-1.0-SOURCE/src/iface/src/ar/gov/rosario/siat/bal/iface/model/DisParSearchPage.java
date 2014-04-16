//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Search Page de DisPar
 * @author tecso
 *
 */
public class DisParSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "disParSearchPageVO";

	private DisParVO disPar = new DisParVO();

	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();

	private List<DisParVO> listDisPar = new ArrayList<DisParVO>();
	
	private Boolean asociarRecursoViaBussEnabled      = true;
	private Boolean asociarPlanBussEnabled      = true;

	
	public DisParSearchPage(){
		super(BalSecurityConstants.ABM_DISPAR);
	}

	// Getters y Setters
	public DisParVO getDisPar() {
		return disPar;
	}
	public void setDisPar(DisParVO disPar) {
		this.disPar = disPar;
	}
	public List<DisParVO> getListDisPar() {
		return listDisPar;
	}
	public void setListDisPar(List<DisParVO> listDisPar) {
		this.listDisPar = listDisPar;
	}
	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}
	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}
	
	// Flags Seguridad
	public Boolean getAsociarPlanBussEnabled() {
		return asociarPlanBussEnabled;
	}

	public void setAsociarPlanBussEnabled(Boolean asociarPlanBussEnabled) {
		this.asociarPlanBussEnabled = asociarPlanBussEnabled;
	}
	
	public String getAsociarPlanEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getAsociarPlanBussEnabled(), 
				BalSecurityConstants.ABM_DISPAR, BalSecurityConstants.ABM_DISPAR_ADM_DISPARPLA);
	}
	
	public Boolean getAsociarRecursoViaBussEnabled() {
		return asociarRecursoViaBussEnabled;
	}
	public void setAsociarRecursoViaBussEnabled(Boolean asociarRecursoViaBussEnabled) {
		this.asociarRecursoViaBussEnabled = asociarRecursoViaBussEnabled;
	}
	
	public String getAsociarRecursoViaEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getAsociarRecursoViaBussEnabled(), 
				BalSecurityConstants.ABM_DISPAR, BalSecurityConstants.ABM_DISPAR_ADM_DISPARREC);
	}
	
	public String getName(){
		return NAME;
	}

	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva	
		 report.setReportTitle("Reporte de Distribuidores de Partidas del Siat");
		 report.setReportBeanName("DistribuidoresPartida");
		 report.setReportFormat(format);
		 // apaisado
		 report.setPageHeight(ReportVO.PAGE_WIDTH);
		 report.setPageWidth(ReportVO.PAGE_HEIGHT);
		 
		 // nombre del archivo a generar en base al class del SearchPage id usuario y formato pasado
		 report.setReportFileName(this.getClass().getName());
		 
		 // carga de filtros:
		 // descripcion
		 report.addReportFiltro("Descripción", this.getDisPar().getDesDisPar());
		 // recurso
		 String desRecurso = "";
		 
		 RecursoVO recursoVO = (RecursoVO) ModelUtil.getBussImageModelByIdForList(
				 this.getDisPar().getRecurso().getId(), 
				 this.getListRecurso());
		 if (recursoVO != null){
			 desRecurso = recursoVO.getDesRecurso();
		 }
		 report.addReportFiltro("Recurso", desRecurso);

	     // Order by
	     //setReportOrderBy("tipoImporte.desTipoImporte ASC"); NO FUNCIONA
	     
	     // PageModel de DisPar, contiene un PageModel de ProRec
		 ReportVO pmDisPar = new ReportVO();
	     pmDisPar.addReportDato("Distribuidor Partida", "desDisPar");
	     if (ModelUtil.isNullOrEmpty(recursoVO)){
	    	 pmDisPar.addReportDato("Recurso", "recurso.desRecurso");
	     }
	     
	     pmDisPar.addReportDato("Tipo Importe", "tipoImporte.desTipoImporte");
	     
	     //PageModel pmDisParRec = new PageModel();     // disParRec de cada DisPar
	     //pmDisParRec.setReportTitle("Recurso: FechaDesde: Fecha Hasta");
	     //pmDisParRec.setReportMetodo("listDisParRec");   // metodo a ejecutar para llenar el pmDisParRec

	     //pmDisParRec.addReportDato("Recurso", "recurso.desRecurso");  
	     //pmDisParRec.addReportDato("Fecha Desde", "fechaDesde");
	     //pmDisParRec.addReportDato("Fecha Hasta", "fechaHasta");
	     
	     ReportTableVO rtDisParRec = new ReportTableVO("DisParRec");
	     rtDisParRec.setTitulo("Distribuidor de Partida con Recurso para determinado");
	     rtDisParRec.setReportMetodo("listDisParRec");  // metodo a ejecutar para llenar el 
	     
	     rtDisParRec.addReportColumn("Fecha Desde", "fechaDesde");
	     rtDisParRec.addReportColumn("Fecha Hasta", "fechaHasta");
	     rtDisParRec.addReportColumn("Valor", "valor");
	     // TODO VER AGREGAR OTRAS
	     pmDisPar.getReportListTable().add(rtDisParRec);

	     ReportTableVO rtDisParDet = new ReportTableVO("DisParDet");  // manzaneros de un proRec
	     rtDisParDet.setTitulo("Detalle de Distribuidor de Partida");
	     rtDisParDet.setReportMetodo("listDisParDetOrderByTipoImpDesRecCon");  // metodo a ejecutar para llenar la tabla de manzanero
	     rtDisParDet.addReportColumn("Fecha Desde", "fechaDesde");
	     rtDisParDet.addReportColumn("Fecha Hasta", "fechaHasta");
	     rtDisParDet.addReportColumn("Tipo Importe", "tipoImporte.desTipoImporte");
	     rtDisParDet.addReportColumn("Concepto", "recCon.desRecCon");
	     rtDisParDet.addReportColumn("Porcentaje", "porcentaje");
	     rtDisParDet.addReportColumn("Partida", "partida.codDesPartida", 45);
	     // TODO VER AGREGAR OTRAS	     
	     pmDisPar.getReportListTable().add(rtDisParDet); 
	     
	     //pmProcurador.getReportListPageModel().add(pmDisPar);
	     report.getListReport().add(pmDisPar);
	}

}
