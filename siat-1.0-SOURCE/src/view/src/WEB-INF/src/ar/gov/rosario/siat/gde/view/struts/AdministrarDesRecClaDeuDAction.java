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
import ar.gov.rosario.siat.gde.iface.model.DesRecClaDeuAdapter;
import ar.gov.rosario.siat.gde.iface.model.DesRecClaDeuVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarDesRecClaDeuDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarDesRecClaDeuDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DESRECCLADEU, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		DesRecClaDeuAdapter desRecClaDeuAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			/*if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getDesRecClaDeuAdapterForView(userSession, commonKey)";
				desRecClaDeuAdapterVO = GdeServiceLocator.getDefinicionService().getDesRecClaDeuAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_DESRECCLADEU_VIEW_ADAPTER);
			}*/
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getDesRecClaDeuAdapterForUpdate(userSession, commonKey)";
				desRecClaDeuAdapterVO = GdeServiceLocator.getDefinicionService().getDesRecClaDeuAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_DESRECCLADEU_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getDesRecClaDeuAdapterForView(userSession, commonKey)";
				desRecClaDeuAdapterVO = GdeServiceLocator.getDefinicionService().getDesRecClaDeuAdapterForView(userSession, commonKey);				
				desRecClaDeuAdapterVO.addMessage(BaseError.MSG_ELIMINAR, GdeError.DESRECCLADEU_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_DESRECCLADEU_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getDesRecClaDeuAdapterForCreate(userSession)";
				desRecClaDeuAdapterVO = GdeServiceLocator.getDefinicionService().getDesRecClaDeuAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_DESRECCLADEU_EDIT_ADAPTER);				
			}
			/*if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getDesRecClaDeuAdapterForView(userSession)";
				desRecClaDeuAdapterVO = GdeServiceLocator.getDefinicionService().getDesRecClaDeuAdapterForView(userSession, commonKey);
				desRecClaDeuAdapterVO.addMessage(BaseError.MSG_ACTIVAR, GdeError.DESRECCLADEU_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_DESRECCLADEU_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getDesRecClaDeuAdapterForView(userSession)";
				desRecClaDeuAdapterVO = GdeServiceLocator.getDefinicionService().getDesRecClaDeuAdapterForView(userSession, commonKey);
				desRecClaDeuAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, GdeError.DESRECCLADEU_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_DESRECCLADEU_VIEW_ADAPTER);				
			}*/

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (desRecClaDeuAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + desRecClaDeuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DesRecClaDeuAdapter.NAME, desRecClaDeuAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			desRecClaDeuAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + DesRecClaDeuAdapter.NAME + ": "+ desRecClaDeuAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(DesRecClaDeuAdapter.NAME, desRecClaDeuAdapterVO);
			// Subo el apdater al userSession
			userSession.put(DesRecClaDeuAdapter.NAME, desRecClaDeuAdapterVO);
			 
			saveDemodaMessages(request, desRecClaDeuAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DesRecClaDeuAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DESRECCLADEU, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DesRecClaDeuAdapter desRecClaDeuAdapterVO = (DesRecClaDeuAdapter) userSession.get(DesRecClaDeuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (desRecClaDeuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DesRecClaDeuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DesRecClaDeuAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(desRecClaDeuAdapterVO, request);
			
            // Tiene errores recuperables
			if (desRecClaDeuAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desRecClaDeuAdapterVO.infoString()); 
				saveDemodaErrors(request, desRecClaDeuAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, DesRecClaDeuAdapter.NAME, desRecClaDeuAdapterVO);
			}
			
			// llamada al servicio
			DesRecClaDeuVO desRecClaDeuVO = GdeServiceLocator.getDefinicionService().createDesRecClaDeu(userSession, desRecClaDeuAdapterVO.getDesRecClaDeu());
			
            // Tiene errores recuperables
			if (desRecClaDeuVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desRecClaDeuVO.infoString()); 
				saveDemodaErrors(request, desRecClaDeuVO);
				return forwardErrorRecoverable(mapping, request, userSession, DesRecClaDeuAdapter.NAME, desRecClaDeuAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (desRecClaDeuVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + desRecClaDeuVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DesRecClaDeuAdapter.NAME, desRecClaDeuAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DesRecClaDeuAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DesRecClaDeuAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DESRECCLADEU, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DesRecClaDeuAdapter desRecClaDeuAdapterVO = (DesRecClaDeuAdapter) userSession.get(DesRecClaDeuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (desRecClaDeuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DesRecClaDeuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DesRecClaDeuAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(desRecClaDeuAdapterVO, request);
			
            // Tiene errores recuperables
			if (desRecClaDeuAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desRecClaDeuAdapterVO.infoString()); 
				saveDemodaErrors(request, desRecClaDeuAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, DesRecClaDeuAdapter.NAME, desRecClaDeuAdapterVO);
			}
			
			// llamada al servicio
			DesRecClaDeuVO desRecClaDeuVO = GdeServiceLocator.getDefinicionService().updateDesRecClaDeu(userSession, desRecClaDeuAdapterVO.getDesRecClaDeu());
			
            // Tiene errores recuperables
			if (desRecClaDeuVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desRecClaDeuAdapterVO.infoString()); 
				saveDemodaErrors(request, desRecClaDeuVO);
				return forwardErrorRecoverable(mapping, request, userSession, DesRecClaDeuAdapter.NAME, desRecClaDeuAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (desRecClaDeuVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + desRecClaDeuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DesRecClaDeuAdapter.NAME, desRecClaDeuAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DesRecClaDeuAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DesRecClaDeuAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DESRECCLADEU, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DesRecClaDeuAdapter desRecClaDeuAdapterVO = (DesRecClaDeuAdapter) userSession.get(DesRecClaDeuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (desRecClaDeuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DesRecClaDeuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DesRecClaDeuAdapter.NAME); 
			}

			// llamada al servicio
			DesRecClaDeuVO desRecClaDeuVO = GdeServiceLocator.getDefinicionService().deleteDesRecClaDeu
				(userSession, desRecClaDeuAdapterVO.getDesRecClaDeu());
			
            // Tiene errores recuperables
			if (desRecClaDeuVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desRecClaDeuAdapterVO.infoString());
				saveDemodaErrors(request, desRecClaDeuVO);				
				request.setAttribute(DesRecClaDeuAdapter.NAME, desRecClaDeuAdapterVO);
				return mapping.findForward(GdeConstants.FWD_DESRECCLADEU_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (desRecClaDeuVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + desRecClaDeuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DesRecClaDeuAdapter.NAME, desRecClaDeuAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DesRecClaDeuAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DesRecClaDeuAdapter.NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, DesRecClaDeuAdapter.NAME);
			
		}

/*	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DESRECCLADEU, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DesRecClaDeuAdapter desRecClaDeuAdapterVO = (DesRecClaDeuAdapter) userSession.get(DesRecClaDeuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (desRecClaDeuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DesRecClaDeuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DesRecClaDeuAdapter.NAME); 
			}

			// llamada al servicio
			DesRecClaDeuVO desRecClaDeuVO = GdeServiceLocator.getDefinicionService().activarDesRecClaDeu
				(userSession, desRecClaDeuAdapterVO.getDesRecClaDeu());
			
            // Tiene errores recuperables
			if (desRecClaDeuVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desRecClaDeuAdapterVO.infoString());
				saveDemodaErrors(request, desRecClaDeuVO);				
				request.setAttribute(DesRecClaDeuAdapter.NAME, desRecClaDeuAdapterVO);
				return mapping.findForward(GdeConstants.FWD_DESRECCLADEU_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (desRecClaDeuVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + desRecClaDeuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DesRecClaDeuAdapter.NAME, desRecClaDeuAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DesRecClaDeuAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DesRecClaDeuAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DESRECCLADEU, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DesRecClaDeuAdapter desRecClaDeuAdapterVO = (DesRecClaDeuAdapter) userSession.get(DesRecClaDeuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (desRecClaDeuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DesRecClaDeuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DesRecClaDeuAdapter.NAME); 
			}

			// llamada al servicio
			DesRecClaDeuVO desRecClaDeuVO = GdeServiceLocator.getDefinicionService().desactivarDesRecClaDeu
				(userSession, desRecClaDeuAdapterVO.getDesRecClaDeu());
			
            // Tiene errores recuperables
			if (desRecClaDeuVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desRecClaDeuAdapterVO.infoString());
				saveDemodaErrors(request, desRecClaDeuVO);				
				request.setAttribute(DesRecClaDeuAdapter.NAME, desRecClaDeuAdapterVO);
				return mapping.findForward(GdeConstants.FWD_DESRECCLADEU_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (desRecClaDeuVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + desRecClaDeuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DesRecClaDeuAdapter.NAME, desRecClaDeuAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DesRecClaDeuAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DesRecClaDeuAdapter.NAME);
		}
	}
	
	public ActionForward param (ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DesRecClaDeuAdapter desRecClaDeuAdapterVO = (DesRecClaDeuAdapter) userSession.get(DesRecClaDeuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (desRecClaDeuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DesRecClaDeuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DesRecClaDeuAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(desRecClaDeuAdapterVO, request);
			
            // Tiene errores recuperables
			if (desRecClaDeuAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desRecClaDeuAdapterVO.infoString()); 
				saveDemodaErrors(request, desRecClaDeuAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, DesRecClaDeuAdapter.NAME, desRecClaDeuAdapterVO);
			}
			
			// llamada al servicio
			desRecClaDeuAdapterVO = GdeServiceLocator.getDefinicionService().getDesRecClaDeuAdapterParam(userSession, desRecClaDeuAdapterVO);
			
            // Tiene errores recuperables
			if (desRecClaDeuAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desRecClaDeuAdapterVO.infoString()); 
				saveDemodaErrors(request, desRecClaDeuAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, DesRecClaDeuAdapter.NAME, desRecClaDeuAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (desRecClaDeuAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + desRecClaDeuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DesRecClaDeuAdapter.NAME, desRecClaDeuAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(DesRecClaDeuAdapter.NAME, desRecClaDeuAdapterVO);
			// Subo el apdater al userSession
			userSession.put(DesRecClaDeuAdapter.NAME, desRecClaDeuAdapterVO);
			
			return mapping.findForward(GdeConstants.FWD_DESRECCLADEU_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DesRecClaDeuAdapter.NAME);
		}
	}
	*/	
}
