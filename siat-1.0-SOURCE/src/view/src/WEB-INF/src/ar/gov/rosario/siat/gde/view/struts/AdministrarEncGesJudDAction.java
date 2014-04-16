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
import ar.gov.rosario.siat.gde.iface.model.GesJudAdapter;
import ar.gov.rosario.siat.gde.iface.model.GesJudVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarEncGesJudDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEncGesJudDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ADM_GESJUD_ENC, act);		

		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		GesJudAdapter gesJudAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());

			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getGesJudAdapterForUpdate(userSession, commonKey)";
				gesJudAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getGesJudAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_GESJUD_ENC_EDIT_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getGesJudAdapterForCreate(userSession)";
				gesJudAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getGesJudAdapterForCreate(userSession);
				actionForward = mapping.findForward(GdeConstants.FWD_GESJUD_ENC_EDIT_ADAPTER);
			}
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (gesJudAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + gesJudAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, GesJudAdapter.ENC_NAME, gesJudAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			gesJudAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + GesJudAdapter.ENC_NAME + ": "+ gesJudAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(GesJudAdapter.ENC_NAME, gesJudAdapterVO);
			// Subo el apdater al userSession
			userSession.put(GesJudAdapter.ENC_NAME, gesJudAdapterVO);

			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, GesJudAdapter.ENC_NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, 
			GdeSecurityConstants.ADM_GESJUD_ENC, BaseSecurityConstants.AGREGAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			GesJudAdapter gesJudAdapterVO = (GesJudAdapter) userSession.get(GesJudAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (gesJudAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + GesJudAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, GesJudAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(gesJudAdapterVO, request);
			
            // Tiene errores recuperables
			if (gesJudAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + gesJudAdapterVO.infoString()); 
				saveDemodaErrors(request, gesJudAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, GesJudAdapter.ENC_NAME, gesJudAdapterVO);
			}
			
			// llamada al servicio
			GesJudVO gesJudVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().createGesJud(userSession, gesJudAdapterVO.getGesJud());
			
            // Tiene errores recuperables
			if (gesJudVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + gesJudVO.infoString()); 
				saveDemodaErrors(request, gesJudVO);
				return forwardErrorRecoverable(mapping, request, userSession, GesJudAdapter.ENC_NAME, gesJudAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (gesJudVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + gesJudVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, GesJudAdapter.ENC_NAME, gesJudAdapterVO);
			}

			// Si tiene permiso lo dirijo al adapter de modificacion, 
			// sino vuelve al searchPage
			if (hasAccess(userSession, GdeSecurityConstants.ADM_GESJUD, 
				BaseSecurityConstants.MODIFICAR)) {
				
				// seteo el id para que lo use el siguiente action 
				userSession.getNavModel().setSelectedId(gesJudVO.getId().toString());

				// lo dirijo al adapter de modificacion
				return forwardConfirmarOk(mapping, request, funcName, GesJudAdapter.ENC_NAME, 
					GdeConstants.PATH_ADMINISTRAR_GESJUD, BaseConstants.METHOD_INICIALIZAR, 
					BaseConstants.ACT_MODIFICAR);
			} else {
				
				// lo dirijo al searchPage				
				return forwardConfirmarOk(mapping, request, funcName, GesJudAdapter.ENC_NAME);
				
			}
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, GesJudAdapter.ENC_NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, 
			GdeSecurityConstants.ADM_GESJUD_ENC, BaseSecurityConstants.MODIFICAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			GesJudAdapter gesJudAdapterVO = (GesJudAdapter) userSession.get(GesJudAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (gesJudAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + GesJudAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, GesJudAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(gesJudAdapterVO, request);
			
            // Tiene errores recuperables
			if (gesJudAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + gesJudAdapterVO.infoString());

				saveDemodaErrors(request, gesJudAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, GesJudAdapter.ENC_NAME, gesJudAdapterVO);
			}
			
			// llamada al servicio
			GesJudVO gesJudVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().updateGesJud(userSession, gesJudAdapterVO.getGesJud());
			gesJudAdapterVO.setGesJud(gesJudVO);
			
            // Tiene errores recuperables
			if (gesJudVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + gesJudAdapterVO.infoString());

				saveDemodaErrors(request, gesJudVO);
				return forwardErrorRecoverable(mapping, request, userSession, GesJudAdapter.ENC_NAME, gesJudAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (gesJudVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + gesJudAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, GesJudAdapter.ENC_NAME, gesJudAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, GesJudAdapter.ENC_NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, GesJudAdapter.ENC_NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, GesJudAdapter.ENC_NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, GesJudAdapter.ENC_NAME);
		
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
			GesJudAdapter adapterVO = (GesJudAdapter)userSession.get(GesJudAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (adapterVO == null) {
				log.error("error en: "  + funcName + ": " + GesJudAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, GesJudAdapter.ENC_NAME); 
			}
			
			// Populate como en un buscar
			DemodaUtil.populateVO(adapterVO, request);
			
			log.debug( funcName + " " + adapterVO.getGesJud().getCaso().infoString());
			
			// llamada al servicio
			CasServiceLocator.getCasCasoService().validarCaso(userSession, adapterVO.getGesJud()); 
			
			adapterVO.getGesJud().passErrorMessages(adapterVO);
		    
		    saveDemodaMessages(request, adapterVO);
		    saveDemodaErrors(request, adapterVO);
		    
			request.setAttribute(GesJudAdapter.ENC_NAME, adapterVO);
			
			return mapping.findForward( GdeConstants.FWD_GESJUD_ENC_EDIT_ADAPTER); 
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, GesJudAdapter.ENC_NAME);
		}	
	}
	
}
	
