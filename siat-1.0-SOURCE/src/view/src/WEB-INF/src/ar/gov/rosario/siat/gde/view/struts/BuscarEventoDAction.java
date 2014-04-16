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
import ar.gov.rosario.siat.gde.iface.model.EventoSearchPage;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarEventoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarEventoDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_EVENTO, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			EventoSearchPage eventoSearchPageVO = GdeServiceLocator.getDefinicionService().getEventoSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (eventoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + eventoSearchPageVO.infoString()); 
				saveDemodaErrors(request, eventoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, EventoSearchPage.NAME, eventoSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (eventoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + eventoSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EventoSearchPage.NAME, eventoSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , EventoSearchPage.NAME, eventoSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_EVENTO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EventoSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, EventoSearchPage.NAME);
		
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
			EventoSearchPage eventoSearchPageVO = (EventoSearchPage) userSession.get(EventoSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (eventoSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + EventoSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EventoSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(eventoSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				eventoSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (eventoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + eventoSearchPageVO.infoString()); 
				saveDemodaErrors(request, eventoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, EventoSearchPage.NAME, eventoSearchPageVO);
			}
				
			// Llamada al servicio	
			eventoSearchPageVO = GdeServiceLocator.getDefinicionService().getEventoSearchPageResult(userSession, eventoSearchPageVO);			

			// Tiene errores recuperables
			if (eventoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + eventoSearchPageVO.infoString()); 
				saveDemodaErrors(request, eventoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, EventoSearchPage.NAME, eventoSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (eventoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + eventoSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, EventoSearchPage.NAME, eventoSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(EventoSearchPage.NAME, eventoSearchPageVO);
			// Nuleo el list result
			//eventoSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(EventoSearchPage.NAME, eventoSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_EVENTO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EventoSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_EVENTO);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		return forwardAgregarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_EVENTO);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_EVENTO);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_EVENTO);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_EVENTO);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_EVENTO);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, EventoSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, EventoSearchPage.NAME);
		
	}
		
	
}
