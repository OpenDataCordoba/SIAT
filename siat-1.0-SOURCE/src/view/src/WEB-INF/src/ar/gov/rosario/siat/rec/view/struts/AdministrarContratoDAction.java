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
import ar.gov.rosario.siat.rec.iface.model.ContratoAdapter;
import ar.gov.rosario.siat.rec.iface.model.ContratoVO;
import ar.gov.rosario.siat.rec.iface.service.RecServiceLocator;
import ar.gov.rosario.siat.rec.iface.util.RecError;
import ar.gov.rosario.siat.rec.iface.util.RecSecurityConstants;
import ar.gov.rosario.siat.rec.view.util.RecConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarContratoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarContratoDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_CONTRATO, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ContratoAdapter contratoAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getContratoAdapterForView(userSession, commonKey)";
				contratoAdapterVO = RecServiceLocator.getCdmService().getContratoAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(RecConstants.FWD_CONTRATO_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getContratoAdapterForUpdate(userSession, commonKey)";
				contratoAdapterVO = RecServiceLocator.getCdmService().getContratoAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(RecConstants.FWD_CONTRATO_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getContratoAdapterForView(userSession, commonKey)";
				contratoAdapterVO = RecServiceLocator.getCdmService().getContratoAdapterForView(userSession, commonKey);				
				contratoAdapterVO.addMessage(BaseError.MSG_ELIMINAR, RecError.CONTRATO_LABEL);
				actionForward = mapping.findForward(RecConstants.FWD_CONTRATO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getContratoAdapterForCreate(userSession)";
				contratoAdapterVO = RecServiceLocator.getCdmService().getContratoAdapterForCreate(userSession);
				actionForward = mapping.findForward(RecConstants.FWD_CONTRATO_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getContratoAdapterForView(userSession)";
				contratoAdapterVO = RecServiceLocator.getCdmService().getContratoAdapterForView(userSession, commonKey);
				contratoAdapterVO.addMessage(BaseError.MSG_ACTIVAR, RecError.CONTRATO_LABEL);
				actionForward = mapping.findForward(RecConstants.FWD_CONTRATO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getContratoAdapterForView(userSession)";
				contratoAdapterVO = RecServiceLocator.getCdmService().getContratoAdapterForView(userSession, commonKey);
				contratoAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, RecError.CONTRATO_LABEL);
				actionForward = mapping.findForward(RecConstants.FWD_CONTRATO_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (contratoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + contratoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ContratoAdapter.NAME, contratoAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			contratoAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ContratoAdapter.NAME + ": "+ contratoAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ContratoAdapter.NAME, contratoAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ContratoAdapter.NAME, contratoAdapterVO);
			 
			saveDemodaMessages(request, contratoAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ContratoAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_CONTRATO, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ContratoAdapter contratoAdapterVO = (ContratoAdapter) userSession.get(ContratoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (contratoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ContratoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ContratoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(contratoAdapterVO, request);
			
            // Tiene errores recuperables
			if (contratoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + contratoAdapterVO.infoString()); 
				saveDemodaErrors(request, contratoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ContratoAdapter.NAME, contratoAdapterVO);
			}
			
			// llamada al servicio
			ContratoVO contratoVO = RecServiceLocator.getCdmService().createContrato(userSession, contratoAdapterVO.getContrato());
			
            // Tiene errores recuperables
			if (contratoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + contratoVO.infoString()); 
				saveDemodaErrors(request, contratoVO);
				return forwardErrorRecoverable(mapping, request, userSession, ContratoAdapter.NAME, contratoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (contratoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + contratoVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ContratoAdapter.NAME, contratoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ContratoAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ContratoAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_CONTRATO, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ContratoAdapter contratoAdapterVO = (ContratoAdapter) userSession.get(ContratoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (contratoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ContratoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ContratoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(contratoAdapterVO, request);
			
            // Tiene errores recuperables
			if (contratoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + contratoAdapterVO.infoString()); 
				saveDemodaErrors(request, contratoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ContratoAdapter.NAME, contratoAdapterVO);
			}
			
			// llamada al servicio
			ContratoVO contratoVO = RecServiceLocator.getCdmService().updateContrato(userSession, contratoAdapterVO.getContrato());
			
            // Tiene errores recuperables
			if (contratoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + contratoAdapterVO.infoString()); 
				saveDemodaErrors(request, contratoVO);
				return forwardErrorRecoverable(mapping, request, userSession, ContratoAdapter.NAME, contratoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (contratoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + contratoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ContratoAdapter.NAME, contratoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ContratoAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ContratoAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_CONTRATO, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ContratoAdapter contratoAdapterVO = (ContratoAdapter) userSession.get(ContratoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (contratoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ContratoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ContratoAdapter.NAME); 
			}

			// llamada al servicio
			ContratoVO contratoVO = RecServiceLocator.getCdmService().deleteContrato
				(userSession, contratoAdapterVO.getContrato());
			
            // Tiene errores recuperables
			if (contratoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + contratoAdapterVO.infoString());
				saveDemodaErrors(request, contratoVO);				
				request.setAttribute(ContratoAdapter.NAME, contratoAdapterVO);
				return mapping.findForward(RecConstants.FWD_CONTRATO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (contratoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + contratoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ContratoAdapter.NAME, contratoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ContratoAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ContratoAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_CONTRATO, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ContratoAdapter contratoAdapterVO = (ContratoAdapter) userSession.get(ContratoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (contratoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ContratoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ContratoAdapter.NAME); 
			}

			// llamada al servicio
			ContratoVO contratoVO = RecServiceLocator.getCdmService().activarContrato
				(userSession, contratoAdapterVO.getContrato());
			
            // Tiene errores recuperables
			if (contratoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + contratoAdapterVO.infoString());
				saveDemodaErrors(request, contratoVO);				
				request.setAttribute(ContratoAdapter.NAME, contratoAdapterVO);
				return mapping.findForward(RecConstants.FWD_CONTRATO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (contratoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + contratoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ContratoAdapter.NAME, contratoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ContratoAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ContratoAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_CONTRATO, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ContratoAdapter contratoAdapterVO = (ContratoAdapter) userSession.get(ContratoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (contratoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ContratoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ContratoAdapter.NAME); 
			}

			// llamada al servicio
			ContratoVO contratoVO = RecServiceLocator.getCdmService().desactivarContrato
				(userSession, contratoAdapterVO.getContrato());
			
            // Tiene errores recuperables
			if (contratoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + contratoAdapterVO.infoString());
				saveDemodaErrors(request, contratoVO);				
				request.setAttribute(ContratoAdapter.NAME, contratoAdapterVO);
				return mapping.findForward(RecConstants.FWD_CONTRATO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (contratoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + contratoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ContratoAdapter.NAME, contratoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ContratoAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ContratoAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ContratoAdapter.NAME);
		
	}

}
