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

import ar.gov.rosario.siat.bal.iface.model.RentasReport;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.gde.iface.model.ConvenioReport;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class ReporteRentasDAction extends BaseDispatchAction {


	private Log log = LogFactory.getLog(ReporteRentasDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.CONSULTAR_RENTAS, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			RentasReport rentasReportVO = BalServiceLocator.getClasificacionService().getRentasReportInit(userSession);
			
			// Tiene errores recuperables
			if (rentasReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + rentasReportVO.infoString()); 
				saveDemodaErrors(request, rentasReportVO);
				return forwardErrorRecoverable(mapping, request, userSession, RentasReport.NAME, rentasReportVO);
			} 

			// Tiene errores no recuperables
			if (rentasReportVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + rentasReportVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, RentasReport.NAME, rentasReportVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , RentasReport.NAME, rentasReportVO);
			
			return mapping.findForward(BalConstants.FWD_RENTAS_REPORT);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, RentasReport.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, RentasReport.NAME);
		
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
				RentasReport rentasReportVO = (RentasReport) userSession.get(RentasReport.NAME);
				
				// Si es nulo no se puede continuar
				if (rentasReportVO == null) {
					log.error("error en: "  + funcName + ": " + RentasReport.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RentasReport.NAME); 
				}
				
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(rentasReportVO, request);

				// Setea el PageNumber del PageModel				
				rentasReportVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
				
	            // Tiene errores recuperables
				if (rentasReportVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + rentasReportVO.infoString()); 
					saveDemodaErrors(request, rentasReportVO);
					return forwardErrorRecoverable(mapping, request, userSession, RentasReport.NAME, rentasReportVO);
				}
					
				// Llamada al servicio	
				rentasReportVO = BalServiceLocator.getClasificacionService().getRentasReportResult(userSession, rentasReportVO);			

				// Tiene errores recuperables
				if (rentasReportVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + rentasReportVO.infoString()); 
					saveDemodaErrors(request, rentasReportVO);
					return forwardErrorRecoverable(mapping, request, userSession, RentasReport.NAME, rentasReportVO);
				}
				
				// Tiene errores no recuperables
				if (rentasReportVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + rentasReportVO.errorString());
					return forwardErrorNonRecoverable(mapping, request, funcName, RentasReport.NAME, rentasReportVO);
				}
							
				// Envio el VO al request
				request.setAttribute(RentasReport.NAME, rentasReportVO);
				// Subo en el el searchPage al userSession
				userSession.put(RentasReport.NAME, rentasReportVO);
				
				return mapping.findForward(BalConstants.FWD_RENTAS_REPORT);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RentasReport.NAME);
			}
		}

		public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, RentasReport.NAME);
			
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
				return baseRefill(mapping, form, request, response, funcName, RentasReport.NAME);
		}

}
