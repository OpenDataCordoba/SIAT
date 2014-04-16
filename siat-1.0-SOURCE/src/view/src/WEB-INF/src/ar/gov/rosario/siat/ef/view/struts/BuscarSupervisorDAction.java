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
import ar.gov.rosario.siat.ef.iface.model.SupervisorSearchPage;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarSupervisorDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarSupervisorDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_SUPERVISOR, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			SupervisorSearchPage supervisorSearchPageVO = EfServiceLocator.getDefinicionService().getSupervisorSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (supervisorSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + supervisorSearchPageVO.infoString()); 
				saveDemodaErrors(request, supervisorSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, SupervisorSearchPage.NAME, supervisorSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (supervisorSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + supervisorSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SupervisorSearchPage.NAME, supervisorSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , SupervisorSearchPage.NAME, supervisorSearchPageVO);
			
			return mapping.findForward(EfConstants.FWD_SUPERVISOR_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SupervisorSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, SupervisorSearchPage.NAME);
		
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
			SupervisorSearchPage supervisorSearchPageVO = (SupervisorSearchPage) userSession.get(SupervisorSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (supervisorSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + SupervisorSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SupervisorSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(supervisorSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				supervisorSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (supervisorSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + supervisorSearchPageVO.infoString()); 
				saveDemodaErrors(request, supervisorSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, SupervisorSearchPage.NAME, supervisorSearchPageVO);
			}
				
			// Llamada al servicio	
			supervisorSearchPageVO = EfServiceLocator.getDefinicionService().getSupervisorSearchPageResult(userSession, supervisorSearchPageVO);			

			// Tiene errores recuperables
			if (supervisorSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + supervisorSearchPageVO.infoString()); 
				saveDemodaErrors(request, supervisorSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, SupervisorSearchPage.NAME, supervisorSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (supervisorSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + supervisorSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, SupervisorSearchPage.NAME, supervisorSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(SupervisorSearchPage.NAME, supervisorSearchPageVO);
			// Nuleo el list result
			//supervisorSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(SupervisorSearchPage.NAME, supervisorSearchPageVO);
			
			return mapping.findForward(EfConstants.FWD_SUPERVISOR_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SupervisorSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_SUPERVISOR);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
				
		return forwardAgregarSearchPage(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_SUPERVISOR);		
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_SUPERVISOR);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_SUPERVISOR);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_SUPERVISOR);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_SUPERVISOR);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, SupervisorSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, SupervisorSearchPage.NAME);
		
	}
		
	
}
