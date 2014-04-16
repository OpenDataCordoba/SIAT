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

import ar.gov.rosario.siat.bal.iface.model.SelladoAdapter;
import ar.gov.rosario.siat.bal.iface.model.SelladoVO;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarEncSelladoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEncSelladoDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_SELLADO_ENC, act);		

		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		SelladoAdapter selladoAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());

			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getSelladoAdapterForUpdate(userSession, commonKey)";
				selladoAdapterVO = BalServiceLocator.getDefinicionService().getSelladoAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_SELLADO_ENC_EDIT_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getSelladoAdapterForCreate(userSession)";
				selladoAdapterVO = BalServiceLocator.getDefinicionService().getSelladoAdapterForCreate(userSession);
				actionForward = mapping.findForward(BalConstants.FWD_SELLADO_ENC_EDIT_ADAPTER);
			}
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (selladoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + selladoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SelladoAdapter.ENC_NAME, selladoAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			selladoAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + SelladoAdapter.ENC_NAME + ": "+ selladoAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(SelladoAdapter.ENC_NAME, selladoAdapterVO);
			// Subo el apdater al userSession
			userSession.put(SelladoAdapter.ENC_NAME, selladoAdapterVO);

			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SelladoAdapter.ENC_NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, 
			BalSecurityConstants.ABM_SELLADO_ENC, BaseSecurityConstants.AGREGAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			SelladoAdapter selladoAdapterVO = (SelladoAdapter) userSession.get(SelladoAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (selladoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + SelladoAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SelladoAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(selladoAdapterVO, request);
			
            // Tiene errores recuperables
			if (selladoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + selladoAdapterVO.infoString()); 
				saveDemodaErrors(request, selladoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, SelladoAdapter.ENC_NAME, selladoAdapterVO);
			}
			
			// llamada al servicio
			SelladoVO selladoVO = BalServiceLocator.getDefinicionService().createSellado(userSession, selladoAdapterVO.getSellado());
			
            // Tiene errores recuperables
			if (selladoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + selladoVO.infoString()); 
				saveDemodaErrors(request, selladoVO);
				return forwardErrorRecoverable(mapping, request, userSession, SelladoAdapter.ENC_NAME, selladoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (selladoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + selladoVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SelladoAdapter.ENC_NAME, selladoAdapterVO);
			}

			// Si tiene permiso lo dirijo al adapter de modificacion, 
			// sino vuelve al searchPage
			if (hasAccess(userSession, BalSecurityConstants.ABM_SELLADO, 
				BaseSecurityConstants.MODIFICAR)) {
				
				// seteo el id para que lo use el siguiente action 
				userSession.getNavModel().setSelectedId(selladoVO.getId().toString());

				// lo dirijo al adapter de modificacion
				return forwardConfirmarOk(mapping, request, funcName, SelladoAdapter.ENC_NAME, 
					BalConstants.PATH_ADMINISTRAR_SELLADO, BaseConstants.METHOD_INICIALIZAR, 
					BaseConstants.ACT_MODIFICAR);
			} else {
				
				// lo dirijo al searchPage				
				return forwardConfirmarOk(mapping, request, funcName, SelladoAdapter.ENC_NAME);
				
			}
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SelladoAdapter.ENC_NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, 
			BalSecurityConstants.ABM_SELLADO_ENC, BaseSecurityConstants.MODIFICAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			SelladoAdapter selladoAdapterVO = (SelladoAdapter) userSession.get(SelladoAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (selladoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + SelladoAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SelladoAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(selladoAdapterVO, request);
			
            // Tiene errores recuperables
			if (selladoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + selladoAdapterVO.infoString()); 
				saveDemodaErrors(request, selladoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, SelladoAdapter.ENC_NAME, selladoAdapterVO);
			}
			
			// llamada al servicio
			SelladoVO selladoVO = BalServiceLocator.getDefinicionService().updateSellado(userSession, selladoAdapterVO.getSellado());
			
            // Tiene errores recuperables
			if (selladoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + selladoAdapterVO.infoString()); 
				saveDemodaErrors(request, selladoVO);
				return forwardErrorRecoverable(mapping, request, userSession, SelladoAdapter.ENC_NAME, selladoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (selladoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + selladoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SelladoAdapter.ENC_NAME, selladoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, SelladoAdapter.ENC_NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SelladoAdapter.ENC_NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, SelladoAdapter.ENC_NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, SelladoAdapter.ENC_NAME);
		
	}
	
}
	
