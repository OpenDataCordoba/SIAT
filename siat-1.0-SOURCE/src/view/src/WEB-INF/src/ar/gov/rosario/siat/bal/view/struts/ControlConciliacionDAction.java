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

import ar.gov.rosario.siat.bal.iface.model.ControlConciliacionSearchPage;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class ControlConciliacionDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(ControlConciliacionDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.CONTROL_CONCILIACION, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			ControlConciliacionSearchPage controlConciliacionSearchPageVO = BalServiceLocator.getBalanceService().getControlConciliacionSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (controlConciliacionSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + controlConciliacionSearchPageVO.infoString()); 
				saveDemodaErrors(request, controlConciliacionSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ControlConciliacionSearchPage.NAME, controlConciliacionSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (controlConciliacionSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + controlConciliacionSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ControlConciliacionSearchPage.NAME, controlConciliacionSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , ControlConciliacionSearchPage.NAME, controlConciliacionSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_CONTROLCONCILIACION_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ControlConciliacionSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, ControlConciliacionSearchPage.NAME);
		
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
			ControlConciliacionSearchPage controlConciliacionSearchPageVO = (ControlConciliacionSearchPage) userSession.get(ControlConciliacionSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (controlConciliacionSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + ControlConciliacionSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ControlConciliacionSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(controlConciliacionSearchPageVO, request);
				
				// Setea el PageNumber del PageModel				
				controlConciliacionSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (controlConciliacionSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + controlConciliacionSearchPageVO.infoString()); 
				saveDemodaErrors(request, controlConciliacionSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ControlConciliacionSearchPage.NAME, controlConciliacionSearchPageVO);
			}
				
			// Llamada al servicio	
			controlConciliacionSearchPageVO = BalServiceLocator.getBalanceService().getControlConciliacionSearchPageResult(userSession, controlConciliacionSearchPageVO);			

			// Tiene errores recuperables
			if (controlConciliacionSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + controlConciliacionSearchPageVO.infoString()); 
				saveDemodaErrors(request, controlConciliacionSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ControlConciliacionSearchPage.NAME, controlConciliacionSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (controlConciliacionSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + controlConciliacionSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, ControlConciliacionSearchPage.NAME, controlConciliacionSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(ControlConciliacionSearchPage.NAME, controlConciliacionSearchPageVO);
			
			// Subo en el el searchPage al userSession
			userSession.put(ControlConciliacionSearchPage.NAME, controlConciliacionSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_CONTROLCONCILIACION_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ControlConciliacionSearchPage.NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ControlConciliacionSearchPage.NAME);
		
	}
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, ControlConciliacionSearchPage.NAME);
	}
}
