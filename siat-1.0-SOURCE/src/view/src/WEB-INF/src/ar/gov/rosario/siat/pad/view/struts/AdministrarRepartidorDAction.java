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
import ar.gov.rosario.siat.pad.iface.model.RepartidorAdapter;
import ar.gov.rosario.siat.pad.iface.model.RepartidorVO;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class AdministrarRepartidorDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarRepartidorDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_REPARTIDOR, act);		
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			RepartidorAdapter repartidorAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
					stringServicio = "getRepartidorAdapterForView(userSession, commonKey)";
					repartidorAdapterVO = PadServiceLocator.getDistribucionService().getRepartidorAdapterForView
						(userSession, commonKey);
					actionForward = mapping.findForward(PadConstants.FWD_REPARTIDOR_VIEW_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
					stringServicio = "getRepartidorAdapterForUpdate(userSession, commonKey)";
					repartidorAdapterVO = PadServiceLocator.getDistribucionService().getRepartidorAdapterForUpdate
						(userSession, commonKey);
					actionForward = mapping.findForward(PadConstants.FWD_REPARTIDOR_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
					stringServicio = "getRepartidorAdapterForDelete(userSession, commonKey)";
					repartidorAdapterVO = PadServiceLocator.getDistribucionService().getRepartidorAdapterForView
						(userSession, commonKey);
					repartidorAdapterVO.addMessage(BaseError.MSG_ELIMINAR, PadError.REPARTIDOR_LABEL);
					actionForward = mapping.findForward(PadConstants.FWD_REPARTIDOR_VIEW_ADAPTER);					
				}
				if (act.equals(BaseConstants.ACT_ACTIVAR)) {
					stringServicio = "getRepartidorAdapterForView(userSession)";
					repartidorAdapterVO = PadServiceLocator.getDistribucionService().getRepartidorAdapterForView
						(userSession, commonKey);
					repartidorAdapterVO.addMessage(BaseError.MSG_ACTIVAR, PadError.REPARTIDOR_LABEL);					
					actionForward = mapping.findForward(PadConstants.FWD_REPARTIDOR_VIEW_ADAPTER);				
				}
				if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
					stringServicio = "getRepartidorAdapterForView(userSession)";
					repartidorAdapterVO = PadServiceLocator.getDistribucionService().getRepartidorAdapterForView
						(userSession, commonKey);
					repartidorAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, PadError.REPARTIDOR_LABEL);				
					actionForward = mapping.findForward(PadConstants.FWD_REPARTIDOR_VIEW_ADAPTER);				
				}

				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (repartidorAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + repartidorAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, RepartidorAdapter.NAME, repartidorAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				repartidorAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + 
					RepartidorAdapter.NAME + ": " + repartidorAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(RepartidorAdapter.NAME, repartidorAdapterVO);
				// Subo el apdater al userSession
				userSession.put(RepartidorAdapter.NAME, repartidorAdapterVO);
				
				saveDemodaMessages(request, repartidorAdapterVO);			
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RepartidorAdapter.NAME);
			}
		}
	
	public ActionForward modificarEncabezado(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardAdapter(mapping, request, funcName, 
				PadConstants.ACTION_ADMINISTRAR_ENCREPARTIDOR, BaseConstants.ACT_MODIFICAR);

	}
	
	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_REPARTIDOR, 
				BaseSecurityConstants.ELIMINAR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				RepartidorAdapter repartidorAdapterVO = (RepartidorAdapter) userSession.get(RepartidorAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (repartidorAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RepartidorAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RepartidorAdapter.NAME); 
				}

				// llamada al servicio
				RepartidorVO repartidorVO = PadServiceLocator.getDistribucionService().deleteRepartidor
					(userSession, repartidorAdapterVO.getRepartidor());
				
	            // Tiene errores recuperables
				if (repartidorVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + repartidorAdapterVO.infoString());
					saveDemodaErrors(request, repartidorVO);				
					request.setAttribute(RepartidorAdapter.NAME, repartidorAdapterVO);
					return mapping.findForward(PadConstants.FWD_REPARTIDOR_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (repartidorVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + repartidorAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, RepartidorAdapter.NAME, repartidorAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, RepartidorAdapter.NAME);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RepartidorAdapter.NAME);
			}
		}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_REPARTIDOR, 
				BaseSecurityConstants.ACTIVAR); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				RepartidorAdapter repartidorAdapterVO = (RepartidorAdapter) userSession.get(RepartidorAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (repartidorAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RepartidorAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RepartidorAdapter.NAME); 
				}
				
				// llamada al servicio
				RepartidorVO repartidorVO = PadServiceLocator.getDistribucionService().activarRepartidor
					(userSession, repartidorAdapterVO.getRepartidor());
				
	            // Tiene errores recuperables
				if (repartidorVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + repartidorAdapterVO.infoString());
					saveDemodaErrors(request, repartidorVO);				
					request.setAttribute(RepartidorAdapter.NAME, repartidorAdapterVO);
					return mapping.findForward(PadConstants.FWD_REPARTIDOR_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (repartidorVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + repartidorAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, RepartidorAdapter.NAME, repartidorAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, RepartidorAdapter.NAME);
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RepartidorAdapter.NAME);
			}	
		}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_REPARTIDOR, 
			BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			RepartidorAdapter repartidorAdapterVO = (RepartidorAdapter) userSession.get(RepartidorAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (repartidorAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + RepartidorAdapter.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, RepartidorAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(repartidorAdapterVO, request);
			
            // Tiene errores recuperables
			if (repartidorAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + repartidorAdapterVO.infoString()); 
				saveDemodaErrors(request, repartidorAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, RepartidorAdapter.NAME, repartidorAdapterVO);
			}
			
			// llamada al servicio
			RepartidorVO repartidorVO = PadServiceLocator.getDistribucionService().desactivarRepartidor
				(userSession, repartidorAdapterVO.getRepartidor());
			
            // Tiene errores recuperables
			if (repartidorVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + repartidorAdapterVO.infoString());
				saveDemodaErrors(request, repartidorVO);				
				request.setAttribute(RepartidorAdapter.NAME, repartidorAdapterVO);
				return mapping.findForward(PadConstants.FWD_REPARTIDOR_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (repartidorVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + repartidorAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, RepartidorAdapter.NAME, repartidorAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, RepartidorAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, RepartidorAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, RepartidorAdapter.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, RepartidorAdapter.NAME);
			
	}
	
	// Metodos relacionados CriRepCat

	public ActionForward verCriRepCat(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_CRIREPCAT);

	}

	public ActionForward modificarCriRepCat(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_CRIREPCAT);

	}

	public ActionForward eliminarCriRepCat(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_CRIREPCAT);

	}
	
	public ActionForward agregarCriRepCat(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_CRIREPCAT);
		
	}

	// Metodos relacionados CriRepCalle

	public ActionForward verCriRepCalle(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_CRIREPCALLE);

	}

	public ActionForward modificarCriRepCalle(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_CRIREPCALLE);

	}

	public ActionForward eliminarCriRepCalle(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_CRIREPCALLE);

	}
	
	public ActionForward agregarCriRepCalle(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_CRIREPCALLE);
		
	}

}
