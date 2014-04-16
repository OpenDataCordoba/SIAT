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

import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.def.iface.model.TipObjImpAdapter;
import ar.gov.rosario.siat.def.iface.model.TipObjImpVO;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarEncTipObjImpDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEncTipObjImpDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName + " act = " + act);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_TIPO_OBJETO_IMPONIBLE, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		TipObjImpAdapter tipObjImpEncAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());

			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getDomAtrAdapterForUpdate(userSession, commonKey)";
				tipObjImpEncAdapterVO = DefServiceLocator.getObjetoImponibleService().getTipObjImpAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_TIPOBJIMP_ENC_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getTipObjImpAdapterForCreate(userContext)";
				tipObjImpEncAdapterVO = DefServiceLocator.getObjetoImponibleService().getTipObjImpAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_TIPOBJIMP_ENC_EDIT_ADAPTER);				
			}
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (tipObjImpEncAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + tipObjImpEncAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipObjImpAdapter.ENC_NAME, tipObjImpEncAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			tipObjImpEncAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + TipObjImpAdapter.ENC_NAME + ": "+ tipObjImpEncAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(TipObjImpAdapter.ENC_NAME, tipObjImpEncAdapterVO);
			// Subo el apdater al userSession
			userSession.put(TipObjImpAdapter.ENC_NAME, tipObjImpEncAdapterVO);

			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipObjImpAdapter.ENC_NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_TIPO_OBJETO_IMPONIBLE, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipObjImpAdapter tipObjImpEncAdapterVO = (TipObjImpAdapter) userSession.get(TipObjImpAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (tipObjImpEncAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipObjImpAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipObjImpAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(tipObjImpEncAdapterVO, request);
			
            // Tiene errores recuperables
			if (tipObjImpEncAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipObjImpEncAdapterVO.infoString()); 
				saveDemodaErrors(request, tipObjImpEncAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipObjImpAdapter.ENC_NAME, tipObjImpEncAdapterVO);
			}
			
			// llamada al servicio
			TipObjImpVO tipObjImpVO = DefServiceLocator.getObjetoImponibleService().createTipObjImp(userSession, tipObjImpEncAdapterVO.getTipObjImp());
			
            // Tiene errores recuperables
			if (tipObjImpVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipObjImpVO.infoString()); 
				saveDemodaErrors(request, tipObjImpVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipObjImpAdapter.ENC_NAME, tipObjImpEncAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (tipObjImpVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipObjImpVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipObjImpAdapter.ENC_NAME, tipObjImpEncAdapterVO);
			}
			
			// Fue Exitoso
			// seteo el id para que lo use el siguiente action 
			userSession.getNavModel().setSelectedId(tipObjImpVO.getId().toString());
			
			// seteo a donde quiero que se dirija despues de la pantalla de confirmacion.
			return forwardConfirmarOk(mapping, request, funcName, TipObjImpAdapter.ENC_NAME, 
				DefConstants.PATH_ADMINISTRAR_TIPOBJIMP, BaseConstants.METHOD_INICIALIZAR, BaseConstants.ACT_MODIFICAR);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipObjImpAdapter.ENC_NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_TIPO_OBJETO_IMPONIBLE, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipObjImpAdapter tipObjImpEncAdapterVO = (TipObjImpAdapter) userSession.get(TipObjImpAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (tipObjImpEncAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipObjImpAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipObjImpAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(tipObjImpEncAdapterVO, request);
			
            // Tiene errores recuperables
			if (tipObjImpEncAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipObjImpEncAdapterVO.infoString()); 
				saveDemodaErrors(request, tipObjImpEncAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipObjImpAdapter.ENC_NAME, tipObjImpEncAdapterVO);
			}
			
			// llamada al servicio
			TipObjImpVO tipObjImpVO = DefServiceLocator.getObjetoImponibleService().updateTipObjImp(userSession, tipObjImpEncAdapterVO.getTipObjImp());
			
            // Tiene errores recuperables
			if (tipObjImpVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipObjImpVO.infoString()); 
				saveDemodaErrors(request, tipObjImpVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipObjImpAdapter.ENC_NAME, tipObjImpEncAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (tipObjImpVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipObjImpVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipObjImpAdapter.ENC_NAME, tipObjImpEncAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipObjImpAdapter.ENC_NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipObjImpAdapter.ENC_NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, TipObjImpAdapter.ENC_NAME);
	}
	
	public ActionForward param (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				TipObjImpAdapter tipObjImpEncAdapterVO = (TipObjImpAdapter) userSession.get(TipObjImpAdapter.ENC_NAME);
				
				// Si es nulo no se puede continuar
				if (tipObjImpEncAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + TipObjImpAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, TipObjImpAdapter.ENC_NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(tipObjImpEncAdapterVO, request);
				
	            // Tiene errores recuperables
				if (tipObjImpEncAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + tipObjImpEncAdapterVO.infoString()); 
					saveDemodaErrors(request, tipObjImpEncAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, TipObjImpAdapter.ENC_NAME, tipObjImpEncAdapterVO);
				}
				
				// llamada al servicio
				tipObjImpEncAdapterVO = DefServiceLocator.getObjetoImponibleService().getTipObjImpAdapterParam(userSession, tipObjImpEncAdapterVO);
				
	            // Tiene errores recuperables
				if (tipObjImpEncAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + tipObjImpEncAdapterVO.infoString()); 
					saveDemodaErrors(request, tipObjImpEncAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, TipObjImpAdapter.ENC_NAME, tipObjImpEncAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (tipObjImpEncAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + tipObjImpEncAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, TipObjImpAdapter.ENC_NAME, tipObjImpEncAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(TipObjImpAdapter.ENC_NAME, tipObjImpEncAdapterVO);
				// Subo el apdater al userSession
				userSession.put(TipObjImpAdapter.ENC_NAME, tipObjImpEncAdapterVO);
				
				return mapping.findForward(DefConstants.FWD_TIPOBJIMP_ENC_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, TipObjImpAdapter.ENC_NAME);
			}
		}

}
	
