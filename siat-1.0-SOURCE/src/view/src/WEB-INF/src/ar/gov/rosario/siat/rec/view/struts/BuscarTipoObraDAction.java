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
import ar.gov.rosario.siat.rec.iface.model.TipoObraSearchPage;
import ar.gov.rosario.siat.rec.iface.service.RecServiceLocator;
import ar.gov.rosario.siat.rec.iface.util.RecSecurityConstants;
import ar.gov.rosario.siat.rec.view.util.RecConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarTipoObraDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarTipoObraDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_TIPOOBRA, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			TipoObraSearchPage tipoObraSearchPageVO = RecServiceLocator.getCdmService().getTipoObraSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (tipoObraSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoObraSearchPageVO.infoString()); 
				saveDemodaErrors(request, tipoObraSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoObraSearchPage.NAME, tipoObraSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (tipoObraSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoObraSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoObraSearchPage.NAME, tipoObraSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , TipoObraSearchPage.NAME, tipoObraSearchPageVO);
			
			return mapping.findForward(RecConstants.FWD_TIPOOBRA_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoObraSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, TipoObraSearchPage.NAME);
		
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
			TipoObraSearchPage tipoObraSearchPageVO = (TipoObraSearchPage) userSession.get(TipoObraSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (tipoObraSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + TipoObraSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoObraSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(tipoObraSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				tipoObraSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (tipoObraSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoObraSearchPageVO.infoString()); 
				saveDemodaErrors(request, tipoObraSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoObraSearchPage.NAME, tipoObraSearchPageVO);
			}
				
			// Llamada al servicio	
			tipoObraSearchPageVO = RecServiceLocator.getCdmService().getTipoObraSearchPageResult(userSession, tipoObraSearchPageVO);			

			// Tiene errores recuperables
			if (tipoObraSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoObraSearchPageVO.infoString()); 
				saveDemodaErrors(request, tipoObraSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoObraSearchPage.NAME, tipoObraSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (tipoObraSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoObraSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoObraSearchPage.NAME, tipoObraSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(TipoObraSearchPage.NAME, tipoObraSearchPageVO);
			// Nuleo el list result
			//tipoObraSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(TipoObraSearchPage.NAME, tipoObraSearchPageVO);
			
			return mapping.findForward(RecConstants.FWD_TIPOOBRA_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoObraSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_TIPOOBRA);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarSearchPage(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_TIPOOBRA);
		
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_TIPOOBRA);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_TIPOOBRA);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_TIPOOBRA);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_TIPOOBRA);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, TipoObraSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, TipoObraSearchPage.NAME);
		
	}
		
}
