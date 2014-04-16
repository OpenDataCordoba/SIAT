//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.esp.iface.model.HabilitacionAdapter;
import ar.gov.rosario.siat.esp.iface.service.EspServiceLocator;
import ar.gov.rosario.siat.esp.iface.util.EspError;
import ar.gov.rosario.siat.esp.iface.util.EspSecurityConstants;
import ar.gov.rosario.siat.esp.view.util.EspConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class AdministrarEmiHabilitacionDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEmiHabilitacionDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping, EspSecurityConstants.ABM_HABILITACION, act);		

			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			HabilitacionAdapter habilitacionAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey=null;
				if(!StringUtil.isNullOrEmpty(navModel.getSelectedId())){
					commonKey = new CommonKey(navModel.getSelectedId());
				}
				if (navModel.getAct().equals(EspConstants.MTD_EMISION_INICIAL)) {
					stringServicio = "getHabilitacionAdapterForView(userSession, commonKey)";
					habilitacionAdapterVO = EspServiceLocator.getHabilitacionService().getHabilitacionAdapterForView(userSession, commonKey);
					habilitacionAdapterVO.addMessage(EspError.HABILITACION_EMISION_INICIAL_MSG);
					actionForward = mapping.findForward(EspConstants.FWD_HABILITACION_EMIVIEW_ADAPTER);					
				}
				
				
				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (habilitacionAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + habilitacionAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, HabilitacionAdapter.EMI_NAME, habilitacionAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				habilitacionAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + HabilitacionAdapter.EMI_NAME + ": "+ habilitacionAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(HabilitacionAdapter.EMI_NAME, habilitacionAdapterVO);
				// Subo el apdater al userSession
				userSession.put(HabilitacionAdapter.EMI_NAME, habilitacionAdapterVO);
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, HabilitacionAdapter.EMI_NAME);
			}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, HabilitacionAdapter.EMI_NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, HabilitacionAdapter.EMI_NAME);
	}
	
	
	public ActionForward emisionInicialConfirmar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, EspSecurityConstants.ABM_HABILITACION, 
				EspSecurityConstants.EMISION_INICIAL);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				HabilitacionAdapter habilitacionAdapterVO = (HabilitacionAdapter) userSession.get(HabilitacionAdapter.EMI_NAME);
				
				// Si es nulo no se puede continuar
				if (habilitacionAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + HabilitacionAdapter.EMI_NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, HabilitacionAdapter.EMI_NAME); 
				}

				// llamada al servicio
				habilitacionAdapterVO = EspServiceLocator.getHabilitacionService().emisionInicial(userSession, habilitacionAdapterVO);
				
	            // Tiene errores recuperables
				if (habilitacionAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + habilitacionAdapterVO.infoString());
					saveDemodaErrors(request, habilitacionAdapterVO);				
					request.setAttribute(HabilitacionAdapter.EMI_NAME, habilitacionAdapterVO);
					return mapping.findForward(EspConstants.FWD_HABILITACION_EMIVIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (habilitacionAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + habilitacionAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, HabilitacionAdapter.EMI_NAME, habilitacionAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, HabilitacionAdapter.EMI_NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, HabilitacionAdapter.EMI_NAME);
			}
		}
	
}
