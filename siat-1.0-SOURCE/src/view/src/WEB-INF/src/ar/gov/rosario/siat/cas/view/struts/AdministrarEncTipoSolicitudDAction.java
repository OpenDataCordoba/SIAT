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

import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.cas.iface.model.TipoSolicitudAdapter;
import ar.gov.rosario.siat.cas.iface.model.TipoSolicitudVO;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.cas.iface.util.CasSecurityConstants;
import ar.gov.rosario.siat.cas.view.util.CasConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarEncTipoSolicitudDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEncTipoSolicitudDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, CasSecurityConstants.ABM_TIPOSOLICITUD_ENC, act);		

		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		TipoSolicitudAdapter tipoSolicitudAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());

			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getTipoSolicitudAdapterForUpdate(userSession, commonKey)";
				tipoSolicitudAdapterVO = CasServiceLocator.getSolicitudService().getTipoSolicitudAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(CasConstants.FWD_TIPOSOLICITUD_ENC_EDIT_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getTipoSolicitudAdapterForCreate(userSession)";
				tipoSolicitudAdapterVO = CasServiceLocator.getSolicitudService().getTipoSolicitudAdapterForCreate(userSession);
				actionForward = mapping.findForward(CasConstants.FWD_TIPOSOLICITUD_ENC_EDIT_ADAPTER);
			}
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (tipoSolicitudAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + tipoSolicitudAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoSolicitudAdapter.ENC_NAME, tipoSolicitudAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			tipoSolicitudAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + TipoSolicitudAdapter.ENC_NAME + ": "+ tipoSolicitudAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(TipoSolicitudAdapter.ENC_NAME, tipoSolicitudAdapterVO);
			// Subo el adapter al userSession
			userSession.put(TipoSolicitudAdapter.ENC_NAME, tipoSolicitudAdapterVO);

			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoSolicitudAdapter.ENC_NAME);
		}
	}
		
	public ActionForward agregar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping, 
				CasSecurityConstants.ABM_TIPOSOLICITUD_ENC, BaseSecurityConstants.AGREGAR);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				TipoSolicitudAdapter tipoSolicitudAdapterVO = (TipoSolicitudAdapter) userSession.get(TipoSolicitudAdapter.ENC_NAME);				
				
				// Si es nulo no se puede continuar
			
				if (tipoSolicitudAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + TipoSolicitudAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, TipoSolicitudAdapter.ENC_NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(tipoSolicitudAdapterVO, request);
				
	            // Tiene errores recuperables
				if (tipoSolicitudAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + tipoSolicitudAdapterVO.infoString()); 
					saveDemodaErrors(request, tipoSolicitudAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, TipoSolicitudAdapter.ENC_NAME, tipoSolicitudAdapterVO);
				}
				
				// llamada al servicio
				TipoSolicitudVO tipoSolicitudVO = CasServiceLocator.getSolicitudService().createTipoSolicitud(userSession, tipoSolicitudAdapterVO.getTipoSolicitud());
				
	            // Tiene errores recuperables
				if (tipoSolicitudVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + tipoSolicitudVO.infoString()); 
					saveDemodaErrors(request, tipoSolicitudVO);
					return forwardErrorRecoverable(mapping, request, userSession, TipoSolicitudAdapter.ENC_NAME, tipoSolicitudAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (tipoSolicitudVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + tipoSolicitudVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, TipoSolicitudAdapter.ENC_NAME, tipoSolicitudAdapterVO);
				}

				// Si tiene permiso lo dirijo al adapter de modificacion, 
				// sino vuelve al searchPage
				if (hasAccess(userSession, CasSecurityConstants.ABM_TIPOSOLICITUD, 
					BaseSecurityConstants.MODIFICAR)) {
					
					// seteo el id para que lo use el siguiente action 
					userSession.getNavModel().setSelectedId(tipoSolicitudVO.getId().toString());

					// lo dirijo al adapter de modificacion
					return forwardConfirmarOk(mapping, request, funcName, TipoSolicitudAdapter.ENC_NAME, 
						CasConstants.PATH_ADMINISTRAR_TIPOSOLICITUD, BaseConstants.METHOD_INICIALIZAR, 
						BaseConstants.ACT_MODIFICAR);
				} else {
					
					// lo dirijo al searchPage				
					return forwardConfirmarOk(mapping, request, funcName, TipoSolicitudAdapter.ENC_NAME);
					
				}
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, TipoSolicitudAdapter.ENC_NAME);
			}
		}
	
	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, 
			CasSecurityConstants.ABM_TIPOSOLICITUD_ENC, BaseSecurityConstants.MODIFICAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipoSolicitudAdapter tipoSolicitudAdapterVO = (TipoSolicitudAdapter) userSession.get(TipoSolicitudAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (tipoSolicitudAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipoSolicitudAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoSolicitudAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(tipoSolicitudAdapterVO, request);
			
            // Tiene errores recuperables
			if (tipoSolicitudAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoSolicitudAdapterVO.infoString()); 
				saveDemodaErrors(request, tipoSolicitudAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoSolicitudAdapter.ENC_NAME, tipoSolicitudAdapterVO);
			}
			
			// llamada al servicio
			TipoSolicitudVO tipoSolicitudVO = CasServiceLocator.getSolicitudService().updateTipoSolicitud(userSession, tipoSolicitudAdapterVO.getTipoSolicitud());
			
            // Tiene errores recuperables
			if (tipoSolicitudVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoSolicitudAdapterVO.infoString()); 
				saveDemodaErrors(request, tipoSolicitudVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoSolicitudAdapter.ENC_NAME, tipoSolicitudAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (tipoSolicitudVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoSolicitudAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoSolicitudAdapter.ENC_NAME, tipoSolicitudAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipoSolicitudAdapter.ENC_NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoSolicitudAdapter.ENC_NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {		
		return baseVolver(mapping, form, request, response, TipoSolicitudAdapter.ENC_NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, TipoSolicitudAdapter.ENC_NAME);
		
	}
	
	
}
	
