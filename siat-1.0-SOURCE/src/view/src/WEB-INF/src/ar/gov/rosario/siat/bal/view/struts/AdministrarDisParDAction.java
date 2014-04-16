//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.bal.iface.model.DisParAdapter;
import ar.gov.rosario.siat.bal.iface.model.DisParVO;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class AdministrarDisParDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarDisParDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_DISPAR, act);		
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			DisParAdapter disParAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
					stringServicio = "getDisParAdapterForView(userSession, commonKey)";
					disParAdapterVO = BalServiceLocator.getDistribucionService().getDisParAdapterForView
						(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_DISPAR_VIEW_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
					stringServicio = "getDisParAdapterForUpdate(userSession, commonKey)";
					disParAdapterVO = BalServiceLocator.getDistribucionService().getDisParAdapterForView
						(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_DISPAR_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
					stringServicio = "getDisParAdapterForDelete(userSession, commonKey)";
					disParAdapterVO = BalServiceLocator.getDistribucionService().getDisParAdapterForView
						(userSession, commonKey);
					disParAdapterVO.addMessage(BaseError.MSG_ELIMINAR, BalError.DISPAR_LABEL);
					actionForward = mapping.findForward(BalConstants.FWD_DISPAR_VIEW_ADAPTER);					
				}
				if (act.equals(BaseConstants.ACT_ACTIVAR)) {
					stringServicio = "getDisParAdapterForView(userSession)";
					disParAdapterVO = BalServiceLocator.getDistribucionService().getDisParAdapterForView
						(userSession, commonKey);
					disParAdapterVO.addMessage(BaseError.MSG_ACTIVAR, BalError.DISPAR_LABEL);					
					actionForward = mapping.findForward(BalConstants.FWD_DISPAR_VIEW_ADAPTER);				
				}
				if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
					stringServicio = "getDisParAdapterForView(userSession)";
					disParAdapterVO = BalServiceLocator.getDistribucionService().getDisParAdapterForView
						(userSession, commonKey);
					disParAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, BalError.DISPAR_LABEL);				
					actionForward = mapping.findForward(BalConstants.FWD_DISPAR_VIEW_ADAPTER);				
				}

				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (disParAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + disParAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, DisParAdapter.NAME, disParAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				disParAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + 
					DisParAdapter.NAME + ": " + disParAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(DisParAdapter.NAME, disParAdapterVO);
				// Subo el apdater al userSession
				userSession.put(DisParAdapter.NAME, disParAdapterVO);
				
				saveDemodaMessages(request, disParAdapterVO);			
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, DisParAdapter.NAME);
			}
		}
	
	public ActionForward modificarEncabezado(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardAdapter(mapping, request, funcName, 
				BalConstants.ACTION_ADMINISTRAR_ENCDISPAR, BaseConstants.ACT_MODIFICAR);

	}
	
	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_DISPAR, 
				BaseSecurityConstants.ELIMINAR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				DisParAdapter disParAdapterVO = (DisParAdapter) userSession.get(DisParAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (disParAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + DisParAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, DisParAdapter.NAME); 
				}

				// llamada al servicio
				DisParVO disParVO = BalServiceLocator.getDistribucionService().deleteDisPar
					(userSession, disParAdapterVO.getDisPar());
				
	            // Tiene errores recuperables
				if (disParVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + disParAdapterVO.infoString());
					saveDemodaErrors(request, disParVO);				
					request.setAttribute(DisParAdapter.NAME, disParAdapterVO);
					return mapping.findForward(BalConstants.FWD_DISPAR_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (disParVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + disParAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, DisParAdapter.NAME, disParAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, DisParAdapter.NAME);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, DisParAdapter.NAME);
			}
		}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_DISPAR, 
				BaseSecurityConstants.ACTIVAR); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				DisParAdapter disParAdapterVO = (DisParAdapter) userSession.get(DisParAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (disParAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + DisParAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, DisParAdapter.NAME); 
				}
				
				// llamada al servicio
				DisParVO disParVO = BalServiceLocator.getDistribucionService().activarDisPar
					(userSession, disParAdapterVO.getDisPar());
				
	            // Tiene errores recuperables
				if (disParVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + disParAdapterVO.infoString());
					saveDemodaErrors(request, disParVO);				
					request.setAttribute(DisParAdapter.NAME, disParAdapterVO);
					return mapping.findForward(BalConstants.FWD_DISPAR_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (disParVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + disParAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, DisParAdapter.NAME, disParAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, DisParAdapter.NAME);
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, DisParAdapter.NAME);
			}	
		}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_DISPAR, 
			BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DisParAdapter disParAdapterVO = (DisParAdapter) userSession.get(DisParAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (disParAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DisParAdapter.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DisParAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(disParAdapterVO, request);
			
            // Tiene errores recuperables
			if (disParAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + disParAdapterVO.infoString()); 
				saveDemodaErrors(request, disParAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, DisParAdapter.NAME, disParAdapterVO);
			}
			
			// llamada al servicio
			DisParVO disParVO = BalServiceLocator.getDistribucionService().desactivarDisPar
				(userSession, disParAdapterVO.getDisPar());
			
            // Tiene errores recuperables
			if (disParVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + disParAdapterVO.infoString());
				saveDemodaErrors(request, disParVO);				
				request.setAttribute(DisParAdapter.NAME, disParAdapterVO);
				return mapping.findForward(BalConstants.FWD_DISPAR_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (disParVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + disParAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DisParAdapter.NAME, disParAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DisParAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DisParAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, DisParAdapter.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, DisParAdapter.NAME);
			
	}
	
	// Metodos relacionados DisParDet

	public ActionForward verDisParDet(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_DISPARDET);

	}

	public ActionForward modificarDisParDet(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_DISPARDET);

	}

	public ActionForward eliminarDisParDet(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_DISPARDET);

	}
	
	public ActionForward agregarDisParDet(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_DISPARDET);
		
	}
	
	public ActionForward paramTipoImporte(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				DisParAdapter disParAdapterVO = (DisParAdapter) userSession.get(DisParAdapter.NAME);
		
				// Si es nulo no se puede continuar
				if (disParAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + DisParAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, DisParAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(disParAdapterVO, request);
				
	            // Tiene errores recuperables
				if (disParAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + disParAdapterVO.infoString()); 
					saveDemodaErrors(request, disParAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, DisParAdapter.NAME, disParAdapterVO);
				}
				
				// Llamada al servicio
				disParAdapterVO = BalServiceLocator.getDistribucionService().paramTipoImporte(userSession, disParAdapterVO);
				
	            // Tiene errores recuperables
				if (disParAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + disParAdapterVO.infoString()); 
					saveDemodaErrors(request, disParAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, DisParAdapter.NAME, disParAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (disParAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + disParAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, DisParAdapter.NAME, disParAdapterVO);
				}

				NavModel navModel = userSession.getNavModel();
				
				navModel.setSelectedId(disParAdapterVO.getDisPar().getId().toString());
				
				// Seteo los valores de navegacion en el adapter
				disParAdapterVO.setValuesFromNavModel(navModel);

				// Envio el VO al request
				request.setAttribute(DisParAdapter.NAME, disParAdapterVO);
				// Subo el adapter al userSession
				userSession.put(DisParAdapter.NAME, disParAdapterVO);
				
				if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
					return mapping.findForward(BalConstants.FWD_DISPAR_VIEW_ADAPTER);
				}else{
					return mapping.findForward(BalConstants.FWD_DISPAR_ADAPTER);
				}	

			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, DisParAdapter.NAME);
			}
	}
	
	public ActionForward paramRecCon(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				DisParAdapter disParAdapterVO = (DisParAdapter) userSession.get(DisParAdapter.NAME);
		
				// Si es nulo no se puede continuar
				if (disParAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + DisParAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, DisParAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(disParAdapterVO, request);
				
	            // Tiene errores recuperables
				if (disParAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + disParAdapterVO.infoString()); 
					saveDemodaErrors(request, disParAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, DisParAdapter.NAME, disParAdapterVO);
				}
				
				// Llamada al servicio
				disParAdapterVO = BalServiceLocator.getDistribucionService().paramRecCon(userSession, disParAdapterVO);
				
	            // Tiene errores recuperables
				if (disParAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + disParAdapterVO.infoString()); 
					saveDemodaErrors(request, disParAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, DisParAdapter.NAME, disParAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (disParAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + disParAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, DisParAdapter.NAME, disParAdapterVO);
				}
				
				NavModel navModel = userSession.getNavModel();
				
				navModel.setSelectedId(disParAdapterVO.getDisPar().getId().toString());
				
				// Seteo los valores de navegacion en el adapter
				disParAdapterVO.setValuesFromNavModel(navModel);
				
				// Envio el VO al request
				request.setAttribute(DisParAdapter.NAME, disParAdapterVO);
				// Subo el apdater al userSession
				userSession.put(DisParAdapter.NAME, disParAdapterVO);
				
				if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
					return mapping.findForward(BalConstants.FWD_DISPAR_VIEW_ADAPTER);
				}else{
					return mapping.findForward(BalConstants.FWD_DISPAR_ADAPTER);
				}			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, DisParAdapter.NAME);
			}
	}

}
