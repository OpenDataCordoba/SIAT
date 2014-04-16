//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.view.struts;

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
import ar.gov.rosario.siat.def.iface.model.RecMinDecAdapter;
import ar.gov.rosario.siat.def.iface.model.RecMinDecVO;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarRecMinDecDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarRecMinDecDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_RECMINDEC, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		RecMinDecAdapter recMinDecAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getRecMinDecAdapterForView(userSession, commonKey)";
				recMinDecAdapterVO = DefServiceLocator.getGravamenService().getRecMinDecAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_RECMINDEC_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getRecMinDecAdapterForUpdate(userSession, commonKey)";
				recMinDecAdapterVO = DefServiceLocator.getGravamenService().getRecMinDecAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_RECMINDEC_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getRecMinDecAdapterForView(userSession, commonKey)";
				recMinDecAdapterVO = DefServiceLocator.getGravamenService().getRecMinDecAdapterForView(userSession, commonKey);				
				recMinDecAdapterVO.addMessage(BaseError.MSG_ELIMINAR, DefError.RECMINDEC_LABEL);
				actionForward = mapping.findForward(DefConstants.FWD_RECMINDEC_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getRecMinDecAdapterForCreate(userSession)";
				recMinDecAdapterVO = DefServiceLocator.getGravamenService().getRecMinDecAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_RECMINDEC_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getRecMinDecAdapterForView(userSession)";
				recMinDecAdapterVO = DefServiceLocator.getGravamenService().getRecMinDecAdapterForView(userSession, commonKey);
				recMinDecAdapterVO.addMessage(BaseError.MSG_ACTIVAR, DefError.RECMINDEC_LABEL);
				actionForward = mapping.findForward(DefConstants.FWD_RECMINDEC_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getRecMinDecAdapterForView(userSession)";
				recMinDecAdapterVO = DefServiceLocator.getGravamenService().getRecMinDecAdapterForView(userSession, commonKey);
				recMinDecAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, DefError.RECMINDEC_LABEL);
				actionForward = mapping.findForward(DefConstants.FWD_RECMINDEC_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (recMinDecAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + recMinDecAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, RecMinDecAdapter.NAME, recMinDecAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			recMinDecAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + RecMinDecAdapter.NAME + ": "+ recMinDecAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(RecMinDecAdapter.NAME, recMinDecAdapterVO);
			// Subo el apdater al userSession
			userSession.put(RecMinDecAdapter.NAME, recMinDecAdapterVO);
			 
			saveDemodaMessages(request, recMinDecAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, RecMinDecAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_RECMINDEC, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			RecMinDecAdapter recMinDecAdapterVO = (RecMinDecAdapter) userSession.get(RecMinDecAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (recMinDecAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + RecMinDecAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, RecMinDecAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(recMinDecAdapterVO, request);
			
            // Tiene errores recuperables
			if (recMinDecAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + recMinDecAdapterVO.infoString()); 
				saveDemodaErrors(request, recMinDecAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, RecMinDecAdapter.NAME, recMinDecAdapterVO);
			}
			
			// llamada al servicio
			RecMinDecVO recMinDecVO = DefServiceLocator.getGravamenService().createRecMinDec(userSession, recMinDecAdapterVO.getRecMinDec());
			
            // Tiene errores recuperables
			if (recMinDecVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + recMinDecVO.infoString()); 
				saveDemodaErrors(request, recMinDecVO);
				return forwardErrorRecoverable(mapping, request, userSession, RecMinDecAdapter.NAME, recMinDecAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (recMinDecVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + recMinDecVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, RecMinDecAdapter.NAME, recMinDecAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, RecMinDecAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, RecMinDecAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_RECMINDEC, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			RecMinDecAdapter recMinDecAdapterVO = (RecMinDecAdapter) userSession.get(RecMinDecAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (recMinDecAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + RecMinDecAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, RecMinDecAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(recMinDecAdapterVO, request);
			
            // Tiene errores recuperables
			if (recMinDecAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + recMinDecAdapterVO.infoString()); 
				saveDemodaErrors(request, recMinDecAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, RecMinDecAdapter.NAME, recMinDecAdapterVO);
			}
			
			// llamada al servicio
			RecMinDecVO recMinDecVO = DefServiceLocator.getGravamenService().updateRecMinDec(userSession, recMinDecAdapterVO.getRecMinDec());
			
            // Tiene errores recuperables
			if (recMinDecVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + recMinDecAdapterVO.infoString()); 
				saveDemodaErrors(request, recMinDecVO);
				return forwardErrorRecoverable(mapping, request, userSession, RecMinDecAdapter.NAME, recMinDecAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (recMinDecVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + recMinDecAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, RecMinDecAdapter.NAME, recMinDecAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, RecMinDecAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, RecMinDecAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_RECMINDEC, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			RecMinDecAdapter recMinDecAdapterVO = (RecMinDecAdapter) userSession.get(RecMinDecAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (recMinDecAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + RecMinDecAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, RecMinDecAdapter.NAME); 
			}

			// llamada al servicio
			RecMinDecVO recMinDecVO = DefServiceLocator.getGravamenService().deleteRecMinDec
				(userSession, recMinDecAdapterVO.getRecMinDec());
			
            // Tiene errores recuperables
			if (recMinDecVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + recMinDecAdapterVO.infoString());
				saveDemodaErrors(request, recMinDecVO);				
				request.setAttribute(RecMinDecAdapter.NAME, recMinDecAdapterVO);
				return mapping.findForward(DefConstants.FWD_RECMINDEC_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (recMinDecVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + recMinDecAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, RecMinDecAdapter.NAME, recMinDecAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, RecMinDecAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, RecMinDecAdapter.NAME);
		}
	}
	
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, RecMinDecAdapter.NAME);
		
	}
	
}
