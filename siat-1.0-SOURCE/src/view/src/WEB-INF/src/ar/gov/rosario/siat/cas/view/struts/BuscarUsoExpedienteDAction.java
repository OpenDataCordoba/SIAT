//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.cas.iface.model.UsoExpedienteSearchPage;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.cas.iface.util.CasSecurityConstants;
import ar.gov.rosario.siat.cas.view.util.CasConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarUsoExpedienteDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarUsoExpedienteDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, CasSecurityConstants.ABM_USOEXPEDIENTE, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			UsoExpedienteSearchPage usoExpedienteSearchPageVO = CasServiceLocator.getCasCasoService().getUsoExpedienteSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (usoExpedienteSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + usoExpedienteSearchPageVO.infoString()); 
				saveDemodaErrors(request, usoExpedienteSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, UsoExpedienteSearchPage.NAME, usoExpedienteSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (usoExpedienteSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + usoExpedienteSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, UsoExpedienteSearchPage.NAME, usoExpedienteSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , UsoExpedienteSearchPage.NAME, usoExpedienteSearchPageVO);
			
			return mapping.findForward(CasConstants.FWD_USOEXPEDIENTE_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, UsoExpedienteSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, UsoExpedienteSearchPage.NAME);
		
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
			UsoExpedienteSearchPage usoExpedienteSearchPageVO = (UsoExpedienteSearchPage) userSession.get(UsoExpedienteSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (usoExpedienteSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + UsoExpedienteSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, UsoExpedienteSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(usoExpedienteSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				usoExpedienteSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (usoExpedienteSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + usoExpedienteSearchPageVO.infoString()); 
				saveDemodaErrors(request, usoExpedienteSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, UsoExpedienteSearchPage.NAME, usoExpedienteSearchPageVO);
			}
				
			// Llamada al servicio	
			usoExpedienteSearchPageVO = CasServiceLocator.getCasCasoService().getUsoExpedienteSearchPageResult(userSession, usoExpedienteSearchPageVO);			

			// Tiene errores recuperables
			if (usoExpedienteSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + usoExpedienteSearchPageVO.infoString()); 
				saveDemodaErrors(request, usoExpedienteSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, UsoExpedienteSearchPage.NAME, usoExpedienteSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (usoExpedienteSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + usoExpedienteSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, UsoExpedienteSearchPage.NAME, usoExpedienteSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(UsoExpedienteSearchPage.NAME, usoExpedienteSearchPageVO);
			// Nuleo el list result
			//usoExpedienteSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(UsoExpedienteSearchPage.NAME, usoExpedienteSearchPageVO);
			
			return mapping.findForward(CasConstants.FWD_USOEXPEDIENTE_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, UsoExpedienteSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, CasConstants.ACTION_ADMINISTRAR_USOEXPEDIENTE);

	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, UsoExpedienteSearchPage.NAME);
		
	}
		
	
}
