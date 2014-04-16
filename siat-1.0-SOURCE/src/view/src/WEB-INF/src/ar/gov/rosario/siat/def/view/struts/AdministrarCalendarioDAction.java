//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.view.struts;

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
import ar.gov.rosario.siat.def.iface.model.CalendarioAdapter;
import ar.gov.rosario.siat.def.iface.model.CalendarioVO;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarCalendarioDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarCalendarioDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_CALENDARIO, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		CalendarioAdapter calendarioAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getCalendarioAdapterForView(userSession, commonKey)";
				calendarioAdapterVO = DefServiceLocator.getGravamenService().getCalendarioAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_CALENDARIO_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getCalendarioAdapterForUpdate(userSession, commonKey)";
				calendarioAdapterVO = DefServiceLocator.getGravamenService().getCalendarioAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_CALENDARIO_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getCalendarioAdapterForView(userSession, commonKey)";
				calendarioAdapterVO = DefServiceLocator.getGravamenService().getCalendarioAdapterForView(userSession, commonKey);				
				calendarioAdapterVO.addMessage(BaseError.MSG_ELIMINAR, DefError.CALENDARIO_LABEL);
				actionForward = mapping.findForward(DefConstants.FWD_CALENDARIO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getCalendarioAdapterForCreate(userSession)";
				calendarioAdapterVO = DefServiceLocator.getGravamenService().getCalendarioAdapterForCreate(userSession);
				actionForward = mapping.findForward(DefConstants.FWD_CALENDARIO_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getCalendarioAdapterForView(userSession)";
				calendarioAdapterVO = DefServiceLocator.getGravamenService().getCalendarioAdapterForView(userSession, commonKey);
				calendarioAdapterVO.addMessage(BaseError.MSG_ACTIVAR, DefError.CALENDARIO_LABEL);
				actionForward = mapping.findForward(DefConstants.FWD_CALENDARIO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getCalendarioAdapterForView(userSession)";
				calendarioAdapterVO = DefServiceLocator.getGravamenService().getCalendarioAdapterForView(userSession, commonKey);
				calendarioAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, DefError.CALENDARIO_LABEL);
				actionForward = mapping.findForward(DefConstants.FWD_CALENDARIO_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (calendarioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + calendarioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CalendarioAdapter.NAME, calendarioAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			calendarioAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + CalendarioAdapter.NAME + ": "+ calendarioAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(CalendarioAdapter.NAME, calendarioAdapterVO);
			// Subo el apdater al userSession
			userSession.put(CalendarioAdapter.NAME, calendarioAdapterVO);
			 
			saveDemodaMessages(request, calendarioAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CalendarioAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_CALENDARIO, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CalendarioAdapter calendarioAdapterVO = (CalendarioAdapter) userSession.get(CalendarioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (calendarioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CalendarioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CalendarioAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(calendarioAdapterVO, request);
			
            // Tiene errores recuperables
			if (calendarioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + calendarioAdapterVO.infoString()); 
				saveDemodaErrors(request, calendarioAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CalendarioAdapter.NAME, calendarioAdapterVO);
			}
			
			// llamada al servicio
			CalendarioVO calendarioVO = DefServiceLocator.getGravamenService().createCalendario(userSession, calendarioAdapterVO.getCalendario());
			
            // Tiene errores recuperables
			if (calendarioVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + calendarioVO.infoString()); 
				saveDemodaErrors(request, calendarioVO);
				return forwardErrorRecoverable(mapping, request, userSession, CalendarioAdapter.NAME, calendarioAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (calendarioVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + calendarioVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CalendarioAdapter.NAME, calendarioAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CalendarioAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CalendarioAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_CALENDARIO, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CalendarioAdapter calendarioAdapterVO = (CalendarioAdapter) userSession.get(CalendarioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (calendarioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CalendarioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CalendarioAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(calendarioAdapterVO, request);
			
            // Tiene errores recuperables
			if (calendarioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + calendarioAdapterVO.infoString()); 
				saveDemodaErrors(request, calendarioAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CalendarioAdapter.NAME, calendarioAdapterVO);
			}
			
			// llamada al servicio
			CalendarioVO calendarioVO = DefServiceLocator.getGravamenService().updateCalendario(userSession, calendarioAdapterVO.getCalendario());
			
            // Tiene errores recuperables
			if (calendarioVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + calendarioAdapterVO.infoString()); 
				saveDemodaErrors(request, calendarioVO);
				return forwardErrorRecoverable(mapping, request, userSession, CalendarioAdapter.NAME, calendarioAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (calendarioVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + calendarioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CalendarioAdapter.NAME, calendarioAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CalendarioAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CalendarioAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_CALENDARIO, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CalendarioAdapter calendarioAdapterVO = (CalendarioAdapter) userSession.get(CalendarioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (calendarioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CalendarioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CalendarioAdapter.NAME); 
			}

			// llamada al servicio
			CalendarioVO calendarioVO = DefServiceLocator.getGravamenService().deleteCalendario
				(userSession, calendarioAdapterVO.getCalendario());
			
            // Tiene errores recuperables
			if (calendarioVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + calendarioAdapterVO.infoString());
				saveDemodaErrors(request, calendarioVO);				
				request.setAttribute(CalendarioAdapter.NAME, calendarioAdapterVO);
				return mapping.findForward(DefConstants.FWD_CALENDARIO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (calendarioVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + calendarioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CalendarioAdapter.NAME, calendarioAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CalendarioAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CalendarioAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_CALENDARIO, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CalendarioAdapter calendarioAdapterVO = (CalendarioAdapter) userSession.get(CalendarioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (calendarioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CalendarioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CalendarioAdapter.NAME); 
			}

			// llamada al servicio
			CalendarioVO calendarioVO = DefServiceLocator.getGravamenService().activarCalendario
				(userSession, calendarioAdapterVO.getCalendario());
			
            // Tiene errores recuperables
			if (calendarioVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + calendarioAdapterVO.infoString());
				saveDemodaErrors(request, calendarioVO);				
				request.setAttribute(CalendarioAdapter.NAME, calendarioAdapterVO);
				return mapping.findForward(DefConstants.FWD_CALENDARIO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (calendarioVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + calendarioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CalendarioAdapter.NAME, calendarioAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CalendarioAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CalendarioAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_CALENDARIO, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CalendarioAdapter calendarioAdapterVO = (CalendarioAdapter) userSession.get(CalendarioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (calendarioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CalendarioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CalendarioAdapter.NAME); 
			}

			// llamada al servicio
			CalendarioVO calendarioVO = DefServiceLocator.getGravamenService().desactivarCalendario
				(userSession, calendarioAdapterVO.getCalendario());
			
            // Tiene errores recuperables
			if (calendarioVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + calendarioAdapterVO.infoString());
				saveDemodaErrors(request, calendarioVO);				
				request.setAttribute(CalendarioAdapter.NAME, calendarioAdapterVO);
				return mapping.findForward(DefConstants.FWD_CALENDARIO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (calendarioVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + calendarioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CalendarioAdapter.NAME, calendarioAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CalendarioAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CalendarioAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, CalendarioAdapter.NAME);
		
	}
	
}
