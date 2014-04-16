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
import ar.gov.rosario.siat.gde.iface.model.PlanImpMinAdapter;
import ar.gov.rosario.siat.gde.iface.model.PlanImpMinVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarPlanImpMinDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarPlanImpMinDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PLANIMPMIN, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		PlanImpMinAdapter planImpMinAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getPlanImpMinAdapterForView(userSession, commonKey)";
				planImpMinAdapterVO = GdeServiceLocator.getDefinicionService().getPlanImpMinAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PLANIMPMIN_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getPlanImpMinAdapterForUpdate(userSession, commonKey)";
				planImpMinAdapterVO = GdeServiceLocator.getDefinicionService().getPlanImpMinAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PLANIMPMIN_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getPlanImpMinAdapterForView(userSession, commonKey)";
				planImpMinAdapterVO = GdeServiceLocator.getDefinicionService().getPlanImpMinAdapterForView(userSession, commonKey);				
				planImpMinAdapterVO.addMessage(BaseError.MSG_ELIMINAR, GdeError.PLANIMPMIN_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_PLANIMPMIN_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getPlanImpMinAdapterForCreate(userSession)";
				planImpMinAdapterVO = GdeServiceLocator.getDefinicionService().getPlanImpMinAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PLANIMPMIN_EDIT_ADAPTER);				
			}
			

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (planImpMinAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + planImpMinAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanImpMinAdapter.NAME, planImpMinAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			planImpMinAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + PlanImpMinAdapter.NAME + ": "+ planImpMinAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(PlanImpMinAdapter.NAME, planImpMinAdapterVO);
			// Subo el apdater al userSession
			userSession.put(PlanImpMinAdapter.NAME, planImpMinAdapterVO);
			 
			saveDemodaMessages(request, planImpMinAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanImpMinAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PLANIMPMIN, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanImpMinAdapter planImpMinAdapterVO = (PlanImpMinAdapter) userSession.get(PlanImpMinAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (planImpMinAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanImpMinAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanImpMinAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(planImpMinAdapterVO, request);
			
            // Tiene errores recuperables
			if (planImpMinAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planImpMinAdapterVO.infoString()); 
				saveDemodaErrors(request, planImpMinAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanImpMinAdapter.NAME, planImpMinAdapterVO);
			}
			
			// llamada al servicio
			PlanImpMinVO planImpMinVO = GdeServiceLocator.getDefinicionService().createPlanImpMin(userSession, planImpMinAdapterVO.getPlanImpMin());
			
            // Tiene errores recuperables
			if (planImpMinVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planImpMinVO.infoString()); 
				saveDemodaErrors(request, planImpMinVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanImpMinAdapter.NAME, planImpMinAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (planImpMinVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planImpMinVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanImpMinAdapter.NAME, planImpMinAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanImpMinAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanImpMinAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PLANIMPMIN, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanImpMinAdapter planImpMinAdapterVO = (PlanImpMinAdapter) userSession.get(PlanImpMinAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (planImpMinAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanImpMinAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanImpMinAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(planImpMinAdapterVO, request);
			
            // Tiene errores recuperables
			if (planImpMinAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planImpMinAdapterVO.infoString()); 
				saveDemodaErrors(request, planImpMinAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanImpMinAdapter.NAME, planImpMinAdapterVO);
			}
			
			// llamada al servicio
			PlanImpMinVO planImpMinVO = GdeServiceLocator.getDefinicionService().updatePlanImpMin(userSession, planImpMinAdapterVO.getPlanImpMin());
			
            // Tiene errores recuperables
			if (planImpMinVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planImpMinAdapterVO.infoString()); 
				saveDemodaErrors(request, planImpMinVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanImpMinAdapter.NAME, planImpMinAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (planImpMinVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planImpMinAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanImpMinAdapter.NAME, planImpMinAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanImpMinAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanImpMinAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PLANIMPMIN, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanImpMinAdapter planImpMinAdapterVO = (PlanImpMinAdapter) userSession.get(PlanImpMinAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (planImpMinAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanImpMinAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanImpMinAdapter.NAME); 
			}

			// llamada al servicio
			PlanImpMinVO planImpMinVO = GdeServiceLocator.getDefinicionService().deletePlanImpMin
				(userSession, planImpMinAdapterVO.getPlanImpMin());
			
            // Tiene errores recuperables
			if (planImpMinVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planImpMinAdapterVO.infoString());
				saveDemodaErrors(request, planImpMinVO);				
				request.setAttribute(PlanImpMinAdapter.NAME, planImpMinAdapterVO);
				return mapping.findForward(GdeConstants.FWD_PLANIMPMIN_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (planImpMinVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planImpMinAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanImpMinAdapter.NAME, planImpMinAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanImpMinAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanImpMinAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, PlanImpMinAdapter.NAME);
		
	}
			
}
