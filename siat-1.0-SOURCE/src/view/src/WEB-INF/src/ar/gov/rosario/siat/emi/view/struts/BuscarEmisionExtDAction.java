//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.emi.iface.model.EmisionExtSearchPage;
import ar.gov.rosario.siat.emi.iface.service.EmiServiceLocator;
import ar.gov.rosario.siat.emi.iface.util.EmiSecurityConstants;
import ar.gov.rosario.siat.emi.view.util.EmiConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarEmisionExtDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarEmisionMasDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, EmiSecurityConstants.ABM_EMISIONEXT, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			EmisionExtSearchPage emisionExtSearchPageVO = EmiServiceLocator.getEmisionService()
						.getEmisionExtSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (emisionExtSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionExtSearchPageVO.infoString()); 
				saveDemodaErrors(request, emisionExtSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, EmisionExtSearchPage.NAME, emisionExtSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (emisionExtSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + emisionExtSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EmisionExtSearchPage.NAME, emisionExtSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , EmisionExtSearchPage.NAME, emisionExtSearchPageVO);
			
			return mapping.findForward(EmiConstants.FWD_EMISIONEXT_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionExtSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, EmisionExtSearchPage.NAME);
		
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
			EmisionExtSearchPage emisionExtSearchPageVO = (EmisionExtSearchPage) userSession.get(EmisionExtSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (emisionExtSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + EmisionExtSearchPage.NAME 
						+ " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EmisionExtSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(emisionExtSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				emisionExtSearchPageVO.setPageNumber(new Long((String) userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (emisionExtSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionExtSearchPageVO.infoString()); 
				saveDemodaErrors(request, emisionExtSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, EmisionExtSearchPage.NAME, emisionExtSearchPageVO);
			}
				
			// Llamada al servicio	
			emisionExtSearchPageVO = EmiServiceLocator.getEmisionService()
					.getEmisionExtSearchPageResult(userSession, emisionExtSearchPageVO);			

			// Tiene errores recuperables
			if (emisionExtSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionExtSearchPageVO.infoString()); 
				saveDemodaErrors(request, emisionExtSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, EmisionExtSearchPage.NAME, emisionExtSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (emisionExtSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + emisionExtSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, EmisionExtSearchPage.NAME, emisionExtSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(EmisionExtSearchPage.NAME, emisionExtSearchPageVO);

			// Subo en el el searchPage al userSession
			userSession.put(EmisionExtSearchPage.NAME, emisionExtSearchPageVO);
			
			return mapping.findForward(EmiConstants.FWD_EMISIONEXT_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionExtSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, EmiConstants.ACTION_ADMINISTRAR_EMISIONEXT);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarSearchPage(mapping, request, funcName, EmiConstants.ACTION_ADMINISTRAR_EMISIONEXT);
	}

	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, EmisionExtSearchPage.NAME);
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, EmisionExtSearchPage.NAME);
	}
}
