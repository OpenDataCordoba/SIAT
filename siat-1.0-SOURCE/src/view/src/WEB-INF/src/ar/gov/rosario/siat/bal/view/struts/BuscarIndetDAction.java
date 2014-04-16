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

import ar.gov.rosario.siat.bal.iface.model.IndetSearchPage;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarIndetDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarIndetDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_INDET, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			IndetSearchPage indetSearchPageVO = BalServiceLocator.getIndetService().getIndetSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (indetSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + indetSearchPageVO.infoString()); 
				saveDemodaErrors(request, indetSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, IndetSearchPage.NAME, indetSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (indetSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + indetSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, IndetSearchPage.NAME, indetSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , IndetSearchPage.NAME, indetSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_INDET_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, IndetSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, IndetSearchPage.NAME);
		
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
			IndetSearchPage indetSearchPageVO = (IndetSearchPage) userSession.get(IndetSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (indetSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + IndetSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, IndetSearchPage.NAME); 
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
				return forwardErrorRecoverable(mapping, request, userSession, IndetSearchPage.NAME, indetSearchPageVO);
			}
				
			// Llamada al servicio	
			indetSearchPageVO = BalServiceLocator.getIndetService().getIndetSearchPageResult(userSession, indetSearchPageVO);			

			// Tiene errores recuperables
			if (indetSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + indetSearchPageVO.infoString()); 
				saveDemodaErrors(request, indetSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, IndetSearchPage.NAME, indetSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (indetSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + indetSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, IndetSearchPage.NAME, indetSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(IndetSearchPage.NAME, indetSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(IndetSearchPage.NAME, indetSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_INDET_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, IndetSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_INDET);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		return forwardAgregarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_INDET);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_INDET);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_INDET);

	}
		
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, IndetSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, IndetSearchPage.NAME);
		
	}
		
	public ActionForward reingresar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_INDET, BalConstants.ACT_REINGRESAR_INDET);

	}
	
	public ActionForward desgloce(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_INDET, BalConstants.ACT_DESGLOCE_INDET);
	}
	
	public ActionForward generarSaldoAFavor(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_INDET, BalConstants.ACT_GENERAR_SALDO_A_FAVOR);

	}
}
