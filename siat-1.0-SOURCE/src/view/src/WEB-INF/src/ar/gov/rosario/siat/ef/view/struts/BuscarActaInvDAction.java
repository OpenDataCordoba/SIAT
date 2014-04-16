//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.ef.iface.model.OpeInvConSearchPage;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarActaInvDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarActaInvDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_ACTAINV, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			OpeInvConSearchPage opeInvConSearchPageVO = EfServiceLocator.getInvestigacionService().getOpeInvConActasSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (opeInvConSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + opeInvConSearchPageVO.infoString()); 
				saveDemodaErrors(request, opeInvConSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, OpeInvConSearchPage.NAME, opeInvConSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (opeInvConSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + opeInvConSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, OpeInvConSearchPage.NAME, opeInvConSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , OpeInvConSearchPage.NAME, opeInvConSearchPageVO);
			
			return mapping.findForward(EfConstants.FWD_ACTAINV_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OpeInvConSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, OpeInvConSearchPage.NAME);
		
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
			OpeInvConSearchPage OpeInvConSearchPageVO = (OpeInvConSearchPage) userSession.get(OpeInvConSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (OpeInvConSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + OpeInvConSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OpeInvConSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(OpeInvConSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				OpeInvConSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (OpeInvConSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + OpeInvConSearchPageVO.infoString()); 
				saveDemodaErrors(request, OpeInvConSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, OpeInvConSearchPage.NAME, OpeInvConSearchPageVO);
			}
				
			// Llamada al servicio	
			OpeInvConSearchPageVO = EfServiceLocator.getInvestigacionService().getOpeInvConActasSearchPageResult(userSession, OpeInvConSearchPageVO);			

			// Tiene errores recuperables
			if (OpeInvConSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + OpeInvConSearchPageVO.infoString()); 
				saveDemodaErrors(request, OpeInvConSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, OpeInvConSearchPage.NAME, OpeInvConSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (OpeInvConSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + OpeInvConSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, OpeInvConSearchPage.NAME, OpeInvConSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(OpeInvConSearchPage.NAME, OpeInvConSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(OpeInvConSearchPage.NAME, OpeInvConSearchPageVO);
			
			return mapping.findForward(EfConstants.FWD_ACTAINV_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OpeInvConSearchPage.NAME);
		}
	}

	public ActionForward paramPlan(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			
			try {
				
				// Bajo el searchPage del userSession
				OpeInvConSearchPage OpeInvConSearchPageVO = (OpeInvConSearchPage) userSession.get(OpeInvConSearchPage.NAME);
				
				// Si es nulo no se puede continuar
				if (OpeInvConSearchPageVO == null) {
					log.error("error en: "  + funcName + ": " + OpeInvConSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, OpeInvConSearchPage.NAME); 
				}
						
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(OpeInvConSearchPageVO, request);

				// Llamada al servicio	
				OpeInvConSearchPageVO = EfServiceLocator.getInvestigacionService().getOpeInvConActasSearchPageParamPlan(userSession, OpeInvConSearchPageVO);			

				// Tiene errores recuperables
				if (OpeInvConSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + OpeInvConSearchPageVO.infoString()); 
					saveDemodaErrors(request, OpeInvConSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, OpeInvConSearchPage.NAME, OpeInvConSearchPageVO);
				}
				
				// Tiene errores no recuperables
				if (OpeInvConSearchPageVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + OpeInvConSearchPageVO.errorString());
					return forwardErrorNonRecoverable(mapping, request, funcName, OpeInvConSearchPage.NAME, OpeInvConSearchPageVO);
				}
			
				// Envio el VO al request
				request.setAttribute(OpeInvConSearchPage.NAME, OpeInvConSearchPageVO);
				// Subo en el el searchPage al userSession
				userSession.put(OpeInvConSearchPage.NAME, OpeInvConSearchPageVO);
				
				return mapping.findForward(EfConstants.FWD_ACTAINV_SEARCHPAGE);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, OpeInvConSearchPage.NAME);
			}
	}
	
	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);

		String selectedId = request.getParameter("selectedId");
		
		userSession.getNavModel().setSelectedId(selectedId);
		return forwardVerSearchPage(mapping, request, funcName, EfConstants.FWD_ACTAINV_SEARCHPAGE_VER_OPEINVCON);

	}

	public ActionForward acta(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardSearchPage (mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_ACTAINV, EfSecurityConstants.ADM_ACTAINV_MODIF_ACTA);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, OpeInvConSearchPage.NAME);
		
	}
		
	
}
