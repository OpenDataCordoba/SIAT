//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pro.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.pro.iface.model.EnvioArchivosAdapter;
import ar.gov.rosario.siat.pro.iface.service.ProServiceLocator;
import ar.gov.rosario.siat.pro.iface.util.ProSecurityConstants;
import ar.gov.rosario.siat.pro.view.util.ProConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarEnvioArchivosDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEnvioArchivosDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, ProSecurityConstants.ABM_CORRIDA, BaseConstants.ACT_VER); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		
		EnvioArchivosAdapter envioArchivosAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			Long idPasoCorrida =  (Long) navModel.getParameter(EnvioArchivosAdapter.PARAM_ID_PASOCORRIDA);
			
			if (act.equals(ProConstants.ACT_ENVIAR_ARCHIVOS)) {
				stringServicio = "getEnvioArchivosAdapterForCreate(userSession, commonKey)";
				envioArchivosAdapterVO = ProServiceLocator.getAdpProcesoService()
						.getEnvioArchivosAdapterForCreate(userSession, idPasoCorrida);
				actionForward = mapping.findForward(ProConstants.FWD_ENVIO_ARCHIVOS_EDIT_ADAPTER);
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			
			// Tiene errores no recuperables
			if (envioArchivosAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + envioArchivosAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EnvioArchivosAdapter.NAME, envioArchivosAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			envioArchivosAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + EnvioArchivosAdapter.NAME + ": " + envioArchivosAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(EnvioArchivosAdapter.NAME, envioArchivosAdapterVO);
			// Subo el apdater al userSession
			userSession.put(EnvioArchivosAdapter.NAME, envioArchivosAdapterVO);
			 
			saveDemodaMessages(request, envioArchivosAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EnvioArchivosAdapter.NAME);
		}
	}
	
	public ActionForward validateEnvio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		try {
			
			NavModel navModel = userSession.getNavModel();
			if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());
		
			// Bajo el adapter del userSession
			EnvioArchivosAdapter envioArchivosAdapterVO = (EnvioArchivosAdapter) userSession.get(EnvioArchivosAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (envioArchivosAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EnvioArchivosAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EnvioArchivosAdapter.NAME); 
			}
			
			// Como usamos multibox de struts, purgamos
			// la lista de archivos seleccionados aqui
			envioArchivosAdapterVO.setListFileNameSelected(null);
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(envioArchivosAdapterVO, request);

            // Tiene errores recuperables
			if (envioArchivosAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + envioArchivosAdapterVO.infoString()); 
				saveDemodaErrors(request, envioArchivosAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
					EnvioArchivosAdapter.NAME, envioArchivosAdapterVO);
			}
			
			// llamada al servicio
			envioArchivosAdapterVO = ProServiceLocator.getAdpProcesoService()
				.getEnvioArchivosAdapterForPreview(userSession, envioArchivosAdapterVO);
			
            // Tiene errores recuperables
			if (envioArchivosAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + envioArchivosAdapterVO.infoString()); 
				saveDemodaErrors(request, envioArchivosAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, EnvioArchivosAdapter.NAME, envioArchivosAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (envioArchivosAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + envioArchivosAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EnvioArchivosAdapter.NAME, envioArchivosAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(EnvioArchivosAdapter.NAME, envioArchivosAdapterVO);
			// Subo el apdater al userSession
			userSession.put(EnvioArchivosAdapter.NAME, envioArchivosAdapterVO);

			saveDemodaMessages(request, envioArchivosAdapterVO);
			
			log.debug("exit: " + funcName);
			
			return mapping.findForward(ProConstants.FWD_ENVIO_ARCHIVOS_PREVIEW_ADAPTER);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EnvioArchivosAdapter.NAME);
		}
	}
	
	public ActionForward volverFromPreview(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		try {
			// Bajo el adapter del userSession
			EnvioArchivosAdapter envioArchivosAdapterVO = (EnvioArchivosAdapter) userSession.get(EnvioArchivosAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (envioArchivosAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EnvioArchivosAdapter.NAME + 
				" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EnvioArchivosAdapter.NAME); 
			}
			
			// Limpiamos la lista de errores
			envioArchivosAdapterVO.clearError();
			saveDemodaErrors(request, envioArchivosAdapterVO);
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(envioArchivosAdapterVO, request);
			
			// Envio el VO al request
			request.setAttribute(EnvioArchivosAdapter.NAME, envioArchivosAdapterVO);

			// Subo el apdater al userSession
			userSession.put(EnvioArchivosAdapter.NAME, envioArchivosAdapterVO);

			log.debug("exit: " + funcName);
			
			return mapping.findForward(ProConstants.FWD_ENVIO_ARCHIVOS_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EnvioArchivosAdapter.NAME);
		}
	}

	public ActionForward enviar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		try {
			
			NavModel navModel = userSession.getNavModel();
			if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());
		
			// Bajo el adapter del userSession
			EnvioArchivosAdapter envioArchivosAdapterVO = (EnvioArchivosAdapter) userSession.get(EnvioArchivosAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (envioArchivosAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EnvioArchivosAdapter.NAME + 
				" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EnvioArchivosAdapter.NAME); 
			}
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(envioArchivosAdapterVO, request);

            // Tiene errores recuperables
			if (envioArchivosAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + envioArchivosAdapterVO.infoString()); 
				saveDemodaErrors(request, envioArchivosAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
					EnvioArchivosAdapter.NAME, envioArchivosAdapterVO);
			}
			
			// llamada al servicio
			envioArchivosAdapterVO = ProServiceLocator.getAdpProcesoService().enviarArchivos(userSession, envioArchivosAdapterVO);
			
            // Tiene errores recuperables
			if (envioArchivosAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + envioArchivosAdapterVO.infoString()); 
				saveDemodaErrors(request, envioArchivosAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, EnvioArchivosAdapter.NAME, envioArchivosAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (envioArchivosAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + envioArchivosAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EnvioArchivosAdapter.NAME, envioArchivosAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(EnvioArchivosAdapter.NAME, envioArchivosAdapterVO);
			// Subo el apdater al userSession
			userSession.put(EnvioArchivosAdapter.NAME, envioArchivosAdapterVO);

			saveDemodaMessages(request, envioArchivosAdapterVO);
			
			log.debug("exit: " + funcName);
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, EnvioArchivosAdapter.NAME);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EnvioArchivosAdapter.NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, EnvioArchivosAdapter.NAME);
	}
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, EnvioArchivosAdapter.NAME);
	}
}
