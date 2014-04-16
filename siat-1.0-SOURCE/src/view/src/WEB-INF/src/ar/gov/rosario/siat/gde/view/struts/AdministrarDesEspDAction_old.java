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
import ar.gov.rosario.siat.gde.iface.model.DesEspAdapter;
import ar.gov.rosario.siat.gde.iface.model.DesEspVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarDesEspDAction_old extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarDesEspDAction_old.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DESESP, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		DesEspAdapter desespAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getDesEspAdapterForView(userSession, commonKey)";
				desespAdapterVO = GdeServiceLocator.getDefinicionService().getDesEspAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_DESESP_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getDesEspAdapterForUpdate(userSession, commonKey)";
				desespAdapterVO = GdeServiceLocator.getDefinicionService().getDesEspAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_DESESP_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getDesEspAdapterForView(userSession, commonKey)";
				desespAdapterVO = GdeServiceLocator.getDefinicionService().getDesEspAdapterForView(userSession, commonKey);				
				desespAdapterVO.addMessage(BaseError.MSG_ELIMINAR, GdeError.DESESP_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_DESESP_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getDesEspAdapterForCreate(userSession)";
				desespAdapterVO = GdeServiceLocator.getDefinicionService().getDesEspAdapterForCreate(userSession);
				actionForward = mapping.findForward(GdeConstants.FWD_DESESP_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getDesEspAdapterForView(userSession)";
				desespAdapterVO = GdeServiceLocator.getDefinicionService().getDesEspAdapterForView(userSession, commonKey);
				desespAdapterVO.addMessage(BaseError.MSG_ACTIVAR, GdeError.DESESP_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_DESESP_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getDesEspAdapterForView(userSession)";
				desespAdapterVO = GdeServiceLocator.getDefinicionService().getDesEspAdapterForView(userSession, commonKey);
				desespAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, GdeError.DESESP_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_DESESP_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (desespAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + desespAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DesEspAdapter.NAME, desespAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			desespAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + DesEspAdapter.NAME + ": "+ desespAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(DesEspAdapter.NAME, desespAdapterVO);
			// Subo el apdater al userSession
			userSession.put(DesEspAdapter.NAME, desespAdapterVO);
			 
			saveDemodaMessages(request, desespAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DesEspAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DESESP, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DesEspAdapter desespAdapterVO = (DesEspAdapter) userSession.get(DesEspAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (desespAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DesEspAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DesEspAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(desespAdapterVO, request);
			
            // Tiene errores recuperables
			if (desespAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desespAdapterVO.infoString()); 
				saveDemodaErrors(request, desespAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, DesEspAdapter.NAME, desespAdapterVO);
			}
			
			// llamada al servicio
			DesEspVO desespVO = GdeServiceLocator.getDefinicionService().createDesEsp(userSession, desespAdapterVO.getDesEsp());
			
            // Tiene errores recuperables
			if (desespVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desespVO.infoString()); 
				saveDemodaErrors(request, desespVO);
				return forwardErrorRecoverable(mapping, request, userSession, DesEspAdapter.NAME, desespAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (desespVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + desespVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DesEspAdapter.NAME, desespAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DesEspAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DesEspAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DESESP, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DesEspAdapter desespAdapterVO = (DesEspAdapter) userSession.get(DesEspAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (desespAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DesEspAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DesEspAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(desespAdapterVO, request);
			
            // Tiene errores recuperables
			if (desespAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desespAdapterVO.infoString()); 
				saveDemodaErrors(request, desespAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, DesEspAdapter.NAME, desespAdapterVO);
			}
			
			// llamada al servicio
			DesEspVO desespVO = GdeServiceLocator.getDefinicionService().updateDesEsp(userSession, desespAdapterVO.getDesEsp());
			
            // Tiene errores recuperables
			if (desespVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desespAdapterVO.infoString()); 
				saveDemodaErrors(request, desespVO);
				return forwardErrorRecoverable(mapping, request, userSession, DesEspAdapter.NAME, desespAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (desespVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + desespAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DesEspAdapter.NAME, desespAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DesEspAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DesEspAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DESESP, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DesEspAdapter desespAdapterVO = (DesEspAdapter) userSession.get(DesEspAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (desespAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DesEspAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DesEspAdapter.NAME); 
			}

			// llamada al servicio
			DesEspVO desespVO = GdeServiceLocator.getDefinicionService().deleteDesEsp
				(userSession, desespAdapterVO.getDesEsp());
			
            // Tiene errores recuperables
			if (desespVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desespAdapterVO.infoString());
				saveDemodaErrors(request, desespVO);				
				request.setAttribute(DesEspAdapter.NAME, desespAdapterVO);
				return mapping.findForward(GdeConstants.FWD_DESESP_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (desespVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + desespAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DesEspAdapter.NAME, desespAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DesEspAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DesEspAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DESESP, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DesEspAdapter desespAdapterVO = (DesEspAdapter) userSession.get(DesEspAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (desespAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DesEspAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DesEspAdapter.NAME); 
			}

			// llamada al servicio
			DesEspVO desespVO = GdeServiceLocator.getDefinicionService().activarDesEsp
				(userSession, desespAdapterVO.getDesEsp());
			
            // Tiene errores recuperables
			if (desespVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desespAdapterVO.infoString());
				saveDemodaErrors(request, desespVO);				
				request.setAttribute(DesEspAdapter.NAME, desespAdapterVO);
				return mapping.findForward(GdeConstants.FWD_DESESP_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (desespVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + desespAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DesEspAdapter.NAME, desespAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DesEspAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DesEspAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DESESP, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DesEspAdapter desespAdapterVO = (DesEspAdapter) userSession.get(DesEspAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (desespAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DesEspAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DesEspAdapter.NAME); 
			}

			// llamada al servicio
			DesEspVO desespVO = GdeServiceLocator.getDefinicionService().desactivarDesEsp
				(userSession, desespAdapterVO.getDesEsp());
			
            // Tiene errores recuperables
			if (desespVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desespAdapterVO.infoString());
				saveDemodaErrors(request, desespVO);				
				request.setAttribute(DesEspAdapter.NAME, desespAdapterVO);
				return mapping.findForward(GdeConstants.FWD_DESESP_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (desespVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + desespAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DesEspAdapter.NAME, desespAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DesEspAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DesEspAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, DesEspAdapter.NAME);
		
	}
	
/*	public ActionForward param (ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DesEspAdapter desespAdapterVO = (DesEspAdapter) userSession.get(DesEspAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (desespAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DesEspAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DesEspAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(desespAdapterVO, request);
			
            // Tiene errores recuperables
			if (desespAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desespAdapterVO.infoString()); 
				saveDemodaErrors(request, desespAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, DesEspAdapter.NAME, desespAdapterVO);
			}
			
			// llamada al servicio
			desespAdapterVO = GdeServiceLocator.getDefinicionService().getDesEspAdapterParam(userSession, desespAdapterVO);
			
            // Tiene errores recuperables
			if (desespAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desespAdapterVO.infoString()); 
				saveDemodaErrors(request, desespAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, DesEspAdapter.NAME, desespAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (desespAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + desespAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DesEspAdapter.NAME, desespAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(DesEspAdapter.NAME, desespAdapterVO);
			// Subo el apdater al userSession
			userSession.put(DesEspAdapter.NAME, desespAdapterVO);
			
			return mapping.findForward(GdeConstants.FWD_DESESP_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DesEspAdapter.NAME);
		}
	}*/
		
}
