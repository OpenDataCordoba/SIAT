//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.view.struts;

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
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.gde.iface.model.DesEspAdapter;
import ar.gov.rosario.siat.gde.iface.model.DesEspVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarEncDesEspDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEncDesEspDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DESESP_ENC, act);		

		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		DesEspAdapter desEspAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());

			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getDesEspAdapterForUpdate(userSession, commonKey)";
				desEspAdapterVO = GdeServiceLocator.getDefinicionService().getDesEspAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_DESESP_ENC_EDIT_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getDesEspAdapterForCreate(userSession)";
				desEspAdapterVO = GdeServiceLocator.getDefinicionService().getDesEspAdapterForCreate(userSession);
				actionForward = mapping.findForward(GdeConstants.FWD_DESESP_ENC_EDIT_ADAPTER);
			}
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (desEspAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + desEspAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DesEspAdapter.ENC_NAME, desEspAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			desEspAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + DesEspAdapter.ENC_NAME + ": "+ desEspAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(DesEspAdapter.ENC_NAME, desEspAdapterVO);
			// Subo el apdater al userSession
			userSession.put(DesEspAdapter.ENC_NAME, desEspAdapterVO);

			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DesEspAdapter.ENC_NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, 
			GdeSecurityConstants.ABM_DESESP_ENC, BaseSecurityConstants.AGREGAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DesEspAdapter desEspAdapterVO = (DesEspAdapter) userSession.get(DesEspAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (desEspAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DesEspAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DesEspAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(desEspAdapterVO, request);
			
            // Tiene errores recuperables
			if (desEspAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desEspAdapterVO.infoString()); 
				saveDemodaErrors(request, desEspAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, DesEspAdapter.ENC_NAME, desEspAdapterVO);
			}
			
			// llamada al servicio
			DesEspVO desEspVO = GdeServiceLocator.getDefinicionService().createDesEsp(userSession, desEspAdapterVO.getDesEsp());
			
            // Tiene errores recuperables
			if (desEspVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desEspVO.infoString()); 
				saveDemodaErrors(request, desEspVO);
				return forwardErrorRecoverable(mapping, request, userSession, DesEspAdapter.ENC_NAME, desEspAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (desEspVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + desEspVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DesEspAdapter.ENC_NAME, desEspAdapterVO);
			}

			// Si tiene permiso lo dirijo al adapter de modificacion, 
			// sino vuelve al searchPage
			if (hasAccess(userSession, GdeSecurityConstants.ABM_DESESP, 
				BaseSecurityConstants.MODIFICAR)) {
				
				// seteo el id para que lo use el siguiente action 
				userSession.getNavModel().setSelectedId(desEspVO.getId().toString());

				// lo dirijo al adapter de modificacion
				return forwardConfirmarOk(mapping, request, funcName, DesEspAdapter.ENC_NAME, 
					GdeConstants.PATH_ADMINISTRAR_DESESP, BaseConstants.METHOD_INICIALIZAR, 
					BaseConstants.ACT_MODIFICAR);
			} else {
				
				// lo dirijo al searchPage				
				return forwardConfirmarOk(mapping, request, funcName, DesEspAdapter.ENC_NAME);
				
			}
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DesEspAdapter.ENC_NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, 
			GdeSecurityConstants.ABM_DESESP_ENC, BaseSecurityConstants.MODIFICAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DesEspAdapter desEspAdapterVO = (DesEspAdapter) userSession.get(DesEspAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (desEspAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DesEspAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DesEspAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(desEspAdapterVO, request);
			
            // Tiene errores recuperables
			if (desEspAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desEspAdapterVO.infoString()); 
				saveDemodaErrors(request, desEspAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, DesEspAdapter.ENC_NAME, desEspAdapterVO);
			}
			
			// llamada al servicio
			DesEspVO desEspVO = GdeServiceLocator.getDefinicionService().updateDesEsp(userSession, desEspAdapterVO.getDesEsp());
			
            // Tiene errores recuperables
			if (desEspVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desEspAdapterVO.infoString()); 
				saveDemodaErrors(request, desEspVO);
				return forwardErrorRecoverable(mapping, request, userSession, DesEspAdapter.ENC_NAME, desEspAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (desEspVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + desEspAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DesEspAdapter.ENC_NAME, desEspAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DesEspAdapter.ENC_NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DesEspAdapter.ENC_NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, DesEspAdapter.ENC_NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, DesEspAdapter.ENC_NAME);
		
	}
	
	
	
	public ActionForward validarCaso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DesEspAdapter adapterVO = (DesEspAdapter)userSession.get(DesEspAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (adapterVO == null) {
				log.error("error en: "  + funcName + ": " + DesEspAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DesEspAdapter.ENC_NAME); 
			}
			
			// Populate como en un buscar
			DemodaUtil.populateVO(adapterVO, request);
			
			log.debug( funcName + " " + adapterVO.getDesEsp().getCaso().infoString());
			
			// llamada al servicio
			CasServiceLocator.getCasCasoService().validarCaso(userSession, adapterVO.getDesEsp()); 
			
			adapterVO.getDesEsp().passErrorMessages(adapterVO);
		    
		    saveDemodaMessages(request, adapterVO);
		    saveDemodaErrors(request, adapterVO);
		    
			request.setAttribute(DesEspAdapter.ENC_NAME, adapterVO);
			
			return mapping.findForward( GdeConstants.FWD_DESESP_ENC_EDIT_ADAPTER); 
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DesEspAdapter.ENC_NAME);
		}	
	}
}
	
