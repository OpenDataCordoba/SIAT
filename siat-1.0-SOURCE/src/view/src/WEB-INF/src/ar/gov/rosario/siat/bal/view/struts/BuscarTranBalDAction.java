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

import ar.gov.rosario.siat.bal.iface.model.TranBalSearchPage;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class BuscarTranBalDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarTranBalDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_TRANBAL, act); 
		if (userSession==null) return forwardErrorSession(request);
		NavModel navModel = userSession.getNavModel();
		
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			TranBalSearchPage tranBalSearchPageVO = BalServiceLocator.getBalanceService().getTranBalSearchPageInit(userSession, commonKey);
						
			// Tiene errores recuperables
			if (tranBalSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tranBalSearchPageVO.infoString()); 
				saveDemodaErrors(request, tranBalSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, TranBalSearchPage.NAME, tranBalSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (tranBalSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tranBalSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TranBalSearchPage.NAME, tranBalSearchPageVO);
			}
						
			// seteo los valores del navegacion en el pageModel
			tranBalSearchPageVO.setValuesFromNavModel(userSession.getNavModel());
	
			// Envio el VO al request
			request.setAttribute(TranBalSearchPage.NAME, tranBalSearchPageVO);
			
			// Subo en el el searchPage al userSession
			userSession.put(TranBalSearchPage.NAME, tranBalSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_TRANBAL_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TranBalSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, TranBalSearchPage.NAME);
		
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
			TranBalSearchPage tranBalSearchPageVO = (TranBalSearchPage) userSession.get(TranBalSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (tranBalSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + TranBalSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TranBalSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(tranBalSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				tranBalSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (tranBalSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tranBalSearchPageVO.infoString()); 
				saveDemodaErrors(request, tranBalSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, TranBalSearchPage.NAME, tranBalSearchPageVO);
			}
				
			// Llamada al servicio	
			tranBalSearchPageVO = BalServiceLocator.getBalanceService().getTranBalSearchPageResult(userSession, tranBalSearchPageVO);			

			// Tiene errores recuperables
			if (tranBalSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tranBalSearchPageVO.infoString()); 
				saveDemodaErrors(request, tranBalSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, TranBalSearchPage.NAME, tranBalSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (tranBalSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tranBalSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, TranBalSearchPage.NAME, tranBalSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(TranBalSearchPage.NAME, tranBalSearchPageVO);
			// Nuleo el list result
			// Subo en el el searchPage al userSession
			userSession.put(TranBalSearchPage.NAME, tranBalSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_TRANBAL_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TranBalSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_TRANBAL);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		return forwardAgregarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_TRANBAL);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_TRANBAL);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_TRANBAL);

	}
		
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, TranBalSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, TranBalSearchPage.NAME);
		
	}	
	
}
