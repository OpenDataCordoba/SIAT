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

import ar.gov.rosario.siat.bal.iface.model.IndetReingSearchPage;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarIndetReingDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarIndetReingDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_INDETREING, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			IndetReingSearchPage indetSearchPageVO = BalServiceLocator.getIndetService().getIndetReingSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (indetSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + indetSearchPageVO.infoString()); 
				saveDemodaErrors(request, indetSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, IndetReingSearchPage.NAME, indetSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (indetSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + indetSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, IndetReingSearchPage.NAME, indetSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , IndetReingSearchPage.NAME, indetSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_INDETREING_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, IndetReingSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, IndetReingSearchPage.NAME);
		
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
			IndetReingSearchPage indetSearchPageVO = (IndetReingSearchPage) userSession.get(IndetReingSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (indetSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + IndetReingSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, IndetReingSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(indetSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				indetSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (indetSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + indetSearchPageVO.infoString()); 
				saveDemodaErrors(request, indetSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, IndetReingSearchPage.NAME, indetSearchPageVO);
			}
				
			// Llamada al servicio	
			indetSearchPageVO = BalServiceLocator.getIndetService().getIndetReingSearchPageResult(userSession, indetSearchPageVO);			

			// Tiene errores recuperables
			if (indetSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + indetSearchPageVO.infoString()); 
				saveDemodaErrors(request, indetSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, IndetReingSearchPage.NAME, indetSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (indetSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + indetSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, IndetReingSearchPage.NAME, indetSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(IndetReingSearchPage.NAME, indetSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(IndetReingSearchPage.NAME, indetSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_INDETREING_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, IndetReingSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_INDETREING);

	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, IndetReingSearchPage.NAME);
		
	}
	
	public ActionForward vueltaAtras(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_INDETREING, BaseConstants.ACT_VUELTA_ATRAS);
	}
		
}
