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
import ar.gov.rosario.siat.gde.iface.model.ProPreDeuDetSearchPage;
import ar.gov.rosario.siat.gde.iface.model.ProPreDeuVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.NavModel;

public final class BuscarProPreDeuDetDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarProPreDeuDetDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PROPREDEU, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			NavModel navModel = userSession.getNavModel();
			if (log.isDebugEnabled()) log.debug(funcName + " navModel" + navModel.infoString());

			Long idProPreDeu = (Long) navModel.getParameter(ProPreDeuVO.ADP_PARAM_ID);
			ProPreDeuDetSearchPage proPreDeuDetSearchPageVO = GdeServiceLocator
				.getAdmDeuConService().getProPreDeuDetSearchPageInit(userSession, idProPreDeu);
			
			// Tiene errores recuperables
			if (proPreDeuDetSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + proPreDeuDetSearchPageVO.infoString()); 
				saveDemodaErrors(request, proPreDeuDetSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProPreDeuDetSearchPage.NAME, proPreDeuDetSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (proPreDeuDetSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + proPreDeuDetSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProPreDeuDetSearchPage.NAME, proPreDeuDetSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , ProPreDeuDetSearchPage.NAME, proPreDeuDetSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_PROPREDEUDET_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProPreDeuDetSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, ProPreDeuDetSearchPage.NAME);
		
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
			ProPreDeuDetSearchPage proPreDeuDetSearchPageVO = (ProPreDeuDetSearchPage) userSession.get(ProPreDeuDetSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (proPreDeuDetSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + ProPreDeuDetSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProPreDeuDetSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(proPreDeuDetSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				proPreDeuDetSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (proPreDeuDetSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + proPreDeuDetSearchPageVO.infoString()); 
				saveDemodaErrors(request, proPreDeuDetSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProPreDeuDetSearchPage.NAME, proPreDeuDetSearchPageVO);
			}
				
			// Llamada al servicio	
			proPreDeuDetSearchPageVO = GdeServiceLocator.getAdmDeuConService().getProPreDeuDetSearchPageResult(userSession, proPreDeuDetSearchPageVO);			

			// Tiene errores recuperables
			if (proPreDeuDetSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + proPreDeuDetSearchPageVO.infoString()); 
				saveDemodaErrors(request, proPreDeuDetSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProPreDeuDetSearchPage.NAME, proPreDeuDetSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (proPreDeuDetSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + proPreDeuDetSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, ProPreDeuDetSearchPage.NAME, proPreDeuDetSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(ProPreDeuDetSearchPage.NAME, proPreDeuDetSearchPageVO);
			// Nuleo el list result
			//proPreDeuDetSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(ProPreDeuDetSearchPage.NAME, proPreDeuDetSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_PROPREDEUDET_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProPreDeuDetSearchPage.NAME);
		}
	}

	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, ProPreDeuDetSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ProPreDeuDetSearchPage.NAME);
		
	}
		
}
