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
import ar.gov.rosario.siat.def.iface.model.EmiMatAdapter;
import ar.gov.rosario.siat.def.iface.model.EmiMatVO;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarEncEmiMatDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEncEmiMatDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_EMIMAT_ENC, act);		

		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		EmiMatAdapter emiMatAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());

			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getEmiMatAdapterForUpdate(userSession, commonKey)";
				emiMatAdapterVO = DefServiceLocator.getEmisionService().getEmiMatAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_EMIMAT_ENC_EDIT_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getEmiMatAdapterForCreate(userSession)";
				emiMatAdapterVO = DefServiceLocator.getEmisionService().getEmiMatAdapterForCreate(userSession);
				actionForward = mapping.findForward(DefConstants.FWD_EMIMAT_ENC_EDIT_ADAPTER);
			}
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (emiMatAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + emiMatAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EmiMatAdapter.ENC_NAME, emiMatAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			emiMatAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + EmiMatAdapter.ENC_NAME + ": "+ emiMatAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(EmiMatAdapter.ENC_NAME, emiMatAdapterVO);
			// Subo el apdater al userSession
			userSession.put(EmiMatAdapter.ENC_NAME, emiMatAdapterVO);

			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmiMatAdapter.ENC_NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, 
			DefSecurityConstants.ABM_EMIMAT_ENC, BaseSecurityConstants.AGREGAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			EmiMatAdapter emiMatAdapterVO = (EmiMatAdapter) userSession.get(EmiMatAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (emiMatAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EmiMatAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EmiMatAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(emiMatAdapterVO, request);
			
            // Tiene errores recuperables
			if (emiMatAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emiMatAdapterVO.infoString()); 
				saveDemodaErrors(request, emiMatAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, EmiMatAdapter.ENC_NAME, emiMatAdapterVO);
			}
			
			// llamada al servicio
			EmiMatVO emiMatVO = DefServiceLocator.getEmisionService().createEmiMat(userSession, emiMatAdapterVO.getEmiMat());
			
            // Tiene errores recuperables
			if (emiMatVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emiMatVO.infoString()); 
				saveDemodaErrors(request, emiMatVO);
				return forwardErrorRecoverable(mapping, request, userSession, EmiMatAdapter.ENC_NAME, emiMatAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (emiMatVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + emiMatVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EmiMatAdapter.ENC_NAME, emiMatAdapterVO);
			}

			// seteo el id para que lo use el siguiente action 
			userSession.getNavModel().setSelectedId(emiMatVO.getId().toString());

			// lo dirijo al adapter de modificacion
			return forwardConfirmarOk(mapping, request, funcName, EmiMatAdapter.ENC_NAME, 
				DefConstants.PATH_ADMINISTRAR_EMIMAT, BaseConstants.METHOD_INICIALIZAR, 
				BaseConstants.ACT_MODIFICAR);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmiMatAdapter.ENC_NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, 
			DefSecurityConstants.ABM_EMIMAT_ENC, BaseSecurityConstants.MODIFICAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			EmiMatAdapter emiMatAdapterVO = (EmiMatAdapter) userSession.get(EmiMatAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (emiMatAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EmiMatAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EmiMatAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(emiMatAdapterVO, request);
			
            // Tiene errores recuperables
			if (emiMatAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emiMatAdapterVO.infoString()); 
				saveDemodaErrors(request, emiMatAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, EmiMatAdapter.ENC_NAME, emiMatAdapterVO);
			}
			
			// llamada al servicio
			EmiMatVO emiMatVO = DefServiceLocator.getEmisionService().updateEmiMat(userSession, emiMatAdapterVO.getEmiMat());
			
            // Tiene errores recuperables
			if (emiMatVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emiMatAdapterVO.infoString()); 
				saveDemodaErrors(request, emiMatVO);
				return forwardErrorRecoverable(mapping, request, userSession, EmiMatAdapter.ENC_NAME, emiMatAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (emiMatVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + emiMatAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EmiMatAdapter.ENC_NAME, emiMatAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, EmiMatAdapter.ENC_NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmiMatAdapter.ENC_NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, EmiMatAdapter.ENC_NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, EmiMatAdapter.ENC_NAME);
	}
		
}
	
