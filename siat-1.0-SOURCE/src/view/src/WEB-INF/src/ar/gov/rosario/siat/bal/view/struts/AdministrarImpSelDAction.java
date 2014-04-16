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

import ar.gov.rosario.siat.bal.iface.model.ImpSelAdapter;
import ar.gov.rosario.siat.bal.iface.model.ImpSelVO;
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

public final class AdministrarImpSelDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarImpSelDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_IMPSEL, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ImpSelAdapter impSelAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getImpSelAdapterForView(userSession, commonKey)";
				impSelAdapterVO = BalServiceLocator.getDefinicionService().getImpSelAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_IMPSEL_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getImpSelAdapterForUpdate(userSession, commonKey)";
				impSelAdapterVO = BalServiceLocator.getDefinicionService().getImpSelAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_IMPSEL_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getImpSelAdapterForView(userSession, commonKey)";
				impSelAdapterVO = BalServiceLocator.getDefinicionService().getImpSelAdapterForView(userSession, commonKey);				
				impSelAdapterVO.addMessage(BaseError.MSG_ELIMINAR, BalError.IMPSEL_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_IMPSEL_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getImpSelAdapterForCreate(userSession)";
				impSelAdapterVO = BalServiceLocator.getDefinicionService().getImpSelAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_IMPSEL_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getImpSelAdapterForView(userSession)";
				impSelAdapterVO = BalServiceLocator.getDefinicionService().getImpSelAdapterForView(userSession, commonKey);
				impSelAdapterVO.addMessage(BaseError.MSG_ACTIVAR, BalError.IMPSEL_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_IMPSEL_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getImpSelAdapterForView(userSession)";
				impSelAdapterVO = BalServiceLocator.getDefinicionService().getImpSelAdapterForView(userSession, commonKey);
				impSelAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, BalError.IMPSEL_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_IMPSEL_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (impSelAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + impSelAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ImpSelAdapter.NAME, impSelAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			impSelAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ImpSelAdapter.NAME + ": "+ impSelAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ImpSelAdapter.NAME, impSelAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ImpSelAdapter.NAME, impSelAdapterVO);
			 
			saveDemodaMessages(request, impSelAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ImpSelAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_IMPSEL, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ImpSelAdapter impSelAdapterVO = (ImpSelAdapter) userSession.get(ImpSelAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (impSelAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ImpSelAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ImpSelAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(impSelAdapterVO, request);
			
            // Tiene errores recuperables
			if (impSelAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + impSelAdapterVO.infoString()); 
				saveDemodaErrors(request, impSelAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ImpSelAdapter.NAME, impSelAdapterVO);
			}
			
			// llamada al servicio
			ImpSelVO impSelVO = BalServiceLocator.getDefinicionService().createImpSel(userSession, impSelAdapterVO.getImpSel());
			
            // Tiene errores recuperables
			if (impSelVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + impSelVO.infoString()); 
				saveDemodaErrors(request, impSelVO);
				return forwardErrorRecoverable(mapping, request, userSession, ImpSelAdapter.NAME, impSelAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (impSelVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + impSelVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ImpSelAdapter.NAME, impSelAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ImpSelAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ImpSelAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_IMPSEL, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ImpSelAdapter impSelAdapterVO = (ImpSelAdapter) userSession.get(ImpSelAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (impSelAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ImpSelAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ImpSelAdapter.NAME); 
			}
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(impSelAdapterVO, request);
			
            // Tiene errores recuperables
			if (impSelAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + impSelAdapterVO.infoString()); 
				saveDemodaErrors(request, impSelAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ImpSelAdapter.NAME, impSelAdapterVO);
			}
			
						
			// llamada al servicio
			ImpSelVO impSelVO = BalServiceLocator.getDefinicionService().updateImpSel(userSession, impSelAdapterVO.getImpSel());
			 
            // Tiene errores recuperables
			if (impSelVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + impSelAdapterVO.infoString()); 
				saveDemodaErrors(request, impSelVO);
				return forwardErrorRecoverable(mapping, request, userSession, ImpSelAdapter.NAME, impSelAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (impSelVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + impSelAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ImpSelAdapter.NAME, impSelAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ImpSelAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ImpSelAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_IMPSEL, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ImpSelAdapter impSelAdapterVO = (ImpSelAdapter) userSession.get(ImpSelAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (impSelAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ImpSelAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ImpSelAdapter.NAME); 
			}

			// llamada al servicio
			ImpSelVO impSelVO = BalServiceLocator.getDefinicionService().deleteImpSel
				(userSession, impSelAdapterVO.getImpSel());
			
            // Tiene errores recuperables
			if (impSelVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + impSelAdapterVO.infoString());
				saveDemodaErrors(request, impSelVO);				
				request.setAttribute(ImpSelAdapter.NAME, impSelAdapterVO);
				return mapping.findForward(BalConstants.FWD_IMPSEL_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (impSelVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + impSelAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ImpSelAdapter.NAME, impSelAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ImpSelAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ImpSelAdapter.NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, ImpSelAdapter.NAME);
			
		}
}
