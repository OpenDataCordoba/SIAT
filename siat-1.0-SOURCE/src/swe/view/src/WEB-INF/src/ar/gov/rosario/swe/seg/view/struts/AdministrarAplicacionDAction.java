//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.seg.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.swe.SweServiceLocator;
import ar.gov.rosario.swe.base.view.struts.SweBaseDispatchAction;
import ar.gov.rosario.swe.iface.model.AplicacionAdapter;
import ar.gov.rosario.swe.iface.model.AplicacionVO;
import ar.gov.rosario.swe.iface.model.SweUserSession;
import ar.gov.rosario.swe.iface.util.SweSecurityConstants;
import ar.gov.rosario.swe.seg.view.util.SweSegConstants;
import ar.gov.rosario.swe.view.util.SweConstants;
import ar.gov.rosario.swe.view.util.SweUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarAplicacionDAction extends SweBaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarAplicacionDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		String act = getCurrentAct(request);
		SweUserSession userSession = canAccess(request, mapping, SweSecurityConstants.ABM_APLICACION, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		AplicacionAdapter aplicacionAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(SweConstants.ACT_VER)) {
				stringServicio = "getAplicacionAdapterForView(userSession, commonKey)";
				aplicacionAdapterVO = SweServiceLocator.getSweService().getAplicacionAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(SweSegConstants.FWD_APLICACION_ADAPTER_VIEW);
			}
			if (act.equals(SweConstants.ACT_MODIFICAR)) {
				stringServicio = "getAplicacionAdapterForUpdate(userSession, commonKey)";
				aplicacionAdapterVO = SweServiceLocator.getSweService().getAplicacionAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(SweSegConstants.FWD_APLICACION_ADAPTER);
			}
			if (act.equals(SweConstants.ACT_ELIMINAR)) {
				stringServicio = "getAplicacionAdapterForDelete(userSession, commonKey)";
				aplicacionAdapterVO = SweServiceLocator.getSweService().getAplicacionAdapterForDelete(userSession, commonKey);
				actionForward = mapping.findForward(SweSegConstants.FWD_APLICACION_ADAPTER_VIEW);					
			}
			if (act.equals(SweConstants.ACT_AGREGAR)) {
				stringServicio = "getAplicacionAdapterForCreate(userSession)";
				aplicacionAdapterVO = SweServiceLocator.getSweService().getAplicacionAdapterForCreate(userSession);
				actionForward = mapping.findForward(SweSegConstants.FWD_APLICACION_ADAPTER);
			}

			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (aplicacionAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + aplicacionAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AplicacionAdapter.NAME, aplicacionAdapterVO);
			}
			
			// Incremento el historial de navegacion del userSession
			pasarNavModelActualAHistorial(mapping, funcName, userSession);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + AplicacionAdapter.NAME + ": "+ aplicacionAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(AplicacionAdapter.NAME, aplicacionAdapterVO);
			// Subo el apdater al userSession
			userSession.put(AplicacionAdapter.NAME, aplicacionAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		SweUserSession userSession = canAccess(request, mapping, SweSecurityConstants.ABM_APLICACION, SweSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AplicacionAdapter aplicacionAdapterVO = (AplicacionAdapter) userSession.get(AplicacionAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (aplicacionAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AplicacionAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AplicacionAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(aplicacionAdapterVO, request);
			
            // Tiene errores recuperables
			if (aplicacionAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + aplicacionAdapterVO.infoString()); 
				saveDemodaErrors(request, aplicacionAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, AplicacionAdapter.NAME, aplicacionAdapterVO);
			}
			
			// llamada al servicio
			AplicacionVO aplicacionVO = SweServiceLocator.getSweService().createAplicacion(userSession, aplicacionAdapterVO.getAplicacion());
			
            // Tiene errores recuperables
			if (aplicacionVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + aplicacionVO.infoString()); 
				saveDemodaErrors(request, aplicacionVO);
				return forwardErrorRecoverable(mapping, request, userSession, AplicacionAdapter.NAME, aplicacionAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (aplicacionVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + aplicacionVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AplicacionAdapter.NAME, aplicacionAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AplicacionAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		SweUserSession userSession = canAccess(request, mapping, SweSecurityConstants.ABM_APLICACION, SweSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AplicacionAdapter aplicacionAdapterVO = (AplicacionAdapter) userSession.get(AplicacionAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (aplicacionAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AplicacionAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AplicacionAdapter.NAME); 
			}
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(aplicacionAdapterVO, request);
			
			// Tiene errores recuperables
			if (aplicacionAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + aplicacionAdapterVO.infoString()); 
				saveDemodaErrors(request, aplicacionAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, AplicacionAdapter.NAME, aplicacionAdapterVO);
			}
			
			// llamada al servicio
			
			
			AplicacionVO aplicacionVO = SweServiceLocator.getSweService().updateAplicacion(userSession, aplicacionAdapterVO.getAplicacion());
			
            // Tiene errores recuperables
			if (aplicacionVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + aplicacionAdapterVO.infoString()); 
				saveDemodaErrors(request, aplicacionVO);
				return forwardErrorRecoverable(mapping, request, userSession, AplicacionAdapter.NAME, aplicacionAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (aplicacionVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + aplicacionAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AplicacionAdapter.NAME, aplicacionAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AplicacionAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		SweUserSession userSession = canAccess(request, mapping, SweSecurityConstants.ABM_APLICACION, SweSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AplicacionAdapter aplicacionAdapterVO = (AplicacionAdapter) userSession.get(AplicacionAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (aplicacionAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AplicacionAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AplicacionAdapter.NAME); 
			}

			// llamada al servicio
			AplicacionVO aplicacionVO = SweServiceLocator.getSweService().deleteAplicacion
				(userSession, aplicacionAdapterVO.getAplicacion());
			
            // Tiene errores recuperables
			if (aplicacionVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + aplicacionAdapterVO.infoString());
				saveDemodaErrors(request, aplicacionVO);				
				request.setAttribute(AplicacionAdapter.NAME, aplicacionAdapterVO);
				return mapping.findForward(SweSegConstants.FWD_APLICACION_ADAPTER_VIEW);
			}
			
			// Tiene errores no recuperables
			if (aplicacionVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + aplicacionAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AplicacionAdapter.NAME, aplicacionAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AplicacionAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		return baseVolver(mapping, form, request, response, AplicacionAdapter.NAME);
	}
	
}
