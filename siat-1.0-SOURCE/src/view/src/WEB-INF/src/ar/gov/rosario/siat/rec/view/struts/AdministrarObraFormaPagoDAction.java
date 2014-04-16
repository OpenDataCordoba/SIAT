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
import ar.gov.rosario.siat.rec.iface.model.ObraAdapter;
import ar.gov.rosario.siat.rec.iface.model.ObraFormaPagoAdapter;
import ar.gov.rosario.siat.rec.iface.model.ObraFormaPagoVO;
import ar.gov.rosario.siat.rec.iface.service.RecServiceLocator;
import ar.gov.rosario.siat.rec.iface.util.RecError;
import ar.gov.rosario.siat.rec.iface.util.RecSecurityConstants;
import ar.gov.rosario.siat.rec.view.util.RecConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarObraFormaPagoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarObraFormaPagoDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_OBRA_FORMAPAGO, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ObraFormaPagoAdapter obraFormaPagoAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getObraFormaPagoAdapterForView(userSession, commonKey)";
				obraFormaPagoAdapterVO = RecServiceLocator.getCdmService().getObraFormaPagoAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(RecConstants.FWD_OBRAFORMAPAGO_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getObraFormaPagoAdapterForUpdate(userSession, commonKey)";
				obraFormaPagoAdapterVO = RecServiceLocator.getCdmService().getObraFormaPagoAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(RecConstants.FWD_OBRAFORMAPAGO_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getObraFormaPagoAdapterForView(userSession, commonKey)";
				obraFormaPagoAdapterVO = RecServiceLocator.getCdmService().getObraFormaPagoAdapterForView(userSession, commonKey);				
				obraFormaPagoAdapterVO.addMessage(BaseError.MSG_ELIMINAR, RecError.OBRAFORMAPAGO_LABEL);
				actionForward = mapping.findForward(RecConstants.FWD_OBRAFORMAPAGO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				ObraAdapter obraAdapter = (ObraAdapter) userSession.get(ObraAdapter.NAME);
				ObraFormaPagoAdapter obraFormaPagoAdapter = new ObraFormaPagoAdapter(obraAdapter.getObra());
				
				stringServicio = "getObraFormaPagoAdapterForCreate(userSession, obraFormaPagoAdapter)";
				obraFormaPagoAdapterVO = RecServiceLocator.getCdmService().getObraFormaPagoAdapterForCreate(userSession, obraFormaPagoAdapter);
				actionForward = mapping.findForward(RecConstants.FWD_OBRAFORMAPAGO_EDIT_ADAPTER);				
			}

			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getObraFormaPagoAdapterForView(userSession)";
				obraFormaPagoAdapterVO = RecServiceLocator.getCdmService().getObraFormaPagoAdapterForView(userSession, commonKey);
				obraFormaPagoAdapterVO.addMessage(BaseError.MSG_ACTIVAR, RecError.OBRAFORMAPAGO_LABEL);
				actionForward = mapping.findForward(RecConstants.FWD_OBRAFORMAPAGO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getObraFormaPagoAdapterForView(userSession)";
				obraFormaPagoAdapterVO = RecServiceLocator.getCdmService().getObraFormaPagoAdapterForView(userSession, commonKey);
				obraFormaPagoAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, RecError.OBRAFORMAPAGO_LABEL);
				actionForward = mapping.findForward(RecConstants.FWD_OBRAFORMAPAGO_VIEW_ADAPTER);				
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (obraFormaPagoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + obraFormaPagoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ObraFormaPagoAdapter.NAME, obraFormaPagoAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			obraFormaPagoAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ObraFormaPagoAdapter.NAME + ": "+ obraFormaPagoAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ObraFormaPagoAdapter.NAME, obraFormaPagoAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ObraFormaPagoAdapter.NAME, obraFormaPagoAdapterVO);
			 
			saveDemodaMessages(request, obraFormaPagoAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ObraFormaPagoAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_OBRA_FORMAPAGO, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ObraFormaPagoAdapter obraFormaPagoAdapterVO = (ObraFormaPagoAdapter) userSession.get(ObraFormaPagoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (obraFormaPagoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ObraFormaPagoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ObraFormaPagoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(obraFormaPagoAdapterVO, request);
			
            // Tiene errores recuperables
			if (obraFormaPagoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + obraFormaPagoAdapterVO.infoString()); 
				saveDemodaErrors(request, obraFormaPagoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ObraFormaPagoAdapter.NAME, obraFormaPagoAdapterVO);
			}
			
			// llamada al servicio
			ObraFormaPagoVO obraFormaPagoVO = RecServiceLocator.getCdmService().createObraFormaPago(userSession, obraFormaPagoAdapterVO.getObraFormaPago());
			
            // Tiene errores recuperables
			if (obraFormaPagoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + obraFormaPagoVO.infoString()); 
				saveDemodaErrors(request, obraFormaPagoVO);
				return forwardErrorRecoverable(mapping, request, userSession, ObraFormaPagoAdapter.NAME, obraFormaPagoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (obraFormaPagoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + obraFormaPagoVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ObraFormaPagoAdapter.NAME, obraFormaPagoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ObraFormaPagoAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ObraFormaPagoAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_OBRA_FORMAPAGO, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ObraFormaPagoAdapter obraFormaPagoAdapterVO = (ObraFormaPagoAdapter) userSession.get(ObraFormaPagoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (obraFormaPagoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ObraFormaPagoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ObraFormaPagoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(obraFormaPagoAdapterVO, request);
			
            // Tiene errores recuperables
			if (obraFormaPagoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + obraFormaPagoAdapterVO.infoString()); 
				saveDemodaErrors(request, obraFormaPagoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ObraFormaPagoAdapter.NAME, obraFormaPagoAdapterVO);
			}
			
			// llamada al servicio
			ObraFormaPagoVO obraFormaPagoVO = RecServiceLocator.getCdmService().updateObraFormaPago(userSession, obraFormaPagoAdapterVO.getObraFormaPago());
			
            // Tiene errores recuperables
			if (obraFormaPagoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + obraFormaPagoAdapterVO.infoString()); 
				saveDemodaErrors(request, obraFormaPagoVO);
				return forwardErrorRecoverable(mapping, request, userSession, ObraFormaPagoAdapter.NAME, obraFormaPagoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (obraFormaPagoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + obraFormaPagoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ObraFormaPagoAdapter.NAME, obraFormaPagoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ObraFormaPagoAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ObraFormaPagoAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_OBRA_FORMAPAGO, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ObraFormaPagoAdapter obraFormaPagoAdapterVO = (ObraFormaPagoAdapter) userSession.get(ObraFormaPagoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (obraFormaPagoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ObraFormaPagoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ObraFormaPagoAdapter.NAME); 
			}

			// llamada al servicio
			ObraFormaPagoVO obraFormaPagoVO = RecServiceLocator.getCdmService().deleteObraFormaPago
				(userSession, obraFormaPagoAdapterVO.getObraFormaPago());
			
            // Tiene errores recuperables
			if (obraFormaPagoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + obraFormaPagoAdapterVO.infoString());
				saveDemodaErrors(request, obraFormaPagoVO);				
				request.setAttribute(ObraFormaPagoAdapter.NAME, obraFormaPagoAdapterVO);
				return mapping.findForward(RecConstants.FWD_OBRAFORMAPAGO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (obraFormaPagoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + obraFormaPagoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ObraFormaPagoAdapter.NAME, obraFormaPagoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ObraFormaPagoAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ObraFormaPagoAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_OBRA_FORMAPAGO, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ObraFormaPagoAdapter obraFormaPagoAdapterVO = (ObraFormaPagoAdapter) userSession.get(ObraFormaPagoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (obraFormaPagoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ObraFormaPagoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ObraFormaPagoAdapter.NAME); 
			}

			// llamada al servicio
			ObraFormaPagoVO obraFormaPagoVO = RecServiceLocator.getCdmService().activarObraFormaPago
				(userSession, obraFormaPagoAdapterVO.getObraFormaPago());
			
            // Tiene errores recuperables
			if (obraFormaPagoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + obraFormaPagoAdapterVO.infoString());
				saveDemodaErrors(request, obraFormaPagoVO);				
				request.setAttribute(ObraFormaPagoAdapter.NAME, obraFormaPagoAdapterVO);
				return mapping.findForward(RecConstants.FWD_OBRAFORMAPAGO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (obraFormaPagoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + obraFormaPagoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ObraFormaPagoAdapter.NAME, obraFormaPagoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ObraFormaPagoAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ObraFormaPagoAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_OBRA_FORMAPAGO, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ObraFormaPagoAdapter obraFormaPagoAdapterVO = (ObraFormaPagoAdapter) userSession.get(ObraFormaPagoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (obraFormaPagoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ObraFormaPagoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ObraFormaPagoAdapter.NAME); 
			}

			// llamada al servicio
			ObraFormaPagoVO obraFormaPagoVO = RecServiceLocator.getCdmService().desactivarObraFormaPago
				(userSession, obraFormaPagoAdapterVO.getObraFormaPago());
			
            // Tiene errores recuperables
			if (obraFormaPagoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + obraFormaPagoAdapterVO.infoString());
				saveDemodaErrors(request, obraFormaPagoVO);				
				request.setAttribute(ObraFormaPagoAdapter.NAME, obraFormaPagoAdapterVO);
				return mapping.findForward(RecConstants.FWD_OBRAFORMAPAGO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (obraFormaPagoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + obraFormaPagoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ObraFormaPagoAdapter.NAME, obraFormaPagoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ObraFormaPagoAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ObraFormaPagoAdapter.NAME);
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
				ObraFormaPagoAdapter obraformaPagoAdapterVO = (ObraFormaPagoAdapter) userSession.get(ObraFormaPagoAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (obraformaPagoAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + ObraFormaPagoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ObraFormaPagoAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(obraformaPagoAdapterVO, request);
				
	            // Tiene errores recuperables
				if (obraformaPagoAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + obraformaPagoAdapterVO.infoString()); 
					saveDemodaErrors(request, obraformaPagoAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, ObraFormaPagoAdapter.NAME, obraformaPagoAdapterVO);
				}
				
				// llamada al servicio
				obraformaPagoAdapterVO = RecServiceLocator.getCdmService().getObraFormaPagoAdapterParamEsEspecial
					(userSession, obraformaPagoAdapterVO);

	            // Tiene errores recuperables
				if (obraformaPagoAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + obraformaPagoAdapterVO.infoString()); 
					saveDemodaErrors(request, obraformaPagoAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, ObraFormaPagoAdapter.NAME, obraformaPagoAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (obraformaPagoAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + obraformaPagoAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, ObraFormaPagoAdapter.NAME, obraformaPagoAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(ObraFormaPagoAdapter.NAME, obraformaPagoAdapterVO);
				// Subo el apdater al userSession
				userSession.put(ObraFormaPagoAdapter.NAME, obraformaPagoAdapterVO);
				
				return mapping.findForward(RecConstants.FWD_OBRAFORMAPAGO_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ObraFormaPagoAdapter.NAME);
			}
		}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ObraFormaPagoAdapter.NAME);
		
	}
	
}
