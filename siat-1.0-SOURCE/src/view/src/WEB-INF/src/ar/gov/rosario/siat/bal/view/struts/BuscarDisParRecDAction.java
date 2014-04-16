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

import ar.gov.rosario.siat.bal.iface.model.DisParRecSearchPage;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class BuscarDisParRecDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarDisParRecDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		//String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		//UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_DISPARREC, act);		
		UserSession userSession = getCurrentUserSession(request, mapping);		
		
		if (userSession==null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		try {
		
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			DisParRecSearchPage disParRecSearchPageVO = BalServiceLocator.getDistribucionService().getDisParRecSearchPageInit(userSession, commonKey);
			
			// Tiene errores recuperables
			if (disParRecSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + disParRecSearchPageVO.infoString()); 
				saveDemodaErrors(request, disParRecSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, DisParRecSearchPage.NAME, disParRecSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (disParRecSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + disParRecSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DisParRecSearchPage.NAME, disParRecSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , DisParRecSearchPage.NAME, disParRecSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_DISPARREC_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DisParRecSearchPage.NAME);
		}
			
	}
	
	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return this.baseRefill(mapping, form, request, response, funcName, DisParRecSearchPage.NAME);

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
			DisParRecSearchPage disParRecSearchPageVO = (DisParRecSearchPage) userSession.get(DisParRecSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (disParRecSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + DisParRecSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DisParRecSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(disParRecSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				disParRecSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (disParRecSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + disParRecSearchPageVO.infoString()); 
				saveDemodaErrors(request, disParRecSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, DisParRecSearchPage.NAME, disParRecSearchPageVO);
			}

			// Llamada al servicio	
			disParRecSearchPageVO = BalServiceLocator.getDistribucionService().getDisParRecSearchPageResult(userSession, disParRecSearchPageVO);			

			// Tiene errores recuperables
			if (disParRecSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + disParRecSearchPageVO.infoString()); 
				saveDemodaErrors(request, disParRecSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, DisParRecSearchPage.NAME, disParRecSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (disParRecSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + disParRecSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, DisParRecSearchPage.NAME, disParRecSearchPageVO);
			}
			
			// Envio el VO al request
			request.setAttribute(DisParRecSearchPage.NAME, disParRecSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(DisParRecSearchPage.NAME, disParRecSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_DISPARREC_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DisParRecSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardVerSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_DISPARREC);
	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardAgregarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_DISPARREC);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardModificarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_DISPARREC);	
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardEliminarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_DISPARREC);
			
	}

	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseSeleccionar(mapping, request, response, funcName, DisParRecSearchPage.NAME);
	}	

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			return baseVolver(mapping, form, request, response, DisParRecSearchPage.NAME);			
	}
	
}
