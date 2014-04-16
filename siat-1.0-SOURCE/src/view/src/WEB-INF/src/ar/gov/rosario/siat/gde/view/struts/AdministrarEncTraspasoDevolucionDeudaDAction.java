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

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.gde.iface.model.AccionTraspasoDevolucion;
import ar.gov.rosario.siat.gde.iface.model.DevolucionDeudaVO;
import ar.gov.rosario.siat.gde.iface.model.TraspasoDeudaVO;
import ar.gov.rosario.siat.gde.iface.model.TraspasoDevolucionDeudaAdapter;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import ar.gov.rosario.siat.pad.iface.model.CuentaSearchPage;
import ar.gov.rosario.siat.pad.view.struts.BuscarCuentaDAction;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarEncTraspasoDevolucionDeudaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEncTraspasoDevolucionDeudaDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName + " act = " + act);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_TRASPASO_DEVOLUCION_DEUDA_ENC, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		TraspasoDevolucionDeudaAdapter traspasoDevolucionDeudaAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());

			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getTraspasoDevolucionDeudaAdapterForUpdate(userSession, commonKey, accionKey)";
				// obtencion del id de la accion que determina si estoy en un traspaso o en una devolucion
				Integer idAccion = (Integer) userSession.getNavModel().getParameter(
						TraspasoDevolucionDeudaAdapter.ACCION_KEY);
				CommonKey accionKey = new CommonKey(idAccion.longValue());
				traspasoDevolucionDeudaAdapterVO = GdeServiceLocator.getGestionDeudaJudicialService().getTraspasoDevolucionDeudaAdapterForUpdate(userSession, commonKey, accionKey);
				actionForward = mapping.findForward(GdeConstants.FWD_TRASPASO_DEVOLUCION_DEUDA_ENC_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				
				CommonKey constanciaKey = null;
				Long idConstancia = (Long) userSession.getNavModel().getParameter(
						TraspasoDevolucionDeudaAdapter.CONSTANCIA_KEY);
				if(idConstancia != null){
					constanciaKey = new CommonKey(idConstancia);
					stringServicio = "getTraspasoDevolucionDeudaAdapterForCreateByConstancia(userSession, constanciaKey)";
					traspasoDevolucionDeudaAdapterVO = GdeServiceLocator.getGestionDeudaJudicialService().getTraspasoDevolucionDeudaAdapterForCreateByConstancia(userSession, constanciaKey);

				}else{
					stringServicio = "getTraspasoDevolucionDeudaAdapterForCreate(userSession, constanciaKey)";
					traspasoDevolucionDeudaAdapterVO = GdeServiceLocator.getGestionDeudaJudicialService().getTraspasoDevolucionDeudaAdapterForCreate(userSession);
				}
				
				actionForward = mapping.findForward(GdeConstants.FWD_TRASPASO_DEVOLUCION_DEUDA_ENC_EDIT_ADAPTER);				
			}
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (traspasoDevolucionDeudaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + traspasoDevolucionDeudaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TraspasoDevolucionDeudaAdapter.ENC_NAME, traspasoDevolucionDeudaAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			traspasoDevolucionDeudaAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + TraspasoDevolucionDeudaAdapter.ENC_NAME + ": "+ traspasoDevolucionDeudaAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(TraspasoDevolucionDeudaAdapter.ENC_NAME, traspasoDevolucionDeudaAdapterVO);
			// Subo el apdater al userSession
			userSession.put(TraspasoDevolucionDeudaAdapter.ENC_NAME, traspasoDevolucionDeudaAdapterVO);

			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TraspasoDevolucionDeudaAdapter.ENC_NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_TRASPASO_DEVOLUCION_DEUDA_ENC,
				BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TraspasoDevolucionDeudaAdapter traspasoDevolucionDeudaAdapterVO = (TraspasoDevolucionDeudaAdapter) userSession.get(TraspasoDevolucionDeudaAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (traspasoDevolucionDeudaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TraspasoDevolucionDeudaAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TraspasoDevolucionDeudaAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(traspasoDevolucionDeudaAdapterVO, request);
			
            // Tiene errores recuperables
			if (traspasoDevolucionDeudaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + traspasoDevolucionDeudaAdapterVO.infoString()); 
				saveDemodaErrors(request, traspasoDevolucionDeudaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, TraspasoDevolucionDeudaAdapter.ENC_NAME, traspasoDevolucionDeudaAdapterVO);
			}
			
			// Comprobacion de permiso para agregar detalles a los traspasos y devoluciones de deuda
			UserSession userSessionTD = canAccess(request, mapping, GdeSecurityConstants.ABM_TRADEVDEUDET,
					BaseSecurityConstants.AGREGAR); 
			if (userSessionTD == null) {
				log.error("No tiene permiso para agregar detalles a los traspasos y devoluciones de deuda");
				traspasoDevolucionDeudaAdapterVO.addRecoverableError(GdeError.TRADEVDEUDET_AGREGAR_NO_PERMITIDO);
				saveDemodaErrors(request, traspasoDevolucionDeudaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, TraspasoDevolucionDeudaAdapter.ENC_NAME, traspasoDevolucionDeudaAdapterVO);
			}
			
			if (traspasoDevolucionDeudaAdapterVO.getAccionTraspasoDevolucion().getEsTraspaso()){

				// carga de datos desde el adapter al traspaso 
				traspasoDevolucionDeudaAdapterVO.cargarDatosTraspaso();
				
				// llamada al servicio
				TraspasoDeudaVO traspasoDeudaVO = GdeServiceLocator.getGestionDeudaJudicialService().createTraspasoDeuda(userSession, traspasoDevolucionDeudaAdapterVO.getTraspasoDeuda());
				
	            // Tiene errores recuperables
				if (traspasoDeudaVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + traspasoDeudaVO.infoString()); 
					saveDemodaErrors(request, traspasoDeudaVO);
					return forwardErrorRecoverable(mapping, request, userSession, TraspasoDevolucionDeudaAdapter.ENC_NAME, traspasoDevolucionDeudaAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (traspasoDeudaVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + traspasoDeudaVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, TraspasoDevolucionDeudaAdapter.ENC_NAME, traspasoDevolucionDeudaAdapterVO);
				}
				
				// Fue Exitoso
				// seteo el id para que lo use el siguiente action 
				userSession.getNavModel().setSelectedId(traspasoDeudaVO.getId().toString());
				// parametro de la accion a realizar en el siguiente action
				userSession.getNavModel().putParameter(TraspasoDevolucionDeudaAdapter.ACCION_KEY, AccionTraspasoDevolucion.TRASPASO.getId());
				
				// seteo a donde quiero que se dirija despues de la pantalla de confirmacion.
				return forwardConfirmarOk(mapping, request, funcName, TraspasoDevolucionDeudaAdapter.ENC_NAME, 
					GdeConstants.PATH_ADMINISTRAR_TRASPASO_DEVOLUCION_DEUDA, BaseConstants.METHOD_INICIALIZAR, 
					GdeConstants.ACT_AGREGAR_TRADEVDEUDET);
				
			}else if (traspasoDevolucionDeudaAdapterVO.getAccionTraspasoDevolucion().getEsDevolucion()){

				// carga de datos desde el adapter a la devolucion				
				traspasoDevolucionDeudaAdapterVO.cargarDatosDevolucion();
				
				// llamada al servicio
				DevolucionDeudaVO devolucionDeudaVO = GdeServiceLocator.getGestionDeudaJudicialService().createDevolucionDeuda(userSession, traspasoDevolucionDeudaAdapterVO.getDevolucionDeuda());
				 
	            // Tiene errores recuperables
				if (devolucionDeudaVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + devolucionDeudaVO.infoString()); 
					saveDemodaErrors(request, devolucionDeudaVO);
					return forwardErrorRecoverable(mapping, request, userSession, TraspasoDevolucionDeudaAdapter.ENC_NAME, traspasoDevolucionDeudaAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (devolucionDeudaVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + devolucionDeudaVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, TraspasoDevolucionDeudaAdapter.ENC_NAME, traspasoDevolucionDeudaAdapterVO);
				}
				
				// Fue Exitoso 
				// seteo el id para que lo use el siguiente action 
				userSession.getNavModel().setSelectedId(devolucionDeudaVO.getId().toString());
				// parametro de la accion a realizar en el siguiente action
				userSession.getNavModel().putParameter(TraspasoDevolucionDeudaAdapter.ACCION_KEY, AccionTraspasoDevolucion.DEVOLUCION.getId());

				// seteo a donde quiero que se dirija despues de la pantalla de confirmacion.
				return forwardConfirmarOk(mapping, request, funcName, TraspasoDevolucionDeudaAdapter.ENC_NAME, 
					GdeConstants.PATH_ADMINISTRAR_TRASPASO_DEVOLUCION_DEUDA, BaseConstants.METHOD_INICIALIZAR, 
					GdeConstants.ACT_AGREGAR_TRADEVDEUDET);
			}

			// error:no es ni traspaso ni devolucion.
			log.error("recoverable error en: "  + funcName + ": " + traspasoDevolucionDeudaAdapterVO.infoString());
			traspasoDevolucionDeudaAdapterVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.TRASPASO_DEVOLUCION_DEUDA_ADAPTER_ACCION);
			saveDemodaErrors(request, traspasoDevolucionDeudaAdapterVO);
			return forwardErrorRecoverable(mapping, request, userSession, TraspasoDevolucionDeudaAdapter.ENC_NAME, traspasoDevolucionDeudaAdapterVO);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TraspasoDevolucionDeudaAdapter.ENC_NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_TRADEVDEUDET, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TraspasoDevolucionDeudaAdapter traspasoDevolucionDeudaAdapterVO = (TraspasoDevolucionDeudaAdapter) userSession.get(TraspasoDevolucionDeudaAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (traspasoDevolucionDeudaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TraspasoDevolucionDeudaAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TraspasoDevolucionDeudaAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(traspasoDevolucionDeudaAdapterVO, request);
			
            // Tiene errores recuperables
			if (traspasoDevolucionDeudaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + traspasoDevolucionDeudaAdapterVO.infoString()); 
				saveDemodaErrors(request, traspasoDevolucionDeudaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, TraspasoDevolucionDeudaAdapter.ENC_NAME, traspasoDevolucionDeudaAdapterVO);
			}
			
			if (traspasoDevolucionDeudaAdapterVO.getAccionTraspasoDevolucion().getEsTraspaso()){
				// llamada al servicio
				TraspasoDeudaVO traspasoDeudaVO = GdeServiceLocator.getGestionDeudaJudicialService().updateTraspasoDeuda(userSession, traspasoDevolucionDeudaAdapterVO.getTraspasoDeuda());
				
	            // Tiene errores recuperables
				if (traspasoDeudaVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + traspasoDeudaVO.infoString()); 
					saveDemodaErrors(request, traspasoDeudaVO);
					return forwardErrorRecoverable(mapping, request, userSession, TraspasoDevolucionDeudaAdapter.ENC_NAME, traspasoDevolucionDeudaAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (traspasoDeudaVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + traspasoDeudaVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, TraspasoDevolucionDeudaAdapter.ENC_NAME, traspasoDevolucionDeudaAdapterVO);
				}
				
			}else if (traspasoDevolucionDeudaAdapterVO.getAccionTraspasoDevolucion().getEsDevolucion()){
				// llamada al servicio
				DevolucionDeudaVO devolucionDeudaVO = GdeServiceLocator.getGestionDeudaJudicialService().updateDevolucionDeuda(userSession, traspasoDevolucionDeudaAdapterVO.getDevolucionDeuda());
				
	            // Tiene errores recuperables
				if (devolucionDeudaVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + devolucionDeudaVO.infoString()); 
					saveDemodaErrors(request, devolucionDeudaVO);
					return forwardErrorRecoverable(mapping, request, userSession, TraspasoDevolucionDeudaAdapter.ENC_NAME, traspasoDevolucionDeudaAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (devolucionDeudaVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + devolucionDeudaVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, TraspasoDevolucionDeudaAdapter.ENC_NAME, traspasoDevolucionDeudaAdapterVO);
				}
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TraspasoDevolucionDeudaAdapter.ENC_NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TraspasoDevolucionDeudaAdapter.ENC_NAME);
		}
	}
	
	public ActionForward paramAccion(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			
			try {
				//bajo el adapter del usserSession
				TraspasoDevolucionDeudaAdapter traspasoDevolucionDeudaAdapterVO =  (TraspasoDevolucionDeudaAdapter) userSession.get(TraspasoDevolucionDeudaAdapter.ENC_NAME);
				
				// Si es nulo no se puede continuar
				if (traspasoDevolucionDeudaAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + TraspasoDevolucionDeudaAdapter.ENC_NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, TraspasoDevolucionDeudaAdapter.ENC_NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(traspasoDevolucionDeudaAdapterVO, request);
				
	            // Tiene errores recuperables
				if (traspasoDevolucionDeudaAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + traspasoDevolucionDeudaAdapterVO.infoString()); 
					saveDemodaErrors(request, traspasoDevolucionDeudaAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, TraspasoDevolucionDeudaAdapter.ENC_NAME, traspasoDevolucionDeudaAdapterVO);
				}

				// no es necesario invocar al servicio
				
	            // Tiene errores recuperables
				if (traspasoDevolucionDeudaAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + traspasoDevolucionDeudaAdapterVO.infoString()); 
					saveDemodaErrors(request, traspasoDevolucionDeudaAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						TraspasoDevolucionDeudaAdapter.ENC_NAME, traspasoDevolucionDeudaAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (traspasoDevolucionDeudaAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + traspasoDevolucionDeudaAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						TraspasoDevolucionDeudaAdapter.ENC_NAME, traspasoDevolucionDeudaAdapterVO);
				}
				
				// grabo los mensajes si hubiere
				saveDemodaMessages(request, traspasoDevolucionDeudaAdapterVO);
				
				// Envio el VO al request
				request.setAttribute(TraspasoDevolucionDeudaAdapter.ENC_NAME, traspasoDevolucionDeudaAdapterVO);

				return mapping.findForward(GdeConstants.FWD_TRASPASO_DEVOLUCION_DEUDA_ENC_EDIT_ADAPTER);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, TraspasoDevolucionDeudaAdapter.ENC_NAME);
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
				TraspasoDevolucionDeudaAdapter traspasoDevolucionDeudaAdapterVO =  (TraspasoDevolucionDeudaAdapter) userSession.get(TraspasoDevolucionDeudaAdapter.ENC_NAME);
				
				// Si es nulo no se puede continuar
				if (traspasoDevolucionDeudaAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + TraspasoDevolucionDeudaAdapter.ENC_NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, TraspasoDevolucionDeudaAdapter.ENC_NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(traspasoDevolucionDeudaAdapterVO, request);
				
	            // Tiene errores recuperables
				if (traspasoDevolucionDeudaAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + traspasoDevolucionDeudaAdapterVO.infoString()); 
					saveDemodaErrors(request, traspasoDevolucionDeudaAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, TraspasoDevolucionDeudaAdapter.ENC_NAME, traspasoDevolucionDeudaAdapterVO);
				}

				traspasoDevolucionDeudaAdapterVO = GdeServiceLocator.getGestionDeudaJudicialService().getTraspasoDevolucionDeudaAdapterParamRecurso(userSession, traspasoDevolucionDeudaAdapterVO);
				
	            // Tiene errores recuperables
				if (traspasoDevolucionDeudaAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + traspasoDevolucionDeudaAdapterVO.infoString()); 
					saveDemodaErrors(request, traspasoDevolucionDeudaAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						TraspasoDevolucionDeudaAdapter.ENC_NAME, traspasoDevolucionDeudaAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (traspasoDevolucionDeudaAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + traspasoDevolucionDeudaAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						TraspasoDevolucionDeudaAdapter.ENC_NAME, traspasoDevolucionDeudaAdapterVO);
				}
				
				// grabo los mensajes si hubiere
				saveDemodaMessages(request, traspasoDevolucionDeudaAdapterVO);
				
				// Envio el VO al request
				request.setAttribute(TraspasoDevolucionDeudaAdapter.ENC_NAME, traspasoDevolucionDeudaAdapterVO);

				return mapping.findForward(GdeConstants.FWD_TRASPASO_DEVOLUCION_DEUDA_ENC_EDIT_ADAPTER);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, TraspasoDevolucionDeudaAdapter.ENC_NAME);
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
		TraspasoDevolucionDeudaAdapter traspasoDevolucionDeudaAdapterVO = (TraspasoDevolucionDeudaAdapter) userSession.get(TraspasoDevolucionDeudaAdapter.ENC_NAME);

		// Si es nulo no se puede continuar
		if (traspasoDevolucionDeudaAdapterVO == null) {
			log.error("error en: "  + funcName + ": " + TraspasoDevolucionDeudaAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, TraspasoDevolucionDeudaAdapter.ENC_NAME); 
		}
		
		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(traspasoDevolucionDeudaAdapterVO, request);

		// Tiene errores recuperables
		if (traspasoDevolucionDeudaAdapterVO.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + traspasoDevolucionDeudaAdapterVO.infoString()); 
			saveDemodaErrors(request, traspasoDevolucionDeudaAdapterVO);
			return forwardErrorRecoverable(mapping, request, userSession, TraspasoDevolucionDeudaAdapter.ENC_NAME, traspasoDevolucionDeudaAdapterVO);
		}

		// carga de parametros a utilizar en la busqueda
		CuentaSearchPage cuentaFiltro = new CuentaSearchPage();
		
		// Seteo el recurso y numero de cuenta
		cuentaFiltro.getCuentaTitular().getCuenta().getRecurso().setId(
				traspasoDevolucionDeudaAdapterVO.getRecurso().getId()); 
		cuentaFiltro.getCuentaTitular().getCuenta().setNumeroCuenta(
				traspasoDevolucionDeudaAdapterVO.getCuenta().getNumeroCuenta());
		
		navModel.putParameter(BuscarCuentaDAction.CUENTA_SEARCHPAGE_FILTRO, cuentaFiltro);
		
		return forwardSeleccionar(mapping, request, "paramCuenta", PadConstants.ACTION_BUSCAR_CUENTA , false);
		
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
			TraspasoDevolucionDeudaAdapter traspasoDevolucionDeudaAdapterVO = (TraspasoDevolucionDeudaAdapter) userSession.get(TraspasoDevolucionDeudaAdapter.ENC_NAME);

			// Si es nulo no se puede continuar
			if (traspasoDevolucionDeudaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TraspasoDevolucionDeudaAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TraspasoDevolucionDeudaAdapter.ENC_NAME); 
			}
			
			// recupero el id seleccionado por el usuario
			String selectedId = navModel.getSelectedId();

			// si el id esta vacio, pq selecciono volver, forwardeo 
			if (StringUtil.isNullOrEmpty(selectedId)) {
				// Envio el VO al request
				request.setAttribute(TraspasoDevolucionDeudaAdapter.ENC_NAME, traspasoDevolucionDeudaAdapterVO);
				return mapping.findForward(GdeConstants.FWD_TRASPASO_DEVOLUCION_DEUDA_ENC_EDIT_ADAPTER); 
			}
			// Seteo el id de la cuenta seleccionada
			traspasoDevolucionDeudaAdapterVO.getCuenta().setId(new Long(selectedId));

			// llamo al param del servicio
			traspasoDevolucionDeudaAdapterVO = GdeServiceLocator.getGestionDeudaJudicialService().getTraspasoDevolucionDeudaAdapterParamCuenta(userSession, traspasoDevolucionDeudaAdapterVO);

			// Tiene errores recuperables
			if (traspasoDevolucionDeudaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + traspasoDevolucionDeudaAdapterVO.infoString()); 
				saveDemodaErrors(request, traspasoDevolucionDeudaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
						TraspasoDevolucionDeudaAdapter.ENC_NAME, traspasoDevolucionDeudaAdapterVO);
			}

			// Tiene errores no recuperables
			if (traspasoDevolucionDeudaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + traspasoDevolucionDeudaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
						TraspasoDevolucionDeudaAdapter.ENC_NAME, traspasoDevolucionDeudaAdapterVO);
			}

			// grabo los mensajes si hubiere
			saveDemodaMessages(request, traspasoDevolucionDeudaAdapterVO);

			// Envio el VO al request
			request.setAttribute(TraspasoDevolucionDeudaAdapter.ENC_NAME, traspasoDevolucionDeudaAdapterVO);

			return mapping.findForward(GdeConstants.FWD_TRASPASO_DEVOLUCION_DEUDA_ENC_EDIT_ADAPTER);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TraspasoDevolucionDeudaAdapter.ENC_NAME);
		}
	}



	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, TraspasoDevolucionDeudaAdapter.ENC_NAME);
	}

}
	
