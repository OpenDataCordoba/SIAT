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

import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.cyq.iface.model.ProcedimientoAdapter;
import ar.gov.rosario.siat.cyq.iface.model.ProcedimientoVO;
import ar.gov.rosario.siat.cyq.iface.service.CyqServiceLocator;
import ar.gov.rosario.siat.cyq.iface.util.CyqSecurityConstants;
import ar.gov.rosario.siat.cyq.view.util.CyqConstants;
import ar.gov.rosario.siat.pad.iface.model.PersonaSearchPage;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarEncProcedimientoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEncProcedimientoDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.ABM_PROCEDIMIENTO_ENC_CyQ, act);		

		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ProcedimientoAdapter procedimientoAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());

			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getProcedimientoAdapterForUpdate(userSession, commonKey)";
				procedimientoAdapterVO = CyqServiceLocator.getConcursoyQuiebraService().getProcedimientoAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(CyqConstants.FWD_PROCEDIMIENTO_ENC_EDIT_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getProcedimientoAdapterForCreate(userSession)";
				procedimientoAdapterVO = CyqServiceLocator.getConcursoyQuiebraService().getProcedimientoAdapterForCreate(userSession);
				actionForward = mapping.findForward(CyqConstants.FWD_PROCEDIMIENTO_ENC_EDIT_ADAPTER);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " act: " + navModel.getAct());
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (procedimientoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + procedimientoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcedimientoAdapter.ENC_NAME, procedimientoAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			procedimientoAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ProcedimientoAdapter.ENC_NAME + ": "+ procedimientoAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ProcedimientoAdapter.ENC_NAME, procedimientoAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ProcedimientoAdapter.ENC_NAME, procedimientoAdapterVO);

			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcedimientoAdapter.ENC_NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, 
			CyqSecurityConstants.ABM_PROCEDIMIENTO_ENC_CyQ, BaseSecurityConstants.AGREGAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ProcedimientoAdapter procedimientoAdapterVO = (ProcedimientoAdapter) userSession.get(ProcedimientoAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (procedimientoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcedimientoAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcedimientoAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(procedimientoAdapterVO, request);
			
			procedimientoAdapterVO.getProcedimiento().setListIdAreaSolicSelected(request.getParameterValues("listIdAreaSolicSelected"));
			
            // Tiene errores recuperables
			if (procedimientoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procedimientoAdapterVO.infoString()); 
				saveDemodaErrors(request, procedimientoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcedimientoAdapter.ENC_NAME, procedimientoAdapterVO);
			}
			
			// llamada al servicio
			ProcedimientoVO procedimientoVO = CyqServiceLocator.getConcursoyQuiebraService().createProcedimiento(userSession, procedimientoAdapterVO.getProcedimiento());
			
            // Tiene errores recuperables
			if (procedimientoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procedimientoVO.infoString()); 
				saveDemodaErrors(request, procedimientoVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcedimientoAdapter.ENC_NAME, procedimientoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (procedimientoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + procedimientoVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcedimientoAdapter.ENC_NAME, procedimientoAdapterVO);
			}

			// Si tiene permiso lo dirijo al adapter de modificacion, 
			// sino vuelve al searchPage
			if (hasAccess(userSession, CyqSecurityConstants.ABM_PROCEDIMIENTO_CyQ, 
				BaseSecurityConstants.MODIFICAR)) {
				
				// seteo el id para que lo use el siguiente action 
				userSession.getNavModel().setSelectedId(procedimientoVO.getId().toString());
				
				userSession.getNavModel().setPrevAction("/cyq/BuscarProcedimientoAva");
				userSession.getNavModel().setPrevActionParameter(BaseConstants.ACT_BUSCAR);
				
				// lo dirijo al adapter de modificacion
				return forwardConfirmarOk(mapping, request, funcName, ProcedimientoAdapter.ENC_NAME, 
					CyqConstants.PATH_ADMINISTRAR_PROCEDIMIENTO, BaseConstants.METHOD_INICIALIZAR, 
					BaseConstants.ACT_MODIFICAR);
			} else {
				// lo dirijo al searchPage				
				return forwardConfirmarOk(mapping, request, funcName, ProcedimientoAdapter.ENC_NAME);
			}
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcedimientoAdapter.ENC_NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, 
			CyqSecurityConstants.ABM_PROCEDIMIENTO_ENC_CyQ, BaseSecurityConstants.MODIFICAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ProcedimientoAdapter procedimientoAdapterVO = (ProcedimientoAdapter) userSession.get(ProcedimientoAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (procedimientoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcedimientoAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcedimientoAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(procedimientoAdapterVO, request);
			
			procedimientoAdapterVO.getProcedimiento().setListIdAreaSolicSelected(request.getParameterValues("listIdAreaSolicSelected"));
			
            // Tiene errores recuperables
			if (procedimientoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procedimientoAdapterVO.infoString()); 
				saveDemodaErrors(request, procedimientoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcedimientoAdapter.ENC_NAME, procedimientoAdapterVO);
			}
			
			// llamada al servicio
			ProcedimientoVO procedimientoVO = CyqServiceLocator.getConcursoyQuiebraService().updateProcedimiento(userSession, procedimientoAdapterVO.getProcedimiento());
			
            // Tiene errores recuperables
			if (procedimientoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procedimientoAdapterVO.infoString()); 
				saveDemodaErrors(request, procedimientoVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcedimientoAdapter.ENC_NAME, procedimientoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (procedimientoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + procedimientoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcedimientoAdapter.ENC_NAME, procedimientoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ProcedimientoAdapter.ENC_NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcedimientoAdapter.ENC_NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ProcedimientoAdapter.ENC_NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, ProcedimientoAdapter.ENC_NAME);
		
	}

	@Deprecated
	public ActionForward paramJuzgado (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
				
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ProcedimientoAdapter procedimientoAdapterVO = (ProcedimientoAdapter) userSession.get(ProcedimientoAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (procedimientoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcedimientoAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcedimientoAdapter.ENC_NAME); 
			}
			
			DemodaUtil.populateVO(procedimientoAdapterVO, request);

			// llamada al servicio
			procedimientoAdapterVO = CyqServiceLocator.getConcursoyQuiebraService().getProcedimientoAdapterParamJuzgado(userSession, procedimientoAdapterVO);
			
            // Tiene errores recuperables
			if (procedimientoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procedimientoAdapterVO.infoString()); 
				saveDemodaErrors(request, procedimientoAdapterVO);
				return forwardErrorRecoverable
					(mapping, request, userSession, ProcedimientoAdapter.ENC_NAME, procedimientoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (procedimientoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + procedimientoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable
					(mapping, request, funcName, ProcedimientoAdapter.ENC_NAME, procedimientoAdapterVO);
			}

			// Envio el VO al request
			request.setAttribute(ProcedimientoAdapter.ENC_NAME, procedimientoAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ProcedimientoAdapter.ENC_NAME, procedimientoAdapterVO);
			
			return mapping.findForward(CyqConstants.FWD_PROCEDIMIENTO_ENC_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcedimientoAdapter.ENC_NAME);
		}
	}

	public ActionForward buscarContribuyente(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		// Bajo el adapter del userSession
		ProcedimientoAdapter procedimientoAdapterVO = (ProcedimientoAdapter) userSession.get(ProcedimientoAdapter.ENC_NAME);
		
		// Si es nulo no se puede continuar
		if (procedimientoAdapterVO == null) {
			log.error("error en: "  + funcName + ": " + ProcedimientoAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, ProcedimientoAdapter.ENC_NAME); 
		}

		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(procedimientoAdapterVO, request);
				
        // Tiene errores recuperables
		if (procedimientoAdapterVO.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + procedimientoAdapterVO.infoString()); 
			saveDemodaErrors(request, procedimientoAdapterVO);
			return forwardErrorRecoverable(mapping, request, userSession, ProcedimientoAdapter.ENC_NAME, procedimientoAdapterVO);
		}
		
		// selecciono solo contribuyentes.
		userSession.getNavModel().putParameter(PersonaSearchPage.PARAM_SELECCIONAR_SOLO_CONTRIB, Boolean.FALSE);
		
		return forwardSeleccionar(mapping, request, 
			CyqConstants.METOD_PROCEDIMIENTO_PARAM_CONTRIBUYENTE, PadConstants.ACTION_BUSCAR_PERSONA , true);

	}

	public ActionForward paramContribuyente(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		NavModel navModel = userSession.getNavModel();
		
		try {
			
			//bajo el adapter del usserSession
			ProcedimientoAdapter procedimientoAdapterVO =  (ProcedimientoAdapter) userSession.get(ProcedimientoAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (procedimientoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcedimientoAdapter.ENC_NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcedimientoAdapter.ENC_NAME); 
			}

			// recupero el id seleccionado por el usuario
			String selectedId = navModel.getSelectedId();
			
			// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
			if (StringUtil.isNullOrEmpty(selectedId)) {
				// Envio el VO al request
				request.setAttribute(ProcedimientoAdapter.ENC_NAME, procedimientoAdapterVO);
				return mapping.findForward(CyqConstants.FWD_PROCEDIMIENTO_ENC_EDIT_ADAPTER); 
			}

			// Seteo el id atributo seleccionado
			procedimientoAdapterVO.getProcedimiento().getContribuyente().setId(new Long(selectedId));
			
			// llamo al param del servicio
			procedimientoAdapterVO = CyqServiceLocator.getConcursoyQuiebraService().getProcedimientoAdapterParamContribuyente(userSession, procedimientoAdapterVO);

            // Tiene errores recuperables
			if (procedimientoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procedimientoAdapterVO.infoString()); 
				saveDemodaErrors(request, procedimientoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
					ProcedimientoAdapter.ENC_NAME, procedimientoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (procedimientoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + procedimientoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
					ProcedimientoAdapter.ENC_NAME, procedimientoAdapterVO);
			}

			// grabo los mensajes si hubiere
			saveDemodaMessages(request, procedimientoAdapterVO);

			// Envio el VO al request
			request.setAttribute(ProcedimientoAdapter.ENC_NAME, procedimientoAdapterVO);

			return mapping.findForward(CyqConstants.FWD_PROCEDIMIENTO_ENC_EDIT_ADAPTER);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcedimientoAdapter.ENC_NAME);
		}
	}
	
	public ActionForward validarCaso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ProcedimientoAdapter adapterVO = (ProcedimientoAdapter)userSession.get(ProcedimientoAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (adapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcedimientoAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcedimientoAdapter.ENC_NAME); 
			}
			
			// Populate como en un buscar
			DemodaUtil.populateVO(adapterVO, request);
			
			log.debug( funcName + " " + adapterVO.getProcedimiento().getCaso().infoString());
			
			// llamada al servicio
			CasServiceLocator.getCasCasoService().validarCaso(userSession, adapterVO.getProcedimiento()); 
			
			adapterVO.getProcedimiento().passErrorMessages(adapterVO);
		    
		    saveDemodaMessages(request, adapterVO);
		    saveDemodaErrors(request, adapterVO);
		    
			request.setAttribute(ProcedimientoAdapter.ENC_NAME, adapterVO);
			
			return mapping.findForward( CyqConstants.FWD_PROCEDIMIENTO_ENC_EDIT_ADAPTER); 
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcedimientoAdapter.ENC_NAME);
		}	
	}

}