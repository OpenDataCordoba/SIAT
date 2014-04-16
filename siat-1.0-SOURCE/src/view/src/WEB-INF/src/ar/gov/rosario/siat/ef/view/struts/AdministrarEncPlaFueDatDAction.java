//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.view.struts;

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
import ar.gov.rosario.siat.ef.iface.model.PlaFueDatAdapter;
import ar.gov.rosario.siat.ef.iface.model.PlaFueDatVO;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarEncPlaFueDatDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEncPlaFueDatDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_PLAFUEDAT_ENC, act);		

		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		PlaFueDatAdapter plaFueDatAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());

			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getPlaFueDatAdapterForUpdate(userSession, commonKey)";
				plaFueDatAdapterVO = EfServiceLocator.getFiscalizacionService().getPlaFueDatAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_PLAFUEDAT_ENC_EDIT_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getPlaFueDatAdapterForCreate(userSession)";
				plaFueDatAdapterVO = EfServiceLocator.getFiscalizacionService().getPlaFueDatAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_PLAFUEDAT_ENC_EDIT_ADAPTER);
			}
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (plaFueDatAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + plaFueDatAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlaFueDatAdapter.ENC_NAME, plaFueDatAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			plaFueDatAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + PlaFueDatAdapter.ENC_NAME + ": "+ plaFueDatAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(PlaFueDatAdapter.ENC_NAME, plaFueDatAdapterVO);
			// Subo el apdater al userSession
			userSession.put(PlaFueDatAdapter.ENC_NAME, plaFueDatAdapterVO);

			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlaFueDatAdapter.ENC_NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, 
			EfSecurityConstants.ABM_PLAFUEDAT_ENC, BaseSecurityConstants.AGREGAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlaFueDatAdapter plaFueDatAdapterVO = (PlaFueDatAdapter) userSession.get(PlaFueDatAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (plaFueDatAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlaFueDatAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlaFueDatAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(plaFueDatAdapterVO, request);
			
            // Tiene errores recuperables
			if (plaFueDatAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + plaFueDatAdapterVO.infoString()); 
				saveDemodaErrors(request, plaFueDatAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlaFueDatAdapter.ENC_NAME, plaFueDatAdapterVO);
			}
			
			// llamada al servicio
			PlaFueDatVO plaFueDatVO = EfServiceLocator.getFiscalizacionService().createPlaFueDat(userSession, plaFueDatAdapterVO.getPlaFueDat());
			
            // Tiene errores recuperables
			if (plaFueDatVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + plaFueDatVO.infoString()); 
				saveDemodaErrors(request, plaFueDatVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlaFueDatAdapter.ENC_NAME, plaFueDatAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (plaFueDatVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + plaFueDatVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlaFueDatAdapter.ENC_NAME, plaFueDatAdapterVO);
			}

			// Si tiene permiso lo dirijo al adapter de modificacion, 
			// sino vuelve al searchPage
			if (hasAccess(userSession, EfSecurityConstants.ABM_PLAFUEDAT,BaseSecurityConstants.MODIFICAR)) {
				
				// seteo el id para que lo use el siguiente action 
				userSession.getNavModel().setSelectedId(plaFueDatVO.getId().toString());

				// lo dirijo al adapter de modificacion
				return forwardConfirmarOk(mapping, request, funcName, PlaFueDatAdapter.ENC_NAME, 
					EfConstants.PATH_ADMINISTRAR_PLAFUEDAT, BaseConstants.METHOD_INICIALIZAR, 
					BaseConstants.ACT_MODIFICAR);
			} else {
				
				// lo dirijo al searchPage				
				return forwardConfirmarOk(mapping, request, funcName, PlaFueDatAdapter.ENC_NAME);
				
			}
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlaFueDatAdapter.ENC_NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping, 
				EfSecurityConstants.ABM_PLAFUEDAT_ENC, BaseSecurityConstants.AGREGAR);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				PlaFueDatAdapter plaFueDatAdapterVO = (PlaFueDatAdapter) userSession.get(PlaFueDatAdapter.ENC_NAME);
				
				// Si es nulo no se puede continuar
				if (plaFueDatAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + PlaFueDatAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, PlaFueDatAdapter.ENC_NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(plaFueDatAdapterVO, request);
				
	            // Tiene errores recuperables
				if (plaFueDatAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + plaFueDatAdapterVO.infoString()); 
					saveDemodaErrors(request, plaFueDatAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, PlaFueDatAdapter.ENC_NAME, plaFueDatAdapterVO);
				}
				
				// llamada al servicio
				PlaFueDatVO plaFueDatVO = EfServiceLocator.getFiscalizacionService().updatePlaFueDat(userSession, plaFueDatAdapterVO.getPlaFueDat());
				
	            // Tiene errores recuperables
				if (plaFueDatVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + plaFueDatVO.infoString()); 
					saveDemodaErrors(request, plaFueDatVO);
					return forwardErrorRecoverable(mapping, request, userSession, PlaFueDatAdapter.ENC_NAME, plaFueDatAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (plaFueDatVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + plaFueDatVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, PlaFueDatAdapter.ENC_NAME, plaFueDatAdapterVO);
				}
				
				return forwardConfirmarOk(mapping, request, funcName, PlaFueDatAdapter.ENC_NAME);
					
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, PlaFueDatAdapter.ENC_NAME);
			}
		}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, PlaFueDatAdapter.ENC_NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, PlaFueDatAdapter.ENC_NAME);
		
	}
	
}
	
