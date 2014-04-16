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
import ar.gov.rosario.siat.def.iface.model.SiatScriptAdapter;
import ar.gov.rosario.siat.def.iface.model.SiatScriptVO;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarEncSiatScriptDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEncSiatScriptDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_SIATSCRIPT_ENC, act);		

		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		SiatScriptAdapter siatScriptAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());

			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getSiatScriptAdapterForUpdate(userSession, commonKey)";
				siatScriptAdapterVO = DefServiceLocator.getConfiguracionService().getSiatScriptAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_SIATSCRIPT_ENC_EDIT_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getSiatScriptAdapterForCreate(userSession)";
				siatScriptAdapterVO = DefServiceLocator.getConfiguracionService().getSiatScriptAdapterForCreate(userSession);
				actionForward = mapping.findForward(DefConstants.FWD_SIATSCRIPT_ENC_EDIT_ADAPTER);
			}
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (siatScriptAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + siatScriptAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SiatScriptAdapter.ENC_NAME, siatScriptAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			siatScriptAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + SiatScriptAdapter.ENC_NAME + ": "+ siatScriptAdapterVO.infoString());
			
			// Guardamos los mensajes de error en el request
			saveDemodaErrors(request, siatScriptAdapterVO);
			
			// Envio el VO al request
			request.setAttribute(SiatScriptAdapter.ENC_NAME, siatScriptAdapterVO);
			// Subo el apdater al userSession
			userSession.put(SiatScriptAdapter.ENC_NAME, siatScriptAdapterVO);

			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SiatScriptAdapter.ENC_NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, 
			DefSecurityConstants.ABM_SIATSCRIPT_ENC, BaseSecurityConstants.AGREGAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			SiatScriptAdapter siatScriptAdapterVO = (SiatScriptAdapter) userSession.get(SiatScriptAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (siatScriptAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + SiatScriptAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SiatScriptAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(siatScriptAdapterVO, request);
			
            // Tiene errores recuperables
			if (siatScriptAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + siatScriptAdapterVO.infoString()); 
				saveDemodaErrors(request, siatScriptAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, SiatScriptAdapter.ENC_NAME, siatScriptAdapterVO);
			}
			
			// llamada al servicio
			SiatScriptVO siatScriptVO = DefServiceLocator.getConfiguracionService().createSiatScript(userSession, siatScriptAdapterVO.getSiatScript());
			
            // Tiene errores recuperables
			if (siatScriptVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + siatScriptVO.infoString()); 
				saveDemodaErrors(request, siatScriptVO);
				return forwardErrorRecoverable(mapping, request, userSession, SiatScriptAdapter.ENC_NAME, siatScriptAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (siatScriptVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + siatScriptVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SiatScriptAdapter.ENC_NAME, siatScriptAdapterVO);
			}

			// Si tiene permiso lo dirijo al adapter de modificacion, 
			// sino vuelve al searchPage
			if (hasAccess(userSession, DefSecurityConstants.ABM_SIATSCRIPT, 
				BaseSecurityConstants.MODIFICAR)) {
				
				// seteo el id para que lo use el siguiente action 
				userSession.getNavModel().setSelectedId(siatScriptVO.getId().toString());

				// lo dirijo al adapter de modificacion
				return forwardConfirmarOk(mapping, request, funcName, SiatScriptAdapter.ENC_NAME, 
					DefConstants.PATH_ADMINISTRAR_SIATSCRIPT, BaseConstants.METHOD_INICIALIZAR, 
					BaseConstants.ACT_MODIFICAR);
			} else {
				
				// lo dirijo al searchPage				
				return forwardConfirmarOk(mapping, request, funcName, SiatScriptAdapter.ENC_NAME);
				
			}
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SiatScriptAdapter.ENC_NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, 
			DefSecurityConstants.ABM_SIATSCRIPT_ENC, BaseSecurityConstants.MODIFICAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			SiatScriptAdapter siatScriptAdapterVO = (SiatScriptAdapter) userSession.get(SiatScriptAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (siatScriptAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + SiatScriptAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SiatScriptAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(siatScriptAdapterVO, request);
			
            // Tiene errores recuperables
			if (siatScriptAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + siatScriptAdapterVO.infoString()); 
				saveDemodaErrors(request, siatScriptAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, SiatScriptAdapter.ENC_NAME, siatScriptAdapterVO);
			}
			
			// llamada al servicio
			SiatScriptVO siatScriptVO = DefServiceLocator.getConfiguracionService().updateSiatScript(userSession, siatScriptAdapterVO.getSiatScript());
			
            // Tiene errores recuperables
			if (siatScriptVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + siatScriptAdapterVO.infoString()); 
				saveDemodaErrors(request, siatScriptVO);
				return forwardErrorRecoverable(mapping, request, userSession, SiatScriptAdapter.ENC_NAME, siatScriptAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (siatScriptVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + siatScriptAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SiatScriptAdapter.ENC_NAME, siatScriptAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, SiatScriptAdapter.ENC_NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SiatScriptAdapter.ENC_NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, SiatScriptAdapter.ENC_NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, SiatScriptAdapter.ENC_NAME);
		
	}
	
}
	
