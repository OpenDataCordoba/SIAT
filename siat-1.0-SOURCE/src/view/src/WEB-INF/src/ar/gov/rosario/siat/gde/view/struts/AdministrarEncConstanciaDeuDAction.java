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

import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.gde.iface.model.ConstanciaDeuAdapter;
import ar.gov.rosario.siat.gde.iface.model.ConstanciaDeuVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import ar.gov.rosario.siat.pad.iface.model.CuentaSearchPage;
import ar.gov.rosario.siat.pad.iface.model.DomicilioAdapter;
import ar.gov.rosario.siat.pad.iface.model.DomicilioVO;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import ar.gov.rosario.siat.pad.view.struts.BuscarCalleDAction;
import ar.gov.rosario.siat.pad.view.struts.BuscarCuentaDAction;
import ar.gov.rosario.siat.pad.view.struts.BuscarDomicilioDAction;
import ar.gov.rosario.siat.pad.view.struts.BuscarLocalidadDAction;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarEncConstanciaDeuDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEncConstanciaDeuDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ADM_CONSTANCIA_DEUDA_JUDICIAL, act);		

		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ConstanciaDeuAdapter constanciaDeuAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());

			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getConstanciaDeuAdapterForUpdate(userSession, commonKey)";
				constanciaDeuAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getConstanciaDeuAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_CONSTANCIADUE_ENC_EDIT_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getConstanciaDeuAdapterForCreate(userSession)";
				constanciaDeuAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getConstanciaDeuAdapterForCreate(userSession);
				actionForward = mapping.findForward(GdeConstants.FWD_CONSTANCIADUE_ENC_EDIT_ADAPTER);
			}
			if (navModel.getAct().equals(GdeConstants.ACTION_CONSTANCIADUE_MODIFICAR_DOMICILIO_ENV)) {
				stringServicio = "getConstanciaDeuAdapterForCreate(userSession)";
				constanciaDeuAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getConstanciaDeuAdapterForUpdateDomicilioEnvio(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_CONSTANCIADUE_MODIF_DOM_ENV_ADAPTER);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (constanciaDeuAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + constanciaDeuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ConstanciaDeuAdapter.ENC_NAME, constanciaDeuAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			constanciaDeuAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ConstanciaDeuAdapter.ENC_NAME + ": "+ constanciaDeuAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ConstanciaDeuAdapter.ENC_NAME, constanciaDeuAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ConstanciaDeuAdapter.ENC_NAME, constanciaDeuAdapterVO);

			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConstanciaDeuAdapter.ENC_NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, 
			GdeSecurityConstants.ADM_CONSTANCIA_DEUDA_JUDICIAL, BaseSecurityConstants.AGREGAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ConstanciaDeuAdapter constanciaDeuAdapterVO = (ConstanciaDeuAdapter) userSession.get(ConstanciaDeuAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (constanciaDeuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ConstanciaDeuAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ConstanciaDeuAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(constanciaDeuAdapterVO, request);
			
            // Tiene errores recuperables
			if (constanciaDeuAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + constanciaDeuAdapterVO.infoString()); 
				saveDemodaErrors(request, constanciaDeuAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ConstanciaDeuAdapter.ENC_NAME, constanciaDeuAdapterVO);
			}
			
			// llamada al servicio
			ConstanciaDeuVO constanciaDeuVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().createConstanciaDeu(userSession, constanciaDeuAdapterVO);
			
            // Tiene errores recuperables
			if (constanciaDeuVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + constanciaDeuVO.infoString()); 
				saveDemodaErrors(request, constanciaDeuVO);
				return forwardErrorRecoverable(mapping, request, userSession, ConstanciaDeuAdapter.ENC_NAME, constanciaDeuAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (constanciaDeuVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + constanciaDeuVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ConstanciaDeuAdapter.ENC_NAME, constanciaDeuAdapterVO);
			}

			// Si tiene permiso lo dirijo al adapter de modificacion, 
			// sino vuelve al searchPage
			if (hasAccess(userSession, GdeSecurityConstants.ADM_CONSTANCIA_DEUDA_JUDICIAL, 
				BaseSecurityConstants.MODIFICAR)) {
				
				// seteo el id para que lo use el siguiente action 
				userSession.getNavModel().setSelectedId(constanciaDeuVO.getId().toString());

				// Seteo de parametros para que vuelva al buscar desde el constanciaDeuAdapter
				userSession.getNavModel().setPrevAction("/gde/BuscarConstanciaDeu");
				userSession.getNavModel().setPrevActionParameter("buscar");

				// lo dirijo al adapter de modificacion
				return forwardConfirmarOk(mapping, request, funcName, ConstanciaDeuAdapter.ENC_NAME, 
					GdeConstants.PATH_ADMINISTRAR_CONSTANCIADUE, BaseConstants.METHOD_INICIALIZAR, 
					BaseConstants.ACT_MODIFICAR);
			} else {
				
				// lo dirijo al searchPage				
				return forwardConfirmarOk(mapping, request, funcName, ConstanciaDeuAdapter.ENC_NAME);
				
			}
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConstanciaDeuAdapter.ENC_NAME);
		}
	}

	public ActionForward paramRecurso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping, 
				GdeSecurityConstants.ADM_CONSTANCIA_DEUDA_JUDICIAL, BaseSecurityConstants.AGREGAR);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				ConstanciaDeuAdapter constanciaDeuAdapterVO = (ConstanciaDeuAdapter) userSession.get(ConstanciaDeuAdapter.ENC_NAME);
				
				// Si es nulo no se puede continuar
				if (constanciaDeuAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + ConstanciaDeuAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ConstanciaDeuAdapter.ENC_NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(constanciaDeuAdapterVO, request);
				
	            // Tiene errores recuperables
				if (constanciaDeuAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + constanciaDeuAdapterVO.infoString()); 
					saveDemodaErrors(request, constanciaDeuAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, ConstanciaDeuAdapter.ENC_NAME, constanciaDeuAdapterVO);
				}
				
				// llamada al servicio
				constanciaDeuAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getConstanciaDeuAdapterParamProcuradorByRecurso(userSession, constanciaDeuAdapterVO);
				
	            // Tiene errores recuperables
				if (constanciaDeuAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + constanciaDeuAdapterVO.infoString()); 
					saveDemodaErrors(request, constanciaDeuAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, ConstanciaDeuAdapter.ENC_NAME, constanciaDeuAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (constanciaDeuAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + constanciaDeuAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, ConstanciaDeuAdapter.ENC_NAME, constanciaDeuAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(ConstanciaDeuAdapter.ENC_NAME, constanciaDeuAdapterVO);
				// Subo el apdater al userSession
				userSession.put(ConstanciaDeuAdapter.ENC_NAME, constanciaDeuAdapterVO);

				return mapping.findForward(GdeConstants.FWD_CONSTANCIADUE_ENC_EDIT_ADAPTER);
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ConstanciaDeuAdapter.ENC_NAME);
			}
	}
	
	public ActionForward buscarCuenta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());

		// Bajo el searchPage del userSession
		ConstanciaDeuAdapter constanciaDeuAdapter = (ConstanciaDeuAdapter) userSession.get(ConstanciaDeuAdapter.ENC_NAME);
		
		// Si es nulo no se puede continuar
		if (constanciaDeuAdapter == null) {
			log.error("error en: "  + funcName + ": " + ConstanciaDeuAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, ConstanciaDeuAdapter.ENC_NAME); 
		}

		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(constanciaDeuAdapter, request);
		
        // Tiene errores recuperables
		if (constanciaDeuAdapter.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + constanciaDeuAdapter.infoString()); 
			saveDemodaErrors(request, constanciaDeuAdapter);

			request.setAttribute(ConstanciaDeuAdapter.ENC_NAME, constanciaDeuAdapter);
			return mapping.getInputForward();
		}
		

		CuentaSearchPage cuentaFiltro = new CuentaSearchPage();
		
		// Seteo el recurso 
		cuentaFiltro.getCuentaTitular().getCuenta()
			.setRecurso(constanciaDeuAdapter.getRecurso()); 

		// y el numero de cuenta
		cuentaFiltro.getCuentaTitular().getCuenta().
			setNumeroCuenta(constanciaDeuAdapter.getConstanciaDeu().getCuenta().getNumeroCuenta());

		navModel.putParameter(BuscarCuentaDAction.CUENTA_SEARCHPAGE_FILTRO, cuentaFiltro);
		
		
		// Forwardeo a la Search Page de Cuenta
		return forwardSeleccionar(mapping, request, 
				"paramCuenta", PadConstants.ACTION_BUSCAR_CUENTA, false);
	}
	
	public ActionForward paramCuenta (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		NavModel navModel = userSession.getNavModel();
		
		try {
			
			//bajo el adapter del usserSession
			ConstanciaDeuAdapter constanciaDeuAdapter =  (ConstanciaDeuAdapter) userSession.get(ConstanciaDeuAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (constanciaDeuAdapter == null) {
				log.error("error en: "  + funcName + ": " + ConstanciaDeuAdapter.ENC_NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ConstanciaDeuAdapter.ENC_NAME); 
			}
			

			// recupero el id seleccionado por el usuario
			String selectedId = navModel.getSelectedId();
			
		//	constanciaDeuSearchPageVO = PadServiceLocator.getCuentaService().getCueExcSelSearchPageInit(userSession);
			
			// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
			if (StringUtil.isNullOrEmpty(selectedId)) {
				// Envio el VO al request
				request.setAttribute(ConstanciaDeuAdapter.ENC_NAME, constanciaDeuAdapter);
				return mapping.findForward(GdeConstants.FWD_CONSTANCIADUE_ENC_EDIT_ADAPTER); 
			}
			
			// Seteo el id de la cuenta
			constanciaDeuAdapter.getConstanciaDeu().getCuenta().setId(new Long(selectedId));
			
			// Llamada al servicio
			constanciaDeuAdapter = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getConstanciaDeuAdapterParamCuenta(userSession, constanciaDeuAdapter);
			
            // Tiene errores recuperables
			if (constanciaDeuAdapter.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + constanciaDeuAdapter.infoString()); 
				saveDemodaErrors(request, constanciaDeuAdapter);
				return forwardErrorRecoverable(mapping, request, userSession, 
						ConstanciaDeuAdapter.ENC_NAME, constanciaDeuAdapter);
			}
			
			// Tiene errores no recuperables
			if (constanciaDeuAdapter.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + constanciaDeuAdapter.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
						ConstanciaDeuAdapter.ENC_NAME, constanciaDeuAdapter);
			}
			
			// Envio el VO al request
			request.setAttribute(ConstanciaDeuAdapter.ENC_NAME, constanciaDeuAdapter);
			
			return mapping.findForward(GdeConstants.FWD_CONSTANCIADUE_ENC_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConstanciaDeuAdapter.ENC_NAME);
		}
	}
	

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, 
			GdeSecurityConstants.ADM_CONSTANCIA_DEUDA_JUDICIAL, BaseSecurityConstants.MODIFICAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ConstanciaDeuAdapter constanciaDeuAdapterVO = (ConstanciaDeuAdapter) userSession.get(ConstanciaDeuAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (constanciaDeuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ConstanciaDeuAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ConstanciaDeuAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(constanciaDeuAdapterVO, request);
			
            // Tiene errores recuperables
			if (constanciaDeuAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + constanciaDeuAdapterVO.infoString()); 
				saveDemodaErrors(request, constanciaDeuAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ConstanciaDeuAdapter.ENC_NAME, constanciaDeuAdapterVO);
			}
			
			// llamada al servicio
			ConstanciaDeuVO constanciaDeuVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().updateConstanciaDeu(userSession, constanciaDeuAdapterVO.getConstanciaDeu());
			
            // Tiene errores recuperables
			if (constanciaDeuVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + constanciaDeuAdapterVO.infoString()); 
				saveDemodaErrors(request, constanciaDeuVO);
				return forwardErrorRecoverable(mapping, request, userSession, ConstanciaDeuAdapter.ENC_NAME, constanciaDeuAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (constanciaDeuVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + constanciaDeuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ConstanciaDeuAdapter.ENC_NAME, constanciaDeuAdapterVO);
			}
						
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ConstanciaDeuAdapter.ENC_NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConstanciaDeuAdapter.ENC_NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ConstanciaDeuAdapter.ENC_NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, ConstanciaDeuAdapter.ENC_NAME);
		
	}
	
	// metodos relacionados con Domicilio
	
	public ActionForward buscarLocalidad(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());

		// Bajo el adapter del userSession
		ConstanciaDeuAdapter constanciaDeuAdapterVO = (ConstanciaDeuAdapter) userSession.get(ConstanciaDeuAdapter.ENC_NAME);
		
		// Si es nulo no se puede continuar
		if (constanciaDeuAdapterVO == null) {
			log.error("error en: "  + funcName + ": " + ConstanciaDeuAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, ConstanciaDeuAdapter.ENC_NAME); 
		}

		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(constanciaDeuAdapterVO, request);
		
		DomicilioVO domicilioVO = constanciaDeuAdapterVO.getConstanciaDeu().getDomicilio();
		
		DemodaUtil.populateVO(domicilioVO, request);
		constanciaDeuAdapterVO.getConstanciaDeu().setDomicilio(domicilioVO);
		
		// seteo de la localidad como filtro a utilizar en la seleccion de localidades
		navModel.putParameter(BuscarLocalidadDAction.PROVINCIA, 
				domicilioVO.getLocalidad().getProvincia().getDuplicate());
		
		return forwardSeleccionar(mapping, request, 
				PadConstants.METOD_PARAM_LOCALIDAD, PadConstants.ACTION_BUSCAR_LOCALIDAD, false);
	}
		
	public ActionForward paramLocalidad(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			NavModel navModel = userSession.getNavModel();
			
			try {
				
				//bajo el adapter del usserSession
				ConstanciaDeuAdapter constanciaDeuAdapterVO =  (ConstanciaDeuAdapter) userSession.get(ConstanciaDeuAdapter.ENC_NAME);
				
				// Si es nulo no se puede continuar
				if (constanciaDeuAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + ConstanciaDeuAdapter.ENC_NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ConstanciaDeuAdapter.ENC_NAME); 
				}
				
				String findForward = GdeConstants.FWD_CONSTANCIADUE_MODIF_DOM_ENV_ADAPTER;
				
				// recupero el id seleccionado por el usuario
				String selectedId = navModel.getSelectedId();
				
				// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
				if (StringUtil.isNullOrEmpty(selectedId)) {
					// Envio el VO al request
					request.setAttribute(ConstanciaDeuAdapter.ENC_NAME, constanciaDeuAdapterVO);
					
					return mapping.findForward(findForward);
				}

				// Seteo el id atributo seleccionado
				constanciaDeuAdapterVO.getConstanciaDeu().getDomicilio().getLocalidad().setId(new Long(selectedId));
				
				// llamo al param del servicio
				DomicilioAdapter domicilioAdapterVO = new DomicilioAdapter(constanciaDeuAdapterVO.getConstanciaDeu().getDomicilio());
				
				domicilioAdapterVO = PadServiceLocator.getPadUbicacionService().getDomicilioAdapterParamLocalidad
					(userSession, domicilioAdapterVO);
				// copia del domicilio.
				constanciaDeuAdapterVO.getConstanciaDeu().setDomicilio(domicilioAdapterVO.getDomicilio());
				// pasaje de errores y mensajes
				domicilioAdapterVO.passErrorMessages(constanciaDeuAdapterVO);
				
	            // Tiene errores recuperables
				if (constanciaDeuAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + constanciaDeuAdapterVO.infoString()); 
					saveDemodaErrors(request, constanciaDeuAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
							ConstanciaDeuAdapter.ENC_NAME, constanciaDeuAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (constanciaDeuAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + constanciaDeuAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
							ConstanciaDeuAdapter.ENC_NAME, constanciaDeuAdapterVO);
				}
				
				// grabo los mensajes si hubiere
				saveDemodaMessages(request, constanciaDeuAdapterVO);
				
				// Envio el VO al request
				request.setAttribute(ConstanciaDeuAdapter.ENC_NAME, constanciaDeuAdapterVO);
				// Subo el apdater al userSession
				userSession.put(ConstanciaDeuAdapter.ENC_NAME, constanciaDeuAdapterVO);

				return mapping.findForward(findForward);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ConstanciaDeuAdapter.ENC_NAME);
			}
		}
	
	public ActionForward buscarCalle(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			UserSession userSession = getCurrentUserSession(request, mapping);
			if (userSession==null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());

			// Bajo el adapter del userSession
			ConstanciaDeuAdapter constanciaDeuAdapterVO = (ConstanciaDeuAdapter) userSession.get(ConstanciaDeuAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (constanciaDeuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ConstanciaDeuAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ConstanciaDeuAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(constanciaDeuAdapterVO, request);

			DomicilioVO domicilioVO = constanciaDeuAdapterVO.getConstanciaDeu().getDomicilio();
			
			DemodaUtil.populateVO(domicilioVO, request);
			constanciaDeuAdapterVO.getConstanciaDeu().setDomicilio(domicilioVO);

			// seteo de la calee como filtro a utilizar en la seleccion de calles
			navModel.putParameter(BuscarCalleDAction.CALLE, 
					constanciaDeuAdapterVO.getConstanciaDeu().getDomicilio().getCalle().getDuplicate());
			
			return forwardSeleccionar(mapping, request, "paramCalle", PadConstants.ACTION_BUSCAR_CALLE, false); 
	}
	
	public ActionForward paramCalle(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		NavModel navModel = userSession.getNavModel();
		
		try {
			
			//bajo el adapter del usserSession
			ConstanciaDeuAdapter constanciaDeuAdapterVO =  (ConstanciaDeuAdapter) userSession.get(ConstanciaDeuAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (constanciaDeuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ConstanciaDeuAdapter.ENC_NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ConstanciaDeuAdapter.ENC_NAME); 
			}

			// recupero el id seleccionado por el usuario
			String selectedId = navModel.getSelectedId();
			
			
			String findForward = GdeConstants.FWD_CONSTANCIADUE_MODIF_DOM_ENV_ADAPTER;
			
			// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
			if (StringUtil.isNullOrEmpty(selectedId)) {
				// Envio el VO al request
				request.setAttribute(ConstanciaDeuAdapter.ENC_NAME, constanciaDeuAdapterVO);
				return mapping.findForward(findForward); 
			}

			// Seteo el id localidad seleccionado
			constanciaDeuAdapterVO.getConstanciaDeu().getDomicilio().getCalle().setId(new Long(selectedId));
			// obtencion del DomicilioAdapter para invocar el servicio
			DomicilioAdapter domicilioAdapterVO = new DomicilioAdapter();
			domicilioAdapterVO.setDomicilio(constanciaDeuAdapterVO.getConstanciaDeu().getDomicilio());
			domicilioAdapterVO = PadServiceLocator.getPadUbicacionService().getDomicilioAdapterParamCalle(userSession, domicilioAdapterVO);
			// paso de errores y mensajes
			constanciaDeuAdapterVO.setListError(domicilioAdapterVO.getListError());
			constanciaDeuAdapterVO.setListMessage(domicilioAdapterVO.getListMessage());
			
			// Tiene errores recuperables
			if (constanciaDeuAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + constanciaDeuAdapterVO.infoString()); 
				saveDemodaErrors(request, constanciaDeuAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
						ConstanciaDeuAdapter.ENC_NAME, constanciaDeuAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (constanciaDeuAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + constanciaDeuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
						ConstanciaDeuAdapter.ENC_NAME, constanciaDeuAdapterVO);
			}
			
			// grabo los mensajes si hubiere
			saveDemodaMessages(request, constanciaDeuAdapterVO);
			
			// Envio el VO al request
			request.setAttribute(ConstanciaDeuAdapter.ENC_NAME, constanciaDeuAdapterVO);

			return mapping.findForward(findForward);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConstanciaDeuAdapter.ENC_NAME);
		}
	}

	public ActionForward validarDomicilio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping);
				
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ConstanciaDeuAdapter constanciaDeuAdapterVO = (ConstanciaDeuAdapter) userSession.get(ConstanciaDeuAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (constanciaDeuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ConstanciaDeuAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ConstanciaDeuAdapter.ENC_NAME); 
			}
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(constanciaDeuAdapterVO, request);
			
			// Tiene errores recuperables
			if (constanciaDeuAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + constanciaDeuAdapterVO.infoString()); 
				saveDemodaErrors(request, constanciaDeuAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ConstanciaDeuAdapter.ENC_NAME, constanciaDeuAdapterVO);
			}

			DomicilioVO domicilioVO = constanciaDeuAdapterVO.getConstanciaDeu().getDomicilio();
			
			DemodaUtil.populateVO(domicilioVO, request);
			
			String findForward = GdeConstants.FWD_CONSTANCIADUE_MODIF_DOM_ENV_ADAPTER;;
			
			// Tiene errores recuperables
			if (domicilioVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + domicilioVO.infoString()); 
				saveDemodaErrors(request, constanciaDeuAdapterVO);
				request.setAttribute(ConstanciaDeuAdapter.ENC_NAME, constanciaDeuAdapterVO);
				//return forwardErrorRecoverable(mapping, request, userSession, ConstanciaDeuAdapter.ENC_NAME, constanciaDeuAdapterVO);
				return mapping.findForward(findForward);
			}
			
			// llamada al servicio
			domicilioVO = PadServiceLocator.getPadUbicacionService().validarDomicilio(userSession, 
					domicilioVO.getDuplicate());

			// paso de errores y mensajes
			domicilioVO.passErrorMessages(constanciaDeuAdapterVO);
			
            // Tiene errores recuperables
			if (domicilioVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + domicilioVO.infoString()); 
				saveDemodaErrors(request, domicilioVO);
				
				if (domicilioVO.getValidoPorRequeridos()){
					// es valido por requerido y no es valido por jar, va a la busqueda de domicilios
					// seteo del domicilio como filtro a utilizar en la seleccion de domicilios validos
					userSession.getNavModel().putParameter(
							BuscarDomicilioDAction.DOMICILIO, domicilioVO);
					return forwardSeleccionar(
							mapping, request, "paramValidarDomicilio", PadConstants.FWD_DOMICILIO_SEARCHPAGE, false);
				}else{
					// es invalido por requeridos, queda en la misma pagina
					saveDemodaMessages(request, constanciaDeuAdapterVO);
					request.setAttribute(ConstanciaDeuAdapter.ENC_NAME, constanciaDeuAdapterVO);
					return mapping.findForward(findForward);
				}
			}
			// Tiene errores no recuperables
			if (domicilioVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + domicilioVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ConstanciaDeuAdapter.ENC_NAME, constanciaDeuAdapterVO);
			}
			
			// Fue Exitoso
			// graba el mensaje: Domicilio Valido
			constanciaDeuAdapterVO.addMessage(PadError.DOMICILIO_VALIDO);
			saveDemodaMessages(request, constanciaDeuAdapterVO);
			request.setAttribute(ConstanciaDeuAdapter.ENC_NAME, constanciaDeuAdapterVO);
			
			return mapping.findForward(findForward);
			
		} catch (Exception exception) {
			exception.printStackTrace();
			return baseException(mapping, request, funcName, exception, ConstanciaDeuAdapter.ENC_NAME);
		}
	}
		
	public ActionForward paramValidarDomicilio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			//bajo el adapter del usserSession
			ConstanciaDeuAdapter constanciaDeuAdapterVO =  (ConstanciaDeuAdapter) userSession.get(ConstanciaDeuAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (constanciaDeuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ConstanciaDeuAdapter.ENC_NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ConstanciaDeuAdapter.ENC_NAME); 
			}
			
			// limpio la lista de errores y mensajes
			constanciaDeuAdapterVO.clearErrorMessages();

			// recupero el domicilio seleccionado
			DomicilioVO domicilioVO = (DomicilioVO) request.getAttribute(DomicilioVO.NAME);
			if (!ModelUtil.isNullOrEmpty(domicilioVO)){
				// seteo del domicilio seleccionado: NO UTILIZAR: cuentaAdapterVO.getPersona().setDomicilio(domicilioVO);
				//cuentaAdapterVO.getPersona().getDomicilio().setId(domicilioVO.getId());
				constanciaDeuAdapterVO.getConstanciaDeu().getDomicilio().setNumero(domicilioVO.getNumero());
				constanciaDeuAdapterVO.getConstanciaDeu().getDomicilio().setCalle(domicilioVO.getCalle());
				constanciaDeuAdapterVO.getConstanciaDeu().getDomicilio().setLetraCalle(domicilioVO.getLetraCalle());
				constanciaDeuAdapterVO.getConstanciaDeu().getDomicilio().setBis(domicilioVO.getBis());
			}
			
			// Envio el VO al request
			request.setAttribute(ConstanciaDeuAdapter.ENC_NAME, constanciaDeuAdapterVO);
			
			String findForward = GdeConstants.FWD_CONSTANCIADUE_MODIF_DOM_ENV_ADAPTER;;;

			return mapping.findForward(findForward);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConstanciaDeuAdapter.ENC_NAME);
		}
	}
	
	public ActionForward modificarDomicilio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				GdeSecurityConstants.ADM_CONSTANCIA_DEUDA_JUDICIAL, BaseSecurityConstants.MODIFICAR);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				ConstanciaDeuAdapter constanciaDeuAdapterVO = (ConstanciaDeuAdapter) userSession.get(ConstanciaDeuAdapter.ENC_NAME);
				
				// Si es nulo no se puede continuar
				if (constanciaDeuAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + ConstanciaDeuAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ConstanciaDeuAdapter.ENC_NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(constanciaDeuAdapterVO, request);
				
	            // Tiene errores recuperables
				if (constanciaDeuAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + constanciaDeuAdapterVO.infoString()); 
					saveDemodaErrors(request, constanciaDeuAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, ConstanciaDeuAdapter.ENC_NAME, constanciaDeuAdapterVO);
				}
				
				// llamada al servicio
				constanciaDeuAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().updateConstanciaDeuDomicilio(userSession, constanciaDeuAdapterVO);
				
	            // Tiene errores recuperables
				if (constanciaDeuAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + constanciaDeuAdapterVO.infoString()); 
					saveDemodaErrors(request, constanciaDeuAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, ConstanciaDeuAdapter.ENC_NAME, constanciaDeuAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (constanciaDeuAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + constanciaDeuAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, ConstanciaDeuAdapter.ENC_NAME, constanciaDeuAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, ConstanciaDeuAdapter.ENC_NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ConstanciaDeuAdapter.ENC_NAME);
			}
		}

}
	
