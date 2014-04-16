//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rod.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.iface.util.SiatUtil;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.pad.iface.model.DomicilioAdapter;
import ar.gov.rosario.siat.pad.iface.model.DomicilioVO;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import ar.gov.rosario.siat.pad.view.struts.BuscarCalleDAction;
import ar.gov.rosario.siat.pad.view.struts.BuscarLocalidadDAction;
import ar.gov.rosario.siat.rod.buss.bean.EstadoTramiteRA;
import ar.gov.rosario.siat.rod.buss.bean.HisEstTra;
import ar.gov.rosario.siat.rod.buss.bean.TramiteRA;
import ar.gov.rosario.siat.rod.iface.model.HisEstTraVO;
import ar.gov.rosario.siat.rod.iface.model.PropietarioVO;
import ar.gov.rosario.siat.rod.iface.model.TramiteRAAdapter;
import ar.gov.rosario.siat.rod.iface.model.TramiteRAVO;
import ar.gov.rosario.siat.rod.iface.service.RodServiceLocator;
import ar.gov.rosario.siat.rod.iface.util.RodError;
import ar.gov.rosario.siat.rod.iface.util.RodSecurityConstants;
import ar.gov.rosario.siat.rod.view.util.RodConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;
import coop.tecso.demoda.iface.model.PrintModel;
import coop.tecso.demoda.iface.model.SiNo;


