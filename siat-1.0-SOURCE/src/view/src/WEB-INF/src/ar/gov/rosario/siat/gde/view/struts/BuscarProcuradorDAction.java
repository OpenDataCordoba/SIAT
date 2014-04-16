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
import ar.gov.rosario.siat.gde.iface.model.ProcuradorSearchPage;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarProcuradorDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarProcuradorDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PROCURADOR, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			ProcuradorSearchPage procuradorSearchPageVO = GdeServiceLocator.getDefinicionService().getProcuradorSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (procuradorSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procuradorSearchPageVO.infoString()); 
				saveDemodaErrors(request, procuradorSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcuradorSearchPage.NAME, procuradorSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (procuradorSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + procuradorSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcuradorSearchPage.NAME, procuradorSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , ProcuradorSearchPage.NAME, procuradorSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_PROCURADOR_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcuradorSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, ProcuradorSearchPage.NAME);
		
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
			ProcuradorSearchPage procuradorSearchPageVO = (ProcuradorSearchPage) userSession.get(ProcuradorSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (procuradorSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + ProcuradorSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcuradorSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(procuradorSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				procuradorSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (procuradorSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procuradorSearchPageVO.infoString()); 
				saveDemodaErrors(request, procuradorSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcuradorSearchPage.NAME, procuradorSearchPageVO);
			}
				
			// Llamada al servicio	
			procuradorSearchPageVO = GdeServiceLocator.getDefinicionService().getProcuradorSearchPageResult(userSession, procuradorSearchPageVO);			

			// Tiene errores recuperables
			if (procuradorSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procuradorSearchPageVO.infoString()); 
				saveDemodaErrors(request, procuradorSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcuradorSearchPage.NAME, procuradorSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (procuradorSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + procuradorSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcuradorSearchPage.NAME, procuradorSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(ProcuradorSearchPage.NAME, procuradorSearchPageVO);
			// Nuleo el list result
			//procuradorSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(ProcuradorSearchPage.NAME, procuradorSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_PROCURADOR_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcuradorSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PROCURADOR);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
	
		return forwardAgregarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_ENC_PROCURADOR);
	
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PROCURADOR);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PROCURADOR);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PROCURADOR);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PROCURADOR);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, ProcuradorSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ProcuradorSearchPage.NAME);
		
	}
		
	
}
