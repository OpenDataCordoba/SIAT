//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.view.struts;

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
import ar.gov.rosario.siat.rec.iface.model.FormaPagoAdapter;
import ar.gov.rosario.siat.rec.iface.model.FormaPagoVO;
import ar.gov.rosario.siat.rec.iface.service.RecServiceLocator;
import ar.gov.rosario.siat.rec.iface.util.RecError;
import ar.gov.rosario.siat.rec.iface.util.RecSecurityConstants;
import ar.gov.rosario.siat.rec.view.util.RecConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarFormaPagoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarFormaPagoDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_FORMAPAGO, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		FormaPagoAdapter formaPagoAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getFormaPagoAdapterForView(userSession, commonKey)";
				formaPagoAdapterVO = RecServiceLocator.getCdmService().getFormaPagoAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(RecConstants.FWD_FORMAPAGO_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getFormaPagoAdapterForUpdate(userSession, commonKey)";
				formaPagoAdapterVO = RecServiceLocator.getCdmService().getFormaPagoAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(RecConstants.FWD_FORMAPAGO_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getFormaPagoAdapterForView(userSession, commonKey)";
				formaPagoAdapterVO = RecServiceLocator.getCdmService().getFormaPagoAdapterForView(userSession, commonKey);				
				formaPagoAdapterVO.addMessage(BaseError.MSG_ELIMINAR, RecError.FORMAPAGO_LABEL);
				actionForward = mapping.findForward(RecConstants.FWD_FORMAPAGO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getFormaPagoAdapterForCreate(userSession)";
				formaPagoAdapterVO = RecServiceLocator.getCdmService().getFormaPagoAdapterForCreate(userSession);
				actionForward = mapping.findForward(RecConstants.FWD_FORMAPAGO_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getFormaPagoAdapterForView(userSession)";
				formaPagoAdapterVO = RecServiceLocator.getCdmService().getFormaPagoAdapterForView(userSession, commonKey);
				formaPagoAdapterVO.addMessage(BaseError.MSG_ACTIVAR, RecError.FORMAPAGO_LABEL);
				actionForward = mapping.findForward(RecConstants.FWD_FORMAPAGO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getFormaPagoAdapterForView(userSession)";
				formaPagoAdapterVO = RecServiceLocator.getCdmService().getFormaPagoAdapterForView(userSession, commonKey);
				formaPagoAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, RecError.FORMAPAGO_LABEL);
				actionForward = mapping.findForward(RecConstants.FWD_FORMAPAGO_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (formaPagoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + formaPagoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, FormaPagoAdapter.NAME, formaPagoAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			formaPagoAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + FormaPagoAdapter.NAME + ": "+ formaPagoAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(FormaPagoAdapter.NAME, formaPagoAdapterVO);
			// Subo el apdater al userSession
			userSession.put(FormaPagoAdapter.NAME, formaPagoAdapterVO);
			 
			saveDemodaMessages(request, formaPagoAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, FormaPagoAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_FORMAPAGO, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			FormaPagoAdapter formaPagoAdapterVO = (FormaPagoAdapter) userSession.get(FormaPagoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (formaPagoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + FormaPagoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, FormaPagoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(formaPagoAdapterVO, request);
			
            // Tiene errores recuperables
			if (formaPagoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + formaPagoAdapterVO.infoString()); 
				saveDemodaErrors(request, formaPagoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, FormaPagoAdapter.NAME, formaPagoAdapterVO);
			}
			
			// llamada al servicio
			FormaPagoVO formaPagoVO = RecServiceLocator.getCdmService().createFormaPago(userSession, formaPagoAdapterVO.getFormaPago());
			
            // Tiene errores recuperables
			if (formaPagoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + formaPagoVO.infoString()); 
				saveDemodaErrors(request, formaPagoVO);
				return forwardErrorRecoverable(mapping, request, userSession, FormaPagoAdapter.NAME, formaPagoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (formaPagoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + formaPagoVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, FormaPagoAdapter.NAME, formaPagoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, FormaPagoAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, FormaPagoAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_FORMAPAGO, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			FormaPagoAdapter formaPagoAdapterVO = (FormaPagoAdapter) userSession.get(FormaPagoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (formaPagoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + FormaPagoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, FormaPagoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(formaPagoAdapterVO, request);
			
            // Tiene errores recuperables
			if (formaPagoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + formaPagoAdapterVO.infoString()); 
				saveDemodaErrors(request, formaPagoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, FormaPagoAdapter.NAME, formaPagoAdapterVO);
			}
			
			// llamada al servicio
			FormaPagoVO formaPagoVO = RecServiceLocator.getCdmService().updateFormaPago(userSession, formaPagoAdapterVO.getFormaPago());
			
            // Tiene errores recuperables
			if (formaPagoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + formaPagoAdapterVO.infoString()); 
				saveDemodaErrors(request, formaPagoVO);
				return forwardErrorRecoverable(mapping, request, userSession, FormaPagoAdapter.NAME, formaPagoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (formaPagoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + formaPagoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, FormaPagoAdapter.NAME, formaPagoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, FormaPagoAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, FormaPagoAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_FORMAPAGO, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			FormaPagoAdapter formaPagoAdapterVO = (FormaPagoAdapter) userSession.get(FormaPagoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (formaPagoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + FormaPagoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, FormaPagoAdapter.NAME); 
			}

			// llamada al servicio
			FormaPagoVO formaPagoVO = RecServiceLocator.getCdmService().deleteFormaPago
				(userSession, formaPagoAdapterVO.getFormaPago());
			
            // Tiene errores recuperables
			if (formaPagoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + formaPagoAdapterVO.infoString());
				saveDemodaErrors(request, formaPagoVO);				
				request.setAttribute(FormaPagoAdapter.NAME, formaPagoAdapterVO);
				return mapping.findForward(RecConstants.FWD_FORMAPAGO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (formaPagoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + formaPagoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, FormaPagoAdapter.NAME, formaPagoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, FormaPagoAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, FormaPagoAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_FORMAPAGO, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			FormaPagoAdapter formaPagoAdapterVO = (FormaPagoAdapter) userSession.get(FormaPagoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (formaPagoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + FormaPagoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, FormaPagoAdapter.NAME); 
			}

			// llamada al servicio
			FormaPagoVO formaPagoVO = RecServiceLocator.getCdmService().activarFormaPago
				(userSession, formaPagoAdapterVO.getFormaPago());
			
            // Tiene errores recuperables
			if (formaPagoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + formaPagoAdapterVO.infoString());
				saveDemodaErrors(request, formaPagoVO);				
				request.setAttribute(FormaPagoAdapter.NAME, formaPagoAdapterVO);
				return mapping.findForward(RecConstants.FWD_FORMAPAGO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (formaPagoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + formaPagoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, FormaPagoAdapter.NAME, formaPagoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, FormaPagoAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, FormaPagoAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_FORMAPAGO, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			FormaPagoAdapter formaPagoAdapterVO = (FormaPagoAdapter) userSession.get(FormaPagoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (formaPagoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + FormaPagoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, FormaPagoAdapter.NAME); 
			}

			// llamada al servicio
			FormaPagoVO formaPagoVO = RecServiceLocator.getCdmService().desactivarFormaPago
				(userSession, formaPagoAdapterVO.getFormaPago());
			
            // Tiene errores recuperables
			if (formaPagoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + formaPagoAdapterVO.infoString());
				saveDemodaErrors(request, formaPagoVO);				
				request.setAttribute(FormaPagoAdapter.NAME, formaPagoAdapterVO);
				return mapping.findForward(RecConstants.FWD_FORMAPAGO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (formaPagoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + formaPagoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, FormaPagoAdapter.NAME, formaPagoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, FormaPagoAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, FormaPagoAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, FormaPagoAdapter.NAME);
		
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
			FormaPagoAdapter formaPagoAdapterVO = (FormaPagoAdapter) userSession.get(FormaPagoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (formaPagoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + FormaPagoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, FormaPagoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(formaPagoAdapterVO, request);
			
            // Tiene errores recuperables
			if (formaPagoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + formaPagoAdapterVO.infoString()); 
				saveDemodaErrors(request, formaPagoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, FormaPagoAdapter.NAME, formaPagoAdapterVO);
			}
			
			// llamada al servicio
			formaPagoAdapterVO = RecServiceLocator.getCdmService().getFormaPagoAdapterParamRecurso
				(userSession, formaPagoAdapterVO);
			
            // Tiene errores recuperables
			if (formaPagoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + formaPagoAdapterVO.infoString()); 
				saveDemodaErrors(request, formaPagoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, FormaPagoAdapter.NAME, formaPagoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (formaPagoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + formaPagoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, FormaPagoAdapter.NAME, formaPagoAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(FormaPagoAdapter.NAME, formaPagoAdapterVO);
			// Subo el apdater al userSession
			userSession.put(FormaPagoAdapter.NAME, formaPagoAdapterVO);
			
			return mapping.findForward(RecConstants.FWD_FORMAPAGO_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, FormaPagoAdapter.NAME);
		}
	}
	
	public ActionForward paramEsEspecial (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				FormaPagoAdapter obraFormaPagoAdapterVO = (FormaPagoAdapter) userSession.get(FormaPagoAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (obraFormaPagoAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + FormaPagoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, FormaPagoAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(obraFormaPagoAdapterVO, request);
				
	            // Tiene errores recuperables
				if (obraFormaPagoAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + obraFormaPagoAdapterVO.infoString()); 
					saveDemodaErrors(request, obraFormaPagoAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, FormaPagoAdapter.NAME, obraFormaPagoAdapterVO);
				}
				
				// llamada al servicio
				obraFormaPagoAdapterVO = RecServiceLocator.getCdmService().getFormaPagoAdapterParamEsEspecial
					(userSession, obraFormaPagoAdapterVO);

	            // Tiene errores recuperables
				if (obraFormaPagoAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + obraFormaPagoAdapterVO.infoString()); 
					saveDemodaErrors(request, obraFormaPagoAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, FormaPagoAdapter.NAME, obraFormaPagoAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (obraFormaPagoAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + obraFormaPagoAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, FormaPagoAdapter.NAME, obraFormaPagoAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(FormaPagoAdapter.NAME, obraFormaPagoAdapterVO);
				// Subo el apdater al userSession
				userSession.put(FormaPagoAdapter.NAME, obraFormaPagoAdapterVO);
				
				return mapping.findForward(RecConstants.FWD_FORMAPAGO_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, FormaPagoAdapter.NAME);
			}
		}

}
