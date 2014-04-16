//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.view.struts;

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
import ar.gov.rosario.siat.cas.iface.model.SolicitudAdapter;
import ar.gov.rosario.siat.cas.iface.model.SolicitudVO;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.cas.iface.util.CasError;
import ar.gov.rosario.siat.cas.iface.util.CasSecurityConstants;
import ar.gov.rosario.siat.cas.view.util.CasConstants;
import ar.gov.rosario.siat.pad.iface.model.CuentaSearchPage;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import ar.gov.rosario.siat.pad.view.struts.BuscarCuentaDAction;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarSolicitudDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarSolicitudDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, CasSecurityConstants.ABM_SOLICITUD, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		SolicitudAdapter solicitudAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getSolicitudAdapterForView(userSession, commonKey)";
				solicitudAdapterVO = CasServiceLocator.getSolicitudService().getSolicitudAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(CasConstants.FWD_SOLICITUD_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getSolicitudAdapterForUpdate(userSession, commonKey)";
				solicitudAdapterVO = CasServiceLocator.getSolicitudService().getSolicitudAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(CasConstants.FWD_SOLICITUD_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getSolicitudAdapterForView(userSession, commonKey)";
				solicitudAdapterVO = CasServiceLocator.getSolicitudService().getSolicitudAdapterForView(userSession, commonKey);				
				solicitudAdapterVO.addMessage(BaseError.MSG_ELIMINAR, CasError.SOLICITUD_LABEL);
				actionForward = mapping.findForward(CasConstants.FWD_SOLICITUD_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getSolicitudAdapterForCreate(userSession)";
				solicitudAdapterVO = CasServiceLocator.getSolicitudService().getSolicitudAdapterForCreate(userSession);
				actionForward = mapping.findForward(CasConstants.FWD_SOLICITUD_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getSolicitudAdapterForView(userSession)";
				solicitudAdapterVO = CasServiceLocator.getSolicitudService().getSolicitudAdapterForView(userSession, commonKey);
				solicitudAdapterVO.addMessage(BaseError.MSG_ACTIVAR, CasError.SOLICITUD_LABEL);
				actionForward = mapping.findForward(CasConstants.FWD_SOLICITUD_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getSolicitudAdapterForView(userSession)";
				solicitudAdapterVO = CasServiceLocator.getSolicitudService().getSolicitudAdapterForView(userSession, commonKey);
				solicitudAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, CasError.SOLICITUD_LABEL);
				actionForward = mapping.findForward(CasConstants.FWD_SOLICITUD_VIEW_ADAPTER);				
			}
			if (act.equals(CasConstants.ACT_CAMBIARESTADO_SOLICITUD)) {
				stringServicio = "getSolicitudAdapterForCambiarEstado(userSession, commonKey)";
				solicitudAdapterVO = CasServiceLocator.getSolicitudService().getSolicitudAdapterForCambiarEstado(userSession, commonKey);
				actionForward = mapping.findForward(CasConstants.FWD_SOLICITUD_VIEW_ADAPTER);
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (solicitudAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + solicitudAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SolicitudAdapter.NAME, solicitudAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			solicitudAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + SolicitudAdapter.NAME + ": "+ solicitudAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(SolicitudAdapter.NAME, solicitudAdapterVO);
			// Subo el apdater al userSession
			userSession.put(SolicitudAdapter.NAME, solicitudAdapterVO);
			 
			saveDemodaMessages(request, solicitudAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SolicitudAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, CasSecurityConstants.ABM_SOLICITUD, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			SolicitudAdapter solicitudAdapterVO = (SolicitudAdapter) userSession.get(SolicitudAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (solicitudAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + SolicitudAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SolicitudAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(solicitudAdapterVO, request);
			
            // Tiene errores recuperables
			if (solicitudAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + solicitudAdapterVO.infoString()); 
				saveDemodaErrors(request, solicitudAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, SolicitudAdapter.NAME, solicitudAdapterVO);
			}
			
			// llamada al servicio
			SolicitudVO solicitudVO = CasServiceLocator.getSolicitudService().createSolicitud(userSession, solicitudAdapterVO.getSolicitud());
			
            // Tiene errores recuperables
			if (solicitudVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + solicitudVO.infoString()); 
				saveDemodaErrors(request, solicitudVO);
				return forwardErrorRecoverable(mapping, request, userSession, SolicitudAdapter.NAME, solicitudAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (solicitudVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + solicitudVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SolicitudAdapter.NAME, solicitudAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, SolicitudAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SolicitudAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, CasSecurityConstants.ABM_SOLICITUD, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			SolicitudAdapter solicitudAdapterVO = (SolicitudAdapter) userSession.get(SolicitudAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (solicitudAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + SolicitudAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SolicitudAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(solicitudAdapterVO, request);
			
            // Tiene errores recuperables
			if (solicitudAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + solicitudAdapterVO.infoString()); 
				saveDemodaErrors(request, solicitudAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, SolicitudAdapter.NAME, solicitudAdapterVO);
			}
			
			// llamada al servicio
			SolicitudVO solicitudVO = CasServiceLocator.getSolicitudService().updateSolicitud(userSession, solicitudAdapterVO.getSolicitud());
			
            // Tiene errores recuperables
			if (solicitudVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + solicitudAdapterVO.infoString()); 
				saveDemodaErrors(request, solicitudVO);
				return forwardErrorRecoverable(mapping, request, userSession, SolicitudAdapter.NAME, solicitudAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (solicitudVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + solicitudAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SolicitudAdapter.NAME, solicitudAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, SolicitudAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SolicitudAdapter.NAME);
		}
	}

	public ActionForward cambiarEstado(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, CasSecurityConstants.ABM_SOLICITUD, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			SolicitudAdapter solicitudAdapterVO = (SolicitudAdapter) userSession.get(SolicitudAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (solicitudAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + SolicitudAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SolicitudAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(solicitudAdapterVO, request);
			
            // Tiene errores recuperables
			if (solicitudAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + solicitudAdapterVO.infoString()); 
				saveDemodaErrors(request, solicitudAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, SolicitudAdapter.NAME, solicitudAdapterVO);
			}
						
			// llamada al servicio
			solicitudAdapterVO = CasServiceLocator.getSolicitudService().cambiarEstado(userSession, solicitudAdapterVO);
			
            // Tiene errores recuperables
			if (solicitudAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + solicitudAdapterVO.infoString()); 
				saveDemodaErrors(request, solicitudAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, SolicitudAdapter.NAME, solicitudAdapterVO, CasConstants.FWD_SOLICITUD_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (solicitudAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + solicitudAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SolicitudAdapter.NAME, solicitudAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, SolicitudAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SolicitudAdapter.NAME);
		}
	}
	
	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, CasSecurityConstants.ABM_SOLICITUD, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			SolicitudAdapter solicitudAdapterVO = (SolicitudAdapter) userSession.get(SolicitudAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (solicitudAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + SolicitudAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SolicitudAdapter.NAME); 
			}

			// llamada al servicio
			SolicitudVO solicitudVO = CasServiceLocator.getSolicitudService().deleteSolicitud
				(userSession, solicitudAdapterVO.getSolicitud());
			
            // Tiene errores recuperables
			if (solicitudVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + solicitudAdapterVO.infoString());
				saveDemodaErrors(request, solicitudVO);				
				request.setAttribute(SolicitudAdapter.NAME, solicitudAdapterVO);
				return mapping.findForward(CasConstants.FWD_SOLICITUD_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (solicitudVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + solicitudAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SolicitudAdapter.NAME, solicitudAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, SolicitudAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SolicitudAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, CasSecurityConstants.ABM_SOLICITUD, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			SolicitudAdapter solicitudAdapterVO = (SolicitudAdapter) userSession.get(SolicitudAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (solicitudAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + SolicitudAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SolicitudAdapter.NAME); 
			}

			// llamada al servicio
			SolicitudVO solicitudVO = CasServiceLocator.getSolicitudService().activarSolicitud
				(userSession, solicitudAdapterVO.getSolicitud());
			
            // Tiene errores recuperables
			if (solicitudVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + solicitudAdapterVO.infoString());
				saveDemodaErrors(request, solicitudVO);				
				request.setAttribute(SolicitudAdapter.NAME, solicitudAdapterVO);
				return mapping.findForward(CasConstants.FWD_SOLICITUD_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (solicitudVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + solicitudAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SolicitudAdapter.NAME, solicitudAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, SolicitudAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SolicitudAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, CasSecurityConstants.ABM_SOLICITUD, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			SolicitudAdapter solicitudAdapterVO = (SolicitudAdapter) userSession.get(SolicitudAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (solicitudAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + SolicitudAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SolicitudAdapter.NAME); 
			}

			// llamada al servicio
			SolicitudVO solicitudVO = CasServiceLocator.getSolicitudService().desactivarSolicitud
				(userSession, solicitudAdapterVO.getSolicitud());
			
            // Tiene errores recuperables
			if (solicitudVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + solicitudAdapterVO.infoString());
				saveDemodaErrors(request, solicitudVO);				
				request.setAttribute(SolicitudAdapter.NAME, solicitudAdapterVO);
				return mapping.findForward(CasConstants.FWD_SOLICITUD_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (solicitudVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + solicitudAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SolicitudAdapter.NAME, solicitudAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, SolicitudAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SolicitudAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, SolicitudAdapter.NAME);
		
	}
	
	
	public ActionForward paramAreaDestino (ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			SolicitudAdapter solicitudAdapterVO = (SolicitudAdapter) userSession.get(SolicitudAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (solicitudAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + SolicitudAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SolicitudAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(solicitudAdapterVO, request);
			
            // Tiene errores recuperables
			if (solicitudAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + solicitudAdapterVO.infoString()); 
				saveDemodaErrors(request, solicitudAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, SolicitudAdapter.NAME, solicitudAdapterVO);
			}
			
			// llamada al servicio
			solicitudAdapterVO = CasServiceLocator.getSolicitudService().getSolicitudAdapterParamAreaDestino(userSession, solicitudAdapterVO);
			
            // Tiene errores recuperables
			if (solicitudAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + solicitudAdapterVO.infoString()); 
				saveDemodaErrors(request, solicitudAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, SolicitudAdapter.NAME, solicitudAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (solicitudAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + solicitudAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SolicitudAdapter.NAME, solicitudAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(SolicitudAdapter.NAME, solicitudAdapterVO);
			// Subo el apdater al userSession
			userSession.put(SolicitudAdapter.NAME, solicitudAdapterVO);
			
			return mapping.findForward(CasConstants.FWD_SOLICITUD_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SolicitudAdapter.NAME);
		}
	}

	public ActionForward buscarCuenta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());
		
		// seteo la accion y el parametro para volver
		navModel.setPrevAction(mapping.getPath());
		navModel.setPrevActionParameter("paramCuenta");

		// seteo los parametros para cuando oprima seleccionar
		navModel.setSelectAction("/cas/AdministrarSolicitud");
		navModel.setSelectActionParameter("paramCuenta");
		navModel.setAgregarEnSeleccion(false);

		// Bajo el adapter del userSession
		SolicitudAdapter solicitudAdapterVO = (SolicitudAdapter) userSession.get(SolicitudAdapter.NAME);
		
		// Si es nulo no se puede continuar
		if (solicitudAdapterVO == null) {
			log.error("error en: "  + funcName + ": " + SolicitudAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, SolicitudAdapter.NAME); 
		}

		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(solicitudAdapterVO, request);
		
        // Tiene errores recuperables
		if (solicitudAdapterVO.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + solicitudAdapterVO.infoString()); 
			saveDemodaErrors(request, solicitudAdapterVO);
			return forwardErrorRecoverable(mapping, request, userSession, SolicitudAdapter.NAME, solicitudAdapterVO);
		}
		
		CuentaSearchPage cuentaFiltro = new CuentaSearchPage();
		
		// Seteo el recurso y numero de cuenta
		cuentaFiltro.getCuentaTitular().getCuenta().setRecurso(solicitudAdapterVO.getSolicitud().getRecurso());
		cuentaFiltro.getCuentaTitular().getCuenta().setNumeroCuenta( solicitudAdapterVO.getSolicitud().getCuenta().getNumeroCuenta());
		
		navModel.putParameter(BuscarCuentaDAction.CUENTA_SEARCHPAGE_FILTRO, cuentaFiltro);
		
		// seteo el act a ejecutar en el accion al cual me dirijo		
		navModel.setAct(BaseConstants.ACT_SELECCIONAR);
		
		return mapping.findForward(PadConstants.ACTION_BUSCAR_CUENTA);
		
	}
	
	public ActionForward paramCuenta (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				SolicitudAdapter solicitudAdapterVO = (SolicitudAdapter) userSession.get(SolicitudAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (solicitudAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + SolicitudAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, SolicitudAdapter.NAME); 
				}

				// Seteo el id selecionado
				NavModel navModel = userSession.getNavModel();
				
				// recupero el id seleccionado por el usuario
				String selectedId = navModel.getSelectedId();
				
				// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
				if (StringUtil.isNullOrEmpty(selectedId)) {
					// Envio el VO al request
					request.setAttribute(SolicitudAdapter.NAME, solicitudAdapterVO);
					return mapping.findForward(CasConstants.FWD_SOLICITUD_EDIT_ADAPTER);
				}
				
				solicitudAdapterVO.getSolicitud().getCuenta().setId(new Long(selectedId));
				
				// llamada al servicio
				solicitudAdapterVO = CasServiceLocator.getSolicitudService().getSolicitudAdapterParamCuenta(userSession, solicitudAdapterVO);
				
	            // Tiene errores recuperables
				if (solicitudAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + solicitudAdapterVO.infoString()); 
					saveDemodaErrors(request, solicitudAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, SolicitudAdapter.NAME, solicitudAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (solicitudAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + solicitudAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, SolicitudAdapter.NAME, solicitudAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(SolicitudAdapter.NAME, solicitudAdapterVO);
				// Subo el apdater al userSession
				userSession.put(SolicitudAdapter.NAME, solicitudAdapterVO);
				
				return mapping.findForward(CasConstants.FWD_SOLICITUD_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, SolicitudAdapter.NAME);
			}
	}
	
	public ActionForward limpiarCampoCuenta (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				SolicitudAdapter solicitudAdapterVO = (SolicitudAdapter) userSession.get(SolicitudAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (solicitudAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + SolicitudAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, SolicitudAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(solicitudAdapterVO, request);
				
	            // Tiene errores recuperables
				if (solicitudAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + solicitudAdapterVO.infoString()); 
					saveDemodaErrors(request, solicitudAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, SolicitudAdapter.NAME, solicitudAdapterVO);
				}
				
				solicitudAdapterVO.getSolicitud().setCuenta(new CuentaVO());
				
/*	            // Tiene errores recuperables
				if (solicitudAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + solicitudAdapterVO.infoString()); 
					saveDemodaErrors(request, solicitudAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, SolicitudAdapter.NAME, solicitudAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (solicitudAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + solicitudAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, SolicitudAdapter.NAME, solicitudAdapterVO);
				}
				*/
				// Envio el VO al request
				request.setAttribute(SolicitudAdapter.NAME, solicitudAdapterVO);
				// Subo el apdater al userSession
				userSession.put(SolicitudAdapter.NAME, solicitudAdapterVO);
				
				return mapping.findForward(CasConstants.FWD_SOLICITUD_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, SolicitudAdapter.NAME);
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
			SolicitudAdapter adapterVO = (SolicitudAdapter)userSession.get(SolicitudAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (adapterVO == null) {
				log.error("error en: "  + funcName + ": " + SolicitudAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SolicitudAdapter.NAME); 
			}
			
			// Populate como en un buscar
			DemodaUtil.populateVO(adapterVO, request);
			
			log.debug( funcName + " " + adapterVO.getSolicitud().getCaso().infoString());
			
			// llamada al servicio
			CasServiceLocator.getCasCasoService().validarCaso(userSession, adapterVO.getSolicitud()); 
			
			adapterVO.getSolicitud().passErrorMessages(adapterVO);
		    
		    saveDemodaMessages(request, adapterVO);
		    saveDemodaErrors(request, adapterVO);
		    
			request.setAttribute(SolicitudAdapter.NAME, adapterVO);
			
			return mapping.findForward( CasConstants.FWD_SOLICITUD_EDIT_ADAPTER); 
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SolicitudAdapter.NAME);
		}	
	}
}
