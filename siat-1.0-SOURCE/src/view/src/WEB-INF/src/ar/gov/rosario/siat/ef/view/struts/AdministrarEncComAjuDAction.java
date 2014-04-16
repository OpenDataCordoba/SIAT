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
import ar.gov.rosario.siat.ef.iface.model.ComAjuAdapter;
import ar.gov.rosario.siat.ef.iface.model.ComAjuVO;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarEncComAjuDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEncComAjuDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_COMAJU_ENC, act);		

		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ComAjuAdapter comAjuAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());

			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getComAjuAdapterForUpdate(userSession, commonKey)";
				comAjuAdapterVO = EfServiceLocator.getFiscalizacionService().getComAjuAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_COMAJU_ENC_EDIT_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getComAjuAdapterForCreate(userSession)";
				comAjuAdapterVO = EfServiceLocator.getFiscalizacionService().getComAjuAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_COMAJU_ENC_EDIT_ADAPTER);
			}
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (comAjuAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + comAjuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ComAjuAdapter.ENC_NAME, comAjuAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			comAjuAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ComAjuAdapter.ENC_NAME + ": "+ comAjuAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ComAjuAdapter.ENC_NAME, comAjuAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ComAjuAdapter.ENC_NAME, comAjuAdapterVO);

			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ComAjuAdapter.ENC_NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, 
			EfSecurityConstants.ABM_COMAJU_ENC, BaseSecurityConstants.AGREGAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ComAjuAdapter comAjuAdapterVO = (ComAjuAdapter) userSession.get(ComAjuAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (comAjuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ComAjuAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ComAjuAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(comAjuAdapterVO, request);
			
            // Tiene errores recuperables
			if (comAjuAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + comAjuAdapterVO.infoString()); 
				saveDemodaErrors(request, comAjuAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ComAjuAdapter.ENC_NAME, comAjuAdapterVO);
			}
			
			// llamada al servicio
			ComAjuVO comAjuVO = EfServiceLocator.getFiscalizacionService().createComAju(userSession, comAjuAdapterVO.getComAju());
			
            // Tiene errores recuperables
			if (comAjuVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + comAjuVO.infoString()); 
				saveDemodaErrors(request, comAjuVO);
				return forwardErrorRecoverable(mapping, request, userSession, ComAjuAdapter.ENC_NAME, comAjuAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (comAjuVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + comAjuVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ComAjuAdapter.ENC_NAME, comAjuAdapterVO);
			}

			// Si tiene permiso lo dirijo al adapter de modificacion, 
			// sino vuelve al searchPage
			if (hasAccess(userSession, EfSecurityConstants.ABM_COMAJU, BaseSecurityConstants.MODIFICAR)) {
				// seteo el id para que lo use el siguiente action 
				userSession.getNavModel().setSelectedId(comAjuVO.getId().toString());

				// lo dirijo al adapter de modificacion
				return forwardConfirmarOk(mapping, request, funcName, ComAjuAdapter.ENC_NAME, 
					EfConstants.PATH_ADMINISTRAR_COMAJU, BaseConstants.METHOD_INICIALIZAR, 
					BaseConstants.ACT_MODIFICAR);
			} else {
				
				// lo dirijo al searchPage				
				return forwardConfirmarOk(mapping, request, funcName, ComAjuAdapter.ENC_NAME);
				
			}
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ComAjuAdapter.ENC_NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, 
			EfSecurityConstants.ABM_COMAJU_ENC, BaseSecurityConstants.MODIFICAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ComAjuAdapter comAjuAdapterVO = (ComAjuAdapter) userSession.get(ComAjuAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (comAjuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ComAjuAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ComAjuAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(comAjuAdapterVO, request);
			
            // Tiene errores recuperables
			if (comAjuAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + comAjuAdapterVO.infoString()); 
				saveDemodaErrors(request, comAjuAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ComAjuAdapter.ENC_NAME, comAjuAdapterVO);
			}
			
			// llamada al servicio
			ComAjuVO comAjuVO = EfServiceLocator.getFiscalizacionService().updateComAju(userSession, comAjuAdapterVO.getComAju());
			
            // Tiene errores recuperables
			if (comAjuVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + comAjuAdapterVO.infoString()); 
				saveDemodaErrors(request, comAjuVO);
				return forwardErrorRecoverable(mapping, request, userSession, ComAjuAdapter.ENC_NAME, comAjuAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (comAjuVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + comAjuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ComAjuAdapter.ENC_NAME, comAjuAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ComAjuAdapter.ENC_NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ComAjuAdapter.ENC_NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ComAjuAdapter.ENC_NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, ComAjuAdapter.ENC_NAME);
		
	}
	
}
	
