//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.bal.iface.model.AsentamientoAdapter;
import ar.gov.rosario.siat.bal.iface.model.AsentamientoVO;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class AdministrarAsentamientoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarAsentamientoDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_ASENTAMIENTO, act);		
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			AsentamientoAdapter asentamientoAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
					stringServicio = "getAsentamientoAdapterForView(userSession, commonKey)";
					asentamientoAdapterVO = BalServiceLocator.getAsentamientoService().getAsentamientoAdapterForView
						(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_ASENTAMIENTO_VIEW_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
					stringServicio = "getAsentamientoAdapterForCreate(userSession, commonKey)";
					asentamientoAdapterVO = BalServiceLocator.getAsentamientoService().getAsentamientoAdapterForCreate
						(userSession);
					actionForward = mapping.findForward(BalConstants.FWD_ASENTAMIENTO_EDIT_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
					stringServicio = "getAsentamientoAdapterForUpdate(userSession, commonKey)";
					asentamientoAdapterVO = BalServiceLocator.getAsentamientoService().getAsentamientoAdapterForView
						(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_ASENTAMIENTO_EDIT_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
					stringServicio = "getAsentamientoAdapterForView(userSession, commonKey)";
					asentamientoAdapterVO = BalServiceLocator.getAsentamientoService().getAsentamientoAdapterForView
						(userSession, commonKey);
					asentamientoAdapterVO.addMessage(BaseError.MSG_ELIMINAR, BalError.ASENTAMIENTO_LABEL);
					actionForward = mapping.findForward(BalConstants.FWD_ASENTAMIENTO_VIEW_ADAPTER);					
				}
				
				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (asentamientoAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + asentamientoAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, AsentamientoAdapter.NAME, asentamientoAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				asentamientoAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + 
					AsentamientoAdapter.NAME + ": " + asentamientoAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(AsentamientoAdapter.NAME, asentamientoAdapterVO);
				// Subo el apdater al userSession
				userSession.put(AsentamientoAdapter.NAME, asentamientoAdapterVO);
				
				saveDemodaMessages(request, asentamientoAdapterVO);			
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, AsentamientoAdapter.NAME);
			}
		}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping, 
				BalSecurityConstants.ABM_ASENTAMIENTO, BaseSecurityConstants.AGREGAR);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				AsentamientoAdapter asentamientoAdapterVO = (AsentamientoAdapter) userSession.get(AsentamientoAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (asentamientoAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + AsentamientoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, AsentamientoAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(asentamientoAdapterVO, request);
				
	            // Tiene errores recuperables
				if (asentamientoAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + asentamientoAdapterVO.infoString()); 
					saveDemodaErrors(request, asentamientoAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, AsentamientoAdapter.NAME, asentamientoAdapterVO);
				}
				
				// llamada al servicio
				AsentamientoVO asentamientoVO = BalServiceLocator.getAsentamientoService().createAsentamiento(userSession, asentamientoAdapterVO.getAsentamiento());
				
	            // Tiene errores recuperables
				if (asentamientoVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + asentamientoVO.infoString()); 
					saveDemodaErrors(request, asentamientoVO);
					return forwardErrorRecoverable(mapping, request, userSession, AsentamientoAdapter.NAME, asentamientoAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (asentamientoVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + asentamientoVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, AsentamientoAdapter.NAME, asentamientoAdapterVO);
				}

				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, AsentamientoAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, AsentamientoAdapter.NAME);
			}
	}
	
	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				BalSecurityConstants.ABM_ASENTAMIENTO, BaseSecurityConstants.MODIFICAR);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				AsentamientoAdapter asentamientoAdapterVO = (AsentamientoAdapter) userSession.get(AsentamientoAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (asentamientoAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + AsentamientoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, AsentamientoAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(asentamientoAdapterVO, request);
				
	            // Tiene errores recuperables
				if (asentamientoAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + asentamientoAdapterVO.infoString()); 
					saveDemodaErrors(request, asentamientoAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, AsentamientoAdapter.NAME, asentamientoAdapterVO);
				}
				
				// llamada al servicio
				AsentamientoVO asentamientoVO = BalServiceLocator.getAsentamientoService().updateAsentamiento(userSession, asentamientoAdapterVO.getAsentamiento());
				
	            // Tiene errores recuperables
				if (asentamientoVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + asentamientoAdapterVO.infoString()); 
					saveDemodaErrors(request, asentamientoVO);
					return forwardErrorRecoverable(mapping, request, userSession, AsentamientoAdapter.NAME, asentamientoAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (asentamientoVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + asentamientoAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, AsentamientoAdapter.NAME, asentamientoAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, AsentamientoAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, AsentamientoAdapter.NAME);
			}
	}

	
	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_ASENTAMIENTO, 
				BaseSecurityConstants.ELIMINAR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				AsentamientoAdapter asentamientoAdapterVO = (AsentamientoAdapter) userSession.get(AsentamientoAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (asentamientoAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + AsentamientoAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, AsentamientoAdapter.NAME); 
				}

				// llamada al servicio
				AsentamientoVO asentamientoVO = BalServiceLocator.getAsentamientoService().deleteAsentamiento
					(userSession, asentamientoAdapterVO.getAsentamiento());
				
	            // Tiene errores recuperables
				if (asentamientoVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + asentamientoAdapterVO.infoString());
					saveDemodaErrors(request, asentamientoVO);				
					request.setAttribute(AsentamientoAdapter.NAME, asentamientoAdapterVO);
					return mapping.findForward(BalConstants.FWD_ASENTAMIENTO_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (asentamientoVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + asentamientoAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, AsentamientoAdapter.NAME, asentamientoAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, AsentamientoAdapter.NAME);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, AsentamientoAdapter.NAME);
			}
		}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, AsentamientoAdapter.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, AsentamientoAdapter.NAME);
			
	}
	

	public ActionForward paramFechaBalance(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				AsentamientoAdapter asentamientoAdapterVO = (AsentamientoAdapter) userSession.get(AsentamientoAdapter.NAME);
		
				// Si es nulo no se puede continuar
				if (asentamientoAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + AsentamientoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, AsentamientoAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(asentamientoAdapterVO, request);
				
	            // Tiene errores recuperables
				if (asentamientoAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + asentamientoAdapterVO.infoString()); 
					saveDemodaErrors(request, asentamientoAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, AsentamientoAdapter.NAME, asentamientoAdapterVO);
				}
				
				// Llamada al servicio
				asentamientoAdapterVO = BalServiceLocator.getAsentamientoService().getAsentamientoAdapterParamFechaBalance(userSession, asentamientoAdapterVO);
				
	            // Tiene errores recuperables
				if (asentamientoAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + asentamientoAdapterVO.infoString()); 
					saveDemodaErrors(request, asentamientoAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, AsentamientoAdapter.NAME, asentamientoAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (asentamientoAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + asentamientoAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, AsentamientoAdapter.NAME, asentamientoAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(AsentamientoAdapter.NAME, asentamientoAdapterVO);
				// Subo el adapter al userSession
				userSession.put(AsentamientoAdapter.NAME, asentamientoAdapterVO);
				
				return mapping.findForward(BalConstants.FWD_ASENTAMIENTO_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, AsentamientoAdapter.NAME);
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
			AsentamientoAdapter adapterVO = (AsentamientoAdapter)userSession.get(AsentamientoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (adapterVO == null) {
				log.error("error en: "  + funcName + ": " + AsentamientoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AsentamientoAdapter.NAME); 
			}
			
			// Populate como en un buscar
			DemodaUtil.populateVO(adapterVO, request);
			
			log.debug( funcName + " " + adapterVO.getAsentamiento().getCaso().infoString());
			
			// llamada al servicio
			CasServiceLocator.getCasCasoService().validarCaso(userSession, adapterVO.getAsentamiento()); 
			
			adapterVO.getAsentamiento().passErrorMessages(adapterVO);
		    
		    saveDemodaMessages(request, adapterVO);
		    saveDemodaErrors(request, adapterVO);
		    
			request.setAttribute(AsentamientoAdapter.NAME, adapterVO);
			
			return mapping.findForward( BalConstants.FWD_ASENTAMIENTO_EDIT_ADAPTER); 
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AsentamientoAdapter.NAME);
		}	
	}

}
