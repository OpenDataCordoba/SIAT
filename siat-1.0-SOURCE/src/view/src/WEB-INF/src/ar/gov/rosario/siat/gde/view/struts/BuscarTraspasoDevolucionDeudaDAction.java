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
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.gde.iface.model.AccionTraspasoDevolucion;
import ar.gov.rosario.siat.gde.iface.model.TraspasoDevolucionDeudaAdapter;
import ar.gov.rosario.siat.gde.iface.model.TraspasoDevolucionDeudaSearchPage;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import ar.gov.rosario.siat.pad.iface.model.CuentaSearchPage;
import ar.gov.rosario.siat.pad.view.struts.BuscarCuentaDAction;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.NavModel;

public final class BuscarTraspasoDevolucionDeudaDAction extends BaseDispatchAction {
	
	private Log log = LogFactory.getLog(BuscarTraspasoDevolucionDeudaDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_TRASPASO_DEVOLUCION_DEUDA, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			TraspasoDevolucionDeudaSearchPage traspasoDevolucionDeudaSearchPage = GdeServiceLocator.getGestionDeudaJudicialService().getTraspasoDevolucionDeudaSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (traspasoDevolucionDeudaSearchPage.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + traspasoDevolucionDeudaSearchPage.infoString()); 
				saveDemodaErrors(request, traspasoDevolucionDeudaSearchPage);
				return forwardErrorRecoverable(mapping, request, userSession, TraspasoDevolucionDeudaSearchPage.NAME, traspasoDevolucionDeudaSearchPage);
			} 

