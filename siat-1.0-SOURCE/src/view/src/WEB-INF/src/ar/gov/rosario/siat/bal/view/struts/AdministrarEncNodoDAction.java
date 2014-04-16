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

import ar.gov.rosario.siat.bal.iface.model.NodoAdapter;
import ar.gov.rosario.siat.bal.iface.model.NodoVO;
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

public class AdministrarEncNodoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEncNodoDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_NODO_ENC, act);		

			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			NodoAdapter nodoAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());

				if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
					stringServicio = "getNodoAdapterForUpdate(userSession, commonKey)";
					nodoAdapterVO = BalServiceLocator.getClasificacionService().getNodoAdapterForUpdate(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_NODO_ENC_EDIT_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
					stringServicio = "getNodoAdapterForCreate(userSession, commonKey)";
					
					String idClaStr = (String) userSession.get("idClasificador");			

					nodoAdapterVO = BalServiceLocator.getClasificacionService().getNodoAdapterForCreate(userSession, commonKey, idClaStr);
					actionForward = mapping.findForward(BalConstants.FWD_NODO_ENC_EDIT_ADAPTER);
				}
				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (nodoAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + nodoAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, NodoAdapter.ENC_NAME, nodoAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				nodoAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + NodoAdapter.ENC_NAME + ": "+ nodoAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(NodoAdapter.ENC_NAME, nodoAdapterVO);
				// Subo el apdater al userSession
				userSession.put(NodoAdapter.ENC_NAME, nodoAdapterVO);
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, NodoAdapter.ENC_NAME);
			}
		}
	

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping, 
				BalSecurityConstants.ABM_NODO_ENC, BaseSecurityConstants.AGREGAR);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				NodoAdapter nodoAdapterVO = (NodoAdapter) userSession.get(NodoAdapter.ENC_NAME);
				
				// Si es nulo no se puede continuar
				if (nodoAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + NodoAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, NodoAdapter.ENC_NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(nodoAdapterVO, request);
				
	            // Tiene errores recuperables
				if (nodoAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + nodoAdapterVO.infoString()); 
					saveDemodaErrors(request, nodoAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, NodoAdapter.ENC_NAME, nodoAdapterVO);
				}
				
				// llamada al servicio
				NodoVO nodoVO = BalServiceLocator.getClasificacionService().createNodo(userSession, nodoAdapterVO.getNodo());
				
	            // Tiene errores recuperables
				if (nodoVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + nodoVO.infoString()); 
					saveDemodaErrors(request, nodoVO);
					return forwardErrorRecoverable(mapping, request, userSession, NodoAdapter.ENC_NAME, nodoAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (nodoVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + nodoVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, NodoAdapter.ENC_NAME, nodoAdapterVO);
				}

				return forwardConfirmarOk(mapping, request, funcName, NodoAdapter.ENC_NAME);
									
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, NodoAdapter.ENC_NAME);
			}
	}
	
	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				BalSecurityConstants.ABM_NODO_ENC, BaseSecurityConstants.MODIFICAR);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				NodoAdapter nodoAdapterVO = (NodoAdapter) userSession.get(NodoAdapter.ENC_NAME);
				
				// Si es nulo no se puede continuar
				if (nodoAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + NodoAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, NodoAdapter.ENC_NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(nodoAdapterVO, request);
				
	            // Tiene errores recuperables
				if (nodoAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + nodoAdapterVO.infoString()); 
					saveDemodaErrors(request, nodoAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, NodoAdapter.ENC_NAME, nodoAdapterVO);
				}
				
				// llamada al servicio
				NodoVO nodoVO = BalServiceLocator.getClasificacionService().updateNodo(userSession, nodoAdapterVO.getNodo());
				
	            // Tiene errores recuperables
				if (nodoVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + nodoAdapterVO.infoString()); 
					saveDemodaErrors(request, nodoVO);
					return forwardErrorRecoverable(mapping, request, userSession, NodoAdapter.ENC_NAME, nodoAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (nodoVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + nodoAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, NodoAdapter.ENC_NAME, nodoAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, NodoAdapter.ENC_NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, NodoAdapter.ENC_NAME);
			}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, NodoAdapter.ENC_NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, NodoAdapter.ENC_NAME);
	}
	
}
