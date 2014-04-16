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
import ar.gov.rosario.siat.def.view.util.DefinitionUtil;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import ar.gov.rosario.siat.pad.iface.model.AltaOficioAdapter;
import ar.gov.rosario.siat.pad.iface.model.AltaOficioVO;
import ar.gov.rosario.siat.pad.iface.model.DomicilioAdapter;
import ar.gov.rosario.siat.pad.iface.model.DomicilioVO;
import ar.gov.rosario.siat.pad.iface.model.TipObjImpAtrDefinition;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;


public final class AdministrarAltaOficioDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarAltaOficioDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_ALTAOFICIO, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		AltaOficioAdapter altaOficioAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getAltaOficioAdapterForView(userSession, commonKey)";
				altaOficioAdapterVO = PadServiceLocator.getPadObjetoImponibleService().getAltaOficioAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(PadConstants.FWD_ALTAOFICIO_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getAltaOficioAdapterForUpdate(userSession, commonKey)";
				altaOficioAdapterVO = PadServiceLocator.getPadObjetoImponibleService().getAltaOficioAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(PadConstants.FWD_ALTAOFICIO_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getAltaOficioAdapterForView(userSession, commonKey)";
				altaOficioAdapterVO = PadServiceLocator.getPadObjetoImponibleService().getAltaOficioAdapterForView(userSession, commonKey);				
				altaOficioAdapterVO.addMessage(BaseError.MSG_ELIMINAR, PadError.ALTAOFICIO_LABEL);
				actionForward = mapping.findForward(PadConstants.FWD_ALTAOFICIO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getAltaOficioAdapterForCreate(userSession)";
				altaOficioAdapterVO = PadServiceLocator.getPadObjetoImponibleService().getAltaOficioAdapterForCreate(userSession);
				actionForward = mapping.findForward(PadConstants.FWD_ALTAOFICIO_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getAltaOficioAdapterForView(userSession)";
				altaOficioAdapterVO = PadServiceLocator.getPadObjetoImponibleService().getAltaOficioAdapterForView(userSession, commonKey);
				altaOficioAdapterVO.addMessage(BaseError.MSG_ACTIVAR, PadError.ALTAOFICIO_LABEL);
				actionForward = mapping.findForward(PadConstants.FWD_ALTAOFICIO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getAltaOficioAdapterForView(userSession)";
				altaOficioAdapterVO = PadServiceLocator.getPadObjetoImponibleService().getAltaOficioAdapterForView(userSession, commonKey);
				altaOficioAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, PadError.ALTAOFICIO_LABEL);
				actionForward = mapping.findForward(PadConstants.FWD_ALTAOFICIO_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (altaOficioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + altaOficioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AltaOficioAdapter.NAME, altaOficioAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			altaOficioAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + AltaOficioAdapter.NAME + ": "+ altaOficioAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(AltaOficioAdapter.NAME, altaOficioAdapterVO);
			// Subo el apdater al userSession
			userSession.put(AltaOficioAdapter.NAME, altaOficioAdapterVO);
			 
			saveDemodaMessages(request, altaOficioAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AltaOficioAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_ALTAOFICIO, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AltaOficioAdapter altaOficioAdapterVO = (AltaOficioAdapter) userSession.get(AltaOficioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (altaOficioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AltaOficioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AltaOficioAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(altaOficioAdapterVO.getContribuyente().getPersona().getDomicilio(), request);
			DemodaUtil.populateVO(altaOficioAdapterVO, request);
			
			log.debug("domicliooooo:"+altaOficioAdapterVO.getContribuyente().getPersona().getDomicilio().getView());
			log.debug("numero:"+altaOficioAdapterVO.getContribuyente().getPersona().getDomicilio().getNumero());
			log.debug("numeroView:"+altaOficioAdapterVO.getContribuyente().getPersona().getDomicilio().getNumeroView());
			// Se valida que si se cargo Piso y Depto en el Domicilio, tengan valores numerico (porque en la base general son integer)
			if(!StringUtil.isNullOrEmpty(altaOficioAdapterVO.getContribuyente().getPersona().getDomicilio().getPiso()) && !StringUtil.isInteger(altaOficioAdapterVO.getContribuyente().getPersona().getDomicilio().getPiso())){
				altaOficioAdapterVO.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, PadError.DOMICILIO_PISO);
			}
			if(!StringUtil.isNullOrEmpty(altaOficioAdapterVO.getContribuyente().getPersona().getDomicilio().getDepto()) && !StringUtil.isInteger(altaOficioAdapterVO.getContribuyente().getPersona().getDomicilio().getDepto())){
				altaOficioAdapterVO.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, PadError.DOMICILIO_DEPTO);
			}

            // Tiene errores recuperables
			if (altaOficioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + altaOficioAdapterVO.infoString()); 
				saveDemodaErrors(request, altaOficioAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, AltaOficioAdapter.NAME, altaOficioAdapterVO);
			}
			
			
			// Metodo auxiliar para loguear todo el request
			DefinitionUtil.requestValues(request);
			
			// Se realiza el populate de los atributos submitidos
			for(TipObjImpAtrDefinition itemDefinition: altaOficioAdapterVO.getTipObjImpDefinition().getListTipObjImpAtrDefinition()){
				DefinitionUtil.populateAtrVal4Edit(itemDefinition, request);
			}
			
			// Se validan formatos
			altaOficioAdapterVO.getTipObjImpDefinition().clearError();
			altaOficioAdapterVO.getTipObjImpDefinition().validate();
			
            // Tiene errores recuperables
			if (altaOficioAdapterVO.getTipObjImpDefinition().hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + altaOficioAdapterVO.infoString()); 
				saveDemodaErrors(request, altaOficioAdapterVO.getTipObjImpDefinition());
				
				return forwardErrorRecoverable(mapping, request, userSession, AltaOficioAdapter.NAME, altaOficioAdapterVO);
			}
			
			// llamada al servicio
			altaOficioAdapterVO = PadServiceLocator.getPadObjetoImponibleService().createAltaOficio(userSession, altaOficioAdapterVO);
			
            // Tiene errores recuperables
			if (altaOficioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + altaOficioAdapterVO.infoString()); 
				saveDemodaErrors(request, altaOficioAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, AltaOficioAdapter.NAME, altaOficioAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (altaOficioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + altaOficioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AltaOficioAdapter.NAME, altaOficioAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AltaOficioAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AltaOficioAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_ALTAOFICIO, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AltaOficioAdapter altaOficioAdapterVO = (AltaOficioAdapter) userSession.get(AltaOficioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (altaOficioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AltaOficioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AltaOficioAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(altaOficioAdapterVO, request);
			
            // Tiene errores recuperables
			if (altaOficioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + altaOficioAdapterVO.infoString()); 
				saveDemodaErrors(request, altaOficioAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, AltaOficioAdapter.NAME, altaOficioAdapterVO);
			}
			
			// Metodo auxiliar para loguear todo el request
			DefinitionUtil.requestValues(request);
			
			// Se realiza el populate de los atributos submitidos
			for(TipObjImpAtrDefinition itemDefinition: altaOficioAdapterVO.getTipObjImpDefinition().getListTipObjImpAtrDefinition()){
				DefinitionUtil.populateAtrVal4Edit(itemDefinition, request);
			}
			
			// Se validan formatos
			altaOficioAdapterVO.getTipObjImpDefinition().clearError();
			altaOficioAdapterVO.getTipObjImpDefinition().validate();
			
            // Tiene errores recuperables
			if (altaOficioAdapterVO.getTipObjImpDefinition().hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + altaOficioAdapterVO.infoString()); 
				saveDemodaErrors(request, altaOficioAdapterVO.getTipObjImpDefinition());
				
				return forwardErrorRecoverable(mapping, request, userSession, AltaOficioAdapter.NAME, altaOficioAdapterVO);
			}
			
			
			// llamada al servicio
			altaOficioAdapterVO = PadServiceLocator.getPadObjetoImponibleService().updateAltaOficio(userSession, altaOficioAdapterVO);
			
            // Tiene errores recuperables
			if (altaOficioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + altaOficioAdapterVO.infoString()); 
				saveDemodaErrors(request, altaOficioAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, AltaOficioAdapter.NAME, altaOficioAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (altaOficioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + altaOficioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AltaOficioAdapter.NAME, altaOficioAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AltaOficioAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AltaOficioAdapter.NAME);
		}
	}

	
	public ActionForward buscarPersona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_ALTAOFICIO, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AltaOficioAdapter altaOficioAdapterVO = (AltaOficioAdapter) userSession.get(AltaOficioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (altaOficioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AltaOficioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AltaOficioAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(altaOficioAdapterVO.getContribuyente().getPersona().getDomicilio(), request);
			DemodaUtil.populateVO(altaOficioAdapterVO, request);
			
            // Tiene errores recuperables
			if (altaOficioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + altaOficioAdapterVO.infoString()); 
				saveDemodaErrors(request, altaOficioAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, AltaOficioAdapter.NAME, altaOficioAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(AltaOficioAdapter.NAME, altaOficioAdapterVO);
			// Subo el apdater al userSession
			userSession.put(AltaOficioAdapter.NAME, altaOficioAdapterVO);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AltaOficioAdapter.NAME);
		}
		
		return forwardSeleccionar(mapping, request, EfConstants.METOD_PARAM_PERSONA, PadConstants.ACTION_BUSCAR_PERSONA, false);		
	}
	
	public ActionForward paramPersona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			NavModel navModel = userSession.getNavModel();
			
			try {
				
				//bajo el adapter del usserSession
				AltaOficioAdapter altaOficioAdapterVO =  (AltaOficioAdapter) userSession.get(AltaOficioAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (altaOficioAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + AltaOficioAdapter.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, AltaOficioAdapter.NAME); 
				}

				// recupero el id seleccionado por el usuario
				String selectedId = navModel.getSelectedId();
				
				// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
				if (StringUtil.isNullOrEmpty(selectedId)) {
					// Envio el VO al request
					request.setAttribute(AltaOficioAdapter.NAME, altaOficioAdapterVO);
					return mapping.findForward(PadConstants.FWD_ALTAOFICIO_EDIT_ADAPTER); 
				}
				
				// Se carga el idPersona seleccionado, en el adapter
				altaOficioAdapterVO.getContribuyente().getPersona().setId(new Long(selectedId));
				
				// llamo al param del servicio
				altaOficioAdapterVO = PadServiceLocator.getPadObjetoImponibleService().getAltaOficioAdapterParamPersona(userSession, altaOficioAdapterVO);

	            // Tiene errores recuperables
				if (altaOficioAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + altaOficioAdapterVO.infoString()); 
					saveDemodaErrors(request, altaOficioAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
							AltaOficioAdapter.NAME, altaOficioAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (altaOficioAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + altaOficioAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
							AltaOficioAdapter.NAME, altaOficioAdapterVO);
				}

				// grabo los mensajes si hubiere
				saveDemodaMessages(request, altaOficioAdapterVO);

				// Envio el VO al request
				request.setAttribute(AltaOficioAdapter.NAME, altaOficioAdapterVO);
				// Subo el apdater al userSession
				userSession.put(AltaOficioAdapter.NAME, altaOficioAdapterVO);

				return mapping.findForward(PadConstants.FWD_ALTAOFICIO_EDIT_ADAPTER);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, AltaOficioAdapter.NAME);
			}
	}
		
	public ActionForward buscarInspector(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_ALTAOFICIO, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AltaOficioAdapter altaOficioAdapterVO = (AltaOficioAdapter) userSession.get(AltaOficioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (altaOficioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AltaOficioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AltaOficioAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(altaOficioAdapterVO.getContribuyente().getPersona().getDomicilio(), request);
			DemodaUtil.populateVO(altaOficioAdapterVO, request);
			
            // Tiene errores recuperables
			if (altaOficioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + altaOficioAdapterVO.infoString()); 
				saveDemodaErrors(request, altaOficioAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, AltaOficioAdapter.NAME, altaOficioAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(AltaOficioAdapter.NAME, altaOficioAdapterVO);
			// Subo el apdater al userSession
			userSession.put(AltaOficioAdapter.NAME, altaOficioAdapterVO);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AltaOficioAdapter.NAME);
		}
		
		return forwardSeleccionar(mapping, request, PadConstants.METOD_PARAM_INSPECTOR, PadConstants.ACTION_BUSCAR_INSPECTOR, false);		
	}
	
	public ActionForward paramInspector(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			NavModel navModel = userSession.getNavModel();
			
			try {
				
				//bajo el adapter del usserSession
				AltaOficioAdapter altaOficioAdapterVO =  (AltaOficioAdapter) userSession.get(AltaOficioAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (altaOficioAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + AltaOficioAdapter.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, AltaOficioAdapter.NAME); 
				}

				// recupero el id seleccionado por el usuario
				String selectedId = navModel.getSelectedId();
				
				// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
				if (StringUtil.isNullOrEmpty(selectedId)) {
					// Envio el VO al request
					request.setAttribute(AltaOficioAdapter.NAME, altaOficioAdapterVO);
					return mapping.findForward(PadConstants.FWD_ALTAOFICIO_EDIT_ADAPTER); 
				}
				
				// Se carga el idInspector seleccionado, en el adapter
				altaOficioAdapterVO.getAltaOficio().getInspector().setId(new Long(selectedId));
				
				// llamo al param del servicio
				altaOficioAdapterVO = PadServiceLocator.getPadObjetoImponibleService().getAltaOficioAdapterParamInspector(userSession, altaOficioAdapterVO);

	            // Tiene errores recuperables
				if (altaOficioAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + altaOficioAdapterVO.infoString()); 
					saveDemodaErrors(request, altaOficioAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
							AltaOficioAdapter.NAME, altaOficioAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (altaOficioAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + altaOficioAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
							AltaOficioAdapter.NAME, altaOficioAdapterVO);
				}

				// grabo los mensajes si hubiere
				saveDemodaMessages(request, altaOficioAdapterVO);

				// Envio el VO al request
				request.setAttribute(AltaOficioAdapter.NAME, altaOficioAdapterVO);
				// Subo el apdater al userSession
				userSession.put(AltaOficioAdapter.NAME, altaOficioAdapterVO);

				return mapping.findForward(PadConstants.FWD_ALTAOFICIO_EDIT_ADAPTER);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, AltaOficioAdapter.NAME);
			}
	}
		
	public ActionForward buscarDomicilio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_ALTAOFICIO, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AltaOficioAdapter altaOficioAdapterVO = (AltaOficioAdapter) userSession.get(AltaOficioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (altaOficioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AltaOficioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AltaOficioAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(altaOficioAdapterVO.getContribuyente().getPersona().getDomicilio(), request);
			DemodaUtil.populateVO(altaOficioAdapterVO, request);
			
            // Tiene errores recuperables
			if (altaOficioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + altaOficioAdapterVO.infoString()); 
				saveDemodaErrors(request, altaOficioAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, AltaOficioAdapter.NAME, altaOficioAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(AltaOficioAdapter.NAME, altaOficioAdapterVO);
			// Subo el apdater al userSession
			userSession.put(AltaOficioAdapter.NAME, altaOficioAdapterVO);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AltaOficioAdapter.NAME);
		}
		
		return baseForwardAdapter(mapping, request, funcName, "buscarDomicilio", BaseConstants.ACT_BUSCAR);		
	}
	
	
	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_ALTAOFICIO, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AltaOficioAdapter altaOficioAdapterVO = (AltaOficioAdapter) userSession.get(AltaOficioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (altaOficioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AltaOficioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AltaOficioAdapter.NAME); 
			}

			// llamada al servicio
			AltaOficioVO altaOficioVO = PadServiceLocator.getPadObjetoImponibleService().deleteAltaOficio
				(userSession, altaOficioAdapterVO.getAltaOficio());
			
            // Tiene errores recuperables
			if (altaOficioVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + altaOficioAdapterVO.infoString());
				saveDemodaErrors(request, altaOficioVO);				
				request.setAttribute(AltaOficioAdapter.NAME, altaOficioAdapterVO);
				return mapping.findForward(PadConstants.FWD_ALTAOFICIO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (altaOficioVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + altaOficioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AltaOficioAdapter.NAME, altaOficioAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AltaOficioAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AltaOficioAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_ALTAOFICIO, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AltaOficioAdapter altaOficioAdapterVO = (AltaOficioAdapter) userSession.get(AltaOficioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (altaOficioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AltaOficioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AltaOficioAdapter.NAME); 
			}

			// llamada al servicio
			AltaOficioVO altaOficioVO = PadServiceLocator.getPadObjetoImponibleService().activarCuentaAltaOficio
				(userSession, altaOficioAdapterVO.getAltaOficio());
			
            // Tiene errores recuperables
			if (altaOficioVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + altaOficioAdapterVO.infoString());
				saveDemodaErrors(request, altaOficioVO);				
				request.setAttribute(AltaOficioAdapter.NAME, altaOficioAdapterVO);
				return mapping.findForward(PadConstants.FWD_ALTAOFICIO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (altaOficioVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + altaOficioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AltaOficioAdapter.NAME, altaOficioAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AltaOficioAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AltaOficioAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_ALTAOFICIO, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AltaOficioAdapter altaOficioAdapterVO = (AltaOficioAdapter) userSession.get(AltaOficioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (altaOficioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AltaOficioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AltaOficioAdapter.NAME); 
			}

			// llamada al servicio
			AltaOficioVO altaOficioVO = PadServiceLocator.getPadObjetoImponibleService().desactivarCuentaAltaOficio
				(userSession, altaOficioAdapterVO.getAltaOficio());
			
            // Tiene errores recuperables
			if (altaOficioVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + altaOficioAdapterVO.infoString());
				saveDemodaErrors(request, altaOficioVO);				
				request.setAttribute(AltaOficioAdapter.NAME, altaOficioAdapterVO);
				return mapping.findForward(PadConstants.FWD_ALTAOFICIO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (altaOficioVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + altaOficioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AltaOficioAdapter.NAME, altaOficioAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AltaOficioAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AltaOficioAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, AltaOficioAdapter.NAME);
		
	}

	public ActionForward buscarLocalidad(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());

		// Bajo el adapter del userSession
		AltaOficioAdapter altaOficioAdapterVO = (AltaOficioAdapter) userSession.get(AltaOficioAdapter.NAME);
		
		// Si es nulo no se puede continuar
		if (altaOficioAdapterVO == null) {
			log.error("error en: "  + funcName + ": " + AltaOficioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, AltaOficioAdapter.NAME); 
		}

		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(altaOficioAdapterVO.getContribuyente().getPersona().getDomicilio(), request);
		DemodaUtil.populateVO(altaOficioAdapterVO, request);
		
		// seteo de la localidad como filtro a utilizar en la seleccion de localidades
		navModel.putParameter(BuscarLocalidadDAction.PROVINCIA, 
				altaOficioAdapterVO.getContribuyente().getPersona().getDomicilio().getLocalidad().getProvincia().getDuplicate());
		
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
				AltaOficioAdapter altaOficioAdapterVO =  (AltaOficioAdapter) userSession.get(AltaOficioAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (altaOficioAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + AltaOficioAdapter.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, AltaOficioAdapter.NAME); 
				}

				// recupero el id seleccionado por el usuario
				String selectedId = navModel.getSelectedId();
				
				// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
				if (StringUtil.isNullOrEmpty(selectedId)) {
					// Envio el VO al request
					request.setAttribute(AltaOficioAdapter.NAME, altaOficioAdapterVO);
					return mapping.findForward(PadConstants.FWD_ALTAOFICIO_EDIT_ADAPTER); 
				}

				// Seteo el id atributo seleccionado
				altaOficioAdapterVO.getContribuyente().getPersona().getDomicilio().getLocalidad().setId(new Long(selectedId));
				
				DomicilioAdapter domicilioAdapterVO = new DomicilioAdapter(altaOficioAdapterVO.getContribuyente().getPersona().getDomicilio());
				
				// llamo al param del servicio
				domicilioAdapterVO = PadServiceLocator.getPadUbicacionService().getDomicilioAdapterParamLocalidad(userSession, domicilioAdapterVO);
				// pasaje de mensajes y errores
				domicilioAdapterVO.passErrorMessages(altaOficioAdapterVO);
				
	            // Tiene errores recuperables
				if (altaOficioAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + altaOficioAdapterVO.infoString()); 
					saveDemodaErrors(request, altaOficioAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						AltaOficioAdapter.NAME, altaOficioAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (altaOficioAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + altaOficioAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						AltaOficioAdapter.NAME, altaOficioAdapterVO);
				}
				
				// grabo los mensajes si hubiere
				saveDemodaMessages(request, altaOficioAdapterVO);
				
				// Envio el VO al request
				request.setAttribute(AltaOficioAdapter.NAME, altaOficioAdapterVO);

				return mapping.findForward(PadConstants.FWD_ALTAOFICIO_EDIT_ADAPTER);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, AltaOficioAdapter.NAME);
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
			AltaOficioAdapter altaOficioAdapterVO = (AltaOficioAdapter) userSession.get(AltaOficioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (altaOficioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AltaOficioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AltaOficioAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(altaOficioAdapterVO.getContribuyente().getPersona().getDomicilio(), request);
			DemodaUtil.populateVO(altaOficioAdapterVO, request);
			
			 // Tiene errores recuperables
			if (altaOficioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + altaOficioAdapterVO.infoString()); 
				saveDemodaErrors(request, altaOficioAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
						AltaOficioAdapter.NAME, altaOficioAdapterVO);
			}
			
			// seteo de la calee como filtro a utilizar en la seleccion de calles
			navModel.putParameter(BuscarCalleDAction.CALLE, 
					altaOficioAdapterVO.getContribuyente().getPersona().getDomicilio().getCalle().getDuplicate());
			
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
			AltaOficioAdapter altaOficioAdapterVO =  (AltaOficioAdapter) userSession.get(AltaOficioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (altaOficioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AltaOficioAdapter.NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AltaOficioAdapter.NAME); 
			}

			// recupero el id seleccionado por el usuario
			String selectedId = navModel.getSelectedId();
			
			// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
			if (StringUtil.isNullOrEmpty(selectedId)) {
				// Envio el VO al request
				request.setAttribute(AltaOficioAdapter.NAME, altaOficioAdapterVO);
				return mapping.findForward(PadConstants.FWD_ALTAOFICIO_EDIT_ADAPTER); 
			}

			// Seteo el id localidad seleccionado
			altaOficioAdapterVO.getContribuyente().getPersona().getDomicilio().getCalle().setId(new Long(selectedId));
			// obtencion del DomicilioAdapter para invocar el servicio
			DomicilioAdapter domicilioAdapterVO = new DomicilioAdapter();
			domicilioAdapterVO.setDomicilio(altaOficioAdapterVO.getContribuyente().getPersona().getDomicilio());
			domicilioAdapterVO = PadServiceLocator.getPadUbicacionService().getDomicilioAdapterParamCalle(userSession, domicilioAdapterVO);
			// paso de errores
			altaOficioAdapterVO.setListError(domicilioAdapterVO.getListError());
			altaOficioAdapterVO.setListMessage(domicilioAdapterVO.getListMessage());
			
			// Tiene errores recuperables
			if (altaOficioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + altaOficioAdapterVO.infoString()); 
				saveDemodaErrors(request, altaOficioAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
						AltaOficioAdapter.NAME, altaOficioAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (altaOficioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + altaOficioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
						AltaOficioAdapter.NAME, altaOficioAdapterVO);
			}
			
			// grabo los mensajes si hubiere
			saveDemodaMessages(request, altaOficioAdapterVO);
			
			// Envio el VO al request
			request.setAttribute(AltaOficioAdapter.NAME, altaOficioAdapterVO);

			return mapping.findForward(PadConstants.FWD_ALTAOFICIO_EDIT_ADAPTER);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AltaOficioAdapter.NAME);
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
			AltaOficioAdapter altaOficioAdapterVO = (AltaOficioAdapter) userSession.get(AltaOficioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (altaOficioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DomicilioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AltaOficioAdapter.NAME); 
			}
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(altaOficioAdapterVO.getContribuyente().getPersona().getDomicilio(), request);
			DemodaUtil.populateVO(altaOficioAdapterVO, request);
			
			// Tiene errores recuperables
			if (altaOficioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + altaOficioAdapterVO.infoString()); 
				saveDemodaErrors(request, altaOficioAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, AltaOficioAdapter.NAME, altaOficioAdapterVO);
			}
			
			// llamada al servicio
			DomicilioVO domicilioVO = PadServiceLocator.getPadUbicacionService().validarDomicilio(userSession, 
					altaOficioAdapterVO.getContribuyente().getPersona().getDomicilio().getDuplicate());

			// paso de errores y mensajes
			domicilioVO.passErrorMessages(altaOficioAdapterVO);
			
            // Tiene errores recuperables
			if (domicilioVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + domicilioVO.infoString()); 
				saveDemodaErrors(request, domicilioVO);
				
				if (domicilioVO.getValidoPorRequeridos()){
					// es valido por requerido y no es valido por jar, va a la busqueda de domicilios
					// seteo del domicilio como filtro a utilizar en la seleccion de domicilios validos
					userSession.getNavModel().putParameter(
							BuscarDomicilioDAction.DOMICILIO, altaOficioAdapterVO.getContribuyente().getPersona().getDomicilio());
					return forwardSeleccionar(
							mapping, request, "paramValidarDomicilio", PadConstants.FWD_DOMICILIO_SEARCHPAGE, false);
				}else{
					// es invalido por requeridos, queda en la misma pagina
					saveDemodaMessages(request, altaOficioAdapterVO);
					request.setAttribute(AltaOficioAdapter.NAME, altaOficioAdapterVO);
					return mapping.findForward(PadConstants.FWD_ALTAOFICIO_EDIT_ADAPTER);
				}
			}
			// Tiene errores no recuperables
			if (domicilioVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + domicilioVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AltaOficioAdapter.NAME, altaOficioAdapterVO);
			}
			
			// Fue Exitoso
			// graba el mensaje: Domicilio Valido
			altaOficioAdapterVO.addMessage(PadError.DOMICILIO_VALIDO);
			saveDemodaMessages(request, altaOficioAdapterVO);
			request.setAttribute(AltaOficioAdapter.NAME, altaOficioAdapterVO);
			
			return mapping.findForward(PadConstants.FWD_ALTAOFICIO_EDIT_ADAPTER);
			
		} catch (Exception exception) {
			exception.printStackTrace();
			return baseException(mapping, request, funcName, exception, AltaOficioAdapter.NAME);
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
			AltaOficioAdapter altaOficioAdapterVO =  (AltaOficioAdapter) userSession.get(AltaOficioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (altaOficioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AltaOficioAdapter.NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AltaOficioAdapter.NAME); 
			}
			
			// limpio la lista de errores y mensajes
			altaOficioAdapterVO.clearErrorMessages();

			// recupero el domicilio seleccionado
			DomicilioVO domicilioVO = (DomicilioVO) request.getAttribute(DomicilioVO.NAME);
			if (!ModelUtil.isNullOrEmpty(domicilioVO)){
				// seteo del domicilio seleccionado: NO UTILIZAR: altaOficioAdapterVO.getContribuyente().getPersona().setDomicilio(domicilioVO);
				//altaOficioAdapterVO.getContribuyente().getPersona().getDomicilio().setId(domicilioVO.getId());
				altaOficioAdapterVO.getContribuyente().getPersona().getDomicilio().setNumero(domicilioVO.getNumero());
				altaOficioAdapterVO.getContribuyente().getPersona().getDomicilio().setCalle(domicilioVO.getCalle());
				altaOficioAdapterVO.getContribuyente().getPersona().getDomicilio().setLetraCalle(domicilioVO.getLetraCalle());
				altaOficioAdapterVO.getContribuyente().getPersona().getDomicilio().setBis(domicilioVO.getBis());
			}
			
			// Envio el VO al request
			request.setAttribute(AltaOficioAdapter.NAME, altaOficioAdapterVO);

			return mapping.findForward(PadConstants.FWD_ALTAOFICIO_EDIT_ADAPTER);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AltaOficioAdapter.NAME);
		}
	}
	

	
}
