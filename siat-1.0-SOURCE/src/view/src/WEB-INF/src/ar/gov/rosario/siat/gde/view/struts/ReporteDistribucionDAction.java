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
import ar.gov.rosario.siat.gde.iface.model.DistribucionReport;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class ReporteDistribucionDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(ReporteDistribucionDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.DISTRIBUCION_REPORT, act);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			DistribucionReport distribucionReportVO = GdeServiceLocator.getReporteService().getDistribucionReportInit(userSession);
			
			// Tiene errores recuperables
			if (distribucionReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + distribucionReportVO.infoString()); 
				saveDemodaErrors(request, distribucionReportVO);
				return forwardErrorRecoverable(mapping, request, userSession, DistribucionReport.NAME, distribucionReportVO);
			} 

			// Tiene errores no recuperables
			if (distribucionReportVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + distribucionReportVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DistribucionReport.NAME, distribucionReportVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , DistribucionReport.NAME, distribucionReportVO);
			
			return mapping.findForward(GdeConstants.FWD_DISTRIBUCION_REPORT);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DistribucionReport.NAME);
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
			DistribucionReport distribucionReportVO = (DistribucionReport) userSession.get(DistribucionReport.NAME);
			
			// Si es nulo no se puede continuar
			if (distribucionReportVO == null) {
				log.error("error en: "  + funcName + ": " + DistribucionReport.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DistribucionReport.NAME); 
			}
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(distribucionReportVO, request);
			// Setea el PageNumber del PageModel				
			distribucionReportVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			
            // Tiene errores recuperables
			if (distribucionReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + distribucionReportVO.infoString()); 
				saveDemodaErrors(request, distribucionReportVO);
				return forwardErrorRecoverable(mapping, request, userSession, DistribucionReport.NAME, distribucionReportVO);
			}
			
			if (distribucionReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + distribucionReportVO.infoString()); 
				saveDemodaErrors(request, distribucionReportVO);
				
				return forwardErrorRecoverable(mapping, request, userSession, DistribucionReport.NAME, distribucionReportVO);
			}

			// Llamada al servicio	
			distribucionReportVO = GdeServiceLocator.getReporteService().getDistribucionReportResult(userSession, distribucionReportVO);			

			// Tiene errores recuperables
			if (distribucionReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + distribucionReportVO.infoString()); 
				saveDemodaErrors(request, distribucionReportVO);
				
				return forwardErrorRecoverable(mapping, request, userSession, DistribucionReport.NAME, distribucionReportVO);
			}
			
			// Tiene errores no recuperables
			if (distribucionReportVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + distribucionReportVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, DistribucionReport.NAME, distribucionReportVO);
			}
		
			// Envio el VO al request
			request.setAttribute(DistribucionReport.NAME, distribucionReportVO);
			// Subo el searchPage al userSession
			userSession.put(DistribucionReport.NAME, distribucionReportVO);
			
			return mapping.findForward(GdeConstants.FWD_DISTRIBUCION_REPORT);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DistribucionReport.NAME);
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
			return this.baseRefill(mapping, form, request, response, funcName, DistribucionReport.NAME);
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return baseVolver(mapping, form, request, response, DistribucionReport.NAME);
	}
		

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, DistribucionReport.NAME);
	}

}
