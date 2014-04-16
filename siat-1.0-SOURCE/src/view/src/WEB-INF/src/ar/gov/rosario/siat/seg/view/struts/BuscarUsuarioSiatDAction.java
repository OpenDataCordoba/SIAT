//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.seg.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.seg.iface.model.UsuarioSiatSearchPage;
import ar.gov.rosario.siat.seg.iface.service.SegServiceLocator;
import ar.gov.rosario.siat.seg.iface.util.SegSecurityConstants;
import ar.gov.rosario.siat.seg.view.util.SegConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarUsuarioSiatDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarUsuarioSiatDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, SegSecurityConstants.ABM_USUARIOSIAT, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			UsuarioSiatSearchPage usuarioSiatSearchPageVO = SegServiceLocator.getSeguridadService().getUsuarioSiatSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (usuarioSiatSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + usuarioSiatSearchPageVO.infoString()); 
				saveDemodaErrors(request, usuarioSiatSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, UsuarioSiatSearchPage.NAME, usuarioSiatSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (usuarioSiatSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + usuarioSiatSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, UsuarioSiatSearchPage.NAME, usuarioSiatSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , UsuarioSiatSearchPage.NAME, usuarioSiatSearchPageVO);
			
			return mapping.findForward(SegConstants.FWD_USUARIOSIAT_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, UsuarioSiatSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, UsuarioSiatSearchPage.NAME);
		
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
			UsuarioSiatSearchPage usuarioSiatSearchPageVO = (UsuarioSiatSearchPage) userSession.get(UsuarioSiatSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (usuarioSiatSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + UsuarioSiatSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, UsuarioSiatSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(usuarioSiatSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				usuarioSiatSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (usuarioSiatSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + usuarioSiatSearchPageVO.infoString()); 
				saveDemodaErrors(request, usuarioSiatSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, UsuarioSiatSearchPage.NAME, usuarioSiatSearchPageVO);
			}
				
			// Llamada al servicio	
			usuarioSiatSearchPageVO = SegServiceLocator.getSeguridadService().getUsuarioSiatSearchPageResult(userSession, usuarioSiatSearchPageVO);			

			// Tiene errores recuperables
			if (usuarioSiatSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + usuarioSiatSearchPageVO.infoString()); 
				saveDemodaErrors(request, usuarioSiatSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, UsuarioSiatSearchPage.NAME, usuarioSiatSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (usuarioSiatSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + usuarioSiatSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, UsuarioSiatSearchPage.NAME, usuarioSiatSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(UsuarioSiatSearchPage.NAME, usuarioSiatSearchPageVO);
			// Nuleo el list result
			//usuarioSiatSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(UsuarioSiatSearchPage.NAME, usuarioSiatSearchPageVO);
			
			return mapping.findForward(SegConstants.FWD_USUARIOSIAT_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, UsuarioSiatSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, SegConstants.ACTION_ADMINISTRAR_USUARIOSIAT);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarSearchPage(mapping, request, funcName, SegConstants.ACTION_ADMINISTRAR_USUARIOSIAT);

	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, SegConstants.ACTION_ADMINISTRAR_USUARIOSIAT);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, SegConstants.ACTION_ADMINISTRAR_USUARIOSIAT);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, SegConstants.ACTION_ADMINISTRAR_USUARIOSIAT);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, SegConstants.ACTION_ADMINISTRAR_USUARIOSIAT);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, UsuarioSiatSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, UsuarioSiatSearchPage.NAME);
		
	}
		
	
}
