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
import ar.gov.rosario.siat.def.iface.model.DomAtrAdapter;
import ar.gov.rosario.siat.def.iface.model.DomAtrVO;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarDomAtrDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarDomAtrDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_DOMINIO_ATRIBUTO, act);		
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		DomAtrAdapter domAtrAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
				stringServicio = "getDomAtrAdapterForView(userSession, commonKey)";
				domAtrAdapterVO = DefServiceLocator.getAtributoService().getDomAtrAdapterForView
					(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_DOMATR_VIEW_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getDomAtrAdapterForUpdate(userSession, commonKey)";
				domAtrAdapterVO = DefServiceLocator.getAtributoService().getDomAtrAdapterForUpdate
					(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_DOMATR_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getDomAtrAdapterForDelete(userSession, commonKey)";
				domAtrAdapterVO = DefServiceLocator.getAtributoService().getDomAtrAdapterForView
					(userSession, commonKey);
				domAtrAdapterVO.addMessage(BaseError.MSG_ELIMINAR, DefError.DOMATR_LABEL);
				actionForward = mapping.findForward(DefConstants.FWD_DOMATR_VIEW_ADAPTER);					
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getAtributoAdapterForView(userSession)";
				domAtrAdapterVO = DefServiceLocator.getAtributoService().getDomAtrAdapterForView
					(userSession, commonKey);
				domAtrAdapterVO.addMessage(BaseError.MSG_ACTIVAR, DefError.DOMATR_LABEL);					
				actionForward = mapping.findForward(DefConstants.FWD_DOMATR_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getAtributoAdapterForView(userSession)";
				domAtrAdapterVO = DefServiceLocator.getAtributoService().getDomAtrAdapterForView
					(userSession, commonKey);
				domAtrAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, DefError.DOMATR_LABEL);				
				actionForward = mapping.findForward(DefConstants.FWD_DOMATR_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (domAtrAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + 
					stringServicio + ": " + domAtrAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DomAtrAdapter.NAME, domAtrAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			domAtrAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + 
				DomAtrAdapter.NAME + ": " + domAtrAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(DomAtrAdapter.NAME, domAtrAdapterVO);
			// Subo el apdater al userSession
			userSession.put(DomAtrAdapter.NAME, domAtrAdapterVO);
			
			saveDemodaMessages(request, domAtrAdapterVO);			
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DomAtrAdapter.NAME);
		}
	}

	public ActionForward modificarEncabezado(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardAdapter(mapping, request, funcName, 
			DefConstants.ACTION_ADMINISTRAR_ENCDOMATR, BaseConstants.ACT_MODIFICAR);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_DOMINIO_ATRIBUTO, 
			BaseSecurityConstants.ELIMINAR);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DomAtrAdapter domAtrAdapterVO = (DomAtrAdapter) userSession.get(DomAtrAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (domAtrAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DomAtrAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DomAtrAdapter.NAME); 
			}

			// llamada al servicio
			DomAtrVO domAtrVO = DefServiceLocator.getAtributoService().deleteDomAtr
				(userSession, domAtrAdapterVO.getDomAtr());
			
            // Tiene errores recuperables
			if (domAtrVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + domAtrAdapterVO.infoString());
				saveDemodaErrors(request, domAtrVO);				
				request.setAttribute(DomAtrAdapter.NAME, domAtrAdapterVO);
				return mapping.findForward(DefConstants.FWD_DOMATR_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (domAtrVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + domAtrAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DomAtrAdapter.NAME, domAtrAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DomAtrAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DomAtrAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_DOMINIO_ATRIBUTO, 
			BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DomAtrAdapter domAtrAdapterVO = (DomAtrAdapter) userSession.get(DomAtrAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (domAtrAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DomAtrAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DomAtrAdapter.NAME); 
			}

			// llamada al servicio
			DomAtrVO domAtrVO = DefServiceLocator.getAtributoService().activarDomAtr
				(userSession, domAtrAdapterVO.getDomAtr());
			
            // Tiene errores recuperables
			if (domAtrVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + domAtrAdapterVO.infoString());
				saveDemodaErrors(request, domAtrVO);				
				request.setAttribute(DomAtrAdapter.NAME, domAtrAdapterVO);
				return mapping.findForward(DefConstants.FWD_DOMATR_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (domAtrVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + domAtrAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DomAtrAdapter.NAME, domAtrAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DomAtrAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DomAtrAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_DOMINIO_ATRIBUTO, 
			BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DomAtrAdapter domAtrAdapterVO = (DomAtrAdapter) userSession.get(DomAtrAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (domAtrAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DomAtrAdapter.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DomAtrAdapter.NAME); 
			}

			// llamada al servicio
			DomAtrVO domAtrVO = DefServiceLocator.getAtributoService().desactivarDomAtr
				(userSession, domAtrAdapterVO.getDomAtr());
			
            // Tiene errores recuperables
			if (domAtrVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + domAtrAdapterVO.infoString());
				saveDemodaErrors(request, domAtrVO);				
				request.setAttribute(DomAtrAdapter.NAME, domAtrAdapterVO);
				return mapping.findForward(DefConstants.FWD_DOMATR_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (domAtrVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + domAtrAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DomAtrAdapter.NAME, domAtrAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DomAtrAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DomAtrAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, DomAtrAdapter.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, DomAtrAdapter.NAME);
		
	}
	
	// Metodos relacionados Dom Atr Val

	public ActionForward verDomAtrVal(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_DOMATRVAL);

	}

	public ActionForward modificarDomAtrVal(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_DOMATRVAL);

	}

	public ActionForward eliminarDomAtrVal(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_DOMATRVAL);

	}
	
	public ActionForward agregarDomAtrVal(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_DOMATRVAL);
		
	}
	
}
	
