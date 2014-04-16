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
import ar.gov.rosario.siat.gde.iface.model.TramiteSearchPage;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarTramiteDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarTramiteDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.INFORME_DEUDA_ESCRIBANO, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			TramiteSearchPage tramiteSearchPageVO = GdeServiceLocator.getGestionDeudaService().getTramiteSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (tramiteSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tramiteSearchPageVO.infoString()); 
				saveDemodaErrors(request, tramiteSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, TramiteSearchPage.NAME, tramiteSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (tramiteSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tramiteSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TramiteSearchPage.NAME, tramiteSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , TramiteSearchPage.NAME, tramiteSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_TRAMITE_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TramiteSearchPage.NAME);
		}
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
			TramiteSearchPage tramiteSearchPageVO = (TramiteSearchPage) userSession.get(TramiteSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (tramiteSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + TramiteSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TramiteSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(tramiteSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				tramiteSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (tramiteSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tramiteSearchPageVO.infoString()); 
				saveDemodaErrors(request, tramiteSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, TramiteSearchPage.NAME, tramiteSearchPageVO);
			}
				
			// Llamada al servicio	
			tramiteSearchPageVO = GdeServiceLocator.getGestionDeudaService().getTramiteSearchPageResult(userSession, tramiteSearchPageVO);			

			
			// Tiene errores recuperables
			if (tramiteSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tramiteSearchPageVO.infoString()); 
				saveDemodaErrors(request, tramiteSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, TramiteSearchPage.NAME, tramiteSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (tramiteSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tramiteSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, TramiteSearchPage.NAME, tramiteSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(TramiteSearchPage.NAME, tramiteSearchPageVO);
			// Nuleo el list result
			// Subo en el el searchPage al userSession
			userSession.put(TramiteSearchPage.NAME, tramiteSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_TRAMITE_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TramiteSearchPage.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, TramiteSearchPage.NAME);
		
	}
		
	
}
