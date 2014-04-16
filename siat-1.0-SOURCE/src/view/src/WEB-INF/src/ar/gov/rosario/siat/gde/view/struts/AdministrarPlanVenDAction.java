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
import ar.gov.rosario.siat.gde.iface.model.PlanVenAdapter;
import ar.gov.rosario.siat.gde.iface.model.PlanVenVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarPlanVenDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarPlanVenDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PLANVEN, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		PlanVenAdapter planVenAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getPlanVenAdapterForView(userSession, commonKey)";
				planVenAdapterVO = GdeServiceLocator.getDefinicionService().getPlanVenAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PLANVEN_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getPlanVenAdapterForUpdate(userSession, commonKey)";
				planVenAdapterVO = GdeServiceLocator.getDefinicionService().getPlanVenAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PLANVEN_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getPlanVenAdapterForView(userSession, commonKey)";
				planVenAdapterVO = GdeServiceLocator.getDefinicionService().getPlanVenAdapterForView(userSession, commonKey);				
				planVenAdapterVO.addMessage(BaseError.MSG_ELIMINAR, GdeError.PLANVEN_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_PLANVEN_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getPlanVenAdapterForCreate(userSession)";
				planVenAdapterVO = GdeServiceLocator.getDefinicionService().getPlanVenAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PLANVEN_EDIT_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (planVenAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + planVenAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanVenAdapter.NAME, planVenAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			planVenAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + PlanVenAdapter.NAME + ": "+ planVenAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(PlanVenAdapter.NAME, planVenAdapterVO);
			// Subo el apdater al userSession
			userSession.put(PlanVenAdapter.NAME, planVenAdapterVO);
			 
			saveDemodaMessages(request, planVenAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanVenAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PLANVEN, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanVenAdapter planVenAdapterVO = (PlanVenAdapter) userSession.get(PlanVenAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (planVenAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanVenAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanVenAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(planVenAdapterVO, request);
			
            // Tiene errores recuperables
			if (planVenAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planVenAdapterVO.infoString()); 
				saveDemodaErrors(request, planVenAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanVenAdapter.NAME, planVenAdapterVO);
			}
			
			// llamada al servicio
			PlanVenVO planVenVO = GdeServiceLocator.getDefinicionService().createPlanVen(userSession, planVenAdapterVO.getPlanVen());
			
            // Tiene errores recuperables
			if (planVenVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planVenVO.infoString()); 
				saveDemodaErrors(request, planVenVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanVenAdapter.NAME, planVenAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (planVenVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planVenVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanVenAdapter.NAME, planVenAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanVenAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanVenAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PLANVEN, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanVenAdapter planVenAdapterVO = (PlanVenAdapter) userSession.get(PlanVenAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (planVenAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanVenAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanVenAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(planVenAdapterVO, request);
			
            // Tiene errores recuperables
			if (planVenAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planVenAdapterVO.infoString()); 
				saveDemodaErrors(request, planVenAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanVenAdapter.NAME, planVenAdapterVO);
			}
			
			// llamada al servicio
			PlanVenVO planVenVO = GdeServiceLocator.getDefinicionService().updatePlanVen(userSession, planVenAdapterVO.getPlanVen());
			
            // Tiene errores recuperables
			if (planVenVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planVenAdapterVO.infoString()); 
				saveDemodaErrors(request, planVenVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanVenAdapter.NAME, planVenAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (planVenVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planVenAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanVenAdapter.NAME, planVenAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanVenAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanVenAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PLANVEN, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanVenAdapter planVenAdapterVO = (PlanVenAdapter) userSession.get(PlanVenAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (planVenAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanVenAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanVenAdapter.NAME); 
			}

			// llamada al servicio
			PlanVenVO planVenVO = GdeServiceLocator.getDefinicionService().deletePlanVen
				(userSession, planVenAdapterVO.getPlanVen());
			
            // Tiene errores recuperables
			if (planVenVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planVenAdapterVO.infoString());
				saveDemodaErrors(request, planVenVO);				
				request.setAttribute(PlanVenAdapter.NAME, planVenAdapterVO);
				return mapping.findForward(GdeConstants.FWD_PLANVEN_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (planVenVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planVenAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanVenAdapter.NAME, planVenAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanVenAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanVenAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, PlanVenAdapter.NAME);
		
	}
}
