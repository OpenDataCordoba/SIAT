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

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.pro.iface.model.ProcesoAdapter;
import ar.gov.rosario.siat.pro.iface.model.ProcesoVO;
import ar.gov.rosario.siat.pro.iface.service.ProServiceLocator;
import ar.gov.rosario.siat.pro.iface.util.ProError;
import ar.gov.rosario.siat.pro.iface.util.ProSecurityConstants;
import ar.gov.rosario.siat.pro.view.util.ProConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarProcesoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarProcesoDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, ProSecurityConstants.ABM_PROCESO, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ProcesoAdapter procesoAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getProcesoAdapterForView(userSession, commonKey)";
				procesoAdapterVO = ProServiceLocator.getConsultaService().getProcesoAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(ProConstants.FWD_PROCESO_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getProcesoAdapterForUpdate(userSession, commonKey)";
				procesoAdapterVO = ProServiceLocator.getConsultaService().getProcesoAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(ProConstants.FWD_PROCESO_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getProcesoAdapterForView(userSession, commonKey)";
				procesoAdapterVO = ProServiceLocator.getConsultaService().getProcesoAdapterForView(userSession, commonKey);				
				procesoAdapterVO.addMessage(BaseError.MSG_ELIMINAR, ProError.PROCESO_LABEL);
				actionForward = mapping.findForward(ProConstants.FWD_PROCESO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getProcesoAdapterForCreate(userSession)";
				procesoAdapterVO = ProServiceLocator.getConsultaService().getProcesoAdapterForCreate(userSession);
				actionForward = mapping.findForward(ProConstants.FWD_PROCESO_EDIT_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (procesoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + procesoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoAdapter.NAME, procesoAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			procesoAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ProcesoAdapter.NAME + ": "+ procesoAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ProcesoAdapter.NAME, procesoAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ProcesoAdapter.NAME, procesoAdapterVO);
			 
			saveDemodaMessages(request, procesoAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcesoAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, ProSecurityConstants.ABM_PROCESO, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ProcesoAdapter procesoAdapterVO = (ProcesoAdapter) userSession.get(ProcesoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (procesoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(procesoAdapterVO, request);
			
            // Tiene errores recuperables
			if (procesoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procesoAdapterVO.infoString()); 
				saveDemodaErrors(request, procesoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcesoAdapter.NAME, procesoAdapterVO);
			}
			
			// llamada al servicio
			ProcesoVO procesoVO = ProServiceLocator.getConsultaService().createProceso(userSession, procesoAdapterVO.getProceso());
			
            // Tiene errores recuperables
			if (procesoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procesoVO.infoString()); 
				saveDemodaErrors(request, procesoVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcesoAdapter.NAME, procesoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (procesoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + procesoVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoAdapter.NAME, procesoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ProcesoAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcesoAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, ProSecurityConstants.ABM_PROCESO, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ProcesoAdapter procesoAdapterVO = (ProcesoAdapter) userSession.get(ProcesoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (procesoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(procesoAdapterVO, request);
			
            // Tiene errores recuperables
			if (procesoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procesoAdapterVO.infoString()); 
				saveDemodaErrors(request, procesoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcesoAdapter.NAME, procesoAdapterVO);
			}
			
			// llamada al servicio
			ProcesoVO procesoVO = ProServiceLocator.getConsultaService().updateProceso(userSession, procesoAdapterVO.getProceso());
			
            // Tiene errores recuperables
			if (procesoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procesoAdapterVO.infoString()); 
				saveDemodaErrors(request, procesoVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcesoAdapter.NAME, procesoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (procesoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + procesoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoAdapter.NAME, procesoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ProcesoAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcesoAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, ProSecurityConstants.ABM_PROCESO, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ProcesoAdapter procesoAdapterVO = (ProcesoAdapter) userSession.get(ProcesoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (procesoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoAdapter.NAME); 
			}

			// llamada al servicio
			ProcesoVO procesoVO = ProServiceLocator.getConsultaService().deleteProceso
				(userSession, procesoAdapterVO.getProceso());
			
            // Tiene errores recuperables
			if (procesoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procesoAdapterVO.infoString());
				saveDemodaErrors(request, procesoVO);				
				request.setAttribute(ProcesoAdapter.NAME, procesoAdapterVO);
				return mapping.findForward(ProConstants.FWD_PROCESO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (procesoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + procesoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoAdapter.NAME, procesoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ProcesoAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcesoAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ProcesoAdapter.NAME);
		
	}
		
	
	public ActionForward paramTipoEjecucion(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				ProcesoAdapter procesoAdapterVO = (ProcesoAdapter) userSession.get(ProcesoAdapter.NAME);
		
				// Si es nulo no se puede continuar
				if (procesoAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + ProcesoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(procesoAdapterVO, request);
				
	            // Tiene errores recuperables
				if (procesoAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + procesoAdapterVO.infoString()); 
					saveDemodaErrors(request, procesoAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, ProcesoAdapter.NAME, procesoAdapterVO);
				}
				
				// Llamada al servicio
				procesoAdapterVO = ProServiceLocator.getConsultaService().getProcesoAdapterParamTipoEjecucion(userSession, procesoAdapterVO);
				
	            // Tiene errores recuperables
				if (procesoAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + procesoAdapterVO.infoString()); 
					saveDemodaErrors(request, procesoAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, ProcesoAdapter.NAME, procesoAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (procesoAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + procesoAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoAdapter.NAME, procesoAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(ProcesoAdapter.NAME, procesoAdapterVO);
				// Subo el adapter al userSession
				userSession.put(ProcesoAdapter.NAME, procesoAdapterVO);
				
				return mapping.findForward(ProConstants.FWD_PROCESO_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ProcesoAdapter.NAME);
			}
	}

}
