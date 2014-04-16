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

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.def.iface.model.RecConAdapter;
import ar.gov.rosario.siat.def.iface.model.RecConVO;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarRecConDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarRecConDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_RECCON, act);		
			if (userSession == null) return forwardErrorSession(request);

			NavModel navModel = userSession.getNavModel();

			RecConAdapter recConAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
					stringServicio = "getRecConAdapterForView(userSession, commonKey)";
					recConAdapterVO = DefServiceLocator.getGravamenService().getRecConAdapterForView
						(userSession, commonKey);
					actionForward = mapping.findForward(DefConstants.FWD_RECCON_VIEW_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
					stringServicio = "getRecConAdapterForUpdate(userSession, commonKey)";
					recConAdapterVO = DefServiceLocator.getGravamenService().getRecConAdapterForUpdate
						(userSession, commonKey);
					actionForward = mapping.findForward(DefConstants.FWD_RECCON_EDIT_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
					stringServicio = "getRecConAdapterForDelete(userSession, commonKey)";
					recConAdapterVO = DefServiceLocator.getGravamenService().getRecConAdapterForView
						(userSession, commonKey);
					recConAdapterVO.addMessage(BaseError.MSG_ELIMINAR, DefError.RECCON_LABEL);				
					actionForward = mapping.findForward(DefConstants.FWD_RECCON_VIEW_ADAPTER);					
				}
				if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
					stringServicio = "getRecConAdapterForCreate(userSession)";
					recConAdapterVO = DefServiceLocator.getGravamenService().getRecConAdapterForCreate
						(userSession, commonKey);
					actionForward = mapping.findForward(DefConstants.FWD_RECCON_EDIT_ADAPTER);
				}

				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (recConAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + recConAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, RecConAdapter.NAME, recConAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				recConAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + RecConAdapter.NAME + ": "+ recConAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(RecConAdapter.NAME, recConAdapterVO);
				// Subo el apdater al userSession
				userSession.put(RecConAdapter.NAME, recConAdapterVO);
				
				saveDemodaMessages(request, recConAdapterVO);			
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RecConAdapter.NAME);
			}
		}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping,
				DefSecurityConstants.ABM_RECCON, BaseSecurityConstants.AGREGAR); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				RecConAdapter recConAdapterVO = (RecConAdapter) userSession.get(RecConAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (recConAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RecConAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RecConAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(recConAdapterVO, request);
				
	            // Tiene errores recuperables
				if (recConAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recConAdapterVO.infoString()); 
					saveDemodaErrors(request, recConAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, RecConAdapter.NAME, recConAdapterVO);
				}
				
				// llamada al servicio
				RecConVO recConVO = DefServiceLocator.getGravamenService().createRecCon(userSession, recConAdapterVO.getRecCon());
				
	            // Tiene errores recuperables
				if (recConVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recConVO.infoString()); 
					saveDemodaErrors(request, recConVO);
					return forwardErrorRecoverable(mapping, request, userSession, RecConAdapter.NAME, recConAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (recConVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + recConVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, RecConAdapter.NAME, recConAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, RecConAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RecConAdapter.NAME);
			}
		}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				DefSecurityConstants.ABM_RECCON, BaseSecurityConstants.AGREGAR);
			
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				RecConAdapter recConAdapterVO = (RecConAdapter) userSession.get(RecConAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (recConAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RecConAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RecConAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(recConAdapterVO, request);
				
	            // Tiene errores recuperables
				if (recConAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recConAdapterVO.infoString()); 
					saveDemodaErrors(request, recConAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						RecConAdapter.NAME, recConAdapterVO);
				}
				
				// llamada al servicio
				RecConVO recConVO = DefServiceLocator.getGravamenService().updateRecCon
					(userSession, recConAdapterVO.getRecCon());
				
				  // Tiene errores recuperables
				if (recConVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recConVO.infoString()); 
					saveDemodaErrors(request, recConVO);
					return forwardErrorRecoverable(mapping, request, userSession, RecConAdapter.NAME, recConAdapterVO);
				}
				// Tiene errores no recuperables
				if (recConVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + recConVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, RecConAdapter.NAME, recConAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, RecConAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RecConAdapter.NAME);
			}
		}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				DefSecurityConstants.ABM_RECCON, BaseSecurityConstants.ELIMINAR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				RecConAdapter recConAdapterVO = (RecConAdapter) userSession.get(RecConAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (recConAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RecConAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RecConAdapter.NAME); 
				}

				// llamada al servicio
				RecConVO recConVO = DefServiceLocator.getGravamenService().deleteRecCon
					(userSession, recConAdapterVO.getRecCon());
				
	            // Tiene errores recuperables
				if (recConVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recConAdapterVO.infoString());
					saveDemodaErrors(request, recConVO);				
					request.setAttribute(RecConAdapter.NAME, recConAdapterVO);
					return mapping.findForward(DefConstants.FWD_RECCON_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (recConVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + recConAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						RecConAdapter.NAME, recConAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, RecConAdapter.NAME);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RecConAdapter.NAME);
			}
		}

		public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, RecConAdapter.NAME);
		}
		

}
