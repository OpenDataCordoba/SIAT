//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.bal.iface.model.ClasificadorReport;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class ReporteClasificadorDAction extends BaseDispatchAction {


	private Log log = LogFactory.getLog(ReporteClasificadorDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.CONSULTAR_CLASIFICADOR, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			ClasificadorReport clasificadorReportVO = BalServiceLocator.getClasificacionService().getClasificadorReportInit(userSession);
			
			// Tiene errores recuperables
			if (clasificadorReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + clasificadorReportVO.infoString()); 
				saveDemodaErrors(request, clasificadorReportVO);
				return forwardErrorRecoverable(mapping, request, userSession, ClasificadorReport.NAME, clasificadorReportVO);
			} 

			// Tiene errores no recuperables
			if (clasificadorReportVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + clasificadorReportVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ClasificadorReport.NAME, clasificadorReportVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , ClasificadorReport.NAME, clasificadorReportVO);
			
			return mapping.findForward(BalConstants.FWD_CLASIFICADOR_REPORT);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ClasificadorReport.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, ClasificadorReport.NAME);
		
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
				ClasificadorReport clasificadorReportVO = (ClasificadorReport) userSession.get(ClasificadorReport.NAME);
				
				// Si es nulo no se puede continuar
				if (clasificadorReportVO == null) {
					log.error("error en: "  + funcName + ": " + ClasificadorReport.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ClasificadorReport.NAME); 
				}
				
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(clasificadorReportVO, request);
				if("on".equals(request.getParameter("reporteExtra"))){
					clasificadorReportVO.setReporteExtra(true);
				}
				// Setea el PageNumber del PageModel				
				clasificadorReportVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
				
	            // Tiene errores recuperables
				if (clasificadorReportVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + clasificadorReportVO.infoString()); 
					saveDemodaErrors(request, clasificadorReportVO);
					return forwardErrorRecoverable(mapping, request, userSession, ClasificadorReport.NAME, clasificadorReportVO);
				}
					
				// Llamada al servicio	
				clasificadorReportVO = BalServiceLocator.getClasificacionService().getClasificadorReportResult(userSession, clasificadorReportVO);			

				// Tiene errores recuperables
				if (clasificadorReportVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + clasificadorReportVO.infoString()); 
					saveDemodaErrors(request, clasificadorReportVO);
					return forwardErrorRecoverable(mapping, request, userSession, ClasificadorReport.NAME, clasificadorReportVO);
				}
				
				// Tiene errores no recuperables
				if (clasificadorReportVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + clasificadorReportVO.errorString());
					return forwardErrorNonRecoverable(mapping, request, funcName, ClasificadorReport.NAME, clasificadorReportVO);
				}
							
				// Envio el VO al request
				request.setAttribute(ClasificadorReport.NAME, clasificadorReportVO);
				// Subo en el el searchPage al userSession
				userSession.put(ClasificadorReport.NAME, clasificadorReportVO);
				
				return mapping.findForward(BalConstants.FWD_CLASIFICADOR_REPORT);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ClasificadorReport.NAME);
			}
		}

		public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, ClasificadorReport.NAME);
			
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
					return baseException(mapping, request, funcName, exception, ClasificadorReport.NAME);
				}
		}
		
		public ActionForward paramEjercicio(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
				HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
				
			try {
				//bajo el report del usserSession
				ClasificadorReport clasificadorReportVO =  (ClasificadorReport) userSession.get(ClasificadorReport.NAME);
					
				// Si es nulo no se puede continuar
				if (clasificadorReportVO == null) {
					log.error("error en: "  + funcName + ": " + ClasificadorReport.NAME + " " +
							  "IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ClasificadorReport.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(clasificadorReportVO, request);
				if("on".equals(request.getParameter("reporteExtra"))){
					clasificadorReportVO.setReporteExtra(true);
				}
			
				// no visualizo la lista de resultados
				clasificadorReportVO.setPageNumber(0L);
				
				// Tiene errores recuperables
				if (clasificadorReportVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + clasificadorReportVO.infoString()); 
					saveDemodaErrors(request, clasificadorReportVO);
					return forwardErrorRecoverable(mapping, request, userSession, ClasificadorReport.NAME, clasificadorReportVO);
				}

				clasificadorReportVO = BalServiceLocator.getClasificacionService().getClasificadorReportParamEjercicio(userSession, clasificadorReportVO);
					
				// Tiene errores recuperables
				if (clasificadorReportVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + clasificadorReportVO.infoString()); 
					saveDemodaErrors(request, clasificadorReportVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
												   ClasificadorReport.NAME, clasificadorReportVO);
				}
					
				// Tiene errores no recuperables
				if (clasificadorReportVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + clasificadorReportVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
													  ClasificadorReport.NAME, clasificadorReportVO);
				}
					
				// grabo los mensajes si hubiere
				saveDemodaMessages(request, clasificadorReportVO);
					
				// Envio el VO al request
				request.setAttribute(ClasificadorReport.NAME, clasificadorReportVO);

				return mapping.findForward(BalConstants.FWD_CLASIFICADOR_REPORT);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ClasificadorReport.NAME);
			}
		}

		public ActionForward refill(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

				String funcName = DemodaUtil.currentMethodName();
				return baseRefill(mapping, form, request, response, funcName, ClasificadorReport.NAME);
		}
		
		public ActionForward paramClasificador(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
				HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
				
			try {
				//bajo el report del usserSession
				ClasificadorReport clasificadorReportVO =  (ClasificadorReport) userSession.get(ClasificadorReport.NAME);
					
				// Si es nulo no se puede continuar
				if (clasificadorReportVO == null) {
					log.error("error en: "  + funcName + ": " + ClasificadorReport.NAME + " " +
							  "IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ClasificadorReport.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(clasificadorReportVO, request);
				if("on".equals(request.getParameter("reporteExtra"))){
					clasificadorReportVO.setReporteExtra(true);
				}
			
				// no visualizo la lista de resultados
				clasificadorReportVO.setPageNumber(0L);

				// Tiene errores recuperables
				if (clasificadorReportVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + clasificadorReportVO.infoString()); 
					saveDemodaErrors(request, clasificadorReportVO);
					return forwardErrorRecoverable(mapping, request, userSession, ClasificadorReport.NAME, clasificadorReportVO);
				}

				clasificadorReportVO = BalServiceLocator.getClasificacionService().getClasificadorReportParamClasificador(userSession, clasificadorReportVO);
					
				// Tiene errores recuperables
				if (clasificadorReportVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + clasificadorReportVO.infoString()); 
					saveDemodaErrors(request, clasificadorReportVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
												   ClasificadorReport.NAME, clasificadorReportVO);
				}
					
				// Tiene errores no recuperables
				if (clasificadorReportVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + clasificadorReportVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
													  ClasificadorReport.NAME, clasificadorReportVO);
				}
					
				// grabo los mensajes si hubiere
				saveDemodaMessages(request, clasificadorReportVO);
					
				// Envio el VO al request
				request.setAttribute(ClasificadorReport.NAME, clasificadorReportVO);

				return mapping.findForward(BalConstants.FWD_CLASIFICADOR_REPORT);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ClasificadorReport.NAME);
			}
		}

		public ActionForward paramNivel(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
				HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
				
			try {
				//bajo el report del usserSession
				ClasificadorReport clasificadorReportVO =  (ClasificadorReport) userSession.get(ClasificadorReport.NAME);
					
				// Si es nulo no se puede continuar
				if (clasificadorReportVO == null) {
					log.error("error en: "  + funcName + ": " + ClasificadorReport.NAME + " " +
							  "IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ClasificadorReport.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(clasificadorReportVO, request);
				if("on".equals(request.getParameter("reporteExtra"))){
					clasificadorReportVO.setReporteExtra(true);
				}
			
				// no visualizo la lista de resultados
				clasificadorReportVO.setPageNumber(0L);

				// Tiene errores recuperables
				if (clasificadorReportVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + clasificadorReportVO.infoString()); 
					saveDemodaErrors(request, clasificadorReportVO);
					return forwardErrorRecoverable(mapping, request, userSession, ClasificadorReport.NAME, clasificadorReportVO);
				}

				clasificadorReportVO = BalServiceLocator.getClasificacionService().getClasificadorReportParamNivel(userSession, clasificadorReportVO);
					
				// Tiene errores recuperables
				if (clasificadorReportVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + clasificadorReportVO.infoString()); 
					saveDemodaErrors(request, clasificadorReportVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
												   ClasificadorReport.NAME, clasificadorReportVO);
				}
					
				// Tiene errores no recuperables
				if (clasificadorReportVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + clasificadorReportVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
													  ClasificadorReport.NAME, clasificadorReportVO);
				}
					
				// grabo los mensajes si hubiere
				saveDemodaMessages(request, clasificadorReportVO);
					
				// Envio el VO al request
				request.setAttribute(ClasificadorReport.NAME, clasificadorReportVO);

				return mapping.findForward(BalConstants.FWD_CLASIFICADOR_REPORT);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ClasificadorReport.NAME);
			}
		}
}
