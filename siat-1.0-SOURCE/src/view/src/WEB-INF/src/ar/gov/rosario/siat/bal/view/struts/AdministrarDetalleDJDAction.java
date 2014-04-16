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

import ar.gov.rosario.siat.bal.iface.model.DetalleDJAdapter;
import ar.gov.rosario.siat.bal.iface.model.DetalleDJVO;
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

public final class AdministrarDetalleDJDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarDetalleDJDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_DETALLEDJ, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		DetalleDJAdapter detalleDJAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getDetalleDJAdapterForView(userSession, commonKey)";
				detalleDJAdapterVO = BalServiceLocator.getEnvioOsirisService().getDetalleDJAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_DETALLEDJ_VIEW_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getRecursoAdapterForDelete(userSession, commonKey)";
				detalleDJAdapterVO = BalServiceLocator.getEnvioOsirisService().getDetalleDJAdapterForView(userSession, commonKey);
				detalleDJAdapterVO.addMessage(BaseError.MSG_ELIMINAR, BalError.DETALLEDJ_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_DETALLEDJ_VIEW_ADAPTER);					
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (detalleDJAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + detalleDJAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DetalleDJAdapter.NAME, detalleDJAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			detalleDJAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + DetalleDJAdapter.NAME + ": "+ detalleDJAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(DetalleDJAdapter.NAME, detalleDJAdapterVO);
			// Subo el apdater al userSession
			userSession.put(DetalleDJAdapter.NAME, detalleDJAdapterVO);
			 
			saveDemodaMessages(request, detalleDJAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DetalleDJAdapter.NAME);
		}
	}	
		
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, DetalleDJAdapter.NAME);
		
	}		
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, DetalleDJAdapter.NAME);
			
	}

	public ActionForward eliminarDetalleDJ(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_DETALLEDJ, BaseSecurityConstants.ELIMINAR);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				DetalleDJAdapter detalleDJAdapterVO = (DetalleDJAdapter) userSession.get(DetalleDJAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (detalleDJAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + DetalleDJAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, DetalleDJAdapter.NAME); 
				}
				
				// llamada al servicio
				DetalleDJVO detalleDJVO = BalServiceLocator.getEnvioOsirisService().eliminarDetalleDJ(userSession, detalleDJAdapterVO.getDetalleDJ());
				
	            // Tiene errores recuperables
				if (detalleDJVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + detalleDJAdapterVO.infoString()); 
					saveDemodaErrors(request, detalleDJVO);
					return forwardErrorRecoverable(mapping, request, userSession, DetalleDJAdapter.NAME, detalleDJAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (detalleDJVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + detalleDJAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, DetalleDJAdapter.NAME, detalleDJAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, DetalleDJAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, DetalleDJAdapter.NAME);
			}
	}
}
