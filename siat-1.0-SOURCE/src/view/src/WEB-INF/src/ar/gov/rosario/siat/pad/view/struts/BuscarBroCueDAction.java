//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.pad.iface.model.BroCueSearchPage;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class BuscarBroCueDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarBroCueDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		//String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		UserSession userSession = getCurrentUserSession(request, mapping);	
		//UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_BROCUE, act);		
		if (userSession==null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		try {
		
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			BroCueSearchPage broCueSearchPageVO = PadServiceLocator.getDistribucionService().getBroCueSearchPageInit(userSession, commonKey);
			
			// Tiene errores recuperables
			if (broCueSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + broCueSearchPageVO.infoString()); 
				saveDemodaErrors(request, broCueSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, BroCueSearchPage.NAME, broCueSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (broCueSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + broCueSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, BroCueSearchPage.NAME, broCueSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , BroCueSearchPage.NAME, broCueSearchPageVO);
			
			
			return mapping.findForward(PadConstants.FWD_BROCUE_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, BroCueSearchPage.NAME);
		}
			
	}
	
	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return this.baseRefill(mapping, form, request, response, funcName, BroCueSearchPage.NAME);

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
			BroCueSearchPage broCueSearchPageVO = (BroCueSearchPage) userSession.get(BroCueSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (broCueSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + BroCueSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, BroCueSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(broCueSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				broCueSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (broCueSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + broCueSearchPageVO.infoString()); 
				saveDemodaErrors(request, broCueSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, BroCueSearchPage.NAME, broCueSearchPageVO);
			}

			// Llamada al servicio	
			broCueSearchPageVO = PadServiceLocator.getDistribucionService().getBroCueSearchPageResult(userSession, broCueSearchPageVO);			

			// Tiene errores recuperables
			if (broCueSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + broCueSearchPageVO.infoString()); 
				saveDemodaErrors(request, broCueSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, BroCueSearchPage.NAME, broCueSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (broCueSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + broCueSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, BroCueSearchPage.NAME, broCueSearchPageVO);
			}
			
			// Envio el VO al request
			request.setAttribute(BroCueSearchPage.NAME, broCueSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(BroCueSearchPage.NAME, broCueSearchPageVO);
			
			return mapping.findForward(PadConstants.FWD_BROCUE_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, BroCueSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardVerSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_BROCUE);
	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardAgregarSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_BROCUE);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardModificarSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_BROCUE);	
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardEliminarSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_BROCUE);
			
	}

	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseSeleccionar(mapping, request, response, funcName, BroCueSearchPage.NAME);
	}	

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			return baseVolver(mapping, form, request, response, BroCueSearchPage.NAME);			
	}

}
