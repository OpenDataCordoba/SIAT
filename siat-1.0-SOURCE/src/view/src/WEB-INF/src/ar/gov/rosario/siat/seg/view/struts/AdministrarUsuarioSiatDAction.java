//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.seg.view.struts;

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
import ar.gov.rosario.siat.seg.iface.model.UsuarioSiatAdapter;
import ar.gov.rosario.siat.seg.iface.model.UsuarioSiatVO;
import ar.gov.rosario.siat.seg.iface.service.SegServiceLocator;
import ar.gov.rosario.siat.seg.iface.util.SegError;
import ar.gov.rosario.siat.seg.iface.util.SegSecurityConstants;
import ar.gov.rosario.siat.seg.view.util.SegConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarUsuarioSiatDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarUsuarioSiatDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, SegSecurityConstants.ABM_USUARIOSIAT, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		UsuarioSiatAdapter usuarioSiatAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getUsuarioSiatAdapterForView(userSession, commonKey)";
				usuarioSiatAdapterVO = SegServiceLocator.getSeguridadService().getUsuarioSiatAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(SegConstants.FWD_USUARIOSIAT_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getUsuarioSiatAdapterForUpdate(userSession, commonKey)";
				usuarioSiatAdapterVO = SegServiceLocator.getSeguridadService().getUsuarioSiatAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(SegConstants.FWD_USUARIOSIAT_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getUsuarioSiatAdapterForView(userSession, commonKey)";
				usuarioSiatAdapterVO = SegServiceLocator.getSeguridadService().getUsuarioSiatAdapterForView(userSession, commonKey);				
				usuarioSiatAdapterVO.addMessage(BaseError.MSG_ELIMINAR, SegError.USUARIOSIAT_LABEL);
				actionForward = mapping.findForward(SegConstants.FWD_USUARIOSIAT_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getUsuarioSiatAdapterForCreate(userSession)";
				usuarioSiatAdapterVO = SegServiceLocator.getSeguridadService().getUsuarioSiatAdapterForCreate(userSession);
				actionForward = mapping.findForward(SegConstants.FWD_USUARIOSIAT_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getUsuarioSiatAdapterForView(userSession)";
				usuarioSiatAdapterVO = SegServiceLocator.getSeguridadService().getUsuarioSiatAdapterForView(userSession, commonKey);
				usuarioSiatAdapterVO.addMessage(BaseError.MSG_ACTIVAR, SegError.USUARIOSIAT_LABEL);
				actionForward = mapping.findForward(SegConstants.FWD_USUARIOSIAT_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getUsuarioSiatAdapterForView(userSession)";
				usuarioSiatAdapterVO = SegServiceLocator.getSeguridadService().getUsuarioSiatAdapterForView(userSession, commonKey);
				usuarioSiatAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, SegError.USUARIOSIAT_LABEL);
				actionForward = mapping.findForward(SegConstants.FWD_USUARIOSIAT_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (usuarioSiatAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + usuarioSiatAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, UsuarioSiatAdapter.NAME, usuarioSiatAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			usuarioSiatAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + UsuarioSiatAdapter.NAME + ": "+ usuarioSiatAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(UsuarioSiatAdapter.NAME, usuarioSiatAdapterVO);
			// Subo el apdater al userSession
			userSession.put(UsuarioSiatAdapter.NAME, usuarioSiatAdapterVO);
			 
			saveDemodaMessages(request, usuarioSiatAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, UsuarioSiatAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, SegSecurityConstants.ABM_USUARIOSIAT, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			UsuarioSiatAdapter usuarioSiatAdapterVO = (UsuarioSiatAdapter) userSession.get(UsuarioSiatAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (usuarioSiatAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + UsuarioSiatAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, UsuarioSiatAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(usuarioSiatAdapterVO, request);
			
            // Tiene errores recuperables
			if (usuarioSiatAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + usuarioSiatAdapterVO.infoString()); 
				saveDemodaErrors(request, usuarioSiatAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, UsuarioSiatAdapter.NAME, usuarioSiatAdapterVO);
			}
			
			// llamada al servicio
			UsuarioSiatVO usuarioSiatVO = SegServiceLocator.getSeguridadService().createUsuarioSiat(userSession, usuarioSiatAdapterVO.getUsuarioSiat());
			
            // Tiene errores recuperables
			if (usuarioSiatVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + usuarioSiatVO.infoString()); 
				saveDemodaErrors(request, usuarioSiatVO);
				return forwardErrorRecoverable(mapping, request, userSession, UsuarioSiatAdapter.NAME, usuarioSiatAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (usuarioSiatVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + usuarioSiatVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, UsuarioSiatAdapter.NAME, usuarioSiatAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, UsuarioSiatAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, UsuarioSiatAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, SegSecurityConstants.ABM_USUARIOSIAT, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			UsuarioSiatAdapter usuarioSiatAdapterVO = (UsuarioSiatAdapter) userSession.get(UsuarioSiatAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (usuarioSiatAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + UsuarioSiatAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, UsuarioSiatAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(usuarioSiatAdapterVO, request);
			
            // Tiene errores recuperables
			if (usuarioSiatAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + usuarioSiatAdapterVO.infoString()); 
				saveDemodaErrors(request, usuarioSiatAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, UsuarioSiatAdapter.NAME, usuarioSiatAdapterVO);
			}
			
			// llamada al servicio
			UsuarioSiatVO usuarioSiatVO = SegServiceLocator.getSeguridadService().updateUsuarioSiat(userSession, usuarioSiatAdapterVO.getUsuarioSiat());
			
            // Tiene errores recuperables
			if (usuarioSiatVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + usuarioSiatAdapterVO.infoString()); 
				saveDemodaErrors(request, usuarioSiatVO);
				return forwardErrorRecoverable(mapping, request, userSession, UsuarioSiatAdapter.NAME, usuarioSiatAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (usuarioSiatVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + usuarioSiatAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, UsuarioSiatAdapter.NAME, usuarioSiatAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, UsuarioSiatAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, UsuarioSiatAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, SegSecurityConstants.ABM_USUARIOSIAT, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			UsuarioSiatAdapter usuarioSiatAdapterVO = (UsuarioSiatAdapter) userSession.get(UsuarioSiatAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (usuarioSiatAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + UsuarioSiatAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, UsuarioSiatAdapter.NAME); 
			}

			// llamada al servicio
			UsuarioSiatVO usuarioSiatVO = SegServiceLocator.getSeguridadService().deleteUsuarioSiat
				(userSession, usuarioSiatAdapterVO.getUsuarioSiat());
			
            // Tiene errores recuperables
			if (usuarioSiatVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + usuarioSiatAdapterVO.infoString());
				saveDemodaErrors(request, usuarioSiatVO);				
				request.setAttribute(UsuarioSiatAdapter.NAME, usuarioSiatAdapterVO);
				return mapping.findForward(SegConstants.FWD_USUARIOSIAT_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (usuarioSiatVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + usuarioSiatAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, UsuarioSiatAdapter.NAME, usuarioSiatAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, UsuarioSiatAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, UsuarioSiatAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, SegSecurityConstants.ABM_USUARIOSIAT, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			UsuarioSiatAdapter usuarioSiatAdapterVO = (UsuarioSiatAdapter) userSession.get(UsuarioSiatAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (usuarioSiatAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + UsuarioSiatAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, UsuarioSiatAdapter.NAME); 
			}

			// llamada al servicio
			UsuarioSiatVO usuarioSiatVO = SegServiceLocator.getSeguridadService().activarUsuarioSiat
				(userSession, usuarioSiatAdapterVO.getUsuarioSiat());
			
            // Tiene errores recuperables
			if (usuarioSiatVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + usuarioSiatAdapterVO.infoString());
				saveDemodaErrors(request, usuarioSiatVO);				
				request.setAttribute(UsuarioSiatAdapter.NAME, usuarioSiatAdapterVO);
				return mapping.findForward(SegConstants.FWD_USUARIOSIAT_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (usuarioSiatVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + usuarioSiatAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, UsuarioSiatAdapter.NAME, usuarioSiatAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, UsuarioSiatAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, UsuarioSiatAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, SegSecurityConstants.ABM_USUARIOSIAT, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			UsuarioSiatAdapter usuarioSiatAdapterVO = (UsuarioSiatAdapter) userSession.get(UsuarioSiatAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (usuarioSiatAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + UsuarioSiatAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, UsuarioSiatAdapter.NAME); 
			}

			// llamada al servicio
			UsuarioSiatVO usuarioSiatVO = SegServiceLocator.getSeguridadService().desactivarUsuarioSiat
				(userSession, usuarioSiatAdapterVO.getUsuarioSiat());
			
            // Tiene errores recuperables
			if (usuarioSiatVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + usuarioSiatAdapterVO.infoString());
				saveDemodaErrors(request, usuarioSiatVO);				
				request.setAttribute(UsuarioSiatAdapter.NAME, usuarioSiatAdapterVO);
				return mapping.findForward(SegConstants.FWD_USUARIOSIAT_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (usuarioSiatVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + usuarioSiatAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, UsuarioSiatAdapter.NAME, usuarioSiatAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, UsuarioSiatAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, UsuarioSiatAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, UsuarioSiatAdapter.NAME);
		
	}
		
}
