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
import ar.gov.rosario.siat.gde.iface.model.AgeRetAdapter;
import ar.gov.rosario.siat.gde.iface.model.AgeRetVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarAgeRetDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarAgeRetDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_AGERET, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		AgeRetAdapter ageRetAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getAgeRetAdapterForView(userSession, commonKey)";
				ageRetAdapterVO = GdeServiceLocator.getDefinicionService().getAgeRetAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_AGERET_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getAgeRetAdapterForUpdate(userSession, commonKey)";
				ageRetAdapterVO = GdeServiceLocator.getDefinicionService().getAgeRetAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_AGERET_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getAgeRetAdapterForView(userSession, commonKey)";
				ageRetAdapterVO = GdeServiceLocator.getDefinicionService().getAgeRetAdapterForView(userSession, commonKey);				
				ageRetAdapterVO.addMessage(BaseError.MSG_ELIMINAR, GdeError.AGERET_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_AGERET_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getAgeRetAdapterForCreate(userSession)";
				ageRetAdapterVO = GdeServiceLocator.getDefinicionService().getAgeRetAdapterForCreate(userSession);
				actionForward = mapping.findForward(GdeConstants.FWD_AGERET_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getAgeRetAdapterForView(userSession,commonKey)";
				ageRetAdapterVO = GdeServiceLocator.getDefinicionService().getAgeRetAdapterForView(userSession, commonKey);
				ageRetAdapterVO.addMessage(BaseError.MSG_ACTIVAR, GdeError.AGERET_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_AGERET_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getAgeRetAdapterForView(userSession)";
				ageRetAdapterVO = GdeServiceLocator.getDefinicionService().getAgeRetAdapterForView(userSession, commonKey);
				ageRetAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, GdeError.AGERET_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_AGERET_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (ageRetAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + ageRetAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AgeRetAdapter.NAME, ageRetAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			ageRetAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + AgeRetAdapter.NAME + ": "+ ageRetAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(AgeRetAdapter.NAME, ageRetAdapterVO);
			// Subo el apdater al userSession
			userSession.put(AgeRetAdapter.NAME, ageRetAdapterVO);
			 
			saveDemodaMessages(request, ageRetAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AgeRetAdapter.NAME);
		}
	}
	
	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_AGERET, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AgeRetAdapter ageRetAdapterVO = (AgeRetAdapter) userSession.get(AgeRetAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (ageRetAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AgeRetAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AgeRetAdapter.NAME); 
			}

			// llamada al servicio
			AgeRetVO ageRetVO = GdeServiceLocator.getDefinicionService().deleteAgeRet
				(userSession, ageRetAdapterVO.getAgeRet());
			
            // Tiene errores recuperables
			if (ageRetVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ageRetAdapterVO.infoString());
				saveDemodaErrors(request, ageRetVO);				
				request.setAttribute(AgeRetAdapter.NAME, ageRetAdapterVO);
				return mapping.findForward(GdeConstants.FWD_AGERET_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (ageRetVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + ageRetAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AgeRetAdapter.NAME, ageRetAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AgeRetAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AgeRetAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_AGERET, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AgeRetAdapter ageRetAdapterVO = (AgeRetAdapter) userSession.get(AgeRetAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (ageRetAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AgeRetAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AgeRetAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(ageRetAdapterVO, request);
			
            // Tiene errores recuperables
			if (ageRetAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ageRetAdapterVO.infoString()); 
				saveDemodaErrors(request, ageRetAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, AgeRetAdapter.NAME, ageRetAdapterVO);
			}
			
			// llamada al servicio
			AgeRetVO ageRetVO = GdeServiceLocator.getDefinicionService().createAgeRet(userSession, ageRetAdapterVO.getAgeRet());
			
            // Tiene errores recuperables
			if (ageRetVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ageRetVO.infoString()); 
				saveDemodaErrors(request, ageRetVO);
				return forwardErrorRecoverable(mapping, request, userSession, AgeRetAdapter.NAME, ageRetAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (ageRetVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + ageRetVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AgeRetAdapter.NAME, ageRetAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AgeRetAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AgeRetAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_AGERET, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AgeRetAdapter ageRetAdapterVO = (AgeRetAdapter) userSession.get(AgeRetAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (ageRetAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AgeRetAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AgeRetAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(ageRetAdapterVO, request);
			
            // Tiene errores recuperables
			if (ageRetAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ageRetAdapterVO.infoString()); 
				saveDemodaErrors(request, ageRetAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, AgeRetAdapter.NAME, ageRetAdapterVO);
			}
			
			// llamada al servicio
			AgeRetVO ageRetVO = GdeServiceLocator.getDefinicionService().updateAgeRet(userSession, ageRetAdapterVO.getAgeRet());
			
            // Tiene errores recuperables
			if (ageRetVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ageRetAdapterVO.infoString()); 
				saveDemodaErrors(request, ageRetVO);
				return forwardErrorRecoverable(mapping, request, userSession, AgeRetAdapter.NAME, ageRetAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (ageRetVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + ageRetAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AgeRetAdapter.NAME, ageRetAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AgeRetAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AgeRetAdapter.NAME);
		}
	}

	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_AGERET, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AgeRetAdapter ageRetAdapterVO = (AgeRetAdapter) userSession.get(AgeRetAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (ageRetAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AgeRetAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AgeRetAdapter.NAME); 
			}

			// llamada al servicio
			AgeRetVO ageRetVO = GdeServiceLocator.getDefinicionService().activarAgeRet
				(userSession, ageRetAdapterVO.getAgeRet());
			
            // Tiene errores recuperables
			if (ageRetVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ageRetAdapterVO.infoString());
				saveDemodaErrors(request, ageRetVO);				
				request.setAttribute(AgeRetAdapter.NAME, ageRetAdapterVO);
				return mapping.findForward(GdeConstants.FWD_AGERET_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (ageRetVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + ageRetAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AgeRetAdapter.NAME, ageRetAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AgeRetAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AgeRetAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_AGERET, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AgeRetAdapter ageRetAdapterVO = (AgeRetAdapter) userSession.get(AgeRetAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (ageRetAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AgeRetAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AgeRetAdapter.NAME); 
			}

			// llamada al servicio
			AgeRetVO ageRetVO = GdeServiceLocator.getDefinicionService().desactivarAgeRet
				(userSession, ageRetAdapterVO.getAgeRet());
			
            // Tiene errores recuperables
			if (ageRetVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ageRetAdapterVO.infoString());
				saveDemodaErrors(request, ageRetVO);				
				request.setAttribute(AgeRetAdapter.NAME, ageRetAdapterVO);
				return mapping.findForward(GdeConstants.FWD_AGERET_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (ageRetVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + ageRetAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AgeRetAdapter.NAME, ageRetAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AgeRetAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AgeRetAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, AgeRetAdapter.NAME);
		
	}
	
}
