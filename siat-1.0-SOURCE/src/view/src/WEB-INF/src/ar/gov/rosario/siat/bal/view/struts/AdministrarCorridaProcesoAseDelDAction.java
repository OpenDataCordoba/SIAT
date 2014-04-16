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

import ar.gov.rosario.siat.bal.iface.model.AseDelVO;
import ar.gov.rosario.siat.bal.iface.model.CorridaProcesoAseDelAdapter;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

/**
 * Action que se Utiliza para Administrar las Corridas Online del Asentamiento Delegado
 * 
 */
public class AdministrarCorridaProcesoAseDelDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarCorridaProcesoAseDelDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = getCurrentUserSession(request, mapping);		
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			CorridaProcesoAseDelAdapter corridaProcesoAseDelAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey procesoAseDelKey = new CommonKey(userSession.getNavModel().getSelectedId());
											
				if (navModel.getAct().equals(BalConstants.ACT_REINICIAR)) {
					stringServicio = "getCorridaProcesoAseDelAdapterForView(userSession, commonKey)";
					corridaProcesoAseDelAdapterVO = BalServiceLocator.getDelegadorService().getCorridaProcesoAseDelAdapterForView(userSession, procesoAseDelKey); 
					
					actionForward = mapping.findForward(BalConstants.FWD_CORRIDA_PRO_ASE_DEL_VIEW_ADAPTER);					
				}

				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (corridaProcesoAseDelAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + corridaProcesoAseDelAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, CorridaProcesoAseDelAdapter.NAME, corridaProcesoAseDelAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				corridaProcesoAseDelAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + 
					CorridaProcesoAseDelAdapter.NAME + ": " + corridaProcesoAseDelAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(CorridaProcesoAseDelAdapter.NAME, corridaProcesoAseDelAdapterVO);
				// Subo el apdater al userSession
				userSession.put(CorridaProcesoAseDelAdapter.NAME, corridaProcesoAseDelAdapterVO);
				
				saveDemodaMessages(request, corridaProcesoAseDelAdapterVO);			
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, CorridaProcesoAseDelAdapter.NAME);
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
			CorridaProcesoAseDelAdapter corridaProcesoAseDelAdapterVO = (CorridaProcesoAseDelAdapter) userSession.get(CorridaProcesoAseDelAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (corridaProcesoAseDelAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CorridaProcesoAseDelAdapter.NAME + 
				" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CorridaProcesoAseDelAdapter.NAME); 
			}
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(corridaProcesoAseDelAdapterVO, request);
			
			// Tiene errores recuperables
			if (corridaProcesoAseDelAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + corridaProcesoAseDelAdapterVO.infoString()); 
				saveDemodaErrors(request, corridaProcesoAseDelAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CorridaProcesoAseDelAdapter.NAME, corridaProcesoAseDelAdapterVO);
			}
			
			// llamada al servicio
			AseDelVO aseDelVO = BalServiceLocator.getDelegadorService().reiniciar(userSession, corridaProcesoAseDelAdapterVO.getAseDel());
			
			// Tiene errores recuperables
			if (aseDelVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + corridaProcesoAseDelAdapterVO.infoString());
				saveDemodaErrors(request, aseDelVO);				
				request.setAttribute(CorridaProcesoAseDelAdapter.NAME, corridaProcesoAseDelAdapterVO);
				return mapping.findForward(BalConstants.FWD_CORRIDA_PRO_ASE_DEL_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (aseDelVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + corridaProcesoAseDelAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CorridaProcesoAseDelAdapter.NAME, corridaProcesoAseDelAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CorridaProcesoAseDelAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CorridaProcesoAseDelAdapter.NAME);
		}	
	}
	
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		
		return baseVolver(mapping, form, request, response, CorridaProcesoAseDelAdapter.NAME);
	}
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, CorridaProcesoAseDelAdapter.NAME);		
	}

}
