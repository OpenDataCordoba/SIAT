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
import ar.gov.rosario.siat.def.iface.model.CodEmiAdapter;
import ar.gov.rosario.siat.def.iface.model.CodEmiVO;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarEncCodEmiDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEncCodEmiDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_CODEMI_ENC, act);		

		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		CodEmiAdapter codEmiAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());

			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getCodEmiAdapterForUpdate(userSession, commonKey)";
				codEmiAdapterVO = DefServiceLocator.getEmisionService().getCodEmiAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_CODEMI_ENC_EDIT_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getCodEmiAdapterForCreate(userSession)";
				codEmiAdapterVO = DefServiceLocator.getEmisionService().getCodEmiAdapterForCreate(userSession);
				actionForward = mapping.findForward(DefConstants.FWD_CODEMI_ENC_EDIT_ADAPTER);
			}
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (codEmiAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + codEmiAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CodEmiAdapter.ENC_NAME, codEmiAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			codEmiAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + CodEmiAdapter.ENC_NAME + ": "+ codEmiAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(CodEmiAdapter.ENC_NAME, codEmiAdapterVO);
			// Subo el apdater al userSession
			userSession.put(CodEmiAdapter.ENC_NAME, codEmiAdapterVO);

			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CodEmiAdapter.ENC_NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, 
			DefSecurityConstants.ABM_CODEMI_ENC, BaseSecurityConstants.AGREGAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CodEmiAdapter codEmiAdapterVO = (CodEmiAdapter) userSession.get(CodEmiAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (codEmiAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CodEmiAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CodEmiAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(codEmiAdapterVO, request);
			
            // Tiene errores recuperables
			if (codEmiAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + codEmiAdapterVO.infoString()); 
				saveDemodaErrors(request, codEmiAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CodEmiAdapter.ENC_NAME, codEmiAdapterVO);
			}
			
			// llamada al servicio
			CodEmiVO codEmiVO = DefServiceLocator.getEmisionService().createCodEmi(userSession, codEmiAdapterVO.getCodEmi());
			
            // Tiene errores recuperables
			if (codEmiVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + codEmiVO.infoString()); 
				saveDemodaErrors(request, codEmiVO);
				return forwardErrorRecoverable(mapping, request, userSession, CodEmiAdapter.ENC_NAME, codEmiAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (codEmiVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + codEmiVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CodEmiAdapter.ENC_NAME, codEmiAdapterVO);
			}

			// Si tiene permiso lo dirijo al adapter de modificacion, 
			// sino vuelve al searchPage
			if (hasAccess(userSession, DefSecurityConstants.ABM_CODEMI, 
				BaseSecurityConstants.MODIFICAR)) {
				
				// seteo el id para que lo use el siguiente action 
				userSession.getNavModel().setSelectedId(codEmiVO.getId().toString());

				// lo dirijo al adapter de modificacion
				return forwardConfirmarOk(mapping, request, funcName, CodEmiAdapter.ENC_NAME, 
					DefConstants.PATH_ADMINISTRAR_CODEMI, BaseConstants.METHOD_INICIALIZAR, 
					BaseConstants.ACT_MODIFICAR);
			} else {
				// lo dirijo al searchPage				
				return forwardConfirmarOk(mapping, request, funcName, CodEmiAdapter.ENC_NAME);
			}
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CodEmiAdapter.ENC_NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, 
			DefSecurityConstants.ABM_CODEMI_ENC, BaseSecurityConstants.MODIFICAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CodEmiAdapter codEmiAdapterVO = (CodEmiAdapter) userSession.get(CodEmiAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (codEmiAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CodEmiAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CodEmiAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(codEmiAdapterVO, request);
			
            // Tiene errores recuperables
			if (codEmiAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + codEmiAdapterVO.infoString()); 
				saveDemodaErrors(request, codEmiAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CodEmiAdapter.ENC_NAME, codEmiAdapterVO);
			}
			
			// llamada al servicio
			CodEmiVO codEmiVO = DefServiceLocator.getEmisionService().updateCodEmi(userSession, codEmiAdapterVO.getCodEmi());
			
            // Tiene errores recuperables
			if (codEmiVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + codEmiAdapterVO.infoString()); 
				saveDemodaErrors(request, codEmiVO);
				return forwardErrorRecoverable(mapping, request, userSession, CodEmiAdapter.ENC_NAME, codEmiAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (codEmiVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + codEmiAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CodEmiAdapter.ENC_NAME, codEmiAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CodEmiAdapter.ENC_NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CodEmiAdapter.ENC_NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, CodEmiAdapter.ENC_NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, CodEmiAdapter.ENC_NAME);
		
	}
	
	public ActionForward paramTipCodEmi(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CodEmiAdapter codEmiAdapterVO = (CodEmiAdapter) userSession.get(CodEmiAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (codEmiAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CodEmiAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CodEmiAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(codEmiAdapterVO, request);
			
            // Tiene errores recuperables
			if (codEmiAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + codEmiAdapterVO.infoString()); 
				saveDemodaErrors(request, codEmiAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CodEmiAdapter.ENC_NAME, codEmiAdapterVO);
			}
			
			// llamada al servicio
			codEmiAdapterVO = DefServiceLocator.getEmisionService()
				.getCodEmiAdapterParamTipCodEmi(userSession, codEmiAdapterVO);
			
            // Tiene errores recuperables
			if (codEmiAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + codEmiAdapterVO.infoString()); 
				saveDemodaErrors(request, codEmiAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CodEmiAdapter.ENC_NAME, codEmiAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (codEmiAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + codEmiAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CodEmiAdapter.ENC_NAME, codEmiAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(CodEmiAdapter.ENC_NAME, codEmiAdapterVO);
			// Subo el apdater al userSession
			userSession.put(CodEmiAdapter.ENC_NAME, codEmiAdapterVO);
			
			return mapping.findForward(DefConstants.FWD_CODEMI_ENC_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CodEmiAdapter.ENC_NAME);
		}
	}
	
}
	
