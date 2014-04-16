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
import ar.gov.rosario.siat.esp.iface.model.EntHabAdapter;
import ar.gov.rosario.siat.esp.iface.model.EntHabVO;
import ar.gov.rosario.siat.esp.iface.service.EspServiceLocator;
import ar.gov.rosario.siat.esp.iface.util.EspError;
import ar.gov.rosario.siat.esp.iface.util.EspSecurityConstants;
import ar.gov.rosario.siat.esp.view.util.EspConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class AdministrarEntHabDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEntHabDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = canAccess(request, mapping, EspSecurityConstants.ABM_ENTHAB, act);		
			if (userSession == null) return forwardErrorSession(request);

			NavModel navModel = userSession.getNavModel();

			EntHabAdapter entHabAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
					stringServicio = "getEntHabAdapterForView(userSession, commonKey)";
					entHabAdapterVO = EspServiceLocator.getHabilitacionService().getEntHabAdapterForView
						(userSession, commonKey);
					actionForward = mapping.findForward(EspConstants.FWD_ENTHAB_VIEW_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
					stringServicio = "getEntHabAdapterForUpdate(userSession, commonKey)";
					entHabAdapterVO = EspServiceLocator.getHabilitacionService().getEntHabAdapterForUpdate
						(userSession, commonKey);
					actionForward = mapping.findForward(EspConstants.FWD_ENTHAB_EDIT_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
					stringServicio = "getEntHabAdapterForDelete(userSession, commonKey)";
					entHabAdapterVO = EspServiceLocator.getHabilitacionService().getEntHabAdapterForView
						(userSession, commonKey);
					entHabAdapterVO.addMessage(BaseError.MSG_ELIMINAR, EspError.ENTHAB_LABEL);				
					actionForward = mapping.findForward(EspConstants.FWD_ENTHAB_VIEW_ADAPTER);					
				}
				if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
					stringServicio = "getEntHabAdapterForCreate(userSession)";
					entHabAdapterVO = EspServiceLocator.getHabilitacionService().getEntHabAdapterForCreate
						(userSession, commonKey);
					actionForward = mapping.findForward(EspConstants.FWD_ENTHAB_EDIT_ADAPTER);
				}

				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (entHabAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + entHabAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, EntHabAdapter.NAME, entHabAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				entHabAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + EntHabAdapter.NAME + ": "+ entHabAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(EntHabAdapter.NAME, entHabAdapterVO);
				// Subo el apdater al userSession
				userSession.put(EntHabAdapter.NAME, entHabAdapterVO);
				
				saveDemodaMessages(request, entHabAdapterVO);			
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, EntHabAdapter.NAME);
			}
		}
	

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping,
				EspSecurityConstants.ABM_ENTHAB, BaseSecurityConstants.AGREGAR); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				EntHabAdapter entHabAdapterVO = (EntHabAdapter) userSession.get(EntHabAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (entHabAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + EntHabAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, EntHabAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(entHabAdapterVO, request);
				
	            // Tiene errores recuperables
				if (entHabAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + entHabAdapterVO.infoString()); 
					saveDemodaErrors(request, entHabAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, EntHabAdapter.NAME, entHabAdapterVO);
				}
				
				// llamada al servicio
				EntHabVO entHabVO = EspServiceLocator.getHabilitacionService().createEntHab(userSession, entHabAdapterVO.getEntHab());
				
	            // Tiene errores recuperables
				if (entHabVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + entHabVO.infoString()); 
					saveDemodaErrors(request, entHabVO);
					return forwardErrorRecoverable(mapping, request, userSession, EntHabAdapter.NAME, entHabAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (entHabVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + entHabVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, EntHabAdapter.NAME, entHabAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, EntHabAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, EntHabAdapter.NAME);
			}
		}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				EspSecurityConstants.ABM_ENTHAB, BaseSecurityConstants.MODIFICAR);
			
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				EntHabAdapter entHabAdapterVO = (EntHabAdapter) userSession.get(EntHabAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (entHabAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + EntHabAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, EntHabAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(entHabAdapterVO, request);
				
	            // Tiene errores recuperables
				if (entHabAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + entHabAdapterVO.infoString()); 
					saveDemodaErrors(request, entHabAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						EntHabAdapter.NAME, entHabAdapterVO);
				}
				
				// llamada al servicio
				EntHabVO entHabVO = EspServiceLocator.getHabilitacionService().updateEntHab(userSession, entHabAdapterVO.getEntHab());
				
	            // Tiene errores recuperables
				if (entHabVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + entHabAdapterVO.infoString()); 
					saveDemodaErrors(request, entHabVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						EntHabAdapter.NAME, entHabAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (entHabVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + entHabAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						EntHabAdapter.NAME, entHabAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, EntHabAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, EntHabAdapter.NAME);
			}
		}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				EspSecurityConstants.ABM_ENTHAB, BaseSecurityConstants.ELIMINAR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				EntHabAdapter entHabAdapterVO = (EntHabAdapter) userSession.get(EntHabAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (entHabAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + EntHabAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, EntHabAdapter.NAME); 
				}

				// llamada al servicio
				EntHabVO entHabVO = EspServiceLocator.getHabilitacionService().deleteEntHab(userSession, entHabAdapterVO.getEntHab());
				
	            // Tiene errores recuperables
				if (entHabVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + entHabAdapterVO.infoString());
					saveDemodaErrors(request, entHabVO);				
					request.setAttribute(EntHabAdapter.NAME, entHabAdapterVO);
					return mapping.findForward(EspConstants.FWD_ENTHAB_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (entHabVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + entHabAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						EntHabAdapter.NAME, entHabAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, EntHabAdapter.NAME);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, EntHabAdapter.NAME);
			}
		}

		public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, EntHabAdapter.NAME);
		}

}
