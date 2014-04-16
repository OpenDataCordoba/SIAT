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

import ar.gov.rosario.siat.bal.iface.model.PartidaAdapter;
import ar.gov.rosario.siat.bal.iface.model.PartidaVO;
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

public final class AdministrarEncPartidaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEncPartidaDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_PARTIDA_ENC, act);		

		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		PartidaAdapter partidaAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());

			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getPartidaAdapterForUpdate(userSession, commonKey)";
				partidaAdapterVO = BalServiceLocator.getDefinicionService().getPartidaAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_PARTIDA_ENC_EDIT_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getPartidaAdapterForCreate(userSession)";
				partidaAdapterVO = BalServiceLocator.getDefinicionService().getPartidaAdapterForCreate(userSession);
				actionForward = mapping.findForward(BalConstants.FWD_PARTIDA_ENC_EDIT_ADAPTER);
			}
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (partidaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + partidaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PartidaAdapter.ENC_NAME, partidaAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			partidaAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + PartidaAdapter.ENC_NAME + ": "+ partidaAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(PartidaAdapter.ENC_NAME, partidaAdapterVO);
			// Subo el apdater al userSession
			userSession.put(PartidaAdapter.ENC_NAME, partidaAdapterVO);

			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PartidaAdapter.ENC_NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, 
			BalSecurityConstants.ABM_PARTIDA_ENC, BaseSecurityConstants.AGREGAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PartidaAdapter partidaAdapterVO = (PartidaAdapter) userSession.get(PartidaAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (partidaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PartidaAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PartidaAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(partidaAdapterVO, request);
			
            // Tiene errores recuperables
			if (partidaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + partidaAdapterVO.infoString()); 
				saveDemodaErrors(request, partidaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PartidaAdapter.ENC_NAME, partidaAdapterVO);
			}
			
			// llamada al servicio
			PartidaVO partidaVO = BalServiceLocator.getDefinicionService().createPartida(userSession, partidaAdapterVO.getPartida());
			
            // Tiene errores recuperables
			if (partidaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + partidaVO.infoString()); 
				saveDemodaErrors(request, partidaVO);
				return forwardErrorRecoverable(mapping, request, userSession, PartidaAdapter.ENC_NAME, partidaAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (partidaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + partidaVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PartidaAdapter.ENC_NAME, partidaAdapterVO);
			}

			// Si tiene permiso lo dirijo al adapter de modificacion, 
			// sino vuelve al searchPage
			if (hasAccess(userSession, BalSecurityConstants.ABM_PARTIDA, 
				BaseSecurityConstants.MODIFICAR)) {
				
				// seteo el id para que lo use el siguiente action 
				userSession.getNavModel().setSelectedId(partidaVO.getId().toString());

				// lo dirijo al adapter de modificacion
				return forwardConfirmarOk(mapping, request, funcName, PartidaAdapter.ENC_NAME, 
					BalConstants.PATH_ADMINISTRAR_PARTIDA, BaseConstants.METHOD_INICIALIZAR, 
					BaseConstants.ACT_MODIFICAR);
			} else {
				
				// lo dirijo al searchPage				
				return forwardConfirmarOk(mapping, request, funcName, PartidaAdapter.ENC_NAME);
				
			}
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PartidaAdapter.ENC_NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, 
			BalSecurityConstants.ABM_PARTIDA_ENC, BaseSecurityConstants.MODIFICAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PartidaAdapter partidaAdapterVO = (PartidaAdapter) userSession.get(PartidaAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (partidaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PartidaAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PartidaAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(partidaAdapterVO, request);
			
            // Tiene errores recuperables
			if (partidaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + partidaAdapterVO.infoString()); 
				saveDemodaErrors(request, partidaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PartidaAdapter.ENC_NAME, partidaAdapterVO);
			}
			
			// llamada al servicio
			PartidaVO partidaVO = BalServiceLocator.getDefinicionService().updatePartida(userSession, partidaAdapterVO.getPartida());
			
            // Tiene errores recuperables
			if (partidaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + partidaAdapterVO.infoString()); 
				saveDemodaErrors(request, partidaVO);
				return forwardErrorRecoverable(mapping, request, userSession, PartidaAdapter.ENC_NAME, partidaAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (partidaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + partidaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PartidaAdapter.ENC_NAME, partidaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PartidaAdapter.ENC_NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PartidaAdapter.ENC_NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, PartidaAdapter.ENC_NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, PartidaAdapter.ENC_NAME);
		
	}
	
}
	
