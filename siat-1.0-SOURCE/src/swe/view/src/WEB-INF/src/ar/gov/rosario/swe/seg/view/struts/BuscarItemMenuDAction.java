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
import ar.gov.rosario.swe.iface.model.ItemMenuSearchPage;
import ar.gov.rosario.swe.iface.model.ItemMenuVO;
import ar.gov.rosario.swe.iface.model.SweUserSession;
import ar.gov.rosario.swe.iface.util.SweSecurityConstants;
import ar.gov.rosario.swe.seg.view.util.SweSegConstants;
import ar.gov.rosario.swe.view.util.SweConstants;
import ar.gov.rosario.swe.view.util.SweUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class BuscarItemMenuDAction extends SweBaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarItemMenuDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String act = getCurrentAct(request);
		SweUserSession userSession = canAccess(request, mapping, SweSecurityConstants.ABM_MENU, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		
		try {
			
			// obtiene el id de Aplicacion
			CommonKey aplicacionCommonKey = new CommonKey(navModel.getSelectedId());
			log.debug("FFF XXX selectedId: " + navModel.getSelectedId());
			userSession.put("idAplicacion", aplicacionCommonKey.getId().toString());
			
			ItemMenuSearchPage itemMenuSearchPageVO = SweServiceLocator.getSweService().getItemMenuSearchPageInit(userSession, aplicacionCommonKey);
			
			// Tiene errores recuperables
			if (itemMenuSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + itemMenuSearchPageVO.infoString()); 
				saveDemodaErrors(request, itemMenuSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ItemMenuSearchPage.NAME, itemMenuSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (itemMenuSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + itemMenuSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ItemMenuSearchPage.NAME, itemMenuSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , ItemMenuSearchPage.NAME, itemMenuSearchPageVO);
			
			//itemMenuSearchPageVO.setPageNumber(new Long(1));
			return buscar(mapping, null, request, response);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}
	
	public ActionForward inicializarHijos(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = SweUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");
			
			String act = getCurrentAct(request);
			SweUserSession userSession = canAccess(request, mapping, SweSecurityConstants.ABM_MENU, act); 
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			
			try {
				
				//NavModel navModelUS = (NavModel)userSession.get(mapping.getPath());

				//navModel.setPrevAction(mapping.getPath()); 
				//navModel.setPrevActionParameter(SweConstants.ACT_BUSCAR);
				//navModel.setPrevNavModel(navModelUS.getPrevNavModel());
				
				if (((String) userSession.get("reqAttIsSubmittedForm")).equals("true")) {
					navModel.setSelectedId((String)userSession.get("reqAttSelectedId"));
				}
				//navModel.setAct(act);
				
				// obtiene el id de ItemMenu Padre
				CommonKey itemMenuPadreCommonKey = new CommonKey(navModel.getSelectedId()); 
				
				ItemMenuSearchPage itemMenuSearchPageVO = SweServiceLocator.getSweService().getItemMenuHijosSearchPageInit(userSession, itemMenuPadreCommonKey);
				
				// Tiene errores recuperables 
				if (itemMenuSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + itemMenuSearchPageVO.infoString()); 
					saveDemodaErrors(request, itemMenuSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, ItemMenuSearchPage.NAME, itemMenuSearchPageVO);
				} 

				// Tiene errores no recuperables
				if (itemMenuSearchPageVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + itemMenuSearchPageVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, ItemMenuSearchPage.NAME, itemMenuSearchPageVO);
				}
				
				// Si no tiene error
				baseInicializarSearchPage(mapping, request, userSession , ItemMenuSearchPage.NAME, itemMenuSearchPageVO);
				
				//return mapping.findForward(SegConstants.FWD_ITEM_MENU_SEARCHPAGE);
				return buscar(mapping, null, request, response);
				
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
			ItemMenuSearchPage itemMenuSearchPageVO = (ItemMenuSearchPage) userSession.get(ItemMenuSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (itemMenuSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + ItemMenuSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ItemMenuSearchPage.NAME); 
			}

			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				if (form != null){ //se realiza porque viene directamente desde el inicializar
					DemodaUtil.populateVO(itemMenuSearchPageVO, request); 
				}
				// Setea el PageNumber del PageModel				
				itemMenuSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (itemMenuSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + itemMenuSearchPageVO.infoString()); 
				saveDemodaErrors(request, itemMenuSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ItemMenuSearchPage.NAME, itemMenuSearchPageVO);
			}
				
			// Llamada al servicio	
			itemMenuSearchPageVO = SweServiceLocator.getSweService().getItemMenuSearchPageResult(userSession, itemMenuSearchPageVO);			

			// Tiene errores recuperables
			if (itemMenuSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + itemMenuSearchPageVO.infoString()); 
				saveDemodaErrors(request, itemMenuSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ItemMenuSearchPage.NAME, itemMenuSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (itemMenuSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + itemMenuSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, ItemMenuSearchPage.NAME, itemMenuSearchPageVO);
			}
			
			// Envio el VO al request
			request.setAttribute(ItemMenuSearchPage.NAME, itemMenuSearchPageVO);
			// Nuleo el list result
			//itemMenuSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(ItemMenuSearchPage.NAME, itemMenuSearchPageVO);
			
			return mapping.findForward(SweSegConstants.FWD_ITEM_MENU_SEARCHPAGE);
			
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
			
			return mapping.findForward(SweSegConstants.ACTION_ADMINISTRAR_ITEM_MENU);
			
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
			
			NavModel navModel = userSession.getNavModel();
			
			navModel.setPrevAction(mapping.getPath()); 
			navModel.setPrevActionParameter(SweConstants.ACT_BUSCAR);
			navModel.setPrevNavModel(navModelUS.getPrevNavModel());
			navModel.setSelectedId(selecteId);
						
			// Bajo el searchPage del userSession
			ItemMenuSearchPage itemMenuSearchPageVO = (ItemMenuSearchPage) userSession.get(ItemMenuSearchPage.NAME);

			if(!ModelUtil.isNullOrEmpty(itemMenuSearchPageVO.getItemMenu())){
				// estamos trabajando sobre un item de menu padre
				navModel.setAct(SweSegConstants.ACT_AGREGAR_HIJO);
			}else{
				// estamos trabajando sobre la lista de menu roots.
				navModel.setAct(SweSegConstants.ACT_AGREGAR_ROOT);
			}
			
			return mapping.findForward(SweSegConstants.ACTION_ADMINISTRAR_ITEM_MENU);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		SweUserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);

		try {

			baseModificar(mapping, userSession, funcName);	
			
			return mapping.findForward(SweSegConstants.ACTION_ADMINISTRAR_ITEM_MENU);
			
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
			
			return mapping.findForward(SweSegConstants.ACTION_ADMINISTRAR_ITEM_MENU);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		
		return baseSeleccionar(mapping, form, request, response, funcName, ItemMenuSearchPage.NAME);
	}	

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		SweUserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);

		ActionForward actionForward = null;

		// Bajo el searchPage del userSession
		ItemMenuSearchPage itemMenuSearchPageVO = (ItemMenuSearchPage) userSession.get(ItemMenuSearchPage.NAME);
		
		ItemMenuVO itemMenuVO = itemMenuSearchPageVO.getItemMenu();
		
		// si tiene padre voy a inicializarHijos con el id del padre en el nav model
		if(!ModelUtil.isNullOrEmpty(itemMenuVO.getItemMenuPadre())){
			String idPadre = itemMenuVO.getItemMenuPadre().getId().toString();
			userSession.getNavModel().setSelectedId(idPadre);
			actionForward = this.inicializarHijos(mapping, form, request, response);
		}

		// si no tiene padre voy a inicializar con el id de la aplicacion en el nav model
		if(ModelUtil.isNullOrEmpty(itemMenuVO.getItemMenuPadre())){
			String idAplicacion = (String) userSession.get("idAplicacion");
			userSession.getNavModel().setSelectedId(idAplicacion);
			// elimino el idAplicacion del usserSession
			userSession.remove("idAplicacion");
			actionForward = this.inicializar(mapping, form, request, response);
		}
		
		// si no tiene item de menu vuelvo a buscar aplicacion
		if(ModelUtil.isNullOrEmpty(itemMenuVO)){
			actionForward = mapping.findForward( SweSegConstants.ACTION_VOLVER_BUSCAR_APLICACION );
		}

		return actionForward;
	}
	
	
	public ActionForward buscarAccionModulo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		SweUserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);

		try {

			baseGenerico(mapping, userSession, funcName, SweSegConstants.ACT_MODIFICAR_ACC_MOD_APL);	
			
			return mapping.findForward(SweSegConstants.ACTION_ADMINISTRAR_ITEM_MENU);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward buscarItemMenuHijos(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		SweUserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);

		try {

			baseGenerico(mapping, userSession, funcName, SweSegConstants.ACT_ADMINISTRAR_ITEM_MENU_HIJOS);	
			
			return mapping.findForward(SweSegConstants.ACTION_ADMINISTRAR_ITEM_MENU);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

}

