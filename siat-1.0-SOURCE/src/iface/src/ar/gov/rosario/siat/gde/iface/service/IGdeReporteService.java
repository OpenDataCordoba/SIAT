//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.service;

import ar.gov.rosario.siat.gde.iface.model.ContribuyenteCerReport;
import ar.gov.rosario.siat.gde.iface.model.ConvenioACaducarReport;
import ar.gov.rosario.siat.gde.iface.model.ConvenioFormReport;
import ar.gov.rosario.siat.gde.iface.model.ConvenioReport;
import ar.gov.rosario.siat.gde.iface.model.DeudaAnuladaReport;
import ar.gov.rosario.siat.gde.iface.model.DeudaProcuradorReport;
import ar.gov.rosario.siat.gde.iface.model.DistribucionReport;
import ar.gov.rosario.siat.gde.iface.model.EmisionReport;
import ar.gov.rosario.siat.gde.iface.model.ImporteRecaudadoReport;
import ar.gov.rosario.siat.gde.iface.model.ImporteRecaudarReport;
import ar.gov.rosario.siat.gde.iface.model.RecaudacionReport;
import ar.gov.rosario.siat.gde.iface.model.RecaudadoReport;
import ar.gov.rosario.siat.gde.iface.model.RespuestaOperativosReport;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.UserContext;


public interface IGdeReporteService {
	
	public ConvenioReport getConvenioReportInit(UserContext userContext) throws DemodaServiceException;
	public ConvenioReport getConvenioReportParamRecurso(UserContext userContext, ConvenioReport convenioReport) throws DemodaServiceException;
	public ConvenioReport getConvenioReportParamPlan(UserContext userContext, ConvenioReport convenioReport) throws DemodaServiceException;	
	public ConvenioReport getConvenioReportParamViaDeuda(UserContext userContext, ConvenioReport convenioReport) throws DemodaServiceException;
	public ConvenioReport getConvenioReportResult(UserContext userContext, ConvenioReport convenioReport) throws DemodaServiceException;
	
	public RecaudacionReport getRecaudacionReportInit(UserContext userContext) throws DemodaServiceException;
	public RecaudacionReport getRecaudacionReportParamCategoria(UserContext userContext, RecaudacionReport recaudacionReport) throws DemodaServiceException;
	public RecaudacionReport getRecaudacionReportResult(UserContext userContext, RecaudacionReport recaudacionReport) throws DemodaServiceException;

	// ---> Consultar Importe a Recaudar  de planes
	public ImporteRecaudarReport getImporteRecaudarReportInit(UserContext userContext) throws DemodaServiceException;
	public ImporteRecaudarReport getImporteRecaudarReportParamRecurso(UserContext userContext, ImporteRecaudarReport importeRecaudarReport) throws DemodaServiceException;
	public ImporteRecaudarReport getImporteRecaudarReportParamPlan(UserContext userContext, ImporteRecaudarReport importeRecaudarReport) throws DemodaServiceException;
	public ImporteRecaudarReport getImporteRecaudarReportResult(UserContext userContext, ImporteRecaudarReport importeRecaudarReport) throws DemodaServiceException;
	// <--- Consultar Importe a Recaudar  de planes
	
	// ---> Consultar Importe Recaudado  de planes
	public ImporteRecaudadoReport getImporteRecaudadoPlanesReportInit(UserContext userContext) throws DemodaServiceException;
	public ImporteRecaudadoReport getImporteRecaudadoPlanesReportParamRecurso(UserContext userContext, ImporteRecaudadoReport importeRecaudadoReport) throws DemodaServiceException;
	public ImporteRecaudadoReport getImporteRecaudadoPlanesReportParamPlan(UserContext userContext, ImporteRecaudadoReport importeRecaudadoReport) throws DemodaServiceException;
	public ImporteRecaudadoReport getImporteRecaudadoPlanesReportParamViaDeuda(UserContext userContext,ImporteRecaudadoReport importeRecaudadoReport) throws DemodaServiceException;
	public ImporteRecaudadoReport getImporteRecaudadoPlanesReportResult(UserContext userContext, ImporteRecaudadoReport importeRecaudadoReport) throws DemodaServiceException;
	// <--- Consultar Importe Recaudado de planes
	
	// ---> Reporte Convenios Formalizados
	public ConvenioFormReport getConvenioFormReportInit(UserContext userContext) throws DemodaServiceException;
	public ConvenioFormReport getConvenioFormReportParamViaDeuda(UserContext userContext, ConvenioFormReport convenioFormReportVO) throws DemodaServiceException;
	public ConvenioFormReport getConvenioFormReportResult(UserContext userContext, ConvenioFormReport convenioFormReport) throws DemodaServiceException;
	// <--- Reporte Convenios Formalizados
	
