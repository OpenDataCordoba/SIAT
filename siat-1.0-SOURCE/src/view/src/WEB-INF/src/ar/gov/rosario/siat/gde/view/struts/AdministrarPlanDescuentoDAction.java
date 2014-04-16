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
import ar.gov.rosario.siat.gde.iface.model.PlanDescuentoAdapter;
import ar.gov.rosario.siat.gde.iface.model.PlanDescuentoVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarPlanDescuentoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarPlanDescuentoDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PLANDESCUENTO, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		PlanDescuentoAdapter planDescuentoAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getPlanDescuentoAdapterForView(userSession, commonKey)";
				planDescuentoAdapterVO = GdeServiceLocator.getDefinicionService().getPlanDescuentoAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PLANDESCUENTO_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getPlanDescuentoAdapterForUpdate(userSession, commonKey)";
				planDescuentoAdapterVO = GdeServiceLocator.getDefinicionService().getPlanDescuentoAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PLANDESCUENTO_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getPlanDescuentoAdapterForView(userSession, commonKey)";
				planDescuentoAdapterVO = GdeServiceLocator.getDefinicionService().getPlanDescuentoAdapterForView(userSession, commonKey);				
				planDescuentoAdapterVO.addMessage(BaseError.MSG_ELIMINAR, GdeError.PLANDESCUENTO_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_PLANDESCUENTO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getPlanDescuentoAdapterForCreate(userSession)";
				planDescuentoAdapterVO = GdeServiceLocator.getDefinicionService().getPlanDescuentoAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PLANDESCUENTO_EDIT_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (planDescuentoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + planDescuentoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanDescuentoAdapter.NAME, planDescuentoAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			planDescuentoAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + PlanDescuentoAdapter.NAME + ": "+ planDescuentoAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(PlanDescuentoAdapter.NAME, planDescuentoAdapterVO);
			// Subo el apdater al userSession
			userSession.put(PlanDescuentoAdapter.NAME, planDescuentoAdapterVO);
			 
			saveDemodaMessages(request, planDescuentoAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanDescuentoAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PLANDESCUENTO, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanDescuentoAdapter planDescuentoAdapterVO = (PlanDescuentoAdapter) userSession.get(PlanDescuentoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (planDescuentoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanDescuentoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanDescuentoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(planDescuentoAdapterVO, request);
			
            // Tiene errores recuperables
			if (planDescuentoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planDescuentoAdapterVO.infoString()); 
				saveDemodaErrors(request, planDescuentoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanDescuentoAdapter.NAME, planDescuentoAdapterVO);
			}
			
			// llamada al servicio
			PlanDescuentoVO planDescuentoVO = GdeServiceLocator.getDefinicionService().createPlanDescuento(userSession, planDescuentoAdapterVO.getPlanDescuento());
			
            // Tiene errores recuperables
			if (planDescuentoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planDescuentoVO.infoString()); 
				saveDemodaErrors(request, planDescuentoVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanDescuentoAdapter.NAME, planDescuentoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (planDescuentoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planDescuentoVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanDescuentoAdapter.NAME, planDescuentoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanDescuentoAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanDescuentoAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PLANDESCUENTO, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanDescuentoAdapter planDescuentoAdapterVO = (PlanDescuentoAdapter) userSession.get(PlanDescuentoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (planDescuentoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanDescuentoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanDescuentoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(planDescuentoAdapterVO, request);
			
            // Tiene errores recuperables
			if (planDescuentoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planDescuentoAdapterVO.infoString()); 
				saveDemodaErrors(request, planDescuentoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanDescuentoAdapter.NAME, planDescuentoAdapterVO);
			}
			
			// llamada al servicio
			PlanDescuentoVO planDescuentoVO = GdeServiceLocator.getDefinicionService().updatePlanDescuento(userSession, planDescuentoAdapterVO.getPlanDescuento());
			
            // Tiene errores recuperables
			if (planDescuentoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planDescuentoAdapterVO.infoString()); 
				saveDemodaErrors(request, planDescuentoVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanDescuentoAdapter.NAME, planDescuentoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (planDescuentoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planDescuentoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanDescuentoAdapter.NAME, planDescuentoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanDescuentoAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanDescuentoAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PLANDESCUENTO, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanDescuentoAdapter planDescuentoAdapterVO = (PlanDescuentoAdapter) userSession.get(PlanDescuentoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (planDescuentoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanDescuentoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanDescuentoAdapter.NAME); 
			}

			// llamada al servicio
			PlanDescuentoVO planDescuentoVO = GdeServiceLocator.getDefinicionService().deletePlanDescuento
				(userSession, planDescuentoAdapterVO.getPlanDescuento());
			
            // Tiene errores recuperables
			if (planDescuentoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planDescuentoAdapterVO.infoString());
				saveDemodaErrors(request, planDescuentoVO);				
				request.setAttribute(PlanDescuentoAdapter.NAME, planDescuentoAdapterVO);
				return mapping.findForward(GdeConstants.FWD_PLANDESCUENTO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (planDescuentoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planDescuentoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanDescuentoAdapter.NAME, planDescuentoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanDescuentoAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanDescuentoAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, PlanDescuentoAdapter.NAME);
		
	}
}
