//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.view.struts;

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
import ar.gov.rosario.siat.esp.iface.model.HabExeAdapter;
import ar.gov.rosario.siat.esp.iface.model.HabExeVO;
import ar.gov.rosario.siat.esp.iface.service.EspServiceLocator;
import ar.gov.rosario.siat.esp.iface.util.EspError;
import ar.gov.rosario.siat.esp.iface.util.EspSecurityConstants;
import ar.gov.rosario.siat.esp.view.util.EspConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class AdministrarHabExeDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarHabExeDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = canAccess(request, mapping, EspSecurityConstants.ABM_HABEXE, act);		
			if (userSession == null) return forwardErrorSession(request);

			NavModel navModel = userSession.getNavModel();

			HabExeAdapter habExeAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
					stringServicio = "getHabExeAdapterForView(userSession, commonKey)";
					habExeAdapterVO = EspServiceLocator.getHabilitacionService().getHabExeAdapterForView
						(userSession, commonKey);
					actionForward = mapping.findForward(EspConstants.FWD_HABEXE_VIEW_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
					stringServicio = "getHabExeAdapterForUpdate(userSession, commonKey)";
					habExeAdapterVO = EspServiceLocator.getHabilitacionService().getHabExeAdapterForUpdate
						(userSession, commonKey);
					actionForward = mapping.findForward(EspConstants.FWD_HABEXE_EDIT_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
					stringServicio = "getHabExeAdapterForDelete(userSession, commonKey)";
					habExeAdapterVO = EspServiceLocator.getHabilitacionService().getHabExeAdapterForView
						(userSession, commonKey);
					habExeAdapterVO.addMessage(BaseError.MSG_ELIMINAR, EspError.HABEXE_LABEL);				
					actionForward = mapping.findForward(EspConstants.FWD_HABEXE_VIEW_ADAPTER);					
				}
				if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
					stringServicio = "getHabExeAdapterForCreate(userSession)";
					habExeAdapterVO = EspServiceLocator.getHabilitacionService().getHabExeAdapterForCreate
						(userSession, commonKey);
					actionForward = mapping.findForward(EspConstants.FWD_HABEXE_EDIT_ADAPTER);
				}

				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (habExeAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + habExeAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, HabExeAdapter.NAME, habExeAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				habExeAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + HabExeAdapter.NAME + ": "+ habExeAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(HabExeAdapter.NAME, habExeAdapterVO);
				// Subo el apdater al userSession
				userSession.put(HabExeAdapter.NAME, habExeAdapterVO);
				
				saveDemodaMessages(request, habExeAdapterVO);			
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, HabExeAdapter.NAME);
			}
		}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping,
				EspSecurityConstants.ABM_HABEXE, BaseSecurityConstants.AGREGAR); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				HabExeAdapter habExeAdapterVO = (HabExeAdapter) userSession.get(HabExeAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (habExeAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + HabExeAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, HabExeAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(habExeAdapterVO, request);
				
	            // Tiene errores recuperables
				if (habExeAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + habExeAdapterVO.infoString()); 
					saveDemodaErrors(request, habExeAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, HabExeAdapter.NAME, habExeAdapterVO);
				}
				
				// llamada al servicio
				HabExeVO habExeVO = EspServiceLocator.getHabilitacionService().createHabExe(userSession, habExeAdapterVO.getHabExe());
				
	            // Tiene errores recuperables
				if (habExeVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + habExeVO.infoString()); 
					saveDemodaErrors(request, habExeVO);
					return forwardErrorRecoverable(mapping, request, userSession, HabExeAdapter.NAME, habExeAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (habExeVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + habExeVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, HabExeAdapter.NAME, habExeAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, HabExeAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, HabExeAdapter.NAME);
			}
		}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				EspSecurityConstants.ABM_HABEXE, BaseSecurityConstants.AGREGAR);
			
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				HabExeAdapter habExeAdapterVO = (HabExeAdapter) userSession.get(HabExeAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (habExeAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + HabExeAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, HabExeAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(habExeAdapterVO, request);
				
	            // Tiene errores recuperables
				if (habExeAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + habExeAdapterVO.infoString()); 
					saveDemodaErrors(request, habExeAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						HabExeAdapter.NAME, habExeAdapterVO);
				}
				
				// llamada al servicio
				HabExeVO habExeVO = EspServiceLocator.getHabilitacionService().updateHabExe(userSession, habExeAdapterVO.getHabExe());
				
	            // Tiene errores recuperables
				if (habExeVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + habExeAdapterVO.infoString()); 
					saveDemodaErrors(request, habExeVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						HabExeAdapter.NAME, habExeAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (habExeVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + habExeAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						HabExeAdapter.NAME, habExeAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, HabExeAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, HabExeAdapter.NAME);
			}
		}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				EspSecurityConstants.ABM_HABEXE, BaseSecurityConstants.ELIMINAR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				HabExeAdapter habExeAdapterVO = (HabExeAdapter) userSession.get(HabExeAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (habExeAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + HabExeAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, HabExeAdapter.NAME); 
				}

				// llamada al servicio
				HabExeVO habExeVO = EspServiceLocator.getHabilitacionService().deleteHabExe(userSession, habExeAdapterVO.getHabExe());
				
	            // Tiene errores recuperables
				if (habExeVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + habExeAdapterVO.infoString());
					saveDemodaErrors(request, habExeVO);				
					request.setAttribute(HabExeAdapter.NAME, habExeAdapterVO);
					return mapping.findForward(EspConstants.FWD_HABEXE_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (habExeVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + habExeAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						HabExeAdapter.NAME, habExeAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, HabExeAdapter.NAME);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, HabExeAdapter.NAME);
			}
		}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, HabExeAdapter.NAME);
	}

}
