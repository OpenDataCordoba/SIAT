//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.bal.iface.model.CierreBancoSearchPage;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;


public class BuscarCierreBancoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarCierreBancoDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_CIERREBANCO, act);		
		if (userSession==null) return forwardErrorSession(request);
		
		
		try {
		
			CierreBancoSearchPage cierreBancoSearchPageVO = BalServiceLocator.getConciliacionOsirisService().getCierreBancoSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (cierreBancoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cierreBancoSearchPageVO.infoString()); 
				saveDemodaErrors(request, cierreBancoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, CierreBancoSearchPage.NAME, cierreBancoSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (cierreBancoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cierreBancoSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CierreBancoSearchPage.NAME, cierreBancoSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , CierreBancoSearchPage.NAME, cierreBancoSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_CIERREBANCO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CierreBancoSearchPage.NAME);
		}
	}
	
	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return this.baseRefill(mapping, form, request, response, funcName, CierreBancoSearchPage.NAME);

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
			CierreBancoSearchPage cierreBancoSearchPageVO = (CierreBancoSearchPage) userSession.get(CierreBancoSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (cierreBancoSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + CierreBancoSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CierreBancoSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(cierreBancoSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				cierreBancoSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (cierreBancoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cierreBancoSearchPageVO.infoString()); 
				saveDemodaErrors(request, cierreBancoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, CierreBancoSearchPage.NAME, cierreBancoSearchPageVO);
			}

			// Llamada al servicio	
			cierreBancoSearchPageVO = BalServiceLocator.getConciliacionOsirisService().getCierreBancoSearchPageResult(userSession,cierreBancoSearchPageVO);			

			// Tiene errores recuperables
			if (cierreBancoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cierreBancoSearchPageVO.infoString()); 
				saveDemodaErrors(request, cierreBancoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, CierreBancoSearchPage.NAME, cierreBancoSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (cierreBancoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cierreBancoSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, CierreBancoSearchPage.NAME, cierreBancoSearchPageVO);
			}
			
			// Envio el VO al request
			request.setAttribute(CierreBancoSearchPage.NAME, cierreBancoSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(CierreBancoSearchPage.NAME, cierreBancoSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_CIERREBANCO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CierreBancoSearchPage.NAME);
		}
	}

	public ActionForward conciliar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_CIERREBANCO, BaseConstants.ACT_CONCILIAR);
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			return baseVolver(mapping, form, request, response, CierreBancoSearchPage.NAME);
	}


}
