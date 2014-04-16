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
import ar.gov.rosario.siat.gde.iface.model.DeudaAnuladaReport;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class ReporteDeudaAnuladaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(ReporteDeudaAnuladaDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_GEJUD_REPORT, act);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			DeudaAnuladaReport deudaAnuladaReportVO = GdeServiceLocator.getReporteService().getDeudaAnuladaReportInit(userSession);
			
			// Tiene errores recuperables
			if (deudaAnuladaReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + deudaAnuladaReportVO.infoString()); 
				saveDemodaErrors(request, deudaAnuladaReportVO);
				return forwardErrorRecoverable(mapping, request, userSession, DeudaAnuladaReport.NAME, deudaAnuladaReportVO);
			} 

			// Tiene errores no recuperables
			if (deudaAnuladaReportVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + deudaAnuladaReportVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DeudaAnuladaReport.NAME, deudaAnuladaReportVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , DeudaAnuladaReport.NAME, deudaAnuladaReportVO);
			
			return mapping.findForward(GdeConstants.FWD_DEUDAANULADA_REPORT);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DeudaAnuladaReport.NAME);
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
			DeudaAnuladaReport deudaAnuladaReportVO = (DeudaAnuladaReport) userSession.get(DeudaAnuladaReport.NAME);
			
			// Si es nulo no se puede continuar
			if (deudaAnuladaReportVO == null) {
				log.error("error en: "  + funcName + ": " + DeudaAnuladaReport.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DeudaAnuladaReport.NAME); 
			}
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(deudaAnuladaReportVO, request);
			// Setea el PageNumber del PageModel				
			deudaAnuladaReportVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			
            // Tiene errores recuperables
			if (deudaAnuladaReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + deudaAnuladaReportVO.infoString()); 
				saveDemodaErrors(request, deudaAnuladaReportVO);
				return forwardErrorRecoverable(mapping, request, userSession, DeudaAnuladaReport.NAME, deudaAnuladaReportVO);
			}
			
			// Llamada al servicio	
			deudaAnuladaReportVO = GdeServiceLocator.getReporteService().getDeudaAnuladaReportResult(userSession, deudaAnuladaReportVO);			

			// Tiene errores recuperables
			if (deudaAnuladaReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + deudaAnuladaReportVO.infoString()); 
				saveDemodaErrors(request, deudaAnuladaReportVO);
				
				return forwardErrorRecoverable(mapping, request, userSession, DeudaAnuladaReport.NAME, deudaAnuladaReportVO);
			}
			
			// Tiene errores no recuperables
			if (deudaAnuladaReportVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + deudaAnuladaReportVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, DeudaAnuladaReport.NAME, deudaAnuladaReportVO);
			}
		
			// Envio el VO al request
			request.setAttribute(DeudaAnuladaReport.NAME, deudaAnuladaReportVO);
			// Subo el searchPage al userSession
			userSession.put(DeudaAnuladaReport.NAME, deudaAnuladaReportVO);
			
			return mapping.findForward(GdeConstants.FWD_DEUDAANULADA_REPORT);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DeudaAnuladaReport.NAME);
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
			return this.baseRefill(mapping, form, request, response, funcName, DeudaAnuladaReport.NAME);
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return baseVolver(mapping, form, request, response, DeudaAnuladaReport.NAME);
	}
		

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, DeudaAnuladaReport.NAME);
	}

}
