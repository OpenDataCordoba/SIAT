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
import ar.gov.rosario.siat.emi.iface.model.ResLiqDeuAdapter;
import ar.gov.rosario.siat.emi.iface.model.ResLiqDeuVO;
import ar.gov.rosario.siat.emi.iface.service.EmiServiceLocator;
import ar.gov.rosario.siat.emi.iface.util.EmiError;
import ar.gov.rosario.siat.emi.iface.util.EmiSecurityConstants;
import ar.gov.rosario.siat.emi.view.util.EmiConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarResLiqDeuDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarResLiqDeuDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EmiSecurityConstants.ABM_RESLIQDEU, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ResLiqDeuAdapter resLiqDeuAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getResLiqDeuAdapterForView(userSession, commonKey)";
				resLiqDeuAdapterVO = EmiServiceLocator.getGeneralService().getResLiqDeuAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(EmiConstants.FWD_RESLIQDEU_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getResLiqDeuAdapterForUpdate(userSession, commonKey)";
				resLiqDeuAdapterVO = EmiServiceLocator.getGeneralService().getResLiqDeuAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(EmiConstants.FWD_RESLIQDEU_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getResLiqDeuAdapterForView(userSession, commonKey)";
				resLiqDeuAdapterVO = EmiServiceLocator.getGeneralService().getResLiqDeuAdapterForView(userSession, commonKey);				
				resLiqDeuAdapterVO.addMessage(BaseError.MSG_ELIMINAR, EmiError.RESLIQDEU_LABEL);
				actionForward = mapping.findForward(EmiConstants.FWD_RESLIQDEU_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getResLiqDeuAdapterForCreate(userSession)";
				resLiqDeuAdapterVO = EmiServiceLocator.getGeneralService().getResLiqDeuAdapterForCreate(userSession);
				actionForward = mapping.findForward(EmiConstants.FWD_RESLIQDEU_EDIT_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (resLiqDeuAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + resLiqDeuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ResLiqDeuAdapter.NAME, resLiqDeuAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			resLiqDeuAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ResLiqDeuAdapter.NAME + ": "+ resLiqDeuAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ResLiqDeuAdapter.NAME, resLiqDeuAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ResLiqDeuAdapter.NAME, resLiqDeuAdapterVO);
			 
			saveDemodaMessages(request, resLiqDeuAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ResLiqDeuAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EmiSecurityConstants.ABM_RESLIQDEU, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ResLiqDeuAdapter resLiqDeuAdapterVO = (ResLiqDeuAdapter) userSession.get(ResLiqDeuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (resLiqDeuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ResLiqDeuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ResLiqDeuAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(resLiqDeuAdapterVO, request);
			
            // Tiene errores recuperables
			if (resLiqDeuAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + resLiqDeuAdapterVO.infoString()); 
				saveDemodaErrors(request, resLiqDeuAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ResLiqDeuAdapter.NAME, resLiqDeuAdapterVO);
			}
			
			// llamada al servicio
			ResLiqDeuVO resLiqDeuVO = EmiServiceLocator.getGeneralService().createResLiqDeu(userSession, resLiqDeuAdapterVO.getResLiqDeu());
			
            // Tiene errores recuperables
			if (resLiqDeuVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + resLiqDeuVO.infoString()); 
				saveDemodaErrors(request, resLiqDeuVO);
				return forwardErrorRecoverable(mapping, request, userSession, ResLiqDeuAdapter.NAME, resLiqDeuAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (resLiqDeuVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + resLiqDeuVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ResLiqDeuAdapter.NAME, resLiqDeuAdapterVO);
			}
			
			// Si tiene permiso lo dirijo al adapter de modificacion, 
			// sino vuelve al searchPage
			if (hasAccess(userSession, EmiSecurityConstants.ABM_RESLIQDEU, BaseSecurityConstants.MODIFICAR)) {
				// seteo el id para que lo use el siguiente action 
				userSession.getNavModel().setSelectedId(resLiqDeuVO.getId().toString());

				// lo dirijo al adapter de administrar proceso
				return forwardConfirmarOk(mapping, request, funcName, ResLiqDeuAdapter.NAME, 
					EmiConstants.PATH_ADMINISTRAR_PROCESO_RESLIQDEU, 
					BaseConstants.METHOD_INICIALIZAR, EmiSecurityConstants.ACT_ADMINISTRAR_PROCESO);
			} 

			// Fue Exitoso, forwardeo al Search Page
			return forwardConfirmarOk(mapping, request, funcName, ResLiqDeuAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ResLiqDeuAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EmiSecurityConstants.ABM_RESLIQDEU, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ResLiqDeuAdapter resLiqDeuAdapterVO = (ResLiqDeuAdapter) userSession.get(ResLiqDeuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (resLiqDeuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ResLiqDeuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ResLiqDeuAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(resLiqDeuAdapterVO, request);
			
            // Tiene errores recuperables
			if (resLiqDeuAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + resLiqDeuAdapterVO.infoString()); 
				saveDemodaErrors(request, resLiqDeuAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ResLiqDeuAdapter.NAME, resLiqDeuAdapterVO);
			}
			
			// llamada al servicio
			ResLiqDeuVO resLiqDeuVO = EmiServiceLocator.getGeneralService().updateResLiqDeu(userSession, resLiqDeuAdapterVO.getResLiqDeu());
			
            // Tiene errores recuperables
			if (resLiqDeuVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + resLiqDeuAdapterVO.infoString()); 
				saveDemodaErrors(request, resLiqDeuVO);
				return forwardErrorRecoverable(mapping, request, userSession, ResLiqDeuAdapter.NAME, resLiqDeuAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (resLiqDeuVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + resLiqDeuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ResLiqDeuAdapter.NAME, resLiqDeuAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ResLiqDeuAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ResLiqDeuAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, EmiSecurityConstants.ABM_RESLIQDEU, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ResLiqDeuAdapter resLiqDeuAdapterVO = (ResLiqDeuAdapter) userSession.get(ResLiqDeuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (resLiqDeuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ResLiqDeuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ResLiqDeuAdapter.NAME); 
			}

			// llamada al servicio
			ResLiqDeuVO resLiqDeuVO = EmiServiceLocator.getGeneralService().deleteResLiqDeu
				(userSession, resLiqDeuAdapterVO.getResLiqDeu());
			
            // Tiene errores recuperables
			if (resLiqDeuVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + resLiqDeuAdapterVO.infoString());
				saveDemodaErrors(request, resLiqDeuVO);				
				request.setAttribute(ResLiqDeuAdapter.NAME, resLiqDeuAdapterVO);
				return mapping.findForward(EmiConstants.FWD_RESLIQDEU_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (resLiqDeuVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + resLiqDeuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ResLiqDeuAdapter.NAME, resLiqDeuAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ResLiqDeuAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ResLiqDeuAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ResLiqDeuAdapter.NAME);
		
	}
	
	public ActionForward paramRecurso (ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ResLiqDeuAdapter resLiqDeuAdapterVO = (ResLiqDeuAdapter) userSession.get(ResLiqDeuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (resLiqDeuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ResLiqDeuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ResLiqDeuAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(resLiqDeuAdapterVO, request);
			
            // Tiene errores recuperables
			if (resLiqDeuAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + resLiqDeuAdapterVO.infoString()); 
				saveDemodaErrors(request, resLiqDeuAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ResLiqDeuAdapter.NAME, resLiqDeuAdapterVO);
			}
			
			// llamada al servicio
			resLiqDeuAdapterVO = EmiServiceLocator.getGeneralService()
				.getResLiqDeuAdapterParamRecurso(userSession, resLiqDeuAdapterVO);
			
            // Tiene errores recuperables
			if (resLiqDeuAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + resLiqDeuAdapterVO.infoString()); 
				saveDemodaErrors(request, resLiqDeuAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ResLiqDeuAdapter.NAME, resLiqDeuAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (resLiqDeuAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + resLiqDeuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ResLiqDeuAdapter.NAME, resLiqDeuAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(ResLiqDeuAdapter.NAME, resLiqDeuAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ResLiqDeuAdapter.NAME, resLiqDeuAdapterVO);
			
			return mapping.findForward(EmiConstants.FWD_RESLIQDEU_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ResLiqDeuAdapter.NAME);
		}
	}
}
