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

import ar.gov.rosario.siat.bal.iface.model.MovBanSearchPage;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarMovBanDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarMovBanDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_MOVBAN, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			MovBanSearchPage movBanSearchPageVO = BalServiceLocator.getConciliacionOsirisService().getMovBanSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (movBanSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + movBanSearchPageVO.infoString()); 
				saveDemodaErrors(request, movBanSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, MovBanSearchPage.NAME, movBanSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (movBanSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + movBanSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, MovBanSearchPage.NAME, movBanSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , MovBanSearchPage.NAME, movBanSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_MOVBAN_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, MovBanSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, MovBanSearchPage.NAME);
		
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
			MovBanSearchPage movBanSearchPageVO = (MovBanSearchPage) userSession.get(MovBanSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (movBanSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + MovBanSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, MovBanSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(movBanSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				movBanSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (movBanSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + movBanSearchPageVO.infoString()); 
				saveDemodaErrors(request, movBanSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, MovBanSearchPage.NAME, movBanSearchPageVO);
			}
				
			// Llamada al servicio	
			movBanSearchPageVO = BalServiceLocator.getConciliacionOsirisService().getMovBanSearchPageResult(userSession, movBanSearchPageVO);			

			// Tiene errores recuperables
			if (movBanSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + movBanSearchPageVO.infoString()); 
				saveDemodaErrors(request, movBanSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, MovBanSearchPage.NAME, movBanSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (movBanSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + movBanSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, MovBanSearchPage.NAME, movBanSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(MovBanSearchPage.NAME, movBanSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(MovBanSearchPage.NAME, movBanSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_MOVBAN_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, MovBanSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_MOVBAN);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
	
		return forwardAgregarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_ENC_MOVBAN);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_MOVBAN);

	}
	
	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardModificarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_MOVBAN);

	}
	
	public ActionForward conciliar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return baseForwardSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_MOVBAN, BaseConstants.ACT_CONCILIAR);			
	}
	
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, MovBanSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, MovBanSearchPage.NAME);
		
	}
		
	
}
