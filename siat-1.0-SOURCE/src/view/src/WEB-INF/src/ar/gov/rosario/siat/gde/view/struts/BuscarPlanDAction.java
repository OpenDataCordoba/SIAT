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
import ar.gov.rosario.siat.gde.iface.model.PlanSearchPage;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarPlanDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarPlanDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PLAN, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			PlanSearchPage planSearchPageVO = GdeServiceLocator.getDefinicionService().getPlanSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (planSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planSearchPageVO.infoString()); 
				saveDemodaErrors(request, planSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanSearchPage.NAME, planSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (planSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanSearchPage.NAME, planSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , PlanSearchPage.NAME, planSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_PLAN_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, PlanSearchPage.NAME);
		
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
			PlanSearchPage planSearchPageVO = (PlanSearchPage) userSession.get(PlanSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (planSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + PlanSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(planSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				planSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (planSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planSearchPageVO.infoString()); 
				saveDemodaErrors(request, planSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanSearchPage.NAME, planSearchPageVO);
			}
				
			// Llamada al servicio	
			planSearchPageVO = GdeServiceLocator.getDefinicionService().getPlanSearchPageResult(userSession, planSearchPageVO);			

			// Tiene errores recuperables
			if (planSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planSearchPageVO.infoString()); 
				saveDemodaErrors(request, planSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanSearchPage.NAME, planSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (planSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanSearchPage.NAME, planSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(PlanSearchPage.NAME, planSearchPageVO);
			// Nuleo el list result
			//planSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(PlanSearchPage.NAME, planSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_PLAN_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLAN);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		return forwardAgregarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_ENC_PLAN);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLAN);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLAN);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLAN);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLAN);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, PlanSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, PlanSearchPage.NAME);
		
	}
		
	public ActionForward admPlanProrroga(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();

		return baseForwardSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLAN, 
				GdeConstants.MTD_ADM_PLANPRORROGA);

	}
}
