//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pro.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.pro.iface.model.AdpCorridaAdapter;
import ar.gov.rosario.siat.pro.iface.model.CorridaVO;
import ar.gov.rosario.siat.pro.iface.service.ProServiceLocator;
import ar.gov.rosario.siat.pro.view.util.ProConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class AdministrarAdpCorridaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarAdpCorridaDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping);		
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		AdpCorridaAdapter adpCorridaAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			Long idCorrida = (Long) userSession.getNavModel().getParameter(AdpCorridaAdapter.PARAM_ID_CORRIDA_SELECTED);
			CommonKey commonKey = new CommonKey(idCorrida);
						
			if (navModel.getAct().equals(ProConstants.ACT_ACTIVAR)) {
				stringServicio = "getProcesoAdapterForView(userSession, commonKey)";
				adpCorridaAdapterVO = ProServiceLocator.getAdpProcesoService().getAdpCorridaAdapterForView
					(userSession, commonKey);
				actionForward = mapping.findForward(ProConstants.FWD_ADPCORRIDA_VIEW_ADAPTER);
			}
			if (navModel.getAct().equals(ProConstants.ACT_REPROGRAMAR)) {
				stringServicio = "getProcesoAdapterForView(userSession, commonKey)";
				adpCorridaAdapterVO = ProServiceLocator.getAdpProcesoService().getAdpCorridaAdapterForView
					(userSession, commonKey);
				actionForward = mapping.findForward(ProConstants.FWD_ADPCORRIDA_VIEW_ADAPTER);
			}
			if (navModel.getAct().equals(ProConstants.ACT_CANCELAR)) {
				stringServicio = "getProcesoAdapterForView(userSession, commonKey)";
				adpCorridaAdapterVO = ProServiceLocator.getAdpProcesoService().getAdpCorridaAdapterForView
					(userSession, commonKey);
				actionForward = mapping.findForward(ProConstants.FWD_ADPCORRIDA_VIEW_ADAPTER);
			}
			if (navModel.getAct().equals(ProConstants.ACT_REINICIAR)) {
				stringServicio = "getProcesoAdapterForView(userSession, commonKey)";
				adpCorridaAdapterVO = ProServiceLocator.getAdpProcesoService().getAdpCorridaAdapterForView
					(userSession, commonKey);
				actionForward = mapping.findForward(ProConstants.FWD_ADPCORRIDA_VIEW_ADAPTER);
			}
			if (navModel.getAct().equals(ProConstants.ACT_SIGUIENTE)) {
				stringServicio = "getProcesoAdapterForView(userSession, commonKey)";
				adpCorridaAdapterVO = ProServiceLocator.getAdpProcesoService().getAdpCorridaAdapterForView
					(userSession, commonKey);
				actionForward = mapping.findForward(ProConstants.FWD_ADPCORRIDA_VIEW_ADAPTER);					
			}

			
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (adpCorridaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + 
					stringServicio + ": " + adpCorridaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AdpCorridaAdapter.NAME, adpCorridaAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			adpCorridaAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + 
				AdpCorridaAdapter.NAME + ": " + adpCorridaAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(AdpCorridaAdapter.NAME, adpCorridaAdapterVO);
			// Subo el apdater al userSession
			userSession.put(AdpCorridaAdapter.NAME, adpCorridaAdapterVO);
			
			saveDemodaMessages(request, adpCorridaAdapterVO);			
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AdpCorridaAdapter.NAME);
		}
	}

	public ActionForward activar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AdpCorridaAdapter adpCorridaAdapterVO = (AdpCorridaAdapter) userSession.get(AdpCorridaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (adpCorridaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AdpCorridaAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AdpCorridaAdapter.NAME); 
			}
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(adpCorridaAdapterVO, request);
			
            // Tiene errores recuperables
			if (adpCorridaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + adpCorridaAdapterVO.infoString()); 
				saveDemodaErrors(request, adpCorridaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, AdpCorridaAdapter.NAME, adpCorridaAdapterVO);
			}
			
			// llamada al servicio
			CorridaVO corridaVO = ProServiceLocator.getAdpProcesoService().activarProceso(userSession, adpCorridaAdapterVO.getCorrida());
			
            // Tiene errores recuperables
			if (corridaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + adpCorridaAdapterVO.infoString());
				saveDemodaErrors(request, corridaVO);				
				request.setAttribute(AdpCorridaAdapter.NAME, adpCorridaAdapterVO);
				return mapping.findForward(ProConstants.FWD_ADPCORRIDA_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (corridaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + adpCorridaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AdpCorridaAdapter.NAME, adpCorridaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AdpCorridaAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AdpCorridaAdapter.NAME);
		}	
	}

	public ActionForward reprogramar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AdpCorridaAdapter adpCorridaAdapterVO = (AdpCorridaAdapter) userSession.get(AdpCorridaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (adpCorridaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AdpCorridaAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AdpCorridaAdapter.NAME); 
			}
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(adpCorridaAdapterVO, request);
			
            // Tiene errores recuperables
			if (adpCorridaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + adpCorridaAdapterVO.infoString()); 
				saveDemodaErrors(request, adpCorridaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, AdpCorridaAdapter.NAME, adpCorridaAdapterVO);
			}
			
			// llamada al servicio
			CorridaVO corridaVO = ProServiceLocator.getAdpProcesoService().reprogramarProceso(userSession, adpCorridaAdapterVO.getCorrida());
			
            // Tiene errores recuperables
			if (corridaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + adpCorridaAdapterVO.infoString());
				saveDemodaErrors(request, corridaVO);				
				request.setAttribute(AdpCorridaAdapter.NAME, adpCorridaAdapterVO);
				return mapping.findForward(ProConstants.FWD_ADPCORRIDA_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (corridaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + adpCorridaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AdpCorridaAdapter.NAME, adpCorridaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AdpCorridaAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AdpCorridaAdapter.NAME);
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
			AdpCorridaAdapter adpCorridaAdapterVO = (AdpCorridaAdapter) userSession.get(AdpCorridaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (adpCorridaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AdpCorridaAdapter.NAME + 
				" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AdpCorridaAdapter.NAME); 
			}
			
			// llamada al servicio
			CorridaVO corridaVO = ProServiceLocator.getAdpProcesoService().reiniciarProceso(userSession, adpCorridaAdapterVO.getCorrida());
			
			// Tiene errores recuperables
			if (corridaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + adpCorridaAdapterVO.infoString());
				saveDemodaErrors(request, corridaVO);				
				request.setAttribute(AdpCorridaAdapter.NAME, adpCorridaAdapterVO);
				return mapping.findForward(ProConstants.FWD_ADPCORRIDA_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (corridaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + adpCorridaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AdpCorridaAdapter.NAME, adpCorridaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AdpCorridaAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AdpCorridaAdapter.NAME);
		}	
	}

	public ActionForward siguiente(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AdpCorridaAdapter adpCorridaAdapterVO = (AdpCorridaAdapter) userSession.get(AdpCorridaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (adpCorridaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AdpCorridaAdapter.NAME + 
				" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AdpCorridaAdapter.NAME); 
			}
			
			// llamada al servicio
			CorridaVO corridaVO = ProServiceLocator.getAdpProcesoService().siguientePaso(userSession, adpCorridaAdapterVO.getCorrida());
			
			// Tiene errores recuperables
			if (corridaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + adpCorridaAdapterVO.infoString());
				saveDemodaErrors(request, corridaVO);				
				request.setAttribute(AdpCorridaAdapter.NAME, adpCorridaAdapterVO);
				return mapping.findForward(ProConstants.FWD_ADPCORRIDA_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (corridaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + adpCorridaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AdpCorridaAdapter.NAME, adpCorridaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AdpCorridaAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AdpCorridaAdapter.NAME);
		}	
	}

	public ActionForward cancelar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AdpCorridaAdapter adpCorridaAdapterVO = (AdpCorridaAdapter) userSession.get(AdpCorridaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (adpCorridaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AdpCorridaAdapter.NAME + 
				" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AdpCorridaAdapter.NAME); 
			}
			
			// llamada al servicio
			CorridaVO corridaVO = ProServiceLocator.getAdpProcesoService().cancelarProceso(userSession, adpCorridaAdapterVO.getCorrida());
			
			// Tiene errores recuperables
			if (corridaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + adpCorridaAdapterVO.infoString());
				saveDemodaErrors(request, corridaVO);				
				request.setAttribute(AdpCorridaAdapter.NAME, adpCorridaAdapterVO);
				return mapping.findForward(ProConstants.FWD_ADPCORRIDA_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (corridaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + adpCorridaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AdpCorridaAdapter.NAME, adpCorridaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AdpCorridaAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AdpCorridaAdapter.NAME);
		}	
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, AdpCorridaAdapter.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, AdpCorridaAdapter.NAME);		
	}

	public ActionForward estadoPaso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return null;

		try {
			Integer paso = Integer.valueOf(request.getParameter("paso"));
			Long id = Long.valueOf(request.getParameter("id"));

			CommonKey idCorrida = new CommonKey(id);

			// llamada al servicio
			AdpCorridaAdapter adpCorridaAdapterVO = ProServiceLocator.getAdpProcesoService().getEstadoPaso(userSession, idCorrida, paso);
			// Tiene errores no recuperables
			if (adpCorridaAdapterVO == null) {
				return null;
			}

			// Envio el VO al request
			request.setAttribute(AdpCorridaAdapter.NAME, adpCorridaAdapterVO);
			// Subo el apdater al userSession
			userSession.put(AdpCorridaAdapter.NAME, adpCorridaAdapterVO);
			
			saveDemodaMessages(request, adpCorridaAdapterVO);			
			
			//return actionForward;
			return mapping.findForward(ProConstants.FWD_ADPCORRIDA_ESTADOPASO_ADAPTER);
		} catch (Exception exception) {
			log.error("AdministrarAdpCorridaDAction.estadoPaso(): ", exception);
			return null;
		}	
	}

}
