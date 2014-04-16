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
import ar.gov.rosario.siat.exe.iface.model.TipoSujetoSearchPage;
import ar.gov.rosario.siat.exe.iface.service.ExeServiceLocator;
import ar.gov.rosario.siat.exe.iface.util.ExeSecurityConstants;
import ar.gov.rosario.siat.exe.view.util.ExeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarTipoSujetoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarTipoSujetoDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_TIPOSUJETO, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			TipoSujetoSearchPage tipoSujetoSearchPageVO = ExeServiceLocator.getDefinicionService().getTipoSujetoSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (tipoSujetoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoSujetoSearchPageVO.infoString()); 
				saveDemodaErrors(request, tipoSujetoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoSujetoSearchPage.NAME, tipoSujetoSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (tipoSujetoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoSujetoSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoSujetoSearchPage.NAME, tipoSujetoSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , TipoSujetoSearchPage.NAME, tipoSujetoSearchPageVO);
			
			return mapping.findForward(ExeConstants.FWD_TIPOSUJETO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoSujetoSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, TipoSujetoSearchPage.NAME);
		
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
			TipoSujetoSearchPage tipoSujetoSearchPageVO = (TipoSujetoSearchPage) userSession.get(TipoSujetoSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (tipoSujetoSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + TipoSujetoSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoSujetoSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(tipoSujetoSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				tipoSujetoSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (tipoSujetoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoSujetoSearchPageVO.infoString()); 
				saveDemodaErrors(request, tipoSujetoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoSujetoSearchPage.NAME, tipoSujetoSearchPageVO);
			}
				
			// Llamada al servicio	
			tipoSujetoSearchPageVO = ExeServiceLocator.getDefinicionService().getTipoSujetoSearchPageResult(userSession, tipoSujetoSearchPageVO);			

			// Tiene errores recuperables
			if (tipoSujetoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoSujetoSearchPageVO.infoString()); 
				saveDemodaErrors(request, tipoSujetoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoSujetoSearchPage.NAME, tipoSujetoSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (tipoSujetoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoSujetoSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoSujetoSearchPage.NAME, tipoSujetoSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(TipoSujetoSearchPage.NAME, tipoSujetoSearchPageVO);
			// Nuleo el list result
			//tipoSujetoSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(TipoSujetoSearchPage.NAME, tipoSujetoSearchPageVO);
			
			return mapping.findForward(ExeConstants.FWD_TIPOSUJETO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoSujetoSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, ExeConstants.ACTION_ADMINISTRAR_TIPOSUJETO);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		return forwardAgregarSearchPage(mapping, request, funcName, ExeConstants.ACTION_ADMINISTRAR_ENC_TIPOSUJETO);
	
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, ExeConstants.ACTION_ADMINISTRAR_TIPOSUJETO);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, ExeConstants.ACTION_ADMINISTRAR_TIPOSUJETO);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, ExeConstants.ACTION_ADMINISTRAR_TIPOSUJETO);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, ExeConstants.ACTION_ADMINISTRAR_TIPOSUJETO);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, TipoSujetoSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, TipoSujetoSearchPage.NAME);
		
	}
		
	
}
