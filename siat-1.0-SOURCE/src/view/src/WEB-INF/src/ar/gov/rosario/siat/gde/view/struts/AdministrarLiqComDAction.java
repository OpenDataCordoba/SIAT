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
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.gde.iface.model.LiqComAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqComVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarLiqComDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarLiqComDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_LIQCOM, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		LiqComAdapter liqComAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getLiqComAdapterForView(userSession, commonKey)";
				liqComAdapterVO = GdeServiceLocator.getGestionDeudaJudicialService().getLiqComAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_LIQCOM_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getLiqComAdapterForUpdate(userSession, commonKey)";
				liqComAdapterVO = GdeServiceLocator.getGestionDeudaJudicialService().getLiqComAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_LIQCOM_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getLiqComAdapterForView(userSession, commonKey)";
				liqComAdapterVO = GdeServiceLocator.getGestionDeudaJudicialService().getLiqComAdapterForView(userSession, commonKey);				
				liqComAdapterVO.addMessage(BaseError.MSG_ELIMINAR, GdeError.LIQCOM_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_LIQCOM_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getLiqComAdapterForCreate(userSession)";
				liqComAdapterVO = GdeServiceLocator.getGestionDeudaJudicialService().getLiqComAdapterForCreate(userSession);
				actionForward = mapping.findForward(GdeConstants.FWD_LIQCOM_EDIT_ADAPTER);				
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (liqComAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + liqComAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqComAdapter.NAME, liqComAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			liqComAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqComAdapter.NAME + ": "+ liqComAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(LiqComAdapter.NAME, liqComAdapterVO);
			// Subo el apdater al userSession
			userSession.put(LiqComAdapter.NAME, liqComAdapterVO);
			 
			saveDemodaMessages(request, liqComAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqComAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_LIQCOM, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			LiqComAdapter liqComAdapterVO = (LiqComAdapter) userSession.get(LiqComAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (liqComAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + LiqComAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, LiqComAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(liqComAdapterVO, request);
			
            // Tiene errores recuperables
			if (liqComAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqComAdapterVO.infoString()); 
				saveDemodaErrors(request, liqComAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, LiqComAdapter.NAME, liqComAdapterVO);
			}
			
			// llamada al servicio
			LiqComVO liqComVO = GdeServiceLocator.getGestionDeudaJudicialService().createLiqCom(userSession, liqComAdapterVO.getLiqCom());
			
            // Tiene errores recuperables
			if (liqComVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqComVO.infoString()); 
				saveDemodaErrors(request, liqComVO);
				return forwardErrorRecoverable(mapping, request, userSession, LiqComAdapter.NAME, liqComAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (liqComVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + liqComVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqComAdapter.NAME, liqComAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, LiqComAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqComAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_LIQCOM, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			LiqComAdapter liqComAdapterVO = (LiqComAdapter) userSession.get(LiqComAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (liqComAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + LiqComAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, LiqComAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(liqComAdapterVO, request);
			
            // Tiene errores recuperables
			if (liqComAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqComAdapterVO.infoString()); 
				saveDemodaErrors(request, liqComAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, LiqComAdapter.NAME, liqComAdapterVO);
			}
			
			// llamada al servicio
			LiqComVO liqComVO = GdeServiceLocator.getGestionDeudaJudicialService().updateLiqCom(userSession, liqComAdapterVO.getLiqCom());
			
            // Tiene errores recuperables
			if (liqComVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqComAdapterVO.infoString()); 
				saveDemodaErrors(request, liqComVO);
				return forwardErrorRecoverable(mapping, request, userSession, LiqComAdapter.NAME, liqComAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (liqComVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + liqComAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqComAdapter.NAME, liqComAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, LiqComAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqComAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_LIQCOM, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			LiqComAdapter liqComAdapterVO = (LiqComAdapter) userSession.get(LiqComAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (liqComAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + LiqComAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, LiqComAdapter.NAME); 
			}

			// llamada al servicio
			LiqComVO liqComVO = GdeServiceLocator.getGestionDeudaJudicialService().deleteLiqCom
				(userSession, liqComAdapterVO.getLiqCom());
			
            // Tiene errores recuperables
			if (liqComVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqComAdapterVO.infoString());
				saveDemodaErrors(request, liqComVO);				
				request.setAttribute(LiqComAdapter.NAME, liqComAdapterVO);
				return mapping.findForward(GdeConstants.FWD_LIQCOM_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (liqComVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + liqComAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqComAdapter.NAME, liqComAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, LiqComAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqComAdapter.NAME);
		}
	}
	

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, LiqComAdapter.NAME);
		
	}
	
	public ActionForward paramRecurso(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			LiqComAdapter liqComAdapterVO = (LiqComAdapter) userSession.get(LiqComAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (liqComAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + LiqComAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, LiqComAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(liqComAdapterVO, request);
			
            // Tiene errores recuperables
			if (liqComAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqComAdapterVO.infoString()); 
				saveDemodaErrors(request, liqComAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, LiqComAdapter.NAME, liqComAdapterVO);
			}
			
			// llamada al servicio
			liqComAdapterVO = GdeServiceLocator.getGestionDeudaJudicialService().getLiqComAdapterParamProcuradores(userSession, liqComAdapterVO);
			
            // Tiene errores recuperables
			if (liqComAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqComAdapterVO.infoString()); 
				saveDemodaErrors(request, liqComAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, LiqComAdapter.NAME, liqComAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (liqComAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + liqComAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqComAdapter.NAME, liqComAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(LiqComAdapter.NAME, liqComAdapterVO);
			// Subo el apdater al userSession
			userSession.put(LiqComAdapter.NAME, liqComAdapterVO);
			
			return mapping.findForward(GdeConstants.FWD_LIQCOM_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqComAdapter.NAME);
		}
	}

	public ActionForward paramServicioBanco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				LiqComAdapter liqComAdapterVO = (LiqComAdapter) userSession.get(LiqComAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (liqComAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + LiqComAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, LiqComAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(liqComAdapterVO, request);
				
	            // Tiene errores recuperables
				if (liqComAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + liqComAdapterVO.infoString()); 
					saveDemodaErrors(request, liqComAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, LiqComAdapter.NAME, liqComAdapterVO);
				}
				
				// llamada al servicio
				liqComAdapterVO = GdeServiceLocator.getGestionDeudaJudicialService().getLiqComAdapterParamRecurso(userSession, liqComAdapterVO);
				
	            // Tiene errores recuperables
				if (liqComAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + liqComAdapterVO.infoString()); 
					saveDemodaErrors(request, liqComAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, LiqComAdapter.NAME, liqComAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (liqComAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + liqComAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, LiqComAdapter.NAME, liqComAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(LiqComAdapter.NAME, liqComAdapterVO);
				// Subo el apdater al userSession
				userSession.put(LiqComAdapter.NAME, liqComAdapterVO);
				
				return mapping.findForward(GdeConstants.FWD_LIQCOM_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, LiqComAdapter.NAME);
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
			LiqComAdapter adapterVO = (LiqComAdapter)userSession.get(LiqComAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (adapterVO == null) {
				log.error("error en: "  + funcName + ": " + LiqComAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, LiqComAdapter.NAME); 
			}
			
			// Populate como en un buscar
			DemodaUtil.populateVO(adapterVO, request);
			
			log.debug( funcName + " " + adapterVO.getLiqCom().getCaso().infoString());
			
			// llamada al servicio
			CasServiceLocator.getCasCasoService().validarCaso(userSession, adapterVO.getLiqCom()); 
			
			adapterVO.getLiqCom().passErrorMessages(adapterVO);
		    
		    saveDemodaMessages(request, adapterVO);
		    saveDemodaErrors(request, adapterVO);
		    
			request.setAttribute(LiqComAdapter.NAME, adapterVO);
			
			return mapping.findForward(GdeConstants.FWD_LIQCOM_EDIT_ADAPTER); 
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqComAdapter.NAME);
		}	
	}
}
