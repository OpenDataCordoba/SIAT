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

import ar.gov.rosario.siat.bal.iface.model.AsentamientoSearchPage;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class BuscarAsentamientoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarAsentamientoDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_ASENTAMIENTO, act);		
		if (userSession==null) return forwardErrorSession(request);
		
		//Chequeo de acceso por instancia
		if (!userSession.getEsAdmin() && SiatParam.isWebSiat() && !SiatParam.isIntranetSiat()) {
			return forwardFuncionNoDisponible(request);
		}

		try {
		
			AsentamientoSearchPage asentamientoSearchPageVO = BalServiceLocator.getAsentamientoService().getAsentamientoSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (asentamientoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + asentamientoSearchPageVO.infoString()); 
				saveDemodaErrors(request, asentamientoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, AsentamientoSearchPage.NAME, asentamientoSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (asentamientoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + asentamientoSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AsentamientoSearchPage.NAME, asentamientoSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , AsentamientoSearchPage.NAME, asentamientoSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_ASENTAMIENTO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AsentamientoSearchPage.NAME);
		}
	}
	
	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return this.baseRefill(mapping, form, request, response, funcName, AsentamientoSearchPage.NAME);

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
			AsentamientoSearchPage asentamientoSearchPageVO = (AsentamientoSearchPage) userSession.get(AsentamientoSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (asentamientoSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + AsentamientoSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AsentamientoSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(asentamientoSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				asentamientoSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (asentamientoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + asentamientoSearchPageVO.infoString()); 
				saveDemodaErrors(request, asentamientoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, AsentamientoSearchPage.NAME, asentamientoSearchPageVO);
			}

			// Llamada al servicio	
			asentamientoSearchPageVO = BalServiceLocator.getAsentamientoService().getAsentamientoSearchPageResult(userSession, asentamientoSearchPageVO);			

			// Tiene errores recuperables
			if (asentamientoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + asentamientoSearchPageVO.infoString()); 
				saveDemodaErrors(request, asentamientoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, AsentamientoSearchPage.NAME, asentamientoSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (asentamientoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + asentamientoSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, AsentamientoSearchPage.NAME, asentamientoSearchPageVO);
			}
			
			// Envio el VO al request
			request.setAttribute(AsentamientoSearchPage.NAME, asentamientoSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(AsentamientoSearchPage.NAME, asentamientoSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_ASENTAMIENTO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AsentamientoSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardVerSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_ASENTAMIENTO);
	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardAgregarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_ASENTAMIENTO);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardModificarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_ASENTAMIENTO);	
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardEliminarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_ASENTAMIENTO);
			
		}

	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseSeleccionar(mapping, request, response, funcName, AsentamientoSearchPage.NAME);
	}	

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			return baseVolver(mapping, form, request, response, AsentamientoSearchPage.NAME);
			
	}

	public ActionForward admProceso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_PROCESO_ASENTAMIENTO, BalConstants.ACT_ADM_PROCESO_ASENTAMIENTO);
	}

}
