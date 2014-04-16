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
import ar.gov.rosario.siat.gde.iface.model.ProcuradorAdapter;
import ar.gov.rosario.siat.gde.iface.model.ProcuradorVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarEncProcuradorDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEncProcuradorDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PROCURADOR_ENC, act);		

		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ProcuradorAdapter procuradorAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());

			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getProcuradorAdapterForUpdate(userSession, commonKey)";
				procuradorAdapterVO = GdeServiceLocator.getDefinicionService().getProcuradorAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PROCURADOR_ENC_EDIT_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getProcuradorAdapterForCreate(userSession)";
				procuradorAdapterVO = GdeServiceLocator.getDefinicionService().getProcuradorAdapterForCreate(userSession);
				actionForward = mapping.findForward(GdeConstants.FWD_PROCURADOR_ENC_EDIT_ADAPTER);
			}
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (procuradorAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + procuradorAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcuradorAdapter.ENC_NAME, procuradorAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			procuradorAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ProcuradorAdapter.ENC_NAME + ": "+ procuradorAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ProcuradorAdapter.ENC_NAME, procuradorAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ProcuradorAdapter.ENC_NAME, procuradorAdapterVO);

			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcuradorAdapter.ENC_NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, 
			GdeSecurityConstants.ABM_PROCURADOR_ENC, BaseSecurityConstants.AGREGAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ProcuradorAdapter procuradorAdapterVO = (ProcuradorAdapter) userSession.get(ProcuradorAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (procuradorAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcuradorAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcuradorAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(procuradorAdapterVO, request);
			
            // Tiene errores recuperables
			if (procuradorAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procuradorAdapterVO.infoString()); 
				saveDemodaErrors(request, procuradorAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcuradorAdapter.ENC_NAME, procuradorAdapterVO);
			}
			
			// llamada al servicio
			ProcuradorVO procuradorVO = GdeServiceLocator.getDefinicionService().createProcurador(userSession, procuradorAdapterVO.getProcurador());
			
            // Tiene errores recuperables
			if (procuradorVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procuradorVO.infoString()); 
				saveDemodaErrors(request, procuradorVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcuradorAdapter.ENC_NAME, procuradorAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (procuradorVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + procuradorVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcuradorAdapter.ENC_NAME, procuradorAdapterVO);
			}

			// Si tiene permiso lo dirijo al adapter de modificacion, 
			// sino vuelve al searchPage
			if (hasAccess(userSession, GdeSecurityConstants.ABM_PROCURADOR, 
				BaseSecurityConstants.MODIFICAR)) {
				
				// seteo el id para que lo use el siguiente action 
				userSession.getNavModel().setSelectedId(procuradorVO.getId().toString());

				// lo dirijo al adapter de modificacion
				return forwardConfirmarOk(mapping, request, funcName, ProcuradorAdapter.ENC_NAME, 
					GdeConstants.PATH_ADMINISTRAR_PROCURADOR, BaseConstants.METHOD_INICIALIZAR, 
					BaseConstants.ACT_MODIFICAR);
			} else {
				
				// lo dirijo al searchPage				
				return forwardConfirmarOk(mapping, request, funcName, ProcuradorAdapter.ENC_NAME);
				
			}
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcuradorAdapter.ENC_NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, 
			GdeSecurityConstants.ABM_PROCURADOR_ENC, BaseSecurityConstants.MODIFICAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ProcuradorAdapter procuradorAdapterVO = (ProcuradorAdapter) userSession.get(ProcuradorAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (procuradorAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcuradorAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcuradorAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(procuradorAdapterVO, request);
			
            // Tiene errores recuperables
			if (procuradorAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procuradorAdapterVO.infoString()); 
				saveDemodaErrors(request, procuradorAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcuradorAdapter.ENC_NAME, procuradorAdapterVO);
			}
			
			// llamada al servicio
			ProcuradorVO procuradorVO = GdeServiceLocator.getDefinicionService().updateProcurador(userSession, procuradorAdapterVO.getProcurador());
			
            // Tiene errores recuperables
			if (procuradorVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procuradorAdapterVO.infoString()); 
				saveDemodaErrors(request, procuradorVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcuradorAdapter.ENC_NAME, procuradorAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (procuradorVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + procuradorAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcuradorAdapter.ENC_NAME, procuradorAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ProcuradorAdapter.ENC_NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcuradorAdapter.ENC_NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ProcuradorAdapter.ENC_NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, ProcuradorAdapter.ENC_NAME);
		
	}
	
}
	
