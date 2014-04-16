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

import ar.gov.rosario.siat.bal.iface.model.DetallePagoAdapter;
import ar.gov.rosario.siat.bal.iface.model.DetallePagoVO;
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

public final class AdministrarDetallePagoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarDetallePagoDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_DETALLEPAGO, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		DetallePagoAdapter detallePagoAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getDetallePagoAdapterForView(userSession, commonKey)";
				detallePagoAdapterVO = BalServiceLocator.getEnvioOsirisService().getDetallePagoAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_DETALLEPAGO_VIEW_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getRecursoAdapterForDelete(userSession, commonKey)";
				detallePagoAdapterVO = BalServiceLocator.getEnvioOsirisService().getDetallePagoAdapterForView(userSession, commonKey);
				detallePagoAdapterVO.addMessage(BaseError.MSG_ELIMINAR, BalError.DETALLEPAGO_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_DETALLEPAGO_VIEW_ADAPTER);					
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (detallePagoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + detallePagoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DetallePagoAdapter.NAME, detallePagoAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			detallePagoAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + DetallePagoAdapter.NAME + ": "+ detallePagoAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(DetallePagoAdapter.NAME, detallePagoAdapterVO);
			// Subo el apdater al userSession
			userSession.put(DetallePagoAdapter.NAME, detallePagoAdapterVO);
			 
			saveDemodaMessages(request, detallePagoAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DetallePagoAdapter.NAME);
		}
	}	
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, DetallePagoAdapter.NAME);
		
	}
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, DetallePagoAdapter.NAME);
			
	}
		
	public ActionForward eliminarDetallePago(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_DETALLEPAGO, BaseSecurityConstants.ELIMINAR);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				DetallePagoAdapter detallePagoAdapterVO = (DetallePagoAdapter) userSession.get(DetallePagoAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (detallePagoAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + DetallePagoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, DetallePagoAdapter.NAME); 
				}
				
				// llamada al servicio
				DetallePagoVO detallePagoVO = BalServiceLocator.getEnvioOsirisService().eliminarDetallePago(userSession, detallePagoAdapterVO.getDetallePago());
				
	            // Tiene errores recuperables
				if (detallePagoVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + detallePagoAdapterVO.infoString()); 
					saveDemodaErrors(request, detallePagoVO);
					return forwardErrorRecoverable(mapping, request, userSession, DetallePagoAdapter.NAME, detallePagoAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (detallePagoVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + detallePagoAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, DetallePagoAdapter.NAME, detallePagoAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, DetallePagoAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, DetallePagoAdapter.NAME);
			}
	}
}
