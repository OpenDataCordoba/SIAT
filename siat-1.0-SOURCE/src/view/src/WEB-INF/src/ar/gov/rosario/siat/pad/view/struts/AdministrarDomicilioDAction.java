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
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.pad.iface.model.DomicilioAdapter;
import ar.gov.rosario.siat.pad.iface.model.DomicilioVO;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarDomicilioDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarDomicilioDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
//UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_DOMICILIO, act); 
		UserSession userSession = getCurrentUserSession(request, mapping);
		
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		DomicilioAdapter domicilioAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			//CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
//if (act.equals( PadConstants.ACT_GET_FOR_VALIDATE)) {
				stringServicio = "getDomicilioAdapterForValidate(userSession)";
				domicilioAdapterVO = PadServiceLocator.getPadUbicacionService().getDomicilioAdapterForValidate(userSession);
				actionForward = mapping.findForward(PadConstants.FWD_DOMICILIO_EDIT_ADAPTER);
//}
			
			/*if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getDomicilioAdapterForUpdate(userSession, commonKey)";
				domicilioAdapterVO = PadServiceLocator.getPadUbicacionService().getDomicilioAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(PadConstants.FWD_DOMICILIO_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getDomicilioAdapterForView(userSession, commonKey)";
				domicilioAdapterVO = PadServiceLocator.getPadUbicacionService().getDomicilioAdapterForView(userSession, commonKey);				
				domicilioAdapterVO.addMessage(BaseError.MSG_ELIMINAR, PadError.DOMICILIO_LABEL);
				actionForward = mapping.findForward(PadConstants.FWD_DOMICILIO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getDomicilioAdapterForCreate(userSession)";
				domicilioAdapterVO = PadServiceLocator.getPadUbicacionService().getDomicilioAdapterForCreate(userSession);
				actionForward = mapping.findForward(PadConstants.FWD_DOMICILIO_EDIT_ADAPTER);				
			}*/
			

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (domicilioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + domicilioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DomicilioAdapter.NAME, domicilioAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			domicilioAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + DomicilioAdapter.NAME + ": "+ domicilioAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(DomicilioAdapter.NAME, domicilioAdapterVO);
			// Subo el apdater al userSession
			userSession.put(DomicilioAdapter.NAME, domicilioAdapterVO);
			 
			saveDemodaMessages(request, domicilioAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			exception.printStackTrace();
			return baseException(mapping, request, funcName, exception, DomicilioAdapter.NAME);
		}
	}
	
	public ActionForward buscarLocalidad(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			UserSession userSession = getCurrentUserSession(request, mapping);
			if (userSession==null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());

			// Bajo el adapter del userSession
			DomicilioAdapter domicilioAdapterVO = (DomicilioAdapter) userSession.get(DomicilioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (domicilioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DomicilioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DomicilioAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(domicilioAdapterVO, request);
			
			 // Tiene errores recuperables
			if (domicilioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + domicilioAdapterVO.infoString()); 
				saveDemodaErrors(request, domicilioAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
						DomicilioAdapter.NAME, domicilioAdapterVO);
			}
			
			return forwardSeleccionar(mapping, request, "paramLocalidad", PadConstants.ACTION_BUSCAR_LOCALIDAD, false); 
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
			DomicilioAdapter domicilioAdapterVO =  (DomicilioAdapter) userSession.get(DomicilioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (domicilioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DomicilioAdapter.NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DomicilioAdapter.NAME); 
			}

			// recupero el id seleccionado por el usuario
			String selectedId = navModel.getSelectedId();
			
			// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
			if (StringUtil.isNullOrEmpty(selectedId)) {
				// Envio el VO al request
				request.setAttribute(DomicilioAdapter.NAME, domicilioAdapterVO);
				return mapping.findForward(PadConstants.FWD_DOMICILIO_EDIT_ADAPTER); 
			}

			// Seteo el id localidad seleccionado
			domicilioAdapterVO.getDomicilio().getLocalidad().setId(new Long(selectedId));
			
			domicilioAdapterVO = PadServiceLocator.getPadUbicacionService().getDomicilioAdapterParamLocalidad(userSession, domicilioAdapterVO);
			
            // Tiene errores recuperables
			if (domicilioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + domicilioAdapterVO.infoString()); 
				saveDemodaErrors(request, domicilioAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
						DomicilioAdapter.NAME, domicilioAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (domicilioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + domicilioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
						DomicilioAdapter.NAME, domicilioAdapterVO);
			}
			
			// grabo los mensajes si hubiere
			saveDemodaMessages(request, domicilioAdapterVO);
			
			// Envio el VO al request
			request.setAttribute(DomicilioAdapter.NAME, domicilioAdapterVO);

			return mapping.findForward(PadConstants.FWD_DOMICILIO_EDIT_ADAPTER);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DomicilioAdapter.NAME);
		}
	}
	
	
	public ActionForward buscarCalle(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			UserSession userSession = getCurrentUserSession(request, mapping);
			if (userSession==null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());

			// Bajo el adapter del userSession
			DomicilioAdapter domicilioAdapterVO = (DomicilioAdapter) userSession.get(DomicilioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (domicilioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DomicilioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DomicilioAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(domicilioAdapterVO, request);
			
			 // Tiene errores recuperables
			if (domicilioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + domicilioAdapterVO.infoString()); 
				saveDemodaErrors(request, domicilioAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
						DomicilioAdapter.NAME, domicilioAdapterVO);
			}
			
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
			DomicilioAdapter domicilioAdapterVO =  (DomicilioAdapter) userSession.get(DomicilioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (domicilioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DomicilioAdapter.NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DomicilioAdapter.NAME); 
			}

			// recupero el id seleccionado por el usuario
			String selectedId = navModel.getSelectedId();
			
			// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
			if (StringUtil.isNullOrEmpty(selectedId)) {
				// Envio el VO al request
				request.setAttribute(DomicilioAdapter.NAME, domicilioAdapterVO);
				return mapping.findForward(PadConstants.FWD_DOMICILIO_EDIT_ADAPTER); 
			}

			// Seteo el id localidad seleccionado
			domicilioAdapterVO.getDomicilio().getCalle().setId(new Long(selectedId));
			
			domicilioAdapterVO = PadServiceLocator.getPadUbicacionService().getDomicilioAdapterParamCalle(userSession, domicilioAdapterVO);
			
            // Tiene errores recuperables
			if (domicilioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + domicilioAdapterVO.infoString()); 
				saveDemodaErrors(request, domicilioAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
						DomicilioAdapter.NAME, domicilioAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (domicilioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + domicilioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
						DomicilioAdapter.NAME, domicilioAdapterVO);
			}
			
			// grabo los mensajes si hubiere
			saveDemodaMessages(request, domicilioAdapterVO);
			
			// Envio el VO al request
			request.setAttribute(DomicilioAdapter.NAME, domicilioAdapterVO);

			return mapping.findForward(PadConstants.FWD_DOMICILIO_EDIT_ADAPTER);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DomicilioAdapter.NAME);
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
			DomicilioAdapter domicilioAdapterVO = (DomicilioAdapter) userSession.get(DomicilioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (domicilioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DomicilioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DomicilioAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(domicilioAdapterVO, request);
			
            // Tiene errores recuperables
			if (domicilioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + domicilioAdapterVO.infoString()); 
				saveDemodaErrors(request, domicilioAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, DomicilioAdapter.NAME, domicilioAdapterVO);
			}
			
			// llamada al servicio
			DomicilioVO domicilioVO = PadServiceLocator.getPadUbicacionService().validarDomicilio(userSession, domicilioAdapterVO.getDomicilio());
			
            // Tiene errores recuperables
			if (domicilioVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + domicilioVO.infoString()); 
				saveDemodaErrors(request, domicilioVO);
				return forwardErrorRecoverable(mapping, request, userSession, DomicilioAdapter.NAME, domicilioAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (domicilioVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + domicilioVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DomicilioAdapter.NAME, domicilioAdapterVO);
			}
			
			// Fue Exitoso
			//return forwardConfirmarOk(mapping, request, funcName, DomicilioAdapter.NAME);
			domicilioVO.addMessage(PadError.DOMICILIO_VALIDO);
			saveDemodaMessages(request, domicilioVO);
			request.setAttribute(DomicilioAdapter.NAME, domicilioAdapterVO);
			
			return mapping.findForward(PadConstants.FWD_DOMICILIO_EDIT_ADAPTER);
			
		} catch (Exception exception) {
			exception.printStackTrace();
			return baseException(mapping, request, funcName, exception, DomicilioAdapter.NAME);
		}
	}
	
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, DomicilioAdapter.NAME);
			
		}
	
	// ************************************************************************************************
	// 						Ver cuales de estos metodos se usan y cuales no
	// ************************************************************************************************
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_DOMICILIO, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DomicilioAdapter domicilioAdapterVO = (DomicilioAdapter) userSession.get(DomicilioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (domicilioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DomicilioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DomicilioAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(domicilioAdapterVO, request);
			
            // Tiene errores recuperables
			if (domicilioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + domicilioAdapterVO.infoString()); 
				saveDemodaErrors(request, domicilioAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, DomicilioAdapter.NAME, domicilioAdapterVO);
			}
			
			// llamada al servicio
			DomicilioVO domicilioVO = null;// = PadServiceLocator.getPadUbicacionService().createDomicilio(userSession, domicilioAdapterVO.getDomicilio());
			
            // Tiene errores recuperables
			if (domicilioVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + domicilioVO.infoString()); 
				saveDemodaErrors(request, domicilioVO);
				return forwardErrorRecoverable(mapping, request, userSession, DomicilioAdapter.NAME, domicilioAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (domicilioVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + domicilioVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DomicilioAdapter.NAME, domicilioAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DomicilioAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DomicilioAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_DOMICILIO, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DomicilioAdapter domicilioAdapterVO = (DomicilioAdapter) userSession.get(DomicilioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (domicilioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DomicilioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DomicilioAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(domicilioAdapterVO, request);
			
            // Tiene errores recuperables
			if (domicilioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + domicilioAdapterVO.infoString()); 
				saveDemodaErrors(request, domicilioAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, DomicilioAdapter.NAME, domicilioAdapterVO);
			}
			
			// llamada al servicio
			DomicilioVO domicilioVO = null;// = PadServiceLocator.getPadUbicacionService().updateDomicilio(userSession, domicilioAdapterVO.getDomicilio());
			
            // Tiene errores recuperables
			if (domicilioVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + domicilioAdapterVO.infoString()); 
				saveDemodaErrors(request, domicilioVO);
				return forwardErrorRecoverable(mapping, request, userSession, DomicilioAdapter.NAME, domicilioAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (domicilioVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + domicilioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DomicilioAdapter.NAME, domicilioAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DomicilioAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DomicilioAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_DOMICILIO, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DomicilioAdapter domicilioAdapterVO = (DomicilioAdapter) userSession.get(DomicilioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (domicilioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DomicilioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DomicilioAdapter.NAME); 
			}

			// llamada al servicio
DomicilioVO domicilioVO = null; // = PadServiceLocator.getPadUbicacionService().deleteDomicilio(userSession, domicilioAdapterVO.getDomicilio());
			
            // Tiene errores recuperables
			if (domicilioVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + domicilioAdapterVO.infoString());
				saveDemodaErrors(request, domicilioVO);				
				request.setAttribute(DomicilioAdapter.NAME, domicilioAdapterVO);
				return mapping.findForward(PadConstants.FWD_DOMICILIO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (domicilioVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + domicilioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DomicilioAdapter.NAME, domicilioAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DomicilioAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DomicilioAdapter.NAME);
		}
	}
	
	
	
	public ActionForward param (ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DomicilioAdapter domicilioAdapterVO = (DomicilioAdapter) userSession.get(DomicilioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (domicilioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DomicilioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DomicilioAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(domicilioAdapterVO, request);
			
            // Tiene errores recuperables
			if (domicilioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + domicilioAdapterVO.infoString()); 
				saveDemodaErrors(request, domicilioAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, DomicilioAdapter.NAME, domicilioAdapterVO);
			}
			
			// llamada al servicio
//domicilioAdapterVO = PadServiceLocator.getPadUbicacionService().getDomicilioAdapterParam(userSession, domicilioAdapterVO);
			
            // Tiene errores recuperables
			if (domicilioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + domicilioAdapterVO.infoString()); 
				saveDemodaErrors(request, domicilioAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, DomicilioAdapter.NAME, domicilioAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (domicilioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + domicilioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DomicilioAdapter.NAME, domicilioAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(DomicilioAdapter.NAME, domicilioAdapterVO);
			// Subo el apdater al userSession
			userSession.put(DomicilioAdapter.NAME, domicilioAdapterVO);
			
			return mapping.findForward(PadConstants.FWD_DOMICILIO_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DomicilioAdapter.NAME);
		}
	}
		
}
