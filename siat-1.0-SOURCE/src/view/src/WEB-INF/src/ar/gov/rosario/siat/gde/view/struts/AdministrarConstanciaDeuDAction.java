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
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;
import coop.tecso.demoda.iface.model.PrintModel;

public final class AdministrarConstanciaDeuDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarConstanciaDeuDAction.class);

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
			
			if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
				stringServicio = "getConstanciaDeuAdapterForView(userSession, commonKey)";
				constanciaDeuAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getConstanciaDeuAdapterForView
					(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_CONSTANCIADUE_VIEW_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getConstanciaDeuAdapterForUpdate(userSession, commonKey)";
				constanciaDeuAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getConstanciaDeuAdapterForUpdate
					(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_CONSTANCIADEU_ADAPTER);
			}
			if (navModel.getAct().equals(GdeConstants.ACT_CONSTANCIADEU_HABILITAR)) {
				stringServicio = "getConstanciaDeuAdapterForUpdate(userSession, commonKey)";
				constanciaDeuAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getConstanciaDeuAdapterForView(userSession, commonKey);
				constanciaDeuAdapterVO.addMessage(GdeError.CONSTANCIADEU_MSJ_HABILITAR);
				actionForward = mapping.findForward(GdeConstants.FWD_CONSTANCIADUE_VIEW_ADAPTER);
			}			
			if (navModel.getAct().equals(GdeConstants.ACT_CONSTANCIADEU_RECOMPONER)) {
				stringServicio = "getConstanciaDeuAdapterForUpdate(userSession, commonKey)";
				constanciaDeuAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getConstanciaDeuAdapterForView(userSession, commonKey);
				constanciaDeuAdapterVO.addMessage(GdeError.CONSTANCIADEU_MSJ_RECOMPONER);
				actionForward = mapping.findForward(GdeConstants.FWD_CONSTANCIADUE_VIEW_ADAPTER);
			}
			if (navModel.getAct().equals(GdeConstants.ACT_CONSTANCIADEU_IMPRIMIR)) {
				stringServicio = "getConstanciaDeuAdapterForUpdate(userSession, commonKey)";
				constanciaDeuAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getConstanciaDeuAdapterForView(userSession, commonKey);				
				actionForward = mapping.findForward(GdeConstants.FWD_CONSTANCIADUE_VIEW_ADAPTER);
			}
			if (navModel.getAct().equals(GdeConstants.ACT_CONSTANCIADEU_ANULAR)) {
				stringServicio = "getConstanciaDeuAdapterForUpdate(userSession, commonKey)";
				constanciaDeuAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getConstanciaDeuAdapterForView(userSession, commonKey);
				constanciaDeuAdapterVO.addMessage(GdeError.CONSTANCIADEU_MSJ_ANULAR);
				actionForward = mapping.findForward(GdeConstants.FWD_CONSTANCIADUE_VIEW_ADAPTER);
			}
			if (navModel.getAct().equals(GdeConstants.ACT_CONSTANCIADEU_ELIMINAR)) {
				stringServicio = "getConstanciaDeuAdapterForUpdate(userSession, commonKey)";
				constanciaDeuAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getConstanciaDeuAdapterForView(userSession, commonKey);
				constanciaDeuAdapterVO.addMessage(GdeError.CONSTANCIADEU_MSJ_ELIMINAR);
				actionForward = mapping.findForward(GdeConstants.FWD_CONSTANCIADUE_VIEW_ADAPTER);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (constanciaDeuAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + 
					stringServicio + ": " + constanciaDeuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ConstanciaDeuAdapter.NAME, constanciaDeuAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			constanciaDeuAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + 
				ConstanciaDeuAdapter.NAME + ": " + constanciaDeuAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ConstanciaDeuAdapter.NAME, constanciaDeuAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ConstanciaDeuAdapter.NAME, constanciaDeuAdapterVO);
			
			saveDemodaMessages(request, constanciaDeuAdapterVO);			
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConstanciaDeuAdapter.NAME);
		}
	}

	public ActionForward modificarEncabezado(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardAdapter(mapping, request, funcName, 
			GdeConstants.ACTION_ADMINISTRAR_ENC_CONSTANCIADUE, BaseConstants.ACT_MODIFICAR);

	}

	public ActionForward modificarDomicilio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardAdapter(mapping, request, funcName, 
				GdeConstants.ACTION_ADMINISTRAR_ENC_CONSTANCIADUE, GdeConstants.ACTION_CONSTANCIADUE_MODIFICAR_DOMICILIO_ENV);
	}

	public ActionForward habilitar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ADM_CONSTANCIA_DEUDA_JUDICIAL, 
				GdeSecurityConstants.MTD_HABILITAR_CONSTANCIA);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				ConstanciaDeuAdapter constanciaDeuAdapterVO = (ConstanciaDeuAdapter) userSession.get(ConstanciaDeuAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (constanciaDeuAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + ConstanciaDeuAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ConstanciaDeuAdapter.NAME); 
				}

				// llamada al servicio
				constanciaDeuAdapterVO  = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().habilitarConstanciaDeu(userSession, constanciaDeuAdapterVO);
				
	            // Tiene errores recuperables
				if (constanciaDeuAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + constanciaDeuAdapterVO.infoString()); 
					saveDemodaErrors(request, constanciaDeuAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, ConstanciaDeuAdapter.NAME, constanciaDeuAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (constanciaDeuAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + constanciaDeuAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, ConstanciaDeuAdapter.NAME, constanciaDeuAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, ConstanciaDeuAdapter.NAME);							

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ConstanciaDeuAdapter.NAME);
			}
		}
	
	public ActionForward irRecomponer(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ADM_CONSTANCIA_DEUDA_JUDICIAL, 
				GdeSecurityConstants.MTD_RECOMPONER_CONSTANCIA);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				ConstanciaDeuAdapter constanciaDeuAdapterVO = (ConstanciaDeuAdapter) userSession.get(ConstanciaDeuAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (constanciaDeuAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + ConstanciaDeuAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ConstanciaDeuAdapter.NAME); 
				}

				// Envio el VO al request
				request.setAttribute(ConstanciaDeuAdapter.NAME, constanciaDeuAdapterVO);
				// Subo el apdater al userSession
				userSession.put(ConstanciaDeuAdapter.NAME, constanciaDeuAdapterVO);

				return mapping.findForward(GdeConstants.ACT_CONSTANCIADEU_IMPRIMIR);							
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ConstanciaDeuAdapter.NAME);
			}
		}
	
	public ActionForward irImprimir(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ADM_CONSTANCIA_DEUDA_JUDICIAL, 
				GdeSecurityConstants.MTD_IMPRIMIR_CONSTANCIA);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				ConstanciaDeuAdapter constanciaDeuAdapterVO = (ConstanciaDeuAdapter) userSession.get(ConstanciaDeuAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (constanciaDeuAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + ConstanciaDeuAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ConstanciaDeuAdapter.NAME); 
				}

				// Envio el VO al request
				request.setAttribute(ConstanciaDeuAdapter.NAME, constanciaDeuAdapterVO);
				// Subo el apdater al userSession
				userSession.put(ConstanciaDeuAdapter.NAME, constanciaDeuAdapterVO);

				return mapping.findForward(GdeConstants.ACT_CONSTANCIADEU_IMPRIMIR);							
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ConstanciaDeuAdapter.NAME);
			}
		}
	public ActionForward recomponer(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ADM_CONSTANCIA_DEUDA_JUDICIAL, 
				GdeSecurityConstants.MTD_RECOMPONER_CONSTANCIA);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				ConstanciaDeuAdapter constanciaDeuAdapterVO = (ConstanciaDeuAdapter) userSession.get(ConstanciaDeuAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (constanciaDeuAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + ConstanciaDeuAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ConstanciaDeuAdapter.NAME); 
				}

				// llamada al servicio
				PrintModel print  = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().recomponerConstanciaDeu(userSession, constanciaDeuAdapterVO);
				
	            // Tiene errores recuperables
				if (constanciaDeuAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + constanciaDeuAdapterVO.infoString()); 
					saveDemodaErrors(request, constanciaDeuAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, ConstanciaDeuAdapter.NAME, constanciaDeuAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (constanciaDeuAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + constanciaDeuAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, ConstanciaDeuAdapter.NAME, constanciaDeuAdapterVO);
				}
				
				baseResponsePrintModel(response, print);
				return null;							
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ConstanciaDeuAdapter.NAME);
			}
		}
	
	public ActionForward anular(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ADM_CONSTANCIA_DEUDA_JUDICIAL, 
																GdeSecurityConstants.MTD_ANULAR_CONSTANCIA);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				ConstanciaDeuAdapter constanciaDeuAdapterVO = (ConstanciaDeuAdapter) userSession.get(ConstanciaDeuAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (constanciaDeuAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + ConstanciaDeuAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ConstanciaDeuAdapter.NAME); 
				}

				// llamada al servicio
				ConstanciaDeuVO constanciaDeuVO = constanciaDeuAdapterVO.getConstanciaDeu();
				constanciaDeuVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().anularConstanciaDeu(userSession, constanciaDeuVO);
				
	            // Tiene errores recuperables
				if (constanciaDeuVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + constanciaDeuAdapterVO.infoString()); 
					saveDemodaErrors(request, constanciaDeuAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, ConstanciaDeuAdapter.NAME, constanciaDeuAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (constanciaDeuVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + constanciaDeuAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, ConstanciaDeuAdapter.NAME, constanciaDeuAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, ConstanciaDeuAdapter.NAME);							
							
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ConstanciaDeuAdapter.NAME);
			}
	}
	
	public ActionForward imprimir(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ADM_CONSTANCIA_DEUDA_JUDICIAL, 
				BaseSecurityConstants.VER);

		if (userSession == null)
			return forwardErrorSession(request);
		String stringServicio = "imprimir";
		try {
			// Bajo el adapter del userSession
			ConstanciaDeuAdapter constanciaDeuAdapterVO = (ConstanciaDeuAdapter) userSession.get(ConstanciaDeuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (constanciaDeuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ConstanciaDeuAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ConstanciaDeuAdapter.NAME); 
			}

			// llamada al servicio
			PrintModel print  = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().imprimirConstanciaDeu(userSession, constanciaDeuAdapterVO);
			
            // Tiene errores recuperables
			if (constanciaDeuAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + constanciaDeuAdapterVO.infoString()); 
				saveDemodaErrors(request, constanciaDeuAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ConstanciaDeuAdapter.NAME, constanciaDeuAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (constanciaDeuAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + constanciaDeuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ConstanciaDeuAdapter.NAME, constanciaDeuAdapterVO);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ConstanciaDeuAdapter.NAME + ": "+ constanciaDeuAdapterVO.infoString());
			baseResponsePrintModel(response, print);
			return null;
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception,
					ConstanciaDeuAdapter.NAME);
		}
	}
	
	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ADM_CONSTANCIA_DEUDA_JUDICIAL, 
				BaseSecurityConstants.ELIMINAR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				ConstanciaDeuAdapter constanciaDeuAdapterVO = (ConstanciaDeuAdapter) userSession.get(ConstanciaDeuAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (constanciaDeuAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + ConstanciaDeuAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ConstanciaDeuAdapter.NAME); 
				}

				// llamada al servicio
				ConstanciaDeuVO constanciaDeuVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().deleteConstanciaDeu(userSession, constanciaDeuAdapterVO.getConstanciaDeu());
				
	            // Tiene errores recuperables
				if (constanciaDeuVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + constanciaDeuVO.infoString()); 
					saveDemodaErrors(request, constanciaDeuVO);
					return forwardErrorRecoverable(mapping, request, userSession, ConstanciaDeuAdapter.NAME, constanciaDeuAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (constanciaDeuVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + constanciaDeuVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, ConstanciaDeuAdapter.NAME, constanciaDeuAdapterVO);
				}
				
				return forwardConfirmarOk(mapping, request, funcName, ConstanciaDeuAdapter.NAME);							
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ConstanciaDeuAdapter.NAME);
			}
	}
	
	// Metodos para ABM de titulares
	public ActionForward verTitular(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMIN_CONDEUTIT);		
	}

	public ActionForward agregarTitular(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		return forwardSeleccionar(mapping, request, GdeConstants.METOD_PARAM_PERSONA, PadConstants.ACTION_BUSCAR_PERSONA, true);		
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
				ConstanciaDeuAdapter constanciaDeuAdapterVO =  (ConstanciaDeuAdapter) userSession.get(ConstanciaDeuAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (constanciaDeuAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + ConstanciaDeuAdapter.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ConstanciaDeuAdapter.NAME); 
				}

				// recupero el id seleccionado por el usuario
				String selectedId = navModel.getSelectedId();
				
				// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
				if (StringUtil.isNullOrEmpty(selectedId)) {
					// Envio el VO al request
					request.setAttribute(ConstanciaDeuAdapter.NAME, constanciaDeuAdapterVO);
					return mapping.findForward(GdeConstants.FWD_CONSTANCIADEU_ADAPTER); 
				}
				
				// llamo al param del servicio
				constanciaDeuAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().agregarConDeuTit(userSession, constanciaDeuAdapterVO, new Long(selectedId));

	            // Tiene errores recuperables
				if (constanciaDeuAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + constanciaDeuAdapterVO.infoString()); 
					saveDemodaErrors(request, constanciaDeuAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
							ConstanciaDeuAdapter.NAME, constanciaDeuAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (constanciaDeuAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + constanciaDeuAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
							ConstanciaDeuAdapter.NAME, constanciaDeuAdapterVO);
				}

				// grabo los mensajes si hubiere
				saveDemodaMessages(request, constanciaDeuAdapterVO);

				// Envio el VO al request
				request.setAttribute(ConstanciaDeuAdapter.NAME, constanciaDeuAdapterVO);
				// Subo el apdater al userSession
				userSession.put(ConstanciaDeuAdapter.NAME, constanciaDeuAdapterVO);

				return forwardConfirmarOk(mapping, request, funcName, null, GdeConstants.PATH_ADMINISTRAR_CONSTANCIADUE, BaseConstants.ACT_REFILL, BaseConstants.ACT_MODIFICAR);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ConstanciaDeuAdapter.NAME);
			}
	}
	
	public ActionForward eliminarTitular(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMIN_CONDEUTIT);		
	}
	// FIN Metodos para ABM de titulares	
	
	
	// Metodos para ver los historicos
	public ActionForward verHistorico(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ADM_CONSTANCIA_DEUDA_JUDICIAL, 
			BaseSecurityConstants.VER);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ConstanciaDeuAdapter constanciaDeuAdapterVO = (ConstanciaDeuAdapter) userSession.get(ConstanciaDeuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (constanciaDeuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ConstanciaDeuAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ConstanciaDeuAdapter.NAME); 
			}

			// no se llama a ningun servicio
			
			constanciaDeuAdapterVO.setAct(GdeConstants.ACTION_ADMINISTRAR_CONSTANCIADUE_VER_HISTORICO);
			
			// Envio el VO al request
			request.setAttribute(ConstanciaDeuAdapter.NAME, constanciaDeuAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ConstanciaDeuAdapter.NAME, constanciaDeuAdapterVO);

			return forwardVerAdapter(mapping, request, funcName, GdeConstants.FWD_CONSTANCIADUE_VIEW_ADAPTER);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConstanciaDeuAdapter.NAME);
		}
	}

	public ActionForward volverDeHistorico(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ADM_CONSTANCIA_DEUDA_JUDICIAL, 
			BaseSecurityConstants.VER);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ConstanciaDeuAdapter constanciaDeuAdapterVO = (ConstanciaDeuAdapter) userSession.get(ConstanciaDeuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (constanciaDeuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ConstanciaDeuAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ConstanciaDeuAdapter.NAME); 
			}

			// Se setea la act para volver a la pantalla del VER
			constanciaDeuAdapterVO.setAct(BaseConstants.ACT_VER);
			
			// Envio el VO al request
			request.setAttribute(ConstanciaDeuAdapter.NAME, constanciaDeuAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ConstanciaDeuAdapter.NAME, constanciaDeuAdapterVO);
			
			userSession.getNavModel().setAct(BaseConstants.ACT_VER);
			
			return refill(mapping, form, request, response);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConstanciaDeuAdapter.NAME);
		}
	}
	// FIN Metodos para ver los historicos
	
	
	// Metodos para ABM de ConDeuDet
	public ActionForward verConDeuDet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, GdeConstants.ACTION_ABM_CONDEUDET);		
	}
	
	public ActionForward modificarConDeuDet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, GdeConstants.ACTION_ABM_CONDEUDET);		
	}
	
	public ActionForward agregarConDeuDet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, GdeConstants.ACTION_ABM_CONDEUDET);		
	}

	public ActionForward eliminarConDeuDet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, GdeConstants.ACTION_ABM_CONDEUDET);		
	}
	
	// FIN Metodos para ABM de ConDeuDet	
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ConstanciaDeuAdapter.NAME);
	}
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, ConstanciaDeuAdapter.NAME);
		
	}

}
	
