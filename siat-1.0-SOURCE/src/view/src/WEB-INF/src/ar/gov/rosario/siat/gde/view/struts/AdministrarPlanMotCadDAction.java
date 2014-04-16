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
import ar.gov.rosario.siat.gde.iface.model.PlanMotCadAdapter;
import ar.gov.rosario.siat.gde.iface.model.PlanMotCadVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarPlanMotCadDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarPlanMotCadDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PLANMOTCAD, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		PlanMotCadAdapter planMotCadAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getPlanMotCadAdapterForView(userSession, commonKey)";
				planMotCadAdapterVO = GdeServiceLocator.getDefinicionService().getPlanMotCadAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PLANMOTCAD_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getPlanMotCadAdapterForUpdate(userSession, commonKey)";
				planMotCadAdapterVO = GdeServiceLocator.getDefinicionService().getPlanMotCadAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PLANMOTCAD_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getPlanMotCadAdapterForView(userSession, commonKey)";
				planMotCadAdapterVO = GdeServiceLocator.getDefinicionService().getPlanMotCadAdapterForView(userSession, commonKey);				
				planMotCadAdapterVO.addMessage(BaseError.MSG_ELIMINAR, GdeError.PLANMOTCAD_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_PLANMOTCAD_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getPlanMotCadAdapterForCreate(userSession)";
				planMotCadAdapterVO = GdeServiceLocator.getDefinicionService().getPlanMotCadAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PLANMOTCAD_EDIT_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (planMotCadAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + planMotCadAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanMotCadAdapter.NAME, planMotCadAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			planMotCadAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + PlanMotCadAdapter.NAME + ": "+ planMotCadAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(PlanMotCadAdapter.NAME, planMotCadAdapterVO);
			// Subo el apdater al userSession
			userSession.put(PlanMotCadAdapter.NAME, planMotCadAdapterVO);
			 
			saveDemodaMessages(request, planMotCadAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanMotCadAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PLANMOTCAD, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanMotCadAdapter planMotCadAdapterVO = (PlanMotCadAdapter) userSession.get(PlanMotCadAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (planMotCadAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanMotCadAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanMotCadAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(planMotCadAdapterVO, request);
			
            // Tiene errores recuperables
			if (planMotCadAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planMotCadAdapterVO.infoString()); 
				saveDemodaErrors(request, planMotCadAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanMotCadAdapter.NAME, planMotCadAdapterVO);
			}
			
			// llamada al servicio
			PlanMotCadVO planMotCadVO = GdeServiceLocator.getDefinicionService().createPlanMotCad(userSession, planMotCadAdapterVO.getPlanMotCad());
			
            // Tiene errores recuperables
			if (planMotCadVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planMotCadVO.infoString()); 
				saveDemodaErrors(request, planMotCadVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanMotCadAdapter.NAME, planMotCadAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (planMotCadVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planMotCadVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanMotCadAdapter.NAME, planMotCadAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanMotCadAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanMotCadAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PLANMOTCAD, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanMotCadAdapter planMotCadAdapterVO = (PlanMotCadAdapter) userSession.get(PlanMotCadAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (planMotCadAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanMotCadAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanMotCadAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(planMotCadAdapterVO, request);
			
            // Tiene errores recuperables
			if (planMotCadAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planMotCadAdapterVO.infoString()); 
				saveDemodaErrors(request, planMotCadAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanMotCadAdapter.NAME, planMotCadAdapterVO);
			}
			
			// llamada al servicio
			PlanMotCadVO planMotCadVO = GdeServiceLocator.getDefinicionService().updatePlanMotCad(userSession, planMotCadAdapterVO.getPlanMotCad());
			
            // Tiene errores recuperables
			if (planMotCadVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planMotCadAdapterVO.infoString()); 
				saveDemodaErrors(request, planMotCadVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanMotCadAdapter.NAME, planMotCadAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (planMotCadVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planMotCadAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanMotCadAdapter.NAME, planMotCadAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanMotCadAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanMotCadAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PLANMOTCAD, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanMotCadAdapter planMotCadAdapterVO = (PlanMotCadAdapter) userSession.get(PlanMotCadAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (planMotCadAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanMotCadAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanMotCadAdapter.NAME); 
			}

			// llamada al servicio
			PlanMotCadVO planMotCadVO = GdeServiceLocator.getDefinicionService().deletePlanMotCad
				(userSession, planMotCadAdapterVO.getPlanMotCad());
			
            // Tiene errores recuperables
			if (planMotCadVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planMotCadAdapterVO.infoString());
				saveDemodaErrors(request, planMotCadVO);				
				request.setAttribute(PlanMotCadAdapter.NAME, planMotCadAdapterVO);
				return mapping.findForward(GdeConstants.FWD_PLANMOTCAD_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (planMotCadVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planMotCadAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanMotCadAdapter.NAME, planMotCadAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanMotCadAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanMotCadAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, PlanMotCadAdapter.NAME);
		
	}
	
	public ActionForward paramEsEspecial (ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanMotCadAdapter planMotCadAdapterVO = (PlanMotCadAdapter) userSession.get(PlanMotCadAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (planMotCadAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanMotCadAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanMotCadAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(planMotCadAdapterVO, request);
			
            // Tiene errores recuperables
			if (planMotCadAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planMotCadAdapterVO.infoString()); 
				saveDemodaErrors(request, planMotCadAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanMotCadAdapter.NAME, planMotCadAdapterVO);
			}
			
			// No llamo al servicio y que.
			if (planMotCadAdapterVO.getPlanMotCad().getEsEspecial().getEsSI()){
				planMotCadAdapterVO.setFlagEsEspecial(true);
				planMotCadAdapterVO.getPlanMotCad().setCantCuoAltView("");
				planMotCadAdapterVO.getPlanMotCad().setCantCuoConView("");
				planMotCadAdapterVO.getPlanMotCad().setCantDiasView("");
			
			} else if (planMotCadAdapterVO.getPlanMotCad().getEsEspecial().getEsNO()){
				planMotCadAdapterVO.setFlagEsEspecial(false);
				planMotCadAdapterVO.getPlanMotCad().setClassName("");
			} else {
				planMotCadAdapterVO.setFlagEsEspecial(null);
				planMotCadAdapterVO.getPlanMotCad().setCantCuoAltView("");
				planMotCadAdapterVO.getPlanMotCad().setCantCuoConView("");
				planMotCadAdapterVO.getPlanMotCad().setCantDiasView("");
				planMotCadAdapterVO.getPlanMotCad().setClassName("");
			}
			
            // Tiene errores recuperables
			if (planMotCadAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planMotCadAdapterVO.infoString()); 
				saveDemodaErrors(request, planMotCadAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanMotCadAdapter.NAME, planMotCadAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (planMotCadAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planMotCadAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanMotCadAdapter.NAME, planMotCadAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(PlanMotCadAdapter.NAME, planMotCadAdapterVO);
			// Subo el apdater al userSession
			userSession.put(PlanMotCadAdapter.NAME, planMotCadAdapterVO);
			
			return mapping.findForward(GdeConstants.FWD_PLANMOTCAD_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanMotCadAdapter.NAME);
		}
	}
		
}
