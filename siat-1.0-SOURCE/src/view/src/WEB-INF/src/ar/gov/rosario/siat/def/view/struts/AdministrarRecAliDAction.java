//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.view.struts;

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
import ar.gov.rosario.siat.def.iface.model.RecAliAdapter;
import ar.gov.rosario.siat.def.iface.model.RecAliVO;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarRecAliDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarRecAliDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_RECALI, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		RecAliAdapter recAliAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getRecAliAdapterForView(userSession, commonKey)";
				recAliAdapterVO = DefServiceLocator.getGravamenService().getRecAliAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_RECALI_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getRecAliAdapterForUpdate(userSession, commonKey)";
				recAliAdapterVO = DefServiceLocator.getGravamenService().getRecAliAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_RECALI_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getRecAliAdapterForView(userSession, commonKey)";
				recAliAdapterVO = DefServiceLocator.getGravamenService().getRecAliAdapterForView(userSession, commonKey);				
				recAliAdapterVO.addMessage(BaseError.MSG_ELIMINAR, DefError.RECALI_LABEL);
				actionForward = mapping.findForward(DefConstants.FWD_RECALI_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getRecAliAdapterForCreate(userSession)";
				recAliAdapterVO = DefServiceLocator.getGravamenService().getRecAliAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_RECALI_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getRecAliAdapterForView(userSession)";
				recAliAdapterVO = DefServiceLocator.getGravamenService().getRecAliAdapterForView(userSession, commonKey);
				recAliAdapterVO.addMessage(BaseError.MSG_ACTIVAR, DefError.RECALI_LABEL);
				actionForward = mapping.findForward(DefConstants.FWD_RECALI_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getRecAliAdapterForView(userSession)";
				recAliAdapterVO = DefServiceLocator.getGravamenService().getRecAliAdapterForView(userSession, commonKey);
				recAliAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, DefError.RECALI_LABEL);
				actionForward = mapping.findForward(DefConstants.FWD_RECALI_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (recAliAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + recAliAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, RecAliAdapter.NAME, recAliAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			recAliAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + RecAliAdapter.NAME + ": "+ recAliAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(RecAliAdapter.NAME, recAliAdapterVO);
			// Subo el apdater al userSession
			userSession.put(RecAliAdapter.NAME, recAliAdapterVO);
			 
			saveDemodaMessages(request, recAliAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, RecAliAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_RECALI, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			RecAliAdapter recAliAdapterVO = (RecAliAdapter) userSession.get(RecAliAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (recAliAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + RecAliAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, RecAliAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(recAliAdapterVO, request);
			
            // Tiene errores recuperables
			if (recAliAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + recAliAdapterVO.infoString()); 
				saveDemodaErrors(request, recAliAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, RecAliAdapter.NAME, recAliAdapterVO);
			}
			
			// llamada al servicio
			RecAliVO recAliVO = DefServiceLocator.getGravamenService().createRecAli(userSession, recAliAdapterVO.getRecAli());
			
            // Tiene errores recuperables
			if (recAliVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + recAliVO.infoString()); 
				saveDemodaErrors(request, recAliVO);
				return forwardErrorRecoverable(mapping, request, userSession, RecAliAdapter.NAME, recAliAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (recAliVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + recAliVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, RecAliAdapter.NAME, recAliAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, RecAliAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, RecAliAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_RECALI, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			RecAliAdapter recAliAdapterVO = (RecAliAdapter) userSession.get(RecAliAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (recAliAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + RecAliAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, RecAliAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(recAliAdapterVO, request);
			
            // Tiene errores recuperables
			if (recAliAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + recAliAdapterVO.infoString()); 
				saveDemodaErrors(request, recAliAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, RecAliAdapter.NAME, recAliAdapterVO);
			}
			
			// llamada al servicio
			RecAliVO recAliVO = DefServiceLocator.getGravamenService().updateRecAli(userSession, recAliAdapterVO.getRecAli());
			
            // Tiene errores recuperables
			if (recAliVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + recAliAdapterVO.infoString()); 
				saveDemodaErrors(request, recAliVO);
				return forwardErrorRecoverable(mapping, request, userSession, RecAliAdapter.NAME, recAliAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (recAliVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + recAliAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, RecAliAdapter.NAME, recAliAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, RecAliAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, RecAliAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_RECALI, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			RecAliAdapter recAliAdapterVO = (RecAliAdapter) userSession.get(RecAliAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (recAliAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + RecAliAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, RecAliAdapter.NAME); 
			}

			// llamada al servicio
			RecAliVO recAliVO = DefServiceLocator.getGravamenService().deleteRecAli
				(userSession, recAliAdapterVO.getRecAli());
			
            // Tiene errores recuperables
			if (recAliVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + recAliAdapterVO.infoString());
				saveDemodaErrors(request, recAliVO);				
				request.setAttribute(RecAliAdapter.NAME, recAliAdapterVO);
				return mapping.findForward(DefConstants.FWD_RECALI_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (recAliVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + recAliAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, RecAliAdapter.NAME, recAliAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, RecAliAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, RecAliAdapter.NAME);
		}
	}
	
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, RecAliAdapter.NAME);
		
	}

	public ActionForward paramTipoAlicuota (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				RecAliAdapter recAliAdapterVO = (RecAliAdapter) userSession.get(RecAliAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (recAliAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RecAliAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RecAliAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(recAliAdapterVO, request);
				
	            // Tiene errores recuperables
				if (recAliAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recAliAdapterVO.infoString()); 
					saveDemodaErrors(request, recAliAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, RecAliAdapter.NAME, recAliAdapterVO);
				}
				
				// llamada al servicio
				recAliAdapterVO = DefServiceLocator.getGravamenService().getRecAliAdapterParamTipoAlicuota(userSession, recAliAdapterVO);
				
	            // Tiene errores recuperables
				if (recAliAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recAliAdapterVO.infoString()); 
					saveDemodaErrors(request, recAliAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, RecAliAdapter.NAME, recAliAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (recAliAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + recAliAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, RecAliAdapter.NAME, recAliAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(RecAliAdapter.NAME, recAliAdapterVO);
				// Subo el apdater al userSession
				userSession.put(RecAliAdapter.NAME, recAliAdapterVO);
				
				return mapping.findForward(DefConstants.FWD_RECALI_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RecAliAdapter.NAME);
			}
		}

}
