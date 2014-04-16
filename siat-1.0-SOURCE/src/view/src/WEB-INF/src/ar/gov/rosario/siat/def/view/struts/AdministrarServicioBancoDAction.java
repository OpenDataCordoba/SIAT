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
import ar.gov.rosario.siat.def.iface.model.ServicioBancoAdapter;
import ar.gov.rosario.siat.def.iface.model.ServicioBancoVO;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarServicioBancoDAction extends BaseDispatchAction {
	
	private Log log = LogFactory.getLog(AdministrarServicioBancoDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_SERVICIO_BANCO, act);		
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			ServicioBancoAdapter servicioBancoAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
					stringServicio = "getServicioBancoAdapterForView(userSession, commonKey)";
					servicioBancoAdapterVO = DefServiceLocator.getServicioBancoService().getServicioBancoAdapterForView
						(userSession, commonKey);
					actionForward = mapping.findForward(DefConstants.FWD_SERVICIOBANCO_VIEW_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
					stringServicio = "getServicioBancoAdapterForUpdate(userSession, commonKey)";
					servicioBancoAdapterVO = DefServiceLocator.getServicioBancoService().getServicioBancoAdapterForView
						(userSession, commonKey);
					actionForward = mapping.findForward(DefConstants.FWD_SERVICIOBANCO_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
					stringServicio = "getServicioBancoAdapterForDelete(userSession, commonKey)";
					servicioBancoAdapterVO = DefServiceLocator.getServicioBancoService().getServicioBancoAdapterForView
						(userSession, commonKey);
					servicioBancoAdapterVO.addMessage(BaseError.MSG_ELIMINAR, DefError.SERVICIOBANCO_LABEL);
					actionForward = mapping.findForward(DefConstants.FWD_SERVICIOBANCO_VIEW_ADAPTER);					
				}
				if (act.equals(BaseConstants.ACT_ACTIVAR)) {
					stringServicio = "getAtributoAdapterForView(userSession)";
					servicioBancoAdapterVO = DefServiceLocator.getServicioBancoService().getServicioBancoAdapterForView
						(userSession, commonKey);
					servicioBancoAdapterVO.addMessage(BaseError.MSG_ACTIVAR, DefError.SERVICIOBANCO_LABEL);					
					actionForward = mapping.findForward(DefConstants.FWD_SERVICIOBANCO_VIEW_ADAPTER);				
				}
				if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
					stringServicio = "getAtributoAdapterForView(userSession)";
					servicioBancoAdapterVO = DefServiceLocator.getServicioBancoService().getServicioBancoAdapterForView
						(userSession, commonKey);
					servicioBancoAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, DefError.SERVICIOBANCO_LABEL);				
					actionForward = mapping.findForward(DefConstants.FWD_SERVICIOBANCO_VIEW_ADAPTER);				
				}

				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (servicioBancoAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + servicioBancoAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, ServicioBancoAdapter.NAME, servicioBancoAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				servicioBancoAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + 
					ServicioBancoAdapter.NAME + ": " + servicioBancoAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(ServicioBancoAdapter.NAME, servicioBancoAdapterVO);
				// Subo el apdater al userSession
				userSession.put(ServicioBancoAdapter.NAME, servicioBancoAdapterVO);
				
				saveDemodaMessages(request, servicioBancoAdapterVO);			
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ServicioBancoAdapter.NAME);
			}
		}

	public ActionForward modificarEncabezado(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardAdapter(mapping, request, funcName, 
				DefConstants.ACTION_ADMINISTRAR_ENCSERVICIOBANCO, BaseConstants.ACT_MODIFICAR);

		}
	
	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_SERVICIO_BANCO, 
				BaseSecurityConstants.ELIMINAR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				ServicioBancoAdapter servicioBancoAdapterVO = (ServicioBancoAdapter) userSession.get(ServicioBancoAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (servicioBancoAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + ServicioBancoAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ServicioBancoAdapter.NAME); 
				}

				// llamada al servicio
				ServicioBancoVO servicioBancoVO = DefServiceLocator.getServicioBancoService().deleteServicioBanco
					(userSession, servicioBancoAdapterVO.getServicioBanco());
				
	            // Tiene errores recuperables
				if (servicioBancoVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + servicioBancoAdapterVO.infoString());
					saveDemodaErrors(request, servicioBancoVO);				
					request.setAttribute(ServicioBancoAdapter.NAME, servicioBancoAdapterVO);
					return mapping.findForward(DefConstants.FWD_SERVICIOBANCO_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (servicioBancoVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + servicioBancoAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, ServicioBancoAdapter.NAME, servicioBancoAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, ServicioBancoAdapter.NAME);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ServicioBancoAdapter.NAME);
			}
		}
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_SERVICIO_BANCO, 
				BaseSecurityConstants.ACTIVAR); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				ServicioBancoAdapter servicioBancoAdapterVO = (ServicioBancoAdapter) userSession.get(ServicioBancoAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (servicioBancoAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + ServicioBancoAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ServicioBancoAdapter.NAME); 
				}

				// llamada al servicio
				ServicioBancoVO servicioBancoVO = DefServiceLocator.getServicioBancoService().activarServicioBanco
					(userSession, servicioBancoAdapterVO.getServicioBanco());
				
	            // Tiene errores recuperables
				if (servicioBancoVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + servicioBancoAdapterVO.infoString());
					saveDemodaErrors(request, servicioBancoVO);				
					request.setAttribute(ServicioBancoAdapter.NAME, servicioBancoAdapterVO);
					return mapping.findForward(DefConstants.FWD_SERVICIOBANCO_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (servicioBancoVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + servicioBancoAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, ServicioBancoAdapter.NAME, servicioBancoAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, ServicioBancoAdapter.NAME);
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ServicioBancoAdapter.NAME);
			}	
		}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_SERVICIO_BANCO, 
			BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ServicioBancoAdapter servicioBancoAdapterVO = (ServicioBancoAdapter) userSession.get(ServicioBancoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (servicioBancoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ServicioBancoAdapter.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ServicioBancoAdapter.NAME); 
			}

			// llamada al servicio
			ServicioBancoVO servicioBancoVO = DefServiceLocator.getServicioBancoService().desactivarServicioBanco
				(userSession, servicioBancoAdapterVO.getServicioBanco());
			
            // Tiene errores recuperables
			if (servicioBancoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + servicioBancoAdapterVO.infoString());
				saveDemodaErrors(request, servicioBancoVO);				
				request.setAttribute(ServicioBancoAdapter.NAME, servicioBancoAdapterVO);
				return mapping.findForward(DefConstants.FWD_SERVICIOBANCO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (servicioBancoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + servicioBancoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ServicioBancoAdapter.NAME, servicioBancoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ServicioBancoAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ServicioBancoAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, ServicioBancoAdapter.NAME);
		}

		public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, ServicioBancoAdapter.NAME);
			
		}

		// Metodos relacionados SerBanRec

		public ActionForward verSerBanRec(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardVerAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_SERBANREC);

		}

		public ActionForward modificarSerBanRec(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardModificarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_SERBANREC);

		}

		public ActionForward eliminarSerBanRec(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardEliminarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_SERBANREC);

		}
		
		public ActionForward agregarSerBanRec(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardAgregarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_SERBANREC);
			
		}
		// Metodos relacionados SerBanDesGen

		public ActionForward verSerBanDesGen(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardVerAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_SERBANDESGEN);

		}

		public ActionForward modificarSerBanDesGen(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardModificarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_SERBANDESGEN);

		}

		public ActionForward eliminarSerBanDesGen(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardEliminarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_SERBANDESGEN);

		}
		
		public ActionForward agregarSerBanDesGen(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardAgregarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_SERBANDESGEN);
			
		}
		

	
}
