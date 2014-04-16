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
import ar.gov.rosario.swe.iface.model.ModAplAdapter;
import ar.gov.rosario.swe.iface.model.ModAplVO;
import ar.gov.rosario.swe.iface.model.SweUserSession;
import ar.gov.rosario.swe.iface.util.SweSecurityConstants;
import ar.gov.rosario.swe.seg.view.util.SweSegConstants;
import ar.gov.rosario.swe.view.util.SweConstants;
import ar.gov.rosario.swe.view.util.SweUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarModAplDAction extends SweBaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarModAplDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String act = getCurrentAct(request);
		SweUserSession userSession = canAccess(request, mapping, SweSecurityConstants.ABM_MODULOS, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ModAplAdapter modAplAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(SweConstants.ACT_VER)) {
				stringServicio = "getModAplAdapterForView(userSession, commonKey)";
				modAplAdapterVO = SweServiceLocator.getSweService().getModAplAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(SweSegConstants.FWD_MODAPL_ADAPTER_VIEW);
			}
			if (act.equals(SweConstants.ACT_MODIFICAR)) {
				stringServicio = "getModAplAdapterForUpdate(userSession, commonKey)";
				modAplAdapterVO = SweServiceLocator.getSweService().getModAplAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(SweSegConstants.FWD_MODAPL_ADAPTER);
			}
			if (act.equals(SweConstants.ACT_ELIMINAR)) {
				stringServicio = "getModAplAdapterForDelete(userSession, commonKey)";
				modAplAdapterVO = SweServiceLocator.getSweService().getModAplAdapterForDelete(userSession, commonKey);
				actionForward = mapping.findForward(SweSegConstants.FWD_MODAPL_ADAPTER_VIEW);					
			}
			if (act.equals(SweConstants.ACT_AGREGAR)) {
				stringServicio = "getModAplAdapterForCreate(userSession)";
				modAplAdapterVO = SweServiceLocator.getSweService().getModAplAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(SweSegConstants.FWD_MODAPL_ADAPTER);
			}

			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (modAplAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + modAplAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ModAplAdapter.NAME, modAplAdapterVO);
			}
			
			// Incremento el historial de navegacion del userSession
			pasarNavModelActualAHistorial(mapping, funcName, userSession);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ModAplAdapter.NAME + ": "+ modAplAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ModAplAdapter.NAME, modAplAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ModAplAdapter.NAME, modAplAdapterVO);
			
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
		
		SweUserSession userSession = canAccess(request, mapping, SweSecurityConstants.ABM_MODULOS, SweSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ModAplAdapter modAplAdapterVO = (ModAplAdapter) userSession.get(ModAplAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (modAplAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ModAplAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ModAplAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(modAplAdapterVO, request);
			
            // Tiene errores recuperables
			if (modAplAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + modAplAdapterVO.infoString()); 
				saveDemodaErrors(request, modAplAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ModAplAdapter.NAME, modAplAdapterVO);
			}
			
			// llamada al servicio
			ModAplVO modAplVO = SweServiceLocator.getSweService().createModApl(userSession, modAplAdapterVO.getModApl());
			
            // Tiene errores recuperables
			if (modAplVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + modAplVO.infoString()); 
				saveDemodaErrors(request, modAplVO);
				return forwardErrorRecoverable(mapping, request, userSession, ModAplAdapter.NAME, modAplAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (modAplVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + modAplVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ModAplAdapter.NAME, modAplAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ModAplAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		SweUserSession userSession = canAccess(request, mapping, SweSecurityConstants.ABM_MODULOS, SweSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ModAplAdapter modAplAdapterVO = (ModAplAdapter) userSession.get(ModAplAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (modAplAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ModAplAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ModAplAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(modAplAdapterVO, request);
			
            // Tiene errores recuperables
			if (modAplAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + modAplAdapterVO.infoString()); 
				saveDemodaErrors(request, modAplAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ModAplAdapter.NAME, modAplAdapterVO);
			}
			
			// llamada al servicio
			ModAplVO modAplVO = SweServiceLocator.getSweService().updateModApl(userSession, modAplAdapterVO.getModApl());
			
            // Tiene errores recuperables
			if (modAplVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + modAplAdapterVO.infoString()); 
				saveDemodaErrors(request, modAplVO);
				return forwardErrorRecoverable(mapping, request, userSession, ModAplAdapter.NAME, modAplAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (modAplVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + modAplAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ModAplAdapter.NAME, modAplAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ModAplAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		SweUserSession userSession = canAccess(request, mapping, SweSecurityConstants.ABM_MODULOS, SweSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ModAplAdapter modAplAdapterVO = (ModAplAdapter) userSession.get(ModAplAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (modAplAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ModAplAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ModAplAdapter.NAME); 
			}
			
			// llamada al servicio
			ModAplVO modAplVO = SweServiceLocator.getSweService().deleteModApl(userSession, modAplAdapterVO.getModApl());
			
            // Tiene errores recuperables
			if (modAplVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + modAplAdapterVO.infoString()); 
				saveDemodaErrors(request, modAplVO);
				request.setAttribute(ModAplAdapter.NAME, modAplAdapterVO);
				return mapping.findForward(SweSegConstants.FWD_MODAPL_ADAPTER_VIEW);
			}

			// Tiene errores no recuperables
			if (modAplVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + modAplAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ModAplAdapter.NAME, modAplAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ModAplAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		return baseVolver(mapping, form, request, response, ModAplAdapter.NAME);
	}
	
}
