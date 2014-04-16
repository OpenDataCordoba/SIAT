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
import ar.gov.rosario.siat.exe.iface.model.EstadoCueExeSearchPage;
import ar.gov.rosario.siat.exe.iface.service.ExeServiceLocator;
import ar.gov.rosario.siat.exe.iface.util.ExeSecurityConstants;
import ar.gov.rosario.siat.exe.view.util.ExeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarEstadoCueExeDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarEstadoCueExeDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_ESTADOCUEEXE, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			EstadoCueExeSearchPage estadoCueExeSearchPageVO = ExeServiceLocator.getDefinicionService().getEstadoCueExeSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (estadoCueExeSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + estadoCueExeSearchPageVO.infoString()); 
				saveDemodaErrors(request, estadoCueExeSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, EstadoCueExeSearchPage.NAME, estadoCueExeSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (estadoCueExeSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + estadoCueExeSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EstadoCueExeSearchPage.NAME, estadoCueExeSearchPageVO);
			}
		
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , EstadoCueExeSearchPage.NAME, estadoCueExeSearchPageVO);
			
			return mapping.findForward(ExeConstants.FWD_ESTADOCUEEXE_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EstadoCueExeSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, EstadoCueExeSearchPage.NAME);
		
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
			EstadoCueExeSearchPage estadoCueExeSearchPageVO = (EstadoCueExeSearchPage) userSession.get(EstadoCueExeSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (estadoCueExeSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + EstadoCueExeSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EstadoCueExeSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(estadoCueExeSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				estadoCueExeSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
				log.debug("estado seleccionado:" + estadoCueExeSearchPageVO.getTipoEstadoCueExe());
			}
			
            // Tiene errores recuperables
			if (estadoCueExeSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + estadoCueExeSearchPageVO.infoString()); 
				saveDemodaErrors(request, estadoCueExeSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, EstadoCueExeSearchPage.NAME, estadoCueExeSearchPageVO);
			}
				
			// Llamada al servicio	
			estadoCueExeSearchPageVO = ExeServiceLocator.getDefinicionService().getEstadoCueExeSearchPageResult(userSession, estadoCueExeSearchPageVO);			

			// Tiene errores recuperables
			if (estadoCueExeSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + estadoCueExeSearchPageVO.infoString()); 
				saveDemodaErrors(request, estadoCueExeSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, EstadoCueExeSearchPage.NAME, estadoCueExeSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (estadoCueExeSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + estadoCueExeSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, EstadoCueExeSearchPage.NAME, estadoCueExeSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(EstadoCueExeSearchPage.NAME, estadoCueExeSearchPageVO);
			// Nuleo el list result
			//estadoCueExeSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(EstadoCueExeSearchPage.NAME, estadoCueExeSearchPageVO);
			
			return mapping.findForward(ExeConstants.FWD_ESTADOCUEEXE_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EstadoCueExeSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, ExeConstants.ACTION_ADMINISTRAR_ESTADOCUEEXE);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		return forwardAgregarSearchPage(mapping, request, funcName, ExeConstants.ACTION_ADMINISTRAR_ESTADOCUEEXE);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, ExeConstants.ACTION_ADMINISTRAR_ESTADOCUEEXE);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, ExeConstants.ACTION_ADMINISTRAR_ESTADOCUEEXE);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, ExeConstants.ACTION_ADMINISTRAR_ESTADOCUEEXE);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, ExeConstants.ACTION_ADMINISTRAR_ESTADOCUEEXE);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, EstadoCueExeSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, EstadoCueExeSearchPage.NAME);
		
	}
		
	
}
