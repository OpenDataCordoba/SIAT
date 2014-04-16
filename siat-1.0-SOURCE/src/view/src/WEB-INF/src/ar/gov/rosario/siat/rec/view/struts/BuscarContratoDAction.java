//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.rec.iface.model.ContratoSearchPage;
import ar.gov.rosario.siat.rec.iface.service.RecServiceLocator;
import ar.gov.rosario.siat.rec.iface.util.RecSecurityConstants;
import ar.gov.rosario.siat.rec.view.util.RecConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarContratoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarContratoDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_CONTRATO, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			ContratoSearchPage contratoSearchPageVO = RecServiceLocator.getCdmService().getContratoSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (contratoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + contratoSearchPageVO.infoString()); 
				saveDemodaErrors(request, contratoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ContratoSearchPage.NAME, contratoSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (contratoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + contratoSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ContratoSearchPage.NAME, contratoSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , ContratoSearchPage.NAME, contratoSearchPageVO);
			
			return mapping.findForward(RecConstants.FWD_CONTRATO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ContratoSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, ContratoSearchPage.NAME);
		
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
			ContratoSearchPage contratoSearchPageVO = (ContratoSearchPage) userSession.get(ContratoSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (contratoSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + ContratoSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ContratoSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(contratoSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				contratoSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (contratoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + contratoSearchPageVO.infoString()); 
				saveDemodaErrors(request, contratoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ContratoSearchPage.NAME, contratoSearchPageVO);
			}
				
			// Llamada al servicio	
			contratoSearchPageVO = RecServiceLocator.getCdmService().getContratoSearchPageResult(userSession, contratoSearchPageVO);			

			// Tiene errores recuperables
			if (contratoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + contratoSearchPageVO.infoString()); 
				saveDemodaErrors(request, contratoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ContratoSearchPage.NAME, contratoSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (contratoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + contratoSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, ContratoSearchPage.NAME, contratoSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(ContratoSearchPage.NAME, contratoSearchPageVO);
			// Nuleo el list result
			//contratoSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(ContratoSearchPage.NAME, contratoSearchPageVO);
			
			return mapping.findForward(RecConstants.FWD_CONTRATO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ContratoSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_CONTRATO);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarSearchPage(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_CONTRATO);

	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_CONTRATO);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_CONTRATO);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_CONTRATO);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_CONTRATO);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, ContratoSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ContratoSearchPage.NAME);
		
	}
		
	
}
