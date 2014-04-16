//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.def.iface.model.BancoSearchPage;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarBancoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarBancoDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_BANCO, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			BancoSearchPage bancoSearchPageVO = DefServiceLocator.getServicioBancoService().getBancoSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (bancoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + bancoSearchPageVO.infoString()); 
				saveDemodaErrors(request, bancoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, BancoSearchPage.NAME, bancoSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (bancoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + bancoSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, BancoSearchPage.NAME, bancoSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , BancoSearchPage.NAME, bancoSearchPageVO);
			
			return mapping.findForward(DefConstants.FWD_BANCO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, BancoSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, BancoSearchPage.NAME);
		
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
			BancoSearchPage bancoSearchPageVO = (BancoSearchPage) userSession.get(BancoSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (bancoSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + BancoSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, BancoSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(bancoSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				bancoSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (bancoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + bancoSearchPageVO.infoString()); 
				saveDemodaErrors(request, bancoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, BancoSearchPage.NAME, bancoSearchPageVO);
			}
				
			// Llamada al servicio	
			bancoSearchPageVO = DefServiceLocator.getServicioBancoService().getBancoSearchPageResult(userSession, bancoSearchPageVO);			

			// Tiene errores recuperables
			if (bancoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + bancoSearchPageVO.infoString()); 
				saveDemodaErrors(request, bancoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, BancoSearchPage.NAME, bancoSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (bancoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + bancoSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, BancoSearchPage.NAME, bancoSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(BancoSearchPage.NAME, bancoSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(BancoSearchPage.NAME, bancoSearchPageVO);
			
			return mapping.findForward(DefConstants.FWD_BANCO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, BancoSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_BANCO);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		
		return forwardAgregarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_BANCO);
	
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_BANCO);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_BANCO);

	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, BancoSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, BancoSearchPage.NAME);
		
	}
		
	
}
