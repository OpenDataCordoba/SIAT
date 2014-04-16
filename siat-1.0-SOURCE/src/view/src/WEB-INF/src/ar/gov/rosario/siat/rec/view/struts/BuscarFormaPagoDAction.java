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

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.rec.iface.model.FormaPagoSearchPage;
import ar.gov.rosario.siat.rec.iface.service.RecServiceLocator;
import ar.gov.rosario.siat.rec.iface.util.RecSecurityConstants;
import ar.gov.rosario.siat.rec.view.util.RecConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;


public final class BuscarFormaPagoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarFormaPagoDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_FORMAPAGO, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			FormaPagoSearchPage formaPagoSearchPageVO = RecServiceLocator.getCdmService().getFormaPagoSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (formaPagoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + formaPagoSearchPageVO.infoString()); 
				saveDemodaErrors(request, formaPagoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, FormaPagoSearchPage.NAME, formaPagoSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (formaPagoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + formaPagoSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, FormaPagoSearchPage.NAME, formaPagoSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , FormaPagoSearchPage.NAME, formaPagoSearchPageVO);
			
			return mapping.findForward(RecConstants.FWD_FORMAPAGO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, FormaPagoSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, FormaPagoSearchPage.NAME);
		
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
			FormaPagoSearchPage formaPagoSearchPageVO = (FormaPagoSearchPage) userSession.get(FormaPagoSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (formaPagoSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + FormaPagoSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, FormaPagoSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(formaPagoSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				formaPagoSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (formaPagoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + formaPagoSearchPageVO.infoString()); 
				saveDemodaErrors(request, formaPagoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, FormaPagoSearchPage.NAME, formaPagoSearchPageVO);
			}
				
			// Llamada al servicio	
			formaPagoSearchPageVO = RecServiceLocator.getCdmService().getFormaPagoSearchPageResult(userSession, formaPagoSearchPageVO);			

			// Tiene errores recuperables
			if (formaPagoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + formaPagoSearchPageVO.infoString()); 
				saveDemodaErrors(request, formaPagoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, FormaPagoSearchPage.NAME, formaPagoSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (formaPagoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + formaPagoSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, FormaPagoSearchPage.NAME, formaPagoSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(FormaPagoSearchPage.NAME, formaPagoSearchPageVO);
			// Nuleo el list result
			//formaPagoSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(FormaPagoSearchPage.NAME, formaPagoSearchPageVO);
			
			return mapping.findForward(RecConstants.FWD_FORMAPAGO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, FormaPagoSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_FORMAPAGO);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();

		return forwardAgregarSearchPage(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_FORMAPAGO);

	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_FORMAPAGO);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_FORMAPAGO);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_FORMAPAGO);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_FORMAPAGO);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, FormaPagoSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, FormaPagoSearchPage.NAME);
		
	}
		
	
}
