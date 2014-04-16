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
import ar.gov.rosario.swe.iface.model.RolAplAdapter;
import ar.gov.rosario.swe.iface.model.RolAplVO;
import ar.gov.rosario.swe.iface.model.SweUserSession;
import ar.gov.rosario.swe.iface.util.SweSecurityConstants;
import ar.gov.rosario.swe.seg.view.util.SweSegConstants;
import ar.gov.rosario.swe.view.util.SweConstants;
import ar.gov.rosario.swe.view.util.SweUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarRolAplDAction extends SweBaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarRolAplDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String act = getCurrentAct(request);
		SweUserSession userSession = canAccess(request, mapping, SweSecurityConstants.ABM_ROLES, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		RolAplAdapter rolAplAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			log.debug(funcName + " navModel.getAct(): " + navModel.getAct());
			
			if (act.equals(SweConstants.ACT_VER)) {
				stringServicio = "getRolAplAdapterForView(userSession, commonKey)";
				rolAplAdapterVO = SweServiceLocator.getSweService().getRolAplAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(SweSegConstants.FWD_ROLAPL_ADAPTER_VIEW);
			}
			if (act.equals(SweConstants.ACT_MODIFICAR)) {
				stringServicio = "getRolAplAdapterForUpdate(userSession, commonKey)";
				rolAplAdapterVO = SweServiceLocator.getSweService().getRolAplAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(SweSegConstants.FWD_ROLAPL_ADAPTER);
			}
			if (act.equals(SweConstants.ACT_ELIMINAR)) {
				stringServicio = "getRolAplAdapterForDelete(userSession, commonKey)";
				rolAplAdapterVO = SweServiceLocator.getSweService().getRolAplAdapterForDelete(userSession, commonKey);
				actionForward = mapping.findForward(SweSegConstants.FWD_ROLAPL_ADAPTER_VIEW);					
			}
			if (act.equals(SweConstants.ACT_AGREGAR)) {
				stringServicio = "getRolAplAdapterForCreate(userSession)";
				rolAplAdapterVO = SweServiceLocator.getSweService().getRolAplAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(SweSegConstants.FWD_ROLAPL_ADAPTER);
			}

			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (rolAplAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + rolAplAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, RolAplAdapter.NAME, rolAplAdapterVO);
			}
			
			// Incremento el historial de navegacion del userSession
			pasarNavModelActualAHistorial(mapping, funcName, userSession);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + RolAplAdapter.NAME + ": "+ rolAplAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(RolAplAdapter.NAME, rolAplAdapterVO);
			// Subo el apdater al userSession
			userSession.put(RolAplAdapter.NAME, rolAplAdapterVO);
			
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
		
		SweUserSession userSession = canAccess(request, mapping, SweSecurityConstants.ABM_ROLES, SweSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			RolAplAdapter rolAplAdapterVO = (RolAplAdapter) userSession.get(RolAplAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (rolAplAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + RolAplAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, RolAplAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(rolAplAdapterVO, request);
			
            // Tiene errores recuperables
			if (rolAplAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + rolAplAdapterVO.infoString()); 
				saveDemodaErrors(request, rolAplAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, RolAplAdapter.NAME, rolAplAdapterVO);
			}
			
			// llamada al servicio
			RolAplVO modAplVO = SweServiceLocator.getSweService().createRolApl(userSession, rolAplAdapterVO.getRolApl());
			
            // Tiene errores recuperables
			if (modAplVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + modAplVO.infoString()); 
				saveDemodaErrors(request, modAplVO);
				return forwardErrorRecoverable(mapping, request, userSession, RolAplAdapter.NAME, rolAplAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (modAplVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + modAplVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, RolAplAdapter.NAME, rolAplAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, RolAplAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		SweUserSession userSession = canAccess(request, mapping, SweSecurityConstants.ABM_ROLES, SweSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);

		try {
			// Bajo el adapter del userSession
			RolAplAdapter rolAplAdapterVO = (RolAplAdapter) userSession.get(RolAplAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (rolAplAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + RolAplAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, RolAplAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(rolAplAdapterVO, request);
			
            // Tiene errores recuperables
			if (rolAplAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + rolAplAdapterVO.infoString()); 
				saveDemodaErrors(request, rolAplAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, RolAplAdapter.NAME, rolAplAdapterVO);
			}
			
			// llamada al servicio
			RolAplVO rolAplVO = SweServiceLocator.getSweService().updateRolApl(userSession, rolAplAdapterVO.getRolApl());
			
            // Tiene errores recuperables
			if (rolAplVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + rolAplAdapterVO.infoString()); 
				saveDemodaErrors(request, rolAplVO);
				return forwardErrorRecoverable(mapping, request, userSession, RolAplAdapter.NAME, rolAplAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (rolAplVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + rolAplAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, RolAplAdapter.NAME, rolAplAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, RolAplAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		SweUserSession userSession = canAccess(request, mapping, SweSecurityConstants.ABM_ROLES, SweSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);

		try {
			// Bajo el adapter del userSession
			RolAplAdapter rolAplAdapterVO = (RolAplAdapter) userSession.get(RolAplAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (rolAplAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + RolAplAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, RolAplAdapter.NAME); 
			}
			
			// llamada al servicio
			RolAplVO rolAplVO = SweServiceLocator.getSweService().deleteRolApl(userSession, rolAplAdapterVO.getRolApl());
			
            // Tiene errores recuperables
			if (rolAplVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + rolAplAdapterVO.infoString()); 
				saveDemodaErrors(request, rolAplVO);
				request.setAttribute(RolAplAdapter.NAME,rolAplAdapterVO);
				return mapping.findForward(SweSegConstants.FWD_ROLAPL_ADAPTER_VIEW);
			}
			
			// Tiene errores no recuperables
			if (rolAplVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + rolAplAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, RolAplAdapter.NAME, rolAplAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, RolAplAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		return baseVolver(mapping, form, request, response, RolAplAdapter.NAME);
	}	
}
