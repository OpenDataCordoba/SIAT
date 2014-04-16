//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.ef.iface.model.AprobacionActaInvAdapter;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarAprobActaInvDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarAprobActaInvDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
				
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
				
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ADM_APROBACIONACTAINV, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		AprobacionActaInvAdapter AprobacionActaInvAdapterVO = new AprobacionActaInvAdapter();
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(request.getParameter("selectedId"));
			
			AprobacionActaInvAdapterVO = EfServiceLocator.getInvestigacionService().getAprobacionActaInvAdapterInit(userSession, commonKey);
			actionForward = mapping.findForward(EfConstants.FWD_APROBACTAINV_ADAPTER);


			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (AprobacionActaInvAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + AprobacionActaInvAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AprobacionActaInvAdapter.NAME, AprobacionActaInvAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			AprobacionActaInvAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + AprobacionActaInvAdapter.NAME + ": "+ AprobacionActaInvAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(AprobacionActaInvAdapter.NAME, AprobacionActaInvAdapterVO);
			// Subo el apdater al userSession
			userSession.put(AprobacionActaInvAdapter.NAME, AprobacionActaInvAdapterVO);
			 
			saveDemodaMessages(request, AprobacionActaInvAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AprobacionActaInvAdapter.NAME);
		}
	}

	public ActionForward guardar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			
			try {
				
				//bajo el adapter del usserSession
				AprobacionActaInvAdapter AprobacionActaInvAdapterVO =  (AprobacionActaInvAdapter) userSession.get(AprobacionActaInvAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (AprobacionActaInvAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + AprobacionActaInvAdapter.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, AprobacionActaInvAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(AprobacionActaInvAdapterVO, request);			

				// llamo al servicio
				AprobacionActaInvAdapterVO = EfServiceLocator.getInvestigacionService().cambiarEstadoAprobActaInv(userSession, AprobacionActaInvAdapterVO);

	            // Tiene errores recuperables
				if (AprobacionActaInvAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + AprobacionActaInvAdapterVO.infoString()); 
					saveDemodaErrors(request, AprobacionActaInvAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
							AprobacionActaInvAdapter.NAME, AprobacionActaInvAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (AprobacionActaInvAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + AprobacionActaInvAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
							AprobacionActaInvAdapter.NAME, AprobacionActaInvAdapterVO);
				}

				// grabo los mensajes si hubiere
				saveDemodaMessages(request, AprobacionActaInvAdapterVO);

				// Envio el VO al request
				request.setAttribute(AprobacionActaInvAdapter.NAME, AprobacionActaInvAdapterVO);
				// Subo el apdater al userSession
				userSession.put(AprobacionActaInvAdapter.NAME, AprobacionActaInvAdapterVO);

				return forwardConfirmarOk(mapping, request, funcName, AprobacionActaInvAdapter.NAME);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, AprobacionActaInvAdapter.NAME);
			}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		return baseVolver(mapping, form, request, response, AprobacionActaInvAdapter.NAME);
		
	}
	

}
