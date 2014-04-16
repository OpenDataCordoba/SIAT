//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.cas.iface.model.SolicitudSearchPage;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.cas.iface.util.CasSecurityConstants;
import ar.gov.rosario.siat.cas.view.util.CasConstants;
import ar.gov.rosario.siat.pad.iface.model.CuentaSearchPage;
import ar.gov.rosario.siat.pad.view.struts.BuscarCuentaDAction;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import ar.gov.rosario.siat.seg.iface.model.MenuAdapter;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.NavModel;

public final class BuscarSolicitudDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarSolicitudDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, CasSecurityConstants.ABM_SOLICITUD, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			SolicitudSearchPage solicitudSearchPageVO = CasServiceLocator.getSolicitudService().getSolicitudSearchPageInit(userSession);
		        

			// Tiene errores recuperables
			if (solicitudSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + solicitudSearchPageVO.infoString()); 
				saveDemodaErrors(request, solicitudSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, SolicitudSearchPage.NAME, solicitudSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (solicitudSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + solicitudSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SolicitudSearchPage.NAME, solicitudSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , SolicitudSearchPage.NAME, solicitudSearchPageVO);
			
			return mapping.findForward(CasConstants.FWD_SOLICITUD_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SolicitudSearchPage.NAME);
		}
	}

	public ActionForward irPendientesArea(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		UserSession userSession = canAccess(request, mapping, CasSecurityConstants.ABM_SOLICITUD, BaseConstants.ACT_VER); 
		if (userSession==null) return forwardErrorSession(request);
          
		// Crea el searchPage
		SolicitudSearchPage solicitudSearchPageVO = new SolicitudSearchPage();
		solicitudSearchPageVO.setPaged(true);
		solicitudSearchPageVO.setPageNumber(1L);
		solicitudSearchPageVO.setRecsByPage(10L);
		solicitudSearchPageVO.setEstaEnSolPendiente(true);
		solicitudSearchPageVO.setEstaEnSolEmitidas(false);
		solicitudSearchPageVO.setDesAreaLog(userSession.getDesArea());   
		// Envio el VO al request
		request.setAttribute(SolicitudSearchPage.NAME, solicitudSearchPageVO);
		// Subo en el el searchPage al userSession
		userSession.put(SolicitudSearchPage.NAME, solicitudSearchPageVO);

		// actualiza en la session el id del menu activo
		userSession.setIdMenuSession(MenuAdapter.ID_MENU_SOLPENDIENTES);
		
		//busca las solicitudes
		return getPendientesArea(mapping, form, request, response);
	}
	
	public ActionForward irEmitidasArea(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		UserSession userSession = canAccess(request, mapping, CasSecurityConstants.ABM_SOLICITUD, BaseConstants.ACT_VER); 
		if (userSession==null) return forwardErrorSession(request);

		// Crea el searchPage
		SolicitudSearchPage solicitudSearchPageVO = new SolicitudSearchPage();
		solicitudSearchPageVO.setPaged(true);
		solicitudSearchPageVO.setPageNumber(1L);
		solicitudSearchPageVO.setRecsByPage(10L);
		solicitudSearchPageVO.setEstaEnSolPendiente(false);
		solicitudSearchPageVO.setEstaEnSolEmitidas(true);

		
		// Envio el VO al request
		request.setAttribute(SolicitudSearchPage.NAME, solicitudSearchPageVO);
		// Subo en el el searchPage al userSession
		userSession.put(SolicitudSearchPage.NAME, solicitudSearchPageVO);

		// actualiza en la session el id del menu activo
		userSession.setIdMenuSession(MenuAdapter.ID_MENU_SOLEMITIDAS);

		//busca las solicitudes
		return getEmitidasArea(mapping, form, request, response);
	}
	
	public ActionForward getPendientesArea(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");

			UserSession userSession = canAccess(request, mapping, CasSecurityConstants.ABM_SOLICITUD, BaseConstants.ACT_VER); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				
				// Baja el searchPage de la session
				SolicitudSearchPage solicitudSearchPageVO = (SolicitudSearchPage) userSession.get(SolicitudSearchPage.NAME);

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(solicitudSearchPageVO, request);
				
				solicitudSearchPageVO = CasServiceLocator.getSolicitudService().getSolicitudSearchPagePendArea(userSession, solicitudSearchPageVO);
				
				// Tiene errores recuperables
				if (solicitudSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + solicitudSearchPageVO.infoString()); 
					saveDemodaErrors(request, solicitudSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, SolicitudSearchPage.NAME, solicitudSearchPageVO);
				} 

				// Tiene errores no recuperables
				if (solicitudSearchPageVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + solicitudSearchPageVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, SolicitudSearchPage.NAME, solicitudSearchPageVO);
				}
								
				// Envio el VO al request
				request.setAttribute(SolicitudSearchPage.NAME, solicitudSearchPageVO);
				// Subo en el el searchPage al userSession
				userSession.put(SolicitudSearchPage.NAME, solicitudSearchPageVO);

				return mapping.findForward(CasConstants.FWD_SOLICITUD_PEND_AREA);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, SolicitudSearchPage.NAME);
			}
	}
	
	
	public ActionForward getEmitidasArea(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");

			UserSession userSession = canAccess(request, mapping, CasSecurityConstants.ABM_SOLICITUD, BaseConstants.ACT_VER); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				
				// Baja el searchPage de la session
				SolicitudSearchPage solicitudSearchPageVO = (SolicitudSearchPage) userSession.get(SolicitudSearchPage.NAME);

				solicitudSearchPageVO = CasServiceLocator.getSolicitudService().getSolicitudSearchPageEmitidasArea(userSession, solicitudSearchPageVO);
				
				// Tiene errores recuperables
				if (solicitudSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + solicitudSearchPageVO.infoString()); 
					saveDemodaErrors(request, solicitudSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, SolicitudSearchPage.NAME, solicitudSearchPageVO);
				} 

				// Tiene errores no recuperables
				if (solicitudSearchPageVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + solicitudSearchPageVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, SolicitudSearchPage.NAME, solicitudSearchPageVO);
				}
								
				// Envio el VO al request
				request.setAttribute(SolicitudSearchPage.NAME, solicitudSearchPageVO);
				// Subo en el el searchPage al userSession
				userSession.put(SolicitudSearchPage.NAME, solicitudSearchPageVO);

				return mapping.findForward(CasConstants.FWD_SOLICITUD_EMITIDAS_AREA);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, SolicitudSearchPage.NAME);
			}
	}
	
	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, SolicitudSearchPage.NAME);
		
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
			SolicitudSearchPage solicitudSearchPageVO = (SolicitudSearchPage) userSession.get(SolicitudSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (solicitudSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + SolicitudSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SolicitudSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(solicitudSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				solicitudSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));				
			}
			
            // Tiene errores recuperables
			if (solicitudSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + solicitudSearchPageVO.infoString()); 
				saveDemodaErrors(request, solicitudSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, SolicitudSearchPage.NAME, solicitudSearchPageVO);
			}
				
			// Llamada al servicio	
			solicitudSearchPageVO = CasServiceLocator.getSolicitudService().getSolicitudSearchPageResult(userSession, solicitudSearchPageVO);			

			// Tiene errores recuperables
			if (solicitudSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + solicitudSearchPageVO.infoString()); 
				saveDemodaErrors(request, solicitudSearchPageVO);

				// Si esta en el mantenedor vuelve al searchPage sino a  la lista de pendientes o emitidas 
				if(solicitudSearchPageVO.getEstaEnSolPendiente()){
					// Envio el VO al request
					request.setAttribute(SolicitudSearchPage.NAME, solicitudSearchPageVO);
					// Subo en el el searchPage al userSession
					userSession.put(SolicitudSearchPage.NAME, solicitudSearchPageVO);
					return mapping.findForward(CasConstants.FWD_SOLICITUD_PEND_AREA);
				}else if(solicitudSearchPageVO.getEstaEnSolEmitidas()){
					// Envio el VO al request
					request.setAttribute(SolicitudSearchPage.NAME, solicitudSearchPageVO);
					// Subo en el el searchPage al userSession
					userSession.put(SolicitudSearchPage.NAME, solicitudSearchPageVO);
					return mapping.findForward(CasConstants.FWD_SOLICITUD_EMITIDAS_AREA);
				}
				
				
				return forwardErrorRecoverable(mapping, request, userSession, SolicitudSearchPage.NAME, solicitudSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (solicitudSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + solicitudSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, SolicitudSearchPage.NAME, solicitudSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(SolicitudSearchPage.NAME, solicitudSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(SolicitudSearchPage.NAME, solicitudSearchPageVO);
			
			// Si esta en el mantenedor vuelve al searchPage sino a  la lista de pendientes o emitidas 
			if(solicitudSearchPageVO.getEstaEnSolPendiente())
				return mapping.findForward(CasConstants.FWD_SOLICITUD_PEND_AREA);
			else if(solicitudSearchPageVO.getEstaEnSolEmitidas())
				return mapping.findForward(CasConstants.FWD_SOLICITUD_EMITIDAS_AREA);
			
			
			return mapping.findForward(CasConstants.FWD_SOLICITUD_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SolicitudSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		ActionForward forward = forwardSolArea(mapping, request, funcName, BaseConstants.ACT_VER);
		if(forward!=null)
			return forward;
		
		return forwardVerSearchPage(mapping, request, funcName, CasConstants.ACTION_ADMINISTRAR_SOLICITUD);

	}

	/**
	 * Devuelve el forward correspondiente para la parte de "Lista de solicitudes Pendientes del Area" y "Lista de solicitudes emitidas por el Area".
	 * si esta en el mantenedor, devuelve null
	 * @param mapping
	 * @param request
	 * @param funcName
	 * @param act
	 * @return
	 * @throws Exception
	 */
	private ActionForward forwardSolArea(ActionMapping mapping,HttpServletRequest request, String funcName, String act) throws Exception{
		UserSession userSession = canAccess(request, mapping, CasSecurityConstants.ABM_SOLICITUD, act);
		if (userSession==null) return forwardErrorSession(request);
		
		SolicitudSearchPage solicitudSearchPage = (SolicitudSearchPage) userSession.get(SolicitudSearchPage.NAME);
		
		if(solicitudSearchPage.getEstaEnSolPendiente()){
			// El usuario esta visualizando la lista de solicitudes del area, no en el mantenedor, setea el volver a la pag correspondiente
			return baseForward(mapping, request, funcName, CasConstants.ACT_SOLICITUD_PEND_AREA, CasConstants.ACTION_ADMINISTRAR_SOLICITUD, act);
		}
		
		if(solicitudSearchPage.getEstaEnSolEmitidas()){
			// El usuario esta visualizando la lista de solicitudes del area, no en el mantenedor, setea el volver a la pag correspondiente
			return baseForward(mapping, request, funcName, CasConstants.ACT_SOLICITUD_EMITIDAS_AREA, CasConstants.ACTION_ADMINISTRAR_SOLICITUD, act);
		}
		return null;
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarSearchPage(mapping, request, funcName, CasConstants.ACTION_ADMINISTRAR_SOLICITUD);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		ActionForward forward = forwardSolArea(mapping, request, funcName, BaseConstants.ACT_MODIFICAR);
		if(forward!=null)
			return forward;

		return forwardModificarSearchPage(mapping, request, funcName, CasConstants.ACTION_ADMINISTRAR_SOLICITUD);

	}

	public ActionForward buscarCuenta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());

		// Bajo el adapter del userSession
		SolicitudSearchPage solicitudSearchPage = (SolicitudSearchPage) userSession.get(SolicitudSearchPage.NAME);
		
		// Si es nulo no se puede continuar
		if (solicitudSearchPage == null) {
			log.error("error en: "  + funcName + ": " + SolicitudSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, SolicitudSearchPage.NAME); 
		}

		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(solicitudSearchPage, request);
				
        // Tiene errores recuperables
		if (solicitudSearchPage.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + solicitudSearchPage.infoString()); 
			saveDemodaErrors(request, solicitudSearchPage);

			request.setAttribute(SolicitudSearchPage.NAME, solicitudSearchPage);
			return mapping.getInputForward();
		}
		
		// Se crea el searchPage para ir a la busqueda de cuenta
		CuentaSearchPage cuentaFiltro = new CuentaSearchPage();
		
		// y el numero de cuenta
		cuentaFiltro.getCuentaTitular().getCuenta().
			setNumeroCuenta(solicitudSearchPage.getSolicitud().getCuenta().getNumeroCuenta());

		navModel.putParameter(BuscarCuentaDAction.CUENTA_SEARCHPAGE_FILTRO, cuentaFiltro);
		
		
		// Forwardeo a la Search Page de Cuenta
		return forwardSeleccionar(mapping, request, 
				"paramCuenta", PadConstants.ACTION_BUSCAR_CUENTA, false);
	}
	
	public ActionForward paramCuenta (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		NavModel navModel = userSession.getNavModel();
		
		try {
			
			//bajo el adapter del usserSession
			SolicitudSearchPage solicitudSearchPage =  (SolicitudSearchPage) userSession.get(SolicitudSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (solicitudSearchPage == null) {
				log.error("error en: "  + funcName + ": " + SolicitudSearchPage.NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SolicitudSearchPage.NAME); 
			}
			

			// recupero el id seleccionado por el usuario
			String selectedId = navModel.getSelectedId();
			
			// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
			if (StringUtil.isNullOrEmpty(selectedId)) {
				// Envio el VO al request
				request.setAttribute(SolicitudSearchPage.NAME, solicitudSearchPage);
				
				// Si esta en el mantenedor vuelve al searchPage sino a  la lista de pendientes o emitidas 
				if(solicitudSearchPage.getEstaEnSolPendiente())
					return mapping.findForward(CasConstants.FWD_SOLICITUD_PEND_AREA);
				else if(solicitudSearchPage.getEstaEnSolEmitidas())
					return mapping.findForward(CasConstants.FWD_SOLICITUD_EMITIDAS_AREA);

				return mapping.findForward(CasConstants.FWD_SOLICITUD_SEARCHPAGE); 
			}
			
			// Seteo el id de la cuenta
			solicitudSearchPage.getSolicitud().getCuenta().setId(new Long(selectedId));
			
			// Llamada al servicio
			solicitudSearchPage = CasServiceLocator.getSolicitudService().getSolicitudSearchPageParamCuenta(userSession, solicitudSearchPage);
			
            // Tiene errores recuperables
			if (solicitudSearchPage.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + solicitudSearchPage.infoString()); 
				saveDemodaErrors(request, solicitudSearchPage);
				return forwardErrorRecoverable(mapping, request, userSession, 
						SolicitudSearchPage.NAME, solicitudSearchPage);
			}
			
			// Tiene errores no recuperables
			if (solicitudSearchPage.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + solicitudSearchPage.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
						SolicitudSearchPage.NAME, solicitudSearchPage);
			}
						
			// Envio el VO al request
			request.setAttribute(SolicitudSearchPage.NAME, solicitudSearchPage);
			// Subo el apdater al userSession
			userSession.put(SolicitudSearchPage.NAME, solicitudSearchPage);
			 
			saveDemodaMessages(request, solicitudSearchPage);

			// Si esta en el mantenedor vuelve al searchPage sino a  la lista de pendientes o emitidas 
			if(solicitudSearchPage.getEstaEnSolPendiente())
				return mapping.findForward(CasConstants.FWD_SOLICITUD_PEND_AREA);
			else if(solicitudSearchPage.getEstaEnSolEmitidas())
				return mapping.findForward(CasConstants.FWD_SOLICITUD_EMITIDAS_AREA);

			return mapping.findForward(CasConstants.FWD_SOLICITUD_SEARCHPAGE);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SolicitudSearchPage.NAME);
		}
	}
	
	
	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, CasConstants.ACTION_ADMINISTRAR_SOLICITUD);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, CasConstants.ACTION_ADMINISTRAR_SOLICITUD);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, CasConstants.ACTION_ADMINISTRAR_SOLICITUD);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, SolicitudSearchPage.NAME);
		
	}

	public ActionForward cambiarEstado(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			ActionForward forward = forwardSolArea(mapping, request, funcName, CasConstants.ACT_CAMBIARESTADO_SOLICITUD);
			if(forward!=null)
				return forward;

			return baseForwardSearchPage(mapping, request, funcName, CasConstants.ACTION_ADMINISTRAR_SOLICITUD, CasConstants.ACT_CAMBIARESTADO_SOLICITUD);
			//return forwardVerSearchPage(mapping, request, funcName, CasConstants.ACTION_ADMINISTRAR_SOLICITUD);
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, SolicitudSearchPage.NAME);
		
	}
		
	
}
