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
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.ef.iface.model.ActaAdapter;
import ar.gov.rosario.siat.ef.iface.model.ActaVO;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarEncActaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEncActaDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_ACTA_ENC, act);		

		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ActaAdapter actaAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {    
			
			
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());

			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getActaAdapterForUpdate(userSession, commonKey)";
				actaAdapterVO = EfServiceLocator.getFiscalizacionService().getActaAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_ACTA_ENC_EDIT_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getActaAdapterForCreate(userSession)";
				actaAdapterVO = EfServiceLocator.getFiscalizacionService().getActaAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_ACTA_ENC_EDIT_ADAPTER);
			}
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (actaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + actaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ActaAdapter.ENC_NAME, actaAdapterVO);
			}
			
			// setea a que parte de la pantalla vuelve
			userSession.put("irA", "bloqueActas");

			// Seteo los valores de navegacion en el adapter
			actaAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ActaAdapter.ENC_NAME + ": "+ actaAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ActaAdapter.ENC_NAME, actaAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ActaAdapter.ENC_NAME, actaAdapterVO);

			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ActaAdapter.ENC_NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, 
			EfSecurityConstants.ABM_ACTA_ENC, BaseSecurityConstants.AGREGAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ActaAdapter actaAdapterVO = (ActaAdapter) userSession.get(ActaAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (actaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ActaAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ActaAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(actaAdapterVO, request);
			
            // Tiene errores recuperables
			if (actaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + actaAdapterVO.infoString()); 
				saveDemodaErrors(request, actaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ActaAdapter.ENC_NAME, actaAdapterVO);
			}
			
			// llamada al servicio
			ActaVO actaVO = EfServiceLocator.getFiscalizacionService().createActa(userSession, actaAdapterVO.getActa());
			
            // Tiene errores recuperables
			if (actaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + actaVO.infoString()); 
				saveDemodaErrors(request, actaVO);
				return forwardErrorRecoverable(mapping, request, userSession, ActaAdapter.ENC_NAME, actaAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (actaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + actaVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ActaAdapter.ENC_NAME, actaAdapterVO);
			}

			// Si tiene permiso lo dirijo al adapter de modificacion, 
			// sino vuelve al searchPage
			if (hasAccess(userSession, EfSecurityConstants.ABM_ACTA, BaseSecurityConstants.MODIFICAR)) {
				
				// seteo el id para que lo use el siguiente action 
				userSession.getNavModel().setSelectedId(actaVO.getId().toString());

				// lo dirijo al adapter de modificacion
				return forwardConfirmarOk(mapping, request, funcName, ActaAdapter.ENC_NAME, 
					EfConstants.PATH_ADMINISTRAR_ACTA, BaseConstants.METHOD_INICIALIZAR,
																			BaseConstants.ACT_MODIFICAR);
			} else {
				
				// lo dirijo al searchPage				
				return forwardConfirmarOk(mapping, request, funcName, ActaAdapter.ENC_NAME);
				
			}
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ActaAdapter.ENC_NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, 
			EfSecurityConstants.ABM_ACTA_ENC, BaseSecurityConstants.MODIFICAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ActaAdapter actaAdapterVO = (ActaAdapter) userSession.get(ActaAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (actaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ActaAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ActaAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(actaAdapterVO, request);
			
            // Tiene errores recuperables
			if (actaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + actaAdapterVO.infoString()); 
				saveDemodaErrors(request, actaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ActaAdapter.ENC_NAME, actaAdapterVO);
			}
			
			// llamada al servicio
			ActaVO actaVO = EfServiceLocator.getFiscalizacionService().updateActa(userSession, actaAdapterVO);
			
            // Tiene errores recuperables
			if (actaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + actaAdapterVO.infoString()); 
				saveDemodaErrors(request, actaVO);
				return forwardErrorRecoverable(mapping, request, userSession, ActaAdapter.ENC_NAME, actaAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (actaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + actaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ActaAdapter.ENC_NAME, actaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ActaAdapter.ENC_NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ActaAdapter.ENC_NAME);
		}
	}

	public ActionForward paramTipoActa(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = getCurrentUserSession(request, mapping);
			if (userSession==null) return forwardErrorSession(request);
						
			try {
				// Bajo el adapter del userSession
				ActaAdapter actaAdapterVO = (ActaAdapter) userSession.get(ActaAdapter.ENC_NAME);
				
				// Si es nulo no se puede continuar
				if (actaAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + ActaAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ActaAdapter.ENC_NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(actaAdapterVO, request);
				
	            // Tiene errores recuperables
				if (actaAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + actaAdapterVO.infoString()); 
					saveDemodaErrors(request, actaAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, ActaAdapter.ENC_NAME, actaAdapterVO);
				}
				
				// llamada al servicio
				actaAdapterVO = EfServiceLocator.getFiscalizacionService().getActaAdapterParamTipoActa(userSession, actaAdapterVO);
				
	            // Tiene errores recuperables
				if (actaAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + actaAdapterVO.infoString()); 
					saveDemodaErrors(request, actaAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, ActaAdapter.ENC_NAME, actaAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (actaAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + actaAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, ActaAdapter.ENC_NAME, actaAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(ActaAdapter.ENC_NAME, actaAdapterVO);
				// Subo el apdater al userSession
				userSession.put(ActaAdapter.ENC_NAME, actaAdapterVO);

				return mapping.findForward(EfConstants.FWD_ACTA_ENC_EDIT_ADAPTER);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ActaAdapter.ENC_NAME);
			}
		}

	public ActionForward buscarPersona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
				
		try {
			// Bajo el adapter del userSession
			ActaAdapter actaAdapterVO = (ActaAdapter) userSession.get(ActaAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (actaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ActaAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ActaAdapter.ENC_NAME); 
			}
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(actaAdapterVO, request);

            // Tiene errores recuperables
			if (actaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + actaAdapterVO.infoString()); 
				saveDemodaErrors(request, actaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ActaAdapter.ENC_NAME, actaAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(ActaAdapter.ENC_NAME, actaAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ActaAdapter.ENC_NAME, actaAdapterVO);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ActaAdapter.ENC_NAME);
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
				ActaAdapter actaAdapter =  (ActaAdapter) userSession.get(ActaAdapter.ENC_NAME);
				
				// Si es nulo no se puede continuar
				if (actaAdapter == null) {
					log.error("error en: "  + funcName + ": " + ActaAdapter.ENC_NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ActaAdapter.ENC_NAME); 
				}

				// recupero el id seleccionado por el usuario
				String selectedId = navModel.getSelectedId();
				
				// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
				if (StringUtil.isNullOrEmpty(selectedId)) {
					// Envio el VO al request
					request.setAttribute(ActaAdapter.ENC_NAME, actaAdapter);
					return mapping.findForward(EfConstants.FWD_ACTA_ENC_EDIT_ADAPTER); 
				}
				
				// Se carga el idPersona seleccionado, en el adapter
				actaAdapter.getActa().getPersona().setId(new Long(selectedId));
				
				// llamo al param del servicio
				actaAdapter = EfServiceLocator.getFiscalizacionService().getActaAdapterParamPersona(userSession, actaAdapter);

	            // Tiene errores recuperables
				if (actaAdapter.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + actaAdapter.infoString()); 
					saveDemodaErrors(request, actaAdapter);
					return forwardErrorRecoverable(mapping, request, userSession, 
							ActaAdapter.ENC_NAME, actaAdapter);
				}
				
				// Tiene errores no recuperables
				if (actaAdapter.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + actaAdapter.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
							ActaAdapter.ENC_NAME, actaAdapter);
				}

				// grabo los mensajes si hubiere
				saveDemodaMessages(request, actaAdapter);

				// Envio el VO al request
				request.setAttribute(ActaAdapter.ENC_NAME, actaAdapter);
				// Subo el apdater al userSession
				userSession.put(ActaAdapter.ENC_NAME, actaAdapter);

				return mapping.findForward(EfConstants.FWD_ACTA_ENC_EDIT_ADAPTER);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ActaAdapter.ENC_NAME);
			}
	}
		

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ActaAdapter.ENC_NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, ActaAdapter.ENC_NAME);
		
	}
	
}
	
