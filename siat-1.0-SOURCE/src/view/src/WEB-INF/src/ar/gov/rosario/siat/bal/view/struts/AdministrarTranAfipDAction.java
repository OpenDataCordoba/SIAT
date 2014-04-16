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

import ar.gov.rosario.siat.bal.iface.model.TranAfipAdapter;
import ar.gov.rosario.siat.bal.iface.model.TranAfipVO;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarTranAfipDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarTranAfipDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_TRANAFIP, act);
		
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		TranAfipAdapter tranAfipAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getTranAfipAdapterForView(userSession, commonKey)";
				tranAfipAdapterVO = BalServiceLocator.getEnvioOsirisService().getTranAfipAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_TRANAFIP_VIEW_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getRecursoAdapterForDelete(userSession, commonKey)";
				tranAfipAdapterVO = BalServiceLocator.getEnvioOsirisService().getTranAfipAdapterForView(userSession, commonKey);
				tranAfipAdapterVO.addMessage(BaseError.MSG_ELIMINAR, BalError.TRANAFIP_LABEL);
				tranAfipAdapterVO.addMessage(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS_WARN, BalError.DETALLEDJ_LABEL);
				tranAfipAdapterVO.addMessage(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS_WARN, BalError.DETALLEPAGO_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_TRANAFIP_VIEW_ADAPTER);					
			}
			if (navModel.getAct().equals(BalConstants.ACT_GENERAR_DECJUR)) {
				stringServicio = "getTranAfipAdapterForGenerarDecJur(userSession)";
				tranAfipAdapterVO = BalServiceLocator.getEnvioOsirisService().getTranAfipAdapterForView(userSession, commonKey);
				tranAfipAdapterVO.addMessage(BalError.TRANAFIP_MSG_GENERAR_DECJUR);
				actionForward = mapping.findForward(BalConstants.FWD_TRANAFIP_VIEW_ADAPTER);
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (tranAfipAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + tranAfipAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TranAfipAdapter.NAME, tranAfipAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			tranAfipAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + TranAfipAdapter.NAME + ": "+ tranAfipAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(TranAfipAdapter.NAME, tranAfipAdapterVO);
			// Subo el apdater al userSession
			userSession.put(TranAfipAdapter.NAME, tranAfipAdapterVO);
			 
			saveDemodaMessages(request, tranAfipAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TranAfipAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, TranAfipAdapter.NAME);
		
	}	
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, TranAfipAdapter.NAME);
			
	}
	
	public ActionForward verDetalleDJ(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_DETALLEDJ);			
	}	
	
	public ActionForward verDetallePago(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			
		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_DETALLEPAGO);			
	}
	
	public ActionForward generarDecJur(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				BalSecurityConstants.ABM_TRANAFIP, BalSecurityConstants.MTD_GENERARDECJUR);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				TranAfipAdapter tranAfipAdapterVO = (TranAfipAdapter) userSession.get(TranAfipAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (tranAfipAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + TranAfipAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, TranAfipAdapter.NAME); 
				}
				
				// llamada al servicio
				TranAfipVO tranAfipVO = BalServiceLocator.getEnvioOsirisService().generarDecJurForTranAfip(userSession, tranAfipAdapterVO.getTranAfip());
				
	            // Tiene errores recuperables
				if (tranAfipVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + tranAfipAdapterVO.infoString()); 
					saveDemodaErrors(request, tranAfipVO);
					return forwardErrorRecoverable(mapping, request, userSession, TranAfipAdapter.NAME, tranAfipAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (tranAfipVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + tranAfipAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, TranAfipAdapter.NAME, tranAfipAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, TranAfipAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, TranAfipAdapter.NAME);
			}
	}

	public ActionForward eliminarTranAfip(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_TRANAFIP, BaseSecurityConstants.ELIMINAR);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				TranAfipAdapter tranAfipAdapterVO = (TranAfipAdapter) userSession.get(TranAfipAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (tranAfipAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + TranAfipAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, TranAfipAdapter.NAME); 
				}
				
				// llamada al servicio
				TranAfipVO tranAfipVO = BalServiceLocator.getEnvioOsirisService().eliminarTranAfip(userSession, tranAfipAdapterVO.getTranAfip());
				
	            // Tiene errores recuperables
				if (tranAfipVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + tranAfipAdapterVO.infoString()); 
					saveDemodaErrors(request, tranAfipVO);
					return forwardErrorRecoverable(mapping, request, userSession, TranAfipAdapter.NAME, tranAfipAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (tranAfipVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + tranAfipAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, TranAfipAdapter.NAME, tranAfipAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, TranAfipAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, TranAfipAdapter.NAME);
			}
	}

	//Action Fowards -> DActions
	public ActionForward eliminarDetalleDJ(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardEliminarAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_DETALLEDJ);
		}
	
	public ActionForward eliminarDetallePago(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardEliminarAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_DETALLEPAGO);
		}
	
	
}
