//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.frm.view.struts;

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
import ar.gov.rosario.siat.frm.iface.model.ForCamAdapter;
import ar.gov.rosario.siat.frm.iface.model.ForCamVO;
import ar.gov.rosario.siat.frm.iface.service.FrmServiceLocator;
import ar.gov.rosario.siat.frm.iface.util.FrmError;
import ar.gov.rosario.siat.frm.iface.util.FrmSecurityConstants;
import ar.gov.rosario.siat.frm.view.util.FrmConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarForCamDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarForCamDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, FrmSecurityConstants.ABM_FORCAM, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ForCamAdapter forCamAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getForCamAdapterForView(userSession, commonKey)";
				forCamAdapterVO = FrmServiceLocator.getFormularioService().getForCamAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(FrmConstants.FWD_FORCAM_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getForCamAdapterForUpdate(userSession, commonKey)";
				forCamAdapterVO = FrmServiceLocator.getFormularioService().getForCamAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(FrmConstants.FWD_FORCAM_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getForCamAdapterForView(userSession, commonKey)";
				forCamAdapterVO = FrmServiceLocator.getFormularioService().getForCamAdapterForView(userSession, commonKey);				
				forCamAdapterVO.addMessage(BaseError.MSG_ELIMINAR, FrmError.FORCAM_LABEL);
				actionForward = mapping.findForward(FrmConstants.FWD_FORCAM_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getForCamAdapterForCreate(userSession)";
				forCamAdapterVO = FrmServiceLocator.getFormularioService().getForCamAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(FrmConstants.FWD_FORCAM_EDIT_ADAPTER);				
			}
			/*if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getForCamAdapterForView(userSession)";
				forCamAdapterVO = FrmServiceLocator.getFormularioService().getForCamAdapterForView(userSession, commonKey);
				forCamAdapterVO.addMessage(BaseError.MSG_ACTIVAR, FrmError.FORCAM_LABEL);
				actionForward = mapping.findForward(FrmConstants.FWD_FORCAM_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getForCamAdapterForView(userSession)";
				forCamAdapterVO = FrmServiceLocator.getFormularioService().getForCamAdapterForView(userSession, commonKey);
				forCamAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, FrmError.FORCAM_LABEL);
				actionForward = mapping.findForward(FrmConstants.FWD_FORCAM_VIEW_ADAPTER);				
			}*/

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (forCamAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + forCamAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ForCamAdapter.NAME, forCamAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			forCamAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ForCamAdapter.NAME + ": "+ forCamAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ForCamAdapter.NAME, forCamAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ForCamAdapter.NAME, forCamAdapterVO);
			 
			saveDemodaMessages(request, forCamAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ForCamAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, FrmSecurityConstants.ABM_FORCAM, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ForCamAdapter forCamAdapterVO = (ForCamAdapter) userSession.get(ForCamAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (forCamAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ForCamAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ForCamAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			request.setAttribute(DemodaUtil.POPULATEVO_TRIMSTRING, "false");
			DemodaUtil.populateVO(forCamAdapterVO, request);
			
            // Tiene errores recuperables
			if (forCamAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + forCamAdapterVO.infoString()); 
				saveDemodaErrors(request, forCamAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ForCamAdapter.NAME, forCamAdapterVO);
			}
			
			// llamada al servicio
			ForCamVO forCamVO = FrmServiceLocator.getFormularioService().createForCam(userSession, forCamAdapterVO.getForCam());
			
            // Tiene errores recuperables
			if (forCamVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + forCamVO.infoString()); 
				saveDemodaErrors(request, forCamVO);
				return forwardErrorRecoverable(mapping, request, userSession, ForCamAdapter.NAME, forCamAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (forCamVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + forCamVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ForCamAdapter.NAME, forCamAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ForCamAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ForCamAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, FrmSecurityConstants.ABM_FORCAM, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ForCamAdapter forCamAdapterVO = (ForCamAdapter) userSession.get(ForCamAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (forCamAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ForCamAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ForCamAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			request.setAttribute(DemodaUtil.POPULATEVO_TRIMSTRING, "false");
			DemodaUtil.populateVO(forCamAdapterVO, request);
			
            // Tiene errores recuperables
			if (forCamAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + forCamAdapterVO.infoString()); 
				saveDemodaErrors(request, forCamAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ForCamAdapter.NAME, forCamAdapterVO);
			}
			
			// llamada al servicio
			ForCamVO forCamVO = FrmServiceLocator.getFormularioService().updateForCam(userSession, forCamAdapterVO.getForCam());
			
            // Tiene errores recuperables
			if (forCamVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + forCamAdapterVO.infoString()); 
				saveDemodaErrors(request, forCamVO);
				return forwardErrorRecoverable(mapping, request, userSession, ForCamAdapter.NAME, forCamAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (forCamVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + forCamAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ForCamAdapter.NAME, forCamAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ForCamAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ForCamAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, FrmSecurityConstants.ABM_FORCAM, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ForCamAdapter forCamAdapterVO = (ForCamAdapter) userSession.get(ForCamAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (forCamAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ForCamAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ForCamAdapter.NAME); 
			}

			// llamada al servicio
			ForCamVO forCamVO = FrmServiceLocator.getFormularioService().deleteForCam
				(userSession, forCamAdapterVO.getForCam());
			
            // Tiene errores recuperables
			if (forCamVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + forCamAdapterVO.infoString());
				saveDemodaErrors(request, forCamVO);				
				request.setAttribute(ForCamAdapter.NAME, forCamAdapterVO);
				return mapping.findForward(FrmConstants.FWD_FORCAM_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (forCamVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + forCamAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ForCamAdapter.NAME, forCamAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ForCamAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ForCamAdapter.NAME);
		}
	}
/*	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, FrmSecurityConstants.ABM_FORCAM, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ForCamAdapter forCamAdapterVO = (ForCamAdapter) userSession.get(ForCamAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (forCamAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ForCamAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ForCamAdapter.NAME); 
			}

			// llamada al servicio
			ForCamVO forCamVO = FrmServiceLocator.getFormularioService().activarForCam
				(userSession, forCamAdapterVO.getForCam());
			
            // Tiene errores recuperables
			if (forCamVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + forCamAdapterVO.infoString());
				saveDemodaErrors(request, forCamVO);				
				request.setAttribute(ForCamAdapter.NAME, forCamAdapterVO);
				return mapping.findForward(FrmConstants.FWD_FORCAM_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (forCamVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + forCamAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ForCamAdapter.NAME, forCamAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ForCamAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ForCamAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, FrmSecurityConstants.ABM_FORCAM, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ForCamAdapter forCamAdapterVO = (ForCamAdapter) userSession.get(ForCamAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (forCamAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ForCamAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ForCamAdapter.NAME); 
			}

			// llamada al servicio
			ForCamVO forCamVO = FrmServiceLocator.getFormularioService().desactivarForCam
				(userSession, forCamAdapterVO.getForCam());
			
            // Tiene errores recuperables
			if (forCamVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + forCamAdapterVO.infoString());
				saveDemodaErrors(request, forCamVO);				
				request.setAttribute(ForCamAdapter.NAME, forCamAdapterVO);
				return mapping.findForward(FrmConstants.FWD_FORCAM_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (forCamVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + forCamAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ForCamAdapter.NAME, forCamAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ForCamAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ForCamAdapter.NAME);
		}
	}
	*/
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ForCamAdapter.NAME);
		
	}
	
/*	public ActionForward param (ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ForCamAdapter forCamAdapterVO = (ForCamAdapter) userSession.get(ForCamAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (forCamAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ForCamAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ForCamAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(forCamAdapterVO, request);
			
            // Tiene errores recuperables
			if (forCamAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + forCamAdapterVO.infoString()); 
				saveDemodaErrors(request, forCamAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ForCamAdapter.NAME, forCamAdapterVO);
			}
			
			// llamada al servicio
			forCamAdapterVO = FrmServiceLocator.getFormularioService().getForCamAdapterParam(userSession, forCamAdapterVO);
			
            // Tiene errores recuperables
			if (forCamAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + forCamAdapterVO.infoString()); 
				saveDemodaErrors(request, forCamAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ForCamAdapter.NAME, forCamAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (forCamAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + forCamAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ForCamAdapter.NAME, forCamAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(ForCamAdapter.NAME, forCamAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ForCamAdapter.NAME, forCamAdapterVO);
			
			return mapping.findForward(FrmConstants.FWD_FORCAM_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ForCamAdapter.NAME);
		}
	}*/
		
}
