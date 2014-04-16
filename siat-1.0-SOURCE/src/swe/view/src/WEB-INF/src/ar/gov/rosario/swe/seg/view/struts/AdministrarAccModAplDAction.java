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
import ar.gov.rosario.swe.iface.model.AccModAplAdapter;
import ar.gov.rosario.swe.iface.model.AccModAplVO;
import ar.gov.rosario.swe.iface.model.SweUserSession;
import ar.gov.rosario.swe.iface.util.SweSecurityConstants;
import ar.gov.rosario.swe.seg.view.util.SweSegConstants;
import ar.gov.rosario.swe.view.util.SweConstants;
import ar.gov.rosario.swe.view.util.SweUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarAccModAplDAction extends SweBaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarAccModAplDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String act = getCurrentAct(request);
		SweUserSession userSession = canAccess(request, mapping, SweSecurityConstants.ABM_ACCIONESMODULO, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		AccModAplAdapter accModAplAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;

		log.debug("XXX navModel.selectedId = " + navModel.getSelectedId());
		try {

			log.debug("AdministrarAccModAplDAction.inicializar: selectedId = " + userSession.getNavModel().getSelectedId());			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(SweConstants.ACT_VER)) {
				stringServicio = "getAccModAplAdapterForView(userSession, commonKey)";
				accModAplAdapterVO = SweServiceLocator.getSweService().getAccModAplAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(SweSegConstants.FWD_ACCMODAPL_ADAPTER_VIEW);
			}
			if (act.equals(SweConstants.ACT_MODIFICAR)) {
				stringServicio = "getAccModAplAdapterForUpdate(userSession, commonKey)";
				accModAplAdapterVO = SweServiceLocator.getSweService().getAccModAplAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(SweSegConstants.FWD_ACCMODAPL_ADAPTER);
			}
			if (act.equals(SweConstants.ACT_ELIMINAR)) {
				stringServicio = "getAccModAplAdapterForDelete(userSession, commonKey)";
				accModAplAdapterVO = SweServiceLocator.getSweService().getAccModAplAdapterForDelete(userSession, commonKey);
				actionForward = mapping.findForward(SweSegConstants.FWD_ACCMODAPL_ADAPTER_VIEW);					
			}
			if (act.equals(SweConstants.ACT_AGREGAR)) {
				stringServicio = "getAccModAplAdapterForCreate(userSession)";
				accModAplAdapterVO = SweServiceLocator.getSweService().getAccModAplAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(SweSegConstants.FWD_ACCMODAPL_ADAPTER);
			}

			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (accModAplAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + accModAplAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AccModAplAdapter.NAME, accModAplAdapterVO);
			}
			
			// Incremento el historial de navegacion del userSession
			pasarNavModelActualAHistorial(mapping, funcName, userSession);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + AccModAplAdapter.NAME + ": "+ accModAplAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(AccModAplAdapter.NAME, accModAplAdapterVO);
			// Subo el apdater al userSession
			userSession.put(AccModAplAdapter.NAME, accModAplAdapterVO);
			
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
		
		SweUserSession userSession = canAccess(request, mapping, SweSecurityConstants.ABM_ACCIONESMODULO, SweSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AccModAplAdapter accModAplAdapterVO = (AccModAplAdapter) userSession.get(AccModAplAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (accModAplAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AccModAplAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AccModAplAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(accModAplAdapterVO, request);
			
            // Tiene errores recuperables
			if (accModAplAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + accModAplAdapterVO.infoString()); 
				saveDemodaErrors(request, accModAplAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, AccModAplAdapter.NAME, accModAplAdapterVO);
			}
			
			// llamada al servicio
			AccModAplVO accAccModAplVO = SweServiceLocator.getSweService().createAccModApl
				(userSession, accModAplAdapterVO.getAccModApl());
			
            // Tiene errores recuperables
			if (accAccModAplVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + accAccModAplVO.infoString()); 
				saveDemodaErrors(request, accAccModAplVO);
				return forwardErrorRecoverable(mapping, request, userSession, AccModAplAdapter.NAME, accModAplAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (accAccModAplVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + accAccModAplVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AccModAplAdapter.NAME, accModAplAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AccModAplAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		SweUserSession userSession = canAccess(request, mapping, SweSecurityConstants.ABM_ACCIONESMODULO, SweSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AccModAplAdapter accModAplAdapterVO = (AccModAplAdapter) userSession.get(AccModAplAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (accModAplAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AccModAplAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AccModAplAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(accModAplAdapterVO, request);
			
            // Tiene errores recuperables
			if (accModAplAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + accModAplAdapterVO.infoString()); 
				saveDemodaErrors(request, accModAplAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, AccModAplAdapter.NAME, accModAplAdapterVO);
			}
			
			// llamada al servicio
			AccModAplVO accAccModAplVO = SweServiceLocator.getSweService().updateAccModApl
				(userSession, accModAplAdapterVO.getAccModApl());
			
            // Tiene errores recuperables
			if (accAccModAplVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + accModAplAdapterVO.infoString()); 
				saveDemodaErrors(request, accAccModAplVO);
				return forwardErrorRecoverable(mapping, request, userSession, AccModAplAdapter.NAME, accModAplAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (accAccModAplVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + accModAplAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AccModAplAdapter.NAME, accModAplAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AccModAplAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		SweUserSession userSession = canAccess(request, mapping, SweSecurityConstants.ABM_ACCIONESMODULO, SweSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AccModAplAdapter accModAplAdapterVO = (AccModAplAdapter) userSession.get(AccModAplAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (accModAplAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AccModAplAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AccModAplAdapter.NAME); 
			}
			
			// llamada al servicio
			AccModAplVO accAccModAplVO = SweServiceLocator.getSweService().deleteAccModApl
				(userSession, accModAplAdapterVO.getAccModApl());
			
            // Tiene errores recuperables
			if (accAccModAplVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + accModAplAdapterVO.infoString()); 
				saveDemodaErrors(request, accAccModAplVO);
				request.setAttribute(AccModAplAdapter.NAME, accModAplAdapterVO);
				return mapping.findForward(SweSegConstants.FWD_ACCMODAPL_ADAPTER_VIEW);
			}
			
			// Tiene errores no recuperables
			if (accAccModAplVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + accModAplAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AccModAplAdapter.NAME, accModAplAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AccModAplAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		return baseVolver(mapping, form, request, response, AccModAplAdapter.NAME);
	}
	
}
