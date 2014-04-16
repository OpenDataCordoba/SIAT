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
import ar.gov.rosario.siat.ef.iface.model.ActaInvAdapter;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarActaInvDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarActaInvDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
				
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
				
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_ACTAINV, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ActaInvAdapter actaInvAdapterVO = new ActaInvAdapter();
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(request.getParameter("selectedId"));
			
			actaInvAdapterVO = EfServiceLocator.getInvestigacionService().getActaInvAdapterInit(userSession, commonKey);
			actionForward = mapping.findForward(EfConstants.FWD_ACTAINV_ADAPTER);


			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (actaInvAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + actaInvAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ActaInvAdapter.NAME, actaInvAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			actaInvAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ActaInvAdapter.NAME + ": "+ actaInvAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ActaInvAdapter.NAME, actaInvAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ActaInvAdapter.NAME, actaInvAdapterVO);
			 
			saveDemodaMessages(request, actaInvAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ActaInvAdapter.NAME);
		}
	}

	public ActionForward modificarPersona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();		
		
		UserSession userSession = getCurrentUserSession(request, mapping);			
		if (userSession == null) return forwardErrorSession(request);
				
		try {
			// Bajo el adapter del userSession
			ActaInvAdapter actaInvAdapterVO = (ActaInvAdapter)userSession.get(ActaInvAdapter.NAME);
	
			// Si es nulo no se puede continuar
			if (actaInvAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ActaInvAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ActaInvAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(actaInvAdapterVO, request);			
			// Este copiado debe ser de esta manera ya que no utilizamos Common Models
			
            // Tiene errores recuperables
			if (actaInvAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + actaInvAdapterVO.infoString()); 
				saveDemodaErrors(request, actaInvAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ActaInvAdapter.NAME, actaInvAdapterVO);
			}
			
			userSession.put("modificar", true);
			
			NavModel navModel = userSession.getNavModel();
			
			// seteo la accion y el parametro para volver
			navModel.setPrevAction(mapping.getPath());
			navModel.setPrevActionParameter("paramPersona");

			userSession.put("modificar", true);
			// seteo el act a ejecutar en el accion al cual me dirijo
			navModel.setAct("modificar");

			return new ActionForward("/pad/AdministrarPersona.do?method=inicializar");
			
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ActaInvAdapter.NAME);
		}
	}
	
	public ActionForward paramPersona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			NavModel navModel = userSession.getNavModel();
			
			try {
				
				//bajo el adapter del usserSession
				ActaInvAdapter actaInvAdapterVO =  (ActaInvAdapter) userSession.get(ActaInvAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (actaInvAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + ActaInvAdapter.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ActaInvAdapter.NAME); 
				}

				// recupero el id seleccionado por el usuario
				String selectedId;
				Boolean personaModificada = (Boolean) userSession.get("modificar");
				if(personaModificada != null && personaModificada){
					selectedId = actaInvAdapterVO.getOpeInvCon().getContribuyente().getPersona().getId().toString();
					userSession.put("modificar", null);
				}else{
					selectedId = navModel.getSelectedId();				
				}
				
				// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
				if (StringUtil.isNullOrEmpty(selectedId)) {
					// Envio el VO al request
					request.setAttribute(ActaInvAdapter.NAME, actaInvAdapterVO);
					return mapping.findForward(EfConstants.FWD_ACTAINV_ADAPTER); 
				}
				
				// llamo al param del servicio
				actaInvAdapterVO = EfServiceLocator.getInvestigacionService().getActaInvAdapterParamPersona(userSession, actaInvAdapterVO);

	            // Tiene errores recuperables
				if (actaInvAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + actaInvAdapterVO.infoString()); 
					saveDemodaErrors(request, actaInvAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
							ActaInvAdapter.NAME, actaInvAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (actaInvAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + actaInvAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
							ActaInvAdapter.NAME, actaInvAdapterVO);
				}

				// grabo los mensajes si hubiere
				saveDemodaMessages(request, actaInvAdapterVO);

				// Envio el VO al request
				request.setAttribute(ActaInvAdapter.NAME, actaInvAdapterVO);
				// Subo el apdater al userSession
				userSession.put(ActaInvAdapter.NAME, actaInvAdapterVO);

				return mapping.findForward(EfConstants.FWD_ACTAINV_ADAPTER);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ActaInvAdapter.NAME);
			}
	}
	
	public ActionForward guardar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			NavModel navModel = userSession.getNavModel();
			
			try {
				
				//bajo el adapter del usserSession
				ActaInvAdapter actaInvAdapterVO =  (ActaInvAdapter) userSession.get(ActaInvAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (actaInvAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + ActaInvAdapter.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ActaInvAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(actaInvAdapterVO, request);			

				// llamo al param del servicio
				actaInvAdapterVO = EfServiceLocator.getInvestigacionService().getGuardarActaInv(userSession, actaInvAdapterVO);

	            // Tiene errores recuperables
				if (actaInvAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + actaInvAdapterVO.infoString()); 
					saveDemodaErrors(request, actaInvAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
							ActaInvAdapter.NAME, actaInvAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (actaInvAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + actaInvAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
							ActaInvAdapter.NAME, actaInvAdapterVO);
				}

				// grabo los mensajes si hubiere
				saveDemodaMessages(request, actaInvAdapterVO);

				// Envio el VO al request
				request.setAttribute(ActaInvAdapter.NAME, actaInvAdapterVO);
				// Subo el apdater al userSession
				userSession.put(ActaInvAdapter.NAME, actaInvAdapterVO);

				return forwardConfirmarOk(mapping, request, funcName, ActaInvAdapter.NAME);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ActaInvAdapter.NAME);
			}
	}
	
	public ActionForward pedidoAprobacion(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			
			try {
				
				//bajo el adapter del usserSession
				ActaInvAdapter actaInvAdapterVO =  (ActaInvAdapter) userSession.get(ActaInvAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (actaInvAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + ActaInvAdapter.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ActaInvAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(actaInvAdapterVO, request);			

				// llamo al param del servicio
				actaInvAdapterVO = EfServiceLocator.getInvestigacionService().getPedidoAprobacionActaInv(userSession, actaInvAdapterVO);

	            // Tiene errores recuperables
				if (actaInvAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + actaInvAdapterVO.infoString()); 
					saveDemodaErrors(request, actaInvAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
							ActaInvAdapter.NAME, actaInvAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (actaInvAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + actaInvAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
							ActaInvAdapter.NAME, actaInvAdapterVO);
				}

				// grabo los mensajes si hubiere
				saveDemodaMessages(request, actaInvAdapterVO);

				// Envio el VO al request
				request.setAttribute(ActaInvAdapter.NAME, actaInvAdapterVO);
				// Subo el apdater al userSession
				userSession.put(ActaInvAdapter.NAME, actaInvAdapterVO);

				return forwardConfirmarOk(mapping, request, funcName, ActaInvAdapter.NAME);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ActaInvAdapter.NAME);
			}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		return baseVolver(mapping, form, request, response, ActaInvAdapter.NAME);
		
	}
	

}
