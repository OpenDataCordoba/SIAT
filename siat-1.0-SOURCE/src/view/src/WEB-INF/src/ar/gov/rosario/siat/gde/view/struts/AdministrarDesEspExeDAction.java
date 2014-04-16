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
import ar.gov.rosario.siat.gde.iface.model.DesEspExeAdapter;
import ar.gov.rosario.siat.gde.iface.model.DesEspExeVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarDesEspExeDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarDesEspExeDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DESESPEXE, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		DesEspExeAdapter desEspExeAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getDesEspExeAdapterForView(userSession, commonKey)";
				desEspExeAdapterVO = GdeServiceLocator.getDefinicionService().getDesEspExeAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_DESESPEXE_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getDesEspExeAdapterForUpdate(userSession, commonKey)";
				desEspExeAdapterVO = GdeServiceLocator.getDefinicionService().getDesEspExeAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_DESESPEXE_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getDesEspExeAdapterForView(userSession, commonKey)";
				desEspExeAdapterVO = GdeServiceLocator.getDefinicionService().getDesEspExeAdapterForView(userSession, commonKey);				
				desEspExeAdapterVO.addMessage(BaseError.MSG_ELIMINAR, GdeError.DESESPEXE_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_DESESPEXE_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getDesEspExeAdapterForCreate(userSession)";
				desEspExeAdapterVO = GdeServiceLocator.getDefinicionService().getDesEspExeAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_DESESPEXE_EDIT_ADAPTER);				
			}
		/*	if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getDesEspExeAdapterForView(userSession)";
				desEspExeAdapterVO = GdeServiceLocator.getDefinicionService().getDesEspExeAdapterForView(userSession, commonKey);
				desEspExeAdapterVO.addMessage(BaseError.MSG_ACTIVAR, GdeError.DESESPEXE_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_DESESPEXE_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getDesEspExeAdapterForView(userSession)";
				desEspExeAdapterVO = GdeServiceLocator.getDefinicionService().getDesEspExeAdapterForView(userSession, commonKey);
				desEspExeAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, GdeError.DESESPEXE_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_DESESPEXE_VIEW_ADAPTER);				
			}*/

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (desEspExeAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + desEspExeAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DesEspExeAdapter.NAME, desEspExeAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			desEspExeAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + DesEspExeAdapter.NAME + ": "+ desEspExeAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(DesEspExeAdapter.NAME, desEspExeAdapterVO);
			// Subo el apdater al userSession
			userSession.put(DesEspExeAdapter.NAME, desEspExeAdapterVO);
			 
			saveDemodaMessages(request, desEspExeAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DesEspExeAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DESESPEXE, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DesEspExeAdapter desEspExeAdapterVO = (DesEspExeAdapter) userSession.get(DesEspExeAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (desEspExeAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DesEspExeAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DesEspExeAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(desEspExeAdapterVO, request);
			
            // Tiene errores recuperables
			if (desEspExeAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desEspExeAdapterVO.infoString()); 
				saveDemodaErrors(request, desEspExeAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, DesEspExeAdapter.NAME, desEspExeAdapterVO);
			}
			
			// llamada al servicio
			DesEspExeVO desEspExeVO = GdeServiceLocator.getDefinicionService().createDesEspExe(userSession, desEspExeAdapterVO.getDesEspExe());
			
            // Tiene errores recuperables
			if (desEspExeVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desEspExeVO.infoString()); 
				saveDemodaErrors(request, desEspExeVO);
				return forwardErrorRecoverable(mapping, request, userSession, DesEspExeAdapter.NAME, desEspExeAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (desEspExeVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + desEspExeVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DesEspExeAdapter.NAME, desEspExeAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DesEspExeAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DesEspExeAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DESESPEXE, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DesEspExeAdapter desEspExeAdapterVO = (DesEspExeAdapter) userSession.get(DesEspExeAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (desEspExeAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DesEspExeAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DesEspExeAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(desEspExeAdapterVO, request);
			
            // Tiene errores recuperables
			if (desEspExeAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desEspExeAdapterVO.infoString()); 
				saveDemodaErrors(request, desEspExeAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, DesEspExeAdapter.NAME, desEspExeAdapterVO);
			}
			
			// llamada al servicio
			DesEspExeVO desEspExeVO = GdeServiceLocator.getDefinicionService().updateDesEspExe(userSession, desEspExeAdapterVO.getDesEspExe());
			
            // Tiene errores recuperables
			if (desEspExeVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desEspExeAdapterVO.infoString()); 
				saveDemodaErrors(request, desEspExeVO);
				return forwardErrorRecoverable(mapping, request, userSession, DesEspExeAdapter.NAME, desEspExeAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (desEspExeVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + desEspExeAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DesEspExeAdapter.NAME, desEspExeAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DesEspExeAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DesEspExeAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DESESPEXE, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DesEspExeAdapter desEspExeAdapterVO = (DesEspExeAdapter) userSession.get(DesEspExeAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (desEspExeAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DesEspExeAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DesEspExeAdapter.NAME); 
			}

			// llamada al servicio
			DesEspExeVO desEspExeVO = GdeServiceLocator.getDefinicionService().deleteDesEspExe
				(userSession, desEspExeAdapterVO.getDesEspExe());
			
            // Tiene errores recuperables
			if (desEspExeVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desEspExeAdapterVO.infoString());
				saveDemodaErrors(request, desEspExeVO);				
				request.setAttribute(DesEspExeAdapter.NAME, desEspExeAdapterVO);
				return mapping.findForward(GdeConstants.FWD_DESESPEXE_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (desEspExeVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + desEspExeAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DesEspExeAdapter.NAME, desEspExeAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DesEspExeAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DesEspExeAdapter.NAME);
		}
	}
/*	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DESESPEXE, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DesEspExeAdapter desEspExeAdapterVO = (DesEspExeAdapter) userSession.get(DesEspExeAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (desEspExeAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DesEspExeAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DesEspExeAdapter.NAME); 
			}

			// llamada al servicio
			DesEspExeVO desEspExeVO = GdeServiceLocator.getDefinicionService().activarDesEspExe
				(userSession, desEspExeAdapterVO.getDesEspExe());
			
            // Tiene errores recuperables
			if (desEspExeVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desEspExeAdapterVO.infoString());
				saveDemodaErrors(request, desEspExeVO);				
				request.setAttribute(DesEspExeAdapter.NAME, desEspExeAdapterVO);
				return mapping.findForward(GdeConstants.FWD_DESESPEXE_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (desEspExeVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + desEspExeAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DesEspExeAdapter.NAME, desEspExeAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DesEspExeAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DesEspExeAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DESESPEXE, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DesEspExeAdapter desEspExeAdapterVO = (DesEspExeAdapter) userSession.get(DesEspExeAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (desEspExeAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DesEspExeAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DesEspExeAdapter.NAME); 
			}

			// llamada al servicio
			DesEspExeVO desEspExeVO = GdeServiceLocator.getdefinicionService().desactivarDesEspExe
				(userSession, desEspExeAdapterVO.getDesEspExe());
			
            // Tiene errores recuperables
			if (desEspExeVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desEspExeAdapterVO.infoString());
				saveDemodaErrors(request, desEspExeVO);				
				request.setAttribute(DesEspExeAdapter.NAME, desEspExeAdapterVO);
				return mapping.findForward(GdeConstants.FWD_DESESPEXE_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (desEspExeVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + desEspExeAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DesEspExeAdapter.NAME, desEspExeAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DesEspExeAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DesEspExeAdapter.NAME);
		}
	}
	*/
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, DesEspExeAdapter.NAME);
		
	}
	/*
	public ActionForward param (ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DesEspExeAdapter desEspExeAdapterVO = (DesEspExeAdapter) userSession.get(DesEspExeAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (desEspExeAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DesEspExeAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DesEspExeAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(desEspExeAdapterVO, request);
			
            // Tiene errores recuperables
			if (desEspExeAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desEspExeAdapterVO.infoString()); 
				saveDemodaErrors(request, desEspExeAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, DesEspExeAdapter.NAME, desEspExeAdapterVO);
			}
			
			// llamada al servicio
			desEspExeAdapterVO = GdeServiceLocator.getdefinicionService().getDesEspExeAdapterParam(userSession, desEspExeAdapterVO);
			
            // Tiene errores recuperables
			if (desEspExeAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desEspExeAdapterVO.infoString()); 
				saveDemodaErrors(request, desEspExeAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, DesEspExeAdapter.NAME, desEspExeAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (desEspExeAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + desEspExeAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DesEspExeAdapter.NAME, desEspExeAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(DesEspExeAdapter.NAME, desEspExeAdapterVO);
			// Subo el apdater al userSession
			userSession.put(DesEspExeAdapter.NAME, desEspExeAdapterVO);
			
			return mapping.findForward(GdeConstants.FWD_DESESPEXE_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DesEspExeAdapter.NAME);
		}
	}
	*/	
}
