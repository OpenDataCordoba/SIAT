//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.view.struts;

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
import ar.gov.rosario.siat.rec.iface.model.PlaCuaDetSearchPage;
import ar.gov.rosario.siat.rec.iface.model.PlanillaCuadraAdapter;
import ar.gov.rosario.siat.rec.iface.model.PlanillaCuadraVO;
import ar.gov.rosario.siat.rec.iface.service.RecServiceLocator;
import ar.gov.rosario.siat.rec.iface.util.RecSecurityConstants;
import ar.gov.rosario.siat.rec.view.util.RecConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarPlaCuaDetDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarPlaCuaDetDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_PLANILLACUADRA_DETALLE, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			PlanillaCuadraAdapter planillaCuadraAdapter = (PlanillaCuadraAdapter) 
				userSession.get(PlanillaCuadraAdapter.NAME);
			PlanillaCuadraVO planillaCuadraVO = planillaCuadraAdapter.getPlanillaCuadra();

			// seteo los filtros en el search page
			PlaCuaDetSearchPage plaCuaDetSearchPage = new PlaCuaDetSearchPage (planillaCuadraVO);
			
			PlaCuaDetSearchPage plaCuaDetSearchPageVO = 
				RecServiceLocator.getCdmService().getPlaCuaDetSearchPageInit(userSession, plaCuaDetSearchPage);
			
			// Tiene errores recuperables
			if (plaCuaDetSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + plaCuaDetSearchPageVO.infoString()); 
				saveDemodaErrors(request, plaCuaDetSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlaCuaDetSearchPage.NAME, plaCuaDetSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (plaCuaDetSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + plaCuaDetSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlaCuaDetSearchPage.NAME, plaCuaDetSearchPageVO);
			}
			
			log.debug("Guardado los mensajes: "  + funcName + ": " + plaCuaDetSearchPageVO.infoString()); 
			saveDemodaMessages(request, plaCuaDetSearchPageVO);
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , PlaCuaDetSearchPage.NAME, plaCuaDetSearchPageVO);
			
			if (plaCuaDetSearchPageVO.hasManzana() && !((String)userSession.get("reqAttMethod")).equals("limpiar")) {
				// si hay manzanas cargadas voy al buscar
				return this.buscar(mapping, form, request, response);
			} else {
				// sino hay manzanas cargadas solo muestro los filtros
				return mapping.findForward(RecConstants.FWD_PLACUADET_SEARCHPAGE);
				
			}
			
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlaCuaDetSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, PlaCuaDetSearchPage.NAME);
		
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
			PlaCuaDetSearchPage plaCuaDetSearchPageVO = (PlaCuaDetSearchPage) userSession.get(PlaCuaDetSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (plaCuaDetSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + PlaCuaDetSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlaCuaDetSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(plaCuaDetSearchPageVO, request);
			}
			
            // Tiene errores recuperables
			if (plaCuaDetSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + plaCuaDetSearchPageVO.infoString()); 
				saveDemodaErrors(request, plaCuaDetSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlaCuaDetSearchPage.NAME, plaCuaDetSearchPageVO);
			}
			
			plaCuaDetSearchPageVO.setPaged(false);
			plaCuaDetSearchPageVO.setPageNumber(1L);
				
			// Llamada al servicio	
			plaCuaDetSearchPageVO = RecServiceLocator.getCdmService().getPlaCuaDetSearchPageResult(userSession, plaCuaDetSearchPageVO);			

			// Tiene errores recuperables
			if (plaCuaDetSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + plaCuaDetSearchPageVO.infoString()); 
				saveDemodaErrors(request, plaCuaDetSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlaCuaDetSearchPage.NAME, plaCuaDetSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (plaCuaDetSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + plaCuaDetSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, PlaCuaDetSearchPage.NAME, plaCuaDetSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(PlaCuaDetSearchPage.NAME, plaCuaDetSearchPageVO);
			// Nuleo el list result
			//plaCuaDetSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(PlaCuaDetSearchPage.NAME, plaCuaDetSearchPageVO);
			
			//Guardamos los mensajes 
			saveDemodaMessages(request, plaCuaDetSearchPageVO);
			
			return mapping.findForward(RecConstants.FWD_PLACUADET_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlaCuaDetSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_PLACUADET);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, 
			RecSecurityConstants.ABM_PLANILLACUADRA_DETALLE, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlaCuaDetSearchPage plaCuaDetSearchPageVO = 
				(PlaCuaDetSearchPage) userSession.get(PlaCuaDetSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (plaCuaDetSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + PlaCuaDetSearchPage.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlaCuaDetSearchPage.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(plaCuaDetSearchPageVO, request);
			
            // Tiene errores recuperables
			if (plaCuaDetSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + plaCuaDetSearchPageVO.infoString()); 
				saveDemodaErrors(request, plaCuaDetSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
					PlaCuaDetSearchPage.NAME, plaCuaDetSearchPageVO);
			}
			
			// llamada al servicio
			plaCuaDetSearchPageVO = RecServiceLocator.getCdmService().createPlaCuaDet
				(userSession, plaCuaDetSearchPageVO);
			
            // Tiene errores recuperables
			if (plaCuaDetSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + plaCuaDetSearchPageVO.infoString()); 
				saveDemodaErrors(request, plaCuaDetSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
					PlaCuaDetSearchPage.NAME, plaCuaDetSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (plaCuaDetSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + plaCuaDetSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
					PlaCuaDetSearchPage.NAME, plaCuaDetSearchPageVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlaCuaDetSearchPage.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlaCuaDetSearchPage.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_PLACUADET);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_PLACUADET);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_PLACUADET);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_PLACUADET);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, PlaCuaDetSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, PlaCuaDetSearchPage.NAME);
		
	}
		
	
}
