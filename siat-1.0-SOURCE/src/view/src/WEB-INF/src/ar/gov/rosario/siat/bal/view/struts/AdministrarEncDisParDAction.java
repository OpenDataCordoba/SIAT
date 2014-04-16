//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.bal.iface.model.DisParAdapter;
import ar.gov.rosario.siat.bal.iface.model.DisParVO;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class AdministrarEncDisParDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEncDisParDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_DISPAR_ENC, act);		

			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			DisParAdapter disParAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());

				if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
					stringServicio = "getDisParAdapterForUpdate(userSession, commonKey)";
					disParAdapterVO = BalServiceLocator.getDistribucionService().getDisParAdapterForUpdate(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_DISPAR_ENC_EDIT_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
					stringServicio = "getDisParAdapterForCreate(userSession)";
					disParAdapterVO = BalServiceLocator.getDistribucionService().getDisParAdapterForCreate(userSession);
					actionForward = mapping.findForward(BalConstants.FWD_DISPAR_ENC_EDIT_ADAPTER);
				}
				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (disParAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + disParAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, DisParAdapter.ENC_NAME, disParAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				disParAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + DisParAdapter.ENC_NAME + ": "+ disParAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(DisParAdapter.ENC_NAME, disParAdapterVO);
				// Subo el apdater al userSession
				userSession.put(DisParAdapter.ENC_NAME, disParAdapterVO);
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, DisParAdapter.ENC_NAME);
			}
		}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping, 
				BalSecurityConstants.ABM_DISPAR_ENC, BaseSecurityConstants.AGREGAR);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				DisParAdapter disParAdapterVO = (DisParAdapter) userSession.get(DisParAdapter.ENC_NAME);
				
				// Si es nulo no se puede continuar
				if (disParAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + DisParAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, DisParAdapter.ENC_NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(disParAdapterVO, request);
				
	            // Tiene errores recuperables
				if (disParAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + disParAdapterVO.infoString()); 
					saveDemodaErrors(request, disParAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, DisParAdapter.ENC_NAME, disParAdapterVO);
				}
				
				// llamada al servicio
				DisParVO disParVO = BalServiceLocator.getDistribucionService().createDisPar(userSession, disParAdapterVO.getDisPar());
				
	            // Tiene errores recuperables
				if (disParVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + disParVO.infoString()); 
					saveDemodaErrors(request, disParVO);
					return forwardErrorRecoverable(mapping, request, userSession, DisParAdapter.ENC_NAME, disParAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (disParVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + disParVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, DisParAdapter.ENC_NAME, disParAdapterVO);
				}

				return forwardConfirmarOk(mapping, request, funcName, DisParAdapter.ENC_NAME);
									
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, DisParAdapter.ENC_NAME);
			}
	}
	
	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				BalSecurityConstants.ABM_DISPAR_ENC, BaseSecurityConstants.MODIFICAR);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				DisParAdapter disParAdapterVO = (DisParAdapter) userSession.get(DisParAdapter.ENC_NAME);
				
				// Si es nulo no se puede continuar
				if (disParAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + DisParAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, DisParAdapter.ENC_NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(disParAdapterVO, request);
				
	            // Tiene errores recuperables
				if (disParAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + disParAdapterVO.infoString()); 
					saveDemodaErrors(request, disParAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, DisParAdapter.ENC_NAME, disParAdapterVO);
				}
				
				// llamada al servicio
				DisParVO disParVO = BalServiceLocator.getDistribucionService().updateDisPar(userSession, disParAdapterVO.getDisPar());
				
	            // Tiene errores recuperables
				if (disParVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + disParAdapterVO.infoString()); 
					saveDemodaErrors(request, disParVO);
					return forwardErrorRecoverable(mapping, request, userSession, DisParAdapter.ENC_NAME, disParAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (disParVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + disParAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, DisParAdapter.ENC_NAME, disParAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, DisParAdapter.ENC_NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, DisParAdapter.ENC_NAME);
			}
	}

	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, DisParAdapter.ENC_NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, DisParAdapter.ENC_NAME);
	}
	
}
