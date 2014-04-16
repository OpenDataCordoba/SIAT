//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.bal.iface.model.LeyParAcuAdapter;
import ar.gov.rosario.siat.bal.iface.model.LeyParAcuVO;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class AdministrarLeyParAcuDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarLeyParAcuDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_LEYPARACU, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		LeyParAcuAdapter leyParAcuAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getLeyParAcuAdapterForView(userSession, commonKey)";
				leyParAcuAdapterVO = BalServiceLocator.getDefinicionService().getLeyParAcuAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_LEYPARACU_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getLeyParAcuAdapterForView(userSession, commonKey)";
				leyParAcuAdapterVO = BalServiceLocator.getDefinicionService().getLeyParAcuAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_LEYPARACU_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getLeyParAcuAdapterForView(userSession, commonKey)";
				leyParAcuAdapterVO = BalServiceLocator.getDefinicionService().getLeyParAcuAdapterForView(userSession, commonKey);				
				leyParAcuAdapterVO.addMessage(BaseError.MSG_ELIMINAR, BalError.LEYPARACU_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_LEYPARACU_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getLeyParAcuAdapterForCreate(userSession)";
				leyParAcuAdapterVO = BalServiceLocator.getDefinicionService().getLeyParAcuAdapterForCreate(userSession);
				actionForward = mapping.findForward(BalConstants.FWD_LEYPARACU_EDIT_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (leyParAcuAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + leyParAcuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LeyParAcuAdapter.NAME, leyParAcuAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			leyParAcuAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LeyParAcuAdapter.NAME + ": "+ leyParAcuAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(LeyParAcuAdapter.NAME, leyParAcuAdapterVO);
			// Subo el apdater al userSession
			userSession.put(LeyParAcuAdapter.NAME, leyParAcuAdapterVO);
			 
			saveDemodaMessages(request, leyParAcuAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LeyParAcuAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_LEYPARACU, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			LeyParAcuAdapter leyParAcuAdapterVO = (LeyParAcuAdapter) userSession.get(LeyParAcuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (leyParAcuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + LeyParAcuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, LeyParAcuAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(leyParAcuAdapterVO, request);
			
            // Tiene errores recuperables
			if (leyParAcuAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + leyParAcuAdapterVO.infoString()); 
				saveDemodaErrors(request, leyParAcuAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, LeyParAcuAdapter.NAME, leyParAcuAdapterVO);
			}
			
			// llamada al servicio
			LeyParAcuVO leyParAcuVO = BalServiceLocator.getDefinicionService().createLeyParAcu(userSession, leyParAcuAdapterVO.getLeyParAcu());
			
            // Tiene errores recuperables
			if (leyParAcuVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + leyParAcuVO.infoString()); 
				saveDemodaErrors(request, leyParAcuVO);
				return forwardErrorRecoverable(mapping, request, userSession, LeyParAcuAdapter.NAME, leyParAcuAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (leyParAcuVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + leyParAcuVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LeyParAcuAdapter.NAME, leyParAcuAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, LeyParAcuAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LeyParAcuAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_LEYPARACU, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			LeyParAcuAdapter leyParAcuAdapterVO = (LeyParAcuAdapter) userSession.get(LeyParAcuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (leyParAcuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + LeyParAcuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, LeyParAcuAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(leyParAcuAdapterVO, request);
			
            // Tiene errores recuperables
			if (leyParAcuAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + leyParAcuAdapterVO.infoString()); 
				saveDemodaErrors(request, leyParAcuAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, LeyParAcuAdapter.NAME, leyParAcuAdapterVO);
			}
			
			// llamada al servicio
			LeyParAcuVO leyParAcuVO = BalServiceLocator.getDefinicionService().updateLeyParAcu(userSession, leyParAcuAdapterVO.getLeyParAcu());
			
            // Tiene errores recuperables
			if (leyParAcuVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + leyParAcuAdapterVO.infoString()); 
				saveDemodaErrors(request, leyParAcuVO);
				return forwardErrorRecoverable(mapping, request, userSession, LeyParAcuAdapter.NAME, leyParAcuAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (leyParAcuVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + leyParAcuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LeyParAcuAdapter.NAME, leyParAcuAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, LeyParAcuAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LeyParAcuAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_LEYPARACU, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			LeyParAcuAdapter leyParAcuAdapterVO = (LeyParAcuAdapter) userSession.get(LeyParAcuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (leyParAcuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + LeyParAcuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, LeyParAcuAdapter.NAME); 
			}

			// llamada al servicio
			LeyParAcuVO leyParAcuVO = BalServiceLocator.getDefinicionService().deleteLeyParAcu(userSession, leyParAcuAdapterVO.getLeyParAcu());
			
            // Tiene errores recuperables
			if (leyParAcuVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + leyParAcuAdapterVO.infoString());
				saveDemodaErrors(request, leyParAcuVO);				
				request.setAttribute(LeyParAcuAdapter.NAME, leyParAcuAdapterVO);
				return mapping.findForward(BalConstants.FWD_LEYPARACU_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (leyParAcuVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + leyParAcuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LeyParAcuAdapter.NAME, leyParAcuAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, LeyParAcuAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LeyParAcuAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, LeyParAcuAdapter.NAME);
		
	}
					

}
