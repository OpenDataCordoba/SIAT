//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.view.struts;

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
import ar.gov.rosario.siat.ef.iface.model.PlanFiscalAdapter;
import ar.gov.rosario.siat.ef.iface.model.PlanFiscalVO;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfError;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarPlanFiscalDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarPlanFiscalDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ADM_PLANFISCAL, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		PlanFiscalAdapter planFiscalAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getPlanFiscalAdapterForView(userSession, commonKey)";
				planFiscalAdapterVO = EfServiceLocator.getInvestigacionService().getPlanFiscalAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_PLANFISCAL_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getPlanFiscalAdapterForUpdate(userSession, commonKey)";
				planFiscalAdapterVO = EfServiceLocator.getInvestigacionService().getPlanFiscalAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_PLANFISCAL_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getPlanFiscalAdapterForView(userSession, commonKey)";
				planFiscalAdapterVO = EfServiceLocator.getInvestigacionService().getPlanFiscalAdapterForView(userSession, commonKey);				
				planFiscalAdapterVO.addMessage(BaseError.MSG_ELIMINAR, EfError.PLANFISCAL_LABEL);
				actionForward = mapping.findForward(EfConstants.FWD_PLANFISCAL_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getPlanFiscalAdapterForCreate(userSession)";
				planFiscalAdapterVO = EfServiceLocator.getInvestigacionService().getPlanFiscalAdapterForCreate(userSession);
				actionForward = mapping.findForward(EfConstants.FWD_PLANFISCAL_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getPlanFiscalAdapterForView(userSession)";
				planFiscalAdapterVO = EfServiceLocator.getInvestigacionService().getPlanFiscalAdapterForView(userSession, commonKey);
				planFiscalAdapterVO.addMessage(BaseError.MSG_ACTIVAR, EfError.PLANFISCAL_LABEL);
				actionForward = mapping.findForward(EfConstants.FWD_PLANFISCAL_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getPlanFiscalAdapterForView(userSession)";
				planFiscalAdapterVO = EfServiceLocator.getInvestigacionService().getPlanFiscalAdapterForView(userSession, commonKey);
				planFiscalAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, EfError.PLANFISCAL_LABEL);
				actionForward = mapping.findForward(EfConstants.FWD_PLANFISCAL_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (planFiscalAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + planFiscalAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanFiscalAdapter.NAME, planFiscalAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			planFiscalAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + PlanFiscalAdapter.NAME + ": "+ planFiscalAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(PlanFiscalAdapter.NAME, planFiscalAdapterVO);
			// Subo el apdater al userSession
			userSession.put(PlanFiscalAdapter.NAME, planFiscalAdapterVO);
			 
			saveDemodaMessages(request, planFiscalAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanFiscalAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ADM_PLANFISCAL, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanFiscalAdapter planFiscalAdapterVO = (PlanFiscalAdapter) userSession.get(PlanFiscalAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (planFiscalAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanFiscalAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanFiscalAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(planFiscalAdapterVO, request);
			
            // Tiene errores recuperables
			if (planFiscalAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planFiscalAdapterVO.infoString()); 
				saveDemodaErrors(request, planFiscalAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanFiscalAdapter.NAME, planFiscalAdapterVO);
			}
			
			// llamada al servicio
			PlanFiscalVO planFiscalVO = EfServiceLocator.getInvestigacionService().createPlanFiscal(userSession, planFiscalAdapterVO.getPlanFiscal());
			
            // Tiene errores recuperables
			if (planFiscalVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planFiscalVO.infoString()); 
				saveDemodaErrors(request, planFiscalVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanFiscalAdapter.NAME, planFiscalAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (planFiscalVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planFiscalVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanFiscalAdapter.NAME, planFiscalAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanFiscalAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanFiscalAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ADM_PLANFISCAL, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanFiscalAdapter planFiscalAdapterVO = (PlanFiscalAdapter) userSession.get(PlanFiscalAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (planFiscalAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanFiscalAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanFiscalAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(planFiscalAdapterVO, request);
			
            // Tiene errores recuperables
			if (planFiscalAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planFiscalAdapterVO.infoString()); 
				saveDemodaErrors(request, planFiscalAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanFiscalAdapter.NAME, planFiscalAdapterVO);
			}
			
			// llamada al servicio
			PlanFiscalVO planFiscalVO = EfServiceLocator.getInvestigacionService().updatePlanFiscal(userSession, planFiscalAdapterVO.getPlanFiscal());
			
            // Tiene errores recuperables
			if (planFiscalVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planFiscalAdapterVO.infoString()); 
				saveDemodaErrors(request, planFiscalVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanFiscalAdapter.NAME, planFiscalAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (planFiscalVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planFiscalAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanFiscalAdapter.NAME, planFiscalAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanFiscalAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanFiscalAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ADM_PLANFISCAL, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanFiscalAdapter planFiscalAdapterVO = (PlanFiscalAdapter) userSession.get(PlanFiscalAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (planFiscalAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanFiscalAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanFiscalAdapter.NAME); 
			}

			// llamada al servicio
			PlanFiscalVO planFiscalVO = EfServiceLocator.getInvestigacionService().deletePlanFiscal
				(userSession, planFiscalAdapterVO.getPlanFiscal());
			
            // Tiene errores recuperables
			if (planFiscalVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planFiscalAdapterVO.infoString());
				saveDemodaErrors(request, planFiscalVO);				
				request.setAttribute(PlanFiscalAdapter.NAME, planFiscalAdapterVO);
				return mapping.findForward(EfConstants.FWD_PLANFISCAL_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (planFiscalVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planFiscalAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanFiscalAdapter.NAME, planFiscalAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanFiscalAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanFiscalAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ADM_PLANFISCAL, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanFiscalAdapter planFiscalAdapterVO = (PlanFiscalAdapter) userSession.get(PlanFiscalAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (planFiscalAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanFiscalAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanFiscalAdapter.NAME); 
			}

			// llamada al servicio
			PlanFiscalVO planFiscalVO = EfServiceLocator.getInvestigacionService().activarPlanFiscal
				(userSession, planFiscalAdapterVO.getPlanFiscal());
			
            // Tiene errores recuperables
			if (planFiscalVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planFiscalAdapterVO.infoString());
				saveDemodaErrors(request, planFiscalVO);				
				request.setAttribute(PlanFiscalAdapter.NAME, planFiscalAdapterVO);
				return mapping.findForward(EfConstants.FWD_PLANFISCAL_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (planFiscalVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planFiscalAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanFiscalAdapter.NAME, planFiscalAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanFiscalAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanFiscalAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ADM_PLANFISCAL, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanFiscalAdapter planFiscalAdapterVO = (PlanFiscalAdapter) userSession.get(PlanFiscalAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (planFiscalAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanFiscalAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanFiscalAdapter.NAME); 
			}

			// llamada al servicio
			PlanFiscalVO planFiscalVO = EfServiceLocator.getInvestigacionService().desactivarPlanFiscal
				(userSession, planFiscalAdapterVO.getPlanFiscal());
			
            // Tiene errores recuperables
			if (planFiscalVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planFiscalAdapterVO.infoString());
				saveDemodaErrors(request, planFiscalVO);				
				request.setAttribute(PlanFiscalAdapter.NAME, planFiscalAdapterVO);
				return mapping.findForward(EfConstants.FWD_PLANFISCAL_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (planFiscalVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planFiscalAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanFiscalAdapter.NAME, planFiscalAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanFiscalAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanFiscalAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, PlanFiscalAdapter.NAME);
		
	}		
}
