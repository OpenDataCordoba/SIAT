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
import ar.gov.rosario.siat.emi.iface.model.ValEmiMatSearchPage;
import ar.gov.rosario.siat.emi.iface.service.EmiServiceLocator;
import ar.gov.rosario.siat.emi.iface.util.EmiSecurityConstants;
import ar.gov.rosario.siat.emi.view.util.EmiConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarValEmiMatDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarValEmiMatDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, EmiSecurityConstants.ABM_VALEMIMAT, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			ValEmiMatSearchPage valEmiMatSearchPageVO = EmiServiceLocator.getDefinicionService().getValEmiMatSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (valEmiMatSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + valEmiMatSearchPageVO.infoString()); 
				saveDemodaErrors(request, valEmiMatSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ValEmiMatSearchPage.NAME, valEmiMatSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (valEmiMatSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + valEmiMatSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ValEmiMatSearchPage.NAME, valEmiMatSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , ValEmiMatSearchPage.NAME, valEmiMatSearchPageVO);
			
			return mapping.findForward(EmiConstants.FWD_VALEMIMAT_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ValEmiMatSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, ValEmiMatSearchPage.NAME);
		
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
			ValEmiMatSearchPage valEmiMatSearchPageVO = (ValEmiMatSearchPage) userSession.get(ValEmiMatSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (valEmiMatSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + ValEmiMatSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ValEmiMatSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(valEmiMatSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				valEmiMatSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (valEmiMatSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + valEmiMatSearchPageVO.infoString()); 
				saveDemodaErrors(request, valEmiMatSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ValEmiMatSearchPage.NAME, valEmiMatSearchPageVO);
			}
				
			// Llamada al servicio	
			valEmiMatSearchPageVO = EmiServiceLocator.getDefinicionService().getValEmiMatSearchPageResult(userSession, valEmiMatSearchPageVO);			

			// Tiene errores recuperables
			if (valEmiMatSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + valEmiMatSearchPageVO.infoString()); 
				saveDemodaErrors(request, valEmiMatSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ValEmiMatSearchPage.NAME, valEmiMatSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (valEmiMatSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + valEmiMatSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, ValEmiMatSearchPage.NAME, valEmiMatSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(ValEmiMatSearchPage.NAME, valEmiMatSearchPageVO);
			// Nuleo el list result
			//valEmiMatSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(ValEmiMatSearchPage.NAME, valEmiMatSearchPageVO);
			
			return mapping.findForward(EmiConstants.FWD_VALEMIMAT_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ValEmiMatSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, EmiConstants.ACTION_ADMINISTRAR_VALEMIMAT);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		return forwardAgregarSearchPage(mapping, request, funcName, EmiConstants.ACTION_ADMINISTRAR_VALEMIMAT);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, EmiConstants.ACTION_ADMINISTRAR_VALEMIMAT);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, EmiConstants.ACTION_ADMINISTRAR_VALEMIMAT);

	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, ValEmiMatSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ValEmiMatSearchPage.NAME);
		
	}
		
	
}
