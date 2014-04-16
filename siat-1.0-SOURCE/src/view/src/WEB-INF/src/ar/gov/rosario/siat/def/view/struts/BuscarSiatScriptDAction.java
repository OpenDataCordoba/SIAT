//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.def.iface.model.SiatScriptSearchPage;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarSiatScriptDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarSiatScriptDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_SIATSCRIPT, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			SiatScriptSearchPage siatScriptSearchPageVO = DefServiceLocator.getConfiguracionService().getSiatScriptSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (siatScriptSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + siatScriptSearchPageVO.infoString()); 
				saveDemodaErrors(request, siatScriptSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, SiatScriptSearchPage.NAME, siatScriptSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (siatScriptSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + siatScriptSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SiatScriptSearchPage.NAME, siatScriptSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , SiatScriptSearchPage.NAME, siatScriptSearchPageVO);
			
			return mapping.findForward(DefConstants.FWD_SIATSCRIPT_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SiatScriptSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, SiatScriptSearchPage.NAME);
		
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
			SiatScriptSearchPage siatScriptSearchPageVO = (SiatScriptSearchPage) userSession.get(SiatScriptSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (siatScriptSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + SiatScriptSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SiatScriptSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(siatScriptSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				siatScriptSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (siatScriptSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + siatScriptSearchPageVO.infoString()); 
				saveDemodaErrors(request, siatScriptSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, SiatScriptSearchPage.NAME, siatScriptSearchPageVO);
			}
				
			// Llamada al servicio	
			siatScriptSearchPageVO = DefServiceLocator.getConfiguracionService().getSiatScriptSearchPageResult(userSession, siatScriptSearchPageVO);			

			// Tiene errores recuperables
			if (siatScriptSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + siatScriptSearchPageVO.infoString()); 
				saveDemodaErrors(request, siatScriptSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, SiatScriptSearchPage.NAME, siatScriptSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (siatScriptSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + siatScriptSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, SiatScriptSearchPage.NAME, siatScriptSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(SiatScriptSearchPage.NAME, siatScriptSearchPageVO);
			// Nuleo el list result
			//siatScriptSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(SiatScriptSearchPage.NAME, siatScriptSearchPageVO);
			
			return mapping.findForward(DefConstants.FWD_SIATSCRIPT_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SiatScriptSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_SIATSCRIPT);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
//		Se utiliza uno de los dos return, segun sea un encabezado detalle o no.
//		return forwardAgregarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_SIATSCRIPT);
		return forwardAgregarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_ENC_SIATSCRIPT);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_SIATSCRIPT);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_SIATSCRIPT);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_SIATSCRIPT);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_SIATSCRIPT);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, SiatScriptSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, SiatScriptSearchPage.NAME);
		
	}
		
	
}
