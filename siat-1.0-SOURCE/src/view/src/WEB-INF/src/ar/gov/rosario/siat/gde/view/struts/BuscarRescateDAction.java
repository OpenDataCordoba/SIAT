//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.gde.iface.model.RescateAdapter;
import ar.gov.rosario.siat.gde.iface.model.RescateSearchPage;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import ar.gov.rosario.siat.pro.iface.model.AdpCorridaAdapter;
import ar.gov.rosario.siat.pro.view.util.ProConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class BuscarRescateDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarRescateDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_RESCATE, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		//Chequeo de acceso por instancia
		if (!userSession.getEsAdmin() && SiatParam.isWebSiat() && !SiatParam.isIntranetSiat()) {
			return forwardFuncionNoDisponible(request);
		}

		try {
			
			RescateSearchPage rescateSearchPageVO = GdeServiceLocator.getGdePlanPagoService().getRescateSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (rescateSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + rescateSearchPageVO.infoString()); 
				saveDemodaErrors(request, rescateSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, RescateSearchPage.NAME, rescateSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (rescateSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + rescateSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, RescateSearchPage.NAME, rescateSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , RescateSearchPage.NAME, rescateSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_RESCATE_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, RescateSearchPage.NAME);
		}
	}

	public ActionForward buscar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// Bajo el searchPage del userSession
			RescateSearchPage rescateSearchPageVO = (RescateSearchPage) userSession.get(RescateSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (rescateSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + RescateSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, RescateSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(rescateSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				rescateSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (rescateSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + rescateSearchPageVO.infoString()); 
				saveDemodaErrors(request, rescateSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, RescateSearchPage.NAME, rescateSearchPageVO);
			}
				
			// Llamada al servicio	
			rescateSearchPageVO = GdeServiceLocator.getGdePlanPagoService().getRescateSearchPageResult(userSession, rescateSearchPageVO);			

			// Tiene errores recuperables
			if (rescateSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + rescateSearchPageVO.infoString()); 
				saveDemodaErrors(request, rescateSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, RescateSearchPage.NAME, rescateSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (rescateSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + rescateSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, RescateSearchPage.NAME, rescateSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(RescateSearchPage.NAME, rescateSearchPageVO);
			// Nuleo el list result
			//rescateSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(RescateSearchPage.NAME, rescateSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_RESCATE_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, RescateSearchPage.NAME);
		}
	}
	
	public ActionForward paramRecurso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			
			try {
				
				// Bajo el searchPage del userSession
				RescateSearchPage rescateSearchPageVO = (RescateSearchPage) userSession.get(RescateSearchPage.NAME);
				
				// Si es nulo no se puede continuar
				if (rescateSearchPageVO == null) {
					log.error("error en: "  + funcName + ": " + RescateSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RescateSearchPage.NAME); 
				}
				
	            // Tiene errores recuperables
				if (rescateSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + rescateSearchPageVO.infoString()); 
					saveDemodaErrors(request, rescateSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, RescateSearchPage.NAME, rescateSearchPageVO);
				}
				
				DemodaUtil.populateVO(rescateSearchPageVO, request);
					
				// Llamada al servicio	
				rescateSearchPageVO = GdeServiceLocator.getGdePlanPagoService().getRescateSearchPageParamRecurso(userSession, rescateSearchPageVO);			

				// Tiene errores recuperables
				if (rescateSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + rescateSearchPageVO.infoString()); 
					saveDemodaErrors(request, rescateSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, RescateSearchPage.NAME, rescateSearchPageVO);
				}
				
				// Tiene errores no recuperables
				if (rescateSearchPageVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + rescateSearchPageVO.errorString());
					return forwardErrorNonRecoverable(mapping, request, funcName, RescateSearchPage.NAME, rescateSearchPageVO);
				}
			
				// Envio el VO al request
				request.setAttribute(RescateSearchPage.NAME, rescateSearchPageVO);
				// Subo en el el searchPage al userSession
				userSession.put(RescateSearchPage.NAME, rescateSearchPageVO);
				
				return mapping.findForward(GdeConstants.FWD_RESCATE_SEARCHPAGE);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RescateSearchPage.NAME);
			}
		}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled())log.debug(funcName+" :enter");
						
			
			return forwardVerSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_RESCATE);
		}

		public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardAgregarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_RESCATE);
		}

		public ActionForward modificar(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardModificarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_RESCATE);	
		}

		public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardEliminarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_RESCATE);
		}

		/*
		public ActionForward admProceso(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response) throws Exception {

				String funcName = DemodaUtil.currentMethodName();
				
				return baseForwardSearchPage(mapping, request, funcName, 
						GdeConstants.ACTION_ADMINISTRAR_PROCESO_PROCESO_MASIVO,
						GdeConstants.ACT_ADMINISTRAR_PROCESO_PROCESO_MASIVO );		
		}
		*/
	
	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return this.baseRefill(mapping, form, request, response, funcName, RescateSearchPage.NAME);
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, RescateSearchPage.NAME);
	}
	
	public ActionForward administrar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception{
		String funcName= DemodaUtil.currentMethodName();
		if(log.isDebugEnabled())log.debug(funcName+" :enter");
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		NavModel navModel = userSession.getNavModel();
		RescateAdapter rescateAdapterVO=null;
		try{
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
		
			rescateAdapterVO = GdeServiceLocator.getGdePlanPagoService().getRescateAdapterForView(userSession, commonKey);
			
			
			navModel.putParameter(AdpCorridaAdapter.PARAM_ID_CORRIDA_SELECTED, rescateAdapterVO.getRescate().getCorrida().getId());
			navModel.setPrevAction("/gde/BuscarRescate");
			navModel.setPrevActionParameter("buscar");
			
			
			return baseForward(mapping, request, funcName,"buscar", ProConstants.ACTION_ADMINISTRAR_ADP_EMISION, ProConstants.ACT_ACTIVAR);
		}catch (Exception exception){
			log.error(rescateAdapterVO, exception);
			return baseException(mapping, request, funcName, exception, RescateSearchPage.NAME);
		}
	}
	
	public ActionForward seleccionar (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception{
		
		String funcName=DemodaUtil.currentMethodName();
		
		return forwardSeleccionar(mapping, request, "buscar", GdeConstants.ACTION_ADMINISTRAR_RESCATE, false);
		
	}
		
	
}
