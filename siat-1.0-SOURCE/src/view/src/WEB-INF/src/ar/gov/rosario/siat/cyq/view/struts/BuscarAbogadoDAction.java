//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.cyq.iface.model.AbogadoSearchPage;
import ar.gov.rosario.siat.cyq.iface.service.CyqServiceLocator;
import ar.gov.rosario.siat.cyq.iface.util.CyqSecurityConstants;
import ar.gov.rosario.siat.cyq.view.util.CyqConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarAbogadoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarAbogadoDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.ABM_ABOGADO, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			AbogadoSearchPage abogadoSearchPageVO = CyqServiceLocator.getDefinicionService().getAbogadoSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (abogadoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + abogadoSearchPageVO.infoString()); 
				saveDemodaErrors(request, abogadoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, AbogadoSearchPage.NAME, abogadoSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (abogadoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + abogadoSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AbogadoSearchPage.NAME, abogadoSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , AbogadoSearchPage.NAME, abogadoSearchPageVO);
			
			return mapping.findForward(CyqConstants.FWD_ABOGADO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AbogadoSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, AbogadoSearchPage.NAME);
		
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
			AbogadoSearchPage abogadoSearchPageVO = (AbogadoSearchPage) userSession.get(AbogadoSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (abogadoSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + AbogadoSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AbogadoSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(abogadoSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				abogadoSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (abogadoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + abogadoSearchPageVO.infoString()); 
				saveDemodaErrors(request, abogadoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, AbogadoSearchPage.NAME, abogadoSearchPageVO);
			}
				
			// Llamada al servicio	
			abogadoSearchPageVO = CyqServiceLocator.getDefinicionService().getAbogadoSearchPageResult(userSession, abogadoSearchPageVO);			

			// Tiene errores recuperables
			if (abogadoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + abogadoSearchPageVO.infoString()); 
				saveDemodaErrors(request, abogadoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, AbogadoSearchPage.NAME, abogadoSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (abogadoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + abogadoSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, AbogadoSearchPage.NAME, abogadoSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(AbogadoSearchPage.NAME, abogadoSearchPageVO);
			// Nuleo el list result
			//abogadoSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(AbogadoSearchPage.NAME, abogadoSearchPageVO);
			
			return mapping.findForward(CyqConstants.FWD_ABOGADO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AbogadoSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, CyqConstants.ACTION_ADMINISTRAR_ABOGADO);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		
		return forwardAgregarSearchPage(mapping, request, funcName, CyqConstants.ACTION_ADMINISTRAR_ABOGADO);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, CyqConstants.ACTION_ADMINISTRAR_ABOGADO);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, CyqConstants.ACTION_ADMINISTRAR_ABOGADO);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, CyqConstants.ACTION_ADMINISTRAR_ABOGADO);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, CyqConstants.ACTION_ADMINISTRAR_ABOGADO);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, AbogadoSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, AbogadoSearchPage.NAME);
		
	}
		
	
}
