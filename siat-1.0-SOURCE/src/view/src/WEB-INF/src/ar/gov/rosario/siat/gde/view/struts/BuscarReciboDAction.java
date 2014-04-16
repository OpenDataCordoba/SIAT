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
import ar.gov.rosario.siat.gde.iface.model.ReciboSearchPage;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarReciboDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarReciboDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.CONSULTAR_RECIBO, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			ReciboSearchPage reciboSearchPageVO = GdeServiceLocator.getGestionDeudaService().getReciboSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (reciboSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + reciboSearchPageVO.infoString()); 
				saveDemodaErrors(request, reciboSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ReciboSearchPage.NAME, reciboSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (reciboSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + reciboSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ReciboSearchPage.NAME, reciboSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , ReciboSearchPage.NAME, reciboSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_RECIBO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ReciboSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, ReciboSearchPage.NAME);
		
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
			ReciboSearchPage reciboSearchPageVO = (ReciboSearchPage) userSession.get(ReciboSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (reciboSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + ReciboSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ReciboSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(reciboSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				reciboSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (reciboSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + reciboSearchPageVO.infoString()); 
				saveDemodaErrors(request, reciboSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ReciboSearchPage.NAME, reciboSearchPageVO);
			}
				
			// Llamada al servicio	
			reciboSearchPageVO = GdeServiceLocator.getGestionDeudaService().getReciboSearchPageResult(userSession, reciboSearchPageVO);			

			// Tiene errores recuperables
			if (reciboSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + reciboSearchPageVO.infoString()); 
				saveDemodaErrors(request, reciboSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ReciboSearchPage.NAME, reciboSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (reciboSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + reciboSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, ReciboSearchPage.NAME, reciboSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(ReciboSearchPage.NAME, reciboSearchPageVO);
			// Nuleo el list result
			//reciboSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(ReciboSearchPage.NAME, reciboSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_RECIBO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ReciboSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_RECIBO);

	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ReciboSearchPage.NAME);
		
	}
		
	
}
