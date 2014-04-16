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

import ar.gov.rosario.siat.bal.iface.model.CompensacionAdapter;
import ar.gov.rosario.siat.bal.iface.model.CompensacionVO;
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

public class AdministrarCompensacionDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarCompensacionDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_COMPENSACION, act);		
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			CompensacionAdapter compensacionAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
					stringServicio = "getCompensacionAdapterForView(userSession, commonKey)";
					compensacionAdapterVO = BalServiceLocator.getCompensacionService().getCompensacionAdapterForView
						(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_COMPENSACION_VIEW_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
					stringServicio = "getCompensacionAdapterForUpdate(userSession, commonKey)";
					compensacionAdapterVO = BalServiceLocator.getCompensacionService().getCompensacionAdapterForView
						(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_COMPENSACION_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
					stringServicio = "getCompensacionAdapterForDelete(userSession, commonKey)";
					compensacionAdapterVO = BalServiceLocator.getCompensacionService().getCompensacionAdapterForView
						(userSession, commonKey);
					compensacionAdapterVO.addMessage(BaseError.MSG_ELIMINAR, BalError.COMPENSACION_LABEL);
					actionForward = mapping.findForward(BalConstants.FWD_COMPENSACION_VIEW_ADAPTER);					
				}
				if (navModel.getAct().equals(BaseConstants.ACT_ENVIAR)) {
					stringServicio = "getCompensacionAdapterForView(userSession, commonKey)";
					compensacionAdapterVO = BalServiceLocator.getCompensacionService().getCompensacionAdapterForView(userSession, commonKey);
					compensacionAdapterVO.addMessage(BalError.COMPENSACION_PREPRARAR_PARA_FOLIO);
					actionForward = mapping.findForward(BalConstants.FWD_COMPENSACION_VIEW_ADAPTER);					
				}
				if (navModel.getAct().equals(BaseConstants.ACT_DEVOLVER)) {
					stringServicio = "getCompensacionAdapterForView(userSession, commonKey)";
					compensacionAdapterVO = BalServiceLocator.getCompensacionService().getCompensacionAdapterForView(userSession, commonKey);
					compensacionAdapterVO.addMessage(BalError.COMPENSACION_VOLVER_A_CREADO);
					actionForward = mapping.findForward(BalConstants.FWD_COMPENSACION_VIEW_ADAPTER);					
				}
				
				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (compensacionAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + compensacionAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, CompensacionAdapter.NAME, compensacionAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				compensacionAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + 
					CompensacionAdapter.NAME + ": " + compensacionAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(CompensacionAdapter.NAME, compensacionAdapterVO);
				
				// Subo el apdater al userSession
				userSession.put(CompensacionAdapter.NAME, compensacionAdapterVO);
				
				saveDemodaMessages(request, compensacionAdapterVO);			
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, CompensacionAdapter.NAME);
			}
		}
	
	public ActionForward modificarEncabezado(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardAdapter(mapping, request, funcName, 
				BalConstants.ACTION_ADMINISTRAR_ENCCOMPENSACION, BaseConstants.ACT_MODIFICAR);

	}
	
	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_COMPENSACION, 
				BaseSecurityConstants.ELIMINAR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				CompensacionAdapter compensacionAdapterVO = (CompensacionAdapter) userSession.get(CompensacionAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (compensacionAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + CompensacionAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, CompensacionAdapter.NAME); 
				}

				// llamada al servicio
				CompensacionVO compensacionVO = BalServiceLocator.getCompensacionService().deleteCompensacion
					(userSession, compensacionAdapterVO.getCompensacion());
				
	            // Tiene errores recuperables
				if (compensacionVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + compensacionAdapterVO.infoString());
					saveDemodaErrors(request, compensacionVO);				
					request.setAttribute(CompensacionAdapter.NAME, compensacionAdapterVO);
					return mapping.findForward(BalConstants.FWD_COMPENSACION_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (compensacionVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + compensacionAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, CompensacionAdapter.NAME, compensacionAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, CompensacionAdapter.NAME);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, CompensacionAdapter.NAME);
			}
		}
	
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, CompensacionAdapter.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, CompensacionAdapter.NAME);
			
	}
	
	// Metodos relacionados ComDeu
	public ActionForward eliminarComDeu(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_COMDEU);

	}
	
	public ActionForward agregarComDeu(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_COMDEU);
		
	}
	
	// Metodos Relacionados a Saldos A Favor	
	public ActionForward verSaldoAFavor(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardVerAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_SALDOAFAVOR);
	}

	public ActionForward incluirSaldoAFavor(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
	
		return baseForwardAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_SALDO_EN_COMPENSACION, BalConstants.ACT_INCLUIR_SALDO_A_FAVOR);
	}

	public ActionForward excluirSaldoAFavor(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
	
		return baseForwardAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_SALDO_EN_COMPENSACION, BalConstants.ACT_EXCLUIR_SALDO_A_FAVOR);
	}
	
	public ActionForward enviar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_COMPENSACION, BaseSecurityConstants.ENVIAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CompensacionAdapter compensacionAdapterVO = (CompensacionAdapter) userSession.get(CompensacionAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (compensacionAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CompensacionAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CompensacionAdapter.NAME); 
			}

			// llamada al servicio
			CompensacionVO compensacionVO = BalServiceLocator.getCompensacionService().enviarCompensacion(userSession, compensacionAdapterVO.getCompensacion());
			
            // Tiene errores recuperables
			if (compensacionVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + compensacionAdapterVO.infoString());
				saveDemodaErrors(request, compensacionVO);				
				request.setAttribute(CompensacionAdapter.NAME, compensacionAdapterVO);
				return mapping.findForward(BalConstants.FWD_COMPENSACION_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (compensacionVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + compensacionAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CompensacionAdapter.NAME, compensacionAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CompensacionAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CompensacionAdapter.NAME);
		}
	}
	
	public ActionForward devolver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_COMPENSACION, BaseSecurityConstants.DEVOLVER); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CompensacionAdapter compensacionAdapterVO = (CompensacionAdapter) userSession.get(CompensacionAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (compensacionAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CompensacionAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CompensacionAdapter.NAME); 
			}

			// llamada al servicio
			CompensacionVO compensacionVO = BalServiceLocator.getCompensacionService().devolverCompensacion(userSession, compensacionAdapterVO.getCompensacion());
			
            // Tiene errores recuperables
			if (compensacionVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + compensacionAdapterVO.infoString());
				saveDemodaErrors(request, compensacionVO);				
				request.setAttribute(CompensacionAdapter.NAME, compensacionAdapterVO);
				return mapping.findForward(BalConstants.FWD_COMPENSACION_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (compensacionVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + compensacionAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CompensacionAdapter.NAME, compensacionAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CompensacionAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CompensacionAdapter.NAME);
		}
	}
}
