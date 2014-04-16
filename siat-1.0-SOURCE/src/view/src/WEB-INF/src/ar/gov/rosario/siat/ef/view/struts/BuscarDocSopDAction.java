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
import ar.gov.rosario.siat.ef.iface.model.DocSopSearchPage;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarDocSopDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarDocSopDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_DOCSOP, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			DocSopSearchPage docSopSearchPageVO = EfServiceLocator.getDefinicionService().getDocSopSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (docSopSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + docSopSearchPageVO.infoString()); 
				saveDemodaErrors(request, docSopSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, DocSopSearchPage.NAME, docSopSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (docSopSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + docSopSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DocSopSearchPage.NAME, docSopSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , DocSopSearchPage.NAME, docSopSearchPageVO);
			
			return mapping.findForward(EfConstants.FWD_DOCSOP_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DocSopSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, DocSopSearchPage.NAME);
		
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
			DocSopSearchPage docSopSearchPageVO = (DocSopSearchPage) userSession.get(DocSopSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (docSopSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + DocSopSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DocSopSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(docSopSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				docSopSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (docSopSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + docSopSearchPageVO.infoString()); 
				saveDemodaErrors(request, docSopSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, DocSopSearchPage.NAME, docSopSearchPageVO);
			}
				
			// Llamada al servicio	
			docSopSearchPageVO = EfServiceLocator.getDefinicionService().getDocSopSearchPageResult(userSession, docSopSearchPageVO);			

			// Tiene errores recuperables
			if (docSopSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + docSopSearchPageVO.infoString()); 
				saveDemodaErrors(request, docSopSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, DocSopSearchPage.NAME, docSopSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (docSopSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + docSopSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, DocSopSearchPage.NAME, docSopSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(DocSopSearchPage.NAME, docSopSearchPageVO);
			// Nuleo el list result
			//docSopSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(DocSopSearchPage.NAME, docSopSearchPageVO);
			
			return mapping.findForward(EfConstants.FWD_DOCSOP_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DocSopSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_DOCSOP);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		return forwardAgregarSearchPage(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_DOCSOP);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_DOCSOP);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_DOCSOP);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_DOCSOP);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_DOCSOP);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, DocSopSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, DocSopSearchPage.NAME);
		
	}
		
	
}
