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

import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.exe.iface.model.TipoSujetoAdapter;
import ar.gov.rosario.siat.exe.iface.model.TipoSujetoVO;
import ar.gov.rosario.siat.exe.iface.service.ExeServiceLocator;
import ar.gov.rosario.siat.exe.iface.util.ExeSecurityConstants;
import ar.gov.rosario.siat.exe.view.util.ExeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarEncTipoSujetoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEncTipoSujetoDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_TIPOSUJETO_ENC, act);		

		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		TipoSujetoAdapter tipoSujetoAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());

			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getTipoSujetoAdapterForUpdate(userSession, commonKey)";
				tipoSujetoAdapterVO = ExeServiceLocator.getDefinicionService().getTipoSujetoAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(ExeConstants.FWD_TIPOSUJETO_ENC_EDIT_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getTipoSujetoAdapterForCreate(userSession)";
				tipoSujetoAdapterVO = ExeServiceLocator.getDefinicionService().getTipoSujetoAdapterForCreate(userSession);
				actionForward = mapping.findForward(ExeConstants.FWD_TIPOSUJETO_ENC_EDIT_ADAPTER);
			}
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (tipoSujetoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + tipoSujetoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoSujetoAdapter.ENC_NAME, tipoSujetoAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			tipoSujetoAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + TipoSujetoAdapter.ENC_NAME + ": "+ tipoSujetoAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(TipoSujetoAdapter.ENC_NAME, tipoSujetoAdapterVO);
			// Subo el apdater al userSession
			userSession.put(TipoSujetoAdapter.ENC_NAME, tipoSujetoAdapterVO);

			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoSujetoAdapter.ENC_NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, 
			ExeSecurityConstants.ABM_TIPOSUJETO_ENC, BaseSecurityConstants.AGREGAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipoSujetoAdapter tipoSujetoAdapterVO = (TipoSujetoAdapter) userSession.get(TipoSujetoAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (tipoSujetoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipoSujetoAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoSujetoAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(tipoSujetoAdapterVO, request);
			
            // Tiene errores recuperables
			if (tipoSujetoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoSujetoAdapterVO.infoString()); 
				saveDemodaErrors(request, tipoSujetoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoSujetoAdapter.ENC_NAME, tipoSujetoAdapterVO);
			}
			
			// llamada al servicio
			TipoSujetoVO tipoSujetoVO = ExeServiceLocator.getDefinicionService().createTipoSujeto(userSession, tipoSujetoAdapterVO.getTipoSujeto());
			
            // Tiene errores recuperables
			if (tipoSujetoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoSujetoVO.infoString()); 
				saveDemodaErrors(request, tipoSujetoVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoSujetoAdapter.ENC_NAME, tipoSujetoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (tipoSujetoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoSujetoVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoSujetoAdapter.ENC_NAME, tipoSujetoAdapterVO);
			}

			// Si tiene permiso lo dirijo al adapter de modificacion, 
			// sino vuelve al searchPage
			if (hasAccess(userSession, ExeSecurityConstants.ABM_TIPOSUJETO, 
				BaseSecurityConstants.MODIFICAR)) {
				
				// seteo el id para que lo use el siguiente action 
				userSession.getNavModel().setSelectedId(tipoSujetoVO.getId().toString());

				// lo dirijo al adapter de modificacion
				return forwardConfirmarOk(mapping, request, funcName, TipoSujetoAdapter.ENC_NAME, 
					ExeConstants.PATH_ADMINISTRAR_TIPOSUJETO, BaseConstants.METHOD_INICIALIZAR, 
					BaseConstants.ACT_MODIFICAR);
			} else {
				
				// lo dirijo al searchPage				
				return forwardConfirmarOk(mapping, request, funcName, TipoSujetoAdapter.ENC_NAME);
				
			}
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoSujetoAdapter.ENC_NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, 
			ExeSecurityConstants.ABM_TIPOSUJETO_ENC, BaseSecurityConstants.MODIFICAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipoSujetoAdapter tipoSujetoAdapterVO = (TipoSujetoAdapter) userSession.get(TipoSujetoAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (tipoSujetoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipoSujetoAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoSujetoAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(tipoSujetoAdapterVO, request);
			
            // Tiene errores recuperables
			if (tipoSujetoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoSujetoAdapterVO.infoString()); 
				saveDemodaErrors(request, tipoSujetoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoSujetoAdapter.ENC_NAME, tipoSujetoAdapterVO);
			}
			
			// llamada al servicio
			TipoSujetoVO tipoSujetoVO = ExeServiceLocator.getDefinicionService().updateTipoSujeto(userSession, tipoSujetoAdapterVO.getTipoSujeto());
			
            // Tiene errores recuperables
			if (tipoSujetoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoSujetoAdapterVO.infoString()); 
				saveDemodaErrors(request, tipoSujetoVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoSujetoAdapter.ENC_NAME, tipoSujetoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (tipoSujetoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoSujetoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoSujetoAdapter.ENC_NAME, tipoSujetoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipoSujetoAdapter.ENC_NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoSujetoAdapter.ENC_NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, TipoSujetoAdapter.ENC_NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, TipoSujetoAdapter.ENC_NAME);
		
	}
	
}
	