public final class AdministrarTramiteRADAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarTramiteRADAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, RodSecurityConstants.ABM_TRAMITERA, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		TramiteRAAdapter tramiteRAAdapterVO = null;
		
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey=null; 
			
			tramiteRAAdapterVO = (TramiteRAAdapter) userSession.get(TramiteRAAdapter.NAME);
			
			if (!StringUtil.isNullOrEmpty(navModel.getSelectedId()))
				commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getTramiteRAAdapterForView(userSession, commonKey)";
				if(tramiteRAAdapterVO==null){
					tramiteRAAdapterVO = RodServiceLocator.getTramiteService().getTramiteRAAdapterForView(userSession, commonKey);
				}
				actionForward = mapping.findForward(RodConstants.FWD_TRAMITERA_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getTramiteRAAdapterForUpdate(userSession, commonKey)";
			
				if(tramiteRAAdapterVO==null){
					tramiteRAAdapterVO = RodServiceLocator.getTramiteService().getTramiteRAAdapterForUpdate(userSession,commonKey);
				}
				actionForward = mapping.findForward(RodConstants.FWD_TRAMITERA_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getTramiteRAAdapterForView(userSession, commonKey)";
				if(tramiteRAAdapterVO==null){
					tramiteRAAdapterVO = RodServiceLocator.getTramiteService().getTramiteRAAdapterForView(userSession,commonKey);
				}				
				tramiteRAAdapterVO.addMessage(BaseError.MSG_ELIMINAR, RodError.TRAMITERA_LABEL);
				actionForward = mapping.findForward(RodConstants.FWD_TRAMITERA_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getTramiteRAAdapterForCreate(userSession)";
				
				if(tramiteRAAdapterVO==null){
					tramiteRAAdapterVO = RodServiceLocator.getTramiteService().getTramiteRAAdapterForCreate(userSession);
				}
				
				actionForward = mapping.findForward(RodConstants.FWD_TRAMITERA_EDIT_ADAPTER);				
			}
			if (act.equals(RodConstants.ACT_CAMBIAR_ESTADO)) {
				stringServicio = "getTramiteRAAdapterForCambiarEstado(userSession, commonKey)";
				if(tramiteRAAdapterVO==null){
					tramiteRAAdapterVO = RodServiceLocator.getTramiteService().getTramiteRAAdapterForCambiarEstado(userSession, commonKey);
				}
				actionForward = mapping.findForward(RodConstants.FWD_TRAMITERA_ADAPTER);				
			}
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (tramiteRAAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + tramiteRAAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TramiteRAAdapter.NAME, tramiteRAAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			tramiteRAAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + TramiteRAAdapter.NAME + ": "+ tramiteRAAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(TramiteRAAdapter.NAME, tramiteRAAdapterVO);
			// Subo el apdater al userSession
			userSession.put(TramiteRAAdapter.NAME, tramiteRAAdapterVO);
			 
			saveDemodaMessages(request, tramiteRAAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TramiteRAAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, RodSecurityConstants.ABM_TRAMITERA, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TramiteRAAdapter tramiteRAAdapterVO = (TramiteRAAdapter) userSession.get(TramiteRAAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tramiteRAAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TramiteRAAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TramiteRAAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(tramiteRAAdapterVO, request);
			
            // Tiene errores recuperables
			if (tramiteRAAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tramiteRAAdapterVO.infoString()); 
				saveDemodaErrors(request, tramiteRAAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, TramiteRAAdapter.NAME, tramiteRAAdapterVO);
			}
			
			// llamada al servicio
			TramiteRAVO tramiteRAVO = RodServiceLocator.getTramiteService().createTramiteRA(userSession, tramiteRAAdapterVO.getTramiteRA());
			
            // Tiene errores recuperables
			if (tramiteRAVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tramiteRAVO.infoString()); 
				saveDemodaErrors(request, tramiteRAVO);
				return forwardErrorRecoverable(mapping, request, userSession, TramiteRAAdapter.NAME, tramiteRAAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (tramiteRAVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tramiteRAVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TramiteRAAdapter.NAME, tramiteRAAdapterVO);
			}
			
			
			
			// Fue Exitoso
			// seteo el id para que lo use el siguiente action 
			userSession.getNavModel().setSelectedId(tramiteRAVO.getId().toString());

			// lo dirijo al adapter de modificacion
			return forwardConfirmarOk(mapping, request, funcName, TramiteRAAdapter.NAME, 
				"/rod/AdministrarTramiteRA", BaseConstants.METHOD_INICIALIZAR, 
				BaseConstants.ACT_VER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TramiteRAAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, RodSecurityConstants.ABM_TRAMITERA, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TramiteRAAdapter tramiteRAAdapterVO = (TramiteRAAdapter) userSession.get(TramiteRAAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tramiteRAAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TramiteRAAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TramiteRAAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(tramiteRAAdapterVO, request);
			
            // Tiene errores recuperables
			if (tramiteRAAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tramiteRAAdapterVO.infoString()); 
				saveDemodaErrors(request, tramiteRAAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, TramiteRAAdapter.NAME, tramiteRAAdapterVO);
			}
			
			// llamada al servicio
			TramiteRAVO tramiteRAVO = RodServiceLocator.getTramiteService().updateTramiteRA(userSession, tramiteRAAdapterVO.getTramiteRA());
			
            // Tiene errores recuperables
			if (tramiteRAVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tramiteRAAdapterVO.infoString()); 
				saveDemodaErrors(request, tramiteRAVO);
				return forwardErrorRecoverable(mapping, request, userSession, TramiteRAAdapter.NAME, tramiteRAAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (tramiteRAVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tramiteRAAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TramiteRAAdapter.NAME, tramiteRAAdapterVO);
			}
			
			// Fue Exitoso
			//return forwardConfirmarOk(mapping, request, funcName, TramiteRAAdapter.NAME);
			// Fue Exitoso
			// seteo el id para que lo use el siguiente action 
			userSession.getNavModel().setSelectedId(tramiteRAVO.getId().toString());

			// lo dirijo al adapter de modificacion
			return forwardConfirmarOk(mapping, request, funcName, TramiteRAAdapter.NAME, 
				"/rod/AdministrarTramiteRA", BaseConstants.METHOD_INICIALIZAR, 
				BaseConstants.ACT_VER);

			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TramiteRAAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, RodSecurityConstants.ABM_TRAMITERA, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TramiteRAAdapter tramiteRAAdapterVO = (TramiteRAAdapter) userSession.get(TramiteRAAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tramiteRAAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TramiteRAAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TramiteRAAdapter.NAME); 
			}

			// llamada al servicio
			TramiteRAVO tramiteRAVO = RodServiceLocator.getTramiteService().deleteTramiteRA
				(userSession, tramiteRAAdapterVO.getTramiteRA());
			
            // Tiene errores recuperables
			if (tramiteRAVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tramiteRAAdapterVO.infoString());
				saveDemodaErrors(request, tramiteRAVO);				
				request.setAttribute(TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				return mapping.findForward(RodConstants.FWD_TRAMITERA_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (tramiteRAVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tramiteRAAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TramiteRAAdapter.NAME, tramiteRAAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TramiteRAAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TramiteRAAdapter.NAME);
		}
	}
			
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, TramiteRAAdapter.NAME);
		
	}
	

	public ActionForward paramTipoFabricacion (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				TramiteRAAdapter tramiteRAAdapterVO = (TramiteRAAdapter) userSession.get(TramiteRAAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (tramiteRAAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + TramiteRAAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, TramiteRAAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(tramiteRAAdapterVO, request);
				
	            // Tiene errores recuperables
				if (tramiteRAAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + tramiteRAAdapterVO.infoString()); 
					saveDemodaErrors(request, tramiteRAAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				}
				
				// llamada al servicio
				tramiteRAAdapterVO = RodServiceLocator.getTramiteService().getTramiteRAAdapterParamTipoFabricacion(userSession, tramiteRAAdapterVO);
				
	            // Tiene errores recuperables
				if (tramiteRAAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + tramiteRAAdapterVO.infoString()); 
					saveDemodaErrors(request, tramiteRAAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (tramiteRAAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + tramiteRAAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				// Subo el apdater al userSession
				userSession.put(TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				
				return mapping.findForward(RodConstants.FWD_TRAMITERA_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, TramiteRAAdapter.NAME);
			}
		}

	public ActionForward paramTipoTramite(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				TramiteRAAdapter tramiteRAAdapterVO = (TramiteRAAdapter) userSession.get(TramiteRAAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (tramiteRAAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + TramiteRAAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, TramiteRAAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(tramiteRAAdapterVO, request);
				
	            // Tiene errores recuperables
				if (tramiteRAAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + tramiteRAAdapterVO.infoString()); 
					saveDemodaErrors(request, tramiteRAAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				}
				
				// llamada al servicio
				tramiteRAAdapterVO = RodServiceLocator.getTramiteService().getTramiteRAAdapterParamTipoTramite(userSession, tramiteRAAdapterVO);
				
	            // Tiene errores recuperables
				if (tramiteRAAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + tramiteRAAdapterVO.infoString()); 
					saveDemodaErrors(request, tramiteRAAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (tramiteRAAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + tramiteRAAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				// Subo el apdater al userSession
				userSession.put(TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				
				return mapping.findForward(RodConstants.FWD_TRAMITERA_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, TramiteRAAdapter.NAME);
			}
		}

	public ActionForward buscarModelo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		
		//UserSession userSession =getCurrentUserSession(request, mapping);
		TramiteRAAdapter tramiteRA = (TramiteRAAdapter) userSession.get(TramiteRAAdapter.NAME);
		userSession.put(TramiteRAAdapter.NAME, tramiteRA);
		
		if (userSession==null) return forwardErrorSession(request);
		
		// Bajo el adapter del userSession
		TramiteRAAdapter tramiteRAAdapterVO = (TramiteRAAdapter) userSession.get(TramiteRAAdapter.NAME);
		
		// Si es nulo no se puede continuar
		if (tramiteRAAdapterVO == null) {
			log.error("error en: "  + funcName + ": " + TramiteRAAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, TramiteRAAdapter.NAME); 
		}

		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(tramiteRAAdapterVO, request);
				
        // Tiene errores recuperables
		if (tramiteRAAdapterVO.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + tramiteRAAdapterVO.infoString()); 
			saveDemodaErrors(request, tramiteRAAdapterVO);
			return forwardErrorRecoverable(mapping, request, userSession, TramiteRAAdapter.NAME, tramiteRAAdapterVO);
		}
		
		return forwardSeleccionar(mapping, request, 
			RodConstants.PARAM_MODELO, RodConstants.ACTION_BUSCAR_MODELO , false);

	}

	public ActionForward paramModelo(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		NavModel navModel = userSession.getNavModel();
		
		try {
			
			//bajo el adapter del usserSession
			TramiteRAAdapter tramiteRAAdapterVO =  (TramiteRAAdapter) userSession.get(TramiteRAAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tramiteRAAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TramiteRAAdapter.NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TramiteRAAdapter.NAME); 
			}

			// recupero el id seleccionado por el usuario
			String selectedId = navModel.getSelectedId();
			
			// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
			if (StringUtil.isNullOrEmpty(selectedId)) {
				// Envio el VO al request
				request.setAttribute(TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				return mapping.findForward(RodConstants.FWD_TRAMITERA_EDIT_ADAPTER); 
			}


			tramiteRAAdapterVO.getTramiteRA().getBModelo().setCodModelo(new Integer(selectedId));
			
			// llamo al param del servicio
			tramiteRAAdapterVO = RodServiceLocator.getTramiteService().getTramiteRAAdapterParamModelo(userSession, tramiteRAAdapterVO);

            // Tiene errores recuperables
			if (tramiteRAAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tramiteRAAdapterVO.infoString()); 
				saveDemodaErrors(request, tramiteRAAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
					TramiteRAAdapter.NAME, tramiteRAAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (tramiteRAAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tramiteRAAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
					TramiteRAAdapter.NAME, tramiteRAAdapterVO);
			}

			// grabo los mensajes si hubiere
			saveDemodaMessages(request, tramiteRAAdapterVO);

			// Envio el VO al request
			request.setAttribute(TramiteRAAdapter.NAME, tramiteRAAdapterVO);

			return mapping.findForward(RodConstants.FWD_TRAMITERA_EDIT_ADAPTER);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TramiteRAAdapter.NAME);
		}
	}
	
	public ActionForward paramPersonaActual(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			NavModel navModel = userSession.getNavModel();
			
			try {
				
				//bajo el adapter del usserSession
				TramiteRAAdapter tramiteRAAdapterVO =  (TramiteRAAdapter) userSession.get(TramiteRAAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (tramiteRAAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + TramiteRAAdapter.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, TramiteRAAdapter.NAME); 
				}
				
				// no visualizo la lista de resultados
				//cuentaSearchPageVO.setPageNumber(0L);

				// recupero el id seleccionado por el usuario
				String selectedId = navModel.getSelectedId();
				
				// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
				if (StringUtil.isNullOrEmpty(selectedId)) {
					// Envio el VO al request
					request.setAttribute(TramiteRAAdapter.NAME, tramiteRAAdapterVO);
					return mapping.findForward(RodConstants.FWD_TRAMITERA_EDIT_ADAPTER); 
				}
				
				// Seteo el id contribuyente seleccionado: no se si es contribuyente o persona
				//cuentaSearchPageVO.getCuentaTitular().getContribuyente().setId(new Long(selectedId));
				
				tramiteRAAdapterVO.getTramiteRA().getCPersonaActual().setId(new Long(selectedId));
				// llamo al param del servicio
				tramiteRAAdapterVO = RodServiceLocator.getTramiteService().getTramiteRAAdapterParamPersonaActual(userSession, tramiteRAAdapterVO);

	            // Tiene errores recuperables
				if (tramiteRAAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + tramiteRAAdapterVO.infoString()); 
					saveDemodaErrors(request, tramiteRAAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
							TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (tramiteRAAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + tramiteRAAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				}
				
				// grabo los mensajes si hubiere
				saveDemodaMessages(request, tramiteRAAdapterVO);
				
				// Envio el VO al request
				request.setAttribute(TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				userSession.put(TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				
				userSession.getNavModel().setAct(BaseConstants.ACT_BUSCAR);
				userSession.getNavModel().setPrevAction(tramiteRAAdapterVO.getPrevAction());
				userSession.getNavModel().setPrevActionParameter(tramiteRAAdapterVO.getPrevActionParameter());
				
				return mapping.findForward(RodConstants.FWD_TRAMITERA_EDIT_ADAPTER);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, TramiteRAAdapter.NAME);
			}
		}


	public ActionForward paramPropAnterior(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			NavModel navModel = userSession.getNavModel();
			
			try {
				
				//bajo el adapter del usserSession
				TramiteRAAdapter tramiteRAAdapterVO =  (TramiteRAAdapter) userSession.get(TramiteRAAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (tramiteRAAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + TramiteRAAdapter.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, TramiteRAAdapter.NAME); 
				}
				
				// no visualizo la lista de resultados
				//cuentaSearchPageVO.setPageNumber(0L);

				// recupero el id seleccionado por el usuario
				String selectedId = navModel.getSelectedId();
				
				// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
				if (StringUtil.isNullOrEmpty(selectedId)) {
					// Envio el VO al request
					request.setAttribute(TramiteRAAdapter.NAME, tramiteRAAdapterVO);
					return mapping.findForward(RodConstants.FWD_TRAMITERA_EDIT_ADAPTER); 
				}
				
				// Seteo el id contribuyente seleccionado: no se si es contribuyente o persona
				//cuentaSearchPageVO.getCuentaTitular().getContribuyente().setId(new Long(selectedId));
				
				tramiteRAAdapterVO.getTramiteRA().getGPropAnterior().setId(new Long(selectedId));
				// llamo al param del servicio
				tramiteRAAdapterVO = RodServiceLocator.getTramiteService().getTramiteRAAdapterParamPropAnterior(userSession, tramiteRAAdapterVO);

	            // Tiene errores recuperables
				if (tramiteRAAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + tramiteRAAdapterVO.infoString()); 
					saveDemodaErrors(request, tramiteRAAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
							TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (tramiteRAAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + tramiteRAAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				}
				
				// grabo los mensajes si hubiere
				saveDemodaMessages(request, tramiteRAAdapterVO);
				
				// Envio el VO al request
				request.setAttribute(TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				userSession.put(TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				
				userSession.getNavModel().setAct(BaseConstants.ACT_BUSCAR);
				userSession.getNavModel().setPrevAction(tramiteRAAdapterVO.getPrevAction());
				userSession.getNavModel().setPrevActionParameter(tramiteRAAdapterVO.getPrevActionParameter());
				
				return mapping.findForward(RodConstants.FWD_TRAMITERA_EDIT_ADAPTER);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, TramiteRAAdapter.NAME);
			}
		}



	public ActionForward paramTipoDocPropAnterior(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				TramiteRAAdapter tramiteRAAdapterVO = (TramiteRAAdapter) userSession.get(TramiteRAAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (tramiteRAAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + TramiteRAAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, TramiteRAAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(tramiteRAAdapterVO, request);
				
	            // Tiene errores recuperables
				if (tramiteRAAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + tramiteRAAdapterVO.infoString()); 
					saveDemodaErrors(request, tramiteRAAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				}
				
				// llamada al servicio
				tramiteRAAdapterVO = RodServiceLocator.getTramiteService().getTramiteRAAdapterParamTipoDocPropAnterior(userSession, tramiteRAAdapterVO);
				
	            // Tiene errores recuperables
				if (tramiteRAAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + tramiteRAAdapterVO.infoString()); 
					saveDemodaErrors(request, tramiteRAAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (tramiteRAAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + tramiteRAAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				// Subo el apdater al userSession
				userSession.put(TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				
				return mapping.findForward(RodConstants.FWD_TRAMITERA_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, TramiteRAAdapter.NAME);
			}
		}

	public ActionForward paramBTipoMotor(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				TramiteRAAdapter tramiteRAAdapterVO = (TramiteRAAdapter) userSession.get(TramiteRAAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (tramiteRAAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + TramiteRAAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, TramiteRAAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(tramiteRAAdapterVO, request);
				
	            // Tiene errores recuperables
				if (tramiteRAAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + tramiteRAAdapterVO.infoString()); 
					saveDemodaErrors(request, tramiteRAAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				}
				
				// llamada al servicio
				tramiteRAAdapterVO = RodServiceLocator.getTramiteService().getTramiteRAAdapterParamBTipoMotor(userSession, tramiteRAAdapterVO);
				
	            // Tiene errores recuperables
				if (tramiteRAAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + tramiteRAAdapterVO.infoString()); 
					saveDemodaErrors(request, tramiteRAAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (tramiteRAAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + tramiteRAAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				// Subo el apdater al userSession
				userSession.put(TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				
				return mapping.findForward(RodConstants.FWD_TRAMITERA_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, TramiteRAAdapter.NAME);
			}
		}

	public ActionForward paramETipoMotor(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				TramiteRAAdapter tramiteRAAdapterVO = (TramiteRAAdapter) userSession.get(TramiteRAAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (tramiteRAAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + TramiteRAAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, TramiteRAAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(tramiteRAAdapterVO, request);
				
	            // Tiene errores recuperables
				if (tramiteRAAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + tramiteRAAdapterVO.infoString()); 
					saveDemodaErrors(request, tramiteRAAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				}
				
				// llamada al servicio
				tramiteRAAdapterVO = RodServiceLocator.getTramiteService().getTramiteRAAdapterParamETipoMotor(userSession, tramiteRAAdapterVO);
				
	            // Tiene errores recuperables
				if (tramiteRAAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + tramiteRAAdapterVO.infoString()); 
					saveDemodaErrors(request, tramiteRAAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (tramiteRAAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + tramiteRAAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				// Subo el apdater al userSession
				userSession.put(TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				
				return mapping.findForward(RodConstants.FWD_TRAMITERA_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, TramiteRAAdapter.NAME);
			}
		}

	public ActionForward paramTipoPago(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				TramiteRAAdapter tramiteRAAdapterVO = (TramiteRAAdapter) userSession.get(TramiteRAAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (tramiteRAAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + TramiteRAAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, TramiteRAAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(tramiteRAAdapterVO, request);
				
	            // Tiene errores recuperables
				if (tramiteRAAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + tramiteRAAdapterVO.infoString()); 
					saveDemodaErrors(request, tramiteRAAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				}
				
				// llamada al servicio
				tramiteRAAdapterVO = RodServiceLocator.getTramiteService().getTramiteRAAdapterParamTipoPago(userSession, tramiteRAAdapterVO);
				
	            // Tiene errores recuperables
				if (tramiteRAAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + tramiteRAAdapterVO.infoString()); 
					saveDemodaErrors(request, tramiteRAAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (tramiteRAAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + tramiteRAAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				// Subo el apdater al userSession
				userSession.put(TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				
				return mapping.findForward(RodConstants.FWD_TRAMITERA_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, TramiteRAAdapter.NAME);
			}
		}

	public ActionForward paramTipoUso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				TramiteRAAdapter tramiteRAAdapterVO = (TramiteRAAdapter) userSession.get(TramiteRAAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (tramiteRAAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + TramiteRAAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, TramiteRAAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(tramiteRAAdapterVO, request);
				
	            // Tiene errores recuperables
				if (tramiteRAAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + tramiteRAAdapterVO.infoString()); 
					saveDemodaErrors(request, tramiteRAAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				}
				
				// llamada al servicio
				tramiteRAAdapterVO = RodServiceLocator.getTramiteService().getTramiteRAAdapterParamTipoUso(userSession, tramiteRAAdapterVO);
				
	            // Tiene errores recuperables
				if (tramiteRAAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + tramiteRAAdapterVO.infoString()); 
					saveDemodaErrors(request, tramiteRAAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (tramiteRAAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + tramiteRAAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				// Subo el apdater al userSession
				userSession.put(TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				
				return mapping.findForward(RodConstants.FWD_TRAMITERA_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, TramiteRAAdapter.NAME);
			}
		}

	public ActionForward paramTipoCarga(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				TramiteRAAdapter tramiteRAAdapterVO = (TramiteRAAdapter) userSession.get(TramiteRAAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (tramiteRAAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + TramiteRAAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, TramiteRAAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(tramiteRAAdapterVO, request);
				
	            // Tiene errores recuperables
				if (tramiteRAAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + tramiteRAAdapterVO.infoString()); 
					saveDemodaErrors(request, tramiteRAAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				}
				
				// llamada al servicio
				tramiteRAAdapterVO = RodServiceLocator.getTramiteService().getTramiteRAAdapterParamTipoCarga(userSession, tramiteRAAdapterVO);
				
	            // Tiene errores recuperables
				if (tramiteRAAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + tramiteRAAdapterVO.infoString()); 
					saveDemodaErrors(request, tramiteRAAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (tramiteRAAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + tramiteRAAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				// Subo el apdater al userSession
				userSession.put(TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				
				return mapping.findForward(RodConstants.FWD_TRAMITERA_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, TramiteRAAdapter.NAME);
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
			TramiteRAAdapter tramiteRAAdapterVO = (TramiteRAAdapter) userSession.get(TramiteRAAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tramiteRAAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TramiteRAAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TramiteRAAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(tramiteRAAdapterVO, request);

			DomicilioVO domicilioVO = tramiteRAAdapterVO.getTramiteRA().getDDomicilio();
			
			DemodaUtil.populateVO(domicilioVO, request);
			tramiteRAAdapterVO.getTramiteRA().setDDomicilio(domicilioVO);

			// seteo de la calle como filtro a utilizar en la seleccion de calles
			navModel.putParameter(BuscarCalleDAction.CALLE, 
					tramiteRAAdapterVO.getTramiteRA().getDDomicilio().getCalle().getDuplicate());
			
			return forwardSeleccionar(mapping, request, "paramCalle", RodConstants.ACTION_BUSCAR_CALLE, false); 
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
			TramiteRAAdapter tramiteRAAdapterVO =  (TramiteRAAdapter) userSession.get(TramiteRAAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tramiteRAAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TramiteRAAdapter.NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TramiteRAAdapter.NAME); 
			}

			// recupero el id seleccionado por el usuario
			String selectedId = navModel.getSelectedId();
			
			
			String findForward = RodConstants.FWD_TRAMITERA_EDIT_ADAPTER;
			
			// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
			if (StringUtil.isNullOrEmpty(selectedId)) {
				// Envio el VO al request
				request.setAttribute(TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				return mapping.findForward(findForward); 
			}

			// Seteo el id localidad seleccionado
			tramiteRAAdapterVO.getTramiteRA().getDDomicilio().getCalle().setId(new Long(selectedId));
			
			tramiteRAAdapterVO = RodServiceLocator.getTramiteService().getTramiteRAAdapterParamCalle(userSession, tramiteRAAdapterVO);
			// Tiene errores recuperables
			if (tramiteRAAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tramiteRAAdapterVO.infoString()); 
				saveDemodaErrors(request, tramiteRAAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
						TramiteRAAdapter.NAME, tramiteRAAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (tramiteRAAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tramiteRAAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
						TramiteRAAdapter.NAME, tramiteRAAdapterVO);
			}
			
			// grabo los mensajes si hubiere
			saveDemodaMessages(request, tramiteRAAdapterVO);
			
			// Envio el VO al request
			request.setAttribute(TramiteRAAdapter.NAME, tramiteRAAdapterVO);

			return mapping.findForward(findForward);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TramiteRAAdapter.NAME);
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
			TramiteRAAdapter tramiteRAAdapterVO = (TramiteRAAdapter) userSession.get(TramiteRAAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tramiteRAAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TramiteRAAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TramiteRAAdapter.NAME); 
			}
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(tramiteRAAdapterVO, request);
			
			// Tiene errores recuperables
			if (tramiteRAAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tramiteRAAdapterVO.infoString()); 
				saveDemodaErrors(request, tramiteRAAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, TramiteRAAdapter.NAME, tramiteRAAdapterVO	);
				}

			
			DomicilioVO domicilioVO = tramiteRAAdapterVO.getTramiteRA().getDDomicilio();
			domicilioVO.getCalle().setNombreCalle(tramiteRAAdapterVO.getTramiteRA().getDDesCalle());
			domicilioVO.setNumero(tramiteRAAdapterVO.getTramiteRA().getDNumero());
			domicilioVO.setPiso(tramiteRAAdapterVO.getTramiteRA().getDPiso());
			domicilioVO.setDepto(tramiteRAAdapterVO.getTramiteRA().getDDpto());
			domicilioVO.setLocalidad(tramiteRAAdapterVO.getTramiteRA().getDDomicilio().getLocalidad());
			
			
			DemodaUtil.populateVO(domicilioVO, request);
			
			// Tiene errores recuperables
			if (domicilioVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + domicilioVO.infoString()); 
				saveDemodaErrors(request, tramiteRAAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, TramiteRAAdapter.NAME, tramiteRAAdapterVO);
			}
			
			String findForward = RodConstants.FWD_TRAMITERA_EDIT_ADAPTER;
			
			// llamada al servicio
			domicilioVO = PadServiceLocator.getPadUbicacionService().validarDomicilio(userSession, domicilioVO.getDuplicate());

			if(!tramiteRAAdapterVO.isValidarDatoDomicilio()){
				// paso de errores y mensajes
				domicilioVO.passErrorMessages(tramiteRAAdapterVO);			
			}
			
			tramiteRAAdapterVO.getTramiteRA().setDEsValido(domicilioVO.getEsValidado().getId());
			
            // Tiene errores recuperables
			if (tramiteRAAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + domicilioVO.infoString()); 
				saveDemodaErrors(request, domicilioVO);
				
				// es invalido por requeridos, queda en la misma pagina
				saveDemodaMessages(request, tramiteRAAdapterVO);
				request.setAttribute(TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				return mapping.findForward(findForward);
		
			}
			// Tiene errores no recuperables
			if (tramiteRAAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + domicilioVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TramiteRAAdapter.NAME, tramiteRAAdapterVO);
			}
			
			// Fue Exitoso
			// graba el mensaje: Domicilio Valido
			if(!tramiteRAAdapterVO.isValidarDatoDomicilio()){
				// paso de errores y mensajes
				tramiteRAAdapterVO.addMessage(RodError.DOMICILIO_VALIDO);
			}
			
			saveDemodaMessages(request, tramiteRAAdapterVO);
			request.setAttribute(TramiteRAAdapter.NAME, tramiteRAAdapterVO);
			
			return mapping.findForward(findForward);
			
		} catch (Exception exception) {
			exception.printStackTrace();
			return baseException(mapping, request, funcName, exception, TramiteRAAdapter.NAME);
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
			TramiteRAAdapter tramiteRAAdapterVO =  (TramiteRAAdapter) userSession.get(TramiteRAAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tramiteRAAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TramiteRAAdapter.NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TramiteRAAdapter.NAME); 
			}
			
			// limpio la lista de errores y mensajes
			tramiteRAAdapterVO.clearErrorMessages();

			// recupero el domicilio seleccionado
			DomicilioVO domicilioVO = (DomicilioVO) request.getAttribute(DomicilioVO.NAME);
			if (!ModelUtil.isNullOrEmpty(domicilioVO)){
				// 			
				tramiteRAAdapterVO.getTramiteRA().getDDomicilio().setNumero(domicilioVO.getNumero());
				tramiteRAAdapterVO.getTramiteRA().getDDomicilio().setCalle(domicilioVO.getCalle());
				tramiteRAAdapterVO.getTramiteRA().getDDomicilio().setLetraCalle(domicilioVO.getLetraCalle());
				tramiteRAAdapterVO.getTramiteRA().getDDomicilio().setBis(domicilioVO.getBis());
			}
			
			// Envio el VO al request
			request.setAttribute(TramiteRAAdapter.NAME, tramiteRAAdapterVO);
			
			String findForward = RodConstants.FWD_TRAMITERA_EDIT_ADAPTER;
			
			return mapping.findForward(findForward);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TramiteRAAdapter.NAME);
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
		TramiteRAAdapter tramiteRAAdapterVO = (TramiteRAAdapter) userSession.get(TramiteRAAdapter.NAME);
		
		// Si es nulo no se puede continuar
		if (tramiteRAAdapterVO == null) {
			log.error("error en: "  + funcName + ": " + TramiteRAAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, TramiteRAAdapter.NAME); 
		}

		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(tramiteRAAdapterVO, request);
		
		DomicilioVO domicilioVO = tramiteRAAdapterVO.getTramiteRA().getDDomicilio();
		
		DemodaUtil.populateVO(domicilioVO, request);
		tramiteRAAdapterVO.getTramiteRA().setDDomicilio(domicilioVO);
		
		// seteo de la localidad como filtro a utilizar en la seleccion de localidades
		navModel.putParameter(BuscarLocalidadDAction.PROVINCIA, 
				domicilioVO.getLocalidad().getProvincia().getDuplicate());
		
		return forwardSeleccionar(mapping, request, 
				RodConstants.PARAM_LOCALIDAD, RodConstants.ACTION_BUSCAR_LOCALIDAD, false);
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
				TramiteRAAdapter tramiteRAAdapterVO =  (TramiteRAAdapter) userSession.get(TramiteRAAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (tramiteRAAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + TramiteRAAdapter.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, TramiteRAAdapter.NAME); 
				}
				
				String findForward = RodConstants.FWD_TRAMITERA_EDIT_ADAPTER;
				
				// recupero el id seleccionado por el usuario
				String selectedId = navModel.getSelectedId();
				
				// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
				if (StringUtil.isNullOrEmpty(selectedId)) {
					// Envio el VO al request
					request.setAttribute(TramiteRAAdapter.NAME, tramiteRAAdapterVO);
					
					return mapping.findForward(findForward);
				}

				// Seteo el id atributo seleccionado
				tramiteRAAdapterVO.getTramiteRA().getDDomicilio().getLocalidad().setId(new Long(selectedId));
				
				// llamo al param del servicio
				DomicilioAdapter domicilioAdapterVO = new DomicilioAdapter(tramiteRAAdapterVO.getTramiteRA().getDDomicilio());
				
				
				domicilioAdapterVO = RodServiceLocator.getTramiteService().getTramiteRAAdapterParamLocalidad(userSession, domicilioAdapterVO);
				// copia del domicilio.
				tramiteRAAdapterVO.getTramiteRA().setDDomicilio(domicilioAdapterVO.getDomicilio());
				tramiteRAAdapterVO.getTramiteRA().getDDomicilio().setNumero(tramiteRAAdapterVO.getTramiteRA().getDNumero());				
				tramiteRAAdapterVO.getTramiteRA().setDLocalidad(domicilioAdapterVO.getDomicilio().getLocalidad());
				tramiteRAAdapterVO.getTramiteRA().setDDesLocalidad(domicilioAdapterVO.getDomicilio().getLocalidad().getDescripcionPostal());
				tramiteRAAdapterVO.getTramiteRA().setDCodLocalidad(domicilioAdapterVO.getDomicilio().getLocalidad().getCodPostal().toString());
						// pasaje de errores y mensajes
				domicilioAdapterVO.passErrorMessages(tramiteRAAdapterVO);
				
	            // Tiene errores recuperables
				if (tramiteRAAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + tramiteRAAdapterVO.infoString()); 
					saveDemodaErrors(request, tramiteRAAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
							TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (tramiteRAAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + tramiteRAAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
							TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				}
				
				// grabo los mensajes si hubiere
				saveDemodaMessages(request, tramiteRAAdapterVO);
				
				// Envio el VO al request
				request.setAttribute(TramiteRAAdapter.NAME, tramiteRAAdapterVO);

				return mapping.findForward(findForward);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, TramiteRAAdapter.NAME);
			}
		}

	public ActionForward cambiarEstado(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				RodSecurityConstants.ABM_TRAMITERA, RodSecurityConstants.MTD_TRAMITERA_CAMBIAR_ESTADO);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				TramiteRAAdapter tramiteRAAdapterVO = 
					(TramiteRAAdapter) userSession.get(TramiteRAAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (tramiteRAAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + TramiteRAAdapter.NAME 
						+ " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, TramiteRAAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(tramiteRAAdapterVO, request);
				
	            // Tiene errores recuperables
				if (tramiteRAAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + tramiteRAAdapterVO.infoString()); 
					saveDemodaErrors(request, tramiteRAAdapterVO);
					return mapping.findForward(RodConstants.FWD_TRAMITERA_VIEW_ADAPTER);
				}
				
				// llamada al servicio
				TramiteRAVO tramiteRAVO = 
					RodServiceLocator.getTramiteService().cambiarEstadoTramiteRA(userSession, tramiteRAAdapterVO);
				
	            // Tiene errores recuperables
				if (tramiteRAVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + tramiteRAAdapterVO.infoString()); 
					saveDemodaErrors(request, tramiteRAVO);
					request.setAttribute(TramiteRAAdapter.NAME, tramiteRAAdapterVO);					
					return mapping.findForward(RodConstants.FWD_TRAMITERA_ADAPTER);					
				}
				
				// Tiene errores no recuperables
				if (tramiteRAVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + tramiteRAAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, TramiteRAAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, TramiteRAAdapter.NAME);
			}
		}

		public ActionForward verPropietarioActual(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			UserSession userSession = canAccess(request, mapping, RodSecurityConstants.ABM_TRAMITERA,BaseSecurityConstants.VER);
			if (userSession == null) return forwardErrorSession(request);
			
			TramiteRAAdapter tramiteRAAdapterVO = (TramiteRAAdapter) userSession.get(TramiteRAAdapter.NAME);
			tramiteRAAdapterVO.setEsPropActual(true);
			DemodaUtil.populateVO(tramiteRAAdapterVO, request);

			return forwardVerAdapter(mapping, request, funcName, RodConstants.ACTION_ADMINISTRAR_PROPIETARIO);

		}
		
		public ActionForward verPropietarioAnterior(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			UserSession userSession = canAccess(request, mapping, RodSecurityConstants.ABM_TRAMITERA,BaseSecurityConstants.VER);
			if (userSession == null) return forwardErrorSession(request);
			
			TramiteRAAdapter tramiteRAAdapterVO = (TramiteRAAdapter) userSession.get(TramiteRAAdapter.NAME);
			tramiteRAAdapterVO.setEsPropActual(false);
			DemodaUtil.populateVO(tramiteRAAdapterVO, request);

			return forwardVerAdapter(mapping, request, funcName, RodConstants.ACTION_ADMINISTRAR_PROPIETARIO);

		}

		public ActionForward modificarPropietarioActual(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			UserSession userSession = canAccess(request, mapping, RodSecurityConstants.ABM_TRAMITERA,BaseSecurityConstants.MODIFICAR);
			if (userSession == null) return forwardErrorSession(request);
			
			TramiteRAAdapter tramiteRAAdapterVO = (TramiteRAAdapter) userSession.get(TramiteRAAdapter.NAME);
			tramiteRAAdapterVO.setEsPropActual(true);
			DemodaUtil.populateVO(tramiteRAAdapterVO, request);
			
			return forwardModificarAdapter(mapping, request, funcName, RodConstants.ACTION_ADMINISTRAR_PROPIETARIO);

		}
		
		public ActionForward modificarPropietarioAnterior(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			UserSession userSession = canAccess(request, mapping, RodSecurityConstants.ABM_TRAMITERA,BaseSecurityConstants.MODIFICAR);
			if (userSession == null) return forwardErrorSession(request);
			
			TramiteRAAdapter tramiteRAAdapterVO = (TramiteRAAdapter) userSession.get(TramiteRAAdapter.NAME);
			tramiteRAAdapterVO.setEsPropActual(false);
			DemodaUtil.populateVO(tramiteRAAdapterVO, request);
			
			return forwardModificarAdapter(mapping, request, funcName, RodConstants.ACTION_ADMINISTRAR_PROPIETARIO);

		}

		public ActionForward eliminarPropietarioActual(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			UserSession userSession = canAccess(request, mapping, RodSecurityConstants.ABM_TRAMITERA,BaseSecurityConstants.ELIMINAR);
			if (userSession == null) return forwardErrorSession(request);
			
			TramiteRAAdapter tramiteRAAdapterVO = (TramiteRAAdapter) userSession.get(TramiteRAAdapter.NAME);
			tramiteRAAdapterVO.setEsPropActual(true);
			DemodaUtil.populateVO(tramiteRAAdapterVO, request);
			
			return forwardEliminarAdapter(mapping, request, funcName, RodConstants.ACTION_ADMINISTRAR_PROPIETARIO);

		}
		
		public ActionForward eliminarPropietarioAnterior(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			UserSession userSession = canAccess(request, mapping, RodSecurityConstants.ABM_TRAMITERA,BaseSecurityConstants.ELIMINAR);
			if (userSession == null) return forwardErrorSession(request);
			
			TramiteRAAdapter tramiteRAAdapterVO = (TramiteRAAdapter) userSession.get(TramiteRAAdapter.NAME);
			tramiteRAAdapterVO.setEsPropActual(false);
			DemodaUtil.populateVO(tramiteRAAdapterVO, request);
			
			return forwardEliminarAdapter(mapping, request, funcName, RodConstants.ACTION_ADMINISTRAR_PROPIETARIO);

		}
		
		public ActionForward agregarPropietarioActual(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			UserSession userSession = canAccess(request, mapping, RodSecurityConstants.ABM_TRAMITERA,BaseSecurityConstants.AGREGAR);
			if (userSession == null) return forwardErrorSession(request);
			
			TramiteRAAdapter tramiteRAAdapterVO = (TramiteRAAdapter) userSession.get(TramiteRAAdapter.NAME);
			tramiteRAAdapterVO.setEsPropActual(true);
			DemodaUtil.populateVO(tramiteRAAdapterVO, request);
			
			return forwardAgregarAdapter(mapping, request, funcName, RodConstants.ACTION_ADMINISTRAR_PROPIETARIO);
			
		}
		
		public ActionForward agregarPropietarioAnterior(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

				String funcName = DemodaUtil.currentMethodName();
				UserSession userSession = canAccess(request, mapping, RodSecurityConstants.ABM_TRAMITERA,BaseSecurityConstants.AGREGAR);
				if (userSession == null) return forwardErrorSession(request);
				
				TramiteRAAdapter tramiteRAAdapterVO = (TramiteRAAdapter) userSession.get(TramiteRAAdapter.NAME);
				tramiteRAAdapterVO.setEsPropActual(false);
				DemodaUtil.populateVO(tramiteRAAdapterVO, request);
				
				return forwardAgregarAdapter(mapping, request, funcName, RodConstants.ACTION_ADMINISTRAR_PROPIETARIO);
				
			}

		
		public ActionForward refill(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

				String funcName = DemodaUtil.currentMethodName();
				return baseRefill(mapping, form, request, response, funcName, TramiteRAAdapter.NAME);
				
		}
		
		public ActionForward marcarTitularActual(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping, RodSecurityConstants.ABM_PROPIETARIO, RodSecurityConstants.MTD_MARCAR_PRINCIPAL); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				
				String selectedId = userSession.getNavModel().getSelectedId();
				
				// Bajo el adapter del userSession
				TramiteRAAdapter tramiteRAAdapterVO = (TramiteRAAdapter) userSession.get(TramiteRAAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (tramiteRAAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + TramiteRAAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, TramiteRAAdapter.NAME); 
				}
				
				if(!ListUtil.isNullOrEmpty(tramiteRAAdapterVO.getTramiteRA().getListPropietario())){
					for (PropietarioVO ct : tramiteRAAdapterVO.getTramiteRA().getListPropietario()) {						
						if(ct.getTipoPropietario().equals(1)){							
							if (SiNo.SI.equals(ct.getEsPropPrincipal())) {	
								ct.setEsPropPrincipal(SiNo.NO);					
							}
							if(ct.getNroDoc().equals(new Long(selectedId))){
								ct.setEsPropPrincipal(SiNo.SI);
								tramiteRAAdapterVO.getTramiteRA().setCApellidoORazon(ct.getApellidoORazon());
								tramiteRAAdapterVO.getTramiteRA().setCDesTipoDoc(ct.getDesTipoDoc());
								tramiteRAAdapterVO.getTramiteRA().setCCodTipoDoc(ct.getCodTipoDoc());
								tramiteRAAdapterVO.getTramiteRA().setCNroDoc(ct.getNroDoc());
								tramiteRAAdapterVO.getTramiteRA().setCNroIB(ct.getNroIB());
								tramiteRAAdapterVO.getTramiteRA().setCNroProdAgr(ct.getNroProdAgr());
								tramiteRAAdapterVO.getTramiteRA().setCNroCuit(ct.getNroCuit());
								tramiteRAAdapterVO.getTramiteRA().setCCodTipoProp(ct.getCodTipoProp());
								tramiteRAAdapterVO.getTramiteRA().setCDesTipoProp(ct.getDesTipoProp());
								tramiteRAAdapterVO.getTramiteRA().setCFechaNac(ct.getFechaNac());
								tramiteRAAdapterVO.getTramiteRA().setCDesEstCiv(ct.getDesEstCiv());
								tramiteRAAdapterVO.getTramiteRA().setCCodEstCiv(ct.getCodEstCiv());
								tramiteRAAdapterVO.getTramiteRA().setCDesSexo(ct.getSexo().getValue());
							}
						}
					}			
				}
				// Seteo la cuenta titular principal		
				DemodaUtil.populateVO(tramiteRAAdapterVO, request);
				// Envio el VO al request
				request.setAttribute(TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				// Subo el apdater al userSession
				userSession.put(TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				
				return mapping.findForward(RodConstants.FWD_TRAMITERA_EDIT_ADAPTER);
								
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, TramiteRAAdapter.NAME);
			}
		}

		public ActionForward marcarTitularAnterior(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping, RodSecurityConstants.ABM_PROPIETARIO, RodSecurityConstants.MTD_MARCAR_PRINCIPAL); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				
				String selectedId = userSession.getNavModel().getSelectedId();
				
				// Bajo el adapter del userSession
				TramiteRAAdapter tramiteRAAdapterVO = (TramiteRAAdapter) userSession.get(TramiteRAAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (tramiteRAAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + TramiteRAAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, TramiteRAAdapter.NAME); 
				}
				
				if(!ListUtil.isNullOrEmpty(tramiteRAAdapterVO.getTramiteRA().getListPropietario())){
					for (PropietarioVO ct : tramiteRAAdapterVO.getTramiteRA().getListPropietario()) {
						if(ct.getTipoPropietario().equals(2)){
							if (SiNo.SI.equals(ct.getEsPropPrincipal())) {	
								ct.setEsPropPrincipal(SiNo.NO);
							}
							if(ct.getNroDoc().equals(new Long(selectedId))){
								ct.setEsPropPrincipal(SiNo.SI);
								tramiteRAAdapterVO.getTramiteRA().setGApellidoORazon(ct.getApellidoORazon());
								tramiteRAAdapterVO.getTramiteRA().setGDesTipoDoc(ct.getDesTipoDoc());
								tramiteRAAdapterVO.getTramiteRA().setGNroDoc(ct.getNroDoc());
								tramiteRAAdapterVO.getTramiteRA().setGNroCuit(ct.getNroCuit());
							}
						}
					}			
				}
				// Seteo la cuenta titular principal		
				DemodaUtil.populateVO(tramiteRAAdapterVO, request);
				// Envio el VO al request
				request.setAttribute(TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				// Subo el apdater al userSession
				userSession.put(TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				
				return mapping.findForward(RodConstants.FWD_TRAMITERA_EDIT_ADAPTER);
								
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, TramiteRAAdapter.NAME);
			}
		}
		
		public ActionForward imprimir(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled())
				log.debug("entrando en " + funcName);

			//UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_MULTA, GdeSecurityConstants.IMPRIMIR);
			UserSession userSession = getCurrentUserSession(request, mapping);
				
			if (userSession == null)
				return forwardErrorSession(request);
			String stringServicio = "imprimir";
			try {
				// Bajo el adapter del userSession
				TramiteRAAdapter tramiteRAAdapterVO = (TramiteRAAdapter) userSession.get(TramiteRAAdapter.NAME);

				// Si es nulo no se puede continuar
				if (tramiteRAAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + TramiteRAAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, TramiteRAAdapter.NAME); 
				}
				
				// llamada al servicio
					PrintModel print  = RodServiceLocator.getTramiteService().imprimirTramiteRA(userSession, tramiteRAAdapterVO);
					
					baseResponsePrintModel(response, print);
					
					return null;
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception,
						TramiteRAAdapter.NAME);
			}
		}

		/**
		 * Valida los datos del tramite
		 * @param mapping
		 * @param form
		 * @param request
		 * @param response
		 * @return
		 * @throws Exception
		 */
		public ActionForward validarDatos(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping);
					
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				TramiteRAAdapter tramiteRAAdapterVO = (TramiteRAAdapter) userSession.get(TramiteRAAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (tramiteRAAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + TramiteRAAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, TramiteRAAdapter.NAME); 
				}
				
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(tramiteRAAdapterVO, request);
				
				// Tiene errores recuperables
				if (tramiteRAAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + tramiteRAAdapterVO.infoString()); 
					saveDemodaErrors(request, tramiteRAAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, TramiteRAAdapter.NAME, tramiteRAAdapterVO	);
					}
				
				DemodaUtil.populateVO(tramiteRAAdapterVO, request);
				
				// Tiene errores recuperables
				if (tramiteRAAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + tramiteRAAdapterVO.infoString()); 
					saveDemodaErrors(request, tramiteRAAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				}
								
				
				TramiteRA tramiteRA = TramiteRA.getByIdNull(tramiteRAAdapterVO.getTramiteRA().getId());
				String descripcion = "";
				
				// validacion de domicilio
				if(tramiteRAAdapterVO.getTramiteRA().getDDomicilio().getLocalidad().getEsRosario() &&
						!StringUtil.isNullOrEmpty(tramiteRAAdapterVO.getTramiteRA().getDDesCalle())){
					tramiteRAAdapterVO.setValidarDatoDomicilio(true);
					this.validarDomicilio(mapping, form, request, response);
					if(tramiteRAAdapterVO.getTramiteRA().getDEsValido().equals(1)){
						descripcion = SiatUtil.getValueFromBundle("rod.tramiteRA.DDomicilio.domicilioValido");
					}else{
						descripcion = SiatUtil.getValueFromBundle("rod.tramiteRA.DDomicilio.domicilioInvalido");
					}
					tramiteRAAdapterVO.setValidarDatoDomicilio(false);
				}else{					
					if(!tramiteRAAdapterVO.getTramiteRA().getDDomicilio().getLocalidad().getEsRosario()){
						descripcion += SiatUtil.getValueFromBundle("rod.tramiteRA.imposibleValidarDomicilio");
					}
				}
				
				// valida propietarios actuales y anteriores
				descripcion += RodServiceLocator.getTramiteService().validatePropietario(userSession,tramiteRAAdapterVO);
				
				// valida patente
				descripcion += RodServiceLocator.getTramiteService().validatePatente(userSession,tramiteRAAdapterVO);
		        
				EstadoTramiteRA estadoTramiteRA = EstadoTramiteRA.getById(EstadoTramiteRA.ID_NOESESTADO);
				estadoTramiteRA.setEsEstado(0);	
				
	            HisEstTra hisEstTra =null;
            	hisEstTra = tramiteRA.createHisEstTra(estadoTramiteRA, descripcion);	
            	tramiteRAAdapterVO.setIdHisEstTra(hisEstTra.getId());
    			tramiteRA.getListError().addAll(hisEstTra.getListError());
				
    			tramiteRAAdapterVO.getTramiteRA().getListHisEstTra().add((HisEstTraVO)hisEstTra.toVO());
				// paso de errores y mensajes
	    		String findForward = RodConstants.FWD_TRAMITERA_VIEW_ADAPTER;
	    		
	            // Tiene errores recuperables	    			
				if (tramiteRAAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + tramiteRAAdapterVO.infoString()); 
					saveDemodaErrors(request, tramiteRAAdapterVO);
					
					saveDemodaMessages(request, tramiteRAAdapterVO);
					request.setAttribute(TramiteRAAdapter.NAME, tramiteRAAdapterVO);
					return mapping.findForward(findForward);
					
				}
				
				// Tiene errores no recuperables
				if (tramiteRAAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + tramiteRAAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, TramiteRAAdapter.NAME, tramiteRAAdapterVO);
				}
				
				saveDemodaMessages(request, tramiteRAAdapterVO);
				request.setAttribute(TramiteRAAdapter.NAME, tramiteRAAdapterVO);
			
				
				return mapping.findForward(findForward);

				
			} catch (Exception exception) {
				exception.printStackTrace();
				return baseException(mapping, request, funcName, exception, TramiteRAAdapter.NAME);
			}
			
		}
}
