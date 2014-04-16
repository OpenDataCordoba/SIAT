//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.gde.iface.model.LiqComSearchPage;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarLiqComDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarLiqComDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_LIQCOM, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			LiqComSearchPage liqComSearchPageVO = GdeServiceLocator.getGestionDeudaJudicialService().getLiqComSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (liqComSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqComSearchPageVO.infoString()); 
				saveDemodaErrors(request, liqComSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, LiqComSearchPage.NAME, liqComSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (liqComSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + liqComSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqComSearchPage.NAME, liqComSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , LiqComSearchPage.NAME, liqComSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_LIQCOM_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqComSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, LiqComSearchPage.NAME);
		
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
			LiqComSearchPage liqComSearchPageVO = (LiqComSearchPage) userSession.get(LiqComSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (liqComSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + LiqComSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, LiqComSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(liqComSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				liqComSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (liqComSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqComSearchPageVO.infoString()); 
				saveDemodaErrors(request, liqComSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, LiqComSearchPage.NAME, liqComSearchPageVO);
			}
				
			// Llamada al servicio	
			liqComSearchPageVO = GdeServiceLocator.getGestionDeudaJudicialService().getLiqComSearchPageResult(userSession, liqComSearchPageVO);			

			// Tiene errores recuperables
			if (liqComSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqComSearchPageVO.infoString()); 
				saveDemodaErrors(request, liqComSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, LiqComSearchPage.NAME, liqComSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (liqComSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + liqComSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqComSearchPage.NAME, liqComSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(LiqComSearchPage.NAME, liqComSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(LiqComSearchPage.NAME, liqComSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_LIQCOM_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqComSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_LIQCOM);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		return forwardAgregarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_LIQCOM);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_LIQCOM);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_LIQCOM);

	}
	
	public ActionForward administrarProceso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardSearchPage(mapping, request, funcName, GdeConstants.ACT_ADM_PROCESO_LIQCOM, GdeConstants.ACT_ADM_PROCESO_LIQCOM);
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_LIQCOM);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_LIQCOM);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, LiqComSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, LiqComSearchPage.NAME);
		
	}
		
	public ActionForward paramRecurso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// Bajo el searchPage del userSession
			LiqComSearchPage liqComSearchPageVO = (LiqComSearchPage) userSession.get(LiqComSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (liqComSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + LiqComSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, LiqComSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(liqComSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				liqComSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (liqComSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqComSearchPageVO.infoString()); 
				saveDemodaErrors(request, liqComSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, LiqComSearchPage.NAME, liqComSearchPageVO);
			}
				
			// Llamada al servicio	
			liqComSearchPageVO = GdeServiceLocator.getGestionDeudaJudicialService().getLiqComSearchPageParamProcuradores(userSession, liqComSearchPageVO);			

			// Tiene errores recuperables
			if (liqComSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqComSearchPageVO.infoString()); 
				saveDemodaErrors(request, liqComSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, LiqComSearchPage.NAME, liqComSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (liqComSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + liqComSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqComSearchPage.NAME, liqComSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(LiqComSearchPage.NAME, liqComSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(LiqComSearchPage.NAME, liqComSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_LIQCOM_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqComSearchPage.NAME);
		}
	}
	
	public ActionForward paramServicioBanco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// Bajo el searchPage del userSession
			LiqComSearchPage liqComSearchPageVO = (LiqComSearchPage) userSession.get(LiqComSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (liqComSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + LiqComSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, LiqComSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(liqComSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				liqComSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (liqComSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqComSearchPageVO.infoString()); 
				saveDemodaErrors(request, liqComSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, LiqComSearchPage.NAME, liqComSearchPageVO);
			}
				
			// Llamada al servicio	
			liqComSearchPageVO = GdeServiceLocator.getGestionDeudaJudicialService().getLiqComSearchPageParamRecurso(userSession, liqComSearchPageVO);			

			// Tiene errores recuperables
			if (liqComSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqComSearchPageVO.infoString()); 
				saveDemodaErrors(request, liqComSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, LiqComSearchPage.NAME, liqComSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (liqComSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + liqComSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqComSearchPage.NAME, liqComSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(LiqComSearchPage.NAME, liqComSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(LiqComSearchPage.NAME, liqComSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_LIQCOM_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqComSearchPage.NAME);
		}
	}

}
