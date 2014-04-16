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
import ar.gov.rosario.siat.gde.iface.model.ImporteRecaudadoReport;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class ReporteImporteRecaudadoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(ReporteImporteRecaudadoDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.CONSULTAR_IMPORTE_RECAUDADO_PLANES, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			ImporteRecaudadoReport importeRecaudadoReportVO = GdeServiceLocator.getReporteService().getImporteRecaudadoPlanesReportInit(userSession);
			
			// Tiene errores recuperables
			if (importeRecaudadoReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + importeRecaudadoReportVO.infoString()); 
				saveDemodaErrors(request, importeRecaudadoReportVO);
				return forwardErrorRecoverable(mapping, request, userSession, ImporteRecaudadoReport.NAME, importeRecaudadoReportVO);
			} 

			// Tiene errores no recuperables
			if (importeRecaudadoReportVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + importeRecaudadoReportVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ImporteRecaudadoReport.NAME, importeRecaudadoReportVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , ImporteRecaudadoReport.NAME, importeRecaudadoReportVO);
			
			return mapping.findForward(GdeConstants.FWD_IMPORTE_RECAUDADO_REPORT);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ImporteRecaudadoReport.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, ImporteRecaudadoReport.NAME);
		
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
				ImporteRecaudadoReport importeRecaudadoReport = (ImporteRecaudadoReport) userSession.get(ImporteRecaudadoReport.NAME);								
				
				// Si es nulo no se puede continuar
				if (importeRecaudadoReport == null) {
					log.error("error en: "  + funcName + ": " + ImporteRecaudadoReport.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ImporteRecaudadoReport.NAME); 
				}
													
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(importeRecaudadoReport, request);
				
				// Llamada al servicio	
				importeRecaudadoReport = GdeServiceLocator.getReporteService().getImporteRecaudadoPlanesReportParamRecurso(userSession, importeRecaudadoReport);			
				
				// no visualizo la lista de resultados
				importeRecaudadoReport.setPageNumber(0L);
				
				// Tiene errores recuperables
				if (importeRecaudadoReport.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + importeRecaudadoReport.infoString()); 
					saveDemodaErrors(request, importeRecaudadoReport);
					// Envio el VO al request
					request.setAttribute(ImporteRecaudadoReport.NAME, importeRecaudadoReport);
					// Subo en el el searchPage al userSession
					userSession.put(ImporteRecaudadoReport.NAME, importeRecaudadoReport);

					return forwardErrorRecoverable(mapping, request, userSession, ImporteRecaudadoReport.NAME, importeRecaudadoReport);
				}
				
				// Tiene errores no recuperables
				if (importeRecaudadoReport.hasErrorNonRecoverable()) {					
					log.error("error en: "  + funcName + ": " + importeRecaudadoReport.errorString());
					
					return forwardErrorNonRecoverable(mapping, request, funcName, ImporteRecaudadoReport.NAME, importeRecaudadoReport);
				}
			
				// Envio el VO al request
				request.setAttribute(ImporteRecaudadoReport.NAME, importeRecaudadoReport);
				// Subo en el el searchPage al userSession
				userSession.put(ImporteRecaudadoReport.NAME, importeRecaudadoReport);
				
				return mapping.findForward(GdeConstants.FWD_IMPORTE_RECAUDADO_REPORT);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ImporteRecaudadoReport.NAME);
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
				ImporteRecaudadoReport importeRecaudadoReport = (ImporteRecaudadoReport) userSession.get(ImporteRecaudadoReport.NAME);
				
				// Si es nulo no se puede continuar
				if (importeRecaudadoReport == null) {
					log.error("error en: "  + funcName + ": " + ImporteRecaudadoReport.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ImporteRecaudadoReport.NAME); 
				}
								
	            // Tiene errores recuperables
				if (importeRecaudadoReport.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + importeRecaudadoReport.infoString()); 
					saveDemodaErrors(request, importeRecaudadoReport);
					return forwardErrorRecoverable(mapping, request, userSession, ImporteRecaudadoReport.NAME, importeRecaudadoReport);
				}
					
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(importeRecaudadoReport, request);
				
				// Llamada al servicio	
				importeRecaudadoReport = GdeServiceLocator.getReporteService().getImporteRecaudadoPlanesReportParamPlan(userSession, importeRecaudadoReport);			
				
				// no visualizo la lista de resultados
				importeRecaudadoReport.setPageNumber(0L);
				
				// Tiene errores recuperables
				if (importeRecaudadoReport.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + importeRecaudadoReport.infoString()); 
					saveDemodaErrors(request, importeRecaudadoReport);
					return forwardErrorRecoverable(mapping, request, userSession, ImporteRecaudadoReport.NAME, importeRecaudadoReport);
				}
				
				// Tiene errores no recuperables
				if (importeRecaudadoReport.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + importeRecaudadoReport.errorString());
					return forwardErrorNonRecoverable(mapping, request, funcName, ImporteRecaudadoReport.NAME, importeRecaudadoReport);
				}
			
				// Envio el VO al request
				request.setAttribute(ImporteRecaudadoReport.NAME, importeRecaudadoReport);
				// Subo en el el searchPage al userSession
				userSession.put(ImporteRecaudadoReport.NAME, importeRecaudadoReport);
				
				return mapping.findForward(GdeConstants.FWD_IMPORTE_RECAUDADO_REPORT);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ImporteRecaudadoReport.NAME);
			}
	}
	
	public ActionForward paramViaDeuda(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			
			try {
				
				// Bajo el searchPage del userSession
				ImporteRecaudadoReport importeRecaudadoReport = (ImporteRecaudadoReport) userSession.get(ImporteRecaudadoReport.NAME);
				
				// Si es nulo no se puede continuar
				if (importeRecaudadoReport == null) {
					log.error("error en: "  + funcName + ": " + ImporteRecaudadoReport.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ImporteRecaudadoReport.NAME); 
				}
								
	            // Tiene errores recuperables
				if (importeRecaudadoReport.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + importeRecaudadoReport.infoString()); 
					saveDemodaErrors(request, importeRecaudadoReport);
					return forwardErrorRecoverable(mapping, request, userSession, ImporteRecaudadoReport.NAME, importeRecaudadoReport);
				}
					
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(importeRecaudadoReport, request);
				
				// Llamada al servicio	
				importeRecaudadoReport = GdeServiceLocator.getReporteService().getImporteRecaudadoPlanesReportParamViaDeuda(userSession, importeRecaudadoReport);			
				
				// no visualizo la lista de resultados
				importeRecaudadoReport.setPageNumber(0L);
				
				// Tiene errores recuperables
				if (importeRecaudadoReport.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + importeRecaudadoReport.infoString()); 
					saveDemodaErrors(request, importeRecaudadoReport);
					return forwardErrorRecoverable(mapping, request, userSession, ImporteRecaudadoReport.NAME, importeRecaudadoReport);
				}
				
				// Tiene errores no recuperables
				if (importeRecaudadoReport.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + importeRecaudadoReport.errorString());
					return forwardErrorNonRecoverable(mapping, request, funcName, ImporteRecaudadoReport.NAME, importeRecaudadoReport);
				}
			
				// Envio el VO al request
				request.setAttribute(ImporteRecaudadoReport.NAME, importeRecaudadoReport);
				// Subo en el el searchPage al userSession
				userSession.put(ImporteRecaudadoReport.NAME, importeRecaudadoReport);
				
				return mapping.findForward(GdeConstants.FWD_IMPORTE_RECAUDADO_REPORT);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ImporteRecaudadoReport.NAME);
			}
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
			ImporteRecaudadoReport importeRecaudadoReportVO = (ImporteRecaudadoReport) userSession.get(ImporteRecaudadoReport.NAME);
			
			// Si es nulo no se puede continuar
			if (importeRecaudadoReportVO == null) {
				log.error("error en: "  + funcName + ": " + ImporteRecaudadoReport.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ImporteRecaudadoReport.NAME); 
			}
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(importeRecaudadoReportVO, request);
			// Setea el PageNumber del PageModel				
			importeRecaudadoReportVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			
            // Tiene errores recuperables
			if (importeRecaudadoReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + importeRecaudadoReportVO.infoString()); 
				saveDemodaErrors(request, importeRecaudadoReportVO);
				return forwardErrorRecoverable(mapping, request, userSession, ImporteRecaudadoReport.NAME, importeRecaudadoReportVO);
			}
				
			// Llamada al servicio	
			importeRecaudadoReportVO = GdeServiceLocator.getReporteService().getImporteRecaudadoPlanesReportResult(userSession, importeRecaudadoReportVO);			

			// Tiene errores recuperables
			if (importeRecaudadoReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + importeRecaudadoReportVO.infoString()); 
				saveDemodaErrors(request, importeRecaudadoReportVO);
				return forwardErrorRecoverable(mapping, request, userSession, ImporteRecaudadoReport.NAME, importeRecaudadoReportVO);
			}
			
			// Tiene errores no recuperables
			if (importeRecaudadoReportVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + importeRecaudadoReportVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, ImporteRecaudadoReport.NAME, importeRecaudadoReportVO);
			}
		
			// Seteo para volver a la pantalla de busqueda
			//importeRecaudadoReportVO.setPrevAction("/gde/ReporteImporteRecaudadoPlanes");
			//importeRecaudadoReportVO.setPrevActionParameter(BaseConstants.ACT_INICIALIZAR);

			// Envio el VO al request
			request.setAttribute(ImporteRecaudadoReport.NAME, importeRecaudadoReportVO);
			// Subo en el el searchPage al userSession
			userSession.put(ImporteRecaudadoReport.NAME, importeRecaudadoReportVO);
			
			return mapping.findForward(GdeConstants.FWD_IMPORTE_RECAUDADO_REPORT);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ImporteRecaudadoReport.NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ImporteRecaudadoReport.NAME);
		
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

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, ImporteRecaudadoReport.NAME);
	}

}
