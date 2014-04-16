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
import ar.gov.rosario.siat.gde.iface.model.DeuCueGesJudSearchPage;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarDeuCueGesJudDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarDeuCueGesJudDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.CONSULTAR_DEUCUEGESJUD, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			DeuCueGesJudSearchPage deuCueGesJudSearchPageVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getDeuCueGesJudSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (deuCueGesJudSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + deuCueGesJudSearchPageVO.infoString()); 
				saveDemodaErrors(request, deuCueGesJudSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, DeuCueGesJudSearchPage.NAME, deuCueGesJudSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (deuCueGesJudSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + deuCueGesJudSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DeuCueGesJudSearchPage.NAME, deuCueGesJudSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , DeuCueGesJudSearchPage.NAME, deuCueGesJudSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_DEUCUEGESJUD_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DeuCueGesJudSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, DeuCueGesJudSearchPage.NAME);
		
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
			DeuCueGesJudSearchPage deuCueGesJudSearchPageVO = (DeuCueGesJudSearchPage) userSession.get(DeuCueGesJudSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (deuCueGesJudSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + DeuCueGesJudSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DeuCueGesJudSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(deuCueGesJudSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				deuCueGesJudSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (deuCueGesJudSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + deuCueGesJudSearchPageVO.infoString()); 
				saveDemodaErrors(request, deuCueGesJudSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, DeuCueGesJudSearchPage.NAME, deuCueGesJudSearchPageVO);
			}
				
			// Llamada al servicio	
			deuCueGesJudSearchPageVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getDeuCueGesJudSearchPageResult(userSession, deuCueGesJudSearchPageVO);			

			// Tiene errores recuperables
			if (deuCueGesJudSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + deuCueGesJudSearchPageVO.infoString()); 
				saveDemodaErrors(request, deuCueGesJudSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, DeuCueGesJudSearchPage.NAME, deuCueGesJudSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (deuCueGesJudSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + deuCueGesJudSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, DeuCueGesJudSearchPage.NAME, deuCueGesJudSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(DeuCueGesJudSearchPage.NAME, deuCueGesJudSearchPageVO);
			// Nuleo el list result
			//deuCueGesJudSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(DeuCueGesJudSearchPage.NAME, deuCueGesJudSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_DEUCUEGESJUD_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DeuCueGesJudSearchPage.NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, DeuCueGesJudSearchPage.NAME);
		
	}
		
	
}
