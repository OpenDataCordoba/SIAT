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
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.gde.iface.model.PlanProrrogaAdapter;
import ar.gov.rosario.siat.gde.iface.model.PlanProrrogaVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarPlanProrrogaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarPlanProrrogaDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ADM_PLANPRORROGA, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		PlanProrrogaAdapter planProrrogaAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getPlanProrrogaAdapterForView(userSession, commonKey)";
				planProrrogaAdapterVO = GdeServiceLocator.getDefinicionService().getPlanProrrogaAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PLANPRORROGA_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getPlanProrrogaAdapterForUpdate(userSession, commonKey)";
				planProrrogaAdapterVO = GdeServiceLocator.getDefinicionService().getPlanProrrogaAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PLANPRORROGA_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getPlanProrrogaAdapterForView(userSession, commonKey)";
				planProrrogaAdapterVO = GdeServiceLocator.getDefinicionService().getPlanProrrogaAdapterForView(userSession, commonKey);				
				planProrrogaAdapterVO.addMessage(BaseError.MSG_ELIMINAR, GdeError.PLANPRORROGA_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_PLANPRORROGA_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getPlanProrrogaAdapterForCreate(userSession)";
				planProrrogaAdapterVO = GdeServiceLocator.getDefinicionService().getPlanProrrogaAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PLANPRORROGA_EDIT_ADAPTER);				
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (planProrrogaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + planProrrogaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanProrrogaAdapter.NAME, planProrrogaAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			planProrrogaAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + PlanProrrogaAdapter.NAME + ": "+ planProrrogaAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(PlanProrrogaAdapter.NAME, planProrrogaAdapterVO);
			// Subo el apdater al userSession
			userSession.put(PlanProrrogaAdapter.NAME, planProrrogaAdapterVO);
			 
			saveDemodaMessages(request, planProrrogaAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanProrrogaAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ADM_PLANPRORROGA, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanProrrogaAdapter planProrrogaAdapterVO = (PlanProrrogaAdapter) userSession.get(PlanProrrogaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (planProrrogaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanProrrogaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanProrrogaAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(planProrrogaAdapterVO, request);
			
            // Tiene errores recuperables
			if (planProrrogaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planProrrogaAdapterVO.infoString()); 
				saveDemodaErrors(request, planProrrogaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanProrrogaAdapter.NAME, planProrrogaAdapterVO);
			}
			
			// llamada al servicio
			PlanProrrogaVO planProrrogaVO = GdeServiceLocator.getDefinicionService().createPlanProrroga(userSession, planProrrogaAdapterVO.getPlanProrroga());
			
            // Tiene errores recuperables
			if (planProrrogaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planProrrogaVO.infoString()); 
				saveDemodaErrors(request, planProrrogaVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanProrrogaAdapter.NAME, planProrrogaAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (planProrrogaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planProrrogaVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanProrrogaAdapter.NAME, planProrrogaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanProrrogaAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanProrrogaAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ADM_PLANPRORROGA, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanProrrogaAdapter planProrrogaAdapterVO = (PlanProrrogaAdapter) userSession.get(PlanProrrogaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (planProrrogaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanProrrogaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanProrrogaAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(planProrrogaAdapterVO, request);
			
            // Tiene errores recuperables
			if (planProrrogaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planProrrogaAdapterVO.infoString()); 
				saveDemodaErrors(request, planProrrogaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanProrrogaAdapter.NAME, planProrrogaAdapterVO);
			}
			
			// llamada al servicio
			PlanProrrogaVO planProrrogaVO = GdeServiceLocator.getDefinicionService().updatePlanProrroga(userSession, planProrrogaAdapterVO.getPlanProrroga());
			
            // Tiene errores recuperables
			if (planProrrogaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planProrrogaAdapterVO.infoString()); 
				saveDemodaErrors(request, planProrrogaVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanProrrogaAdapter.NAME, planProrrogaAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (planProrrogaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planProrrogaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanProrrogaAdapter.NAME, planProrrogaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanProrrogaAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanProrrogaAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ADM_PLANPRORROGA, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanProrrogaAdapter planProrrogaAdapterVO = (PlanProrrogaAdapter) userSession.get(PlanProrrogaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (planProrrogaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanProrrogaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanProrrogaAdapter.NAME); 
			}

			// llamada al servicio
			PlanProrrogaVO planProrrogaVO = GdeServiceLocator.getDefinicionService().deletePlanProrroga
				(userSession, planProrrogaAdapterVO.getPlanProrroga());
			
            // Tiene errores recuperables
			if (planProrrogaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planProrrogaAdapterVO.infoString());
				saveDemodaErrors(request, planProrrogaVO);				
				request.setAttribute(PlanProrrogaAdapter.NAME, planProrrogaAdapterVO);
				return mapping.findForward(GdeConstants.FWD_PLANPRORROGA_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (planProrrogaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planProrrogaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanProrrogaAdapter.NAME, planProrrogaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanProrrogaAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanProrrogaAdapter.NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, PlanProrrogaAdapter.NAME);
		
	}
	
	public ActionForward validarCaso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanProrrogaAdapter adapterVO = (PlanProrrogaAdapter)userSession.get(PlanProrrogaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (adapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanProrrogaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanProrrogaAdapter.NAME); 
			}
			
			// Populate como en un buscar
			DemodaUtil.populateVO(adapterVO, request);
			
			log.debug( funcName + " " + adapterVO.getPlanProrroga().getCaso().infoString());
			
			// llamada al servicio
			CasServiceLocator.getCasCasoService().validarCaso(userSession, adapterVO.getPlanProrroga()); 
			
			adapterVO.getPlanProrroga().passErrorMessages(adapterVO);
		    
		    saveDemodaMessages(request, adapterVO);
		    saveDemodaErrors(request, adapterVO);
		    
			request.setAttribute(PlanProrrogaAdapter.NAME, adapterVO);
			
			return mapping.findForward( GdeConstants.FWD_PLANPRORROGA_EDIT_ADAPTER); 
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanProrrogaAdapter.NAME);
		}	
	}
	
}
