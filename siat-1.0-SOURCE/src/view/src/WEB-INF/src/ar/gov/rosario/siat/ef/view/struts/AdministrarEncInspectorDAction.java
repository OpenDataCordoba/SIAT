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
import ar.gov.rosario.siat.ef.iface.model.InspectorAdapter;
import ar.gov.rosario.siat.ef.iface.model.InspectorVO;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarEncInspectorDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEncInspectorDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_INSPECTOR_ENC, act);		

		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		InspectorAdapter inspectorAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());

			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getInspectorAdapterForUpdate(userSession, commonKey)";
				inspectorAdapterVO = EfServiceLocator.getDefinicionService().getInspectorAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_INSPECTOR_ENC_EDIT_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getInspectorAdapterForCreate(userSession)";
				inspectorAdapterVO = EfServiceLocator.getDefinicionService().getInspectorAdapterForCreate(userSession);
				actionForward = mapping.findForward(EfConstants.FWD_INSPECTOR_ENC_EDIT_ADAPTER);
			}
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (inspectorAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + inspectorAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, InspectorAdapter.ENC_NAME, inspectorAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			inspectorAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + InspectorAdapter.ENC_NAME + ": "+ inspectorAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(InspectorAdapter.ENC_NAME, inspectorAdapterVO);
			// Subo el apdater al userSession
			userSession.put(InspectorAdapter.ENC_NAME, inspectorAdapterVO);

			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, InspectorAdapter.ENC_NAME);
		}
	}
	
	public ActionForward buscarPersona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		return forwardSeleccionar(mapping, request, EfConstants.METOD_PARAM_PERSONA, PadConstants.ACTION_BUSCAR_PERSONA, false);		
	}
	
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, 
			EfSecurityConstants.ABM_INSPECTOR_ENC, BaseSecurityConstants.AGREGAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			InspectorAdapter inspectorAdapterVO = (InspectorAdapter) userSession.get(InspectorAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (inspectorAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + InspectorAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, InspectorAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(inspectorAdapterVO, request);
			
            // Tiene errores recuperables
			if (inspectorAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + inspectorAdapterVO.infoString()); 
				saveDemodaErrors(request, inspectorAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, InspectorAdapter.ENC_NAME, inspectorAdapterVO);
			}
			
			// llamada al servicio
			InspectorVO inspectorVO = EfServiceLocator.getDefinicionService().createInspector(userSession, inspectorAdapterVO.getInspector());
			
            // Tiene errores recuperables
			if (inspectorVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + inspectorVO.infoString()); 
				saveDemodaErrors(request, inspectorVO);
				return forwardErrorRecoverable(mapping, request, userSession, InspectorAdapter.ENC_NAME, inspectorAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (inspectorVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + inspectorVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, InspectorAdapter.ENC_NAME, inspectorAdapterVO);
			}

			// Si tiene permiso lo dirijo al adapter de modificacion, 
			// sino vuelve al searchPage
			if (hasAccess(userSession, EfSecurityConstants.ABM_INSPECTOR, 
				BaseSecurityConstants.MODIFICAR)) {
				
				// seteo el id para que lo use el siguiente action 
				userSession.getNavModel().setSelectedId(inspectorVO.getId().toString());

				// lo dirijo al adapter de modificacion
				return forwardConfirmarOk(mapping, request, funcName, InspectorAdapter.ENC_NAME, 
					EfConstants.PATH_ADMINISTRAR_INSPECTOR, BaseConstants.METHOD_INICIALIZAR, 
					BaseConstants.ACT_MODIFICAR);
			} else {
				
				// lo dirijo al searchPage				
				return forwardConfirmarOk(mapping, request, funcName, InspectorAdapter.ENC_NAME);
				
			}
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, InspectorAdapter.ENC_NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, 
			EfSecurityConstants.ABM_INSPECTOR_ENC, BaseSecurityConstants.MODIFICAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			InspectorAdapter inspectorAdapterVO = (InspectorAdapter) userSession.get(InspectorAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (inspectorAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + InspectorAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, InspectorAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(inspectorAdapterVO, request);
			
            // Tiene errores recuperables
			if (inspectorAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + inspectorAdapterVO.infoString()); 
				saveDemodaErrors(request, inspectorAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, InspectorAdapter.ENC_NAME, inspectorAdapterVO);
			}
			
			// llamada al servicio
			InspectorVO inspectorVO = EfServiceLocator.getDefinicionService().updateInspector(userSession, inspectorAdapterVO.getInspector());
			
            // Tiene errores recuperables
			if (inspectorVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + inspectorAdapterVO.infoString()); 
				saveDemodaErrors(request, inspectorVO);
				return forwardErrorRecoverable(mapping, request, userSession, InspectorAdapter.ENC_NAME, inspectorAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (inspectorVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + inspectorAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, InspectorAdapter.ENC_NAME, inspectorAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, InspectorAdapter.ENC_NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, InspectorAdapter.ENC_NAME);
		}
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
				InspectorAdapter inspectorAdapterVO =  (InspectorAdapter) userSession.get(InspectorAdapter.ENC_NAME);
				
				// Si es nulo no se puede continuar
				if (inspectorAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + InspectorAdapter.ENC_NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, InspectorAdapter.ENC_NAME); 
				}

				// recupero el id seleccionado por el usuario
				String selectedId = navModel.getSelectedId();
				
				// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
				if (StringUtil.isNullOrEmpty(selectedId)) {
					// Envio el VO al request
					request.setAttribute(InspectorAdapter.ENC_NAME, inspectorAdapterVO);
					return mapping.findForward(EfConstants.FWD_INSPECTOR_ENC_EDIT_ADAPTER); 
				}
				
				// Se carga el idPersona seleccionado, en el adapter
				inspectorAdapterVO.getInspector().setIdPersona(new Long(selectedId));
				
				// llamo al param del servicio
				inspectorAdapterVO = EfServiceLocator.getDefinicionService().getInspectorAdapterParamPersona(userSession, inspectorAdapterVO);

	            // Tiene errores recuperables
				if (inspectorAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + inspectorAdapterVO.infoString()); 
					saveDemodaErrors(request, inspectorAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
							InspectorAdapter.ENC_NAME, inspectorAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (inspectorAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + inspectorAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
							InspectorAdapter.ENC_NAME, inspectorAdapterVO);
				}

				// grabo los mensajes si hubiere
				saveDemodaMessages(request, inspectorAdapterVO);

				// Envio el VO al request
				request.setAttribute(InspectorAdapter.ENC_NAME, inspectorAdapterVO);
				// Subo el apdater al userSession
				userSession.put(InspectorAdapter.ENC_NAME, inspectorAdapterVO);

				return mapping.findForward(EfConstants.FWD_INSPECTOR_ENC_EDIT_ADAPTER);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, InspectorAdapter.ENC_NAME);
			}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, InspectorAdapter.ENC_NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, InspectorAdapter.ENC_NAME);
		
	}
	
}
	
