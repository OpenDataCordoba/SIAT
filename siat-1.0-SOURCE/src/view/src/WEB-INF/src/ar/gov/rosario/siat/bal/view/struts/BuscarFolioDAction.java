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

import ar.gov.rosario.siat.bal.iface.model.FolioSearchPage;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class BuscarFolioDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarFolioDAction.class);
	public final static String FOLIO_SEARCHPAGE_FILTRO = "folioSPFiltro";
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_FOLIO, act);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
		
			FolioSearchPage folioSearchPageFiltro = (FolioSearchPage) userSession.getNavModel().getParameter(BuscarFolioDAction.FOLIO_SEARCHPAGE_FILTRO);

			FolioSearchPage folioSearchPageVO = BalServiceLocator.getFolioTesoreriaService().getFolioSearchPageInit(userSession, folioSearchPageFiltro);
			
			// Tiene errores recuperables
			if (folioSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + folioSearchPageVO.infoString()); 
				saveDemodaErrors(request, folioSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, FolioSearchPage.NAME, folioSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (folioSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + folioSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, FolioSearchPage.NAME, folioSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , FolioSearchPage.NAME, folioSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_FOLIO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, FolioSearchPage.NAME);
		}
	}
	
	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return this.baseRefill(mapping, form, request, response, funcName, FolioSearchPage.NAME);

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
			FolioSearchPage folioSearchPageVO = (FolioSearchPage) userSession.get(FolioSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (folioSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + FolioSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, FolioSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(folioSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				folioSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (folioSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + folioSearchPageVO.infoString()); 
				saveDemodaErrors(request, folioSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, FolioSearchPage.NAME, folioSearchPageVO);
			}

			// Llamada al servicio	
			folioSearchPageVO = BalServiceLocator.getFolioTesoreriaService().getFolioSearchPageResult(userSession, folioSearchPageVO);			

			// Tiene errores recuperables
			if (folioSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + folioSearchPageVO.infoString()); 
				saveDemodaErrors(request, folioSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, FolioSearchPage.NAME, folioSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (folioSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + folioSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, FolioSearchPage.NAME, folioSearchPageVO);
			}
			
			// Envio el VO al request
			request.setAttribute(FolioSearchPage.NAME, folioSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(FolioSearchPage.NAME, folioSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_FOLIO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, FolioSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardVerSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_FOLIO);
	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardAgregarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_ENCFOLIO);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardModificarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_FOLIO);	
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardEliminarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_FOLIO);
			
	}

	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseSeleccionar(mapping, request, response, funcName, FolioSearchPage.NAME);
	}	

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			return baseVolver(mapping, form, request, response, FolioSearchPage.NAME);
			
	}
	
	public ActionForward enviar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_FOLIO, BaseConstants.ACT_ENVIAR);	
	}

	public ActionForward devolver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_FOLIO, BaseConstants.ACT_DEVOLVER);	
	}

}
