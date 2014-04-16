//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.gde.iface.model.ConvenioReport;
import ar.gov.rosario.siat.gde.iface.model.ImporteRecaudarReport;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class ReporteImporteRecaudarDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(ReporteImporteRecaudarDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.CONSULTAR_IMPORTE_RECAUDAR, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			ImporteRecaudarReport importeRecaudarReportVO = GdeServiceLocator.getReporteService().getImporteRecaudarReportInit(userSession);
			
			// Tiene errores recuperables
			if (importeRecaudarReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + importeRecaudarReportVO.infoString()); 
				saveDemodaErrors(request, importeRecaudarReportVO);
				return forwardErrorRecoverable(mapping, request, userSession, ImporteRecaudarReport.NAME, importeRecaudarReportVO);
			} 

			// Tiene errores no recuperables
			if (importeRecaudarReportVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + importeRecaudarReportVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ImporteRecaudarReport.NAME, importeRecaudarReportVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , ImporteRecaudarReport.NAME, importeRecaudarReportVO);
			
			return mapping.findForward(GdeConstants.FWD_IMPORTE_RECAUDAR_REPORT);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ImporteRecaudarReport.NAME);
		}
	}

	public ActionForward paramPlanByRecurso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			
			try {
				
				// Bajo el searchPage del userSession
				ImporteRecaudarReport importeRecaudarReport = (ImporteRecaudarReport) userSession.get(ImporteRecaudarReport.NAME);
				
				// Si es nulo no se puede continuar
				if (importeRecaudarReport == null) {
					log.error("error en: "  + funcName + ": " + ImporteRecaudarReport.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ImporteRecaudarReport.NAME); 
				}
													
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(importeRecaudarReport, request);
				
				// Llamada al servicio	
				importeRecaudarReport = GdeServiceLocator.getReporteService().getImporteRecaudarReportParamRecurso(userSession, importeRecaudarReport);			
				
				// no visualizo la lista de resultados
				importeRecaudarReport.setPageNumber(0L);
				
				// Tiene errores recuperables
				if (importeRecaudarReport.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + importeRecaudarReport.infoString()); 
					saveDemodaErrors(request, importeRecaudarReport);
					return forwardErrorRecoverable(mapping, request, userSession, ImporteRecaudarReport.NAME, importeRecaudarReport);
				}
				
				// Tiene errores no recuperables
				if (importeRecaudarReport.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + importeRecaudarReport.errorString());
					return forwardErrorNonRecoverable(mapping, request, funcName, ImporteRecaudarReport.NAME, importeRecaudarReport);
				}
			
				// Envio el VO al request
				request.setAttribute(ImporteRecaudarReport.NAME, importeRecaudarReport);
				// Subo en el el searchPage al userSession
				userSession.put(ImporteRecaudarReport.NAME, importeRecaudarReport);
				
				return mapping.findForward(GdeConstants.FWD_IMPORTE_RECAUDAR_REPORT);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ImporteRecaudarReport.NAME);
			}
	}
	
	public ActionForward paramPlan(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			
			try {
				
				// Bajo el searchPage del userSession
				ImporteRecaudarReport importeRecaudarReport = (ImporteRecaudarReport) userSession.get(ImporteRecaudarReport.NAME);
				
				// Si es nulo no se puede continuar
				if (importeRecaudarReport == null) {
					log.error("error en: "  + funcName + ": " + ImporteRecaudarReport.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ImporteRecaudarReport.NAME); 
				}
								
	            // Tiene errores recuperables
				if (importeRecaudarReport.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + importeRecaudarReport.infoString()); 
					saveDemodaErrors(request, importeRecaudarReport);
					return forwardErrorRecoverable(mapping, request, userSession, ImporteRecaudarReport.NAME, importeRecaudarReport);
				}
					
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(importeRecaudarReport, request);
				
				// Llamada al servicio	
				importeRecaudarReport = GdeServiceLocator.getReporteService().getImporteRecaudarReportParamPlan(userSession, importeRecaudarReport);			
				
				// no visualizo la lista de resultados
				importeRecaudarReport.setPageNumber(0L);
				
				// Tiene errores recuperables
				if (importeRecaudarReport.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + importeRecaudarReport.infoString()); 
					saveDemodaErrors(request, importeRecaudarReport);
					return forwardErrorRecoverable(mapping, request, userSession, ImporteRecaudarReport.NAME, importeRecaudarReport);
				}
				
				// Tiene errores no recuperables
				if (importeRecaudarReport.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + importeRecaudarReport.errorString());
					return forwardErrorNonRecoverable(mapping, request, funcName, ImporteRecaudarReport.NAME, importeRecaudarReport);
				}
			
				// Envio el VO al request
				request.setAttribute(ImporteRecaudarReport.NAME, importeRecaudarReport);
				// Subo en el el searchPage al userSession
				userSession.put(ImporteRecaudarReport.NAME, importeRecaudarReport);
				
				return mapping.findForward(GdeConstants.FWD_IMPORTE_RECAUDAR_REPORT);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ImporteRecaudarReport.NAME);
			}
	}
	
	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, ImporteRecaudarReport.NAME);
		
	}
	
	public ActionForward buscar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// Bajo el searchPage del userSession
			ImporteRecaudarReport importeRecaudarReportVO = (ImporteRecaudarReport) userSession.get(ImporteRecaudarReport.NAME);
			
			// Si es nulo no se puede continuar
			if (importeRecaudarReportVO == null) {
				log.error("error en: "  + funcName + ": " + ImporteRecaudarReport.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ImporteRecaudarReport.NAME); 
			}
			

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(importeRecaudarReportVO, request);
			// Setea el PageNumber del PageModel				
			importeRecaudarReportVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));

			
            // Tiene errores recuperables
			if (importeRecaudarReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + importeRecaudarReportVO.infoString()); 
				saveDemodaErrors(request, importeRecaudarReportVO);
				return forwardErrorRecoverable(mapping, request, userSession, ImporteRecaudarReport.NAME, importeRecaudarReportVO);
			}
				
			// Llamada al servicio	
			ImporteRecaudarReport importeRecaudarReport = GdeServiceLocator.getReporteService().getImporteRecaudarReportResult(userSession, importeRecaudarReportVO);			

			// Tiene errores recuperables
			if (importeRecaudarReport.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + importeRecaudarReport.infoString()); 
				saveDemodaErrors(request, importeRecaudarReport);
				return forwardErrorRecoverable(mapping, request, userSession, ImporteRecaudarReport.NAME, importeRecaudarReport);
			}
			
			// Tiene errores no recuperables
			if (importeRecaudarReport.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + importeRecaudarReport.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, ImporteRecaudarReport.NAME, importeRecaudarReport);
			}
			
			// Envio el VO al request
			request.setAttribute(ImporteRecaudarReport.NAME, importeRecaudarReport);
			// Subo en el el searchPage al userSession
			userSession.put(ImporteRecaudarReport.NAME, importeRecaudarReport);
			
			return mapping.findForward(GdeConstants.FWD_IMPORTE_RECAUDAR_REPORT);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ImporteRecaudarReport.NAME);
		}
	}

	
	public ActionForward downloadFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);

			try {
				
				// obtenemos el nombre del archivo seleccionado
				String fileName = request.getParameter("fileParam");
				
				baseResponseFile(response,fileName);

				log.debug("exit: " + funcName);
				
				return null;
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ConvenioReport.NAME);
			}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ImporteRecaudarReport.NAME);
		
	}
		
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, ImporteRecaudarReport.NAME);
	}
}
