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

import ar.gov.rosario.siat.bal.iface.model.TipCueBanSearchPage;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarTipCueBanDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarTipCueBanDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_TIPCUEBAN, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			TipCueBanSearchPage tipCueBanSearchPageVO = BalServiceLocator.getDefinicionService().getTipCueBanSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (tipCueBanSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipCueBanSearchPageVO.infoString()); 
				saveDemodaErrors(request, tipCueBanSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipCueBanSearchPage.NAME, tipCueBanSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (tipCueBanSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipCueBanSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipCueBanSearchPage.NAME, tipCueBanSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , TipCueBanSearchPage.NAME, tipCueBanSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_TIPCUEBAN_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipCueBanSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, TipCueBanSearchPage.NAME);
		
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
			TipCueBanSearchPage tipCueBanSearchPageVO = (TipCueBanSearchPage) userSession.get(TipCueBanSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (tipCueBanSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + TipCueBanSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipCueBanSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(tipCueBanSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				tipCueBanSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (tipCueBanSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipCueBanSearchPageVO.infoString()); 
				saveDemodaErrors(request, tipCueBanSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipCueBanSearchPage.NAME, tipCueBanSearchPageVO);
			}
				
			// Llamada al servicio	
			tipCueBanSearchPageVO = BalServiceLocator.getDefinicionService().getTipCueBanSearchPageResult(userSession, tipCueBanSearchPageVO);			

			// Tiene errores recuperables
			if (tipCueBanSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipCueBanSearchPageVO.infoString()); 
				saveDemodaErrors(request, tipCueBanSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipCueBanSearchPage.NAME, tipCueBanSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (tipCueBanSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipCueBanSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, TipCueBanSearchPage.NAME, tipCueBanSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(TipCueBanSearchPage.NAME, tipCueBanSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(TipCueBanSearchPage.NAME, tipCueBanSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_TIPCUEBAN_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipCueBanSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_TIPCUEBAN);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		
		return forwardAgregarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_TIPCUEBAN);
	
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_TIPCUEBAN);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_TIPCUEBAN);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_TIPCUEBAN);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_TIPCUEBAN);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, TipCueBanSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, TipCueBanSearchPage.NAME);
		
	}
		
	
}
