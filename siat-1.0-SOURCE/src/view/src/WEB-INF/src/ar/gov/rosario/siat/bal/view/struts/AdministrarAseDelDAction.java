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

import ar.gov.rosario.siat.bal.iface.model.AseDelAdapter;
import ar.gov.rosario.siat.bal.iface.model.AseDelVO;
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

public class AdministrarAseDelDAction  extends BaseDispatchAction{

	private Log log = LogFactory.getLog(AdministrarAseDelDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_ASEDEL, act);		
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			AseDelAdapter aseDelAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
					stringServicio = "getAseDelAdapterForView(userSession, commonKey)";
					aseDelAdapterVO = BalServiceLocator.getDelegadorService().getAseDelAdapterForView
						(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_ASEDEL_VIEW_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
					stringServicio = "getAseDelAdapterForCreate(userSession, commonKey)";
					aseDelAdapterVO = BalServiceLocator.getDelegadorService().getAseDelAdapterForCreate
						(userSession);
					actionForward = mapping.findForward(BalConstants.FWD_ASEDEL_EDIT_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
					stringServicio = "getAseDelAdapterForUpdate(userSession, commonKey)";
					aseDelAdapterVO = BalServiceLocator.getDelegadorService().getAseDelAdapterForView
						(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_ASEDEL_EDIT_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
					stringServicio = "getAseDelAdapterForView(userSession, commonKey)";
					aseDelAdapterVO = BalServiceLocator.getDelegadorService().getAseDelAdapterForView
						(userSession, commonKey);
					aseDelAdapterVO.addMessage(BaseError.MSG_ELIMINAR, BalError.ASEDEL_LABEL);
					actionForward = mapping.findForward(BalConstants.FWD_ASEDEL_VIEW_ADAPTER);					
				}
				
				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (aseDelAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + aseDelAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, AseDelAdapter.NAME, aseDelAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				aseDelAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + 
					AseDelAdapter.NAME + ": " + aseDelAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(AseDelAdapter.NAME, aseDelAdapterVO);
				// Subo el apdater al userSession
				userSession.put(AseDelAdapter.NAME, aseDelAdapterVO);
				
				saveDemodaMessages(request, aseDelAdapterVO);			
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, AseDelAdapter.NAME);
			}
		}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping, 
				BalSecurityConstants.ABM_ASEDEL, BaseSecurityConstants.AGREGAR);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				AseDelAdapter aseDelAdapterVO = (AseDelAdapter) userSession.get(AseDelAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (aseDelAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + AseDelAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, AseDelAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(aseDelAdapterVO, request);
				
	            // Tiene errores recuperables
				if (aseDelAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + aseDelAdapterVO.infoString()); 
					saveDemodaErrors(request, aseDelAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, AseDelAdapter.NAME, aseDelAdapterVO);
				}
				
				// llamada al servicio
				AseDelVO aseDelVO = BalServiceLocator.getDelegadorService().createAseDel(userSession, aseDelAdapterVO.getAseDel());
				
	            // Tiene errores recuperables
				if (aseDelVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + aseDelVO.infoString()); 
					saveDemodaErrors(request, aseDelVO);
					return forwardErrorRecoverable(mapping, request, userSession, AseDelAdapter.NAME, aseDelAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (aseDelVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + aseDelVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, AseDelAdapter.NAME, aseDelAdapterVO);
				}

				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, AseDelAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, AseDelAdapter.NAME);
			}
	}
	
	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				BalSecurityConstants.ABM_ASEDEL, BaseSecurityConstants.MODIFICAR);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				AseDelAdapter aseDelAdapterVO = (AseDelAdapter) userSession.get(AseDelAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (aseDelAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + AseDelAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, AseDelAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(aseDelAdapterVO, request);
				
	            // Tiene errores recuperables
				if (aseDelAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + aseDelAdapterVO.infoString()); 
					saveDemodaErrors(request, aseDelAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, AseDelAdapter.NAME, aseDelAdapterVO);
				}
				
				// llamada al servicio
				AseDelVO aseDelVO = BalServiceLocator.getDelegadorService().updateAseDel(userSession, aseDelAdapterVO.getAseDel());
				
	            // Tiene errores recuperables
				if (aseDelVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + aseDelAdapterVO.infoString()); 
					saveDemodaErrors(request, aseDelVO);
					return forwardErrorRecoverable(mapping, request, userSession, AseDelAdapter.NAME, aseDelAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (aseDelVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + aseDelAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, AseDelAdapter.NAME, aseDelAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, AseDelAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, AseDelAdapter.NAME);
			}
	}

	
	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_ASEDEL, 
				BaseSecurityConstants.ELIMINAR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				AseDelAdapter aseDelAdapterVO = (AseDelAdapter) userSession.get(AseDelAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (aseDelAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + AseDelAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, AseDelAdapter.NAME); 
				}

				// llamada al servicio
				AseDelVO aseDelVO = BalServiceLocator.getDelegadorService().deleteAseDel
					(userSession, aseDelAdapterVO.getAseDel());
				
	            // Tiene errores recuperables
				if (aseDelVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + aseDelAdapterVO.infoString());
					saveDemodaErrors(request, aseDelVO);				
					request.setAttribute(AseDelAdapter.NAME, aseDelAdapterVO);
					return mapping.findForward(BalConstants.FWD_ASEDEL_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (aseDelVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + aseDelAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, AseDelAdapter.NAME, aseDelAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, AseDelAdapter.NAME);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, AseDelAdapter.NAME);
			}
		}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, AseDelAdapter.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, AseDelAdapter.NAME);
			
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
				AseDelAdapter aseDelAdapterVO = (AseDelAdapter) userSession.get(AseDelAdapter.NAME);
		
				// Si es nulo no se puede continuar
				if (aseDelAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + AseDelAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, AseDelAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(aseDelAdapterVO, request);
				
	            // Tiene errores recuperables
				if (aseDelAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + aseDelAdapterVO.infoString()); 
					saveDemodaErrors(request, aseDelAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, AseDelAdapter.NAME, aseDelAdapterVO);
				}
				
				// Llamada al servicio
				aseDelAdapterVO = BalServiceLocator.getDelegadorService().getAseDelAdapterParamFechaBalance(userSession, aseDelAdapterVO);
				
	            // Tiene errores recuperables
				if (aseDelAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + aseDelAdapterVO.infoString()); 
					saveDemodaErrors(request, aseDelAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, AseDelAdapter.NAME, aseDelAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (aseDelAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + aseDelAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, AseDelAdapter.NAME, aseDelAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(AseDelAdapter.NAME, aseDelAdapterVO);
				// Subo el adapter al userSession
				userSession.put(AseDelAdapter.NAME, aseDelAdapterVO);
				
				return mapping.findForward(BalConstants.FWD_ASEDEL_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, AseDelAdapter.NAME);
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
			AseDelAdapter adapterVO = (AseDelAdapter)userSession.get(AseDelAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (adapterVO == null) {
				log.error("error en: "  + funcName + ": " + AseDelAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AseDelAdapter.NAME); 
			}
			
			// Populate como en un buscar
			DemodaUtil.populateVO(adapterVO, request);
			
			log.debug( funcName + " " + adapterVO.getAseDel().getCaso().infoString());
			
			// llamada al servicio
			CasServiceLocator.getCasCasoService().validarCaso(userSession, adapterVO.getAseDel()); 
			
			adapterVO.getAseDel().passErrorMessages(adapterVO);
		    
		    saveDemodaMessages(request, adapterVO);
		    saveDemodaErrors(request, adapterVO);
		    
			request.setAttribute(AseDelAdapter.NAME, adapterVO);
			
			return mapping.findForward( BalConstants.FWD_ASEDEL_EDIT_ADAPTER); 
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AseDelAdapter.NAME);
		}	
	}


}
