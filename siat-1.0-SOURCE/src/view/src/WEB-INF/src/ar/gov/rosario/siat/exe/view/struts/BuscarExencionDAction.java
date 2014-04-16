//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.exe.iface.model.ExencionSearchPage;
import ar.gov.rosario.siat.exe.iface.service.ExeServiceLocator;
import ar.gov.rosario.siat.exe.iface.util.ExeSecurityConstants;
import ar.gov.rosario.siat.exe.view.util.ExeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarExencionDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarExencionDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_EXENCION, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			ExencionSearchPage exencionSearchPageVO = ExeServiceLocator.getDefinicionService().getExencionSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (exencionSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + exencionSearchPageVO.infoString()); 
				saveDemodaErrors(request, exencionSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ExencionSearchPage.NAME, exencionSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (exencionSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + exencionSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ExencionSearchPage.NAME, exencionSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , ExencionSearchPage.NAME, exencionSearchPageVO);
			
			return mapping.findForward(ExeConstants.FWD_EXENCION_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ExencionSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, ExencionSearchPage.NAME);
		
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
			ExencionSearchPage exencionSearchPageVO = (ExencionSearchPage) userSession.get(ExencionSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (exencionSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + ExencionSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ExencionSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(exencionSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				exencionSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (exencionSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + exencionSearchPageVO.infoString()); 
				saveDemodaErrors(request, exencionSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ExencionSearchPage.NAME, exencionSearchPageVO);
			}
				
			// Llamada al servicio	
			exencionSearchPageVO = ExeServiceLocator.getDefinicionService().getExencionSearchPageResult(userSession, exencionSearchPageVO);			

			// Tiene errores recuperables
			if (exencionSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + exencionSearchPageVO.infoString()); 
				saveDemodaErrors(request, exencionSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ExencionSearchPage.NAME, exencionSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (exencionSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + exencionSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, ExencionSearchPage.NAME, exencionSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(ExencionSearchPage.NAME, exencionSearchPageVO);
			// Nuleo el list result
			//exencionSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(ExencionSearchPage.NAME, exencionSearchPageVO);
			
			return mapping.findForward(ExeConstants.FWD_EXENCION_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ExencionSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, ExeConstants.ACTION_ADMINISTRAR_EXENCION);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
				
		return forwardAgregarSearchPage(mapping, request, funcName, ExeConstants.ACTION_ADMINISTRAR_ENC_EXENCION);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, ExeConstants.ACTION_ADMINISTRAR_EXENCION);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, ExeConstants.ACTION_ADMINISTRAR_EXENCION);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, ExeConstants.ACTION_ADMINISTRAR_EXENCION);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, ExeConstants.ACTION_ADMINISTRAR_EXENCION);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, ExencionSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ExencionSearchPage.NAME);
		
	}
		
	
}
