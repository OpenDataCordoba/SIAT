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
import ar.gov.rosario.siat.emi.iface.model.EmiInfCueSearchPage;
import ar.gov.rosario.siat.emi.iface.model.ResLiqDeuVO;
import ar.gov.rosario.siat.emi.iface.service.EmiServiceLocator;
import ar.gov.rosario.siat.emi.iface.util.EmiSecurityConstants;
import ar.gov.rosario.siat.emi.view.util.EmiConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.NavModel;

public final class BuscarEmiInfCueDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarEmiInfCueDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, EmiSecurityConstants.ABM_RESLIQDEU, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			NavModel navModel = userSession.getNavModel();
			if (log.isDebugEnabled()) log.debug(funcName + " navModel" + navModel.infoString());
			
			Long idResLiqDeu = (Long) navModel.getParameter(ResLiqDeuVO.ADP_PARAM_ID);
			EmiInfCueSearchPage emiInfCueSearchPageVO = EmiServiceLocator.getGeneralService()
						.getEmiInfCueSearchPageInit(userSession, idResLiqDeu);
			
			// Tiene errores recuperables
			if (emiInfCueSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emiInfCueSearchPageVO.infoString()); 
				saveDemodaErrors(request, emiInfCueSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, EmiInfCueSearchPage.NAME, emiInfCueSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (emiInfCueSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + emiInfCueSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EmiInfCueSearchPage.NAME, emiInfCueSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , EmiInfCueSearchPage.NAME, emiInfCueSearchPageVO);
			
			return mapping.findForward(EmiConstants.FWD_EMIINFCUE_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmiInfCueSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, EmiInfCueSearchPage.NAME);
		
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
			EmiInfCueSearchPage emiInfCueSearchPageVO = (EmiInfCueSearchPage) userSession.get(EmiInfCueSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (emiInfCueSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + EmiInfCueSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EmiInfCueSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(emiInfCueSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				emiInfCueSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (emiInfCueSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emiInfCueSearchPageVO.infoString()); 
				saveDemodaErrors(request, emiInfCueSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, EmiInfCueSearchPage.NAME, emiInfCueSearchPageVO);
			}
				
			// Llamada al servicio	
			emiInfCueSearchPageVO = EmiServiceLocator.getGeneralService()
					.getEmiInfCueSearchPageResult(userSession, emiInfCueSearchPageVO);			

			// Tiene errores recuperables
			if (emiInfCueSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emiInfCueSearchPageVO.infoString()); 
				saveDemodaErrors(request, emiInfCueSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, EmiInfCueSearchPage.NAME, emiInfCueSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (emiInfCueSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + emiInfCueSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, EmiInfCueSearchPage.NAME, emiInfCueSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(EmiInfCueSearchPage.NAME, emiInfCueSearchPageVO);

			// Subo en el el searchPage al userSession
			userSession.put(EmiInfCueSearchPage.NAME, emiInfCueSearchPageVO);
			
			return mapping.findForward(EmiConstants.FWD_EMIINFCUE_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmiInfCueSearchPage.NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, EmiInfCueSearchPage.NAME);
		
	}
	
}
