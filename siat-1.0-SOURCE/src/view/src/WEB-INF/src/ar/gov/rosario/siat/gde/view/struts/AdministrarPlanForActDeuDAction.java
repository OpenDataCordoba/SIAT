//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.gde.iface.model.PlanForActDeuAdapter;
import ar.gov.rosario.siat.gde.iface.model.PlanForActDeuVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarPlanForActDeuDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarPlanForActDeuDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PLANFORACTDEU, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		PlanForActDeuAdapter planForActDeuAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getPlanForActDeuAdapterForView(userSession, commonKey)";
				planForActDeuAdapterVO = GdeServiceLocator.getDefinicionService().getPlanForActDeuAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PLANFORACTDEU_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getPlanForActDeuAdapterForUpdate(userSession, commonKey)";
				planForActDeuAdapterVO = GdeServiceLocator.getDefinicionService().getPlanForActDeuAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PLANFORACTDEU_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getPlanForActDeuAdapterForView(userSession, commonKey)";
				planForActDeuAdapterVO = GdeServiceLocator.getDefinicionService().getPlanForActDeuAdapterForView(userSession, commonKey);				
				planForActDeuAdapterVO.addMessage(BaseError.MSG_ELIMINAR, GdeError.PLANFORACTDEU_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_PLANFORACTDEU_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getPlanForActDeuAdapterForCreate(userSession)";
				planForActDeuAdapterVO = GdeServiceLocator.getDefinicionService().getPlanForActDeuAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PLANFORACTDEU_EDIT_ADAPTER);				
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (planForActDeuAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + planForActDeuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanForActDeuAdapter.NAME, planForActDeuAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			planForActDeuAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + PlanForActDeuAdapter.NAME + ": "+ planForActDeuAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(PlanForActDeuAdapter.NAME, planForActDeuAdapterVO);
			// Subo el apdater al userSession
			userSession.put(PlanForActDeuAdapter.NAME, planForActDeuAdapterVO);
			 
			saveDemodaMessages(request, planForActDeuAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanForActDeuAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PLANFORACTDEU, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanForActDeuAdapter planForActDeuAdapterVO = (PlanForActDeuAdapter) userSession.get(PlanForActDeuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (planForActDeuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanForActDeuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanForActDeuAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(planForActDeuAdapterVO, request);
			
            // Tiene errores recuperables
			if (planForActDeuAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planForActDeuAdapterVO.infoString()); 
				saveDemodaErrors(request, planForActDeuAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanForActDeuAdapter.NAME, planForActDeuAdapterVO);
			}
			
			// llamada al servicio
			PlanForActDeuVO planForActDeuVO = GdeServiceLocator.getDefinicionService().createPlanForActDeu(userSession, planForActDeuAdapterVO.getPlanForActDeu());
			
            // Tiene errores recuperables
			if (planForActDeuVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planForActDeuVO.infoString()); 
				saveDemodaErrors(request, planForActDeuVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanForActDeuAdapter.NAME, planForActDeuAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (planForActDeuVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planForActDeuVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanForActDeuAdapter.NAME, planForActDeuAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanForActDeuAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanForActDeuAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PLANFORACTDEU, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanForActDeuAdapter planForActDeuAdapterVO = (PlanForActDeuAdapter) userSession.get(PlanForActDeuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (planForActDeuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanForActDeuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanForActDeuAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(planForActDeuAdapterVO, request);
			
            // Tiene errores recuperables
			if (planForActDeuAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planForActDeuAdapterVO.infoString()); 
				saveDemodaErrors(request, planForActDeuAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanForActDeuAdapter.NAME, planForActDeuAdapterVO);
			}
			
			// llamada al servicio
			PlanForActDeuVO planForActDeuVO = GdeServiceLocator.getDefinicionService().updatePlanForActDeu(userSession, planForActDeuAdapterVO.getPlanForActDeu());
			
            // Tiene errores recuperables
			if (planForActDeuVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planForActDeuAdapterVO.infoString()); 
				saveDemodaErrors(request, planForActDeuVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanForActDeuAdapter.NAME, planForActDeuAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (planForActDeuVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planForActDeuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanForActDeuAdapter.NAME, planForActDeuAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanForActDeuAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanForActDeuAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PLANFORACTDEU, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanForActDeuAdapter planForActDeuAdapterVO = (PlanForActDeuAdapter) userSession.get(PlanForActDeuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (planForActDeuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanForActDeuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanForActDeuAdapter.NAME); 
			}

			// llamada al servicio
			PlanForActDeuVO planForActDeuVO = GdeServiceLocator.getDefinicionService().deletePlanForActDeu
				(userSession, planForActDeuAdapterVO.getPlanForActDeu());
			
            // Tiene errores recuperables
			if (planForActDeuVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planForActDeuAdapterVO.infoString());
				saveDemodaErrors(request, planForActDeuVO);				
				request.setAttribute(PlanForActDeuAdapter.NAME, planForActDeuAdapterVO);
				return mapping.findForward(GdeConstants.FWD_PLANFORACTDEU_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (planForActDeuVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planForActDeuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanForActDeuAdapter.NAME, planForActDeuAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanForActDeuAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanForActDeuAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, PlanForActDeuAdapter.NAME);
		
	}

	public ActionForward paramEsComun (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				PlanForActDeuAdapter planForActDeuAdapterVO = (PlanForActDeuAdapter) userSession.get(PlanForActDeuAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (planForActDeuAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + PlanForActDeuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, PlanForActDeuAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(planForActDeuAdapterVO, request);
				
	            // Tiene errores recuperables
				if (planForActDeuAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + planForActDeuAdapterVO.infoString()); 
					saveDemodaErrors(request, planForActDeuAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, PlanForActDeuAdapter.NAME, planForActDeuAdapterVO);
				}
				
				log.debug(funcName + " planForActDeuAdapterVO.getFlagEsEspecial() " + planForActDeuAdapterVO.getFlagEsComun()); 
				if (planForActDeuAdapterVO.getPlanForActDeu().getEsComun().getEsSI()){
					planForActDeuAdapterVO.setFlagEsComun(true);
				
				} else if (planForActDeuAdapterVO.getPlanForActDeu().getEsComun().getEsNO()){
					planForActDeuAdapterVO.setFlagEsComun(false);
				
				} else {
					planForActDeuAdapterVO.setFlagEsComun(null);				
				}
				
	            // Tiene errores recuperables
				if (planForActDeuAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + planForActDeuAdapterVO.infoString()); 
					saveDemodaErrors(request, planForActDeuAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, PlanForActDeuAdapter.NAME, planForActDeuAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (planForActDeuAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + planForActDeuAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, PlanForActDeuAdapter.NAME, planForActDeuAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(PlanForActDeuAdapter.NAME, planForActDeuAdapterVO);
				// Subo el apdater al userSession
				userSession.put(PlanForActDeuAdapter.NAME, planForActDeuAdapterVO);
				
				return mapping.findForward(GdeConstants.FWD_PLANFORACTDEU_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, PlanForActDeuAdapter.NAME);
			}
		}
}
