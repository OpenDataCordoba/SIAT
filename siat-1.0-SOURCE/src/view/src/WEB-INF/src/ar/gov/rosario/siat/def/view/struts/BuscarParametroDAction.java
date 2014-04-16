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
import ar.gov.rosario.siat.def.iface.model.ParametroSearchPage;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarParametroDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarParametroDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_PARAMETRO, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			log.debug("Test" + userSession.getNavModel().getSelectedId());
			
			ParametroSearchPage parametroSearchPageVO = DefServiceLocator.getConfiguracionService().getParametroSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (parametroSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + parametroSearchPageVO.infoString()); 
				saveDemodaErrors(request, parametroSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ParametroSearchPage.NAME, parametroSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (parametroSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + parametroSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ParametroSearchPage.NAME, parametroSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , ParametroSearchPage.NAME, parametroSearchPageVO);
			
			return mapping.findForward(DefConstants.FWD_PARAMETRO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ParametroSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, ParametroSearchPage.NAME);
		
	}
	
	public ActionForward buscar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);

		if (log.isDebugEnabled()) log.debug(funcName + ": Hay session!!!: nombre usuario:" + userSession.getUserName());		
		
		try {
			
			// Bajo el searchPage del userSession
			ParametroSearchPage parametroSearchPageVO = (ParametroSearchPage) userSession.get(ParametroSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (parametroSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + ParametroSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ParametroSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(parametroSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				parametroSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (parametroSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + parametroSearchPageVO.infoString()); 
				saveDemodaErrors(request, parametroSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ParametroSearchPage.NAME, parametroSearchPageVO);
			}
				
			// Llamada al servicio	
			parametroSearchPageVO = DefServiceLocator.getConfiguracionService().getParametroSearchPageResult(userSession, parametroSearchPageVO);			

			// Tiene errores recuperables
			if (parametroSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + parametroSearchPageVO.infoString()); 
				saveDemodaErrors(request, parametroSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ParametroSearchPage.NAME, parametroSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (parametroSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + parametroSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, ParametroSearchPage.NAME, parametroSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(ParametroSearchPage.NAME, parametroSearchPageVO);
			// Nuleo el list result
			//parametroSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(ParametroSearchPage.NAME, parametroSearchPageVO);
			
			return mapping.findForward(DefConstants.FWD_PARAMETRO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ParametroSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_PARAMETRO);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		return forwardAgregarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_PARAMETRO);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_PARAMETRO);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_PARAMETRO);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_PARAMETRO);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_PARAMETRO);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, ParametroSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ParametroSearchPage.NAME);
		
	}
		
	
}
