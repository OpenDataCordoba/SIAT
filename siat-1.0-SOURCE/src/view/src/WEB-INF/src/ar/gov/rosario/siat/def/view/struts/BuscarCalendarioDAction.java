//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.def.iface.model.CalendarioSearchPage;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarCalendarioDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarCalendarioDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_CALENDARIO, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			CalendarioSearchPage calendarioSearchPageVO = DefServiceLocator.getGravamenService().getCalendarioSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (calendarioSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + calendarioSearchPageVO.infoString()); 
				saveDemodaErrors(request, calendarioSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, CalendarioSearchPage.NAME, calendarioSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (calendarioSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + calendarioSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CalendarioSearchPage.NAME, calendarioSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , CalendarioSearchPage.NAME, calendarioSearchPageVO);
			
			return mapping.findForward(DefConstants.FWD_CALENDARIO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CalendarioSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, CalendarioSearchPage.NAME);
		
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
			CalendarioSearchPage calendarioSearchPageVO = (CalendarioSearchPage) userSession.get(CalendarioSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (calendarioSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + CalendarioSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CalendarioSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(calendarioSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				calendarioSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (calendarioSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + calendarioSearchPageVO.infoString()); 
				saveDemodaErrors(request, calendarioSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, CalendarioSearchPage.NAME, calendarioSearchPageVO);
			}
				
			// Llamada al servicio	
			calendarioSearchPageVO = DefServiceLocator.getGravamenService().getCalendarioSearchPageResult(userSession, calendarioSearchPageVO);			

			// Tiene errores recuperables
			if (calendarioSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + calendarioSearchPageVO.infoString()); 
				saveDemodaErrors(request, calendarioSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, CalendarioSearchPage.NAME, calendarioSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (calendarioSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + calendarioSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, CalendarioSearchPage.NAME, calendarioSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(CalendarioSearchPage.NAME, calendarioSearchPageVO);
			// Nuleo el list result
			//calendarioSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(CalendarioSearchPage.NAME, calendarioSearchPageVO);
			
			return mapping.findForward(DefConstants.FWD_CALENDARIO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CalendarioSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_CALENDARIO);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		return forwardAgregarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_CALENDARIO);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_CALENDARIO);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_CALENDARIO);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_CALENDARIO);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_CALENDARIO);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, CalendarioSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, CalendarioSearchPage.NAME);
		
	}
		
	
}
