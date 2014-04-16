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
import ar.gov.rosario.siat.def.iface.model.ServicioBancoAdapter;
import ar.gov.rosario.siat.def.iface.model.ServicioBancoVO;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarEncServicioBancoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEncServicioBancoDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_SERVICIO_BANCO_ENC, act);		

			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			ServicioBancoAdapter servicioBancoAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());

				if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
					stringServicio = "getServicioBancoAdapterForUpdate(userSession, commonKey)";
					servicioBancoAdapterVO = DefServiceLocator.getServicioBancoService().getServicioBancoAdapterForUpdate(userSession, commonKey);
					actionForward = mapping.findForward(DefConstants.FWD_SERVICIOBANCO_ENC_EDIT_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
					stringServicio = "getServicioBancoAdapterForCreate(userSession)";
					servicioBancoAdapterVO = DefServiceLocator.getServicioBancoService().getServicioBancoAdapterForCreate(userSession);
					actionForward = mapping.findForward(DefConstants.FWD_SERVICIOBANCO_ENC_EDIT_ADAPTER);
				}
				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (servicioBancoAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + servicioBancoAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, ServicioBancoAdapter.ENC_NAME, servicioBancoAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				servicioBancoAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + ServicioBancoAdapter.ENC_NAME + ": "+ servicioBancoAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(ServicioBancoAdapter.ENC_NAME, servicioBancoAdapterVO);
				// Subo el apdater al userSession
				userSession.put(ServicioBancoAdapter.ENC_NAME, servicioBancoAdapterVO);

				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ServicioBancoAdapter.ENC_NAME);
			}
		}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping, 
				DefSecurityConstants.ABM_SERVICIO_BANCO_ENC, BaseSecurityConstants.AGREGAR);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				ServicioBancoAdapter servicioBancoAdapterVO = (ServicioBancoAdapter) userSession.get(ServicioBancoAdapter.ENC_NAME);
				
				// Si es nulo no se puede continuar
				if (servicioBancoAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + ServicioBancoAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ServicioBancoAdapter.ENC_NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(servicioBancoAdapterVO, request);
				
	            // Tiene errores recuperables
				if (servicioBancoAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + servicioBancoAdapterVO.infoString()); 
					saveDemodaErrors(request, servicioBancoAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, ServicioBancoAdapter.ENC_NAME, servicioBancoAdapterVO);
				}
				
				// llamada al servicio
				ServicioBancoVO servicioBancoVO = DefServiceLocator.getServicioBancoService().createServicioBanco(userSession, servicioBancoAdapterVO.getServicioBanco());
				
	            // Tiene errores recuperables
				if (servicioBancoVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + servicioBancoVO.infoString()); 
					saveDemodaErrors(request, servicioBancoVO);
					return forwardErrorRecoverable(mapping, request, userSession, ServicioBancoAdapter.ENC_NAME, servicioBancoAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (servicioBancoVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + servicioBancoVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, ServicioBancoAdapter.ENC_NAME, servicioBancoAdapterVO);
				}

				// Si tiene permiso lo dirijo al adapter de modificacion, 
				// sino vuelve al searchPage
				if (hasAccess(userSession, DefSecurityConstants.ABM_SERVICIO_BANCO, 
					BaseSecurityConstants.MODIFICAR)) {
					
					// seteo el id para que lo use el siguiente action 
					userSession.getNavModel().setSelectedId(servicioBancoVO.getId().toString());

					// lo dirijo al adapter de modificacion
					return forwardConfirmarOk(mapping, request, funcName, ServicioBancoAdapter.ENC_NAME, 
						DefConstants.PATH_ADMINISTRAR_SERVICIOBANCO, BaseConstants.METHOD_INICIALIZAR, 
						BaseConstants.ACT_MODIFICAR);
				} else {
					
					// lo dirijo al searchPage				
					return forwardConfirmarOk(mapping, request, funcName, ServicioBancoAdapter.ENC_NAME);
					
				}
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ServicioBancoAdapter.ENC_NAME);
			}
		}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				DefSecurityConstants.ABM_SERVICIO_BANCO_ENC, BaseSecurityConstants.MODIFICAR);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				ServicioBancoAdapter servicioBancoAdapterVO = (ServicioBancoAdapter) userSession.get(ServicioBancoAdapter.ENC_NAME);
				
				// Si es nulo no se puede continuar
				if (servicioBancoAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + ServicioBancoAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ServicioBancoAdapter.ENC_NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(servicioBancoAdapterVO, request);
				
	            // Tiene errores recuperables
				if (servicioBancoAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + servicioBancoAdapterVO.infoString()); 
					saveDemodaErrors(request, servicioBancoAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, ServicioBancoAdapter.ENC_NAME, servicioBancoAdapterVO);
				}
				
				// llamada al servicio
				ServicioBancoVO servicioBancoVO = DefServiceLocator.getServicioBancoService().updateServicioBanco(userSession, servicioBancoAdapterVO.getServicioBanco());
				
	            // Tiene errores recuperables
				if (servicioBancoVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + servicioBancoAdapterVO.infoString()); 
					saveDemodaErrors(request, servicioBancoVO);
					return forwardErrorRecoverable(mapping, request, userSession, ServicioBancoAdapter.ENC_NAME, servicioBancoAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (servicioBancoVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + servicioBancoAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, ServicioBancoAdapter.ENC_NAME, servicioBancoAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, ServicioBancoAdapter.ENC_NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ServicioBancoAdapter.ENC_NAME);
			}
		}
	
		public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, ServicioBancoAdapter.ENC_NAME);
		}

		public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, ServicioBancoAdapter.ENC_NAME);
			
		}

	
}
