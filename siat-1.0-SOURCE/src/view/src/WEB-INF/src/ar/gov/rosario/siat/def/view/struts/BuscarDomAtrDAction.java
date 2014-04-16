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
import ar.gov.rosario.siat.def.iface.model.DomAtrSearchPage;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarDomAtrDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarDomAtrDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_DOMINIO_ATRIBUTO, act);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			DomAtrSearchPage domAtrSearchPageVO = DefServiceLocator.getAtributoService().getDomAtrSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (domAtrSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + domAtrSearchPageVO.infoString()); 
				saveDemodaErrors(request, domAtrSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, DomAtrSearchPage.NAME, domAtrSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (domAtrSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + domAtrSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DomAtrSearchPage.NAME, domAtrSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , DomAtrSearchPage.NAME, domAtrSearchPageVO);
			
			return mapping.findForward(DefConstants.FWD_DOMATR_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DomAtrSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, DomAtrSearchPage.NAME);

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
			DomAtrSearchPage domAtrSearchPageVO = (DomAtrSearchPage) userSession.get(DomAtrSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (domAtrSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + DomAtrSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DomAtrSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(domAtrSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				domAtrSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (domAtrSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + domAtrSearchPageVO.infoString()); 
				saveDemodaErrors(request, domAtrSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, DomAtrSearchPage.NAME, domAtrSearchPageVO);
			}

			// Llamada al servicio	
			domAtrSearchPageVO = DefServiceLocator.getAtributoService().getDomAtrSearchPageResult(userSession, domAtrSearchPageVO);			

			// Tiene errores recuperables
			if (domAtrSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + domAtrSearchPageVO.infoString()); 
				saveDemodaErrors(request, domAtrSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, DomAtrSearchPage.NAME, domAtrSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (domAtrSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + domAtrSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, DomAtrSearchPage.NAME, domAtrSearchPageVO);
			}
			
			// Envio el VO al request
			request.setAttribute(DomAtrSearchPage.NAME, domAtrSearchPageVO);
			// Nuleo el list result
			//domAtrSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(DomAtrSearchPage.NAME, domAtrSearchPageVO);
			
			return mapping.findForward(DefConstants.FWD_DOMATR_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DomAtrSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_DOMATR);
	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_ENCDOMATR);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_DOMATR);	
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_DOMATR);
		
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_DOMATR);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_DOMATR);
	}

	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, DomAtrSearchPage.NAME);
	}	

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		return baseVolver(mapping, form, request, response, DomAtrSearchPage.NAME);
		
	}

}