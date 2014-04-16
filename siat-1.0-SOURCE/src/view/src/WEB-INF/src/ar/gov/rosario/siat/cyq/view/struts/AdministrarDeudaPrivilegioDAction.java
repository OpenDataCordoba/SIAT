//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.cyq.iface.model.DeudaPrivilegioAdapter;
import ar.gov.rosario.siat.cyq.iface.model.DeudaPrivilegioVO;
import ar.gov.rosario.siat.cyq.iface.service.CyqServiceLocator;
import ar.gov.rosario.siat.cyq.iface.util.CyqSecurityConstants;
import ar.gov.rosario.siat.cyq.view.util.CyqConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarDeudaPrivilegioDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarDeudaPrivilegioDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.ABM_DEUDAPRIVILEGIO, act);
		
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		DeudaPrivilegioAdapter deudaPrivilegioAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getDeudaPrivilegioAdapterForView(userSession, commonKey)";
				deudaPrivilegioAdapterVO = CyqServiceLocator.getConcursoyQuiebraService().getDeudaPrivilegioAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(CyqConstants.FWD_DEUDAPRIVILEGIO_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getDeudaPrivilegioAdapterForUpdate(userSession, commonKey)";
				deudaPrivilegioAdapterVO = CyqServiceLocator.getConcursoyQuiebraService().getDeudaPrivilegioAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(CyqConstants.FWD_DEUDAPRIVILEGIO_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getDeudaPrivilegioAdapterForView(userSession, commonKey)";
				deudaPrivilegioAdapterVO = CyqServiceLocator.getConcursoyQuiebraService().getDeudaPrivilegioAdapterForView(userSession, commonKey);				
				//deudaPrivilegioAdapterVO.addMessage(BaseError.MSG_ELIMINAR, CyqError.DEUDAPRIVILEGIO_LABEL);
				actionForward = mapping.findForward(CyqConstants.FWD_DEUDAPRIVILEGIO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getDeudaPrivilegioAdapterForCreate(userSession)";
				deudaPrivilegioAdapterVO = CyqServiceLocator.getConcursoyQuiebraService().getDeudaPrivilegioAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(CyqConstants.FWD_DEUDAPRIVILEGIO_EDIT_ADAPTER);				
			}
			

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (deudaPrivilegioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + deudaPrivilegioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DeudaPrivilegioAdapter.NAME, deudaPrivilegioAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			deudaPrivilegioAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + DeudaPrivilegioAdapter.NAME + ": "+ deudaPrivilegioAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(DeudaPrivilegioAdapter.NAME, deudaPrivilegioAdapterVO);
			// Subo el apdater al userSession
			userSession.put(DeudaPrivilegioAdapter.NAME, deudaPrivilegioAdapterVO);
			 
			saveDemodaMessages(request, deudaPrivilegioAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DeudaPrivilegioAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.ABM_DEUDAPRIVILEGIO, BaseSecurityConstants.AGREGAR);
		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DeudaPrivilegioAdapter deudaPrivilegioAdapterVO = (DeudaPrivilegioAdapter) userSession.get(DeudaPrivilegioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (deudaPrivilegioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DeudaPrivilegioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DeudaPrivilegioAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(deudaPrivilegioAdapterVO, request);
			
            // Tiene errores recuperables
			if (deudaPrivilegioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + deudaPrivilegioAdapterVO.infoString()); 
				saveDemodaErrors(request, deudaPrivilegioAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, DeudaPrivilegioAdapter.NAME, deudaPrivilegioAdapterVO);
			}
			
			// llamada al servicio
			DeudaPrivilegioVO deudaPrivilegioVO = CyqServiceLocator.getConcursoyQuiebraService().createDeudaPrivilegio(userSession, deudaPrivilegioAdapterVO.getDeudaPrivilegio());
			
            // Tiene errores recuperables
			if (deudaPrivilegioVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + deudaPrivilegioVO.infoString()); 
				saveDemodaErrors(request, deudaPrivilegioVO);
				return forwardErrorRecoverable(mapping, request, userSession, DeudaPrivilegioAdapter.NAME, deudaPrivilegioAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (deudaPrivilegioVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + deudaPrivilegioVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DeudaPrivilegioAdapter.NAME, deudaPrivilegioAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DeudaPrivilegioAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DeudaPrivilegioAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.ABM_DEUDAPRIVILEGIO, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DeudaPrivilegioAdapter deudaPrivilegioAdapterVO = (DeudaPrivilegioAdapter) userSession.get(DeudaPrivilegioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (deudaPrivilegioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DeudaPrivilegioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DeudaPrivilegioAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(deudaPrivilegioAdapterVO, request);
			
            // Tiene errores recuperables
			if (deudaPrivilegioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + deudaPrivilegioAdapterVO.infoString()); 
				saveDemodaErrors(request, deudaPrivilegioAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, DeudaPrivilegioAdapter.NAME, deudaPrivilegioAdapterVO);
			}
			
			// llamada al servicio
			DeudaPrivilegioVO deudaPrivilegioVO = CyqServiceLocator.getConcursoyQuiebraService().updateDeudaPrivilegio(userSession, deudaPrivilegioAdapterVO.getDeudaPrivilegio());
			
            // Tiene errores recuperables
			if (deudaPrivilegioVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + deudaPrivilegioAdapterVO.infoString()); 
				saveDemodaErrors(request, deudaPrivilegioVO);
				return forwardErrorRecoverable(mapping, request, userSession, DeudaPrivilegioAdapter.NAME, deudaPrivilegioAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (deudaPrivilegioVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + deudaPrivilegioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DeudaPrivilegioAdapter.NAME, deudaPrivilegioAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DeudaPrivilegioAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DeudaPrivilegioAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.ABM_DEUDAPRIVILEGIO, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DeudaPrivilegioAdapter deudaPrivilegioAdapterVO = (DeudaPrivilegioAdapter) userSession.get(DeudaPrivilegioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (deudaPrivilegioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DeudaPrivilegioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DeudaPrivilegioAdapter.NAME); 
			}

			// llamada al servicio
			DeudaPrivilegioVO deudaPrivilegioVO = CyqServiceLocator.getConcursoyQuiebraService().deleteDeudaPrivilegio
				(userSession, deudaPrivilegioAdapterVO.getDeudaPrivilegio());
			
            // Tiene errores recuperables
			if (deudaPrivilegioVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + deudaPrivilegioAdapterVO.infoString());
				saveDemodaErrors(request, deudaPrivilegioVO);				
				request.setAttribute(DeudaPrivilegioAdapter.NAME, deudaPrivilegioAdapterVO);
				return mapping.findForward(CyqConstants.FWD_DEUDAPRIVILEGIO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (deudaPrivilegioVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + deudaPrivilegioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DeudaPrivilegioAdapter.NAME, deudaPrivilegioAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DeudaPrivilegioAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DeudaPrivilegioAdapter.NAME);
		}
	}

	public ActionForward paramRecurso (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
				
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DeudaPrivilegioAdapter deudaPrivilegioAdapterVO = (DeudaPrivilegioAdapter)userSession.get(DeudaPrivilegioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (deudaPrivilegioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DeudaPrivilegioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DeudaPrivilegioAdapter.NAME); 
			}
			
			DemodaUtil.populateVO(deudaPrivilegioAdapterVO, request);
						
			// llamada al servicio
			deudaPrivilegioAdapterVO = CyqServiceLocator.getConcursoyQuiebraService().getDeudaPrivilegioAdapterParamRecurso(userSession, deudaPrivilegioAdapterVO);
			
            // Tiene errores recuperables
			if (deudaPrivilegioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + deudaPrivilegioAdapterVO.infoString()); 
				saveDemodaErrors(request, deudaPrivilegioAdapterVO);
				return forwardErrorRecoverable
					(mapping, request, userSession, DeudaPrivilegioAdapter.NAME, deudaPrivilegioAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (deudaPrivilegioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + deudaPrivilegioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable
					(mapping, request, funcName, DeudaPrivilegioAdapter.NAME, deudaPrivilegioAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(DeudaPrivilegioAdapter.NAME, deudaPrivilegioAdapterVO);
			// Subo el apdater al userSession
			userSession.put(DeudaPrivilegioAdapter.NAME, deudaPrivilegioAdapterVO);
			
			return mapping.findForward(CyqConstants.FWD_DEUDAPRIVILEGIO_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DeudaPrivilegioAdapter.NAME);
		}
	}

	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, DeudaPrivilegioAdapter.NAME);
		
	}
	

}
