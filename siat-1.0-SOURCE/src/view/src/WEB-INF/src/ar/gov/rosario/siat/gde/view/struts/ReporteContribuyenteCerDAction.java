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
import ar.gov.rosario.siat.gde.iface.model.ContribuyenteCerReport;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class ReporteContribuyenteCerDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(ReporteEmisionDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.CONTRIBUYENTECER_REPORT, act);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			ContribuyenteCerReport contribuyenteCerReportVO = GdeServiceLocator.getReporteService().getContribuyenteCerReportInit(userSession);
			
			// Tiene errores recuperables
			if (contribuyenteCerReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + contribuyenteCerReportVO.infoString()); 
				saveDemodaErrors(request, contribuyenteCerReportVO);
				return forwardErrorRecoverable(mapping, request, userSession, ContribuyenteCerReport.NAME, contribuyenteCerReportVO);
			} 

			// Tiene errores no recuperables
			if (contribuyenteCerReportVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + contribuyenteCerReportVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ContribuyenteCerReport.NAME, contribuyenteCerReportVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , ContribuyenteCerReport.NAME, contribuyenteCerReportVO);
			
			return mapping.findForward(GdeConstants.FWD_CONTRIBUYENTECER_REPORT);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ContribuyenteCerReport.NAME);
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
			ContribuyenteCerReport contribuyenteCerReportVO = (ContribuyenteCerReport) userSession.get(ContribuyenteCerReport.NAME);
			
			// Si es nulo no se puede continuar
			if (contribuyenteCerReportVO == null) {
				log.error("error en: "  + funcName + ": " + ContribuyenteCerReport.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ContribuyenteCerReport.NAME); 
			}
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(contribuyenteCerReportVO, request);
			// Setea el PageNumber del PageModel				
			contribuyenteCerReportVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			
            // Tiene errores recuperables
			if (contribuyenteCerReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + contribuyenteCerReportVO.infoString()); 
				saveDemodaErrors(request, contribuyenteCerReportVO);
				return forwardErrorRecoverable(mapping, request, userSession, ContribuyenteCerReport.NAME, contribuyenteCerReportVO);
			}
			
			if (contribuyenteCerReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + contribuyenteCerReportVO.infoString()); 
				saveDemodaErrors(request, contribuyenteCerReportVO);
				
				return forwardErrorRecoverable(mapping, request, userSession, ContribuyenteCerReport.NAME, contribuyenteCerReportVO);
			}

			// Llamada al servicio	
			contribuyenteCerReportVO = GdeServiceLocator.getReporteService().getContribuyenteCerReportResult(userSession, contribuyenteCerReportVO);			

			// Tiene errores recuperables
			if (contribuyenteCerReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + contribuyenteCerReportVO.infoString()); 
				saveDemodaErrors(request, contribuyenteCerReportVO);
				
				return forwardErrorRecoverable(mapping, request, userSession, ContribuyenteCerReport.NAME, contribuyenteCerReportVO);
			}
			
			// Tiene errores no recuperables
			if (contribuyenteCerReportVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + contribuyenteCerReportVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, ContribuyenteCerReport.NAME, contribuyenteCerReportVO);
			}
		
			// Envio el VO al request
			request.setAttribute(ContribuyenteCerReport.NAME, contribuyenteCerReportVO);
			// Subo el searchPage al userSession
			userSession.put(ContribuyenteCerReport.NAME, contribuyenteCerReportVO);
			
			return mapping.findForward(GdeConstants.FWD_CONTRIBUYENTECER_REPORT);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ContribuyenteCerReport.NAME);
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
				log.debug("nombre archivo: " + fileName);
				
				baseResponseFile(response,fileName);

				log.debug("exit: " + funcName);
				
				return null;
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ContribuyenteCerReport.NAME);
			}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return this.baseRefill(mapping, form, request, response, funcName, ContribuyenteCerReport.NAME);
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return baseVolver(mapping, form, request, response, ContribuyenteCerReport.NAME);
	}
		

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, ContribuyenteCerReport.NAME);
	}

}
