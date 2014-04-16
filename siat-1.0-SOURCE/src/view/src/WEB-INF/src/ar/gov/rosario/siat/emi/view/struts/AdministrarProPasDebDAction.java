//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.view.struts;

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
import ar.gov.rosario.siat.emi.iface.model.ProPasDebAdapter;
import ar.gov.rosario.siat.emi.iface.model.ProPasDebVO;
import ar.gov.rosario.siat.emi.iface.service.EmiServiceLocator;
import ar.gov.rosario.siat.emi.iface.util.EmiError;
import ar.gov.rosario.siat.emi.iface.util.EmiSecurityConstants;
import ar.gov.rosario.siat.emi.view.util.EmiConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarProPasDebDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarProPasDebDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EmiSecurityConstants.ABM_PROPASDEB, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ProPasDebAdapter proPasDebAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getProPasDebAdapterForView(userSession, commonKey)";
				proPasDebAdapterVO = EmiServiceLocator.getGeneralService().getProPasDebAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(EmiConstants.FWD_PROPASDEB_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getProPasDebAdapterForUpdate(userSession, commonKey)";
				proPasDebAdapterVO = EmiServiceLocator.getGeneralService().getProPasDebAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(EmiConstants.FWD_PROPASDEB_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getProPasDebAdapterForView(userSession, commonKey)";
				proPasDebAdapterVO = EmiServiceLocator.getGeneralService().getProPasDebAdapterForView(userSession, commonKey);				
				proPasDebAdapterVO.addMessage(BaseError.MSG_ELIMINAR, EmiError.PROPASDEB_LABEL);
				actionForward = mapping.findForward(EmiConstants.FWD_PROPASDEB_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getProPasDebAdapterForCreate(userSession)";
				proPasDebAdapterVO = EmiServiceLocator.getGeneralService().getProPasDebAdapterForCreate(userSession);
				actionForward = mapping.findForward(EmiConstants.FWD_PROPASDEB_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getProPasDebAdapterForView(userSession)";
				proPasDebAdapterVO = EmiServiceLocator.getGeneralService().getProPasDebAdapterForView(userSession, commonKey);
				proPasDebAdapterVO.addMessage(BaseError.MSG_ACTIVAR, EmiError.PROPASDEB_LABEL);
				actionForward = mapping.findForward(EmiConstants.FWD_PROPASDEB_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getProPasDebAdapterForView(userSession)";
				proPasDebAdapterVO = EmiServiceLocator.getGeneralService().getProPasDebAdapterForView(userSession, commonKey);
				proPasDebAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, EmiError.PROPASDEB_LABEL);
				actionForward = mapping.findForward(EmiConstants.FWD_PROPASDEB_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (proPasDebAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + proPasDebAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProPasDebAdapter.NAME, proPasDebAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			proPasDebAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ProPasDebAdapter.NAME + ": "+ proPasDebAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ProPasDebAdapter.NAME, proPasDebAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ProPasDebAdapter.NAME, proPasDebAdapterVO);
			 
			saveDemodaMessages(request, proPasDebAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProPasDebAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EmiSecurityConstants.ABM_PROPASDEB, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ProPasDebAdapter proPasDebAdapterVO = (ProPasDebAdapter) userSession.get(ProPasDebAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (proPasDebAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProPasDebAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProPasDebAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(proPasDebAdapterVO, request);
			
            // Tiene errores recuperables
			if (proPasDebAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + proPasDebAdapterVO.infoString()); 
				saveDemodaErrors(request, proPasDebAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProPasDebAdapter.NAME, proPasDebAdapterVO);
			}
			
			// llamada al servicio
			ProPasDebVO proPasDebVO = EmiServiceLocator.getGeneralService().createProPasDeb(userSession, proPasDebAdapterVO.getProPasDeb());
			
            // Tiene errores recuperables
			if (proPasDebVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + proPasDebVO.infoString()); 
				saveDemodaErrors(request, proPasDebVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProPasDebAdapter.NAME, proPasDebAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (proPasDebVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + proPasDebVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProPasDebAdapter.NAME, proPasDebAdapterVO);
			}
			
			// Si tiene permiso lo dirijo al adapter de modificacion, 
			// sino vuelve al searchPage
			if (hasAccess(userSession, EmiSecurityConstants.ABM_PROPASDEB, BaseSecurityConstants.MODIFICAR)) {

				// seteo el id para que lo use el siguiente action 
				userSession.getNavModel().setSelectedId(proPasDebVO.getId().toString());

				// lo dirijo al adapter de administrar proceso
				return forwardConfirmarOk(mapping, request, funcName, ProPasDebAdapter.NAME, 
					EmiConstants.PATH_ADMINISTRAR_PROCESO_PROPASDEB, 
					BaseConstants.METHOD_INICIALIZAR, EmiSecurityConstants.ACT_ADMINISTRAR_PROCESO);
			} else {
				// lo dirijo al searchPage				
				return forwardConfirmarOk(mapping, request, funcName, ProPasDebAdapter.NAME);
			}			
						
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProPasDebAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EmiSecurityConstants.ABM_PROPASDEB, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ProPasDebAdapter proPasDebAdapterVO = (ProPasDebAdapter) userSession.get(ProPasDebAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (proPasDebAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProPasDebAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProPasDebAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(proPasDebAdapterVO, request);
			
            // Tiene errores recuperables
			if (proPasDebAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + proPasDebAdapterVO.infoString()); 
				saveDemodaErrors(request, proPasDebAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProPasDebAdapter.NAME, proPasDebAdapterVO);
			}
			
			// llamada al servicio
			ProPasDebVO proPasDebVO = EmiServiceLocator.getGeneralService().updateProPasDeb(userSession, proPasDebAdapterVO.getProPasDeb());
			
            // Tiene errores recuperables
			if (proPasDebVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + proPasDebAdapterVO.infoString()); 
				saveDemodaErrors(request, proPasDebVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProPasDebAdapter.NAME, proPasDebAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (proPasDebVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + proPasDebAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProPasDebAdapter.NAME, proPasDebAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ProPasDebAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProPasDebAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, EmiSecurityConstants.ABM_PROPASDEB, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ProPasDebAdapter proPasDebAdapterVO = (ProPasDebAdapter) userSession.get(ProPasDebAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (proPasDebAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProPasDebAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProPasDebAdapter.NAME); 
			}

			// llamada al servicio
			ProPasDebVO proPasDebVO = EmiServiceLocator.getGeneralService().deleteProPasDeb
				(userSession, proPasDebAdapterVO.getProPasDeb());
			
            // Tiene errores recuperables
			if (proPasDebVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + proPasDebAdapterVO.infoString());
				saveDemodaErrors(request, proPasDebVO);				
				request.setAttribute(ProPasDebAdapter.NAME, proPasDebAdapterVO);
				return mapping.findForward(EmiConstants.FWD_PROPASDEB_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (proPasDebVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + proPasDebAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProPasDebAdapter.NAME, proPasDebAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ProPasDebAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProPasDebAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ProPasDebAdapter.NAME);
		
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
			ProPasDebAdapter proPasDebAdapterVO = (ProPasDebAdapter) userSession.get(ProPasDebAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (proPasDebAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProPasDebAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProPasDebAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(proPasDebAdapterVO, request);
			
            // Tiene errores recuperables
			if (proPasDebAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + proPasDebAdapterVO.infoString()); 
				saveDemodaErrors(request, proPasDebAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProPasDebAdapter.NAME, proPasDebAdapterVO);
			}
			
			// llamada al servicio
			proPasDebAdapterVO = EmiServiceLocator.getGeneralService()
				.getProPasDebAdapterParamRecurso(userSession, proPasDebAdapterVO);
			
            // Tiene errores recuperables
			if (proPasDebAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + proPasDebAdapterVO.infoString()); 
				saveDemodaErrors(request, proPasDebAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProPasDebAdapter.NAME, proPasDebAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (proPasDebAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + proPasDebAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProPasDebAdapter.NAME, proPasDebAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(ProPasDebAdapter.NAME, proPasDebAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ProPasDebAdapter.NAME, proPasDebAdapterVO);
			
			return mapping.findForward(EmiConstants.FWD_PROPASDEB_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProPasDebAdapter.NAME);
		}
	}
		
}
