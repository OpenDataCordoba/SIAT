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
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.exe.iface.model.ExencionAdapter;
import ar.gov.rosario.siat.exe.iface.model.ExencionVO;
import ar.gov.rosario.siat.exe.iface.service.ExeServiceLocator;
import ar.gov.rosario.siat.exe.iface.util.ExeSecurityConstants;
import ar.gov.rosario.siat.exe.view.util.ExeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarEncExencionDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEncExencionDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_EXENCION_ENC, act);		

		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ExencionAdapter exencionAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());

			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getExencionAdapterForUpdate(userSession, commonKey)";
				exencionAdapterVO = ExeServiceLocator.getDefinicionService().getExencionAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(ExeConstants.FWD_EXENCION_ENC_EDIT_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getExencionAdapterForCreate(userSession)";
				exencionAdapterVO = ExeServiceLocator.getDefinicionService().getExencionAdapterForCreate(userSession);
				actionForward = mapping.findForward(ExeConstants.FWD_EXENCION_ENC_EDIT_ADAPTER);
			}
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (exencionAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + exencionAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ExencionAdapter.ENC_NAME, exencionAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			exencionAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ExencionAdapter.ENC_NAME + ": "+ exencionAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ExencionAdapter.ENC_NAME, exencionAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ExencionAdapter.ENC_NAME, exencionAdapterVO);

			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ExencionAdapter.ENC_NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, 
			ExeSecurityConstants.ABM_EXENCION_ENC, BaseSecurityConstants.AGREGAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ExencionAdapter exencionAdapterVO = (ExencionAdapter) userSession.get(ExencionAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (exencionAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ExencionAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ExencionAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(exencionAdapterVO, request);
			
            // Tiene errores recuperables
			if (exencionAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + exencionAdapterVO.infoString()); 
				saveDemodaErrors(request, exencionAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ExencionAdapter.ENC_NAME, exencionAdapterVO);
			}
			
			// llamada al servicio
			ExencionVO exencionVO = ExeServiceLocator.getDefinicionService().createExencion(userSession, exencionAdapterVO.getExencion());
			
            // Tiene errores recuperables
			if (exencionVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + exencionVO.infoString()); 
				saveDemodaErrors(request, exencionVO);
				return forwardErrorRecoverable(mapping, request, userSession, ExencionAdapter.ENC_NAME, exencionAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (exencionVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + exencionVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ExencionAdapter.ENC_NAME, exencionAdapterVO);
			}

			// Si tiene permiso lo dirijo al adapter de modificacion, 
			// sino vuelve al searchPage
			if (hasAccess(userSession, ExeSecurityConstants.ABM_EXENCION, 
				BaseSecurityConstants.MODIFICAR)) {
				
				// seteo el id para que lo use el siguiente action 
				userSession.getNavModel().setSelectedId(exencionVO.getId().toString());

				// lo dirijo al adapter de modificacion
				return forwardConfirmarOk(mapping, request, funcName, ExencionAdapter.ENC_NAME, 
					ExeConstants.PATH_ADMINISTRAR_EXENCION, BaseConstants.METHOD_INICIALIZAR, 
					BaseConstants.ACT_MODIFICAR);
			} else {
				
				// lo dirijo al searchPage				
				return forwardConfirmarOk(mapping, request, funcName, ExencionAdapter.ENC_NAME);
				
			}
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ExencionAdapter.ENC_NAME);
		}
	}

	public ActionForward paramAplicaMinimo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
	
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ExencionAdapter exencionAdapterVO = (ExencionAdapter) userSession.get(ExencionAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (exencionAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ExencionAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ExencionAdapter.ENC_NAME); 
			}
	
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(exencionAdapterVO, request);
			
	        // Tiene errores recuperables
			if (exencionAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + exencionAdapterVO.infoString()); 
				saveDemodaErrors(request, exencionAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ExencionAdapter.ENC_NAME, exencionAdapterVO);
			}
			
			// limpia el campo de monto mínimo
				exencionAdapterVO.getExencion().setMontoMinimo(null); 
			
	        // Tiene errores recuperables
			if (exencionAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + exencionAdapterVO.infoString()); 
				saveDemodaErrors(request, exencionAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ExencionAdapter.ENC_NAME, exencionAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (exencionAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + exencionAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ExencionAdapter.ENC_NAME, exencionAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(ExencionAdapter.ENC_NAME, exencionAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ExencionAdapter.ENC_NAME, exencionAdapterVO);
			
			return mapping.findForward(ExeConstants.FWD_EXENCION_ENC_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ExencionAdapter.ENC_NAME);
		}
	}
	
	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, 
			ExeSecurityConstants.ABM_EXENCION_ENC, BaseSecurityConstants.MODIFICAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ExencionAdapter exencionAdapterVO = (ExencionAdapter) userSession.get(ExencionAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (exencionAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ExencionAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ExencionAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(exencionAdapterVO, request);
			
            // Tiene errores recuperables
			if (exencionAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + exencionAdapterVO.infoString()); 
				saveDemodaErrors(request, exencionAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ExencionAdapter.ENC_NAME, exencionAdapterVO);
			}
			
			// llamada al servicio
			ExencionVO exencionVO = ExeServiceLocator.getDefinicionService().updateExencion(userSession, exencionAdapterVO.getExencion());
			
            // Tiene errores recuperables
			if (exencionVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + exencionAdapterVO.infoString()); 
				saveDemodaErrors(request, exencionVO);
				return forwardErrorRecoverable(mapping, request, userSession, ExencionAdapter.ENC_NAME, exencionAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (exencionVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + exencionAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ExencionAdapter.ENC_NAME, exencionAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ExencionAdapter.ENC_NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ExencionAdapter.ENC_NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ExencionAdapter.ENC_NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, ExencionAdapter.ENC_NAME);
		
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
			ExencionAdapter adapterVO = (ExencionAdapter)userSession.get(ExencionAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (adapterVO == null) {
				log.error("error en: "  + funcName + ": " + ExencionAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ExencionAdapter.ENC_NAME); 
			}
			
			// Populate como en un buscar
			DemodaUtil.populateVO(adapterVO, request);
			
			log.debug( funcName + " " + adapterVO.getExencion().getCaso().infoString());
			
			// llamada al servicio
			CasServiceLocator.getCasCasoService().validarCaso(userSession, adapterVO.getExencion()); 
			
			adapterVO.getExencion().passErrorMessages(adapterVO);
		    
		    saveDemodaMessages(request, adapterVO);
		    saveDemodaErrors(request, adapterVO);
		    
			request.setAttribute(ExencionAdapter.ENC_NAME, adapterVO);
			
			return mapping.findForward( ExeConstants.FWD_EXENCION_ENC_EDIT_ADAPTER); 
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ExencionAdapter.ENC_NAME);
		}	
	}
	
}
	
