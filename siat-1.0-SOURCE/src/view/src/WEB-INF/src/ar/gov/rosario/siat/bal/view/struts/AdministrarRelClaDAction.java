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

import ar.gov.rosario.siat.bal.iface.model.RelClaAdapter;
import ar.gov.rosario.siat.bal.iface.model.RelClaVO;
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

public class AdministrarRelClaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarRelClaDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_RELCLA, act);		
			if (userSession == null) return forwardErrorSession(request);

			NavModel navModel = userSession.getNavModel();

			RelClaAdapter relClaAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
					stringServicio = "getRelClaAdapterForView(userSession, commonKey)";
					relClaAdapterVO = BalServiceLocator.getClasificacionService().getRelClaAdapterForView
						(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_RELCLA_VIEW_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
					stringServicio = "getRelClaAdapterForDelete(userSession, commonKey)";
					relClaAdapterVO = BalServiceLocator.getClasificacionService().getRelClaAdapterForView
						(userSession, commonKey);
					relClaAdapterVO.addMessage(BaseError.MSG_ELIMINAR, BalError.RELCLA_LABEL);				
					actionForward = mapping.findForward(BalConstants.FWD_RELCLA_VIEW_ADAPTER);					
				}
				if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
					stringServicio = "getRelClaAdapterForView(userSession)";
					relClaAdapterVO = BalServiceLocator.getClasificacionService().getRelClaAdapterForView
						(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_RELCLA_EDIT_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
					stringServicio = "getRelClaAdapterForCreate(userSession)";
					relClaAdapterVO = BalServiceLocator.getClasificacionService().getRelClaAdapterForCreate
						(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_RELCLA_EDIT_ADAPTER);
				}

				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (relClaAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + relClaAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, RelClaAdapter.NAME, relClaAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				relClaAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + RelClaAdapter.NAME + ": "+ relClaAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(RelClaAdapter.NAME, relClaAdapterVO);
				// Subo el apdater al userSession
				userSession.put(RelClaAdapter.NAME, relClaAdapterVO);
				
				saveDemodaMessages(request, relClaAdapterVO);			
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RelClaAdapter.NAME);
			}
		}
	

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping,
				BalSecurityConstants.ABM_RELCLA, BaseSecurityConstants.AGREGAR); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				RelClaAdapter relClaAdapterVO = (RelClaAdapter) userSession.get(RelClaAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (relClaAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RelClaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RelClaAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(relClaAdapterVO, request);
				
	            // Tiene errores recuperables
				if (relClaAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + relClaAdapterVO.infoString()); 
					saveDemodaErrors(request, relClaAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, RelClaAdapter.NAME, relClaAdapterVO);
				}
				
				// llamada al servicio
				RelClaVO relClaVO = BalServiceLocator.getClasificacionService().createRelCla(userSession, relClaAdapterVO.getRelCla());
				
	            // Tiene errores recuperables
				if (relClaVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + relClaVO.infoString()); 
					saveDemodaErrors(request, relClaVO);
					return forwardErrorRecoverable(mapping, request, userSession, RelClaAdapter.NAME, relClaAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (relClaVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + relClaVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, RelClaAdapter.NAME, relClaAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, RelClaAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RelClaAdapter.NAME);
			}
		}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				BalSecurityConstants.ABM_RELCLA, BaseSecurityConstants.AGREGAR);
			
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				RelClaAdapter relClaAdapterVO = (RelClaAdapter) userSession.get(RelClaAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (relClaAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RelClaAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RelClaAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(relClaAdapterVO, request);
				
	            // Tiene errores recuperables
				if (relClaAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + relClaAdapterVO.infoString()); 
					saveDemodaErrors(request, relClaAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						RelClaAdapter.NAME, relClaAdapterVO);
				}
				
				// llamada al servicio
				RelClaVO relClaVO = BalServiceLocator.getClasificacionService().updateRelCla(userSession, relClaAdapterVO.getRelCla());
				
	            // Tiene errores recuperables
				if (relClaVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + relClaAdapterVO.infoString()); 
					saveDemodaErrors(request, relClaVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						RelClaAdapter.NAME, relClaAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (relClaVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + relClaAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						RelClaAdapter.NAME, relClaAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, RelClaAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RelClaAdapter.NAME);
			}
		}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				BalSecurityConstants.ABM_RELCLA, BaseSecurityConstants.ELIMINAR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				RelClaAdapter relClaAdapterVO = (RelClaAdapter) userSession.get(RelClaAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (relClaAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RelClaAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RelClaAdapter.NAME); 
				}

				// llamada al servicio
				RelClaVO relClaVO = BalServiceLocator.getClasificacionService().deleteRelCla(userSession, relClaAdapterVO.getRelCla());
				
	            // Tiene errores recuperables
				if (relClaVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + relClaAdapterVO.infoString());
					saveDemodaErrors(request, relClaVO);				
					request.setAttribute(RelClaAdapter.NAME, relClaAdapterVO);
					return mapping.findForward(BalConstants.FWD_RELCLA_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (relClaVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + relClaAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						RelClaAdapter.NAME, relClaAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, RelClaAdapter.NAME);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RelClaAdapter.NAME);
			}
		}

		public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, RelClaAdapter.NAME);
		}

		public ActionForward paramClasificador(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {
				
				String funcName = DemodaUtil.currentMethodName();
				if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
				
				UserSession userSession = getCurrentUserSession(request, mapping); 
				if (userSession==null) return forwardErrorSession(request);
				
				try {
					// Bajo el adapter del userSession
					RelClaAdapter relClaAdapterVO = (RelClaAdapter) userSession.get(RelClaAdapter.NAME);
			
					// Si es nulo no se puede continuar
					if (relClaAdapterVO == null) {
						log.error("error en: "  + funcName + ": " + RelClaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
						return forwardErrorSessionNullObject(mapping, request, funcName, RelClaAdapter.NAME); 
					}

					// Recuperamos datos del form en el vo
					DemodaUtil.populateVO(relClaAdapterVO, request);
					
		            // Tiene errores recuperables
					if (relClaAdapterVO.hasErrorRecoverable()) {
						log.error("recoverable error en: "  + funcName + ": " + relClaAdapterVO.infoString()); 
						saveDemodaErrors(request, relClaAdapterVO);
						return forwardErrorRecoverable(mapping, request, userSession, RelClaAdapter.NAME, relClaAdapterVO);
					}
					
					// Llamada al servicio
					relClaAdapterVO = BalServiceLocator.getClasificacionService().getRelClaAdapterParamClasificador(userSession, relClaAdapterVO);
					
		            // Tiene errores recuperables
					if (relClaAdapterVO.hasErrorRecoverable()) {
						log.error("recoverable error en: "  + funcName + ": " + relClaAdapterVO.infoString()); 
						saveDemodaErrors(request, relClaAdapterVO);
						return forwardErrorRecoverable(mapping, request, userSession, RelClaAdapter.NAME, relClaAdapterVO);
					}
					
					// Tiene errores no recuperables
					if (relClaAdapterVO.hasErrorNonRecoverable()) {
						log.error("error en: "  + funcName + ": " + relClaAdapterVO.errorString()); 
						return forwardErrorNonRecoverable(mapping, request, funcName, RelClaAdapter.NAME, relClaAdapterVO);
					}
					
					// Envio el VO al request
					request.setAttribute(RelClaAdapter.NAME, relClaAdapterVO);
					// Subo el adapter al userSession
					userSession.put(RelClaAdapter.NAME, relClaAdapterVO);
					
					return mapping.findForward(BalConstants.FWD_RELCLA_EDIT_ADAPTER);
				
				} catch (Exception exception) {
					return baseException(mapping, request, funcName, exception, RelClaAdapter.NAME);
				}
		}
}
