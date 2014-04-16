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

import ar.gov.rosario.siat.bal.iface.model.TotalParReport;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.RangoFechaVO;

public final class ReporteTotalParDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(ReporteTotalParDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.CONSULTAR_TOTAL_PAR, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			TotalParReport totalParReportVO = BalServiceLocator.getClasificacionService().getTotalParReportInit(userSession);
			
			// Tiene errores recuperables
			if (totalParReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + totalParReportVO.infoString()); 
				saveDemodaErrors(request, totalParReportVO);
				return forwardErrorRecoverable(mapping, request, userSession, TotalParReport.NAME, totalParReportVO);
			} 

			// Tiene errores no recuperables
			if (totalParReportVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + totalParReportVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TotalParReport.NAME, totalParReportVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , TotalParReport.NAME, totalParReportVO);
			
			return mapping.findForward(BalConstants.FWD_TOTALPAR_REPORT);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TotalParReport.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, TotalParReport.NAME);
		
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
			TotalParReport totalParReportVO = (TotalParReport) userSession.get(TotalParReport.NAME);
			
			// Si es nulo no se puede continuar
			if (totalParReportVO == null) {
				log.error("error en: "  + funcName + ": " + TotalParReport.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TotalParReport.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(totalParReportVO, request);
				
				// Si estan habilitados los rangos de fechas adicionales
				if("on".equals(request.getParameter("rangosFechaExtras"))){
					totalParReportVO.setRangosFechaExtras(true);
					
					// Popula los rangos adicionales de fechas
					for(RangoFechaVO rangoFecha: totalParReportVO.getListRangosFecha()){
						String fechaDesdeView = request.getParameter("fechaDesde"+rangoFecha.getIndice());
						String fechaHastaView = request.getParameter("fechaHasta"+rangoFecha.getIndice());
						rangoFecha.setFechaDesdeView(fechaDesdeView);
						rangoFecha.setFechaHastaView(fechaHastaView);
					}
				}
				
				// Setea el PageNumber del PageModel				
				totalParReportVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (totalParReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + totalParReportVO.infoString()); 
				saveDemodaErrors(request, totalParReportVO);
				return forwardErrorRecoverable(mapping, request, userSession, TotalParReport.NAME, totalParReportVO);
			}
				
			// Llamada al servicio	
			totalParReportVO = BalServiceLocator.getClasificacionService().getTotalParReportResult(userSession, totalParReportVO);			

			// Tiene errores recuperables
			if (totalParReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + totalParReportVO.infoString()); 
				saveDemodaErrors(request, totalParReportVO);
				return forwardErrorRecoverable(mapping, request, userSession, TotalParReport.NAME, totalParReportVO);
			}
			
			// Tiene errores no recuperables
			if (totalParReportVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + totalParReportVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, TotalParReport.NAME, totalParReportVO);
			}
		
			// Envio el VO al request
			request.setAttribute(TotalParReport.NAME, totalParReportVO);
			
			// Subo en el el searchPage al userSession
			userSession.put(TotalParReport.NAME, totalParReportVO);
			
			return mapping.findForward(BalConstants.FWD_TOTALPAR_REPORT);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TotalParReport.NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, TotalParReport.NAME);
		
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
				return baseException(mapping, request, funcName, exception, TotalParReport.NAME);
			}
	}
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, TotalParReport.NAME);
	}
	
	public ActionForward paramRango(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
			
		try {
			//bajo el report del usserSession
			TotalParReport totalParReportVO =  (TotalParReport) userSession.get(TotalParReport.NAME);
				
			// Si es nulo no se puede continuar
			if (totalParReportVO == null) {
				log.error("error en: "  + funcName + ": " + TotalParReport.NAME + " " +
						  "IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TotalParReport.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(totalParReportVO, request);
		
			// Si estan habilitados los rangos de fechas adicionales
			if("on".equals(request.getParameter("rangosFechaExtras"))){
				totalParReportVO.setRangosFechaExtras(true);
				
				// Popula los rangos adicionales de fechas
				for(RangoFechaVO rangoFecha: totalParReportVO.getListRangosFecha()){
					String fechaDesdeView = request.getParameter("fechaDesde"+rangoFecha.getIndice());
					String fechaHastaView = request.getParameter("fechaHasta"+rangoFecha.getIndice());
					rangoFecha.setFechaDesdeView(fechaDesdeView);
					rangoFecha.setFechaHastaView(fechaHastaView);
				}
			}

			// Tiene errores recuperables
			if (totalParReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + totalParReportVO.infoString()); 
				saveDemodaErrors(request, totalParReportVO);
				return forwardErrorRecoverable(mapping, request, userSession, TotalParReport.NAME, totalParReportVO);
			}

			totalParReportVO = BalServiceLocator.getClasificacionService().getTotalParReportParamRango(userSession, totalParReportVO);
				
			// Tiene errores recuperables
			if (totalParReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + totalParReportVO.infoString()); 
				saveDemodaErrors(request, totalParReportVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
											   TotalParReport.NAME, totalParReportVO);
			}
				
			// Tiene errores no recuperables
			if (totalParReportVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + totalParReportVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
												  TotalParReport.NAME, totalParReportVO);
			}
				
			// grabo los mensajes si hubiere
			saveDemodaMessages(request, totalParReportVO);
				
			// Envio el VO al request
			request.setAttribute(TotalParReport.NAME, totalParReportVO);

			return mapping.findForward(BalConstants.FWD_TOTALPAR_REPORT);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TotalParReport.NAME);
		}
	}
}
