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
import ar.gov.rosario.siat.def.iface.model.CategoriaSearchPage;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarCategoriaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarCategoriaDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_CATEGORIA, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			CategoriaSearchPage categoriaSearchPageVO = DefServiceLocator.getGravamenService().getCategoriaSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (categoriaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + categoriaSearchPageVO.infoString()); 
				saveDemodaErrors(request, categoriaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, CategoriaSearchPage.NAME, categoriaSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (categoriaSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + categoriaSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CategoriaSearchPage.NAME, categoriaSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , CategoriaSearchPage.NAME, categoriaSearchPageVO);
			
			return mapping.findForward(DefConstants.FWD_CATEGORIA_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CategoriaSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, CategoriaSearchPage.NAME);
		
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
			CategoriaSearchPage categoriaSearchPageVO = (CategoriaSearchPage) userSession.get(CategoriaSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (categoriaSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + CategoriaSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CategoriaSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(categoriaSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				categoriaSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (categoriaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + categoriaSearchPageVO.infoString()); 
				saveDemodaErrors(request, categoriaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, CategoriaSearchPage.NAME, categoriaSearchPageVO);
			}
				
			// Llamada al servicio	
			categoriaSearchPageVO = DefServiceLocator.getGravamenService().getCategoriaSearchPageResult(userSession, categoriaSearchPageVO);			
						
			// Tiene errores recuperables
			if (categoriaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + categoriaSearchPageVO.infoString()); 
				saveDemodaErrors(request, categoriaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, CategoriaSearchPage.NAME, categoriaSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (categoriaSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + categoriaSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, CategoriaSearchPage.NAME, categoriaSearchPageVO);
			}
			// Envio el VO al request
			request.setAttribute(CategoriaSearchPage.NAME, categoriaSearchPageVO);
			// Nuleo el list result
			//categoriaSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(CategoriaSearchPage.NAME, categoriaSearchPageVO);
			
			return mapping.findForward(DefConstants.FWD_CATEGORIA_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CategoriaSearchPage.NAME);
		}
	}
	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_CATEGORIA);

	}
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_CATEGORIA);
		
	}
	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_CATEGORIA);

	}
	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_CATEGORIA);
	}
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			return forwardActivarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_CATEGORIA);			
		}
		
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			return forwardDesactivarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_CATEGORIA);
		}
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, CategoriaSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, CategoriaSearchPage.NAME);
		
	}

}