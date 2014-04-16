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
import ar.gov.rosario.siat.def.iface.model.AtributoSearchPage;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarAtributoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarAtributoDAction.class);
	
	public final static String  LIST_ATRIBUTOS_EXCLUIDOS = "listAtributosExcluidos";
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_ATRIBUTO, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			AtributoSearchPage atributoSearchPageVO = DefServiceLocator.getAtributoService().getAtributoSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (atributoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + atributoSearchPageVO.infoString()); 
				saveDemodaErrors(request, atributoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, AtributoSearchPage.NAME, atributoSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (atributoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + atributoSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AtributoSearchPage.NAME, atributoSearchPageVO);
			}

			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , AtributoSearchPage.NAME, 
				atributoSearchPageVO);

			return mapping.findForward(DefConstants.FWD_ATRIBUTO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AtributoSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, AtributoSearchPage.NAME);

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
			AtributoSearchPage atributoSearchPageVO = (AtributoSearchPage) userSession.get(AtributoSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (atributoSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + AtributoSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AtributoSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(atributoSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				atributoSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (atributoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + atributoSearchPageVO.infoString()); 
				saveDemodaErrors(request, atributoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, AtributoSearchPage.NAME, atributoSearchPageVO);
			}
				
			// Llamada al servicio	
			atributoSearchPageVO = DefServiceLocator.getAtributoService().getAtributoSearchPageResult(userSession, atributoSearchPageVO);			

			// Tiene errores recuperables
			if (atributoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + atributoSearchPageVO.infoString()); 
				saveDemodaErrors(request, atributoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, AtributoSearchPage.NAME, atributoSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (atributoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + atributoSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, AtributoSearchPage.NAME, atributoSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(AtributoSearchPage.NAME, atributoSearchPageVO);
			// Nuleo el list result
			//atributoSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(AtributoSearchPage.NAME, atributoSearchPageVO);
			
			return mapping.findForward(DefConstants.FWD_ATRIBUTO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AtributoSearchPage.NAME);
		}
	}


	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_ATRIBUTO);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_ATRIBUTO);
		
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_ATRIBUTO);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_ATRIBUTO);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_ATRIBUTO);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_ATRIBUTO);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, AtributoSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, AtributoSearchPage.NAME);
		
	}

}
