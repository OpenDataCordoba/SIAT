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

import ar.gov.rosario.siat.bal.iface.model.DupliceSearchPage;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class BuscarDupliceDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarDupliceDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_DUPLICE, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			DupliceSearchPage dupliceSearchPageVO = BalServiceLocator.getCompensacionService().getDupliceSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (dupliceSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + dupliceSearchPageVO.infoString()); 
				saveDemodaErrors(request, dupliceSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, DupliceSearchPage.NAME, dupliceSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (dupliceSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + dupliceSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DupliceSearchPage.NAME, dupliceSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , DupliceSearchPage.NAME, dupliceSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_DUPLICE_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DupliceSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, DupliceSearchPage.NAME);
		
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
			DupliceSearchPage dupliceSearchPageVO = (DupliceSearchPage) userSession.get(DupliceSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (dupliceSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + DupliceSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DupliceSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(dupliceSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				dupliceSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (dupliceSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + dupliceSearchPageVO.infoString()); 
				saveDemodaErrors(request, dupliceSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, DupliceSearchPage.NAME, dupliceSearchPageVO);
			}
				
			// Llamada al servicio	
			dupliceSearchPageVO = BalServiceLocator.getCompensacionService().getDupliceSearchPageResult(userSession, dupliceSearchPageVO);			

			// Tiene errores recuperables
			if (dupliceSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + dupliceSearchPageVO.infoString()); 
				saveDemodaErrors(request, dupliceSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, DupliceSearchPage.NAME, dupliceSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (dupliceSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + dupliceSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, DupliceSearchPage.NAME, dupliceSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(DupliceSearchPage.NAME, dupliceSearchPageVO);

			// Subo en el el searchPage al userSession
			userSession.put(DupliceSearchPage.NAME, dupliceSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_DUPLICE_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DupliceSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_DUPLICE);

	}
		
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, DupliceSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, DupliceSearchPage.NAME);
		
	}
		
	public ActionForward imputar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_DUPLICE, BalConstants.ACT_IMPUTAR);

	}
	
	public ActionForward generarSaldoAFavor(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_DUPLICE, BalConstants.ACT_GENERAR_SALDO_A_FAVOR);

	}
	
	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardEliminarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_DUPLICE);
	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			
			return forwardAgregarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_DUPLICE);
		}
}
