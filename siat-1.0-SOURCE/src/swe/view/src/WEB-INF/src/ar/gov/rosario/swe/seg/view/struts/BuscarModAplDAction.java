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
import ar.gov.rosario.swe.iface.model.ModAplSearchPage;
import ar.gov.rosario.swe.iface.model.SweUserSession;
import ar.gov.rosario.swe.iface.util.SweSecurityConstants;
import ar.gov.rosario.swe.seg.view.util.SweSegConstants;
import ar.gov.rosario.swe.view.util.SweUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class BuscarModAplDAction extends SweBaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarModAplDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		String act = getCurrentAct(request);
		SweUserSession userSession = canAccess(request, mapping, SweSecurityConstants.ABM_MODULOS, act); 
		if (userSession == null) return forwardErrorSession(request);
				
		try {	
			// tomo el id de aplicacion del userSession
			String selectedId = userSession.getNavModel().getSelectedId();
			CommonKey aplicacionKey = new CommonKey (selectedId); 
			
			ModAplSearchPage modAplSearchPageVO = 
				SweServiceLocator.getSweService().getModAplSearchPageInit(userSession, aplicacionKey);
			
			// Tiene errores recuperables
			if (modAplSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + modAplSearchPageVO.infoString()); 
				saveDemodaErrors(request, modAplSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ModAplSearchPage.NAME, modAplSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (modAplSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + modAplSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ModAplSearchPage.NAME, modAplSearchPageVO);
			}
			
			modAplSearchPageVO.setPageNumber(new Long(1));

			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , ModAplSearchPage.NAME, modAplSearchPageVO);
			
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
			userSession.getNavModel().setAct(funcName);
			
			// Bajo el searchPage del userSession
			ModAplSearchPage modAplSearchPageVO = (ModAplSearchPage) userSession.get(ModAplSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (modAplSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + ModAplSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ModAplSearchPage.NAME); 
			}

			// si el buscar diparado desde la pagina de busqueda			
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(modAplSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				modAplSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}			

            // Tiene errores recuperables
			if (modAplSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + modAplSearchPageVO.infoString()); 
				saveDemodaErrors(request, modAplSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ModAplSearchPage.NAME, modAplSearchPageVO);
			}
				
			// Llamada al servicio	
			modAplSearchPageVO = SweServiceLocator.getSweService().getModAplSearchPageResult(userSession, modAplSearchPageVO);			

			// Tiene errores recuperables
			if (modAplSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + modAplSearchPageVO.infoString()); 
				saveDemodaErrors(request, modAplSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ModAplSearchPage.NAME, modAplSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (modAplSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + modAplSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, ModAplSearchPage.NAME, modAplSearchPageVO);
			}
			
			// Envio el VO al request
			request.setAttribute(ModAplSearchPage.NAME, modAplSearchPageVO);
			// Nuleo el list result
			//modAplSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(ModAplSearchPage.NAME, modAplSearchPageVO);
			
			return mapping.findForward(SweSegConstants.FWD_MODAPL_SEARCHPAGE);
			
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
			
			return mapping.findForward(SweSegConstants.ACTION_ADMINISTRAR_MODAPL);
			
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
			
			// recupero el navModel inicial para obtener el id de aplicacion
			NavModel navModelUS = (NavModel) userSession.get(mapping.getPath());
			String idAplicacion = navModelUS.getSelectedId();
			
			baseAgregar(mapping, userSession, funcName, idAplicacion);

			return mapping.findForward(SweSegConstants.ACTION_ADMINISTRAR_MODAPL);
			
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
			
			return mapping.findForward(SweSegConstants.ACTION_ADMINISTRAR_MODAPL);
			
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
			
			return mapping.findForward(SweSegConstants.ACTION_ADMINISTRAR_MODAPL);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = SweUtil.currentMethodName();
		
		return baseSeleccionar(mapping, form, request, response, funcName, ModAplSearchPage.NAME);
	}	

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ModAplSearchPage.NAME);
	}
	
	public ActionForward buscarAccModApl(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		System.out.println("selectedId: " + request.getParameter("selectedId"));

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		SweUserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);

		try {
			log.debug("selectedId = " + userSession.getNavModel().getSelectedId());
			baseBuscar(mapping, userSession, funcName);	
			log.debug("BuscarModAplDAction.inicializa: selectedId = " + userSession.getNavModel().getSelectedId());			

			return mapping.findForward(SweSegConstants.ACTION_BUSCAR_ACCMODAPL);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}

	}
	
	
}
