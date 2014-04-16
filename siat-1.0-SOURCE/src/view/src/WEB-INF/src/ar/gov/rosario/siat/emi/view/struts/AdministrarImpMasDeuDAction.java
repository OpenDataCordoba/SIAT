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
import ar.gov.rosario.siat.emi.iface.model.ImpMasDeuAdapter;
import ar.gov.rosario.siat.emi.iface.model.ImpMasDeuVO;
import ar.gov.rosario.siat.emi.iface.service.EmiServiceLocator;
import ar.gov.rosario.siat.emi.iface.util.EmiError;
import ar.gov.rosario.siat.emi.iface.util.EmiSecurityConstants;
import ar.gov.rosario.siat.emi.view.util.EmiConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarImpMasDeuDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarImpMasDeuDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EmiSecurityConstants.ABM_IMPMASDEU, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ImpMasDeuAdapter impMasDeuAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getImpMasDeuAdapterForView(userSession, commonKey)";
				impMasDeuAdapterVO = EmiServiceLocator.getImpresionService().getImpMasDeuAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(EmiConstants.FWD_IMPMASDEU_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getImpMasDeuAdapterForUpdate(userSession, commonKey)";
				impMasDeuAdapterVO = EmiServiceLocator.getImpresionService().getImpMasDeuAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(EmiConstants.FWD_IMPMASDEU_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getImpMasDeuAdapterForView(userSession, commonKey)";
				impMasDeuAdapterVO = EmiServiceLocator.getImpresionService().getImpMasDeuAdapterForView(userSession, commonKey);				
				impMasDeuAdapterVO.addMessage(BaseError.MSG_ELIMINAR, EmiError.IMPMASDEU_LABEL);
				actionForward = mapping.findForward(EmiConstants.FWD_IMPMASDEU_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getImpMasDeuAdapterForCreate(userSession)";
				impMasDeuAdapterVO = EmiServiceLocator.getImpresionService().getImpMasDeuAdapterForCreate(userSession);
				actionForward = mapping.findForward(EmiConstants.FWD_IMPMASDEU_EDIT_ADAPTER);				
			}


			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (impMasDeuAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + impMasDeuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ImpMasDeuAdapter.NAME, impMasDeuAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			impMasDeuAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ImpMasDeuAdapter.NAME + ": "+ impMasDeuAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ImpMasDeuAdapter.NAME, impMasDeuAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ImpMasDeuAdapter.NAME, impMasDeuAdapterVO);
			 
			saveDemodaMessages(request, impMasDeuAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ImpMasDeuAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EmiSecurityConstants.ABM_IMPMASDEU, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ImpMasDeuAdapter impMasDeuAdapterVO = (ImpMasDeuAdapter) userSession.get(ImpMasDeuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (impMasDeuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ImpMasDeuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ImpMasDeuAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(impMasDeuAdapterVO, request);
			
            // Tiene errores recuperables
			if (impMasDeuAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + impMasDeuAdapterVO.infoString()); 
				saveDemodaErrors(request, impMasDeuAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ImpMasDeuAdapter.NAME, impMasDeuAdapterVO);
			}
			
			// llamada al servicio
			ImpMasDeuVO impMasDeuVO = EmiServiceLocator.getImpresionService().createImpMasDeu(userSession, impMasDeuAdapterVO.getImpMasDeu());
			
            // Tiene errores recuperables
			if (impMasDeuVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + impMasDeuVO.infoString()); 
				saveDemodaErrors(request, impMasDeuVO);
				return forwardErrorRecoverable(mapping, request, userSession, ImpMasDeuAdapter.NAME, impMasDeuAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (impMasDeuVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + impMasDeuVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ImpMasDeuAdapter.NAME, impMasDeuAdapterVO);
			}
			
			// Si tiene permiso lo dirijo al adapter de modificacion, 
			// sino vuelve al searchPage
			if (hasAccess(userSession, EmiSecurityConstants.ABM_IMPMASDEU, BaseSecurityConstants.MODIFICAR)) {
				// seteo el id para que lo use el siguiente action 
				userSession.getNavModel().setSelectedId(impMasDeuVO.getId().toString());

				// lo dirijo al adapter de administrar proceso
				return forwardConfirmarOk(mapping, request, funcName, ImpMasDeuAdapter.NAME, 
					EmiConstants.PATH_ADMINISTRAR_PROCESO_IMPMASDEU, 
					BaseConstants.METHOD_INICIALIZAR, EmiConstants.ACT_ADM_PROCESO_IMPMASDEU);
			} else {
				// lo dirijo al searchPage				
				return forwardConfirmarOk(mapping, request, funcName, ImpMasDeuAdapter.NAME);
			}
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ImpMasDeuAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EmiSecurityConstants.ABM_IMPMASDEU, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ImpMasDeuAdapter impMasDeuAdapterVO = (ImpMasDeuAdapter) userSession.get(ImpMasDeuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (impMasDeuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ImpMasDeuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ImpMasDeuAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(impMasDeuAdapterVO, request);
			
            // Tiene errores recuperables
			if (impMasDeuAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + impMasDeuAdapterVO.infoString()); 
				saveDemodaErrors(request, impMasDeuAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ImpMasDeuAdapter.NAME, impMasDeuAdapterVO);
			}
			
			// llamada al servicio
			ImpMasDeuVO impMasDeuVO = EmiServiceLocator.getImpresionService().updateImpMasDeu(userSession, impMasDeuAdapterVO.getImpMasDeu());
			
            // Tiene errores recuperables
			if (impMasDeuVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + impMasDeuAdapterVO.infoString()); 
				saveDemodaErrors(request, impMasDeuVO);
				return forwardErrorRecoverable(mapping, request, userSession, ImpMasDeuAdapter.NAME, impMasDeuAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (impMasDeuVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + impMasDeuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ImpMasDeuAdapter.NAME, impMasDeuAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ImpMasDeuAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ImpMasDeuAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, EmiSecurityConstants.ABM_IMPMASDEU, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ImpMasDeuAdapter impMasDeuAdapterVO = (ImpMasDeuAdapter) userSession.get(ImpMasDeuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (impMasDeuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ImpMasDeuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ImpMasDeuAdapter.NAME); 
			}

			// llamada al servicio
			ImpMasDeuVO impMasDeuVO = EmiServiceLocator.getImpresionService().deleteImpMasDeu
				(userSession, impMasDeuAdapterVO.getImpMasDeu());
			
            // Tiene errores recuperables
			if (impMasDeuVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + impMasDeuAdapterVO.infoString());
				saveDemodaErrors(request, impMasDeuVO);				
				request.setAttribute(ImpMasDeuAdapter.NAME, impMasDeuAdapterVO);
				return mapping.findForward(EmiConstants.FWD_IMPMASDEU_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (impMasDeuVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + impMasDeuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ImpMasDeuAdapter.NAME, impMasDeuAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ImpMasDeuAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ImpMasDeuAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ImpMasDeuAdapter.NAME);
		
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
			ImpMasDeuAdapter impMasDeuAdapterVO = (ImpMasDeuAdapter) userSession.get(ImpMasDeuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (impMasDeuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ImpMasDeuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ImpMasDeuAdapter.NAME); 
			}
	
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(impMasDeuAdapterVO, request);
			
	        // Tiene errores recuperables
			if (impMasDeuAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + impMasDeuAdapterVO.infoString()); 
				saveDemodaErrors(request, impMasDeuAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ImpMasDeuAdapter.NAME, impMasDeuAdapterVO);
			}
			
			// llamada al servicio
			impMasDeuAdapterVO = EmiServiceLocator.getImpresionService()
				.getImpMasDeuAdapterParamRecurso(userSession, impMasDeuAdapterVO);
			
	        // Tiene errores recuperables
			if (impMasDeuAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + impMasDeuAdapterVO.infoString()); 
				saveDemodaErrors(request, impMasDeuAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ImpMasDeuAdapter.NAME, impMasDeuAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (impMasDeuAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + impMasDeuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ImpMasDeuAdapter.NAME, impMasDeuAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(ImpMasDeuAdapter.NAME, impMasDeuAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ImpMasDeuAdapter.NAME, impMasDeuAdapterVO);
			
			return mapping.findForward(EmiConstants.FWD_IMPMASDEU_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ImpMasDeuAdapter.NAME);
		}
	}
	
}
