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

import ar.gov.rosario.siat.bal.iface.model.AccionSelladoAdapter;
import ar.gov.rosario.siat.bal.iface.model.AccionSelladoVO;
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

public final class AdministrarAccionSelladoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarAccionSelladoDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_ACCIONSELLADO, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		AccionSelladoAdapter accionselladoAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getAccionSelladoAdapterForView(userSession, commonKey)";
				accionselladoAdapterVO = BalServiceLocator.getDefinicionService().getAccionSelladoAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_ACCIONSELLADO_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getAccionSelladoAdapterForUpdate(userSession, commonKey)";
				accionselladoAdapterVO = BalServiceLocator.getDefinicionService().getAccionSelladoAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_ACCIONSELLADO_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getAccionSelladoAdapterForView(userSession, commonKey)";
				accionselladoAdapterVO = BalServiceLocator.getDefinicionService().getAccionSelladoAdapterForView(userSession, commonKey);				
				accionselladoAdapterVO.addMessage(BaseError.MSG_ELIMINAR, BalError.ACCIONSELLADO_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_ACCIONSELLADO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getAccionSelladoAdapterForCreate(userSession)";
				accionselladoAdapterVO = BalServiceLocator.getDefinicionService().getAccionSelladoAdapterForCreate(userSession,commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_ACCIONSELLADO_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getAccionSelladoAdapterForView(userSession)";
				accionselladoAdapterVO = BalServiceLocator.getDefinicionService().getAccionSelladoAdapterForView(userSession, commonKey);
				accionselladoAdapterVO.addMessage(BaseError.MSG_ACTIVAR, BalError.ACCIONSELLADO_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_ACCIONSELLADO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getAccionSelladoAdapterForView(userSession)";
				accionselladoAdapterVO = BalServiceLocator.getDefinicionService().getAccionSelladoAdapterForView(userSession, commonKey);
				accionselladoAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, BalError.ACCIONSELLADO_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_ACCIONSELLADO_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (accionselladoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + accionselladoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AccionSelladoAdapter.NAME, accionselladoAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			accionselladoAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + AccionSelladoAdapter.NAME + ": "+ accionselladoAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(AccionSelladoAdapter.NAME, accionselladoAdapterVO);
			// Subo el apdater al userSession
			userSession.put(AccionSelladoAdapter.NAME, accionselladoAdapterVO);
			 
			saveDemodaMessages(request, accionselladoAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AccionSelladoAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_ACCIONSELLADO, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AccionSelladoAdapter accionselladoAdapterVO = (AccionSelladoAdapter) userSession.get(AccionSelladoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (accionselladoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AccionSelladoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AccionSelladoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(accionselladoAdapterVO, request);
			
            // Tiene errores recuperables
			if (accionselladoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + accionselladoAdapterVO.infoString()); 
				saveDemodaErrors(request, accionselladoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, AccionSelladoAdapter.NAME, accionselladoAdapterVO);
			}
			
			// llamada al servicio
			AccionSelladoVO accionselladoVO = BalServiceLocator.getDefinicionService().createAccionSellado(userSession, accionselladoAdapterVO.getAccionSellado());
			
            // Tiene errores recuperables
			if (accionselladoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + accionselladoVO.infoString()); 
				saveDemodaErrors(request, accionselladoVO);
				return forwardErrorRecoverable(mapping, request, userSession, AccionSelladoAdapter.NAME, accionselladoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (accionselladoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + accionselladoVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AccionSelladoAdapter.NAME, accionselladoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AccionSelladoAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AccionSelladoAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_ACCIONSELLADO, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AccionSelladoAdapter accionselladoAdapterVO = (AccionSelladoAdapter) userSession.get(AccionSelladoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (accionselladoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AccionSelladoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AccionSelladoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(accionselladoAdapterVO, request);
			
            // Tiene errores recuperables
			if (accionselladoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + accionselladoAdapterVO.infoString()); 
				saveDemodaErrors(request, accionselladoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, AccionSelladoAdapter.NAME, accionselladoAdapterVO);
			}
			
			// llamada al servicio
			AccionSelladoVO accionselladoVO = BalServiceLocator.getDefinicionService().updateAccionSellado(userSession, accionselladoAdapterVO.getAccionSellado());
			
            // Tiene errores recuperables
			if (accionselladoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + accionselladoAdapterVO.infoString()); 
				saveDemodaErrors(request, accionselladoVO);
				return forwardErrorRecoverable(mapping, request, userSession, AccionSelladoAdapter.NAME, accionselladoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (accionselladoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + accionselladoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AccionSelladoAdapter.NAME, accionselladoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AccionSelladoAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AccionSelladoAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_ACCIONSELLADO, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AccionSelladoAdapter accionselladoAdapterVO = (AccionSelladoAdapter) userSession.get(AccionSelladoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (accionselladoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AccionSelladoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AccionSelladoAdapter.NAME); 
			}

			// llamada al servicio
			AccionSelladoVO accionselladoVO = BalServiceLocator.getDefinicionService().deleteAccionSellado
				(userSession, accionselladoAdapterVO.getAccionSellado());
			
            // Tiene errores recuperables
			if (accionselladoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + accionselladoAdapterVO.infoString());
				saveDemodaErrors(request, accionselladoVO);				
				request.setAttribute(AccionSelladoAdapter.NAME, accionselladoAdapterVO);
				return mapping.findForward(BalConstants.FWD_ACCIONSELLADO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (accionselladoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + accionselladoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AccionSelladoAdapter.NAME, accionselladoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AccionSelladoAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AccionSelladoAdapter.NAME);
		}
	}
	
	public ActionForward paramEsEspecial(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				AccionSelladoAdapter accionSelladoAdapterVO = (AccionSelladoAdapter) userSession.get(AccionSelladoAdapter.NAME);
		
				// Si es nulo no se puede continuar
				if (accionSelladoAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + AccionSelladoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, AccionSelladoAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(accionSelladoAdapterVO, request);
				
	            // Tiene errores recuperables
				if (accionSelladoAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " +accionSelladoAdapterVO.infoString()); 
					saveDemodaErrors(request, accionSelladoAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, AccionSelladoAdapter.NAME, accionSelladoAdapterVO);
				}
				
				// Llamada al servicio
				accionSelladoAdapterVO = BalServiceLocator.getDefinicionService().getAccionSelladoAdapterParamEsEspecial(userSession, accionSelladoAdapterVO);
				
	            // Tiene errores recuperables
				if (accionSelladoAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + accionSelladoAdapterVO.infoString()); 
					saveDemodaErrors(request, accionSelladoAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, AccionSelladoAdapter.NAME, accionSelladoAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (accionSelladoAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + accionSelladoAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, AccionSelladoAdapter.NAME, accionSelladoAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(AccionSelladoAdapter.NAME, accionSelladoAdapterVO);
				// Subo el adapter al userSession
				userSession.put(AccionSelladoAdapter.NAME, accionSelladoAdapterVO);
				
				return mapping.findForward(BalConstants.FWD_ACCIONSELLADO_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, AccionSelladoAdapter.NAME);
			}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, AccionSelladoAdapter.NAME);
		
	}
	
}
