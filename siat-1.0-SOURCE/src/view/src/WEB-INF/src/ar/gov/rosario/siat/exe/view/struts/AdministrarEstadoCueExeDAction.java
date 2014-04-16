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
import ar.gov.rosario.siat.exe.iface.model.EstadoCueExeAdapter;
import ar.gov.rosario.siat.exe.iface.model.EstadoCueExeVO;
import ar.gov.rosario.siat.exe.iface.service.ExeServiceLocator;
import ar.gov.rosario.siat.exe.iface.util.ExeError;
import ar.gov.rosario.siat.exe.iface.util.ExeSecurityConstants;
import ar.gov.rosario.siat.exe.view.util.ExeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarEstadoCueExeDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEstadoCueExeDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_ESTADOCUEEXE, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		EstadoCueExeAdapter estadoCueExeAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getEstadoCueExeAdapterForView(userSession, commonKey)";
				estadoCueExeAdapterVO = ExeServiceLocator.getDefinicionService().getEstadoCueExeAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(ExeConstants.FWD_ESTADOCUEEXE_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getEstadoCueExeAdapterForUpdate(userSession, commonKey)";
				estadoCueExeAdapterVO = ExeServiceLocator.getDefinicionService().getEstadoCueExeAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(ExeConstants.FWD_ESTADOCUEEXE_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getEstadoCueExeAdapterForView(userSession, commonKey)";
				estadoCueExeAdapterVO = ExeServiceLocator.getDefinicionService().getEstadoCueExeAdapterForView(userSession, commonKey);				
				estadoCueExeAdapterVO.addMessage(BaseError.MSG_ELIMINAR, ExeError.ESTADOCUEEXE_LABEL);
				actionForward = mapping.findForward(ExeConstants.FWD_ESTADOCUEEXE_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getEstadoCueExeAdapterForCreate(userSession)";
				estadoCueExeAdapterVO = ExeServiceLocator.getDefinicionService().getEstadoCueExeAdapterForCreate(userSession);
				actionForward = mapping.findForward(ExeConstants.FWD_ESTADOCUEEXE_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getEstadoCueExeAdapterForView(userSession)";
				estadoCueExeAdapterVO = ExeServiceLocator.getDefinicionService().getEstadoCueExeAdapterForView(userSession, commonKey);
				estadoCueExeAdapterVO.addMessage(BaseError.MSG_ACTIVAR, ExeError.ESTADOCUEEXE_LABEL);
				actionForward = mapping.findForward(ExeConstants.FWD_ESTADOCUEEXE_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getEstadoCueExeAdapterForView(userSession)";
				estadoCueExeAdapterVO = ExeServiceLocator.getDefinicionService().getEstadoCueExeAdapterForView(userSession, commonKey);
				estadoCueExeAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, ExeError.ESTADOCUEEXE_LABEL);
				actionForward = mapping.findForward(ExeConstants.FWD_ESTADOCUEEXE_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (estadoCueExeAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + estadoCueExeAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EstadoCueExeAdapter.NAME, estadoCueExeAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			estadoCueExeAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + EstadoCueExeAdapter.NAME + ": "+ estadoCueExeAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(EstadoCueExeAdapter.NAME, estadoCueExeAdapterVO);
			// Subo el apdater al userSession
			userSession.put(EstadoCueExeAdapter.NAME, estadoCueExeAdapterVO);
			 
			saveDemodaMessages(request, estadoCueExeAdapterVO);
		
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EstadoCueExeAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_ESTADOCUEEXE, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			EstadoCueExeAdapter estadoCueExeAdapterVO = (EstadoCueExeAdapter) userSession.get(EstadoCueExeAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (estadoCueExeAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EstadoCueExeAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EstadoCueExeAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(estadoCueExeAdapterVO, request);
			
            // Tiene errores recuperables
			if (estadoCueExeAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + estadoCueExeAdapterVO.infoString()); 
				saveDemodaErrors(request, estadoCueExeAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, EstadoCueExeAdapter.NAME, estadoCueExeAdapterVO);
			}
			
			// llamada al servicio
			EstadoCueExeVO estadoCueExeVO = ExeServiceLocator.getDefinicionService().createEstadoCueExe(userSession, estadoCueExeAdapterVO.getEstadoCueExe());
			
            // Tiene errores recuperables
			if (estadoCueExeVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + estadoCueExeVO.infoString()); 
				saveDemodaErrors(request, estadoCueExeVO);
				return forwardErrorRecoverable(mapping, request, userSession, EstadoCueExeAdapter.NAME, estadoCueExeAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (estadoCueExeVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + estadoCueExeVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EstadoCueExeAdapter.NAME, estadoCueExeAdapterVO);
			}
				
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, EstadoCueExeAdapter.NAME);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EstadoCueExeAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_ESTADOCUEEXE, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			EstadoCueExeAdapter estadoCueExeAdapterVO = (EstadoCueExeAdapter) userSession.get(EstadoCueExeAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (estadoCueExeAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EstadoCueExeAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EstadoCueExeAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(estadoCueExeAdapterVO, request);
			
            // Tiene errores recuperables
			if (estadoCueExeAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + estadoCueExeAdapterVO.infoString()); 
				saveDemodaErrors(request, estadoCueExeAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, EstadoCueExeAdapter.NAME, estadoCueExeAdapterVO);
			}
			
			// llamada al servicio
			EstadoCueExeVO estadoCueExeVO = ExeServiceLocator.getDefinicionService().updateEstadoCueExe(userSession, estadoCueExeAdapterVO.getEstadoCueExe());
			
            // Tiene errores recuperables
			if (estadoCueExeVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + estadoCueExeAdapterVO.infoString()); 
				saveDemodaErrors(request, estadoCueExeVO);
				return forwardErrorRecoverable(mapping, request, userSession, EstadoCueExeAdapter.NAME, estadoCueExeAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (estadoCueExeVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + estadoCueExeAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EstadoCueExeAdapter.NAME, estadoCueExeAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, EstadoCueExeAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EstadoCueExeAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_ESTADOCUEEXE, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			EstadoCueExeAdapter estadoCueExeAdapterVO = (EstadoCueExeAdapter) userSession.get(EstadoCueExeAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (estadoCueExeAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EstadoCueExeAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EstadoCueExeAdapter.NAME); 
			}

			// llamada al servicio
			EstadoCueExeVO estadoCueExeVO = ExeServiceLocator.getDefinicionService().deleteEstadoCueExe
				(userSession, estadoCueExeAdapterVO.getEstadoCueExe());
			
            // Tiene errores recuperables
			if (estadoCueExeVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + estadoCueExeAdapterVO.infoString());
				saveDemodaErrors(request, estadoCueExeVO);				
				request.setAttribute(EstadoCueExeAdapter.NAME, estadoCueExeAdapterVO);
				return mapping.findForward(ExeConstants.FWD_ESTADOCUEEXE_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (estadoCueExeVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + estadoCueExeAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EstadoCueExeAdapter.NAME, estadoCueExeAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, EstadoCueExeAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EstadoCueExeAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_ESTADOCUEEXE, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			EstadoCueExeAdapter estadoCueExeAdapterVO = (EstadoCueExeAdapter) userSession.get(EstadoCueExeAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (estadoCueExeAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EstadoCueExeAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EstadoCueExeAdapter.NAME); 
			}

			// llamada al servicio
			EstadoCueExeVO estadoCueExeVO = ExeServiceLocator.getDefinicionService().activarEstadoCueExe
				(userSession, estadoCueExeAdapterVO.getEstadoCueExe());
			
            // Tiene errores recuperables
			if (estadoCueExeVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + estadoCueExeAdapterVO.infoString());
				saveDemodaErrors(request, estadoCueExeVO);				
				request.setAttribute(EstadoCueExeAdapter.NAME, estadoCueExeAdapterVO);
				return mapping.findForward(ExeConstants.FWD_ESTADOCUEEXE_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (estadoCueExeVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + estadoCueExeAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EstadoCueExeAdapter.NAME, estadoCueExeAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, EstadoCueExeAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EstadoCueExeAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_ESTADOCUEEXE, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			EstadoCueExeAdapter estadoCueExeAdapterVO = (EstadoCueExeAdapter) userSession.get(EstadoCueExeAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (estadoCueExeAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EstadoCueExeAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EstadoCueExeAdapter.NAME); 
			}

			// llamada al servicio
			EstadoCueExeVO estadoCueExeVO = ExeServiceLocator.getDefinicionService().desactivarEstadoCueExe
				(userSession, estadoCueExeAdapterVO.getEstadoCueExe());
			
            // Tiene errores recuperables
			if (estadoCueExeVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + estadoCueExeAdapterVO.infoString());
				saveDemodaErrors(request, estadoCueExeVO);				
				request.setAttribute(EstadoCueExeAdapter.NAME, estadoCueExeAdapterVO);
				return mapping.findForward(ExeConstants.FWD_ESTADOCUEEXE_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (estadoCueExeVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + estadoCueExeAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EstadoCueExeAdapter.NAME, estadoCueExeAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, EstadoCueExeAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EstadoCueExeAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, EstadoCueExeAdapter.NAME);
		
	}
	
			
}
