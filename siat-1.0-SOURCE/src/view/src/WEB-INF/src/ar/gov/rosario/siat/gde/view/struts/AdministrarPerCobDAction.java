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
import ar.gov.rosario.siat.gde.iface.model.PerCobAdapter;
import ar.gov.rosario.siat.gde.iface.model.PerCobVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarPerCobDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarPerCobDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PERCOB, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		PerCobAdapter perCobAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getPerCobAdapterForView(userSession, commonKey)";
				perCobAdapterVO = GdeServiceLocator.getDefinicionService().getPerCobAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PERCOB_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getPerCobAdapterForUpdate(userSession, commonKey)";
				perCobAdapterVO = GdeServiceLocator.getDefinicionService().getPerCobAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PERCOB_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getPerCobAdapterForView(userSession, commonKey)";
				perCobAdapterVO = GdeServiceLocator.getDefinicionService().getPerCobAdapterForView(userSession, commonKey);				
				perCobAdapterVO.addMessage(BaseError.MSG_ELIMINAR, GdeError.PERCOB_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_PERCOB_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getPerCobAdapterForCreate(userSession)";
				perCobAdapterVO = GdeServiceLocator.getDefinicionService().getPerCobAdapterForCreate(userSession,commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PERCOB_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getPerCobAdapterForView(userSession)";
				perCobAdapterVO = GdeServiceLocator.getDefinicionService().getPerCobAdapterForView(userSession, commonKey);
				perCobAdapterVO.addMessage(BaseError.MSG_ACTIVAR, GdeError.PERCOB_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_PERCOB_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getPerCobAdapterForView(userSession)";
				perCobAdapterVO = GdeServiceLocator.getDefinicionService().getPerCobAdapterForView(userSession, commonKey);
				perCobAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, GdeError.PERCOB_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_PERCOB_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (perCobAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + perCobAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PerCobAdapter.NAME, perCobAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			perCobAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + PerCobAdapter.NAME + ": "+ perCobAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(PerCobAdapter.NAME, perCobAdapterVO);
			// Subo el apdater al userSession
			userSession.put(PerCobAdapter.NAME, perCobAdapterVO);
			 
			saveDemodaMessages(request, perCobAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PerCobAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PERCOB, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PerCobAdapter perCobAdapterVO = (PerCobAdapter) userSession.get(PerCobAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (perCobAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PerCobAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PerCobAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(perCobAdapterVO, request);
			
            // Tiene errores recuperables
			if (perCobAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + perCobAdapterVO.infoString()); 
				saveDemodaErrors(request, perCobAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PerCobAdapter.NAME, perCobAdapterVO);
			}
			
			// llamada al servicio
			PerCobVO perCobVO = GdeServiceLocator.getDefinicionService().createPerCob(userSession, perCobAdapterVO.getPerCob());
			
            // Tiene errores recuperables
			if (perCobVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + perCobVO.infoString()); 
				saveDemodaErrors(request, perCobVO);
				return forwardErrorRecoverable(mapping, request, userSession, PerCobAdapter.NAME, perCobAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (perCobVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + perCobVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PerCobAdapter.NAME, perCobAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PerCobAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PerCobAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PERCOB, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PerCobAdapter perCobAdapterVO = (PerCobAdapter) userSession.get(PerCobAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (perCobAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PerCobAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PerCobAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(perCobAdapterVO, request);
			
            // Tiene errores recuperables
			if (perCobAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + perCobAdapterVO.infoString()); 
				saveDemodaErrors(request, perCobAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PerCobAdapter.NAME, perCobAdapterVO);
			}
			
			// llamada al servicio
			PerCobVO perCobVO = GdeServiceLocator.getDefinicionService().updatePerCob(userSession, perCobAdapterVO.getPerCob());
			
            // Tiene errores recuperables
			if (perCobVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + perCobAdapterVO.infoString()); 
				saveDemodaErrors(request, perCobVO);
				return forwardErrorRecoverable(mapping, request, userSession, PerCobAdapter.NAME, perCobAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (perCobVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + perCobAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PerCobAdapter.NAME, perCobAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PerCobAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PerCobAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PERCOB, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PerCobAdapter perCobAdapterVO = (PerCobAdapter) userSession.get(PerCobAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (perCobAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PerCobAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PerCobAdapter.NAME); 
			}

			// llamada al servicio
			PerCobVO perCobVO = GdeServiceLocator.getDefinicionService().deletePerCob(userSession, perCobAdapterVO.getPerCob());
			
            // Tiene errores recuperables
			if (perCobVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + perCobAdapterVO.infoString());
				saveDemodaErrors(request, perCobVO);				
				request.setAttribute(PerCobAdapter.NAME, perCobAdapterVO);
				return mapping.findForward(GdeConstants.FWD_PERCOB_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (perCobVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + perCobAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PerCobAdapter.NAME, perCobAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PerCobAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PerCobAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PERCOB, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PerCobAdapter perCobAdapterVO = (PerCobAdapter) userSession.get(PerCobAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (perCobAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PerCobAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PerCobAdapter.NAME); 
			}

			// llamada al servicio
			PerCobVO perCobVO = GdeServiceLocator.getDefinicionService().activarPerCob(userSession, perCobAdapterVO.getPerCob());
			
            // Tiene errores recuperables
			if (perCobVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + perCobAdapterVO.infoString());
				saveDemodaErrors(request, perCobVO);				
				request.setAttribute(PerCobAdapter.NAME, perCobAdapterVO);
				return mapping.findForward(GdeConstants.FWD_PERCOB_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (perCobVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + perCobAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PerCobAdapter.NAME, perCobAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PerCobAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PerCobAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PERCOB, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PerCobAdapter perCobAdapterVO = (PerCobAdapter) userSession.get(PerCobAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (perCobAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PerCobAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PerCobAdapter.NAME); 
			}

			// llamada al servicio
			PerCobVO perCobVO = GdeServiceLocator.getDefinicionService().desactivarPerCob(userSession, perCobAdapterVO.getPerCob());
			
            // Tiene errores recuperables
			if (perCobVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + perCobAdapterVO.infoString());
				saveDemodaErrors(request, perCobVO);				
				request.setAttribute(PerCobAdapter.NAME, perCobAdapterVO);
				return mapping.findForward(GdeConstants.FWD_PERCOB_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (perCobVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + perCobAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PerCobAdapter.NAME, perCobAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PerCobAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PerCobAdapter.NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, PerCobAdapter.NAME);
		
	}		
		
}
