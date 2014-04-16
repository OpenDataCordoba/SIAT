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
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.gde.iface.model.PerCobSearchPage;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarPerCobDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarPerCobDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PERCOB, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			PerCobSearchPage perCobSearchPageVO = GdeServiceLocator.getDefinicionService().getPerCobSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (perCobSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + perCobSearchPageVO.infoString()); 
				saveDemodaErrors(request, perCobSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, PerCobSearchPage.NAME, perCobSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (perCobSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + perCobSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PerCobSearchPage.NAME, perCobSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , PerCobSearchPage.NAME, perCobSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_PERCOB_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PerCobSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, PerCobSearchPage.NAME);
		
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
			PerCobSearchPage perCobSearchPage = (PerCobSearchPage) userSession.get(PerCobSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (perCobSearchPage == null) {
				log.error("error en: "  + funcName + ": " + PerCobSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PerCobSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(perCobSearchPage, request);
				// Setea el PageNumber del PageModel				
				perCobSearchPage.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (perCobSearchPage.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + perCobSearchPage.infoString()); 
				saveDemodaErrors(request, perCobSearchPage);
				return forwardErrorRecoverable(mapping, request, userSession, PerCobSearchPage.NAME, perCobSearchPage);
			}
				
			// Llamada al servicio	
			perCobSearchPage = GdeServiceLocator.getDefinicionService().getPerCobSearchPageResult(userSession, perCobSearchPage);			

			// Tiene errores recuperables
			if (perCobSearchPage.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + perCobSearchPage.infoString()); 
				saveDemodaErrors(request, perCobSearchPage);
				return forwardErrorRecoverable(mapping, request, userSession, PerCobSearchPage.NAME, perCobSearchPage);
			}
			
			// Tiene errores no recuperables
			if (perCobSearchPage.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + perCobSearchPage.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, PerCobSearchPage.NAME, perCobSearchPage);
			}
		
			// Envio el VO al request
			request.setAttribute(PerCobSearchPage.NAME, perCobSearchPage);
			// Nuleo el list result
			//mandatarioSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(PerCobSearchPage.NAME, perCobSearchPage);
			
			return mapping.findForward(GdeConstants.FWD_PERCOB_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PerCobSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PERCOB);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		//Se utiliza uno de los dos return, segun sea un encabezado detalle o no.
		return forwardAgregarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PERCOB);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PERCOB);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PERCOB);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PERCOB);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PERCOB);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, PerCobSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, PerCobSearchPage.NAME);
		
	}
		
	
}
