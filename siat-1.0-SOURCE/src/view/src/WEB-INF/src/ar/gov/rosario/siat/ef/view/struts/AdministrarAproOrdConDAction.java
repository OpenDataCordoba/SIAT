//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.view.struts;

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
import ar.gov.rosario.siat.ef.iface.model.AproOrdConAdapter;
import ar.gov.rosario.siat.ef.iface.model.AproOrdConVO;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfError;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarAproOrdConDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarAproOrdConDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_APROORDCON, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		AproOrdConAdapter aproOrdConAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getAproOrdConAdapterForView(userSession, commonKey)";
				aproOrdConAdapterVO = EfServiceLocator.getFiscalizacionService().getAproOrdConAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_APROORDCON_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getAproOrdConAdapterForUpdate(userSession, commonKey)";
				aproOrdConAdapterVO = EfServiceLocator.getFiscalizacionService().getAproOrdConAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_APROORDCON_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getAproOrdConAdapterForView(userSession, commonKey)";
				aproOrdConAdapterVO = EfServiceLocator.getFiscalizacionService().getAproOrdConAdapterForView(userSession, commonKey);				
				aproOrdConAdapterVO.addMessage(BaseError.MSG_ELIMINAR, EfError.APROORDCON_LABEL);
				actionForward = mapping.findForward(EfConstants.FWD_APROORDCON_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getAproOrdConAdapterForCreate(userSession)";
				aproOrdConAdapterVO = EfServiceLocator.getFiscalizacionService().getAproOrdConAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_APROORDCON_EDIT_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (aproOrdConAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + aproOrdConAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AproOrdConAdapter.NAME, aproOrdConAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			aproOrdConAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + AproOrdConAdapter.NAME + ": "+ aproOrdConAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(AproOrdConAdapter.NAME, aproOrdConAdapterVO);
			// Subo el apdater al userSession
			userSession.put(AproOrdConAdapter.NAME, aproOrdConAdapterVO);
			 
			saveDemodaMessages(request, aproOrdConAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AproOrdConAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_APROORDCON, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AproOrdConAdapter aproOrdConAdapterVO = (AproOrdConAdapter) userSession.get(AproOrdConAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (aproOrdConAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AproOrdConAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AproOrdConAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(aproOrdConAdapterVO, request);
			
            // Tiene errores recuperables
			if (aproOrdConAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + aproOrdConAdapterVO.infoString()); 
				saveDemodaErrors(request, aproOrdConAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, AproOrdConAdapter.NAME, aproOrdConAdapterVO);
			}
			
			// llamada al servicio
			AproOrdConVO aproOrdConVO = EfServiceLocator.getFiscalizacionService().createAproOrdCon(userSession, aproOrdConAdapterVO.getAproOrdCon());
			
            // Tiene errores recuperables
			if (aproOrdConVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + aproOrdConVO.infoString()); 
				saveDemodaErrors(request, aproOrdConVO);
				return forwardErrorRecoverable(mapping, request, userSession, AproOrdConAdapter.NAME, aproOrdConAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (aproOrdConVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + aproOrdConVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AproOrdConAdapter.NAME, aproOrdConAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AproOrdConAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AproOrdConAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_APROORDCON, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AproOrdConAdapter aproOrdConAdapterVO = (AproOrdConAdapter) userSession.get(AproOrdConAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (aproOrdConAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AproOrdConAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AproOrdConAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(aproOrdConAdapterVO, request);
			
            // Tiene errores recuperables
			if (aproOrdConAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + aproOrdConAdapterVO.infoString()); 
				saveDemodaErrors(request, aproOrdConAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, AproOrdConAdapter.NAME, aproOrdConAdapterVO);
			}
			
			// llamada al servicio
			AproOrdConVO aproOrdConVO = EfServiceLocator.getFiscalizacionService().updateAproOrdCon(userSession, aproOrdConAdapterVO.getAproOrdCon());
			
            // Tiene errores recuperables
			if (aproOrdConVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + aproOrdConAdapterVO.infoString()); 
				saveDemodaErrors(request, aproOrdConVO);
				return forwardErrorRecoverable(mapping, request, userSession, AproOrdConAdapter.NAME, aproOrdConAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (aproOrdConVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + aproOrdConAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AproOrdConAdapter.NAME, aproOrdConAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AproOrdConAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AproOrdConAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_APROORDCON, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AproOrdConAdapter aproOrdConAdapterVO = (AproOrdConAdapter) userSession.get(AproOrdConAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (aproOrdConAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AproOrdConAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AproOrdConAdapter.NAME); 
			}

			// llamada al servicio
			AproOrdConVO aproOrdConVO = EfServiceLocator.getFiscalizacionService().deleteAproOrdCon
				(userSession, aproOrdConAdapterVO.getAproOrdCon());
			
            // Tiene errores recuperables
			if (aproOrdConVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + aproOrdConAdapterVO.infoString());
				saveDemodaErrors(request, aproOrdConVO);				
				request.setAttribute(AproOrdConAdapter.NAME, aproOrdConAdapterVO);
				return mapping.findForward(EfConstants.FWD_APROORDCON_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (aproOrdConVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + aproOrdConAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AproOrdConAdapter.NAME, aproOrdConAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AproOrdConAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AproOrdConAdapter.NAME);
		}
	}
	
//	public ActionForward emitirAjustes(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception{
//		String funcName = DemodaUtil.currentMethodName();
//		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
//		
//		UserSession userSession = canAccess(request, mapping,
//			GdeSecurityConstants.ABM_COBRANZA, BaseSecurityConstants.EMITIR); 
//		if (userSession==null) return forwardErrorSession(request);
//		
//		try {
//			// Bajo el adapter del userSession
//			AproOrdConAdapter aproOrdConAdapter = (AproOrdConAdapter) userSession.get(AproOrdConAdapter.NAME);
//			
//			// Si es nulo no se puede continuar
//			if (aproOrdConAdapter == null) {
//				log.error("error en: "  + funcName + ": " + AproOrdConAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
//				return forwardErrorSessionNullObject(mapping, request, funcName, AproOrdConAdapter.NAME); 
//			}
//			DemodaUtil.populateVO(aproOrdConAdapter, request);
//			
//			// llamada al servicio
//			aproOrdConAdapter = EfServiceLocator.getFiscalizacionService().createEmisionAjustes(userSession, aproOrdConAdapter);
//            // Tiene errores recuperables
//			if (aproOrdConAdapter.hasErrorRecoverable()) {
//				log.error("recoverable error en: "  + funcName + ": " + aproOrdConAdapter.infoString()); 
//				saveDemodaErrors(request, aproOrdConAdapter);
//				return forwardErrorRecoverable(mapping, request, userSession, AproOrdConAdapter.NAME, aproOrdConAdapter);
//			}
//			
//			// Tiene errores no recuperables
//			if (aproOrdConAdapter.hasErrorNonRecoverable()) {
//				log.error("error en: "  + funcName + ": " + aproOrdConAdapter.errorString()); 
//				return forwardErrorNonRecoverable(mapping, request, funcName, AproOrdConAdapter.NAME, aproOrdConAdapter);
//			}
//			
//			aproOrdConAdapter.setPrevAction(EfConstants.FWD_APROORDCON_EDIT_ADAPTER);
//			aproOrdConAdapter.setPrevActionParameter(BaseConstants.ACT_REFILL);
//			aproOrdConAdapter.setSelectedId(aproOrdConAdapter.getCobranza().getId().toString());
//			
//			userSession.put(AproOrdConAdapter.NAME, aproOrdConAdapter);
//			
//			// Fue Exitoso
//			return forwardConfirmarOk(mapping, request, funcName, AproOrdConAdapter.NAME);
//			
//		} catch (Exception exception) {
//			return baseException(mapping, request, funcName, exception, AproOrdConAdapter.NAME);
//		}
//	}
	
	public ActionForward paramEstadoOrden (ActionMapping mapping, ActionForm form,	HttpServletRequest request, HttpServletResponse response) throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				AproOrdConAdapter aproOrdConAdapterVO = (AproOrdConAdapter) userSession.get(AproOrdConAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (aproOrdConAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + AproOrdConAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, AproOrdConAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(aproOrdConAdapterVO, request);
				
	            // Tiene errores recuperables
				if (aproOrdConAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + aproOrdConAdapterVO.infoString()); 
					saveDemodaErrors(request, aproOrdConAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, AproOrdConAdapter.NAME, aproOrdConAdapterVO);
				}
				
				// llamada al servicio
				aproOrdConAdapterVO = EfServiceLocator.getFiscalizacionService().getAproOrdConAdapterParamEstado(userSession, aproOrdConAdapterVO);
				
	            // Tiene errores recuperables
				if (aproOrdConAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + aproOrdConAdapterVO.infoString()); 
					saveDemodaErrors(request, aproOrdConAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, AproOrdConAdapter.NAME, aproOrdConAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (aproOrdConAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + aproOrdConAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, AproOrdConAdapter.NAME, aproOrdConAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(AproOrdConAdapter.NAME, aproOrdConAdapterVO);
				// Subo el apdater al userSession
				userSession.put(AproOrdConAdapter.NAME, aproOrdConAdapterVO);
				
				return mapping.findForward(EfConstants.FWD_APROORDCON_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, AproOrdConAdapter.NAME);
			}
	}
	
	public ActionForward paramAplicarAjuste (ActionMapping mapping, ActionForm form,	HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AproOrdConAdapter aproOrdConAdapterVO = (AproOrdConAdapter) userSession.get(AproOrdConAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (aproOrdConAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AproOrdConAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AproOrdConAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(aproOrdConAdapterVO, request);
			
            // Tiene errores recuperables
			if (aproOrdConAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + aproOrdConAdapterVO.infoString()); 
				saveDemodaErrors(request, aproOrdConAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, AproOrdConAdapter.NAME, aproOrdConAdapterVO);
			}
			
			// llamada al servicio
			aproOrdConAdapterVO = EfServiceLocator.getFiscalizacionService().getAproOrdConAdapterParamAjuste(userSession, aproOrdConAdapterVO);
			
            // Tiene errores recuperables
			if (aproOrdConAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + aproOrdConAdapterVO.infoString()); 
				saveDemodaErrors(request, aproOrdConAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, AproOrdConAdapter.NAME, aproOrdConAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (aproOrdConAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + aproOrdConAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AproOrdConAdapter.NAME, aproOrdConAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(AproOrdConAdapter.NAME, aproOrdConAdapterVO);
			// Subo el apdater al userSession
			userSession.put(AproOrdConAdapter.NAME, aproOrdConAdapterVO);
			
			return mapping.findForward(EfConstants.FWD_APROORDCON_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AproOrdConAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, AproOrdConAdapter.NAME);
		
	}
	
	public ActionForward validarCaso(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AproOrdConAdapter aproOrdConAdapterVO = (AproOrdConAdapter) userSession.get(AproOrdConAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (aproOrdConAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AproOrdConAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AproOrdConAdapter.NAME); 
			}
			
			// Populate como en un buscar
			DemodaUtil.populateVO(aproOrdConAdapterVO, request);
			
			// llamada al servicio
			CasServiceLocator.getCasCasoService().validarCaso(userSession, aproOrdConAdapterVO.getAproOrdCon().getCobranza()); 
			
			aproOrdConAdapterVO.getAproOrdCon().getCobranza().passErrorMessages(aproOrdConAdapterVO);
		    
		    saveDemodaMessages(request, aproOrdConAdapterVO);
		    saveDemodaErrors(request, aproOrdConAdapterVO);
		    
			request.setAttribute(AproOrdConAdapter.NAME, aproOrdConAdapterVO);
			
			return mapping.findForward(EfConstants.FWD_APROORDCON_EDIT_ADAPTER); 
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AproOrdConAdapter.NAME);
		}	
	}
	
	public ActionForward quitarCaso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AproOrdConAdapter aproOrdConAdapterVO = (AproOrdConAdapter) userSession.get(AproOrdConAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (aproOrdConAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AproOrdConAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AproOrdConAdapter.NAME); 
			}
			
			// Populate como en un buscar
			DemodaUtil.populateVO(aproOrdConAdapterVO, request);
			
			// llamada al servicio
			aproOrdConAdapterVO = EfServiceLocator.getFiscalizacionService().quitarCaso(userSession, aproOrdConAdapterVO); 
			
			aproOrdConAdapterVO.getAproOrdCon().getCobranza().passErrorMessages(aproOrdConAdapterVO);
		    
		    saveDemodaMessages(request, aproOrdConAdapterVO);
		    saveDemodaErrors(request, aproOrdConAdapterVO);
		    
			request.setAttribute(AproOrdConAdapter.NAME, aproOrdConAdapterVO);
			
			return mapping.findForward(EfConstants.FWD_APROORDCON_EDIT_ADAPTER); 
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AproOrdConAdapter.NAME);
		}	
	}
	
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			UserSession userSession = getCurrentUserSession(request, mapping);
			AproOrdConAdapter aproOrdConAdapterVO = (AproOrdConAdapter)userSession.get(AproOrdConAdapter.NAME);
			
			userSession.put(AproOrdConAdapter.NAME, aproOrdConAdapterVO);
			if(aproOrdConAdapterVO != null){
				request.setAttribute(BaseConstants.SELECTED_ID, aproOrdConAdapterVO.getAproOrdCon().getCobranza().getId().toString());
				log.debug("SELECTED_ID: "+aproOrdConAdapterVO.getAproOrdCon().getCobranza().getId());
			}

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, AproOrdConAdapter.NAME);
			
	}
}
