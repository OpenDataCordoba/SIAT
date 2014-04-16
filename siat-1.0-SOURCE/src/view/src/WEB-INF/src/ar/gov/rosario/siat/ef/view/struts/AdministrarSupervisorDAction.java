//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.view.struts;

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
import ar.gov.rosario.siat.ef.iface.model.SupervisorAdapter;
import ar.gov.rosario.siat.ef.iface.model.SupervisorVO;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfError;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarSupervisorDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarSupervisorDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_SUPERVISOR, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		SupervisorAdapter supervisorAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getSupervisorAdapterForView(userSession, commonKey)";
				supervisorAdapterVO = EfServiceLocator.getDefinicionService().getSupervisorAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_SUPERVISOR_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getSupervisorAdapterForUpdate(userSession, commonKey)";
				supervisorAdapterVO = EfServiceLocator.getDefinicionService().getSupervisorAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_SUPERVISOR_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getSupervisorAdapterForView(userSession, commonKey)";
				supervisorAdapterVO = EfServiceLocator.getDefinicionService().getSupervisorAdapterForView(userSession, commonKey);				
				supervisorAdapterVO.addMessage(BaseError.MSG_ELIMINAR, EfError.SUPERVISOR_LABEL);
				actionForward = mapping.findForward(EfConstants.FWD_SUPERVISOR_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getSupervisorAdapterForCreate(userSession)";
				supervisorAdapterVO = EfServiceLocator.getDefinicionService().getSupervisorAdapterForCreate(userSession);
				actionForward = mapping.findForward(EfConstants.FWD_SUPERVISOR_EDIT_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (supervisorAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + supervisorAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SupervisorAdapter.NAME, supervisorAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			supervisorAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + SupervisorAdapter.NAME + ": "+ supervisorAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(SupervisorAdapter.NAME, supervisorAdapterVO);
			// Subo el apdater al userSession
			userSession.put(SupervisorAdapter.NAME, supervisorAdapterVO);
			 
			saveDemodaMessages(request, supervisorAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SupervisorAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_SUPERVISOR, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			SupervisorAdapter supervisorAdapterVO = (SupervisorAdapter) userSession.get(SupervisorAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (supervisorAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + SupervisorAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SupervisorAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(supervisorAdapterVO, request);
			
            // Tiene errores recuperables
			if (supervisorAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + supervisorAdapterVO.infoString()); 
				saveDemodaErrors(request, supervisorAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, SupervisorAdapter.NAME, supervisorAdapterVO);
			}
			
			// llamada al servicio
			SupervisorVO supervisorVO = EfServiceLocator.getDefinicionService().createSupervisor(userSession, supervisorAdapterVO.getSupervisor());
			
            // Tiene errores recuperables
			if (supervisorVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + supervisorVO.infoString()); 
				saveDemodaErrors(request, supervisorVO);
				return forwardErrorRecoverable(mapping, request, userSession, SupervisorAdapter.NAME, supervisorAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (supervisorVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + supervisorVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SupervisorAdapter.NAME, supervisorAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, SupervisorAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SupervisorAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_SUPERVISOR, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			SupervisorAdapter supervisorAdapterVO = (SupervisorAdapter) userSession.get(SupervisorAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (supervisorAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + SupervisorAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SupervisorAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(supervisorAdapterVO, request);
			
            // Tiene errores recuperables
			if (supervisorAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + supervisorAdapterVO.infoString()); 
				saveDemodaErrors(request, supervisorAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, SupervisorAdapter.NAME, supervisorAdapterVO);
			}
			
			// llamada al servicio
			SupervisorVO supervisorVO = EfServiceLocator.getDefinicionService().updateSupervisor(userSession, supervisorAdapterVO.getSupervisor());
			
            // Tiene errores recuperables
			if (supervisorVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + supervisorAdapterVO.infoString()); 
				saveDemodaErrors(request, supervisorVO);
				return forwardErrorRecoverable(mapping, request, userSession, SupervisorAdapter.NAME, supervisorAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (supervisorVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + supervisorAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SupervisorAdapter.NAME, supervisorAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, SupervisorAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SupervisorAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_SUPERVISOR, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			SupervisorAdapter supervisorAdapterVO = (SupervisorAdapter) userSession.get(SupervisorAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (supervisorAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + SupervisorAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SupervisorAdapter.NAME); 
			}

			// llamada al servicio
			SupervisorVO supervisorVO = EfServiceLocator.getDefinicionService().deleteSupervisor
				(userSession, supervisorAdapterVO.getSupervisor());
			
            // Tiene errores recuperables
			if (supervisorVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + supervisorAdapterVO.infoString());
				saveDemodaErrors(request, supervisorVO);				
				request.setAttribute(SupervisorAdapter.NAME, supervisorAdapterVO);
				return mapping.findForward(EfConstants.FWD_SUPERVISOR_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (supervisorVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + supervisorAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SupervisorAdapter.NAME, supervisorAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, SupervisorAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SupervisorAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, SupervisorAdapter.NAME);
		
	}

	public ActionForward buscarPersona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		return forwardSeleccionar(mapping, request, EfConstants.METOD_PARAM_PERSONA, PadConstants.ACTION_BUSCAR_PERSONA, false);		
	}
	
	public ActionForward paramPersona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			NavModel navModel = userSession.getNavModel();
			
			try {
				
				//bajo el adapter del usserSession
				SupervisorAdapter supervisorAdapterVO =  (SupervisorAdapter) userSession.get(SupervisorAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (supervisorAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + SupervisorAdapter.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, SupervisorAdapter.NAME); 
				}

				// recupero el id seleccionado por el usuario
				String selectedId = navModel.getSelectedId();
				
				// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
				if (StringUtil.isNullOrEmpty(selectedId)) {
					// Envio el VO al request
					request.setAttribute(SupervisorAdapter.NAME, supervisorAdapterVO);
					return mapping.findForward(EfConstants.FWD_SUPERVISOR_EDIT_ADAPTER); 
				}
				
				// Se carga el idPersona seleccionado, en el adapter
				supervisorAdapterVO.getSupervisor().setIdPersona(new Long(selectedId));
				
				// llamo al param del servicio
				supervisorAdapterVO = EfServiceLocator.getDefinicionService().getSupervisorAdapterParamPersona(userSession, supervisorAdapterVO);

	            // Tiene errores recuperables
				if (supervisorAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + supervisorAdapterVO.infoString()); 
					saveDemodaErrors(request, supervisorAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
							SupervisorAdapter.NAME, supervisorAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (supervisorAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + supervisorAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
							SupervisorAdapter.NAME, supervisorAdapterVO);
				}

				// grabo los mensajes si hubiere
				saveDemodaMessages(request, supervisorAdapterVO);

				// Envio el VO al request
				request.setAttribute(SupervisorAdapter.NAME, supervisorAdapterVO);
				// Subo el apdater al userSession
				userSession.put(SupervisorAdapter.NAME, supervisorAdapterVO);

				return mapping.findForward(EfConstants.FWD_SUPERVISOR_EDIT_ADAPTER);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, SupervisorAdapter.NAME);
			}
	}
		
}
