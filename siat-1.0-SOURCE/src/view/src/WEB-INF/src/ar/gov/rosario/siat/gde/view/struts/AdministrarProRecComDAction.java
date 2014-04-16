//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.view.struts;

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
import ar.gov.rosario.siat.gde.iface.model.ProRecComAdapter;
import ar.gov.rosario.siat.gde.iface.model.ProRecComVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarProRecComDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarProRecComDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PRORECCOM, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ProRecComAdapter proRecComAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getProRecComAdapterForView(userSession, commonKey)";
				proRecComAdapterVO = GdeServiceLocator.getDefinicionService().getProRecComAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PRORECCOM_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getProRecComAdapterForUpdate(userSession, commonKey)";
				proRecComAdapterVO = GdeServiceLocator.getDefinicionService().getProRecComAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PRORECCOM_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getProRecComAdapterForView(userSession, commonKey)";
				proRecComAdapterVO = GdeServiceLocator.getDefinicionService().getProRecComAdapterForView(userSession, commonKey);				
				proRecComAdapterVO.addMessage(BaseError.MSG_ELIMINAR, GdeError.PRORECCOM_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_PRORECCOM_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getProRecComAdapterForCreate(userSession)";
				proRecComAdapterVO = GdeServiceLocator.getDefinicionService().getProRecComAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PRORECCOM_EDIT_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (proRecComAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + proRecComAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProRecComAdapter.NAME, proRecComAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			proRecComAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ProRecComAdapter.NAME + ": "+ proRecComAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ProRecComAdapter.NAME, proRecComAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ProRecComAdapter.NAME, proRecComAdapterVO);
			 
			saveDemodaMessages(request, proRecComAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProRecComAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PRORECCOM, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ProRecComAdapter proRecComAdapterVO = (ProRecComAdapter) userSession.get(ProRecComAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (proRecComAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProRecComAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProRecComAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(proRecComAdapterVO, request);
			
            // Tiene errores recuperables
			if (proRecComAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + proRecComAdapterVO.infoString()); 
				saveDemodaErrors(request, proRecComAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProRecComAdapter.NAME, proRecComAdapterVO);
			}
			
			// llamada al servicio
			ProRecComVO proRecComVO = GdeServiceLocator.getDefinicionService().createProRecCom(userSession, proRecComAdapterVO.getProRecCom());
			
            // Tiene errores recuperables
			if (proRecComVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + proRecComVO.infoString()); 
				saveDemodaErrors(request, proRecComVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProRecComAdapter.NAME, proRecComAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (proRecComVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + proRecComVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProRecComAdapter.NAME, proRecComAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ProRecComAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProRecComAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PRORECCOM, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ProRecComAdapter proRecComAdapterVO = (ProRecComAdapter) userSession.get(ProRecComAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (proRecComAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProRecComAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProRecComAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(proRecComAdapterVO, request);
			
            // Tiene errores recuperables
			if (proRecComAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + proRecComAdapterVO.infoString()); 
				saveDemodaErrors(request, proRecComAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProRecComAdapter.NAME, proRecComAdapterVO);
			}
			
			// llamada al servicio
			ProRecComVO proRecComVO = GdeServiceLocator.getDefinicionService().updateProRecCom(userSession, proRecComAdapterVO.getProRecCom());
			
            // Tiene errores recuperables
			if (proRecComVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + proRecComAdapterVO.infoString()); 
				saveDemodaErrors(request, proRecComVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProRecComAdapter.NAME, proRecComAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (proRecComVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + proRecComAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProRecComAdapter.NAME, proRecComAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ProRecComAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProRecComAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PRORECCOM, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ProRecComAdapter proRecComAdapterVO = (ProRecComAdapter) userSession.get(ProRecComAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (proRecComAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProRecComAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProRecComAdapter.NAME); 
			}

			// llamada al servicio
			ProRecComVO proRecComVO = GdeServiceLocator.getDefinicionService().deleteProRecCom
				(userSession, proRecComAdapterVO.getProRecCom());
			
            // Tiene errores recuperables
			if (proRecComVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + proRecComAdapterVO.infoString());
				saveDemodaErrors(request, proRecComVO);				
				request.setAttribute(ProRecComAdapter.NAME, proRecComAdapterVO);
				return mapping.findForward(GdeConstants.FWD_PRORECCOM_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (proRecComVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + proRecComAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProRecComAdapter.NAME, proRecComAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ProRecComAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProRecComAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ProRecComAdapter.NAME);
		
	}
}
