//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.view.struts;

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
import ar.gov.rosario.siat.exe.iface.model.TipoSujetoAdapter;
import ar.gov.rosario.siat.exe.iface.model.TipoSujetoVO;
import ar.gov.rosario.siat.exe.iface.service.ExeServiceLocator;
import ar.gov.rosario.siat.exe.iface.util.ExeError;
import ar.gov.rosario.siat.exe.iface.util.ExeSecurityConstants;
import ar.gov.rosario.siat.exe.view.util.ExeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarTipoSujetoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarTipoSujetoDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_TIPOSUJETO, act);		
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		TipoSujetoAdapter tipoSujetoAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
				stringServicio = "getTipoSujetoAdapterForView(userSession, commonKey)";
				tipoSujetoAdapterVO = ExeServiceLocator.getDefinicionService().getTipoSujetoAdapterForView
					(userSession, commonKey);
				actionForward = mapping.findForward(ExeConstants.FWD_TIPOSUJETO_VIEW_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getTipoSujetoAdapterForUpdate(userSession, commonKey)";
				tipoSujetoAdapterVO = ExeServiceLocator.getDefinicionService().getTipoSujetoAdapterForUpdate
					(userSession, commonKey);
				actionForward = mapping.findForward(ExeConstants.FWD_TIPOSUJETO_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getTipoSujetoAdapterForDelete(userSession, commonKey)";
				tipoSujetoAdapterVO = ExeServiceLocator.getDefinicionService().getTipoSujetoAdapterForView
					(userSession, commonKey);
				tipoSujetoAdapterVO.addMessage(BaseError.MSG_ELIMINAR, ExeError.TIPOSUJETO_LABEL);
				actionForward = mapping.findForward(ExeConstants.FWD_TIPOSUJETO_VIEW_ADAPTER);					
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getTipoSujetoAdapterForView(userSession)";
				tipoSujetoAdapterVO = ExeServiceLocator.getDefinicionService().getTipoSujetoAdapterForView
					(userSession, commonKey);
				tipoSujetoAdapterVO.addMessage(BaseError.MSG_ACTIVAR, ExeError.TIPOSUJETO_LABEL);
				actionForward = mapping.findForward(ExeConstants.FWD_TIPOSUJETO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getTipoSujetoAdapterForView(userSession)";
				tipoSujetoAdapterVO = ExeServiceLocator.getDefinicionService().getTipoSujetoAdapterForView
					(userSession, commonKey);
				tipoSujetoAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, ExeError.TIPOSUJETO_LABEL);			
				actionForward = mapping.findForward(ExeConstants.FWD_TIPOSUJETO_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (tipoSujetoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + 
					stringServicio + ": " + tipoSujetoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoSujetoAdapter.NAME, tipoSujetoAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			tipoSujetoAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + 
				TipoSujetoAdapter.NAME + ": " + tipoSujetoAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(TipoSujetoAdapter.NAME, tipoSujetoAdapterVO);
			// Subo el apdater al userSession
			userSession.put(TipoSujetoAdapter.NAME, tipoSujetoAdapterVO);
			
			saveDemodaMessages(request, tipoSujetoAdapterVO);			
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoSujetoAdapter.NAME);
		}
	}

	public ActionForward modificarEncabezado(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardAdapter(mapping, request, funcName, 
			ExeConstants.ACTION_ADMINISTRAR_ENC_TIPOSUJETO, BaseConstants.ACT_MODIFICAR);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_TIPOSUJETO, 
			BaseSecurityConstants.ELIMINAR);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipoSujetoAdapter tipoSujetoAdapterVO = (TipoSujetoAdapter) userSession.get(TipoSujetoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipoSujetoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipoSujetoAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoSujetoAdapter.NAME); 
			}

			// llamada al servicio
			TipoSujetoVO tipoSujetoVO = ExeServiceLocator.getDefinicionService().deleteTipoSujeto
				(userSession, tipoSujetoAdapterVO.getTipoSujeto());
			
            // Tiene errores recuperables
			if (tipoSujetoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoSujetoAdapterVO.infoString());
				saveDemodaErrors(request, tipoSujetoVO);				
				request.setAttribute(TipoSujetoAdapter.NAME, tipoSujetoAdapterVO);
				return mapping.findForward(ExeConstants.FWD_TIPOSUJETO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (tipoSujetoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoSujetoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoSujetoAdapter.NAME, tipoSujetoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipoSujetoAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoSujetoAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_TIPOSUJETO, 
			BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipoSujetoAdapter tipoSujetoAdapterVO = (TipoSujetoAdapter) userSession.get(TipoSujetoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipoSujetoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipoSujetoAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoSujetoAdapter.NAME); 
			}

			// llamada al servicio
			TipoSujetoVO tipoSujetoVO = ExeServiceLocator.getDefinicionService().activarTipoSujeto
				(userSession, tipoSujetoAdapterVO.getTipoSujeto());
			
            // Tiene errores recuperables
			if (tipoSujetoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoSujetoAdapterVO.infoString());
				saveDemodaErrors(request, tipoSujetoVO);				
				request.setAttribute(TipoSujetoAdapter.NAME, tipoSujetoAdapterVO);
				return mapping.findForward(ExeConstants.FWD_TIPOSUJETO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (tipoSujetoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoSujetoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoSujetoAdapter.NAME, tipoSujetoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipoSujetoAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoSujetoAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_TIPOSUJETO, 
			BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipoSujetoAdapter tipoSujetoAdapterVO = (TipoSujetoAdapter) userSession.get(TipoSujetoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipoSujetoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipoSujetoAdapter.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoSujetoAdapter.NAME); 
			}

			// llamada al servicio
			TipoSujetoVO tipoSujetoVO = ExeServiceLocator.getDefinicionService().desactivarTipoSujeto
				(userSession, tipoSujetoAdapterVO.getTipoSujeto());
			
            // Tiene errores recuperables
			if (tipoSujetoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoSujetoAdapterVO.infoString());
				saveDemodaErrors(request, tipoSujetoVO);				
				request.setAttribute(TipoSujetoAdapter.NAME, tipoSujetoAdapterVO);
				return mapping.findForward(ExeConstants.FWD_TIPOSUJETO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (tipoSujetoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoSujetoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoSujetoAdapter.NAME, tipoSujetoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipoSujetoAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoSujetoAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, TipoSujetoAdapter.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, TipoSujetoAdapter.NAME);
		
	}
	
	// Metodos relacionados TipSujExe
	public ActionForward verTipSujExe(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, ExeConstants.ACTION_ADMINISTRAR_TIPSUJEXE);

	}

	public ActionForward modificarTipSujExe(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, ExeConstants.ACTION_ADMINISTRAR_TIPSUJEXE);

	}

	public ActionForward eliminarTipSujExe(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, ExeConstants.ACTION_ADMINISTRAR_TIPSUJEXE);

	}
	
	public ActionForward agregarTipSujExe(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		log.debug("funcName:"+funcName);
		return forwardAgregarAdapter(mapping, request, funcName, ExeConstants.ACTION_ADMINISTRAR_TIPSUJEXE);
		
	}
	
}
	
