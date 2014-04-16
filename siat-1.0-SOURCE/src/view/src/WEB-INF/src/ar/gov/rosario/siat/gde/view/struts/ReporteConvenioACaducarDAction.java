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
import ar.gov.rosario.siat.gde.iface.model.ConvenioACaducarReport;
import ar.gov.rosario.siat.gde.iface.model.ConvenioReport;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class ReporteConvenioACaducarDAction extends BaseDispatchAction {

		private Log log = LogFactory.getLog(ReporteConvenioACaducarDAction.class);
			
		public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");

			UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.CONSULTAR_CONVENIO_A_CADUCAR, act); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				
				ConvenioACaducarReport convenioACaducarReportVO = GdeServiceLocator.getReporteService().getConvenioACaducarReportInit(userSession);
				
				// Tiene errores recuperables
				if (convenioACaducarReportVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + convenioACaducarReportVO.infoString()); 
					saveDemodaErrors(request, convenioACaducarReportVO);
					return forwardErrorRecoverable(mapping, request, userSession, ConvenioACaducarReport.NAME, convenioACaducarReportVO);
				} 

				// Tiene errores no recuperables
				if (convenioACaducarReportVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + convenioACaducarReportVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, ConvenioACaducarReport.NAME, convenioACaducarReportVO);
				}
				
				// Si no tiene error
				baseInicializarSearchPage(mapping, request, userSession , ConvenioACaducarReport.NAME, convenioACaducarReportVO);
				
				return mapping.findForward(GdeConstants.FWD_CONVENIO_A_CADUCAR_REPORT);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ConvenioACaducarReport.NAME);
			}
		}

		public ActionForward limpiar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return this.baseRefill(mapping, form, request, response, funcName, ConvenioACaducarReport.NAME);
			
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
					ConvenioACaducarReport convenioACaducarReportVO = (ConvenioACaducarReport) userSession.get(ConvenioACaducarReport.NAME);
					
					// Si es nulo no se puede continuar
					if (convenioACaducarReportVO == null) {
						log.error("error en: "  + funcName + ": " + ConvenioACaducarReport.NAME + " IS NULL. No se pudo obtener de la sesion");
						return forwardErrorSessionNullObject(mapping, request, funcName, ConvenioACaducarReport.NAME); 
					}
					
					// Recuperamos datos del form en el vo
					DemodaUtil.populateVO(convenioACaducarReportVO, request);
					// Setea el PageNumber del PageModel				
					convenioACaducarReportVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
					
		            // Tiene errores recuperables
					if (convenioACaducarReportVO.hasErrorRecoverable()) {
						log.error("recoverable error en: "  + funcName + ": " + convenioACaducarReportVO.infoString()); 
						saveDemodaErrors(request, convenioACaducarReportVO);
						return forwardErrorRecoverable(mapping, request, userSession, ConvenioACaducarReport.NAME, convenioACaducarReportVO);
					}
						
					// Llamada al servicio	
					convenioACaducarReportVO = GdeServiceLocator.getReporteService().getConvenioACaducarReportResult(userSession, convenioACaducarReportVO);			

					// Tiene errores recuperables
					if (convenioACaducarReportVO.hasErrorRecoverable()) {
						log.error("recoverable error en: "  + funcName + ": " + convenioACaducarReportVO.infoString()); 
						saveDemodaErrors(request, convenioACaducarReportVO);
						return forwardErrorRecoverable(mapping, request, userSession, ConvenioACaducarReport.NAME, convenioACaducarReportVO);
					}
					
					// Tiene errores no recuperables
					if (convenioACaducarReportVO.hasErrorNonRecoverable()) {
						log.error("error en: "  + funcName + ": " + convenioACaducarReportVO.errorString());
						return forwardErrorNonRecoverable(mapping, request, funcName, ConvenioACaducarReport.NAME, convenioACaducarReportVO);
					}
				
					// Seteo para volver a la pantalla de busqueda
					//convenioACaducarReportVO.setPrevAction("/gde/ReporteConvenioACaducar");
					//convenioACaducarReportVO.setPrevActionParameter(BaseConstants.ACT_INICIALIZAR);
					
					// Envio el VO al request
					request.setAttribute(ConvenioACaducarReport.NAME, convenioACaducarReportVO);
					// Subo en el el searchPage al userSession
					userSession.put(ConvenioACaducarReport.NAME, convenioACaducarReportVO);
					
					return mapping.findForward(GdeConstants.FWD_CONVENIO_A_CADUCAR_REPORT);
					
				} catch (Exception exception) {
					return baseException(mapping, request, funcName, exception, ConvenioACaducarReport.NAME);
				}
			}


			public ActionForward volver(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {
				
				return baseVolver(mapping, form, request, response, ConvenioACaducarReport.NAME);
				
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
					return baseRefill(mapping, form, request, response, funcName, ConvenioACaducarReport.NAME);
			}

			public ActionForward paramRecurso(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
					HttpServletResponse response) throws Exception {

				String funcName = DemodaUtil.currentMethodName();
				if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

				UserSession userSession = getCurrentUserSession(request, mapping); 
				if (userSession == null) return forwardErrorSession(request);
					
				try {
					//bajo el report del usserSession
					ConvenioACaducarReport convenioACaducarReportVO =  (ConvenioACaducarReport) userSession.get(ConvenioACaducarReport.NAME);
						
					// Si es nulo no se puede continuar
					if (convenioACaducarReportVO == null) {
						log.error("error en: "  + funcName + ": " + ConvenioACaducarReport.NAME + " " +
								  "IS NULL. No se pudo obtener de la sesion");
						return forwardErrorSessionNullObject(mapping, request, funcName, ConvenioACaducarReport.NAME); 
					}

					// Recuperamos datos del form en el vo
					DemodaUtil.populateVO(convenioACaducarReportVO, request); request.getParameter("idTipoReporte");

					// no visualizo la lista de resultados
					convenioACaducarReportVO.setPageNumber(0L);
					
					// Tiene errores recuperables
					if (convenioACaducarReportVO.hasErrorRecoverable()) {
						log.error("recoverable error en: "  + funcName + ": " + convenioACaducarReportVO.infoString()); 
						saveDemodaErrors(request, convenioACaducarReportVO);
						return forwardErrorRecoverable(mapping, request, userSession, ConvenioACaducarReport.NAME, convenioACaducarReportVO);
					}

					convenioACaducarReportVO = GdeServiceLocator.getReporteService().getConvenioACaducarReportParamRecurso(userSession, convenioACaducarReportVO);
						
					// Tiene errores recuperables
					if (convenioACaducarReportVO.hasErrorRecoverable()) {
						log.error("recoverable error en: "  + funcName + ": " + convenioACaducarReportVO.infoString()); 
						saveDemodaErrors(request, convenioACaducarReportVO);
						return forwardErrorRecoverable(mapping, request, userSession, 
													   ConvenioACaducarReport.NAME, convenioACaducarReportVO);
					}
						
					// Tiene errores no recuperables
					if (convenioACaducarReportVO.hasErrorNonRecoverable()) {
						log.error("error en: "  + funcName + ": " + convenioACaducarReportVO.errorString()); 
						return forwardErrorNonRecoverable(mapping, request, funcName, 
														  ConvenioACaducarReport.NAME, convenioACaducarReportVO);
					}
						
					// grabo los mensajes si hubiere
					saveDemodaMessages(request, convenioACaducarReportVO);
						
					// Envio el VO al request
					request.setAttribute(ConvenioACaducarReport.NAME, convenioACaducarReportVO);

					return mapping.findForward(GdeConstants.FWD_CONVENIO_A_CADUCAR_REPORT);

				} catch (Exception exception) {
					return baseException(mapping, request, funcName, exception, ConvenioACaducarReport.NAME);
				}
			}

			public ActionForward paramPlan(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
					HttpServletResponse response) throws Exception {

				String funcName = DemodaUtil.currentMethodName();
				if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

				UserSession userSession = getCurrentUserSession(request, mapping); 
				if (userSession == null) return forwardErrorSession(request);
					
				try {
					//bajo el report del usserSession
					ConvenioACaducarReport convenioACaducarReportVO =  (ConvenioACaducarReport) userSession.get(ConvenioACaducarReport.NAME);
						
					// Si es nulo no se puede continuar
					if (convenioACaducarReportVO == null) {
						log.error("error en: "  + funcName + ": " + ConvenioACaducarReport.NAME + " " +
								  "IS NULL. No se pudo obtener de la sesion");
						return forwardErrorSessionNullObject(mapping, request, funcName, ConvenioACaducarReport.NAME); 
					}
					
					// Recuperamos datos del form en el vo
					DemodaUtil.populateVO(convenioACaducarReportVO, request);
					
					// no visualizo la lista de resultados
					convenioACaducarReportVO.setPageNumber(0L);
						
					// Tiene errores recuperables
					if (convenioACaducarReportVO.hasErrorRecoverable()) {
						log.error("recoverable error en: "  + funcName + ": " + convenioACaducarReportVO.infoString()); 
						saveDemodaErrors(request, convenioACaducarReportVO);
						return forwardErrorRecoverable(mapping, request, userSession, ConvenioACaducarReport.NAME, convenioACaducarReportVO);
					}

					// llamar al servicio
					convenioACaducarReportVO = GdeServiceLocator.getReporteService().getConvenioACaducarReportParamPlan(userSession, convenioACaducarReportVO);
					
					// Tiene errores recuperables
					if (convenioACaducarReportVO.hasErrorRecoverable()) {
						log.error("recoverable error en: "  + funcName + ": " + convenioACaducarReportVO.infoString()); 
						saveDemodaErrors(request, convenioACaducarReportVO);
						return forwardErrorRecoverable(mapping, request, userSession, ConvenioACaducarReport.NAME, convenioACaducarReportVO);
					} 

					// Tiene errores no recuperables
					if (convenioACaducarReportVO.hasErrorNonRecoverable()) {
						log.error("error en: "  + funcName + ": " + convenioACaducarReportVO.errorString()); 
						return forwardErrorNonRecoverable(mapping, request, funcName, ConvenioACaducarReport.NAME, convenioACaducarReportVO);
					}

					// grabo los mensajes si hubiere
					saveDemodaMessages(request, convenioACaducarReportVO);
						
					// Envio el VO al request
					request.setAttribute(ConvenioACaducarReport.NAME, convenioACaducarReportVO);

					return mapping.findForward(GdeConstants.FWD_CONVENIO_A_CADUCAR_REPORT);

				} catch (Exception exception) {
					return baseException(mapping, request, funcName, exception, ConvenioACaducarReport.NAME);
				}
			}

}
