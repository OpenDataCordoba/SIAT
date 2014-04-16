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
import ar.gov.rosario.siat.gde.iface.model.DeudaProcuradorReport;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class ReporteDeudaProcuradorDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(ReporteEmisionDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.DEUDA_PROCURADOR_REPORT, act);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			DeudaProcuradorReport recaudadoReportVO = GdeServiceLocator.getReporteService().getDeudaProcuradorReportInit(userSession);
			
			// Tiene errores recuperables
			if (recaudadoReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + recaudadoReportVO.infoString()); 
				saveDemodaErrors(request, recaudadoReportVO);
				return forwardErrorRecoverable(mapping, request, userSession, DeudaProcuradorReport.NAME, recaudadoReportVO);
			} 

			// Tiene errores no recuperables
			if (recaudadoReportVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + recaudadoReportVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DeudaProcuradorReport.NAME, recaudadoReportVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , DeudaProcuradorReport.NAME, recaudadoReportVO);
			
			return mapping.findForward(GdeConstants.FWD_DEUDA_PROCURADOR_REPORT);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DeudaProcuradorReport.NAME);
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
			DeudaProcuradorReport deudaProcuradorReportVO = (DeudaProcuradorReport) userSession.get(DeudaProcuradorReport.NAME);
			
			// Si es nulo no se puede continuar
			if (deudaProcuradorReportVO == null) {
				log.error("error en: "  + funcName + ": " + DeudaProcuradorReport.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DeudaProcuradorReport.NAME); 
			}
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(deudaProcuradorReportVO, request);
			// Setea el PageNumber del PageModel				
			deudaProcuradorReportVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			
            // Tiene errores recuperables
			if (deudaProcuradorReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + deudaProcuradorReportVO.infoString()); 
				saveDemodaErrors(request, deudaProcuradorReportVO);
				return forwardErrorRecoverable(mapping, request, userSession, DeudaProcuradorReport.NAME, deudaProcuradorReportVO);
			}
			
			if (deudaProcuradorReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + deudaProcuradorReportVO.infoString()); 
				saveDemodaErrors(request, deudaProcuradorReportVO);
				
				return forwardErrorRecoverable(mapping, request, userSession, DeudaProcuradorReport.NAME, deudaProcuradorReportVO);
			}

			// Llamada al servicio	
			deudaProcuradorReportVO = GdeServiceLocator.getReporteService().getDeudaProcuradorReportResult(userSession, deudaProcuradorReportVO);			

			// Tiene errores recuperables
			if (deudaProcuradorReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + deudaProcuradorReportVO.infoString()); 
				saveDemodaErrors(request, deudaProcuradorReportVO);
				
				return forwardErrorRecoverable(mapping, request, userSession, DeudaProcuradorReport.NAME, deudaProcuradorReportVO);
			}
			
			// Tiene errores no recuperables
			if (deudaProcuradorReportVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + deudaProcuradorReportVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, DeudaProcuradorReport.NAME, deudaProcuradorReportVO);
			}
		
			// Envio el VO al request
			request.setAttribute(DeudaProcuradorReport.NAME, deudaProcuradorReportVO);
			// Subo el searchPage al userSession
			userSession.put(DeudaProcuradorReport.NAME, deudaProcuradorReportVO);
			
			return mapping.findForward(GdeConstants.FWD_DEUDA_PROCURADOR_REPORT);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DeudaProcuradorReport.NAME);
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
			return this.baseRefill(mapping, form, request, response, funcName, DeudaProcuradorReport.NAME);
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return baseVolver(mapping, form, request, response, DeudaProcuradorReport.NAME);
	}
		

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, DeudaProcuradorReport.NAME);
	}

}
