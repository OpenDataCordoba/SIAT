//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.pad.iface.model.RepartidorSearchPage;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class BuscarRepartidorDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarRepartidorDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_REPARTIDOR, act);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
		
			RepartidorSearchPage repartidorSearchPageVO = PadServiceLocator.getDistribucionService().getRepartidorSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (repartidorSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + repartidorSearchPageVO.infoString()); 
				saveDemodaErrors(request, repartidorSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, RepartidorSearchPage.NAME, repartidorSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (repartidorSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + repartidorSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, RepartidorSearchPage.NAME, repartidorSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , RepartidorSearchPage.NAME, repartidorSearchPageVO);
			
			return mapping.findForward(PadConstants.FWD_REPARTIDOR_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, RepartidorSearchPage.NAME);
		}
	}
	
	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return this.baseRefill(mapping, form, request, response, funcName, RepartidorSearchPage.NAME);

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
			RepartidorSearchPage repartidorSearchPageVO = (RepartidorSearchPage) userSession.get(RepartidorSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (repartidorSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + RepartidorSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, RepartidorSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(repartidorSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				repartidorSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (repartidorSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + repartidorSearchPageVO.infoString()); 
				saveDemodaErrors(request, repartidorSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, RepartidorSearchPage.NAME, repartidorSearchPageVO);
			}

			// Llamada al servicio	
			repartidorSearchPageVO = PadServiceLocator.getDistribucionService().getRepartidorSearchPageResult(userSession, repartidorSearchPageVO);			

			// Tiene errores recuperables
			if (repartidorSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + repartidorSearchPageVO.infoString()); 
				saveDemodaErrors(request, repartidorSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, RepartidorSearchPage.NAME, repartidorSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (repartidorSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + repartidorSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, RepartidorSearchPage.NAME, repartidorSearchPageVO);
			}
			
			// Envio el VO al request
			request.setAttribute(RepartidorSearchPage.NAME, repartidorSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(RepartidorSearchPage.NAME, repartidorSearchPageVO);
			
			return mapping.findForward(PadConstants.FWD_REPARTIDOR_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, RepartidorSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardVerSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_REPARTIDOR);
	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardAgregarSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_ENCREPARTIDOR);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardModificarSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_REPARTIDOR);	
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardEliminarSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_REPARTIDOR);
			
		}
		
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			return forwardActivarSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_REPARTIDOR);			
	}
		
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			return forwardDesactivarSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_REPARTIDOR);
	}

	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseSeleccionar(mapping, request, response, funcName, RepartidorSearchPage.NAME);
	}	

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			return baseVolver(mapping, form, request, response, RepartidorSearchPage.NAME);
			
	}

	public ActionForward paramRecurso (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el SearchPage del userSession
				RepartidorSearchPage repartidorSearchPageVO = (RepartidorSearchPage) userSession.get(RepartidorSearchPage.NAME);
				
				// Si es nulo no se puede continuar
				if (repartidorSearchPageVO == null) {
					log.error("error en: "  + funcName + ": " + RepartidorSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RepartidorSearchPage.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(repartidorSearchPageVO, request);
				
	            // Tiene errores recuperables
				if (repartidorSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + repartidorSearchPageVO.infoString()); 
					saveDemodaErrors(request, repartidorSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, RepartidorSearchPage.NAME, repartidorSearchPageVO);
				}
				
				// llamada al servicio
				repartidorSearchPageVO = PadServiceLocator.getDistribucionService().getRepartidorSearchPageParamRecurso(userSession, repartidorSearchPageVO);
				
	            // Tiene errores recuperables
				if (repartidorSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + repartidorSearchPageVO.infoString()); 
					saveDemodaErrors(request, repartidorSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, RepartidorSearchPage.NAME, repartidorSearchPageVO);
				}
				
				// Tiene errores no recuperables
				if (repartidorSearchPageVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + repartidorSearchPageVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, RepartidorSearchPage.NAME, repartidorSearchPageVO);
				}
				
				// Vuelvo el PAGENUMBER a 0 para evitar que el viewresult sea false
				repartidorSearchPageVO.setPageNumber(new Long(0)); 
				// Envio el VO al request
				request.setAttribute(RepartidorSearchPage.NAME, repartidorSearchPageVO);
				// Subo el searchpage al userSession
				userSession.put(RepartidorSearchPage.NAME, repartidorSearchPageVO);
				
				return mapping.findForward(PadConstants.FWD_REPARTIDOR_SEARCHPAGE);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RepartidorSearchPage.NAME);
			}
		}

}
