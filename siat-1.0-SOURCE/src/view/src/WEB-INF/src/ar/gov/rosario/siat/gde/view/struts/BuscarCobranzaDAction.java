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

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.gde.iface.model.CobranzaSearchPage;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.NavModel;

public final class BuscarCobranzaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarCobranzaDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession= canAccess(request, mapping, GdeSecurityConstants.ABM_COBRANZA, act);
		
		if (userSession == null) return forwardErrorSession(request);
		
		try { 
			
			
			CobranzaSearchPage cobranzaSearchPageVO = GdeServiceLocator.getGdeGCobranzaService().getCobranzaSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (cobranzaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cobranzaSearchPageVO.infoString()); 
				saveDemodaErrors(request, cobranzaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, CobranzaSearchPage.NAME, cobranzaSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (cobranzaSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cobranzaSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CobranzaSearchPage.NAME, cobranzaSearchPageVO);
			}
			
			// Si idDeuda no es nula
			
			baseInicializarSearchPage(mapping, request, userSession , CobranzaSearchPage.NAME, cobranzaSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_COBRANZA_SEARCHPAGE);
			
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CobranzaSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, CobranzaSearchPage.NAME);
		
	}
	
	public ActionForward buscar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// Bajo el searchPage del userSession
			CobranzaSearchPage cobranzaSearchPageVO = (CobranzaSearchPage) userSession.get(CobranzaSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (cobranzaSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + CobranzaSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CobranzaSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(cobranzaSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				cobranzaSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (cobranzaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cobranzaSearchPageVO.infoString()); 
				saveDemodaErrors(request, cobranzaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, CobranzaSearchPage.NAME, cobranzaSearchPageVO);
			}
				
			// Llamada al servicio	
			cobranzaSearchPageVO = GdeServiceLocator.getGdeGCobranzaService().getCobranzaSearchPageResult(userSession, cobranzaSearchPageVO);			

			// Tiene errores recuperables
			if (cobranzaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cobranzaSearchPageVO.infoString()); 
				saveDemodaErrors(request, cobranzaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, CobranzaSearchPage.NAME, cobranzaSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (cobranzaSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cobranzaSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, CobranzaSearchPage.NAME, cobranzaSearchPageVO);
			}
			
			// seteo los valores del navegacion en el pageModel
			cobranzaSearchPageVO.setValuesFromNavModel(userSession.getNavModel());
		
			// Envio el VO al request
			request.setAttribute(CobranzaSearchPage.NAME, cobranzaSearchPageVO);
			
			// Subo en el el searchPage al userSession
			userSession.put(CobranzaSearchPage.NAME, cobranzaSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_COBRANZA_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CobranzaSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		return forwardVerSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_COBRANZA);
		
	}
	
	
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, CobranzaSearchPage.NAME);
		
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String funcName= DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_DECJUD);
	}
	
	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String funcName= DemodaUtil.currentMethodName();
		
			return forwardModificarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_COBRANZA);
			
	}
	
	public ActionForward asignar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String funcName= DemodaUtil.currentMethodName();
		
		return baseForwardSearchPage (mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_COBRANZA, BaseConstants.ACT_ASIGNAR);
			
	}
	
	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName= DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_DECJUD);
			
	}
		
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, CobranzaSearchPage.NAME);
			
	}

	
	public ActionForward validarCaso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CobranzaSearchPage cobranzaSearchPageVO = (CobranzaSearchPage)userSession.get(CobranzaSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (cobranzaSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + CobranzaSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CobranzaSearchPage.NAME); 
			}
			
			// Populate como en un buscar
			DemodaUtil.populateVO(cobranzaSearchPageVO, request);
			
			//log.debug( funcName + " " + cobranzaSearchPageVO.getCobranza().getOrdenControl().getCaso().infoString());
			
			// llamada al servicio
			CasServiceLocator.getCasCasoService().validarCaso(userSession, cobranzaSearchPageVO.getCobranza().getOrdenControl()); 
			
			cobranzaSearchPageVO.getCobranza().getOrdenControl().passErrorMessages(cobranzaSearchPageVO);
		    			
		    saveDemodaMessages(request, cobranzaSearchPageVO);
		    saveDemodaErrors(request, cobranzaSearchPageVO);
		    
		 // Tiene errores recuperables
			if (cobranzaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cobranzaSearchPageVO.infoString()); 
				saveDemodaErrors(request, cobranzaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, CobranzaSearchPage.NAME, cobranzaSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (cobranzaSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cobranzaSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, CobranzaSearchPage.NAME, cobranzaSearchPageVO);
			}
			
			
			request.setAttribute(CobranzaSearchPage.NAME, cobranzaSearchPageVO);
		
			return mapping.findForward(GdeConstants.FWD_COBRANZA_SEARCHPAGE); 
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CobranzaSearchPage.NAME);
		}	
	}
	
	public ActionForward buscarPersona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();		
		
		UserSession userSession = getCurrentUserSession(request, mapping);			
		if (userSession == null) return forwardErrorSession(request);
				
		try {
			// Bajo el adapter del userSession
			CobranzaSearchPage cobranzaSearchPageVO = (CobranzaSearchPage)userSession.get(CobranzaSearchPage.NAME);
	
			// Si es nulo no se puede continuar
			if (cobranzaSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + CobranzaSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CobranzaSearchPage.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(cobranzaSearchPageVO, request);
			
            // Tiene errores recuperables
			if (cobranzaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cobranzaSearchPageVO.infoString()); 
				saveDemodaErrors(request, cobranzaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, CobranzaSearchPage.NAME, cobranzaSearchPageVO);
			}
			
			return forwardSeleccionar(mapping, request, 
					GdeConstants.METHOD_COBRANZA_PARAM_PERSONA, PadConstants.ACTION_BUSCAR_PERSONA, true);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CobranzaSearchPage.NAME);
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
			
			// Bajo el adapter del userSession
			CobranzaSearchPage cobranzaSearchPageVO = (CobranzaSearchPage)userSession.get(CobranzaSearchPage.NAME);
	
			// Si es nulo no se puede continuar
			if (cobranzaSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + CobranzaSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CobranzaSearchPage.NAME); 
			}

			DemodaUtil.populateVO(cobranzaSearchPageVO, request);
			
			// recupero el id seleccionado por el usuario
			String selectedId =  navModel.getSelectedId();				
			
			// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
			if (StringUtil.isNullOrEmpty(selectedId)) {
				// Envio el VO al request
				request.setAttribute(CobranzaSearchPage.NAME, cobranzaSearchPageVO);
				return mapping.findForward(GdeConstants.FWD_COBRANZA_SEARCHPAGE); 
			}

			// llamo al param del servicio
			cobranzaSearchPageVO = GdeServiceLocator.getGdeGCobranzaService().paramPersona(userSession, cobranzaSearchPageVO, new Long(selectedId));

            // Tiene errores recuperables
			if (cobranzaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cobranzaSearchPageVO.infoString()); 
				saveDemodaErrors(request, cobranzaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
						CobranzaSearchPage.NAME, cobranzaSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (cobranzaSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cobranzaSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
						CobranzaSearchPage.NAME, cobranzaSearchPageVO);
			}
			
			
			
			// grabo los mensajes si hubiere
			saveDemodaMessages(request, cobranzaSearchPageVO);
			
			// Envio el VO al request
			request.setAttribute(CobranzaSearchPage.NAME, cobranzaSearchPageVO);
			
			userSession.put(CobranzaSearchPage.NAME, cobranzaSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_COBRANZA_SEARCHPAGE);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CobranzaSearchPage.NAME);
		}
	}

}
