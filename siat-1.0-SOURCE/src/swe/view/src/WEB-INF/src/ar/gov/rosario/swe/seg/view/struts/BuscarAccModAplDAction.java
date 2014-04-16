//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.seg.view.struts;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.swe.SweServiceLocator;
import ar.gov.rosario.swe.base.view.struts.SweBaseDispatchAction;
import ar.gov.rosario.swe.iface.model.AccModAplSearchPage;
import ar.gov.rosario.swe.iface.model.RolAccModAplSearchPage;
import ar.gov.rosario.swe.iface.model.SweUserSession;
import ar.gov.rosario.swe.iface.util.SweSecurityConstants;
import ar.gov.rosario.swe.seg.view.util.SweSegConstants;
import ar.gov.rosario.swe.view.util.SweConstants;
import ar.gov.rosario.swe.view.util.SweUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.NavModel;
import coop.tecso.demoda.iface.model.PageModel;

public final class BuscarAccModAplDAction extends SweBaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarAccModAplDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String act = getCurrentAct(request);
		SweUserSession userSession = canAccess(request, mapping, SweSecurityConstants.ABM_ACCIONESMODULO, act); 
		if (userSession == null) return forwardErrorSession(request);

		try {
			
			// tomo el id de modulo del userSession
			String selectedId = userSession.getNavModel().getSelectedId();
			log.debug("BuscarAccModAplDAction.inicializa: selectedId = " + userSession.getNavModel().getSelectedId());			

			
			AccModAplSearchPage accModAplSearchPageVO = new AccModAplSearchPage();
			accModAplSearchPageVO.setInitFor(AccModAplSearchPage.INIT_FOR_MODULO);
			
			// Seteo el id de Modulo
			accModAplSearchPageVO.getModApl().setId(new Long(selectedId)); 
			
			accModAplSearchPageVO =	SweServiceLocator.getSweService().getAccModAplSearchPageInit(userSession, accModAplSearchPageVO);
			
			// Tiene errores recuperables
			if (accModAplSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + accModAplSearchPageVO.infoString()); 
				saveDemodaErrors(request, accModAplSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, AccModAplSearchPage.NAME, accModAplSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (accModAplSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + accModAplSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AccModAplSearchPage.NAME, accModAplSearchPageVO);
			}
			
			accModAplSearchPageVO.setAplicacion(accModAplSearchPageVO.getModApl().getAplicacion()); // para no perder la aplicacion
			accModAplSearchPageVO.setPageNumber(new Long(1));
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , AccModAplSearchPage.NAME, accModAplSearchPageVO);
			
			return buscar(mapping, form, request, response);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}
	
	public ActionForward inicializarForMultiSelect(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String act = getCurrentAct(request);
		SweUserSession userSession = canAccess(request, mapping, SweSecurityConstants.ABM_ACCIONESMODULO, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
						
			RolAccModAplSearchPage rolAccModAplSearchPage = (RolAccModAplSearchPage)userSession.get(RolAccModAplSearchPage.NAME);
			
			if (rolAccModAplSearchPage == null || 
					rolAccModAplSearchPage.getAplicacion() == null){
				log.error("error en: "  + funcName + ": " + RolAccModAplSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, RolAccModAplSearchPage.NAME);					
			}
			
			AccModAplSearchPage accModAplSearchPageVO = new AccModAplSearchPage();
			accModAplSearchPageVO.setInitFor(AccModAplSearchPage.INIT_FOR_ROL);
			// Seteo el id de aplicacion 
			accModAplSearchPageVO.getAplicacion().setId(new Long(rolAccModAplSearchPage.getAplicacion().getId()));
			// Seteo el Rol seleccinado
			accModAplSearchPageVO.getModApl().setId(new Long(rolAccModAplSearchPage.getModApl().getId()));
			
			accModAplSearchPageVO.getRolApl().setId(new Long(rolAccModAplSearchPage.getRolApl().getId()));
			
			accModAplSearchPageVO =	SweServiceLocator.getSweService().getAccModAplSearchPageInit(userSession, accModAplSearchPageVO);
			
			// Tiene errores recuperables
			if (accModAplSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + accModAplSearchPageVO.infoString()); 
				saveDemodaErrors(request, accModAplSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, AccModAplSearchPage.NAME, accModAplSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (accModAplSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + accModAplSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AccModAplSearchPage.NAME, accModAplSearchPageVO);
			}
			
			accModAplSearchPageVO.setMultiSelectEnabled(true);
			
			accModAplSearchPageVO.setPaged(false);
			
			pasarNavModelActualAHistorial(mapping, funcName, userSession);
			
			// Envio el VO al request
			request.setAttribute(AccModAplSearchPage.NAME, accModAplSearchPageVO);
			// Vacio el list result
			accModAplSearchPageVO.setListResult(new ArrayList());
			// Subo en el el searchPage al userSession
			userSession.put(AccModAplSearchPage.NAME, accModAplSearchPageVO);
			
			return mapping.findForward(SweSegConstants.FWD_ACCMODAPL_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}
	
	public ActionForward inicializarConAplicacion(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = SweUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");
			
			String act = getCurrentAct(request);
			SweUserSession userSession = canAccess(request, mapping, SweSecurityConstants.ABM_ACCIONESMODULO, act); 
			if (userSession == null) return forwardErrorSession(request);
			
			try {
				
				// tomo el id de aplicacion del userSession
				String selectedId = userSession.getNavModel().getSelectedId();
				
				AccModAplSearchPage accModAplSearchPageVO = new AccModAplSearchPage();
				accModAplSearchPageVO.setInitFor(AccModAplSearchPage.INIT_FOR_ITEM_MENU); // para que vuelva aca
				
				// Seteo el id de Aplicacion
				accModAplSearchPageVO.getAplicacion().setId(new Long(selectedId));
				accModAplSearchPageVO.getModApl().setId(null); // sin modulo seleccionado
				accModAplSearchPageVO.setInactivo(true); // para que seleccione 
				accModAplSearchPageVO.setABMEnabled(PageModel.DISABLED); // para que no permita agregar, modificar y eliminar
				
				accModAplSearchPageVO.setHabilitarFiltroModulo(true); // habilito el uso del combo de modulos
				accModAplSearchPageVO =	SweServiceLocator.getSweService().getAccModAplSearchPageInit(userSession, accModAplSearchPageVO);
				
				// Tiene errores recuperables
				if (accModAplSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + accModAplSearchPageVO.infoString()); 
					saveDemodaErrors(request, accModAplSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, AccModAplSearchPage.NAME, accModAplSearchPageVO);
				} 

				// Tiene errores no recuperables
				if (accModAplSearchPageVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + accModAplSearchPageVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, AccModAplSearchPage.NAME, accModAplSearchPageVO);
				}
				
				// Si no tiene error
				
				userSession.getNavModel().setPrevAction("/seg/AdministrarItemMenu"); // para que vuelva al paramActualizarAccModApl 
				userSession.getNavModel().setPrevActionParameter("paramActualizarAccModApl");

				baseInicializarSearchPage(mapping, request, userSession , AccModAplSearchPage.NAME, accModAplSearchPageVO);
				
				return mapping.findForward(SweSegConstants.FWD_ACCMODAPL_SEARCHPAGE);
				
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
			

			// Bajo el searchPage del userSession
			AccModAplSearchPage accModAplSearchPageVO = (AccModAplSearchPage) userSession.get(AccModAplSearchPage.NAME);

			// Si es nulo no se puede continuar
			if (accModAplSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + AccModAplSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AccModAplSearchPage.NAME); 
			}

			// dependiendo de donde fue inicializado, tiene que retornar.
			if(AccModAplSearchPage.INIT_FOR_ITEM_MENU.equals(accModAplSearchPageVO.getInitFor())){
				return this.inicializarConAplicacion(mapping, form, request, response);
			}else if(AccModAplSearchPage.INIT_FOR_ROL.equals(accModAplSearchPageVO.getInitFor())){
				return this.inicializarForMultiSelect(mapping, form, request, response);
			}
			
			return this.inicializar(mapping, form, request, response);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}
	
	public ActionForward paramMod(ActionMapping mapping, ActionForm form,
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
				AccModAplSearchPage accModAplSearchPageVO = (AccModAplSearchPage) userSession.get(AccModAplSearchPage.NAME);
				
				// Si es nulo no se puede continuar
				if (accModAplSearchPageVO == null) {
					log.error("error en: "  + funcName + ": " + AccModAplSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, AccModAplSearchPage.NAME); 
				}

				// si el buscar diparado desde la pagina de busqueda
				if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
					// Recuperamos datos del form en el vo
					DemodaUtil.populateVO(accModAplSearchPageVO, request);
					// Setea el PageNumber del PageModel
					accModAplSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
				}

	            // Tiene errores recuperables
				if (accModAplSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + accModAplSearchPageVO.infoString()); 
					saveDemodaErrors(request, accModAplSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, AccModAplSearchPage.NAME, accModAplSearchPageVO);
				}
					
				// Llamada al servicio	
				accModAplSearchPageVO = SweServiceLocator.getSweService().getAccModAplSearchPageParamMod(userSession, accModAplSearchPageVO);			

				// Tiene errores recuperables
				if (accModAplSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + accModAplSearchPageVO.infoString()); 
					saveDemodaErrors(request, accModAplSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, AccModAplSearchPage.NAME, accModAplSearchPageVO);
				}
				
				// Tiene errores no recuperables
				if (accModAplSearchPageVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + accModAplSearchPageVO.errorString());
					return forwardErrorNonRecoverable(mapping, request, funcName, AccModAplSearchPage.NAME, accModAplSearchPageVO);
				}
				
				// Envio el VO al request
				request.setAttribute(AccModAplSearchPage.NAME, accModAplSearchPageVO);
				// Nuleo el list result
				//accModAplSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
				// Subo en el el searchPage al userSession
				userSession.put(AccModAplSearchPage.NAME, accModAplSearchPageVO);
				
				return mapping.findForward(SweSegConstants.FWD_ACCMODAPL_SEARCHPAGE);
				
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
			AccModAplSearchPage accModAplSearchPageVO = (AccModAplSearchPage) userSession.get(AccModAplSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (accModAplSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + AccModAplSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AccModAplSearchPage.NAME); 
			}

			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(accModAplSearchPageVO, request);
				// Setea el PageNumber del PageModel
				accModAplSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}

            // Tiene errores recuperables
			if (accModAplSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + accModAplSearchPageVO.infoString()); 
				saveDemodaErrors(request, accModAplSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, AccModAplSearchPage.NAME, accModAplSearchPageVO);
			}
				
			// Llamada al servicio	
			accModAplSearchPageVO = SweServiceLocator.getSweService().getAccModAplSearchPageResult(userSession, accModAplSearchPageVO);			

			// Tiene errores recuperables
			if (accModAplSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + accModAplSearchPageVO.infoString()); 
				saveDemodaErrors(request, accModAplSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, AccModAplSearchPage.NAME, accModAplSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (accModAplSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + accModAplSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, AccModAplSearchPage.NAME, accModAplSearchPageVO);
			}
			
			// Envio el VO al request
			request.setAttribute(AccModAplSearchPage.NAME, accModAplSearchPageVO);
			// Nuleo el list result
			//accModAplSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(AccModAplSearchPage.NAME, accModAplSearchPageVO);
			
			return mapping.findForward(SweSegConstants.FWD_ACCMODAPL_SEARCHPAGE);
			
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
			return mapping.findForward(SweSegConstants.ACTION_ADMINISTRAR_ACCMODAPL);
			
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
			
			// recupero el navModel inicial para obtener el id de aplicacion
			NavModel navModelUS = (NavModel) userSession.get(mapping.getPath());
			String idModApl = navModelUS.getSelectedId();

			log.debug("XXX selectedId: navmodelUS idModApl: " + navModelUS.getSelectedId());
			log.debug("XXX selectedId: navmodel idModApl: " + userSession.getNavModel().getSelectedId());

			baseAgregar(mapping, userSession, funcName, idModApl);

			return mapping.findForward(SweSegConstants.ACTION_ADMINISTRAR_ACCMODAPL);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}
	
	public ActionForward agregarMultiple(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		SweUserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		try {
			NavModel navModelUS = (NavModel)userSession.get(mapping.getPath());

			navModel.setPrevAction("/seg/BuscarRolAccModApl"); 
			navModel.setPrevActionParameter(SweConstants.ACT_BUSCAR);
			navModel.setPrevNavModel(navModelUS.getPrevNavModel());
			navModel.setSelectedId(navModelUS.getSelectedId()); // Guardo el id del rol seleccionado
			navModel.setAct(AdministrarRolAccModAplDAction.ACT_AGREGAR_MULTIPLE);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": navModel" + navModel.infoString());
			
			return mapping.findForward(SweSegConstants.ACTION_ADMINISTRAR_ROLACCMODAPL);
			
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
			
			return mapping.findForward(SweSegConstants.ACTION_ADMINISTRAR_ACCMODAPL);
			
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
			
			return mapping.findForward(SweSegConstants.ACTION_ADMINISTRAR_ACCMODAPL);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = SweUtil.currentMethodName();
		
		return baseSeleccionar(mapping, form, request, response, funcName, AccModAplSearchPage.NAME);
	}	

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, AccModAplSearchPage.NAME);
	}
	
}
