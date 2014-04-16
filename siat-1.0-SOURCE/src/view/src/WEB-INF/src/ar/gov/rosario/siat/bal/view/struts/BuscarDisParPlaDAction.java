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

import ar.gov.rosario.siat.bal.iface.model.DisParPlaSearchPage;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class BuscarDisParPlaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarDisParPlaDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		//String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		UserSession userSession = getCurrentUserSession(request, mapping);		
		//UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_DISPARPLA, act);		
		if (userSession==null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		try {
		
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			DisParPlaSearchPage disParPlaSearchPageVO = BalServiceLocator.getDistribucionService().getDisParPlaSearchPageInit(userSession, commonKey);
			
			// Tiene errores recuperables
			if (disParPlaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + disParPlaSearchPageVO.infoString()); 
				saveDemodaErrors(request, disParPlaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, DisParPlaSearchPage.NAME, disParPlaSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (disParPlaSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + disParPlaSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DisParPlaSearchPage.NAME, disParPlaSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , DisParPlaSearchPage.NAME, disParPlaSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_DISPARPLA_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DisParPlaSearchPage.NAME);
		}
			
	}
	
	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return this.baseRefill(mapping, form, request, response, funcName, DisParPlaSearchPage.NAME);

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
			DisParPlaSearchPage disParPlaSearchPageVO = (DisParPlaSearchPage) userSession.get(DisParPlaSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (disParPlaSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + DisParPlaSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DisParPlaSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(disParPlaSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				disParPlaSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (disParPlaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + disParPlaSearchPageVO.infoString()); 
				saveDemodaErrors(request, disParPlaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, DisParPlaSearchPage.NAME, disParPlaSearchPageVO);
			}

			// Llamada al servicio	
			disParPlaSearchPageVO = BalServiceLocator.getDistribucionService().getDisParPlaSearchPageResult(userSession, disParPlaSearchPageVO);			

			// Tiene errores recuperables
			if (disParPlaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + disParPlaSearchPageVO.infoString()); 
				saveDemodaErrors(request, disParPlaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, DisParPlaSearchPage.NAME, disParPlaSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (disParPlaSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + disParPlaSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, DisParPlaSearchPage.NAME, disParPlaSearchPageVO);
			}
			
			// Envio el VO al request
			request.setAttribute(DisParPlaSearchPage.NAME, disParPlaSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(DisParPlaSearchPage.NAME, disParPlaSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_DISPARPLA_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DisParPlaSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardVerSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_DISPARPLA);
	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardAgregarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_DISPARPLA);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardModificarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_DISPARPLA);	
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardEliminarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_DISPARPLA);
			
	}

	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseSeleccionar(mapping, request, response, funcName, DisParPlaSearchPage.NAME);
	}	

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			return baseVolver(mapping, form, request, response, DisParPlaSearchPage.NAME);			
	}
	
}
