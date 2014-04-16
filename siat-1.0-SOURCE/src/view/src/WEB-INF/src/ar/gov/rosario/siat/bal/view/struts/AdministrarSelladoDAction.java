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

import ar.gov.rosario.siat.bal.iface.model.SelladoAdapter;
import ar.gov.rosario.siat.bal.iface.model.SelladoVO;
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

public final class AdministrarSelladoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarSelladoDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_SELLADO, act);		
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		SelladoAdapter selladoAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
				stringServicio = "getSelladoAdapterForView(userSession, commonKey)";
				selladoAdapterVO = BalServiceLocator.getDefinicionService().getSelladoAdapterForView
					(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_SELLADO_VIEW_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getSelladoAdapterForUpdate(userSession, commonKey)";
				selladoAdapterVO = BalServiceLocator.getDefinicionService().getSelladoAdapterForUpdate
					(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_SELLADO_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getSelladoAdapterForDelete(userSession, commonKey)";
				selladoAdapterVO = BalServiceLocator.getDefinicionService().getSelladoAdapterForView
					(userSession, commonKey);
				selladoAdapterVO.addMessage(BaseError.MSG_ELIMINAR, BalError.SELLADO_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_SELLADO_VIEW_ADAPTER);					
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getSelladoAdapterForView(userSession)";
				selladoAdapterVO = BalServiceLocator.getDefinicionService().getSelladoAdapterForView
					(userSession, commonKey);
				selladoAdapterVO.addMessage(BaseError.MSG_ACTIVAR, BalError.SELLADO_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_SELLADO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getSelladoAdapterForView(userSession)";
				selladoAdapterVO = BalServiceLocator.getDefinicionService().getSelladoAdapterForView
					(userSession, commonKey);
				selladoAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, BalError.SELLADO_LABEL);			
				actionForward = mapping.findForward(BalConstants.FWD_SELLADO_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (selladoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + 
					stringServicio + ": " + selladoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SelladoAdapter.NAME, selladoAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			selladoAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + 
				SelladoAdapter.NAME + ": " + selladoAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(SelladoAdapter.NAME, selladoAdapterVO);
			// Subo el apdater al userSession
			userSession.put(SelladoAdapter.NAME, selladoAdapterVO);
			
			saveDemodaMessages(request, selladoAdapterVO);			
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SelladoAdapter.NAME);
		}
	}

	public ActionForward modificarEncabezado(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardAdapter(mapping, request, funcName, 
			BalConstants.ACTION_ADMINISTRAR_ENC_SELLADO, BaseConstants.ACT_MODIFICAR);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_SELLADO, 
			BaseSecurityConstants.ELIMINAR);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			SelladoAdapter selladoAdapterVO = (SelladoAdapter) userSession.get(SelladoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (selladoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + SelladoAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SelladoAdapter.NAME); 
			}

			// llamada al servicio
			SelladoVO selladoVO = BalServiceLocator.getDefinicionService().deleteSellado
				(userSession, selladoAdapterVO.getSellado());
			
            // Tiene errores recuperables
			if (selladoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + selladoAdapterVO.infoString());
				saveDemodaErrors(request, selladoVO);				
				request.setAttribute(SelladoAdapter.NAME, selladoAdapterVO);
				return mapping.findForward(BalConstants.FWD_SELLADO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (selladoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + selladoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SelladoAdapter.NAME, selladoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, SelladoAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SelladoAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_SELLADO, 
			BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			SelladoAdapter selladoAdapterVO = (SelladoAdapter) userSession.get(SelladoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (selladoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + SelladoAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SelladoAdapter.NAME); 
			}

			// llamada al servicio
			SelladoVO selladoVO = BalServiceLocator.getDefinicionService().activarSellado
				(userSession, selladoAdapterVO.getSellado());
			
            // Tiene errores recuperables
			if (selladoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + selladoAdapterVO.infoString());
				saveDemodaErrors(request, selladoVO);				
				request.setAttribute(SelladoAdapter.NAME, selladoAdapterVO);
				return mapping.findForward(BalConstants.FWD_SELLADO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (selladoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + selladoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SelladoAdapter.NAME, selladoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, SelladoAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SelladoAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_SELLADO, 
			BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			SelladoAdapter selladoAdapterVO = (SelladoAdapter) userSession.get(SelladoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (selladoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + SelladoAdapter.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SelladoAdapter.NAME); 
			}

			// llamada al servicio
			SelladoVO selladoVO = BalServiceLocator.getDefinicionService().desactivarSellado
				(userSession, selladoAdapterVO.getSellado());
			
            // Tiene errores recuperables
			if (selladoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + selladoAdapterVO.infoString());
				saveDemodaErrors(request, selladoVO);				
				request.setAttribute(SelladoAdapter.NAME, selladoAdapterVO);
				return mapping.findForward(BalConstants.FWD_SELLADO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (selladoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + selladoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SelladoAdapter.NAME, selladoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, SelladoAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SelladoAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, SelladoAdapter.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, SelladoAdapter.NAME);
		
	}
	
	// Metodos relacionados ImpSel
	public ActionForward verImpSel(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_IMPSEL);

	}

	public ActionForward modificarImpSel(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_IMPSEL);

	}

	public ActionForward eliminarImpSel(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_IMPSEL);

	}
	
	public ActionForward agregarImpSel(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_IMPSEL);
		
	}
	
	//	 Metodos relacionados Accion Sellado
	public ActionForward verAccionSellado(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_ACCIONSELLADO);
	}
	
	public ActionForward modificarAccionSellado(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			
		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_ACCIONSELLADO);

		}

	public ActionForward eliminarAccionSellado(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_ACCIONSELLADO);

	}
		
	public ActionForward agregarAccionSellado(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_ACCIONSELLADO);
	}

	//	 Metodos relacionados ParSel
	public ActionForward verParSel(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_PARSEL);
	}
	
	public ActionForward modificarParSel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
				
			String funcName = DemodaUtil.currentMethodName();
			return forwardModificarAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_PARSEL);

			}

	public ActionForward eliminarParSel(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardEliminarAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_PARSEL);

		}
			
	public ActionForward agregarParSel(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_PARSEL);
		}

}
	
