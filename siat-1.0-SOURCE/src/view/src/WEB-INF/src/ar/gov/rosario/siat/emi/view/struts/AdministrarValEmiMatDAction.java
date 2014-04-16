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
import ar.gov.rosario.siat.emi.iface.model.ValEmiMatAdapter;
import ar.gov.rosario.siat.emi.iface.model.ValEmiMatVO;
import ar.gov.rosario.siat.emi.iface.service.EmiServiceLocator;
import ar.gov.rosario.siat.emi.iface.util.EmiError;
import ar.gov.rosario.siat.emi.iface.util.EmiSecurityConstants;
import ar.gov.rosario.siat.emi.view.util.EmiConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarValEmiMatDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarValEmiMatDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EmiSecurityConstants.ABM_VALEMIMAT, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ValEmiMatAdapter valEmiMatAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getValEmiMatAdapterForView(userSession, commonKey)";
				valEmiMatAdapterVO = EmiServiceLocator.getDefinicionService().getValEmiMatAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(EmiConstants.FWD_VALEMIMAT_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getValEmiMatAdapterForUpdate(userSession, commonKey)";
				valEmiMatAdapterVO = EmiServiceLocator.getDefinicionService().getValEmiMatAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(EmiConstants.FWD_VALEMIMAT_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getValEmiMatAdapterForView(userSession, commonKey)";
				valEmiMatAdapterVO = EmiServiceLocator.getDefinicionService().getValEmiMatAdapterForView(userSession, commonKey);				
				valEmiMatAdapterVO.addMessage(BaseError.MSG_ELIMINAR, EmiError.VALEMIMAT_LABEL);
				actionForward = mapping.findForward(EmiConstants.FWD_VALEMIMAT_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getValEmiMatAdapterForCreate(userSession)";
				valEmiMatAdapterVO = EmiServiceLocator.getDefinicionService().getValEmiMatAdapterForCreate(userSession);
				actionForward = mapping.findForward(EmiConstants.FWD_VALEMIMAT_EDIT_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (valEmiMatAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + valEmiMatAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ValEmiMatAdapter.NAME, valEmiMatAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			valEmiMatAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ValEmiMatAdapter.NAME + ": "+ valEmiMatAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ValEmiMatAdapter.NAME, valEmiMatAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ValEmiMatAdapter.NAME, valEmiMatAdapterVO);
			 
			saveDemodaMessages(request, valEmiMatAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ValEmiMatAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EmiSecurityConstants.ABM_VALEMIMAT, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ValEmiMatAdapter valEmiMatAdapterVO = (ValEmiMatAdapter) userSession.get(ValEmiMatAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (valEmiMatAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ValEmiMatAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ValEmiMatAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(valEmiMatAdapterVO, request);
			
            // Tiene errores recuperables
			if (valEmiMatAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + valEmiMatAdapterVO.infoString()); 
				saveDemodaErrors(request, valEmiMatAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ValEmiMatAdapter.NAME, valEmiMatAdapterVO);
			}
			
			// llamada al servicio
			ValEmiMatVO valEmiMatVO = EmiServiceLocator.getDefinicionService().createValEmiMat(userSession, valEmiMatAdapterVO.getValEmiMat());
			
            // Tiene errores recuperables
			if (valEmiMatVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + valEmiMatVO.infoString()); 
				saveDemodaErrors(request, valEmiMatVO);
				return forwardErrorRecoverable(mapping, request, userSession, ValEmiMatAdapter.NAME, valEmiMatAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (valEmiMatVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + valEmiMatVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ValEmiMatAdapter.NAME, valEmiMatAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ValEmiMatAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ValEmiMatAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EmiSecurityConstants.ABM_VALEMIMAT, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ValEmiMatAdapter valEmiMatAdapterVO = (ValEmiMatAdapter) userSession.get(ValEmiMatAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (valEmiMatAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ValEmiMatAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ValEmiMatAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(valEmiMatAdapterVO, request);
			
            // Tiene errores recuperables
			if (valEmiMatAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + valEmiMatAdapterVO.infoString()); 
				saveDemodaErrors(request, valEmiMatAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ValEmiMatAdapter.NAME, valEmiMatAdapterVO);
			}
			
			// llamada al servicio
			ValEmiMatVO valEmiMatVO = EmiServiceLocator.getDefinicionService().updateValEmiMat(userSession, valEmiMatAdapterVO.getValEmiMat());
			
            // Tiene errores recuperables
			if (valEmiMatVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + valEmiMatAdapterVO.infoString()); 
				saveDemodaErrors(request, valEmiMatVO);
				return forwardErrorRecoverable(mapping, request, userSession, ValEmiMatAdapter.NAME, valEmiMatAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (valEmiMatVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + valEmiMatAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ValEmiMatAdapter.NAME, valEmiMatAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ValEmiMatAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ValEmiMatAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, EmiSecurityConstants.ABM_VALEMIMAT, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ValEmiMatAdapter valEmiMatAdapterVO = (ValEmiMatAdapter) userSession.get(ValEmiMatAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (valEmiMatAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ValEmiMatAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ValEmiMatAdapter.NAME); 
			}

			// llamada al servicio
			ValEmiMatVO valEmiMatVO = EmiServiceLocator.getDefinicionService().deleteValEmiMat
				(userSession, valEmiMatAdapterVO.getValEmiMat());
			
            // Tiene errores recuperables
			if (valEmiMatVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + valEmiMatAdapterVO.infoString());
				saveDemodaErrors(request, valEmiMatVO);				
				request.setAttribute(ValEmiMatAdapter.NAME, valEmiMatAdapterVO);
				return mapping.findForward(EmiConstants.FWD_VALEMIMAT_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (valEmiMatVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + valEmiMatAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ValEmiMatAdapter.NAME, valEmiMatAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ValEmiMatAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ValEmiMatAdapter.NAME);
		}
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
			ValEmiMatAdapter valEmiMatAdapterVO = (ValEmiMatAdapter) userSession.get(ValEmiMatAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (valEmiMatAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ValEmiMatAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ValEmiMatAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(valEmiMatAdapterVO, request);
			
            // Tiene errores recuperables
			if (valEmiMatAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + valEmiMatAdapterVO.infoString()); 
				saveDemodaErrors(request, valEmiMatAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ValEmiMatAdapter.NAME, valEmiMatAdapterVO);
			}
			
			// llamada al servicio
			valEmiMatAdapterVO = EmiServiceLocator.getDefinicionService()
						.getValEmiMatAdapterParamRecurso(userSession, valEmiMatAdapterVO);
			
            // Tiene errores recuperables
			if (valEmiMatAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + valEmiMatAdapterVO.infoString()); 
				saveDemodaErrors(request, valEmiMatAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ValEmiMatAdapter.NAME, valEmiMatAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (valEmiMatAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + valEmiMatAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ValEmiMatAdapter.NAME, valEmiMatAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(ValEmiMatAdapter.NAME, valEmiMatAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ValEmiMatAdapter.NAME, valEmiMatAdapterVO);
			
			return mapping.findForward(EmiConstants.FWD_VALEMIMAT_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ValEmiMatAdapter.NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ValEmiMatAdapter.NAME);
		
	}

}
