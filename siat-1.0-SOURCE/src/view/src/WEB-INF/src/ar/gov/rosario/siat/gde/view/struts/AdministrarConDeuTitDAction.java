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
import ar.gov.rosario.siat.gde.iface.model.ConDeuTitAdapter;
import ar.gov.rosario.siat.gde.iface.model.ConDeuTitVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarConDeuTitDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarConDeuTitDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ADM_TITULARES_CONSTANCIA_DEUDA_JUDICIAL, act);		
		if (userSession == null) return forwardErrorSession(request);
		
		ConDeuTitAdapter conDeuTitAdapterVO = null;

		NavModel navModel = userSession.getNavModel();
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
				stringServicio = "getConstanciaDeuAdapterForView(userSession, commonKey)";
				conDeuTitAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getConDeuTitAdapterForView(userSession, commonKey);					
				actionForward = mapping.findForward(GdeConstants.FWD_ADMIN_CONDEUTIT_VIEW_ADAPTER);
			}

			if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getConstanciaDeuAdapterForDelete(userSession, commonKey)";
				conDeuTitAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getConDeuTitAdapterForView(userSession, commonKey);					
				actionForward = mapping.findForward(GdeConstants.FWD_ADMIN_CONDEUTIT_VIEW_ADAPTER);
				conDeuTitAdapterVO.addMessage(BaseError.MSG_ELIMINAR, GdeError.CONSTANCIADEU_TITULAR_REF);					
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (conDeuTitAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + 
					stringServicio + ": " + conDeuTitAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ConDeuTitAdapter.NAME, conDeuTitAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			conDeuTitAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + 
					ConDeuTitAdapter.NAME + ": " + conDeuTitAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ConDeuTitAdapter.NAME, conDeuTitAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ConDeuTitAdapter.NAME, conDeuTitAdapterVO);
			
			saveDemodaMessages(request, conDeuTitAdapterVO);			
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConDeuTitAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ADM_TITULARES_CONSTANCIA_DEUDA_JUDICIAL,	BaseSecurityConstants.ELIMINAR);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ConDeuTitAdapter ConDeuTitAdapterVO = (ConDeuTitAdapter) userSession.get(ConDeuTitAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (ConDeuTitAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ConDeuTitAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ConDeuTitAdapter.NAME); 
			}

			// llamada al servicio
			ConDeuTitVO conDeuTitVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().deleteConDeuTit(userSession, ConDeuTitAdapterVO.getConDeuTit());
			
            // Tiene errores recuperables
			if (conDeuTitVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ConDeuTitAdapterVO.infoString());
				saveDemodaErrors(request, conDeuTitVO);				
				request.setAttribute(ConDeuTitAdapter.NAME, ConDeuTitAdapterVO);
				return mapping.findForward(GdeConstants.FWD_ADMIN_CONDEUTIT_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (conDeuTitVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + ConDeuTitAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ConDeuTitAdapter.NAME, ConDeuTitAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ConDeuTitAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConDeuTitAdapter.NAME);
		}
	}
	/*
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_CONSTANCIADUE, 
			BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ConstanciaDeuAdapter constanciaDeuAdapterVO = (ConstanciaDeuAdapter) userSession.get(ConstanciaDeuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (constanciaDeuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ConstanciaDeuAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ConstanciaDeuAdapter.NAME); 
			}

			// llamada al servicio
			ConstanciaDeuVO constanciaDeuVO = GdeServiceLocator.getAdmDeuJudService().activarConstanciaDeu
				(userSession, constanciaDeuAdapterVO.getConstanciaDeu());
			
            // Tiene errores recuperables
			if (constanciaDeuVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + constanciaDeuAdapterVO.infoString());
				saveDemodaErrors(request, constanciaDeuVO);				
				request.setAttribute(ConstanciaDeuAdapter.NAME, constanciaDeuAdapterVO);
				return mapping.findForward(GdeConstants.FWD_CONSTANCIADUE_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (constanciaDeuVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + constanciaDeuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ConstanciaDeuAdapter.NAME, constanciaDeuAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ConstanciaDeuAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConstanciaDeuAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_CONSTANCIADUE, 
			BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ConstanciaDeuAdapter constanciaDeuAdapterVO = (ConstanciaDeuAdapter) userSession.get(ConstanciaDeuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (constanciaDeuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ConstanciaDeuAdapter.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ConstanciaDeuAdapter.NAME); 
			}

			// llamada al servicio
			ConstanciaDeuVO constanciaDeuVO = GdeServiceLocator.getAdmDeuJudService().desactivarConstanciaDeu
				(userSession, constanciaDeuAdapterVO.getConstanciaDeu());
			
            // Tiene errores recuperables
			if (constanciaDeuVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + constanciaDeuAdapterVO.infoString());
				saveDemodaErrors(request, constanciaDeuVO);				
				request.setAttribute(ConstanciaDeuAdapter.NAME, constanciaDeuAdapterVO);
				return mapping.findForward(GdeConstants.FWD_CONSTANCIADUE_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (constanciaDeuVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + constanciaDeuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ConstanciaDeuAdapter.NAME, constanciaDeuAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ConstanciaDeuAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConstanciaDeuAdapter.NAME);
		}
	}
*/	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ConDeuTitAdapter.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, ConDeuTitAdapter.NAME);
		
	}


}
	
