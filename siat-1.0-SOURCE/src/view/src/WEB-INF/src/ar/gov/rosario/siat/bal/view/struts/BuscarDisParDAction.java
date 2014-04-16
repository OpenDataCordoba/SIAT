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

import ar.gov.rosario.siat.bal.iface.model.DisParSearchPage;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class BuscarDisParDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarDisParDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_DISPAR, act);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
		
			DisParSearchPage disParSearchPageVO = BalServiceLocator.getDistribucionService().getDisParSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (disParSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + disParSearchPageVO.infoString()); 
				saveDemodaErrors(request, disParSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, DisParSearchPage.NAME, disParSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (disParSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + disParSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DisParSearchPage.NAME, disParSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , DisParSearchPage.NAME, disParSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_DISPAR_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DisParSearchPage.NAME);
		}
	}
	
	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return this.baseRefill(mapping, form, request, response, funcName, DisParSearchPage.NAME);

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
			DisParSearchPage disParSearchPageVO = (DisParSearchPage) userSession.get(DisParSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (disParSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + DisParSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DisParSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(disParSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				disParSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (disParSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + disParSearchPageVO.infoString()); 
				saveDemodaErrors(request, disParSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, DisParSearchPage.NAME, disParSearchPageVO);
			}

			// Llamada al servicio	
			disParSearchPageVO = BalServiceLocator.getDistribucionService().getDisParSearchPageResult(userSession, disParSearchPageVO);			

			// Tiene errores recuperables
			if (disParSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + disParSearchPageVO.infoString()); 
				saveDemodaErrors(request, disParSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, DisParSearchPage.NAME, disParSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (disParSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + disParSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, DisParSearchPage.NAME, disParSearchPageVO);
			}
			
			// Envio el VO al request
			request.setAttribute(DisParSearchPage.NAME, disParSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(DisParSearchPage.NAME, disParSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_DISPAR_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DisParSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardVerSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_DISPAR);
	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardAgregarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_ENCDISPAR);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardModificarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_DISPAR);	
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardEliminarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_DISPAR);
			
		}
		
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			return forwardActivarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_DISPAR);			
	}
		
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			return forwardDesactivarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_DISPAR);
	}

	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseSeleccionar(mapping, request, response, funcName, DisParSearchPage.NAME);
	}	

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			return baseVolver(mapping, form, request, response, DisParSearchPage.NAME);
			
	}
	
	public ActionForward asociarRecursoVia(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardSearchPage (mapping, request, funcName, BalConstants.FWD_DISPARREC_SEARCHPAGE, BaseConstants.ACT_INICIALIZAR);
	}

	public ActionForward asociarPlan(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardSearchPage (mapping, request, funcName, BalConstants.FWD_DISPARPLA_SEARCHPAGE, BaseConstants.ACT_INICIALIZAR);
	}

}
