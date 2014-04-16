//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.view.struts;
        
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.esp.iface.model.HabilitacionSearchPage;
import ar.gov.rosario.siat.esp.iface.service.EspServiceLocator;
import ar.gov.rosario.siat.esp.iface.util.EspSecurityConstants;
import ar.gov.rosario.siat.esp.view.util.EspConstants;
import ar.gov.rosario.siat.pad.iface.model.CuentaSearchPage;
import ar.gov.rosario.siat.pad.iface.model.PersonaVO;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import ar.gov.rosario.siat.pad.view.struts.BuscarCuentaDAction;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.NavModel;
import coop.tecso.demoda.iface.model.PrintModel;

public class BuscarHabilitacionDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarHabilitacionDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		UserSession userSession = canAccess(request, mapping, EspSecurityConstants.ABM_HABILITACION, act);		
		if (userSession==null) return forwardErrorSession(request);
		try {
		
			HabilitacionSearchPage habilitacionSearchPageVO = EspServiceLocator.getHabilitacionService().getHabilitacionSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (habilitacionSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + habilitacionSearchPageVO.infoString()); 
				saveDemodaErrors(request, habilitacionSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, HabilitacionSearchPage.NAME, habilitacionSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (habilitacionSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + habilitacionSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, HabilitacionSearchPage.NAME, habilitacionSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , HabilitacionSearchPage.NAME, habilitacionSearchPageVO);
			
			return mapping.findForward(EspConstants.FWD_HABILITACION_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, HabilitacionSearchPage.NAME);
		}
	}
	
	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return this.baseRefill(mapping, form, request, response, funcName, HabilitacionSearchPage.NAME);

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
			HabilitacionSearchPage habilitacionSearchPageVO = (HabilitacionSearchPage) userSession.get(HabilitacionSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (habilitacionSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + HabilitacionSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, HabilitacionSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(habilitacionSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				habilitacionSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (habilitacionSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + habilitacionSearchPageVO.infoString()); 
				saveDemodaErrors(request, habilitacionSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, HabilitacionSearchPage.NAME, habilitacionSearchPageVO);
			}

			// Llamada al servicio	
			habilitacionSearchPageVO = EspServiceLocator.getHabilitacionService().getHabilitacionSearchPageResult(userSession, habilitacionSearchPageVO);			

			// Tiene errores recuperables
			if (habilitacionSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + habilitacionSearchPageVO.infoString()); 
				saveDemodaErrors(request, habilitacionSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, HabilitacionSearchPage.NAME, habilitacionSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (habilitacionSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + habilitacionSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, HabilitacionSearchPage.NAME, habilitacionSearchPageVO);
			}
			
			// Envio el VO al request
			request.setAttribute(HabilitacionSearchPage.NAME, habilitacionSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(HabilitacionSearchPage.NAME, habilitacionSearchPageVO);
			
			return mapping.findForward(EspConstants.FWD_HABILITACION_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, HabilitacionSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardVerSearchPage(mapping, request, funcName, EspConstants.ACTION_ADMINISTRAR_HABILITACION);
	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardAgregarSearchPage(mapping, request, funcName, EspConstants.ACTION_ADMINISTRAR_ENCHABILITACION);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardModificarSearchPage(mapping, request, funcName, EspConstants.ACTION_ADMINISTRAR_HABILITACION);	
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardEliminarSearchPage(mapping, request, funcName, EspConstants.ACTION_ADMINISTRAR_HABILITACION);
			
		}

	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseSeleccionar(mapping, request, response, funcName, HabilitacionSearchPage.NAME);
	}	

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			return baseVolver(mapping, form, request, response, HabilitacionSearchPage.NAME);
			
	}

	public ActionForward buscarCuenta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());
				
		HabilitacionSearchPage  habilitacionSearchPage = (HabilitacionSearchPage) userSession.get(HabilitacionSearchPage.NAME);
		
		// Si es nulo no se puede continuar
		if (habilitacionSearchPage == null) {
			log.error("error en: "  + funcName + ": " + HabilitacionSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, HabilitacionSearchPage.NAME); 
		}
		
		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(habilitacionSearchPage, request);
		
        // Tiene errores recuperables
		if (habilitacionSearchPage.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + habilitacionSearchPage.infoString()); 
			saveDemodaErrors(request, habilitacionSearchPage);
			return forwardErrorRecoverable(mapping, request, userSession, HabilitacionSearchPage.NAME, habilitacionSearchPage);
		}
		
		CuentaSearchPage cuentaFiltro = new CuentaSearchPage();
		cuentaFiltro.getCuentaTitular().getCuenta().setRecurso(habilitacionSearchPage.getHabilitacion().getRecurso());
		cuentaFiltro.getCuentaTitular().getCuenta().setNumeroCuenta(habilitacionSearchPage.getHabilitacion().getCuenta().getNumeroCuenta());
		
		navModel.putParameter(BuscarCuentaDAction.CUENTA_SEARCHPAGE_FILTRO, cuentaFiltro);

		//return mapping.findForward(PadConstants.ACTION_BUSCAR_CUENTA);
		return forwardSeleccionar(mapping, request, 
				"paramCuenta", PadConstants.ACTION_BUSCAR_CUENTA , false);
	}
	
	public ActionForward paramCuenta (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			HabilitacionSearchPage habilitacionSearchPageVO = (HabilitacionSearchPage) userSession.get(HabilitacionSearchPage.NAME);
			
			// Para que no se muestre el resultSet
			habilitacionSearchPageVO.setPageNumber(0L);
			
			// Si es nulo no se puede continuar
			if (habilitacionSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + HabilitacionSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, HabilitacionSearchPage.NAME); 
			}


			// recupero el id seleccionado por el usuario
			String selectedId = request.getParameter("selectedId");
						
			// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
			if (StringUtil.isNullOrEmpty(selectedId)) {
				// Envio el VO al request
				request.setAttribute(HabilitacionSearchPage.NAME, habilitacionSearchPageVO);
				return mapping.findForward(EspConstants.FWD_HABILITACION_SEARCHPAGE); 
			}

			// Seteo el id selecionado
			habilitacionSearchPageVO.getHabilitacion().getCuenta().setId(new Long(selectedId));
			
			// llamada al servicio
			habilitacionSearchPageVO = EspServiceLocator.getHabilitacionService()
				.getHabilitacionSearchPageParamCuenta(userSession, habilitacionSearchPageVO);
			
            // Tiene errores recuperables
			if (habilitacionSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + habilitacionSearchPageVO.infoString()); 
				saveDemodaErrors(request, habilitacionSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, HabilitacionSearchPage.NAME, habilitacionSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (habilitacionSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + habilitacionSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, HabilitacionSearchPage.NAME, habilitacionSearchPageVO);
			}
			
			// Envio el VO al request
			request.setAttribute(HabilitacionSearchPage.NAME, habilitacionSearchPageVO);
			// Subo el apdater al userSession
			userSession.put(HabilitacionSearchPage.NAME, habilitacionSearchPageVO);
			
			return mapping.findForward(EspConstants.FWD_HABILITACION_SEARCHPAGE);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, HabilitacionSearchPage.NAME);
		}
	}
	
	public ActionForward buscarPersonaSimple(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();		
		
		UserSession userSession = getCurrentUserSession(request, mapping);			
		if (userSession == null) return forwardErrorSession(request);
				
		try {
			// Bajo el adapter del userSession
			HabilitacionSearchPage habilitacionSearchPageVO = (HabilitacionSearchPage)userSession.get(HabilitacionSearchPage.NAME);
	
			// Si es nulo no se puede continuar
			if (habilitacionSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + HabilitacionSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, HabilitacionSearchPage.NAME); 
			}
			
			// Reseteamos la persona utilizada como filtro
			habilitacionSearchPageVO.setTitular(new PersonaVO());
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(habilitacionSearchPageVO, request);

			// Para que no se muestre el resultSet
			habilitacionSearchPageVO.setPageNumber(0L);

			// Tiene errores recuperables solo puede ser por formato en el numero de documento
			if (habilitacionSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + habilitacionSearchPageVO.infoString()); 
								
				habilitacionSearchPageVO.getTitular().setPersonaBuscada(true);
				habilitacionSearchPageVO.getTitular().addRecoverableValueError("El formato del campo N\u00FAmero Doc. es incorrecto");
				
				request.setAttribute(HabilitacionSearchPage.NAME, habilitacionSearchPageVO);
				userSession.put(HabilitacionSearchPage.NAME, habilitacionSearchPageVO);
				return mapping.findForward(EspConstants.FWD_HABILITACION_SEARCHPAGE);
			}
						
			habilitacionSearchPageVO.setTitular(PadServiceLocator.getPadPersonaService().
					getPersonaBySexoyNroDoc(userSession,habilitacionSearchPageVO.getTitular()));
			
			if (habilitacionSearchPageVO.getTitular().isPersonaEncontrada())
				habilitacionSearchPageVO.setPoseeDatosPersona(true);
			else
				habilitacionSearchPageVO.setPoseeDatosPersona(false);

			// Envio el VO al request
			request.setAttribute(HabilitacionSearchPage.NAME, habilitacionSearchPageVO);
			
			userSession.put(HabilitacionSearchPage.NAME, habilitacionSearchPageVO);
			
			return mapping.findForward(EspConstants.FWD_HABILITACION_SEARCHPAGE);
			
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, HabilitacionSearchPage.NAME);
		}
	}
	
	public ActionForward limpiarPersona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();		
		
		UserSession userSession = getCurrentUserSession(request, mapping);			
		if (userSession == null) return forwardErrorSession(request);
				
		try {
			// Bajo el adapter del userSession
			HabilitacionSearchPage habilitacionSearchPageVO = (HabilitacionSearchPage)userSession.get(HabilitacionSearchPage.NAME);
	
			// Para que no se muestre el resultSet
			habilitacionSearchPageVO.setPageNumber(0L);

			// Si es nulo no se puede continuar
			if (habilitacionSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + HabilitacionSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, HabilitacionSearchPage.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(habilitacionSearchPageVO, request);
			
			// Para que no se muestre el resultSet
			habilitacionSearchPageVO.setPageNumber(0L);

			habilitacionSearchPageVO.setTitular(new PersonaVO());
			habilitacionSearchPageVO.getTitular().setPersonaBuscada(false);//true);
			habilitacionSearchPageVO.getTitular().setPersonaEncontrada(false);
			habilitacionSearchPageVO.setPoseeDatosPersona(false);
			
			// Envio el VO al request
			request.setAttribute(HabilitacionSearchPage.NAME, habilitacionSearchPageVO);
			
			userSession.put(HabilitacionSearchPage.NAME, habilitacionSearchPageVO);
			
			return mapping.findForward(EspConstants.FWD_HABILITACION_SEARCHPAGE);
			
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, HabilitacionSearchPage.NAME);
		}
	}
	
	public ActionForward buscarPersona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();		
		
		UserSession userSession = getCurrentUserSession(request, mapping);			
		if (userSession == null) return forwardErrorSession(request);
				
		try {
			// Bajo el adapter del userSession
			HabilitacionSearchPage habilitacionSearchPageVO = (HabilitacionSearchPage)userSession.get(HabilitacionSearchPage.NAME);
	
			// Para que no se muestre el resultSet
			habilitacionSearchPageVO.setPageNumber(0L);

			// Si es nulo no se puede continuar
			if (habilitacionSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + HabilitacionSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, HabilitacionSearchPage.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(habilitacionSearchPageVO, request);
			
            // Tiene errores recuperables
			if (habilitacionSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + habilitacionSearchPageVO.infoString()); 
				saveDemodaErrors(request, habilitacionSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, HabilitacionSearchPage.NAME, habilitacionSearchPageVO);
			}
			
			return forwardSeleccionar(mapping, request, 
					EspConstants.METHOD_HABILITACION_PARAM_PERSONA, PadConstants.ACTION_BUSCAR_PERSONA, true);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, HabilitacionSearchPage.NAME);
		}
	}
	
	public ActionForward paramPersona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		NavModel navModel = userSession.getNavModel();
		
		try {
			
			// Bajo el adapter del userSession
			HabilitacionSearchPage habilitacionSearchPageVO = (HabilitacionSearchPage)userSession.get(HabilitacionSearchPage.NAME);
	
			// Para que no se muestre el resultSet
			habilitacionSearchPageVO.setPageNumber(0L);

			// Si es nulo no se puede continuar
			if (habilitacionSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + HabilitacionSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, HabilitacionSearchPage.NAME); 
			}

			// recupero el id seleccionado por el usuario
			String selectedId =  navModel.getSelectedId();				
			
			// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
			if (StringUtil.isNullOrEmpty(selectedId)) {
				// Envio el VO al request
				request.setAttribute(HabilitacionSearchPage.NAME, habilitacionSearchPageVO);
				return mapping.findForward(EspConstants.FWD_HABILITACION_SEARCHPAGE); 
			}

			// llamo al param del servicio
			habilitacionSearchPageVO = EspServiceLocator.getHabilitacionService().paramPersona(userSession, habilitacionSearchPageVO, new Long(selectedId));

            // Tiene errores recuperables
			if (habilitacionSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + habilitacionSearchPageVO.infoString()); 
				saveDemodaErrors(request, habilitacionSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
						HabilitacionSearchPage.NAME, habilitacionSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (habilitacionSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + habilitacionSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
						HabilitacionSearchPage.NAME, habilitacionSearchPageVO);
			}
			
			DemodaUtil.populateVO(habilitacionSearchPageVO, request);
			
			// grabo los mensajes si hubiere
			saveDemodaMessages(request, habilitacionSearchPageVO);
			
			// Envio el VO al request
			request.setAttribute(HabilitacionSearchPage.NAME, habilitacionSearchPageVO);
			
			userSession.put(HabilitacionSearchPage.NAME, habilitacionSearchPageVO);
			
			return mapping.findForward(EspConstants.FWD_HABILITACION_SEARCHPAGE);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, HabilitacionSearchPage.NAME);
		}
	}
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, HabilitacionSearchPage.NAME);
			
	}
	
	// Entradas vendidas
	public ActionForward admEntVen(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();

		return baseForwardSearchPage(mapping, request, funcName, EspConstants.ACTION_ADMINISTRAR_HABILITACION, 
				EspConstants.MTD_ADM_ENTVEN);
	}
	
	// Entradas vendidas internas
	public ActionForward admEntVenInt(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();

		return baseForwardSearchPage(mapping, request, funcName, EspConstants.ACTION_ADMINISTRAR_HABILITACION, 
				EspConstants.MTD_ADM_ENTVENINT);

	}
	
	public ActionForward imprimirHabSinEntVen(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = getCurrentUserSession(request, mapping);
			
		if (userSession == null) return forwardErrorSession(request);
		try {
			// Bajo el adapter del userSession
			HabilitacionSearchPage habilitacionSearchPage = (HabilitacionSearchPage) userSession.get(HabilitacionSearchPage.NAME);

			// Si es nulo no se puede continuar
			if (habilitacionSearchPage == null) {
				log.error("error en: "  + funcName + ": " + HabilitacionSearchPage.NAME + 
				" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, HabilitacionSearchPage.NAME); 
			}
			
			// llamada al servicio
			PrintModel print  = EspServiceLocator.getHabilitacionService().imprimirHabSinEntVen(userSession, habilitacionSearchPage);
			
			baseResponsePrintModel(response, print);
				
			return null;
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, HabilitacionSearchPage.NAME);
		}
	}
	
	// Cambiar Estado de Habilitacion
	public ActionForward cambiarEstado(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();

		return baseForwardSearchPage(mapping, request, funcName, EspConstants.ACTION_ADMINISTRAR_HABILITACION, 
				EspConstants.MTD_CAMBIAR_ESTADO);
	}

}
