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
import ar.gov.rosario.siat.gde.iface.model.PlanExeAdapter;
import ar.gov.rosario.siat.gde.iface.model.PlanExeVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarPlanExeDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarPlanExeDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PLANEXE, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		PlanExeAdapter planExeAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getPlanExeAdapterForView(userSession, commonKey)";
				planExeAdapterVO = GdeServiceLocator.getDefinicionService().getPlanExeAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PLANEXE_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getPlanExeAdapterForUpdate(userSession, commonKey)";
				planExeAdapterVO = GdeServiceLocator.getDefinicionService().getPlanExeAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PLANEXE_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getPlanExeAdapterForView(userSession, commonKey)";
				planExeAdapterVO = GdeServiceLocator.getDefinicionService().getPlanExeAdapterForView(userSession, commonKey);				
				planExeAdapterVO.addMessage(BaseError.MSG_ELIMINAR, GdeError.PLANEXE_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_PLANEXE_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getPlanExeAdapterForCreate(userSession)";
				planExeAdapterVO = GdeServiceLocator.getDefinicionService().getPlanExeAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PLANEXE_EDIT_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (planExeAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + planExeAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanExeAdapter.NAME, planExeAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			planExeAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + PlanExeAdapter.NAME + ": "+ planExeAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(PlanExeAdapter.NAME, planExeAdapterVO);
			// Subo el apdater al userSession
			userSession.put(PlanExeAdapter.NAME, planExeAdapterVO);
			 
			saveDemodaMessages(request, planExeAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanExeAdapter.NAME);
		}
	}
	
	public ActionForward paramRecurso (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception{
		
		String funcName = DemodaUtil.currentMethodName();
		
		if(log.isDebugEnabled())log.debug(funcName + " :enter");
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		
		try{
			PlanExeAdapter planExeAdapter = (PlanExeAdapter)userSession.get(PlanExeAdapter.NAME);
			DemodaUtil.populateVO(planExeAdapter, request);
			
			planExeAdapter = GdeServiceLocator.getDefinicionService().getPlanExeParam(userSession, planExeAdapter);
			
			userSession.put(PlanExeAdapter.NAME, planExeAdapter);
			request.setAttribute(PlanExeAdapter.NAME, planExeAdapter);
			
			return mapping.findForward(GdeConstants.FWD_PLANEXE_EDIT_ADAPTER);
			
		}catch (Exception exception){
			return baseException(mapping, request, funcName, exception, PlanClaDeuAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PLANEXE, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanExeAdapter planExeAdapterVO = (PlanExeAdapter) userSession.get(PlanExeAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (planExeAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanExeAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanExeAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(planExeAdapterVO, request);
			
            // Tiene errores recuperables
			if (planExeAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planExeAdapterVO.infoString()); 
				saveDemodaErrors(request, planExeAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanExeAdapter.NAME, planExeAdapterVO);
			}
			
			// llamada al servicio
			PlanExeVO planExeVO = GdeServiceLocator.getDefinicionService().createPlanExe(userSession, planExeAdapterVO.getPlanExe());
			
            // Tiene errores recuperables
			if (planExeVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planExeVO.infoString()); 
				saveDemodaErrors(request, planExeVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanExeAdapter.NAME, planExeAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (planExeVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planExeVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanExeAdapter.NAME, planExeAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanExeAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanExeAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PLANEXE, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanExeAdapter planExeAdapterVO = (PlanExeAdapter) userSession.get(PlanExeAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (planExeAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanExeAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanExeAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(planExeAdapterVO, request);
			
            // Tiene errores recuperables
			if (planExeAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planExeAdapterVO.infoString()); 
				saveDemodaErrors(request, planExeAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanExeAdapter.NAME, planExeAdapterVO);
			}
			
			// llamada al servicio
			PlanExeVO planExeVO = GdeServiceLocator.getDefinicionService().updatePlanExe(userSession, planExeAdapterVO.getPlanExe());
			
            // Tiene errores recuperables
			if (planExeVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planExeAdapterVO.infoString()); 
				saveDemodaErrors(request, planExeVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanExeAdapter.NAME, planExeAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (planExeVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planExeAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanExeAdapter.NAME, planExeAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanExeAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanExeAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PLANEXE, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanExeAdapter planExeAdapterVO = (PlanExeAdapter) userSession.get(PlanExeAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (planExeAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanExeAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanExeAdapter.NAME); 
			}

			// llamada al servicio
			PlanExeVO planExeVO = GdeServiceLocator.getDefinicionService().deletePlanExe
				(userSession, planExeAdapterVO.getPlanExe());
			
            // Tiene errores recuperables
			if (planExeVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planExeAdapterVO.infoString());
				saveDemodaErrors(request, planExeVO);				
				request.setAttribute(PlanExeAdapter.NAME, planExeAdapterVO);
				return mapping.findForward(GdeConstants.FWD_PLANEXE_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (planExeVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planExeAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanExeAdapter.NAME, planExeAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanExeAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanExeAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, PlanExeAdapter.NAME);
		
	}
			
}
