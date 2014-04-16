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

import ar.gov.rosario.siat.bal.iface.model.RelPartidaAdapter;
import ar.gov.rosario.siat.bal.iface.model.RelPartidaVO;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class AdministrarRelPartidaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarRelPartidaDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_RELPARTIDA, act);		
			if (userSession == null) return forwardErrorSession(request);

			NavModel navModel = userSession.getNavModel();

			RelPartidaAdapter relPartidaAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
					stringServicio = "getRelPartidaAdapterForView(userSession, commonKey)";
					relPartidaAdapterVO = BalServiceLocator.getClasificacionService().getRelPartidaAdapterForView
						(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_RELPARTIDA_VIEW_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
					stringServicio = "getRelPartidaAdapterForDelete(userSession, commonKey)";
					relPartidaAdapterVO = BalServiceLocator.getClasificacionService().getRelPartidaAdapterForView
						(userSession, commonKey);
					relPartidaAdapterVO.addMessage(BaseError.MSG_ELIMINAR, BalError.RELPARTIDA_LABEL);				
					actionForward = mapping.findForward(BalConstants.FWD_RELPARTIDA_VIEW_ADAPTER);					
				}
				if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
					stringServicio = "getRelPartidaAdapterForView(userSession)";
					relPartidaAdapterVO = BalServiceLocator.getClasificacionService().getRelPartidaAdapterForView
						(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_RELPARTIDA_EDIT_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
					stringServicio = "getRelPartidaAdapterForCreate(userSession)";
					relPartidaAdapterVO = BalServiceLocator.getClasificacionService().getRelPartidaAdapterForCreate
						(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_RELPARTIDA_EDIT_ADAPTER);
				}

				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (relPartidaAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + relPartidaAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, RelPartidaAdapter.NAME, relPartidaAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				relPartidaAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + RelPartidaAdapter.NAME + ": "+ relPartidaAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(RelPartidaAdapter.NAME, relPartidaAdapterVO);
				// Subo el apdater al userSession
				userSession.put(RelPartidaAdapter.NAME, relPartidaAdapterVO);
				
				saveDemodaMessages(request, relPartidaAdapterVO);			
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RelPartidaAdapter.NAME);
			}
		}
	

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping,
				BalSecurityConstants.ABM_RELPARTIDA, BaseSecurityConstants.AGREGAR); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				RelPartidaAdapter relPartidaAdapterVO = (RelPartidaAdapter) userSession.get(RelPartidaAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (relPartidaAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RelPartidaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RelPartidaAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(relPartidaAdapterVO, request);
				
	            // Tiene errores recuperables
				if (relPartidaAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + relPartidaAdapterVO.infoString()); 
					saveDemodaErrors(request, relPartidaAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, RelPartidaAdapter.NAME, relPartidaAdapterVO);
				}
				
				// llamada al servicio
				RelPartidaVO relPartidaVO = BalServiceLocator.getClasificacionService().createRelPartida(userSession, relPartidaAdapterVO.getRelPartida());
				
	            // Tiene errores recuperables
				if (relPartidaVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + relPartidaVO.infoString()); 
					saveDemodaErrors(request, relPartidaVO);
					return forwardErrorRecoverable(mapping, request, userSession, RelPartidaAdapter.NAME, relPartidaAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (relPartidaVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + relPartidaVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, RelPartidaAdapter.NAME, relPartidaAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, RelPartidaAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RelPartidaAdapter.NAME);
			}
		}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				BalSecurityConstants.ABM_RELPARTIDA, BaseSecurityConstants.AGREGAR);
			
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				RelPartidaAdapter relPartidaAdapterVO = (RelPartidaAdapter) userSession.get(RelPartidaAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (relPartidaAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RelPartidaAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RelPartidaAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(relPartidaAdapterVO, request);
				
	            // Tiene errores recuperables
				if (relPartidaAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + relPartidaAdapterVO.infoString()); 
					saveDemodaErrors(request, relPartidaAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						RelPartidaAdapter.NAME, relPartidaAdapterVO);
				}
				
				// llamada al servicio
				RelPartidaVO relPartidaVO = BalServiceLocator.getClasificacionService().updateRelPartida(userSession, relPartidaAdapterVO.getRelPartida());
				
	            // Tiene errores recuperables
				if (relPartidaVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + relPartidaAdapterVO.infoString()); 
					saveDemodaErrors(request, relPartidaVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						RelPartidaAdapter.NAME, relPartidaAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (relPartidaVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + relPartidaAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						RelPartidaAdapter.NAME, relPartidaAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, RelPartidaAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RelPartidaAdapter.NAME);
			}
		}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				BalSecurityConstants.ABM_RELPARTIDA, BaseSecurityConstants.ELIMINAR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				RelPartidaAdapter relPartidaAdapterVO = (RelPartidaAdapter) userSession.get(RelPartidaAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (relPartidaAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RelPartidaAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RelPartidaAdapter.NAME); 
				}

				// llamada al servicio
				RelPartidaVO relPartidaVO = BalServiceLocator.getClasificacionService().deleteRelPartida(userSession, relPartidaAdapterVO.getRelPartida());
				
	            // Tiene errores recuperables
				if (relPartidaVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + relPartidaAdapterVO.infoString());
					saveDemodaErrors(request, relPartidaVO);				
					request.setAttribute(RelPartidaAdapter.NAME, relPartidaAdapterVO);
					return mapping.findForward(BalConstants.FWD_RELPARTIDA_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (relPartidaVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + relPartidaAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						RelPartidaAdapter.NAME, relPartidaAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, RelPartidaAdapter.NAME);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RelPartidaAdapter.NAME);
			}
		}

		public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, RelPartidaAdapter.NAME);
		}
}
