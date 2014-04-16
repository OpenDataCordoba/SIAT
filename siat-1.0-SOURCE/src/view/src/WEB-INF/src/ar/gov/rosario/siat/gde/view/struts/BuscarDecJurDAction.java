//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.gde.iface.model.DecJurSearchPage;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

public final class BuscarDecJurDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarDecJurDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession;
		Long idDeuda = null;
		log.debug(funcName + " idDeuda: " + request.getParameter("idDeuda"));

		//verificamos si nos pasaron un idDeuda, si viene NO verificamos permiso,
		//sino es un inicializar comun, y verificamos por los permisos.
		if (!StringUtil.isNullOrEmpty(request.getParameter("idDeuda"))) {
			idDeuda = new Long(request.getParameter("idDeuda"));
		
			userSession = getCurrentUserSession(request, mapping);
			if (userSession==null) return forwardErrorSession(request);
		} else {
			
			userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DECJUR, act);
			if (userSession == null) return forwardErrorSession(request);
		}
		
		//-Mantis#6264: si vengo desde el menu, pongo el idDeuda en null, asi no tengo problemas con el volver
		if (BaseConstants.ACT_SIAT_MENU.equals(userSession.getNavModel().getPrevAction())) {
			idDeuda=null;
		}
		
		try {
			
			DecJurSearchPage decJurSeachPageVO = (DecJurSearchPage) userSession.get(DecJurSearchPage.NAME); 
			
			if (decJurSeachPageVO == null)
				decJurSeachPageVO = new DecJurSearchPage();
			
			decJurSeachPageVO.getDecJur().setIdDeuda(idDeuda);
			
			decJurSeachPageVO = GdeServiceLocator.getGestionDeudaService().getDecJurSearchPageInit(userSession, decJurSeachPageVO);
			
			// Tiene errores recuperables
			if (decJurSeachPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + decJurSeachPageVO.infoString()); 
				saveDemodaErrors(request, decJurSeachPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, DecJurSearchPage.NAME, decJurSeachPageVO);
			} 

			// Tiene errores no recuperables
			if (decJurSeachPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + decJurSeachPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DecJurSearchPage.NAME, decJurSeachPageVO);
			}
			
			// Si idDeuda no es nula
			if (decJurSeachPageVO.getListResult().size()>0){
				
				// seteo los valores del navegacion en el pageModel
				decJurSeachPageVO.setValuesFromNavModel(userSession.getNavModel());
				if (idDeuda!=null)
					decJurSeachPageVO.setModoVer(true);
				
				// Envio el VO al request
				request.setAttribute(DecJurSearchPage.NAME, decJurSeachPageVO);

				// Subo en el el searchPage al userSession
				userSession.put(DecJurSearchPage.NAME, decJurSeachPageVO);
				
			} else {
				if (idDeuda!=null)
					decJurSeachPageVO.setModoVer(true);
				baseInicializarSearchPage(mapping, request, userSession , DecJurSearchPage.NAME, decJurSeachPageVO);
			}
			
			return mapping.findForward(GdeConstants.FWD_DECJUD_SEARCHPAGE);
			
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DecJurSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, DecJurSearchPage.NAME);
		
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
			DecJurSearchPage decJurSearchPageVO = (DecJurSearchPage) userSession.get(DecJurSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (decJurSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + DecJurSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DecJurSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(decJurSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				decJurSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (decJurSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + decJurSearchPageVO.infoString()); 
				saveDemodaErrors(request, decJurSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, DecJurSearchPage.NAME, decJurSearchPageVO);
			}
				
			// Llamada al servicio	
			decJurSearchPageVO = GdeServiceLocator.getGestionDeudaService().getDecJurSearchPageResult(userSession, decJurSearchPageVO);			

			// Tiene errores recuperables
			if (decJurSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + decJurSearchPageVO.infoString()); 
				saveDemodaErrors(request, decJurSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, DecJurSearchPage.NAME, decJurSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (decJurSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + decJurSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, DecJurSearchPage.NAME, decJurSearchPageVO);
			}
		
			decJurSearchPageVO.setValuesFromNavModel(userSession.getNavModel());
			// Envio el VO al request
			request.setAttribute(DecJurSearchPage.NAME, decJurSearchPageVO);
			// Nuleo el list result
			//convenioSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(DecJurSearchPage.NAME, decJurSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_DECJUD_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DecJurSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		return forwardVerSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_DECJUD);
		
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, DecJurSearchPage.NAME);
		
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String funcName= DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_DECJUD);
	}
	
	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String funcName= DemodaUtil.currentMethodName();
		
			return forwardModificarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_DECJUD);
			
	}
	
	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName= DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_DECJUD);
			
	}
	
	public ActionForward vueltaAtras(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName= DemodaUtil.currentMethodName();
		return baseForwardSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_DECJUD, BaseConstants.ACT_VUELTA_ATRAS);
	}
		
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, DecJurSearchPage.NAME);
			
	}
	
}
