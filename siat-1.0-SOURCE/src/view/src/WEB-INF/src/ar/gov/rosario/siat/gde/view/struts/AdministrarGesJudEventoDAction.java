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

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.gde.iface.model.GesJudEventoAdapter;
import ar.gov.rosario.siat.gde.iface.model.GesJudEventoVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarGesJudEventoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarGesJudEventoDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_GESJUDEVENTO, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		GesJudEventoAdapter gesJudEventoAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getGesJudEventoAdapterForView(userSession, commonKey)";
				gesJudEventoAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getGesJudEventoAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_GESJUDEVENTO_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getGesJudEventoAdapterForView(userSession, commonKey)";
				gesJudEventoAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getGesJudEventoAdapterForView(userSession, commonKey);				
				gesJudEventoAdapterVO.addMessage(BaseError.MSG_ELIMINAR, GdeError.GESJUDEVENTO_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_GESJUDEVENTO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getGesJudEventoAdapterForCreate(userSession)";
				gesJudEventoAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getGesJudEventoAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_GESJUDEVENTO_EDIT_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (gesJudEventoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + gesJudEventoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, GesJudEventoAdapter.NAME, gesJudEventoAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			gesJudEventoAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + GesJudEventoAdapter.NAME + ": "+ gesJudEventoAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(GesJudEventoAdapter.NAME, gesJudEventoAdapterVO);
			// Subo el apdater al userSession
			userSession.put(GesJudEventoAdapter.NAME, gesJudEventoAdapterVO);
			 
			saveDemodaMessages(request, gesJudEventoAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, GesJudEventoAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_GESJUDEVENTO, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			GesJudEventoAdapter gesJudEventoAdapterVO = (GesJudEventoAdapter) userSession.get(GesJudEventoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (gesJudEventoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + GesJudEventoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, GesJudEventoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(gesJudEventoAdapterVO, request);
			
            // Tiene errores recuperables
			if (gesJudEventoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + gesJudEventoAdapterVO.infoString()); 
				saveDemodaErrors(request, gesJudEventoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, GesJudEventoAdapter.NAME, gesJudEventoAdapterVO);
			}
			
			// llamada al servicio
			GesJudEventoVO gesJudEventoVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().createGesJudEvento(userSession, gesJudEventoAdapterVO.getGesJudEvento());
			
            // Tiene errores recuperables
			if (gesJudEventoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + gesJudEventoVO.infoString()); 
				saveDemodaErrors(request, gesJudEventoVO);
				return forwardErrorRecoverable(mapping, request, userSession, GesJudEventoAdapter.NAME, gesJudEventoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (gesJudEventoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + gesJudEventoVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, GesJudEventoAdapter.NAME, gesJudEventoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, GesJudEventoAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, GesJudEventoAdapter.NAME);
		}
	}


	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_GESJUDEVENTO, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			GesJudEventoAdapter gesJudEventoAdapterVO = (GesJudEventoAdapter) userSession.get(GesJudEventoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (gesJudEventoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + GesJudEventoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, GesJudEventoAdapter.NAME); 
			}

			// llamada al servicio
			GesJudEventoVO gesJudEventoVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().deleteGesJudEvento
				(userSession, gesJudEventoAdapterVO.getGesJudEvento());
			
            // Tiene errores recuperables
			if (gesJudEventoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + gesJudEventoAdapterVO.infoString());
				saveDemodaErrors(request, gesJudEventoVO);				
				request.setAttribute(GesJudEventoAdapter.NAME, gesJudEventoAdapterVO);
				return mapping.findForward(GdeConstants.FWD_GESJUDEVENTO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (gesJudEventoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + gesJudEventoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, GesJudEventoAdapter.NAME, gesJudEventoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, GesJudEventoAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, GesJudEventoAdapter.NAME);
		}
	}
	

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, GesJudEventoAdapter.NAME);
		
	}	
}
