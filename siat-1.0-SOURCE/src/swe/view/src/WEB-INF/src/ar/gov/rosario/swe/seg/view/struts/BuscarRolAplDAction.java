//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.seg.view.struts;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.swe.SweServiceLocator;
import ar.gov.rosario.swe.base.view.struts.SweBaseDispatchAction;
import ar.gov.rosario.swe.iface.model.RolAplSearchPage;
import ar.gov.rosario.swe.iface.model.SweUserSession;
import ar.gov.rosario.swe.iface.util.SweSecurityConstants;
import ar.gov.rosario.swe.seg.view.util.SweSegConstants;
import ar.gov.rosario.swe.view.util.SweUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;
import coop.tecso.demoda.iface.model.PageModel;

public final class BuscarRolAplDAction extends SweBaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarRolAplDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String act = getCurrentAct(request);
		SweUserSession userSession = canAccess(request, mapping, SweSecurityConstants.ABM_ROLES, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// tomo el id de aplicacion del userSession
			String selectedId = userSession.getNavModel().getSelectedId();
			CommonKey aplicacionKey = new CommonKey (selectedId); 
			
			RolAplSearchPage rolAplSearchPageVO = 
				SweServiceLocator.getSweService().getRolAplSearchPageInit(userSession, aplicacionKey);
			
			// Tiene errores recuperables
			if (rolAplSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + rolAplSearchPageVO.infoString()); 
				saveDemodaErrors(request, rolAplSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, RolAplSearchPage.NAME, rolAplSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (rolAplSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + rolAplSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, RolAplSearchPage.NAME, rolAplSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , RolAplSearchPage.NAME, rolAplSearchPageVO);

			rolAplSearchPageVO.setPageNumber(new Long(1));
			return buscar(mapping, form, request, response);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}
	
	public ActionForward inicializarForCreateUsrRolApl(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = SweUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");
			
			String act = getCurrentAct(request);
			SweUserSession userSession = canAccess(request, mapping, SweSecurityConstants.ABM_ROLES, act); 
			if (userSession == null) return forwardErrorSession(request);
			
			try {
				
				// tomo el id del usuario de aplicacion del userSession
				String selectedId = userSession.getNavModel().getSelectedId();
				CommonKey usrAplCommonKey = new CommonKey (selectedId); 
				
				RolAplSearchPage rolAplSearchPageVO = 
					SweServiceLocator.getSweService().getRolAplSearchPageInitForCreateUsrRolApl(userSession, usrAplCommonKey);
				
				rolAplSearchPageVO.setInactivo(true); // para que seleccione en vez de ver
				rolAplSearchPageVO.setABMEnabled(PageModel.DISABLED); // para que no se visualice el insertar, modificar y eliminar
				// Tiene errores recuperables
				if (rolAplSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + rolAplSearchPageVO.infoString()); 
					saveDemodaErrors(request, rolAplSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, RolAplSearchPage.NAME, rolAplSearchPageVO);
				} 

				// Tiene errores no recuperables
				if (rolAplSearchPageVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + rolAplSearchPageVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, RolAplSearchPage.NAME, rolAplSearchPageVO);
				}
				
				// Si no tiene error
				userSession.getNavModel().setPrevAction("/seg/AdministrarUsrRolApl"); // para que vuelva al insertar 
				userSession.getNavModel().setPrevActionParameter("agregar");
				
				// reemplazo al baseInicializarSearchPage
				pasarNavModelActualAHistorial(mapping, funcName, userSession);
				
				// Envio el VO al request
				request.setAttribute(RolAplSearchPage.NAME, rolAplSearchPageVO);
				// Vacio el list result
				rolAplSearchPageVO.setListResult(new ArrayList());
				// Subo en el el searchPage al userSession
				userSession.put(RolAplSearchPage.NAME, rolAplSearchPageVO);

				return mapping.findForward(SweSegConstants.FWD_ROLAPL_SEARCHPAGE);
				
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
			RolAplSearchPage rolAplSearchPageVO = (RolAplSearchPage) userSession.get(RolAplSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (rolAplSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + RolAplSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, RolAplSearchPage.NAME); 
			}

			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(rolAplSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				rolAplSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
			
            // Tiene errores recuperables
			if (rolAplSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + rolAplSearchPageVO.infoString()); 
				saveDemodaErrors(request, rolAplSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, RolAplSearchPage.NAME, rolAplSearchPageVO);
			}
				
			// Llamada al servicio	
			rolAplSearchPageVO = SweServiceLocator.getSweService().getRolAplSearchPageResult(userSession, rolAplSearchPageVO);			

			// Tiene errores recuperables
			if (rolAplSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + rolAplSearchPageVO.infoString()); 
				saveDemodaErrors(request, rolAplSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, RolAplSearchPage.NAME, rolAplSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (rolAplSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + rolAplSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, RolAplSearchPage.NAME, rolAplSearchPageVO);
			}
			
			// Envio el VO al request
			request.setAttribute(RolAplSearchPage.NAME, rolAplSearchPageVO);
			// Nuleo el list result
			//rolAplSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(RolAplSearchPage.NAME, rolAplSearchPageVO);
			
			return mapping.findForward(SweSegConstants.FWD_ROLAPL_SEARCHPAGE);
			
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
			
			return mapping.findForward(SweSegConstants.ACTION_ADMINISTRAR_ROLAPL);
			
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

			return mapping.findForward(SweSegConstants.ACTION_ADMINISTRAR_ROLAPL);
			
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
			
			return mapping.findForward(SweSegConstants.ACTION_ADMINISTRAR_ROLAPL);
			
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
			
			return mapping.findForward(SweSegConstants.ACTION_ADMINISTRAR_ROLAPL);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = SweUtil.currentMethodName();
		
		return baseSeleccionar(mapping, form, request, response, funcName, RolAplSearchPage.NAME);
	}	

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, RolAplSearchPage.NAME);
	}
	
	
	//	va a la busqueda de Acciones Modulo del Rol de la Aplicacion
	public ActionForward buscarRolAccModApl(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		SweUserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);

		try {

			baseBuscar(mapping, userSession, funcName);	
			
			return mapping.findForward(SweSegConstants.ACTION_BUSCAR_ROLACCMODAPL);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}
}
