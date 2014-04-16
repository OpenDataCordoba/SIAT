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
import ar.gov.rosario.siat.pad.iface.model.LocalidadSearchPage;
import ar.gov.rosario.siat.pad.iface.model.ProvinciaVO;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;

public final class BuscarLocalidadDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarLocalidadDAction.class);
		
	public final static String  PROVINCIA = "provincia";
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		//UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_LOCALIDAD, act);
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			ProvinciaVO provinciaFiltro = (ProvinciaVO) userSession.getNavModel().getParameter(BuscarLocalidadDAction.PROVINCIA);
			CommonKey provinciaKey = new CommonKey(provinciaFiltro.getId());
			LocalidadSearchPage localidadSearchPageVO = PadServiceLocator.getPadUbicacionService().getLocalidadSearchPageInit(userSession, provinciaKey );
			
			// Tiene errores recuperables
			if (localidadSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + localidadSearchPageVO.infoString()); 
				saveDemodaErrors(request, localidadSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, LocalidadSearchPage.NAME, localidadSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (localidadSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + localidadSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LocalidadSearchPage.NAME, localidadSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , LocalidadSearchPage.NAME, localidadSearchPageVO);
			
			return mapping.findForward(PadConstants.FWD_LOCALIDAD_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LocalidadSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, LocalidadSearchPage.NAME);
		
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
			LocalidadSearchPage localidadSearchPageVO = (LocalidadSearchPage) userSession.get(LocalidadSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (localidadSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + LocalidadSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, LocalidadSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(localidadSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				localidadSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
				// Setea el MaxRegistros del PageModel porque la busqueda pagina pero no hacemos un count
				localidadSearchPageVO.setMaxRegistros(new Long(1000));
			}
			
            // Tiene errores recuperables
			if (localidadSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + localidadSearchPageVO.infoString()); 
				saveDemodaErrors(request, localidadSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, LocalidadSearchPage.NAME, localidadSearchPageVO);
			}
				
			// Llamada al servicio	
			localidadSearchPageVO = PadServiceLocator.getPadUbicacionService().getLocalidadSearchPageResult(userSession, localidadSearchPageVO);			

			// Tiene errores recuperables
			if (localidadSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + localidadSearchPageVO.infoString()); 
				saveDemodaErrors(request, localidadSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, LocalidadSearchPage.NAME, localidadSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (localidadSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + localidadSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, LocalidadSearchPage.NAME, localidadSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(LocalidadSearchPage.NAME, localidadSearchPageVO);
			// Nuleo el list result
			//localidadSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(LocalidadSearchPage.NAME, localidadSearchPageVO);
			
			return mapping.findForward(PadConstants.FWD_LOCALIDAD_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LocalidadSearchPage.NAME);
		}
	}

	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, LocalidadSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, LocalidadSearchPage.NAME);
		
	}
		
	
}
