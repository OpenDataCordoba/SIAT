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
import ar.gov.rosario.siat.def.iface.model.CodEmiSearchPage;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarCodEmiDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarCodEmiDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_CODEMI, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			CodEmiSearchPage codEmiSearchPageVO = DefServiceLocator.getEmisionService().getCodEmiSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (codEmiSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + codEmiSearchPageVO.infoString()); 
				saveDemodaErrors(request, codEmiSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, CodEmiSearchPage.NAME, codEmiSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (codEmiSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + codEmiSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CodEmiSearchPage.NAME, codEmiSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , CodEmiSearchPage.NAME, codEmiSearchPageVO);
			
			return mapping.findForward(DefConstants.FWD_CODEMI_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CodEmiSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, CodEmiSearchPage.NAME);
		
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
			CodEmiSearchPage codEmiSearchPageVO = (CodEmiSearchPage) userSession.get(CodEmiSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (codEmiSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + CodEmiSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CodEmiSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(codEmiSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				codEmiSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (codEmiSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + codEmiSearchPageVO.infoString()); 
				saveDemodaErrors(request, codEmiSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, CodEmiSearchPage.NAME, codEmiSearchPageVO);
			}
				
			// Llamada al servicio	
			codEmiSearchPageVO = DefServiceLocator.getEmisionService().getCodEmiSearchPageResult(userSession, codEmiSearchPageVO);			

			// Tiene errores recuperables
			if (codEmiSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + codEmiSearchPageVO.infoString()); 
				saveDemodaErrors(request, codEmiSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, CodEmiSearchPage.NAME, codEmiSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (codEmiSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + codEmiSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, CodEmiSearchPage.NAME, codEmiSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(CodEmiSearchPage.NAME, codEmiSearchPageVO);

			// Subo en el el searchPage al userSession
			userSession.put(CodEmiSearchPage.NAME, codEmiSearchPageVO);
			
			return mapping.findForward(DefConstants.FWD_CODEMI_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CodEmiSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_CODEMI);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_ENC_CODEMI);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_CODEMI);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_CODEMI);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_CODEMI);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_CODEMI);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, CodEmiSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, CodEmiSearchPage.NAME);
		
	}
		
	
}
