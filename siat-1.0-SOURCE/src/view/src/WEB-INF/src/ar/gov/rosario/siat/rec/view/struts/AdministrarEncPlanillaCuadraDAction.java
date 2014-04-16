//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.view.struts;

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
import ar.gov.rosario.siat.rec.iface.model.PlanillaCuadraAdapter;
import ar.gov.rosario.siat.rec.iface.model.PlanillaCuadraVO;
import ar.gov.rosario.siat.rec.iface.service.RecServiceLocator;
import ar.gov.rosario.siat.rec.iface.util.RecSecurityConstants;
import ar.gov.rosario.siat.rec.view.util.RecConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarEncPlanillaCuadraDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEncPlanillaCuadraDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_PLANILLACUADRA, act);		
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		PlanillaCuadraAdapter planillaCuadraAdapterVO = null;
		
		String stringServicio = "";
		ActionForward actionForward = null;
		
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());

			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getPlanillaCuadraAdapterForUpdate(userSession, commonKey)";
				planillaCuadraAdapterVO = RecServiceLocator.getCdmService().getPlanillaCuadraAdapterForUpdate
					(userSession, commonKey);
				actionForward = mapping.findForward(RecConstants.FWD_PLANILLACUADRA_ENC_EDIT_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getPlanillaCuadraAdapterForCreate(userSession)";
				planillaCuadraAdapterVO = RecServiceLocator.getCdmService().getPlanillaCuadraAdapterForCreate
					(userSession);
				actionForward = mapping.findForward(RecConstants.FWD_PLANILLACUADRA_ENC_EDIT_ADAPTER);
			}

			if (navModel.getAct().equals(RecConstants.ACT_MODIFICAR_NUMERO_CUADRA)) {
				stringServicio = "getPlanillaCuadraAdapterForUpdate(userSession, commonKey)";
				planillaCuadraAdapterVO = RecServiceLocator.getCdmService().getPlanillaCuadraAdapterForUpdate
					(userSession, commonKey);
				actionForward = mapping.findForward(RecConstants.FWD_PLANILLACUADRA_ENC_EDIT_ADAPTER);				
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );

			// Tiene errores no recuperables
			if (planillaCuadraAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio 
					+ ": " + planillaCuadraAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
					PlanillaCuadraAdapter.ENC_NAME, planillaCuadraAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			planillaCuadraAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + PlanillaCuadraAdapter.ENC_NAME + 
				": "+ planillaCuadraAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(PlanillaCuadraAdapter.ENC_NAME, planillaCuadraAdapterVO);

			// Subo el apdater al userSession
			userSession.put(PlanillaCuadraAdapter.ENC_NAME, planillaCuadraAdapterVO);

			return actionForward;

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanillaCuadraAdapter.ENC_NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, 
			RecSecurityConstants.ABM_PLANILLACUADRA, BaseSecurityConstants.AGREGAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanillaCuadraAdapter planillaCuadraAdapterVO = (PlanillaCuadraAdapter) 
				userSession.get(PlanillaCuadraAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (planillaCuadraAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanillaCuadraAdapter.ENC_NAME 
					+ " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanillaCuadraAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(planillaCuadraAdapterVO, request);
			
            // Tiene errores recuperables
			if (planillaCuadraAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planillaCuadraAdapterVO.infoString()); 
				saveDemodaErrors(request, planillaCuadraAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
					PlanillaCuadraAdapter.ENC_NAME, planillaCuadraAdapterVO);
			}
			
			// llamada al servicio
			PlanillaCuadraVO planillaCuadraVO = RecServiceLocator.getCdmService().createPlanillaCuadra
				(userSession, planillaCuadraAdapterVO.getPlanillaCuadra());
			
            // Tiene errores recuperables
			if (planillaCuadraVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planillaCuadraVO.infoString()); 
				saveDemodaErrors(request, planillaCuadraVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
					PlanillaCuadraAdapter.ENC_NAME, planillaCuadraAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (planillaCuadraVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planillaCuadraVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
					PlanillaCuadraAdapter.ENC_NAME, planillaCuadraAdapterVO);
			}
			
			// lo dirijo al searchPage				
			return forwardConfirmarOk(mapping, request, funcName, PlanillaCuadraAdapter.ENC_NAME);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanillaCuadraAdapter.ENC_NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, 
			RecSecurityConstants.ABM_PLANILLACUADRA, BaseSecurityConstants.MODIFICAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanillaCuadraAdapter planillaCuadraAdapterVO = (PlanillaCuadraAdapter) userSession.get(PlanillaCuadraAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (planillaCuadraAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanillaCuadraAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanillaCuadraAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(planillaCuadraAdapterVO, request);
			
            // Tiene errores recuperables
			if (planillaCuadraAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planillaCuadraAdapterVO.infoString()); 
				saveDemodaErrors(request, planillaCuadraAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanillaCuadraAdapter.ENC_NAME, planillaCuadraAdapterVO);
			}
			
			// llamada al servicio
			PlanillaCuadraVO planillaCuadraVO = RecServiceLocator.getCdmService().updatePlanillaCuadra(userSession, planillaCuadraAdapterVO.getPlanillaCuadra());
			
            // Tiene errores recuperables
			if (planillaCuadraVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planillaCuadraAdapterVO.infoString()); 
				saveDemodaErrors(request, planillaCuadraVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanillaCuadraAdapter.ENC_NAME, planillaCuadraAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (planillaCuadraVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planillaCuadraAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanillaCuadraAdapter.ENC_NAME, planillaCuadraAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanillaCuadraAdapter.ENC_NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanillaCuadraAdapter.ENC_NAME);
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
		PlanillaCuadraAdapter planillaCuadraAdapterVO = 
			(PlanillaCuadraAdapter) userSession.get(PlanillaCuadraAdapter.ENC_NAME);

		// Si es nulo no se puede continuar
		if (planillaCuadraAdapterVO == null) {
			log.error("error en: "  + funcName + ": " + PlanillaCuadraAdapter.ENC_NAME + 
				" IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, PlanillaCuadraAdapter.ENC_NAME); 
		}
		
		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(planillaCuadraAdapterVO, request);

        // Tiene errores recuperables
		if (planillaCuadraAdapterVO.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + planillaCuadraAdapterVO.infoString()); 
			saveDemodaErrors(request, planillaCuadraAdapterVO);
			return mapping.findForward(RecConstants.FWD_PLANILLACUADRA_ENC_EDIT_ADAPTER);
		}

		// el id puede ser 1, 2, o 3.
		// indica si sa va a buscar la calle principal (1),calle desde (2) o calle hasta (3)
		planillaCuadraAdapterVO.setCalle(navModel.getSelectedId());

		return forwardSeleccionar(mapping, request, RecConstants.MTD_PARAM_CALLE, 
			RecConstants.ACTION_BUSCAR_CALLE, false); 
	}
	
	public ActionForward limpiarCalles(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());
		
		// Bajo el adapter del userSession
		PlanillaCuadraAdapter planillaCuadraAdapterVO = 
			(PlanillaCuadraAdapter) userSession.get(PlanillaCuadraAdapter.ENC_NAME);

		// Si es nulo no se puede continuar
		if (planillaCuadraAdapterVO == null) {
			log.error("error en: "  + funcName + ": " + PlanillaCuadraAdapter.ENC_NAME + 
				" IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, PlanillaCuadraAdapter.ENC_NAME); 
		}

		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(planillaCuadraAdapterVO, request);

        // Tiene errores recuperables
		if (planillaCuadraAdapterVO.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + planillaCuadraAdapterVO.infoString()); 
			saveDemodaErrors(request, planillaCuadraAdapterVO);
			return mapping.findForward(RecConstants.FWD_PLANILLACUADRA_ENC_EDIT_ADAPTER);
		}
		
		planillaCuadraAdapterVO = RecServiceLocator.getCdmService().getPlanillaCuadraAdapterLimpiarCalles
			(userSession, planillaCuadraAdapterVO);

        // Tiene errores recuperables
		if (planillaCuadraAdapterVO.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + planillaCuadraAdapterVO.infoString()); 
			saveDemodaErrors(request, planillaCuadraAdapterVO);
			return forwardErrorRecoverable(mapping, request, userSession, 
					PlanillaCuadraAdapter.ENC_NAME, planillaCuadraAdapterVO);
		}

		// Tiene errores no recuperables
		if (planillaCuadraAdapterVO.hasErrorNonRecoverable()) {
			log.error("error en: "  + funcName + ": " + planillaCuadraAdapterVO.errorString()); 
			return forwardErrorNonRecoverable(mapping, request, funcName, 
					PlanillaCuadraAdapter.ENC_NAME, planillaCuadraAdapterVO);
		}

		// grabo los mensajes si hubiere
		saveDemodaMessages(request, planillaCuadraAdapterVO);

		// Envio el VO al request
		request.setAttribute(PlanillaCuadraAdapter.ENC_NAME, planillaCuadraAdapterVO);

		return mapping.findForward(RecConstants.FWD_PLANILLACUADRA_ENC_EDIT_ADAPTER);

	}

	public ActionForward paramCalle(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		NavModel navModel = userSession.getNavModel();
		
		try {
			
			// Bajo el adapter del userSession
			PlanillaCuadraAdapter planillaCuadraAdapterVO = 
				(PlanillaCuadraAdapter) userSession.get(PlanillaCuadraAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (planillaCuadraAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanillaCuadraAdapter.ENC_NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanillaCuadraAdapter.ENC_NAME); 
			}

			// recupero el id seleccionado por el usuario
			String selectedId = navModel.getSelectedId();
			
			// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
			if (StringUtil.isNullOrEmpty(selectedId)) {
				// Envio el VO al request
				request.setAttribute(PlanillaCuadraAdapter.ENC_NAME, planillaCuadraAdapterVO);
				return mapping.findForward(RecConstants.FWD_PLANILLACUADRA_ENC_EDIT_ADAPTER); 
			}

			// Seteo el id seleccionado
			planillaCuadraAdapterVO.setIdCalleSeleccionada(selectedId);

			planillaCuadraAdapterVO = RecServiceLocator.getCdmService().getPlanillaCuadraAdapterParamCalle
				(userSession, planillaCuadraAdapterVO); 

            // Tiene errores recuperables
			if (planillaCuadraAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planillaCuadraAdapterVO.infoString()); 
				saveDemodaErrors(request, planillaCuadraAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
						PlanillaCuadraAdapter.ENC_NAME, planillaCuadraAdapterVO);
			}

			// Tiene errores no recuperables
			if (planillaCuadraAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planillaCuadraAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
						PlanillaCuadraAdapter.ENC_NAME, planillaCuadraAdapterVO);
			}

			// grabo los mensajes si hubiere
			saveDemodaMessages(request, planillaCuadraAdapterVO);

			// Envio el VO al request
			request.setAttribute(PlanillaCuadraAdapter.ENC_NAME, planillaCuadraAdapterVO);

			return mapping.findForward(RecConstants.FWD_PLANILLACUADRA_ENC_EDIT_ADAPTER);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanillaCuadraAdapter.ENC_NAME);
		}
	}

	public ActionForward paramRecurso (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				PlanillaCuadraAdapter planillaCuadraAdapterVO = (PlanillaCuadraAdapter) userSession.get(PlanillaCuadraAdapter.ENC_NAME);
				
				// Si es nulo no se puede continuar
				if (planillaCuadraAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + PlanillaCuadraAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, PlanillaCuadraAdapter.ENC_NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(planillaCuadraAdapterVO, request);
				
	            // Tiene errores recuperables
				if (planillaCuadraAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + planillaCuadraAdapterVO.infoString()); 
					saveDemodaErrors(request, planillaCuadraAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, PlanillaCuadraAdapter.ENC_NAME, planillaCuadraAdapterVO);
				}
				
				// llamada al servicio
				planillaCuadraAdapterVO = RecServiceLocator.getCdmService().getPlanillaCuadraAdapterParamRecurso(userSession, planillaCuadraAdapterVO);
				
	            // Tiene errores recuperables
				if (planillaCuadraAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + planillaCuadraAdapterVO.infoString()); 
					saveDemodaErrors(request, planillaCuadraAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, PlanillaCuadraAdapter.ENC_NAME, planillaCuadraAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (planillaCuadraAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + planillaCuadraAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, PlanillaCuadraAdapter.ENC_NAME, planillaCuadraAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(PlanillaCuadraAdapter.ENC_NAME, planillaCuadraAdapterVO);
				// Subo el apdater al userSession
				userSession.put(PlanillaCuadraAdapter.ENC_NAME, planillaCuadraAdapterVO);
				
				return mapping.findForward(RecConstants.FWD_PLANILLACUADRA_ENC_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, PlanillaCuadraAdapter.ENC_NAME);
			}
		}
	
	public ActionForward paramContrato (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				PlanillaCuadraAdapter planillaCuadraAdapterVO = (PlanillaCuadraAdapter) userSession.get(PlanillaCuadraAdapter.ENC_NAME);
				
				// Si es nulo no se puede continuar
				if (planillaCuadraAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + PlanillaCuadraAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, PlanillaCuadraAdapter.ENC_NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(planillaCuadraAdapterVO, request);
				
	            // Tiene errores recuperables
				if (planillaCuadraAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + planillaCuadraAdapterVO.infoString()); 
					saveDemodaErrors(request, planillaCuadraAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, PlanillaCuadraAdapter.ENC_NAME, planillaCuadraAdapterVO);
				}
				
				// llamada al servicio
				planillaCuadraAdapterVO = RecServiceLocator.getCdmService().getPlanillaCuadraAdapterParamContrato(userSession, planillaCuadraAdapterVO);
				
	            // Tiene errores recuperables
				if (planillaCuadraAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + planillaCuadraAdapterVO.infoString()); 
					saveDemodaErrors(request, planillaCuadraAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, PlanillaCuadraAdapter.ENC_NAME, planillaCuadraAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (planillaCuadraAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + planillaCuadraAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, PlanillaCuadraAdapter.ENC_NAME, planillaCuadraAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(PlanillaCuadraAdapter.ENC_NAME, planillaCuadraAdapterVO);
				// Subo el apdater al userSession
				userSession.put(PlanillaCuadraAdapter.ENC_NAME, planillaCuadraAdapterVO);
				
				return mapping.findForward(RecConstants.FWD_PLANILLACUADRA_ENC_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, PlanillaCuadraAdapter.ENC_NAME);
			}
		}
	
	public ActionForward paramTipoObra (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				PlanillaCuadraAdapter planillaCuadraAdapterVO = (PlanillaCuadraAdapter) userSession.get(PlanillaCuadraAdapter.ENC_NAME);
				
				// Si es nulo no se puede continuar
				if (planillaCuadraAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + PlanillaCuadraAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, PlanillaCuadraAdapter.ENC_NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(planillaCuadraAdapterVO, request);
				
	            // Tiene errores recuperables
				if (planillaCuadraAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + planillaCuadraAdapterVO.infoString()); 
					saveDemodaErrors(request, planillaCuadraAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, PlanillaCuadraAdapter.ENC_NAME, planillaCuadraAdapterVO);
				}
				
				// llamada al servicio
				planillaCuadraAdapterVO = RecServiceLocator.getCdmService().getPlanillaCuadraAdapterParamTipoObra(userSession, planillaCuadraAdapterVO);
				
	            // Tiene errores recuperables
				if (planillaCuadraAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + planillaCuadraAdapterVO.infoString()); 
					saveDemodaErrors(request, planillaCuadraAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, PlanillaCuadraAdapter.ENC_NAME, planillaCuadraAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (planillaCuadraAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + planillaCuadraAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, PlanillaCuadraAdapter.ENC_NAME, planillaCuadraAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(PlanillaCuadraAdapter.ENC_NAME, planillaCuadraAdapterVO);
				// Subo el apdater al userSession
				userSession.put(PlanillaCuadraAdapter.ENC_NAME, planillaCuadraAdapterVO);
				
				return mapping.findForward(RecConstants.FWD_PLANILLACUADRA_ENC_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, PlanillaCuadraAdapter.ENC_NAME);
			}
		}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, PlanillaCuadraAdapter.ENC_NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, PlanillaCuadraAdapter.ENC_NAME);
		
	}
	
}
	
