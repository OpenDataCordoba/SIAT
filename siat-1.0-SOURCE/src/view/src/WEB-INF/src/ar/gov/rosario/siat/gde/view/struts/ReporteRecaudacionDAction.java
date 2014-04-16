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

import ar.gov.rosario.siat.bal.iface.model.ProcesoAsentamientoAdapter;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.gde.iface.model.RecaudacionReport;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class ReporteRecaudacionDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(ReporteRecaudacionDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_RECAUDACION_REPORT, act);
		//TODO SACAR 
		//userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_CONVENIO_REPORT, act);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			RecaudacionReport recaudacionReportVO = GdeServiceLocator.getReporteService().getRecaudacionReportInit(userSession);
			
			// Tiene errores recuperables
			if (recaudacionReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + recaudacionReportVO.infoString()); 
				saveDemodaErrors(request, recaudacionReportVO);
				return forwardErrorRecoverable(mapping, request, userSession, RecaudacionReport.NAME, recaudacionReportVO);
			} 

			// Tiene errores no recuperables
			if (recaudacionReportVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + recaudacionReportVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, RecaudacionReport.NAME, recaudacionReportVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , RecaudacionReport.NAME, recaudacionReportVO);
			
			return mapping.findForward(GdeConstants.FWD_RECAUDACION_REPORT);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, RecaudacionReport.NAME);
		}
	}
	
	public ActionForward paramCategoria(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
			
		try {
			//bajo el report del usserSession
			RecaudacionReport recaudacionReportVO =  (RecaudacionReport) userSession.get(RecaudacionReport.NAME);
				
			// Si es nulo no se puede continuar
			if (recaudacionReportVO == null) {
				log.error("error en: "  + funcName + ": " + RecaudacionReport.NAME + " " +
						  "IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, RecaudacionReport.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(recaudacionReportVO, request);
			
			// no visualizo la lista de resultados
			recaudacionReportVO.setPageNumber(0L);
			
			// Tiene errores recuperables
			if (recaudacionReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + recaudacionReportVO.infoString()); 
				saveDemodaErrors(request, recaudacionReportVO);
				return forwardErrorRecoverable(mapping, request, userSession, RecaudacionReport.NAME, recaudacionReportVO);
			}

			recaudacionReportVO = GdeServiceLocator.getReporteService().getRecaudacionReportParamCategoria(userSession, recaudacionReportVO);
				
			// Tiene errores recuperables
			if (recaudacionReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + recaudacionReportVO.infoString()); 
				saveDemodaErrors(request, recaudacionReportVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
											   RecaudacionReport.NAME, recaudacionReportVO);
			}
				
			// Tiene errores no recuperables
			if (recaudacionReportVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + recaudacionReportVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
												  RecaudacionReport.NAME, recaudacionReportVO);
			}
				
			// grabo los mensajes si hubiere
			saveDemodaMessages(request, recaudacionReportVO);
				
			// Envio el VO al request
			request.setAttribute(RecaudacionReport.NAME, recaudacionReportVO);

			return mapping.findForward(GdeConstants.FWD_RECAUDACION_REPORT);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, RecaudacionReport.NAME);
		}
	}



	public ActionForward buscar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// Bajo el searchPage del userSession
			RecaudacionReport recaudacionReportVO = (RecaudacionReport) userSession.get(RecaudacionReport.NAME);
			
			// Si es nulo no se puede continuar
			if (recaudacionReportVO == null) {
				log.error("error en: "  + funcName + ": " + RecaudacionReport.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, RecaudacionReport.NAME); 
			}
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(recaudacionReportVO, request);
			// Setea el PageNumber del PageModel				
			recaudacionReportVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			
            // Tiene errores recuperables
			if (recaudacionReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + recaudacionReportVO.infoString()); 
				saveDemodaErrors(request, recaudacionReportVO);
				return forwardErrorRecoverable(mapping, request, userSession, RecaudacionReport.NAME, recaudacionReportVO);
			}
				
			// Llamada al servicio	
			recaudacionReportVO = GdeServiceLocator.getReporteService().getRecaudacionReportResult(userSession, recaudacionReportVO);			

			// Tiene errores recuperables
			if (recaudacionReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + recaudacionReportVO.infoString()); 
				saveDemodaErrors(request, recaudacionReportVO);
				
				return forwardErrorRecoverable(mapping, request, userSession, RecaudacionReport.NAME, recaudacionReportVO);
			}
			
			// Tiene errores no recuperables
			if (recaudacionReportVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + recaudacionReportVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, RecaudacionReport.NAME, recaudacionReportVO);
			}
		
			// Envio el VO al request
			request.setAttribute(RecaudacionReport.NAME, recaudacionReportVO);
			// Nuleo el list result
			// Subo en el el searchPage al userSession
			userSession.put(RecaudacionReport.NAME, recaudacionReportVO);
			
			return mapping.findForward(GdeConstants.FWD_RECAUDACION_REPORT);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, RecaudacionReport.NAME);
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
				return baseException(mapping, request, funcName, exception, ProcesoAsentamientoAdapter.NAME);
			}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return this.baseRefill(mapping, form, request, response, funcName, RecaudacionReport.NAME);
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return baseVolver(mapping, form, request, response, RecaudacionReport.NAME);
	}
		

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, RecaudacionReport.NAME);
	}

}