			// Tiene errores no recuperables
			if (traspasoDevolucionDeudaSearchPage.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + traspasoDevolucionDeudaSearchPage.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TraspasoDevolucionDeudaSearchPage.NAME, traspasoDevolucionDeudaSearchPage);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , TraspasoDevolucionDeudaSearchPage.NAME, traspasoDevolucionDeudaSearchPage);
			return mapping.findForward(GdeConstants.FWD_TRASPASO_DEVOLUCION_DEUDA_SEARCHPAGE);

			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TraspasoDevolucionDeudaSearchPage.NAME);
		}
	}
	
	public ActionForward buscarCuenta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);

		NavModel navModel = userSession.getNavModel();
		if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());

		// Bajo el adapter del userSession
		TraspasoDevolucionDeudaSearchPage traspasoDevolucionDeudaSearchPageVO = (TraspasoDevolucionDeudaSearchPage) userSession.get(TraspasoDevolucionDeudaSearchPage.NAME);

		// Si es nulo no se puede continuar
		if (traspasoDevolucionDeudaSearchPageVO == null) {
			log.error("error en: "  + funcName + ": " + TraspasoDevolucionDeudaSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, TraspasoDevolucionDeudaSearchPage.NAME); 
		}
		
		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(traspasoDevolucionDeudaSearchPageVO, request);

		// Tiene errores recuperables
		if (traspasoDevolucionDeudaSearchPageVO.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + traspasoDevolucionDeudaSearchPageVO.infoString()); 
			saveDemodaErrors(request, traspasoDevolucionDeudaSearchPageVO);
			return forwardErrorRecoverable(mapping, request, userSession, TraspasoDevolucionDeudaSearchPage.NAME, traspasoDevolucionDeudaSearchPageVO);
		}

		// carga de parametros a utilizar en la busqueda
		CuentaSearchPage cuentaFiltro = new CuentaSearchPage();
		
		// Seteo el recurso y numero de cuenta
		cuentaFiltro.getCuentaTitular().getCuenta().getRecurso().setId(
				traspasoDevolucionDeudaSearchPageVO.getRecurso().getId()); 
		cuentaFiltro.getCuentaTitular().getCuenta().setNumeroCuenta(
				traspasoDevolucionDeudaSearchPageVO.getCuenta().getNumeroCuenta());
		
		navModel.putParameter(BuscarCuentaDAction.CUENTA_SEARCHPAGE_FILTRO, cuentaFiltro);
		
		return forwardSeleccionar(mapping, request, "paramCuenta", PadConstants.ACTION_BUSCAR_CUENTA , false);
		
	}
	
	public ActionForward paramAccion(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			
			try {
				//bajo el adapter del usserSession
				TraspasoDevolucionDeudaSearchPage traspasoDevolucionDeudaSearchPageVO =  (TraspasoDevolucionDeudaSearchPage) userSession.get(TraspasoDevolucionDeudaSearchPage.NAME);
				
				// Si es nulo no se puede continuar
				if (traspasoDevolucionDeudaSearchPageVO == null) {
					log.error("error en: "  + funcName + ": " + TraspasoDevolucionDeudaSearchPage.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, TraspasoDevolucionDeudaSearchPage.NAME); 
				}
				
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(traspasoDevolucionDeudaSearchPageVO, request);
				
				// no visualizo la lista de resultados
				traspasoDevolucionDeudaSearchPageVO.setPageNumber(0L);
				
	            // Tiene errores recuperables
				if (traspasoDevolucionDeudaSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + traspasoDevolucionDeudaSearchPageVO.infoString()); 
					saveDemodaErrors(request, traspasoDevolucionDeudaSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, TraspasoDevolucionDeudaSearchPage.NAME, traspasoDevolucionDeudaSearchPageVO);
				}

				// no es necesario invocar al servicio
				
	            // Tiene errores recuperables
				if (traspasoDevolucionDeudaSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + traspasoDevolucionDeudaSearchPageVO.infoString()); 
					saveDemodaErrors(request, traspasoDevolucionDeudaSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						TraspasoDevolucionDeudaSearchPage.NAME, traspasoDevolucionDeudaSearchPageVO);
				}
				
				// Tiene errores no recuperables
				if (traspasoDevolucionDeudaSearchPageVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + traspasoDevolucionDeudaSearchPageVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						TraspasoDevolucionDeudaSearchPage.NAME, traspasoDevolucionDeudaSearchPageVO);
				}
				
				// grabo los mensajes si hubiere
				saveDemodaMessages(request, traspasoDevolucionDeudaSearchPageVO);
				
				// Envio el VO al request
				request.setAttribute(TraspasoDevolucionDeudaSearchPage.NAME, traspasoDevolucionDeudaSearchPageVO);

				return mapping.findForward(GdeConstants.FWD_TRASPASO_DEVOLUCION_DEUDA_SEARCHPAGE);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, TraspasoDevolucionDeudaSearchPage.NAME);
			}
		}

	public ActionForward paramRecurso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			
			try {
				//bajo el adapter del usserSession
				TraspasoDevolucionDeudaSearchPage traspasoDevolucionDeudaSearchPageVO =  (TraspasoDevolucionDeudaSearchPage) userSession.get(TraspasoDevolucionDeudaSearchPage.NAME);
				
				// Si es nulo no se puede continuar
				if (traspasoDevolucionDeudaSearchPageVO == null) {
					log.error("error en: "  + funcName + ": " + TraspasoDevolucionDeudaSearchPage.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, TraspasoDevolucionDeudaSearchPage.NAME); 
				}
				
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(traspasoDevolucionDeudaSearchPageVO, request);
				
				// no visualizo la lista de resultados
				traspasoDevolucionDeudaSearchPageVO.setPageNumber(0L);
				
	            // Tiene errores recuperables
				if (traspasoDevolucionDeudaSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + traspasoDevolucionDeudaSearchPageVO.infoString()); 
					saveDemodaErrors(request, traspasoDevolucionDeudaSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, TraspasoDevolucionDeudaSearchPage.NAME, traspasoDevolucionDeudaSearchPageVO);
				}

				traspasoDevolucionDeudaSearchPageVO = GdeServiceLocator.getGestionDeudaJudicialService().getTraspasoDevolucionDeudaSearchPageParamRecurso(userSession, traspasoDevolucionDeudaSearchPageVO);
				 
	            // Tiene errores recuperables
				if (traspasoDevolucionDeudaSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + traspasoDevolucionDeudaSearchPageVO.infoString()); 
					saveDemodaErrors(request, traspasoDevolucionDeudaSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						TraspasoDevolucionDeudaSearchPage.NAME, traspasoDevolucionDeudaSearchPageVO);
				}
				
				// Tiene errores no recuperables
				if (traspasoDevolucionDeudaSearchPageVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + traspasoDevolucionDeudaSearchPageVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						TraspasoDevolucionDeudaSearchPage.NAME, traspasoDevolucionDeudaSearchPageVO);
				}
				
				// grabo los mensajes si hubiere
				saveDemodaMessages(request, traspasoDevolucionDeudaSearchPageVO);
				
				// Envio el VO al request
				request.setAttribute(TraspasoDevolucionDeudaSearchPage.NAME, traspasoDevolucionDeudaSearchPageVO);

				return mapping.findForward(GdeConstants.FWD_TRASPASO_DEVOLUCION_DEUDA_SEARCHPAGE);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, TraspasoDevolucionDeudaSearchPage.NAME);
			}
		}


	public ActionForward paramCuenta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		NavModel navModel = userSession.getNavModel();

		try {

			// Bajo el SearchPage del userSession
			TraspasoDevolucionDeudaSearchPage traspasoDevolucionDeudaSearchPageVO = (TraspasoDevolucionDeudaSearchPage) userSession.get(TraspasoDevolucionDeudaSearchPage.NAME);

			// Si es nulo no se puede continuar
			if (traspasoDevolucionDeudaSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + TraspasoDevolucionDeudaSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TraspasoDevolucionDeudaSearchPage.NAME); 
			}
			
			// no visualizo la lista de resultados
			traspasoDevolucionDeudaSearchPageVO.setPageNumber(0L);

			// recupero el id seleccionado por el usuario
			String selectedId = navModel.getSelectedId();

			// si el id esta vacio, pq selecciono volver, forwardeo 
			if (StringUtil.isNullOrEmpty(selectedId)) {
				// Envio el VO al request
				request.setAttribute(TraspasoDevolucionDeudaSearchPage.NAME, traspasoDevolucionDeudaSearchPageVO);
				return mapping.findForward(GdeConstants.FWD_TRASPASO_DEVOLUCION_DEUDA_SEARCHPAGE); 
			}
			//traspasoDevolucionDeudaSearchPageVO.setPageNumber(1L);
			
			// Seteo el id de la cuenta seleccionada
			traspasoDevolucionDeudaSearchPageVO.getCuenta().setId(new Long(selectedId));

			// llamo al param del servicio
			traspasoDevolucionDeudaSearchPageVO = GdeServiceLocator.getGestionDeudaJudicialService().getTraspasoDevolucionDeudaSearchPageParamCuenta(userSession, traspasoDevolucionDeudaSearchPageVO);

			// Tiene errores recuperables
			if (traspasoDevolucionDeudaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + traspasoDevolucionDeudaSearchPageVO.infoString()); 
				saveDemodaErrors(request, traspasoDevolucionDeudaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
						TraspasoDevolucionDeudaSearchPage.NAME, traspasoDevolucionDeudaSearchPageVO);
			}

			// Tiene errores no recuperables
			if (traspasoDevolucionDeudaSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + traspasoDevolucionDeudaSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
						TraspasoDevolucionDeudaSearchPage.NAME, traspasoDevolucionDeudaSearchPageVO);
			}

			// grabo los mensajes si hubiere
			saveDemodaMessages(request, traspasoDevolucionDeudaSearchPageVO);

			// Envio el VO al request
			request.setAttribute(TraspasoDevolucionDeudaSearchPage.NAME, traspasoDevolucionDeudaSearchPageVO);

			return mapping.findForward(GdeConstants.FWD_TRASPASO_DEVOLUCION_DEUDA_SEARCHPAGE);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TraspasoDevolucionDeudaSearchPage.NAME);
		}
	}

	
	public ActionForward buscar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = getCurrentUserSession(request, mapping);		
		if (userSession == null) return forwardErrorSession(request);
		
		try {
		
			// Bajo el searchPage del userSession
			TraspasoDevolucionDeudaSearchPage traspasoDevolucionDeudaSearchPageVO = (TraspasoDevolucionDeudaSearchPage) userSession.get(TraspasoDevolucionDeudaSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (traspasoDevolucionDeudaSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + TraspasoDevolucionDeudaSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TraspasoDevolucionDeudaSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(traspasoDevolucionDeudaSearchPageVO, request);
				// Setea el PageNumber del PageModel
				
				String reqAttPageNumber = (String)userSession.get("reqAttPageNumber");
				if (!StringUtil.isNullOrEmpty(reqAttPageNumber))
					traspasoDevolucionDeudaSearchPageVO.setPageNumber(new Long(reqAttPageNumber));
				else
					traspasoDevolucionDeudaSearchPageVO.setPageNumber(new Long(traspasoDevolucionDeudaSearchPageVO.getRecsByPage()));
			}
			
            // Tiene errores recuperables
			if (traspasoDevolucionDeudaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + traspasoDevolucionDeudaSearchPageVO.infoString()); 
				saveDemodaErrors(request, traspasoDevolucionDeudaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, TraspasoDevolucionDeudaSearchPage.NAME, traspasoDevolucionDeudaSearchPageVO);
			}

			// Llamada al servicio	
			traspasoDevolucionDeudaSearchPageVO = GdeServiceLocator.getGestionDeudaJudicialService().getTraspasoDevolucionDeudaSearchPageResult(userSession, traspasoDevolucionDeudaSearchPageVO);

			// Tiene errores recuperables
			if (traspasoDevolucionDeudaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + traspasoDevolucionDeudaSearchPageVO.infoString()); 
				saveDemodaErrors(request, traspasoDevolucionDeudaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, TraspasoDevolucionDeudaSearchPage.NAME, traspasoDevolucionDeudaSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (traspasoDevolucionDeudaSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + traspasoDevolucionDeudaSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, TraspasoDevolucionDeudaSearchPage.NAME, traspasoDevolucionDeudaSearchPageVO);
			}
			
			// Envio el VO al request
			request.setAttribute(TraspasoDevolucionDeudaSearchPage.NAME, traspasoDevolucionDeudaSearchPageVO);
			// Nuleo el list result
			//traspasoDevolucionDeudaSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(TraspasoDevolucionDeudaSearchPage.NAME, traspasoDevolucionDeudaSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_TRASPASO_DEVOLUCION_DEUDA_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TraspasoDevolucionDeudaSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		UserSession userSession = getCurrentUserSession(request, mapping);		
		if (userSession == null) return forwardErrorSession(request);

		// Bajo el searchPage del userSession
		TraspasoDevolucionDeudaSearchPage traspasoDevolucionDeudaSearchPageVO = (TraspasoDevolucionDeudaSearchPage) userSession.get(TraspasoDevolucionDeudaSearchPage.NAME);
		
		// Si es nulo no se puede continuar
		if (traspasoDevolucionDeudaSearchPageVO == null) {
			log.error("error en: "  + funcName + ": " + TraspasoDevolucionDeudaSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, TraspasoDevolucionDeudaSearchPage.NAME); 
		}
		
		// obtengo la accion seleccionada y cargo su id como parametro para que este disponible en el siguiente action
		AccionTraspasoDevolucion accion = traspasoDevolucionDeudaSearchPageVO.getAccionTraspasoDevolucion();
		userSession.getNavModel().putParameter(TraspasoDevolucionDeudaAdapter.ACCION_KEY, accion.getId());

		return forwardVerSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_TRASPASO_DEVOLUCION_DEUDA);
	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_ENC_TRASPASO_DEVOLUCION_DEUDA);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		UserSession userSession = getCurrentUserSession(request, mapping);		
		if (userSession == null) return forwardErrorSession(request);

		// Bajo el searchPage del userSession
		TraspasoDevolucionDeudaSearchPage traspasoDevolucionDeudaSearchPageVO = (TraspasoDevolucionDeudaSearchPage) userSession.get(TraspasoDevolucionDeudaSearchPage.NAME);
		
		// Si es nulo no se puede continuar
		if (traspasoDevolucionDeudaSearchPageVO == null) {
			log.error("error en: "  + funcName + ": " + TraspasoDevolucionDeudaSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, TraspasoDevolucionDeudaSearchPage.NAME); 
		}
		
		// obtengo la accion seleccionada y cargo su id como parametro para que este disponible en el siguiente action
		AccionTraspasoDevolucion accion = traspasoDevolucionDeudaSearchPageVO.getAccionTraspasoDevolucion();
		userSession.getNavModel().putParameter(TraspasoDevolucionDeudaAdapter.ACCION_KEY, accion.getId());

		return forwardModificarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_ENC_TRASPASO_DEVOLUCION_DEUDA);	
	}
	
	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		UserSession userSession = getCurrentUserSession(request, mapping);		
		if (userSession == null) return forwardErrorSession(request);

		// Bajo el searchPage del userSession
		TraspasoDevolucionDeudaSearchPage traspasoDevolucionDeudaSearchPageVO = (TraspasoDevolucionDeudaSearchPage) userSession.get(TraspasoDevolucionDeudaSearchPage.NAME);
		
		// Si es nulo no se puede continuar
		if (traspasoDevolucionDeudaSearchPageVO == null) {
			log.error("error en: "  + funcName + ": " + TraspasoDevolucionDeudaSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, TraspasoDevolucionDeudaSearchPage.NAME); 
		}
		
		// obtengo la accion seleccionada y cargo su id como parametro para que este disponible en el siguiente action
		AccionTraspasoDevolucion accion = traspasoDevolucionDeudaSearchPageVO.getAccionTraspasoDevolucion();
		userSession.getNavModel().putParameter(TraspasoDevolucionDeudaAdapter.ACCION_KEY, accion.getId());

		return forwardEliminarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_TRASPASO_DEVOLUCION_DEUDA);
	}
	
	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, TraspasoDevolucionDeudaSearchPage.NAME);
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return baseVolver(mapping, form, request, response, TraspasoDevolucionDeudaSearchPage.NAME);
	}
	
}
