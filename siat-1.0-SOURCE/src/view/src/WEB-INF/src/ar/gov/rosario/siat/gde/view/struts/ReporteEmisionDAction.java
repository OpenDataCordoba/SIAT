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
import ar.gov.rosario.siat.gde.iface.model.EmisionReport;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class ReporteEmisionDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(ReporteEmisionDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.EMISION_REPORT, act);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			EmisionReport emisionReportVO = GdeServiceLocator.getReporteService().getEmisionReportInit(userSession);
			
			// Tiene errores recuperables
			if (emisionReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionReportVO.infoString()); 
				saveDemodaErrors(request, emisionReportVO);
				return forwardErrorRecoverable(mapping, request, userSession, EmisionReport.NAME, emisionReportVO);
			} 

			// Tiene errores no recuperables
			if (emisionReportVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + emisionReportVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EmisionReport.NAME, emisionReportVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , EmisionReport.NAME, emisionReportVO);
			
			return mapping.findForward(GdeConstants.FWD_EMISION_REPORT);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionReport.NAME);
		}
	}
	
	public ActionForward paramRecurso(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
			
		try {
			//bajo el report del usserSession
			EmisionReport emisionReportVO =  (EmisionReport) userSession.get(EmisionReport.NAME);
				
			// Si es nulo no se puede continuar
			if (emisionReportVO == null) {
				log.error("error en: "  + funcName + ": " + EmisionReport.NAME + " " +
						  "IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EmisionReport.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(emisionReportVO, request);
			
			// no visualizo la lista de resultados
			emisionReportVO.setPageNumber(0L);
			
			// Tiene errores recuperables
			if (emisionReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionReportVO.infoString()); 
				saveDemodaErrors(request, emisionReportVO);
				return forwardErrorRecoverable(mapping, request, userSession, EmisionReport.NAME, emisionReportVO);
			}

			emisionReportVO = GdeServiceLocator.getReporteService().getEmisionReportReportParamRecurso(userSession, emisionReportVO);
				
			// Tiene errores recuperables
			if (emisionReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionReportVO.infoString()); 
				saveDemodaErrors(request, emisionReportVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
											   EmisionReport.NAME, emisionReportVO);
			}
				
			// Tiene errores no recuperables
			if (emisionReportVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + emisionReportVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
						EmisionReport.NAME, emisionReportVO);
			}
				
			// grabo los mensajes si hubiere
			saveDemodaMessages(request, emisionReportVO);
				
			// Envio el VO al request
			request.setAttribute(EmisionReport.NAME, emisionReportVO);

			return mapping.findForward(GdeConstants.FWD_EMISION_REPORT);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionReport.NAME);
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
			EmisionReport emisionReportVO = (EmisionReport) userSession.get(EmisionReport.NAME);
			
			// Si es nulo no se puede continuar
			if (emisionReportVO == null) {
				log.error("error en: "  + funcName + ": " + EmisionReport.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EmisionReport.NAME); 
			}
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(emisionReportVO, request);
			// Setea el PageNumber del PageModel				
			emisionReportVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			
            // Tiene errores recuperables
			if (emisionReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionReportVO.infoString()); 
				saveDemodaErrors(request, emisionReportVO);
				return forwardErrorRecoverable(mapping, request, userSession, EmisionReport.NAME, emisionReportVO);
			}
			
			if (emisionReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionReportVO.infoString()); 
				saveDemodaErrors(request, emisionReportVO);
				
				return forwardErrorRecoverable(mapping, request, userSession, EmisionReport.NAME, emisionReportVO);
			}

			// Llamada al servicio	
			emisionReportVO = GdeServiceLocator.getReporteService().getEmisionReportResult(userSession, emisionReportVO);			

			// Tiene errores recuperables
			if (emisionReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionReportVO.infoString()); 
				saveDemodaErrors(request, emisionReportVO);
				
				return forwardErrorRecoverable(mapping, request, userSession, EmisionReport.NAME, emisionReportVO);
			}
			
			// Tiene errores no recuperables
			if (emisionReportVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + emisionReportVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, EmisionReport.NAME, emisionReportVO);
			}
		
			// Envio el VO al request
			request.setAttribute(EmisionReport.NAME, emisionReportVO);
			// Subo el searchPage al userSession
			userSession.put(EmisionReport.NAME, emisionReportVO);
			
			return mapping.findForward(GdeConstants.FWD_EMISION_REPORT);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionReport.NAME);
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
			return this.baseRefill(mapping, form, request, response, funcName, EmisionReport.NAME);
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return baseVolver(mapping, form, request, response, EmisionReport.NAME);
	}
		

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, EmisionReport.NAME);
	}

}
