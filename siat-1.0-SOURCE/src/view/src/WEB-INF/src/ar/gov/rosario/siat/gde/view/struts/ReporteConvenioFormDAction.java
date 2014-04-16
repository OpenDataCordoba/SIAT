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
import ar.gov.rosario.siat.gde.iface.model.ConvenioFormReport;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class ReporteConvenioFormDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(ReporteConvenioFormDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_CONVENIO_REPORT, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			ConvenioFormReport convenioFormReportVO = GdeServiceLocator.getReporteService().getConvenioFormReportInit(userSession);
			
			// Tiene errores recuperables
			if (convenioFormReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + convenioFormReportVO.infoString()); 
				saveDemodaErrors(request, convenioFormReportVO);
				return forwardErrorRecoverable(mapping, request, userSession, ConvenioFormReport.NAME, convenioFormReportVO);
			} 

			// Tiene errores no recuperables
			if (convenioFormReportVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + convenioFormReportVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ConvenioFormReport.NAME, convenioFormReportVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , ConvenioFormReport.NAME, convenioFormReportVO);
			
			return mapping.findForward(GdeConstants.FWD_CONVENIO_FORM_REPORT);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConvenioFormReport.NAME);
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
			ConvenioFormReport convenioFormReportVO = (ConvenioFormReport) userSession.get(ConvenioFormReport.NAME);
			
			// Si es nulo no se puede continuar
			if (convenioFormReportVO == null) {
				log.error("error en: "  + funcName + ": " + ConvenioFormReport.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ConvenioFormReport.NAME); 
			}
			
			// siempre el buscar es diparado desde la pagina de busqueda
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(convenioFormReportVO, request);

			// Setea el PageNumber del PageModel				
			convenioFormReportVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			
            // Tiene errores recuperables
			if (convenioFormReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + convenioFormReportVO.infoString()); 
				saveDemodaErrors(request, convenioFormReportVO);
				return forwardErrorRecoverable(mapping, request, userSession, ConvenioFormReport.NAME, convenioFormReportVO);
			}
				
			// Llamada al servicio	
			convenioFormReportVO = GdeServiceLocator.getReporteService().getConvenioFormReportResult(userSession, convenioFormReportVO);			

			// Tiene errores recuperables
			if (convenioFormReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + convenioFormReportVO.infoString()); 
				saveDemodaErrors(request, convenioFormReportVO);
				
				return forwardErrorRecoverable(mapping, request, userSession, ConvenioFormReport.NAME, convenioFormReportVO);
			}
			
			// Tiene errores no recuperables
			if (convenioFormReportVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + convenioFormReportVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, ConvenioFormReport.NAME, convenioFormReportVO);
			}
		
			// Envio el VO al request
			request.setAttribute(ConvenioFormReport.NAME, convenioFormReportVO);
			// Nuleo el list result
			// Subo en el el searchPage al userSession
			userSession.put(ConvenioFormReport.NAME, convenioFormReportVO);
			
			return mapping.findForward(GdeConstants.FWD_CONVENIO_FORM_REPORT);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConvenioFormReport.NAME);
		}
	}

	public ActionForward paramViaDeuda(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
			
		try {
			//bajo el report del usserSession
			ConvenioFormReport convenioFormReportVO =  (ConvenioFormReport) userSession.get(ConvenioFormReport.NAME);
				
			// Si es nulo no se puede continuar
			if (convenioFormReportVO == null) {
				log.error("error en: "  + funcName + ": " + ConvenioFormReport.NAME + " " +
						  "IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ConvenioFormReport.NAME); 
			}
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(convenioFormReportVO, request);

			// no visualizo la lista de resultados
			convenioFormReportVO.setPageNumber(0L);
				
			// Tiene errores recuperables
			if (convenioFormReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + convenioFormReportVO.infoString()); 
				saveDemodaErrors(request, convenioFormReportVO);
				return forwardErrorRecoverable(mapping, request, userSession, ConvenioFormReport.NAME, convenioFormReportVO);
			}

			// llamar al servicio
			convenioFormReportVO = GdeServiceLocator.getReporteService().getConvenioFormReportParamViaDeuda(userSession, convenioFormReportVO);
			
			// Tiene errores recuperables
			if (convenioFormReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + convenioFormReportVO.infoString()); 
				saveDemodaErrors(request, convenioFormReportVO);
				return forwardErrorRecoverable(mapping, request, userSession, ConvenioFormReport.NAME, convenioFormReportVO);
			} 

			// Tiene errores no recuperables
			if (convenioFormReportVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + convenioFormReportVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ConvenioFormReport.NAME, convenioFormReportVO);
			}

			// grabo los mensajes si hubiere
			saveDemodaMessages(request, convenioFormReportVO);
				
			// Envio el VO al request
			request.setAttribute(ConvenioFormReport.NAME, convenioFormReportVO);

			return mapping.findForward(GdeConstants.FWD_CONVENIO_FORM_REPORT);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConvenioFormReport.NAME);
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
				return baseException(mapping, request, funcName, exception, ConvenioFormReport.NAME);
			}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return this.baseRefill(mapping, form, request, response, funcName, ConvenioFormReport.NAME);
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return baseVolver(mapping, form, request, response, ConvenioFormReport.NAME);
	}
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, ConvenioFormReport.NAME);
	}


}
