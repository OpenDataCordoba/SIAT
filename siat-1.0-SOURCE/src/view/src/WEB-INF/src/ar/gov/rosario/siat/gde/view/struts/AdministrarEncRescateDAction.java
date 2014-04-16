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

import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.gde.iface.model.RescateAdapter;
import ar.gov.rosario.siat.gde.iface.model.RescateVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarEncRescateDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEncRescateDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
									 HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_RESCATE, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		RescateAdapter rescateAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getRescateAdapterForCreate(userSession)";
				rescateAdapterVO = GdeServiceLocator.getGdePlanPagoService().getRescateAdapterForCreate(userSession);
				actionForward = mapping.findForward(GdeConstants.FWD_RESCATE_EDIT_ADAPTER); 				
			}
			
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getRescateAdapterForUpdate(userSession)";
				rescateAdapterVO = GdeServiceLocator.getGdePlanPagoService().getRescateAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_RESCATE_EDIT_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (rescateAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + rescateAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, RescateAdapter.ENC_NAME, rescateAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			rescateAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + RescateAdapter.ENC_NAME + ": "+ rescateAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(RescateAdapter.ENC_NAME, rescateAdapterVO);
			// Subo el apdater al userSession
			userSession.put(RescateAdapter.ENC_NAME, rescateAdapterVO);
			 
			saveDemodaMessages(request, rescateAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, RescateAdapter.ENC_NAME);
		}
	}
	
	public ActionForward paramRecurso(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
			
		try {
			//bajo el adapter del usserSession
			RescateAdapter rescateAdapterVO =  (RescateAdapter) userSession.get(RescateAdapter.ENC_NAME);
				
			// Si es nulo no se puede continuar
			if (rescateAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + RescateAdapter.ENC_NAME + " " +
						  "IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, RescateAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(rescateAdapterVO, request);
				
			// Tiene errores recuperables
			if (rescateAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + rescateAdapterVO.infoString()); 
				saveDemodaErrors(request, rescateAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, RescateAdapter.ENC_NAME, rescateAdapterVO);
			}

			rescateAdapterVO = GdeServiceLocator.getGdePlanPagoService().getRescateAdapterParamRecurso(userSession, rescateAdapterVO);
				
			// Tiene errores recuperables
			if (rescateAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + rescateAdapterVO.infoString()); 
				saveDemodaErrors(request, rescateAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
											   RescateAdapter.ENC_NAME, rescateAdapterVO);
			}
				
			// Tiene errores no recuperables
			if (rescateAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + rescateAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
												  RescateAdapter.ENC_NAME, rescateAdapterVO);
			}
				
			// grabo los mensajes si hubiere
			saveDemodaMessages(request, rescateAdapterVO);
				
			// Envio el VO al request
			request.setAttribute(RescateAdapter.ENC_NAME, rescateAdapterVO);

			return mapping.findForward(GdeConstants.FWD_RESCATE_EDIT_ADAPTER);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, RescateAdapter.ENC_NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
								 HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_RESCATE, 
				BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			RescateAdapter rescateAdapterVO = (RescateAdapter) userSession.get(RescateAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (rescateAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + RescateAdapter.ENC_NAME + 
						" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, RescateAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(rescateAdapterVO, request);
			
            // Tiene errores recuperables
			if (rescateAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + rescateAdapterVO.infoString()); 
				saveDemodaErrors(request, rescateAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, RescateAdapter.ENC_NAME, rescateAdapterVO);
			}
			
			// llamada al servicio
			RescateVO rescateVO = GdeServiceLocator.getGdePlanPagoService().createRescate(userSession, rescateAdapterVO.getRescate());
			
            // Tiene errores recuperables
			if (rescateVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + rescateVO.infoString()); 
				saveDemodaErrors(request, rescateVO);
				return forwardErrorRecoverable(mapping, request, userSession, RescateAdapter.ENC_NAME, rescateAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (rescateVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + rescateVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, RescateAdapter.ENC_NAME, rescateAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, RescateAdapter.ENC_NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, RescateAdapter.ENC_NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
								   HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_RESCATE, 
				BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
			
		try {
			// Bajo el adapter del userSession
			RescateAdapter rescateAdapterVO = (RescateAdapter) userSession.get(RescateAdapter.ENC_NAME);
				
			// Si es nulo no se puede continuar
			if (rescateAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + RescateAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, RescateAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(rescateAdapterVO, request);
				
			// Tiene errores recuperables
			if (rescateAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + rescateAdapterVO.infoString()); 
				saveDemodaErrors(request, rescateAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, RescateAdapter.ENC_NAME, rescateAdapterVO);
			}
				
			// llamada al servicio
			RescateVO rescateVO = GdeServiceLocator.getGdePlanPagoService().updateRescate(userSession, rescateAdapterVO.getRescate());
				
			// Tiene errores recuperables
			if (rescateVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + rescateVO.infoString()); 
				saveDemodaErrors(request, rescateVO);
				return forwardErrorRecoverable(mapping, request, userSession, RescateAdapter.ENC_NAME, rescateAdapterVO);
			}
				
			// Tiene errores no recuperables
			if (rescateVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + rescateVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, RescateAdapter.ENC_NAME, rescateAdapterVO);
			}
				
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, RescateAdapter.ENC_NAME);
				
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, RescateAdapter.ENC_NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
								  HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_RESCATE, 
				BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
			
		try {
			// Bajo el adapter del userSession
			RescateAdapter rescateAdapterVO = (RescateAdapter) userSession.get(RescateAdapter.ENC_NAME);
				
			// Si es nulo no se puede continuar
			if (rescateAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + RescateAdapter.ENC_NAME + 
						" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, RescateAdapter.ENC_NAME); 
			}

			// llamada al servicio
			RescateVO rescateVO = GdeServiceLocator.getGdePlanPagoService().
				deleteRescate(userSession, rescateAdapterVO.getRescate());
				
			// Tiene errores recuperables
			if (rescateVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + rescateVO.infoString()); 
				saveDemodaErrors(request, rescateVO);
				request.setAttribute(RescateAdapter.ENC_NAME, rescateAdapterVO);
				// no usamos forwardErrorRecoverable porque no forwardeamos al inputForward del struts-config.
				return mapping.findForward(GdeConstants.FWD_RESCATE_VIEW_ADAPTER);
			}
				
			// Tiene errores no recuperables
			if (rescateVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + rescateVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, RescateAdapter.ENC_NAME, rescateAdapterVO);
			}
				
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, RescateAdapter.ENC_NAME);
				
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, RescateAdapter.ENC_NAME);
		}
	}

	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
								HttpServletRequest request, HttpServletResponse response)throws Exception {
		
		return baseVolver(mapping, form, request, response, RescateAdapter.ENC_NAME);
	}
	

		
}
