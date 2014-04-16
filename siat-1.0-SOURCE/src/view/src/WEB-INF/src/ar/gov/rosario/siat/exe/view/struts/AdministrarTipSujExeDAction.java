//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.view.struts;

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
import ar.gov.rosario.siat.exe.iface.model.TipSujExeAdapter;
import ar.gov.rosario.siat.exe.iface.model.TipSujExeVO;
import ar.gov.rosario.siat.exe.iface.service.ExeServiceLocator;
import ar.gov.rosario.siat.exe.iface.util.ExeError;
import ar.gov.rosario.siat.exe.iface.util.ExeSecurityConstants;
import ar.gov.rosario.siat.exe.view.util.ExeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarTipSujExeDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarTipSujExeDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		String act = getCurrentAct(request);
		
		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_TIPSUJEXE, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		TipSujExeAdapter tipSujExeAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
		    if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getTipSujExeAdapterForView(userSession, commonKey)";
				tipSujExeAdapterVO = ExeServiceLocator.getDefinicionService().getTipSujExeAdapterForView(userSession, commonKey);				
				tipSujExeAdapterVO.addMessage(BaseError.MSG_ELIMINAR, ExeError.EXENCION_LABEL);
				actionForward = mapping.findForward(ExeConstants.FWD_TIPSUJEXE_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getTipSujExeAdapterForCreate(userSession)";
				tipSujExeAdapterVO = ExeServiceLocator.getDefinicionService().getTipSujExeAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(ExeConstants.FWD_TIPSUJEXE_EDIT_ADAPTER);				
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (tipSujExeAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + tipSujExeAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipSujExeAdapter.NAME, tipSujExeAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			tipSujExeAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + TipSujExeAdapter.NAME + ": "+ tipSujExeAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(TipSujExeAdapter.NAME, tipSujExeAdapterVO);
			// Subo el apdater al userSession
			userSession.put(TipSujExeAdapter.NAME, tipSujExeAdapterVO);
			 
			saveDemodaMessages(request, tipSujExeAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipSujExeAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_TIPSUJEXE, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipSujExeAdapter tipSujExeAdapterVO = (TipSujExeAdapter) userSession.get(TipSujExeAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipSujExeAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipSujExeAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipSujExeAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(tipSujExeAdapterVO, request);
			
            // Tiene errores recuperables
			if (tipSujExeAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipSujExeAdapterVO.infoString()); 
				saveDemodaErrors(request, tipSujExeAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipSujExeAdapter.NAME, tipSujExeAdapterVO);
			}
			
			// llamada al servicio
			TipSujExeVO tipSujExeVO = ExeServiceLocator.getDefinicionService().createTipSujExe(userSession, tipSujExeAdapterVO.getTipSujExe());
			
            // Tiene errores recuperables
			if (tipSujExeVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipSujExeVO.infoString()); 
				saveDemodaErrors(request, tipSujExeVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipSujExeAdapter.NAME, tipSujExeAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (tipSujExeVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipSujExeVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipSujExeAdapter.NAME, tipSujExeAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipSujExeAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipSujExeAdapter.NAME);
		}
	}

	
	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_TIPSUJEXE, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipSujExeAdapter tipSujExeAdapterVO = (TipSujExeAdapter) userSession.get(TipSujExeAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipSujExeAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipSujExeAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipSujExeAdapter.NAME); 
			}

			// llamada al servicio
			TipSujExeVO tipSujExeVO = ExeServiceLocator.getDefinicionService().deleteTipSujExe
				(userSession, tipSujExeAdapterVO.getTipSujExe());
			
            // Tiene errores recuperables
			if (tipSujExeVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipSujExeAdapterVO.infoString());
				saveDemodaErrors(request, tipSujExeVO);				
				request.setAttribute(TipSujExeAdapter.NAME, tipSujExeAdapterVO);
				return mapping.findForward(ExeConstants.FWD_TIPSUJEXE_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (tipSujExeVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipSujExeAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipSujExeAdapter.NAME, tipSujExeAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipSujExeAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipSujExeAdapter.NAME);
		}
	}
	
		
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, TipSujExeAdapter.NAME);
		
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
			TipSujExeAdapter tipSujExeAdapterVO = (TipSujExeAdapter) userSession.get(TipSujExeAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipSujExeAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipSujExeAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipSujExeAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(tipSujExeAdapterVO, request);
			
            // Tiene errores recuperables
			if (tipSujExeAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipSujExeAdapterVO.infoString()); 
				saveDemodaErrors(request, tipSujExeAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipSujExeAdapter.NAME, tipSujExeAdapterVO);
			}
			
			// llamada al servicio
			tipSujExeAdapterVO = ExeServiceLocator.getDefinicionService().getTipSujExeAdapterParamRecurso(userSession, tipSujExeAdapterVO);
			
            // Tiene errores recuperables
			if (tipSujExeAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipSujExeAdapterVO.infoString()); 
				saveDemodaErrors(request, tipSujExeAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipSujExeAdapter.NAME, tipSujExeAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (tipSujExeAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipSujExeAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipSujExeAdapter.NAME, tipSujExeAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(TipSujExeAdapter.NAME, tipSujExeAdapterVO);
			// Subo el apdater al userSession
			userSession.put(TipSujExeAdapter.NAME, tipSujExeAdapterVO);
			
			return mapping.findForward(ExeConstants.FWD_TIPSUJEXE_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipSujExeAdapter.NAME);
		}
	}
		
}
