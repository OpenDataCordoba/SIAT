//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.pad.iface.model.CalleSearchPage;
import ar.gov.rosario.siat.pad.iface.model.CalleVO;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarCalleDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarCalleDAction.class);
	
	public static final String CALLE = "calle";
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		//UserSession userSession = canAccess(request, mapping, PadSecurityConstants.BUSQUEDA_CALLE, act);
		UserSession userSession = getCurrentUserSession(request, mapping);
		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			CalleVO calleFiltro = (CalleVO) userSession.getNavModel().getParameter(BuscarCalleDAction.CALLE);
			
			CalleSearchPage calleSearchPageVO = PadServiceLocator.getPadUbicacionService().getCalleSearchPageInit(userSession, calleFiltro);
			
			// Tiene errores recuperables
			if (calleSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + calleSearchPageVO.infoString()); 
				saveDemodaErrors(request, calleSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, CalleSearchPage.NAME, calleSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (calleSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + calleSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CalleSearchPage.NAME, calleSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , CalleSearchPage.NAME, calleSearchPageVO);
			
			return mapping.findForward(PadConstants.FWD_CALLE_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CalleSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, CalleSearchPage.NAME);
		
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
			CalleSearchPage calleSearchPageVO = (CalleSearchPage) userSession.get(CalleSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (calleSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + CalleSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CalleSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(calleSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				calleSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
				// Setea el MaxRegistros del PageModel porque la busqueda pagina pero no hacemos un count
				calleSearchPageVO.setMaxRegistros(new Long(1000));
			}
			
            // Tiene errores recuperables
			if (calleSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + calleSearchPageVO.infoString()); 
				saveDemodaErrors(request, calleSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, CalleSearchPage.NAME, calleSearchPageVO);
			}
				
			// Llamada al servicio	
			calleSearchPageVO = PadServiceLocator.getPadUbicacionService().getCalleSearchPageResult(userSession, calleSearchPageVO);			

			// Tiene errores recuperables
			if (calleSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + calleSearchPageVO.infoString()); 
				saveDemodaErrors(request, calleSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, CalleSearchPage.NAME, calleSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (calleSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + calleSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, CalleSearchPage.NAME, calleSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(CalleSearchPage.NAME, calleSearchPageVO);
			// Nuleo el list result
			//calleSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(CalleSearchPage.NAME, calleSearchPageVO);
			
			return mapping.findForward(PadConstants.FWD_CALLE_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CalleSearchPage.NAME);
		}
	}

/*	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_CALLE);

	}*/
	
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, CalleSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, CalleSearchPage.NAME);
		
	}
		
	
}
