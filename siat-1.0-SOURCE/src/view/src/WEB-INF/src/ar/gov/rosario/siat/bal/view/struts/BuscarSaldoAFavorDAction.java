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

import ar.gov.rosario.siat.bal.iface.model.SaldoAFavorSearchPage;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarSaldoAFavorDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarSaldoAFavorDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_SALDOAFAVOR, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			SaldoAFavorSearchPage saldoAFavorSearchPageVO = BalServiceLocator.getSaldoAFavorService().getSaldoAFavorSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (saldoAFavorSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + saldoAFavorSearchPageVO.infoString()); 
				saveDemodaErrors(request, saldoAFavorSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, SaldoAFavorSearchPage.NAME, saldoAFavorSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (saldoAFavorSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + saldoAFavorSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SaldoAFavorSearchPage.NAME, saldoAFavorSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , SaldoAFavorSearchPage.NAME, saldoAFavorSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_SALDOAFAVOR_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SaldoAFavorSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, SaldoAFavorSearchPage.NAME);
		
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
			SaldoAFavorSearchPage saldoAFavorSearchPageVO = (SaldoAFavorSearchPage) userSession.get(SaldoAFavorSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (saldoAFavorSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + SaldoAFavorSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SaldoAFavorSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(saldoAFavorSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				saldoAFavorSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (saldoAFavorSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + saldoAFavorSearchPageVO.infoString()); 
				saveDemodaErrors(request, saldoAFavorSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, SaldoAFavorSearchPage.NAME, saldoAFavorSearchPageVO);
			}
				
			// Llamada al servicio	
			saldoAFavorSearchPageVO = BalServiceLocator.getSaldoAFavorService().getSaldoAFavorSearchPageResult(userSession, saldoAFavorSearchPageVO);			

			// Tiene errores recuperables
			if (saldoAFavorSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + saldoAFavorSearchPageVO.infoString()); 
				saveDemodaErrors(request, saldoAFavorSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, SaldoAFavorSearchPage.NAME, saldoAFavorSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (saldoAFavorSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + saldoAFavorSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, SaldoAFavorSearchPage.NAME, saldoAFavorSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(SaldoAFavorSearchPage.NAME, saldoAFavorSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(SaldoAFavorSearchPage.NAME, saldoAFavorSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_SALDOAFAVOR_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SaldoAFavorSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_SALDOAFAVOR);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		
		return forwardAgregarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_SALDOAFAVOR);

	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_SALDOAFAVOR);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_SALDOAFAVOR);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_SALDOAFAVOR);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_SALDOAFAVOR);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, SaldoAFavorSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, SaldoAFavorSearchPage.NAME);
		
	}
		
	
}
