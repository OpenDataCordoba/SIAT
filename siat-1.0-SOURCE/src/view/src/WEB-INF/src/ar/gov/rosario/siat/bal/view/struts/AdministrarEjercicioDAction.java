//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.bal.iface.model.EjercicioAdapter;
import ar.gov.rosario.siat.bal.iface.model.EjercicioVO;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarEjercicioDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEjercicioDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_EJERCICIO, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		EjercicioAdapter ejercicioAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getEjercicioAdapterForView(userSession, commonKey)";
				ejercicioAdapterVO = BalServiceLocator.getDefinicionService().getEjercicioAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_EJERCICIO_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getEjercicioAdapterForUpdate(userSession, commonKey)";
				ejercicioAdapterVO = BalServiceLocator.getDefinicionService().getEjercicioAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_EJERCICIO_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getEjercicioAdapterForView(userSession, commonKey)";
				ejercicioAdapterVO = BalServiceLocator.getDefinicionService().getEjercicioAdapterForView(userSession, commonKey);				
				ejercicioAdapterVO.addMessage(BaseError.MSG_ELIMINAR, BalError.EJERCICIO_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_EJERCICIO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getEjercicioAdapterForCreate(userSession)";
				ejercicioAdapterVO = BalServiceLocator.getDefinicionService().getEjercicioAdapterForCreate(userSession);
				actionForward = mapping.findForward(BalConstants.FWD_EJERCICIO_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getEjercicioAdapterForView(userSession)";
				ejercicioAdapterVO = BalServiceLocator.getDefinicionService().getEjercicioAdapterForView(userSession, commonKey);
				ejercicioAdapterVO.addMessage(BaseError.MSG_ACTIVAR, BalError.EJERCICIO_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_EJERCICIO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getEjercicioAdapterForView(userSession)";
				ejercicioAdapterVO = BalServiceLocator.getDefinicionService().getEjercicioAdapterForView(userSession, commonKey);
				ejercicioAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, BalError.EJERCICIO_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_EJERCICIO_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (ejercicioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + ejercicioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EjercicioAdapter.NAME, ejercicioAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			ejercicioAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + EjercicioAdapter.NAME + ": "+ ejercicioAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(EjercicioAdapter.NAME, ejercicioAdapterVO);
			// Subo el apdater al userSession
			userSession.put(EjercicioAdapter.NAME, ejercicioAdapterVO);
			 
			saveDemodaMessages(request, ejercicioAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EjercicioAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_EJERCICIO, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			EjercicioAdapter ejercicioAdapterVO = (EjercicioAdapter) userSession.get(EjercicioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (ejercicioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EjercicioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EjercicioAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(ejercicioAdapterVO, request);
			
            // Tiene errores recuperables
			if (ejercicioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ejercicioAdapterVO.infoString()); 
				saveDemodaErrors(request, ejercicioAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, EjercicioAdapter.NAME, ejercicioAdapterVO);
			}
			
			// llamada al servicio
			EjercicioVO ejercicioVO = BalServiceLocator.getDefinicionService().createEjercicio(userSession, ejercicioAdapterVO.getEjercicio());
			
            // Tiene errores recuperables
			if (ejercicioVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ejercicioVO.infoString()); 
				saveDemodaErrors(request, ejercicioVO);
				return forwardErrorRecoverable(mapping, request, userSession, EjercicioAdapter.NAME, ejercicioAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (ejercicioVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + ejercicioVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EjercicioAdapter.NAME, ejercicioAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, EjercicioAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EjercicioAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_EJERCICIO, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			EjercicioAdapter ejercicioAdapterVO = (EjercicioAdapter) userSession.get(EjercicioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (ejercicioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EjercicioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EjercicioAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(ejercicioAdapterVO, request);
			
            // Tiene errores recuperables
			if (ejercicioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ejercicioAdapterVO.infoString()); 
				saveDemodaErrors(request, ejercicioAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, EjercicioAdapter.NAME, ejercicioAdapterVO);
			}
			
			// llamada al servicio
			EjercicioVO ejercicioVO = BalServiceLocator.getDefinicionService().updateEjercicio(userSession, ejercicioAdapterVO.getEjercicio());
			
            // Tiene errores recuperables
			if (ejercicioVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ejercicioAdapterVO.infoString()); 
				saveDemodaErrors(request, ejercicioVO);
				return forwardErrorRecoverable(mapping, request, userSession, EjercicioAdapter.NAME, ejercicioAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (ejercicioVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + ejercicioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EjercicioAdapter.NAME, ejercicioAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, EjercicioAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EjercicioAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_EJERCICIO, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			EjercicioAdapter ejercicioAdapterVO = (EjercicioAdapter) userSession.get(EjercicioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (ejercicioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EjercicioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EjercicioAdapter.NAME); 
			}

			// llamada al servicio
			EjercicioVO ejercicioVO = BalServiceLocator.getDefinicionService().deleteEjercicio
				(userSession, ejercicioAdapterVO.getEjercicio());
			
            // Tiene errores recuperables
			if (ejercicioVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ejercicioAdapterVO.infoString());
				saveDemodaErrors(request, ejercicioVO);				
				request.setAttribute(EjercicioAdapter.NAME, ejercicioAdapterVO);
				return mapping.findForward(BalConstants.FWD_EJERCICIO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (ejercicioVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + ejercicioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EjercicioAdapter.NAME, ejercicioAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, EjercicioAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EjercicioAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_EJERCICIO, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			EjercicioAdapter ejercicioAdapterVO = (EjercicioAdapter) userSession.get(EjercicioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (ejercicioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EjercicioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EjercicioAdapter.NAME); 
			}

			// llamada al servicio
			EjercicioVO ejercicioVO = BalServiceLocator.getDefinicionService().activarEjercicio
				(userSession, ejercicioAdapterVO.getEjercicio());
			
            // Tiene errores recuperables
			if (ejercicioVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ejercicioAdapterVO.infoString());
				saveDemodaErrors(request, ejercicioVO);				
				request.setAttribute(EjercicioAdapter.NAME, ejercicioAdapterVO);
				return mapping.findForward(BalConstants.FWD_EJERCICIO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (ejercicioVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + ejercicioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EjercicioAdapter.NAME, ejercicioAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, EjercicioAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EjercicioAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_EJERCICIO, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			EjercicioAdapter ejercicioAdapterVO = (EjercicioAdapter) userSession.get(EjercicioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (ejercicioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EjercicioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EjercicioAdapter.NAME); 
			}

			// llamada al servicio
			EjercicioVO ejercicioVO = BalServiceLocator.getDefinicionService().desactivarEjercicio
				(userSession, ejercicioAdapterVO.getEjercicio());
			
            // Tiene errores recuperables
			if (ejercicioVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ejercicioAdapterVO.infoString());
				saveDemodaErrors(request, ejercicioVO);				
				request.setAttribute(EjercicioAdapter.NAME, ejercicioAdapterVO);
				return mapping.findForward(BalConstants.FWD_EJERCICIO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (ejercicioVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + ejercicioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EjercicioAdapter.NAME, ejercicioAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, EjercicioAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EjercicioAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, EjercicioAdapter.NAME);
		
	}
		
}
