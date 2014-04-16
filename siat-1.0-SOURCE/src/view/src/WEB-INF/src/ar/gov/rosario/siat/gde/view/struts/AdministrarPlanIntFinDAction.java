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
import ar.gov.rosario.siat.gde.iface.model.PlanIntFinAdapter;
import ar.gov.rosario.siat.gde.iface.model.PlanIntFinVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarPlanIntFinDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarPlanIntFinDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PLANINTFIN, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		PlanIntFinAdapter planIntFinAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getPlanIntFinAdapterForView(userSession, commonKey)";
				planIntFinAdapterVO = GdeServiceLocator.getDefinicionService().getPlanIntFinAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PLANINTFIN_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getPlanIntFinAdapterForUpdate(userSession, commonKey)";
				planIntFinAdapterVO = GdeServiceLocator.getDefinicionService().getPlanIntFinAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PLANINTFIN_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getPlanIntFinAdapterForView(userSession, commonKey)";
				planIntFinAdapterVO = GdeServiceLocator.getDefinicionService().getPlanIntFinAdapterForView(userSession, commonKey);				
				planIntFinAdapterVO.addMessage(BaseError.MSG_ELIMINAR, GdeError.PLANINTFIN_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_PLANINTFIN_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getPlanIntFinAdapterForCreate(userSession)";
				planIntFinAdapterVO = GdeServiceLocator.getDefinicionService().getPlanIntFinAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PLANINTFIN_EDIT_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (planIntFinAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + planIntFinAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanIntFinAdapter.NAME, planIntFinAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			planIntFinAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + PlanIntFinAdapter.NAME + ": "+ planIntFinAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(PlanIntFinAdapter.NAME, planIntFinAdapterVO);
			// Subo el apdater al userSession
			userSession.put(PlanIntFinAdapter.NAME, planIntFinAdapterVO);
			 
			saveDemodaMessages(request, planIntFinAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanIntFinAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PLANINTFIN, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanIntFinAdapter planIntFinAdapterVO = (PlanIntFinAdapter) userSession.get(PlanIntFinAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (planIntFinAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanIntFinAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanIntFinAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(planIntFinAdapterVO, request);
			
            // Tiene errores recuperables
			if (planIntFinAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planIntFinAdapterVO.infoString()); 
				saveDemodaErrors(request, planIntFinAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanIntFinAdapter.NAME, planIntFinAdapterVO);
			}
			
			// llamada al servicio
			PlanIntFinVO planIntFinVO = GdeServiceLocator.getDefinicionService().createPlanIntFin(userSession, planIntFinAdapterVO.getPlanIntFin());
			
            // Tiene errores recuperables
			if (planIntFinVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planIntFinVO.infoString()); 
				saveDemodaErrors(request, planIntFinVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanIntFinAdapter.NAME, planIntFinAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (planIntFinVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planIntFinVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanIntFinAdapter.NAME, planIntFinAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanIntFinAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanIntFinAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PLANINTFIN, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanIntFinAdapter planIntFinAdapterVO = (PlanIntFinAdapter) userSession.get(PlanIntFinAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (planIntFinAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanIntFinAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanIntFinAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(planIntFinAdapterVO, request);
			
            // Tiene errores recuperables
			if (planIntFinAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planIntFinAdapterVO.infoString()); 
				saveDemodaErrors(request, planIntFinAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanIntFinAdapter.NAME, planIntFinAdapterVO);
			}
			
			// llamada al servicio
			PlanIntFinVO planIntFinVO = GdeServiceLocator.getDefinicionService().updatePlanIntFin(userSession, planIntFinAdapterVO.getPlanIntFin());
			
            // Tiene errores recuperables
			if (planIntFinVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planIntFinAdapterVO.infoString()); 
				saveDemodaErrors(request, planIntFinVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanIntFinAdapter.NAME, planIntFinAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (planIntFinVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planIntFinAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanIntFinAdapter.NAME, planIntFinAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanIntFinAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanIntFinAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PLANINTFIN, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanIntFinAdapter planIntFinAdapterVO = (PlanIntFinAdapter) userSession.get(PlanIntFinAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (planIntFinAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanIntFinAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanIntFinAdapter.NAME); 
			}

			// llamada al servicio
			PlanIntFinVO planIntFinVO = GdeServiceLocator.getDefinicionService().deletePlanIntFin
				(userSession, planIntFinAdapterVO.getPlanIntFin());
			
            // Tiene errores recuperables
			if (planIntFinVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planIntFinAdapterVO.infoString());
				saveDemodaErrors(request, planIntFinVO);				
				request.setAttribute(PlanIntFinAdapter.NAME, planIntFinAdapterVO);
				return mapping.findForward(GdeConstants.FWD_PLANINTFIN_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (planIntFinVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planIntFinAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanIntFinAdapter.NAME, planIntFinAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanIntFinAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanIntFinAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, PlanIntFinAdapter.NAME);
		
	}
}
