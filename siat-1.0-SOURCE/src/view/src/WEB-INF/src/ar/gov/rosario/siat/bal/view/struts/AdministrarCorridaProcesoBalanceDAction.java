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

import ar.gov.rosario.siat.bal.iface.model.BalanceVO;
import ar.gov.rosario.siat.bal.iface.model.CorridaProcesoBalanceAdapter;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

/**
 * Action que se Utiliza para Administrar las Corridas Online del Balance 
 * 
 */
public class AdministrarCorridaProcesoBalanceDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarCorridaProcesoBalanceDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = getCurrentUserSession(request, mapping);		
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			CorridaProcesoBalanceAdapter corridaProcesoBalanceAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey procesoBalanceKey = new CommonKey(userSession.getNavModel().getSelectedId());
											
				if (navModel.getAct().equals(BalConstants.ACT_REINICIAR)) {
					stringServicio = "getCorridaProcesoBalanceAdapterForView(userSession, commonKey)";
					corridaProcesoBalanceAdapterVO = BalServiceLocator.getBalanceService().getCorridaProcesoBalanceAdapterForView(userSession, procesoBalanceKey); 
					
					actionForward = mapping.findForward(BalConstants.FWD_CORRIDA_PRO_BAL_VIEW_ADAPTER);					
				}
				if (navModel.getAct().equals(BalConstants.ACT_RETROCEDER)) {
					stringServicio = "getCorridaProcesoBalanceAdapterForView(userSession, commonKey)";
					corridaProcesoBalanceAdapterVO = BalServiceLocator.getBalanceService().getCorridaProcesoBalanceAdapterForView(userSession, procesoBalanceKey); 
					
					actionForward = mapping.findForward(BalConstants.FWD_CORRIDA_PRO_BAL_VIEW_ADAPTER);					
				}


				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (corridaProcesoBalanceAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + corridaProcesoBalanceAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, CorridaProcesoBalanceAdapter.NAME, corridaProcesoBalanceAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				corridaProcesoBalanceAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + 
					CorridaProcesoBalanceAdapter.NAME + ": " + corridaProcesoBalanceAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(CorridaProcesoBalanceAdapter.NAME, corridaProcesoBalanceAdapterVO);
				// Subo el apdater al userSession
				userSession.put(CorridaProcesoBalanceAdapter.NAME, corridaProcesoBalanceAdapterVO);
				
				saveDemodaMessages(request, corridaProcesoBalanceAdapterVO);			
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, CorridaProcesoBalanceAdapter.NAME);
			}
	}
	
	public ActionForward reiniciar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CorridaProcesoBalanceAdapter corridaProcesoBalanceAdapterVO = (CorridaProcesoBalanceAdapter) userSession.get(CorridaProcesoBalanceAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (corridaProcesoBalanceAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CorridaProcesoBalanceAdapter.NAME + 
				" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CorridaProcesoBalanceAdapter.NAME); 
			}
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(corridaProcesoBalanceAdapterVO, request);
			
			// Tiene errores recuperables
			if (corridaProcesoBalanceAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + corridaProcesoBalanceAdapterVO.infoString()); 
				saveDemodaErrors(request, corridaProcesoBalanceAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CorridaProcesoBalanceAdapter.NAME, corridaProcesoBalanceAdapterVO);
			}
			
			// llamada al servicio
			BalanceVO balanceVO = BalServiceLocator.getBalanceService().reiniciar(userSession, corridaProcesoBalanceAdapterVO.getBalance());
			
			// Tiene errores recuperables
			if (balanceVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + corridaProcesoBalanceAdapterVO.infoString());
				saveDemodaErrors(request, balanceVO);				
				request.setAttribute(CorridaProcesoBalanceAdapter.NAME, corridaProcesoBalanceAdapterVO);
				return mapping.findForward(BalConstants.FWD_CORRIDA_PRO_BAL_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (balanceVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + corridaProcesoBalanceAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CorridaProcesoBalanceAdapter.NAME, corridaProcesoBalanceAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CorridaProcesoBalanceAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CorridaProcesoBalanceAdapter.NAME);
		}	
	}
	
	public ActionForward retroceder(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CorridaProcesoBalanceAdapter corridaProcesoBalanceAdapterVO = (CorridaProcesoBalanceAdapter) userSession.get(CorridaProcesoBalanceAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (corridaProcesoBalanceAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CorridaProcesoBalanceAdapter.NAME + 
				" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CorridaProcesoBalanceAdapter.NAME); 
			}
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(corridaProcesoBalanceAdapterVO, request);
			
			// Tiene errores recuperables
			if (corridaProcesoBalanceAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + corridaProcesoBalanceAdapterVO.infoString()); 
				saveDemodaErrors(request, corridaProcesoBalanceAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CorridaProcesoBalanceAdapter.NAME, corridaProcesoBalanceAdapterVO);
			}
			
			// llamada al servicio
			BalanceVO balanceVO = BalServiceLocator.getBalanceService().retrocederPaso(userSession, corridaProcesoBalanceAdapterVO.getBalance());
			
			// Tiene errores recuperables
			if (balanceVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + corridaProcesoBalanceAdapterVO.infoString());
				saveDemodaErrors(request, balanceVO);				
				request.setAttribute(CorridaProcesoBalanceAdapter.NAME, corridaProcesoBalanceAdapterVO);
				return mapping.findForward(BalConstants.FWD_CORRIDA_PRO_BAL_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (balanceVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + corridaProcesoBalanceAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CorridaProcesoBalanceAdapter.NAME, corridaProcesoBalanceAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CorridaProcesoBalanceAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CorridaProcesoBalanceAdapter.NAME);
		}	
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		
		return baseVolver(mapping, form, request, response, CorridaProcesoBalanceAdapter.NAME);
	}
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, CorridaProcesoBalanceAdapter.NAME);		
	}

}
