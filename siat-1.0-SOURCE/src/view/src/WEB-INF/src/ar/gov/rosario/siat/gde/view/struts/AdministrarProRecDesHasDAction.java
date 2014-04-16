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
import ar.gov.rosario.siat.gde.iface.model.ProRecDesHasAdapter;
import ar.gov.rosario.siat.gde.iface.model.ProRecDesHasVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarProRecDesHasDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarProRecDesHasDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PRORECDESHAS, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ProRecDesHasAdapter proRecDesHasAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getProRecDesHasAdapterForView(userSession, commonKey)";
				proRecDesHasAdapterVO = GdeServiceLocator.getDefinicionService().getProRecDesHasAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PRORECDESHAS_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getProRecDesHasAdapterForUpdate(userSession, commonKey)";
				proRecDesHasAdapterVO = GdeServiceLocator.getDefinicionService().getProRecDesHasAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PRORECDESHAS_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getProRecDesHasAdapterForView(userSession, commonKey)";
				proRecDesHasAdapterVO = GdeServiceLocator.getDefinicionService().getProRecDesHasAdapterForView(userSession, commonKey);				
				proRecDesHasAdapterVO.addMessage(BaseError.MSG_ELIMINAR, GdeError.PRORECDESHAS_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_PRORECDESHAS_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getProRecDesHasAdapterForCreate(userSession)";
				proRecDesHasAdapterVO = GdeServiceLocator.getDefinicionService().getProRecDesHasAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PRORECDESHAS_EDIT_ADAPTER);				
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (proRecDesHasAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + proRecDesHasAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProRecDesHasAdapter.NAME, proRecDesHasAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			proRecDesHasAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ProRecDesHasAdapter.NAME + ": "+ proRecDesHasAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ProRecDesHasAdapter.NAME, proRecDesHasAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ProRecDesHasAdapter.NAME, proRecDesHasAdapterVO);
			 
			saveDemodaMessages(request, proRecDesHasAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProRecDesHasAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PRORECDESHAS, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ProRecDesHasAdapter proRecDesHasAdapterVO = (ProRecDesHasAdapter) userSession.get(ProRecDesHasAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (proRecDesHasAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProRecDesHasAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProRecDesHasAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(proRecDesHasAdapterVO, request);
			
            // Tiene errores recuperables
			if (proRecDesHasAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + proRecDesHasAdapterVO.infoString()); 
				saveDemodaErrors(request, proRecDesHasAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProRecDesHasAdapter.NAME, proRecDesHasAdapterVO);
			}
			
			// llamada al servicio
			ProRecDesHasVO proRecDesHasVO = GdeServiceLocator.getDefinicionService().createProRecDesHas(userSession, proRecDesHasAdapterVO.getProRecDesHas());
			
            // Tiene errores recuperables
			if (proRecDesHasVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + proRecDesHasVO.infoString()); 
				saveDemodaErrors(request, proRecDesHasVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProRecDesHasAdapter.NAME, proRecDesHasAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (proRecDesHasVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + proRecDesHasVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProRecDesHasAdapter.NAME, proRecDesHasAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ProRecDesHasAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProRecDesHasAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PRORECDESHAS, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ProRecDesHasAdapter proRecDesHasAdapterVO = (ProRecDesHasAdapter) userSession.get(ProRecDesHasAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (proRecDesHasAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProRecDesHasAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProRecDesHasAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(proRecDesHasAdapterVO, request);
			
            // Tiene errores recuperables
			if (proRecDesHasAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + proRecDesHasAdapterVO.infoString()); 
				saveDemodaErrors(request, proRecDesHasAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProRecDesHasAdapter.NAME, proRecDesHasAdapterVO);
			}
			
			// llamada al servicio
			ProRecDesHasVO proRecDesHasVO = GdeServiceLocator.getDefinicionService().updateProRecDesHas(userSession, proRecDesHasAdapterVO.getProRecDesHas());
			
            // Tiene errores recuperables
			if (proRecDesHasVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + proRecDesHasAdapterVO.infoString()); 
				saveDemodaErrors(request, proRecDesHasVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProRecDesHasAdapter.NAME, proRecDesHasAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (proRecDesHasVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + proRecDesHasAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProRecDesHasAdapter.NAME, proRecDesHasAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ProRecDesHasAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProRecDesHasAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PRORECDESHAS, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ProRecDesHasAdapter proRecDesHasAdapterVO = (ProRecDesHasAdapter) userSession.get(ProRecDesHasAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (proRecDesHasAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProRecDesHasAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProRecDesHasAdapter.NAME); 
			}

			// llamada al servicio
			ProRecDesHasVO proRecDesHasVO = GdeServiceLocator.getDefinicionService().deleteProRecDesHas
				(userSession, proRecDesHasAdapterVO.getProRecDesHas());
			
            // Tiene errores recuperables
			if (proRecDesHasVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + proRecDesHasAdapterVO.infoString());
				saveDemodaErrors(request, proRecDesHasVO);				
				request.setAttribute(ProRecDesHasAdapter.NAME, proRecDesHasAdapterVO);
				return mapping.findForward(GdeConstants.FWD_PRORECDESHAS_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (proRecDesHasVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + proRecDesHasAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProRecDesHasAdapter.NAME, proRecDesHasAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ProRecDesHasAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProRecDesHasAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ProRecDesHasAdapter.NAME);
		
	}
}	
