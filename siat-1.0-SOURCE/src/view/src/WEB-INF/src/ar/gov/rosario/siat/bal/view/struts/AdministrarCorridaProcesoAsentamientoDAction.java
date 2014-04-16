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

import ar.gov.rosario.siat.bal.iface.model.AsentamientoVO;
import ar.gov.rosario.siat.bal.iface.model.CorridaProcesoAsentamientoAdapter;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.pro.iface.model.CorridaVO;
import ar.gov.rosario.siat.pro.iface.service.ProServiceLocator;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

/**
 * Action que se Utiliza para Administrar las Corridas Online del Asentamiento 
 * 
 */
public class AdministrarCorridaProcesoAsentamientoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarCorridaProcesoAsentamientoDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = getCurrentUserSession(request, mapping);		
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			CorridaProcesoAsentamientoAdapter corridaProcesoAsentamientoAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey procesoAsentamientoKey = new CommonKey(userSession.getNavModel().getSelectedId());
											
				if (navModel.getAct().equals(BalConstants.ACT_REINICIAR)) {
					stringServicio = "getCorridaProcesoAsentamientoAdapterForView(userSession, commonKey)";
					corridaProcesoAsentamientoAdapterVO = BalServiceLocator.getAsentamientoService().getCorridaProcesoAsentamientoAdapterForView(userSession, procesoAsentamientoKey); 
					
					actionForward = mapping.findForward(BalConstants.FWD_CORRIDA_PRO_ASE_VIEW_ADAPTER);					
				}
				if (navModel.getAct().equals(BalConstants.ACT_FORZAR)) {
					stringServicio = "getCorridaProcesoAsentamientoAdapterForView(userSession, commonKey)";
					corridaProcesoAsentamientoAdapterVO = BalServiceLocator.getAsentamientoService().getCorridaProcesoAsentamientoAdapterForView(userSession, procesoAsentamientoKey); 
					
					actionForward = mapping.findForward(BalConstants.FWD_CORRIDA_PRO_ASE_VIEW_ADAPTER);					
				}

				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (corridaProcesoAsentamientoAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + corridaProcesoAsentamientoAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, CorridaProcesoAsentamientoAdapter.NAME, corridaProcesoAsentamientoAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				corridaProcesoAsentamientoAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + 
					CorridaProcesoAsentamientoAdapter.NAME + ": " + corridaProcesoAsentamientoAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(CorridaProcesoAsentamientoAdapter.NAME, corridaProcesoAsentamientoAdapterVO);
				// Subo el apdater al userSession
				userSession.put(CorridaProcesoAsentamientoAdapter.NAME, corridaProcesoAsentamientoAdapterVO);
				
				saveDemodaMessages(request, corridaProcesoAsentamientoAdapterVO);			
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, CorridaProcesoAsentamientoAdapter.NAME);
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
			CorridaProcesoAsentamientoAdapter corridaProcesoAsentamientoAdapterVO = (CorridaProcesoAsentamientoAdapter) userSession.get(CorridaProcesoAsentamientoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (corridaProcesoAsentamientoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CorridaProcesoAsentamientoAdapter.NAME + 
				" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CorridaProcesoAsentamientoAdapter.NAME); 
			}
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(corridaProcesoAsentamientoAdapterVO, request);
			
			// Tiene errores recuperables
			if (corridaProcesoAsentamientoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + corridaProcesoAsentamientoAdapterVO.infoString()); 
				saveDemodaErrors(request, corridaProcesoAsentamientoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CorridaProcesoAsentamientoAdapter.NAME, corridaProcesoAsentamientoAdapterVO);
			}
			
			// llamada al servicio
			AsentamientoVO asentamientoVO = BalServiceLocator.getAsentamientoService().reiniciar(userSession, corridaProcesoAsentamientoAdapterVO.getAsentamiento());
			
			// Tiene errores recuperables
			if (asentamientoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + corridaProcesoAsentamientoAdapterVO.infoString());
				saveDemodaErrors(request, asentamientoVO);				
				request.setAttribute(CorridaProcesoAsentamientoAdapter.NAME, corridaProcesoAsentamientoAdapterVO);
				return mapping.findForward(BalConstants.FWD_CORRIDA_PRO_ASE_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (asentamientoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + corridaProcesoAsentamientoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CorridaProcesoAsentamientoAdapter.NAME, corridaProcesoAsentamientoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CorridaProcesoAsentamientoAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CorridaProcesoAsentamientoAdapter.NAME);
		}	
	}
	
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		
		return baseVolver(mapping, form, request, response, CorridaProcesoAsentamientoAdapter.NAME);
	}
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, CorridaProcesoAsentamientoAdapter.NAME);		
	}

	public ActionForward forzar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CorridaProcesoAsentamientoAdapter corridaProcesoAsentamientoAdapterVO = (CorridaProcesoAsentamientoAdapter) userSession.get(CorridaProcesoAsentamientoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (corridaProcesoAsentamientoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CorridaProcesoAsentamientoAdapter.NAME + 
				" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CorridaProcesoAsentamientoAdapter.NAME); 
			}
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(corridaProcesoAsentamientoAdapterVO, request);
			
			// Tiene errores recuperables
			if (corridaProcesoAsentamientoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + corridaProcesoAsentamientoAdapterVO.infoString()); 
				saveDemodaErrors(request, corridaProcesoAsentamientoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CorridaProcesoAsentamientoAdapter.NAME, corridaProcesoAsentamientoAdapterVO);
			}
			
			// llamada al servicio
			AsentamientoVO asentamientoVO = BalServiceLocator.getAsentamientoService().forzar(userSession, corridaProcesoAsentamientoAdapterVO.getAsentamiento());
			
			// 	llamada al servicio
			CorridaVO corridaVO = ProServiceLocator.getAdpProcesoService().activarProceso(userSession, corridaProcesoAsentamientoAdapterVO.getAsentamiento().getCorrida());
			corridaVO.passErrorMessages(asentamientoVO);
			
			// Tiene errores recuperables
			if (asentamientoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + corridaProcesoAsentamientoAdapterVO.infoString());
				saveDemodaErrors(request, asentamientoVO);				
				request.setAttribute(CorridaProcesoAsentamientoAdapter.NAME, corridaProcesoAsentamientoAdapterVO);
				return mapping.findForward(BalConstants.FWD_CORRIDA_PRO_ASE_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (asentamientoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + corridaProcesoAsentamientoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CorridaProcesoAsentamientoAdapter.NAME, corridaProcesoAsentamientoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CorridaProcesoAsentamientoAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CorridaProcesoAsentamientoAdapter.NAME);
		}	
	}
	
}
