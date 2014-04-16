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
import ar.gov.rosario.siat.gde.iface.model.ConvenioSearchPage;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarConvenioDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarConvenioDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_CONVENIO, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			ConvenioSearchPage convenioSearchPageVO = GdeServiceLocator.getGestionDeudaService().getConvenioSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (convenioSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + convenioSearchPageVO.infoString()); 
				saveDemodaErrors(request, convenioSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ConvenioSearchPage.NAME, convenioSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (convenioSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + convenioSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ConvenioSearchPage.NAME, convenioSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , ConvenioSearchPage.NAME, convenioSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_CONVENIO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConvenioSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, ConvenioSearchPage.NAME);
		
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
			ConvenioSearchPage convenioSearchPageVO = (ConvenioSearchPage) userSession.get(ConvenioSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (convenioSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + ConvenioSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ConvenioSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(convenioSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				convenioSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (convenioSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + convenioSearchPageVO.infoString()); 
				saveDemodaErrors(request, convenioSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ConvenioSearchPage.NAME, convenioSearchPageVO);
			}
				
			// Llamada al servicio	
			convenioSearchPageVO = GdeServiceLocator.getGestionDeudaService().getConvenioSearchPageResult(userSession, convenioSearchPageVO);			

			// Tiene errores recuperables
			if (convenioSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + convenioSearchPageVO.infoString()); 
				saveDemodaErrors(request, convenioSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ConvenioSearchPage.NAME, convenioSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (convenioSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + convenioSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, ConvenioSearchPage.NAME, convenioSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(ConvenioSearchPage.NAME, convenioSearchPageVO);
			// Nuleo el list result
			//convenioSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(ConvenioSearchPage.NAME, convenioSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_CONVENIO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConvenioSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		request.setAttribute("vieneDe", "buscarConvenio");
		return forwardVerSearchPage(mapping, request, funcName, GdeConstants.ACTION_VER_CONVENIO_CUENTA);
		
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ConvenioSearchPage.NAME);
		
	}
		
	
}
