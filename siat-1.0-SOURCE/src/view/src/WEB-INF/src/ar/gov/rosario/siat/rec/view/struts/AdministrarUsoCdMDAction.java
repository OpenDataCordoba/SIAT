//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.view.struts;

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
import ar.gov.rosario.siat.rec.iface.model.UsoCdMAdapter;
import ar.gov.rosario.siat.rec.iface.model.UsoCdMVO;
import ar.gov.rosario.siat.rec.iface.service.RecServiceLocator;
import ar.gov.rosario.siat.rec.iface.util.RecError;
import ar.gov.rosario.siat.rec.iface.util.RecSecurityConstants;
import ar.gov.rosario.siat.rec.view.util.RecConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarUsoCdMDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarUsoCdMDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_USOCDM, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		UsoCdMAdapter usoCdMAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getUsoCdMAdapterForView(userSession, commonKey)";
				usoCdMAdapterVO = RecServiceLocator.getCdmService().getUsoCdMAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(RecConstants.FWD_USOCDM_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getUsoCdMAdapterForUpdate(userSession, commonKey)";
				usoCdMAdapterVO = RecServiceLocator.getCdmService().getUsoCdMAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(RecConstants.FWD_USOCDM_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getUsoCdMAdapterForView(userSession, commonKey)";
				usoCdMAdapterVO = RecServiceLocator.getCdmService().getUsoCdMAdapterForView(userSession, commonKey);				
				usoCdMAdapterVO.addMessage(BaseError.MSG_ELIMINAR, RecError.USOCDM_LABEL);
				actionForward = mapping.findForward(RecConstants.FWD_USOCDM_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getUsoCdMAdapterForCreate(userSession)";
				usoCdMAdapterVO = RecServiceLocator.getCdmService().getUsoCdMAdapterForCreate(userSession);
				actionForward = mapping.findForward(RecConstants.FWD_USOCDM_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getUsoCdMAdapterForView(userSession)";
				usoCdMAdapterVO = RecServiceLocator.getCdmService().getUsoCdMAdapterForView(userSession, commonKey);
				usoCdMAdapterVO.addMessage(BaseError.MSG_ACTIVAR, RecError.USOCDM_LABEL);
				actionForward = mapping.findForward(RecConstants.FWD_USOCDM_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getUsoCdMAdapterForView(userSession)";
				usoCdMAdapterVO = RecServiceLocator.getCdmService().getUsoCdMAdapterForView(userSession, commonKey);
				usoCdMAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, RecError.USOCDM_LABEL);
				actionForward = mapping.findForward(RecConstants.FWD_USOCDM_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (usoCdMAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + usoCdMAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, UsoCdMAdapter.NAME, usoCdMAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			usoCdMAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + UsoCdMAdapter.NAME + ": "+ usoCdMAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(UsoCdMAdapter.NAME, usoCdMAdapterVO);
			// Subo el apdater al userSession
			userSession.put(UsoCdMAdapter.NAME, usoCdMAdapterVO);
			 
			saveDemodaMessages(request, usoCdMAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, UsoCdMAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_USOCDM, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			UsoCdMAdapter usoCdMAdapterVO = (UsoCdMAdapter) userSession.get(UsoCdMAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (usoCdMAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + UsoCdMAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, UsoCdMAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(usoCdMAdapterVO, request);
			
            // Tiene errores recuperables
			if (usoCdMAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + usoCdMAdapterVO.infoString()); 
				saveDemodaErrors(request, usoCdMAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, UsoCdMAdapter.NAME, usoCdMAdapterVO);
			}
			
			// llamada al servicio
			UsoCdMVO usoCdMVO = RecServiceLocator.getCdmService().createUsoCdM(userSession, usoCdMAdapterVO.getUsoCdM());
			
            // Tiene errores recuperables
			if (usoCdMVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + usoCdMVO.infoString()); 
				saveDemodaErrors(request, usoCdMVO);
				return forwardErrorRecoverable(mapping, request, userSession, UsoCdMAdapter.NAME, usoCdMAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (usoCdMVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + usoCdMVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, UsoCdMAdapter.NAME, usoCdMAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, UsoCdMAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, UsoCdMAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_USOCDM, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			UsoCdMAdapter usoCdMAdapterVO = (UsoCdMAdapter) userSession.get(UsoCdMAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (usoCdMAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + UsoCdMAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, UsoCdMAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(usoCdMAdapterVO, request);
			
            // Tiene errores recuperables
			if (usoCdMAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + usoCdMAdapterVO.infoString()); 
				saveDemodaErrors(request, usoCdMAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, UsoCdMAdapter.NAME, usoCdMAdapterVO);
			}
			
			// llamada al servicio
			UsoCdMVO usoCdMVO = RecServiceLocator.getCdmService().updateUsoCdM(userSession, usoCdMAdapterVO.getUsoCdM());
			
            // Tiene errores recuperables
			if (usoCdMVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + usoCdMAdapterVO.infoString()); 
				saveDemodaErrors(request, usoCdMVO);
				return forwardErrorRecoverable(mapping, request, userSession, UsoCdMAdapter.NAME, usoCdMAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (usoCdMVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + usoCdMAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, UsoCdMAdapter.NAME, usoCdMAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, UsoCdMAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, UsoCdMAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_USOCDM, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			UsoCdMAdapter usoCdMAdapterVO = (UsoCdMAdapter) userSession.get(UsoCdMAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (usoCdMAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + UsoCdMAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, UsoCdMAdapter.NAME); 
			}

			// llamada al servicio
			UsoCdMVO usoCdMVO = RecServiceLocator.getCdmService().deleteUsoCdM
				(userSession, usoCdMAdapterVO.getUsoCdM());
			
            // Tiene errores recuperables
			if (usoCdMVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + usoCdMAdapterVO.infoString());
				saveDemodaErrors(request, usoCdMVO);				
				request.setAttribute(UsoCdMAdapter.NAME, usoCdMAdapterVO);
				return mapping.findForward(RecConstants.FWD_USOCDM_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (usoCdMVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + usoCdMAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, UsoCdMAdapter.NAME, usoCdMAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, UsoCdMAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, UsoCdMAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_USOCDM, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			UsoCdMAdapter usoCdMAdapterVO = (UsoCdMAdapter) userSession.get(UsoCdMAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (usoCdMAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + UsoCdMAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, UsoCdMAdapter.NAME); 
			}

			// llamada al servicio
			UsoCdMVO usoCdMVO = RecServiceLocator.getCdmService().activarUsoCdM
				(userSession, usoCdMAdapterVO.getUsoCdM());
			
            // Tiene errores recuperables
			if (usoCdMVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + usoCdMAdapterVO.infoString());
				saveDemodaErrors(request, usoCdMVO);				
				request.setAttribute(UsoCdMAdapter.NAME, usoCdMAdapterVO);
				return mapping.findForward(RecConstants.FWD_USOCDM_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (usoCdMVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + usoCdMAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, UsoCdMAdapter.NAME, usoCdMAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, UsoCdMAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, UsoCdMAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_USOCDM, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			UsoCdMAdapter usoCdMAdapterVO = (UsoCdMAdapter) userSession.get(UsoCdMAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (usoCdMAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + UsoCdMAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, UsoCdMAdapter.NAME); 
			}

			// llamada al servicio
			UsoCdMVO usoCdMVO = RecServiceLocator.getCdmService().desactivarUsoCdM
				(userSession, usoCdMAdapterVO.getUsoCdM());
			
            // Tiene errores recuperables
			if (usoCdMVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + usoCdMAdapterVO.infoString());
				saveDemodaErrors(request, usoCdMVO);				
				request.setAttribute(UsoCdMAdapter.NAME, usoCdMAdapterVO);
				return mapping.findForward(RecConstants.FWD_USOCDM_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (usoCdMVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + usoCdMAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, UsoCdMAdapter.NAME, usoCdMAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, UsoCdMAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, UsoCdMAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, UsoCdMAdapter.NAME);
		
	}
	
}
