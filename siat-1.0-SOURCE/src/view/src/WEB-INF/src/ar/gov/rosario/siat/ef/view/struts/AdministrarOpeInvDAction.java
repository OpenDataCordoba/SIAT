//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.view.struts;

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
import ar.gov.rosario.siat.ef.iface.model.OpeInvAdapter;
import ar.gov.rosario.siat.ef.iface.model.OpeInvVO;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfError;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarOpeInvDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarOpeInvDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_OPEINV, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		OpeInvAdapter opeInvAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getOpeInvAdapterForView(userSession, commonKey)";
				opeInvAdapterVO = EfServiceLocator.getInvestigacionService().getOpeInvAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_OPEINV_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getOpeInvAdapterForUpdate(userSession, commonKey)";
				opeInvAdapterVO = EfServiceLocator.getInvestigacionService().getOpeInvAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_OPEINV_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getOpeInvAdapterForView(userSession, commonKey)";
				opeInvAdapterVO = EfServiceLocator.getInvestigacionService().getOpeInvAdapterForView(userSession, commonKey);				
				opeInvAdapterVO.addMessage(BaseError.MSG_ELIMINAR, EfError.OPEINV_LABEL);
				actionForward = mapping.findForward(EfConstants.FWD_OPEINV_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getOpeInvAdapterForCreate(userSession)";
				opeInvAdapterVO = EfServiceLocator.getInvestigacionService().getOpeInvAdapterForCreate(userSession);
				actionForward = mapping.findForward(EfConstants.FWD_OPEINV_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getOpeInvAdapterForView(userSession)";
				opeInvAdapterVO = EfServiceLocator.getInvestigacionService().getOpeInvAdapterForView(userSession, commonKey);
				opeInvAdapterVO.addMessage(BaseError.MSG_ACTIVAR, EfError.OPEINV_LABEL);
				actionForward = mapping.findForward(EfConstants.FWD_OPEINV_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getOpeInvAdapterForView(userSession)";
				opeInvAdapterVO = EfServiceLocator.getInvestigacionService().getOpeInvAdapterForView(userSession, commonKey);
				opeInvAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, EfError.OPEINV_LABEL);
				actionForward = mapping.findForward(EfConstants.FWD_OPEINV_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (opeInvAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + opeInvAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, OpeInvAdapter.NAME, opeInvAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			opeInvAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + OpeInvAdapter.NAME + ": "+ opeInvAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(OpeInvAdapter.NAME, opeInvAdapterVO);
			// Subo el apdater al userSession
			userSession.put(OpeInvAdapter.NAME, opeInvAdapterVO);
			 
			saveDemodaMessages(request, opeInvAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OpeInvAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_OPEINV, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			OpeInvAdapter opeInvAdapterVO = (OpeInvAdapter) userSession.get(OpeInvAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (opeInvAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + OpeInvAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OpeInvAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(opeInvAdapterVO, request);
			
            // Tiene errores recuperables
			if (opeInvAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + opeInvAdapterVO.infoString()); 
				saveDemodaErrors(request, opeInvAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, OpeInvAdapter.NAME, opeInvAdapterVO);
			}
			
			// llamada al servicio
			OpeInvVO opeInvVO = EfServiceLocator.getInvestigacionService().createOpeInv(userSession, opeInvAdapterVO.getOpeInv());
			
            // Tiene errores recuperables
			if (opeInvVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + opeInvVO.infoString()); 
				saveDemodaErrors(request, opeInvVO);
				return forwardErrorRecoverable(mapping, request, userSession, OpeInvAdapter.NAME, opeInvAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (opeInvVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + opeInvVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, OpeInvAdapter.NAME, opeInvAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, OpeInvAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OpeInvAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_OPEINV, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			OpeInvAdapter opeInvAdapterVO = (OpeInvAdapter) userSession.get(OpeInvAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (opeInvAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + OpeInvAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OpeInvAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(opeInvAdapterVO, request);
			
            // Tiene errores recuperables
			if (opeInvAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + opeInvAdapterVO.infoString()); 
				saveDemodaErrors(request, opeInvAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, OpeInvAdapter.NAME, opeInvAdapterVO);
			}
			
			// llamada al servicio
			OpeInvVO opeInvVO = EfServiceLocator.getInvestigacionService().updateOpeInv(userSession, opeInvAdapterVO.getOpeInv());
			
            // Tiene errores recuperables
			if (opeInvVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + opeInvAdapterVO.infoString()); 
				saveDemodaErrors(request, opeInvVO);
				return forwardErrorRecoverable(mapping, request, userSession, OpeInvAdapter.NAME, opeInvAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (opeInvVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + opeInvAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, OpeInvAdapter.NAME, opeInvAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, OpeInvAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OpeInvAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_OPEINV, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			OpeInvAdapter opeInvAdapterVO = (OpeInvAdapter) userSession.get(OpeInvAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (opeInvAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + OpeInvAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OpeInvAdapter.NAME); 
			}

			// llamada al servicio
			OpeInvVO opeInvVO = EfServiceLocator.getInvestigacionService().deleteOpeInv
				(userSession, opeInvAdapterVO.getOpeInv());
			
            // Tiene errores recuperables
			if (opeInvVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + opeInvAdapterVO.infoString());
				saveDemodaErrors(request, opeInvVO);				
				request.setAttribute(OpeInvAdapter.NAME, opeInvAdapterVO);
				return mapping.findForward(EfConstants.FWD_OPEINV_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (opeInvVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + opeInvAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, OpeInvAdapter.NAME, opeInvAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, OpeInvAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OpeInvAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, OpeInvAdapter.NAME);
		
	}
	

		
}
