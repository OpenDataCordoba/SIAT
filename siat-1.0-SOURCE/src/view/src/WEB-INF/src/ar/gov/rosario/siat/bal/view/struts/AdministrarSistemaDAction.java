//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.bal.iface.model.SistemaAdapter;
import ar.gov.rosario.siat.bal.iface.model.SistemaVO;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarSistemaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarSistemaDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_SISTEMA, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		SistemaAdapter sistemaAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getSistemaAdapterForView(userSession, commonKey)";
				sistemaAdapterVO = BalServiceLocator.getDefinicionService().getSistemaAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_SISTEMA_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getSistemaAdapterForUpdate(userSession, commonKey)";
				sistemaAdapterVO = BalServiceLocator.getDefinicionService().getSistemaAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_SISTEMA_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getSistemaAdapterForView(userSession, commonKey)";
				sistemaAdapterVO = BalServiceLocator.getDefinicionService().getSistemaAdapterForView(userSession, commonKey);				
				sistemaAdapterVO.addMessage(BaseError.MSG_ELIMINAR, BalError.SISTEMA_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_SISTEMA_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getSistemaAdapterForCreate(userSession)";
				sistemaAdapterVO = BalServiceLocator.getDefinicionService().getSistemaAdapterForCreate(userSession);
				actionForward = mapping.findForward(BalConstants.FWD_SISTEMA_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getSistemaAdapterForView(userSession)";
				sistemaAdapterVO = BalServiceLocator.getDefinicionService().getSistemaAdapterForView(userSession, commonKey);
				sistemaAdapterVO.addMessage(BaseError.MSG_ACTIVAR, BalError.SISTEMA_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_SISTEMA_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getSistemaAdapterForView(userSession)";
				sistemaAdapterVO = BalServiceLocator.getDefinicionService().getSistemaAdapterForView(userSession, commonKey);
				sistemaAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, BalError.SISTEMA_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_SISTEMA_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (sistemaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + sistemaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SistemaAdapter.NAME, sistemaAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			sistemaAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + SistemaAdapter.NAME + ": "+ sistemaAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(SistemaAdapter.NAME, sistemaAdapterVO);
			// Subo el apdater al userSession
			userSession.put(SistemaAdapter.NAME, sistemaAdapterVO);
			 
			saveDemodaMessages(request, sistemaAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SistemaAdapter.NAME);
		}
	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_SISTEMA, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			SistemaAdapter sistemaAdapterVO = (SistemaAdapter) userSession.get(SistemaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (sistemaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + SistemaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SistemaAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(sistemaAdapterVO, request);
			
            // Tiene errores recuperables
			if (sistemaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + sistemaAdapterVO.infoString()); 
				saveDemodaErrors(request, sistemaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, SistemaAdapter.NAME, sistemaAdapterVO);
			}
			
			// llamada al servicio
			SistemaVO sistemaVO = BalServiceLocator.getDefinicionService().createSistema(userSession, sistemaAdapterVO.getSistema());
			
            // Tiene errores recuperables
			if (sistemaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + sistemaVO.infoString()); 
				saveDemodaErrors(request, sistemaVO);
				return forwardErrorRecoverable(mapping, request, userSession, SistemaAdapter.NAME, sistemaAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (sistemaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + sistemaVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SistemaAdapter.NAME, sistemaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, SistemaAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SistemaAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_SISTEMA, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			SistemaAdapter sistemaAdapterVO = (SistemaAdapter) userSession.get(SistemaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (sistemaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + SistemaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SistemaAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(sistemaAdapterVO, request);
			
            // Tiene errores recuperables
			if (sistemaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + sistemaAdapterVO.infoString()); 
				saveDemodaErrors(request, sistemaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, SistemaAdapter.NAME, sistemaAdapterVO);
			}
			
			// llamada al servicio
			SistemaVO sistemaVO = BalServiceLocator.getDefinicionService().updateSistema(userSession, sistemaAdapterVO.getSistema());
			
            // Tiene errores recuperables
			if (sistemaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + sistemaAdapterVO.infoString()); 
				saveDemodaErrors(request, sistemaVO);
				return forwardErrorRecoverable(mapping, request, userSession, SistemaAdapter.NAME, sistemaAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (sistemaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + sistemaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SistemaAdapter.NAME, sistemaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, SistemaAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SistemaAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_SISTEMA, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			SistemaAdapter sistemaAdapterVO = (SistemaAdapter) userSession.get(SistemaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (sistemaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + SistemaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SistemaAdapter.NAME); 
			}

			// llamada al servicio
			SistemaVO sistemaVO = BalServiceLocator.getDefinicionService().deleteSistema
				(userSession, sistemaAdapterVO.getSistema());
			
            // Tiene errores recuperables
			if (sistemaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + sistemaAdapterVO.infoString());
				saveDemodaErrors(request, sistemaVO);				
				request.setAttribute(SistemaAdapter.NAME, sistemaAdapterVO);
				return mapping.findForward(BalConstants.FWD_SISTEMA_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (sistemaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + sistemaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SistemaAdapter.NAME, sistemaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, SistemaAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SistemaAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_SISTEMA, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			SistemaAdapter sistemaAdapterVO = (SistemaAdapter) userSession.get(SistemaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (sistemaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + SistemaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SistemaAdapter.NAME); 
			}

			// llamada al servicio
			SistemaVO sistemaVO = BalServiceLocator.getDefinicionService().activarSistema
				(userSession, sistemaAdapterVO.getSistema());
			
            // Tiene errores recuperables
			if (sistemaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + sistemaAdapterVO.infoString());
				saveDemodaErrors(request, sistemaVO);				
				request.setAttribute(SistemaAdapter.NAME, sistemaAdapterVO);
				return mapping.findForward(BalConstants.FWD_SISTEMA_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (sistemaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + sistemaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SistemaAdapter.NAME, sistemaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, SistemaAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SistemaAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_SISTEMA, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			SistemaAdapter sistemaAdapterVO = (SistemaAdapter) userSession.get(SistemaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (sistemaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + SistemaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SistemaAdapter.NAME); 
			}

			// llamada al servicio
			SistemaVO sistemaVO = BalServiceLocator.getDefinicionService().desactivarSistema
				(userSession, sistemaAdapterVO.getSistema());
			
            // Tiene errores recuperables
			if (sistemaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + sistemaAdapterVO.infoString());
				saveDemodaErrors(request, sistemaVO);				
				request.setAttribute(SistemaAdapter.NAME, sistemaAdapterVO);
				return mapping.findForward(BalConstants.FWD_SISTEMA_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (sistemaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + sistemaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SistemaAdapter.NAME, sistemaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, SistemaAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SistemaAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, SistemaAdapter.NAME);
			
	}

}
