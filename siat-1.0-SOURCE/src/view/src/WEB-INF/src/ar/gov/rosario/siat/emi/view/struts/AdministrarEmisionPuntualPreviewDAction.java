//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.emi.iface.model.EmisionPuntualAdapter;
import ar.gov.rosario.siat.emi.iface.model.EmisionPuntualPreviewAdapter;
import ar.gov.rosario.siat.emi.iface.service.EmiServiceLocator;
import ar.gov.rosario.siat.emi.iface.util.EmiSecurityConstants;
import ar.gov.rosario.siat.emi.view.util.EmiConstants;
import ar.gov.rosario.siat.esp.iface.model.EntVenAdapter;
import ar.gov.rosario.siat.esp.iface.service.EspServiceLocator;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.NavModel;


public final class AdministrarEmisionPuntualPreviewDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEmisionPuntualPreviewDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, EmiSecurityConstants.ABM_EMISIONPUNTUAL, act);		
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		EmisionPuntualPreviewAdapter emisionPuntualPreviewAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			emisionPuntualPreviewAdapterVO = (EmisionPuntualPreviewAdapter) userSession.get(EmisionPuntualPreviewAdapter.NAME); 
			
			if (navModel.getAct().equals(EmiConstants.ACT_PREVIEW)) {
				stringServicio = "getEmisionPuntualPreviewAdapter(userSession, emisionVO)";
				emisionPuntualPreviewAdapterVO = EmiServiceLocator.getEmisionService().getEmisionPuntualPreviewAdapter(userSession, emisionPuntualPreviewAdapterVO);
				actionForward = mapping.findForward(EmiConstants.FWD_EMISIONPUNTUAL_PREVIEW_ADAPTER);
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (emisionPuntualPreviewAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + 
					stringServicio + ": " + emisionPuntualPreviewAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EmisionPuntualPreviewAdapter.NAME, emisionPuntualPreviewAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			emisionPuntualPreviewAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + EmisionPuntualPreviewAdapter.NAME + ": " + emisionPuntualPreviewAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(EmisionPuntualPreviewAdapter.NAME, emisionPuntualPreviewAdapterVO);

			// Subo el apdater al userSession
			userSession.put(EmisionPuntualPreviewAdapter.NAME, emisionPuntualPreviewAdapterVO);
			
			saveDemodaMessages(request, emisionPuntualPreviewAdapterVO);			
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionPuntualPreviewAdapter.NAME);
		}
	}
	
	public ActionForward confirmar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, 
				EmiSecurityConstants.ABM_EMISIONPUNTUAL, BaseSecurityConstants.AGREGAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			EmisionPuntualPreviewAdapter emisionPuntualPreviewAdapterVO = (EmisionPuntualPreviewAdapter) 
					userSession.get(EmisionPuntualPreviewAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (emisionPuntualPreviewAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EmisionPuntualPreviewAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EmisionPuntualPreviewAdapter.NAME); 
			}
	
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(emisionPuntualPreviewAdapterVO, request);
			
	        // Tiene errores recuperables
			if (emisionPuntualPreviewAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionPuntualPreviewAdapterVO.infoString()); 
				saveDemodaErrors(request, emisionPuntualPreviewAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, EmisionPuntualPreviewAdapter.NAME, emisionPuntualPreviewAdapterVO);
			}
			
			// llamada al servicio
			EmisionPuntualAdapter emisionPuntualAdapter = (EmisionPuntualAdapter) userSession.get(EmisionPuntualAdapter.ENC_NAME);
			emisionPuntualAdapter.setEmision(emisionPuntualPreviewAdapterVO.getEmision());
			
			emisionPuntualAdapter = EmiServiceLocator.getEmisionService().createDeudaAdminFromAuxDeuda(userSession, emisionPuntualAdapter);				
			
			// Tiene errores recuperables
			if (emisionPuntualAdapter.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionPuntualAdapter.infoString()); 
				saveDemodaErrors(request, emisionPuntualAdapter);
				return forwardErrorRecoverable(mapping, request, userSession, EmisionPuntualPreviewAdapter.NAME, emisionPuntualPreviewAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (emisionPuntualAdapter.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + emisionPuntualPreviewAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EmisionPuntualPreviewAdapter.NAME, emisionPuntualPreviewAdapterVO);
			}

			// llamada al servicio de Espectaculos Publicos
			EntVenAdapter entVenAdapter = (EntVenAdapter) userSession.get(EntVenAdapter.NAME); 
			if (entVenAdapter != null) {
				entVenAdapter.setEmision(emisionPuntualAdapter.getEmision());
				EspServiceLocator.getHabilitacionService().createEntVen(userSession, entVenAdapter);
				userSession.remove(EntVenAdapter.NAME);
			}

			
			// Envio el VO al request
			request.setAttribute(EmisionPuntualAdapter.NAME, emisionPuntualAdapter);

			// Subo el apdater al userSession
			userSession.put(EmisionPuntualAdapter.NAME, emisionPuntualAdapter);
			
			// Eliminamos el Adapter del Preview por seguridad.
			// (para que no se repita la operacion, por ejemplo: por un refresh)
			userSession.remove(EmisionPuntualPreviewAdapter.NAME);
			
			return mapping.findForward(EmiConstants.FWD_EMISIONPUNTUAL_RECIBOS_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionPuntualPreviewAdapter.NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		return baseVolver(mapping, form, request, response, EmisionPuntualPreviewAdapter.NAME);
	}
}
