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
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.gde.iface.model.LiqCodRefPagSearchPage;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarCodRefPagDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarCodRefPagDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_CODREFPAG, BaseConstants.ACT_SELECCIONAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			LiqCodRefPagSearchPage liqCodRefPagSearchPageVO = GdeServiceLocator.getGestionDeudaService().getLiqCodRefPagSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (liqCodRefPagSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqCodRefPagSearchPageVO.infoString()); 
				saveDemodaErrors(request, liqCodRefPagSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, LiqCodRefPagSearchPage.NAME, liqCodRefPagSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (liqCodRefPagSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + liqCodRefPagSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqCodRefPagSearchPage.NAME, liqCodRefPagSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , LiqCodRefPagSearchPage.NAME, liqCodRefPagSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_CODREFPAG_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqCodRefPagSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, LiqCodRefPagSearchPage.NAME);
		
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
			LiqCodRefPagSearchPage liqCodRefPagSearchPageVO = (LiqCodRefPagSearchPage) userSession.get(LiqCodRefPagSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (liqCodRefPagSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + LiqCodRefPagSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, LiqCodRefPagSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(liqCodRefPagSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				liqCodRefPagSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (liqCodRefPagSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqCodRefPagSearchPageVO.infoString()); 
				saveDemodaErrors(request, liqCodRefPagSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, LiqCodRefPagSearchPage.NAME, liqCodRefPagSearchPageVO);
			}
				
			// Llamada al servicio	
			liqCodRefPagSearchPageVO = GdeServiceLocator.getGestionDeudaService().getLiqCodRefPagSearchPageResult(userSession, liqCodRefPagSearchPageVO);			

			// Tiene errores recuperables
			if (liqCodRefPagSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqCodRefPagSearchPageVO.infoString()); 
				saveDemodaErrors(request, liqCodRefPagSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, LiqCodRefPagSearchPage.NAME, liqCodRefPagSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (liqCodRefPagSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + liqCodRefPagSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqCodRefPagSearchPage.NAME, liqCodRefPagSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(LiqCodRefPagSearchPage.NAME, liqCodRefPagSearchPageVO);
			// Nuleo el list result
			//liqCodRefPagSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(LiqCodRefPagSearchPage.NAME, liqCodRefPagSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_CODREFPAG_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqCodRefPagSearchPage.NAME);
		}
	}



	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, LiqCodRefPagSearchPage.NAME);
		
	}
		
	
}
