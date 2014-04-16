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
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.frm.view.util.FrmConstants;
import ar.gov.rosario.siat.gde.iface.model.PlanAdapter;
import ar.gov.rosario.siat.gde.iface.model.PlanVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarEncPlanDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEncPlanDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PLAN_ENC, act);		

		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		PlanAdapter planAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());

			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getPlanAdapterForUpdate(userSession, commonKey)";
				planAdapterVO = GdeServiceLocator.getDefinicionService().getPlanAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PLAN_ENC_EDIT_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getPlanAdapterForCreate(userSession)";
				planAdapterVO = GdeServiceLocator.getDefinicionService().getPlanAdapterForCreate(userSession);
				actionForward = mapping.findForward(GdeConstants.FWD_PLAN_ENC_EDIT_ADAPTER);
			}
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (planAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + planAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanAdapter.ENC_NAME, planAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			planAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + PlanAdapter.ENC_NAME + ": "+ planAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(PlanAdapter.ENC_NAME, planAdapterVO);
			// Subo el apdater al userSession
			userSession.put(PlanAdapter.ENC_NAME, planAdapterVO);

			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanAdapter.ENC_NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, 
			GdeSecurityConstants.ABM_PLAN_ENC, BaseSecurityConstants.AGREGAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanAdapter planAdapterVO = (PlanAdapter) userSession.get(PlanAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (planAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(planAdapterVO, request);
			
            // Tiene errores recuperables
			if (planAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planAdapterVO.infoString()); 
				saveDemodaErrors(request, planAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanAdapter.ENC_NAME, planAdapterVO);
			}
			
			// llamada al servicio
			PlanVO planVO = GdeServiceLocator.getDefinicionService().createPlan(userSession, planAdapterVO.getPlan());
			
            // Tiene errores recuperables
			if (planVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planVO.infoString()); 
				saveDemodaErrors(request, planVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanAdapter.ENC_NAME, planAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (planVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanAdapter.ENC_NAME, planAdapterVO);
			}

			// Si tiene permiso lo dirijo al adapter de modificacion, 
			// sino vuelve al searchPage
			if (hasAccess(userSession, GdeSecurityConstants.ABM_PLAN, 
				BaseSecurityConstants.MODIFICAR)) {
				
				// seteo el id para que lo use el siguiente action 
				userSession.getNavModel().setSelectedId(planVO.getId().toString());

				// lo dirijo al adapter de modificacion
				return forwardConfirmarOk(mapping, request, funcName, PlanAdapter.ENC_NAME, 
					GdeConstants.PATH_ADMINISTRAR_PLAN, BaseConstants.METHOD_INICIALIZAR, 
					BaseConstants.ACT_MODIFICAR);
			} else {
				
				// lo dirijo al searchPage				
				return forwardConfirmarOk(mapping, request, funcName, PlanAdapter.ENC_NAME);
				
			}
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanAdapter.ENC_NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, 
			GdeSecurityConstants.ABM_PLAN_ENC, BaseSecurityConstants.MODIFICAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanAdapter planAdapterVO = (PlanAdapter) userSession.get(PlanAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (planAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(planAdapterVO, request);
			
            // Tiene errores recuperables
			if (planAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planAdapterVO.infoString()); 
				saveDemodaErrors(request, planAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanAdapter.ENC_NAME, planAdapterVO);
			}
			
			// llamada al servicio
			PlanVO planVO = GdeServiceLocator.getDefinicionService().updatePlan(userSession, planAdapterVO.getPlan());
			
            // Tiene errores recuperables
			if (planVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planAdapterVO.infoString()); 
				saveDemodaErrors(request, planVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanAdapter.ENC_NAME, planAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (planVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanAdapter.ENC_NAME, planAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanAdapter.ENC_NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanAdapter.ENC_NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, PlanAdapter.ENC_NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, PlanAdapter.ENC_NAME);
		
	}

	
	public ActionForward buscarFormulario(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			UserSession userSession = getCurrentUserSession(request, mapping);
			if (userSession==null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());

			// Bajo el adapter del userSession
			PlanAdapter planAdapterVO = (PlanAdapter) userSession.get(PlanAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (planAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(planAdapterVO, request);

			return forwardSeleccionar(mapping, request, "paramFormulario", FrmConstants.ACTION_BUSCAR_FORMULARIO, false); 
	}
	
	public ActionForward paramFormulario(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		NavModel navModel = userSession.getNavModel();
		
		try {
			
			//bajo el adapter del usserSession
			PlanAdapter planAdapterVO =  (PlanAdapter) userSession.get(PlanAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (planAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanAdapter.ENC_NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanAdapter.ENC_NAME); 
			}

			// recupero el id seleccionado por el usuario
			String selectedId = navModel.getSelectedId();
			
			// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
			if (StringUtil.isNullOrEmpty(selectedId)) {
				// Envio el VO al request
				request.setAttribute(PlanAdapter.ENC_NAME, planAdapterVO);
				return mapping.findForward( GdeConstants.FWD_PLAN_ENC_EDIT_ADAPTER); 
			}

			// Seteo el id localidad seleccionado
			planAdapterVO.getPlan().getFormulario().setId(new Long(selectedId));
			
			planAdapterVO = GdeServiceLocator.getDefinicionService().getPlanAdapterParamFormulario(userSession, planAdapterVO);
			
			// Tiene errores recuperables
			if (planAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planAdapterVO.infoString()); 
				saveDemodaErrors(request, planAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
						PlanAdapter.ENC_NAME, planAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (planAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
						PlanAdapter.ENC_NAME, planAdapterVO);
			}
			
			// grabo los mensajes si hubiere
			saveDemodaMessages(request, planAdapterVO);
			
			// Envio el VO al request
			request.setAttribute(PlanAdapter.ENC_NAME, planAdapterVO);

			return mapping.findForward(GdeConstants.FWD_PLAN_ENC_EDIT_ADAPTER);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanAdapter.ENC_NAME);
		}
	}


	public ActionForward validarCaso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanAdapter adapterVO = (PlanAdapter)userSession.get(PlanAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (adapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanAdapter.ENC_NAME); 
			}
			
			// Populate como en un buscar
			DemodaUtil.populateVO(adapterVO, request);
			
			log.debug( funcName + " " + adapterVO.getPlan().getCaso().infoString());
			
			// llamada al servicio
			CasServiceLocator.getCasCasoService().validarCaso(userSession, adapterVO.getPlan()); 
			
			adapterVO.getPlan().passErrorMessages(adapterVO);
		    
		    saveDemodaMessages(request, adapterVO);
		    saveDemodaErrors(request, adapterVO);
		    
			request.setAttribute(PlanAdapter.ENC_NAME, adapterVO);
			
			return mapping.findForward( GdeConstants.FWD_PLAN_ENC_EDIT_ADAPTER); 
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanAdapter.ENC_NAME);
		}	
	}	

	
	
	public ActionForward paramEsManual(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
				
		try {
			
			//bajo el adapter del usserSession
			PlanAdapter planAdapterVO = (PlanAdapter) userSession.get(PlanAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (planAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanAdapter.ENC_NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanAdapter.ENC_NAME); 
			}
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(planAdapterVO, request);
			
	        // Tiene errores recuperables
			if (planAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planAdapterVO.infoString()); 
				saveDemodaErrors(request, planAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanAdapter.ENC_NAME, planAdapterVO);
			}

			// grabo los mensajes si hubiere
			saveDemodaErrors(request, planAdapterVO);
			saveDemodaMessages(request, planAdapterVO);
			
			// Envio el VO al request
			request.setAttribute(PlanAdapter.ENC_NAME, planAdapterVO);

			return mapping.findForward(GdeConstants.FWD_PLAN_ENC_EDIT_ADAPTER);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanAdapter.ENC_NAME);
		}
	}
}
	