	// ---> Consultar Respuesta Operativos
	public RespuestaOperativosReport getRespuestaOperativosReportInit(UserContext userContext) throws DemodaServiceException;
	public RespuestaOperativosReport getRespuestaOperativosReportResult(UserContext userContext, RespuestaOperativosReport respuestaOperativosReport) throws DemodaServiceException;
	public RespuestaOperativosReport getRespuestaOperativosReportParamTipProMas(UserContext userContext, RespuestaOperativosReport respuestaOperativosReport) throws DemodaServiceException;
	// <--- Consultar Respuesta Operativos
	
	// ---> Consultar Convenios A Caducar
	public ConvenioACaducarReport getConvenioACaducarReportInit(UserContext userContext) throws DemodaServiceException;
	public ConvenioACaducarReport getConvenioACaducarReportResult(UserContext userContext, ConvenioACaducarReport convenioACaducarReport) throws DemodaServiceException;
	public ConvenioACaducarReport getConvenioACaducarReportParamRecurso(UserContext userContext, ConvenioACaducarReport convenioACaducarReport) throws DemodaServiceException;
	public ConvenioACaducarReport getConvenioACaducarReportParamPlan(UserContext userContext, ConvenioACaducarReport convenioACaducarReport) throws DemodaServiceException;	
	// <--- Consultar Convenios A Caducar
	
	// ---> Reporte de Anulacion de Deuda
	public DeudaAnuladaReport getDeudaAnuladaReportInit(UserContext userContext) throws DemodaServiceException;
	public DeudaAnuladaReport getDeudaAnuladaReportResult(UserContext userContext, DeudaAnuladaReport deudaAnuladaReportVO) throws DemodaServiceException;
	// <--- Reporte de Anulacion de Deuda	
	
	// ---> Reporte de totales de emision
	public EmisionReport getEmisionReportInit(UserContext userContext) throws DemodaServiceException;
	public EmisionReport getEmisionReportReportParamRecurso(UserContext userContext, EmisionReport emisionReport) throws DemodaServiceException;
	public EmisionReport getEmisionReportResult(UserContext userContext, EmisionReport emisionReport) throws DemodaServiceException;
	// <--- Reporte de totales de emision

	// ---> Reporte de Recaudado NUEVO
	public RecaudadoReport getRecaudadoReportInit(UserContext userContext) throws DemodaServiceException;
	public RecaudadoReport getRecaudadoReportResult(UserContext userContext, RecaudadoReport recaudadoReport) throws DemodaServiceException;
	// <--- Reporte de Recaudado NUEVO
	
	// ---> Reporte de Deuda de Procurador
	public DeudaProcuradorReport getDeudaProcuradorReportInit(UserContext userContext) throws DemodaServiceException;
	public DeudaProcuradorReport getDeudaProcuradorReportResult(UserContext userContext, DeudaProcuradorReport deudaProcuradorReport) throws DemodaServiceException;
	// <--- Reporte de Deuda de Procurador	


	// ---> Reporte Total contribuyente CER
	public ContribuyenteCerReport getContribuyenteCerReportInit(UserContext userContext) throws DemodaServiceException;
	public ContribuyenteCerReport getContribuyenteCerReportResult(UserContext userContext, ContribuyenteCerReport contribuyenteCerReport) throws DemodaServiceException;
	// <--- Reporte Total contribuyente CER
	
	// ---> Reporte Total contribuyente CER X Recurso
	public ContribuyenteCerReport getRecConCerReportInit(UserContext userContext) throws DemodaServiceException;
	public ContribuyenteCerReport getRecConCerReportResult(UserContext userContext, ContribuyenteCerReport contribuyenteCerReport) throws DemodaServiceException;
	// <--- Reporte Total contribuyente CER X Recurso
	
	// ---> Reporte Total Recaudación CER X Sector
	public ContribuyenteCerReport getRecaudacionCerReportInit(UserContext userContext) throws DemodaServiceException;
	public ContribuyenteCerReport getRecaudacionCerReportResult(UserContext userContext, ContribuyenteCerReport contribuyenteCerReport) throws DemodaServiceException;
	// <--- Reporte Total Recaudación CER X Sector
	
	// --->Reporte total detallado Recaudación CER X Sector	
	public ContribuyenteCerReport getDetRecaudacionCerReportInit(UserContext userContext) throws DemodaServiceException;
	public ContribuyenteCerReport getDetRecaudacionCerReportResult(UserContext userContext, ContribuyenteCerReport contribuyenteCerReport) throws DemodaServiceException;
	// <--- Reporte Total detallado Recaudación CER X Sector
	
	// ---> Reporte de Distribucion de Totales Emitidos
	public DistribucionReport getDistribucionReportInit(UserContext userContext) throws DemodaServiceException;
	public DistribucionReport getDistribucionReportResult(UserContext userContext, DistribucionReport distribucionReport) throws DemodaServiceException;
	// <--- Reporte de Distribucion de Totales Emitidos



}
