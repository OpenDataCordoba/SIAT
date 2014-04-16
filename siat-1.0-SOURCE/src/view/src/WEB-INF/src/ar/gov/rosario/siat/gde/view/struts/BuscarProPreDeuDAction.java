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
import ar.gov.rosario.siat.gde.iface.model.ProPreDeuSearchPage;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarProPreDeuDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarProPreDeuDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		String act = getCurrentAct(request);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PROPREDEU, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			ProPreDeuSearchPage proPreDeuSearchPageVO = GdeServiceLocator
					.getAdmDeuConService().getProPreDeuSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (proPreDeuSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + proPreDeuSearchPageVO.infoString()); 
				saveDemodaErrors(request, proPreDeuSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProPreDeuSearchPage.NAME, proPreDeuSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (proPreDeuSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + proPreDeuSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProPreDeuSearchPage.NAME, proPreDeuSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , ProPreDeuSearchPage.NAME, proPreDeuSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_PROPREDEU_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProPreDeuSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, ProPreDeuSearchPage.NAME);
		
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
			ProPreDeuSearchPage proPreDeuSearchPageVO = (ProPreDeuSearchPage) userSession.get(ProPreDeuSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (proPreDeuSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + ProPreDeuSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProPreDeuSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(proPreDeuSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				proPreDeuSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (proPreDeuSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + proPreDeuSearchPageVO.infoString()); 
				saveDemodaErrors(request, proPreDeuSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProPreDeuSearchPage.NAME, proPreDeuSearchPageVO);
			}
				
			// Llamada al servicio	
			proPreDeuSearchPageVO = GdeServiceLocator.getAdmDeuConService().getProPreDeuSearchPageResult(userSession, proPreDeuSearchPageVO);			

			// Tiene errores recuperables
			if (proPreDeuSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + proPreDeuSearchPageVO.infoString()); 
				saveDemodaErrors(request, proPreDeuSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProPreDeuSearchPage.NAME, proPreDeuSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (proPreDeuSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + proPreDeuSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, ProPreDeuSearchPage.NAME, proPreDeuSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(ProPreDeuSearchPage.NAME, proPreDeuSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(ProPreDeuSearchPage.NAME, proPreDeuSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_PROPREDEU_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProPreDeuSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PROPREDEU);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PROPREDEU);

	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PROPREDEU);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PROPREDEU);

	}

	public ActionForward administrarProceso(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardSearchPage(mapping, request, funcName, 
					GdeConstants.ACTION_ADMINISTRAR_PROCESO_PRESCRIPCION_DEUDA, 
					GdeConstants.ACT_ADM_PROCESO_PRESCRIPCION_DEUDA);
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ProPreDeuSearchPage.NAME);
		
	}
		
}
