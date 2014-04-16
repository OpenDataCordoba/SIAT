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
import ar.gov.rosario.siat.emi.iface.model.EmisionMasAdapter;
import ar.gov.rosario.siat.emi.iface.model.EmisionVO;
import ar.gov.rosario.siat.emi.iface.service.EmiServiceLocator;
import ar.gov.rosario.siat.emi.iface.util.EmiError;
import ar.gov.rosario.siat.emi.iface.util.EmiSecurityConstants;
import ar.gov.rosario.siat.emi.view.util.EmiConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarEmisionMasDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEmisionMasDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EmiSecurityConstants.ABM_EMISIONMAS, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		EmisionMasAdapter emisionMasAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getEmisionMasAdapterForView(userSession, commonKey)";
				emisionMasAdapterVO = EmiServiceLocator.getEmisionService().getEmisionMasAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(EmiConstants.FWD_EMISIONMAS_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getEmisionMasAdapterForUpdate(userSession, commonKey)";
				emisionMasAdapterVO = EmiServiceLocator.getEmisionService().getEmisionMasAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(EmiConstants.FWD_EMISIONMAS_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getEmisionMasAdapterForView(userSession, commonKey)";
				emisionMasAdapterVO = EmiServiceLocator.getEmisionService().getEmisionMasAdapterForView(userSession, commonKey);				
				emisionMasAdapterVO.addMessage(BaseError.MSG_ELIMINAR, EmiError.EMISION_LABEL);
				actionForward = mapping.findForward(EmiConstants.FWD_EMISIONMAS_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getEmisionMasAdapterForCreate(userSession)";
				emisionMasAdapterVO = EmiServiceLocator.getEmisionService().getEmisionMasAdapterForCreate(userSession);
				actionForward = mapping.findForward(EmiConstants.FWD_EMISIONMAS_EDIT_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (emisionMasAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + emisionMasAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EmisionMasAdapter.NAME, emisionMasAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			emisionMasAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + EmisionMasAdapter.NAME + ": "+ emisionMasAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(EmisionMasAdapter.NAME, emisionMasAdapterVO);
			// Subo el apdater al userSession
			userSession.put(EmisionMasAdapter.NAME, emisionMasAdapterVO);
			 
			saveDemodaMessages(request, emisionMasAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionMasAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EmiSecurityConstants.ABM_EMISIONMAS, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			EmisionMasAdapter emisionMasAdapterVO = (EmisionMasAdapter) userSession.get(EmisionMasAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (emisionMasAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EmisionMasAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EmisionMasAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(emisionMasAdapterVO, request);
			
            // Tiene errores recuperables
			if (emisionMasAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionMasAdapterVO.infoString()); 
				saveDemodaErrors(request, emisionMasAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, EmisionMasAdapter.NAME, emisionMasAdapterVO);
			}
			
			// llamada al servicio
			EmisionVO emisionVO = EmiServiceLocator.getEmisionService()
				.createEmisionMas(userSession, emisionMasAdapterVO);
			
            // Tiene errores recuperables
			if (emisionVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionVO.infoString()); 
				saveDemodaErrors(request, emisionVO);
				return forwardErrorRecoverable(mapping, request, userSession, EmisionMasAdapter.NAME, emisionMasAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (emisionVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + emisionVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EmisionMasAdapter.NAME, emisionMasAdapterVO);
			}
			
			// Si tiene permiso lo dirijo al adapter de modificacion, 
			// sino vuelve al searchPage
			if (hasAccess(userSession, EmiSecurityConstants.ABM_EMISIONMAS, BaseSecurityConstants.MODIFICAR)) {

				// seteo el id para que lo use el siguiente action 
				userSession.getNavModel().setSelectedId(emisionVO.getId().toString());

				// lo dirijo al adapter de administrar proceso
				return forwardConfirmarOk(mapping, request, funcName, EmisionMasAdapter.NAME, 
					EmiConstants.PATH_ADMINISTRAR_PROCESO_EMISIONMAS, 
					BaseConstants.METHOD_INICIALIZAR, EmiSecurityConstants.ACT_ADMINISTRAR_PROCESO);
			} else {
				// lo dirijo al searchPage				
				return forwardConfirmarOk(mapping, request, funcName, EmisionMasAdapter.NAME);
			}			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionMasAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EmiSecurityConstants.ABM_EMISIONMAS, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			EmisionMasAdapter emisionMasAdapterVO = (EmisionMasAdapter) userSession.get(EmisionMasAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (emisionMasAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EmisionMasAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EmisionMasAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(emisionMasAdapterVO, request);
			
            // Tiene errores recuperables
			if (emisionMasAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionMasAdapterVO.infoString()); 
				saveDemodaErrors(request, emisionMasAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, EmisionMasAdapter.NAME, emisionMasAdapterVO);
			}
			
			// llamada al servicio
			EmisionVO emisionVO = EmiServiceLocator.getEmisionService()
				.updateEmisionMas(userSession, emisionMasAdapterVO);
			
            // Tiene errores recuperables
			if (emisionVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionMasAdapterVO.infoString()); 
				saveDemodaErrors(request, emisionVO);
				return forwardErrorRecoverable(mapping, request, userSession, EmisionMasAdapter.NAME, emisionMasAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (emisionVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + emisionMasAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EmisionMasAdapter.NAME, emisionMasAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, EmisionMasAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionMasAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, EmiSecurityConstants.ABM_EMISIONMAS, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			EmisionMasAdapter emisionMasAdapterVO = (EmisionMasAdapter) userSession.get(EmisionMasAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (emisionMasAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EmisionMasAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EmisionMasAdapter.NAME); 
			}

			// llamada al servicio
			EmisionVO emisionVO = EmiServiceLocator.getEmisionService()
				.deleteEmisionMas(userSession, emisionMasAdapterVO);
			
            // Tiene errores recuperables
			if (emisionVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionMasAdapterVO.infoString());
				saveDemodaErrors(request, emisionVO);				
				request.setAttribute(EmisionMasAdapter.NAME, emisionMasAdapterVO);
				return mapping.findForward(EmiConstants.FWD_EMISIONMAS_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (emisionVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + emisionMasAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EmisionMasAdapter.NAME, emisionMasAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, EmisionMasAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionMasAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, EmisionMasAdapter.NAME);
		
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
			EmisionMasAdapter emisionMasAdapterVO = (EmisionMasAdapter) userSession.get(EmisionMasAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (emisionMasAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EmisionMasAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EmisionMasAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(emisionMasAdapterVO, request);
			
            // Tiene errores recuperables
			if (emisionMasAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionMasAdapterVO.infoString()); 
				saveDemodaErrors(request, emisionMasAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, EmisionMasAdapter.NAME, emisionMasAdapterVO);
			}
			
			// llamada al servicio
			emisionMasAdapterVO = EmiServiceLocator.getEmisionService().getEmisionMasAdapterParamRecurso(userSession, emisionMasAdapterVO);
			
            // Tiene errores recuperables
			if (emisionMasAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionMasAdapterVO.infoString()); 
				saveDemodaErrors(request, emisionMasAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, EmisionMasAdapter.NAME, emisionMasAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (emisionMasAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + emisionMasAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EmisionMasAdapter.NAME, emisionMasAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(EmisionMasAdapter.NAME, emisionMasAdapterVO);
			// Subo el apdater al userSession
			userSession.put(EmisionMasAdapter.NAME, emisionMasAdapterVO);
			
			return mapping.findForward(EmiConstants.FWD_EMISIONMAS_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionMasAdapter.NAME);
		}
	}
		
}
