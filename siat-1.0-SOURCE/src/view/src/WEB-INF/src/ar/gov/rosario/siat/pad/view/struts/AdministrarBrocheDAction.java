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

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.pad.iface.model.BrocheAdapter;
import ar.gov.rosario.siat.pad.iface.model.BrocheVO;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class AdministrarBrocheDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarBrocheDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_BROCHE, act);		
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			BrocheAdapter brocheAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
					stringServicio = "getBrocheAdapterForView(userSession, commonKey)";
					brocheAdapterVO = PadServiceLocator.getDistribucionService().getBrocheAdapterForView
						(userSession, commonKey);
					actionForward = mapping.findForward(PadConstants.FWD_BROCHE_VIEW_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
					stringServicio = "getBrocheAdapterForCreate(userSession, commonKey)";
					brocheAdapterVO = PadServiceLocator.getDistribucionService().getBrocheAdapterForCreate
						(userSession);
					actionForward = mapping.findForward(PadConstants.FWD_BROCHE_EDIT_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
					stringServicio = "getBrocheAdapterForUpdate(userSession, commonKey)";
					brocheAdapterVO = PadServiceLocator.getDistribucionService().getBrocheAdapterForUpdate
						(userSession, commonKey);
					actionForward = mapping.findForward(PadConstants.FWD_BROCHE_EDIT_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
					stringServicio = "getBrocheAdapterForDelete(userSession, commonKey)";
					brocheAdapterVO = PadServiceLocator.getDistribucionService().getBrocheAdapterForView
						(userSession, commonKey);
					brocheAdapterVO.addMessage(BaseError.MSG_ELIMINAR, PadError.BROCHE_LABEL);
					actionForward = mapping.findForward(PadConstants.FWD_BROCHE_VIEW_ADAPTER);					
				}
				if (act.equals(BaseConstants.ACT_ACTIVAR)) {
					stringServicio = "getBrocheAdapterForView(userSession)";
					brocheAdapterVO = PadServiceLocator.getDistribucionService().getBrocheAdapterForView
						(userSession, commonKey);
					brocheAdapterVO.addMessage(BaseError.MSG_ACTIVAR, PadError.BROCHE_LABEL);					
					actionForward = mapping.findForward(PadConstants.FWD_BROCHE_VIEW_ADAPTER);				
				}
				if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
					stringServicio = "getBrocheAdapterForView(userSession)";
					brocheAdapterVO = PadServiceLocator.getDistribucionService().getBrocheAdapterForView
						(userSession, commonKey);
					brocheAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, PadError.BROCHE_LABEL);				
					actionForward = mapping.findForward(PadConstants.FWD_BROCHE_VIEW_ADAPTER);				
				}
				
				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (brocheAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + brocheAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, BrocheAdapter.NAME, brocheAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				brocheAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + 
					BrocheAdapter.NAME + ": " + brocheAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(BrocheAdapter.NAME, brocheAdapterVO);
				// Subo el apdater al userSession
				userSession.put(BrocheAdapter.NAME, brocheAdapterVO);
				
				saveDemodaMessages(request, brocheAdapterVO);			
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, BrocheAdapter.NAME);
			}
		}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping, 
				PadSecurityConstants.ABM_BROCHE, BaseSecurityConstants.AGREGAR);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				BrocheAdapter brocheAdapterVO = (BrocheAdapter) userSession.get(BrocheAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (brocheAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + BrocheAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, BrocheAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(brocheAdapterVO, request);
				
	            // Tiene errores recuperables
				if (brocheAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + brocheAdapterVO.infoString()); 
					saveDemodaErrors(request, brocheAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, BrocheAdapter.NAME, brocheAdapterVO);
				}
				
				// llamada al servicio
				BrocheVO brocheVO = PadServiceLocator.getDistribucionService().createBroche(userSession, brocheAdapterVO.getBroche());
				
	            // Tiene errores recuperables
				if (brocheVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + brocheVO.infoString()); 
					saveDemodaErrors(request, brocheVO);
					return forwardErrorRecoverable(mapping, request, userSession, BrocheAdapter.NAME, brocheAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (brocheVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + brocheVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, BrocheAdapter.NAME, brocheAdapterVO);
				}

				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, BrocheAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, BrocheAdapter.NAME);
			}
	}
	
	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				PadSecurityConstants.ABM_BROCHE, BaseSecurityConstants.MODIFICAR);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				BrocheAdapter brocheAdapterVO = (BrocheAdapter) userSession.get(BrocheAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (brocheAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + BrocheAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, BrocheAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(brocheAdapterVO, request);
				
	            // Tiene errores recuperables
				if (brocheAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + brocheAdapterVO.infoString()); 
					saveDemodaErrors(request, brocheAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, BrocheAdapter.NAME, brocheAdapterVO);
				}
				
				// llamada al servicio
				BrocheVO brocheVO = PadServiceLocator.getDistribucionService().updateBroche(userSession, brocheAdapterVO.getBroche());
				
	            // Tiene errores recuperables
				if (brocheVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + brocheAdapterVO.infoString()); 
					saveDemodaErrors(request, brocheVO);
					return forwardErrorRecoverable(mapping, request, userSession, BrocheAdapter.NAME, brocheAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (brocheVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + brocheAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, BrocheAdapter.NAME, brocheAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, BrocheAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, BrocheAdapter.NAME);
			}
	}

	
	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_BROCHE, 
				BaseSecurityConstants.ELIMINAR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				BrocheAdapter brocheAdapterVO = (BrocheAdapter) userSession.get(BrocheAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (brocheAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + BrocheAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, BrocheAdapter.NAME); 
				}

				// llamada al servicio
				BrocheVO brocheVO = PadServiceLocator.getDistribucionService().deleteBroche
					(userSession, brocheAdapterVO.getBroche());
				
	            // Tiene errores recuperables
				if (brocheVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + brocheAdapterVO.infoString());
					saveDemodaErrors(request, brocheVO);				
					request.setAttribute(BrocheAdapter.NAME, brocheAdapterVO);
					return mapping.findForward(PadConstants.FWD_BROCHE_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (brocheVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + brocheAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, BrocheAdapter.NAME, brocheAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, BrocheAdapter.NAME);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, BrocheAdapter.NAME);
			}
		}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_BROCHE, 
				BaseSecurityConstants.ACTIVAR); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				BrocheAdapter brocheAdapterVO = (BrocheAdapter) userSession.get(BrocheAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (brocheAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + BrocheAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, BrocheAdapter.NAME); 
				}
				
				// llamada al servicio
				BrocheVO brocheVO = PadServiceLocator.getDistribucionService().activarBroche
					(userSession, brocheAdapterVO.getBroche());
				
	            // Tiene errores recuperables
				if (brocheVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + brocheAdapterVO.infoString());
					saveDemodaErrors(request, brocheVO);				
					request.setAttribute(BrocheAdapter.NAME, brocheAdapterVO);
					return mapping.findForward(PadConstants.FWD_BROCHE_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (brocheVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + brocheAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, BrocheAdapter.NAME, brocheAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, BrocheAdapter.NAME);
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, BrocheAdapter.NAME);
			}	
		}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_BROCHE, 
			BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			BrocheAdapter brocheAdapterVO = (BrocheAdapter) userSession.get(BrocheAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (brocheAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + BrocheAdapter.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, BrocheAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(brocheAdapterVO, request);
			
            // Tiene errores recuperables
			if (brocheAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + brocheAdapterVO.infoString()); 
				saveDemodaErrors(request, brocheAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, BrocheAdapter.NAME, brocheAdapterVO);
			}
			
			// llamada al servicio
			BrocheVO brocheVO = PadServiceLocator.getDistribucionService().desactivarBroche
				(userSession, brocheAdapterVO.getBroche());
			
            // Tiene errores recuperables
			if (brocheVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + brocheAdapterVO.infoString());
				saveDemodaErrors(request, brocheVO);				
				request.setAttribute(BrocheAdapter.NAME, brocheAdapterVO);
				return mapping.findForward(PadConstants.FWD_BROCHE_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (brocheVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + brocheAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, BrocheAdapter.NAME, brocheAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, BrocheAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, BrocheAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, BrocheAdapter.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, BrocheAdapter.NAME);
			
	}
	
	public ActionForward paramTipoBroche(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				BrocheAdapter brocheAdapterVO = (BrocheAdapter) userSession.get(BrocheAdapter.NAME);
		
				// Si es nulo no se puede continuar
				if (brocheAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + BrocheAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, BrocheAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(brocheAdapterVO, request);
				
	            // Tiene errores recuperables
				if (brocheAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + brocheAdapterVO.infoString()); 
					saveDemodaErrors(request, brocheAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, BrocheAdapter.NAME, brocheAdapterVO);
				}
				
				// Llamada al servicio
				brocheAdapterVO = PadServiceLocator.getDistribucionService().getBrocheAdapterParamTipoBroche(userSession, brocheAdapterVO);
				
	            // Tiene errores recuperables
				if (brocheAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + brocheAdapterVO.infoString()); 
					saveDemodaErrors(request, brocheAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, BrocheAdapter.NAME, brocheAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (brocheAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + brocheAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, BrocheAdapter.NAME, brocheAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(BrocheAdapter.NAME, brocheAdapterVO);
				// Subo el adapter al userSession
				userSession.put(BrocheAdapter.NAME, brocheAdapterVO);
				
				return mapping.findForward(PadConstants.FWD_BROCHE_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, BrocheAdapter.NAME);
			}
	}
	
}
