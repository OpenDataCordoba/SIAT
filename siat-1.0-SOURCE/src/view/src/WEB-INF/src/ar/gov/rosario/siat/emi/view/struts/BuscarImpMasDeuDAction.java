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
import ar.gov.rosario.siat.emi.iface.model.ImpMasDeuSearchPage;
import ar.gov.rosario.siat.emi.iface.service.EmiServiceLocator;
import ar.gov.rosario.siat.emi.iface.util.EmiSecurityConstants;
import ar.gov.rosario.siat.emi.view.util.EmiConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarImpMasDeuDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarImpMasDeuDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, EmiSecurityConstants.ABM_IMPMASDEU, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			ImpMasDeuSearchPage impMasDeuSearchPageVO = EmiServiceLocator
						.getImpresionService().getImpMasDeuSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (impMasDeuSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + impMasDeuSearchPageVO.infoString()); 
				saveDemodaErrors(request, impMasDeuSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ImpMasDeuSearchPage.NAME, impMasDeuSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (impMasDeuSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + impMasDeuSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ImpMasDeuSearchPage.NAME, impMasDeuSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , ImpMasDeuSearchPage.NAME, impMasDeuSearchPageVO);
			
			return mapping.findForward(EmiConstants.FWD_IMPMASDEU_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ImpMasDeuSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, ImpMasDeuSearchPage.NAME);
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
			ImpMasDeuSearchPage impMasDeuSearchPageVO = (ImpMasDeuSearchPage) userSession.get(ImpMasDeuSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (impMasDeuSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + ImpMasDeuSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ImpMasDeuSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(impMasDeuSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				impMasDeuSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (impMasDeuSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + impMasDeuSearchPageVO.infoString()); 
				saveDemodaErrors(request, impMasDeuSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ImpMasDeuSearchPage.NAME, impMasDeuSearchPageVO);
			}
				
			// Llamada al servicio	
			impMasDeuSearchPageVO = EmiServiceLocator.getImpresionService().getImpMasDeuSearchPageResult(userSession, impMasDeuSearchPageVO);			

			// Tiene errores recuperables
			if (impMasDeuSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + impMasDeuSearchPageVO.infoString()); 
				saveDemodaErrors(request, impMasDeuSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ImpMasDeuSearchPage.NAME, impMasDeuSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (impMasDeuSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + impMasDeuSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, ImpMasDeuSearchPage.NAME, impMasDeuSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(ImpMasDeuSearchPage.NAME, impMasDeuSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(ImpMasDeuSearchPage.NAME, impMasDeuSearchPageVO);
			
			return mapping.findForward(EmiConstants.FWD_IMPMASDEU_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ImpMasDeuSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, EmiConstants.ACTION_ADMINISTRAR_IMPMASDEU);
	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarSearchPage(mapping, request, funcName, EmiConstants.ACTION_ADMINISTRAR_IMPMASDEU);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, EmiConstants.ACTION_ADMINISTRAR_IMPMASDEU);
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, EmiConstants.ACTION_ADMINISTRAR_IMPMASDEU);
	}

	public ActionForward administrarProceso(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardSearchPage(mapping, request, funcName, 
					EmiConstants.ACTION_ADMINISTRAR_PROCESO_IMPMASDEU, 
					EmiConstants.ACT_ADM_PROCESO_IMPMASDEU);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, ImpMasDeuSearchPage.NAME);
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ImpMasDeuSearchPage.NAME);
	}
}
