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
import ar.gov.rosario.siat.esp.iface.model.PrecioEventoAdapter;
import ar.gov.rosario.siat.esp.iface.model.PrecioEventoVO;
import ar.gov.rosario.siat.esp.iface.service.EspServiceLocator;
import ar.gov.rosario.siat.esp.iface.util.EspError;
import ar.gov.rosario.siat.esp.iface.util.EspSecurityConstants;
import ar.gov.rosario.siat.esp.view.util.EspConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class AdministrarPrecioEventoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarPrecioEventoDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = canAccess(request, mapping, EspSecurityConstants.ABM_PRECIOEVENTO, act);		
			if (userSession == null) return forwardErrorSession(request);

			NavModel navModel = userSession.getNavModel();

			PrecioEventoAdapter precioEventoAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
					stringServicio = "getPrecioEventoAdapterForView(userSession, commonKey)";
					precioEventoAdapterVO = EspServiceLocator.getHabilitacionService().getPrecioEventoAdapterForView
						(userSession, commonKey);
					actionForward = mapping.findForward(EspConstants.FWD_PRECIOEVENTO_VIEW_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
					stringServicio = "getPrecioEventoAdapterForUpdate(userSession, commonKey)";
					precioEventoAdapterVO = EspServiceLocator.getHabilitacionService().getPrecioEventoAdapterForUpdate
						(userSession, commonKey);
					actionForward = mapping.findForward(EspConstants.FWD_PRECIOEVENTO_EDIT_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
					stringServicio = "getPrecioEventoAdapterForDelete(userSession, commonKey)";
					precioEventoAdapterVO = EspServiceLocator.getHabilitacionService().getPrecioEventoAdapterForView
						(userSession, commonKey);
					precioEventoAdapterVO.addMessage(BaseError.MSG_ELIMINAR, EspError.PRECIOEVENTO_LABEL);				
					actionForward = mapping.findForward(EspConstants.FWD_PRECIOEVENTO_VIEW_ADAPTER);					
				}
				if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
					stringServicio = "getPrecioEventoAdapterForCreate(userSession)";
					precioEventoAdapterVO = EspServiceLocator.getHabilitacionService().getPrecioEventoAdapterForCreate
						(userSession, commonKey);
					actionForward = mapping.findForward(EspConstants.FWD_PRECIOEVENTO_EDIT_ADAPTER);
				}

				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (precioEventoAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + precioEventoAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, PrecioEventoAdapter.NAME, precioEventoAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				precioEventoAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + PrecioEventoAdapter.NAME + ": "+ precioEventoAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(PrecioEventoAdapter.NAME, precioEventoAdapterVO);
				// Subo el apdater al userSession
				userSession.put(PrecioEventoAdapter.NAME, precioEventoAdapterVO);
				
				saveDemodaMessages(request, precioEventoAdapterVO);			
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, PrecioEventoAdapter.NAME);
			}
		}
	

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping,
				EspSecurityConstants.ABM_PRECIOEVENTO, BaseSecurityConstants.AGREGAR); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				PrecioEventoAdapter precioEventoAdapterVO = (PrecioEventoAdapter) userSession.get(PrecioEventoAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (precioEventoAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + PrecioEventoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, PrecioEventoAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(precioEventoAdapterVO, request);
				
	            // Tiene errores recuperables
				if (precioEventoAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + precioEventoAdapterVO.infoString()); 
					saveDemodaErrors(request, precioEventoAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, PrecioEventoAdapter.NAME, precioEventoAdapterVO);
				}
				
				// llamada al servicio
				PrecioEventoVO precioEventoVO = EspServiceLocator.getHabilitacionService().createPrecioEvento(userSession, precioEventoAdapterVO.getPrecioEvento());
				
	            // Tiene errores recuperables
				if (precioEventoVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + precioEventoVO.infoString()); 
					saveDemodaErrors(request, precioEventoVO);
					return forwardErrorRecoverable(mapping, request, userSession, PrecioEventoAdapter.NAME, precioEventoAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (precioEventoVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + precioEventoVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, PrecioEventoAdapter.NAME, precioEventoAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, PrecioEventoAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, PrecioEventoAdapter.NAME);
			}
		}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				EspSecurityConstants.ABM_PRECIOEVENTO, BaseSecurityConstants.AGREGAR);
			
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				PrecioEventoAdapter precioEventoAdapterVO = (PrecioEventoAdapter) userSession.get(PrecioEventoAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (precioEventoAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + PrecioEventoAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, PrecioEventoAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(precioEventoAdapterVO, request);
				
	            // Tiene errores recuperables
				if (precioEventoAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + precioEventoAdapterVO.infoString()); 
					saveDemodaErrors(request, precioEventoAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						PrecioEventoAdapter.NAME, precioEventoAdapterVO);
				}
				
				// llamada al servicio
				PrecioEventoVO precioEventoVO = EspServiceLocator.getHabilitacionService().updatePrecioEvento(userSession, precioEventoAdapterVO.getPrecioEvento());
				
	            // Tiene errores recuperables
				if (precioEventoVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + precioEventoAdapterVO.infoString()); 
					saveDemodaErrors(request, precioEventoVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						PrecioEventoAdapter.NAME, precioEventoAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (precioEventoVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + precioEventoAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						PrecioEventoAdapter.NAME, precioEventoAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, PrecioEventoAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, PrecioEventoAdapter.NAME);
			}
		}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				EspSecurityConstants.ABM_PRECIOEVENTO, BaseSecurityConstants.ELIMINAR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				PrecioEventoAdapter precioEventoAdapterVO = (PrecioEventoAdapter) userSession.get(PrecioEventoAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (precioEventoAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + PrecioEventoAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, PrecioEventoAdapter.NAME); 
				}

				// llamada al servicio
				PrecioEventoVO precioEventoVO = EspServiceLocator.getHabilitacionService().deletePrecioEvento(userSession, precioEventoAdapterVO.getPrecioEvento());
				
	            // Tiene errores recuperables
				if (precioEventoVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + precioEventoAdapterVO.infoString());
					saveDemodaErrors(request, precioEventoVO);				
					request.setAttribute(PrecioEventoAdapter.NAME, precioEventoAdapterVO);
					return mapping.findForward(EspConstants.FWD_PRECIOEVENTO_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (precioEventoVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + precioEventoAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						PrecioEventoAdapter.NAME, precioEventoAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, PrecioEventoAdapter.NAME);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, PrecioEventoAdapter.NAME);
			}
		}

		public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, PrecioEventoAdapter.NAME);
		}
}
