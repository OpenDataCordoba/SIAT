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
import ar.gov.rosario.siat.ef.iface.model.ActasInicioAdapter;
import ar.gov.rosario.siat.ef.iface.model.OpeInvSearchPage;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfError;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.NavModel;
import coop.tecso.demoda.iface.model.PrintModel;

public final class AdministrarOpeInvConActasDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarOpeInvConActasDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
				
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
				
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_ASIG_ACTAS, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ActasInicioAdapter actasInicioAdapterVO = new ActasInicioAdapter();
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			// recupera los ids selecionados y los pone en el adapter
			String[] idsSelected = (String[]) navModel.getParameter("idsSelected");
			actasInicioAdapterVO.setIdsSelectedFromSearch(idsSelected);

			if(act.equals(EfConstants.ACT_ASIGNARINV)){
				actasInicioAdapterVO = EfServiceLocator.getInvestigacionService().getActasInicioInvestigadorAdapterInit(userSession, actasInicioAdapterVO);
				actionForward = mapping.findForward(EfConstants.FWD_OPEINVCONACTA_INVESTIG_ADAPTER);
			}

			if(act.equals(EfConstants.ACT_HOJARUTA)){
				actasInicioAdapterVO = EfServiceLocator.getInvestigacionService().getActasInicioHojaRutaAdapterInit(userSession, actasInicioAdapterVO);
				actionForward = mapping.findForward(EfConstants.FWD_OPEINVCONACTA_HOJARUTA_ADAPTER);
			}

			

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (actasInicioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + actasInicioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ActasInicioAdapter.NAME, actasInicioAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			actasInicioAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ActasInicioAdapter.NAME + ": "+ actasInicioAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ActasInicioAdapter.NAME, actasInicioAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ActasInicioAdapter.NAME, actasInicioAdapterVO);
			 
			saveDemodaMessages(request, actasInicioAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ActasInicioAdapter.NAME);
		}
	}

	public ActionForward asignar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// Bajo el adapter del userSession
			ActasInicioAdapter actasInicioAdapter = (ActasInicioAdapter) userSession.get(ActasInicioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (actasInicioAdapter == null) {
				log.error("error en: "  + funcName + ": " + OpeInvSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ActasInicioAdapter.NAME); 
			}
						
			log.debug("ids:"+request.getParameter("idsSelectedForAsignar"));
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(actasInicioAdapter, request);

			if(request.getParameter("idsSelectedForAsignar")==null){
				actasInicioAdapter.addRecoverableError(EfError.ASIGNAR_INV_MSG_IDS_REQUERIDO);
				// Envio el VO al request
				request.setAttribute(ActasInicioAdapter.NAME, actasInicioAdapter);
				// Subo en el adapter al userSession
				userSession.put(ActasInicioAdapter.NAME, actasInicioAdapter);
				
				saveDemodaErrors(request, actasInicioAdapter);
				return mapping.findForward(EfConstants.FWD_ASIGNAR_INV_ADAPTER);
			}
			
			// llama al servicio
			actasInicioAdapter = EfServiceLocator.getInvestigacionService().asignarInvestigador(userSession, actasInicioAdapter); 
			
            // Tiene errores recuperables
			if (actasInicioAdapter.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + actasInicioAdapter.infoString()); 
				saveDemodaErrors(request, actasInicioAdapter);
				return forwardErrorRecoverable(mapping, request, userSession, ActasInicioAdapter.NAME, actasInicioAdapter);
			}
			
			// Tiene errores no recuperables
			if (actasInicioAdapter.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + actasInicioAdapter.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ActasInicioAdapter.NAME, actasInicioAdapter);
			}

			// Envio el VO al request
			request.setAttribute(ActasInicioAdapter.NAME, actasInicioAdapter);
			// Subo en el adapter al userSession
			userSession.put(ActasInicioAdapter.NAME, actasInicioAdapter);
			
			saveDemodaErrors(request, actasInicioAdapter);
			return mapping.findForward(EfConstants.FWD_ASIGNAR_INV_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OpeInvSearchPage.NAME);
		}
			
	}
	
	public ActionForward verMapa(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// Bajo el adapter del userSession
			ActasInicioAdapter actasInicioAdapter = (ActasInicioAdapter) userSession.get(ActasInicioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (actasInicioAdapter == null) {
				log.error("error en: "  + funcName + ": " + ActasInicioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ActasInicioAdapter.NAME); 
			}
									
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(actasInicioAdapter, request);
			
			// llama al servicio
			actasInicioAdapter = EfServiceLocator.getInvestigacionService().verMapa(userSession, actasInicioAdapter); 
			
            // Tiene errores recuperables
			if (actasInicioAdapter.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + actasInicioAdapter.infoString()); 
				saveDemodaErrors(request, actasInicioAdapter);
				return forwardErrorRecoverable(mapping, request, userSession, ActasInicioAdapter.NAME, actasInicioAdapter);
			}
			
			// Tiene errores no recuperables
			if (actasInicioAdapter.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + actasInicioAdapter.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ActasInicioAdapter.NAME, actasInicioAdapter);
			}
			
			// Envio el VO al request
			request.setAttribute(ActasInicioAdapter.NAME, actasInicioAdapter);
			// Subo en el adapter al userSession
			userSession.put(ActasInicioAdapter.NAME, actasInicioAdapter);
			
			saveDemodaErrors(request, actasInicioAdapter);
			return mapping.findForward(EfConstants.FWD_ASIGNAR_INV_MAPA_VIEW);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OpeInvSearchPage.NAME);
		}
			
	}
	
	public ActionForward volverDeVerMapa(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// Bajo el adapter del userSession
			ActasInicioAdapter actasInicioAdapter = (ActasInicioAdapter) userSession.get(ActasInicioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (actasInicioAdapter == null) {
				log.error("error en: "  + funcName + ": " + ActasInicioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ActasInicioAdapter.NAME); 
			}
									
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(actasInicioAdapter, request);
			
			// llama al servicio
			actasInicioAdapter = EfServiceLocator.getInvestigacionService().getActasInicioHojaRutaAdapterInit(userSession, actasInicioAdapter); 
			
            // Tiene errores recuperables
			if (actasInicioAdapter.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + actasInicioAdapter.infoString()); 
				saveDemodaErrors(request, actasInicioAdapter);
				return forwardErrorRecoverable(mapping, request, userSession, ActasInicioAdapter.NAME, actasInicioAdapter);
			}
			
			// Tiene errores no recuperables
			if (actasInicioAdapter.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + actasInicioAdapter.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ActasInicioAdapter.NAME, actasInicioAdapter);
			}

			// Envio el VO al request
			request.setAttribute(ActasInicioAdapter.NAME, actasInicioAdapter);
			// Subo en el adapter al userSession
			userSession.put(ActasInicioAdapter.NAME, actasInicioAdapter);
			
			return mapping.findForward(EfConstants.FWD_OPEINVCONACTA_HOJARUTA_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OpeInvSearchPage.NAME);
		}
			
	}

	public ActionForward imprimirHojaRuta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// Bajo el adapter del userSession
			ActasInicioAdapter actasInicioAdapter = (ActasInicioAdapter) userSession.get(ActasInicioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (actasInicioAdapter == null) {
				log.error("error en: "  + funcName + ": " + ActasInicioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ActasInicioAdapter.NAME); 
			}
									
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(actasInicioAdapter, request);
			
			// llama al servicio
			PrintModel print = EfServiceLocator.getInvestigacionService().getHojaDeRutaPrintModel(userSession, actasInicioAdapter); 
			
            // Tiene errores recuperables
			if (actasInicioAdapter.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + actasInicioAdapter.infoString()); 
				saveDemodaErrors(request, actasInicioAdapter);
				return forwardErrorRecoverable(mapping, request, userSession, ActasInicioAdapter.NAME, actasInicioAdapter);
			}
			
			// Tiene errores no recuperables
			if (actasInicioAdapter.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + actasInicioAdapter.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ActasInicioAdapter.NAME, actasInicioAdapter);
			}
			
			// Envio el VO al request
			request.setAttribute(ActasInicioAdapter.NAME, actasInicioAdapter);
			// Subo en el adapter al userSession
			userSession.put(ActasInicioAdapter.NAME, actasInicioAdapter);
			
			saveDemodaErrors(request, actasInicioAdapter);
			baseResponsePrintModel(response, print);
			return null;
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OpeInvSearchPage.NAME);
		}
			
	}
	

	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		return baseVolver(mapping, form, request, response, ActasInicioAdapter.NAME);
		
	}
	

}
