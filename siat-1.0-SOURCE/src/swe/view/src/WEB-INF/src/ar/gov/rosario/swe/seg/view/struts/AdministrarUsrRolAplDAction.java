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
import ar.gov.rosario.swe.iface.model.SweUserSession;
import ar.gov.rosario.swe.iface.model.UsrRolAplAdapter;
import ar.gov.rosario.swe.iface.model.UsrRolAplVO;
import ar.gov.rosario.swe.iface.util.SweSecurityConstants;
import ar.gov.rosario.swe.seg.view.util.SweSegConstants;
import ar.gov.rosario.swe.view.util.SweConstants;
import ar.gov.rosario.swe.view.util.SweUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarUsrRolAplDAction extends SweBaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarUsrRolAplDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		String act = getCurrentAct(request);
		SweUserSession userSession = canAccess(request, mapping, SweSecurityConstants.ABM_ROLESUSUARIO, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		UsrRolAplAdapter usrRolAplAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			// puede ser la clave de UsrRolApl o del UsrApl
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(SweConstants.ACT_VER)) {
				stringServicio = "getUsrRolAplAdapterForView(userSession, commonKey)";
				usrRolAplAdapterVO = SweServiceLocator.getSweService().getUsrRolAplAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(SweSegConstants.FWD_USRROLAPL_ADAPTER_VIEW);
			}
			/* no se permite la modificacion del UsrRolApl
			if (navModel.getAct().equals(SweConstants.ACT_MODIFICAR)) {
				stringServicio = "getUsrRolAplAdapterForUpdate(userSession, commonKey)";
				usrRolAplAdapterVO = SweServiceLocator.getSweService().getUsrRolAplAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(SegConstants.FWD_USRROLAPL_ADAPTER);
			}
			*/
			if (act.equals(SweConstants.ACT_ELIMINAR)) {
				stringServicio = "getUsrRolAplAdapterForDelete(userSession, commonKey)";
				usrRolAplAdapterVO = SweServiceLocator.getSweService().getUsrRolAplAdapterForDelete(userSession, commonKey);
				actionForward = mapping.findForward(SweSegConstants.FWD_USRROLAPL_ADAPTER_VIEW);					
			}
			if (act.equals(SweConstants.ACT_AGREGAR)) {
				stringServicio = "getUsrRolAplAdapterForCreate(userSession)";
				//usrRolAplAdapterVO = SweServiceLocator.getSweService().getUsrAplAdapterForCreate(userSession, commonKey);
				usrRolAplAdapterVO = new UsrRolAplAdapter();
				usrRolAplAdapterVO.getUsrRolApl().getUsrApl().setId(commonKey.getId());
				
				// tiene que ir al inicializar nuevo del buscar rolApl
				actionForward = mapping.findForward(SweSegConstants.ACTION_BUSCAR_ROLAPL_FOR_CREATE_USRROLAPL);
				funcName = "agregar";
 			}

			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (usrRolAplAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + usrRolAplAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, UsrRolAplAdapter.NAME, usrRolAplAdapterVO);
			}
			
			// Incremento el historial de navegacion del userSession
			pasarNavModelActualAHistorial(mapping, funcName, userSession);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + UsrRolAplAdapter.NAME + ": "+ usrRolAplAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(UsrRolAplAdapter.NAME, usrRolAplAdapterVO);
			// Subo el apdater al userSession
			userSession.put(UsrRolAplAdapter.NAME, usrRolAplAdapterVO);
			
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

		SweUserSession userSession = canAccess(request, mapping, SweSecurityConstants.ABM_ROLESUSUARIO, SweSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			UsrRolAplAdapter usrRolAplAdapterVO = (UsrRolAplAdapter) userSession.get(UsrRolAplAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (usrRolAplAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + UsrRolAplAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, UsrRolAplAdapter.NAME); 
			}

			//No Recuperamos datos del form porque viene desde otro action
			//DemodaUtil.populateVO(usrRolAplAdapterVO, form);
			
			// obtengo el rolApl seleccionado
			NavModel navModel = userSession.getNavModel();
			// es la clave de RolApl seleccionado en el buscar
			String selectedId = navModel.getSelectedId();
			if (!StringUtil.isNullOrEmpty(selectedId)){
				CommonKey commonKey = new CommonKey(selectedId);
				usrRolAplAdapterVO.getUsrRolApl().getRolApl().setId(commonKey.getId());
			}else{
				// regresa del buscar rolApl sin rol Apl seleccionado: tiene que regresar al que lo llamo 
				return baseVolver(mapping, form, request, response, UsrRolAplAdapter.NAME);
			}
			
            // Tiene errores recuperables
			if (usrRolAplAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + usrRolAplAdapterVO.infoString()); 
				saveDemodaErrors(request, usrRolAplAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, UsrRolAplAdapter.NAME, usrRolAplAdapterVO);
			}
			
			// llamada al servicio
			UsrRolAplVO usrRolAplVO = SweServiceLocator.getSweService().createUsrRolApl(userSession, usrRolAplAdapterVO.getUsrRolApl());
			
            // Tiene errores recuperables
			if (usrRolAplVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + usrRolAplVO.infoString()); 
				saveDemodaErrors(request, usrRolAplVO);
				return forwardErrorRecoverable(mapping, request, userSession, UsrRolAplAdapter.NAME, usrRolAplAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (usrRolAplVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + usrRolAplVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, UsrRolAplAdapter.NAME, usrRolAplAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, UsrRolAplAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		SweUserSession userSession = canAccess(request, mapping, SweSecurityConstants.ABM_ROLESUSUARIO, SweSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);

		try {
			// Bajo el adapter del userSession
			UsrRolAplAdapter usrRolAplAdapterVO = (UsrRolAplAdapter) userSession.get(UsrRolAplAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (usrRolAplAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + UsrRolAplAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, UsrRolAplAdapter.NAME); 
			}

			// llamada al servicio
			UsrRolAplVO usrRolAplVO = SweServiceLocator.getSweService().deleteUsrRolApl(userSession, usrRolAplAdapterVO.getUsrRolApl());
			
            // Tiene errores recuperables
			if (usrRolAplVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + usrRolAplAdapterVO.infoString()); 
				saveDemodaErrors(request, usrRolAplVO);
				request.setAttribute(UsrRolAplAdapter.NAME, usrRolAplAdapterVO);
				return mapping.findForward(SweSegConstants.FWD_USRROLAPL_ADAPTER_VIEW);
			}
			
			// Tiene errores no recuperables
			if (usrRolAplVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + usrRolAplAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, UsrRolAplAdapter.NAME, usrRolAplAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, UsrRolAplAdapter.NAME);
	
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		return baseVolver(mapping, form, request, response, UsrRolAplAdapter.NAME);
	}
	
}
