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
import ar.gov.rosario.siat.gde.iface.model.TipoMultaAdapter;
import ar.gov.rosario.siat.gde.iface.model.TipoMultaVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarTipoMultaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarTipoMultaDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_TIPOMULTA, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		TipoMultaAdapter tipoMultaAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getTipoMultaAdapterForView(userSession, commonKey)";
				tipoMultaAdapterVO = GdeServiceLocator.getDefinicionService().getTipoMultaAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_TIPOMULTA_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getTipoMultaAdapterForUpdate(userSession, commonKey)";
				tipoMultaAdapterVO = GdeServiceLocator.getDefinicionService().getTipoMultaAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_TIPOMULTA_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getTipoMultaAdapterForView(userSession, commonKey)";
				tipoMultaAdapterVO = GdeServiceLocator.getDefinicionService().getTipoMultaAdapterForView(userSession, commonKey);				
				tipoMultaAdapterVO.addMessage(BaseError.MSG_ELIMINAR, GdeError.TIPOMULTA_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_TIPOMULTA_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getTipoMultaAdapterForCreate(userSession)";
				tipoMultaAdapterVO = GdeServiceLocator.getDefinicionService().getTipoMultaAdapterForCreate(userSession);
				actionForward = mapping.findForward(GdeConstants.FWD_TIPOMULTA_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getTipoMultaAdapterForView(userSession,commonKey)";
				tipoMultaAdapterVO = GdeServiceLocator.getDefinicionService().getTipoMultaAdapterForView(userSession, commonKey);
				tipoMultaAdapterVO.addMessage(BaseError.MSG_ACTIVAR, GdeError.TIPOMULTA_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_TIPOMULTA_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getTipoMultaAdapterForView(userSession)";
				tipoMultaAdapterVO = GdeServiceLocator.getDefinicionService().getTipoMultaAdapterForView(userSession, commonKey);
				tipoMultaAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, GdeError.TIPOMULTA_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_TIPOMULTA_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (tipoMultaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + tipoMultaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoMultaAdapter.NAME, tipoMultaAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			tipoMultaAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + TipoMultaAdapter.NAME + ": "+ tipoMultaAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(TipoMultaAdapter.NAME, tipoMultaAdapterVO);
			// Subo el apdater al userSession
			userSession.put(TipoMultaAdapter.NAME, tipoMultaAdapterVO);
			 
			saveDemodaMessages(request, tipoMultaAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoMultaAdapter.NAME);
		}
	}
	
	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_TIPOMULTA, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipoMultaAdapter tipoMultaAdapterVO = (TipoMultaAdapter) userSession.get(TipoMultaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipoMultaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipoMultaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoMultaAdapter.NAME); 
			}

			// llamada al servicio
			TipoMultaVO tipoMultaVO = GdeServiceLocator.getDefinicionService().deleteTipoMulta
				(userSession, tipoMultaAdapterVO.getTipoMulta());
			
            // Tiene errores recuperables
			if (tipoMultaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoMultaAdapterVO.infoString());
				saveDemodaErrors(request, tipoMultaVO);				
				request.setAttribute(TipoMultaAdapter.NAME, tipoMultaAdapterVO);
				return mapping.findForward(GdeConstants.FWD_TIPOMULTA_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (tipoMultaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoMultaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoMultaAdapter.NAME, tipoMultaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipoMultaAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoMultaAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_TIPOMULTA, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipoMultaAdapter tipoMultaAdapterVO = (TipoMultaAdapter) userSession.get(TipoMultaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipoMultaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipoMultaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoMultaAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(tipoMultaAdapterVO, request);
			
            // Tiene errores recuperables
			if (tipoMultaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoMultaAdapterVO.infoString()); 
				saveDemodaErrors(request, tipoMultaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoMultaAdapter.NAME, tipoMultaAdapterVO);
			}
			
			// llamada al servicio
			TipoMultaVO tipoMultaVO = GdeServiceLocator.getDefinicionService().createTipoMulta(userSession, tipoMultaAdapterVO.getTipoMulta());
			
            // Tiene errores recuperables
			if (tipoMultaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoMultaVO.infoString()); 
				saveDemodaErrors(request, tipoMultaVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoMultaAdapter.NAME, tipoMultaAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (tipoMultaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoMultaVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoMultaAdapter.NAME, tipoMultaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipoMultaAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoMultaAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_TIPOMULTA, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipoMultaAdapter tipoMultaAdapterVO = (TipoMultaAdapter) userSession.get(TipoMultaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipoMultaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipoMultaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoMultaAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(tipoMultaAdapterVO, request);
			
            // Tiene errores recuperables
			if (tipoMultaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoMultaAdapterVO.infoString()); 
				saveDemodaErrors(request, tipoMultaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoMultaAdapter.NAME, tipoMultaAdapterVO);
			}
			
			// llamada al servicio
			TipoMultaVO tipoMultaVO = GdeServiceLocator.getDefinicionService().updateTipoMulta(userSession, tipoMultaAdapterVO.getTipoMulta());
			
            // Tiene errores recuperables
			if (tipoMultaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoMultaAdapterVO.infoString()); 
				saveDemodaErrors(request, tipoMultaVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoMultaAdapter.NAME, tipoMultaAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (tipoMultaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoMultaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoMultaAdapter.NAME, tipoMultaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipoMultaAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoMultaAdapter.NAME);
		}
	}

	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_TIPOMULTA, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipoMultaAdapter tipoMultaAdapterVO = (TipoMultaAdapter) userSession.get(TipoMultaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipoMultaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipoMultaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoMultaAdapter.NAME); 
			}

			// llamada al servicio
			TipoMultaVO tipoMultaVO = GdeServiceLocator.getDefinicionService().activarTipoMulta
				(userSession, tipoMultaAdapterVO.getTipoMulta());
			
            // Tiene errores recuperables
			if (tipoMultaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoMultaAdapterVO.infoString());
				saveDemodaErrors(request, tipoMultaVO);				
				request.setAttribute(TipoMultaAdapter.NAME, tipoMultaAdapterVO);
				return mapping.findForward(GdeConstants.FWD_TIPOMULTA_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (tipoMultaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoMultaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoMultaAdapter.NAME, tipoMultaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipoMultaAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoMultaAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_TIPOMULTA, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipoMultaAdapter tipoMultaAdapterVO = (TipoMultaAdapter) userSession.get(TipoMultaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipoMultaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipoMultaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoMultaAdapter.NAME); 
			}

			// llamada al servicio
			TipoMultaVO tipoMultaVO = GdeServiceLocator.getDefinicionService().desactivarTipoMulta
				(userSession, tipoMultaAdapterVO.getTipoMulta());
			
            // Tiene errores recuperables
			if (tipoMultaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoMultaAdapterVO.infoString());
				saveDemodaErrors(request, tipoMultaVO);				
				request.setAttribute(TipoMultaAdapter.NAME, tipoMultaAdapterVO);
				return mapping.findForward(GdeConstants.FWD_TIPOMULTA_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (tipoMultaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoMultaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoMultaAdapter.NAME, tipoMultaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipoMultaAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoMultaAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, TipoMultaAdapter.NAME);
		
	}

	public ActionForward paramRecurso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				TipoMultaAdapter tipoMultaAdapterVO = (TipoMultaAdapter) userSession.get(TipoMultaAdapter.NAME);
		
				// Si es nulo no se puede continuar
				if (tipoMultaAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + TipoMultaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, TipoMultaAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(tipoMultaAdapterVO, request);
				
	            // Tiene errores recuperables
				if (tipoMultaAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + tipoMultaAdapterVO.infoString()); 
					saveDemodaErrors(request, tipoMultaAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, TipoMultaAdapter.NAME, tipoMultaAdapterVO);
				}
				
				// Llamada al servicio
				tipoMultaAdapterVO = GdeServiceLocator.getDefinicionService().getTipoMultaAdapterParamRecurso(userSession, tipoMultaAdapterVO);
				
	            // Tiene errores recuperables
				if (tipoMultaAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + tipoMultaAdapterVO.infoString()); 
					saveDemodaErrors(request, tipoMultaAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, TipoMultaAdapter.NAME, tipoMultaAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (tipoMultaAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + tipoMultaAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, TipoMultaAdapter.NAME, tipoMultaAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(TipoMultaAdapter.NAME, tipoMultaAdapterVO);
				// Subo el adapter al userSession
				userSession.put(TipoMultaAdapter.NAME, tipoMultaAdapterVO);
				
				return mapping.findForward(GdeConstants.FWD_TIPOMULTA_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, TipoMultaAdapter.NAME);
			}
	}

	
	
	
}
