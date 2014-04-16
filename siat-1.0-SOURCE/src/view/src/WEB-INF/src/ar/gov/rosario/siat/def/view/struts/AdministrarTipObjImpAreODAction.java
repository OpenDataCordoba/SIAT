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
import ar.gov.rosario.siat.def.iface.model.TipObjImpAreOAdapter;
import ar.gov.rosario.siat.def.iface.model.TipObjImpAreOVO;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarTipObjImpAreODAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarTipObjImpAreODAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_TIPO_OBJETO_IMPONIBLE_ARE_O, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		TipObjImpAreOAdapter tipObjImpAreOAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getTipObjImpAreOAdapterForView(userSession, commonKey)";
				tipObjImpAreOAdapterVO = DefServiceLocator.getObjetoImponibleService().getTipObjImpAreOAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_TIPOBJIMPAREO_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getTipObjImpAreOAdapterForView(userSession, commonKey)";
				tipObjImpAreOAdapterVO = DefServiceLocator.getObjetoImponibleService().getTipObjImpAreOAdapterForView(userSession, commonKey);
				tipObjImpAreOAdapterVO.addMessage(BaseError.MSG_ELIMINAR, DefError.TIPOBJIMPAREO_LABEL);					
				actionForward = mapping.findForward(DefConstants.FWD_TIPOBJIMPAREO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getTipObjImpAreOAdapterForCreate(userSession)";
				tipObjImpAreOAdapterVO = DefServiceLocator.getObjetoImponibleService().getTipObjImpAreOAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_TIPOBJIMPAREO_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getTipObjImpAreOAdapterForView(userSession)";
				tipObjImpAreOAdapterVO = DefServiceLocator.getObjetoImponibleService().getTipObjImpAreOAdapterForView(userSession, commonKey);
				tipObjImpAreOAdapterVO.addMessage(BaseError.MSG_ACTIVAR, DefError.TIPOBJIMPAREO_LABEL);
				actionForward = mapping.findForward(DefConstants.FWD_TIPOBJIMPAREO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getTipObjImpAreOAdapterForView(userSession)";
				tipObjImpAreOAdapterVO = DefServiceLocator.getObjetoImponibleService().getTipObjImpAreOAdapterForView(userSession, commonKey);
				tipObjImpAreOAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, DefError.TIPOBJIMPAREO_LABEL);
				actionForward = mapping.findForward(DefConstants.FWD_TIPOBJIMPAREO_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (tipObjImpAreOAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + tipObjImpAreOAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipObjImpAreOAdapter.NAME, tipObjImpAreOAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			tipObjImpAreOAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + TipObjImpAreOAdapter.NAME + ": "+ tipObjImpAreOAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(TipObjImpAreOAdapter.NAME, tipObjImpAreOAdapterVO);
			// Subo el apdater al userSession
			userSession.put(TipObjImpAreOAdapter.NAME, tipObjImpAreOAdapterVO);
			 
			saveDemodaMessages(request, tipObjImpAreOAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipObjImpAreOAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_TIPO_OBJETO_IMPONIBLE_ARE_O, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipObjImpAreOAdapter tipObjImpAreOAdapterVO = (TipObjImpAreOAdapter) userSession.get(TipObjImpAreOAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipObjImpAreOAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipObjImpAreOAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipObjImpAreOAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(tipObjImpAreOAdapterVO, request);
			
            // Tiene errores recuperables
			if (tipObjImpAreOAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipObjImpAreOAdapterVO.infoString()); 
				saveDemodaErrors(request, tipObjImpAreOAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipObjImpAreOAdapter.NAME, tipObjImpAreOAdapterVO);
			}
			
			// llamada al servicio
			TipObjImpAreOVO tipObjImpAreOVO = DefServiceLocator.getObjetoImponibleService().createTipObjImpAreO(userSession, tipObjImpAreOAdapterVO.getTipObjImpAreO());
			
            // Tiene errores recuperables
			if (tipObjImpAreOVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipObjImpAreOVO.infoString()); 
				saveDemodaErrors(request, tipObjImpAreOVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipObjImpAreOAdapter.NAME, tipObjImpAreOAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (tipObjImpAreOVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipObjImpAreOVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipObjImpAreOAdapter.NAME, tipObjImpAreOAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipObjImpAreOAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipObjImpAreOAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_TIPO_OBJETO_IMPONIBLE_ARE_O, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipObjImpAreOAdapter tipObjImpAreOAdapterVO = (TipObjImpAreOAdapter) userSession.get(TipObjImpAreOAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipObjImpAreOAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipObjImpAreOAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipObjImpAreOAdapter.NAME); 
			}

			// llamada al servicio
			TipObjImpAreOVO tipObjImpAreOVO = DefServiceLocator.getObjetoImponibleService().deleteTipObjImpAreO
				(userSession, tipObjImpAreOAdapterVO.getTipObjImpAreO());
			
            // Tiene errores recuperables
			if (tipObjImpAreOVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipObjImpAreOAdapterVO.infoString());
				saveDemodaErrors(request, tipObjImpAreOVO);				
				request.setAttribute(TipObjImpAreOAdapter.NAME, tipObjImpAreOAdapterVO);
				return mapping.findForward(DefConstants.FWD_TIPOBJIMPAREO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (tipObjImpAreOVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipObjImpAreOAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipObjImpAreOAdapter.NAME, tipObjImpAreOAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipObjImpAreOAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipObjImpAreOAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_TIPO_OBJETO_IMPONIBLE_ARE_O, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipObjImpAreOAdapter tipObjImpAreOAdapterVO = (TipObjImpAreOAdapter) userSession.get(TipObjImpAreOAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipObjImpAreOAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipObjImpAreOAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipObjImpAreOAdapter.NAME); 
			}

			// llamada al servicio
			TipObjImpAreOVO tipObjImpAreOVO = DefServiceLocator.getObjetoImponibleService().activarTipObjImpAreO
				(userSession, tipObjImpAreOAdapterVO.getTipObjImpAreO());
			
            // Tiene errores recuperables
			if (tipObjImpAreOVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipObjImpAreOAdapterVO.infoString());
				saveDemodaErrors(request, tipObjImpAreOVO);				
				request.setAttribute(TipObjImpAreOAdapter.NAME, tipObjImpAreOAdapterVO);
				return mapping.findForward(DefConstants.FWD_TIPOBJIMPAREO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (tipObjImpAreOVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipObjImpAreOAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipObjImpAreOAdapter.NAME, tipObjImpAreOAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipObjImpAreOAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipObjImpAreOAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_TIPO_OBJETO_IMPONIBLE_ARE_O, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipObjImpAreOAdapter tipObjImpAreOAdapterVO = (TipObjImpAreOAdapter) userSession.get(TipObjImpAreOAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipObjImpAreOAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipObjImpAreOAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipObjImpAreOAdapter.NAME); 
			}

			// llamada al servicio
			TipObjImpAreOVO tipObjImpAreOVO = DefServiceLocator.getObjetoImponibleService().desactivarTipObjImpAreO
				(userSession, tipObjImpAreOAdapterVO.getTipObjImpAreO());
			
            // Tiene errores recuperables
			if (tipObjImpAreOVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipObjImpAreOAdapterVO.infoString());
				saveDemodaErrors(request, tipObjImpAreOVO);				
				request.setAttribute(TipObjImpAreOAdapter.NAME, tipObjImpAreOAdapterVO);
				return mapping.findForward(DefConstants.FWD_TIPOBJIMPAREO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (tipObjImpAreOVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipObjImpAreOAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipObjImpAreOAdapter.NAME, tipObjImpAreOAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipObjImpAreOAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipObjImpAreOAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, TipObjImpAreOAdapter.NAME);
		
	}
	
		
}
