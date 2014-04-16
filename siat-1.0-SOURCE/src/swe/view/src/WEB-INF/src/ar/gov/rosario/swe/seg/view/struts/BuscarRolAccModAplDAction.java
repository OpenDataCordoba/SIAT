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
import ar.gov.rosario.swe.iface.model.RolAccModAplSearchPage;
import ar.gov.rosario.swe.iface.model.SweUserSession;
import ar.gov.rosario.swe.iface.util.SweSecurityConstants;
import ar.gov.rosario.swe.seg.view.util.SweSegConstants;
import ar.gov.rosario.swe.view.util.SweConstants;
import ar.gov.rosario.swe.view.util.SweUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class BuscarRolAccModAplDAction extends SweBaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarRolAccModAplDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String act = getCurrentAct(request);
		SweUserSession userSession = canAccess(request, mapping, SweSecurityConstants.ABM_ACCIONESROL, act); 
		if (userSession == null) return forwardErrorSession(request);

		RolAccModAplSearchPage rolAccModAplSearchPageVO = null;		
		try {
			
			// tomo el id de rolApl del userSession
			String selectedId = userSession.getNavModel().getSelectedId();
			CommonKey rolAplKey = new CommonKey (selectedId);
			
			rolAccModAplSearchPageVO = SweServiceLocator.getSweService().getRolAccModAplSearchPageInit(userSession, rolAplKey);
			
			// Tiene errores recuperables
			if (rolAccModAplSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + rolAccModAplSearchPageVO.infoString()); 
				saveDemodaErrors(request, rolAccModAplSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, RolAccModAplSearchPage.NAME, rolAccModAplSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (rolAccModAplSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + rolAccModAplSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, RolAccModAplSearchPage.NAME, rolAccModAplSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , RolAccModAplSearchPage.NAME, rolAccModAplSearchPageVO);
			rolAccModAplSearchPageVO.setPageNumber(new Long(1));
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
			// seteo la accion buscar al pervActionParameter para que muetre el boton agregar
			userSession.getNavModel().setAct(funcName);
			
			// Bajo el searchPage del userSession
			RolAccModAplSearchPage rolAccModAplSearchPageVO = (RolAccModAplSearchPage) userSession.get(RolAccModAplSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (rolAccModAplSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + RolAccModAplSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, RolAccModAplSearchPage.NAME); 
			}
			
			// TODO: ver si se puede sacar, lo remplaza el "reqAttIsSubmittedForm" de abajo
			/*
			if (request.getParameter("pageName") != null &&
					request.getParameter("pageName").equals("rolAccModAplSearchPage")){
				// Recuperamos datos del form en el vo
				populateVO(rolAccModAplSearchPageVO, form);
			}
			*/
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(rolAccModAplSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				rolAccModAplSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}			
			
            // Tiene errores recuperables
			if (rolAccModAplSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + rolAccModAplSearchPageVO.infoString()); 
				saveDemodaErrors(request, rolAccModAplSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, RolAccModAplSearchPage.NAME, rolAccModAplSearchPageVO);
			}
				
			// Llamada al servicio	
			rolAccModAplSearchPageVO = SweServiceLocator.getSweService().getRolAccModAplSearchPageResult(userSession, rolAccModAplSearchPageVO);			

			// Tiene errores recuperables
			if (rolAccModAplSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + rolAccModAplSearchPageVO.infoString()); 
				saveDemodaErrors(request, rolAccModAplSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, RolAccModAplSearchPage.NAME, rolAccModAplSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (rolAccModAplSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + rolAccModAplSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, RolAccModAplSearchPage.NAME, rolAccModAplSearchPageVO);
			}
			
			// Envio el VO al request
			request.setAttribute(RolAccModAplSearchPage.NAME, rolAccModAplSearchPageVO);
			// Nuleo el list result
			//accModAplSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(RolAccModAplSearchPage.NAME, rolAccModAplSearchPageVO);
			
			return mapping.findForward(SweSegConstants.FWD_ROLACCMODAPL_SEARCHPAGE);
			
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
			
			return mapping.findForward(SweSegConstants.ACTION_ADMINISTRAR_ROLACCMODAPL);
			
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
			
			return mapping.findForward(SweSegConstants.ACTION_ADMINISTRAR_ROLACCMODAPL);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}
	
	//	va a la busqueda de Acciones Modulo 
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		SweUserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);

		try {
			
			// Bajo el searchPage del userSession
			RolAccModAplSearchPage rolAccModAplSearchPageVO = (RolAccModAplSearchPage) userSession.get(RolAccModAplSearchPage.NAME);
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(rolAccModAplSearchPageVO, request);
			
		/*	if (ModelUtil.isNullOrEmpty(rolAccModAplSearchPageVO.getModApl())) {
				rolAccModAplSearchPageVO.addRecoverableError(new DemodaStringMsg(-1, "swe.seg.rolAccModAplSearchPage.modulo.required"));
			}*/

            // Tiene errores recuperables
			if (rolAccModAplSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + rolAccModAplSearchPageVO.infoString()); 
				saveDemodaErrors(request, rolAccModAplSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, RolAccModAplSearchPage.NAME, rolAccModAplSearchPageVO);
			}



			NavModel navModel = userSession.getNavModel();
			NavModel navModelUS = (NavModel)userSession.get(mapping.getPath());

			navModel.setPrevAction("/seg/BuscarRolAccModApl"); 
			navModel.setPrevActionParameter(SweConstants.ACT_BUSCAR);
			navModel.setPrevNavModel(navModelUS.getPrevNavModel());
			navModel.setSelectedId((String)userSession.get("reqAttSelectedId"));
			navModel.setAct(SweConstants.ACT_SELECCIONAR);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": navModel" + navModel.infoString());

			return mapping.findForward(SweSegConstants.ACTION_BUSCAR_ACCMODAPL);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, RolAccModAplSearchPage.NAME);
	}

}
