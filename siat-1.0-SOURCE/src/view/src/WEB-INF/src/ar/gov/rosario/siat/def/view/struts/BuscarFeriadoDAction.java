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
import ar.gov.rosario.siat.def.iface.model.FeriadoSearchPage;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarFeriadoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarFeriadoDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_FERIADO, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			FeriadoSearchPage feriadoSearchPageVO = DefServiceLocator.getGravamenService().getFeriadoSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (feriadoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + feriadoSearchPageVO.infoString()); 
				saveDemodaErrors(request, feriadoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, FeriadoSearchPage.NAME, feriadoSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (feriadoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + feriadoSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, FeriadoSearchPage.NAME, feriadoSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , FeriadoSearchPage.NAME, feriadoSearchPageVO);
			
			return mapping.findForward(DefConstants.FWD_FERIADO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, FeriadoSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, FeriadoSearchPage.NAME);
		
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
			FeriadoSearchPage feriadoSearchPageVO = (FeriadoSearchPage) userSession.get(FeriadoSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (feriadoSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + FeriadoSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, FeriadoSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(feriadoSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				feriadoSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (feriadoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + feriadoSearchPageVO.infoString()); 
				saveDemodaErrors(request, feriadoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, FeriadoSearchPage.NAME, feriadoSearchPageVO);
			}
				
			// Llamada al servicio	
			feriadoSearchPageVO = DefServiceLocator.getGravamenService().getFeriadoSearchPageResult(userSession, feriadoSearchPageVO);			

			// Tiene errores recuperables
			if (feriadoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + feriadoSearchPageVO.infoString()); 
				saveDemodaErrors(request, feriadoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, FeriadoSearchPage.NAME, feriadoSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (feriadoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + feriadoSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, FeriadoSearchPage.NAME, feriadoSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(FeriadoSearchPage.NAME, feriadoSearchPageVO);
			// Nuleo el list result
			//feriadoSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(FeriadoSearchPage.NAME, feriadoSearchPageVO);
			
			return mapping.findForward(DefConstants.FWD_FERIADO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, FeriadoSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_FERIADO);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_FERIADO);
		
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_FERIADO);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_FERIADO);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_FERIADO);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_FERIADO);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, FeriadoSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, FeriadoSearchPage.NAME);
		
	}
		
	
}
