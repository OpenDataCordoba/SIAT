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
import ar.gov.rosario.siat.emi.iface.model.EmisionExternaSearchPage;
import ar.gov.rosario.siat.emi.iface.service.EmiServiceLocator;
import ar.gov.rosario.siat.emi.iface.util.EmiSecurityConstants;
import ar.gov.rosario.siat.emi.view.util.EmiConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarEmisionExternaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarEmisionExternaDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, EmiSecurityConstants.ABM_EMISION_EXTERNA, act); 
		if (userSession == null) return forwardErrorSession(request);
		try {
			EmisionExternaSearchPage emisionExternaSearchPageVO = EmiServiceLocator.getEmisionService().getEmisionExternaSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (emisionExternaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionExternaSearchPageVO.infoString()); 
				saveDemodaErrors(request, emisionExternaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, EmisionExternaSearchPage.NAME, emisionExternaSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (emisionExternaSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + emisionExternaSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EmisionExternaSearchPage.NAME, emisionExternaSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , EmisionExternaSearchPage.NAME, emisionExternaSearchPageVO);
			
			return mapping.findForward(EmiConstants.FWD_EMISIONEXTERNA_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionExternaSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, EmisionExternaSearchPage.NAME);
		
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
			EmisionExternaSearchPage emisionExternaSearchPageVO = (EmisionExternaSearchPage) userSession.get(EmisionExternaSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (emisionExternaSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + EmisionExternaSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EmisionExternaSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(emisionExternaSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				emisionExternaSearchPageVO.setPageNumber(new Long((String) userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (emisionExternaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionExternaSearchPageVO.infoString()); 
				saveDemodaErrors(request, emisionExternaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, EmisionExternaSearchPage.NAME, emisionExternaSearchPageVO);
			}
				
			// Llamada al servicio	
			emisionExternaSearchPageVO = EmiServiceLocator.getEmisionService()
										.getEmisionExternaSearchPageResult(userSession, emisionExternaSearchPageVO);			

			// Tiene errores recuperables
			if (emisionExternaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionExternaSearchPageVO.infoString()); 
				saveDemodaErrors(request, emisionExternaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, EmisionExternaSearchPage.NAME, emisionExternaSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (emisionExternaSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + emisionExternaSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, EmisionExternaSearchPage.NAME, emisionExternaSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(EmisionExternaSearchPage.NAME, emisionExternaSearchPageVO);

			// Subo en el el searchPage al userSession
			userSession.put(EmisionExternaSearchPage.NAME, emisionExternaSearchPageVO);
			
			return mapping.findForward(EmiConstants.FWD_EMISIONEXTERNA_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionExternaSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, EmiConstants.ACTION_ADMINISTRAR_EMISIONEXTERNA);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarSearchPage(mapping, request, funcName, EmiConstants.ACTION_ADMINISTRAR_EMISIONEXTERNA);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, EmiConstants.ACTION_ADMINISTRAR_EMISIONEXTERNA);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, EmiConstants.ACTION_ADMINISTRAR_EMISIONEXTERNA);

	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, EmisionExternaSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, EmisionExternaSearchPage.NAME);
		
	}

	public ActionForward administrarProceso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardSearchPage(mapping, request, funcName, 
					EmiConstants.ACTION_ADMINISTRAR_PROCESO_EMISIONEXTERNA, 
					EmiSecurityConstants.ACT_ADMINISTRAR_PROCESO);
		}

}
