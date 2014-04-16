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
import ar.gov.rosario.siat.gde.iface.model.ConDeuDetAdapter;
import ar.gov.rosario.siat.gde.iface.model.ConDeuDetVO;
import ar.gov.rosario.siat.gde.iface.model.ConstanciaDeuAdapter;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarConDeuDetDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarConDeuDetDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_CONDEUDET, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ConDeuDetAdapter conDeuDetAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		CommonKey commonKey= null;
		try {
			
			if (!act.equals(BaseConstants.ACT_AGREGAR)){
				commonKey = new CommonKey(navModel.getSelectedId());
			}
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getConDeuDetAdapterForView(userSession, commonKey)";
				conDeuDetAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getConDeuDetAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_CONDEUDET_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getConDeuDetAdapterForUpdate(userSession, commonKey)";
				conDeuDetAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getConDeuDetAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_CONDEUDET_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getConDeuDetAdapterForView(userSession, commonKey)";
				conDeuDetAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getConDeuDetAdapterForView(userSession, commonKey);				
				conDeuDetAdapterVO.addMessage(BaseError.MSG_ELIMINAR, GdeError.CONDEUDET_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_CONDEUDET_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getConDeuDetAdapterForCreate(userSession)";
				//bajo el adapter de la constancia del userSession
				ConstanciaDeuAdapter constanciaDeuAdapterVO =  (ConstanciaDeuAdapter) userSession.get(ConstanciaDeuAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (constanciaDeuAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + ConstanciaDeuAdapter.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ConstanciaDeuAdapter.NAME); 
				}
				conDeuDetAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getConDeuDetAdapterForCreate(userSession, constanciaDeuAdapterVO.getConstanciaDeu().getId());
				actionForward = mapping.findForward(GdeConstants.FWD_CONDEUDET_EDIT_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (conDeuDetAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + conDeuDetAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ConDeuDetAdapter.NAME, conDeuDetAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			conDeuDetAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ConDeuDetAdapter.NAME + ": "+ conDeuDetAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ConDeuDetAdapter.NAME, conDeuDetAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ConDeuDetAdapter.NAME, conDeuDetAdapterVO);
			 
			saveDemodaMessages(request, conDeuDetAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConDeuDetAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_CONDEUDET, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ConDeuDetAdapter conDeuDetAdapterVO = (ConDeuDetAdapter) userSession.get(ConDeuDetAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (conDeuDetAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ConDeuDetAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ConDeuDetAdapter.NAME); 
			}
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(conDeuDetAdapterVO, request);
			
			// Verifica si se selecciono alguna deuda
			if(request.getParameter("idsDeudaSelected")==null){
				conDeuDetAdapterVO.addRecoverableError(GdeError.CONDEUDET_AGEGAR_NINGUNA_DEUDA_SELECTED);
			}

            // Tiene errores recuperables
			if (conDeuDetAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + conDeuDetAdapterVO.infoString()); 
				// Envio el VO al request
				request.setAttribute(ConDeuDetAdapter.NAME, conDeuDetAdapterVO);
				// Subo el apdater al userSession
				userSession.put(ConDeuDetAdapter.NAME, conDeuDetAdapterVO);
				saveDemodaErrors(request, conDeuDetAdapterVO);				
				return mapping.findForward(GdeConstants.FWD_CONDEUDET_EDIT_ADAPTER);
			}
			
			// llamada al servicio
			conDeuDetAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().createConDeuDet(userSession, conDeuDetAdapterVO);
			
            // Tiene errores recuperables
			if (conDeuDetAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + conDeuDetAdapterVO.infoString()); 
				saveDemodaErrors(request, conDeuDetAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ConDeuDetAdapter.NAME, conDeuDetAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (conDeuDetAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + conDeuDetAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ConDeuDetAdapter.NAME, conDeuDetAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ConDeuDetAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConDeuDetAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_CONDEUDET, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ConDeuDetAdapter conDeuDetAdapterVO = (ConDeuDetAdapter) userSession.get(ConDeuDetAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (conDeuDetAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ConDeuDetAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ConDeuDetAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(conDeuDetAdapterVO, request);
			
            // Tiene errores recuperables
			if (conDeuDetAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + conDeuDetAdapterVO.infoString()); 
				saveDemodaErrors(request, conDeuDetAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ConDeuDetAdapter.NAME, conDeuDetAdapterVO);
			}
			
			// llamada al servicio
			ConDeuDetVO conDeuDetVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().updateConDeuDet(userSession, conDeuDetAdapterVO.getConDeuDet());
			
            // Tiene errores recuperables
			if (conDeuDetVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + conDeuDetAdapterVO.infoString()); 
				saveDemodaErrors(request, conDeuDetVO);
				return forwardErrorRecoverable(mapping, request, userSession, ConDeuDetAdapter.NAME, conDeuDetAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (conDeuDetVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + conDeuDetAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ConDeuDetAdapter.NAME, conDeuDetAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ConDeuDetAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConDeuDetAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_CONDEUDET, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ConDeuDetAdapter conDeuDetAdapterVO = (ConDeuDetAdapter) userSession.get(ConDeuDetAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (conDeuDetAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ConDeuDetAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ConDeuDetAdapter.NAME); 
			}

			// llamada al servicio
			ConDeuDetVO conDeuDetVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().deleteConDeuDet(userSession, conDeuDetAdapterVO.getConDeuDet());
			
            // Tiene errores recuperables
			if (conDeuDetVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + conDeuDetAdapterVO.infoString());
				saveDemodaErrors(request, conDeuDetVO);				
				request.setAttribute(ConDeuDetAdapter.NAME, conDeuDetAdapterVO);
				return mapping.findForward(GdeConstants.FWD_CONDEUDET_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (conDeuDetVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + conDeuDetAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ConDeuDetAdapter.NAME, conDeuDetAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ConDeuDetAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConDeuDetAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ConDeuDetAdapter.NAME);
		
	}		
}
