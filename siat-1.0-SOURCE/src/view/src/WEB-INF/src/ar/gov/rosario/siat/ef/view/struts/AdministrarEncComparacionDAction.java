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
import ar.gov.rosario.siat.ef.iface.model.ComparacionAdapter;
import ar.gov.rosario.siat.ef.iface.model.ComparacionVO;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarEncComparacionDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEncComparacionDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_COMPARACION_ENC, act);		

		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ComparacionAdapter comparacionAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());

			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getComparacionAdapterForUpdate(userSession, commonKey)";
				comparacionAdapterVO = EfServiceLocator.getFiscalizacionService().getComparacionAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_COMPARACION_ENC_EDIT_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getComparacionAdapterForCreate(userSession)";
				comparacionAdapterVO = EfServiceLocator.getFiscalizacionService().getComparacionAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_COMPARACION_ENC_EDIT_ADAPTER);
			}
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (comparacionAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + comparacionAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ComparacionAdapter.ENC_NAME, comparacionAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			comparacionAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ComparacionAdapter.ENC_NAME + ": "+ comparacionAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ComparacionAdapter.ENC_NAME, comparacionAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ComparacionAdapter.ENC_NAME, comparacionAdapterVO);

			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ComparacionAdapter.ENC_NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, 
			EfSecurityConstants.ABM_COMPARACION_ENC, BaseSecurityConstants.AGREGAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ComparacionAdapter comparacionAdapterVO = (ComparacionAdapter) userSession.get(ComparacionAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (comparacionAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ComparacionAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ComparacionAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(comparacionAdapterVO, request);
			
            // Tiene errores recuperables
			if (comparacionAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + comparacionAdapterVO.infoString()); 
				saveDemodaErrors(request, comparacionAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ComparacionAdapter.ENC_NAME, comparacionAdapterVO);
			}
			
			// llamada al servicio
			ComparacionVO comparacionVO = EfServiceLocator.getFiscalizacionService().createComparacion(userSession, comparacionAdapterVO.getComparacion());
			
            // Tiene errores recuperables
			if (comparacionVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + comparacionVO.infoString()); 
				saveDemodaErrors(request, comparacionVO);
				return forwardErrorRecoverable(mapping, request, userSession, ComparacionAdapter.ENC_NAME, comparacionAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (comparacionVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + comparacionVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ComparacionAdapter.ENC_NAME, comparacionAdapterVO);
			}

			
			// Si tiene permiso lo dirijo al adapter de modificacion, 
			// sino vuelve al searchPage
			if (hasAccess(userSession, EfSecurityConstants.ABM_COMPARACION, BaseSecurityConstants.MODIFICAR)) {
				
				// seteo el id para que lo use el siguiente action 
				userSession.getNavModel().setSelectedId(comparacionVO.getId().toString());

				// lo dirijo al adapter de modificacion
				return forwardConfirmarOk(mapping, request, funcName, ComparacionAdapter.ENC_NAME, 
					EfConstants.PATH_ADMINISTRAR_COMPARACION, BaseConstants.METHOD_INICIALIZAR, 
					BaseConstants.ACT_MODIFICAR);
			} else {
				
				// lo dirijo al searchPage				
				return forwardConfirmarOk(mapping, request, funcName, ComparacionAdapter.ENC_NAME);
				
			}
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ComparacionAdapter.ENC_NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, 
			EfSecurityConstants.ABM_COMPARACION_ENC, BaseSecurityConstants.MODIFICAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ComparacionAdapter comparacionAdapterVO = (ComparacionAdapter) userSession.get(ComparacionAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (comparacionAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ComparacionAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ComparacionAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(comparacionAdapterVO, request);
			
            // Tiene errores recuperables
			if (comparacionAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + comparacionAdapterVO.infoString()); 
				saveDemodaErrors(request, comparacionAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ComparacionAdapter.ENC_NAME, comparacionAdapterVO);
			}
			
			// llamada al servicio
			ComparacionVO comparacionVO = EfServiceLocator.getFiscalizacionService().updateComparacion(userSession, comparacionAdapterVO.getComparacion());
			
            // Tiene errores recuperables
			if (comparacionVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + comparacionAdapterVO.infoString()); 
				saveDemodaErrors(request, comparacionVO);
				return forwardErrorRecoverable(mapping, request, userSession, ComparacionAdapter.ENC_NAME, comparacionAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (comparacionVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + comparacionAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ComparacionAdapter.ENC_NAME, comparacionAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ComparacionAdapter.ENC_NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ComparacionAdapter.ENC_NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ComparacionAdapter.ENC_NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, ComparacionAdapter.ENC_NAME);
		
	}
	
}
	
