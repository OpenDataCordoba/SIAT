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

import ar.gov.rosario.siat.bal.iface.model.ReingresoAdapter;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class AdministrarReingresoDAction extends BaseDispatchAction {


	private Log log = LogFactory.getLog(AdministrarReingresoDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_REINGRESO, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ReingresoAdapter reingresoAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getReingresoAdapterForView(userSession, commonKey)";
				reingresoAdapterVO = BalServiceLocator.getBalanceService().getReingresoAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_REINGRESO_VIEW_ADAPTER);
			}
			if (act.equals(BalConstants.ACT_EXCLUIR)) {
				stringServicio = "getReingresoAdapterForExcluir(userSession, commonKey)";
				reingresoAdapterVO = BalServiceLocator.getBalanceService().getReingresoAdapterForExcluir(userSession, commonKey);				
				//reingresoAdapterVO.addMessage(BaseError.MSG_ELIMINAR, BalError.REINGRESO_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_REINGRESO_EDIT_ADAPTER);				
			}
			if (act.equals(BalConstants.ACT_INCLUIR)) {
				stringServicio = "getReingresoAdapterForIncluir(userSession)";
				reingresoAdapterVO = BalServiceLocator.getBalanceService().getReingresoAdapterForIncluir(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_REINGRESO_EDIT_ADAPTER);				
			}
			/*if (act.equals(BaseConstants.ACT_LISTAR)) {
				stringServicio = "getReingresoAdapterForView(userSession, commonKey)";
				reingresoAdapterVO = BalServiceLocator.getBalanceService().getReingresoAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_REINGRESO_VIEW_ADAPTER);
			}*/
			
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (reingresoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + reingresoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ReingresoAdapter.NAME, reingresoAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			reingresoAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ReingresoAdapter.NAME + ": "+ reingresoAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ReingresoAdapter.NAME, reingresoAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ReingresoAdapter.NAME, reingresoAdapterVO);
			 
			saveDemodaMessages(request, reingresoAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ReingresoAdapter.NAME);
		}
	}
	

	/*public ActionForward incluirReingreso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_REINGRESO, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ReingresoAdapter reingresoAdapterVO = (ReingresoAdapter) userSession.get(ReingresoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (reingresoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ReingresoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ReingresoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(reingresoAdapterVO, request);
			
            // Tiene errores recuperables
			if (reingresoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + reingresoAdapterVO.infoString()); 
				saveDemodaErrors(request, reingresoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ReingresoAdapter.NAME, reingresoAdapterVO);
			}
			
			// llamada al servicio
			ReingresoVO reingresoVO = BalServiceLocator.getBalanceService().incluirReingreso(userSession, reingresoAdapterVO.getReingreso());
			
            // Tiene errores recuperables
			if (reingresoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + reingresoVO.infoString()); 
				saveDemodaErrors(request, reingresoVO);
				return forwardErrorRecoverable(mapping, request, userSession, ReingresoAdapter.NAME, reingresoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (reingresoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + reingresoVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ReingresoAdapter.NAME, reingresoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ReingresoAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ReingresoAdapter.NAME);
		}
	}

	public ActionForward excluirReingreso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_REINGRESO, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ReingresoAdapter reingresoAdapterVO = (ReingresoAdapter) userSession.get(ReingresoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (reingresoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ReingresoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ReingresoAdapter.NAME); 
			}

			// llamada al servicio
			ReingresoVO reingresoVO = BalServiceLocator.getBalanceService().excluirReingreso
				(userSession, reingresoAdapterVO.getReingreso());
			
            // Tiene errores recuperables
			if (reingresoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + reingresoAdapterVO.infoString());
				saveDemodaErrors(request, reingresoVO);				
				request.setAttribute(ReingresoAdapter.NAME, reingresoAdapterVO);
				return mapping.findForward(BalConstants.FWD_REINGRESO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (reingresoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + reingresoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ReingresoAdapter.NAME, reingresoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ReingresoAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ReingresoAdapter.NAME);
		}
	}
	*/

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ReingresoAdapter.NAME);
	}
	
	public ActionForward incluir(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_REINGRESO, BaseSecurityConstants.INCLUIR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ReingresoAdapter reingresoAdapterVO = (ReingresoAdapter) userSession.get(ReingresoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (reingresoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ReingresoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ReingresoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(reingresoAdapterVO, request);
			
            // Tiene errores recuperables
			if (reingresoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + reingresoAdapterVO.infoString()); 
				saveDemodaErrors(request, reingresoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ReingresoAdapter.NAME, reingresoAdapterVO);
			}
			
			// llamada al servicio
			reingresoAdapterVO = BalServiceLocator.getBalanceService().incluirReingreso(userSession, reingresoAdapterVO);
			
            // Tiene errores recuperables
			if (reingresoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + reingresoAdapterVO.infoString()); 
				saveDemodaErrors(request, reingresoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ReingresoAdapter.NAME, reingresoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (reingresoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + reingresoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ReingresoAdapter.NAME, reingresoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ReingresoAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ReingresoAdapter.NAME);
		}
	}
	
	public ActionForward excluir(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_REINGRESO, BaseSecurityConstants.EXCLUIR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ReingresoAdapter reingresoAdapterVO = (ReingresoAdapter) userSession.get(ReingresoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (reingresoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ReingresoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ReingresoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(reingresoAdapterVO, request);
			
            // Tiene errores recuperables
			if (reingresoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + reingresoAdapterVO.infoString()); 
				saveDemodaErrors(request, reingresoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ReingresoAdapter.NAME, reingresoAdapterVO);
			}
			
			// llamada al servicio
			reingresoAdapterVO = BalServiceLocator.getBalanceService().excluirReingreso(userSession, reingresoAdapterVO);
			
            // Tiene errores recuperables
			if (reingresoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + reingresoAdapterVO.infoString()); 
				saveDemodaErrors(request, reingresoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ReingresoAdapter.NAME, reingresoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (reingresoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + reingresoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ReingresoAdapter.NAME, reingresoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ReingresoAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ReingresoAdapter.NAME);
		}
	}

	public ActionForward paramActualizar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				ReingresoAdapter reingresoAdapterVO = (ReingresoAdapter) userSession.get(ReingresoAdapter.NAME);
		
				// Si es nulo no se puede continuar
				if (reingresoAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + ReingresoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ReingresoAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(reingresoAdapterVO, request);
				
	            // Tiene errores recuperables
				if (reingresoAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + reingresoAdapterVO.infoString()); 
					saveDemodaErrors(request, reingresoAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, ReingresoAdapter.NAME, reingresoAdapterVO);
				}
				
				// Llamada al servicio
				reingresoAdapterVO = BalServiceLocator.getBalanceService().getReingresoAdapterParamActualizar(userSession, reingresoAdapterVO);
				
	            // Tiene errores recuperables
				if (reingresoAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + reingresoAdapterVO.infoString()); 
					saveDemodaErrors(request, reingresoAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, ReingresoAdapter.NAME, reingresoAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (reingresoAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + reingresoAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, ReingresoAdapter.NAME, reingresoAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(ReingresoAdapter.NAME, reingresoAdapterVO);
				// Subo el adapter al userSession
				userSession.put(ReingresoAdapter.NAME, reingresoAdapterVO);
				
				return mapping.findForward(BalConstants.FWD_REINGRESO_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ReingresoAdapter.NAME);
			}
	}
}
