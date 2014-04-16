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
import ar.gov.rosario.siat.def.view.util.DefinitionUtil;
import ar.gov.rosario.siat.gde.iface.model.PlanAtrValAdapter;
import ar.gov.rosario.siat.gde.iface.model.PlanAtrValVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarPlanAtrValDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarPlanAtrValDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PLANATRVAL, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		PlanAtrValAdapter planAtrValAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getPlanAtrValAdapterForView(userSession, commonKey)";
				planAtrValAdapterVO = GdeServiceLocator.getDefinicionService().getPlanAtrValAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PLANATRVAL_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getPlanAtrValAdapterForUpdate(userSession, commonKey)";
				planAtrValAdapterVO = GdeServiceLocator.getDefinicionService().getPlanAtrValAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PLANATRVAL_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getPlanAtrValAdapterForView(userSession, commonKey)";
				planAtrValAdapterVO = GdeServiceLocator.getDefinicionService().getPlanAtrValAdapterForView(userSession, commonKey);				
				planAtrValAdapterVO.addMessage(BaseError.MSG_ELIMINAR, GdeError.PLANATRVAL_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_PLANATRVAL_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getPlanAtrValAdapterForCreate(userSession)";
				planAtrValAdapterVO = GdeServiceLocator.getDefinicionService().getPlanAtrValAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PLANATRVAL_EDIT_ADAPTER);				
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (planAtrValAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + planAtrValAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanAtrValAdapter.NAME, planAtrValAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			planAtrValAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + PlanAtrValAdapter.NAME + ": "+ planAtrValAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(PlanAtrValAdapter.NAME, planAtrValAdapterVO);
			// Subo el apdater al userSession
			userSession.put(PlanAtrValAdapter.NAME, planAtrValAdapterVO);
			 
			saveDemodaMessages(request, planAtrValAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanAtrValAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PLANATRVAL, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanAtrValAdapter planAtrValAdapterVO = (PlanAtrValAdapter) userSession.get(PlanAtrValAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (planAtrValAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanAtrValAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanAtrValAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(planAtrValAdapterVO, request);
			
            // Tiene errores recuperables
			if (planAtrValAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planAtrValAdapterVO.infoString()); 
				saveDemodaErrors(request, planAtrValAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanAtrValAdapter.NAME, planAtrValAdapterVO);
			}
			
			// Si es nulo no se puede continuar
			/*if (planAtrValAdapterVO.getGenericAtrDefinition().getIdDefinition() == null) {
				planAtrValAdapterVO.getGenericAtrDefinition().addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, "& Valor del Atributo");
			}else{*/
			
			if (planAtrValAdapterVO.getGenericAtrDefinition().getIdDefinition() != null){
				// Se realiza el populate de los atributos submitidos			
				DefinitionUtil.populateAtrVal4Edit(planAtrValAdapterVO.getGenericAtrDefinition(), request);
			
				// Si fue submitido el valor se setea bandera para mostrarlo si no pasa la validacion
				if (!planAtrValAdapterVO.getGenericAtrDefinition().getValorView().equals(""))
					planAtrValAdapterVO.getGenericAtrDefinition().setIsSubmited(true);
			
				// Se validan formatos
				planAtrValAdapterVO.getGenericAtrDefinition().clearError();
				planAtrValAdapterVO.getGenericAtrDefinition().validate4EditVig();
			}
			
            // Tiene errores recuperables
			if (planAtrValAdapterVO.getGenericAtrDefinition().hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planAtrValAdapterVO.infoString()); 
				saveDemodaErrors(request, planAtrValAdapterVO.getGenericAtrDefinition());
				return forwardErrorRecoverable(mapping, request, userSession, PlanAtrValAdapter.NAME, planAtrValAdapterVO);
			}
			
			// Antes de llamar al servicio, pasamos el valor del definition al disParPlaVO
			planAtrValAdapterVO.getPlanAtrVal().setValor(planAtrValAdapterVO.getGenericAtrDefinition().getValorString());
			
			
			// llamada al servicio
			PlanAtrValVO planAtrValVO = GdeServiceLocator.getDefinicionService().createPlanAtrVal(userSession, planAtrValAdapterVO.getPlanAtrVal());
			
            // Tiene errores recuperables
			if (planAtrValVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planAtrValVO.infoString()); 
				saveDemodaErrors(request, planAtrValVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanAtrValAdapter.NAME, planAtrValAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (planAtrValVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planAtrValVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanAtrValAdapter.NAME, planAtrValAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanAtrValAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanAtrValAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PLANATRVAL, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanAtrValAdapter planAtrValAdapterVO = (PlanAtrValAdapter) userSession.get(PlanAtrValAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (planAtrValAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanAtrValAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanAtrValAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(planAtrValAdapterVO, request);
			
            // Tiene errores recuperables
			if (planAtrValAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planAtrValAdapterVO.infoString()); 
				saveDemodaErrors(request, planAtrValAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanAtrValAdapter.NAME, planAtrValAdapterVO);
			}
			
			// llamada al servicio
			PlanAtrValVO planAtrValVO = GdeServiceLocator.getDefinicionService().updatePlanAtrVal(userSession, planAtrValAdapterVO.getPlanAtrVal());
			
            // Tiene errores recuperables
			if (planAtrValVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planAtrValAdapterVO.infoString()); 
				saveDemodaErrors(request, planAtrValVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanAtrValAdapter.NAME, planAtrValAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (planAtrValVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planAtrValAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanAtrValAdapter.NAME, planAtrValAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanAtrValAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanAtrValAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PLANATRVAL, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanAtrValAdapter planAtrValAdapterVO = (PlanAtrValAdapter) userSession.get(PlanAtrValAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (planAtrValAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanAtrValAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanAtrValAdapter.NAME); 
			}

			// llamada al servicio
			PlanAtrValVO planAtrValVO = GdeServiceLocator.getDefinicionService().deletePlanAtrVal
				(userSession, planAtrValAdapterVO.getPlanAtrVal());
			
            // Tiene errores recuperables
			if (planAtrValVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planAtrValAdapterVO.infoString());
				saveDemodaErrors(request, planAtrValVO);				
				request.setAttribute(PlanAtrValAdapter.NAME, planAtrValAdapterVO);
				return mapping.findForward(GdeConstants.FWD_PLANATRVAL_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (planAtrValVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planAtrValAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanAtrValAdapter.NAME, planAtrValAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanAtrValAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanAtrValAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, PlanAtrValAdapter.NAME);
		
	}
	
	public ActionForward paramAtributo (ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanAtrValAdapter planAtrValAdapterVO = (PlanAtrValAdapter) userSession.get(PlanAtrValAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (planAtrValAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanAtrValAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanAtrValAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(planAtrValAdapterVO, request);
			
            // Tiene errores recuperables
			if (planAtrValAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planAtrValAdapterVO.infoString()); 
				saveDemodaErrors(request, planAtrValAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanAtrValAdapter.NAME, planAtrValAdapterVO);
			}
			
			// llamada al servicio
			planAtrValAdapterVO = GdeServiceLocator.getDefinicionService().getPlanAtrValAdapterParamAtributo(userSession, planAtrValAdapterVO);
			
            // Tiene errores recuperables
			if (planAtrValAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planAtrValAdapterVO.infoString()); 
				saveDemodaErrors(request, planAtrValAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanAtrValAdapter.NAME, planAtrValAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (planAtrValAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planAtrValAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanAtrValAdapter.NAME, planAtrValAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(PlanAtrValAdapter.NAME, planAtrValAdapterVO);
			// Subo el apdater al userSession
			userSession.put(PlanAtrValAdapter.NAME, planAtrValAdapterVO);
			
			return mapping.findForward(GdeConstants.FWD_PLANATRVAL_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanAtrValAdapter.NAME);
		}
	}
		
}
