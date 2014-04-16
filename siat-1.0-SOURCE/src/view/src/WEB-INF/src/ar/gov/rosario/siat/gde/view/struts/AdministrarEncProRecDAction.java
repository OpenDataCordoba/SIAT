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
import ar.gov.rosario.siat.gde.iface.model.ProRecAdapter;
import ar.gov.rosario.siat.gde.iface.model.ProRecVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarEncProRecDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEncProRecDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PROREC_ENC, act);		

		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ProRecAdapter proRecAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());

			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getProRecAdapterForUpdate(userSession, commonKey)";
				proRecAdapterVO = GdeServiceLocator.getDefinicionService().getProRecAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PROREC_ENC_EDIT_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getProRecAdapterForCreate(userSession)";
				proRecAdapterVO = GdeServiceLocator.getDefinicionService().getProRecAdapterForCreate(userSession,commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PROREC_ENC_EDIT_ADAPTER);
			}
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (proRecAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + proRecAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProRecAdapter.ENC_NAME, proRecAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			proRecAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ProRecAdapter.ENC_NAME + ": "+ proRecAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ProRecAdapter.ENC_NAME, proRecAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ProRecAdapter.ENC_NAME, proRecAdapterVO);

			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProRecAdapter.ENC_NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, 
			GdeSecurityConstants.ABM_PROREC_ENC, BaseSecurityConstants.AGREGAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ProRecAdapter proRecAdapterVO = (ProRecAdapter) userSession.get(ProRecAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (proRecAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProRecAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProRecAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(proRecAdapterVO, request);
			
            // Tiene errores recuperables
			if (proRecAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + proRecAdapterVO.infoString()); 
				saveDemodaErrors(request, proRecAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProRecAdapter.ENC_NAME, proRecAdapterVO);
			}
			
			// llamada al servicio
			ProRecVO proRecVO = GdeServiceLocator.getDefinicionService().createProRec(userSession, proRecAdapterVO.getProRec());
			
            // Tiene errores recuperables
			if (proRecVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + proRecVO.infoString()); 
				saveDemodaErrors(request, proRecVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProRecAdapter.ENC_NAME, proRecAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (proRecVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + proRecVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProRecAdapter.ENC_NAME, proRecAdapterVO);
			}

			// Si tiene permiso lo dirijo al adapter de modificacion, 
			// sino vuelve al searchPage
			if (hasAccess(userSession, GdeSecurityConstants.ABM_PROREC, 
				BaseSecurityConstants.MODIFICAR)) {
				
				// seteo el id para que lo use el siguiente action 
				userSession.getNavModel().setSelectedId(proRecVO.getId().toString());

				// lo dirijo al adapter de modificacion
				return forwardConfirmarOk(mapping, request, funcName, ProRecAdapter.ENC_NAME, 
					GdeConstants.PATH_ADMINISTRAR_PROREC, BaseConstants.METHOD_INICIALIZAR, 
					BaseConstants.ACT_MODIFICAR);
			} else {
				
				// lo dirijo al searchPage				
				return forwardConfirmarOk(mapping, request, funcName, ProRecAdapter.ENC_NAME);
				
			}
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProRecAdapter.ENC_NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, 
			GdeSecurityConstants.ABM_PROREC_ENC, BaseSecurityConstants.MODIFICAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ProRecAdapter proRecAdapterVO = (ProRecAdapter) userSession.get(ProRecAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (proRecAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProRecAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProRecAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(proRecAdapterVO, request);
			
            // Tiene errores recuperables
			if (proRecAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + proRecAdapterVO.infoString()); 
				saveDemodaErrors(request, proRecAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProRecAdapter.ENC_NAME, proRecAdapterVO);
			}
			
			// llamada al servicio
			ProRecVO proRecVO = GdeServiceLocator.getDefinicionService().updateProRec(userSession, proRecAdapterVO.getProRec());
			
            // Tiene errores recuperables
			if (proRecVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + proRecAdapterVO.infoString()); 
				saveDemodaErrors(request, proRecVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProRecAdapter.ENC_NAME, proRecAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (proRecVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + proRecAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProRecAdapter.ENC_NAME, proRecAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ProRecAdapter.ENC_NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProRecAdapter.ENC_NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ProRecAdapter.ENC_NAME);
	}
	
}
	
