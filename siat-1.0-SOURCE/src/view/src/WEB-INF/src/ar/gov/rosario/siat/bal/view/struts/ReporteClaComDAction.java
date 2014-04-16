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

import ar.gov.rosario.siat.bal.iface.model.ClaComReport;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class ReporteClaComDAction extends BaseDispatchAction {


	private Log log = LogFactory.getLog(ReporteClaComDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.CONSULTAR_CLACOM, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			ClaComReport claComReportVO = BalServiceLocator.getClasificacionService().getClaComReportInit(userSession);
			
			// Tiene errores recuperables
			if (claComReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + claComReportVO.infoString()); 
				saveDemodaErrors(request, claComReportVO);
				return forwardErrorRecoverable(mapping, request, userSession, ClaComReport.NAME, claComReportVO);
			} 

			// Tiene errores no recuperables
			if (claComReportVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + claComReportVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ClaComReport.NAME, claComReportVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , ClaComReport.NAME, claComReportVO);
			
			return mapping.findForward(BalConstants.FWD_CLACOM_REPORT);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ClaComReport.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, ClaComReport.NAME);
		
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
				ClaComReport claComReportVO = (ClaComReport) userSession.get(ClaComReport.NAME);
				
				// Si es nulo no se puede continuar
				if (claComReportVO == null) {
					log.error("error en: "  + funcName + ": " + ClaComReport.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ClaComReport.NAME); 
				}
				
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(claComReportVO, request);
				
				// Setea el PageNumber del PageModel				
				claComReportVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
				
	            // Tiene errores recuperables
				if (claComReportVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + claComReportVO.infoString()); 
					saveDemodaErrors(request, claComReportVO);
					return forwardErrorRecoverable(mapping, request, userSession, ClaComReport.NAME, claComReportVO);
				}
					
				// Llamada al servicio	
				claComReportVO = BalServiceLocator.getClasificacionService().getClaComReportResult(userSession, claComReportVO);			

				// Tiene errores recuperables
				if (claComReportVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + claComReportVO.infoString()); 
					saveDemodaErrors(request, claComReportVO);
					return forwardErrorRecoverable(mapping, request, userSession, ClaComReport.NAME, claComReportVO);
				}
				
				// Tiene errores no recuperables
				if (claComReportVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + claComReportVO.errorString());
					return forwardErrorNonRecoverable(mapping, request, funcName, ClaComReport.NAME, claComReportVO);
				}
							
				// Envio el VO al request
				request.setAttribute(ClaComReport.NAME, claComReportVO);
				// Subo en el el searchPage al userSession
				userSession.put(ClaComReport.NAME, claComReportVO);
				
				return mapping.findForward(BalConstants.FWD_CLACOM_REPORT);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ClaComReport.NAME);
			}
		}

		public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, ClaComReport.NAME);
			
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
					return baseException(mapping, request, funcName, exception, ClaComReport.NAME);
				}
		}
		
		public ActionForward refill(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

				String funcName = DemodaUtil.currentMethodName();
				return baseRefill(mapping, form, request, response, funcName, ClaComReport.NAME);
		}
		
		public ActionForward paramClasificador(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
				HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
				
			try {
				//bajo el report del usserSession
				ClaComReport claComReportVO =  (ClaComReport) userSession.get(ClaComReport.NAME);
					
				// Si es nulo no se puede continuar
				if (claComReportVO == null) {
					log.error("error en: "  + funcName + ": " + ClaComReport.NAME + " " +
							  "IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ClaComReport.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(claComReportVO, request);
			
				// no visualizo la lista de resultados
				claComReportVO.setPageNumber(0L);

				// Tiene errores recuperables
				if (claComReportVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + claComReportVO.infoString()); 
					saveDemodaErrors(request, claComReportVO);
					return forwardErrorRecoverable(mapping, request, userSession, ClaComReport.NAME, claComReportVO);
				}

				claComReportVO = BalServiceLocator.getClasificacionService().getClaComReportParamClasificador(userSession, claComReportVO);
					
				// Tiene errores recuperables
				if (claComReportVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + claComReportVO.infoString()); 
					saveDemodaErrors(request, claComReportVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
												   ClaComReport.NAME, claComReportVO);
				}
					
				// Tiene errores no recuperables
				if (claComReportVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + claComReportVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
													  ClaComReport.NAME, claComReportVO);
				}
					
				// grabo los mensajes si hubiere
				saveDemodaMessages(request, claComReportVO);
					
				// Envio el VO al request
				request.setAttribute(ClaComReport.NAME, claComReportVO);

				return mapping.findForward(BalConstants.FWD_CLACOM_REPORT);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ClaComReport.NAME);
			}
		}
}
