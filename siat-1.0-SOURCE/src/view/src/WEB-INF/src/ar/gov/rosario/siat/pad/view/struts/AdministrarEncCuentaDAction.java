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

import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.def.iface.model.DomAtrAdapter;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.TipObjImpVO;
import ar.gov.rosario.siat.def.view.util.DefinitionUtil;
import ar.gov.rosario.siat.gde.iface.model.DeudaContribSearchPage;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import ar.gov.rosario.siat.pad.iface.model.CuentaAdapter;
import ar.gov.rosario.siat.pad.iface.model.CuentaSearchPage;
import ar.gov.rosario.siat.pad.iface.model.CuentaTitularVO;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import ar.gov.rosario.siat.pad.iface.model.DomicilioAdapter;
import ar.gov.rosario.siat.pad.iface.model.DomicilioVO;
import ar.gov.rosario.siat.pad.iface.model.ObjImpSearchPage;
import ar.gov.rosario.siat.pad.iface.model.PersonaVO;
import ar.gov.rosario.siat.pad.iface.model.RecAtrCueDefinition;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarEncCuentaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEncCuentaDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = null;

		if (PadConstants.ACT_MODIFICAR_DOMICILIO_ENV.equals(act)){
			userSession = canAccess(request, mapping, PadSecurityConstants.ABM_CUENTA_DOMICILIO_ENV, BaseSecurityConstants.MODIFICAR);
		} else if (PadConstants.ACT_AGREGAR_DOMICILIO_ENV.equals(act)){
			userSession = canAccess(request, mapping, PadSecurityConstants.ABM_CUENTA_DOMICILIO_ENV, BaseSecurityConstants.AGREGAR);
		} else{
			userSession = canAccess(request, mapping, PadSecurityConstants.ABM_CUENTA_ENC, act);
		}
		
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		CuentaAdapter cuentaAdapterVO = null;
		DeudaContribSearchPage deudaContribSearchPageVO = null;
		PersonaVO personaVO = null;
		RecursoVO recursoVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getCuentaAdapterForUpdate(userSession, commonKey)";
				cuentaAdapterVO = PadServiceLocator.getCuentaService().getCuentaAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(PadConstants.FWD_CUENTA_ENC_EDIT_ADAPTER);
			}
			
			if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getCuentaAdapterForCreate(userSession, commonKey)";
				
				deudaContribSearchPageVO = (DeudaContribSearchPage) userSession.get(DeudaContribSearchPage.NAME);
				boolean esCMD = false;
				
				if (deudaContribSearchPageVO != null){
					personaVO = deudaContribSearchPageVO.getContribuyente().getPersona();
					esCMD = true;
				}
				
				// Pasamos el recurso seleccionado en la busqueda.
				CuentaSearchPage cuentaSearchPage = (CuentaSearchPage) userSession.get(CuentaSearchPage.NAME);
				
				if (cuentaSearchPage != null){
					recursoVO = cuentaSearchPage.getCuentaTitular().getCuenta().getRecurso();
					esCMD = cuentaSearchPage.isEsCMD();
				}				
				
				cuentaAdapterVO = PadServiceLocator.getCuentaService().getCuentaAdapterForCreate(
						userSession, 
						recursoVO,
						personaVO,
						esCMD);
				
				actionForward = mapping.findForward(PadConstants.FWD_CUENTA_ENC_EDIT_AGREGAR_ADAPTER);
			}
			
			if (navModel.getAct().equals(PadConstants.ACT_MODIFICAR_DOMICILIO_ENV)) {
				stringServicio = "getCuentaAdapterForUpdateDomicilioEnvio(userSession, commonKey)";
				cuentaAdapterVO = PadServiceLocator.getCuentaService().getCuentaAdapterForUpdateDomicilioEnvio(userSession, commonKey);
				actionForward = mapping.findForward(PadConstants.FWD_CUENTA_MODIF_DOM_ENV_ADAPTER);
			}
			if (navModel.getAct().equals(PadConstants.ACT_AGREGAR_DOMICILIO_ENV)) {
				stringServicio = "getCuentaAdapterForUpdateDomicilioEnvio(userSession, commonKey)";
				cuentaAdapterVO = PadServiceLocator.getCuentaService().getCuentaAdapterForUpdateDomicilioEnvio(userSession, commonKey);
				actionForward = mapping.findForward(PadConstants.FWD_CUENTA_MODIF_DOM_ENV_ADAPTER);
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (cuentaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio 
					+ ": " + cuentaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
					CuentaAdapter.ENC_NAME, cuentaAdapterVO);
			}
			
			// Pasamos los datos de navegacion.
			if (deudaContribSearchPageVO != null){
				log.debug("deudaContribSearchPageVO: " + deudaContribSearchPageVO.infoString());
				cuentaAdapterVO.setPrevAction(GdeConstants.PATH_BUSCAR_DEUDACONTRIB);
				cuentaAdapterVO.setPrevActionParameter(GdeConstants.ACT_PARAM_CONTRIBUYENTE + "&idContrib=" +  personaVO.getIdView());
				cuentaAdapterVO.setAct(BaseConstants.ACT_AGREGAR);
				
			} else {
				// Seteo los valores de navegacion en el adapter
				cuentaAdapterVO.setValuesFromNavModel(navModel);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " 
				+ CuentaAdapter.ENC_NAME + ": "+ cuentaAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(CuentaAdapter.ENC_NAME, cuentaAdapterVO);
			// Subo el apdater al userSession
			userSession.put(CuentaAdapter.ENC_NAME, cuentaAdapterVO);

			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentaAdapter.ENC_NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping, 
					PadSecurityConstants.ABM_CUENTA_ENC, BaseSecurityConstants.AGREGAR);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				CuentaAdapter cuentaAdapterVO = (CuentaAdapter) userSession.get(CuentaAdapter.ENC_NAME);
				
				// Si es nulo no se puede continuar
				if (cuentaAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + DomAtrAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, CuentaAdapter.ENC_NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(cuentaAdapterVO, request);
				
				DomicilioVO domicilioVO = cuentaAdapterVO.getCuenta().getDomicilioEnvio();
				DemodaUtil.populateVO(domicilioVO, request);
				
				domicilioVO.addErrorMessages(cuentaAdapterVO);
				cuentaAdapterVO.getCuenta().setDomicilioEnvio(domicilioVO);
				
				// populado de atributos
				for (RecAtrCueDefinition recAtrCueDefinition:cuentaAdapterVO.getCuenta().getListRecAtrCueDefinition()){
					DefinitionUtil.populateAtrVal4Edit(recAtrCueDefinition, request);
					
					if (!recAtrCueDefinition.validate("manual")){
						cuentaAdapterVO.addRecoverableValueError("El formato del campo " + 
								recAtrCueDefinition.getAtributo().getDesAtributo() + " es incorrecto." );
					}
				}

				// pasamos el titular principal
				CuentaTitularVO cuentaTitularVO = new CuentaTitularVO();
				cuentaTitularVO.getContribuyente().setPersona(cuentaAdapterVO.getTitular());

				// Si han sido levantados los titulares secundarios
				if(cuentaAdapterVO.getCuenta().getListCuentaTitular().size() > 1){
					// Insertamos el titular principal en la primer posicion
					cuentaAdapterVO.getCuenta().getListCuentaTitular().add(0, cuentaTitularVO);
				} else {
					cuentaAdapterVO.getCuenta().getListCuentaTitular().add(0, cuentaTitularVO);
				}
				
				// Tiene errores recuperables
				if (cuentaAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + cuentaAdapterVO.infoString()); 
					saveDemodaErrors(request, cuentaAdapterVO);

					// no utilizo el forwardErrorRecoverable porque el inputForward del struts-config corresponde a la modificacion
					request.setAttribute(CuentaAdapter.ENC_NAME, cuentaAdapterVO);
					return mapping.findForward(PadConstants.FWD_CUENTA_ENC_EDIT_AGREGAR_ADAPTER);
				}
				
				// llamada al servicio
				CuentaVO cuentaVO = PadServiceLocator.getCuentaService().createCuenta(userSession, cuentaAdapterVO.getCuenta());
				
				log.debug(" ################### cuentaAdapterVO.isEsCMD(): " + cuentaAdapterVO.isEsCMD());
				
	            // Tiene errores recuperables
				if (cuentaVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + cuentaVO.infoString()); 
					saveDemodaErrors(request, cuentaVO);

					// Si tubo error, sacamo el titular principal de la lista, que agregamos en el servicio
					cuentaAdapterVO.getCuenta().getListCuentaTitular().remove(0);
					
					// no utilizo el forwardErrorRecoverable porque el inputForward del struts-config corresponde a la modificacion
					request.setAttribute(CuentaAdapter.ENC_NAME, cuentaAdapterVO);
					return mapping.findForward(PadConstants.FWD_CUENTA_ENC_EDIT_AGREGAR_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (cuentaVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + cuentaVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, CuentaAdapter.ENC_NAME, cuentaAdapterVO);
				}
				
				// Si viene de Deuda por Contribuyente, volvemos ahi
				DeudaContribSearchPage deudaContribSearchPageVO = (DeudaContribSearchPage) userSession.get(DeudaContribSearchPage.NAME);
				if (deudaContribSearchPageVO != null){
					
					Long idContribuyenteBuscado = deudaContribSearchPageVO.getContribuyente().getPersona().getId();
					Long idTitularPrincipal = cuentaVO.getListCuentaTitular().get(0).getContribuyente().getPersona().getId(); 
					
					// Si fue cambiado el titular principal, dirigimos a la deuda por contribuyente del principal de la cuenta.
					if(idContribuyenteBuscado.longValue() != idTitularPrincipal.longValue()){
						cuentaAdapterVO.setPrevActionParameter(GdeConstants.ACT_PARAM_CONTRIBUYENTE + "&idContrib=" +  idTitularPrincipal.toString());
					}
					
					return baseVolver(mapping, form, request, response, CuentaAdapter.ENC_NAME);
				}
				
				// Si tiene permiso lo dirijo al adapter de modificacion, 
				// sino vuelve al searchPage de busqueda de cuenta
				if (hasAccess(userSession, PadSecurityConstants.ABM_CUENTA, BaseSecurityConstants.MODIFICAR)) {
					
					// para que pueda volver a la busqueda de cuentas
					userSession.getNavModel().setPrevAction(PadConstants.PATH_BUSCAR_CUENTA);
					userSession.getNavModel().setPrevActionParameter(BaseConstants.ACT_BUSCAR);
					// seteo el id para que lo use el siguiente action 
					userSession.getNavModel().setSelectedId(cuentaVO.getId().toString());

					// lo dirijo al adapter de modificacion (en realidad de administracion)
					return forwardConfirmarOk(mapping, request, funcName, CuentaAdapter.ENC_NAME, 
						PadConstants.PATH_ADMINISTRAR_CUENTA, BaseConstants.METHOD_INICIALIZAR, 
						BaseConstants.ACT_MODIFICAR);
				} else {
					
					// lo dirijo al searchPage de cuenta				
					return forwardConfirmarOk(mapping, request, funcName, CuentaAdapter.ENC_NAME);
				}
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, CuentaAdapter.ENC_NAME);
			}
		}


	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, 
			PadSecurityConstants.ABM_CUENTA_ENC, BaseSecurityConstants.MODIFICAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CuentaAdapter cuentaAdapterVO = (CuentaAdapter) 
				userSession.get(CuentaAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (cuentaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + 
					CuentaAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CuentaAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(cuentaAdapterVO, request);
			
            // Tiene errores recuperables
			if (cuentaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cuentaAdapterVO.infoString()); 
				saveDemodaErrors(request, cuentaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
					CuentaAdapter.ENC_NAME, cuentaAdapterVO);
			}
			
			// llamada al servicio
			CuentaVO cuentaVO = PadServiceLocator.getCuentaService().updateCuenta
				(userSession, cuentaAdapterVO.getCuenta());
			
            // Tiene errores recuperables
			if (cuentaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cuentaAdapterVO.infoString()); 
				saveDemodaErrors(request, cuentaVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
					CuentaAdapter.ENC_NAME, cuentaAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (cuentaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cuentaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
					CuentaAdapter.ENC_NAME, cuentaVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CuentaAdapter.ENC_NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentaAdapter.ENC_NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return baseVolver(mapping, form, request, response, CuentaAdapter.ENC_NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, CuentaAdapter.ENC_NAME);
	}

	
	public ActionForward paramRecurso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			//bajo el adapter del usserSession
			CuentaAdapter cuentaAdapterVO =  (CuentaAdapter) userSession.get(CuentaAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (cuentaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CuentaAdapter.ENC_NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CuentaAdapter.ENC_NAME); 
			}
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(cuentaAdapterVO, request);
			
			DomicilioVO domicilioVO = cuentaAdapterVO.getCuenta().getDomicilioEnvio();
			
			DemodaUtil.populateVO(domicilioVO, request);
			cuentaAdapterVO.getCuenta().setDomicilioEnvio(domicilioVO);
			
			
			// llamo al param del servicio
			cuentaAdapterVO = PadServiceLocator.getCuentaService().getCuentaAdapterParamRecurso(userSession, cuentaAdapterVO);

            // Tiene errores recuperables
			if (cuentaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cuentaAdapterVO.infoString()); 
				saveDemodaErrors(request, cuentaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
					CuentaAdapter.ENC_NAME, cuentaAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (cuentaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cuentaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
					CuentaAdapter.ENC_NAME, cuentaAdapterVO);
			}
			
			// grabo los mensajes si hubiere
			saveDemodaMessages(request, cuentaAdapterVO);
			
			// Envio el VO al request
			request.setAttribute(CuentaAdapter.ENC_NAME, cuentaAdapterVO);
			return mapping.findForward(PadConstants.FWD_CUENTA_ENC_EDIT_AGREGAR_ADAPTER);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentaAdapter.ENC_NAME);
		}
	}
	
	
	public ActionForward buscarObjImp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());

		// Bajo el adapter del userSession
		CuentaAdapter cuentaAdapterVO = (CuentaAdapter) userSession.get(CuentaAdapter.ENC_NAME);
		
		// Si es nulo no se puede continuar
		if (cuentaAdapterVO == null) {
			log.error("error en: "  + funcName + ": " + CuentaAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, CuentaAdapter.ENC_NAME); 
		}

		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(cuentaAdapterVO, request);
		
		DomicilioVO domicilioVO = cuentaAdapterVO.getCuenta().getDomicilioEnvio();
		
		DemodaUtil.populateVO(domicilioVO, request);
		cuentaAdapterVO.getCuenta().setDomicilioEnvio(domicilioVO);
		
		TipObjImpVO tipObjImpVO = PadServiceLocator.getCuentaService().obtenerTipObjImpFromRecurso(cuentaAdapterVO.getCuenta().getRecurso().getId());

		if(tipObjImpVO != null)
			userSession.getNavModel().putParameter(ObjImpSearchPage.PARAM_TIPOBJIMP_READONLY, tipObjImpVO);
		
		
		return forwardSeleccionar(mapping, request, 
				PadConstants.METOD_CUENTA_PARAM_OBJ_IMP, PadConstants.ACTION_BUSCAR_OBJIMP, false);
	}
		
	public ActionForward paramObjImp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			NavModel navModel = userSession.getNavModel();
			
			try {
				
				//bajo el adapter del usserSession
				CuentaAdapter cuentaAdapterVO =  (CuentaAdapter) userSession.get(CuentaAdapter.ENC_NAME);
				
				// Si es nulo no se puede continuar
				if (cuentaAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + CuentaAdapter.ENC_NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, CuentaAdapter.ENC_NAME); 
				}
				
				// recupero el id seleccionado por el usuario
				String selectedId = navModel.getSelectedId();
				
				// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
				if (StringUtil.isNullOrEmpty(selectedId)) {
					// Envio el VO al request
					request.setAttribute(CuentaAdapter.ENC_NAME, cuentaAdapterVO);
					return mapping.findForward(PadConstants.FWD_CUENTA_ENC_EDIT_AGREGAR_ADAPTER); 
				}
				
				// Seteo el id objeto imponible seleccionado
				cuentaAdapterVO.getCuenta().getObjImp().setId(new Long(selectedId));
				
				// llamo al param del servicio
				cuentaAdapterVO = PadServiceLocator.getCuentaService().getCuentaAdapterParamObjImp
					(userSession, cuentaAdapterVO);

	            // Tiene errores recuperables
				if (cuentaAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + cuentaAdapterVO.infoString()); 
					saveDemodaErrors(request, cuentaAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						CuentaAdapter.ENC_NAME, cuentaAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (cuentaAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + cuentaAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						CuentaAdapter.ENC_NAME, cuentaAdapterVO);
				}
				
				// grabo los mensajes si hubiere
				saveDemodaMessages(request, cuentaAdapterVO);
				
				// Envio el VO al request
				request.setAttribute(CuentaAdapter.ENC_NAME, cuentaAdapterVO);
				return mapping.findForward(PadConstants.FWD_CUENTA_ENC_EDIT_AGREGAR_ADAPTER);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, CuentaAdapter.ENC_NAME);
			}
		}

	public ActionForward buscarLocalidad(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());

		// Bajo el adapter del userSession
		CuentaAdapter cuentaAdapterVO = (CuentaAdapter) userSession.get(CuentaAdapter.ENC_NAME);
		
		// Si es nulo no se puede continuar
		if (cuentaAdapterVO == null) {
			log.error("error en: "  + funcName + ": " + CuentaAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, CuentaAdapter.ENC_NAME); 
		}

		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(cuentaAdapterVO, request);
		
		DomicilioVO domicilioVO = cuentaAdapterVO.getCuenta().getDomicilioEnvio();
		
		DemodaUtil.populateVO(domicilioVO, request);
		cuentaAdapterVO.getCuenta().setDomicilioEnvio(domicilioVO);
		
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
				CuentaAdapter cuentaAdapterVO =  (CuentaAdapter) userSession.get(CuentaAdapter.ENC_NAME);
				
				// Si es nulo no se puede continuar
				if (cuentaAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + CuentaAdapter.ENC_NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, CuentaAdapter.ENC_NAME); 
				}
				
				String findForward = "";
				
				if (cuentaAdapterVO.getAct().equals(BaseConstants.ACT_AGREGAR)) {
					findForward = PadConstants.FWD_CUENTA_ENC_EDIT_AGREGAR_ADAPTER;
				}else if (cuentaAdapterVO.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
					findForward = PadConstants.FWD_CUENTA_ENC_EDIT_ADAPTER;
				}else if (cuentaAdapterVO.getAct().equals(PadConstants.ACT_MODIFICAR_DOMICILIO_ENV)){
					findForward = PadConstants.FWD_CUENTA_MODIF_DOM_ENV_ADAPTER;
				}else if (cuentaAdapterVO.getAct().equals(PadConstants.ACT_AGREGAR_DOMICILIO_ENV)){
					findForward = PadConstants.FWD_CUENTA_MODIF_DOM_ENV_ADAPTER;
				}

				// recupero el id seleccionado por el usuario
				String selectedId = navModel.getSelectedId();
				
				// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
				if (StringUtil.isNullOrEmpty(selectedId)) {
					// Envio el VO al request
					request.setAttribute(CuentaAdapter.ENC_NAME, cuentaAdapterVO);
					
					return mapping.findForward(findForward);
				}

				// Seteo el id atributo seleccionado
				cuentaAdapterVO.getCuenta().getDomicilioEnvio().getLocalidad().setId(new Long(selectedId));
				
				// llamo al param del servicio
				DomicilioAdapter domicilioAdapterVO = new DomicilioAdapter(cuentaAdapterVO.getCuenta().getDomicilioEnvio());
				
				domicilioAdapterVO = PadServiceLocator.getPadUbicacionService().getDomicilioAdapterParamLocalidad
					(userSession, domicilioAdapterVO);
				// copia del domicilio.
				cuentaAdapterVO.getCuenta().setDomicilioEnvio(domicilioAdapterVO.getDomicilio());
				// pasaje de errores y mensajes
				domicilioAdapterVO.passErrorMessages(cuentaAdapterVO);
				
	            // Tiene errores recuperables
				if (cuentaAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + cuentaAdapterVO.infoString()); 
					saveDemodaErrors(request, cuentaAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						CuentaAdapter.ENC_NAME, cuentaAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (cuentaAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + cuentaAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						CuentaAdapter.ENC_NAME, cuentaAdapterVO);
				}
				
				// grabo los mensajes si hubiere
				saveDemodaMessages(request, cuentaAdapterVO);
				
				// Envio el VO al request
				request.setAttribute(CuentaAdapter.ENC_NAME, cuentaAdapterVO);

				return mapping.findForward(findForward);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, CuentaAdapter.ENC_NAME);
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
			CuentaAdapter cuentaAdapterVO = (CuentaAdapter) userSession.get(CuentaAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (cuentaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CuentaAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CuentaAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(cuentaAdapterVO, request);

			DomicilioVO domicilioVO = cuentaAdapterVO.getCuenta().getDomicilioEnvio();
			
			DemodaUtil.populateVO(domicilioVO, request);
			cuentaAdapterVO.getCuenta().setDomicilioEnvio(domicilioVO);

			// seteo de la calee como filtro a utilizar en la seleccion de calles
			navModel.putParameter(BuscarCalleDAction.CALLE, 
					cuentaAdapterVO.getCuenta().getDomicilioEnvio().getCalle().getDuplicate());
			
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
			CuentaAdapter cuentaAdapterVO =  (CuentaAdapter) userSession.get(CuentaAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (cuentaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CuentaAdapter.ENC_NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CuentaAdapter.ENC_NAME); 
			}

			// recupero el id seleccionado por el usuario
			String selectedId = navModel.getSelectedId();
			
			
			String findForward = "";
			if (cuentaAdapterVO.getAct().equals(BaseConstants.ACT_AGREGAR)) {
				findForward = PadConstants.FWD_CUENTA_ENC_EDIT_AGREGAR_ADAPTER;
			}else if (cuentaAdapterVO.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				findForward = PadConstants.FWD_CUENTA_ENC_EDIT_ADAPTER;
			}else if (cuentaAdapterVO.getAct().equals(PadConstants.ACT_MODIFICAR_DOMICILIO_ENV)){
				findForward = PadConstants.FWD_CUENTA_MODIF_DOM_ENV_ADAPTER;
			}else if (cuentaAdapterVO.getAct().equals(PadConstants.ACT_AGREGAR_DOMICILIO_ENV)){
				findForward = PadConstants.FWD_CUENTA_MODIF_DOM_ENV_ADAPTER;
			}
			
			// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
			if (StringUtil.isNullOrEmpty(selectedId)) {
				// Envio el VO al request
				request.setAttribute(CuentaAdapter.ENC_NAME, cuentaAdapterVO);
				return mapping.findForward(findForward); 
			}

			// Seteo el id localidad seleccionado
			cuentaAdapterVO.getCuenta().getDomicilioEnvio().getCalle().setId(new Long(selectedId));
			// obtencion del DomicilioAdapter para invocar el servicio
			DomicilioAdapter domicilioAdapterVO = new DomicilioAdapter();
			domicilioAdapterVO.setDomicilio(cuentaAdapterVO.getCuenta().getDomicilioEnvio());
			domicilioAdapterVO = PadServiceLocator.getPadUbicacionService().getDomicilioAdapterParamCalle(userSession, domicilioAdapterVO);
			// paso de errores y mensajes
			cuentaAdapterVO.setListError(domicilioAdapterVO.getListError());
			cuentaAdapterVO.setListMessage(domicilioAdapterVO.getListMessage());
			
			// Tiene errores recuperables
			if (cuentaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cuentaAdapterVO.infoString()); 
				saveDemodaErrors(request, cuentaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
						CuentaAdapter.ENC_NAME, cuentaAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (cuentaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cuentaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
						CuentaAdapter.ENC_NAME, cuentaAdapterVO);
			}
			
			// grabo los mensajes si hubiere
			saveDemodaMessages(request, cuentaAdapterVO);
			
			// Envio el VO al request
			request.setAttribute(CuentaAdapter.ENC_NAME, cuentaAdapterVO);

			return mapping.findForward(findForward);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentaAdapter.ENC_NAME);
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
			CuentaAdapter cuentaAdapterVO = (CuentaAdapter) userSession.get(CuentaAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (cuentaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CuentaAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CuentaAdapter.ENC_NAME); 
			}
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(cuentaAdapterVO, request);
			
			// Tiene errores recuperables
			if (cuentaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cuentaAdapterVO.infoString()); 
				saveDemodaErrors(request, cuentaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CuentaAdapter.ENC_NAME, cuentaAdapterVO);
			}

			DomicilioVO domicilioVO = cuentaAdapterVO.getCuenta().getDomicilioEnvio();
			
			DemodaUtil.populateVO(domicilioVO, request);
			
			// Tiene errores recuperables
			if (domicilioVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + domicilioVO.infoString()); 
				saveDemodaErrors(request, cuentaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CuentaAdapter.ENC_NAME, cuentaAdapterVO);
			}
			
			String findForward = "";
			if (cuentaAdapterVO.getAct().equals(BaseConstants.ACT_AGREGAR)) {
				findForward = PadConstants.FWD_CUENTA_ENC_EDIT_AGREGAR_ADAPTER;
			} else if (cuentaAdapterVO.getAct().equals(PadConstants.ACT_MODIFICAR_DOMICILIO_ENV)){
				findForward = PadConstants.FWD_CUENTA_MODIF_DOM_ENV_ADAPTER;
			//Mantis 0005671: al agregar un domicilio y presionar validar sale una pantalla en blanco. 
			} else if (cuentaAdapterVO.getAct().equals("agregarDomicilioEnvio")) {
				findForward = PadConstants.FWD_CUENTA_MODIF_DOM_ENV_ADAPTER;
			}
			
			// llamada al servicio
			domicilioVO = PadServiceLocator.getPadUbicacionService().validarDomicilio(userSession, 
					domicilioVO.getDuplicate());

			// paso de errores y mensajes
			domicilioVO.passErrorMessages(cuentaAdapterVO);
			
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
					saveDemodaMessages(request, cuentaAdapterVO);
					request.setAttribute(CuentaAdapter.ENC_NAME, cuentaAdapterVO);
					return mapping.findForward(findForward);
				}
			}
			// Tiene errores no recuperables
			if (domicilioVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + domicilioVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CuentaAdapter.ENC_NAME, cuentaAdapterVO);
			}
			
			// Fue Exitoso
			// graba el mensaje: Domicilio Valido
			cuentaAdapterVO.addMessage(PadError.DOMICILIO_VALIDO);
			saveDemodaMessages(request, cuentaAdapterVO);
			request.setAttribute(CuentaAdapter.ENC_NAME, cuentaAdapterVO);
			
			return mapping.findForward(findForward);
			
		} catch (Exception exception) {
			exception.printStackTrace();
			return baseException(mapping, request, funcName, exception, CuentaAdapter.ENC_NAME);
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
			CuentaAdapter cuentaAdapterVO =  (CuentaAdapter) userSession.get(CuentaAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (cuentaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CuentaAdapter.ENC_NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CuentaAdapter.ENC_NAME); 
			}
			
			// limpio la lista de errores y mensajes
			cuentaAdapterVO.clearErrorMessages();

			// recupero el domicilio seleccionado
			DomicilioVO domicilioVO = (DomicilioVO) request.getAttribute(DomicilioVO.NAME);
			if (!ModelUtil.isNullOrEmpty(domicilioVO)){
				// seteo del domicilio seleccionado: NO UTILIZAR: cuentaAdapterVO.getPersona().setDomicilio(domicilioVO);
				//cuentaAdapterVO.getPersona().getDomicilio().setId(domicilioVO.getId());
				cuentaAdapterVO.getCuenta().getDomicilioEnvio().setNumero(domicilioVO.getNumero());
				cuentaAdapterVO.getCuenta().getDomicilioEnvio().setCalle(domicilioVO.getCalle());
				cuentaAdapterVO.getCuenta().getDomicilioEnvio().setLetraCalle(domicilioVO.getLetraCalle());
				cuentaAdapterVO.getCuenta().getDomicilioEnvio().setBis(domicilioVO.getBis());
			}
			
			// Envio el VO al request
			request.setAttribute(CuentaAdapter.ENC_NAME, cuentaAdapterVO);
			
			String findForward = "";
			if (cuentaAdapterVO.getAct().equals(BaseConstants.ACT_AGREGAR)) {
				findForward = PadConstants.FWD_CUENTA_ENC_EDIT_AGREGAR_ADAPTER;
			}else if (cuentaAdapterVO.getAct().equals(PadConstants.ACT_MODIFICAR_DOMICILIO_ENV)){
				findForward = PadConstants.FWD_CUENTA_MODIF_DOM_ENV_ADAPTER;
			}else if (cuentaAdapterVO.getAct().equals(PadConstants.ACT_AGREGAR_DOMICILIO_ENV)){
				findForward = PadConstants.FWD_CUENTA_MODIF_DOM_ENV_ADAPTER;
			}

			return mapping.findForward(findForward);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentaAdapter.ENC_NAME);
		}
	}
	
	public ActionForward modificarDomicilioEnvio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				PadSecurityConstants.ABM_CUENTA_DOMICILIO_ENV, BaseSecurityConstants.MODIFICAR);
			
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				CuentaAdapter cuentaAdapterVO = (CuentaAdapter) 
					userSession.get(CuentaAdapter.ENC_NAME);
				
				// Si es nulo no se puede continuar
				if (cuentaAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + 
						CuentaAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, CuentaAdapter.ENC_NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(cuentaAdapterVO, request);
				
				DomicilioVO domicilioVO = cuentaAdapterVO.getCuenta().getDomicilioEnvio();
				
				DemodaUtil.populateVO(domicilioVO, request);
				cuentaAdapterVO.getCuenta().setDomicilioEnvio(domicilioVO);
				domicilioVO.addErrorMessages(cuentaAdapterVO);
				
	            // Tiene errores recuperables
				if (cuentaAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + cuentaAdapterVO.infoString()); 
					saveDemodaErrors(request, cuentaAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						CuentaAdapter.ENC_NAME, cuentaAdapterVO);
				}
				
				// llamada al servicio
				domicilioVO = PadServiceLocator.getCuentaService().updateCuentaDomicilioEnvio
					(userSession, cuentaAdapterVO.getCuenta());
				
	            // Tiene errores recuperables
				if (domicilioVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + cuentaAdapterVO.infoString()); 
					saveDemodaErrors(request, domicilioVO);
					request.setAttribute(CuentaAdapter.ENC_NAME, cuentaAdapterVO);
					return mapping.findForward(PadConstants.FWD_CUENTA_MODIF_DOM_ENV_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (domicilioVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + cuentaAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						CuentaAdapter.ENC_NAME, domicilioVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, CuentaAdapter.ENC_NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, CuentaAdapter.ENC_NAME);
			}
		}

	public ActionForward agregarDomicilioEnvio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				PadSecurityConstants.ABM_CUENTA_DOMICILIO_ENV, BaseSecurityConstants.AGREGAR);
			
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				CuentaAdapter cuentaAdapterVO = (CuentaAdapter) 
					userSession.get(CuentaAdapter.ENC_NAME);
				
				// Si es nulo no se puede continuar
				if (cuentaAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + 
						CuentaAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, CuentaAdapter.ENC_NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(cuentaAdapterVO, request);
				
				DomicilioVO domicilioVO = cuentaAdapterVO.getCuenta().getDomicilioEnvio();
				
				DemodaUtil.populateVO(domicilioVO, request);
				cuentaAdapterVO.getCuenta().setDomicilioEnvio(domicilioVO);
				domicilioVO.addErrorMessages(cuentaAdapterVO);
				
	            // Tiene errores recuperables
				if (cuentaAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + cuentaAdapterVO.infoString()); 
					saveDemodaErrors(request, cuentaAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						CuentaAdapter.ENC_NAME, cuentaAdapterVO);
				}
				
				// llamada al servicio
				domicilioVO = PadServiceLocator.getCuentaService().updateCuentaDomicilioEnvio(userSession, cuentaAdapterVO.getCuenta());
				
	            // Tiene errores recuperables
				if (domicilioVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + domicilioVO.infoString()); 
					saveDemodaErrors(request, domicilioVO);
					request.setAttribute(CuentaAdapter.ENC_NAME, cuentaAdapterVO);
					return mapping.findForward(PadConstants.FWD_CUENTA_MODIF_DOM_ENV_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (domicilioVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + cuentaAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						CuentaAdapter.ENC_NAME, domicilioVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, CuentaAdapter.ENC_NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, CuentaAdapter.ENC_NAME);
			}
		}

	
	// Titular principal
	public ActionForward buscarPersonaSimple(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();		
		
		UserSession userSession = getCurrentUserSession(request, mapping);			
		if (userSession == null) return forwardErrorSession(request);
				
		try {
			// Bajo el adapter del userSession
			CuentaAdapter cuentaAdapterVO = (CuentaAdapter)userSession.get(CuentaAdapter.ENC_NAME);
	
			// Si es nulo no se puede continuar
			if (cuentaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CuentaAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CuentaAdapter.ENC_NAME); 
			}
			
			// Reseteamos la persona utilizada como filtro
			cuentaAdapterVO.setTitular(new PersonaVO());
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(cuentaAdapterVO, request);
			
            // Tiene errores recuperables solo puede ser por formato en el numero de documento
			if (cuentaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cuentaAdapterVO.infoString()); 
								
				cuentaAdapterVO.getTitular().setPersonaBuscada(true);
				cuentaAdapterVO.getTitular().addRecoverableValueError("El formato del campo N\u00FAmero Doc. es incorrecto");
				
				request.setAttribute(CuentaAdapter.ENC_NAME, cuentaAdapterVO);
				userSession.put(CuentaAdapter.ENC_NAME, cuentaAdapterVO);
				return mapping.findForward(PadConstants.FWD_CUENTA_ENC_EDIT_AGREGAR_ADAPTER);
			}
						
			cuentaAdapterVO.setTitular(PadServiceLocator.getPadPersonaService().getPersonaBySexoyNroDoc(userSession,cuentaAdapterVO.getTitular()));
			
			if (cuentaAdapterVO.getTitular().isPersonaEncontrada()){
				cuentaAdapterVO.getCuenta().setDomicilioEnvio(cuentaAdapterVO.getTitular().getDomicilio());
				cuentaAdapterVO.setPoseeDatosPersona(true);
			} else {				
				cuentaAdapterVO.setPoseeDatosPersona(false);
			}
			
			// Envio el VO al request
			request.setAttribute(CuentaAdapter.ENC_NAME, cuentaAdapterVO);
			
			userSession.put(CuentaAdapter.ENC_NAME, cuentaAdapterVO);
			
			return mapping.findForward(PadConstants.FWD_CUENTA_ENC_EDIT_AGREGAR_ADAPTER);
			
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentaAdapter.ENC_NAME);
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
			CuentaAdapter cuentaAdapterVO = (CuentaAdapter)userSession.get(CuentaAdapter.ENC_NAME);
	
			// Si es nulo no se puede continuar
			if (cuentaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CuentaAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CuentaAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(cuentaAdapterVO, request);
			
			cuentaAdapterVO.setTitular(new PersonaVO());
			cuentaAdapterVO.getTitular().setPersonaBuscada(false);//true);
			cuentaAdapterVO.getTitular().setPersonaEncontrada(false);
			cuentaAdapterVO.setPoseeDatosPersona(false);
			
			// Envio el VO al request
			request.setAttribute(CuentaAdapter.ENC_NAME, cuentaAdapterVO);
			
			userSession.put(CuentaAdapter.ENC_NAME, cuentaAdapterVO);
			
			return mapping.findForward(PadConstants.FWD_CUENTA_ENC_EDIT_AGREGAR_ADAPTER);
			
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentaAdapter.ENC_NAME);
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
			CuentaAdapter cuentaAdapterVO = (CuentaAdapter)userSession.get(CuentaAdapter.ENC_NAME);
	
			// Si es nulo no se puede continuar
			if (cuentaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CuentaAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CuentaAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(cuentaAdapterVO, request);
			
            // Tiene errores recuperables
			if (cuentaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cuentaAdapterVO.infoString()); 
				saveDemodaErrors(request, cuentaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CuentaAdapter.ENC_NAME, cuentaAdapterVO);
			}
			
			return forwardSeleccionar(mapping, request, 
					PadConstants.METHOD_CUENTA_PARAM_PERSONA, PadConstants.ACTION_BUSCAR_PERSONA, true);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentaAdapter.ENC_NAME);
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
			CuentaAdapter cuentaAdapterVO = (CuentaAdapter)userSession.get(CuentaAdapter.ENC_NAME);
	
			// Si es nulo no se puede continuar
			if (cuentaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CuentaAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CuentaAdapter.ENC_NAME); 
			}

			// recupero el id seleccionado por el usuario
			String selectedId =  navModel.getSelectedId();				
			
			// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
			if (StringUtil.isNullOrEmpty(selectedId)) {
				// Envio el VO al request
				request.setAttribute(CuentaAdapter.ENC_NAME, cuentaAdapterVO);
				return mapping.findForward(PadConstants.FWD_CUENTA_ENC_EDIT_AGREGAR_ADAPTER); 
			}

			// llamo al param del servicio
			cuentaAdapterVO = PadServiceLocator.getCuentaService().paramPersona(userSession, cuentaAdapterVO, new Long(selectedId));

            // Tiene errores recuperables
			if (cuentaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cuentaAdapterVO.infoString()); 
				saveDemodaErrors(request, cuentaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
						CuentaAdapter.ENC_NAME, cuentaAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (cuentaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cuentaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
						CuentaAdapter.ENC_NAME, cuentaAdapterVO);
			}
			
			DemodaUtil.populateVO(cuentaAdapterVO, request);
			
			// grabo los mensajes si hubiere
			saveDemodaMessages(request, cuentaAdapterVO);
			
			// Envio el VO al request
			request.setAttribute(CuentaAdapter.ENC_NAME, cuentaAdapterVO);
			
			userSession.put(CuentaAdapter.ENC_NAME, cuentaAdapterVO);
			
			return mapping.findForward(PadConstants.FWD_CUENTA_ENC_EDIT_AGREGAR_ADAPTER);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentaAdapter.ENC_NAME);
		}
	}

}
	
