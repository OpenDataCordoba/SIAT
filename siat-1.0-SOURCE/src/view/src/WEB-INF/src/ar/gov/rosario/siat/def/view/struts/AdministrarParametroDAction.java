//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.view.struts;

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
import ar.gov.rosario.siat.def.iface.model.ParametroAdapter;
import ar.gov.rosario.siat.def.iface.model.ParametroVO;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarParametroDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarParametroDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_PARAMETRO, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ParametroAdapter parametroAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			log.debug(" ########  commonKey: " + commonKey.getId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getParametroAdapterForView(userSession, commonKey)";
				parametroAdapterVO = DefServiceLocator.getConfiguracionService().getParametroAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_PARAMETRO_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getParametroAdapterForUpdate(userSession, commonKey)";
				parametroAdapterVO = DefServiceLocator.getConfiguracionService().getParametroAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_PARAMETRO_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getParametroAdapterForView(userSession, commonKey)";
				parametroAdapterVO = DefServiceLocator.getConfiguracionService().getParametroAdapterForView(userSession, commonKey);				
				parametroAdapterVO.addMessage(BaseError.MSG_ELIMINAR, DefError.PARAMETRO_LABEL);
				actionForward = mapping.findForward(DefConstants.FWD_PARAMETRO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getParametroAdapterForCreate(userSession)";
				parametroAdapterVO = DefServiceLocator.getConfiguracionService().getParametroAdapterForCreate(userSession);
				actionForward = mapping.findForward(DefConstants.FWD_PARAMETRO_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getParametroAdapterForView(userSession)";
				parametroAdapterVO = DefServiceLocator.getConfiguracionService().getParametroAdapterForView(userSession, commonKey);
				parametroAdapterVO.addMessage(BaseError.MSG_ACTIVAR, DefError.PARAMETRO_LABEL);
				actionForward = mapping.findForward(DefConstants.FWD_PARAMETRO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getParametroAdapterForView(userSession)";
				parametroAdapterVO = DefServiceLocator.getConfiguracionService().getParametroAdapterForView(userSession, commonKey);
				parametroAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, DefError.PARAMETRO_LABEL);
				actionForward = mapping.findForward(DefConstants.FWD_PARAMETRO_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (parametroAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + parametroAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ParametroAdapter.NAME, parametroAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			parametroAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ParametroAdapter.NAME + ": "+ parametroAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ParametroAdapter.NAME, parametroAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ParametroAdapter.NAME, parametroAdapterVO);
			 
			saveDemodaMessages(request, parametroAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ParametroAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_PARAMETRO, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ParametroAdapter parametroAdapterVO = (ParametroAdapter) userSession.get(ParametroAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (parametroAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ParametroAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ParametroAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(parametroAdapterVO, request);
			
            // Tiene errores recuperables
			if (parametroAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + parametroAdapterVO.infoString()); 
				saveDemodaErrors(request, parametroAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ParametroAdapter.NAME, parametroAdapterVO);
			}
			
			// llamada al servicio
			ParametroVO parametroVO = DefServiceLocator.getConfiguracionService().createParametro(userSession, parametroAdapterVO.getParametro());
			
            // Tiene errores recuperables
			if (parametroVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + parametroVO.infoString()); 
				saveDemodaErrors(request, parametroVO);
				return forwardErrorRecoverable(mapping, request, userSession, ParametroAdapter.NAME, parametroAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (parametroVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + parametroVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ParametroAdapter.NAME, parametroAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ParametroAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ParametroAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_PARAMETRO, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ParametroAdapter parametroAdapterVO = (ParametroAdapter) userSession.get(ParametroAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (parametroAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ParametroAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ParametroAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(parametroAdapterVO, request);
			
            // Tiene errores recuperables
			if (parametroAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + parametroAdapterVO.infoString()); 
				saveDemodaErrors(request, parametroAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ParametroAdapter.NAME, parametroAdapterVO);
			}
			
			// llamada al servicio
			ParametroVO parametroVO = DefServiceLocator.getConfiguracionService().updateParametro(userSession, parametroAdapterVO.getParametro());
			
            // Tiene errores recuperables
			if (parametroVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + parametroAdapterVO.infoString()); 
				saveDemodaErrors(request, parametroVO);
				return forwardErrorRecoverable(mapping, request, userSession, ParametroAdapter.NAME, parametroAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (parametroVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + parametroAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ParametroAdapter.NAME, parametroAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ParametroAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ParametroAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_PARAMETRO, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ParametroAdapter parametroAdapterVO = (ParametroAdapter) userSession.get(ParametroAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (parametroAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ParametroAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ParametroAdapter.NAME); 
			}

			// llamada al servicio
			ParametroVO parametroVO = DefServiceLocator.getConfiguracionService().deleteParametro
				(userSession, parametroAdapterVO.getParametro());
			
            // Tiene errores recuperables
			if (parametroVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + parametroAdapterVO.infoString());
				saveDemodaErrors(request, parametroVO);				
				request.setAttribute(ParametroAdapter.NAME, parametroAdapterVO);
				return mapping.findForward(DefConstants.FWD_PARAMETRO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (parametroVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + parametroAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ParametroAdapter.NAME, parametroAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ParametroAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ParametroAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_PARAMETRO, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ParametroAdapter parametroAdapterVO = (ParametroAdapter) userSession.get(ParametroAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (parametroAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ParametroAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ParametroAdapter.NAME); 
			}

			// llamada al servicio
			ParametroVO parametroVO = DefServiceLocator.getConfiguracionService().activarParametro
				(userSession, parametroAdapterVO.getParametro());
			
            // Tiene errores recuperables
			if (parametroVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + parametroAdapterVO.infoString());
				saveDemodaErrors(request, parametroVO);				
				request.setAttribute(ParametroAdapter.NAME, parametroAdapterVO);
				return mapping.findForward(DefConstants.FWD_PARAMETRO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (parametroVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + parametroAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ParametroAdapter.NAME, parametroAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ParametroAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ParametroAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_PARAMETRO, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ParametroAdapter parametroAdapterVO = (ParametroAdapter) userSession.get(ParametroAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (parametroAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ParametroAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ParametroAdapter.NAME); 
			}

			// llamada al servicio
			ParametroVO parametroVO = DefServiceLocator.getConfiguracionService().desactivarParametro
				(userSession, parametroAdapterVO.getParametro());
			
            // Tiene errores recuperables
			if (parametroVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + parametroAdapterVO.infoString());
				saveDemodaErrors(request, parametroVO);				
				request.setAttribute(ParametroAdapter.NAME, parametroAdapterVO);
				return mapping.findForward(DefConstants.FWD_PARAMETRO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (parametroVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + parametroAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ParametroAdapter.NAME, parametroAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ParametroAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ParametroAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ParametroAdapter.NAME);
		
	}
	
}
