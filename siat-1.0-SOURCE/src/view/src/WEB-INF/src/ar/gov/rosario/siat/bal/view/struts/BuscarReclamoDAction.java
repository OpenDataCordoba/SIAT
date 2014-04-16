//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.bal.iface.model.ReclamoSearchPage;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarReclamoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarReclamoDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_RECLAMO, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			ReclamoSearchPage reclamoSearchPageVO = BalServiceLocator.getReclamoService().getReclamoSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (reclamoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + reclamoSearchPageVO.infoString()); 
				saveDemodaErrors(request, reclamoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ReclamoSearchPage.NAME, reclamoSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (reclamoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + reclamoSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ReclamoSearchPage.NAME, reclamoSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , ReclamoSearchPage.NAME, reclamoSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_RECLAMO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ReclamoSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, ReclamoSearchPage.NAME);
		
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
			ReclamoSearchPage reclamoSearchPageVO = (ReclamoSearchPage) userSession.get(ReclamoSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (reclamoSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + ReclamoSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ReclamoSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(reclamoSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				reclamoSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (reclamoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + reclamoSearchPageVO.infoString()); 
				saveDemodaErrors(request, reclamoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ReclamoSearchPage.NAME, reclamoSearchPageVO);
			}
				
			// Llamada al servicio	
			reclamoSearchPageVO = BalServiceLocator.getReclamoService().getReclamoSearchPageResult(userSession, reclamoSearchPageVO);			

			// Tiene errores recuperables
			if (reclamoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + reclamoSearchPageVO.infoString()); 
				saveDemodaErrors(request, reclamoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ReclamoSearchPage.NAME, reclamoSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (reclamoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + reclamoSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, ReclamoSearchPage.NAME, reclamoSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(ReclamoSearchPage.NAME, reclamoSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(ReclamoSearchPage.NAME, reclamoSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_RECLAMO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ReclamoSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_RECLAMO);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
	
		return forwardAgregarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_RECLAMO);
		
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_RECLAMO);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_RECLAMO);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_RECLAMO);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_RECLAMO);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, ReclamoSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ReclamoSearchPage.NAME);
		
	}
		
	
}
