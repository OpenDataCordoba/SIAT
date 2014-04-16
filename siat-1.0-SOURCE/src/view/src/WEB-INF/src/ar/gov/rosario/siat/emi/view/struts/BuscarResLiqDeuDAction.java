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
import ar.gov.rosario.siat.emi.iface.model.ResLiqDeuSearchPage;
import ar.gov.rosario.siat.emi.iface.service.EmiServiceLocator;
import ar.gov.rosario.siat.emi.iface.util.EmiSecurityConstants;
import ar.gov.rosario.siat.emi.view.util.EmiConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarResLiqDeuDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarResLiqDeuDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, EmiSecurityConstants.ABM_RESLIQDEU, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			ResLiqDeuSearchPage resLiqDeuSearchPageVO = EmiServiceLocator.getGeneralService().getResLiqDeuSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (resLiqDeuSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + resLiqDeuSearchPageVO.infoString()); 
				saveDemodaErrors(request, resLiqDeuSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ResLiqDeuSearchPage.NAME, resLiqDeuSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (resLiqDeuSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + resLiqDeuSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ResLiqDeuSearchPage.NAME, resLiqDeuSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , ResLiqDeuSearchPage.NAME, resLiqDeuSearchPageVO);
			
			return mapping.findForward(EmiConstants.FWD_RESLIQDEU_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ResLiqDeuSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, ResLiqDeuSearchPage.NAME);
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
			ResLiqDeuSearchPage resLiqDeuSearchPageVO = (ResLiqDeuSearchPage) userSession.get(ResLiqDeuSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (resLiqDeuSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + ResLiqDeuSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ResLiqDeuSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(resLiqDeuSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				resLiqDeuSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (resLiqDeuSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + resLiqDeuSearchPageVO.infoString()); 
				saveDemodaErrors(request, resLiqDeuSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ResLiqDeuSearchPage.NAME, resLiqDeuSearchPageVO);
			}
				
			// Llamada al servicio	
			resLiqDeuSearchPageVO = EmiServiceLocator.getGeneralService().getResLiqDeuSearchPageResult(userSession, resLiqDeuSearchPageVO);			

			// Tiene errores recuperables
			if (resLiqDeuSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + resLiqDeuSearchPageVO.infoString()); 
				saveDemodaErrors(request, resLiqDeuSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ResLiqDeuSearchPage.NAME, resLiqDeuSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (resLiqDeuSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + resLiqDeuSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, ResLiqDeuSearchPage.NAME, resLiqDeuSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(ResLiqDeuSearchPage.NAME, resLiqDeuSearchPageVO);

			// Subo en el el searchPage al userSession
			userSession.put(ResLiqDeuSearchPage.NAME, resLiqDeuSearchPageVO);
			
			return mapping.findForward(EmiConstants.FWD_RESLIQDEU_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ResLiqDeuSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, EmiConstants.ACTION_ADMINISTRAR_RESLIQDEU);
	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarSearchPage(mapping, request, funcName, EmiConstants.ACTION_ADMINISTRAR_RESLIQDEU);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, EmiConstants.ACTION_ADMINISTRAR_RESLIQDEU);
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, EmiConstants.ACTION_ADMINISTRAR_RESLIQDEU);
	}
	
	public ActionForward administrarProceso(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardSearchPage(mapping, request, funcName, 
				EmiConstants.ACTION_ADMINISTRAR_PROCESO_RESLIQDEU, 
				EmiSecurityConstants.ACT_ADMINISTRAR_PROCESO);
	}

	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, ResLiqDeuSearchPage.NAME);
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ResLiqDeuSearchPage.NAME);
	}
}