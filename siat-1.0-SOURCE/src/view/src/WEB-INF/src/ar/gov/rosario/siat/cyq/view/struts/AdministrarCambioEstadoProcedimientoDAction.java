//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.cyq.iface.model.ProcedimientoAdapter;
import ar.gov.rosario.siat.cyq.iface.service.CyqServiceLocator;
import ar.gov.rosario.siat.cyq.iface.util.CyqSecurityConstants;
import ar.gov.rosario.siat.cyq.view.util.CyqConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarCambioEstadoProcedimientoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarCambioEstadoProcedimientoDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.ABM_PROCEDIMIENTO_CyQ, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ProcedimientoAdapter procedimientoAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(CyqConstants.METOD_CAMBIAR_ESTADO)) {
				stringServicio = "getProcedimientoAdapterForCambioEstado(userSession, commonKey)";
				procedimientoAdapterVO = CyqServiceLocator.getConcursoyQuiebraService().getProcedimientoAdapterForCambioEstado(userSession, commonKey);
				actionForward = mapping.findForward(CyqConstants.FWD_CAMBIOESTADO_PROCEDIMIENTO_ADAPTER);
			}
		
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (procedimientoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + procedimientoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcedimientoAdapter.CAMBIOESTADO_NAME, procedimientoAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			procedimientoAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ProcedimientoAdapter.CAMBIOESTADO_NAME + ": "+ procedimientoAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ProcedimientoAdapter.CAMBIOESTADO_NAME, procedimientoAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ProcedimientoAdapter.CAMBIOESTADO_NAME, procedimientoAdapterVO);
			 
			saveDemodaMessages(request, procedimientoAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcedimientoAdapter.CAMBIOESTADO_NAME);
		}
	}

	
	public ActionForward cambiarEstado(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
				
		UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.ABM_PROCEDIMIENTO_CyQ, CyqSecurityConstants.MTD_CAMBIAR_ESTADO); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ProcedimientoAdapter procedimientoAdapterVO = (ProcedimientoAdapter) userSession.get(ProcedimientoAdapter.CAMBIOESTADO_NAME);
			
			// Si es nulo no se puede continuar
			if (procedimientoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcedimientoAdapter.CAMBIOESTADO_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcedimientoAdapter.CAMBIOESTADO_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(procedimientoAdapterVO, request);
			
            // Tiene errores recuperables
			if (procedimientoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procedimientoAdapterVO.infoString()); 
				saveDemodaErrors(request, procedimientoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcedimientoAdapter.CAMBIOESTADO_NAME, procedimientoAdapterVO);
			}

			procedimientoAdapterVO = CyqServiceLocator.getConcursoyQuiebraService().cambiarEstadoProcedimiento(userSession, procedimientoAdapterVO);
			
            // Tiene errores recuperables
			if (procedimientoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procedimientoAdapterVO.infoString()); 
				saveDemodaErrors(request, procedimientoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcedimientoAdapter.CAMBIOESTADO_NAME, procedimientoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (procedimientoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + procedimientoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName,
						ProcedimientoAdapter.CAMBIOESTADO_NAME, procedimientoAdapterVO);
			}

			return forwardConfirmarOk(mapping, request, funcName, ProcedimientoAdapter.CAMBIOESTADO_NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcedimientoAdapter.CAMBIOESTADO_NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ProcedimientoAdapter.CAMBIOESTADO_NAME);
		
	}
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, ProcedimientoAdapter.CAMBIOESTADO_NAME);
		
	}
	

