//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.view.struts;

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
import ar.gov.rosario.siat.pad.iface.model.RepartidorAdapter;
import ar.gov.rosario.siat.pad.iface.model.RepartidorVO;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class AdministrarEncRepartidorDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEncRepartidorDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_REPARTIDOR_ENC, act);		

			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			RepartidorAdapter repartidorAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());

				if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
					stringServicio = "getRepartidorAdapterForUpdate(userSession, commonKey)";
					repartidorAdapterVO = PadServiceLocator.getDistribucionService().getRepartidorAdapterForUpdate(userSession, commonKey);
					actionForward = mapping.findForward(PadConstants.FWD_REPARTIDOR_ENC_EDIT_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
					stringServicio = "getRepartidorAdapterForCreate(userSession)";
					repartidorAdapterVO = PadServiceLocator.getDistribucionService().getRepartidorAdapterForCreate(userSession);
					actionForward = mapping.findForward(PadConstants.FWD_REPARTIDOR_ENC_EDIT_ADAPTER);
				}
				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (repartidorAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + repartidorAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, RepartidorAdapter.ENC_NAME, repartidorAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				repartidorAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + RepartidorAdapter.ENC_NAME + ": "+ repartidorAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(RepartidorAdapter.ENC_NAME, repartidorAdapterVO);
				// Subo el apdater al userSession
				userSession.put(RepartidorAdapter.ENC_NAME, repartidorAdapterVO);
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RepartidorAdapter.ENC_NAME);
			}
		}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping, 
				PadSecurityConstants.ABM_REPARTIDOR_ENC, BaseSecurityConstants.AGREGAR);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				RepartidorAdapter repartidorAdapterVO = (RepartidorAdapter) userSession.get(RepartidorAdapter.ENC_NAME);
				
				// Si es nulo no se puede continuar
				if (repartidorAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RepartidorAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RepartidorAdapter.ENC_NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(repartidorAdapterVO, request);
				
	            // Tiene errores recuperables
				if (repartidorAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + repartidorAdapterVO.infoString()); 
					saveDemodaErrors(request, repartidorAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, RepartidorAdapter.ENC_NAME, repartidorAdapterVO);
				}
				
				// llamada al servicio
				RepartidorVO repartidorVO = PadServiceLocator.getDistribucionService().createRepartidor(userSession, repartidorAdapterVO.getRepartidor());
				
	            // Tiene errores recuperables
				if (repartidorVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + repartidorVO.infoString()); 
					saveDemodaErrors(request, repartidorVO);
					return forwardErrorRecoverable(mapping, request, userSession, RepartidorAdapter.ENC_NAME, repartidorAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (repartidorVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + repartidorVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, RepartidorAdapter.ENC_NAME, repartidorAdapterVO);
				}

							
				return forwardConfirmarOk(mapping, request, funcName, RepartidorAdapter.ENC_NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RepartidorAdapter.ENC_NAME);
			}
	}
	
	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				PadSecurityConstants.ABM_REPARTIDOR_ENC, BaseSecurityConstants.MODIFICAR);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				RepartidorAdapter repartidorAdapterVO = (RepartidorAdapter) userSession.get(RepartidorAdapter.ENC_NAME);
				
				// Si es nulo no se puede continuar
				if (repartidorAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RepartidorAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RepartidorAdapter.ENC_NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(repartidorAdapterVO, request);
				
	            // Tiene errores recuperables
				if (repartidorAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + repartidorAdapterVO.infoString()); 
					saveDemodaErrors(request, repartidorAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, RepartidorAdapter.ENC_NAME, repartidorAdapterVO);
				}
				
				// llamada al servicio
				RepartidorVO repartidorVO = PadServiceLocator.getDistribucionService().updateRepartidor(userSession, repartidorAdapterVO.getRepartidor());
				
	            // Tiene errores recuperables
				if (repartidorVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + repartidorAdapterVO.infoString()); 
					saveDemodaErrors(request, repartidorVO);
					return forwardErrorRecoverable(mapping, request, userSession, RepartidorAdapter.ENC_NAME, repartidorAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (repartidorVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + repartidorAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, RepartidorAdapter.ENC_NAME, repartidorAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, RepartidorAdapter.ENC_NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RepartidorAdapter.ENC_NAME);
			}
	}

	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, RepartidorAdapter.ENC_NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, RepartidorAdapter.ENC_NAME);
	}
	
	public ActionForward paramRecurso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				RepartidorAdapter repartidorAdapterVO = (RepartidorAdapter) userSession.get(RepartidorAdapter.ENC_NAME);
		
				// Si es nulo no se puede continuar
				if (repartidorAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RepartidorAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RepartidorAdapter.ENC_NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(repartidorAdapterVO, request);
				
	            // Tiene errores recuperables
				if (repartidorAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + repartidorAdapterVO.infoString()); 
					saveDemodaErrors(request, repartidorAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, RepartidorAdapter.ENC_NAME, repartidorAdapterVO);
				}
				
				// llamada al servicio
				repartidorAdapterVO = PadServiceLocator.getDistribucionService().getRepartidorAdapterParamRecurso(userSession, repartidorAdapterVO);
				
	            // Tiene errores recuperables
				if (repartidorAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + repartidorAdapterVO.infoString()); 
					saveDemodaErrors(request, repartidorAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, RepartidorAdapter.ENC_NAME, repartidorAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (repartidorAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + repartidorAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, RepartidorAdapter.ENC_NAME, repartidorAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(RepartidorAdapter.ENC_NAME, repartidorAdapterVO);
				// Subo el adapter al userSession
				userSession.put(RepartidorAdapter.ENC_NAME, repartidorAdapterVO);
				
				return mapping.findForward(PadConstants.FWD_REPARTIDOR_ENC_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RepartidorAdapter.ENC_NAME);
			}
	}
	
	
	public ActionForward buscarPersona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();		
		
		UserSession userSession = getCurrentUserSession(request, mapping);			
		if (userSession == null) return forwardErrorSession(request);
				
		//bajo el adapter del usserSession
		//RepartidorAdapter repartidorAdapterVO =  (RepartidorAdapter) userSession.get(RepartidorAdapter.NAME);
		try {
			// Bajo el adapter del userSession
			RepartidorAdapter repartidorAdapterVO = (RepartidorAdapter) userSession.get(RepartidorAdapter.ENC_NAME);
	
			// Si es nulo no se puede continuar
			if (repartidorAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + RepartidorAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, RepartidorAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(repartidorAdapterVO, request);
			
            // Tiene errores recuperables
			if (repartidorAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + repartidorAdapterVO.infoString()); 
				saveDemodaErrors(request, repartidorAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, RepartidorAdapter.ENC_NAME, repartidorAdapterVO);
			}
			
			return forwardSeleccionar(mapping, request, 
					PadConstants.METOD_REPARTIDOR_PARAM_PERSONA, PadConstants.ACTION_BUSCAR_PERSONA, false);
			
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, RepartidorAdapter.ENC_NAME);
		}
	}

	public ActionForward paramPersona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		NavModel navModel = userSession.getNavModel();
		
		try {
			
			// Bajo el adapter del userSession
			RepartidorAdapter repartidorAdapterVO = (RepartidorAdapter) userSession.get(RepartidorAdapter.ENC_NAME);
	
			// Si es nulo no se puede continuar
			if (repartidorAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + RepartidorAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, RepartidorAdapter.ENC_NAME); 
			}

			// recupero el id seleccionado por el usuario
			String selectedId = navModel.getSelectedId();
			
			// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
			if (StringUtil.isNullOrEmpty(selectedId)) {
				// Envio el VO al request
				request.setAttribute(RepartidorAdapter.ENC_NAME, repartidorAdapterVO);
				return mapping.findForward(PadConstants.FWD_REPARTIDOR_ENC_EDIT_ADAPTER); 
			}

			// llamo al param del servicio
			repartidorAdapterVO = PadServiceLocator.getDistribucionService().paramPersonaRepartidor
				(userSession, repartidorAdapterVO, new Long(selectedId));

            // Tiene errores recuperables
			if (repartidorAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + repartidorAdapterVO.infoString()); 
				saveDemodaErrors(request, repartidorAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
					RepartidorAdapter.NAME, repartidorAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (repartidorAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + repartidorAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
					RepartidorAdapter.NAME, repartidorAdapterVO);
			}
			
			// grabo los mensajes si hubiere
			saveDemodaMessages(request, repartidorAdapterVO);
			
			// Envio el VO al request
			request.setAttribute(RepartidorAdapter.ENC_NAME, repartidorAdapterVO);

			return mapping.findForward(PadConstants.FWD_REPARTIDOR_ENC_EDIT_ADAPTER);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, RepartidorAdapter.ENC_NAME);
		}
	}

	
	public ActionForward paramTipoRepartidor(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				RepartidorAdapter repartidorAdapterVO = (RepartidorAdapter) userSession.get(RepartidorAdapter.ENC_NAME);
		
				// Si es nulo no se puede continuar
				if (repartidorAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RepartidorAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RepartidorAdapter.ENC_NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(repartidorAdapterVO, request);
				
	            // Tiene errores recuperables
				if (repartidorAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + repartidorAdapterVO.infoString()); 
					saveDemodaErrors(request, repartidorAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, RepartidorAdapter.ENC_NAME, repartidorAdapterVO);
				}
				
				// llamada al servicio
				repartidorAdapterVO = PadServiceLocator.getDistribucionService().getRepartidorAdapterParamTipoRepartidor(userSession, repartidorAdapterVO);
				
	            // Tiene errores recuperables
				if (repartidorAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + repartidorAdapterVO.infoString()); 
					saveDemodaErrors(request, repartidorAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, RepartidorAdapter.ENC_NAME, repartidorAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (repartidorAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + repartidorAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, RepartidorAdapter.ENC_NAME, repartidorAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(RepartidorAdapter.ENC_NAME, repartidorAdapterVO);
				// Subo el adapter al userSession
				userSession.put(RepartidorAdapter.ENC_NAME, repartidorAdapterVO);
				
				return mapping.findForward(PadConstants.FWD_REPARTIDOR_ENC_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RepartidorAdapter.ENC_NAME);
			}
	}

}
