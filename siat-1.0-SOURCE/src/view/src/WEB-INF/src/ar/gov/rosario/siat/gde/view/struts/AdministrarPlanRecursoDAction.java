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
import ar.gov.rosario.siat.gde.iface.model.PlanRecursoAdapter;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarPlanRecursoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarPlanRecursoDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PLANRECURSO, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		PlanRecursoAdapter planRecursoAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getPlanRecursoAdapterForView(userSession, commonKey)";
				planRecursoAdapterVO = GdeServiceLocator.getDefinicionService().getPlanRecursoAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PLANRECURSOVIEW);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getPlanRecursoAdapterForUpdate(userSession, commonKey)";
				planRecursoAdapterVO = GdeServiceLocator.getDefinicionService().getPlanRecursoAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PLANRECURSOEDIT);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getPlanRecursoAdapterForView(userSession, commonKey)";
				planRecursoAdapterVO = GdeServiceLocator.getDefinicionService().getPlanRecursoAdapterForView(userSession, commonKey);				
				planRecursoAdapterVO.addMessage(BaseError.MSG_ELIMINAR, GdeError.PLANDESCUENTO_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_PLANRECURSOVIEW);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getPlanRecursoAdapterForCreate(userSession)";
				planRecursoAdapterVO = GdeServiceLocator.getDefinicionService().getPlanRecursoAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PLANRECURSOEDIT);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (planRecursoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + planRecursoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanDescuentoAdapter.NAME, planRecursoAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			planRecursoAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + PlanDescuentoAdapter.NAME + ": "+ planRecursoAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(PlanRecursoAdapter.NAME, planRecursoAdapterVO);
			// Subo el apdater al userSession
			userSession.put(PlanRecursoAdapter.NAME, planRecursoAdapterVO);
			 
			saveDemodaMessages(request, planRecursoAdapterVO);
			
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
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PLANRECURSO, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanRecursoAdapter planRecursoAdapterVO = (PlanRecursoAdapter) userSession.get(PlanRecursoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (planRecursoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanDescuentoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanRecursoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(planRecursoAdapterVO, request);
			
            // Tiene errores recuperables
			if (planRecursoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planRecursoAdapterVO.infoString());
				userSession.put(PlanRecursoAdapter.NAME, planRecursoAdapterVO);
				request.setAttribute(PlanRecursoAdapter.NAME, planRecursoAdapterVO);
				
				saveDemodaErrors(request, planRecursoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanRecursoAdapter.NAME, planRecursoAdapterVO);
			}
			
			// llamada al servicio
			planRecursoAdapterVO = GdeServiceLocator.getDefinicionService().createPlanRecurso(userSession, planRecursoAdapterVO);
			
            // Tiene errores recuperables
			if (planRecursoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planRecursoAdapterVO.infoString());
				userSession.put(PlanRecursoAdapter.NAME, planRecursoAdapterVO);
				request.setAttribute(PlanRecursoAdapter.NAME, planRecursoAdapterVO);
				saveDemodaErrors(request, planRecursoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanRecursoAdapter.NAME, planRecursoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (planRecursoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planRecursoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanRecursoAdapter.NAME, planRecursoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanRecursoAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanRecursoAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PLANRECURSO, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanRecursoAdapter planRecursoAdapterVO = (PlanRecursoAdapter) userSession.get(PlanRecursoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (planRecursoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanRecursoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanRecursoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(planRecursoAdapterVO, request);
			
            // Tiene errores recuperables
			if (planRecursoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planRecursoAdapterVO.infoString()); 
				saveDemodaErrors(request, planRecursoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanRecursoAdapter.NAME, planRecursoAdapterVO);
			}
			
			// llamada al servicio
			 planRecursoAdapterVO = GdeServiceLocator.getDefinicionService().updatePlanRecurso(userSession, planRecursoAdapterVO);
			
            // Tiene errores recuperables
			if (planRecursoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planRecursoAdapterVO.infoString()); 
				saveDemodaErrors(request, planRecursoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanRecursoAdapter.NAME, planRecursoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (planRecursoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planRecursoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanRecursoAdapter.NAME, planRecursoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanRecursoAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanRecursoAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PLANRECURSO, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanRecursoAdapter planRecursoAdapterVO = (PlanRecursoAdapter) userSession.get(PlanRecursoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (planRecursoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanDescuentoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanDescuentoAdapter.NAME); 
			}

			// llamada al servicio
			planRecursoAdapterVO = GdeServiceLocator.getDefinicionService().deletePlanRecurso
				(userSession, planRecursoAdapterVO);
			
            // Tiene errores recuperables
			if (planRecursoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planRecursoAdapterVO.infoString());
				saveDemodaErrors(request, planRecursoAdapterVO);				
				request.setAttribute(PlanRecursoAdapter.NAME, planRecursoAdapterVO);
				return mapping.findForward(GdeConstants.FWD_PLANRECURSOVIEW);
			}
			
			// Tiene errores no recuperables
			if (planRecursoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planRecursoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanRecursoAdapter.NAME, planRecursoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanRecursoAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanRecursoAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, PlanRecursoAdapter.NAME);
		
	}
}