/*	public ActionForward agregarSolicitud(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.ABM_PROCEDIMIENTO, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ProcedimientoAdapter procedimientoAdapterVO = (ProcedimientoAdapter) userSession.get(ProcedimientoAdapter.CAMBIOESTADO_NAME);
			
			// Si es nulo no se puede continuar
			if (procedimientoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcedimientoAdapter.CAMBIOESTADO_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcedimientoAdapter.CAMBIOESTADO_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(procedimientoAdapterVO, request);
			
            // Tiene errores recuperables
			if (procedimientoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procedimientoAdapterVO.infoString()); 
				saveDemodaErrors(request, procedimientoAdapterVO);
				
				request.setAttribute(ProcedimientoAdapter.CAMBIOESTADO_NAME, procedimientoAdapterVO);
				return mapping.findForward(CyqConstants.FWD_AGREGARSOLICITUD_PROCEDIMIENTO_ADAPTER);
			}
			
			// llamada al servicio
			ProcedimientoVO cueExeVO = CyqServiceLocator.getConcursoyQuiebraService().agregarSolicitudProcedimiento(userSession, procedimientoAdapterVO.getProcedimiento());
			
            // Tiene errores recuperables
			if (cueExeVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procedimientoAdapterVO.infoString()); 
				saveDemodaErrors(request, cueExeVO);
				
				request.setAttribute(ProcedimientoAdapter.CAMBIOESTADO_NAME, procedimientoAdapterVO);
				return mapping.findForward(CyqConstants.FWD_AGREGARSOLICITUD_PROCEDIMIENTO_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (cueExeVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + procedimientoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcedimientoAdapter.CAMBIOESTADO_NAME, procedimientoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ProcedimientoAdapter.CAMBIOESTADO_NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcedimientoAdapter.CAMBIOESTADO_NAME);
		}
	}	
	
	
	public ActionForward buscarSolicitante(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		// Bajo el adapter del userSession
		ProcedimientoAdapter procedimientoAdapterVO = (ProcedimientoAdapter) userSession.get(ProcedimientoAdapter.CAMBIOESTADO_NAME);
		
		// Si es nulo no se puede continuar
		if (procedimientoAdapterVO == null) {
			log.error("error en: "  + funcName + ": " + ProcedimientoAdapter.CAMBIOESTADO_NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, ProcedimientoAdapter.CAMBIOESTADO_NAME); 
		}

		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(procedimientoAdapterVO, request);
				
        // Tiene errores recuperables
		if (procedimientoAdapterVO.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + procedimientoAdapterVO.infoString()); 
			saveDemodaErrors(request, procedimientoAdapterVO);
			return forwardErrorRecoverable(mapping, request, userSession, ProcedimientoAdapter.CAMBIOESTADO_NAME, procedimientoAdapterVO);
		}
		
		return forwardSeleccionar(mapping, request, 
			CyqConstants.METOD_PROCEDIMIENTO_PARAM_SOLICITANTE, PadConstants.ACTION_BUSCAR_PERSONA , false);

	}

	public ActionForward paramSolicitante(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		NavModel navModel = userSession.getNavModel();
		
		try {
			
			//bajo el adapter del usserSession
			ProcedimientoAdapter procedimientoAdapterVO =  (ProcedimientoAdapter) userSession.get(ProcedimientoAdapter.CAMBIOESTADO_NAME);
			
			// Si es nulo no se puede continuar
			if (procedimientoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcedimientoAdapter.CAMBIOESTADO_NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcedimientoAdapter.CAMBIOESTADO_NAME); 
			}

			// recupero el id seleccionado por el usuario
			String selectedId = navModel.getSelectedId();
			
			// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
			if (StringUtil.isNullOrEmpty(selectedId)) {
				// Envio el VO al request
				request.setAttribute(ProcedimientoAdapter.CAMBIOESTADO_NAME, procedimientoAdapterVO);
				return mapping.findForward(CyqConstants.FWD_CAMBIOESTADO_PROCEDIMIENTO_ADAPTER);
			}

			// Seteo el id atributo seleccionado
			procedimientoAdapterVO.getProcedimiento().getSolicitante().setId(new Long(selectedId));
			
			// llamo al param del servicio
			procedimientoAdapterVO = CyqServiceLocator.getConcursoyQuiebraService().getProcedimientoAdapterParamSolicitante(userSession, procedimientoAdapterVO);

            // Tiene errores recuperables
			if (procedimientoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procedimientoAdapterVO.infoString()); 
				saveDemodaErrors(request, procedimientoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
					ProcedimientoAdapter.CAMBIOESTADO_NAME, procedimientoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (procedimientoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + procedimientoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
					ProcedimientoAdapter.CAMBIOESTADO_NAME, procedimientoAdapterVO);
			}

			// grabo los mensajes si hubiere
			saveDemodaMessages(request, procedimientoAdapterVO);

			// Envio el VO al request
			request.setAttribute(ProcedimientoAdapter.CAMBIOESTADO_NAME, procedimientoAdapterVO);
			
			return mapping.findForward(CyqConstants.FWD_CAMBIOESTADO_PROCEDIMIENTO_ADAPTER);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcedimientoAdapter.CAMBIOESTADO_NAME);
		}
	}
*/
	


}
