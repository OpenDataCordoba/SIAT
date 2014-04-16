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
import ar.gov.rosario.siat.ef.iface.model.InvestigadorSearchPage;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarInvestigadorDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarInvestigadorDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_INVESTIGADOR, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			InvestigadorSearchPage investigadorSearchPageVO = EfServiceLocator.getDefinicionService().getInvestigadorSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (investigadorSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + investigadorSearchPageVO.infoString()); 
				saveDemodaErrors(request, investigadorSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, InvestigadorSearchPage.NAME, investigadorSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (investigadorSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + investigadorSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, InvestigadorSearchPage.NAME, investigadorSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , InvestigadorSearchPage.NAME, investigadorSearchPageVO);
			
			return mapping.findForward(EfConstants.FWD_INVESTIGADOR_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, InvestigadorSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, InvestigadorSearchPage.NAME);
		
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
			InvestigadorSearchPage investigadorSearchPageVO = (InvestigadorSearchPage) userSession.get(InvestigadorSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (investigadorSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + InvestigadorSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, InvestigadorSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(investigadorSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				investigadorSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (investigadorSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + investigadorSearchPageVO.infoString()); 
				saveDemodaErrors(request, investigadorSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, InvestigadorSearchPage.NAME, investigadorSearchPageVO);
			}
				
			// Llamada al servicio	
			investigadorSearchPageVO = EfServiceLocator.getDefinicionService().getInvestigadorSearchPageResult(userSession, investigadorSearchPageVO);			

			// Tiene errores recuperables
			if (investigadorSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + investigadorSearchPageVO.infoString()); 
				saveDemodaErrors(request, investigadorSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, InvestigadorSearchPage.NAME, investigadorSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (investigadorSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + investigadorSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, InvestigadorSearchPage.NAME, investigadorSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(InvestigadorSearchPage.NAME, investigadorSearchPageVO);
			// Nuleo el list result
			//investigadorSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(InvestigadorSearchPage.NAME, investigadorSearchPageVO);
			
			return mapping.findForward(EfConstants.FWD_INVESTIGADOR_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, InvestigadorSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_INVESTIGADOR);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
				
		return forwardAgregarSearchPage(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_INVESTIGADOR);		
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_INVESTIGADOR);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_INVESTIGADOR);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_INVESTIGADOR);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_INVESTIGADOR);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, InvestigadorSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, InvestigadorSearchPage.NAME);
		
	}
		
	
}
