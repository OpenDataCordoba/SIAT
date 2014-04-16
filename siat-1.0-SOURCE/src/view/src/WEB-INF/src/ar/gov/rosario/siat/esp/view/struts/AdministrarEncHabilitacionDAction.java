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

import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.esp.iface.model.HabilitacionAdapter;
import ar.gov.rosario.siat.esp.iface.model.HabilitacionVO;
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
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class AdministrarEncHabilitacionDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEncHabilitacionDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping, EspSecurityConstants.ABM_HABILITACION_ENC, act);		

			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			HabilitacionAdapter habilitacionAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey=null;
				if(!StringUtil.isNullOrEmpty(navModel.getSelectedId())){
					commonKey = new CommonKey(navModel.getSelectedId());
				}
				if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
					stringServicio = "getHabilitacionAdapterForUpdate(userSession, commonKey)";
					habilitacionAdapterVO = EspServiceLocator.getHabilitacionService().getHabilitacionAdapterForUpdate(userSession, commonKey);
					actionForward = mapping.findForward(EspConstants.FWD_HABILITACION_ENC_EDIT_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
					stringServicio = "getHabilitacionAdapterForCreate(userSession)";
					habilitacionAdapterVO = EspServiceLocator.getHabilitacionService().getHabilitacionAdapterForCreate(userSession);
					actionForward = mapping.findForward(EspConstants.FWD_HABILITACION_ENC_EDIT_ADAPTER);
				}
				
				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (habilitacionAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + habilitacionAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, HabilitacionAdapter.ENC_NAME, habilitacionAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				habilitacionAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + HabilitacionAdapter.ENC_NAME + ": "+ habilitacionAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(HabilitacionAdapter.ENC_NAME, habilitacionAdapterVO);
				// Subo el apdater al userSession
				userSession.put(HabilitacionAdapter.ENC_NAME, habilitacionAdapterVO);
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, HabilitacionAdapter.ENC_NAME);
			}
		}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping, 
				EspSecurityConstants.ABM_HABILITACION_ENC, BaseSecurityConstants.AGREGAR);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				HabilitacionAdapter habilitacionAdapterVO = (HabilitacionAdapter) userSession.get(HabilitacionAdapter.ENC_NAME);
				
				// Si es nulo no se puede continuar
				if (habilitacionAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + HabilitacionAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, HabilitacionAdapter.ENC_NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(habilitacionAdapterVO, request);
				
	            // Tiene errores recuperables
				if (habilitacionAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + habilitacionAdapterVO.infoString()); 
					saveDemodaErrors(request, habilitacionAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, HabilitacionAdapter.ENC_NAME, habilitacionAdapterVO);
				}
				// llamada al servicio para generar advertencia
				habilitacionAdapterVO = EspServiceLocator.getHabilitacionService().verificarHabilitacion(userSession, habilitacionAdapterVO);
				
				// Tiene alguna advertencia
				if (habilitacionAdapterVO.hasMessage()) {
					saveDemodaMessages(request, habilitacionAdapterVO);		
					return forwardErrorRecoverable(mapping, request, userSession, HabilitacionAdapter.ENC_NAME, habilitacionAdapterVO); // TODO cambiar por un forward
				}
				
				// llamada al servicio para agregar
				HabilitacionVO habilitacionVO = EspServiceLocator.getHabilitacionService().createHabilitacion(userSession, habilitacionAdapterVO.getHabilitacion());
				
	            // Tiene errores recuperables
				if (habilitacionVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + habilitacionVO.infoString()); 
					saveDemodaErrors(request, habilitacionVO);
					return forwardErrorRecoverable(mapping, request, userSession, HabilitacionAdapter.ENC_NAME, habilitacionAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (habilitacionVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + habilitacionVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, HabilitacionAdapter.ENC_NAME, habilitacionAdapterVO);
				}

				return forwardConfirmarOk(mapping, request, funcName, HabilitacionAdapter.ENC_NAME);
									
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, HabilitacionAdapter.ENC_NAME);
			}
	}
	
	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				EspSecurityConstants.ABM_HABILITACION_ENC, BaseSecurityConstants.MODIFICAR);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				HabilitacionAdapter habilitacionAdapterVO = (HabilitacionAdapter) userSession.get(HabilitacionAdapter.ENC_NAME);
				
				// Si es nulo no se puede continuar
				if (habilitacionAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + HabilitacionAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, HabilitacionAdapter.ENC_NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(habilitacionAdapterVO, request);
				
	            // Tiene errores recuperables
				if (habilitacionAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + habilitacionAdapterVO.infoString()); 
					saveDemodaErrors(request, habilitacionAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, HabilitacionAdapter.ENC_NAME, habilitacionAdapterVO);
				}
				
				// llamada al servicio
				HabilitacionVO habilitacionVO = EspServiceLocator.getHabilitacionService().updateHabilitacion(userSession, habilitacionAdapterVO.getHabilitacion());
				
	            // Tiene errores recuperables
				if (habilitacionVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + habilitacionAdapterVO.infoString()); 
					saveDemodaErrors(request, habilitacionVO);
					return forwardErrorRecoverable(mapping, request, userSession, HabilitacionAdapter.ENC_NAME, habilitacionAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (habilitacionVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + habilitacionAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, HabilitacionAdapter.ENC_NAME, habilitacionAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, HabilitacionAdapter.ENC_NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, HabilitacionAdapter.ENC_NAME);
			}
	}

	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, HabilitacionAdapter.ENC_NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, HabilitacionAdapter.ENC_NAME);
	}

	public ActionForward paramTipoHab(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				HabilitacionAdapter habilitacionAdapterVO = (HabilitacionAdapter) userSession.get(HabilitacionAdapter.ENC_NAME);
		
				// Si es nulo no se puede continuar
				if (habilitacionAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + HabilitacionAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, HabilitacionAdapter.ENC_NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(habilitacionAdapterVO, request);
				
	            // Tiene errores recuperables
				if (habilitacionAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + habilitacionAdapterVO.infoString()); 
					saveDemodaErrors(request, habilitacionAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, HabilitacionAdapter.ENC_NAME, habilitacionAdapterVO);
				}
				
				// Llamada al servicio
				habilitacionAdapterVO = EspServiceLocator.getHabilitacionService().getHabilitacionAdapterParamTipoHab(userSession, habilitacionAdapterVO);
				
	            // Tiene errores recuperables
				if (habilitacionAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + habilitacionAdapterVO.infoString()); 
					saveDemodaErrors(request, habilitacionAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, HabilitacionAdapter.ENC_NAME, habilitacionAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (habilitacionAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + habilitacionAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, HabilitacionAdapter.ENC_NAME, habilitacionAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(HabilitacionAdapter.ENC_NAME, habilitacionAdapterVO);
				// Subo el adapter al userSession
				userSession.put(HabilitacionAdapter.ENC_NAME, habilitacionAdapterVO);
				
				return mapping.findForward(EspConstants.FWD_HABILITACION_ENC_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, HabilitacionAdapter.ENC_NAME);
			}
	}
	
	public ActionForward buscarCuenta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());
		
		// seteo la accion y el parametro para volver
		navModel.setPrevAction(mapping.getPath());
		navModel.setPrevActionParameter(BaseConstants.ACT_REFILL);

		// seteo los parametros para cuando oprima seleccionar
		navModel.setSelectAction("/esp/AdministrarEncHabilitacion");
		navModel.setSelectActionParameter("paramCuenta");
		navModel.setAgregarEnSeleccion(false);
		
		HabilitacionAdapter  habilitacionAdapter = (HabilitacionAdapter) userSession.get(HabilitacionAdapter.ENC_NAME);
		
		// Si es nulo no se puede continuar
		if (habilitacionAdapter == null) {
			log.error("error en: "  + funcName + ": " + HabilitacionAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, HabilitacionAdapter.ENC_NAME); 
		}
		
		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(habilitacionAdapter, request);
		
        // Tiene errores recuperables
		if (habilitacionAdapter.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + habilitacionAdapter.infoString()); 
			saveDemodaErrors(request, habilitacionAdapter);
			return forwardErrorRecoverable(mapping, request, userSession, HabilitacionAdapter.ENC_NAME, habilitacionAdapter);
		}
		
		CuentaSearchPage cuentaFiltro = new CuentaSearchPage();
		cuentaFiltro.getCuentaTitular().getCuenta().setRecurso(habilitacionAdapter.getHabilitacion().getRecurso());
		cuentaFiltro.getCuentaTitular().getCuenta().setNumeroCuenta(habilitacionAdapter.getHabilitacion().getCuenta().getNumeroCuenta());
		
		navModel.putParameter(BuscarCuentaDAction.CUENTA_SEARCHPAGE_FILTRO, cuentaFiltro);

		// seteo el act a ejecutar en el accion al cual me dirijo		
		navModel.setAct(BaseConstants.ACT_SELECCIONAR);
		
		return mapping.findForward(PadConstants.ACTION_BUSCAR_CUENTA);
		
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
			HabilitacionAdapter habilitacionAdapterVO = (HabilitacionAdapter) userSession.get(HabilitacionAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (habilitacionAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + HabilitacionAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, HabilitacionAdapter.ENC_NAME); 
			}

			// Seteo el id selecionado
			NavModel navModel = userSession.getNavModel();
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			habilitacionAdapterVO.getHabilitacion().getCuenta().setId(commonKey.getId());
			
			// llamada al servicio
			habilitacionAdapterVO = EspServiceLocator.getHabilitacionService().getHabilitacionAdapterParamCuenta(userSession, habilitacionAdapterVO);
			
            // Tiene errores recuperables
			if (habilitacionAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + habilitacionAdapterVO.infoString()); 
				saveDemodaErrors(request, habilitacionAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, HabilitacionAdapter.ENC_NAME, habilitacionAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (habilitacionAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + habilitacionAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, HabilitacionAdapter.ENC_NAME, habilitacionAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(HabilitacionAdapter.ENC_NAME, habilitacionAdapterVO);
			// Subo el apdater al userSession
			userSession.put(HabilitacionAdapter.ENC_NAME, habilitacionAdapterVO);
			
			return mapping.findForward(EspConstants.FWD_HABILITACION_ENC_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, HabilitacionAdapter.ENC_NAME);
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
			HabilitacionAdapter habilitacionAdapterVO = (HabilitacionAdapter)userSession.get(HabilitacionAdapter.ENC_NAME);
	
			// Si es nulo no se puede continuar
			if (habilitacionAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + HabilitacionAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, HabilitacionAdapter.ENC_NAME); 
			}
			
			// Reseteamos la persona utilizada como filtro
			habilitacionAdapterVO.getHabilitacion().setPerHab(new PersonaVO());
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(habilitacionAdapterVO, request);
			
            // Tiene errores recuperables solo puede ser por formato en el numero de documento
			if (habilitacionAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + habilitacionAdapterVO.infoString()); 
								
				habilitacionAdapterVO.getHabilitacion().getPerHab().setPersonaBuscada(true);
				habilitacionAdapterVO.getHabilitacion().getPerHab().addRecoverableValueError("El formato del campo N\u00FAmero Doc. es incorrecto");
				
				request.setAttribute(HabilitacionAdapter.ENC_NAME, habilitacionAdapterVO);
				userSession.put(HabilitacionAdapter.ENC_NAME, habilitacionAdapterVO);
				return mapping.findForward(EspConstants.FWD_HABILITACION_ENC_EDIT_ADAPTER);
			}
						
			habilitacionAdapterVO.getHabilitacion().setPerHab(PadServiceLocator.getPadPersonaService().
					getPersonaBySexoyNroDoc(userSession,habilitacionAdapterVO.getHabilitacion().getPerHab()));
			
			if (habilitacionAdapterVO.getHabilitacion().getPerHab().isPersonaEncontrada())
				habilitacionAdapterVO.setPoseeDatosPersona(true);
			else
				habilitacionAdapterVO.setPoseeDatosPersona(false);
			
			// Envio el VO al request
			request.setAttribute(HabilitacionAdapter.ENC_NAME, habilitacionAdapterVO);
			
			userSession.put(HabilitacionAdapter.ENC_NAME, habilitacionAdapterVO);
			
			return mapping.findForward(EspConstants.FWD_HABILITACION_ENC_EDIT_ADAPTER);
			
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, HabilitacionAdapter.ENC_NAME);
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
			HabilitacionAdapter habilitacionAdapterVO = (HabilitacionAdapter)userSession.get(HabilitacionAdapter.ENC_NAME);
	
			// Si es nulo no se puede continuar
			if (habilitacionAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + HabilitacionAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, HabilitacionAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(habilitacionAdapterVO, request);
			
			habilitacionAdapterVO.getHabilitacion().setPerHab(new PersonaVO());
			habilitacionAdapterVO.getHabilitacion().getPerHab().setPersonaBuscada(false);//true);
			habilitacionAdapterVO.getHabilitacion().getPerHab().setPersonaEncontrada(false);
			habilitacionAdapterVO.setPoseeDatosPersona(false);
			
			// Envio el VO al request
			request.setAttribute(HabilitacionAdapter.ENC_NAME, habilitacionAdapterVO);
			
			userSession.put(HabilitacionAdapter.ENC_NAME, habilitacionAdapterVO);
			
			return mapping.findForward(EspConstants.FWD_HABILITACION_ENC_EDIT_ADAPTER);
			
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, HabilitacionAdapter.ENC_NAME);
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
			HabilitacionAdapter habilitacionAdapterVO = (HabilitacionAdapter)userSession.get(HabilitacionAdapter.ENC_NAME);
	
			// Si es nulo no se puede continuar
			if (habilitacionAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + HabilitacionAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, HabilitacionAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(habilitacionAdapterVO, request);
			
            // Tiene errores recuperables
			if (habilitacionAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + habilitacionAdapterVO.infoString()); 
				saveDemodaErrors(request, habilitacionAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, HabilitacionAdapter.ENC_NAME, habilitacionAdapterVO);
			}
			
			return forwardSeleccionar(mapping, request, 
					EspConstants.METHOD_HABILITACION_PARAM_PERSONA, PadConstants.ACTION_BUSCAR_PERSONA, true);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, HabilitacionAdapter.ENC_NAME);
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
			HabilitacionAdapter habilitacionAdapterVO = (HabilitacionAdapter)userSession.get(HabilitacionAdapter.ENC_NAME);
	
			// Si es nulo no se puede continuar
			if (habilitacionAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + HabilitacionAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, HabilitacionAdapter.ENC_NAME); 
			}

			// recupero el id seleccionado por el usuario
			String selectedId =  navModel.getSelectedId();				
			
			// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
			if (StringUtil.isNullOrEmpty(selectedId)) {
				// Envio el VO al request
				request.setAttribute(HabilitacionAdapter.ENC_NAME, habilitacionAdapterVO);
				return mapping.findForward(EspConstants.FWD_HABILITACION_ENC_EDIT_ADAPTER); 
			}

			// llamo al param del servicio
			habilitacionAdapterVO = EspServiceLocator.getHabilitacionService().paramPersonaForAdapter(userSession, habilitacionAdapterVO, new Long(selectedId));

            // Tiene errores recuperables
			if (habilitacionAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + habilitacionAdapterVO.infoString()); 
				saveDemodaErrors(request, habilitacionAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
						HabilitacionAdapter.ENC_NAME, habilitacionAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (habilitacionAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + habilitacionAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
						HabilitacionAdapter.ENC_NAME, habilitacionAdapterVO);
			}
			
			// grabo los mensajes si hubiere
			saveDemodaMessages(request, habilitacionAdapterVO);
			
			if (habilitacionAdapterVO.getHabilitacion().getPerHab().isPersonaEncontrada())
				habilitacionAdapterVO.setPoseeDatosPersona(true);
			else
				habilitacionAdapterVO.setPoseeDatosPersona(false);
	
			// Envio el VO al request
			request.setAttribute(HabilitacionAdapter.ENC_NAME, habilitacionAdapterVO);
			
			userSession.put(HabilitacionAdapter.ENC_NAME, habilitacionAdapterVO);
			
			return mapping.findForward(EspConstants.FWD_HABILITACION_ENC_EDIT_ADAPTER);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, HabilitacionAdapter.ENC_NAME);
		}
	}


	public ActionForward paramLugarEvento(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				HabilitacionAdapter habilitacionAdapterVO = (HabilitacionAdapter) userSession.get(HabilitacionAdapter.ENC_NAME);
		
				// Si es nulo no se puede continuar
				if (habilitacionAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + HabilitacionAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, HabilitacionAdapter.ENC_NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(habilitacionAdapterVO, request);
				
	            // Tiene errores recuperables
				if (habilitacionAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + habilitacionAdapterVO.infoString()); 
					saveDemodaErrors(request, habilitacionAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, HabilitacionAdapter.ENC_NAME, habilitacionAdapterVO);
				}
				
				// Llamada al servicio
				habilitacionAdapterVO = EspServiceLocator.getHabilitacionService().getHabilitacionAdapterParamLugarEvento(userSession, habilitacionAdapterVO);
				
	            // Tiene errores recuperables
				if (habilitacionAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + habilitacionAdapterVO.infoString()); 
					saveDemodaErrors(request, habilitacionAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, HabilitacionAdapter.ENC_NAME, habilitacionAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (habilitacionAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + habilitacionAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, HabilitacionAdapter.ENC_NAME, habilitacionAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(HabilitacionAdapter.ENC_NAME, habilitacionAdapterVO);
				// Subo el adapter al userSession
				userSession.put(HabilitacionAdapter.ENC_NAME, habilitacionAdapterVO);
				
				return mapping.findForward(EspConstants.FWD_HABILITACION_ENC_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, HabilitacionAdapter.ENC_NAME);
			}
	}

	
}
