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

import ar.gov.rosario.siat.bal.iface.model.ParSelAdapter;
import ar.gov.rosario.siat.bal.iface.model.ParSelVO;
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

public final class AdministrarParSelDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarParSelDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_PARSEL, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ParSelAdapter parSelAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getParSelAdapterForView(userSession, commonKey)";
				parSelAdapterVO = BalServiceLocator.getDefinicionService().getParSelAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_PARSEL_VIEW_ADAPTER);
			}
			
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getParSelAdapterForUpdate(userSession, commonKey)";
				parSelAdapterVO = BalServiceLocator.getDefinicionService().getParSelAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_PARSEL_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getParSelAdapterAdapterForView(userSession, commonKey)";
				parSelAdapterVO = BalServiceLocator.getDefinicionService().getParSelAdapterForView(userSession, commonKey);				
				parSelAdapterVO.addMessage(BaseError.MSG_ELIMINAR, BalError.PARSEL_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_PARSEL_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getParSelAdapterForCreate(userSession)";
				parSelAdapterVO = BalServiceLocator.getDefinicionService().getParSelAdapterForCreate(userSession,commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_PARSEL_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getParSelAdapterForView(userSession)";
				parSelAdapterVO = BalServiceLocator.getDefinicionService().getParSelAdapterForView(userSession, commonKey);
				parSelAdapterVO.addMessage(BaseError.MSG_ACTIVAR, BalError.PARSEL_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_PARSEL_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getParSelAdapterForView(userSession)";
				parSelAdapterVO = BalServiceLocator.getDefinicionService().getParSelAdapterForView(userSession, commonKey);
				parSelAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, BalError.PARSEL_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_PARSEL_VIEW_ADAPTER);				
			}

			
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (parSelAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + parSelAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ParSelAdapter.NAME, parSelAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			parSelAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ParSelAdapter.NAME + ": "+ parSelAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ParSelAdapter.NAME, parSelAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ParSelAdapter.NAME, parSelAdapterVO);
			 
			saveDemodaMessages(request, parSelAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ParSelAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_PARSEL, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ParSelAdapter parSelAdapterVO = (ParSelAdapter) userSession.get(ParSelAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (parSelAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ParSelAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ParSelAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(parSelAdapterVO, request);
			
            // Tiene errores recuperables
			if (parSelAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + parSelAdapterVO.infoString()); 
				saveDemodaErrors(request, parSelAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ParSelAdapter.NAME, parSelAdapterVO);
			}
			
			// llamada al servicio
			ParSelVO parSelVO = BalServiceLocator.getDefinicionService().createParSel(userSession, parSelAdapterVO.getParSel());
			
            // Tiene errores recuperables
			if (parSelVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + parSelVO.infoString()); 
				saveDemodaErrors(request, parSelVO);
				return forwardErrorRecoverable(mapping, request, userSession, ParSelAdapter.NAME, parSelAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (parSelVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + parSelVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ParSelAdapter.NAME, parSelAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ParSelAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ParSelAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_PARSEL, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ParSelAdapter parSelAdapterVO = (ParSelAdapter) userSession.get(ParSelAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (parSelAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ParSelAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ParSelAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(parSelAdapterVO, request);
			
            // Tiene errores recuperables
			if (parSelAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + parSelAdapterVO.infoString()); 
				saveDemodaErrors(request, parSelAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ParSelAdapter.NAME, parSelAdapterVO);
			}
			
			// llamada al servicio
			ParSelVO parSelVO = BalServiceLocator.getDefinicionService().updateParSel(userSession, parSelAdapterVO.getParSel());
			
            // Tiene errores recuperables
			if (parSelVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + parSelAdapterVO.infoString()); 
				saveDemodaErrors(request, parSelVO);
				return forwardErrorRecoverable(mapping, request, userSession, ParSelAdapter.NAME, parSelAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (parSelVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + parSelAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ParSelAdapter.NAME, parSelAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ParSelAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ParSelAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_PARSEL, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ParSelAdapter parSelAdapterVO = (ParSelAdapter) userSession.get(ParSelAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (parSelAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ParSelAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ParSelAdapter.NAME); 
			}

			// llamada al servicio
			ParSelVO parSelVO = BalServiceLocator.getDefinicionService().deleteParSel
				(userSession, parSelAdapterVO.getParSel());
			
            // Tiene errores recuperables
			if (parSelVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + parSelAdapterVO.infoString());
				saveDemodaErrors(request, parSelVO);				
				request.setAttribute(ParSelAdapter.NAME, parSelAdapterVO);
				return mapping.findForward(BalConstants.FWD_PARSEL_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (parSelVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + parSelAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ParSelAdapter.NAME, parSelAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ParSelAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ParSelAdapter.NAME);
		}
	}
	
		
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ParSelAdapter.NAME);
		
	}
	
}
