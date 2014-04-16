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

import ar.gov.rosario.siat.bal.iface.model.EnvioOsirisAdapter;
import ar.gov.rosario.siat.bal.iface.model.EnvioOsirisVO;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarEnvioOsirisDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEnvioOsirisDAction.class);


	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_ENVIOOSIRIS, act);		
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			EnvioOsirisAdapter envioOsirisAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
					stringServicio = "getEnvioOsirisAdapterForView(userSession, commonKey)";
					envioOsirisAdapterVO = BalServiceLocator.getEnvioOsirisService().getEnvioOsirisAdapterForView(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_ENVIOOSIRIS_VIEW_ADAPTER);
				}
				if (navModel.getAct().equals(BalConstants.ACT_OBTENER_ENVIOOSIRIS)) {
					stringServicio = "getEnvioOsirisAdapterForMasivo(userSession)";
					envioOsirisAdapterVO = BalServiceLocator.getEnvioOsirisService().getEnvioOsirisAdapterForObtener(userSession);
					envioOsirisAdapterVO.addMessage(BalError.ENVIOOSIRIS_MSG_OBTENER_ENVIOOSIRIS);
					actionForward = mapping.findForward(BalConstants.FWD_ENVIOOSIRIS_VIEW_ADAPTER);
				}
				if (navModel.getAct().equals(BalConstants.ACT_PROCESAR_ENVIOOSIRIS)) {
					stringServicio = "getEnvioOsirisAdapterForMasivo(userSession)";
					envioOsirisAdapterVO = BalServiceLocator.getEnvioOsirisService().getEnvioOsirisAdapterForProcesar(userSession);
					envioOsirisAdapterVO.addMessage(BalError.ENVIOOSIRIS_MSG_PROCESAR_ENVIOOSIRIS);
					actionForward = mapping.findForward(BalConstants.FWD_ENVIOOSIRIS_VIEW_ADAPTER);
				}
				if (navModel.getAct().equals(BalConstants.ACT_GENERAR_TRANSACCION)) {
					stringServicio = "getEnvioOsirisAdapterForGenerarTransaccion(userSession)";
					envioOsirisAdapterVO = BalServiceLocator.getEnvioOsirisService().getEnvioOsirisAdapterForView(userSession, commonKey);
					envioOsirisAdapterVO.addMessage(BalError.ENVIOOSIRIS_MSG_GENERAR_TRANSACCION);
					actionForward = mapping.findForward(BalConstants.FWD_ENVIOOSIRIS_VIEW_ADAPTER);
				}
				if (navModel.getAct().equals(BalConstants.ACT_GENERAR_DECJUR)) {
					stringServicio = "getEnvioOsirisAdapterForGenerarDecJur(userSession)";
					envioOsirisAdapterVO = BalServiceLocator.getEnvioOsirisService().getEnvioOsirisAdapterForView(userSession, commonKey);
					envioOsirisAdapterVO.addMessage(BalError.ENVIOOSIRIS_MSG_GENERAR_DECJUR);
					actionForward = mapping.findForward(BalConstants.FWD_ENVIOOSIRIS_VIEW_ADAPTER);
				}				
				if (navModel.getAct().equals(BalConstants.ACT_CAMBIAR_ESTADO)) {
					stringServicio = "getEnvioOsirisAdapterForCambiarEstado(userSession)";
					envioOsirisAdapterVO = BalServiceLocator.getEnvioOsirisService().getEnvioOsirisAdapterForView(userSession, commonKey);
					envioOsirisAdapterVO.addMessage(BalError.ENVIOOSIRIS_MSG_CAMBIAR_ESTADO_ENVIO);
					actionForward = mapping.findForward(BalConstants.FWD_ENVIOOSIRIS_VIEW_ADAPTER);
				}
				
				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (envioOsirisAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + envioOsirisAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, EnvioOsirisAdapter.NAME, envioOsirisAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				envioOsirisAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + 
					EnvioOsirisAdapter.NAME + ": " + envioOsirisAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(EnvioOsirisAdapter.NAME, envioOsirisAdapterVO);
				// Subo el apdater al userSession
				userSession.put(EnvioOsirisAdapter.NAME, envioOsirisAdapterVO);
				
				saveDemodaMessages(request, envioOsirisAdapterVO);			
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, EnvioOsirisAdapter.NAME);
			}
		}
	
	public ActionForward obtenerEnvios(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				BalSecurityConstants.ABM_ENVIOOSIRIS, BalSecurityConstants.MTD_OBTENERENVIO);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				EnvioOsirisAdapter envioOsirisAdapterVO = (EnvioOsirisAdapter) userSession.get(EnvioOsirisAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (envioOsirisAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + EnvioOsirisAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, EnvioOsirisAdapter.NAME); 
				}
				
				// llamada al servicio
				envioOsirisAdapterVO = BalServiceLocator.getEnvioOsirisService().obtenerEnviosOsiris(userSession,envioOsirisAdapterVO);
				
	            // Tiene errores recuperables
				if (envioOsirisAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + envioOsirisAdapterVO.infoString()); 
					saveDemodaErrors(request, envioOsirisAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, EnvioOsirisAdapter.NAME, envioOsirisAdapterVO, BalConstants.FWD_ENVIOOSIRIS_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (envioOsirisAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + envioOsirisAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, EnvioOsirisAdapter.NAME, envioOsirisAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, EnvioOsirisAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, EnvioOsirisAdapter.NAME);
			}
	}
	
	public ActionForward procesarEnvios(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				BalSecurityConstants.ABM_ENVIOOSIRIS, BalSecurityConstants.MTD_PROCESARENVIO);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				EnvioOsirisAdapter envioOsirisAdapterVO = (EnvioOsirisAdapter) userSession.get(EnvioOsirisAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (envioOsirisAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + EnvioOsirisAdapter.NAME + " IS NULL. No se pudo procesar de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, EnvioOsirisAdapter.NAME); 
				}
				
				// llamada al servicio
				envioOsirisAdapterVO = BalServiceLocator.getEnvioOsirisService().procesarEnviosOsiris(userSession,envioOsirisAdapterVO);
				
	            // Tiene errores recuperables
				if (envioOsirisAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + envioOsirisAdapterVO.infoString()); 
					saveDemodaErrors(request, envioOsirisAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, EnvioOsirisAdapter.NAME, envioOsirisAdapterVO, BalConstants.FWD_ENVIOOSIRIS_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (envioOsirisAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + envioOsirisAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, EnvioOsirisAdapter.NAME, envioOsirisAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, EnvioOsirisAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, EnvioOsirisAdapter.NAME);
			}
	}
	
	public ActionForward verCierreBanco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception{		

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_CIERREBANCO);
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, EnvioOsirisAdapter.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, EnvioOsirisAdapter.NAME);
			
	}
	
	public ActionForward generarTransaccion(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				BalSecurityConstants.ABM_ENVIOOSIRIS, BalSecurityConstants.MTD_GENERARTRANSACCION);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				EnvioOsirisAdapter envioOsirisAdapterVO = (EnvioOsirisAdapter) userSession.get(EnvioOsirisAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (envioOsirisAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + EnvioOsirisAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, EnvioOsirisAdapter.NAME); 
				}
				
				// llamada al servicio
				EnvioOsirisVO envioOsirisVO = BalServiceLocator.getEnvioOsirisService().generarTransaccion(userSession, envioOsirisAdapterVO.getEnvioOsiris());
				
	            // Tiene errores recuperables
				if (envioOsirisVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + envioOsirisAdapterVO.infoString()); 
					saveDemodaErrors(request, envioOsirisVO);
					return forwardErrorRecoverable(mapping, request, userSession, EnvioOsirisAdapter.NAME, envioOsirisAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (envioOsirisVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + envioOsirisAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, EnvioOsirisAdapter.NAME, envioOsirisAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, EnvioOsirisAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, EnvioOsirisAdapter.NAME);
			}
	}

	public ActionForward generarDecJur(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				BalSecurityConstants.ABM_ENVIOOSIRIS, BalSecurityConstants.MTD_GENERARDECJUR);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				EnvioOsirisAdapter envioOsirisAdapterVO = (EnvioOsirisAdapter) userSession.get(EnvioOsirisAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (envioOsirisAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + EnvioOsirisAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, EnvioOsirisAdapter.NAME); 
				}
				
				// llamada al servicio
				EnvioOsirisVO envioOsirisVO = BalServiceLocator.getEnvioOsirisService().generarDecJur(userSession, envioOsirisAdapterVO.getEnvioOsiris());
				
	            // Tiene errores recuperables
				if (envioOsirisVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + envioOsirisAdapterVO.infoString()); 
					saveDemodaErrors(request, envioOsirisVO);
					return forwardErrorRecoverable(mapping, request, userSession, EnvioOsirisAdapter.NAME, envioOsirisAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (envioOsirisVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + envioOsirisAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, EnvioOsirisAdapter.NAME, envioOsirisAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, EnvioOsirisAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, EnvioOsirisAdapter.NAME);
			}
	}
	
	public ActionForward cambiarEstado(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping,BalSecurityConstants.ABM_ENVIOOSIRIS, BalSecurityConstants.MTD_CAMBIAR_ESTADO);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				EnvioOsirisAdapter envioOsirisAdapterVO = (EnvioOsirisAdapter) userSession.get(EnvioOsirisAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (envioOsirisAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + EnvioOsirisAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, EnvioOsirisAdapter.NAME); 
				}
				
				// llamada al servicio
				EnvioOsirisVO envioOsirisVO = BalServiceLocator.getEnvioOsirisService().cambiarEstado(userSession, envioOsirisAdapterVO.getEnvioOsiris());
				
	            // Tiene errores recuperables
				if (envioOsirisVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + envioOsirisAdapterVO.infoString()); 
					saveDemodaErrors(request, envioOsirisVO);
					return forwardErrorRecoverable(mapping, request, userSession, EnvioOsirisAdapter.NAME, envioOsirisAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (envioOsirisVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + envioOsirisAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, EnvioOsirisAdapter.NAME, envioOsirisAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, EnvioOsirisAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, EnvioOsirisAdapter.NAME);
			}
	}
	
	public ActionForward verConciliacion(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception{		

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_CONCILIACION);
	}
}
