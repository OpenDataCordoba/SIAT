//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.fra.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.fra.iface.model.FraseSearchPage;
import ar.gov.rosario.siat.fra.iface.service.FraServiceLocator;
import ar.gov.rosario.siat.fra.iface.util.FraSecurityConstants;
import ar.gov.rosario.siat.fra.view.util.FraConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarFraseDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarFraseDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, FraSecurityConstants.ABM_FRASE, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			FraseSearchPage fraseSearchPageVO = FraServiceLocator.getFraseService().getFraseSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (fraseSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + fraseSearchPageVO.infoString()); 
				saveDemodaErrors(request, fraseSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, FraseSearchPage.NAME, fraseSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (fraseSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + fraseSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, FraseSearchPage.NAME, fraseSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , FraseSearchPage.NAME, fraseSearchPageVO);
			
			return mapping.findForward(FraConstants.FWD_FRASE_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, FraseSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, FraseSearchPage.NAME);
		
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
			FraseSearchPage fraseSearchPageVO = (FraseSearchPage) userSession.get(FraseSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (fraseSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + FraseSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, FraseSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(fraseSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				fraseSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (fraseSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + fraseSearchPageVO.infoString()); 
				saveDemodaErrors(request, fraseSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, FraseSearchPage.NAME, fraseSearchPageVO);
			}
				
			// Llamada al servicio	
			fraseSearchPageVO = FraServiceLocator.getFraseService().getFraseSearchPageResult(userSession, fraseSearchPageVO);			

			// Tiene errores recuperables
			if (fraseSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + fraseSearchPageVO.infoString()); 
				saveDemodaErrors(request, fraseSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, FraseSearchPage.NAME, fraseSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (fraseSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + fraseSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, FraseSearchPage.NAME, fraseSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(FraseSearchPage.NAME, fraseSearchPageVO);
			// Nuleo el list result
			//fraseSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(FraseSearchPage.NAME, fraseSearchPageVO);
			
			return mapping.findForward(FraConstants.FWD_FRASE_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, FraseSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, FraConstants.ACTION_ADMINISTRAR_FRASE);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, FraConstants.ACTION_ADMINISTRAR_FRASE);
	}

	public ActionForward publicar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return baseForwardSearchPage (mapping, request, funcName, FraConstants.ACTION_ADMINISTRAR_FRASE, 
				FraConstants.ACT_PUBLICAR);
	}


	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, FraseSearchPage.NAME);
		
	}
		
	
}
