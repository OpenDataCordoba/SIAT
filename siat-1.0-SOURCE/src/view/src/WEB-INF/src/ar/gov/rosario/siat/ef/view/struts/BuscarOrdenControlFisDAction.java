//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.ef.iface.model.OrdenControlFisSearchPage;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.NavModel;

public final class BuscarOrdenControlFisDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarOrdenControlFisDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ADM_ORDENCONTROLFIS, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			OrdenControlFisSearchPage ordenControlFisSearchPageVO = EfServiceLocator.getFiscalizacionService().getOrdenControlFisSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (ordenControlFisSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ordenControlFisSearchPageVO.infoString()); 
				saveDemodaErrors(request, ordenControlFisSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, OrdenControlFisSearchPage.NAME, ordenControlFisSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (ordenControlFisSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + ordenControlFisSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, OrdenControlFisSearchPage.NAME, ordenControlFisSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , OrdenControlFisSearchPage.NAME, ordenControlFisSearchPageVO);
			
			return mapping.findForward(EfConstants.FWD_ORDENCONTROLFIS_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OrdenControlFisSearchPage.NAME);
		}
	}

	public ActionForward buscarPersona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			OrdenControlFisSearchPage altaOficioAdapterVO = (OrdenControlFisSearchPage) userSession.get(OrdenControlFisSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (altaOficioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + OrdenControlFisSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OrdenControlFisSearchPage.NAME); 
			}
			
            // Tiene errores recuperables
			if (altaOficioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + altaOficioAdapterVO.infoString()); 
				saveDemodaErrors(request, altaOficioAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, OrdenControlFisSearchPage.NAME, altaOficioAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(OrdenControlFisSearchPage.NAME, altaOficioAdapterVO);
			// Subo el apdater al userSession
			userSession.put(OrdenControlFisSearchPage.NAME, altaOficioAdapterVO);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OrdenControlFisSearchPage.NAME);
		}
		
		return forwardSeleccionar(mapping, request, EfConstants.METOD_PARAM_PERSONA, PadConstants.ACTION_BUSCAR_PERSONA, false);		
	}
	
	public ActionForward paramPersona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			NavModel navModel = userSession.getNavModel();
			
			try {
				
				//bajo el adapter del usserSession
				OrdenControlFisSearchPage ordenControlFisSearchPage =  (OrdenControlFisSearchPage) userSession.get(OrdenControlFisSearchPage.NAME);
				
				// Si es nulo no se puede continuar
				if (ordenControlFisSearchPage == null) {
					log.error("error en: "  + funcName + ": " + OrdenControlFisSearchPage.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, OrdenControlFisSearchPage.NAME); 
				}

				// recupero el id seleccionado por el usuario
				String selectedId = navModel.getSelectedId();
				
				// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
				if (StringUtil.isNullOrEmpty(selectedId)) {
					// Envio el VO al request
					request.setAttribute(OrdenControlFisSearchPage.NAME, ordenControlFisSearchPage);
					return mapping.findForward(EfConstants.FWD_ORDENCONTROLFIS_SEARCHPAGE); 
				}
				
				// Se carga el idPersona seleccionado, en el adapter
				ordenControlFisSearchPage.getOrdenControl().getContribuyente().getPersona().setId(new Long(selectedId));
				
				// llamo al param del servicio
				ordenControlFisSearchPage = EfServiceLocator.getFiscalizacionService().getOrdenControlFisSearchPageParamPersona(userSession, ordenControlFisSearchPage);

	            // Tiene errores recuperables
				if (ordenControlFisSearchPage.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + ordenControlFisSearchPage.infoString()); 
					saveDemodaErrors(request, ordenControlFisSearchPage);
					return forwardErrorRecoverable(mapping, request, userSession, 
							OrdenControlFisSearchPage.NAME, ordenControlFisSearchPage);
				}
				
				// Tiene errores no recuperables
				if (ordenControlFisSearchPage.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + ordenControlFisSearchPage.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
							OrdenControlFisSearchPage.NAME, ordenControlFisSearchPage);
				}

				// grabo los mensajes si hubiere
				saveDemodaMessages(request, ordenControlFisSearchPage);

				// Envio el VO al request
				request.setAttribute(OrdenControlFisSearchPage.NAME, ordenControlFisSearchPage);
				// Subo el apdater al userSession
				userSession.put(OrdenControlFisSearchPage.NAME, ordenControlFisSearchPage);

				return mapping.findForward(EfConstants.FWD_ORDENCONTROLFIS_SEARCHPAGE);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, OrdenControlFisSearchPage.NAME);
			}
	}
		
	public ActionForward buscarInspector(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ADM_ORDENCONTROLFIS, BaseSecurityConstants.BUSCAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			OrdenControlFisSearchPage ordenControlFisSearchPage = (OrdenControlFisSearchPage) userSession.get(OrdenControlFisSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (ordenControlFisSearchPage == null) {
				log.error("error en: "  + funcName + ": " + OrdenControlFisSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OrdenControlFisSearchPage.NAME); 
			}

            // Tiene errores recuperables
			if (ordenControlFisSearchPage.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ordenControlFisSearchPage.infoString()); 
				saveDemodaErrors(request, ordenControlFisSearchPage);
				return forwardErrorRecoverable(mapping, request, userSession, OrdenControlFisSearchPage.NAME, ordenControlFisSearchPage);
			}
			
			// Envio el VO al request
			request.setAttribute(OrdenControlFisSearchPage.NAME, ordenControlFisSearchPage);
			// Subo el apdater al userSession
			userSession.put(OrdenControlFisSearchPage.NAME, ordenControlFisSearchPage);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OrdenControlFisSearchPage.NAME);
		}
		
		return forwardSeleccionar(mapping, request, PadConstants.METOD_PARAM_INSPECTOR, PadConstants.ACTION_BUSCAR_INSPECTOR, false);		
	}
	
	public ActionForward paramInspector(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			NavModel navModel = userSession.getNavModel();
			
			try {
				
				//bajo el adapter del usserSession
				OrdenControlFisSearchPage ordenControlFisSearchPage =  (OrdenControlFisSearchPage) userSession.get(OrdenControlFisSearchPage.NAME);
				
				// Si es nulo no se puede continuar
				if (ordenControlFisSearchPage == null) {
					log.error("error en: "  + funcName + ": " + OrdenControlFisSearchPage.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, OrdenControlFisSearchPage.NAME); 
				}

				// recupero el id seleccionado por el usuario
				String selectedId = navModel.getSelectedId();
				
				// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
				if (StringUtil.isNullOrEmpty(selectedId)) {
					// Envio el VO al request
					request.setAttribute(OrdenControlFisSearchPage.NAME, ordenControlFisSearchPage);
					return mapping.findForward(EfConstants.FWD_ORDENCONTROLFIS_SEARCHPAGE); 
				}
				
				// Se carga el idInspector seleccionado, en el adapter
				ordenControlFisSearchPage.getOrdenControl().getInspector().setId(new Long(selectedId));
				
				// llamo al param del servicio
				ordenControlFisSearchPage = EfServiceLocator.getFiscalizacionService().getOrdenControlFisSearchPageParamInspector(userSession, ordenControlFisSearchPage);

	            // Tiene errores recuperables
				if (ordenControlFisSearchPage.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + ordenControlFisSearchPage.infoString()); 
					saveDemodaErrors(request, ordenControlFisSearchPage);
					return forwardErrorRecoverable(mapping, request, userSession, 
							OrdenControlFisSearchPage.NAME, ordenControlFisSearchPage);
				}
				
				// Tiene errores no recuperables
				if (ordenControlFisSearchPage.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + ordenControlFisSearchPage.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
							OrdenControlFisSearchPage.NAME, ordenControlFisSearchPage);
				}

				// grabo los mensajes si hubiere
				saveDemodaMessages(request, ordenControlFisSearchPage);

				// Envio el VO al request
				request.setAttribute(OrdenControlFisSearchPage.NAME, ordenControlFisSearchPage);
				// Subo el apdater al userSession
				userSession.put(OrdenControlFisSearchPage.NAME, ordenControlFisSearchPage);

				return mapping.findForward(EfConstants.FWD_ORDENCONTROLFIS_SEARCHPAGE);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, OrdenControlFisSearchPage.NAME);
			}
	}
		
	public ActionForward paramOrigenOrden(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			
			try {
				
				//bajo el adapter del usserSession
				OrdenControlFisSearchPage ordenControlFisSearchPage =  (OrdenControlFisSearchPage) userSession.get(OrdenControlFisSearchPage.NAME);
				
				// Si es nulo no se puede continuar
				if (ordenControlFisSearchPage == null) {
					log.error("error en: "  + funcName + ": " + OrdenControlFisSearchPage.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, OrdenControlFisSearchPage.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(ordenControlFisSearchPage, request);

				// llamo al param del servicio
				ordenControlFisSearchPage = EfServiceLocator.getFiscalizacionService().getOrdenControlFisSearchPageParamOrigenOrden(userSession, ordenControlFisSearchPage);

	            // Tiene errores recuperables
				if (ordenControlFisSearchPage.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + ordenControlFisSearchPage.infoString()); 
					saveDemodaErrors(request, ordenControlFisSearchPage);
					return forwardErrorRecoverable(mapping, request, userSession, 
							OrdenControlFisSearchPage.NAME, ordenControlFisSearchPage);
				}
				
				// Tiene errores no recuperables
				if (ordenControlFisSearchPage.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + ordenControlFisSearchPage.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
							OrdenControlFisSearchPage.NAME, ordenControlFisSearchPage);
				}

				// grabo los mensajes si hubiere
				saveDemodaMessages(request, ordenControlFisSearchPage);

				// Envio el VO al request
				request.setAttribute(OrdenControlFisSearchPage.NAME, ordenControlFisSearchPage);
				// Subo el apdater al userSession
				userSession.put(OrdenControlFisSearchPage.NAME, ordenControlFisSearchPage);

				return mapping.findForward(EfConstants.FWD_ORDENCONTROLFIS_SEARCHPAGE);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, OrdenControlFisSearchPage.NAME);
			}
	}

	public ActionForward buscarSupervisor(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ADM_ORDENCONTROLFIS, BaseSecurityConstants.BUSCAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			OrdenControlFisSearchPage ordenControlFisSearchPage = (OrdenControlFisSearchPage) userSession.get(OrdenControlFisSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (ordenControlFisSearchPage == null) {
				log.error("error en: "  + funcName + ": " + OrdenControlFisSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OrdenControlFisSearchPage.NAME); 
			}

            // Tiene errores recuperables
			if (ordenControlFisSearchPage.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ordenControlFisSearchPage.infoString()); 
				saveDemodaErrors(request, ordenControlFisSearchPage);
				return forwardErrorRecoverable(mapping, request, userSession, OrdenControlFisSearchPage.NAME, ordenControlFisSearchPage);
			}
			
			// Envio el VO al request
			request.setAttribute(OrdenControlFisSearchPage.NAME, ordenControlFisSearchPage);
			// Subo el apdater al userSession
			userSession.put(OrdenControlFisSearchPage.NAME, ordenControlFisSearchPage);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OrdenControlFisSearchPage.NAME);
		}
		
		return forwardSeleccionar(mapping, request, PadConstants.METOD_PARAM_SUPERVISOR, PadConstants.ACTION_BUSCAR_SUPERVISOR, false);		
	}
	
	public ActionForward paramSupervisor(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			NavModel navModel = userSession.getNavModel();
			
			try {
				
				//bajo el adapter del usserSession
				OrdenControlFisSearchPage ordenControlFisSearchPage =  (OrdenControlFisSearchPage) userSession.get(OrdenControlFisSearchPage.NAME);
				
				// Si es nulo no se puede continuar
				if (ordenControlFisSearchPage == null) {
					log.error("error en: "  + funcName + ": " + OrdenControlFisSearchPage.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, OrdenControlFisSearchPage.NAME); 
				}

				// recupero el id seleccionado por el usuario
				String selectedId = navModel.getSelectedId();
				
				// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
				if (StringUtil.isNullOrEmpty(selectedId)) {
					// Envio el VO al request
					request.setAttribute(OrdenControlFisSearchPage.NAME, ordenControlFisSearchPage);
					return mapping.findForward(EfConstants.FWD_ORDENCONTROLFIS_SEARCHPAGE); 
				}
				
				// Se carga el idInspector seleccionado, en el adapter
				ordenControlFisSearchPage.getOrdenControl().getSupervisor().setId(new Long(selectedId));
				
				// llamo al param del servicio
				ordenControlFisSearchPage = EfServiceLocator.getFiscalizacionService().getOrdenControlFisSearchPageParamSupervisor(userSession, ordenControlFisSearchPage);

	            // Tiene errores recuperables
				if (ordenControlFisSearchPage.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + ordenControlFisSearchPage.infoString()); 
					saveDemodaErrors(request, ordenControlFisSearchPage);
					return forwardErrorRecoverable(mapping, request, userSession, 
							OrdenControlFisSearchPage.NAME, ordenControlFisSearchPage);
				}
				
				// Tiene errores no recuperables
				if (ordenControlFisSearchPage.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + ordenControlFisSearchPage.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
							OrdenControlFisSearchPage.NAME, ordenControlFisSearchPage);
				}

				// grabo los mensajes si hubiere
				saveDemodaMessages(request, ordenControlFisSearchPage);

				// Envio el VO al request
				request.setAttribute(OrdenControlFisSearchPage.NAME, ordenControlFisSearchPage);
				// Subo el apdater al userSession
				userSession.put(OrdenControlFisSearchPage.NAME, ordenControlFisSearchPage);

				return mapping.findForward(EfConstants.FWD_ORDENCONTROLFIS_SEARCHPAGE);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, OrdenControlFisSearchPage.NAME);
			}
	}
		


	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, OrdenControlFisSearchPage.NAME);
		
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
			OrdenControlFisSearchPage ordenControlFisSearchPageVO = (OrdenControlFisSearchPage) userSession.get(OrdenControlFisSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (ordenControlFisSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + OrdenControlFisSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OrdenControlFisSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(ordenControlFisSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				ordenControlFisSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (ordenControlFisSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ordenControlFisSearchPageVO.infoString()); 
				saveDemodaErrors(request, ordenControlFisSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, OrdenControlFisSearchPage.NAME, ordenControlFisSearchPageVO);
			}
				
			// Llamada al servicio	
			ordenControlFisSearchPageVO = EfServiceLocator.getFiscalizacionService().getOrdenControlFisSearchPageResult(userSession, ordenControlFisSearchPageVO);			

			// Tiene errores recuperables
			if (ordenControlFisSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ordenControlFisSearchPageVO.infoString()); 
				saveDemodaErrors(request, ordenControlFisSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, OrdenControlFisSearchPage.NAME, ordenControlFisSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (ordenControlFisSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + ordenControlFisSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, OrdenControlFisSearchPage.NAME, ordenControlFisSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(OrdenControlFisSearchPage.NAME, ordenControlFisSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(OrdenControlFisSearchPage.NAME, ordenControlFisSearchPageVO);
			
			return mapping.findForward(EfConstants.FWD_ORDENCONTROLFIS_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OrdenControlFisSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_ORDENCONTROLFIS);

	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_ORDENCONTROLFIS);

	}

	public ActionForward administrar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardSearchPage(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_ORDENCONTROLFIS, EfConstants.ACT_ADMINISTRAR);

		}
	
	public ActionForward asignarOrdenInit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardSearchPage(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_ORDENCONTROLFIS, EfConstants.ACT_ASIGNAR_ORDENCONTROLFISINIT);

		}


	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, OrdenControlFisSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, OrdenControlFisSearchPage.NAME);
		
	}
		
	public ActionForward validarCaso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			OrdenControlFisSearchPage adapterVO = (OrdenControlFisSearchPage)userSession.get(OrdenControlFisSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (adapterVO == null) {
				log.error("error en: "  + funcName + ": " + OrdenControlFisSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OrdenControlFisSearchPage.NAME); 
			}
			
			// Populate como en un buscar
			DemodaUtil.populateVO(adapterVO, request);
			
			log.debug( funcName + " " + adapterVO.getOrdenControl().getCaso().infoString());
			
			// llamada al servicio
			CasServiceLocator.getCasCasoService().validarCaso(userSession, adapterVO.getOrdenControl()); 
			
			adapterVO.getOrdenControl().passErrorMessages(adapterVO);
		    
		    saveDemodaMessages(request, adapterVO);
		    saveDemodaErrors(request, adapterVO);
		    
			request.setAttribute(OrdenControlFisSearchPage.NAME, adapterVO);
			
			return mapping.findForward( EfConstants.FWD_ORDENCONTROLFIS_SEARCHPAGE); 
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OrdenControlFisSearchPage.NAME);
		}	
	}
	
}
