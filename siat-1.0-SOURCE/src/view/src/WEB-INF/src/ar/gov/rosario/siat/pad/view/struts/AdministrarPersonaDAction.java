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

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.pad.iface.model.DomicilioAdapter;
import ar.gov.rosario.siat.pad.iface.model.DomicilioVO;
import ar.gov.rosario.siat.pad.iface.model.PersonaAdapter;
import ar.gov.rosario.siat.pad.iface.model.PersonaVO;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import ar.gov.rosario.siat.rec.iface.model.ObraAdapter;
import ar.gov.rosario.siat.rec.iface.service.RecServiceLocator;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarPersonaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarPersonaDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_PERSONA, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		PersonaAdapter personaAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getPersonaAdapterForView(userSession, commonKey)";
				personaAdapterVO = PadServiceLocator.getPadPersonaService().getPersonaAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(PadConstants.FWD_PERSONA_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getPersonaAdapterForUpdate(userSession, commonKey)";
				personaAdapterVO = PadServiceLocator.getPadPersonaService().getPersonaAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(PadConstants.FWD_PERSONA_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getPersonaAdapterForView(userSession, commonKey)";
				personaAdapterVO = PadServiceLocator.getPadPersonaService().getPersonaAdapterForView(userSession, commonKey);
				personaAdapterVO.addMessage(BaseError.MSG_ELIMINAR, PadError.PERSONA_LABEL);					
				actionForward = mapping.findForward(PadConstants.FWD_PERSONA_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				
				stringServicio = "getPersonaAdapterForCreate(userSession)";
				
				PersonaVO personaVO = (PersonaVO) navModel.getParameter(BuscarPersonaDAction.PERSONA);
				
				personaAdapterVO = PadServiceLocator.getPadPersonaService().getPersonaAdapterForCreate(userSession, personaVO);
				actionForward = mapping.findForward(PadConstants.FWD_PERSONA_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getPersonaAdapterForView(userSession)";
				personaAdapterVO = PadServiceLocator.getPadPersonaService().getPersonaAdapterForView(userSession, commonKey);
				personaAdapterVO.addMessage(BaseError.MSG_ACTIVAR, PadError.PERSONA_LABEL);					
				actionForward = mapping.findForward(PadConstants.FWD_PERSONA_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getPersonaAdapterForView(userSession)";
				personaAdapterVO = PadServiceLocator.getPadPersonaService().getPersonaAdapterForView(userSession, commonKey);
				personaAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, PadError.PERSONA_LABEL);				
				actionForward = mapping.findForward(PadConstants.FWD_PERSONA_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (personaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + personaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PersonaAdapter.NAME, personaAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			personaAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + PersonaAdapter.NAME + ": "+ personaAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(PersonaAdapter.NAME, personaAdapterVO);
			// Subo el apdater al userSession
			userSession.put(PersonaAdapter.NAME, personaAdapterVO);
			 
			saveDemodaMessages(request, personaAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PersonaAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_PERSONA, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PersonaAdapter personaAdapterVO = (PersonaAdapter) userSession.get(PersonaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (personaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PersonaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PersonaAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(personaAdapterVO, request);
			
			// Se valida que si se cargo Piso y Depto en el Domicilio, tengan valores numerico (porque en la base general son integer)
			if(!StringUtil.isNullOrEmpty(personaAdapterVO.getDomicilio().getPiso()) && !StringUtil.isInteger(personaAdapterVO.getDomicilio().getPiso())){
				personaAdapterVO.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, PadError.DOMICILIO_PISO);
			}
			if(!StringUtil.isNullOrEmpty(personaAdapterVO.getDomicilio().getDepto()) && !StringUtil.isInteger(personaAdapterVO.getDomicilio().getDepto())){
				personaAdapterVO.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, PadError.DOMICILIO_DEPTO);
			}
			
            // Tiene errores recuperables
			if (personaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + personaAdapterVO.infoString()); 
				saveDemodaErrors(request, personaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PersonaAdapter.NAME, personaAdapterVO);
			}
			
			// llamada al servicio
			PersonaVO personaVO = PadServiceLocator.getPadPersonaService().createPersona(userSession, personaAdapterVO.getPersona());
			
            // Tiene errores recuperables
			if (personaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + personaVO.infoString()); 
				saveDemodaErrors(request, personaVO);
				return forwardErrorRecoverable(mapping, request, userSession, PersonaAdapter.NAME, personaAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (personaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + personaVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PersonaAdapter.NAME, personaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PersonaAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PersonaAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_PERSONA, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PersonaAdapter personaAdapterVO = (PersonaAdapter) userSession.get(PersonaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (personaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PersonaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PersonaAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(personaAdapterVO, request);
			
			// Se valida que si se cargo Piso y Depto en el Domicilio, tengan valores numerico (porque en la base general son integer)
			if(!StringUtil.isNullOrEmpty(personaAdapterVO.getDomicilio().getPiso()) && !StringUtil.isInteger(personaAdapterVO.getDomicilio().getPiso())){
				personaAdapterVO.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, PadError.DOMICILIO_PISO);
			}
			if(!StringUtil.isNullOrEmpty(personaAdapterVO.getDomicilio().getDepto()) && !StringUtil.isInteger(personaAdapterVO.getDomicilio().getDepto())){
				personaAdapterVO.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, PadError.DOMICILIO_DEPTO);
			}
			
			// Tiene errores recuperables
			if (personaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + personaAdapterVO.infoString()); 
				saveDemodaErrors(request, personaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PersonaAdapter.NAME, personaAdapterVO);
			}
			
			// llamada al servicio
			PersonaVO personaVO = PadServiceLocator.getPadPersonaService().updatePersona(userSession, personaAdapterVO.getPersona());
			 
            // Tiene errores recuperables
			if (personaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + personaAdapterVO.infoString()); 
				saveDemodaErrors(request, personaVO);
				return forwardErrorRecoverable(mapping, request, userSession, PersonaAdapter.NAME, personaAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (personaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + personaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PersonaAdapter.NAME, personaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PersonaAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PersonaAdapter.NAME);
		}
	}

	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, PersonaAdapter.NAME);
		
	}
	
	
	public ActionForward buscarLocalidad(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());

		// Bajo el adapter del userSession
		PersonaAdapter personaAdapterVO = (PersonaAdapter) userSession.get(PersonaAdapter.NAME);
		
		// Si es nulo no se puede continuar
		if (personaAdapterVO == null) {
			log.error("error en: "  + funcName + ": " + PersonaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, PersonaAdapter.NAME); 
		}

		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(personaAdapterVO, request);
		
		// seteo de la localidad como filtro a utilizar en la seleccion de localidades
		navModel.putParameter(BuscarLocalidadDAction.PROVINCIA, 
				personaAdapterVO.getPersona().getDomicilio().getLocalidad().getProvincia().getDuplicate());
		
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
				PersonaAdapter personaAdapterVO =  (PersonaAdapter) userSession.get(PersonaAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (personaAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + PersonaAdapter.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, PersonaAdapter.NAME); 
				}

				// recupero el id seleccionado por el usuario
				String selectedId = navModel.getSelectedId();
				
				// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
				if (StringUtil.isNullOrEmpty(selectedId)) {
					// Envio el VO al request
					request.setAttribute(PersonaAdapter.NAME, personaAdapterVO);
					return mapping.findForward(PadConstants.FWD_PERSONA_EDIT_ADAPTER); 
				}

				// Seteo el id atributo seleccionado
				personaAdapterVO.getPersona().getDomicilio().getLocalidad().setId(new Long(selectedId));
				
				DomicilioAdapter domicilioAdapterVO = new DomicilioAdapter(personaAdapterVO.getPersona().getDomicilio());
				
				// llamo al param del servicio
				domicilioAdapterVO = PadServiceLocator.getPadUbicacionService().getDomicilioAdapterParamLocalidad(userSession, domicilioAdapterVO);
				// pasaje de mensajes y errores
				domicilioAdapterVO.passErrorMessages(personaAdapterVO);
				
	            // Tiene errores recuperables
				if (personaAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + personaAdapterVO.infoString()); 
					saveDemodaErrors(request, personaAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						PersonaAdapter.NAME, personaAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (personaAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + personaAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						PersonaAdapter.NAME, personaAdapterVO);
				}
				
				// grabo los mensajes si hubiere
				saveDemodaMessages(request, personaAdapterVO);
				
				// Envio el VO al request
				request.setAttribute(PersonaAdapter.NAME, personaAdapterVO);

				return mapping.findForward(PadConstants.FWD_PERSONA_EDIT_ADAPTER);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, PersonaAdapter.NAME);
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
			PersonaAdapter personaAdapterVO = (PersonaAdapter) userSession.get(PersonaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (personaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PersonaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PersonaAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(personaAdapterVO, request);
			
			 // Tiene errores recuperables
			if (personaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + personaAdapterVO.infoString()); 
				saveDemodaErrors(request, personaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
						PersonaAdapter.NAME, personaAdapterVO);
			}
			
			// seteo de la calee como filtro a utilizar en la seleccion de calles
			navModel.putParameter(BuscarCalleDAction.CALLE, 
					personaAdapterVO.getPersona().getDomicilio().getCalle().getDuplicate());
			
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
			PersonaAdapter personaAdapterVO =  (PersonaAdapter) userSession.get(PersonaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (personaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PersonaAdapter.NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PersonaAdapter.NAME); 
			}

			// recupero el id seleccionado por el usuario
			String selectedId = navModel.getSelectedId();
			
			// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
			if (StringUtil.isNullOrEmpty(selectedId)) {
				// Envio el VO al request
				request.setAttribute(PersonaAdapter.NAME, personaAdapterVO);
				return mapping.findForward(PadConstants.FWD_PERSONA_EDIT_ADAPTER); 
			}

			// Seteo el id localidad seleccionado
			personaAdapterVO.getPersona().getDomicilio().getCalle().setId(new Long(selectedId));
			// obtencion del DomicilioAdapter para invocar el servicio
			DomicilioAdapter domicilioAdapterVO = new DomicilioAdapter();
			domicilioAdapterVO.setDomicilio(personaAdapterVO.getPersona().getDomicilio());
			domicilioAdapterVO = PadServiceLocator.getPadUbicacionService().getDomicilioAdapterParamCalle(userSession, domicilioAdapterVO);
			// paso de errores
			personaAdapterVO.setListError(domicilioAdapterVO.getListError());
			personaAdapterVO.setListMessage(domicilioAdapterVO.getListMessage());
			
			// Tiene errores recuperables
			if (personaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + personaAdapterVO.infoString()); 
				saveDemodaErrors(request, personaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
						PersonaAdapter.NAME, personaAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (personaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + personaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
						PersonaAdapter.NAME, personaAdapterVO);
			}
			
			// grabo los mensajes si hubiere
			saveDemodaMessages(request, personaAdapterVO);
			
			// Envio el VO al request
			request.setAttribute(PersonaAdapter.NAME, personaAdapterVO);

			return mapping.findForward(PadConstants.FWD_PERSONA_EDIT_ADAPTER);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PersonaAdapter.NAME);
		}
	}

	public ActionForward validarDomicilio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		//UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_DOMICILIO, BaseSecurityConstants.AGREGAR);
		UserSession userSession = getCurrentUserSession(request, mapping);
				
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PersonaAdapter personaAdapterVO = (PersonaAdapter) userSession.get(PersonaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (personaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DomicilioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PersonaAdapter.NAME); 
			}
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(personaAdapterVO, request);
			
			// Tiene errores recuperables
			if (personaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + personaAdapterVO.infoString()); 
				saveDemodaErrors(request, personaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PersonaAdapter.NAME, personaAdapterVO);
			}
			
			// llamada al servicio
			DomicilioVO domicilioVO = PadServiceLocator.getPadUbicacionService().validarDomicilio(userSession, 
					personaAdapterVO.getDomicilio().getDuplicate());

			// paso de errores y mensajes
			domicilioVO.passErrorMessages(personaAdapterVO);
			
            // Tiene errores recuperables
			if (domicilioVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + domicilioVO.infoString()); 
				saveDemodaErrors(request, domicilioVO);
				
				if (domicilioVO.getValidoPorRequeridos()){
					// es valido por requerido y no es valido por jar, va a la busqueda de domicilios
					// seteo del domicilio como filtro a utilizar en la seleccion de domicilios validos
					userSession.getNavModel().putParameter(
							BuscarDomicilioDAction.DOMICILIO, personaAdapterVO.getPersona().getDomicilio());
					return forwardSeleccionar(
							mapping, request, "paramValidarDomicilio", PadConstants.FWD_DOMICILIO_SEARCHPAGE, false);
				}else{
					// es invalido por requeridos, queda en la misma pagina
					saveDemodaMessages(request, personaAdapterVO);
					request.setAttribute(PersonaAdapter.NAME, personaAdapterVO);
					return mapping.findForward(PadConstants.FWD_PERSONA_EDIT_ADAPTER);
				}
			}
			// Tiene errores no recuperables
			if (domicilioVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + domicilioVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PersonaAdapter.NAME, personaAdapterVO);
			}
			
			// Fue Exitoso
			// graba el mensaje: Domicilio Valido
			personaAdapterVO.addMessage(PadError.DOMICILIO_VALIDO);
			saveDemodaMessages(request, personaAdapterVO);
			request.setAttribute(PersonaAdapter.NAME, personaAdapterVO);
			
			return mapping.findForward(PadConstants.FWD_PERSONA_EDIT_ADAPTER);
			
		} catch (Exception exception) {
			exception.printStackTrace();
			return baseException(mapping, request, funcName, exception, PersonaAdapter.NAME);
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
			PersonaAdapter personaAdapterVO =  (PersonaAdapter) userSession.get(PersonaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (personaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PersonaAdapter.NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PersonaAdapter.NAME); 
			}
			
			// limpio la lista de errores y mensajes
			personaAdapterVO.clearErrorMessages();

			// recupero el domicilio seleccionado
			DomicilioVO domicilioVO = (DomicilioVO) request.getAttribute(DomicilioVO.NAME);
			if (!ModelUtil.isNullOrEmpty(domicilioVO)){
				// seteo del domicilio seleccionado: NO UTILIZAR: personaAdapterVO.getPersona().setDomicilio(domicilioVO);
				//personaAdapterVO.getPersona().getDomicilio().setId(domicilioVO.getId());
				personaAdapterVO.getPersona().getDomicilio().setNumero(domicilioVO.getNumero());
				personaAdapterVO.getPersona().getDomicilio().setCalle(domicilioVO.getCalle());
				personaAdapterVO.getPersona().getDomicilio().setLetraCalle(domicilioVO.getLetraCalle());
				personaAdapterVO.getPersona().getDomicilio().setBis(domicilioVO.getBis());
			}
			
			// Envio el VO al request
			request.setAttribute(PersonaAdapter.NAME, personaAdapterVO);

			return mapping.findForward(PadConstants.FWD_PERSONA_EDIT_ADAPTER);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PersonaAdapter.NAME);
		}
	}
	

	public ActionForward paramTipoPersona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			
			try {
				
				//bajo el adapter del usserSession
				PersonaAdapter personaAdapterVO = (PersonaAdapter) userSession.get(PersonaAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (personaAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + PersonaAdapter.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, PersonaAdapter.NAME); 
				}
				Character tp = request.getParameter("persona.tipoPersona").charAt(0);	
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(personaAdapterVO, request);
				log.debug("tipo persona despues del populate: " + personaAdapterVO.getPersona().getTipoPersona());
				personaAdapterVO.getPersona().setTipoPersona(tp);
				
	            // Tiene errores recuperables
				if (personaAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + personaAdapterVO.infoString()); 
					saveDemodaErrors(request, personaAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, PersonaAdapter.NAME,
							personaAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(PersonaAdapter.NAME, personaAdapterVO);

				return mapping.findForward(PadConstants.FWD_PERSONA_EDIT_ADAPTER);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, PersonaAdapter.NAME);
			}
		}
	
	
	public ActionForward buzonCambios(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		request.setAttribute("vieneDe", "administrarPersona");
		return new ActionForward(PadConstants.PATH_ADMINISTRAR_BUZON_CAMBIOS_INICIALIZAR);
	}
	
	public ActionForward imprimirReportFromAdapter(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			// obtiene el nombre del page del request
			//String name = request.getParameter("name");
			String name = PersonaAdapter.NAME;
			String reportFormat = request.getParameter("report.reportFormat");
			
			// **Bajo el searchPage del userSession
			String responseFile = request.getParameter("responseFile");
			if ("1".equals(responseFile)) {
				String fileName = (String) userSession.get("baseImprimir.reportFilename");
				// realiza la visualizacion del reporte
				baseResponseEmbedContent(response, fileName, "application/pdf");
				return null;
			}
			
			// Bajo el adapter del userSession
			ObraAdapter obraAdapterVO = (ObraAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (obraAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName, ObraAdapter.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			obraAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			obraAdapterVO = RecServiceLocator.getCdmService().imprimirInfObrRep(userSession, obraAdapterVO);

			// limpia la lista de reports y la lista de tablas
			obraAdapterVO.getReport().getListReport().clear();
			obraAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (obraAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + obraAdapterVO.infoString());
				saveDemodaErrors(request, obraAdapterVO);				
				request.setAttribute(name, obraAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (obraAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + obraAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.NAME, obraAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, obraAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = obraAdapterVO.getReport().getReportFileName();
			
			// **preparamos para mostrar el imprimir
			request.setAttribute("path", request.getRequestURI());
			userSession.put("baseImprimir.reportFilename", fileName);
			log.debug("exit: " + funcName);
			return new ActionForward(BaseConstants.FWD_VIEW_IMPRIMIR);

		} catch (Exception exception) {
			return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
		}
	}

}
