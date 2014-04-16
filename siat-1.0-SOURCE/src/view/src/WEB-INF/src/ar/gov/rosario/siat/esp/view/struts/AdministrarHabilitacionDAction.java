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

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.esp.iface.model.HabilitacionAdapter;
import ar.gov.rosario.siat.esp.iface.model.HabilitacionVO;
import ar.gov.rosario.siat.esp.iface.service.EspServiceLocator;
import ar.gov.rosario.siat.esp.iface.util.EspError;
import ar.gov.rosario.siat.esp.iface.util.EspSecurityConstants;
import ar.gov.rosario.siat.esp.view.util.EspConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;
import coop.tecso.demoda.iface.model.PrintModel;

public class AdministrarHabilitacionDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarHabilitacionDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = canAccess(request, mapping, EspSecurityConstants.ABM_HABILITACION, act);		
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			HabilitacionAdapter habilitacionAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey = null;
				
				if(!StringUtil.isNullOrEmpty(navModel.getSelectedId())){
					commonKey = new CommonKey(navModel.getSelectedId());
				}
				
				if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
					stringServicio = "getHabilitacionAdapterForView(userSession, commonKey)";
					habilitacionAdapterVO = EspServiceLocator.getHabilitacionService().getHabilitacionAdapterForView
						(userSession, commonKey);
					actionForward = mapping.findForward(EspConstants.FWD_HABILITACION_VIEW_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
					stringServicio = "getHabilitacionAdapterForUpdate(userSession, commonKey)";
					habilitacionAdapterVO = EspServiceLocator.getHabilitacionService().getHabilitacionAdapterForView
						(userSession, commonKey);
					actionForward = mapping.findForward(EspConstants.FWD_HABILITACION_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
					stringServicio = "getHabilitacionAdapterForDelete(userSession, commonKey)";
					habilitacionAdapterVO = EspServiceLocator.getHabilitacionService().getHabilitacionAdapterForView
						(userSession, commonKey);
					habilitacionAdapterVO.addMessage(BaseError.MSG_ELIMINAR, EspError.HABILITACION_LABEL);
					actionForward = mapping.findForward(EspConstants.FWD_HABILITACION_VIEW_ADAPTER);					
				}
				if (act.equals(EspConstants.MTD_ADM_ENTVEN)) {
					stringServicio = "getEntVenAdapterForView(userSession)";
					 habilitacionAdapterVO = EspServiceLocator.getHabilitacionService().getHabilitacionAdapterForView(userSession, commonKey);
					 //entVenApdater.passErrorMessages(habilitacionAdapterVO);
					 actionForward = mapping.findForward(EspConstants.FWD_ENTVEN_ADAPTER);				
				}
				if (act.equals(EspConstants.MTD_ADM_ENTVENINT)) {
					stringServicio = "getEntVenAdapterForView(userSession)";
					 habilitacionAdapterVO = EspServiceLocator.getHabilitacionService().getHabilitacionAdapterForView(userSession, commonKey);
					 //entVenApdater.passErrorMessages(habilitacionAdapterVO);
					 actionForward = mapping.findForward(EspConstants.FWD_ENTVEN_ADAPTER);				
				}
				if (act.equals(EspConstants.MTD_CAMBIAR_ESTADO)) {
					stringServicio = "getHabilitacionAdapterForView(userSession)";
					 habilitacionAdapterVO = EspServiceLocator.getHabilitacionService().getHabilitacionAdapterForView(userSession, commonKey);
					 //entVenApdater.passErrorMessages(habilitacionAdapterVO);
					 actionForward = mapping.findForward(EspConstants.FWD_HABILITACION_VIEW_ADAPTER);			
				}
				
				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (habilitacionAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + habilitacionAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, HabilitacionAdapter.NAME, habilitacionAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				habilitacionAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + 
					HabilitacionAdapter.NAME + ": " + habilitacionAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(HabilitacionAdapter.NAME, habilitacionAdapterVO);
				// Subo el apdater al userSession
				userSession.put(HabilitacionAdapter.NAME, habilitacionAdapterVO);
				
				saveDemodaMessages(request, habilitacionAdapterVO);			
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, HabilitacionAdapter.NAME);
			}
		}
	
	public ActionForward modificarEncabezado(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardAdapter(mapping, request, funcName, 
				EspConstants.ACTION_ADMINISTRAR_ENCHABILITACION, BaseConstants.ACT_MODIFICAR);

	}
	
	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, EspSecurityConstants.ABM_HABILITACION, 
				BaseSecurityConstants.ELIMINAR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				HabilitacionAdapter habilitacionAdapterVO = (HabilitacionAdapter) userSession.get(HabilitacionAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (habilitacionAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + HabilitacionAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, HabilitacionAdapter.NAME); 
				}

				// llamada al servicio
				HabilitacionVO habilitacionVO = EspServiceLocator.getHabilitacionService().deleteHabilitacion
					(userSession, habilitacionAdapterVO.getHabilitacion());
				
	            // Tiene errores recuperables
				if (habilitacionVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + habilitacionAdapterVO.infoString());
					saveDemodaErrors(request, habilitacionVO);				
					request.setAttribute(HabilitacionAdapter.NAME, habilitacionAdapterVO);
					return mapping.findForward(EspConstants.FWD_HABILITACION_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (habilitacionVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + habilitacionAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, HabilitacionAdapter.NAME, habilitacionAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, HabilitacionAdapter.NAME);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, HabilitacionAdapter.NAME);
			}
		}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, HabilitacionAdapter.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, HabilitacionAdapter.NAME);
	}
	
	// Metodos relacionados PrecioEvento
	public ActionForward verPrecioEvento(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, EspConstants.ACTION_ADMINISTRAR_PRECIOEVENTO);

	}

	public ActionForward modificarPrecioEvento(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, EspConstants.ACTION_ADMINISTRAR_PRECIOEVENTO);

	}

	public ActionForward eliminarPrecioEvento(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, EspConstants.ACTION_ADMINISTRAR_PRECIOEVENTO);

	}
	
	public ActionForward agregarPrecioEvento(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, EspConstants.ACTION_ADMINISTRAR_PRECIOEVENTO);
		
	}
	
	// Metodos relacionados EntHab

	public ActionForward verEntHab(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, EspConstants.ACTION_ADMINISTRAR_ENTHAB);

	}

	public ActionForward modificarEntHab(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, EspConstants.ACTION_ADMINISTRAR_ENTHAB);

	}

	public ActionForward eliminarEntHab(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, EspConstants.ACTION_ADMINISTRAR_ENTHAB);

	}
	
	public ActionForward agregarEntHab(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, EspConstants.ACTION_ADMINISTRAR_ENTHAB);
		
	}

	// Metodos relacionados EntVen

	public ActionForward verEntVen(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, EspConstants.ACTION_ADMINISTRAR_ENTVEN);

	}

	public ActionForward modificarEntVen(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, EspConstants.ACTION_ADMINISTRAR_ENTVEN);

	}

	public ActionForward eliminarEntVen(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, EspConstants.ACTION_ADMINISTRAR_ENTVEN);

	}
	
	public ActionForward agregarEntVen(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, EspConstants.ACTION_ADMINISTRAR_ENTVEN);
		
	}
	
	public ActionForward anularEntVen(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
			String funcName = DemodaUtil.currentMethodName();
			return baseForwardAdapter(mapping, request, funcName, EspConstants.ACTION_ADMINISTRAR_ENTVEN, EspConstants.MTD_ANULAR);			
	}


	// Metodos relacionados HabExe

	public ActionForward verHabExe(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, EspConstants.ACTION_ADMINISTRAR_HABEXE);

	}

	public ActionForward modificarHabExe(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, EspConstants.ACTION_ADMINISTRAR_HABEXE);

	}

	public ActionForward eliminarHabExe(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, EspConstants.ACTION_ADMINISTRAR_HABEXE);

	}
	
	public ActionForward agregarHabExe(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, EspConstants.ACTION_ADMINISTRAR_HABEXE);
		
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
			HabilitacionAdapter habilitacionAdapter = (HabilitacionAdapter) userSession.get(HabilitacionAdapter.NAME);

			// Si es nulo no se puede continuar
			if (habilitacionAdapter == null) {
				log.error("error en: "  + funcName + ": " + HabilitacionAdapter.NAME + 
				" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, HabilitacionAdapter.NAME); 
			}
			
			// llamada al servicio
				PrintModel print  = EspServiceLocator.getHabilitacionService().imprimirHabilitacion(userSession, habilitacionAdapter);
				
				baseResponsePrintModel(response, print);
				
				return null;
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception,
					HabilitacionAdapter.NAME);
		}
	}

	
	public ActionForward imprimirEntHabSinEntVen(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception {

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
			HabilitacionAdapter habilitacionAdapter = (HabilitacionAdapter) userSession.get(HabilitacionAdapter.NAME);

			// Si es nulo no se puede continuar
			if (habilitacionAdapter == null) {
				log.error("error en: "  + funcName + ": " + HabilitacionAdapter.NAME + 
				" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, HabilitacionAdapter.NAME); 
			}
			
			// llamada al servicio
				PrintModel print  = EspServiceLocator.getHabilitacionService().imprimirEntHabSinEntVen(userSession, habilitacionAdapter);
				
				baseResponsePrintModel(response, print);
				
				return null;
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception,
					HabilitacionAdapter.NAME);
		}
	}


	public ActionForward emisionInicial(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardAdapter(mapping, request, funcName, EspConstants.ACTION_ADMINISTRAR_EMIHABILITACION, EspConstants.MTD_EMISION_INICIAL);
	}
	

	public ActionForward cambiarEstado(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, EspSecurityConstants.ABM_HABILITACION, 
				EspSecurityConstants.CAMBIAR_ESTADO);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				HabilitacionAdapter habilitacionAdapterVO = (HabilitacionAdapter) userSession.get(HabilitacionAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (habilitacionAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + HabilitacionAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, HabilitacionAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(habilitacionAdapterVO, request);
				
	            // Tiene errores recuperables
				if (habilitacionAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + habilitacionAdapterVO.infoString()); 
					saveDemodaErrors(request, habilitacionAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, HabilitacionAdapter.NAME, habilitacionAdapterVO);
				}
				
				// llamada al servicio
				habilitacionAdapterVO = EspServiceLocator.getHabilitacionService().cambiarEstadoHabilitacion(userSession, habilitacionAdapterVO);
				
	            // Tiene errores recuperables
				if (habilitacionAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + habilitacionAdapterVO.infoString());
					saveDemodaErrors(request, habilitacionAdapterVO);				
					request.setAttribute(HabilitacionAdapter.NAME, habilitacionAdapterVO);
					return mapping.findForward(EspConstants.FWD_HABILITACION_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (habilitacionAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + habilitacionAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, HabilitacionAdapter.NAME, habilitacionAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, HabilitacionAdapter.NAME);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, HabilitacionAdapter.NAME);
			}
		}
}
