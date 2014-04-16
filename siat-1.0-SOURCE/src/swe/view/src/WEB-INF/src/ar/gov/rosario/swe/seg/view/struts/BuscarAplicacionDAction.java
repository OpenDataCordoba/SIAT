//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.seg.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.swe.SweServiceLocator;
import ar.gov.rosario.swe.base.view.struts.SweBaseDispatchAction;
import ar.gov.rosario.swe.iface.model.AplicacionSearchPage;
import ar.gov.rosario.swe.iface.model.SweUserSession;
import ar.gov.rosario.swe.iface.util.SweSecurityConstants;
import ar.gov.rosario.swe.seg.view.util.SweSegConstants;
import ar.gov.rosario.swe.view.util.SweUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.NavModel;

public final class BuscarAplicacionDAction extends SweBaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarAplicacionDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String act = getCurrentAct(request);
		SweUserSession userSession = canAccess(request, mapping, SweSecurityConstants.ABM_APLICACION, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			AplicacionSearchPage aplicacionSearchPageVO = SweServiceLocator.getSweService().getAplicacionSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (aplicacionSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + aplicacionSearchPageVO.infoString()); 
				saveDemodaErrors(request, aplicacionSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, AplicacionSearchPage.NAME, aplicacionSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (aplicacionSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + aplicacionSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, AplicacionSearchPage.NAME, aplicacionSearchPageVO);
			}
			
			aplicacionSearchPageVO.setPageNumber(new Long(1));

			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , AplicacionSearchPage.NAME, aplicacionSearchPageVO);
			return buscar(mapping, form, request, response);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		SweUserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try{
			// seteo en el navModel el que estaba cargado en el inicializar
			userSession.setNavModel( (NavModel) userSession.get(mapping.getPath()) );
			return this.inicializar(mapping, form, request, response);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}
	
	public ActionForward buscar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		SweUserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// seteo la accion buscar al pervActionParameter para que muetre el boton agregar
			// ahora seteo el act ?
			userSession.getNavModel().setAct(funcName);
			
			// Bajo el searchPage del userSession
			AplicacionSearchPage aplicacionSearchPageVO = (AplicacionSearchPage) userSession.get(AplicacionSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (aplicacionSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + AplicacionSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AplicacionSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(aplicacionSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				aplicacionSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (aplicacionSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + aplicacionSearchPageVO.infoString()); 
				saveDemodaErrors(request, aplicacionSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, AplicacionSearchPage.NAME, aplicacionSearchPageVO);
			}
				
			// Llamada al servicio
			aplicacionSearchPageVO = SweServiceLocator.getSweService().getAplicacionSearchPageResult(userSession, aplicacionSearchPageVO);			

			// Tiene errores recuperables
			if (aplicacionSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + aplicacionSearchPageVO.infoString()); 
				saveDemodaErrors(request, aplicacionSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, AplicacionSearchPage.NAME, aplicacionSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (aplicacionSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + aplicacionSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, AplicacionSearchPage.NAME, aplicacionSearchPageVO);
			}
			
			// Envio el VO al request
			request.setAttribute(AplicacionSearchPage.NAME, aplicacionSearchPageVO);
			// Nuleo el list result
			//aplicacionSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(AplicacionSearchPage.NAME, aplicacionSearchPageVO);
			
			return mapping.findForward(SweSegConstants.FWD_APLICACION_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		SweUserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			baseVer(mapping, userSession, funcName);
			
			return mapping.findForward(SweSegConstants.ACTION_ADMINISTRAR_APLICACION);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		SweUserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {

			baseAgregar(mapping, userSession, funcName);
			
			return mapping.findForward(SweSegConstants.ACTION_ADMINISTRAR_APLICACION);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		SweUserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);

		try {

			baseModificar(mapping, userSession, funcName);	
			
			return mapping.findForward(SweSegConstants.ACTION_ADMINISTRAR_APLICACION);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		SweUserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);

		try {
			
			baseEliminar(mapping, userSession, funcName);
			
			return mapping.findForward(SweSegConstants.ACTION_ADMINISTRAR_APLICACION);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		
		return baseSeleccionar(mapping, form, request, response, funcName, AplicacionSearchPage.NAME);
	}	

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		return baseVolver(mapping, form, request, response, AplicacionSearchPage.NAME);
	}


	// va a la busqueda de usuarios de la aplicacion
	public ActionForward buscarUsrApl(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		SweUserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);

		try {

			baseBuscar(mapping, userSession, funcName);	
			
			return mapping.findForward(SweSegConstants.ACTION_BUSCAR_USRAPL);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	
	public ActionForward buscarModApl(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		try {
			SweUserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);

			baseBuscar(mapping, userSession, funcName);	

			return mapping.findForward(SweSegConstants.ACTION_BUSCAR_MODAPL);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}

	}
	
	
	//	 va a la busqueda de roles de la aplicacion
	public ActionForward buscarRolApl(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		SweUserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);

		try {

			baseBuscar(mapping, userSession, funcName);	
			
			return mapping.findForward(SweSegConstants.ACTION_BUSCAR_ROLAPL);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}
	
	//	 va a la busqueda de items de menu roots de la aplicacion
	public ActionForward buscarItemMenuRoot(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		SweUserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);

		try {

			baseBuscar(mapping, userSession, funcName);	
			
			return mapping.findForward(SweSegConstants.ACTION_BUSCAR_ITEM_MENU);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}
}
