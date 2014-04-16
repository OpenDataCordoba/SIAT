//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.def.iface.model.ServicioBancoSearchPage;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarServicioBancoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarServicioBancoDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");
			
			UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_SERVICIO_BANCO, act);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				
				ServicioBancoSearchPage servicioBancoSearchPageVO = DefServiceLocator.getServicioBancoService().getServicioBancoSearchPageInit(userSession);
				
				// Tiene errores recuperables
				if (servicioBancoSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + servicioBancoSearchPageVO.infoString()); 
					saveDemodaErrors(request, servicioBancoSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, ServicioBancoSearchPage.NAME, servicioBancoSearchPageVO);
				} 

				// Tiene errores no recuperables
				if (servicioBancoSearchPageVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + servicioBancoSearchPageVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, ServicioBancoSearchPage.NAME, servicioBancoSearchPageVO);
				}
				
				// Si no tiene error
				baseInicializarSearchPage(mapping, request, userSession , ServicioBancoSearchPage.NAME, servicioBancoSearchPageVO);
				
				return mapping.findForward(DefConstants.FWD_SERVICIOBANCO_SEARCHPAGE);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ServicioBancoSearchPage.NAME);
			}
		}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return this.baseRefill(mapping, form, request, response, funcName, ServicioBancoSearchPage.NAME);

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
			ServicioBancoSearchPage servicioBancoSearchPageVO = (ServicioBancoSearchPage) userSession.get(ServicioBancoSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (servicioBancoSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + ServicioBancoSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ServicioBancoSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(servicioBancoSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				servicioBancoSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (servicioBancoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + servicioBancoSearchPageVO.infoString()); 
				saveDemodaErrors(request, servicioBancoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ServicioBancoSearchPage.NAME, servicioBancoSearchPageVO);
			}

			// Llamada al servicio	
			servicioBancoSearchPageVO = DefServiceLocator.getServicioBancoService().getServicioBancoSearchPageResult(userSession, servicioBancoSearchPageVO);			

			// Tiene errores recuperables
			if (servicioBancoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + servicioBancoSearchPageVO.infoString()); 
				saveDemodaErrors(request, servicioBancoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ServicioBancoSearchPage.NAME, servicioBancoSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (servicioBancoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + servicioBancoSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, ServicioBancoSearchPage.NAME, servicioBancoSearchPageVO);
			}
			
			// Envio el VO al request
			request.setAttribute(ServicioBancoSearchPage.NAME, servicioBancoSearchPageVO);
			// Nuleo el list result
			//servicioBancoSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(ServicioBancoSearchPage.NAME, servicioBancoSearchPageVO);
			
			return mapping.findForward(DefConstants.FWD_SERVICIOBANCO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ServicioBancoSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardVerSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_SERVICIOBANCO);
		}

		public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardAgregarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_ENCSERVICIOBANCO);
		}

		public ActionForward modificar(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardModificarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_SERVICIOBANCO);	
		}

		public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardEliminarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_SERVICIOBANCO);
			
		}
		
		public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			return forwardActivarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_SERVICIOBANCO);			
		}
		
		public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			return forwardDesactivarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_SERVICIOBANCO);
		}

		public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseSeleccionar(mapping, request, response, funcName, ServicioBancoSearchPage.NAME);
		}	

		public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			return baseVolver(mapping, form, request, response, ServicioBancoSearchPage.NAME);
			
		}

	
}
