//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.seg.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.swe.SweServiceLocator;
import ar.gov.rosario.swe.base.view.struts.SweBaseDispatchAction;
import ar.gov.rosario.swe.iface.model.SweUserSession;
import ar.gov.rosario.swe.iface.model.UsrRolAplSearchPage;
import ar.gov.rosario.swe.iface.util.SweSecurityConstants;
import ar.gov.rosario.swe.seg.view.util.SweSegConstants;
import ar.gov.rosario.swe.view.util.SweUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class BuscarUsrRolAplDAction extends SweBaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarUsrRolAplDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String act = getCurrentAct(request);
		SweUserSession userSession = canAccess(request, mapping, SweSecurityConstants.ABM_ROLES, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		
		try {
			
			// obtiene el id de usrApl
			CommonKey usrAplCommonKey = new CommonKey(navModel.getSelectedId()); 
			
			UsrRolAplSearchPage usrRolAplSearchPageVO = SweServiceLocator.getSweService().getUsrRolAplSearchPageInit(userSession, usrAplCommonKey);
			
			// Tiene errores recuperables
			if (usrRolAplSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + usrRolAplSearchPageVO.infoString()); 
				saveDemodaErrors(request, usrRolAplSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, UsrRolAplSearchPage.NAME, usrRolAplSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (usrRolAplSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + usrRolAplSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, UsrRolAplSearchPage.NAME, usrRolAplSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , UsrRolAplSearchPage.NAME, usrRolAplSearchPageVO);
			
			usrRolAplSearchPageVO.setPageNumber(new Long(1));
			return buscar(mapping, form, request, response);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		SweUserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try{
			// seteo en el navModel el que estaba cargado en el inicializar
			userSession.setNavModel( (NavModel) userSession.get(mapping.getPath()) );
			return this.inicializar(mapping, form, request, response);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}
	
	public ActionForward buscar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		SweUserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// seteo la accion buscar del act para que muetre el boton agregar
			userSession.getNavModel().setAct(funcName);
			
			// Bajo el searchPage del userSession
			UsrRolAplSearchPage usrRolAplSearchPageVO = (UsrRolAplSearchPage) userSession.get(UsrRolAplSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (usrRolAplSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + UsrRolAplSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, UsrRolAplSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				if (form != null){ //se realiza porque viene directamente desde el inicializar
					DemodaUtil.populateVO(usrRolAplSearchPageVO, request); 
				}
				// Setea el PageNumber del PageModel
				usrRolAplSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (usrRolAplSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + usrRolAplSearchPageVO.infoString()); 
				saveDemodaErrors(request, usrRolAplSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, UsrRolAplSearchPage.NAME, usrRolAplSearchPageVO);
			}
				
			// Llamada al servicio	
			usrRolAplSearchPageVO = SweServiceLocator.getSweService().getUsrRolAplSearchPageResult(userSession, usrRolAplSearchPageVO);			

			// Tiene errores recuperables
			if (usrRolAplSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + usrRolAplSearchPageVO.infoString()); 
				saveDemodaErrors(request, usrRolAplSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, UsrRolAplSearchPage.NAME, usrRolAplSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (usrRolAplSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + usrRolAplSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, UsrRolAplSearchPage.NAME, usrRolAplSearchPageVO);
			}
			
			// Envio el VO al request
			request.setAttribute(UsrRolAplSearchPage.NAME, usrRolAplSearchPageVO);
			// Nuleo el list result
			//usrRolAplSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(UsrRolAplSearchPage.NAME, usrRolAplSearchPageVO);
			
			return mapping.findForward(SweSegConstants.FWD_USRROLAPL_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		SweUserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			baseVer(mapping, userSession, funcName);
			
			return mapping.findForward(SweSegConstants.ACTION_ADMINISTRAR_USRROLAPL);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		SweUserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {

			NavModel navModelUS = (NavModel) userSession.get(mapping.getPath());  // contiene la aplicacion sobre la que trabajo
			String   selecteId  = navModelUS.getSelectedId();                     // id de la aplicacion 
			baseAgregar(mapping, userSession, funcName, selecteId);
			
			return mapping.findForward(SweSegConstants.ACTION_ADMINISTRAR_USRROLAPL);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		SweUserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);

		try {
			
			baseEliminar(mapping, userSession, funcName);
			
			return mapping.findForward(SweSegConstants.ACTION_ADMINISTRAR_USRROLAPL);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		
		return baseSeleccionar(mapping, form, request, response, funcName, UsrRolAplSearchPage.NAME);
	}	

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		return baseVolver(mapping, form, request, response, UsrRolAplSearchPage.NAME);
	}
}
