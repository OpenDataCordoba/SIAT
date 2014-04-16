//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.view.struts;

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
import ar.gov.rosario.siat.cas.iface.model.SolPendReport;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.cas.iface.util.CasSecurityConstants;
import ar.gov.rosario.siat.cas.view.util.CasConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

/**
 * 
 * @author Andrei
 *
 */

public class ReporteSolPendDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(ReporteSolPendDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception {

		// Seteos comunes para el LOG
		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled())
			log.debug(funcName + ": enter");

		// Si no tiene acceso
		UserSession userSession = canAccess(request, mapping, CasSecurityConstants.SOLPEND_REPORT, act);
		if (userSession == null) return forwardErrorSession(request);

		try {

			// Llamada al INIT del Servicio Reporte
			SolPendReport solPendReportVO = CasServiceLocator.getReporteService().getSolPendReportInit(userSession);

			// Tiene errores recuperables
			if (solPendReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: " + funcName + ": "+ solPendReportVO.infoString());
				saveDemodaErrors(request, solPendReportVO);
				return forwardErrorRecoverable(mapping, request, userSession,SolPendReport.NAME, solPendReportVO);
			}

			// Tiene errores no recuperables
			if (solPendReportVO.hasErrorNonRecoverable()) {
				log.error("error en: " + funcName + ": "+ solPendReportVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName,SolPendReport.NAME, solPendReportVO);
			}

			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession,SolPendReport.NAME, solPendReportVO);

			return mapping.findForward(CasConstants.FWD_SOLPEND_REPORT);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception,SolPendReport.NAME);
		}
	}

	public ActionForward buscar(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception {

		// Seteos comunes para el LOG
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug(funcName + ": enter");
		
		//Si no existe userSession
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession == null)
			return forwardErrorSession(request);

		try {

			// Bajo el searchPage del userSession
			SolPendReport solPendReportVO = (SolPendReport) userSession.get(SolPendReport.NAME);

			// Si es nulo no se puede continuar
			if (solPendReportVO == null) {
				log.error("error en: " + funcName + ": "
						+ SolPendReport.NAME
						+ " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SolPendReport.NAME);
			}

			// Recuperamos datos del FORM para Completar el VO
			DemodaUtil.populateVO(solPendReportVO, request);
			
			// Setea el PageNumber del PageModel
			solPendReportVO.setPageNumber(new Long((String) userSession.get("reqAttPageNumber")));

			// Tiene errores recuperables
			if (solPendReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: " + funcName + ": "
						+ solPendReportVO.infoString());
				saveDemodaErrors(request, solPendReportVO);
				
				return forwardErrorRecoverable(mapping, request, userSession,
						SolPendReport.NAME, solPendReportVO);
			}
			
			// Tiene errores no recuperables
			if (solPendReportVO.hasErrorNonRecoverable()) {
				log.error("error en: " + funcName + ": "
						+ solPendReportVO.errorString());
				
				return forwardErrorNonRecoverable(mapping, request, funcName,
						SolPendReport.NAME, solPendReportVO);
			}

			// Llamada al RESULT del Servicio Reporte
			solPendReportVO = CasServiceLocator.getReporteService().getSolPendReportResult(userSession,solPendReportVO);

			// Tiene errores recuperables
			if (solPendReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: " + funcName + ": "
						+ solPendReportVO.infoString());
				saveDemodaErrors(request, solPendReportVO);

				return forwardErrorRecoverable(mapping, request, userSession,
						SolPendReport.NAME, solPendReportVO);
			}

			// Tiene errores no recuperables
			if (solPendReportVO.hasErrorNonRecoverable()) {
				log.error("error en: " + funcName + ": "
						+ solPendReportVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName,
						SolPendReport.NAME, solPendReportVO);
			}

			// Envio el VO al request
			request.setAttribute(SolPendReport.NAME, solPendReportVO);
			// Subo el searchPage al userSession
			userSession.put(SolPendReport.NAME, solPendReportVO);

			return mapping.findForward(CasConstants.FWD_SOLPEND_REPORT);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception,
					SolPendReport.NAME);
		}
	}

	public ActionForward paramArea (ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception {
			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el SearchPage del userSession
			SolPendReport solPendReportVO = (SolPendReport) userSession.get(SolPendReport.NAME);
			
			// Si es nulo no se puede continuar
			if (solPendReportVO == null) {
				log.error("error en: "  + funcName + ": " + SolPendReport.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SolPendReport.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(solPendReportVO, request);
			
            // Tiene errores recuperables
			if (solPendReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + solPendReportVO.infoString()); 
				saveDemodaErrors(request, solPendReportVO);
				return forwardErrorRecoverable(mapping, request, userSession, SolPendReport.NAME, solPendReportVO);
			}
			
			// llamada al servicio
			solPendReportVO = CasServiceLocator.getReporteService().getSolPendReportParamArea(userSession, solPendReportVO);
			
            // Tiene errores recuperables
			if (solPendReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + solPendReportVO.infoString()); 
				saveDemodaErrors(request, solPendReportVO);
				return forwardErrorRecoverable(mapping, request, userSession, SolPendReport.NAME, solPendReportVO);
			}
			
			// Tiene errores no recuperables
			if (solPendReportVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + solPendReportVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SolPendReport.NAME, solPendReportVO);
			}
			
			// Vuelvo el PAGENUMBER a 0 para evitar que el viewresult sea false
			solPendReportVO.setPageNumber(new Long(0)); 
			// Envio el VO al request
			request.setAttribute(SolPendReport.NAME, solPendReportVO);
			// Subo el searchpage al userSession
			userSession.put(SolPendReport.NAME, solPendReportVO);
			
			return mapping.findForward(CasConstants.FWD_SOLPEND_REPORT);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SolPendReport.NAME);
		}
	}
	/*
	 * Demás métodos comunes
	 */
	
	public ActionForward downloadFile(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception {
		
		// Seteos comunes para el LOG
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug(funcName + ": enter");
		
		//Si no existe userSession
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession == null)
			return forwardErrorSession(request);

		try {

			// Se obtiene del REQUEST el nombre del archivo seleccionado
			String fileName = request.getParameter("fileParam");

			// TODO: ¿Qué hace el método baseResponseFile?
			baseResponseFile(response, fileName);

			log.debug("exit: " + funcName);

			return null;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception,ProcesoAsentamientoAdapter.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName,SolPendReport.NAME);
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception {

		return baseVolver(mapping, form, request, response,SolPendReport.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, SolPendReport.NAME);
	}

}
