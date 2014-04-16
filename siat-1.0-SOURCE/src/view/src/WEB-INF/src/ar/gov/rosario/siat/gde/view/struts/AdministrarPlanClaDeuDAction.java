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
import ar.gov.rosario.siat.gde.iface.model.PlanClaDeuAdapter;
import ar.gov.rosario.siat.gde.iface.model.PlanClaDeuVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarPlanClaDeuDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarPlanClaDeuDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PLANCLADEU, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		PlanClaDeuAdapter planClaDeuAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getPlanClaDeuAdapterForView(userSession, commonKey)";
				planClaDeuAdapterVO = GdeServiceLocator.getDefinicionService().getPlanClaDeuAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PLANCLADEU_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getPlanClaDeuAdapterForUpdate(userSession, commonKey)";
				planClaDeuAdapterVO = GdeServiceLocator.getDefinicionService().getPlanClaDeuAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PLANCLADEU_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getPlanClaDeuAdapterForView(userSession, commonKey)";
				planClaDeuAdapterVO = GdeServiceLocator.getDefinicionService().getPlanClaDeuAdapterForView(userSession, commonKey);				
				planClaDeuAdapterVO.addMessage(BaseError.MSG_ELIMINAR, GdeError.PLANCLADEU_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_PLANCLADEU_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getPlanClaDeuAdapterForCreate(userSession)";
				planClaDeuAdapterVO = GdeServiceLocator.getDefinicionService().getPlanClaDeuAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PLANCLADEU_EDIT_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
	
			
			// Tiene errores no recuperables
			if (planClaDeuAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + planClaDeuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanClaDeuAdapter.NAME, planClaDeuAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			planClaDeuAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + PlanClaDeuAdapter.NAME + ": "+ planClaDeuAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(PlanClaDeuAdapter.NAME, planClaDeuAdapterVO);
			// Subo el apdater al userSession
			userSession.put(PlanClaDeuAdapter.NAME, planClaDeuAdapterVO);
			 
			saveDemodaMessages(request, planClaDeuAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanClaDeuAdapter.NAME);
		}
	}
	
	public ActionForward paramRecurso (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception{
		
		String funcName = DemodaUtil.currentMethodName();
		
		if(log.isDebugEnabled())log.debug(funcName + " :enter");
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		
		try{
			PlanClaDeuAdapter planClaDeuAdapter = (PlanClaDeuAdapter)userSession.get(PlanClaDeuAdapter.NAME);
			DemodaUtil.populateVO(planClaDeuAdapter, request);
			
			planClaDeuAdapter = GdeServiceLocator.getDefinicionService().getPlanClaDeuParam(userSession, planClaDeuAdapter);
			
			userSession.put(PlanClaDeuAdapter.NAME, planClaDeuAdapter);
			request.setAttribute(PlanClaDeuAdapter.NAME, planClaDeuAdapter);
			
			return mapping.findForward(GdeConstants.FWD_PLANCLADEU_EDIT_ADAPTER);
			
		}catch (Exception exception){
			return baseException(mapping, request, funcName, exception, PlanClaDeuAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PLANCLADEU, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanClaDeuAdapter planClaDeuAdapterVO = (PlanClaDeuAdapter) userSession.get(PlanClaDeuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (planClaDeuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanClaDeuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanClaDeuAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(planClaDeuAdapterVO, request);
			
            // Tiene errores recuperables
			if (planClaDeuAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planClaDeuAdapterVO.infoString()); 
				saveDemodaErrors(request, planClaDeuAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanClaDeuAdapter.NAME, planClaDeuAdapterVO);
			}
			
			// llamada al servicio
			PlanClaDeuVO planClaDeuVO = GdeServiceLocator.getDefinicionService().createPlanClaDeu(userSession, planClaDeuAdapterVO.getPlanClaDeu());
			
            // Tiene errores recuperables
			if (planClaDeuVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planClaDeuVO.infoString()); 
				saveDemodaErrors(request, planClaDeuVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanClaDeuAdapter.NAME, planClaDeuAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (planClaDeuVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planClaDeuVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanClaDeuAdapter.NAME, planClaDeuAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanClaDeuAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanClaDeuAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PLANCLADEU, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanClaDeuAdapter planClaDeuAdapterVO = (PlanClaDeuAdapter) userSession.get(PlanClaDeuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (planClaDeuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanClaDeuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanClaDeuAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(planClaDeuAdapterVO, request);
			
            // Tiene errores recuperables
			if (planClaDeuAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planClaDeuAdapterVO.infoString()); 
				saveDemodaErrors(request, planClaDeuAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanClaDeuAdapter.NAME, planClaDeuAdapterVO);
			}
			
			// llamada al servicio
			PlanClaDeuVO planClaDeuVO = GdeServiceLocator.getDefinicionService().updatePlanClaDeu(userSession, planClaDeuAdapterVO.getPlanClaDeu());
			
            // Tiene errores recuperables
			if (planClaDeuVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planClaDeuAdapterVO.infoString()); 
				saveDemodaErrors(request, planClaDeuVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanClaDeuAdapter.NAME, planClaDeuAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (planClaDeuVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planClaDeuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanClaDeuAdapter.NAME, planClaDeuAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanClaDeuAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanClaDeuAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PLANCLADEU, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanClaDeuAdapter planClaDeuAdapterVO = (PlanClaDeuAdapter) userSession.get(PlanClaDeuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (planClaDeuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanClaDeuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanClaDeuAdapter.NAME); 
			}

			// llamada al servicio
			PlanClaDeuVO planClaDeuVO = GdeServiceLocator.getDefinicionService().deletePlanClaDeu
				(userSession, planClaDeuAdapterVO.getPlanClaDeu());
			
            // Tiene errores recuperables
			if (planClaDeuVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planClaDeuAdapterVO.infoString());
				saveDemodaErrors(request, planClaDeuVO);				
				request.setAttribute(PlanClaDeuAdapter.NAME, planClaDeuAdapterVO);
				return mapping.findForward(GdeConstants.FWD_PLANCLADEU_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (planClaDeuVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planClaDeuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanClaDeuAdapter.NAME, planClaDeuAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanClaDeuAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanClaDeuAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, PlanClaDeuAdapter.NAME);
		
	}
		
}
