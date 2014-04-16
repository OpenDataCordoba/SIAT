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
import ar.gov.rosario.siat.def.iface.model.RecClaDeuAdapter;
import ar.gov.rosario.siat.def.iface.model.RecClaDeuVO;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarRecClaDeuDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarRecClaDeuDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_RECATRVAL, act);		
			if (userSession == null) return forwardErrorSession(request);

			NavModel navModel = userSession.getNavModel();

			RecClaDeuAdapter recClaDeuAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
					stringServicio = "getRecClaDeuAdapterForView(userSession, commonKey)";
					recClaDeuAdapterVO = DefServiceLocator.getGravamenService().getRecClaDeuAdapterForView
						(userSession, commonKey);
					actionForward = mapping.findForward(DefConstants.FWD_RECCLADEU_VIEW_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
					stringServicio = "getRecClaDeuAdapterForUpdate(userSession, commonKey)";
					recClaDeuAdapterVO = DefServiceLocator.getGravamenService().getRecClaDeuAdapterForUpdate
						(userSession, commonKey);
					actionForward = mapping.findForward(DefConstants.FWD_RECCLADEU_EDIT_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
					stringServicio = "getRecClaDeuAdapterForDelete(userSession, commonKey)";
					recClaDeuAdapterVO = DefServiceLocator.getGravamenService().getRecClaDeuAdapterForView
						(userSession, commonKey);
					recClaDeuAdapterVO.addMessage(BaseError.MSG_ELIMINAR, DefError.RECCLADEU_LABEL);				
					actionForward = mapping.findForward(DefConstants.FWD_RECCLADEU_VIEW_ADAPTER);					
				}
				if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
					stringServicio = "getRecClaDeuAdapterForCreate(userSession)";
					recClaDeuAdapterVO = DefServiceLocator.getGravamenService().getRecClaDeuAdapterForCreate
						(userSession, commonKey);
					actionForward = mapping.findForward(DefConstants.FWD_RECCLADEU_EDIT_ADAPTER);
				}

				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (recClaDeuAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + recClaDeuAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, RecClaDeuAdapter.NAME, recClaDeuAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				recClaDeuAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + RecClaDeuAdapter.NAME + ": "+ recClaDeuAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(RecClaDeuAdapter.NAME, recClaDeuAdapterVO);
				// Subo el apdater al userSession
				userSession.put(RecClaDeuAdapter.NAME, recClaDeuAdapterVO);
				
				saveDemodaMessages(request, recClaDeuAdapterVO);			
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RecClaDeuAdapter.NAME);
			}
		}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping,
				DefSecurityConstants.ABM_RECCLADEU, BaseSecurityConstants.AGREGAR); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				RecClaDeuAdapter recClaDeuAdapterVO = (RecClaDeuAdapter) userSession.get(RecClaDeuAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (recClaDeuAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RecClaDeuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RecClaDeuAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(recClaDeuAdapterVO, request);
				
	            // Tiene errores recuperables
				if (recClaDeuAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recClaDeuAdapterVO.infoString()); 
					saveDemodaErrors(request, recClaDeuAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, RecClaDeuAdapter.NAME, recClaDeuAdapterVO);
				}
				
				// llamada al servicio
				RecClaDeuVO recClaDeuVO = DefServiceLocator.getGravamenService().createRecClaDeu(userSession, recClaDeuAdapterVO.getRecClaDeu());
				
	            // Tiene errores recuperables
				if (recClaDeuVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recClaDeuVO.infoString()); 
					saveDemodaErrors(request, recClaDeuVO);
					return forwardErrorRecoverable(mapping, request, userSession, RecClaDeuAdapter.NAME, recClaDeuAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (recClaDeuVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + recClaDeuVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, RecClaDeuAdapter.NAME, recClaDeuAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, RecClaDeuAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RecClaDeuAdapter.NAME);
			}
		}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				DefSecurityConstants.ABM_RECCLADEU, BaseSecurityConstants.AGREGAR);
			
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				RecClaDeuAdapter recClaDeuAdapterVO = (RecClaDeuAdapter) userSession.get(RecClaDeuAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (recClaDeuAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RecClaDeuAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RecClaDeuAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(recClaDeuAdapterVO, request);
				
	            // Tiene errores recuperables
				if (recClaDeuAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recClaDeuAdapterVO.infoString()); 
					saveDemodaErrors(request, recClaDeuAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						RecClaDeuAdapter.NAME, recClaDeuAdapterVO);
				}
				
				// llamada al servicio
				RecClaDeuVO recClaDeuVO = DefServiceLocator.getGravamenService().updateRecClaDeu
					(userSession, recClaDeuAdapterVO.getRecClaDeu());
				
	            // Tiene errores recuperables
				if (recClaDeuVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recClaDeuAdapterVO.infoString()); 
					saveDemodaErrors(request, recClaDeuVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						RecClaDeuAdapter.NAME, recClaDeuAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (recClaDeuVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + recClaDeuAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						RecClaDeuAdapter.NAME, recClaDeuAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, RecClaDeuAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RecClaDeuAdapter.NAME);
			}
		}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				DefSecurityConstants.ABM_RECCLADEU, BaseSecurityConstants.ELIMINAR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				RecClaDeuAdapter recClaDeuAdapterVO = (RecClaDeuAdapter) userSession.get(RecClaDeuAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (recClaDeuAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RecClaDeuAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RecClaDeuAdapter.NAME); 
				}

				// llamada al servicio
				RecClaDeuVO recClaDeuVO = DefServiceLocator.getGravamenService().deleteRecClaDeu
					(userSession, recClaDeuAdapterVO.getRecClaDeu());
				
	            // Tiene errores recuperables
				if (recClaDeuVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recClaDeuAdapterVO.infoString());
					saveDemodaErrors(request, recClaDeuVO);				
					request.setAttribute(RecClaDeuAdapter.NAME, recClaDeuAdapterVO);
					return mapping.findForward(DefConstants.FWD_RECCLADEU_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (recClaDeuVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + recClaDeuAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						RecClaDeuAdapter.NAME, recClaDeuAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, RecClaDeuAdapter.NAME);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RecClaDeuAdapter.NAME);
			}
		}

		public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, RecClaDeuAdapter.NAME);
		}

}
