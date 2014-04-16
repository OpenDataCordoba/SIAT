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

import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.exe.iface.model.CueExeAdapter;
import ar.gov.rosario.siat.exe.iface.model.CueExeVO;
import ar.gov.rosario.siat.exe.iface.service.ExeServiceLocator;
import ar.gov.rosario.siat.exe.iface.util.ExeSecurityConstants;
import ar.gov.rosario.siat.exe.view.util.ExeConstants;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarCambioEstadoCueExeDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarCueExeDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_CUEEXE, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		CueExeAdapter cueExeAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals("cambiarEstado")) {
				stringServicio = "getCueExeAdapterForCambioEstado(userSession, commonKey)";
				cueExeAdapterVO = ExeServiceLocator.getExencionService().getCueExeAdapterForCambioEstado(userSession, commonKey);
				actionForward = mapping.findForward(ExeConstants.FWD_CAMBIOESTADO_CUEEXE_ADAPTER);
			}

			if (act.equals("modificarHisEstCueExe")) {
				stringServicio = "getCueExeAdapterForModificarHisEstCueExe(userSession, commonKey)";
				cueExeAdapterVO = ExeServiceLocator.getExencionService().getCueExeAdapterForModificarHisEstCueExe(userSession, commonKey);
				actionForward = mapping.findForward(ExeConstants.FWD_CAMBIOESTADO_CUEEXE_ADAPTER);
			}
			
			if (act.equals("agregarSolicitud")) {
				stringServicio = "getCueExeAdapterForAgregarSolicitud(userSession, commonKey)";
				cueExeAdapterVO = ExeServiceLocator.getExencionService().getCueExeAdapterForAgregarSolicitud(userSession, commonKey);
				actionForward = mapping.findForward(ExeConstants.FWD_AGREGARSOLICITUD_CUEEXE_ADAPTER);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (cueExeAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + cueExeAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CueExeAdapter.CAMBIOESTADO_NAME, cueExeAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			cueExeAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + CueExeAdapter.CAMBIOESTADO_NAME + ": "+ cueExeAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(CueExeAdapter.CAMBIOESTADO_NAME, cueExeAdapterVO);
			// Subo el apdater al userSession
			userSession.put(CueExeAdapter.CAMBIOESTADO_NAME, cueExeAdapterVO);
			 
			saveDemodaMessages(request, cueExeAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CueExeAdapter.CAMBIOESTADO_NAME);
		}
	}

	
	public ActionForward cambiarEstado(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		ActionForward forward = null;
		
		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_CUEEXE, ExeSecurityConstants.MTD_CAMBIARESTADO); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CueExeAdapter cueExeAdapterVO = (CueExeAdapter) userSession.get(CueExeAdapter.CAMBIOESTADO_NAME);
			
			// Si es nulo no se puede continuar
			if (cueExeAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CueExeAdapter.CAMBIOESTADO_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CueExeAdapter.CAMBIOESTADO_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(cueExeAdapterVO, request);
			
            // Tiene errores recuperables
			if (cueExeAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExeAdapterVO.infoString()); 
				saveDemodaErrors(request, cueExeAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CueExeAdapter.CAMBIOESTADO_NAME, cueExeAdapterVO);
			}
			
			// llamadas al servicio
			// primero busco la dueda asociada
			cueExeAdapterVO = ExeServiceLocator.getExencionService().buscarDeudaAsocida(userSession, cueExeAdapterVO);

            // Tiene errores recuperables
			if (cueExeAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExeAdapterVO.infoString()); 
				saveDemodaErrors(request, cueExeAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CueExeAdapter.CAMBIOESTADO_NAME, cueExeAdapterVO);
			}

			// si no tiene deuda a seleccionar, llamo al servicio para agregar
			if (!cueExeAdapterVO.hasDeudaASeleccionar()) {
				cueExeAdapterVO = ExeServiceLocator.getExencionService().cambiarEstadoCueExe(userSession, cueExeAdapterVO);
				
	            // Tiene errores recuperables
				if (cueExeAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + cueExeAdapterVO.infoString()); 
					saveDemodaErrors(request, cueExeAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, CueExeAdapter.CAMBIOESTADO_NAME, cueExeAdapterVO);
				}
				
				forward = forwardConfirmarOk(mapping, request, funcName, CueExeAdapter.CAMBIOESTADO_NAME);
			}
			
			if (cueExeAdapterVO.hasDeudaASeleccionar()) {
				forward = mapping.findForward(ExeConstants.ACTION_SELECCIONAR_DEUDA);
			}

			// Tiene errores no recuperables
			if (cueExeAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cueExeAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName,
						CueExeAdapter.CAMBIOESTADO_NAME, cueExeAdapterVO);
			}

			return forward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CueExeAdapter.CAMBIOESTADO_NAME);
		}
	}
	
	
	public ActionForward agregarSolicitud(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_CUEEXE, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CueExeAdapter cueExeAdapterVO = (CueExeAdapter) userSession.get(CueExeAdapter.CAMBIOESTADO_NAME);
			
			// Si es nulo no se puede continuar
			if (cueExeAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CueExeAdapter.CAMBIOESTADO_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CueExeAdapter.CAMBIOESTADO_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(cueExeAdapterVO, request);
			
            // Tiene errores recuperables
			if (cueExeAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExeAdapterVO.infoString()); 
				saveDemodaErrors(request, cueExeAdapterVO);
				
				request.setAttribute(CueExeAdapter.CAMBIOESTADO_NAME, cueExeAdapterVO);
				return mapping.findForward(ExeConstants.FWD_AGREGARSOLICITUD_CUEEXE_ADAPTER);
			}
			
			// llamada al servicio
			CueExeVO cueExeVO = ExeServiceLocator.getExencionService().agregarSolicitudCueExe(userSession, cueExeAdapterVO.getCueExe());
			
            // Tiene errores recuperables
			if (cueExeVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExeAdapterVO.infoString()); 
				saveDemodaErrors(request, cueExeVO);
				
				request.setAttribute(CueExeAdapter.CAMBIOESTADO_NAME, cueExeAdapterVO);
				return mapping.findForward(ExeConstants.FWD_AGREGARSOLICITUD_CUEEXE_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (cueExeVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cueExeAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CueExeAdapter.CAMBIOESTADO_NAME, cueExeAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CueExeAdapter.CAMBIOESTADO_NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CueExeAdapter.CAMBIOESTADO_NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, CueExeAdapter.CAMBIOESTADO_NAME);
		
	}
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, CueExeAdapter.CAMBIOESTADO_NAME);
		
	}
	
	public ActionForward buscarSolicitante(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		// Bajo el adapter del userSession
		CueExeAdapter cueExeAdapterVO = (CueExeAdapter) userSession.get(CueExeAdapter.CAMBIOESTADO_NAME);
		
		// Si es nulo no se puede continuar
		if (cueExeAdapterVO == null) {
			log.error("error en: "  + funcName + ": " + CueExeAdapter.CAMBIOESTADO_NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, CueExeAdapter.CAMBIOESTADO_NAME); 
		}

		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(cueExeAdapterVO, request);
				
        // Tiene errores recuperables
		if (cueExeAdapterVO.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + cueExeAdapterVO.infoString()); 
			saveDemodaErrors(request, cueExeAdapterVO);
			return forwardErrorRecoverable(mapping, request, userSession, CueExeAdapter.CAMBIOESTADO_NAME, cueExeAdapterVO);
		}
		
		return forwardSeleccionar(mapping, request, 
			ExeConstants.METOD_CUEEXE_PARAM_SOLICITANTE, PadConstants.ACTION_BUSCAR_PERSONA , false);

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
			CueExeAdapter cueExeAdapterVO =  (CueExeAdapter) userSession.get(CueExeAdapter.CAMBIOESTADO_NAME);
			
			// Si es nulo no se puede continuar
			if (cueExeAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CueExeAdapter.CAMBIOESTADO_NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CueExeAdapter.CAMBIOESTADO_NAME); 
			}

			// recupero el id seleccionado por el usuario
			String selectedId = navModel.getSelectedId();
			
			// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
			if (StringUtil.isNullOrEmpty(selectedId)) {
				// Envio el VO al request
				request.setAttribute(CueExeAdapter.CAMBIOESTADO_NAME, cueExeAdapterVO);
				return mapping.findForward(ExeConstants.FWD_CAMBIOESTADO_CUEEXE_ADAPTER);
			}

			// Seteo el id atributo seleccionado
			cueExeAdapterVO.getCueExe().getSolicitante().setId(new Long(selectedId));
			
			// llamo al param del servicio
			cueExeAdapterVO = ExeServiceLocator.getExencionService().getCueExeAdapterParamSolicitante(userSession, cueExeAdapterVO);

            // Tiene errores recuperables
			if (cueExeAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExeAdapterVO.infoString()); 
				saveDemodaErrors(request, cueExeAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
					CueExeAdapter.CAMBIOESTADO_NAME, cueExeAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (cueExeAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cueExeAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
					CueExeAdapter.CAMBIOESTADO_NAME, cueExeAdapterVO);
			}

			// grabo los mensajes si hubiere
			saveDemodaMessages(request, cueExeAdapterVO);

			// Envio el VO al request
			request.setAttribute(CueExeAdapter.CAMBIOESTADO_NAME, cueExeAdapterVO);
			
			return mapping.findForward(ExeConstants.FWD_CAMBIOESTADO_CUEEXE_ADAPTER);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CueExeAdapter.CAMBIOESTADO_NAME);
		}
	}

	


}
