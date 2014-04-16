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
import ar.gov.rosario.siat.gde.iface.model.MandatarioAdapter;
import ar.gov.rosario.siat.gde.iface.model.MandatarioVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarMandatarioDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarMandatarioDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_MANDATARIO, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		MandatarioAdapter mandatarioAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getMandatarioAdapterForView(userSession, commonKey)";
				mandatarioAdapterVO = GdeServiceLocator.getDefinicionService().getMandatarioAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_MANDATARIO_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getMandatarioAdapterForUpdate(userSession, commonKey)";
				mandatarioAdapterVO = GdeServiceLocator.getDefinicionService().getMandatarioAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_MANDATARIO_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getMandatarioAdapterForView(userSession, commonKey)";
				mandatarioAdapterVO = GdeServiceLocator.getDefinicionService().getMandatarioAdapterForView(userSession, commonKey);				
				mandatarioAdapterVO.addMessage(BaseError.MSG_ELIMINAR, GdeError.MANDATARIO_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_MANDATARIO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getMandatarioAdapterForCreate(userSession)";
				mandatarioAdapterVO = GdeServiceLocator.getDefinicionService().getMandatarioAdapterForCreate(userSession,commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_MANDATARIO_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getMandatarioAdapterForView(userSession)";
				mandatarioAdapterVO = GdeServiceLocator.getDefinicionService().getMandatarioAdapterForView(userSession, commonKey);
				mandatarioAdapterVO.addMessage(BaseError.MSG_ACTIVAR, GdeError.MANDATARIO_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_MANDATARIO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getMandatarioAdapterForView(userSession)";
				mandatarioAdapterVO = GdeServiceLocator.getDefinicionService().getMandatarioAdapterForView(userSession, commonKey);
				mandatarioAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, GdeError.MANDATARIO_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_MANDATARIO_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (mandatarioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + mandatarioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, MandatarioAdapter.NAME, mandatarioAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			mandatarioAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + MandatarioAdapter.NAME + ": "+ mandatarioAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(MandatarioAdapter.NAME, mandatarioAdapterVO);
			// Subo el apdater al userSession
			userSession.put(MandatarioAdapter.NAME, mandatarioAdapterVO);
			 
			saveDemodaMessages(request, mandatarioAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, MandatarioAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_MANDATARIO, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			MandatarioAdapter mandatarioAdapterVO = (MandatarioAdapter) userSession.get(MandatarioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (mandatarioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + MandatarioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, MandatarioAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(mandatarioAdapterVO, request);
			
            // Tiene errores recuperables
			if (mandatarioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + mandatarioAdapterVO.infoString()); 
				saveDemodaErrors(request, mandatarioAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, MandatarioAdapter.NAME, mandatarioAdapterVO);
			}
			
			// llamada al servicio
			MandatarioVO mandatarioVO = GdeServiceLocator.getDefinicionService().createMandatario(userSession, mandatarioAdapterVO.getMandatario());
			
            // Tiene errores recuperables
			if (mandatarioVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + mandatarioVO.infoString()); 
				saveDemodaErrors(request, mandatarioVO);
				return forwardErrorRecoverable(mapping, request, userSession, MandatarioAdapter.NAME, mandatarioAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (mandatarioVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + mandatarioVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, MandatarioAdapter.NAME, mandatarioAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, MandatarioAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, MandatarioAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_MANDATARIO, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			MandatarioAdapter mandatarioAdapterVO = (MandatarioAdapter) userSession.get(MandatarioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (mandatarioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + MandatarioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, MandatarioAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(mandatarioAdapterVO, request);
			
            // Tiene errores recuperables
			if (mandatarioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + mandatarioAdapterVO.infoString()); 
				saveDemodaErrors(request, mandatarioAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, MandatarioAdapter.NAME, mandatarioAdapterVO);
			}
			
			// llamada al servicio
			MandatarioVO mandatarioVO = GdeServiceLocator.getDefinicionService().updateMandatario(userSession, mandatarioAdapterVO.getMandatario());
			
            // Tiene errores recuperables
			if (mandatarioVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + mandatarioAdapterVO.infoString()); 
				saveDemodaErrors(request, mandatarioVO);
				return forwardErrorRecoverable(mapping, request, userSession, MandatarioAdapter.NAME, mandatarioAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (mandatarioVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + mandatarioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, MandatarioAdapter.NAME, mandatarioAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, MandatarioAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, MandatarioAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_MANDATARIO, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			MandatarioAdapter mandatarioAdapterVO = (MandatarioAdapter) userSession.get(MandatarioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (mandatarioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + MandatarioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, MandatarioAdapter.NAME); 
			}

			// llamada al servicio
			MandatarioVO mandatarioVO = GdeServiceLocator.getDefinicionService().deleteMandatario
				(userSession, mandatarioAdapterVO.getMandatario());
			
            // Tiene errores recuperables
			if (mandatarioVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + mandatarioAdapterVO.infoString());
				saveDemodaErrors(request, mandatarioVO);				
				request.setAttribute(MandatarioAdapter.NAME, mandatarioAdapterVO);
				return mapping.findForward(GdeConstants.FWD_MANDATARIO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (mandatarioVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + mandatarioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, MandatarioAdapter.NAME, mandatarioAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, MandatarioAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, MandatarioAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_MANDATARIO, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			MandatarioAdapter mandatarioAdapterVO = (MandatarioAdapter) userSession.get(MandatarioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (mandatarioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + MandatarioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, MandatarioAdapter.NAME); 
			}

			// llamada al servicio
			MandatarioVO mandatarioVO = GdeServiceLocator.getDefinicionService().activarMandatario
				(userSession, mandatarioAdapterVO.getMandatario());
			
            // Tiene errores recuperables
			if (mandatarioVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + mandatarioAdapterVO.infoString());
				saveDemodaErrors(request, mandatarioVO);				
				request.setAttribute(MandatarioAdapter.NAME, mandatarioAdapterVO);
				return mapping.findForward(GdeConstants.FWD_MANDATARIO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (mandatarioVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + mandatarioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, MandatarioAdapter.NAME, mandatarioAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, MandatarioAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, MandatarioAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_MANDATARIO, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			MandatarioAdapter mandatarioAdapterVO = (MandatarioAdapter) userSession.get(MandatarioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (mandatarioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + MandatarioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, MandatarioAdapter.NAME); 
			}

			// llamada al servicio
			MandatarioVO mandatarioVO = GdeServiceLocator.getDefinicionService().desactivarMandatario
				(userSession, mandatarioAdapterVO.getMandatario());
			
            // Tiene errores recuperables
			if (mandatarioVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + mandatarioAdapterVO.infoString());
				saveDemodaErrors(request, mandatarioVO);				
				request.setAttribute(MandatarioAdapter.NAME, mandatarioAdapterVO);
				return mapping.findForward(GdeConstants.FWD_MANDATARIO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (mandatarioVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + mandatarioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, MandatarioAdapter.NAME, mandatarioAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, MandatarioAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, MandatarioAdapter.NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, MandatarioAdapter.NAME);
		
	}		
		
}
