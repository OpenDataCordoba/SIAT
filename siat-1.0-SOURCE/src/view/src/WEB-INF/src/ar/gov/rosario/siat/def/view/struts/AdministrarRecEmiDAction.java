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
import ar.gov.rosario.siat.def.iface.model.RecEmiAdapter;
import ar.gov.rosario.siat.def.iface.model.RecEmiVO;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarRecEmiDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarRecEmiDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_RECEMI, act);		
			if (userSession == null) return forwardErrorSession(request);

			NavModel navModel = userSession.getNavModel();

			RecEmiAdapter recEmiAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
					stringServicio = "getRecEmiAdapterForView(userSession, commonKey)";
					recEmiAdapterVO = DefServiceLocator.getGravamenService().getRecEmiAdapterForView
						(userSession, commonKey);
					actionForward = mapping.findForward(DefConstants.FWD_RECEMI_VIEW_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
					stringServicio = "getRecEmiAdapterForUpdate(userSession, commonKey)";
					recEmiAdapterVO = DefServiceLocator.getGravamenService().getRecEmiAdapterForUpdate
						(userSession, commonKey);
					actionForward = mapping.findForward(DefConstants.FWD_RECEMI_EDIT_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
					stringServicio = "getRecEmiAdapterForDelete(userSession, commonKey)";
					recEmiAdapterVO = DefServiceLocator.getGravamenService().getRecEmiAdapterForView
						(userSession, commonKey);
					recEmiAdapterVO.addMessage(BaseError.MSG_ELIMINAR, DefError.RECEMI_LABEL);				
					actionForward = mapping.findForward(DefConstants.FWD_RECEMI_VIEW_ADAPTER);					
				}
				if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
					stringServicio = "getRecEmiAdapterForCreate(userSession)";
					recEmiAdapterVO = DefServiceLocator.getGravamenService().getRecEmiAdapterForCreate
						(userSession, commonKey);
					actionForward = mapping.findForward(DefConstants.FWD_RECEMI_EDIT_ADAPTER);
				}

				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (recEmiAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + recEmiAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, RecEmiAdapter.NAME, recEmiAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				recEmiAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + RecEmiAdapter.NAME + ": "+ recEmiAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(RecEmiAdapter.NAME, recEmiAdapterVO);
				// Subo el apdater al userSession
				userSession.put(RecEmiAdapter.NAME, recEmiAdapterVO);
				
				saveDemodaMessages(request, recEmiAdapterVO);			
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RecEmiAdapter.NAME);
			}
		}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping,
				DefSecurityConstants.ABM_RECEMI, BaseSecurityConstants.AGREGAR); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				RecEmiAdapter recEmiAdapterVO = (RecEmiAdapter) userSession.get(RecEmiAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (recEmiAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RecEmiAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RecEmiAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(recEmiAdapterVO, request);
				
	            // Tiene errores recuperables
				if (recEmiAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recEmiAdapterVO.infoString()); 
					saveDemodaErrors(request, recEmiAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, RecEmiAdapter.NAME, recEmiAdapterVO);
				}
				
				// llamada al servicio
				RecEmiVO recEmiVO = DefServiceLocator.getGravamenService().createRecEmi(userSession, recEmiAdapterVO.getRecEmi());
				
	            // Tiene errores recuperables
				if (recEmiVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recEmiVO.infoString()); 
					saveDemodaErrors(request, recEmiVO);
					return forwardErrorRecoverable(mapping, request, userSession, RecEmiAdapter.NAME, recEmiAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (recEmiVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + recEmiVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, RecEmiAdapter.NAME, recEmiAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, RecEmiAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RecEmiAdapter.NAME);
			}
		}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				DefSecurityConstants.ABM_RECEMI, BaseSecurityConstants.AGREGAR);
			
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				RecEmiAdapter recEmiAdapterVO = (RecEmiAdapter) userSession.get(RecEmiAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (recEmiAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RecEmiAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RecEmiAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(recEmiAdapterVO, request);
				
	            // Tiene errores recuperables
				if (recEmiAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recEmiAdapterVO.infoString()); 
					saveDemodaErrors(request, recEmiAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						RecEmiAdapter.NAME, recEmiAdapterVO);
				}
				
				// llamada al servicio
				RecEmiVO recEmiVO = DefServiceLocator.getGravamenService().updateRecEmi
					(userSession, recEmiAdapterVO.getRecEmi());
				
	            // Tiene errores recuperables
				if (recEmiVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recEmiAdapterVO.infoString()); 
					saveDemodaErrors(request, recEmiVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						RecEmiAdapter.NAME, recEmiAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (recEmiVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + recEmiAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						RecEmiAdapter.NAME, recEmiAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, RecEmiAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RecEmiAdapter.NAME);
			}
		}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				DefSecurityConstants.ABM_RECEMI, BaseSecurityConstants.ELIMINAR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				RecEmiAdapter recEmiAdapterVO = (RecEmiAdapter) userSession.get(RecEmiAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (recEmiAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RecEmiAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RecEmiAdapter.NAME); 
				}

				// llamada al servicio
				RecEmiVO recEmiVO = DefServiceLocator.getGravamenService().deleteRecEmi
					(userSession, recEmiAdapterVO.getRecEmi());
				
	            // Tiene errores recuperables
				if (recEmiVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recEmiAdapterVO.infoString());
					saveDemodaErrors(request, recEmiVO);				
					request.setAttribute(RecEmiAdapter.NAME, recEmiAdapterVO);
					return mapping.findForward(DefConstants.FWD_RECEMI_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (recEmiVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + recEmiAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						RecEmiAdapter.NAME, recEmiAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, RecEmiAdapter.NAME);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RecEmiAdapter.NAME);
			}
		}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, RecEmiAdapter.NAME);
		}

}
