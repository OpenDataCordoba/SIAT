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
import ar.gov.rosario.siat.ef.iface.model.DetAjuAdapter;
import ar.gov.rosario.siat.ef.iface.model.DetAjuVO;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarEncDetAjuDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEncDetAjuDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_DETAJU_ENC, act);		

		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		DetAjuAdapter detAjuAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());

			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getDetAjuAdapterForUpdate(userSession, commonKey)";
				detAjuAdapterVO = EfServiceLocator.getFiscalizacionService().getDetAjuAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_DETAJU_ENC_EDIT_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getDetAjuAdapterForCreate(userSession)";
				detAjuAdapterVO = EfServiceLocator.getFiscalizacionService().getDetAjuAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_DETAJU_ENC_EDIT_ADAPTER);
			}
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (detAjuAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + detAjuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DetAjuAdapter.ENC_NAME, detAjuAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			detAjuAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + DetAjuAdapter.ENC_NAME + ": "+ detAjuAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(DetAjuAdapter.ENC_NAME, detAjuAdapterVO);
			// Subo el apdater al userSession
			userSession.put(DetAjuAdapter.ENC_NAME, detAjuAdapterVO);

			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DetAjuAdapter.ENC_NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, 
			EfSecurityConstants.ABM_DETAJU_ENC, BaseSecurityConstants.AGREGAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DetAjuAdapter detAjuAdapterVO = (DetAjuAdapter) userSession.get(DetAjuAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (detAjuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DetAjuAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DetAjuAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(detAjuAdapterVO, request);
			
            // Tiene errores recuperables
			if (detAjuAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + detAjuAdapterVO.infoString()); 
				saveDemodaErrors(request, detAjuAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, DetAjuAdapter.ENC_NAME, detAjuAdapterVO);
			}
			
			// llamada al servicio
			DetAjuVO detAjuVO = EfServiceLocator.getFiscalizacionService().createDetAju(userSession, detAjuAdapterVO.getDetAju());
			
            // Tiene errores recuperables
			if (detAjuVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + detAjuVO.infoString()); 
				saveDemodaErrors(request, detAjuVO);
				return forwardErrorRecoverable(mapping, request, userSession, DetAjuAdapter.ENC_NAME, detAjuAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (detAjuVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + detAjuVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DetAjuAdapter.ENC_NAME, detAjuAdapterVO);
			}

			// Si tiene permiso lo dirijo al adapter de modificacion, 
			// sino vuelve al searchPage
			if (hasAccess(userSession, EfSecurityConstants.ABM_DETAJU, 
				BaseSecurityConstants.MODIFICAR)) {
				
				// seteo el id para que lo use el siguiente action 
				userSession.getNavModel().setSelectedId(detAjuVO.getId().toString());

				// lo dirijo al adapter de modificacion
				return forwardConfirmarOk(mapping, request, funcName, DetAjuAdapter.ENC_NAME, 
					EfConstants.PATH_ADMINISTRAR_DETAJU, BaseConstants.METHOD_INICIALIZAR, 
					BaseConstants.ACT_MODIFICAR);
			} else {
				
				// lo dirijo al searchPage				
				return forwardConfirmarOk(mapping, request, funcName, DetAjuAdapter.ENC_NAME);
				
			}
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DetAjuAdapter.ENC_NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, 
			EfSecurityConstants.ABM_DETAJU_ENC, BaseSecurityConstants.MODIFICAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DetAjuAdapter detAjuAdapterVO = (DetAjuAdapter) userSession.get(DetAjuAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (detAjuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DetAjuAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DetAjuAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(detAjuAdapterVO, request);
			
            // Tiene errores recuperables
			if (detAjuAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + detAjuAdapterVO.infoString()); 
				saveDemodaErrors(request, detAjuAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, DetAjuAdapter.ENC_NAME, detAjuAdapterVO);
			}
			
			// llamada al servicio
			DetAjuVO detAjuVO = EfServiceLocator.getFiscalizacionService().updateDetAju(userSession, detAjuAdapterVO.getDetAju());
			
            // Tiene errores recuperables
			if (detAjuVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + detAjuAdapterVO.infoString()); 
				saveDemodaErrors(request, detAjuVO);
				return forwardErrorRecoverable(mapping, request, userSession, DetAjuAdapter.ENC_NAME, detAjuAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (detAjuVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + detAjuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DetAjuAdapter.ENC_NAME, detAjuAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DetAjuAdapter.ENC_NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DetAjuAdapter.ENC_NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, DetAjuAdapter.ENC_NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, DetAjuAdapter.ENC_NAME);
		
	}
	
}
	
