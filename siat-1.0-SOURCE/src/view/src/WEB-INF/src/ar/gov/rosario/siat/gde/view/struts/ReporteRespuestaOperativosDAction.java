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
import ar.gov.rosario.siat.gde.iface.model.RespuestaOperativosReport;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class ReporteRespuestaOperativosDAction extends BaseDispatchAction {


	private Log log = LogFactory.getLog(ReporteRespuestaOperativosDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.CONSULTAR_RESPUESTA_OPERATIVOS, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			RespuestaOperativosReport respuestaOperativosReportVO = GdeServiceLocator.getReporteService().getRespuestaOperativosReportInit(userSession);
			
			// Tiene errores recuperables
			if (respuestaOperativosReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + respuestaOperativosReportVO.infoString()); 
				saveDemodaErrors(request, respuestaOperativosReportVO);
				return forwardErrorRecoverable(mapping, request, userSession, RespuestaOperativosReport.NAME, respuestaOperativosReportVO);
			} 

			// Tiene errores no recuperables
			if (respuestaOperativosReportVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + respuestaOperativosReportVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, RespuestaOperativosReport.NAME, respuestaOperativosReportVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , RespuestaOperativosReport.NAME, respuestaOperativosReportVO);
			
			return mapping.findForward(GdeConstants.FWD_RESPUESTA_OPERATIVOS_REPORT);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, RespuestaOperativosReport.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, RespuestaOperativosReport.NAME);
		
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
				RespuestaOperativosReport respuestaOperativosReportVO = (RespuestaOperativosReport) userSession.get(RespuestaOperativosReport.NAME);
				
				// Si es nulo no se puede continuar
				if (respuestaOperativosReportVO == null) {
					log.error("error en: "  + funcName + ": " + RespuestaOperativosReport.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RespuestaOperativosReport.NAME); 
				}
				
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(respuestaOperativosReportVO, request);
				// Setea el PageNumber del PageModel				
				respuestaOperativosReportVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
				
	            // Tiene errores recuperables
				if (respuestaOperativosReportVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + respuestaOperativosReportVO.infoString()); 
					saveDemodaErrors(request, respuestaOperativosReportVO);
					return forwardErrorRecoverable(mapping, request, userSession, RespuestaOperativosReport.NAME, respuestaOperativosReportVO);
				}
					
				// Llamada al servicio	
				respuestaOperativosReportVO = GdeServiceLocator.getReporteService().getRespuestaOperativosReportResult(userSession, respuestaOperativosReportVO);			

				// Tiene errores recuperables
				if (respuestaOperativosReportVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + respuestaOperativosReportVO.infoString()); 
					saveDemodaErrors(request, respuestaOperativosReportVO);
					return forwardErrorRecoverable(mapping, request, userSession, RespuestaOperativosReport.NAME, respuestaOperativosReportVO);
				}
				
				// Tiene errores no recuperables
				if (respuestaOperativosReportVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + respuestaOperativosReportVO.errorString());
					return forwardErrorNonRecoverable(mapping, request, funcName, RespuestaOperativosReport.NAME, respuestaOperativosReportVO);
				}
			
				// Seteo para volver a la pantalla de busqueda
				//respuestaOperativosReportVO.setPrevAction("/gde/ReporteRespuestaOperativos");
				//respuestaOperativosReportVO.setPrevActionParameter(BaseConstants.ACT_INICIALIZAR);
				
				// Envio el VO al request
				request.setAttribute(RespuestaOperativosReport.NAME, respuestaOperativosReportVO);
				// Subo en el el searchPage al userSession
				userSession.put(RespuestaOperativosReport.NAME, respuestaOperativosReportVO);
				
				return mapping.findForward(GdeConstants.FWD_RESPUESTA_OPERATIVOS_REPORT);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RespuestaOperativosReport.NAME);
			}
		}


		public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, RespuestaOperativosReport.NAME);
			
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
		
		public ActionForward paramTipProMas(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
				HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
				
			try {
				//bajo el report del usserSession
				RespuestaOperativosReport respuestaOperativosReportVO =  (RespuestaOperativosReport) userSession.get(RespuestaOperativosReport.NAME);
					
				// Si es nulo no se puede continuar
				if (respuestaOperativosReportVO == null) {
					log.error("error en: "  + funcName + ": " + RespuestaOperativosReport.NAME + " " +
							  "IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RespuestaOperativosReport.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(respuestaOperativosReportVO, request);
				
				// no visualizo la lista de resultados
				respuestaOperativosReportVO.setPageNumber(0L);
				
				// Tiene errores recuperables
				if (respuestaOperativosReportVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + respuestaOperativosReportVO.infoString()); 
					saveDemodaErrors(request, respuestaOperativosReportVO);
					return forwardErrorRecoverable(mapping, request, userSession, RespuestaOperativosReport.NAME, respuestaOperativosReportVO);
				}

				respuestaOperativosReportVO = GdeServiceLocator.getReporteService().getRespuestaOperativosReportParamTipProMas(userSession, respuestaOperativosReportVO);
					
				// Tiene errores recuperables
				if (respuestaOperativosReportVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + respuestaOperativosReportVO.infoString()); 
					saveDemodaErrors(request, respuestaOperativosReportVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
												   RespuestaOperativosReport.NAME, respuestaOperativosReportVO);
				}
					
				// Tiene errores no recuperables
				if (respuestaOperativosReportVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + respuestaOperativosReportVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
													  RespuestaOperativosReport.NAME, respuestaOperativosReportVO);
				}
					
				// grabo los mensajes si hubiere
				saveDemodaMessages(request, respuestaOperativosReportVO);
					
				// Envio el VO al request
				request.setAttribute(RespuestaOperativosReport.NAME, respuestaOperativosReportVO);

				return mapping.findForward(GdeConstants.FWD_RESPUESTA_OPERATIVOS_REPORT);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RespuestaOperativosReport.NAME);
			}
		}

		public ActionForward refill(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

				String funcName = DemodaUtil.currentMethodName();
				return baseRefill(mapping, form, request, response, funcName, RespuestaOperativosReport.NAME);
		}

}
